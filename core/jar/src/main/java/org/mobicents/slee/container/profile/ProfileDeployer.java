/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * ProfileDeployer.java
 * 
 * Created on 30 sept. 2004
 *
 */
package org.mobicents.slee.container.profile;

import java.io.File;
import java.util.HashSet;

import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileSpecificationDescriptor;

import org.jboss.logging.Logger;

/**
 * Class deploying a profile
 * @depracated
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public class ProfileDeployer {
    /**
     * Logger to logg information
     */
    private static Logger logger=null;
    /** 
     * Generator used to generate the concrete classes from the Profile classes 
     * & interfaces provided by the sbb developer
     */
    private ConcreteProfileManagementGenerator concreteProfileManagementGenerator=null;
    /**
     * Verifier used to verify the sbb abstract class before generating 
     * the concrete class and to verify the generated concrete class
     */
    private ProfileVerifier profileVerifier=null;	
    /**
     * Path where to find the classes 
     */
    protected  String deployPath;
    /**
     * This hashtable will contain the name of the CMP interface whose 
     * concrete classes have already been generated
     * helps to avoid problems of class already loaded from the class loader
     */
    protected static HashSet profileClassesGenerated=null;
    
    static{
        profileClassesGenerated=new HashSet();
        logger = Logger.getLogger(ProfileDeployer.class);
      
    }
    
    public ProfileDeployer() {        
        String myPath  = "./classes";
        File file=new File(myPath);
        
        deployPath=file.getPath();
        
    }
    /**
     * Default Constructor
     */
    public ProfileDeployer(File dirPath) {   
        
        deployPath = dirPath.getPath();
    }
    
    
    public ProfileDeployer (String deployPath ) {
        this.deployPath = deployPath;
    }
    
    /**
     * This method cleans the table holding clues to the classes 
     * already generated
     */
    public static void init(){
       profileClassesGenerated=new HashSet(); 
    }    
    
    /**
     * Deploy a Profile.
     * @param profileSpecificationDescriptor descriptor used to deploy a profile.
     * @return true if the profile has been correctly deployed
     * @throws DeploymentException
     */
    public boolean deployProfile(ProfileSpecificationDescriptor profileSpecificationDescriptor) throws DeploymentException{
        profileVerifier=new ProfileVerifier(profileSpecificationDescriptor);
        //Verify class
       	boolean classVerifiedSuccessfully=profileVerifier.verifyProfileSpecification();
       	//Generates the class if it has been successfully verified
        if(classVerifiedSuccessfully){
            concreteProfileManagementGenerator=
                new ConcreteProfileManagementGenerator(profileSpecificationDescriptor);
            Class profileClass=null;
            Class profileMBeanInterface=null;
            Class profileMBeanClass=null;
            //the Concrete ProfileCMP class is generated            
            profileClass=concreteProfileManagementGenerator.
            		generateConcreteProfileCMP();
            //Adding the profile CMP interface name to the list of profile specification
            //already generated
            if(profileClass==null) {
                return false;                              
            }
            profileMBeanInterface=concreteProfileManagementGenerator.
        		generateProfileMBeanInterface();
            if(profileMBeanInterface==null)
                return false;
            profileMBeanClass=concreteProfileManagementGenerator.
    			generateConcreteProfileMBean();
            if(profileMBeanClass==null)
                return false;
            
            initializePersistedProfiles(profileSpecificationDescriptor.getCMPInterfaceName());
            
            return true;
        } else {
            throw new DeploymentException ("Verification error in Profile " + profileSpecificationDescriptor.getName());
        }
    }    
    
    /**
     * This method browses all the profiles existing in the backend storage.
     * The ones that match the spec id of the currently deployed profile are 
     * instantiated and registered with the mbean server.
     */
    private void initializePersistedProfiles(String deployedProfileCmpInterfaceName) {
        if (logger.isDebugEnabled()) logger.debug("Loading profile persistent state information...");
        /* FIXME emmartins: old code and feature to be reworked
    	SleeProfileManager profileManager = SleeContainer.lookupFromJndi().getSleeProfileManager();
        try {
        	Set allProfileTableNames = profileManager.getProfileTableNames();
            if (allProfileTableNames != null) {
                Iterator it = allProfileTableNames.iterator();
                boolean hasProfileChildren = false;
                while (it.hasNext()) {
                    //logger.info("Profile Table :");
                    hasProfileChildren = true;
                    String profileTableName = (String) it.next();
                    if (logger.isDebugEnabled())
                         logger.debug("Initializing defaultProfile for profile table "
                            + profileTableName);
                    String profileTableKey = profileManager
                            .generateProfileTableKey(profileTableName);
                    String cmpInterfaceName = (String) profileManager.loadObjectFromCache(profileTableKey, "cmpInterfaceName");
                    
                    // if the CMP interface of the deployed profile does not match the current table found in the cache, skip the table and go to the next one 
                    if (!deployedProfileCmpInterfaceName.equals(cmpInterfaceName)) continue;
                    
                    profileManager.instantiateProfile(cmpInterfaceName, profileTableName,
                            null, true);    
                    
                    Node childNode = (Node) profileManager.loadNodeFromCache(profileTableKey);            
                    for (Object obj : childNode.getChildren()) {
                    	Node profileNode = (Node) obj;
                        String profileName = profileNode.getFqn().getLastElementAsString();
                        if (logger.isDebugEnabled()) {
                        	logger.debug("Initializing profile " + profileName);
                        }
                        profileManager.instantiateProfile(cmpInterfaceName,
                                profileTableName, profileName, true);
                    }                    
                }
                if (!hasProfileChildren) {
                	if (logger.isDebugEnabled()) {
                		logger.debug("No information found in the backend storage");
                	}
                }
            } else {
            	if (logger.isDebugEnabled()) {
            		logger.debug("No information found in the backend storage");
            	}
            }
        } catch (Exception e) {
            logger.error("Failed to load profile persistent state information",e);
        } finally {
            profileManager.displayAllProfilePersistentInformation();
        }
        */
    }
     
}
