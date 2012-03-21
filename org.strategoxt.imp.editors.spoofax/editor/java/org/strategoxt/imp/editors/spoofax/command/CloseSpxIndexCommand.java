package org.strategoxt.imp.editors.spoofax.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.strategoxt.imp.editors.spoofax.generated.close_spoofaxlang_index_jvm_0_0;
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

public class CloseSpxIndexCommand implements ISpxlangCommand<Set<IProject>, Boolean> {

	private Set<IProject> projects;
	private boolean isSuccessful = false;

	private boolean active = false;

	public void execute() {
		try {
			Environment.getStrategoLock().lock();
			if( !isActive()) {
				try {
					setCommandIsActiveTo(true);
					EditorIOAgent currentIOAgent; 
					
					for( IProject p : projects){
						currentIOAgent = SpxlangCommandHelper.newEditorIOAgent( p.getLocation() , null);
						executeCloseSpxIndex(p, new NullProgressMonitor(), currentIOAgent);
					}
					isSuccessful= true;
					
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


	private boolean executeCloseSpxIndex(IResource project,IProgressMonitor monitor , EditorIOAgent agent){
		boolean  retValue = false;

		Context contextSpoofaxLang = new Context(Environment.getTermFactory(), agent);
		contextSpoofaxLang.addOperatorRegistry(new IMPLibrary());
		spoofaxlang.init(contextSpoofaxLang);

		ITermFactory termFactory = contextSpoofaxLang.getFactory();
		IStrategoTerm input = termFactory.makeString(project.getLocation().toOSString());

		dr_scope_all_start_0_0.instance.invoke(contextSpoofaxLang, input);
		try {
			monitor.setTaskName("closing spx index for following project : " +project );
			close_spoofaxlang_index_jvm_0_0.instance.invoke(contextSpoofaxLang , input);
			retValue = true;
			monitor.setTaskName("closing spx index is done");

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

	public Boolean getResult() {
		return isSuccessful;
	}
	
	public void setContext(Set<IProject> projects) {
		this.projects  = projects;
	}

	private void setCommandIsActiveTo( boolean value){
		active = value;
	}

	private boolean isActive(){
		return active;
	} 

}
