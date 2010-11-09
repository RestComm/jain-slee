package org.mobicents.eclipslee.servicecreation.popup.actions;

import org.eclipse.core.resources.IProject;
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


/**
 * @author ammendonca
 */
public class AddModuleSubmenu implements IObjectActionDelegate, IMenuCreator {
	
	public AddModuleSubmenu() {
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
		createModulesMenus(menu);
	}
	
	private void createModulesMenus(Menu parent) {
		
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
		
		// project selected.
		if (obj instanceof IProject) {
			try {
        MenuItem eventsItem = new MenuItem(parent, SWT.NONE);
        eventsItem.setText("Events");
        eventsItem.setData("events");
        eventsItem.addSelectionListener(new AddSelectionListener());
        MenuItem sbbItem = new MenuItem(parent, SWT.NONE);
        sbbItem.setText("Service Building Block (SBB)");
        sbbItem.setData("sbb");
        sbbItem.addSelectionListener(new AddSelectionListener());
        MenuItem profileSpecItem = new MenuItem(parent, SWT.NONE);
        profileSpecItem.setText("Profile Specification");
        profileSpecItem.setData("profile-spec");
        profileSpecItem.addSelectionListener(new AddSelectionListener());
        MenuItem raTypeItem = new MenuItem(parent, SWT.NONE);
        raTypeItem.setText("Resource Adaptor Type");
        raTypeItem.setData("ratype");
        raTypeItem.addSelectionListener(new AddSelectionListener());
        MenuItem raItem = new MenuItem(parent, SWT.NONE);
        raItem.setText("Resource Adaptor");
        raItem.setData("ra");
        raItem.addSelectionListener(new AddSelectionListener());
        MenuItem libraryIitem = new MenuItem(parent, SWT.NONE);
        libraryIitem.setText("Library");
        libraryIitem.setData("library");
        libraryIitem.addSelectionListener(new AddSelectionListener());
			}
			catch (Exception e) {
				ServiceCreationPlugin.log("Exception caught creating menu: " + e.getMessage());
			}
		}
	}
	
	private class AddSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			MenuItem item = (MenuItem) e.getSource();
			
			AddModuleAction action = new AddModuleAction((String) item.getData());
			action.selectionChanged(null, selection);
			action.run(null);			
		}	
	}
	
	private IAction fDelegateAction;
	private ISelection selection;
	private boolean fFillMenu;
}