package org.strategoxt.imp.editors.spoofax.ui.providers;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.strategoxt.imp.editors.spoofax.ui.actions.SpxOpenEditorAction;


public class SpxPackageExplorerActionProvider extends CommonActionProvider
{
	private SpxOpenEditorAction openAction;
	private Action refreshAction;
	
    public SpxPackageExplorerActionProvider()
    {

    }

    @Override
    public void init(ICommonActionExtensionSite site)
    {
        ICommonViewerSite viewSite = site.getViewSite();
        if (viewSite instanceof ICommonViewerWorkbenchSite){
            ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite) viewSite;
            openAction = new SpxOpenEditorAction(workbenchSite.getSelectionProvider());
        }
    }
    

    @Override
    public void restoreState(IMemento memento)
    {
        super.restoreState(memento);
    }

    @Override
    public void saveState(IMemento memento)
    {
        super.saveState(memento);
    }

    @Override
    public void fillActionBars(IActionBars actionBars)
    {
    	 if (openAction.isEnabled())
         {
             actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN, openAction);
         }
    }
    
    @Override
    public void fillContextMenu(IMenuManager menu)
    {
    	if ( openAction.isEnabled()){
    		menu.appendToGroup(ICommonMenuConstants.GROUP_OPEN, openAction);
    	}
    }
}
