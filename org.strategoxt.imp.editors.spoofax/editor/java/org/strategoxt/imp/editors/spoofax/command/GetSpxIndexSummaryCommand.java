package org.strategoxt.imp.editors.spoofax.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.spoofax.interpreter.terms.IStrategoString;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.imp.editors.spoofax.generated.get_index_summary_jvm_0_0;
import org.strategoxt.imp.editors.spoofax.generated.spoofaxlang;
import org.strategoxt.imp.runtime.Environment;
import org.strategoxt.imp.runtime.stratego.EditorIOAgent;
import org.strategoxt.imp.runtime.stratego.IMPLibrary;
import org.strategoxt.lang.Context;
import org.strategoxt.lang.StrategoErrorExit;
import org.strategoxt.lang.StrategoException;
import org.strategoxt.lang.StrategoExit;
import org.strategoxt.stratego_lib.dr_scope_all_end_0_0;
import org.strategoxt.stratego_lib.dr_scope_all_start_0_0;

public class GetSpxIndexSummaryCommand implements ISpxlangCommand<String, IStrategoTerm> {

	private String projectPath; 
	private IStrategoTerm result;

	private boolean active = false;

	public void execute() {
		try {
			Environment.getStrategoLock().lock();

			final IResource file = EditorIOAgent.getResource(new File(projectPath));
			if( !isActive() && file.exists()) {
				try {
					setCommandIsActiveTo(true);
					EditorIOAgent currentIOAgent = SpxlangCommandHelper.newEditorIOAgent(file.getLocation() , null);
					result = getIndexSummary(file, new NullProgressMonitor(), currentIOAgent);
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


	private IStrategoTerm getIndexSummary(IResource project,IProgressMonitor monitor , EditorIOAgent agent   ){
		IStrategoTerm retValue = null;

		Context contextSpoofaxLang = new Context(Environment.getTermFactory(), agent);
		contextSpoofaxLang.addOperatorRegistry(new IMPLibrary());
		spoofaxlang.init(contextSpoofaxLang);

		IStrategoString input = contextSpoofaxLang.getFactory().makeString(project.getLocation().toOSString());

		dr_scope_all_start_0_0.instance.invoke(contextSpoofaxLang, input);
		try {
			monitor.setTaskName("Analyzing SPX files and building Index summary.");
			retValue = get_index_summary_jvm_0_0.instance.invoke(contextSpoofaxLang , input);

			monitor.setTaskName("Building Index Summary is done.");

		} catch (StrategoErrorExit e) {
			Environment.logException(e);
			throw new StrategoException("Command Failed: " + e.getMessage() + "\nLog follows:\n\n"
					+ agent.getLog(), e);
		} catch (StrategoExit e) {
			if (e.getValue() != 0) {
				throw new StrategoException("Command Failed.\nLog follows:\n\n"
						+ agent.getLog(), e);
			}
		}
		finally {
			dr_scope_all_end_0_0.instance.invoke(contextSpoofaxLang, input);
		}

		monitor.setTaskName(null);
		return retValue;

	}

	public IStrategoTerm getResult() {
		return result;
	}

	public void setContext(String projectPath) {
		this.projectPath = projectPath;

	}

	private void setCommandIsActiveTo(boolean value){
		active = value;
	}

	private boolean isActive(){
		return active;
	} 

}
