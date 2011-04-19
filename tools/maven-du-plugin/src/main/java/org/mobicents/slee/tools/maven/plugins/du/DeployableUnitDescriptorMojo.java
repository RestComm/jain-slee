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

package org.mobicents.slee.tools.maven.plugins.du;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Generates a JAIN SLEE 1.1 Deployable Unit Descriptor, from content in a
 * specific directory.
 */
public class DeployableUnitDescriptorMojo extends AbstractMojo {
	
	/**
	 * The directory where SLEE component jars will be placed, in the DU jar.
	 * If set, each <jar/> entry in the DU descriptor will be <jar>${duJarDirectory}/jarFileName</jar>, otherwise it will be will be <jar>jarFileName</jar> 
	 */
	private String duJarDirectory;
	
	/**
	 * The directory where SLEE service xml descriptors will be placed, in the DU jar.
	 * If set, each <service/> entry in the DU descriptor will be <service>${duServiceDirectory}/serviceXmlFileName</service>, otherwise it will be will be <service>serviceXmlFileName</service> 
	 */
	private String duServiceDirectory;
	
	/**
	 * Directory to be used as the source for SLEE component jars.
	 * If not set, ${workDirectory}/${duJarDirectory} or ${workDirectory} will be used, depending if ${duJarDirectory} is set or not.
	 */
	private File jarInputDirectory;

	/**
	 * Directory to be used as the source for SLEE service xml descriptors.
	 * If not set, ${workDirectory}/${duServiceDirectory} or ${workDirectory} will be used, depending if ${duServiceDirectory} is set or not.
	 */
	private File serviceInputDirectory;

	/**
	 * Directory to be used as the output for the generated SLEE du xml
	 * descriptor.
	 * If not set, ${workDirectory}/META-INF will be used
	 */
	private File duXmlOutputDirectory;
	
	/**
	 * The work directory is used to calculate other directories which may not be set.
	 */
	private File workDirectory;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {
		
		setupDirectories();
		
		// create the output dir if does not exists
		if (!duXmlOutputDirectory.exists()) {
			duXmlOutputDirectory.mkdirs();
		}

		if (getLog().isDebugEnabled()) {
			getLog().debug("Collecting SLEE component jars...");
		}
		Set<String> jars = collectFiles(jarInputDirectory,".jar");

		if (getLog().isDebugEnabled()) {
			getLog().debug("Collecting SLEE service descriptors...");
		}
		Set<String> services = collectFiles(serviceInputDirectory,".xml");

		// generate the xml
		if (getLog().isDebugEnabled()) {
			getLog().debug("Generating JAIN SLEE 1.1 Deployable Unit XML descriptor...");
		}
		String xml = generateDuDescriptor(jars, services);
		
		File descriptor = new File(duXmlOutputDirectory,
				"deployable-unit.xml");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(descriptor);
			fileWriter.write(xml);
			getLog().info("Generated JAIN SLEE 1.1 Deployable Unit XML descriptor:\n"+xml);			
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file "
					+ descriptor, e);
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private String generateDuDescriptor(Set<String> jars, Set<String> services) {
		StringBuilder sb = new StringBuilder(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
						+ "<!DOCTYPE deployable-unit PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.1//EN\" \"http://java.sun.com/dtd/slee-deployable-unit_1_1.dtd\">\r\n"
						+ "<deployable-unit>\r\n");

		for (String jar : jars) {
			sb.append("\t<jar>");
			if (duJarDirectory == null) {
				sb.append(jar);
			} else {
				sb.append(duJarDirectory).append('/').append(jar);
			}
			sb.append("</jar>\r\n");
		}

		for (String service : services) {
			sb.append("\t<service-xml>");
			if (duServiceDirectory == null) {
				sb.append(service);
			} else {
				sb.append(duServiceDirectory).append('/').append(service);
			}
			sb.append("</service-xml>\r\n");
		}

		sb.append("</deployable-unit>");

		return sb.toString();
	}

	private void setupDirectories() {
		
		if (jarInputDirectory == null) {
			if (duJarDirectory == null) {
				jarInputDirectory = workDirectory;
			}
			else {
				jarInputDirectory = new File(workDirectory,duJarDirectory);
			}
		}
		if (getLog().isDebugEnabled()) {
			getLog().debug("Jar input directory: "+jarInputDirectory.getAbsolutePath());
		}
		
		if (serviceInputDirectory == null) {
			if (duServiceDirectory == null) {
				serviceInputDirectory = workDirectory;
			}
			else {
				serviceInputDirectory = new File(workDirectory,duServiceDirectory);
			}
		}
		if (getLog().isDebugEnabled()) {
			getLog().debug("Service input directory: "+serviceInputDirectory.getAbsolutePath());
		}
		
		if (duXmlOutputDirectory == null) {
			duXmlOutputDirectory = new File(workDirectory,"META-INF");
		}
		if (getLog().isDebugEnabled()) {
			getLog().debug("XML output directory: "+duXmlOutputDirectory.getAbsolutePath());
		}
	}

	private Set<String> collectFiles(File inputDirectory, String suffix) {

		if (getLog().isDebugEnabled()) {
			getLog().debug("Collecting non hidden files with "+suffix+" name sufix from directory "+inputDirectory.getAbsolutePath());
		}
		
		if (inputDirectory == null || !inputDirectory.exists()
				|| !inputDirectory.isDirectory()) {
			return Collections.emptySet();
		}

		if (getLog().isDebugEnabled()) {
			getLog().debug("Directory "+inputDirectory.getAbsolutePath()+" sucessfully validated.");
		}

		Set<String> result = new HashSet<String>();

		for (File f : inputDirectory.listFiles()) {
			if (f.isDirectory() || f.isHidden()
					|| !f.getName().endsWith(suffix)) {
				continue;
			} else {
				if (getLog().isDebugEnabled()) {
					getLog().debug("Collecting file " + f.getName());
				}
				result.add(f.getName());
			}
		}

		return result;
	}
}
