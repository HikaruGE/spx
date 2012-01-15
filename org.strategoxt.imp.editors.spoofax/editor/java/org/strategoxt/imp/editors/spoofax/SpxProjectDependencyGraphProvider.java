package org.strategoxt.imp.editors.spoofax;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.imp.editors.spoofax.command.GetSpxIndexSummariesCommand;
import org.strategoxt.imp.editors.spoofax.command.GetSpxIndexSummaryCommand;
import org.strategoxt.imp.editors.spoofax.command.ISpxlangCommand;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxProjectDependencyGraphProvider {

	public SpxProjectDependencyGraph getDependencyGraph(IProject project){
		SpxProjectDependencyGraph graph = new SpxProjectDependencyGraph(project);
		
		return getDependencyGraph(graph);
	}
	
	
	public static SpxProjectDependencyGraph getDependencyGraph(SpxProjectDependencyGraph g){
		
		SpxDependencyGraphAdapter adapter = new SpxDependencyGraphAdapter();
		ISpxlangCommand<String, IStrategoTerm>  command = new GetSpxIndexSummaryCommand();
		command.setContext(g.getProjectLocation());
		command.execute();
		
		IStrategoTerm retTermIndexSummary = command.getResult();
		return adapter.adapt(g, retTermIndexSummary);
	}
		
	
	public static Set<SpxProjectDependencyGraph> getDependencyGraphs(Set<IProject> projects){
		ISpxlangCommand<Set<IProject>, Set<SpxProjectDependencyGraph>>  command = new GetSpxIndexSummariesCommand();
		command.setContext(projects);
		command.execute();
		
		return command.getResult();
	}
	
	public static Set<SpxProjectDependencyGraph> getDependencyEmptyDependencyGraphs(Set<IProject> projects){
		
		Set<SpxProjectDependencyGraph> graphs = new HashSet<SpxProjectDependencyGraph>();
		for( IProject p : projects){
			graphs.add(new SpxProjectDependencyGraph(p));
		}
		
		return graphs;
	}
	
}
