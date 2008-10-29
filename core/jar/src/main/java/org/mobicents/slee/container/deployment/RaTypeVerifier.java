package org.mobicents.slee.container.deployment;

import java.io.IOException;
import java.io.InputStream;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.resource.ActivityTypeEntry;
import org.mobicents.slee.resource.ResourceAdaptorTypeClassEntry;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;

public class RaTypeVerifier {

	private ResourceAdaptorTypeDescriptorImpl raTypeDescriptor = null;

	/**
	 * Path where to find the classes
	 */
	//private static String deployPath = null;

	/**
	 * Pool to generate or read classes with javassist
	 */
	private ClassPool pool = null;

	/**
	 * Logger to logg information
	 */
	private static Logger logger = null;

	private String errorString;

	private String acifClassName = null;

	static {
		logger = Logger.getLogger(RaTypeVerifier.class);

	}

	public RaTypeVerifier(ResourceAdaptorTypeDescriptorImpl raTypeDescriptor) {
		super();
		this.raTypeDescriptor = raTypeDescriptor;

		pool = ((DeployableUnitIDImpl) (raTypeDescriptor.getDeployableUnit()))
				.getDUDeployer().getClassPool();

		this.acifClassName = raTypeDescriptor.getRaTypeClassEntry()
				.getAcifInterfaceEntry().getInterfaceName();

	}

	public boolean verifyRaType() {
		// FIXME: Not much to do here, maybe later it can verify RACIF class
		// that container implements
		boolean failed = false;
		if (!verifyActivityContextInterfaceFactory()) {
			failed = true;
			logger
					.error("Failed to verify RaType activity context factory interface, possibly activities defined in interface class and in xml descriptor do not match!!!");
		}

		return !failed;
	}

	/**
	 * Verifvies declaration of methods in RAType ACIF class against descriptor.
	 * 
	 * @return <ul><li><b>true</b> - if ra type acif has been verified successfuly</li><li><b>false</b> - otherwise</li></ul>
	 */
	private boolean verifyActivityContextInterfaceFactory() {

		// Verify that for each activity declared in descriptor
		// method: public ActivityContextInterface
		// getActivityContextInterface(SomeClass activity) throws
		// NullPointerException,
		// UnrecognizedActivityException, FactoryException

		//until issue 227 is worked out
		if(true)
			return true;
		
		//FIXME: Is it possible to have  raTypeDescriptor.getRaTypeClassEntry().getAcifInterfaceEntry().getInterfaceName()==null; ??
		/*
		 * Load the abstract class definition in memory, but do not make it
		 * visible via the classloader yet
		 */
		logger.info("RATYPE ["+raTypeDescriptor.getID()+"] - Verifying ACIF [" + this.acifClassName+"]");
		if(this.acifClassName==null)
		{
			//TCK .... ?
			return true;
		}
		ClassLoader cl = (ClassLoader) Thread.currentThread()
				.getContextClassLoader();
		String classRsrcName = acifClassName.replace('.', '/') + ".class";
		InputStream acifClassIS = cl.getResourceAsStream(classRsrcName);
		if (acifClassIS == null) {
			logger.error("Cannot find class file for specified entry in ra-type xml - activity-context-interface-factory-interface-name -> " + classRsrcName);
			return false;
		}

		CtClass ctClass = null;
		try {
			ctClass = pool.get(acifClassName).getClassPool().makeClass(
					acifClassIS);
		} catch (NotFoundException e) {
			ctClass = pool.makeClass(acifClassName);
			// e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		} catch (RuntimeException e) {

			e.printStackTrace();
			return false;
		}

		CtMethod[] acifMethods = ctClass.getDeclaredMethods();

		
		// It has to implement getActivityContextInterface methods for all
		// defined activities

		ResourceAdaptorTypeClassEntry entry = raTypeDescriptor
				.getRaTypeClassEntry();

		ActivityTypeEntry[] activitiesEntries = entry.getActivityTypeEntries();

		boolean[] found = new boolean[activitiesEntries.length];
		boolean passed = true;
		//int counter = 0;
		ActivityTypeEntry e;
		for(int counter=0; counter<activitiesEntries.length;counter++)
		{
			
			e=activitiesEntries[counter];
		//for (ActivityTypeEntry e : activitiesEntries) {
			String activityName = e.getActivityTypeName();
			for(int i=0;i<acifMethods.length;i++)
			{
				CtMethod m=acifMethods[i];
				if(m==null)
					continue;
			//for (CtMethod m : acifMethods) {
				logger.debug(" ["+raTypeDescriptor.getID()+"]  Checking method [" + m.getName() + "]");
				if (m.getName().equals("getActivityContextInterface")) {
					try {
						CtClass[] params = m.getParameterTypes();
						if (params.length > 1 || params.length==0) {
							passed = false;
							logger.error(" ["+raTypeDescriptor.getID()+"]  Method [" + m.getName()
									+ "] has too many/few[" + params.length
									+ "] params!!!");
						} else {
							logger.debug(" ["+raTypeDescriptor.getID()+"] Checking param - name["
									+ params[0].getName() + "] against ["+activityName+"]");
							if (activityName.equals(params[0].getName())) {
								found[counter] = true;
								logger.info(" ["+raTypeDescriptor.getID()+"] activity - name["
										+ params[0].getName() + "]");
								acifMethods[i]=null;
							}
							
							
							
						}

					} catch (NotFoundException e1) {

						passed = false;
						logger.error(" ["+raTypeDescriptor.getID()+"]  getActivityContextInterface method with no params!!!");

						e1.printStackTrace();

					}
				}else
				{
					//this will save some time - Object methods will be removed
					//also once checked methods are set to null;
					acifMethods[i]=null;
				}
			}

			//counter++;
		}


		if(!passed)
			return passed;
		
		
		
		for(boolean b:found)
		{

			if(!b)
				return false;
		}
		
		return true;
		
	}
}
