/*
Copyright 2004
Universite Libre de Bruxelles
Department of Informatics and Networks- Faculty of Engineering
BioMaze Project
*/

package org.alcatel.jsce.servicecreation.graph.view.action;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;


/**
 * @author Skhiri dit Gabouje Sabri
 * BioMaze, ULB
 * 
 * <p>
 * Export the drawing area (@link org.eclipse.draw2d.Figure) in other format.
 * </p>
 */
public class ExportDrawing {
    private static ExportDrawing instance = null;
    
    ////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////

    /**
     * 
     */
    private ExportDrawing() {
    }
    
    /**
     * @return Returns the instance.
     */
    public static ExportDrawing getInstance() {
        if(instance == null){
            instance = new ExportDrawing();
        }
        return instance;
    }
    
    ////////////////////////////////////////////////////////////
    //
    // Export As JPG
    //
    ////////////////////////////////////////////////////////////
    

    /**
     * Export the drawing as a JPG file.
     */
    public void exportAsJPG() {
       IFigure draw2DFigure = ServiceCreationPlugin.getDefault().getMainControl().getCurrentDrawing();
        if (draw2DFigure.getChildren().size() < 1) {
            MessageDialog.openInformation(null, "Information", "The view is empty!");
        } else {
            if (draw2DFigure != null) {
                Shell _shell = new Shell(
                        new Shell().getDisplay());
                FileDialog dialog = new FileDialog(_shell, SWT.SAVE);
                String extension[] = { "*.jpg" };
                dialog.setText("Save image as");
                dialog.setFilterExtensions(extension);
                String userFilename = dialog.open();
                if(userFilename!=null){
                    StringBuffer fileNameBuffer = new StringBuffer(userFilename);
                    int result = processFilename(fileNameBuffer,"jpg");
                    final String filename = fileNameBuffer.toString();
                    switch (result) {
                    case 1:
                        saveFile(draw2DFigure, filename);
                        break;
                    case 3:
                        saveFile(draw2DFigure, filename);
                        break;
                    case 6:
                        MessageDialog.openError(new Shell(), "SCE-SE Error", " The specified file name is not valid ! \n The image wa not saved");
                        break;
                    case 0:
                        MessageDialog.openError(new Shell(), "SCE-SE Error", " The extension is not jpg ! \n The image wa not saved");
                        break;

                    default:
                        break;
                    }
                }
            } else {
                MessageDialog.openInformation(null, "SCE-SE Information", "The SCE-SE view is not open !");
            }
        }

    }
    
    /**
     * Export the drawing as a JPG file.
     */
    public void exportAsPNG() {
        IFigure draw2DFigure = ServiceCreationPlugin.getDefault().getMainControl().getCurrentDrawing();
        if (draw2DFigure.getChildren().size() < 1) {
            MessageDialog.openInformation(null, "SCE-SE Information", "The SCE-SE view is empty !");
        } else {
            if (draw2DFigure != null) {
                Shell _shell = new Shell(
                        new Shell().getDisplay());
                FileDialog dialog = new FileDialog(_shell, SWT.SAVE);
                String extension[] = { "*.png" };
                dialog.setText("Save image as");
                dialog.setFilterExtensions(extension);
                String userFilename = dialog.open();
                if(userFilename!=null){
                    StringBuffer fileNameBuffer = new StringBuffer(userFilename);
                    int result = processFilename(fileNameBuffer,"png");
                    final String filename = fileNameBuffer.toString();
                    switch (result) {
                    case 1:
                        saveFile(draw2DFigure, filename);
                        break;
                    case 3:
                        saveFile(draw2DFigure, filename);
                        break;
                    case 6:
                        MessageDialog.openError(new Shell(), "SCE-SE Error", " The specified file name is not valid ! \n The image wa not saved");
                        break;
                    case 0:
                        MessageDialog.openError(new Shell(), "SCE-SE Error", " The extension is not jpg ! \n The image wa not saved");
                        break;

                    default:
                        break;
                    }
                }
            } else {
                MessageDialog.openInformation(null, "SCE-SE Information", "The SCE-SE view is not open !");
            }
        }

    }
    
    ////////////////////////////////////////////////////////////
    //
    // Utilities
    //
    ////////////////////////////////////////////////////////////

    /**
     *@param draw2DFigure The figure to save
     *@param filename The path where the figure must be saved
     */
    private void saveFile(IFigure draw2DFigure, final String filename) {
       
        if (filename != null) {
            try {
                Rectangle rectangle = draw2DFigure.getClientArea();
                System.out.println("Size of client area before : " + rectangle);
                
                //IFigure copy = copy(draw2DFigure);
                
//                VScalableFigure drawing = new VScalableFigure();
//                drawing.setContent(copy);
//                drawing.setFactor(4);
//                drawing.lessZoom();
//                
//                rectangle = drawing.getClientArea();
//                System.out.println("Size of client area after: " + rectangle);
                
                
                final Image image = new Image(null, rectangle.width,
                        rectangle.height);
                GC gc = new GC(image);
                
                
                
                
                Graphics graphics = new SWTGraphics(gc);
                graphics.translate(draw2DFigure.getBounds().getLocation().getNegated());
                draw2DFigure.paint(graphics);
                
//                graphics.translate(drawing.getBounds().getLocation().getNegated());
//                drawing.paint(graphics);
//                
                graphics.dispose();
                gc.dispose();
                IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
                progressService.runInUI(PlatformUI.getWorkbench().getProgressService(), new IRunnableWithProgress() {
                    public void run(IProgressMonitor monitor) {
                        try {
                            monitor.setTaskName("Save image in " + filename);
                            monitor.subTask(" Get image Data ..");
                            ImageData data = image.getImageData();
                            ImageLoader loader = new ImageLoader();
                            monitor.subTask(" Get Load into Image loader ..");
                            loader.data = new ImageData[1];
                            loader.data[0] = data;
                            System.out.println(" data lenght : =" + data.width
                                    + "  , " + data.height);
                            monitor.subTask(" Save Image ..");
                            loader.save(filename, SWT.IMAGE_JPEG);
                            monitor.done();
                        } catch (OutOfMemoryError e) {
                            MessageDialog.openError(new Shell(), "SCE-SE Error", " Sorry: an Out of Memory was caugth");
                        }
                    }
                }, null);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                MessageDialog.openError(new Shell(), "SCE-SE Error", " Sorry: an Out of Memory was caugth");
            }
        } else {
            MessageDialog.openError(new Shell(), "SCE-SE Error", " The specified file name is not valid ! \n The image wa not saved");
        }
    }

    /**
     *@param draw2DFigure
     *@return 
     */
    /*private Figure copy(Figure draw2DFigure) {
      Figure copy = new Figure(); 
      List childs = GraphOperation.getInstance().copyToList(draw2DFigure.getChildren());
      for (Iterator iter = childs.iterator(); iter.hasNext();) {
        Figure child = (Figure) iter.next();
        Object constraint = null;
        if(draw2DFigure.getLayoutManager()!=null){
            constraint = draw2DFigure.getLayoutManager().getConstraint(child);
        }
        List child_i = child.getChildren();
        if(child_i.size() >0){
            Figure copyChild = copy(child);
            copy.add(copyChild, constraint);
        }else{
            copy.add(child, constraint);
        } 
    }
      return copy;
    }*/

    /**
     * @param userFilename
     * @param allowedExt 
     * @return
     */
    private int processFilename(StringBuffer userFilename, String allowedExt) {
        if (userFilename != null) {
            if (userFilename.length() > 5) {
                char dot = userFilename.charAt(userFilename.length() - 4);
                if (dot == '.') {
                    String ext = userFilename.substring(userFilename.length() - 3);
                    if (ext.equals(allowedExt)) {
                        return 1;
                    } else {
                        // MessageDialog.openError(new Shell(),
                        // "SCE-SE Error", " Please enter the \".jpg\"
                        // extension");
                        return 0;
                    }
                } else {
                    userFilename = userFilename.append("."+allowedExt);
                    return 3;
                }

            } else {
                if (userFilename.length() > 0) {
                    userFilename = userFilename.append("."+allowedExt);
                    return 3;
                }
                return 3;
            }
        } else {
            /* File name null; */
            return 6;
        }

    }

}
