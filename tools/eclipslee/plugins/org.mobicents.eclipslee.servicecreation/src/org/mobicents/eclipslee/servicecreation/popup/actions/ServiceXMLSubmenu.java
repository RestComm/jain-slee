/**
 *   Copyright 2005 Open Cloud Ltd.
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

package org.mobicents.eclipslee.servicecreation.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.util.slee.xml.components.Service;
import org.mobicents.eclipslee.xml.ServiceXML;


/**
 * @author cath
 */
public class ServiceXMLSubmenu implements IObjectActionDelegate, IMenuCreator {
	
	public ServiceXMLSubmenu() {
		super();
	}
	
	public void run(IAction action) {
	}
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
	
	public void dispose() {	
	}
	
	public void selectionChanged(IAction action, ISelection selection) {
		
		if (selection instanceof IStructuredSelection) {
			
			fFillMenu = true;
			if (action != null) {
				if (fDelegateAction != action) {
					fDelegateAction = action;
					fDelegateAction.setMenuCreator(this);
				}
				
				this.selection = selection;
				action.setEnabled(true);
				return;				
			}
			return;		
		}
		action.setEnabled(false);	
	}
	
	public Menu getMenu(Control control) { return null; } // NOP
	
	public Menu getMenu(Menu parent) {
		Menu menu = new Menu(parent);
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent e) {
				if (fFillMenu) {
					Menu m = (Menu) e.widget;
					MenuItem items[] = m.getItems();
					for (int i= 0; i < items.length; i++)
						items[i].dispose();
					fillMenu(m);
					fFillMenu = false;					
				}
			}		
		});
		
		return menu;
	}
	
	private void fillMenu(Menu menu) {
		createServiceMenus(menu);
	}
	
	private void createServiceMenus(Menu parent) {
		
		if (selection == null && selection.isEmpty()) {
			return;
		}
		
		if (!(selection instanceof IStructuredSelection)) {
			return;			
		}
		
		IStructuredSelection ssel = (IStructuredSelection) selection;
		if (ssel.size() > 1) {
			return;
		}
		
		// Get the first (and only) item in the selection.
		Object obj = ssel.getFirstElement();
		
		// .java file selected.
		if (obj instanceof IFile) {
			try {
				ServiceXML serviceXML = new ServiceXML((IFile) obj);
				Service services[] = serviceXML.getServices();
				
				for (int i = 0; i < services.length; i++) {
					
					Menu child = new Menu(parent);
					MenuItem item = new MenuItem(parent, SWT.CASCADE);
					item.setText(services[i].getName() + "," + services[i].getVersion() + "," + services[i].getVendor());
					item.setMenu(child);
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Identity...");
					item.addSelectionListener(new IdentitySelectionListener());

					item = new MenuItem(child, SWT.NONE);
					item.setText("Root SBB...");
					item.addSelectionListener(new RootSbbSelectionListener());
					
					item = new MenuItem(child, SWT.SEPARATOR);					
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Delete");
					item.addSelectionListener(new DeleteSelectionListener());
					
				}
			} catch (Exception e) {
				ServiceCreationPlugin.log("Exception caught creating menu: " + e.getMessage());
			}
			
		}
		
	}
	
	private class IdentitySelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditServiceIdentityAction action = new EditServiceIdentityAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}

	private class RootSbbSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditServiceRootSbbAction action = new EditServiceRootSbbAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}

	private class DeleteSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			
			DeleteServiceAction action = new DeleteServiceAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}	
	}
	
	private IAction fDelegateAction;
	private ISelection selection;
	private boolean fFillMenu;
}