package org.strategoxt.imp.editors.spoofax.ui.providers;

import java.util.ArrayList;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.strategoxt.imp.editors.spoofax.SpxProjectDependencyGraphProvider;
import org.strategoxt.imp.editors.spoofax.SpoofaxlangWorkspace;
import org.strategoxt.imp.editors.spoofax.ui.model.SpoofaxlangCnfRoot;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxImportReference;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxImportReferenceGroup;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxModuleDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxPackageDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

public class SpxPackageExplorerContentProvider implements ITreeContentProvider{

	public void dispose() {
	}
	Set<SpxProjectDependencyGraph> packageExplorerContent = null;

	SpxProjectDependencyGraphProvider provider = new SpxProjectDependencyGraphProvider();

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing
	}

	public Object[] getElements(Object inputElement) {

		ArrayList<Object> results = new ArrayList<Object>();

		if ( inputElement instanceof SpoofaxlangCnfRoot){
			Set<IProject> projects = SpoofaxlangWorkspace.getWorkspaceProjects();
			results.addAll(SpxProjectDependencyGraphProvider.getDependencyEmptyDependencyGraphs(projects));
		}

		if( inputElement instanceof SpxProjectDependencyGraph){

			SpxProjectDependencyGraph g = SpxProjectDependencyGraphProvider.getDependencyGraph((SpxProjectDependencyGraph)inputElement);
			results.addAll(g.getEnclosedPackages());
		}

		if ( inputElement instanceof SpxPackageDescriptor){
			if( ((SpxPackageDescriptor)inputElement).getDependencies().size() >0){
				results.add( 
						new SpxImportReferenceGroup(
								SpxImportReferenceGroup.IMPORT_DECLARATIONS_LABEL_TEXT,    
								((SpxPackageDescriptor)inputElement).getDependencies(),
								(SpxPackageDescriptor)inputElement
						)
				);
			}
			if( ((SpxPackageDescriptor)inputElement).getDependants().size() >0){
				results.add( 
						new SpxImportReferenceGroup(
								SpxImportReferenceGroup.DEPENDANTS_LABEL_TEXT,    
								((SpxPackageDescriptor)inputElement).getDependants(),
								(SpxPackageDescriptor)inputElement
						)
				);
			}
			results.addAll(((SpxPackageDescriptor) inputElement).getEnclosedModules());
		}

		if(inputElement instanceof SpxImportReferenceGroup){
			results.addAll( ((SpxImportReferenceGroup) inputElement).getChildren());
		}

		return results.toArray() ;
	}

	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object getParent(Object element) {
		if ( element instanceof SpxProjectDependencyGraph) return ((SpxProjectDependencyGraph) element).getParent();

		if ( element instanceof SpxPackageDescriptor) return ((SpxPackageDescriptor) element).getParent();

		if( element instanceof SpxModuleDescriptor)	return ((SpxModuleDescriptor) element).getParent();

		if (element instanceof SpxImportReferenceGroup) return ((SpxImportReferenceGroup) element).getParent();

		if (element instanceof  SpxImportReference) return ((SpxImportReference) element).getParent();

		return null;
	}

	public boolean hasChildren(Object element) {

		return element instanceof SpoofaxlangCnfRoot ||  element instanceof SpxProjectDependencyGraph || element instanceof SpxPackageDescriptor || element instanceof SpxImportReferenceGroup;
	}



}
