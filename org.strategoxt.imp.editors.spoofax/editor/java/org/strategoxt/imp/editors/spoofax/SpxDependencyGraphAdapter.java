package org.strategoxt.imp.editors.spoofax;

import static org.spoofax.interpreter.core.Tools.asJavaString;
import static org.spoofax.interpreter.core.Tools.listAt;
import static org.spoofax.interpreter.core.Tools.stringAt;

import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.terms.StrategoListIterator;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxPackageDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxDependencyGraphAdapter {
	static final int PACKAGE_NAME_INDEX = 0;
	static final int IMPORT_REFS_INDEX = 1;
	static final int PACKAGE_MODULES_INDEX = 2;


	// Converts IStrategoTerm representation to DependencyGraph

	public SpxDependencyGraphAdapter(){ }

	public SpxProjectDependencyGraph adapt(SpxProjectDependencyGraph projectSpxDependencies, IStrategoTerm atermDependencyGraph){
		assert projectSpxDependencies != null : "Non Null projectSpxDependencies expected";
		assert atermDependencyGraph instanceof IStrategoList : "IStrategoList is expected. Type mismatch at atermDependencyGraph ";
		try {
			// building package and module descriptors
			for (IStrategoTerm i: StrategoListIterator.iterable((IStrategoList)atermDependencyGraph)) {
				buildPackageDescriptor(projectSpxDependencies, i);
			}

			// setting the references 
			for (IStrategoTerm i: StrategoListIterator.iterable((IStrategoList)atermDependencyGraph)) {
				buildPackageImportReferences(projectSpxDependencies, i);
			}

		} catch (Exception ex){
			ex.printStackTrace();
		}
	
		return projectSpxDependencies;
	}

	private void buildPackageImportReferences(
			SpxProjectDependencyGraph projectSpxDependencies, IStrategoTerm packageTerm) {

		String packageName = asJavaString(stringAt(packageTerm, PACKAGE_NAME_INDEX));

		for (IStrategoTerm importRef: StrategoListIterator.iterable((IStrategoList)listAt(packageTerm, IMPORT_REFS_INDEX))) {
			projectSpxDependencies.addImportDependency(packageName,  asJavaString(importRef));
		}

	}

	private void buildPackageDescriptor(SpxProjectDependencyGraph projectSpxDependencies , IStrategoTerm packageTerm){

		SpxPackageDescriptor desc  = null;

		desc = new SpxPackageDescriptor(asJavaString(stringAt(packageTerm, PACKAGE_NAME_INDEX)));

		for (IStrategoTerm module: StrategoListIterator.iterable((IStrategoList)listAt(packageTerm, PACKAGE_MODULES_INDEX))) {
			desc.addEnclosedModule( asJavaString(stringAt(module , 0)),  asJavaString(stringAt(module , 1)));
		}
		projectSpxDependencies.addPackage(desc);
	} 
}


