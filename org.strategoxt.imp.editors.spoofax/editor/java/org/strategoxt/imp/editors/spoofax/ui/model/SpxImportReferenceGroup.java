package org.strategoxt.imp.editors.spoofax.ui.model;

import java.util.Collection;
import java.util.HashSet;

public class SpxImportReferenceGroup  extends SpxBaseDescriptor{

	public static final String IMPORT_DECLARATIONS_LABEL_TEXT = "import declarations";
	public static final String DEPENDANTS_LABEL_TEXT   = "dependants";
	
	
	final String importReferenceType ;
	final Collection<SpxImportReference> importReferences = new HashSet<SpxImportReference>();
	
	public SpxImportReferenceGroup( String type  , Collection<String> importRefs , SpxPackageDescriptor packageDesc){
		setParent(packageDesc);
		this.importReferenceType = type;
		
		for (String importRef : importRefs){
			this.importReferences.add( new SpxImportReference(this, importRef));
		}
	}
	
	public String getGroupName() { return importReferenceType  ; }
	
	public Collection<SpxImportReference> getChildren(){
		return this.importReferences;
	}
	
}

