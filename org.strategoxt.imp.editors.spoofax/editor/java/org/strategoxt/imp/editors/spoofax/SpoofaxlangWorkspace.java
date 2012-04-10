package org.strategoxt.imp.editors.spoofax;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.strategoxt.imp.editors.spoofax.command.CloseSpxIndexCommand;
import org.strategoxt.imp.editors.spoofax.command.ISpxlangCommand;

/**
 * @author  Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public final class SpoofaxlangWorkspace {

	public static Set<IProject> getWorkspaceProjects(){
		return getWorkspaceProjects(ResourcesPlugin.getWorkspace());
	}
	
	public static Set<IProject> getWorkspaceProjects(IWorkspace w){
		Set<IProject> activeProjects= new HashSet<IProject>(); 
		IProject[] allProjects =w.getRoot().getProjects();
		
		for (IProject p : allProjects){
			if(p.isOpen())
				activeProjects.add(p);
		}
		
		return activeProjects;
	}
	
	
	public static void closeSpoofaxLangWorkspace(IWorkspace workspace) {
		ISpxlangCommand<Set<IProject>, Boolean>  command = new CloseSpxIndexCommand();
		command.setContext(getWorkspaceProjects(workspace));
		command.execute();
	
		if( command.getResult()){
			System.out.println("[org.spoofaxlang] Index is closed successfully for the current workspace");
		}
		else
			System.out.println("[org.spoofaxlang] Index close failed for the current workspace");
		
	}

}
