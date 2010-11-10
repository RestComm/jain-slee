package org.mobicents.eclipslee.servicecreation.importing.wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author Paolo
 * This class is a wizard for importing Jain SLEE components into the workspace from
 * the file system.
 */
// (non Javadoc) Stable version

public class ImportSLEEFolderWizard extends ImportSLEEWizard {
public static String FOLDERWIZARD_ID = "ImportSLEEJARWizard"; 
	
    /**
     * Creates a wizard for importing Jain SLEE components into the workspace from
     * the file system.
     */
    public ImportSLEEFolderWizard () {
    	super();
        IDialogSettings section = getDialogSettings().getSection(FOLDERWIZARD_ID);//$NON-NLS-1$
        if (section == null)
        	section = getDialogSettings().addNewSection(FOLDERWIZARD_ID);
        setDialogSettings(section);
    }
	
	public void addPages (){    	
    	IWizardPage folderPage;
    	folderPage= new FolderPage (workbench, selection);
    	addPage(folderPage);    	
    }
	
	public String getIconPath(){
		return "icons/importWiz.gif";
	}

}
