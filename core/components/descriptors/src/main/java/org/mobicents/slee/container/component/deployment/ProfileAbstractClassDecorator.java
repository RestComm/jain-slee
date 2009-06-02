package org.mobicents.slee.container.component.deployment;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileAbstractClass;

/**
 * Class decorating the profile abstract class. 
 * @author martins
 *  
 */
public class ProfileAbstractClassDecorator {

	private static final Logger logger = Logger.getLogger(ProfileAbstractClassDecorator.class);
	
    /**
     * the abstract class
     */
    private CtClass ctClass = null;    

    private final ProfileSpecificationComponent component;

    /**
     * Constructor
     */
    public ProfileAbstractClassDecorator(ProfileSpecificationComponent component) {
        this.component = component;
    }

    /**
     * Decorate the abstract Class
     * 
     * @return true is the class was decorated
     */
    public boolean decorateAbstractClass() throws DeploymentException {
       
    	ClassPool pool = component.getClassPool();
    	MProfileAbstractClass abstractClass = component.getDescriptor().getProfileAbstractClass();
    	if (abstractClass == null) {
    		return false;
    	}
    	String abstractClassName = abstractClass.getProfileAbstractClassName();
        try {
            ctClass = pool.get(abstractClassName);
        } catch (NotFoundException nfe) {
            throw new DeploymentException("Could not find Abstract Class: "
                    + abstractClassName, nfe);
        }

        decorateClassJNDIAddToEnvironmentCalls();
        
        if (isAbstractClassDecorated) {
        	try {
        		String deployDir = component.getDeploymentDir().getAbsolutePath();
        		ctClass.writeFile(deployDir);
        		ctClass.detach();
        		// the file on disk is now in sync with the latest in-memory version
        		if (logger.isDebugEnabled()) {
        			logger.debug("Modified Abstract Class "
        					+ ctClass.getName()
        					+ " generated in the following path "
        					+ deployDir);
        		}        		
        	} catch (Throwable e) {
        		throw new SLEEException ( e.getMessage(), e);                
        	} finally { 
        		ctClass.defrost();
        	}
        	return true;
        }
        else {
        	return false;
        }
    }

   /**
    * 
    * @return
    */
    private void decorateClassJNDIAddToEnvironmentCalls() {
        for (CtMethod ctMethod : ctClass.getMethods()) {
    		if (!Modifier.isAbstract(ctMethod.getModifiers()) && !Modifier.isNative(ctMethod.getModifiers()) && !ctMethod.getDeclaringClass().getName().equals(Object.class.getName())) {
    			try {
    				decorateMethodJNDIAddToEnvironmentCalls(ctMethod);
                } catch (CannotCompileException e) {
                    throw new SLEEException(e.getMessage(),e);
                }
    		}
        } 
    }

    private void decorateMethodJNDIAddToEnvironmentCalls(CtMethod method)
            throws CannotCompileException {
        method.instrument(new ExprEditor() {
    
            public void edit(MethodCall m) throws CannotCompileException {
                // the call has to be on a JNDI Context object
                boolean isJndiCtx = false;
                try {
                    // see if the method call is on javax.naming.Context or a derived type
                    if (m.getClassName().equals(javax.naming.Context.class.getName())) {
                        isJndiCtx = true;
                    } else {
                        CtClass cl0 = component.getClassPool().get(m.getClassName());
                        CtClass[] cl0Interfaces = cl0.getInterfaces();
                        for (int i = 0; i < cl0Interfaces.length; i++) {
                            CtClass cl0If = cl0Interfaces[i];
                            if (cl0If.getName().equals(javax.naming.Context.class.getName())) {
                                isJndiCtx = true;
                                break;
                            }
                        }
                    }
                } catch (NotFoundException e) {
                	// The class is not in the pool so ignore it
                	return;
                    //throw new CannotCompileException(e);
                }
                if (!isJndiCtx)
                    return;
    
                if (m.getMethodName().endsWith("bind") || m.getMethodName().equals("createSubcontext") ) {
                    m.replace("{ throw new javax.naming.OperationNotSupportedException(); $_ = $proceed($$); }");
                    isAbstractClassDecorated = true;
                }
                
                else if (m.getMethodName().equals("addToEnvironment")) {
                    m.replace("{ throw new javax.naming.NamingException(); $_ = $proceed($$); }");
                    isAbstractClassDecorated = true;
                }
            }
        });
    } 

    boolean isAbstractClassDecorated = false;
    
}