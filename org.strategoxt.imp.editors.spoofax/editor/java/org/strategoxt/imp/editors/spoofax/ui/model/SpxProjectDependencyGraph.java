package org.strategoxt.imp.editors.spoofax.ui.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxProjectDependencyGraph extends SpxBaseDescriptor{
	private final Map<String, SpxPackageDescriptor> packages = new HashMap<String, SpxPackageDescriptor>();

	private final String projectLocation;
	private final String projectName;

	public SpxProjectDependencyGraph(IProject project) {
		this(project.getName(), project.getLocation().toOSString());
		
		this.setParent(SpoofaxlangCnfRoot.getDefault());
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectLocation() {
		return projectLocation;
	}
	
	public SpxProjectDependencyGraph(String projectName, String projectLocation) {
		this.projectLocation = projectLocation;
		this.projectName = projectName;
	}

	public void clearAllPackages() {
		packages.clear();
	}

	public Collection<SpxPackageDescriptor> getEnclosedPackages() {
		return packages.values();
	}

	public void addPackage(SpxPackageDescriptor p) {
		packages.put(p.getPackageName(), p);
		p.setParent(this);
	}

	public SpxPackageDescriptor removePackage(SpxPackageDescriptor p) {
		return packages.remove(p.getPackageName());
	}
	
	public SpxPackageDescriptor getPackageDescriptor( String id){return packages.get(id);}

	
	public Collection<SpxPackageDescriptor> getDependency( Object selected) {
		Set<SpxPackageDescriptor>  descriptors = new HashSet<SpxPackageDescriptor>();
		if( selected instanceof SpxPackageDescriptor){
			SpxPackageDescriptor p = (SpxPackageDescriptor)selected;
			
			for ( String pId :p.getDependencies() ){
				descriptors.add(getPackageDescriptor(pId)); 
			}
		}
		return descriptors;
	}
	
	public Collection<SpxPackageDescriptor> getDependant(Object selected) {
		Set<SpxPackageDescriptor>  descriptors = new HashSet<SpxPackageDescriptor>();
		if( selected instanceof SpxPackageDescriptor){
			SpxPackageDescriptor p = (SpxPackageDescriptor)selected;
			
			for ( String pId :p.getDependants() ){
				descriptors.add(getPackageDescriptor(pId)); 
			}
		}
		return descriptors;
	}
	
	public void addImportDependency( String packageName , String importRef){
		SpxPackageDescriptor spxPackageInstance = this.packages.get(packageName) ;
		SpxPackageDescriptor importedPackage = this.packages.get(importRef) ;
		
		if((spxPackageInstance != null) && (importedPackage != null)){
			spxPackageInstance.addImportedPackage(importedPackage.getPackageName());
			importedPackage.addImportedToPackage(spxPackageInstance.getPackageName());
		}
	}
	
	public SpoofaxlangCnfRoot getEnclosingParent(){
		return this.<SpoofaxlangCnfRoot>getTypedParent();
	}
}
