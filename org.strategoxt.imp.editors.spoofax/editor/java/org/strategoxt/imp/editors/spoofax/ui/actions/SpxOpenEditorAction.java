package org.strategoxt.imp.editors.spoofax.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.strategoxt.imp.editors.spoofax.Activator;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxModuleDescriptor;
import org.strategoxt.imp.runtime.EditorState;

public class SpxOpenEditorAction extends Action{
	
	private ISelectionProvider provider; 
	private SpxModuleDescriptor mDesc;
	
	public SpxOpenEditorAction(ISelectionProvider selectionProvider) {
		provider = selectionProvider;
	}
	
	public void run() {
		if(isEnabled()){
			IWorkbenchWindow activeWorkbenchWindow = Activator.getInstance().getWorkbench().getActiveWorkbenchWindow();
			
			Display display = activeWorkbenchWindow.getShell().getDisplay();
						
			try {
				if(EditorState.isUIThread()){
					IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
					IEditorPart editor = IDE.openEditor(page, (IFile)mDesc.getPhysicalResource()); 
					if(editor instanceof ITextEditor){
						IDocument doc = ((ITextEditor) editor)
								.getDocumentProvider().getDocument(
										editor.getEditorInput());
						
						
						final FindReplaceDocumentAdapter searchAdapter = new FindReplaceDocumentAdapter(doc);
						
						final String packageSearchString = "package(\\s)*"+ mDesc.getEnclosingParent().getPackageName();
						IRegion pRegion = getRegion(searchAdapter , packageSearchString, 0);
						
						
						final String moduleSearchString = "module(\\s)*"+ mDesc.getModuleName();
						IRegion mRegion = getRegion(searchAdapter , moduleSearchString,pRegion.getOffset());
						
						((ITextEditor)editor).selectAndReveal(mRegion.getOffset(), mRegion.getLength());
					}
				}
				else
					EditorState.asyncOpenEditor(display, (IFile)mDesc.getPhysicalResource(), true);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param searchAdapter
	 * @return
	 * @throws BadLocationException
	 */
	private IRegion getRegion(final FindReplaceDocumentAdapter searchAdapter , String searchString , int startOffset)
			throws BadLocationException {
		return searchAdapter.find(startOffset,searchString, true, true, false, true);
	}
	
	public boolean isEnabled() {
		IStructuredSelection selection = (IStructuredSelection)provider.getSelection();
		
		if(selection.size() > 0) {
			Object toOpen = selection.getFirstElement();
			if( toOpen instanceof SpxModuleDescriptor){
				mDesc = (SpxModuleDescriptor)toOpen;
				
				this.setText("Open \'" + mDesc.getModuleName() + "\'");
				this.setToolTipText(this.getText());
				this.setImageDescriptor(SpoofaxlangDependencyViewImages.DESC_OPEN);
				return true;
			}
		}
		
		return false;
	}

}
