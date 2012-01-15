package org.strategoxt.imp.editors.spoofax.ui.views;

import org.eclipse.jface.viewers.ViewerComparator;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxImportReferenceGroup;

class PackageExplorerComparator extends ViewerComparator {
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
	 */
	public int category(Object element) {
		if( element instanceof SpxImportReferenceGroup){
			return Integer.MAX_VALUE - 1;
		}
		return Integer.MAX_VALUE;
	}
}