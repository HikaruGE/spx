package org.strategoxt.imp.editors.spoofax.ui.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonNavigator;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;
import org.strategoxt.imp.editors.spoofax.ui.model.SpoofaxlangCnfRoot;



public class SpoofaxPackageExplorerView extends CommonNavigator{

	private Action refreshAction;
	protected IAdaptable getInitialInput()
	{
		return SpoofaxlangCnfRoot.getDefault();
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
		this.getCommonViewer().setComparator(new PackageExplorerComparator());
		makeActions();
		this.fillToolBar();
	}
	
	private void makeActions(){
		refreshAction = new Action(){
			public void run(){
				getCommonViewer().refresh();
				//getCommonViewer().setInput(getInitialInput());
			}
		};

		refreshAction.setText("Refresh");
		refreshAction.setToolTipText("Refresh");
		refreshAction.setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_REFRESH);
	}
	
	private void fillToolBar() {
		IActionBars bars = getViewSite().getActionBars();

		fillLocalToolBar(bars.getToolBarManager());
	}



	private void fillLocalToolBar(IToolBarManager toolBarManager) {

		
		toolBarManager.add(new Separator());
		toolBarManager.add(refreshAction);
		toolBarManager.add(new Separator());
	}
}
