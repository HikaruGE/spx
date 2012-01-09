package org.strategoxt.imp.editors.spoofax.ui.providers;

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.strategoxt.imp.editors.spoofax.ui.SpoofaxlangDependencyViewImages;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxModuleDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxPackageDescriptor;
import org.strategoxt.imp.editors.spoofax.ui.model.SpxProjectDependencyGraph;

/**
 * @author Md. Adil Akhter <md.adilakhter add gmail.com>
 *
 */
public class SpxDependencyLabelProvider implements ISpxVisualizationLabelProvider, IConnectionStyleProvider, IEntityStyleProvider,ISelfStyleProvider {


	public Color GRAY = new Color(Display.getDefault(), 128, 128, 128);
	public Color LIGHT_GRAY = new Color(Display.getDefault(), 220, 220, 220);
	public Color BLACK = new Color(Display.getDefault(), 0, 0, 0);
	public Color DARK_RED = new Color(Display.getDefault(), 127, 0, 0);
	public Color DISABLED = new Color(Display.getDefault(), 230, 240, 250);
	public Color LIGHT_BLUE = new Color(Display.getDefault(), 216, 228, 248);
	
	private HashSet interestingRelationships = new HashSet();
	private HashSet interestingDependencies = new HashSet();
	private Color disabledColor = null;
	private Object selected = null;
	private GraphViewer viewer;
	
	private Color rootColor = null;
	private Color rootSelectedColor = null;
	private boolean reverseDependencies = false;

	
	public SpxDependencyLabelProvider(GraphViewer viewer) {
		this.viewer = viewer;
	}

	public Image getImage(Object element) {
		/*
		 * PDELabelProvider fSharedProvider =
		 * PDEPlugin.getDefault().getLabelProvider(); return
		 * fSharedProvider.get(PDEPluginImages.DESC_PLUGIN_OBJ, 0);
		 */
		if (element.getClass() == EntityConnectionData.class) {
			return null;
		}
		if( element instanceof SpxPackageDescriptor ){
			if (((SpxPackageDescriptor)element).getEnclosedModules().size() > 0 )
				return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_PACKAGE);
			else
				return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_EMPTY_PACKAGE);
		}
		
		if( element instanceof SpxModuleDescriptor){
			return SpoofaxlangDependencyViewImages.get(SpoofaxlangDependencyViewImages.IMG_SPX_MODULE);
		}
		
		return null;
	}

	public String getText(Object element) {
		
		if( element instanceof SpxPackageDescriptor){
			return ((SpxPackageDescriptor)element).getPackageName();
		}
		
		if( element instanceof SpxModuleDescriptor){
			return ((SpxModuleDescriptor)element).getModuleName();
		}	
		
		return "";
	}

	public void addListener(ILabelProviderListener listener) {

	}

	public boolean isLabelProperty(Object element, String property) {

		return false;
	}

	public void removeListener(ILabelProviderListener listener) {

	}

	public Color getColor(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return DARK_RED;
		}
		return LIGHT_GRAY;
	}

	public int getConnectionStyle(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return ZestStyles.CONNECTIONS_DASH | ZestStyles.CONNECTIONS_DIRECTED;
		}
		return ZestStyles.CONNECTIONS_DIRECTED;
	}

	public Color getHighlightColor(Object rel) {
		return DARK_RED;
	}

	public Color getNodeHighlightColor(Object entity) {
		return null;
	}

	public int getLineWidth(Object rel) {
		if (interestingRelationships.contains(rel)) {
			return 1;
		}
		return 1;
	}

	public Color getAdjacentEntityHighlightColor(Object entity) {
		return null;
	}

	public Color getBorderColor(Object entity) {
		if (this.selected != null ) {
			if (entity == this.selected ) {
				// If this is the selected node return no colour. The default
				// selected colour is fine.
				return BLACK;
			} else if (interestingDependencies.contains(entity)) {
				// If this entity is directly connected to the selected entity
				return BLACK;
			} else {
				return LIGHT_GRAY;
			}
		}
		return BLACK;
	}

	public Color getBorderHighlightColor(Object entity) {
		return null;
	}

	public int getBorderWidth(Object entity) {
		return 0;
	}

	public boolean highlightAdjacentEntities(Object entity) {
		return false;
	}

	public Color getBackgroundColour(Object entity) {
		
		if (entity == this.selected ) {
			return viewer.getGraphControl().DEFAULT_NODE_COLOR;
		} else if (interestingDependencies.contains(entity)) {
			return viewer.getGraphControl().HIGHLIGHT_ADJACENT_COLOR;
		}
		else {
			return getDisabledColor();
		}
	}

	public Color getForegroundColour(Object entity) {
		if (this.selected != null ) {
			if (entity == this.selected ) {
				// If this is the selected node return no colour. The default
				// selected colour is fine.
				return BLACK;
			} else if (interestingDependencies.contains(entity)) {
				// If this entity is directly connected to the selected entity
				return BLACK;
			}
			else if( entity instanceof SpxModuleDescriptor){
				return BLACK;
			}
			else {
				return GRAY;
			}
		}
		return BLACK;
	}

	protected Object getSelected() {
		return selected;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.mylyn.zest.core.viewers.IEntityStyleProvider#fisheyeNode(java.lang.Object)
	 */
	public boolean fisheyeNode(Object entity) {
		if(entity instanceof SpxModuleDescriptor)
			return true;
		return false;
	}
	
	
	/**
	 * Sets the current selection
	 * 
	 * @param previouslySelectedItem
	 * @param currentSelection
	 */
	public void setCurrentSelection(SpxProjectDependencyGraph g,  Object currentSelection) {
		if (currentSelection instanceof SpxPackageDescriptor){
			for (Iterator iter = interestingRelationships.iterator(); iter.hasNext();) {
				EntityConnectionData entityConnectionData = (EntityConnectionData) iter.next();
				viewer.unReveal(entityConnectionData);
			}

			this.selected = null;

			this.selected = currentSelection;

			interestingRelationships = new HashSet();
			interestingDependencies = new HashSet();
			if (this.selected != null) {
				calculateInterestingDependencies(g , interestingRelationships, interestingDependencies);
			}

			Object[] connections = viewer.getConnectionElements();
			for (Iterator iter = interestingRelationships.iterator(); iter.hasNext();) {
				Object entityConnectionData = iter.next();
				viewer.reveal(entityConnectionData);
			}

			for (int i = 0; i < connections.length; i++) {
				viewer.update(connections[i], null);
			}
		}
	}

	/**
	 * Calculate all the interesting dependencies. Create an instance of this
	 * method to calculate the entities and relationships that should
	 * "stand-out" for this view.
	 * 
	 * @param interestingRels
	 * @param interestingEntities
	 */
	protected void calculateInterestingDependencies(SpxProjectDependencyGraph g,   HashSet interestingRels, HashSet interestingEntities){
		Object[] descriptors = null; 
		if (getSelected() != null) {
			if (!isReverseDependencies()) {
				descriptors  = g.getDependency(this.selected).toArray();
				for (int i = 0; i < descriptors.length; i++) {
					EntityConnectionData entityConnectionData = new EntityConnectionData(this.getSelected(), descriptors[i]);
					interestingRels.add(entityConnectionData);
					interestingEntities.add(descriptors[i]);
				}
				
			} else  {
				
				descriptors  = g.getDependant(this.selected).toArray();
				for (int i = 0; i < descriptors.length; i++) {
					EntityConnectionData entityConnectionData = new EntityConnectionData( descriptors[i], this.getSelected());
					interestingRels.add(entityConnectionData);
					interestingEntities.add(descriptors[i]);
				}
			}
		}
	}

	public Object[] getInterestingRelationships() {
		return interestingRelationships.toArray();
	}

	/**
	 * Creates a colour for disabled lines.
	 * 
	 * @return
	 */
	private Color getDisabledColor() {
		if (disabledColor == null) {
			disabledColor = new Color(Display.getDefault(), new RGB(225, 238, 255));
		}
		return disabledColor;
	}

	public IFigure getTooltip(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
		if (this.disabledColor != null) {
			this.disabledColor.dispose();
			this.disabledColor = null;
		}
		if ( this.rootColor != null) {
			this.rootColor.dispose();
			this.rootColor = null;
		}
		if ( this.rootSelectedColor != null) {
			this.rootSelectedColor.dispose();
			this.rootSelectedColor = null;
		}
	}

	public IFigure getNodeFigure(Object entity) {
		return null;
	}
	
	public void setReverseDependencies(boolean reverseBundleDependencies) {
		this.reverseDependencies  = reverseBundleDependencies;
	}

	public boolean isReverseDependencies() {
		return reverseDependencies;
	}

	public void selfStyleConnection(Object element, GraphConnection connection) {
		// TODO Auto-generated method stub
		
	}

	public void selfStyleNode(Object element, GraphNode node) {
		if (node instanceof GraphContainer)
		{	
			int style = LayoutStyles.NO_LAYOUT_NODE_RESIZING;
			LayoutAlgorithm l =  new CompositeLayoutAlgorithm(style,
							new LayoutAlgorithm[]{
						 	new VerticalLayoutAlgorithm(style),
							new HorizontalShift(style)
						});
				 
			GraphContainer c  = (GraphContainer)node;
			c.setLayoutAlgorithm(l, true);
		
		}
		
	}

	
	
}