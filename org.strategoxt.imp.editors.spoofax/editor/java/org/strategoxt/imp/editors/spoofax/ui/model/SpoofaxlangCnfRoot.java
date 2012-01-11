package org.strategoxt.imp.editors.spoofax.ui.model;

import org.eclipse.core.runtime.PlatformObject;


public class SpoofaxlangCnfRoot extends PlatformObject{

	private static SpoofaxlangCnfRoot instance = new SpoofaxlangCnfRoot();
	
	public static SpoofaxlangCnfRoot getDefault(){ return instance; }
}

