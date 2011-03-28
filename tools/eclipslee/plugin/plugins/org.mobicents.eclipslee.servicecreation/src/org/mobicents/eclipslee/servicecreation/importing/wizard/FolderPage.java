package org.mobicents.eclipslee.servicecreation.importing.wizard;
/*******************************************************************************
 * @author Paolo Costa
 *
 *******************************************************************************/

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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FileSystemElement;
import org.eclipse.ui.dialogs.WizardResourceImportPage;
import org.eclipse.ui.internal.ide.dialogs.IElementFilter;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.IDataTransferHelpContextIds;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.IImportStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;

/**
 *	Page 1 of the wizard to import SLEE components 
 *	from a external directory to the project	
 */
public class FolderPage extends SLEEImportPage {
    
    private final static String STORE_SOURCE_NAMES_ID = "SLEEImportFolderPage.STORE_SOURCE_NAMES_ID";//$NON-NLS-1$
    private final static String STORE_OVERWRITE_EXISTING_RESOURCES_ID = "SLEEImportFolderPage.STORE_OVERWRITE_EXISTING_RESOURCES_ID";//$NON-NLS-1$
    private final static String STORE_CREATE_CONTAINER_STRUCTURE_ID = "SLEEImportFolderPage.STORE_CREATE_CONTAINER_STRUCTURE_ID";//$NON-NLS-1$
      
    /**
     *	Creates an instance of this class
     *
     * @param aWorkbench IWorkbench
     * @param selection IStructuredSelection
     */
    public FolderPage(IWorkbench aWorkbench,
            IStructuredSelection selection) {
        super("folderPage", aWorkbench, selection);//$NON-NLS-1$
    }

    
    /* (non-Javadoc)
     * Method declared on IDialogPage.
     */
    public void createControl(Composite parent) {
        super.createControl(parent);
        //PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IDataTransferHelpContextIds.FILE_SYSTEM_IMPORT_WIZARD_PAGE);
        //TODO Add help support
    }
    
    /**
     *	Create the import options specification widgets.
     */
    protected void createOptionsGroupButtons(Group optionsGroup) {

        // overwrite... checkbox
        overwriteExistingResourcesCheckbox = new Button(optionsGroup, SWT.CHECK);
        overwriteExistingResourcesCheckbox.setFont(optionsGroup.getFont());
        overwriteExistingResourcesCheckbox.setText(DataTransferMessages.FileImport_overwriteExisting);

        // create containers radio
        createContainerStructureButton = new Button(optionsGroup, SWT.RADIO);
        createContainerStructureButton.setFont(optionsGroup.getFont());
        createContainerStructureButton.setText(DataTransferMessages.FileImport_createComplete);
        createContainerStructureButton.setSelection(false);

        // create selection only radio
        createOnlySelectedButton = new Button(optionsGroup, SWT.RADIO);
        createOnlySelectedButton.setFont(optionsGroup.getFont());
        createOnlySelectedButton.setText(DataTransferMessages.FileImport_createSelectedFolders);
        createOnlySelectedButton.setSelection(true);

    }

    /**
     *	The Finish button was pressed.  Try to do the required work now and answer
     *	a boolean indicating success.  If false is returned then the wizard will
     *	not close.
     *
     * @return boolean
     */
    public boolean finish() {
    	boolean result = super.finish();
    	//TODO Add XML management
    	return result;
    }

    /**
     *	Open an appropriate source browser so that the user can specify a source
     *	to import from
     */
    protected void handleSourceBrowseButtonPressed() {

        String currentSource = this.sourceNameField.getText();
        DirectoryDialog dialog = new DirectoryDialog(
                sourceNameField.getShell(), SWT.SAVE);
        dialog.setText(SELECT_SOURCE_TITLE);
        dialog.setMessage(SELECT_SOURCE_MESSAGE);
        dialog.setFilterPath(getSourceDirectoryName(currentSource));

        String selectedDirectory = dialog.open();
        if (selectedDirectory != null) {
            //Just quit if the directory is not valid
            if ((getSourceDirectory(selectedDirectory) == null)
                    || selectedDirectory.equals(currentSource))
                return;
            //If it is valid then proceed to populate
            setErrorMessage(null);
            setSourceName(selectedDirectory);
            selectionGroup.setFocus();
        }
    }
    
    
    /**
     * Returns a content provider for <code>FileSystemElement</code>s that returns 
     * only files as children.
     */
    protected ITreeContentProvider getFileProvider() {
        return new WorkbenchContentProvider() {
            public Object[] getChildren(Object o) {
                if (o instanceof MinimizedFileSystemElement) {
                    MinimizedFileSystemElement element = (MinimizedFileSystemElement) o;
                    return element.getFiles(
                            FileSystemStructureProvider.INSTANCE).getChildren(
                            element);
                }
                return new Object[0];
            }
        };
    }
    
    /**
     *	Answer the root FileSystemElement that represents the contents of
     *	the currently-specified source.  If this FileSystemElement is not
     *	currently defined then create and return it.
     */
    protected MinimizedFileSystemElement getFileSystemTree() {

        File sourceDirectory = getSourceDirectory();
        if (sourceDirectory == null)
            return null;

        return selectFiles(sourceDirectory,
                FileSystemStructureProvider.INSTANCE);
    }
    
    /**
     * Returns a content provider for <code>FileSystemElement</code>s that returns 
     * only folders as children.
     */
    protected ITreeContentProvider getFolderProvider() {
        return new WorkbenchContentProvider() {
            public Object[] getChildren(Object o) {
                if (o instanceof MinimizedFileSystemElement) {
                    MinimizedFileSystemElement element = (MinimizedFileSystemElement) o;
                    return element.getFolders(
                            FileSystemStructureProvider.INSTANCE).getChildren(
                            element);
                }
                return new Object[0];
            }

            public boolean hasChildren(Object o) {
                if (o instanceof MinimizedFileSystemElement) {
                    MinimizedFileSystemElement element = (MinimizedFileSystemElement) o;
                    if (element.isPopulated())
                        return getChildren(element).length > 0;

                    //If we have not populated then wait until asked
                    return true;
                }
                return false;
            }
        };
    }
    
    /**
     *	Answer the string to display as the label for the source specification field
     */
    protected String getSourceLabel() {
        return DataTransferMessages.FileImport_fromDirectory;
    }
    
    /**
     *  Import the resources with extensions as specified by the user
     */
    protected boolean importResources(List fileSystemObjects) {
        ImportOperation operation = new ImportOperation(getContainerFullPath(),
                getSourceDirectory(), FileSystemStructureProvider.INSTANCE,
                this, fileSystemObjects);

        operation.setContext(getShell());
        return executeImportOperation(operation);
    }
    
    /**
     * Initializes the specified operation appropriately.
     */
    protected void initializeOperation(ImportOperation op) {
    	super.initializeOperation(op);
        op.setCreateContainerStructure(createContainerStructureButton
                .getSelection());
        }
    
    
    /**
     *	Use the dialog store to restore widget values to the values that they held
     *	last time this wizard was used to completion
     */
    protected void restoreWidgetValues() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            String[] sourceNames = settings.getArray(STORE_SOURCE_NAMES_ID);
            if (sourceNames == null)
                return; // ie.- no values stored, so stop

            // set filenames history
            for (int i = 0; i < sourceNames.length; i++)
                sourceNameField.add(sourceNames[i]);

            // radio buttons and checkboxes	
            overwriteExistingResourcesCheckbox.setSelection(settings
                    .getBoolean(STORE_OVERWRITE_EXISTING_RESOURCES_ID));

            boolean createStructure = settings
                    .getBoolean(STORE_CREATE_CONTAINER_STRUCTURE_ID);
            createContainerStructureButton.setSelection(createStructure);
            createOnlySelectedButton.setSelection(!createStructure);

        }
    }

    /**
     * 	Since Finish was pressed, write widget values to the dialog store so that they
     *	will persist into the next invocation of this wizard page
     */
    public void saveWidgetValues() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            // update source names history
            String[] sourceNames = settings.getArray(STORE_SOURCE_NAMES_ID);
            if (sourceNames == null)
                sourceNames = new String[0];

            sourceNames = addToHistory(sourceNames, getSourceDirectoryName());
            settings.put(STORE_SOURCE_NAMES_ID, sourceNames);

            // radio buttons and checkboxes	
            settings.put(STORE_OVERWRITE_EXISTING_RESOURCES_ID,
                    overwriteExistingResourcesCheckbox.getSelection());

            settings.put(STORE_CREATE_CONTAINER_STRUCTURE_ID,
                    createContainerStructureButton.getSelection());

        }
    }

    public String selfDescript() {
    	return "Import JAIN SLEE components created externally to the EclipSLEE plugin from a folder in the file system.";
    }

}
