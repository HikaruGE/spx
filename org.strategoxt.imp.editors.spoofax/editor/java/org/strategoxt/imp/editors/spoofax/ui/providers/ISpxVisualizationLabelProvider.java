package org.strategoxt.imp.editors.spoofax.ui.providers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public interface ISpxVisualizationLabelProvider extends ILabelProvider {

	public void setCurrentSelection(SpxProjectDependencyGraph g, Object currentSelection );
	
	/**
	 * Get all the interesting relationships.  These can be used to raise them
	 * to the front layer
	 * @return
	 */
	public Object[] getInterestingRelationships( );
	
	
	public void setReverseDependencies(boolean reverseBundleDependencies);
}
