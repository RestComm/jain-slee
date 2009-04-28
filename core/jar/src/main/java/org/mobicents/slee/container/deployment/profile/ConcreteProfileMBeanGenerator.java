package org.mobicents.slee.container.deployment.profile;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.slee.management.DeploymentException;
import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileImplementationException;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.profile.ProfileMBeanConcrete;

public class ConcreteProfileMBeanGenerator {

	private static final Logger logger = Logger.getLogger(ConcreteProfileManagementGenerator.class);
	public static final String _MBEAN_CMP_INTERCEPTOR = "super.profileObject.getProfileConcrete()";
	public static final String _MBEAN_MGMT_INTERCEPTOR = "super.profileObject.getProfileConcrete()";
	private ProfileSpecificationComponent component = null;
	private String cmpProfileInterfaceName = null;

	private String profileManagementInterfaceName = null;
	private ClassPool pool = null;
	private CtClass cmpProfileInterface = null;
	private CtClass profileManagementInterface = null;
	private CtClass profileMBeanConcreteClass = null;
	private CtClass profileMBeanConcreteInterface = null;
	private CtClass sleeProfileMBean = null;
	private CtClass mobicentsProfileMBean = null;

	public ConcreteProfileMBeanGenerator(ProfileSpecificationComponent component)
	{
		super();
		this.component = component;
		ProfileSpecificationDescriptorImpl descriptor = component.getDescriptor();
		this.cmpProfileInterfaceName = descriptor.getProfileClasses().getProfileCMPInterface().getProfileCmpInterfaceName();
		this.profileManagementInterfaceName = descriptor.getProfileManagementInterface() == null ? null : descriptor.getProfileManagementInterface().getProfileManagementInterfaceName();
		this.pool = component.getClassPool();
	}

	/**
	 * Generates the Profile MBean interface
	 * 
	 * @return the interface generated
	 */
	public void generateProfileMBeanInterface() throws Exception
	{
		if (SleeProfileClassCodeGenerator.checkCombination(component) == -1)
		{
			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2");
		}
		
		// Extends the javax.slee.profile.ProfileMBean
		profileMBeanConcreteInterface = pool.makeInterface(cmpProfileInterfaceName + "MBean");

		try
		{
			sleeProfileMBean = pool.get("javax.slee.profile.ProfileMBean");
		}
		catch (NotFoundException nfe) {
			nfe.printStackTrace();
			throw new DeploymentException("Failed to locate MBean class for " + this.component);
		}
		
		ConcreteClassGeneratorUtils.createInterfaceLinks(profileMBeanConcreteInterface, new CtClass[] { sleeProfileMBean });
		CtClass[] exceptions = new CtClass[3];
		try
		{
			exceptions[0] = pool.get("javax.slee.management.ManagementException");
			exceptions[1] = pool.get("javax.slee.InvalidStateException");
			exceptions[2] = pool.get("javax.slee.profile.ProfileImplementationException");
		}
		catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate exception class for " + component, nfe);
		}
		
		// If the Profile Specification defines a Profile Management Interface, the profileMBean interface has the same methods
		try
		{
      cmpProfileInterface = pool.get(cmpProfileInterfaceName);
			profileManagementInterface = profileManagementInterfaceName != null ? pool.get(profileManagementInterfaceName) : null;
		}
		catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate CMP/Management Interface for " + component, nfe);
		}
		
		ConcreteClassGeneratorUtils.copyMethods((profileManagementInterface != null ? profileManagementInterface : cmpProfileInterface), profileMBeanConcreteInterface, exceptions);
		
		try
		{
			// pool.writeFile(cmpProfileInterfaceName+"MBean", deployPath);
			pool.get(cmpProfileInterfaceName + "MBean").writeFile(this.component.getDeploymentDir().getAbsolutePath());
			if (logger.isDebugEnabled())
			{
				logger.debug("Concrete Interface " + cmpProfileInterfaceName + "MBean" + " generated in the following path " + this.component.getDeploymentDir().getAbsolutePath());
			}
		}
		catch (Exception e) {
			throw new DeploymentException("Encountered exception while generating class.", e);
		}
		finally
		{
			if (profileMBeanConcreteInterface != null)
			{
				profileMBeanConcreteInterface.defrost();
			}
		}

		try
		{
		  Class clazz = component.getClassLoader().loadClass(cmpProfileInterfaceName + "MBean");
			this.component.setProfileMBeanConcreteInterfaceClass(clazz);
		}
		catch (ClassNotFoundException e) {
			throw new DeploymentException("Encountered exception while generating class.", e);
		}

		if (this.component.getProfileMBeanConcreteInterfaceClass() == null) {
			throw new DeploymentException("Failed to generate MBean interface, some sort of bug?");
		}
	}

	/**
	 * This method generates concrete class of MBean impl
	 */
	public void generateProfileMBean() throws Exception
	{
		if (SleeProfileClassCodeGenerator.checkCombination(component) == -1)
		{
			throw new DeploymentException("Profile Specification doesn't match any combination " + "from the JSLEE spec 1.0 section 10.5.2");
		}

		String tmpClassName = ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_PREFIX + cmpProfileInterfaceName + ConcreteClassGeneratorUtils.PROFILE_MBEAN_CONCRETE_CLASS_NAME_SUFFIX;

		profileMBeanConcreteClass = pool.makeClass(tmpClassName);

		try
		{
			mobicentsProfileMBean = pool.get(ProfileMBeanConcrete.class.getName());
		}
		catch (NotFoundException nfe) {
			throw new DeploymentException("Failed to locate requried class.", nfe);
		}
		
		CtClass[] interfaces = new CtClass[1];
		//interfaces[0] = sleeProfileMBean;
		interfaces[0] = profileMBeanConcreteInterface;
		ConcreteClassGeneratorUtils.createInterfaceLinks(profileMBeanConcreteClass, interfaces);
		ConcreteClassGeneratorUtils.createInheritanceLink(profileMBeanConcreteClass, mobicentsProfileMBean);
		
		// implements methods defined in the profileMBean interface previously generated
		
		Map<String,CtMethod> profileCmpMethods = ClassUtils.getInterfaceMethodsFromInterface(cmpProfileInterface);
		//if there is no management interface management interface == CMP interface
		Map<String,CtMethod> managementInterfaceMethods = ClassUtils.getInterfaceMethodsFromInterface(profileManagementInterfaceName == null ? cmpProfileInterface : profileManagementInterface);

		generateCmpAccessors(profileMBeanConcreteClass, managementInterfaceMethods, profileCmpMethods);
		generateBussinesMethods(profileMBeanConcreteClass, managementInterfaceMethods);
		
		try
		{
		  pool.get(tmpClassName).writeFile(this.component.getDeploymentDir().getAbsolutePath());
			if (logger.isDebugEnabled()) {
				logger.debug("Concrete Class " + tmpClassName + " generated in the following path " + this.component.getDeploymentDir().getAbsolutePath());
			}
		}
		catch (CannotCompileException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			profileMBeanConcreteClass.defrost();
		}

		Class clazz = null;
		try
		{
			clazz = component.getClassLoader().loadClass(tmpClassName);
			this.component.setProfileMBeanConcreteImplClass(clazz);
		}
		catch (ClassNotFoundException e1) {
			throw new DeploymentException("Failed to load concrete MBean impl class", e1);
		}

		if (this.component.getProfileMBeanConcreteImplClass() == null)
		{
			throw new DeploymentException("Failed to obtain concrete MBean impl class, bug?");
		}
	}
	
	/**
	 * Delegates all methods to proper interceptor. Takes care of section 10.26.2
	 * @param profileMBeanConcreteClass
	 * @param profileManagementMethods
	 * @throws Exception
	 */
	private void generateBussinesMethods(CtClass profileMBeanConcreteClass, Map<String, CtMethod> profileManagementMethods) throws Exception
	{
		Iterator<Map.Entry<String, CtMethod>> it= profileManagementMethods.entrySet().iterator();
		
		while(it.hasNext())
		{
			Map.Entry<String, CtMethod> entry = it.next();
			instrumentBussinesMethod(profileMBeanConcreteClass,entry.getValue(),"((" + this.component.getProfileCmpConcreteClass().getName() + ")" + _MBEAN_MGMT_INTERCEPTOR + ")");		
		}
	}

	private void instrumentBussinesMethod(CtClass profileMBeanConcreteClass, CtMethod method,  String interceptorAccess) throws Exception
	{
    CtMethod newMethod = CtNewMethod.copy( method, profileMBeanConcreteClass, null );
    newMethod.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
    
		String returnStatement = "";
		boolean hasReturnValue = false;
		if(method.getReturnType().toString().compareTo("void")==0)
		{
			returnStatement="";
		}
		else
		{
			hasReturnValue = true;
			returnStatement ="return ($r)";
		}
		
		//Slee 1.1 specs section 10.26.2
		String body="{ "+
		"boolean createdTransaction = false;"+
		"boolean rollback = true;"+
		"Thread t = Thread.currentThread();"+
		"ClassLoader oldClassLoader = t.getContextClassLoader();"+
		"t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());"+
		"try {"+
		"	createdTransaction = super.sleeTransactionManager.requireTransaction();";
		if(hasReturnValue)
			body+="Object result = "+interceptorAccess+"."+method.getName()+"($$);";
		else
			body+=" = "+interceptorAccess+"."+method.getName()+"($$);";
		
		body+="rollback = false;";
		if(hasReturnValue)
			body+=returnStatement+"result;";
		else	
			body+=returnStatement+";";		
		
		body+="} catch (javax.slee.profile.ReadOnlyProfileException e) {"+
		"	throw new "+javax.slee.InvalidStateException.class.getName()+"(\"Profile is read only.\");"+
		"} catch("+java.lang.RuntimeException.class.getName()+" re)"+
		"{"+
		"	throw new "+ProfileImplementationException.class.getName()+"(re);"+
		"} catch("+java.lang.Exception.class.getName()+" checked)"+
		"{"+
		"	throw new "+ProfileImplementationException.class.getName()+"(checked);"+
		"}"+
		"finally {"+
		"t.setContextClassLoader(oldClassLoader);"+	
		"	if (rollback) {"+
		"		try {"+
		"			super.sleeTransactionManager.rollback();"+
		"		} catch ("+java.lang.Exception.class.getName()+" e) {"+

		"			e.printStackTrace();"+
		"			throw new "+ManagementException.class.getName()+"(\"Failed to rollback\", e);"+
		"		}"+
		"	} else if (createdTransaction) {"+
		"		 {"+
		"			try {"+
		"				super.sleeTransactionManager.commit();"+
		"			} catch ("+java.lang.Exception.class.getName()+" e) {"+

		"				e.printStackTrace();"+
		"				throw new "+ManagementException.class.getName()+"(\"Failed to commit\", e);"+
		"			}"+
		"		}"+
		"	}"+
		"}" +
    "}";

		if(logger.isDebugEnabled())
		{
			logger.debug("Instrumented method, name:"+method.getName()+", with body:\n"+body);
		}
		
		newMethod.setBody(body);
		profileMBeanConcreteClass.addMethod(newMethod);
	}

	private void instrumentCmpMethod(CtClass concreteClass,CtMethod method, boolean isGet, String interceptorAccess) throws Exception
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Instrumenting method, name:"+method.getName());
		}
		
		CtMethod newMethod = CtNewMethod.copy( method, concreteClass, null );
		newMethod.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
		String body = "{"+ (isGet ? "return ($r)" : "") + interceptorAccess+"."+method.getName()+"($$); }";
		if(logger.isDebugEnabled())
		{
			logger.debug("Instrumented method, name:"+method.getName()+", with body:\n"+body);
		}
		
		newMethod.setBody(body);
		concreteClass.addMethod(newMethod);
		
	
	}

	/**
	 * This method generates cmp accessors methods passed in firs map arg.
	 * second contains management methods that will be instrumented. See
	 * description of params.
	 * 
	 * @param profileMBeanConcreteClass
	 *            - concrete class to add concrete methods.
	 * @param profileManagementMethods
	 *            - management methods. This is full list of management methods
	 *            - including methods from management interface. In case there
	 *            is no management interface, this contains full cmp list. As
	 *            methods are instrumented, cmp methods are removed from here.
	 *            At the end of execution only management methods will remain
	 * @param profileCmpMethods
	 *            - cmp itnerface methods, this is used to determine if method
	 *            is cmp or management || management cmp hidding method
	 * @throws Exception
	 */
	private void generateCmpAccessors(CtClass profileMBeanConcreteClass, Map<String, CtMethod> profileManagementMethods, Map<String, CtMethod> profileCmpMethods) throws  Exception
	{
		Iterator<Map.Entry<String, CtMethod>> it= profileManagementMethods.entrySet().iterator();
		
		while(it.hasNext())
		{
			Map.Entry<String, CtMethod> entry = it.next();
			if(profileCmpMethods.containsKey(ClassUtils.getMethodKey(entry.getValue())))
			{
				instrumentCmpMethod(profileMBeanConcreteClass, entry.getValue(), entry.getKey().startsWith(ClassUtils.GET_PREFIX), "((" + this.component.getProfileCmpConcreteClass().getName() + ")" + _MBEAN_CMP_INTERCEPTOR + ")");
				it.remove();
			}
			else
			{
				//its management method, interceptedelsewhere
				if(logger.isDebugEnabled())
				{
					logger.debug("Not instrumenting CMP like method as it is management method,name: "+entry.getValue().getName());
				}
			}
		}
	}

}
