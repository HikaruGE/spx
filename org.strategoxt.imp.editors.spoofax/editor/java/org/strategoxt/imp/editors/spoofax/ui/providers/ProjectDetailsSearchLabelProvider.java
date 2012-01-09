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
public class ProjectDetailsSearchLabelProvider  extends LabelProvider {
		
	@Override
	public String getText(Object element) {
		if (element instanceof IProject) {
			IProject project = (IProject)element;
			StringBuilder projectDesc = new StringBuilder();
			projectDesc.append(project.getProject().getName() );
			projectDesc.append("[");
			projectDesc.append(project.getLocation().toOSString());
			projectDesc.append("]");
			return projectDesc.toString();
		}

		if (element instanceof EntityConnectionData) {
			return "";
		}
		return super.getText(getText(element));
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof IProject) {
			return SpoofaxlangDependencyViewImages.DESC_SPX_PROJECT.createImage();		
		}
		return super.getImage(element);
	}	
}
