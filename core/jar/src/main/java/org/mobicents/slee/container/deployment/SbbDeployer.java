/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * SbbDeployer.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import java.io.File;

import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;

/**
 * Class deploying a sbb
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 *  
 */
public class SbbDeployer {
    /**
     * Logger to logg information
     */
    private static Logger logger = Logger.getLogger(SbbDeployer.class);;

    /**
     * Pointer to the service container -- this is needed to check if the
     * correct eventhandler methods exist.
     */
    private SleeContainer serviceContainer;

    /**
     * Generator used to generatr the concrete class of the Sbb abstract class
     * provided by the sbb developer
     */
    private ConcreteSbbGenerator concreteSbbGenerator = null;

    /**
     * Verifier used to verify the sbb abstract class before generating the
     * concrete class and to verify the generated concrete class
     */
    private SbbVerifier sbbVerifier = null;

    
    // (ranga): IVELIN -- do we need this static for UsageParameter class generation?
    
    // protected static HashSet concreteClassesGenerated;
    

    private String deployPath;

    private SbbDeployer() {
    }

    /**
     * @param rootPath
     *            is where SBB classes should be located
     * 
     * Default Constructor
     */
    public SbbDeployer(String rootPath) {
    	if (logger.isDebugEnabled()) {
    		logger.debug("Creating SbbDeployer with root path " + rootPath);
    	}
        File file = new File(rootPath);

        deployPath = file.getPath();
    }

    /**
     * Deploy a Sbb.
     * 
     * @param sbbDeploymentDescriptor
     *            descriptor used to deploy a sbb.
     * @return true if the sbb has been correctly deployed
     */
    public boolean deploySbb(MobicentsSbbDescriptor sbbDeploymentDescriptor,
            SleeContainer serviceContainer) throws DeploymentException {
        String sbbAbstractClassName = ((MobicentsSbbDescriptor) sbbDeploymentDescriptor)
                .getSbbAbstractClassName();
        sbbVerifier = new SbbVerifier(sbbDeploymentDescriptor);
        //Verify abstract class
        boolean classVerifiedSuccessfully = sbbVerifier
                .verifySbbAbstractClass(sbbAbstractClassName,
                        serviceContainer);
        MobicentsSbbDescriptor descriptorImpl = (MobicentsSbbDescriptor) sbbDeploymentDescriptor;
        
        if (logger.isDebugEnabled()) {
        	logger.debug("usageParametersInterface = "
                + descriptorImpl.getUsageParametersInterface());
        }
        boolean usageParameterVerified = descriptorImpl
                .getUsageParametersInterface() == null
                || ConcreteUsageParameterClassGenerator
                        .checkUsageParameterInterface(descriptorImpl);
        if (logger.isDebugEnabled()) {
        	logger.debug("classVerifiedSuccessfully ? =>"
                + classVerifiedSuccessfully);
        	logger
                .debug("usageParameterVerified ? =>"
                        + usageParameterVerified);
        }
        //Generates the class if it has been successfully verified
        if (classVerifiedSuccessfully && usageParameterVerified) {

            if (descriptorImpl.getUsageParametersInterface() != null) {
                try {
                    Class usageParamClazz = new ConcreteUsageParameterClassGenerator(sbbDeploymentDescriptor)
                            .generateConcreteUsageParameterClass(descriptorImpl);
                    descriptorImpl.setUsageParameterClass(usageParamClazz);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }

            //Enhancement code goes here...
            SbbAbstractDecorator abstractSbbDecorator = new SbbAbstractDecorator(
                    sbbDeploymentDescriptor);
            abstractSbbDecorator.decorateAbstractSbb();

            concreteSbbGenerator = new ConcreteSbbGenerator(
                    sbbDeploymentDescriptor);
            Class clazz = concreteSbbGenerator.generateConcreteSbb();
           
            //TODO if the class has been generated, delete it from the disk
            if (clazz != null) {
         
            	if (logger.isDebugEnabled()) {
            		logger.debug("Generated all classes!");
            	}
                return true;
            } else
                throw new DeploymentException(" Could not deploy Sbb "
                        + sbbDeploymentDescriptor.getName());
        } else {
            throw new DeploymentException("Verification error in SBB "
                    + sbbDeploymentDescriptor.getName());
        }
    }

    /**
     * The path where common SLEE jars are located. Typically the mobicents.sar
     * directory
     *  
     */
    private static String libPath = null;

    static {
        libPath = SleeContainer.getDeployPath() + File.separator;
    }

    /**
     * TODO: using the SLEE class loader is not the most elegant way to do this.
     * Reconsider.
     * 
     * @return the root directory where common jar files are located
     */
    public static String getLibPath() {
        return libPath;
    }

    
}