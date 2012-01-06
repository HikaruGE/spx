package org.strategoxt.imp.editors.spoofax;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public final class SpxProjectsAnalyzer {

	public static List<IProject> getWorkspaceProjects()
	{
		ArrayList<IProject> activeProjects= new ArrayList<IProject>(); 
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		for (IProject p : allProjects){
			if(p.isOpen())
				activeProjects.add(p);
		}
		
		return activeProjects;
	}
}
