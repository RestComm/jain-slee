package org.mobicents.slee.container.deployment.profile;


import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.SbbClassCodeGenerator;
/**
 * 
 * Start time:17:25:37 2009-03-12<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeProfileClassCodeGenerator {

	private final static Logger logger = Logger.getLogger(SbbClassCodeGenerator.class);

	public void process(ProfileSpecificationComponent component) throws Exception{

		if(logger.isDebugEnabled())
		{
			logger.debug("Generating code for "+component);
		}
		ConcreteProfileManagementGenerator concreteProfileManagementGenerator=new ConcreteProfileManagementGenerator(component);
		concreteProfileManagementGenerator.generateProfileCmpConcreteClass();
		
		if(logger.isDebugEnabled())
		{
			logger.debug("Generated management for "+component);
		}
		ConcreteProfileMBeanGenerator mbeanGenerator =new ConcreteProfileMBeanGenerator(component,concreteProfileManagementGenerator.getProfileManagementAbstractClass());
		mbeanGenerator.generateProfileMBeanInterface();
		if(logger.isDebugEnabled())
		{
			logger.debug("Generated MBean interface for "+component);
		}
		
		mbeanGenerator.generateProfileMBean();
		if(logger.isDebugEnabled())
		{
			logger.debug("Generated MBean impl for "+component);
			logger.debug("Class generation completed for "+component);
		}
		
		
		if(logger.isDebugEnabled())
		{
			
		}
		
		

	}

	
	/**
     * Check which combination (see JSLEE 1.0 spec section 10.5.2) matches the
     * Sbb Developer's Profile Specification
     * 
     * @param profileCMPInterfaceName
     *            name of the Profile CMP interface
     * @param profileManagementInterfaceName
     *            name of the Profile Management interface
     * @param profileManagementAbstractClassName
     *            name of the Profile Management Abstract class
     * @return the number of the combination (see JSLEE 1.0 spec section
     *         10.5.2), -1 if it doesn't match no combination
     */
    public static int checkCombination(ProfileSpecificationComponent compoenent) {

    	Object profileCmpInterface = compoenent.getDescriptor().getProfileCMPInterface();
    	Object profileManagementInterface = compoenent.getDescriptor().getProfileManagementInterface();
    	Object profileManagementAbstractClass = compoenent.getDescriptor().getProfileAbstractClass();
    	
        //if the Profile Specification has no Profile CMP interface, it is
        // incorrect
        if (profileCmpInterface == null)
            return -1;
        
        if (profileCmpInterface == null
                && profileManagementAbstractClass == null) {
            if (logger.isDebugEnabled()) {
            	logger
                    .debug("The Profile Specification provided by the Sbb Developer "
                            + "is the combination 1 of JSLEE 1.0 spec section 10.5.2");
            }
            return 1;
        }
        if (profileManagementInterface != null
                && profileManagementAbstractClass == null) {
        	if (logger.isDebugEnabled()) {
        		logger
                    .debug("The Profile Specification provided by the Sbb Developper "
                            + "is the combination 2 of JSLEE 1.0 spec section 10.5.2");
        	}
            return 2;
        }
        if (profileManagementInterface == null
                && profileManagementAbstractClass != null) {
        	if (logger.isDebugEnabled()) {
        		logger
                    .debug("The Profile Specification provided by the Sbb Developper "
                            + "is the combination 3 of JSLEE 1.0 spec section 10.5.2");
        	}
            return 3;
        }
        if (profileManagementInterface != null
                && profileManagementAbstractClass != null) {
        	if (logger.isDebugEnabled()) {
        		logger
                    .debug("The Profile Specification provided by the Sbb Developper "
                            + "is the combination 4 of JSLEE 1.0 spec section 10.5.2");
        	}
            return 4;
        }
        return -1;
    }
	
}
