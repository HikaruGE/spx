package org.strategoxt.imp.editors.spoofax;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * @author  Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public final class SpxProjectsAnalyzer {

	public static Set<IProject> getWorkspaceProjects()
	{
		Set<IProject> activeProjects= new HashSet<IProject>(); 
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		for (IProject p : allProjects){
			if(p.isOpen())
				activeProjects.add(p);
		}
		
		return activeProjects;
	}
}
