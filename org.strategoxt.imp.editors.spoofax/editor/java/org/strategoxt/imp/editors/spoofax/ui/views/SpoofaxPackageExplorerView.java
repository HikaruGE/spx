package org.strategoxt.imp.editors.spoofax.ui.views;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.navigator.CommonNavigator;
import org.strategoxt.imp.editors.spoofax.ui.model.SpoofaxlangCnfRoot;

public class SpoofaxPackageExplorerView extends CommonNavigator{
	
	protected IAdaptable getInitialInput()
    {
		return SpoofaxlangCnfRoot.getDefault();
    }
}
