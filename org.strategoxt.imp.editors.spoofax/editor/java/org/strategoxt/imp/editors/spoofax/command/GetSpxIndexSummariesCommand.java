package org.strategoxt.imp.editors.spoofax.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.imp.editors.spoofax.SpxDependencyGraphAdapter;
import org.strategoxt.imp.editors.spoofax.generated.get_index_summary_jvm_0_0;
import org.strategoxt.imp.editors.spoofax.generated.spoofaxlang;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;
import org.strategoxt.imp.runtime.Environment;
import org.strategoxt.imp.runtime.stratego.EditorIOAgent;
import org.strategoxt.imp.runtime.stratego.IMPLibrary;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.StrategoErrorExit;
import org.strategoxt.lang.StrategoException;
import org.strategoxt.lang.StrategoExit;
import org.strategoxt.stratego_lib.dr_scope_all_end_0_0;
import org.strategoxt.stratego_lib.dr_scope_all_start_0_0;

public class GetSpxIndexSummariesCommand implements ISpxlangCommand<Set<IProject>, Set<SpxProjectDependencyGraph>> {

	private Set<IProject> projects;
	private Set<SpxProjectDependencyGraph> results;

	private boolean active = false;
	
	final SpxDependencyGraphAdapter adapter = new SpxDependencyGraphAdapter();

	public void execute() {
		try {
			Environment.getStrategoLock().lock();
			if( !isActive()) {
				try {
					setCommandIsActiveTo(true);
					computeIndexSummaries(projects, new NullProgressMonitor());
				}finally{
					setCommandIsActiveTo(false);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			Environment.getStrategoLock().unlock();
		}
	}


	private void computeIndexSummaries(Set<IProject> projects,IProgressMonitor monitor  ) throws StrategoException, FileNotFoundException, IOException{
		results = new HashSet<SpxProjectDependencyGraph>();
		if(projects != null){
			for( IProject p : projects){
				
				SpxProjectDependencyGraph g = new SpxProjectDependencyGraph(p);
				IStrategoTerm summaryAterm = getIndexSummaryForProject(p, monitor);
				
				results.add(adapter.adapt(g, summaryAterm));
			}
		}
	}


	/**
	 * @param p
	 * @param agent
	 * @param contextSpoofaxLang
	 * @param summaries
	 * @param monitor
	 * @throws StrategoException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private IStrategoTerm getIndexSummaryForProject(IProject p, IProgressMonitor monitor) throws StrategoException, FileNotFoundException, IOException {
		
		IStrategoTerm retValue = null;
		
		EditorIOAgent currentIOAgent = SpxlangCommandHelper.newEditorIOAgent(p.getLocation() , null);

		Context contextSpoofaxLang = new Context(Environment.getTermFactory(), currentIOAgent);
		contextSpoofaxLang.addOperatorRegistry(new IMPLibrary());
		spoofaxlang.init(contextSpoofaxLang);


		IStrategoString input = contextSpoofaxLang.getFactory().makeString(p.getLocation().toOSString());
		dr_scope_all_start_0_0.instance.invoke(contextSpoofaxLang, input);
		try {
			monitor.setTaskName("Analyzing SPX files and building Index summary.");
			retValue = get_index_summary_jvm_0_0.instance.invoke(contextSpoofaxLang , input);

			monitor.setTaskName("Building Index Summary is done.");
		} catch (StrategoErrorExit e) {
			Environment.logException(e);
			throw new StrategoException("Command Failed: " + e.getMessage() + "\nLog follows:\n\n"
					+ currentIOAgent.getLog(), e);
		} catch (StrategoExit e) {
			if (e.getValue() != 0) {
				throw new StrategoException("Command Failed.\nLog follows:\n\n"
						+ currentIOAgent.getLog(), e);
			}
		}
		finally {
			dr_scope_all_end_0_0.instance.invoke(contextSpoofaxLang, input);
			monitor.setTaskName(null);
		}
		return retValue;
	}


	private void setCommandIsActiveTo( boolean value){
		active = value;
	}

	private boolean isActive(){
		return active;
	}


	public void setContext(Set<IProject> context) {
		this.projects = context;
	}


	public Set<SpxProjectDependencyGraph> getResult() {
		return this.results;
	} 

}
