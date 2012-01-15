package org.strategoxt.imp.editors.spoofax.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.strategoxt.imp.editors.spoofax.ui.views.SpoofaxPackageExplorerView;

public class SpoofaxlangRefreshHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);
		
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if((part != null) && (part instanceof SpoofaxPackageExplorerView)){
				System.out.println("Refresh " + currentSelection);
				SpoofaxPackageExplorerView view = (SpoofaxPackageExplorerView)part;
				view.getCommonViewer().refresh();
		}
	
	    return null;
	}
}
