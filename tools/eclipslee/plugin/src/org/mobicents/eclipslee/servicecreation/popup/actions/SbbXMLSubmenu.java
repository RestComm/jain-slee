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
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class SbbXMLSubmenu implements IObjectActionDelegate, IMenuCreator {
	
	public SbbXMLSubmenu() {
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
		createEventMenus(menu);
	}
	
	private void createEventMenus(Menu parent) {
		
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
		
		// .xml menu
		if (obj instanceof IFile) {
			try {
				
				SbbJarXML sbbJarXML = new SbbJarXML((IFile) obj);
				SbbXML sbbs[] = sbbJarXML.getSbbs();
				
				for (int i = 0; i < sbbs.length; i++) {
					
					Menu child = new Menu(parent);
					MenuItem item = new MenuItem(parent, SWT.CASCADE);
					item.setText(sbbs[i].getName() + "," + sbbs[i].getVersion() + "," + sbbs[i].getVendor());
					item.setMenu(child);
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Identity...");
					item.addSelectionListener(new IdentitySelectionListener());
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Classes...");
					item.addSelectionListener(new ClassesSelectionListener());
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("CMP Fields...");
					item.addSelectionListener(new CMPSelectionListener());
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Usage Parameters...");
					item.addSelectionListener(new UsageSelectionListener());
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Events...");
					item.addSelectionListener(new EventsSelectionListener());
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Profiles...");
					item.addSelectionListener(new ProfileSelectionListener());
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Children...");
					item.addSelectionListener(new ChildrenSelectionListener());
		
					item = new MenuItem(child, SWT.NONE);
					item.setText("Resource Adaptor Bindings...");
					item.addSelectionListener(new RABindingSelectionListener());
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Environment Entries...");
					item.addSelectionListener(new EnvEntrySelectionListener());
										
					item = new MenuItem(child, SWT.SEPARATOR);					
					
					item = new MenuItem(child, SWT.NONE);
					item.setText("Delete");
					item.addSelectionListener(new DeleteSelectionListener());
					
				}
			} catch (Exception e) {
				System.err.println("Exception caught creating menu: " + e.getMessage());
			}
			
		}
		
	}

	private class ProfileSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditSbbProfileAction action = new EditSbbProfileAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class EventsSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditSbbEventsAction action = new EditSbbEventsAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class ClassesSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditSbbClassesAction action = new EditSbbClassesAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class ChildrenSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditSbbChildAction action = new EditSbbChildAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class UsageSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditSbbUsageAction action = new EditSbbUsageAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class CMPSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditSbbCMPAction action = new EditSbbCMPAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class IdentitySelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();
			EditSbbIdentityAction action = new EditSbbIdentityAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class EnvEntrySelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			EditSbbEnvEntryAction action = new EditSbbEnvEntryAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}
	}
	private class DeleteSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			
			DeleteSbbAction action = new DeleteSbbAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}	
	}
	private class RABindingSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			Menu parent = item.getParent();
			MenuItem parentItem = parent.getParentItem();			
			
			EditSbbResourceAdaptorBindingsAction action = new EditSbbResourceAdaptorBindingsAction(parentItem.getText());
			action.selectionChanged(null, selection);
			action.run(null);			
		}	
	}
	
	private IAction fDelegateAction;
	private ISelection selection;
	private boolean fFillMenu;
	
}
