package org.strategoxt.imp.editors.spoofax.ui.views;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;


/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
class SpxDependencyVisualizationForm {

	/*
	 * These are all the strings used in the form. These can probably be
	 * abstracted for internationalization
	 */
	private static String SpoofaxProject_Dependency_Analysis = "SpoofaxLang Dependency Analyzer";

	/*
	 * Some parts of the form we may need access to
	 */
	private ScrolledForm form;
	private FormToolkit toolkit;
	private ManagedForm managedForm;
	private GraphViewer viewer;
	private SpxDependencyVisualizationView view;


	private SashForm sash;
	private Text searchBox;
	private ToolItem cancelIcon;
	private Label searchLabel;

	/**
	 * Creates the form.
	 * 
	 * @param toolKit
	 * @return
	 */
	SpxDependencyVisualizationForm(Composite parent, FormToolkit toolkit, SpxDependencyVisualizationView view) {
		this.toolkit = toolkit;
		this.view = view;
		form = this.toolkit.createScrolledForm(parent);
		managedForm = new ManagedForm(this.toolkit, this.form);
		createHeaderRegion(form);
		FillLayout layout = new FillLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 4;
		form.getBody().setLayout(layout);

		this.toolkit.decorateFormHeading(this.form.getForm());
		createSash(form.getBody());
	}

	public void setFocusedNodeName(String nodeName) {
		form.setText(SpoofaxProject_Dependency_Analysis + ": " + nodeName);
		searchBox.setText("");
		form.reflow(true);
	}

	/**
	 * Creates the header region of the form, with the search dialog, background
	 * and title.  It also sets up the error reporting
	 * @param form
	 */
	private void createHeaderRegion(ScrolledForm form) {
		Composite headClient = new Composite(form.getForm().getHead(), SWT.NULL);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 3;
		headClient.setLayout(glayout);
		headClient.setBackgroundMode(SWT.INHERIT_DEFAULT);
		searchLabel = new Label(headClient, SWT.NONE);
		searchLabel.setText("Search:");
		searchBox = toolkit.createText(headClient, "");
		GridData data = new GridData();
		data.widthHint = 300;
		searchBox.setLayoutData(data);
		ToolBar cancelBar = new ToolBar(headClient, SWT.FLAT );
		
		cancelIcon = new ToolItem(cancelBar, SWT.NONE);
		cancelIcon.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				searchBox.setText("");
			}
		});
		cancelIcon.setImage(SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SEARCH_CANCEL));
		toolkit.paintBordersFor(headClient);
		form.setHeadClient(headClient);
		searchBox.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (searchBox.getText().length() > 0) {
					cancelIcon.setEnabled(true);
				} else {
					cancelIcon.setEnabled(false);
				}
			}
		});
		cancelIcon.setEnabled(false);

		form.setText(SpoofaxProject_Dependency_Analysis);
		form.setImage(SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_VIZ_LABEL));
		enableSearchBox(false);
	}



	String createFormTextContent(IMessage[] messages) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("<form>");
		for (int i = 0; i < messages.length; i++) {
			IMessage message = messages[i];
			pw.print("<li vspace=\"false\" style=\"image\" indent=\"16\" value=\"");
			switch (message.getMessageType()) {
			case IMessageProvider.ERROR:
				pw.print("error");
				break;
			case IMessageProvider.WARNING:
				pw.print("warning");
				break;
			case IMessageProvider.INFORMATION:
				pw.print("info");
				break;
			}
			pw.print("\"> <a href=\"");
			pw.print(i + "");
			pw.print("\">");
			if (message.getPrefix() != null) {
				pw.print(message.getPrefix());
			}
			pw.print(message.getMessage());
			pw.println("</a></li>");
		}
		pw.println("</form>");
		pw.flush();
		return sw.toString();
	}

	/**
	 * Creates the sashform to separate the graph from the controls.
	 * 
	 * @param parent
	 */
	private void createSash(Composite parent) {
		sash = new SashForm(parent, SWT.NONE);
		this.toolkit.paintBordersFor(parent);

		createGraphSection(sash);
	}

	private class MyGraphViewer extends GraphViewer {
		public MyGraphViewer(Composite parent, int style) {
			super(parent, style);
			Graph graph = new Graph(parent, style) {
				public Point computeSize(int hint, int hint2, boolean changed) {
					return new Point(0, 0);
				}
			};
			setControl(graph);
		}
	}

	/**
	 * Creates the section of the form where the graph is drawn
	 * 
	 * @param parent
	 */
	private void createGraphSection(Composite parent) {
		Section section = this.toolkit.createSection(parent, Section.TITLE_BAR);
		viewer = new MyGraphViewer(section, SWT.NONE);
		section.setClient(viewer.getControl());
	}

	
	/**
	 * Gets the currentGraphViewern
	 * 
	 * @return
	 */
	public GraphViewer getGraphViewer() {
		return viewer;
	}

	/**
	 * Gets the form we created.
	 */
	public ScrolledForm getForm() {
		return form;
	}

	public ManagedForm getManagedForm() {
		return managedForm;
	}

	public Text getSearchBox() {
		return this.searchBox;
	}

	public void enableSearchBox(boolean enable) {
		this.searchLabel.setEnabled(enable);
		this.searchBox.setEnabled(enable);
	}
}
