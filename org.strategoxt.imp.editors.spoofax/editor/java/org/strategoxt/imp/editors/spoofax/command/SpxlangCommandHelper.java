package org.strategoxt.imp.editors.spoofax.command;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.strategoxt.imp.runtime.services.StrategoObserver;
import org.strategoxt.imp.runtime.stratego.EditorIOAgent;

public final class SpxlangCommandHelper {

	static EditorIOAgent newEditorIOAgent(IPath location , StrategoObserver observer) throws FileNotFoundException, IOException{
		
		EditorIOAgent agent = new EditorIOAgent();
		agent.setAlwaysActivateConsole(true);
		agent.setWorkingDir(location.toOSString());
		agent.setProjectPath(location.toOSString());

		//  TODO FIX : imploder attachment is null while project is built from ant script.
		//  hence, adding an stratego observer is not adding any value
		
		//	observer.getRuntime().setIOAgent(agent);
		//	((EditorIOAgent)observer.getRuntime().getIOAgent()).setJob(new StrategoObserverUpdateJob(observer));

		return agent;
	}
}
