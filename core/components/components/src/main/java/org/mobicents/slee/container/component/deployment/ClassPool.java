package org.mobicents.slee.container.component.deployment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import javassist.ClassPath;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * Wrapper for javassist class pool. Tracks class made and allows the pool
 * cleanup.
 * 
 * @author martins
 * 
 */
public class ClassPool {

	private static final Logger logger = Logger.getLogger(ClassPool.class);

	private final List<String> classesMade;
	private final List<ClassPath> classPaths;

	private final javassist.ClassPool classPool;

	public ClassPool() {
		classesMade = new ArrayList<String>();
		classPaths = new ArrayList<ClassPath>();
		/*
		 * classPool = new ClassPool(ClassPool.getDefault());
		 * 
		 * try { File mobicentsSar = new File(SbbDeployer.getLibPath()); List
		 * filesJars =
		 * ConcreteClassGeneratorUtils.getJarsFileListing(mobicentsSar); for
		 * (int i = 0; i < filesJars.size(); i++) { File jar =
		 * (File)filesJars.get(i); appendClassPath(SbbDeployer.getLibPath() +
		 * jar.getName()); } appendClassPath(SbbDeployer.getLibPath());
		 *  } catch (Exception e) { e.printStackTrace(); throw new
		 * RuntimeException("Failed initializing javassist", e); }
		 */
		classPool = new javassist.ClassPool();
	}

	/**
	 * @see javassist.ClassPool#appendClassPath(ClassPath)
	 */
	public ClassPath appendClassPath(ClassPath cp) {
		ClassPath classPath = classPool.appendClassPath(cp);
		classPaths.add(classPath);
		return classPath;
	}

	/**
	 * @see javassist.ClassPool#get(String)
	 */
	public CtClass get(String classname) throws NotFoundException {
		return classPool.get(classname);
	}

	/**
	 * @see javassist.ClassPool#makeClass(String)
	 */
	public CtClass makeClass(String classname) throws RuntimeException {		
		//classLoaderDomain.flushCaches();
		CtClass ctClass = classPool.makeClass(classname);
		classesMade.add(classname);
		return ctClass;
	}

	/**
	 * @see javassist.ClassPool#makeClass(InputStream)
	 * @param classfile
	 * @return
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public CtClass makeClass(InputStream inputStream) throws IOException,
			RuntimeException {
		//classLoaderDomain.flushCaches();
		CtClass ctClass = classPool.makeClass(inputStream);
		classesMade.add(ctClass.getName());
		return ctClass;
	}

	/**
	 * @see javassist.ClassPool#makeInterface(String)
	 * @param interfacename
	 * @return
	 * @throws RuntimeException
	 */
	public CtClass makeInterface(String interfacename) throws RuntimeException {
		//classLoaderDomain.flushCaches();
		CtClass ctClass = classPool.makeInterface(interfacename);		
		classesMade.add(interfacename);
		return ctClass;
	}

	/**
	 * cleans up the class pool cache
	 */
	public void clean() {
		for (ClassPath classPath : classPaths) {
			classPool.removeClassPath(classPath);
		}		
		for (String classMade : classesMade) {
			try {
				classPool.get(classMade).detach();
			} catch (NotFoundException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("Failed to detach class " + classMade
						+ " from class pool", e);
				}	
			}
		}
	}

	
  public javassist.ClassPool getClassPool()
  {
    return classPool;
  }

}
