/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.deployment;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.CMPField;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.GetChildRelationMethod;
import org.mobicents.slee.container.component.ProfileCMPMethod;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor;
import org.mobicents.slee.container.deployment.interceptors.DefaultChildRelationInterceptor;
import org.mobicents.slee.container.deployment.interceptors.DefaultFireEventInterceptor;
import org.mobicents.slee.container.deployment.interceptors.DefaultPersistenceInterceptor;
import org.mobicents.slee.container.deployment.interceptors.DefaultSBBProfileCMPInterceptor;
import org.mobicents.slee.container.deployment.interceptors.FireEventInterceptor;
import org.mobicents.slee.container.deployment.interceptors.SBBProfileCMPInterceptor;
import org.mobicents.slee.container.deployment.interceptors.UsageParameterInterceptor;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;
import org.mobicents.slee.runtime.sbb.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * Class generating the sbb concrete class from a sbb abstract class provided by
 * a sbb developer
 * 
 * 
 * @author DERUELLE Jean <a
 *         href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com </a>
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author Stefano Gamma
 *  
 */
public class ConcreteSbbGenerator {

    private static final String SBB_CHILDRELATION_INTERCEPTOR = "sbbChildRelationInterceptor";
    private static final String SBB_FIREEVENT_INTERCEPTOR = "sbbFireEventInterceptor";
    /**
     * The name of SBB Interceptor field that deals with SBB CMP fields
     */
    private static final String SBB_PERSISTENCE_INTERCEPTOR_FIELD = "sbbPersistenceInterceptor";
    
    /**
     * The name of SBB Interceptor field that deals with Profile CMP fields
     */
    private static final String SBB_PROFILE_CMP_INTERCEPTOR_FIELD = "sbbProfileCMPInterceptor";

    public static final String DEFAULT_USAGE_PARAMETER_SETTER = "sbbSetDefaultUsageParameter";

    public static final String NAMED_USAGE_PARAMETER_SETTER = "sbbSetNamedUsageParameterTable";

    /**
     * the sbb deployment descriptor used to generate the concrete class
     */
    private MobicentsSbbDescriptor sbbDeploymentDescriptor = null;

    /**
     * the sbb abstract class used to generate the concrete class
     */
    private CtClass sbbAbstractClass = null;

    /**
     * the sbb concrete class to generate
     */
    private CtClass sbbConcreteClass = null;

    /**
     * manager used to get the method call interceptors
     */
    private MappingManager mappingManager = null;

    /**
     * Logger to logg information
     */
    private static Logger logger = null;

    /**
     * the sbb abstract methods used to generate the concrete class methods
     */
    private Map abstractMethods = null;

    /**
     * Generator of the concrete activity context interface in case the sbb
     * developer specified a narrow method in th descriptor
     */
    private ConcreteActivityContextInterfaceGenerator concreteActivityContextInterfaceGenerator = null;

    /**
     * Pool to generate or read classes with javassist
     */
    private ClassPool pool = null;

    private Map superClassesAbstractMethods;

    /**
     * The path where DU classes will reside
     */
    private String deployPath;

    static {
        logger = Logger.getLogger(ConcreteSbbGenerator.class);
    }

    /**
     * Constructor
     */
    public ConcreteSbbGenerator(MobicentsSbbDescriptor sbbDeploymentDescriptor) {
        this.sbbDeploymentDescriptor = sbbDeploymentDescriptor;
        this.mappingManager = new MappingManager();

        // FIXME: the inderection to get to the deployment path is too high.
        // move the deployment path from the ID to the desciriptor
        this.deployPath = ((DeployableUnitIDImpl) (sbbDeploymentDescriptor
                .getDeployableUnit())).getDUDeployer().getTempClassDeploymentDir()
                .getAbsolutePath();

        this.pool = ((DeployableUnitIDImpl) sbbDeploymentDescriptor
                .getDeployableUnit()).getDUDeployer().getClassPool();
    }

    /**
     * Generate the concrete sbb Class
     * 
     * @return the concrete sbb class
     */
    public Class generateConcreteSbb() throws DeploymentException {
        String sbbAbstractClassName = sbbDeploymentDescriptor
                .getSbbAbstractClassName();
        String sbbConcreteClassName = ConcreteClassGeneratorUtils
                .getSbbConcreteClassName(sbbAbstractClassName);
        
        sbbConcreteClass = pool.makeClass(sbbConcreteClassName);
		        
        
		 try {
		 
            try {
                sbbAbstractClass = pool.get(sbbAbstractClassName);
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                return null;
            }
            try {
                ConcreteClassGeneratorUtils.createInterfaceLinks(
                        sbbConcreteClass, new CtClass[] { pool
                                .get(SbbConcrete.class.getName()) });
            } catch (NotFoundException nfe) {
                nfe.printStackTrace();
                return null;
            }

            ConcreteClassGeneratorUtils.createInheritanceLink(sbbConcreteClass,
                    sbbAbstractClass);
            createInterceptorFields();
            createDefaultConstructor();

            try {
                createFields(new CtClass[] {
                        pool.get(SbbEntity.class.getName()),
                        pool.get(SbbObjectState.class.getName()) });

                CtClass[] parameters = new CtClass[] { pool.get(SbbEntity.class
                        .getName()) };
                createStateGetterAndSetter(sbbConcreteClass);
                createInterceptors(sbbConcreteClass);
                createSbbEntityGetterAndSetter(sbbConcreteClass);
                createDefaultUsageParameterSetter(sbbConcreteClass);
                createDefaultUsageParameterGetter(sbbConcreteClass);
                createNamedUsageParameterSetter(sbbConcreteClass);
                createNamedUsageParameterGetter(sbbConcreteClass);
                createConstructorWithParameter(parameters);
            } catch (NotFoundException nfe) {
                logger.error("Constructor With Parameter not created");
                throw new DeploymentException("Constructor not created.", nfe);
            }
            abstractMethods = ClassUtils
                    .getAbstractMethodsFromClass(sbbAbstractClass);
            superClassesAbstractMethods = ClassUtils
                    .getSuperClassesAbstractMethodsFromClass(sbbAbstractClass);
            CMPField[] cmpFieldDescriptors = sbbDeploymentDescriptor
                    .getCMPFields();
            createCMPAccessors(cmpFieldDescriptors);
            GetChildRelationMethod[] childRelations = sbbDeploymentDescriptor
                    .getChildRelationMethods();
            createGetChildRelationsMethod(childRelations);
            ProfileCMPMethod[] cmpProfiles = sbbDeploymentDescriptor
                    .getProfileCMPMethods();
            createGetProfileCMPMethods(cmpProfiles);
            HashSet sbbEventEntries = sbbDeploymentDescriptor
                    .getSbbEventEntries();
            createFireEventMethods(sbbEventEntries);
            //GetUsageParametersMethod[] usageParameters=
            //  sbbDeploymentDescriptor.getUsageParametersMethods();

            //if the activity context interface has been defined in the
            // descriptor
            // file
            //then generates the concrete class of the activity context
            // interface
            //and implements the narrow method
            String activityContextInterfaceName = sbbDeploymentDescriptor
                    .getActivityContextInterfaceClassName();

            if (activityContextInterfaceName != null) {
                Class activityContextInterfaceClass = null;
                try {
                    activityContextInterfaceClass = Thread.currentThread()
                            .getContextClassLoader().loadClass(
                                    activityContextInterfaceName);
                } catch (ClassNotFoundException e2) {
                    String s = "Error creating constructor -  class not found";
                    logger.error(s, e2);
                    throw new DeploymentException(s, e2);
                }
                // Check the activity context interface for illegal method names.
                Method[] methods = activityContextInterfaceClass.getMethods();
                ArrayList<String> allSetters = new ArrayList<String>();
                ArrayList<String>  missingSetters = new ArrayList<String>();
                if (methods != null) {
                    for (int i = 0; i < methods.length; i++) {
                      if(!methods[i].getDeclaringClass().getName()
                          .equals("javax.slee.ActivityContextInterface"))
                      {
                          String methodName = methods[i].getName();
                          // setters should have a single parameter and should
                          // return void type.
                          if (methodName.startsWith("set")) {
                              Class[] args = methods[i].getParameterTypes();
                              
                              // setter should only have one argument
                              if (args.length != 1)
                                throw new DeploymentException("Setter method '" + 
                                    methodName + "' should only have one argument.");
                              
                              // setter return type should be void
                              Class returnClass = methods[i].getReturnType();
                              if (!returnClass.equals(Void.TYPE))
                                throw new DeploymentException("Setter method '" + 
                                    methodName + "' return type should be void.");
                              
                              allSetters.add(methodName);
                          } else if (methodName.startsWith("get")) {
                              Class[] args = methods[i].getParameterTypes();

                              // getter should have no parameters.
                              if (args != null && args.length != 0)
                                throw new DeploymentException("Getter method '" + 
                                    methodName + "' should have no parameters.");
                              
                              // getter return type should not be void
                              if (methods[i].getReturnType().equals(Void.TYPE))
                                throw new DeploymentException("Getter method '" + 
                                    methodName + "' return type cannot be void.");

                              String setterName = methodName
                                  .replaceFirst("get", "set");
                              
                              try {
                                activityContextInterfaceClass.getMethod(
                                    setterName, methods[i].getReturnType());
                              }
                              catch (NoSuchMethodException nsme) {
                                missingSetters.add(setterName);
                              }
                          } else {
                            throw new DeploymentException("Invalid method '" + 
                                methodName + "' in SBB Activity Context Interface.");
                          }
                      }

                    }
                    
                    // Check if the missing setters aren't defined with different arg
                    for(String setter : missingSetters)
                      if(allSetters.contains(setter))
                        throw new DeploymentException("Getter argument type and" +
                        		" setter return type for attribute '" + setter
                        		.replaceFirst( "set", "").toLowerCase() + 
                        		"' must be the same.");
                }
                /*
                 * CtMethod[] abstractClassMethods =
                 * sbbAbstractClass.getDeclaredMethods();
                 * 
                 * for ( int i = 0; i < abstractClassMethods.length; i ++ ) {
                 * CtMethod ctMethod = abstractClassMethods[i]; if ( !
                 * Modifier.isAbstract(ctMethod.getModifiers())) {
                 * this.createMethodWrapper(sbbConcreteClass,ctMethod); } }
                 */

                //check if the concrete class has already been generated.
                //if that the case, the guess is that the concrete class is a
                // safe
                // one
                //and so it is not generated again
                //avoid also problems of class already loaded from the class
                // loader
                //  

                CtClass activityContextInterface = null;
                try {
                	activityContextInterface = pool.get(activityContextInterfaceClass.getName());

                	createField(activityContextInterface,"sbbActivityContextInterface");

                	this.createSetActivityContextInterfaceMethod(activityContextInterface);

                	ConcreteActivityContextInterfaceGenerator concreteActivityContextInterfaceGenerator = new ConcreteActivityContextInterfaceGenerator(
                			sbbDeploymentDescriptor);
                	
                	Class concreteActivityContextInterfaceClass = concreteActivityContextInterfaceGenerator
                	.generateActivityContextInterfaceConcreteClass(activityContextInterfaceClass
                			.getName());
                	
                	createGetSbbActivityContextInterfaceMethod(
                			activityContextInterface,
                			concreteActivityContextInterfaceClass);
                	//set the concrete activity context interface class in
                	// the
                	// descriptor
                	if (logger.isDebugEnabled()) {
                		logger.debug("SETTING ACI concrete class  "
                				+ concreteActivityContextInterfaceClass
                				+ " in " + sbbDeploymentDescriptor);
                	}
                	sbbDeploymentDescriptor
                	.setActivityContextInterfaceConcreteClass(concreteActivityContextInterfaceClass);

                } catch (NotFoundException nfe) {
                	logger
                	.error("Narrow Activity context interface method and "
                			+ "activity context interface concrete class not created");
                	nfe.printStackTrace();
                } finally {
                	/*if (activityContextInterface != null) {
                		activityContextInterface.detach();
                	}*/
                }
                
            }
            //if the sbb local object has been defined in the descriptor file
            //then generates the concrete class of the sbb local object
            //and implements the narrow method
            Class sbbLocalInterfaceClass = sbbDeploymentDescriptor
                    .getSbbLocalInterface();
            if (logger.isDebugEnabled()) {
                logger.debug("Sbb Local Object interface :"
                    + sbbLocalInterfaceClass.getName());
            }
            if (sbbLocalInterfaceClass != null
                    && !sbbLocalInterfaceClass.getName().equals(
                            "javax.slee.SbbLocalObject")) {
                //check if the concrete class has already been generated.
                //if that the case, the guess is that the concrete class is a
                // safe
                // one
                //and so it is not generated again
                //avoid also problems of class already loaded from the class
                // loader
                if (true /* !SbbDeployer.concreteClassesGenerated
                        .contains(sbbAbstractClassName)*/ ) {
                    try {
                        CtClass sbbLocalInterface = pool
                                .get(sbbLocalInterfaceClass.getName());
                        ConcreteSbbLocalObjectGenerator concreteSbbLocalObjectGenerator = new ConcreteSbbLocalObjectGenerator(
                                this.sbbDeploymentDescriptor);
                        Class concreteSbbLocalObjectClass = concreteSbbLocalObjectGenerator
                                .generateSbbLocalObjectConcreteClass(
                                        this.deployPath, sbbLocalInterfaceClass
                                                .getName(),
                                        sbbAbstractClassName);
                        //set the sbb Local object class in the descriptor
                        sbbDeploymentDescriptor
                                .setLocalInterfaceConcreteClass(concreteSbbLocalObjectClass);

                    } catch (NotFoundException nfe) {
                        String s = "sbb Local Object concrete class not created for interface "
                                + sbbLocalInterfaceClass.getName();
                        logger.error(s, nfe);
                        throw new RuntimeException(s, nfe);

                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(sbbLocalInterfaceClass.getName()
                            + " concrete class already "
                            + "generated. No generated a second time.");
                    }
                }
            }
            //if there is no interface defined in the descriptor for sbb local
            // object
            //then the slee implementation is taken
            else {

                try {
                    sbbDeploymentDescriptor
                            .setSbbLocalInterfaceClassName("javax.slee.SbbLocalObject");
                    sbbDeploymentDescriptor
                            .setLocalInterfaceConcreteClass(SbbLocalObjectImpl.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
            	sbbConcreteClass.writeFile(deployPath);            	
            	//@@2.4+ -> 3.4+
                //pool.writeFile(sbbConcreteClassName, deployPath);
                if (logger.isDebugEnabled()) {
                    logger.debug("Concrete Class " + sbbConcreteClassName
                        + " generated in the following path " + deployPath);
                }
            } catch (Exception e) {
                String s = "Error generating concrete class";
                logger.error(s, e);
                throw new RuntimeException(s, e);
            }

            Class clazz = null;
            try {                
                clazz = Thread.currentThread().getContextClassLoader().loadClass(sbbConcreteClassName);
            } catch (ClassNotFoundException e1) {
                String s = "What the heck?! Could not find generated class. Is it under the chair?";
                logger.error(s, e1);
                throw new RuntimeException(s, e1);
            }
            //set the concrete class in the descriptor
            sbbDeploymentDescriptor.setConcreteSbb(clazz);
                        
            return clazz;
		 } finally {
			 if(sbbConcreteClass != null) {
				 sbbConcreteClass.defrost();

			 }
		 }
    }

    /**
     * @param class1
     * @param string
     */
    private void createField(CtClass parameter, String parameterName) {
        // TODO Auto-generated method stub
        try {
            CtField ctField = new CtField(parameter, parameterName,
                    sbbConcreteClass);
            ctField.setModifiers(Modifier.PRIVATE);
            sbbConcreteClass.addField(ctField);
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        }
    }

    /**
     * Creates a constructor with parameters <BR>
     * For every parameter a field of the same class is created in the concrete
     * class And each field is gonna be initialized with the corresponding
     * parameter
     * 
     * @param parameters
     *            the parameters of the constructor to add
     */
    protected void createConstructorWithParameter(CtClass[] parameters) {
        CtConstructor constructorWithParameter = new CtConstructor(parameters,
                sbbConcreteClass);
        String constructorBody = "{" + "this();";
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = parameters[i].getName();
            parameterName = parameterName.substring(parameterName
                    .lastIndexOf(".") + 1);
            String firstCharLowerCase = parameterName.substring(0, 1)
                    .toLowerCase();
            parameterName = firstCharLowerCase.concat(parameterName
                    .substring(1));

            int paramNumber = i + 1;
            constructorBody += parameterName + "=$" + paramNumber + ";";
        }
        constructorBody += "createInterceptors();";
        constructorBody += "this.setSbbEntity($1);";
        constructorBody += "}";
        try {
            sbbConcreteClass.addConstructor(constructorWithParameter);
            constructorWithParameter.setBody(constructorBody);
            if (logger.isDebugEnabled()) {
                logger.debug("ConstructorWithParameter created");
            }
        } catch (CannotCompileException e) {
            //Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createFields(CtClass[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = parameters[i].getName();
            parameterName = parameterName.substring(parameterName
                    .lastIndexOf(".") + 1);
            String firstCharLowerCase = parameterName.substring(0, 1)
                    .toLowerCase();
            parameterName = firstCharLowerCase.concat(parameterName
                    .substring(1));
            try {
                CtField ctField = new CtField(parameters[i], parameterName,
                        sbbConcreteClass);
                ctField.setModifiers(Modifier.PRIVATE);
                sbbConcreteClass.addField(ctField);
            } catch (CannotCompileException cce) {
                cce.printStackTrace();
            }
        }
    }

    /**
     * Create a default constructor on the Sbb Concrete Class
     */
    protected void createDefaultConstructor() {

        CtConstructor defaultConstructor = new CtConstructor(null,
                sbbConcreteClass);
        // We need a "do nothing" constructor because the
        // convergence name creation method may need to actually
        // create the object instance to run the method that
        // creates the convergence name.

        String constructorBody = "{}";

        try {
            defaultConstructor.setBody(constructorBody);
            sbbConcreteClass.addConstructor(defaultConstructor);
            logger.debug("DefaultConstructor created");
        } catch (CannotCompileException e) {
            //Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Create the interceptors in the concrete class It creates 4 interceptors
     * for the different method calls:
     * persistenceInterceptor,FireEventInterceptor,ChildRelationInterceptor and
     * UsageParameterInterceptor
     */
    protected void createInterceptorFields() {
        //TODO Get the interceptors from a xml file
        //Add the persistence Manager
        CtField persistenceInterceptor = null;
        try {
            persistenceInterceptor = new CtField(
                    pool
                            .get(org.mobicents.slee.container.deployment.interceptors.PersistenceInterceptor.class
                                    .getName()), SBB_PERSISTENCE_INTERCEPTOR_FIELD,
                    sbbConcreteClass);
            persistenceInterceptor.setModifiers(Modifier.PRIVATE);
            sbbConcreteClass.addField(persistenceInterceptor);
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
        }
        //Add the fire event Interceptor
        CtField fireEventInterceptor = null;
        try {
            fireEventInterceptor = new CtField(
                    pool
                            .get(FireEventInterceptor.class.getName()),
                    SBB_FIREEVENT_INTERCEPTOR, sbbConcreteClass);
            fireEventInterceptor.setModifiers(Modifier.PRIVATE);
            sbbConcreteClass.addField(fireEventInterceptor);
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
        }
        //Add the child relation Interceptor
        CtField childRelationInterceptor = null;
        try {
            childRelationInterceptor = new CtField(
                    pool
                            .get(ChildRelationInterceptor.class.getName()),
                    SBB_CHILDRELATION_INTERCEPTOR, sbbConcreteClass);
            childRelationInterceptor.setModifiers(Modifier.PRIVATE);
            sbbConcreteClass.addField(childRelationInterceptor);
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
        }
        //Add the Usage parameter Interceptor
        CtField usageParameterInterceptor = null;
        try {
            usageParameterInterceptor = new CtField(
                    pool
                            .get(UsageParameterInterceptor.class.getName()),
                    "sbbUsageParameterInterceptor", sbbConcreteClass);
            usageParameterInterceptor.setModifiers(Modifier.PRIVATE);
            sbbConcreteClass.addField(usageParameterInterceptor);
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
        }
        //Add the Usage parameter Interceptor
        CtField profileCMPInterceptor = null;
        try {
            profileCMPInterceptor = new CtField(
                    pool.get(SBBProfileCMPInterceptor.class.getName()),
                            SBB_PROFILE_CMP_INTERCEPTOR_FIELD, sbbConcreteClass);
            profileCMPInterceptor.setModifiers(Modifier.PRIVATE);
            sbbConcreteClass.addField(profileCMPInterceptor);
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
        }
    }

    /**
     * Create the cmp fields and their accessors (getter and setter)
     * 
     * @param cmpAccessors
     *            the description of the cmp fields
     */
    protected void createCMPAccessors(CMPField[] cmpAccessors) {
        if (cmpAccessors == null)
            return;
        //Create the concrete implemntation of the accessors
        for (int i = 0; i < cmpAccessors.length; i++) {
            String fieldName = cmpAccessors[i].getFieldName();
            //Set the first char of the accessor to UpperCase to follow the
            // javabean requirements
            fieldName = fieldName.substring(0, 1).toUpperCase()
                    + fieldName.substring(1);

            CtMethod setterAccessor = (CtMethod) abstractMethods.get("set"
                    + fieldName);
            CtMethod getterAccessor = (CtMethod) abstractMethods.get("get"
                    + fieldName);
            if (setterAccessor == null)
                setterAccessor = (CtMethod) this.superClassesAbstractMethods.get("set"
                        + fieldName);
            if (getterAccessor == null)
                getterAccessor = (CtMethod) this.superClassesAbstractMethods.get("get"
                        + fieldName);
            if (setterAccessor != null)
                ConcreteClassGeneratorUtils.addInterceptedMethod(
                        sbbConcreteClass, setterAccessor,
                        SBB_PERSISTENCE_INTERCEPTOR_FIELD, false);
            if (getterAccessor != null)
                ConcreteClassGeneratorUtils.addInterceptedMethod(
                        sbbConcreteClass, getterAccessor,
                        SBB_PERSISTENCE_INTERCEPTOR_FIELD, false);
        }
        //Create the persistent state of the sbb
        createPersistentStateHolderClass(cmpAccessors);
    }

    /**
     * Create methods to set and get the Sbb Object state. This avoids the need
     * for another wrapper object If we think of doing something fancy like
     * writing the sbb out to disk, its current state needs to be saved also. It
     * will be handy to keep it here. Note that there can be no CMP fields that
     * start with Sbb Or EJB so we are ok.
     *  
     */
    private void createStateGetterAndSetter(CtClass sbbConcrete) {
        try {

            CtMethod getSbbState = CtNewMethod.make("public "
                    + SbbObjectState.class.getName()
                    + " getState() { return sbbObjectState; }", sbbConcrete);
            getSbbState.setModifiers(Modifier.PUBLIC);
            sbbConcrete.addMethod(getSbbState);
            CtMethod setSbbState = CtNewMethod.make("public void setState ( "
                    + SbbObjectState.class.getName()
                    + " state ) { this.sbbObjectState = state; }", sbbConcrete);
            getSbbState.setModifiers(Modifier.PUBLIC);
            sbbConcrete.addMethod(setSbbState);
        } catch (Exception ex) {
            logger.fatal("unexpected exception ", ex);
        }
    }

    /**
     * Create a default usage parameter getter and setter.
     * 
     * @param sbbConcrete
     */
    private void createDefaultUsageParameterGetter(CtClass sbbConcrete) {
        String usageParameterInterfaceName = this.sbbDeploymentDescriptor
                .getUsageParametersInterface();
        if (usageParameterInterfaceName == null)
            return;
        try {
            CtMethod getDefaultUsageParameter = CtNewMethod.make("public "
                    + usageParameterInterfaceName
                    + " getDefaultSbbUsageParameterSet( ) { " +

                    "return this.defaultUsageParameterSet;" +

                    "}", sbbConcrete);
            getDefaultUsageParameter.setModifiers(Modifier.PUBLIC);
            sbbConcrete.addMethod(getDefaultUsageParameter);
        } catch (Exception ex) {
            logger.fatal("Unexpected exception ", ex);
        }

    }

    /**
     * Create a setter for the default usage parameter
     * 
     * @param sbbConcrete
     */
    private void createDefaultUsageParameterSetter(CtClass sbbConcrete) {
        String usageParameterInterfaceName = this.sbbDeploymentDescriptor
                .getUsageParametersInterface();
        if (usageParameterInterfaceName == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No usage parameter interface for this sbb.");
            }
            return;
        }
        try {
            CtField defaultUsageParameter = new CtField(pool
                    .get(usageParameterInterfaceName),
                    "defaultUsageParameterSet", sbbConcreteClass);
            defaultUsageParameter.setModifiers(Modifier.PRIVATE);
            sbbConcreteClass.addField(defaultUsageParameter);
            CtMethod setDefaultUsageParameter = CtNewMethod.make(
                    "public void  " + DEFAULT_USAGE_PARAMETER_SETTER + "("
                            + usageParameterInterfaceName
                            + " defaultUsageParameter ) { " +

                            "this.defaultUsageParameterSet = defaultUsageParameter ;"
                            +

                            "}", sbbConcrete);
            setDefaultUsageParameter.setModifiers(Modifier.PUBLIC);
            sbbConcrete.addMethod(setDefaultUsageParameter);
        } catch (Exception ex) {
            logger.fatal("Unexpected exception ", ex);
        }
    }

    /**
     * Create a way to export the hash table where the service container stores
     * named sbb usage parameters.
     * 
     * @param sbbConcrete
     */
    
    private void createNamedUsageParameterSetter(CtClass sbbConcrete) {
        try {
            CtField ctField = new CtField(pool.get(Map.class.getName()),
                    "usageParameterTable", sbbConcrete);
            ctField.setModifiers(Modifier.PRIVATE);
            sbbConcrete.addField(ctField);
            CtMethod setNamedUsageParameterTable = CtNewMethod.make(
                    "public void " + NAMED_USAGE_PARAMETER_SETTER + " ( "
                            + Map.class.getName() + " usageParamTable )  {"
                            + "this.usageParameterTable = usageParamTable; }",
                    sbbConcrete);
            sbbConcrete.addMethod(setNamedUsageParameterTable);
        } catch (Exception ex) {
            String s = "Unexpected exception in setNamedUsageParameterTable generation";
            logger.fatal(s, ex);
        }
    }
    

    /**
     * Create a named usage parameter getter.
     * 
     * @param sbbConcrete
     */
    
    private void createNamedUsageParameterGetter(CtClass sbbConcrete) {
        String usageParameterInterfaceName = this.sbbDeploymentDescriptor
                .getUsageParametersInterface();
        if (usageParameterInterfaceName == null)
            return;
        try {
            CtMethod getDefaultUsageParameter = CtNewMethod.make("public "
                    + usageParameterInterfaceName
                    + " getSbbUsageParameterSet( String name ) "
                    + "throws "
                    + UnrecognizedUsageParameterSetNameException.class
                            .getName() + " { "
                    + "if(this.usageParameterTable.get( sbbEntity.getUsageParameterPathName(name))"+ 
                    	" ==null) throw new "
                    + UnrecognizedUsageParameterSetNameException.class.getName()
                    + "(\"Usage Parameter Set \" + name +  \" does not exist!\");"
                    + "return " + "("
                    + usageParameterInterfaceName
                    + ") this.usageParameterTable.get(sbbEntity.getUsageParameterPathName(name)) ;"+ 
                      " } ",
                    sbbConcrete);
            getDefaultUsageParameter.setModifiers(Modifier.PUBLIC);
            sbbConcrete.addMethod(getDefaultUsageParameter);
        } catch (Exception ex) {
            String s = "Unexpected exception in createNamedUsageParameterGetter";
            logger.fatal(s, ex);
            throw new RuntimeException(s, ex);
        }

    }
    

    /**
     * Create a method wrapper. This sets the classloader before invoking the
     * method in the parent class.
     * 
     * @param sbbConcrete
     * @param methodToWrap
     */
    private void createMethodWrapper(CtClass sbbConcrete, CtMethod methodToWrap) {
        try {
            String methodName = methodToWrap.getName();
            CtClass[] params = methodToWrap.getParameterTypes();
            String returnType = null;
            if (methodToWrap.getReturnType().equals(CtClass.voidType)) {
                returnType = "void";
            } else {
                returnType = methodToWrap.getReturnType().getName();
            }

            String args = "";
            for (int i = 0; i < params.length; i++) {
                args += params[i].getName() + " " + "param" + i;
                if (i < params.length - 1)
                    args += ",";
            }
            CtClass[] exceptions = methodToWrap.getExceptionTypes();

            String wrapper = "public " + returnType + " " + methodName + "("
                    + args + ") ";

            if (exceptions.length > 0)
                wrapper += SLEEException.class.getName() + " , ";

            for (int i = 0; i < exceptions.length; i++) {
                wrapper += exceptions[i].getName();
                if (i < exceptions.length - 1)
                    wrapper += ",";
            }

            wrapper += "   {  "
                	+ "sbbEntity.checkReEntrant();"
                	+ MobicentsSbbDescriptor.class.getName()
                	+ "sbbDescriptor  =  sbbEntity.getSbbDescriptor();"
                    + ClassLoader.class.getName()
                    + " oldClassLoader =  Thread.currentThread().setContextClassLoader(sbbDescriptor.getClassLoader());";
            wrapper += " try { ";
            if (!returnType.equals("void")) {
                wrapper += "return ";
            }
            wrapper += "super." + methodName + "(";
            for (int i = 0; i < params.length; i++) {
                wrapper += "param" + i;
                if (i < params.length - 1)
                    wrapper += ",";
            }

            wrapper += "); "
                    + "} finally { Thread.currentThread().setContextClassLoader(oldClassLoader);  }"
                    + "}";

            logger.debug("wrapper generated = " + wrapper);

            CtMethod method = CtNewMethod.make(wrapper, sbbConcrete);
            sbbConcrete.addMethod(method);
        } catch (Exception ex) {
            String s = "Unexpected error in createMethodWrapper";
            logger.fatal(s, ex);
            throw new RuntimeException(s, ex); // naughty boy! tut tut!!
        }

    }

    private void createInterceptors(CtClass sbbConcrete) {
        try {
            String body = "public void createInterceptors ( ) { ";
            body += SBB_PERSISTENCE_INTERCEPTOR_FIELD +" = new "
                    + DefaultPersistenceInterceptor.class.getName() + "();";
            body += SBB_FIREEVENT_INTERCEPTOR + " = new "
                    + DefaultFireEventInterceptor.class.getName() + "();";
            body += SBB_CHILDRELATION_INTERCEPTOR + " =new "
                    + DefaultChildRelationInterceptor.class.getName() + "();";
            body += SBB_PROFILE_CMP_INTERCEPTOR_FIELD + " = new "
                + DefaultSBBProfileCMPInterceptor.class.getName() + "();";
            body += "}";
            CtMethod createInterceptors = CtNewMethod.make(body, sbbConcrete);
            sbbConcrete.addMethod(createInterceptors);
        } catch (Exception ex) {
            String s = "Unexpected error createInterceptors";
            logger.fatal(s, ex);
            throw new RuntimeException(s, ex);
        }
    }

    /**
     * Create a method to retrive the entity from the SbbObject.
     * 
     * @param cmpAccessors
     */
    private void createSbbEntityGetterAndSetter(CtClass sbbConcrete) {
        try {
            CtClass sbbEntityClass = pool.get(SbbEntity.class.getName());

            CtMethod getSbbEntity = CtNewMethod
                    .make("public " + SbbEntity.class.getName()
                            + " getSbbEntity() { return this.sbbEntity; }",
                            sbbConcrete);
            getSbbEntity.setModifiers(Modifier.PUBLIC);
            sbbConcrete.addMethod(getSbbEntity);
            CtMethod setSbbEntity = CtNewMethod
                    .make(
                            "public void setSbbEntity ( "
                                    + SbbEntity.class.getName()
                                    + " sbbEntity )"
                                    + "{"
                                    + "this.createInterceptors();"
                                    + "this.sbbEntity = sbbEntity;"
                                    + SBB_PERSISTENCE_INTERCEPTOR_FIELD + ".setSbbEntity(sbbEntity);"
                                    + SBB_FIREEVENT_INTERCEPTOR + ".setSbbEntity(sbbEntity);"
                                    + SBB_CHILDRELATION_INTERCEPTOR + ".setSbbEntity(sbbEntity);"
                                    + SBB_PROFILE_CMP_INTERCEPTOR_FIELD + ".setSbbEntity(sbbEntity);"
                                    + "}", sbbConcrete);

            setSbbEntity.setModifiers(Modifier.PUBLIC);
            sbbConcrete.addMethod(setSbbEntity);
        } catch (Exception ex) {
            logger.fatal("unexpected exception ", ex);
        }
    }

    /**
     * Create the persistent object that will hold the persistent state of the
     * sbb
     * 
     * @param cmpAccessors
     *            the description of the cmp fields
     */
    protected void createPersistentStateHolderClass(CMPField[] cmpAccessors) {
        //Create the class of the persistent state of the sbb
    	
        CtClass sbbPersisentStateClass=pool.makeClass(sbbAbstractClass.getName() + "PersistentState");
        
        
		try {
		
            try {
                //Make the persistent state serializable : this is mandatory by
                // the
                // slee spec
                ConcreteClassGeneratorUtils.createInterfaceLinks(
                        sbbPersisentStateClass, new CtClass[] { pool
                                .get("java.io.Serializable") });
            } catch (NotFoundException e1) {
                // Auto-generated catch block
                e1.printStackTrace();
            }
            //Create the fields of the sbb persistent state class
            for (int i = 0; i < cmpAccessors.length; i++) {
                String fieldName = cmpAccessors[i].getFieldName();
                //Set the first char of the accessor to UpperCase to follow the
                // javabean requirements
                fieldName = fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);

                CtMethod accessor = (CtMethod) abstractMethods.get("get"
                        + fieldName);
                if (accessor == null)
                    accessor = (CtMethod) abstractMethods
                            .get("set" + fieldName);
                if (accessor == null)
                    accessor = (CtMethod) this.superClassesAbstractMethods
                    .get("set" + fieldName);
                // FIXME if the abstract method is not define the following code will 
                // throw a NullPointerException
                CtField persistentField = null;
                try {
                    persistentField = new CtField(accessor.getReturnType(),
                            "persistentState" + fieldName,
                            sbbPersisentStateClass);
                    persistentField.setModifiers(Modifier.PUBLIC);
                    sbbPersisentStateClass.addField(persistentField);
                } catch (CannotCompileException cce) {
                    cce.printStackTrace();
                } catch (NotFoundException nfe) {
                    nfe.printStackTrace();
                }
            }
            //generate the persistent state class of the sbb
            try {
            	sbbPersisentStateClass.writeFile(deployPath);            	
            	//@@2.4+ -> 3.4+
                //pool.writeFile(sbbAbstractClass.getName() + "PersistentState",deployPath);
                if (logger.isDebugEnabled()) {
                    logger.debug("Concrete Class " + sbbAbstractClass.getName()
                        + "PersistentState"
                        + " generated in the following path " + deployPath);
                }            
            } catch (CannotCompileException e) {
                // Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            // release from javassist pool to allow consequent manipulation
            sbbPersisentStateClass.defrost();
        }
    }

    /**
     * Create the implementation of the fire event methods
     * 
     * @param firedEvents
     *            the set of fire event
     */
    protected void createFireEventMethods(HashSet firedEvents) {
        if (firedEvents == null)
            return;
        Iterator it = firedEvents.iterator();
        //firedEvents
        while (it.hasNext()) {
            SbbEventEntry sbbEventEntry = (SbbEventEntry) it.next();
            if ( sbbEventEntry.isFired() )  {
	            String methodName = "fire" + sbbEventEntry.getEventName();
	            CtMethod method = (CtMethod) abstractMethods.get(methodName);
	            if  ( method == null ) {
	            	method = (CtMethod) superClassesAbstractMethods.get(methodName);
	            }
	            if ( method != null ) {
	                ConcreteClassGeneratorUtils.addInterceptedMethod(
	                        sbbConcreteClass, method, SBB_FIREEVENT_INTERCEPTOR,
	                        false);
	            }
            }
        }
    }

    /**
     * Create the get child relation method (this method redirects the call to a
     * child relation interceptor)
     * 
     * @param childRelations
     *            the childRelations method to add to the concrete class
     */
    protected void createGetChildRelationsMethod(
            GetChildRelationMethod[] childRelations) {
        if (childRelations == null)
            return;
        for (int i = 0; i < childRelations.length; i++) {
            String methodName = childRelations[i].getMethodName();
            CtMethod method = (CtMethod) abstractMethods.get(methodName);
            CtMethod superClassMethod = (CtMethod) this.superClassesAbstractMethods
                    .get(methodName);
            if (method != null)
                ConcreteClassGeneratorUtils.addInterceptedMethod(
                        sbbConcreteClass, method,
                        SBB_CHILDRELATION_INTERCEPTOR, false);
            else if (superClassMethod != null) {
                ConcreteClassGeneratorUtils.addInterceptedMethod(
                        sbbConcreteClass, superClassMethod,
                        SBB_CHILDRELATION_INTERCEPTOR, false);
            }
        }
    }

    /**
     * Create the get profile CMP method (this method redirects the call to a
     * profile cmp interceptor)
     * 
     * @param cmpProfiles
     *            the CMP Profiles method to add to the concrete class
     */
    protected void createGetProfileCMPMethods(ProfileCMPMethod[] cmpProfiles) {
        if (cmpProfiles == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("no CMP Profile method implementation to generate.");
            }
            return;
        }
        for (int i = 0; i < cmpProfiles.length; i++) {
            String methodName = cmpProfiles[i].getProfileCMPMethod();
            CtMethod method = (CtMethod) abstractMethods.get(methodName);
            if ( method == null )
            	method = (CtMethod) superClassesAbstractMethods.get(methodName);
            if (method != null)
                ConcreteClassGeneratorUtils.addInterceptedMethod(
                        sbbConcreteClass, method, SBB_PROFILE_CMP_INTERCEPTOR_FIELD,
                        false);
        }
    }

    protected void createSetActivityContextInterfaceMethod(
            CtClass activityContextInterface) {
        String methodToAdd = "public void sbbSetActivityContextInterface( Object aci ) {"
                + "this.sbbActivityContextInterface = ("
                + activityContextInterface.getName() + ") aci ; } ";
        CtMethod methodTest;
        try {
            methodTest = CtNewMethod.make(methodToAdd, sbbConcreteClass);
            sbbConcreteClass.addMethod(methodTest);
            logger.debug("Method " + methodToAdd + " added");
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        }
    }

    /**
     * Create the narrow method to get the activity context interface
     * 
     * @param activityContextInterface
     *            the activity context interface return type of the narrow
     *            method
     * @param concreteActivityContextInterfaceClass
     */
    protected void createGetSbbActivityContextInterfaceMethod(
            CtClass activityContextInterface,
            Class concreteActivityContextInterfaceClass) {
        String methodToAdd = "public "
                + activityContextInterface.getName()
                + " asSbbActivityContextInterface(javax.slee.ActivityContextInterface aci) {"
                + "if(aci==null)" 
                +"     throw new "+IllegalStateException.class.getName()+"(\"Passed argument can not be of null value.\");"
                + " if(sbbEntity == null || sbbEntity.getSbbObject().getState() != "
                + SbbObjectState.class.getName() + ".READY) { throw new "
                + IllegalStateException.class.getName()
                + "(\"Cannot call asSbbActivityContextInterface\"); } "
                + "else if ( aci instanceof "
                + concreteActivityContextInterfaceClass.getName()
                + ") return aci;" + "else return  new "
                + concreteActivityContextInterfaceClass.getName() + " ( ( "
                + ActivityContextInterfaceImpl.class.getName() + ")$1, "
                + "sbbEntity.getSbbDescriptor());" + "}";
        CtMethod methodTest;
        try {
            methodTest = CtNewMethod.make(methodToAdd, sbbConcreteClass);
            sbbConcreteClass.addMethod(methodTest);
            if (logger.isDebugEnabled()) {
                logger.debug("Method " + methodToAdd + " added");
            }
        } catch (CannotCompileException cce) {
            cce.printStackTrace();
        }
    }

    /**
     * 
     * @param usageParameters
     */
    /*
     * protected void createSbbUsageParameterInterface(
     * GetUsageParametersMethod[] usageParameters){ }
     */
}