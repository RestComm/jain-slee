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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.LibraryComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.classloading.URLClassLoaderDomain;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.library.MJar;

/**
 * DU Component jar builder
 * 
 * @author martins
 * 
 */
public class DeployableUnitJarComponentBuilder {

	private static final Logger logger = Logger
			.getLogger(DeployableUnitJarComponentBuilder.class);

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
	public List<SleeComponent> buildComponents(String componentJarFileName,
			JarFile deployableUnitJar, File deploymentDir) throws DeploymentException {

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
		List<SleeComponent> components = new ArrayList<SleeComponent>();

		try {
			// now extract the jar file to a new dir
			File componentJarDeploymentDir = new File(deploymentDir,
					componentJarFileName+"-contents");
			if (!componentJarDeploymentDir.exists()) {
				if (!componentJarDeploymentDir.mkdir()) {
					throw new SLEEException("dir for jar " + componentJarFileName
							+ " not created in " + deploymentDir);
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
				URLClassLoaderDomain classLoaderDomain = new URLClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() }, Thread.currentThread()
								.getContextClassLoader());
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
				.getInputStream(componentDescriptor);
				SbbDescriptorFactory descriptorFactory = new SbbDescriptorFactory();
				List<SbbDescriptorImpl> descriptors = descriptorFactory.parse(componentDescriptorInputStream);
				// create components
				for (SbbDescriptorImpl descriptor : descriptors) {					
					SbbComponent component = new SbbComponent(descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/profile-spec-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomain classLoaderDomain = new URLClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() }, Thread.currentThread()
								.getContextClassLoader());
				// ugly hack to load tck rmi stub interfaces from slee class loader (till no better solution arises)
				try {				
					classLoaderDomain
							.loadClassFromSlee("com.opencloud.sleetck.lib.rautils.RMIObjectChannel");
					classLoaderDomain
					.loadClassFromSlee("com.opencloud.sleetck.lib.rautils.MessageHandlerRegistry");
			classLoaderDomain
					.loadClassFromSlee("com.opencloud.sleetck.lib.rautils.MessageHandler");
				}
				catch (Throwable e) {
					// ignore
				}
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
				.getInputStream(componentDescriptor);
				ProfileSpecificationDescriptorFactory descriptorFactory = new ProfileSpecificationDescriptorFactory();
				List<ProfileSpecificationDescriptorImpl> descriptors = descriptorFactory.parse(componentDescriptorInputStream);
				// create components
				for (ProfileSpecificationDescriptorImpl descriptor : descriptors) {
					ProfileSpecificationComponent component = new ProfileSpecificationComponent(descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/library-jar.xml")) != null) {
				// create the class loader domain shared by all libs in this lib jar
				URLClassLoaderDomain sharedClassLoaderDomain = new URLClassLoaderDomain(new URL[]{componentJarDeploymentDir.toURL()},Thread.currentThread().getContextClassLoader());
				// create a map for temp storage of each jar class loader domain 
				Map<String, URLClassLoaderDomain> jarClassLoaderDomains = new HashMap<String, URLClassLoaderDomain>();
				// parse the descriptor
				componentDescriptorInputStream = componentJarFile
				.getInputStream(componentDescriptor);
				LibraryDescriptorFactory descriptorFactory = new LibraryDescriptorFactory();
				List<LibraryDescriptorImpl> descriptors = descriptorFactory.parse(componentDescriptorInputStream);
				// create components
				for (LibraryDescriptorImpl descriptor : descriptors) {
					LibraryComponent component = new LibraryComponent(descriptor);
					// each lib component has a different class loader domain that depends on the shared domain and each jar it refers in the descriptor
					URLClassLoaderDomain componentClassLoaderDomain = new URLClassLoaderDomain(new URL[]{},Thread.currentThread().getContextClassLoader());
					componentClassLoaderDomain.getDependencies().add(sharedClassLoaderDomain);
					// for each referenced jar create a class loader domain
					for (MJar mJar : descriptor.getJars()) {
						// create the domain for that specific jar if does not exists yet
						URLClassLoaderDomain jarClassLoaderDomain = jarClassLoaderDomains.get(mJar.getJarName());
						if (jarClassLoaderDomain == null) {
							jarClassLoaderDomain = new URLClassLoaderDomain(new URL[]{(new File(componentJarDeploymentDir,mJar.getJarName())).toURL()},Thread.currentThread().getContextClassLoader());
							jarClassLoaderDomains.put(mJar.getJarName(), jarClassLoaderDomain);
						}
						componentClassLoaderDomain.getDependencies().add(jarClassLoaderDomain);
					}					
					// set deploy dir and cl domain
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(componentClassLoaderDomain);
					components.add(component);					
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/event-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomain classLoaderDomain = new URLClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() }, Thread.currentThread()
								.getContextClassLoader());
				// parse descriptor
				componentDescriptorInputStream = componentJarFile.getInputStream(componentDescriptor);
				EventTypeDescriptorFactory descriptorFactory = new EventTypeDescriptorFactory();
				List<EventTypeDescriptorImpl> descriptors = descriptorFactory.parse(componentDescriptorInputStream);
				// create components
				for (EventTypeDescriptorImpl descriptor : descriptors) {
					EventTypeComponent component = new EventTypeComponent(descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/resource-adaptor-type-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomain classLoaderDomain = new URLClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() }, Thread.currentThread()
								.getContextClassLoader());
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
				.getInputStream(componentDescriptor);
				ResourceAdaptorTypeDescriptorFactory descriptorFactory = new ResourceAdaptorTypeDescriptorFactory();
				List<ResourceAdaptorTypeDescriptorImpl> descriptors = descriptorFactory.parse(componentDescriptorInputStream);
				// create components
				for (ResourceAdaptorTypeDescriptorImpl descriptor : descriptors) {
					ResourceAdaptorTypeComponent component = new ResourceAdaptorTypeComponent(descriptor);
					component.setDeploymentDir(componentJarDeploymentDir);
					component.setClassLoaderDomain(classLoaderDomain);
					components.add(component);
				}
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/resource-adaptor-jar.xml")) != null) {
				// create class loader domain shared by all components
				URLClassLoaderDomain classLoaderDomain = new URLClassLoaderDomain(
						new URL[] { componentJarDeploymentDir.toURL() }, Thread.currentThread()
								.getContextClassLoader());
				// ugly hack to load tck rmi stub interfaces from slee class loader (till no better solution arises)
				try {
					classLoaderDomain
							.loadClassFromSlee("com.opencloud.sleetck.lib.rautils.MessageHandlerRegistry");
					classLoaderDomain
							.loadClassFromSlee("com.opencloud.sleetck.lib.rautils.MessageHandler");
					classLoaderDomain
							.loadClassFromSlee("com.opencloud.sleetck.lib.rautils.RMIObjectChannel");
				}
				catch (Throwable e) {
					// ignore
				}
				// parse descriptor
				componentDescriptorInputStream = componentJarFile
				.getInputStream(componentDescriptor);
				ResourceAdaptorDescriptorFactory descriptorFactory = new ResourceAdaptorDescriptorFactory();
				List<ResourceAdaptorDescriptorImpl> descriptors = descriptorFactory.parse(componentDescriptorInputStream);
				// create components
				for (ResourceAdaptorDescriptorImpl descriptor : descriptors) {
					ResourceAdaptorComponent component = new ResourceAdaptorComponent(descriptor);
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
	private File extractFile(String fileName, JarFile containingJar,
			File dstDir) throws DeploymentException {

		ZipEntry zipFileEntry = containingJar.getEntry(fileName);
		logger.debug("Extracting file " + fileName + " from "
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
				logger.debug("jar entry = " + entry.getName());

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
						logger.debug("Created directory"
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
							logger.debug("Created directory"
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
	private void pipeStream(InputStream is, OutputStream os)
			throws IOException {
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
