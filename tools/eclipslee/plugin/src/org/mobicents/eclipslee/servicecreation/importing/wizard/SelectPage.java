/**
 * 
 */
package org.mobicents.eclipslee.servicecreation.importing.wizard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.dialogs.WizardResourceImportPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FileSystemElement;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.dialogs.IElementFilter;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.IDataTransferHelpContextIds;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
/**
 * @author Paolo
 * @deprecated This class is obsolete
 */
public class SelectPage extends WizardPage implements ISleeImportPage {
	
	public SelectPage(String name){
		super(name);
	}
    
	
    /* (non-Javadoc)
     * Method declared on IDialogPage.
     */
    public void createControl(Composite parent) {
    	initializeDialogUnits(parent);
    	
    	// Create the extern composite ad set his layout 
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());
        GridLayout pageLayout = new GridLayout();
        pageLayout.numColumns = 2;
        composite.setLayout(pageLayout);
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING
                | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        setTitle("Jain SLEE Components");
        setDescription("Import Jain SLEE Components, created externally from the EclipSLEE plug-in, in a SLEE project");
        
        // Create the label and the combo
        createSourceGroup(composite);
        setControl(sourceTypeCombo);
        
        restoreWidgetValues();
        comboValidate();
        /// TODO Add help topic
        //PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IDataTransferHelpContextIds.FILE_SYSTEM_IMPORT_WIZARD_PAGE);
    }
    
    /**
     *	Create the options specification widgets.
     *
     *	@param parent org.eclipse.swt.widgets.Composite
     */    
    protected void createSourceGroup(Composite parent) {
    	sourceTypeLabel = new Label(parent, SWT.SHADOW_NONE | SWT.LEFT);
    	sourceTypeLabel.setText("The Jain SLEE Components are to be imported from ");
    	GridData sourceTypeLabelGD = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
    	sourceTypeLabel.setLayoutData(sourceTypeLabelGD);
    	
    	sourceTypeCombo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN); 
    	sourceTypeCombo.add("folder in file system");
    	sourceTypeCombo.add("JAR file");
    	sourceTypeCombo.deselectAll();
    	
    	GridData sourceComboGD = new GridData (GridData.HORIZONTAL_ALIGN_END, GridData.VERTICAL_ALIGN_BEGINNING);
    	sourceTypeCombo.setLayoutData(sourceComboGD); 
    	
    	sourceTypeCombo.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent e) {
				comboValidate();
			}
    	}
    	);
    }
    
    /**
     *	The Finish button was pressed.  Try to do the required work now and answer
     *	a boolean indicating success.  If false is returned then the wizard will
     *	not close.
     *
     * @return boolean
     */
    public boolean finish() {
    	
    //	TODO Return false, no Finish
         return true;
    }
   
    /**
     *	Use the dialog store to restore widget values to the values that they held
     *	last time this wizard was used to completion
     */
    private void restoreWidgetValues() {
    	IDialogSettings settings = getDialogSettings().getSection(getName());
        if (settings == null) return;
        
        try {
			sourceTypeCombo.select(settings.getInt(COMBO_ID));
		} catch (NumberFormatException e) {
			return;
		}
    }

    /**
     * 	Since Finish was pressed, write widget values to the dialog store so that they
     *	will persist into the next invocation of this wizard page
     */
    public void saveWidgetValues() {   
        IDialogSettings settings = getDialogSettings().getSection(getName());
        if (settings == null) settings = getDialogSettings().addNewSection(getName());
        settings.put(COMBO_ID, sourceTypeCombo.getSelectionIndex());
        }
 
    
    private void comboValidate() {
    	switch (sourceTypeCombo.getSelectionIndex()) {
    	case -1:{
    		// No selection
    		updateStatus("Select the source type for the externally created Jain SLEE Components");
    		break;
    		}
    	case 0:{
    		// File System
    		// TODO aggiungere pagina dinamica
    		updateStatus(null);
    		saveWidgetValues();
    		break;
    		}
    	case 1:{
    		// JAR file
    		// TODO aggiungere pagina dinamica
    		updateStatus(null);
    		saveWidgetValues();
    		break;
    		}
    	}
    }
    
    private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}
    
    protected Combo sourceTypeCombo;
    protected Label sourceTypeLabel;
    protected static String COMBO_ID = "SELECT_PAGE_COMBO";
}


