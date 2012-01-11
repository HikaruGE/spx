package org.strategoxt.imp.editors.spoofax.ui.model;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.core.resources.IResource;
import org.strategoxt.imp.runtime.stratego.EditorIOAgent;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxModuleDescriptor  extends SpxBaseDescriptor{

	private final String moduleName ; 
	private final String resourceLocation; 
	
	public SpxModuleDescriptor(String moduleName, String absPath){
		this.moduleName = moduleName;
		this.resourceLocation = absPath;
	}
	
	public String getModuleName(){
		return moduleName;
	}
	
	public static String getModuleName(String packageName , String qualifiedModuleName){
		return stripPackageName(packageName,qualifiedModuleName);
	}
	
	private static String stripPackageName(String packageName, String qualifiedModuleName){
		return qualifiedModuleName.substring(packageName.length()+1);
	}
	
	
	public String getRelativePath (String base){
		 return new File(base).toURI().relativize(new File(resourceLocation).toURI()).getPath();
	}
	
	public String getAbsoluteLocation(){return resourceLocation;}
	
	public IResource getPhysicalResource() throws FileNotFoundException{return  EditorIOAgent.getResource(new File(resourceLocation));}
	
	
	public SpxPackageDescriptor getEnclosingParent(){
		return this.<SpxPackageDescriptor>getTypedParent();
	}
}
