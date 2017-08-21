/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
