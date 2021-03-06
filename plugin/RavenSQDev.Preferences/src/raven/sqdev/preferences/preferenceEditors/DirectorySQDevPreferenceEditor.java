package raven.sqdev.preferences.preferenceEditors;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import raven.sqdev.preferences.pages.ISQDevPreferencePage;
import raven.sqdev.preferences.util.EStatus;
import raven.sqdev.preferences.util.SQDevChangeEvent;
import raven.sqdev.util.FileSystemUtil;

/**
 * A preference editor letting the user select a directory and that can be set
 * to check for ceratin files/folders that have to present in the specified
 * directory in order for this editor to get a valid state.
 * 
 * @author Raven
 * 
 */
public class DirectorySQDevPreferenceEditor extends AbstractSQDevPreferenceEditor
		implements VerifyListener {
	
	/**
	 * The TextField used to show the current selected path to the user
	 */
	protected Text pathText;
	
	/**
	 * The initial text of {@linkplain #pathText}.<br>
	 * Initial value is empty.
	 */
	private String initialText;
	
	/**
	 * The button to open the <code>FileDialog</code>
	 */
	protected Button browseButton;
	
	/**
	 * The text used on the created button. Default value is
	 * "<code>Browse...</code>"
	 */
	private String buttonText = "Browse...";
	
	/**
	 * A list of files that have to exist in the selected directory
	 */
	protected ArrayList<String> filesToMatch;
	
	/**
	 * A list of folders that have to be present within the given folder
	 */
	protected ArrayList<String> foldersToMatch;
	
	
	/**
	 * Creates a new <code>SQDevPreferenceEditor</code><br>
	 * <b>This constructor can only be used if the given container is an
	 * instance of <code>ISQDevPreferencePage</code> or one of it's parents
	 * is. </b>
	 * 
	 * @param preferenceKey
	 *            The key of the preference to work on
	 * @param labelText
	 *            The text of the label
	 * @param tooltip
	 *            The tooltip that will be displayed on the editor's preference
	 *            value-field
	 * @param container
	 *            The container the GUI elements should be placed in
	 */
	public DirectorySQDevPreferenceEditor(String preferenceKey, String labelText, String tooltip,
			Composite container) {
		super(preferenceKey, labelText, tooltip, container);
		
		filesToMatch = new ArrayList<String>();
		foldersToMatch = new ArrayList<String>();
	}
	
	/**
	 * Creates a new <code>SQDevPreferenceEditor</code><br>
	 * <b>This constructor can only be used if the given container is an
	 * instance of <code>ISQDevPreferencePage</code> or one of it's parents
	 * is. </b>
	 * 
	 * @param preferenceKey
	 *            The key of the preference to work on
	 * @param labelText
	 *            The text of the label
	 * @param container
	 *            The container the GUI elements should be placed in
	 */
	public DirectorySQDevPreferenceEditor(String preferenceKey, String labelText,
			Composite container) {
		super(preferenceKey, labelText, "", container);
		
		filesToMatch = new ArrayList<String>();
		foldersToMatch = new ArrayList<String>();
	}
	
	/**
	 * Creates a new <code>SQDevPreferenceEditor</code>
	 * 
	 * @param preferenceKey
	 *            The key of the preference to work on
	 * @param labelText
	 *            The text of the label
	 * @param tooltip
	 *            The tooltip that will be displayed on the editor's preference
	 *            value-field
	 * @param container
	 *            The container the GUI elements should be placed in
	 * @param page
	 *            The <code>ISQDevPreferencePage</code> this editor is apllied
	 *            to
	 */
	public DirectorySQDevPreferenceEditor(String preferenceKey, String labelText,
			Composite container, String tooltip, ISQDevPreferencePage page) {
		super(preferenceKey, labelText, tooltip, container, page);
		
		filesToMatch = new ArrayList<String>();
		foldersToMatch = new ArrayList<String>();
	}
	
	/**
	 * Creates a new <code>SQDevPreferenceEditor</code>
	 * 
	 * @param preferenceKey
	 *            The key of the preference to work on
	 * @param labelText
	 *            The text of the label
	 * @param container
	 *            The container the GUI elements should be placed in
	 * @param page
	 *            The <code>ISQDevPreferencePage</code> this editor is apllied
	 *            to
	 */
	public DirectorySQDevPreferenceEditor(String preferenceKey, String labelText,
			Composite container, ISQDevPreferencePage page) {
		super(preferenceKey, labelText, "", container, page);
		
		filesToMatch = new ArrayList<String>();
		foldersToMatch = new ArrayList<String>();
	}
	
	@Override
	public boolean doSave() {
		if (super.doSave()) {
			getPreferenceStore().setValue(getPreferenceKey(), pathText.getText());
			
			// update save status
			updateSaveStatus();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void doLoad() {
		// get the stored path
		load(getPreferenceStore().getString(getPreferenceKey()));
	}
	
	/**
	 * Loads the given text in the UI.<br>
	 * If you want to load the value from the preference use
	 * {@linkplain #doLoad()}
	 * 
	 * @param text
	 *            The text to display<br>
	 */
	private void load(String text) {
		if (pathText == null) {
			setInitialText(text);
		} else {
			if (pathText.isDisposed()) {
				return;
			}
			
			pathText.setText(text);
			// evaluate the new input
			evaluateInput();
			updateSaveStatus();
		}
		
		// notify about change
		changed(new SQDevChangeEvent(SQDevChangeEvent.SQDEV_VALUE_LOADED));
	}
	
	@Override
	public void doLoadDefault() {
		load(getPreferenceStore().getDefaultString(getPreferenceKey()));
	}
	
	@Override
	public void evaluateInput() {
		evaluateInput(pathText.getText());
	}
	
	public void evaluateInput(String input) {
		File inputFile = new File(input);
		EStatus status = EStatus.OK;
		
		if (input.isEmpty()) {
			status = EStatus.ERROR;
			status.setHint("No path specified!");
			setStatus(status);
			return;
		}
		
		// check if the directory exists
		if (!inputFile.exists()) {
			status = EStatus.ERROR;
			status.setHint("The given directory does not exist!");
			setStatus(status);
			return;
		}
		
		// check if given file is a directory
		if (!inputFile.isDirectory()) {
			status = EStatus.ERROR;
			status.setHint("The given path is no directory!");
			setStatus(status);
			return;
		}
		
		// check for the files that have to be present
		for (String currentFile : filesToMatch) {
			if (!new File(input + File.separator + currentFile).exists()) {
				status = EStatus.ERROR;
				status.setHint(
						"Can't find the file \"" + currentFile + "\" in the specified directory!");
				setStatus(status);
				return;
			}
		}
		
		// check for the directories that have to be present
		for (String currentFolder : foldersToMatch) {
			if (!FileSystemUtil.containsFolder(inputFile, currentFolder)) {
				status = EStatus.ERROR;
				status.setHint("Can't find the directory \"" + currentFolder
						+ "\" in the specified directory!");
				setStatus(status);
				return;
			}
		}
		
		setStatus(status);
	}
	
	@Override
	public int getComponentCount() {
		return 3;
	}
	
	@Override
	public void createComponents(Composite container) {
		label = new Label(container, SWT.BOLD);
		setLabelText(getLabelText());
		label.setToolTipText(getTooltip());
		
		// check that initialText is not null
		if (getInitialText() == null) {
			setInitialText("");
		}
		
		pathText = new Text(container, SWT.SINGLE | SWT.BORDER);
		pathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pathText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		pathText.setToolTipText(getTooltip());
		pathText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// validate input every time it changes
				evaluateInput();
			}
		});
		pathText.addVerifyListener(this);
		load(initialText);
		
		browseButton = new Button(container, SWT.PUSH);
		browseButton.setText(getButtonText());
		browseButton.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				DirectoryDialog chooser = new DirectoryDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell());
				
				// start chooser on the typed in path if it exists
				if (new File(pathText.getText()).exists()) {
					chooser.setFilterPath(pathText.getText());
				}
				
				String path = chooser.open();
				
				if (path != null) {
					// display selected path
					load(path);
				}
			}
		});
	}
	
	public String getButtonText() {
		return buttonText;
	}
	
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
		
		if (browseButton != null) {
			browseButton.setText(buttonText);
			
			browseButton.pack();
		}
	}
	
	public String getInitialText() {
		return initialText;
	}
	
	public void setInitialText(String initialText) {
		this.initialText = initialText;
	}
	
	@Override
	public boolean needsSave() {
		return willNeedSave(pathText.getText());
	}
	
	@Override
	public boolean willNeedSave(String content) {
		if (content.equals(getPreferenceStore().getString(getPreferenceKey()))) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Indicates graphically whether the editor has a value that differs from
	 * the value of the preference this editor is working on
	 */
	public void updateSaveStatus() {
		updateSaveStatus(pathText.getText());
	}
	
	@Override
	public void verifyText(VerifyEvent e) {
		// notify about change
		changed(new SQDevChangeEvent(SQDevChangeEvent.SQDEV_VALUE_ABOUT_TO_CHANGE));
	}
	
	@Override
	public void setStatus(EStatus status) {
		super.setStatus(status);
		
		if (pathText == null) {
			return;
		}
		
		// indicate an error via red backgroundColor
		if (status == EStatus.ERROR) {
			pathText.setBackground(new Color(Display.getCurrent(), 255, 153, 153));
		} else {
			pathText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		}
	}
	
	/**
	 * Adds a file that has to present in the directory specified by this editor
	 * in order to get the valid state
	 * 
	 * @param fileName
	 *            The name of the file to match
	 */
	public void addFileToMatch(String fileName) {
		filesToMatch.add(fileName);
	}
	
	/**
	 * Adds a folder that has to be contained in the specified directory in
	 * order to get the valid state for this editor
	 * 
	 * @param folderName
	 */
	public void addFolderToMatch(String folderName) {
		foldersToMatch.add(folderName);
	}
	
}
