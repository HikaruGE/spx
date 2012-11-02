package org.strategoxt.imp.editors.spoofax.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;


public class PerspectiveFactory implements IPerspectiveFactory {

	public static final String ID = "org.strategoxt.imp.editors.spoofax.DefaultPerspective";
	public static final String DEPENDENCY_EXPLORER_ID = "org.strategoxt.imp.editors.spoofax.ui.views.SpxDependencyVisualizationView";
	public static final String PACKAGE_EXPLORER_ID = "org.strategoxt.imp.editors.spoofax.ui.views.SpoofaxPackageExplorerView";
	public static final String NEW_PROJECT_WIZARD_ID = "org.strategoxt.imp.metatooling.wizards.NewSpxEditorWizard";
	public static final String NEW_LANGUAGE_WIZARD_ID = "org.strategoxt.imp.metatooling.wizards.NewSpxLanguageWizard";
	
	public void createInitialLayout(IPageLayout layout) {
	
		String editorArea = layout.getEditorArea();
	
		layout.addNewWizardShortcut(NEW_PROJECT_WIZARD_ID);
		layout.addNewWizardShortcut(NEW_LANGUAGE_WIZARD_ID);			 
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
	
		
		
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT,    (float) 0.20, editorArea);
		IFolderLayout down = layout.createFolder("down", IPageLayout.BOTTOM,  (float) 0.80, editorArea);
		IFolderLayout right = layout.createFolder("right", IPageLayout.RIGHT, (float) 0.75, editorArea);

		IFolderLayout rightBottom = layout.createFolder("rightBottom", IPageLayout.BOTTOM, (float) 0.70, "right");
		
		
		left.addView("org.eclipse.jdt.ui.PackageExplorer");
		
		down.addView(IPageLayout.ID_PROBLEM_VIEW);
		down.addView("org.eclipse.ui.console.ConsoleView");
		down.addView(DEPENDENCY_EXPLORER_ID);
		
		right.addView(PACKAGE_EXPLORER_ID);
		rightBottom.addView(IPageLayout.ID_OUTLINE);

		
		layout.addShowViewShortcut(PACKAGE_EXPLORER_ID);
		layout.addShowViewShortcut(DEPENDENCY_EXPLORER_ID);
		layout.addShowViewShortcut("org.eclipse.ui.console.ConsoleView");
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);		
	}

}
