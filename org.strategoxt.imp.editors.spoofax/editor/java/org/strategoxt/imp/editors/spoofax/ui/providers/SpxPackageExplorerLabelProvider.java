package org.strategoxt.imp.editors.spoofax.ui.providers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.IDescriptionProvider;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxImportReference;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxImportReferenceGroup;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxModuleDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxPackageDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

public class SpxPackageExplorerLabelProvider  extends LabelProvider implements ILabelProvider, IDescriptionProvider {

	public String getDescription(Object anElement) {
		if ( anElement instanceof SpxModuleDescriptor){
			SpxModuleDescriptor aModule = (SpxModuleDescriptor)anElement;
			return "Module - "+ aModule.getModuleQualifiedName() + ", located at: "+ aModule.getAbsoluteLocation() + ".";
		}
		
		if( anElement instanceof SpxPackageDescriptor){
			SpxPackageDescriptor aPackage = (SpxPackageDescriptor )anElement;
			return "Package - " + aPackage.getPackageName() + ". It has" + aPackage.getEnclosedModules().size() + " enlosed Modules.";
		}
		
		return getText(anElement);
	}
	
	@Override
	public String getText(Object element) {
		if( element instanceof SpxProjectDependencyGraph){
			return  ((SpxProjectDependencyGraph)element).getProjectName();
		}
		
		if( element instanceof SpxPackageDescriptor){
			return ((SpxPackageDescriptor)element).getPackageName();
		}
		
		
		if( element instanceof SpxImportReferenceGroup){
			return ((SpxImportReferenceGroup) element).getGroupName();
		}
		
		if(element instanceof SpxImportReference) return ((SpxImportReference) element).getName();
		
		if( element instanceof SpxModuleDescriptor){
			return ((SpxModuleDescriptor)element).getModuleQualifiedName();
		}	
		
		if(element instanceof SpxImportReferenceGroup) return ((SpxImportReferenceGroup) element).getGroupName();
		
		if (element instanceof SpxImportReference) return ((SpxImportReference) element).getName();
		
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if( element instanceof SpxProjectDependencyGraph) return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_PROJECT);
		
		if( element instanceof SpxPackageDescriptor ){
			if (((SpxPackageDescriptor)element).getEnclosedModules().size() > 0 )
				return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_PACKAGE);
			else
				return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_EMPTY_PACKAGE);
		}
		
		if( element instanceof SpxModuleDescriptor) return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_MODULE);
		
		if( element instanceof SpxImportReferenceGroup) return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_IMPORT_REFS_GROUP);
		
		if(element instanceof SpxImportReference) return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_IMPORT);
		
		return super.getImage(element);
	}

}
