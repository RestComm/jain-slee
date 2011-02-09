package org.mobicents.slee.container.component;

import java.io.IOException;
import java.io.InputStream;

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
public interface ClassPool {

	/**
	 * @see javassist.ClassPool#appendClassPath(ClassPath)
	 */
	public ClassPath appendClassPath(ClassPath cp);

	/**
	 * cleans up the class pool cache
	 */
	public void clean();

	/**
	 * @see javassist.ClassPool#get(String)
	 */
	public CtClass get(String classname) throws NotFoundException;

	/**
	 * Retrieves the wrapped javassist class pool.
	 * 
	 * @return
	 */
	public javassist.ClassPool getClassPool();

	/**
	 * @see javassist.ClassPool#makeClass(InputStream)
	 * @param classfile
	 * @return
	 * @throws IOException
	 * @throws RuntimeException
	 */
	public CtClass makeClass(InputStream inputStream) throws IOException,
			RuntimeException;

	/**
	 * @see javassist.ClassPool#makeClass(String)
	 */
	public CtClass makeClass(String classname) throws RuntimeException;

	/**
	 * @see javassist.ClassPool#makeInterface(String)
	 * @param interfacename
	 * @return
	 * @throws RuntimeException
	 */
	public CtClass makeInterface(String interfacename) throws RuntimeException;

}
