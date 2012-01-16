package org.strategoxt.imp.editors.spoofax.command;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.interpreter.terms.ITermFactory;
import org.strategoxt.imp.editors.spoofax.generated.clean_resource_from_spx_index_jvm_0_0;
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

public class CleanResouceFromSpxIndexCommand implements ISpxlangCommand<Object[], Boolean> {

	private IProject project;
	private IFile fileToClean;
	private boolean isSuccessful = false;

	private boolean active = false;

	public void execute() {
		try {
			Environment.getStrategoLock().lock();
			if( !isActive() && project.exists() && !fileToClean.exists()) {
				try {
					setCommandIsActiveTo(true);
					EditorIOAgent currentIOAgent = SpxlangCommandHelper.newEditorIOAgent(project.getLocation() , null);
					isSuccessful = executeCleanResourceFromSpxIndex(project,fileToClean, new NullProgressMonitor(), currentIOAgent);
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


	private boolean executeCleanResourceFromSpxIndex(IResource project, IResource resource , IProgressMonitor monitor , EditorIOAgent agent){
		boolean  retValue = false;

		Context contextSpoofaxLang = new Context(Environment.getTermFactory(), agent);
		contextSpoofaxLang.addOperatorRegistry(new IMPLibrary());
		spoofaxlang.init(contextSpoofaxLang);

		ITermFactory termFactory = contextSpoofaxLang.getFactory();
		
		IStrategoTerm input = termFactory.makeTuple(
				termFactory.makeString(project.getLocation().toOSString()),
				termFactory.makeString(resource.getLocation().toOSString())
				);

		dr_scope_all_start_0_0.instance.invoke(contextSpoofaxLang, input);
		try {
			monitor.setTaskName("Analyzing SPX files and building Index summary.");
			clean_resource_from_spx_index_jvm_0_0.instance.invoke(contextSpoofaxLang , input);
			retValue = true;
			monitor.setTaskName("Building Index Summary is done.");

		} catch (StrategoErrorExit e) {
			Environment.logException(e);
			throw new StrategoException("Project builder failed: " + e.getMessage() + "\nLog follows:\n\n"
					+ agent.getLog(), e);
		} catch (StrategoExit e) {
			if (e.getValue() != 0) {
				throw new StrategoException("Project builder failed.\nLog follows:\n\n"
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

	public void setContext(Object[] inputParams) {
		this.project  = (IProject)inputParams[0];
		this.fileToClean =(IFile) inputParams[1];
	}

	private void setCommandIsActiveTo( boolean value){
		active = value;
	}

	private boolean isActive(){
		return active;
	} 

}
