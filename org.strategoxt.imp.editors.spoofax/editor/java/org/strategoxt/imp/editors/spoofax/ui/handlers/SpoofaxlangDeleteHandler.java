package org.strategoxt.imp.editors.spoofax.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class SpoofaxlangDeleteHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection currentSelection = HandlerUtil.getCurrentSelection(event);

		//TODO  IMPLEMENT : SPOOFAXLANG SPECIFIC DELETE  
		System.out.println("Delete " + currentSelection);
		
		return null;
	}
}
