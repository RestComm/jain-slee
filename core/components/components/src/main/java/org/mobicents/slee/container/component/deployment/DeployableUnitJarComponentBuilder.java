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

package org.mobicents.slee.container.component.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.AbstractSleeComponent;
import org.mobicents.slee.container.component.ComponentManagementImpl;
import org.mobicents.slee.container.component.EventTypeComponentImpl;
import org.mobicents.slee.container.component.LibraryComponentImpl;
import org.mobicents.slee.container.component.ProfileSpecificationComponentImpl;
import org.mobicents.slee.container.component.ResourceAdaptorComponentImpl;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponentImpl;
import org.mobicents.slee.container.component.SbbComponentImpl;
import org.mobicents.slee.container.component.deployment.classloading.URLClassLoaderDomainImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactoryImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.library.JarDescriptor;

/**
 * DU Component jar builder
 * 
 * @author martins
 * 
 */
public class DeployableUnitJarComponentBuilder {

	private static final Logger logger = Logger
			.getLogger(DeployableUnitJarComponentBuilder.class);

	private final ComponentManagementImpl componentManagement;

	/**
	 * 
	 */
	public DeployableUnitJarComponentBuilder(
			ComponentManagementImpl componentManagement) {
		this.componentManagement = componentManagement;
	}

	/**
	 * Builds the DU component from a jar with the specified file name,
	 * contained in the specified DU jar file. The component is built in the
	 * specified deployment dir.
	 * 
	 * @param componentJarFileName
	 * @param deployableUnitJar
	 * @param deploymentDir
	 * @param documentBuilder
	 * @return
	 * @throws DeploymentException
	 */
	@SuppressWarnings("deprecation")
	public List<AbstractSleeComponent> buildComponents(
			String componentJarFileName, JarFile deployableUnitJar,
			File deploymentDir)
			throws DeploymentException {
		
		// extract the component jar from the DU jar, to the temp du dir
		File extractedFile = extractFile(componentJarFileName,
				deployableUnitJar, deploymentDir);
		JarFile componentJarFile = null;
		try {
			componentJarFile = new JarFile(extractedFile);
		} catch (IOException e) {
			throw new DeploymentException(
					"failed to create jar file for extracted file "
							+ extractedFile);
		}

		InputStream componentDescriptorInputStream = null;
		List<AbstractSleeComponent> components = new ArrayList<AbstractSleeComponent>();

		try {
			// now extract the jar file to a new dir
			File componentJarDeploymentDir = new File(deploymentDir,
					componentJarFileName + "-contents");
			if (!componentJarDeploymentDir.exists()) {
				// the jar may not be on root of DU, create additional dirs if needed
				LinkedList<File> dirsToCreate = new LinkedList<File>();
				File dir = componentJarDeploymentDir.getParentFile();
				while(!dir.equals(deploymentDir)) {
					dirsToCreate.addFirst(dir);
					dir = dir.getParentFile();
				}
				for (File f : dirsToCreate) {
					f.mkdir();
				}
				// now create the dir for the component jar
				if (!componentJarDeploymentDir.mkdir()) {
						throw new SLEEException("dir for jar "
								+ componentJarFileName + " not created in "
								+ deploymentDir);					
				}				
			} else {
				throw new SLEEException("dir for jar " + componentJarFileName
						+ " already exists in " + deploymentDir);
			}
			extractJar(componentJarFile, componentJarDeploymentDir);
			// create components from descriptor
			JarEntry componentDescriptor = null;
			if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/sbb-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomainImpl classLoaderDomain = componentManagement
				.getClassLoaderFactory()
				.newClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() },
						Thread.currentThread().getContextClassLoader());
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				SbbDescriptorFactoryImpl descriptorFactory = componentManagement
						.getComponentDescriptorFactory()
						.getSbbDescriptorFactory();
				List<SbbDescriptorImpl> descriptors = descriptorFactory
						.parse(componentDescriptorInputStream);
				// create components
				for (SbbDescriptorImpl descriptor : descriptors) {
					SbbComponentImpl component = new SbbComponentImpl(
							descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/profile-spec-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomainImpl classLoaderDomain = componentManagement
				.getClassLoaderFactory()
				.newClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() },
						Thread.currentThread().getContextClassLoader());
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				ProfileSpecificationDescriptorFactoryImpl descriptorFactory = componentManagement
						.getComponentDescriptorFactory()
						.getProfileSpecificationDescriptorFactory();
				List<ProfileSpecificationDescriptorImpl> descriptors = descriptorFactory
						.parse(componentDescriptorInputStream);
				// create components
				for (ProfileSpecificationDescriptorImpl descriptor : descriptors) {
					ProfileSpecificationComponentImpl component = new ProfileSpecificationComponentImpl(
							descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/library-jar.xml")) != null) {
				Set<LibraryComponentImpl> libraryComponents = new HashSet<LibraryComponentImpl>();
				// we need to gather all URLs for the shared class loader domain
				// to watch
				Set<URL> classLoaderDomainURLs = new HashSet<URL>();
				classLoaderDomainURLs.add(componentJarDeploymentDir.toURL());
				// parse the descriptor
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				LibraryDescriptorFactoryImpl descriptorFactory = componentManagement
						.getComponentDescriptorFactory()
						.getLibraryDescriptorFactory();
				List<LibraryDescriptorImpl> descriptors = descriptorFactory
						.parse(componentDescriptorInputStream);
				// create components
				for (LibraryDescriptorImpl descriptor : descriptors) {
					LibraryComponentImpl component = new LibraryComponentImpl(
							descriptor);
					for (JarDescriptor mJar : descriptor.getJars()) {
						classLoaderDomainURLs.add(new File(
								componentJarDeploymentDir, mJar.getJarName())
								.toURL());
					}
					// set deploy dir and cl domain
					component.setDeploymentDir(componentJarDeploymentDir);
					components.add(component);
					libraryComponents.add(component);
				}
				// create shared url domain
				URLClassLoaderDomainImpl classLoaderDomain = componentManagement
				.getClassLoaderFactory()
				.newClassLoaderDomain(
						classLoaderDomainURLs
						.toArray(new URL[classLoaderDomainURLs.size()]),
						Thread.currentThread().getContextClassLoader());
				// add it to each component
				for (LibraryComponentImpl component : libraryComponents) {
					component.setClassLoaderDomain(classLoaderDomain);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/event-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomainImpl classLoaderDomain = componentManagement
				.getClassLoaderFactory()
				.newClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() },
						Thread.currentThread().getContextClassLoader());				
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				EventTypeDescriptorFactoryImpl descriptorFactory = componentManagement
						.getComponentDescriptorFactory()
						.getEventTypeDescriptorFactory();
				List<EventTypeDescriptorImpl> descriptors = descriptorFactory
						.parse(componentDescriptorInputStream);
				// create components
				for (EventTypeDescriptorImpl descriptor : descriptors) {
					EventTypeComponentImpl component = new EventTypeComponentImpl(
							descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/resource-adaptor-type-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomainImpl classLoaderDomain = componentManagement.getClassLoaderFactory().newClassLoaderDomain(new URL[] { componentJarDeploymentDir.toURL() }, Thread
						.currentThread().getContextClassLoader());
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				ResourceAdaptorTypeDescriptorFactoryImpl descriptorFactory = componentManagement
						.getComponentDescriptorFactory()
						.getResourceAdaptorTypeDescriptorFactory();
				List<ResourceAdaptorTypeDescriptorImpl> descriptors = descriptorFactory
						.parse(componentDescriptorInputStream);
				// create components
				for (ResourceAdaptorTypeDescriptorImpl descriptor : descriptors) {
					ResourceAdaptorTypeComponentImpl component = new ResourceAdaptorTypeComponentImpl(
							descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/resource-adaptor-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomainImpl classLoaderDomain = componentManagement
						.getClassLoaderFactory()
						.newClassLoaderDomain(
								new URL[] { componentJarDeploymentDir.toURL() },
								Thread.currentThread().getContextClassLoader());				
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				ResourceAdaptorDescriptorFactoryImpl descriptorFactory = componentManagement
						.getComponentDescriptorFactory()
						.getResourceAdaptorDescriptorFactory();
				List<ResourceAdaptorDescriptorImpl> descriptors = descriptorFactory
						.parse(componentDescriptorInputStream);
				// create components
				for (ResourceAdaptorDescriptorImpl descriptor : descriptors) {
					ResourceAdaptorComponentImpl component = new ResourceAdaptorComponentImpl(
							descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else {
				throw new DeploymentException(
						"No Deployment Descriptor found in the "
								+ componentJarFile.getName()
								+ " entry of a deployable unit.");
			}
		} catch (IOException e) {
			throw new DeploymentException(
					"failed to parse jar descriptor from "
							+ componentJarFile.getName(), e);
		} finally {
			if (componentDescriptorInputStream != null) {
				try {
					componentDescriptorInputStream.close();
				} catch (IOException e) {
					logger
							.error("failed to close inputstream of descriptor for jar "
									+ componentJarFile);
				}
			}
		}

		// close component jar file
		try {
			componentJarFile.close();
		} catch (IOException e) {
			logger.error("failed to close component jar file", e);
		}
		// and delete the extracted jar file, we don't need it anymore
		if (!extractedFile.delete()) {
			logger.warn("failed to delete " + extractedFile);
		}

		return components;
	}

	/**
	 * Extracts the file with name <code>fileName</code> out of the
	 * <code>containingJar</code> archive and stores it in <code>dstDir</code>.
	 * 
	 * @param fileName
	 *            the name of the file to extract.
	 * @param containingJar
	 *            the archive where to extract it from.
	 * @param dstDir
	 *            the location where the extracted file should be stored.
	 * @return a <code>java.io.File</code> reference to the extracted file.
	 * @throws DeploymentException
	 */
	private File extractFile(String fileName, JarFile containingJar, File dstDir)
			throws DeploymentException {

		ZipEntry zipFileEntry = containingJar.getEntry(fileName);
		logger.trace("Extracting file " + fileName + " from "
				+ containingJar.getName());
		if (zipFileEntry == null) {
			throw new DeploymentException("Error extracting jar file  "
					+ fileName + " from " + containingJar.getName());
		}
		File extractedFile = new File(dstDir, new File(zipFileEntry.getName())
				.getName());
		try {
			pipeStream(containingJar.getInputStream(zipFileEntry),
					new FileOutputStream(extractedFile));
		} catch (FileNotFoundException e) {
			throw new DeploymentException("file " + fileName + " not found in "
					+ containingJar.getName(), e);
		} catch (IOException e) {
			throw new DeploymentException("erro extracting file " + fileName
					+ " from " + containingJar.getName(), e);
		}
		logger.debug("Extracted file " + extractedFile.getName());
		return extractedFile;
	}

	/**
	 * This method will extract all the files in the jar file
	 * 
	 * @param jarFile
	 *            the jar file
	 * @param dstDir
	 *            the destination where files in the jar file be extracted
	 * @param deployableUnitID
	 * @return
	 * @throws DeploymentException
	 *             failed to extract files
	 */
	private void extractJar(JarFile jarFile, File dstDir)
			throws DeploymentException {

		// Extract jar contents to a classpath location
		JarInputStream jarIs = null;
		try {
			jarIs = new JarInputStream(new BufferedInputStream(
					new FileInputStream(jarFile.getName())));

			for (JarEntry entry = jarIs.getNextJarEntry(); jarIs.available() > 0
					&& entry != null; entry = jarIs.getNextJarEntry()) {
				logger.trace("jar entry = " + entry.getName());

				if (entry.isDirectory()) {
					// Create jar directories.
					File dir = new File(dstDir, entry.getName());
					if (!dir.exists()) {
						if (!dir.mkdirs()) {
							logger.debug("Failed to create directory "
									+ dir.getAbsolutePath());
							throw new IOException("Failed to create directory "
									+ dir.getAbsolutePath());
						}
					} else
						logger.trace("Created directory"
								+ dir.getAbsolutePath());
				} else // unzip files
				{
					File file = new File(dstDir, entry.getName());
					File dir = file.getParentFile();
					if (!dir.exists()) {
						if (!dir.mkdirs()) {
							logger.debug("Failed to create directory "
									+ dir.getAbsolutePath());
							throw new IOException("Failed to create directory "
									+ dir.getAbsolutePath());
						} else
							logger.trace("Created directory"
									+ dir.getAbsolutePath());
					}
					pipeStream(jarFile.getInputStream(entry),
							new FileOutputStream(file));

				}
			}
		} catch (Exception e) {
			throw new DeploymentException("failed to extract jar file "
					+ jarFile.getName());
		} finally {
			if (jarIs != null) {
				try {
					jarIs.close();
				} catch (IOException e) {
					logger.error("failed to close jar input stream", e);
				}
			}
		}

	}

	private static byte buffer[] = new byte[8192];

	/**
	 * Pipes data from the input stream into the output stream.
	 * 
	 * @param is
	 *            The InputStream where the data is coming from.
	 * @param os
	 *            The OutputStream where the data is going to.
	 * @throws IOException
	 *             if reading or writing the data fails.
	 */
	private void pipeStream(InputStream is, OutputStream os) throws IOException {
		synchronized (buffer) {
			try {
				for (int bytesRead = is.read(buffer); bytesRead != -1; bytesRead = is
						.read(buffer))
					os.write(buffer, 0, bytesRead);
				is.close();
				os.close();
			} catch (IOException ioe) {
				try {
					is.close();
				} catch (Exception ioexc) {/* do sth? */
				}
				try {
					os.close();
				} catch (Exception ioexc) {/* do sth? */
				}
				throw ioe;
			}
		}
	}

}
