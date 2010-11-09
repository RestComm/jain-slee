
/**
 *   Copyright 2006 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.servicecreation.graph.view;

import java.util.ArrayList;
import java.util.List;

import org.alcatel.jsce.servicecreation.graph.component.figure.AreaSelecter;
import org.alcatel.jsce.servicecreation.graph.component.figure.ColorManager;
import org.alcatel.jsce.servicecreation.graph.component.figure.GlyphBox;
import org.alcatel.jsce.servicecreation.graph.component.figure.IGlyph;
import org.alcatel.jsce.util.image.ImageManager;
import org.alcatel.jsce.servicecreation.graph.view.action.SaveDrawingAction;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 *  Description:
 * <p>
 * A view allowing to draw sbb graph dependencies.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class GraphDrawingViewer extends ViewPart {
	/** Define if the view was already initialized*/
	private boolean initialized = false;
	/**The canvas between the SBT parent and the drawing*/
	private FigureCanvas canvas= null;
	/** The action to export the drawing as png*/
	private  SaveDrawingAction exportAsImageAction = null;
	/**The graph selecter*/
	private AreaSelecter areaSelecter = null;
	/** The glyph box used as selection*/
	private GlyphBox selection = null;
	/** The selection dragger*/
	private SelectionDragger selectionDragger = null;
	/** The current Drawing*/
	private IFigure currentDrawing = null;
	/** The list of selected nodes*/
	private List selectedNodes = null;
	private boolean presentSelecter = false;
	private boolean actingSelection = false;
	
	/**
	 * 
	 */
	public GraphDrawingViewer() {
		super();
		selectedNodes = new ArrayList();
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		if(!initialized){
			canvas = new FigureCanvas(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			LightweightSystem lws = new LightweightSystem(canvas);
			canvas.setViewport(new Viewport(true));
			canvas.setScrollBarVisibility(FigureCanvas.ALWAYS);
			//The drawing is stored in the Main control by the graph manager
			canvas.setContents(ServiceCreationPlugin.getDefault().getMainControl().getCurrentDrawing());
			lws.setContents(canvas.getViewport());
			initialized = true;
			createAction();
			createToolBarButton();
			hookSlectionListener();
			this.currentDrawing = ServiceCreationPlugin.getDefault().getMainControl().getCurrentDrawing();
			hookGraphSelectionProvider();
			
		}
		

	}

	/**
	 * Initializes the current drawing
	 * @param currentDrawing
	 */
	private void hookGraphSelectionProvider() {
		  /*Area selecter*/
        areaSelecter = new AreaSelecter(this, currentDrawing);
        selection = new GlyphBox(1, 1, 0, 0, null, null);
        selection.setOpaque(false);
        selection.setBorderEffect(IGlyph.DOTED);
        selection.setBorderColor(ColorManager.getInstance().getColor(IGlyph.COLOR_GRAY));
        selectionDragger = new SelectionDragger(selection,this,  currentDrawing);
        /*Area selecter - Fin*/
		
	}

	/**
	 * Register a selection listener.
	 */
	private void hookSlectionListener() {
		getSite().getPage().addPostSelectionListener(ServiceCreationPlugin.getDefault().getMainControl().getSelectionListener());
		
	}
	
	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		if(initialized){
			getSite().getPage().removePostSelectionListener(ServiceCreationPlugin.getDefault().getMainControl().getSelectionListener());
		}
		super.dispose();
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}
	
	///////////////////////////////////////////
	//
	// Init
	//
	//////////////////////////////////////////
	
	/**
     * Init the context menu.
     */
    private void createAction() {
       ImageDescriptor save = ImageManager.getInstance().getImgeDescriptor("alcatel/save25x25.png");
       exportAsImageAction = new SaveDrawingAction("Export drawing", save);
       exportAsImageAction.setToolTipText("Export drawing as PNG");
     
        
    }
    
    /**
     * Init toolBar button
     */
    private void createToolBarButton() {
       getViewSite().getActionBars().getToolBarManager().add(exportAsImageAction);
       exportAsImageAction.setEnabled(true);
       getViewSite().getActionBars().updateActionBars();
        
    }
    
    ////////////////////////////////////////////////////////////
    //
    // Selection Management
    //
    ////////////////////////////////////////////////////////////

    public void addSelecter(int x, int y, int h, int w) {
        selection.setBounds(new Rectangle(x, y, w, h));
        this.currentDrawing.add(selection, 0);
        presentSelecter  = true;
    }

    public void updateSelecter(int x, int y, int h, int w) {
        if (this.currentDrawing.getChildren().contains(selection)) {
        	this.currentDrawing.remove(selection);
        }
        selection.setBounds(new Rectangle(x, y, w, h));
        this.currentDrawing.add(selection);
    }

    public void removeSelecter() {
        if (this.currentDrawing.getChildren().contains(selection)) {
        	this.currentDrawing.remove(selection);
            presentSelecter = false;
        }

    }

    public boolean isActingSelection() {
        return actingSelection ;
    }

    public void setActingSelection(boolean actingSelection) {
        this.actingSelection = actingSelection;
    }

    public boolean isPresentSelecter() {
        return presentSelecter;
    }

    /**
     * Recieve selection area and select nodes inside.
     */
    public void sendSelectedArea() {
		this.selectedNodes = ServiceCreationPlugin.getDefault().getMainControl().getNodeInstersected(
				this.currentDrawing.getChildren(), selection.getBounds());
		setSelctionArea(selectedNodes);
		/* Fire property (selected Nodes) */
		// Todo fire an event that say: selection changed fireGraphicSelectionChange(new VBMSelectionEvent(
		// selectedNodes, this));

	}

    /**
     * Select the specified nodes.
     *@param selectedNodes 
     */
    private void setSelctionArea(List selectedNodes) {
        removeCurrentSelection();
        /*Set selection to the dragger selection, toggle selcted involved figures*/
        //1. Set the current selection (figure) to the selection dragger. After he will be able to drag them.
        selectionDragger.setCurrentSelection(ServiceCreationPlugin.getDefault().getMainControl().getFiguresFromNodes(selectedNodes));
        //this.currentDrawing.add(selection);
        selectionDragger.setActive();
    }

	private void removeCurrentSelection() {
		//this.selectedNodes.clear();
		if (selectionDragger != null) {
			if(currentDrawing.getChildren().contains(selection))
				this.currentDrawing.remove(selection);
			selectionDragger.setNotActive();
			selectionDragger.removeSelection();
			this.currentDrawing.repaint(selection.getBounds());
		}
	}
	
	   /**
     * @param point
     * @return true if the point is in the selection area.
     * 
     */
    public boolean isPressedInSelecter(Point point) {
    	boolean in = selectionDragger.isPressedInSelection(point);
        if (!in) {
            setActingSelection(false);
            removeCurrentSelection();
            removeSelecter();
        }
        return in;
    }
    
    /**
     * Remove only the current selection.
     */
    public void removeGraphicSelection() {
    	this.selectedNodes.clear();
        if (selectionDragger != null) {
        	selectionDragger.setNotActive();
        	selectionDragger.removeSelection();
        }
        this.currentDrawing.repaint(selection.getBounds());
    }

}
