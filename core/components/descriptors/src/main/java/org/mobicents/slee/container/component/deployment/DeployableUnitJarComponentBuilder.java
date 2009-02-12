package org.mobicents.slee.container.component.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.slee.SLEEException;
import javax.slee.management.DeploymentException;
import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.policy.VFSClassLoaderPolicy;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.LibraryComponent;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.ResourceAdaptorTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
	public SleeComponent buildComponent(String componentJarFileName,
			JarFile deployableUnitJar, File deploymentDir,
			DocumentBuilder documentBuilder) throws DeploymentException {

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

		// now extract the jar file to a new dir
		File componentJarDeploymentDir = new File(deploymentDir,
				componentJarFileName);
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

		// and delete the extracted jar file, we don't need it anymore
		if (!extractedFile.delete()) {
			logger.warn("failed to delete " + extractedFile);
		}

		// create component
		JarEntry componentDescriptor = null;
		InputStream componentDescriptorInputStream = null;
		SleeComponent component = null;
		try {
			if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/sbb-jar.xml")) != null) {
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				Document componentDescriptorDocument = documentBuilder
						.parse(componentDescriptorInputStream);
				SbbDescriptorImpl descriptor = new SbbDescriptorImpl(
						componentDescriptorDocument);
				component = new SbbComponent(descriptor);
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/profile-spec-jar.xml")) != null) {
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				Document componentDescriptorDocument = documentBuilder
						.parse(componentDescriptorInputStream);
				ProfileSpecificationDescriptorImpl descriptor = new ProfileSpecificationDescriptorImpl(
						componentDescriptorDocument);
				component = new ProfileSpecificationComponent(descriptor);
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/library-jar.xml")) != null) {
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				Document componentDescriptorDocument = documentBuilder
						.parse(componentDescriptorInputStream);
				LibraryDescriptorImpl descriptor = new LibraryDescriptorImpl(
						componentDescriptorDocument);
				component = new LibraryComponent(descriptor);
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/event-jar.xml")) != null) {
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				Document componentDescriptorDocument = documentBuilder
						.parse(componentDescriptorInputStream);
				EventDescriptorImpl descriptor = new EventDescriptorImpl(
						componentDescriptorDocument);
				component = new EventTypeComponent(descriptor);
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/resource-adaptor-type-jar.xml")) != null) {
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				Document componentDescriptorDocument = documentBuilder
						.parse(componentDescriptorInputStream);
				ResourceAdaptorTypeDescriptorImpl descriptor = new ResourceAdaptorTypeDescriptorImpl(
						componentDescriptorDocument);
				component = new ResourceAdaptorTypeComponent(descriptor);
			} else if ((componentDescriptor = componentJarFile
					.getJarEntry("META-INF/resource-adaptor-jar.xml")) != null) {
				componentDescriptorInputStream = componentJarFile
						.getInputStream(componentDescriptor);
				Document componentDescriptorDocument = documentBuilder
						.parse(componentDescriptorInputStream);
				ResourceAdaptorDescriptorImpl descriptor = new ResourceAdaptorDescriptorImpl(
						componentDescriptorDocument);
				component = new ResourceAdaptorComponent(descriptor);
			} else {
				throw new DeploymentException(
						"No Deployment Descriptor found in the "
								+ componentJarFile.getName()
								+ " entry of a deployable unit.");
			}
		} catch (SAXException e) {
			throw new DeploymentException(
					"failed to parse jar descriptor from "
							+ componentJarFile.getName(), e);
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

		try {
			componentJarFile.close();
		} catch (IOException e) {
			logger.error("failed to close component jar file", e);
		}

		// create class loading policy pointing to the dir where the component jar was extracted
		VirtualFile tempClassDeploymentDirVF = null;
		try {
			tempClassDeploymentDirVF = VFS.getRoot(componentJarDeploymentDir.toURL());
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(),e);
		}
		VFSClassLoaderPolicy classLoaderPolicy = VFSClassLoaderPolicy.createVFSClassLoaderPolicy(tempClassDeploymentDirVF);
    	classLoaderPolicy.setImportAll(true); // if you want to see other classes in the domain
    	classLoaderPolicy.setBlackListable(false);
    	classLoaderPolicy.setExportAll(ExportAll.NON_EMPTY); // if you want others to see your classes
    	classLoaderPolicy.setCacheable(true);            	
    	if (logger.isDebugEnabled()) {
    		logger.debug("DU Component built has class loader policy with the following exported packages: "+Arrays.asList(classLoaderPolicy.getExportedPackages()));
    	}
    	component.setClassLoaderPolicy(classLoaderPolicy);
		
		return component;
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
	private static File extractFile(String fileName, JarFile containingJar,
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
	private static void extractJar(JarFile jarFile, File dstDir)
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
	private static void pipeStream(InputStream is, OutputStream os)
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
