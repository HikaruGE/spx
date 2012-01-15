package org.strategoxt.imp.editors.spoofax.ui.providers;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;
import org.eclipse.zest.core.viewers.INestedContentProvider;
import org.strategoxt.imp.editors.spoofax.SpxProjectDependencyGraphProvider;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxPackageDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxDependencyContentProvider implements IGraphEntityContentProvider  , INestedContentProvider{
	
	Object currentProject = null;
	SpxProjectDependencyGraph graph = null;
	
	public Object[] getConnectedTo(Object entity) {
		ArrayList<Object> results = new ArrayList<Object>();
		
		if (entity instanceof SpxPackageDescriptor){
			Collection<String> conntectedPackageIDs = ((SpxPackageDescriptor)entity).getDependencies();
			for( String id : conntectedPackageIDs){
				SpxPackageDescriptor p = graph.getPackageDescriptor(id);
				if(p != null){
					results.add(p);
				}
			}
		}
		
		return results.toArray();
	}

	public Object[] getElements(Object inputElement) {
		ArrayList<Object> results = new ArrayList<Object>();
		if ( inputElement instanceof IProject){
			if(graph == null){
				computeDependecyGraphFor( (IProject) inputElement);
			}
			results.addAll(graph.getEnclosedPackages());
		}
		return results.toArray() ;
	}

	public void computeDependecyGraphFor(Object obj){
		if( obj instanceof IProject){
			IProject project = (IProject)obj;
			SpxProjectDependencyGraphProvider provider = new SpxProjectDependencyGraphProvider();
			graph = provider.getDependencyGraph(project);
		}
	} 
	
	public SpxProjectDependencyGraph getActiveDependencyGraph(){ return this.graph;}
	
	
	public double getWeight(Object entity1, Object entity2) {
		return 0;
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		currentProject = newInput;
		computeDependecyGraphFor(currentProject);
	}

	public boolean hasChildren(Object element) {
		if( element instanceof SpxPackageDescriptor){
			return true;
		}
		return false;
	}

	public Object[] getChildren(Object element) {
		ArrayList<Object> results = new ArrayList<Object>();
		if( element instanceof SpxPackageDescriptor){
			SpxPackageDescriptor  p = (SpxPackageDescriptor)element;
			results.addAll(p.getEnclosedModules());
		}
		return results.toArray();
	}
}