package org.strategoxt.imp.editors.spoofax.ui.model;

public class SpxImportReference  extends SpxBaseDescriptor{
	final String importReference;
	
	public SpxImportReference(SpxImportReferenceGroup enclosingParent,String packageName) {
		this.setParent(enclosingParent);
		this.importReference = packageName ; 
	}
	
	public String getName() { return importReference ; }
	
}
