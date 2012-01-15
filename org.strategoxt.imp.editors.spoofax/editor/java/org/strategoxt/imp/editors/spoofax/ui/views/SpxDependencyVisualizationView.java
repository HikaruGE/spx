package org.strategoxt.imp.editors.spoofax.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.DirectedGraphLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.strategoxt.imp.editors.spoofax.Activator;
import org.strategoxt.imp.editors.spoofax.ui.ImagePreviewPane;
import org.strategoxt.imp.editors.spoofax.ui.ProjectSelectionDialog;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;
import org.strategoxt.imp.editors.spoofax.ui.actions.ShowCalleesAction;
import org.strategoxt.imp.editors.spoofax.ui.actions.ShowCallersAction;
import org.strategoxt.imp.editors.spoofax.ui.actions.SpxOpenEditorAction;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxModuleDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxPackageDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.providers.ISpxVisualizationLabelProvider;
import org.strategoxt.imp.editors.spoofax.ui.providers.SpxDependencyContentProvider;
import org.strategoxt.imp.editors.spoofax.ui.providers.SpxDependencyLabelProvider;


/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxDependencyVisualizationView extends ViewPart implements IZoomableWorkbenchPart {

	public static final int BACKSPACE = 8;
	public static final int ENTER = 13;

	private FormToolkit toolKit = null;
	private ScrolledForm form = null;
	private ManagedForm managedForm = null;
	private GraphViewer viewer;
	private Action focusDialogAction;
	private Action focusDialogActionToolbar;
	private Action showCalleesAction;
	private Action showCallersAction;
	private Action openAction;
	private Action forwardAction;
	private Action screenshotAction;
	private Action refreshAction;
	private Action refreshActionToolbar;
	private Object currentProject = null;
	private Object currentNode = null;
	private ISpxVisualizationLabelProvider currentLabelProvider;
	private SpxDependencyContentProvider contentProvider;
	private ZoomContributionViewItem contextZoomContributionViewItem;
	private ZoomContributionViewItem toolbarZoomContributionViewItem;
	private SpxDependencyVisualizationForm visualizationForm;
	private Font searchFont;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		// Adding a toolkit to the form 
		toolKit = new FormToolkit(parent.getDisplay());


		visualizationForm = new SpxDependencyVisualizationForm(parent, toolKit, this);
		viewer = visualizationForm.getGraphViewer();
		form = visualizationForm.getForm();
		managedForm = visualizationForm.getManagedForm();


		this.contentProvider = new SpxDependencyContentProvider();
		this.currentLabelProvider = new SpxDependencyLabelProvider(this.viewer);

		viewer.setContentProvider(this.contentProvider);
		viewer.setLabelProvider(this.currentLabelProvider);

		viewer.setInput(null);
		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		viewer.setLayoutAlgorithm(new CompositeLayoutAlgorithm(
				LayoutStyles.NO_LAYOUT_NODE_RESIZING, new LayoutAlgorithm[] {
						new DirectedGraphLayoutAlgorithm(
								LayoutStyles.NO_LAYOUT_NODE_RESIZING),
						new HorizontalShift(
								LayoutStyles.NO_LAYOUT_NODE_RESIZING),
						}));

		FontData fontData = Display.getCurrent().getSystemFont().getFontData()[0];
		fontData.height = 42;

		searchFont = new Font(Display.getCurrent(), fontData);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				Object selectedElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
				if (selectedElement instanceof EntityConnectionData) {
					return;
				}
				SpxDependencyVisualizationView.this.selectionChanged(selectedElement);
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				if (selection.size() < 1) {
					return;
				}
				Object selectedElement = selection.getFirstElement();
				if (selectedElement instanceof SpxPackageDescriptor) {
					SpxDependencyVisualizationView.this.selectionChanged(selectedElement);
				}
				if (selectedElement instanceof SpxModuleDescriptor) {
					openAction.run();
				}
			}
		});

		visualizationForm.getSearchBox().addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String textString = visualizationForm.getSearchBox().getText();

				HashMap figureListing = new HashMap();
				ArrayList list = new ArrayList();
				Iterator iterator = viewer.getGraphControl().getNodes().iterator();
				while (iterator.hasNext()) {
					GraphItem item = (GraphItem) iterator.next();
					figureListing.put(item.getText(), item);
				}
				iterator = figureListing.keySet().iterator();
				if (textString.length() > 0) {
					while (iterator.hasNext()) {
						String string = (String) iterator.next();
						if (string.toLowerCase().indexOf(textString.toLowerCase()) >= 0) {
							list.add(figureListing.get(string));
						}
					}
				}
				viewer.getGraphControl().setSelection((GraphItem[]) list.toArray(new GraphItem[list.size()]));
			}

		});

		toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		contextZoomContributionViewItem = new ZoomContributionViewItem(this);
		
		
		makeActions();
		hookContextMenu();
		fillToolBar();
	}


	public void showDependencies(boolean enable) {

		this.currentLabelProvider.setReverseDependencies(enable);

		StructuredSelection selection = ((StructuredSelection) viewer.getSelection());
		if (selection != null) {
			viewer.setSelection(new StructuredSelection());
			this.selectionChanged(currentNode);
		}
	}


	/**
	 * Enable dependency path in the view. This will highlight all the nodes
	 * from the selected node to the root.
	 * 
	 * @param dependencyPath
	 */
	void setDependencyPath(boolean dependencyPath, String dependencyPathType) {
		// If the viewer has not been created, return
		if (viewer == null) {
			return;
		}

		StructuredSelection selection = ((StructuredSelection) viewer.getSelection());
		if (selection != null) {
			viewer.setSelection(new StructuredSelection());
			this.selectionChanged(null);
		}

		if (selection != null) {
			viewer.setSelection(selection);
		}
	}

	/**
	 * Handle the select changed. This will update the view whenever a selection
	 * occurs.
	 * 
	 * @param selectedItem
	 */
	private void selectionChanged(Object selectedItem) {
		if(selectedItem == null){
			if( currentNode != null)
			{
				selectedItem  = currentNode;
			}
			else if( (contentProvider.getActiveDependencyGraph()!=null) && (contentProvider.getActiveDependencyGraph().getEnclosedPackages().size() > 0 )){
				selectedItem = contentProvider.getActiveDependencyGraph().getEnclosedPackages().toArray()[0];
			}
		}

		if(selectedItem != null)
		{
			currentNode = selectedItem;
			viewer.setSelection(new StructuredSelection(selectedItem));
			currentLabelProvider.setCurrentSelection(contentProvider.getActiveDependencyGraph() , selectedItem);
			viewer.update(contentProvider.getElements(currentProject), null);
		}
	}

	/**
	 * Set the toolbar
	 */
	private void fillToolBar() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);

		fillLocalToolBar(bars.getToolBarManager());

	}

	/**
	 * Add the actions to the tool bar
	 * 
	 * @param toolBarManager
	 */
	private void fillLocalToolBar(IToolBarManager toolBarManager) {
		toolBarManager.add(refreshActionToolbar);
		toolBarManager.add(new Separator());
		
		toolBarManager.add(focusDialogActionToolbar);
		toolBarManager.add(new Separator());
		toolBarManager.add(showCalleesAction);
		toolBarManager.add(showCallersAction);
		toolBarManager.add(new Separator());
		toolBarManager.add(screenshotAction);
		toolBarManager.add(new Separator());

	}

	/**
	 * Update the view to focus on a particular bundle. If record history is set
	 * to true, and bundle does not equal the current bundle, then the current
	 * bundle will be saved on the history stack
	 * 
	 */
	private void focusOn(IProject selectedProject) {
		viewer.setSelection(new StructuredSelection());
		this.selectionChanged(null);
		viewer.setFilters(new ViewerFilter[] {});
		viewer.setInput(selectedProject);

		visualizationForm.setFocusedNodeName(selectedProject.getName());
		Iterator nodes = viewer.getGraphControl().getNodes().iterator();
		if (viewer.getGraphControl().getNodes().size() > 0) {
			visualizationForm.enableSearchBox(true);
		} else {
			visualizationForm.enableSearchBox(false);
		}
		visualizationForm.enableSearchBox(true);
		Graph graph = viewer.getGraphControl();
		Dimension centre = new Dimension(graph.getBounds().width / 2, graph.getBounds().height / 2);
		while (nodes.hasNext()) {
			GraphNode node = (GraphNode) nodes.next();
			if (node.getLocation().x <= 1 && node.getLocation().y <= 1) {
				node.setLocation(centre.width, centre.height);
			}
		}
		currentProject = selectedProject;
		currentNode = null;

		this.selectionChanged(null);
	}

	/**
	 * Make the actions that can be called on this viewer. This currently
	 * includes: - Focus on ... - Focus on Selected Node - History action
	 */
	private void makeActions() {
		openAction = new SpxOpenEditorAction((ISelectionProvider) viewer);
		
		refreshAction = new Action(){
			public void run(){
				focusOn((IProject)currentProject);
			}
		};
		
		refreshAction.setText("Refresh");
		refreshAction.setToolTipText("Refresh");
		refreshAction.setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_REFRESH);
		
		
		refreshActionToolbar = new Action() {
			public void run() {
				refreshAction.run();
			}
		};
		refreshActionToolbar.setText("Refresh");
		refreshActionToolbar.setToolTipText("Refresh");
		refreshActionToolbar.setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_REFRESH);

		
		focusDialogAction = new Action() {
			public void run() {
				ProjectSelectionDialog dialog = new ProjectSelectionDialog(viewer.getControl().getShell());
				dialog.setInitialPattern("?");
				dialog.create();
				if (dialog.open() == Window.OK) {
					IProject project = (IProject) dialog.getFirstResult();
					focusOn(project);

					// When a new plug-in is selected, disable the forward action			
					// The forward action only stores history when the back button was used (much like a browser)
					//forwardStack.clear();
					forwardAction.setEnabled(false);
				}
			}
		};
		
		
		focusDialogAction.setText("Focus On ...");
		focusDialogAction.setToolTipText("Focus on a plug-in");
		focusDialogAction.setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_FOCUS);
		
		focusDialogActionToolbar = new Action() {
			public void run() {
				focusDialogAction.run();
			}
		};
		focusDialogActionToolbar.setToolTipText("Focus on a plug-in...");
		focusDialogActionToolbar.setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_FOCUS);



		screenshotAction = new Action() {
			public void run() {

				Shell shell = Activator.getInstance().getWorkbench().getActiveWorkbenchWindow().getShell();
				Graph g = (Graph) viewer.getControl();
				Rectangle bounds = g.getContents().getBounds();
				Point size = new Point(g.getContents().getSize().width, g.getContents().getSize().height);
				org.eclipse.draw2d.geometry.Point viewLocation = g.getViewport().getViewLocation();
				final Image image = new Image(null, size.x, size.y);
				GC gc = new GC(image);
				SWTGraphics swtGraphics = new SWTGraphics(gc);

				swtGraphics.translate(-1 * bounds.x + viewLocation.x, -1 * bounds.y + viewLocation.y);
				g.getViewport().paint(swtGraphics);
				gc.copyArea(image, 0, 0);
				gc.dispose();

				ImagePreviewPane previewPane = new ImagePreviewPane(shell);
				previewPane.setText("Image Preview");
				previewPane.open(image, size);
			}
		};

		screenshotAction.setText("Take A Screenshot");
		screenshotAction.setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_SNAPSHOT);
		screenshotAction.setToolTipText("Take screenshot");
		screenshotAction.setEnabled(true);

		showCalleesAction = new ShowCalleesAction(this);
		showCalleesAction.setChecked(true);
		showCallersAction = new ShowCallersAction(this);
		showCallersAction.setChecked(false);
	}



	/**
	 * Creates the context menu for this view.
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		fillContextMenu(menuMgr);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SpxDependencyVisualizationView.this.fillContextMenu(manager);

			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);

	}
	
	/**
	 * Add the items to the context menu
	 * 
	 * @param manager
	 */
	private void fillContextMenu(IMenuManager manager) {
		
		
		if (openAction.isEnabled()) {
			manager.add(openAction);
		}
		
		manager.add(refreshAction);
		manager.add(new Separator());
		
		manager.add(focusDialogAction);
		manager.add(new Separator());
		
		manager.add(screenshotAction);
		manager.add(new Separator());
		manager.add(contextZoomContributionViewItem);
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Dispose the form
	 */
	public void dispose() {
		form.dispose();
		searchFont.dispose();
		super.dispose();
	}

	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

}