package org.strategoxt.imp.editors.spoofax;

import org.eclipse.core.resources.ResourcesPlugin;
import org.osgi.framework.BundleContext;

public class SpoofaxlangActivator extends Activator {
	
	private SpxResourceRemoveListener changeListener;
	
	public SpoofaxlangActivator(){
		changeListener  = new SpxResourceRemoveListener();
	}

	@Override
	public void start(BundleContext context) throws Exception {
		try{
			changeListener.addListenerTo(ResourcesPlugin.getWorkspace());
		}
		finally{
			super.start(context);
		}
	}

	
	@Override
	public void stop(BundleContext context) throws Exception {
		try{
			changeListener.removeListenerFrom(ResourcesPlugin.getWorkspace());
		}
		finally{
			super.stop(context);
		}
	}
}
