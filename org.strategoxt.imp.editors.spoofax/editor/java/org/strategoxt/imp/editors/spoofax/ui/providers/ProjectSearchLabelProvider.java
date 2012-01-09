package org.strategoxt.imp.editors.spoofax.ui.providers;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;


/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class ProjectSearchLabelProvider  extends LabelProvider {
		
	@Override
	public String getText(Object element) {
		
		if (element instanceof IProject) {
			IProject project = (IProject)element;
			return project.getProject().getName();
		}

		if (element instanceof EntityConnectionData) {
			return "";
		}
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof IProject) {
			return  SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_PROJECT);
			
		}
		return super.getImage(element);
	}	
}
