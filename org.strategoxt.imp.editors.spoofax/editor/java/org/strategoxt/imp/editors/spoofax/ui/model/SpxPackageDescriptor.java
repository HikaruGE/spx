package org.strategoxt.imp.editors.spoofax.ui.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SpxPackageDescriptor extends SpxBaseDescriptor{

	private final String packageName; // name of the package 
	private final Set<String> importedPackages; // id of the imported Package 
	private final Set<String> importedToPackages; // Id of the packages where this package is importedTo 
	private final Set<SpxModuleDescriptor> enclosedModules;

	public SpxPackageDescriptor(String packageName) {
		this(packageName, new HashSet<String>(), new HashSet<String>(),
				new HashSet<SpxModuleDescriptor>());
	}

	public SpxPackageDescriptor(String packageName,
			Set<String> importedPackages, Set<String> importedToPackages,
			Set<SpxModuleDescriptor> enclosedModules) {

		this.packageName = packageName;
		this.importedPackages = importedPackages;
		this.importedToPackages = importedToPackages;
		this.enclosedModules = enclosedModules;
	}

	
	public String getPackageName() {
		return packageName;
	}

	public void addImportedPackage(String packageName) {
		importedPackages.add(packageName);
		
	}

	public void addImportedToPackage(String packageName) {
		importedToPackages.add(packageName);
	}

	public void addEnclosedModule(String moduleName, String moduleLocation) {
		enclosedModules
				.add(new SpxModuleDescriptor( SpxModuleDescriptor.getModuleName(packageName, moduleName), moduleLocation));
	}
	
	public Collection<String> getDependentOn(){
		return importedPackages; 
	}
	
	public Collection<String> getDependant(){
		return importedToPackages; 
	}
	
	public Set<SpxModuleDescriptor> getEnclosedModules() {return  enclosedModules;}
}
