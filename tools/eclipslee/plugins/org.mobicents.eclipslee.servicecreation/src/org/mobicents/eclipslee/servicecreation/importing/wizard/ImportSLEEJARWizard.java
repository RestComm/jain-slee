package org.mobicents.eclipslee.servicecreation.importing.wizard;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author Paolo
 *	This class is a wizard for importing Jain SLEE components into the workspace from
 *  a archive file 
 */
//(non Javadoc) stable version

public class ImportSLEEJARWizard extends ImportSLEEWizard {
	
	public static String JARWIZARD_ID = "ImportSLEEJARWizard"; 
	
    /**
     * Creates a wizard for importing Jain SLEE components into the workspace from
     * a archive file
     */
    public ImportSLEEJARWizard () {
    	super();
        IDialogSettings section = getDialogSettings().getSection(JARWIZARD_ID);//$NON-NLS-1$
        if (section == null)
        	section = getDialogSettings().addNewSection(JARWIZARD_ID);
        setDialogSettings(section);
    }
	
	public void addPages (){    	
    	IWizardPage archivePage;
    	archivePage= new ArchivePage (workbench, selection);
    	addPage(archivePage);    	
    }
	
	public String getIconPath(){
		return "icons/importWizZip.gif";
	}

}
