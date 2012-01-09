package org.strategoxt.imp.editors.spoofax.ui.actions;

import org.eclipse.jface.action.Action;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;
import org.strategoxt.imp.editors.spoofax.ui.views.SpxDependencyVisualizationView;

public class ShowCallersAction extends Action {

	SpxDependencyVisualizationView view;

	public ShowCallersAction(SpxDependencyVisualizationView view) {
		super("", AS_RADIO_BUTTON); //$NON-NLS-1$
		setText("Dependents");
		setDescription("Show Depedents");
		setToolTipText("Show Depedents");
		setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_CALLERS);
		this.view = view;
	}

	/*
	 * @see Action#actionPerformed
	 */
	public void run() {
		if (isChecked()) {
			view.showDependencies(true);
		}
	}
}