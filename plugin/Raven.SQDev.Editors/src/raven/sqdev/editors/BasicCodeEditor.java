package raven.sqdev.editors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.DefaultCharacterPairMatcher;
import org.eclipse.jface.text.source.ICharacterPairMatcher;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

import raven.sqdev.constants.SQDevPreferenceConstants;
import raven.sqdev.exceptions.SQDevEditorException;
import raven.sqdev.interfaces.IMacroSupport;
import raven.sqdev.interfaces.IManager;
import raven.sqdev.interfaces.IMarkerSupport;
import raven.sqdev.interfaces.IParseResult;
import raven.sqdev.misc.CharacterPair;
import raven.sqdev.misc.MultiPreferenceStore;
import raven.sqdev.misc.SQDevInfobox;
import raven.sqdev.misc.SQDevPreferenceUtil;
import raven.sqdev.parser.misc.ParseUtil;
import raven.sqdev.parser.preprocessor.PreprocessorParseResult;

/***
 * A default implementation of a code editor. This contains the autoCompletion
 * for some basic <code>CharacterPairs</code>. If you want to change those you
 * have to override addCharacterPairHandler().<br>
 * Also this editor provides an <code>EditorKeyEventQueue</code> that can be
 * used.<br>
 * Furthermore it adds an CharacterPairMatcher that will highlight corresponding
 * pairs when selected. It also installs the PreferenceStore of
 * <code>raven.sqdev.preferences</code>.
 * 
 * @author Raven
 * 
 * @see {@linkplain CharacterPairHandler}
 * @see {@linkplain CharacterPair}
 * @see {@linkplain EditorKeyEventQueue}
 * @see {@linkplain EditorKeyEventManager}
 * 
 */
public class BasicCodeEditor extends TextEditor implements IMarkerSupport {

	/**
	 * The color manager
	 */
	protected ColorManager colorManager;

	/**
	 * The queue for the keyEvents
	 */
	protected EditorKeyEventQueue editorKeyEventQueue;

	/**
	 * The source viewer configuration for this editor
	 */
	protected BasicSourceViewerConfiguration configuration;

	/**
	 * The parse result representing the input of this editor
	 */
	protected IParseResult parseResult;
	/**
	 * The name of the rules used for parsing this editor's input
	 */
	protected List<String> parseRuleNames;

	/**
	 * The document provider of this editor
	 */
	protected BasicDocumentProvider provider;
	/**
	 * A list of <code>IManager</code> working on this editor
	 */
	protected List<IManager> managerList;
	/**
	 * A list of character pairs that should be used in this editor
	 */
	protected List<CharacterPair> characterPairs;
	/**
	 * The <code>Job</code> used to parse if no suspension is wished
	 */
	private Job parseJob;
	/**
	 * Indicates whether the current parsing should be cancelled
	 */
	private Boolean parsingIsCancelled;

	public BasicCodeEditor() {
		super();

		setColorManager(new ColorManager());
		setEditorKeyEventQueue(new EditorKeyEventQueue());

		this.setSourceViewerConfiguration(getBasicConfiguration());
		this.setDocumentProvider(getBasicProvider());

		managerList = new ArrayList<IManager>();
		characterPairs = getCharacterPairs();
		parsingIsCancelled = false;

		// set up key handlers
		configureKeyHandler();
	}

	@Override
	public void dispose() {
		this.getColorManager().dispose();
		super.dispose();
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public void setColorManager(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		ISourceViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(),
				styles);

		getSourceViewerDecorationSupport(viewer);

		if (viewer instanceof ITextViewerExtension) {
			EditorKeyEventManager manager = new EditorKeyEventManager();

			// associate the newly created manager with the EditorKeyEventQueue
			// of this editor
			getEditorKeyEventQueue().setManager(manager);

			((ITextViewerExtension) viewer).appendVerifyKeyListener(manager);
		}

		// add parse listener
		getBasicProvider().getDocument(getEditorInput()).addDocumentListener(new BasicParseTimeListener(this));

		return viewer;
	}

	@Override
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);

		char[] matchChars = { '(', ')', '[', ']', '{', '}' }; // which brackets
																// to match
		ICharacterPairMatcher matcher = new DefaultCharacterPairMatcher(matchChars,
				IDocumentExtension3.DEFAULT_PARTITIONING, true);

		// character pair matching
		support.setCharacterPairMatcher(matcher);
		support.setMatchingCharacterPainterPreferenceKeys(SQDevPreferenceConstants.SQDEV_EDITOR_MATCHING_BRACKETS_KEY,
				SQDevPreferenceConstants.SQDEV_EDITOR_MATCHING_BRACKETS_COLOR_KEY);

		// newLine highlighting
		support.setCursorLinePainterPreferenceKeys(SQDevPreferenceConstants.SQDEV_EDITOR_HIGHLIGHT_CURRENTLINE_KEY,
				SQDevPreferenceConstants.SQDEV_EDITOR_HIGHLIGHT_CURRENTLINE_COLOR_KEY);

	}

	/**
	 * Gets the <code>EditorKeyEventQueue</code> of this editor.
	 * 
	 * @see EditorKeyEventQueue
	 */
	public EditorKeyEventQueue getEditorKeyEventQueue() {
		return editorKeyEventQueue;
	}

	public void setEditorKeyEventQueue(EditorKeyEventQueue editorKeyEventQueue) {
		this.editorKeyEventQueue = editorKeyEventQueue;
	}

	/**
	 * Adds the configured <code>CharacterPairs</code> as a
	 * <code>CharacterPairHandler</code> to this editor.<br>
	 * If you want to change the pairs you have to override
	 * {@link #getCharacterPairs()}
	 * 
	 * @see CharacterPairHandler
	 * @see CharacterPair
	 */
	protected void configureCharacterPairHandler() {
		CharacterPairHandler pairHandler = new CharacterPairHandler(this);

		for (CharacterPair currentPair : getConfiguredCharacterPairs()) {
			pairHandler.addPair(currentPair);
		}

		getEditorKeyEventQueue().queueEditorKeyHandler(pairHandler);
	}

	/**
	 * Gets the <code>CharacterPairs</code> that should be used by this editor
	 * 
	 * @return A <code>List</code> of CharacterPairs
	 */
	protected List<CharacterPair> getCharacterPairs() {
		List<CharacterPair> pairList = new ArrayList<CharacterPair>();

		pairList.add(CharacterPair.DOUBLE_QUOTATION_MARKS);
		pairList.add(CharacterPair.SINGLE_QUOTATION_MARKS);
		pairList.add(CharacterPair.ROUND_BRACKETS);
		pairList.add(CharacterPair.SQUARE_BRACKETS);
		pairList.add(CharacterPair.CURLY_BRACKETS);

		return pairList;
	}

	/**
	 * Gets a list of all configured character pairs from this editor
	 */
	public List<CharacterPair> getConfiguredCharacterPairs() {
		if (characterPairs == null) {
			characterPairs = new ArrayList<CharacterPair>(0);
		}

		return characterPairs;
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		// infrastructure for code folding
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

		ProjectionSupport projectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());

		projectionSupport.install();

		// turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);


		if (fSourceViewerDecorationSupport != null) {
			// combine the SQDev PreferenceStore with the editor's one

			// use the SQDev preferenceStore as the baseStore
			MultiPreferenceStore multiStore = new MultiPreferenceStore(SQDevPreferenceUtil.getPreferenceStore());

			// add the editor's preferenceStore if available
			IPreferenceStore editorStore = this.getPreferenceStore();
			if (editorStore != null) {
				multiStore.addPreferenceStore(editorStore);
			}

			fSourceViewerDecorationSupport.install(multiStore);
		}

		createManagers(managerList);

		// parse the input for the first time
		parseInput(true);
	}

	/**
	 * Updates the editor. Needed when some changes are made to the way the editor
	 * content should be displayed or when the behaviour of the editor should
	 * change.<br>
	 * <br>
	 * <b>Note:</b> This method can be called from any Thread
	 * 
	 * @param reconfigureSourceViewer
	 *            Whether it is necessary to reconfigure the sourceVieweer
	 */
	public void update(boolean reconfigureSourceViewer) {
		// TODO: gets called way to often on editor opening

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (getSourceViewer() != null) {
					getSourceViewer().invalidateTextPresentation();

					if ((getSourceViewer() instanceof ISourceViewerExtension2) && reconfigureSourceViewer) {
						// reconfigure the SourceViewer
						((ISourceViewerExtension2) getSourceViewer()).unconfigure();
						getSourceViewer().configure(getBasicConfiguration());
					}
				}
			}
		});
	}

	/**
	 * Gets the <code>BasicSourceViewerConfiguration</code> of this editor
	 * 
	 * @see {@linkplain BasicSourceViewerConfiguration}
	 */
	public BasicSourceViewerConfiguration getBasicConfiguration() {
		if (configuration == null) {
			configuration = new BasicSourceViewerConfiguration(getColorManager(), this);
		}

		return configuration;
	}

	/**
	 * Gets the <code>BasicDocumentProvider</code> of this editor
	 * 
	 * @see {@linkplain BasicDocumentProvider}
	 */
	public BasicDocumentProvider getBasicProvider() {
		if (provider == null) {
			provider = new BasicDocumentProvider();
		}

		return provider;
	}

	/**
	 * Gets the {@linkplain IParseResult} representing the input of this editor
	 * 
	 * @return The result or <code>null</code> if none has been set
	 *         so far
	 */
	public IParseResult getParseResult() {
		return parseResult;
	}

	/**
	 * Gets the names of the rules used for parsing this editor's input
	 * 
	 * @return The default implementation returns <code>null</code>
	 */
	public List<String> getParseRuleNames() {
		return parseRuleNames;
	}

	/**
	 * This is a helper method that will do the parsing for the given input without
	 * any checks (whether there is an active parsing job) and in the same thread as
	 * it is called
	 * 
	 * @param input
	 *            The input to parse
	 * @return The result of the parsing in form of an IStatus
	 */
	private IStatus startParsingInput(String input) {
		// preprocess
		doPreprocessorParsing(new ByteArrayInputStream(input.getBytes()));

		// check if this parsing should be cancelled
		synchronized (parsingIsCancelled) {
			if (parsingIsCancelled) {
				parsingIsCancelled = false;
				return Status.CANCEL_STATUS;
			}
		}

		// parse
		IParseResult output = doParse(new ByteArrayInputStream(input.getBytes()));

		// check if this parsing should be cancelled
		synchronized (parsingIsCancelled) {
			if (parsingIsCancelled) {
				parsingIsCancelled = false;
				return Status.CANCEL_STATUS;
			}
		}

		if (output == null
				|| output.getMarkers().stream().filter((element) -> element.getSeverity() == IMarker.SEVERITY_ERROR)
						.collect(Collectors.toList()).size() > 0) {
			// don't process the parse tree if errors came up during lexing/parsing
			applyParseChanges();

			return Status.CANCEL_STATUS;
		} else {
			parseResult = output;

			if (!processParseTree(parseResult)) {
				applyParseChanges();
			}

			return Status.OK_STATUS;
		}
	}

	/**
	 * Parses the input of this editor, updates the parseTree and sends it to the
	 * {@link #processParseTree(ParseTree)} method automatically. Before doing so it
	 * will call the preprocessor parser via {@link #doPreprocessorParsing(String)}.
	 * If you need to specify a custom preprocessor parser or disable it you have to
	 * overwrite that method.
	 * 
	 * @param suspend
	 *            Indicates whether the calling thread should be suspended until the
	 *            parsing is done. If there is currently another {@link #parseJob}
	 *            the parsing will be rescheduled but the suspension will be
	 *            cancelled
	 * 
	 * @return <code>True</code> if the parsing could be done successfully and
	 *         <code>False</code> otherwise
	 */
	public boolean parseInput(boolean suspend) {
		if (getEditorInput() == null) {
			return false;
		}

		IDocument document = getBasicProvider().getDocument(getEditorInput());

		if (document == null) {
			return false;
		}

		String content = document.get();

		if (content == null) {
			return false;
		}

		synchronized (parsingIsCancelled) {
			if (parsingIsCancelled && (parseJob == null || parseJob.getResult() != null)) {
				// There is no other parsing in progress that should be
				// cancelled and canceling is only possible after having
				// initialized it
				parsingIsCancelled = false;
			}
		}

		if (parseJob != null && parseJob.getState() != Job.NONE) {
			// The previous Job is still running -> reschedule
			parseJob.addJobChangeListener(new JobChangeAdapter() {

				@Override
				public void done(IJobChangeEvent event) {
					// As there has been a request to parse the input again
					// do  it now as the old parsing process is finished
					parseInput();
				}
			});

			return false;
		}

		if (suspend) {
			startParsingInput(content);
		} else {
			parseJob = new Job("Parsing \"" + getEditorInput().getName() + "\"...") {

				@Override
				protected IStatus run(IProgressMonitor monitor) {
					return startParsingInput(content);
				}
			};

			// schedule parsing
			parseJob.schedule();
		}

		return true;
	}

	/**
	 * Parses the input of this editor, updates the parseTree and sends it to the
	 * {@link #processParseTree(ParseTree)} method automatically. Before doing so it
	 * will call the preprocessor parser via {@link #doPreprocessorParsing(String)}.
	 * If you need to specify a custom preprocessor parser or disable it you have to
	 * overwrite that method.<br>
	 * This method will not wait until the parsing is done. If you need the parsing
	 * to be done when this method returns youo can use {@link #parseInput(boolean)}
	 * instead
	 * 
	 * @return <code>True</code> if the parsing could be done successfully and
	 *         <code>False</code> otherwise
	 */
	public boolean parseInput() {
		return parseInput(false);
	}

	/**
	 * This method will cancel a running parse-process or at least the corresponding
	 * processing of the parse result
	 */
	public void cancelParsing() {
		synchronized (parsingIsCancelled) {
			parsingIsCancelled = true;
		}
	}

	/**
	 * A default implementation of a preprocessor parser that parses the input first
	 * and sets the found macros if this editor is an instance of
	 * <code>IMacroSupport</code>.
	 */
	protected void doPreprocessorParsing(InputStream input) {
		if (this instanceof IMacroSupport && getEditorInput() instanceof IFileEditorInput) {
			PreprocessorParseResult result;
			try {
				result = ParseUtil.parseAndValidatePreprocess(input,
						((IFileEditorInput) getEditorInput()).getFile().getLocation());
			} catch (IOException e) {
				e.printStackTrace();
				createMarker(IMarker.PROBLEM, 0, 0, "Unable to preprocess the file", IMarker.SEVERITY_ERROR);
				return;
			}

			((IMacroSupport) this).setMacros(result.getMacros(), true);

			// apply markers
			result.applyMarkersTo(this);
		} else {
			createMarker(IMarker.PROBLEM, 0, 0, "Unable to preprocess the file", IMarker.SEVERITY_ERROR);
		}
	}

	/**
	 * Processes whatever needs to be processed when the ParseTree has changed <br>
	 * Note: You might want to call {@link #applyParseChanges()} after the
	 * processing
	 * 
	 * @param parseResult
	 *            The generated parse result
	 * @return Whether this function has called {@link #applyParseChanges()}. If not
	 *         the default implementation of {@link #parseInput()} will call this
	 *         function afterwards.
	 */
	protected boolean processParseTree(IParseResult parseResult) {
		return false;
	}

	/**
	 * Parses the input of this editor in order to set the {@link #parseResult} for
	 * this editor. <br>
	 * Note: You might want to call {@link #applyParseChanges()} after parsing (or
	 * rather after {@link #processParseTree(ParseTree)}.<br>
	 * Note that before this method is called {@link #doPreprocessorParsing(String)}
	 * gets called. If you don't want to use the default preprocessor parsing
	 * strategy you have to overwrite that method.
	 * 
	 * @param input
	 *            The input to parse
	 * 
	 * @return The resulting <code>ParseTree</code> or <code>null</code> if the
	 *         parsing failed (if not overridden by subclasses this method always
	 *         returns <code>null</code>
	 */
	protected IParseResult doParse(InputStream input) {
		// parsing diabled
		return null;
	}

	/**
	 * Creates all managers that should work on this editor
	 * 
	 * @param managerList
	 *            The list of managers. The newly created ones have to be added to
	 *            this list
	 */
	protected void createManagers(List<IManager> managerList) {
		// add folding manager
		managerList.add(new BasicFoldingManager(((ProjectionViewer) getSourceViewer()).getProjectionAnnotationModel()));
		// add marker manager
		managerList.add(new BasicMarkerManager(this));
	}

	@Override
	public void createMarker(String type, int offset, int length, String message, int severity) {
		if (getEditorInput() == null) {
			return;
		}

		int line;
		try {
			line = getBasicProvider().getDocument(getEditorInput()).getLineOfOffset(offset);
		} catch (BadLocationException e) {
			try {
				throw new SQDevEditorException("Can't create marker", e);
			} catch (SQDevEditorException e1) {
				e1.printStackTrace();

				return;
			}
		}

		((BasicMarkerManager) getManager(BasicMarkerManager.TYPE)).addMarker(type, line, offset, length, severity,
				message);
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		super.doSave(progressMonitor);

		// reparse on save
		parseInput();
	}

	/**
	 * Applies the changes detected by the parsing by notifying the respective
	 * managers to apply their work
	 */
	public void applyParseChanges() {
		for (IManager manager : managerList) {
			manager.apply();
		}
	}

	/**
	 * Gets a manager working on this editor of the given type
	 * 
	 * @param type
	 *            The type of the editor
	 * @return The respective editor or <code>null</code> if none could be found
	 */
	public IManager getManager(String type) {
		for (IManager manager : managerList) {
			if (manager.getType().equals(type)) {
				return manager;
			}
		}

		return null;
	}

	/**
	 * Adds a foldable area to the editor if a {@link BasicFoldingManager} has been
	 * installed.<br>
	 * In order of the changes to take effect {@link #applyParseChanges()} has to be
	 * called
	 * 
	 * @param position
	 *            The <code>Position</code> this area should be on
	 */
	public void addFoldingArea(Position position) {
		IDocument doc = getDocumentProvider().getDocument(getEditorInput());

		// don't fold if the code is only one line long
		try {
			if (doc == null
					|| doc.getLineOfOffset(position.offset) == doc.getLineOfOffset(position.offset + position.length)) {
				return;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();

			SQDevInfobox info = new SQDevInfobox("Error in code folding framework!", e);
			info.open(false);

			return;
		}

		ProjectionAnnotation annotation = new ProjectionAnnotation();

		BasicFoldingManager foldingManager = (BasicFoldingManager) getManager(BasicFoldingManager.getManagerType());

		if (foldingManager == null) {
			return;
		}

		foldingManager
				.addFoldingArea(new AbstractMap.SimpleEntry<ProjectionAnnotation, Position>(annotation, position));
	}

	/**
	 * Checks whether this editor is in a valid state (no errors in the source code)
	 */
	public boolean isValid() {
		return ((BasicMarkerManager) getManager(BasicMarkerManager.TYPE)).isValidState();
	}

	/**
	 * Confidures all KeyHandler for this editor
	 */
	protected void configureKeyHandler() {
		// set up character completion
		configureCharacterPairHandler();
	}
}
