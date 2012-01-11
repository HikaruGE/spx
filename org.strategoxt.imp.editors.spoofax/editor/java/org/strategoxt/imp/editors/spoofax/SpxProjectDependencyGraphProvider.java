package org.strategoxt.imp.editors.spoofax;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.imp.editors.spoofax.command.GetSpxIndexSummaryCommand;
import org.strategoxt.imp.editors.spoofax.command.ISpxlangCommand;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxProjectDependencyGraphProvider {

	final SpxDependencyGraphAdapter adapter;
	
	HashMap<String, SpxProjectDependencyGraph> testMap = new HashMap<String, SpxProjectDependencyGraph>();
	
	public SpxProjectDependencyGraphProvider(){
		adapter = new SpxDependencyGraphAdapter();
	} 

	public SpxProjectDependencyGraph getDependencyGraph(IProject project){
		
		String projectLocation = project.getLocation().toOSString();
		SpxProjectDependencyGraph graph = new SpxProjectDependencyGraph(project);
		
		ISpxlangCommand<String, IStrategoTerm>  command = new GetSpxIndexSummaryCommand();
		command.setContext(projectLocation);
		
		command.execute();
		
		IStrategoTerm retTermIndexSummary = command.getResult();
		
		return adapter.adapt(graph, retTermIndexSummary);
	}
	
	
}
