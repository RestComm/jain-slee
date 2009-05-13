package org.mobicents.slee.container.deployment.profile;

import java.beans.Introspector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileTable;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.profile.ProfileQueryHandler;
import org.mobicents.slee.container.profile.ProfileTableImpl;

/**
 * 
 * ConcreteProfileTableGenerator.java
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ConcreteProfileTableGenerator {

  private static final Logger logger = Logger.getLogger(ConcreteProfileTableGenerator.class);
  
  public static final String _INTERCEPTOR_QUERY = ProfileQueryHandler.class.getName() + ".handle";
  public static final String _QUERY_METHOD_NAME_PREFIX = "query";
  
  private ProfileSpecificationComponent component = null;
  
  private String cmpProfileInterfaceName = null;
  private String profileTableInterfaceName = null;
  private String profileTableConcreteClassName = null;

  private ClassPool pool;
  private CtClass profileTableInterface = null;
  private CtClass sleeProfileTableInterface = null;
  private CtClass mobicentsConcreteProfileTableClass = null;
  private CtClass profileTableConcreteClass = null;

  public ConcreteProfileTableGenerator(ProfileSpecificationComponent component)
  {
    super();
    this.component = component;
    ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
    cmpProfileInterfaceName = descriptor.getProfileClasses().getProfileCMPInterface().getProfileCmpInterfaceName();
    profileTableInterfaceName = descriptor.getProfileClasses().getProfileTableInterface() == null ? null : descriptor.getProfileClasses().getProfileTableInterface().getProfileTableInterfaceName();

    pool = component.getClassPool();
  }

  public void generateProfileTable() throws Exception
  {
    try {
      mobicentsConcreteProfileTableClass = pool.get(ProfileTableImpl.class.getName());
    }
    catch (NotFoundException nfe) {
      throw new SLEEException("Failed to locate ProfileTableConcreteImpl class for " + component, nfe);
    }

    try {
      sleeProfileTableInterface = pool.get(ProfileTable.class.getName());
    }
    catch (NotFoundException nfe) {
      throw new SLEEException("Failed to locate ProfileTable interface for " + component, nfe);
    }

    //Those methods are already implemented.
    Map<String,CtMethod> alreadyImplemented = ClassUtils.getInterfaceMethodsFromInterface(sleeProfileTableInterface);
    Map<String, CtMethod> queriesMapToDivert = new HashMap<String, CtMethod>();

    CtClass[] targetInterfaces = null;

    if (profileTableInterfaceName != null)
    {
      // we have some interface, also we have methods from generic ProfileTable interface, we want only queries.
      try
      {
        profileTableInterface = pool.get(profileTableInterfaceName);
        queriesMapToDivert = ClassUtils.getInterfaceMethodsFromInterface(profileTableInterface);
        //Lets remove methods that we already have
        queriesMapToDivert.keySet().removeAll(alreadyImplemented.keySet());
      }
      catch (NotFoundException nfe) {
        throw new DeploymentException("Failed to locate ProfiteTable interface for " + component, nfe);
      }

      profileTableConcreteClassName  = ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_PREFIX + profileTableInterface.getName() + ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_SUFFIX;
      CtClass[] presentInterfaces = this.mobicentsConcreteProfileTableClass.getInterfaces();
      targetInterfaces = new CtClass[presentInterfaces.length + 1];

      for (int index = 0; index < presentInterfaces.length; index++) {
        targetInterfaces[index] = presentInterfaces[index];
      }

      targetInterfaces[targetInterfaces.length - 1] = profileTableInterface;
    }
    else
    {
      //There is no custom profile table interface.
      profileTableConcreteClassName = ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_PREFIX + cmpProfileInterfaceName + ConcreteClassGeneratorUtils.PROFILE_TABLE_CLASS_NAME_SUFFIX;
      targetInterfaces = this.mobicentsConcreteProfileTableClass.getInterfaces();
    }

    //lets make concrete class
    try {
      profileTableConcreteClass = pool.makeClass(profileTableConcreteClassName);
    }
    catch (Exception e) {
      throw new SLEEException("Failed to create ProfileTableConcreteClass implementation class.");
    }

    ConcreteClassGeneratorUtils.createInterfaceLinks(profileTableConcreteClass, targetInterfaces);
    ConcreteClassGeneratorUtils.createInheritanceLink(profileTableConcreteClass, mobicentsConcreteProfileTableClass);

    //now we have to instrument queries methods.
    generateQueries(profileTableConcreteClass,queriesMapToDivert);

    //write and store
    try {
      // @@2.4 +  -> 3.4 + 
      profileTableConcreteClass.writeFile(component.getDeploymentDir().getAbsolutePath());

      if (logger.isDebugEnabled()) {
        logger.debug("Concrete Class " + profileTableConcreteClass.getName() + " generated in the following path " + component.getDeploymentDir().getAbsolutePath());
      }
    }
    catch (Exception e) {
      throw new SLEEException("Unexpected exception generating ProfileTableConcrete Class.", e);
    }
    finally {
      // let go, so that it's not holding subsequent deployments of the same profile component.
      // This would not have been necessary is the ClassPool is not one shared instance in the SLEE,
      // but there is instead a hierarchy mimicing the classloader hierarchy. This also makes
      // our deployer essentially single threaded.
      profileTableConcreteClass.defrost();
    }

    try {
      // load the generated class
      Class clazz = component.getClassLoader().loadClass(profileTableConcreteClass.getName());
      component.setProfileTableConcreteClass(clazz);
    }
    catch (ClassNotFoundException cnfe) {
      throw new SLEEException("Unexpected exception generating ProfileTableConcrete Class.", cnfe);
    }
  }

  private void generateQueries(CtClass profileTableConcreteClass, Map<String, CtMethod> queriesMapToDivert) throws Exception
  {
    Iterator<Map.Entry<String, CtMethod>> it = queriesMapToDivert.entrySet().iterator();

    while(it.hasNext())
    {
      instrumentQuery(profileTableConcreteClass,it.next().getValue(),_INTERCEPTOR_QUERY);
      it.remove();
    }
  }

  private void instrumentQuery(CtClass profileTableConcreteClass, CtMethod iMethod, String interceptorQuery) throws Exception
  {
    if(logger.isDebugEnabled())
    {
      logger.debug("About to instrument query method: " + iMethod.getName() + ", into: " + profileTableConcreteClass);
    }

    CtMethod method = CtNewMethod.copy(iMethod, profileTableConcreteClass, null);
    method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);

    String queryName = method.getName();

    if(queryName.startsWith(_QUERY_METHOD_NAME_PREFIX))
    {
      queryName= Introspector.decapitalize(queryName.replace(_QUERY_METHOD_NAME_PREFIX, ""));
    }
    else
    {
      throw new SLEEException("Method has wrong prefix, method name: " + queryName);
    }

    String body = "{ return " + _INTERCEPTOR_QUERY + "(this,\"" + queryName + "\",$args); }";

    if(logger.isDebugEnabled())
    {
      logger.debug("Instrument query method: " + method.getName() + ", into: " + profileTableConcreteClass + ", with body:\n" + body);
    }

    method.setBody(body);
    profileTableConcreteClass.addMethod(method);
  }

}
