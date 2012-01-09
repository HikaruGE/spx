package org.strategoxt.imp.editors.spoofax;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.strategoxt.imp.editors.spoofax.command.GetSpxIndexSummaryCommand;
import org.strategoxt.imp.editors.spoofax.command.ISpxlangCommand;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxDependencyGraphAdapter;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxPackageDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxProjectDependencyGraphProvider {

	final SpxDependencyGraphAdapter adapter;
	
	HashMap<String, SpxProjectDependencyGraph> testMap = new HashMap<String, SpxProjectDependencyGraph>();
	
	public SpxProjectDependencyGraphProvider(){
		adapter = new SpxDependencyGraphAdapter();
	
		buildTestGraphs();
	} 
	
	SpxProjectDependencyGraph g1; 
	SpxProjectDependencyGraph g2;
	static boolean flip = false;

	private void buildTestGraphs() {
		
		g1 = new SpxProjectDependencyGraph("test" , "c:/test");
		
		final String packageId1 = "test.p1";
		final String packageId2 = "test.p2";
		
		SpxPackageDescriptor p1 = new SpxPackageDescriptor(packageId1);
		SpxPackageDescriptor p2 = new SpxPackageDescriptor(packageId2);

		p1.addEnclosedModule(packageId1+".m1", "c:/test/m1.spx");
		p1.addEnclosedModule(packageId1+".m2", "c:/test/m2.spx");
		p2.addEnclosedModule(packageId2+".m3", "c:/test/m3.spx");
		
		p1.addImportedPackage(packageId2);
		p2.addImportedToPackage(packageId1);
		
		g1.addPackage(p1);
		g1.addPackage(p2);
	
	
		g2 = new SpxProjectDependencyGraph("test2" , "c:/test2");
		
		final String packageId3 = "test2.p3";
		final String packageId4 = "test2.p4";
		final String packageId5 = "test2.p5";
		
		SpxPackageDescriptor p3 = new SpxPackageDescriptor(packageId3);
		SpxPackageDescriptor p4 = new SpxPackageDescriptor(packageId4);
		SpxPackageDescriptor p5 = new SpxPackageDescriptor(packageId5);

		p3.addEnclosedModule(packageId3+".m4", "c:/test2/m4.spx");
		p3.addEnclosedModule(packageId3+".m5", "c:/test2/m5.spx");
		p4.addEnclosedModule(packageId4+".m6", "c:/test2/m6.spx");
		p5.addEnclosedModule(packageId5+".m7", "c:/test2/m7.spx");
		
		p3.addImportedPackage(packageId4);
		p4.addImportedToPackage(packageId3);
		
		p4.addImportedPackage(packageId5);
		p5.addImportedToPackage(packageId4);
		
		
		g2.addPackage(p3);
		g2.addPackage(p4);
		g2.addPackage(p5);
	}
	
	
	public SpxProjectDependencyGraph getDependencyGraph(IProject project){
		
		String projectLocation = project.getLocation().toOSString();
		String projectName = project.getName();
		
		SpxProjectDependencyGraph graph = new SpxProjectDependencyGraph(project);
		
		ISpxlangCommand<String, IStrategoTerm>  command = new GetSpxIndexSummaryCommand();
		command.setContext(projectLocation);
		
		command.execute();
		
		IStrategoTerm retTermIndexSummary = command.getResult();
		
		return adapter.adapt(graph, retTermIndexSummary);
	}
	
	
}
