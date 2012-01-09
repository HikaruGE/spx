package org.strategoxt.imp.editors.spoofax.ui.actions;

import org.eclipse.jface.action.Action;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;
import org.strategoxt.imp.editors.spoofax.ui.views.SpxDependencyVisualizationView;

public class ShowCalleesAction extends Action {

	SpxDependencyVisualizationView view;
	
	public ShowCalleesAction( SpxDependencyVisualizationView view) {
		super("", AS_RADIO_BUTTON); //$NON-NLS-1$
		this.view = view;
		setText("Dependencies");
		setDescription("Show Dependencies");
		setToolTipText("Show Dependencies");
		setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_CALLEES);
	}

	/*
	 * @see Action#actionPerformed
	 */
	public void run() {
		if (isChecked()) {
			view.showDependencies(false);
		}
	}
}