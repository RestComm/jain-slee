package org.mobicents.slee.container.deployment.profile;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.ConcreteProfileGenerator;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;

/**
 * 
 * Start time:17:25:37 2009-03-12<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * Base class for calling profile code generation methdos.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeProfileClassCodeGenerator {

  private final static Logger logger = Logger.getLogger(SleeProfileClassCodeGenerator.class);

  public void process(ProfileSpecificationComponent component) throws Exception
  {
    if (logger.isDebugEnabled()) {
      logger.debug("Generating code for " + component);
    }
    
    ConcreteProfileGenerator concreteProfileGenerator = new ConcreteProfileGenerator(component);

    // 1. Generate CMP Interface Impl with JPA Annotations
    Class c = concreteProfileGenerator.generateConcreteProfile();
    component.setProfileCmpConcreteClass(c);

    // 2. Create the corresponding JPA PU -- FIXME: Should be somewhere else?
    JPAUtils.INSTANCE.createPersistenceUnit( component );

    // Generate Profile MBean Interface 
    ConcreteProfileMBeanGenerator mbeanGenerator = new ConcreteProfileMBeanGenerator(component);
    mbeanGenerator.generateProfileMBeanInterface();
    if (logger.isDebugEnabled()) {
      logger.debug("Generated MBean interface for " + component);
      logger.debug("Generated MBean interface " + component.getProfileMBeanConcreteInterfaceClass());
    }

		// Generate Profile MBean Impl
    mbeanGenerator.generateProfileMBean();
    if (logger.isDebugEnabled()) {
      logger.debug("Generated MBean impl for " + component);
      logger.debug("Generated MBean concrete " + component.getProfileMBeanConcreteImplClass());
    }

//    ConcreteProfileLocalObjectGenerator concreteProfileLocalObjectGenerator = new ConcreteProfileLocalObjectGenerator(component);
//    concreteProfileLocalObjectGenerator.generateProfileLocalConcreteClass();
//    if (logger.isDebugEnabled()) {
//      logger.debug("Generated Profile Local Object impl for " + component);
//      logger.debug("Generated Profile Local Object concrete " + component.getProfileLocalObjectConcreteClass());
//    }
//
//    ConcreteProfileTableGenerator concreteProfileTableGenerator = new ConcreteProfileTableGenerator(component);
//    concreteProfileTableGenerator.generateProfileTable();
//    if (logger.isDebugEnabled()) {
//      logger.debug("Generated Profile Table Interface impl for " + component);
//      logger.debug("Generated Profile Table Interface concrete " + component.getProfileTableConcreteClass());
//    }
//
//    new SleeComponentWithUsageParametersClassCodeGenerator().process(component);
//    if (logger.isDebugEnabled()) {
//      logger.debug("Generated Profile Table Usage Interface impl for " + component);
//      logger.debug("Generated Profile Table Usage Interface concrete " + component.getUsageParametersConcreteClass());
//      logger.debug("Generated Profile Table Usage MBean impl for " + component);
//      logger.debug("Generated Profile Table Usage MBean concrete " + component.getUsageParametersMBeanImplConcreteClass());
//    }

    /* FIXME: Alexandre: 1. Generate classes! Commented to not disturb TCK.
		ConcreteProfileManagementGenerator concreteProfileManagementGenerator = new ConcreteProfileManagementGenerator(component);
		concreteProfileManagementGenerator.generateProfileCmpConcreteClass();


		if (logger.isDebugEnabled()) {
			logger.debug("Generated management for " + component);
			logger.debug("Generated management class " + component.getProfileCmpConcreteClass());
		}
     */

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
   * @return the number of the combination (see JSLEE 1.0 spec section 10.5.2
   *         or 10.5.1.2 in JSLEE 1.1 spec), -1 if it doesn't match no
   *         combination<br>
   *         <ul>
   *         <li><b>1</b> - all cmp fields are exposed to management client and SLEE components(for SLEE as read only), no managemetn methods</li>
   *         <li><b>2</b> - ony double defined CMP accessors and management methods are visible</li>
   *         <li><b>3</b></li>
   *         <li><b>4</b></li>
   *         <li><b>-1</b> - when error occurs, no definitions</li>
   *         </ul>
   */
  public static int checkCombination(ProfileSpecificationComponent component)
  {
    Object profileCmpInterface = component.getDescriptor().getProfileCMPInterface();
    Object profileManagementInterface = component.getDescriptor().getProfileManagementInterface();
    Object profileManagementAbstractClass = component.getDescriptor().getProfileAbstractClass();
    //Object profileManagementLocalObjectInterface = compoenent.getDescriptor().getProfileLocalInterface();
    // if the Profile Specification has no Profile CMP interface, it is incorrect
    if (profileCmpInterface == null)
      return -1;

//    if (compoenent.isSlee11()) {
//      if (profileCmpInterface != null && profileManagementLocalObjectInterface != null && profileManagementAbstractClass != null)
//        return 4;
//      if (profileCmpInterface != null && profileManagementAbstractClass != null)
//        return 3;
//      if (profileCmpInterface != null && profileManagementLocalObjectInterface != null)
//        return 2;
//
//      return 1;
//    }
//    else
//    {
      if (profileCmpInterface != null && profileManagementInterface != null && profileManagementAbstractClass != null)
        return 4;
      if (profileCmpInterface != null && profileManagementAbstractClass != null)
        return 3;
      if (profileCmpInterface != null && profileManagementInterface != null)
        return 2;

      return 1;
//    }
  }

}
