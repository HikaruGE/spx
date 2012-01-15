package org.strategoxt.imp.editors.spoofax.ui;

import java.util.Comparator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;
import org.strategoxt.imp.editors.spoofax.Activator;
import org.strategoxt.imp.editors.spoofax.SpxProjectsAnalyzer;
import org.strategoxt.imp.editors.spoofax.ui.providers.ProjectDetailsSearchLabelProvider;
import org.strategoxt.imp.editors.spoofax.ui.providers.ProjectSearchLabelProvider;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class ProjectSelectionDialog extends FilteredItemsSelectionDialog{
	   
	public ProjectSelectionDialog(Shell shell) {
		super(shell);
		setTitle("Search Projects");
		setListLabelProvider(new ProjectSearchLabelProvider());
		setDetailsLabelProvider(new ProjectDetailsSearchLabelProvider());
	}

	@Override
	protected Control createExtendedContentArea(Composite parent) {
		// TODO Auto-generated method stub
		return null;
	}

	 private static final String DIALOG_SETTINGS = "org.strategoxt.imp.editors.spoofax.visualization.ProjectSelectionDialog";	
		
	 protected IDialogSettings getDialogSettings() {
		 IDialogSettings settings = Activator.getInstance().getDialogSettings()
		 .getSection(DIALOG_SETTINGS);
		 if (settings == null) {
			 settings = Activator.getInstance().getDialogSettings().addNewSection(DIALOG_SETTINGS);
		 }
		 return settings;
	 }
	
	 @Override
	protected IStatus validateItem(Object item) {
		 return Status.OK_STATUS;
	}

	@Override
	protected ItemsFilter createFilter() {
		 return new ProjectSearchItemFilter();
	}

	@Override
	protected Comparator getItemsComparator() {
		return new ProjectSearchComparator();
	}

	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor progressMonitor) throws CoreException {
		
		Set<IProject> projects = SpxProjectsAnalyzer.getWorkspaceProjects(); 
		
		progressMonitor.beginTask("Searching Projects...", projects.size()); 
		
		for (IProject p : projects) {
			contentProvider.add(p, itemsFilter);
			progressMonitor.worked(1);
		}
		progressMonitor.done();
	}

	@Override
	public String getElementName(Object item) {
		if (item instanceof IProject) {
			IProject project = (IProject)item;
			return project.getProject().getName();
		}
		return null;
	}
		
	private class ProjectSearchItemFilter extends ItemsFilter {

		public boolean isConsistentItem(Object item) {
			return true;
		}

		public boolean matchItem(Object item) {
			String id = null;
			if (item instanceof IProject) {
				IProject model = (IProject) item;
				id = model.getName();
			}

			return (matches(id));
		}

		protected boolean matches(String text) {
			String pattern = patternMatcher.getPattern();
			if (pattern.indexOf("*") != 0 & pattern.indexOf("?") != 0 & pattern.indexOf(".") != 0) {//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				pattern = "*" + pattern; 
				patternMatcher.setPattern(pattern);
			}
			return patternMatcher.matches(text);
		}
	}
	
	private class ProjectSearchComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			int id1 = getId(o1);
			int id2 = getId(o2);

			if (id1 != id2)
				return id1 - id2;
			return compareSimilarObjects(o1, o2);
		}

		private int getId(Object element) {
			if (element instanceof IProject) {
				return 100;
			}
			return 0;
		}

		private int compareSimilarObjects(Object o1, Object o2) {
			if (o1 instanceof IProject && o2 instanceof IProject) {
				IProject project1 = (IProject) o1;
				IProject project2 = (IProject) o2;
				return comparePlugins(project1, project2);
			}
			return 0;
		}

		private int comparePlugins(IProject project1, IProject project2) {
			return project1.getName().compareTo(project1.getName());
		}

	}
	

}
