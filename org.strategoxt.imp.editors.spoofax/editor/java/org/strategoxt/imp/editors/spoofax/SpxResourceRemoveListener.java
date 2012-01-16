package org.strategoxt.imp.editors.spoofax;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.strategoxt.imp.editors.spoofax.command.CleanResouceFromSpxIndexCommand;
import org.strategoxt.imp.editors.spoofax.command.ISpxlangCommand;

public class SpxResourceRemoveListener  implements IResourceChangeListener,IResourceDeltaVisitor {

	private static final Object SPOOFAXLANG_EXT = "spx"; //$NON-NLS-1$

	public SpxResourceRemoveListener(){
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {

		IResourceDelta delta = event.getDelta();
		try {
			if( delta!= null) {
				delta.accept(this);
			}
		} catch (CoreException e) { 
			System.err.println("Error at SpxResourceChangeListener.");
			e.printStackTrace();
		} 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
	 */
	
	public final boolean visit(final IResourceDelta delta) throws CoreException {
		if(delta == null) { return false;}
	
		IResource source = delta.getResource();
		if ( (source.getType() == IResource.FILE) && (delta.getKind() ==  IResourceDelta.REMOVED)) {
			if (SPOOFAXLANG_EXT.equals(source.getFileExtension())) {
				final IFile file = (IFile) source;
				updateModel(file);
				return false;
			}
		}
		return true;
	}

	private void updateModel(IFile file) {
		
		ISpxlangCommand<Object[], Boolean>  command = new CleanResouceFromSpxIndexCommand();
		command.setContext(new Object[]{file.getProject() , file});
		command.execute();
		if( command.getResult()){
			System.out.println("Index is cleaned successfully : "+ file.toString());
		}
		else
			System.out.println("Index clean failed for: "+ file.toString());
		
	}
	
	
	public void addListenerTo(IWorkspace workspace){
		workspace.addResourceChangeListener(this);
	}
	
	public void removeListenerFrom( IWorkspace workspace){
		workspace.removeResourceChangeListener(this);
	}
}


