
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.servicecreation.ui;

import java.io.File;
import java.util.List;

import org.alcatel.jsce.interfaces.com.IPageAdaptor;
import org.alcatel.jsce.util.image.ImageManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 *  Description:
 * <p>
 * Generic dialog allowing to re-use the wizard page inside a dialog (whit error validation).
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public abstract class GenericOspDialogContent extends Composite implements IPageAdaptor {
	/** The name of icon relative to the icon dir.*/
	private String  iconName = null;
	/** The title name*/
	private String title = null;
	/** the Height of the content composite*/
	private int h = 10;
	private int w =10;
	
	private Label messageLabel = null;
	private Label errorIconLabel = null;
	private Image erroImage = null;
	private  Font font =null;
	private Label titleLabel = null;
	private Label titleImgLabel = null;
	private Image titleImage= null;
	private Composite contentComposite = null;
	private boolean pagecomplete = true;


	/**
	 * @param parent
	 * @param style
	 */
	public GenericOspDialogContent(Composite parent, int style, String icon, String title,int compositeW, int compositeH) {
		super(parent, style);
		this.iconName = icon;
		this.title = title;
		this.h = compositeH;
		this.w = compositeW;
		initialize();
	}

	private void initialize() {
		setSize(new org.eclipse.swt.graphics.Point(w+300,h+500));
		messageLabel = new Label(this, SWT.NONE);
		messageLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(57,100+h,551,13));
		messageLabel.setText("");
		errorIconLabel = new Label(this, SWT.NONE);
		errorIconLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(23,100+h,25,13));
		titleLabel = new Label(this, SWT.NONE);
		int size = title.length()*11;
		titleLabel.setText(title);
		titleLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(7,6,size,49));
		titleImgLabel = new Label(this, SWT.NONE);
		titleImgLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(size+15,0,74,58));
		createContentComposite();
		ImageDescriptor titleD = ImageManager.getInstance().getImgeDescriptor(this.iconName);
		titleImage = titleD.createImage();
		titleImgLabel.setImage(titleImage);
		FontData newFont = new FontData("Arial", 16, SWT.BOLD);
		font = new Font(getDisplay(), newFont);
		titleLabel.setFont(font);
		ImageDescriptor errorD = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_ERROR_TSK);
		erroImage = errorD.createImage();
		errorIconLabel.setImage(erroImage);
		setErrorMessage(null);
	}
	

	/**
	 * @param externalDir the given directory
	 * @param subfolders the output of the method, with all subfolder of the given directoty
	 */
	private void getSubFolders(File externalDir, List subfolders) {
		File [] subChildren = externalDir.listFiles();
		for (int i = 0; i < subChildren.length; i++) {
			File file = subChildren[i];
			if(file.isDirectory()){
				subfolders.add(file.getPath());
				getSubFolders(file, subfolders);
			}
		}
		
	}

	private void setErrorImage(boolean show) {
		if (show) {
			if (erroImage != null) {
				errorIconLabel.setImage(erroImage);
			} else {
				ImageDescriptor errorD = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_OBJS_ERROR_TSK);
				erroImage = errorD.createImage();
				errorIconLabel.setImage(erroImage);
			}
		} else {
			errorIconLabel.setImage(null);
		}

	}


	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		if(erroImage!=null){
			erroImage.dispose();
		}
		if(font!=null){
			font.dispose();
		}
		if(titleImage!=null){
			titleImage.dispose();
		}
		super.dispose();
	}

	
	private Composite createContentComposite() {
		contentComposite = new Composite(this, SWT.NONE);
		contentComposite.setBounds(new org.eclipse.swt.graphics.Rectangle(14,73,w,h));
		createContentAreaComposite();
		return contentComposite;
	}
	
	/**
	 * This method initializes contentComposite area, the client should call:
	 * <code>
	 * 	protected Composite createContentAreaComposite() {
	 * 		Coposite contentComposite = getContentComposite()
	 * 		//add your stuff
	 * 		return contentComposite;
	 * 	}
	 * </code>
	 *
	 */
	public abstract Composite createContentAreaComposite();
	
	/**
	 * @see org.alcatel.jsce.interfaces.com.IPageAdaptor#setErrorMessage(java.lang.String)
	 */
	public void setErrorMessage(String msg) {
		if(msg!=null){
			this.messageLabel.setText(msg);
			this.setErrorImage(true);
		}else{
			this.messageLabel.setText("");
			this.setErrorImage(false);
		}
		
	}

	/**
	 * @see org.alcatel.jsce.interfaces.com.IPageAdaptor#setPageComplete(boolean)
	 */
	public void setPageComplete(boolean complete) {
		this.pagecomplete = false;
	}

	/**
	 * @return Returns the pagecomplete.
	 */
	public boolean isPagecomplete() {
		return pagecomplete;
	}

	public Composite getContentComposite() {
		return contentComposite;
	}
	
	

}
