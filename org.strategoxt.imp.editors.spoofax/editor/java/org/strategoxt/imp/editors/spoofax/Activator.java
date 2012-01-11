package org.strategoxt.imp.editors.spoofax;

import org.eclipse.imp.preferences.PreferencesService;
import org.eclipse.imp.runtime.PluginBase;
import org.osgi.framework.BundleContext;

public class Activator extends PluginBase 
{ 
  public static final String kPluginID = "SpoofaxLang";

  public static final String kLanguageName = "SpoofaxLang";

  protected static Activator sPlugin;

  public static Activator getInstance()
  { 
    if(sPlugin == null)
      return new Activator();
    return sPlugin;
  }

  public Activator () 
  { 
    super();
    sPlugin = this;
  }

  @Override public void start(BundleContext context) throws Exception
  { 
	  // add resource change listener 
	  super.start(context);
  }

  @Override public void stop(BundleContext context) throws Exception {
	  try{
		 // clean up the resource change listener 
	  }
	  finally{
		  super.stop(context);
	  }
  };
  @Override public String getID()
  { 
    return kPluginID;
  }

  @Override public String getLanguageID()
  { 
    return kLanguageName;
  }

  protected static PreferencesService preferencesService = null;
}