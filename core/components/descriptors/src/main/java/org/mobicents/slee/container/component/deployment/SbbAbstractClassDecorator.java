/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

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
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;

/**
 * Class decorating the sbb abstract class. The byte code is modified IF
 * necessary to trap SBB code that violates the SLEE spec: 
 * <pre>
 * 	1) write access to java:comp/env 
 *  2) new Thread()
 * </pre>
 * TODO: Currently we check the SBB abstract class itself, but not its
 * ascendands or utility classes it uses.
 * 
 * @author Ivelin Ivanov
 *  
 */
public class SbbAbstractClassDecorator {

    /**
     * the sbb abstract class
     */
    private CtClass sbbAbstractClass = null;

    /**
     * Logger to logg information
     */
    private static Logger logger = null;

    /**
     * the sbb concrete methods written by the SBB developer
     */
    private Map concreteMethods = null;

    private final SbbComponent component;

    /**
     * Optimization variable. Helps avoid writing to disk abstract classes, which are not modified.
     */
    private boolean isAbstractSbbClassDecorated = false;
    
    static {
        logger = Logger.getLogger(SbbAbstractClassDecorator.class);
    }

    /**
     * Constructor
     */
    public SbbAbstractClassDecorator(SbbComponent component) {
        this.component = component;
    }

    /**
     * Decorate the abstract sbb Class
     * 
     * @return the decorated sbb abstract class
     */
    public boolean decorateAbstractSbb() throws DeploymentException {
       
    	ClassPool pool = component.getClassPool();
    	String sbbAbstractClassName = component.getDescriptor().getSbbAbstractClass().getSbbAbstractClassName();
        try {
            sbbAbstractClass = pool.get(sbbAbstractClassName);
        } catch (NotFoundException nfe) {
            throw new DeploymentException("Could not find Abstract Sbb Class: "
                    + sbbAbstractClassName, nfe);
        }

        // populate the list of concrete methods. It will be needed by the
        // decorating methods.
        concreteMethods = new HashMap();
    	CtMethod[] methods = sbbAbstractClass.getDeclaredMethods();
    	for (int i = 0; i < methods.length; i++) {
    	    int mods = methods[i].getModifiers();
    		if (!Modifier.isAbstract(mods) && !Modifier.isNative(mods)) {
    			concreteMethods.put(methods[i].getName() + methods[i].getSignature(), methods[i]);
    		}
    	}

        decorateENCBindCalls();
        decorateNewThreadCalls();
        
        if (isAbstractSbbClassDecorated) {
        	try {
        		String deployDir = component.getDeploymentDir().getAbsolutePath();
        		sbbAbstractClass.writeFile(deployDir);
        		sbbAbstractClass.detach();
        		// the file on disk is now in sync with the latest in-memory version
        		if (logger.isDebugEnabled()) {
        			logger.debug("Modified Abstract Class "
        					+ sbbAbstractClass.getName()
        					+ " generated in the following path "
        					+ deployDir);
        		}
        		//} catch (NotFoundException e) {
        		//    String s = "Error writing modified abstract sbb class";
        		//    logger.error(s,e);
        		//    throw new DeploymentException (s,e);
        	} catch (Throwable e) {
        		throw new SLEEException ( e.getMessage(), e);                
        	} finally { 
        		sbbAbstractClass.defrost();
        	}
        	return true;
        }
        else {
        	return false;
        }
    }

    /**
     * 
     * If there are new Thread() calls, replace with exception.
     * 
     * Required by SLEE 1.0 spec.
     * 
     *  
     */
    private void decorateNewThreadCalls() {
        // TODO: Find which TCK test shows the required functionality.
    }

    /**
     * 
     * If there are bind calls to ENC, replace with
     * OperationNotSupportedException exception.
     * 
     * Required by SLEE 1.0 spec.
     * 
     * @see com.opencloud.sleetck.lib.testsuite.sbb.abstractclass.Test522Test
     *  
     */
    private void decorateENCBindCalls() {
        Iterator iter = concreteMethods.values().iterator();
        while (iter.hasNext()) {
            CtMethod method = (CtMethod) iter.next();
            try {
                trapMethodIfENCBindCall(method);
            } catch (CannotCompileException e) {
                throw new SLEEException(
                        "Could not decorate ENC bind calls for sbb abstract class: "
                                + sbbAbstractClass, e);
            }
        }
    }

    private void trapMethodIfENCBindCall(CtMethod method)
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
    
                /*
                 * trap calls to Contenxt bind, unbind, rebind
                 */
                if (m.getMethodName().endsWith("bind") || m.getMethodName().equals("createSubcontext")) {
                    m.replace("{ "
                                + "String fullJndiName = $0.composeName($1, $0.getNameInNamespace());"
                                + "System.err.println(\"TRAPPED SBB METHOD - JNDI BIND CALL; fullJndiName: \" + fullJndiName);"
                                + "if (fullJndiName.startsWith(\"java:comp/env\")"
                                +     " || " + "fullJndiName.startsWith(\"env\")"  
                            	+     ") {" 
                                + "	   throw new javax.naming.OperationNotSupportedException(\"SBB is not allowed write access to java:comp/env. See SLEE 1.0 spec for details. \");	"
                                + "} " 
                                + "else {$_ = $proceed($$);}; "  
                            + "}");
                    isAbstractSbbClassDecorated = true;
                }
            }
        });
    } // trapMethodIfENCBindCall

}