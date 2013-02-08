package org.strategoxt.imp.editors.spoofax.configuration;

import org.strategoxt.imp.runtime.dynamicloading.Descriptor;
import org.strategoxt.imp.runtime.services.MetaFileLanguageValidator;

public class SpoofaxConfigurationValidator extends MetaFileLanguageValidator 
{ 
  @Override public Descriptor getDescriptor()
  { 
    return SpoofaxConfigurationParseController.getDescriptor();
  }
}