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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

import javax.slee.ServiceID;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.mobicents.slee.tools.maven.plugins.du.ant.DeployConfig;
import org.mobicents.slee.tools.maven.plugins.du.ant.RAEntity;
import org.mobicents.slee.tools.maven.plugins.du.ant.ServiceIds;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Generates a Ant script, which can be used to deploy/undeploy JAIN SLEE
 * Deployable Units.
 */
public class AntManagementScriptMojo extends AbstractMojo {

	/**
	 * The location of the deploy-config.xml file, which is used by Mobicents
	 * JAIN SLEE to create RA entities and links.
	 */
	private File deployConfigFile;

	/**
	 * Name of the generated script file.
	 * 
	 */
	private String scriptFileName;

	/**
	 * Name of the DU jar file.
	 * 
	 */
	private String duFileName;

	/**
	 * Directory to be used as the source for SLEE service xml descriptors.
	 * 
	 */
	private File serviceInputDirectory;

	/**
	 * Directory to be used as the output for the generated ant management
	 * script.
	 * 
	 */
	private File outputDirectory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.AbstractMojo#execute()
	 */
	public void execute() throws MojoExecutionException {

		if (getLog().isDebugEnabled()) {
			getLog().debug("Collecting SLEE service descriptors...");
		}
		Set<String> services = collectFiles(serviceInputDirectory, ".xml");

		// generate the xml
		String xml = generateManagementAntScript(services);

		File file = new File(outputDirectory, scriptFileName);

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(xml);
			getLog().info("Ant management script generated with success.");
		} catch (IOException e) {
			throw new MojoExecutionException("Error creating file " + file, e);
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

	private String generateManagementAntScript(Set<String> services)
			throws MojoExecutionException {

		getLog().info("Generating ant script for management without maven...");

		// read header and footer
		String header = "";
		String footer = "";
		LinkedList<String> deployElements = new LinkedList<String>();
		LinkedList<String> undeployElements = new LinkedList<String>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(this
					.getClass().getResourceAsStream("build.header")));
			String str = "";
			while ((str = in.readLine()) != null) {
				header += str + "\r\n";
			}

			in = new BufferedReader(new InputStreamReader(this.getClass()
					.getResourceAsStream("build.footer")));

			while ((str = in.readLine()) != null) {
				footer += str + "\r\n";
			}
		} catch (IOException e) {
			throw new MojoExecutionException(
					"failed to read header and footer of build.xml file", e);
		}

		// first process deploy-config file
		if (deployConfigFile.exists()) {

			try {
				getLog().info("Parsing deploy-config.xml without validation");
				// parse doc into dom
				SchemaFactory schemaFactory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				Schema schema = schemaFactory.newSchema(getClass()
						.getClassLoader().getResource("deploy-config.xsd"));

				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setSchema(schema);
				DocumentBuilder parser = factory.newDocumentBuilder();

				parser.setErrorHandler(new ErrorHandler() {
					public void error(SAXParseException e) throws SAXException {
						throw e;
					}

					public void fatalError(SAXParseException e)
							throws SAXException {
						throw e;
					}

					public void warning(SAXParseException e)
							throws SAXException {
						throw e;
					}
				});

				Document document = parser.parse(deployConfigFile);

				// parse dom into DeployConfig
				DeployConfig deployConfig = DeployConfig.parse(document
						.getDocumentElement());

				for (RAEntity raEntity : deployConfig.getRaEntities()) {

					// let's start by processing properties
					Properties properties = new Properties();

					// FIXME we need to come up with an alternative, we can't
					// convert config properties into properties
					/*
					 * if (!raEntity.getPropertiesFile().equals("")) { // we
					 * have a properties file in META-INF File propertiesFile =
					 * new File(outputDirectory, "META-INF" + File.separator +
					 * raEntity.getPropertiesFile()); if
					 * (propertiesFile.exists()) { properties.load(new
					 * FileInputStream(propertiesFile)); } }
					 */
					// put all properties defined in the deploy-config also
					// properties.putAll(raEntity.getProperties());

					// ok, now we do the strings for the ant script
					// the ant call to create and activate the ra entity
					String xml = "\t\t<antcall target=\"activate-raentity\">\r\n"
							+ "\t\t\t<param name=\"ra.entity\" value=\""
							+ raEntity.getEntityName()
							+ "\" />\r\n"
							+ "\t\t\t<param name=\"ra.id\" value=\""
							+ raEntity.getResourceAdaptorId() + "\" />\r\n";
					if (!properties.isEmpty()) {
						// generate file in target and add property to ant call
						xml += "\t\t\t<param name=\"ra.entity.properties.filename\" value=\""
								+ raEntity.getEntityName()
								+ ".properties\" />\r\n";

						try {
							File propertiesFile = new File(outputDirectory,
									".." + File.separator
											+ raEntity.getEntityName()
											+ ".properties");
							properties.store(new FileOutputStream(
									propertiesFile), null);
							getLog().info(
									"generated properties file "
											+ raEntity.getEntityName()
											+ ".properties");
						} catch (IOException e) {
							throw new MojoExecutionException(
									"failed to write ra entity properties file into output dir",
									e);
						}
					}
					xml += "\t\t</antcall>\r\n";
					// add this to the deployElements tail
					deployElements.addLast(xml);

					// the ant call to deactivate and remove the ra entity

					xml = "\t\t<antcall target=\"deactivate-raentity\">\r\n"
							+ "\t\t\t<param name=\"ra.entity\" value=\""
							+ raEntity.getEntityName() + "\" />\r\n"
							+ "\t\t\t<param name=\"ra.id\" value=\""
							+ raEntity.getResourceAdaptorId() + "\" />\r\n"
							+ "\t\t</antcall>\r\n";

					// add this to the undeployElements head
					undeployElements.addFirst(xml);

					// second we bind/unbind ra links
					for (String raLink : raEntity.getRaLinks()) {
						xml = "\t\t<antcall target=\"bind-ralink\">\r\n"
								+ "\t\t\t<param name=\"ra.entity\" value=\""
								+ raEntity.getEntityName() + "\" />\r\n"
								+ "\t\t\t<param name=\"ra.link\" value=\""
								+ raLink + "\" />\r\n" + "\t\t</antcall>\r\n";
						// add this to the deployElements tail
						deployElements.addLast(xml);

						xml = "\t\t<antcall target=\"unbind-ralink\">\r\n"
								+ "\t\t\t<param name=\"ra.entity\" value=\""
								+ raEntity.getEntityName() + "\" />\r\n"
								+ "\t\t\t<param name=\"ra.link\" value=\""
								+ raLink + "\" />\r\n" + "\t\t</antcall>\r\n";
						// add this to the undeployElements head
						undeployElements.addFirst(xml);
					}
				}
			} catch (Exception e) {
				throw new MojoExecutionException(
						"failed to parse resource META-INF/deploy-config.xml",
						e);
			}
		}

		// now the services descriptors
		for (String fileName : services) {

			File serviceDescriptorFile = new File(serviceInputDirectory, fileName);

			try {
				// http://code.google.com/p/mobicents/issues/detail?id=104
				getLog().info("Parsing " + fileName + " with validation");

				// parse doc into dom
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setValidating(true);
				DocumentBuilder parser = factory.newDocumentBuilder();

				parser.setEntityResolver(new EntityResolver() {
					public InputSource resolveEntity(String publicID,
							String systemID) throws SAXException {

						InputStream is = null;
						BufferedReader br = null;
						String line;
						StringWriter stringWriter = null;
						StringReader stringReader = null;
						try {
							URI uri = new URI(systemID);
							getLog().info(
									"Resolving " + systemID + " locally from "
											+ uri);
							String filename = uri.toString().substring(
									uri.toString().lastIndexOf("/") + 1,
									uri.toString().length());
							getLog().info("Resolved filename " + filename);
							stringWriter = new StringWriter();
							is = getClass().getClassLoader()
									.getResourceAsStream(filename);
							br = new BufferedReader(new InputStreamReader(is));
							while (null != (line = br.readLine())) {
								stringWriter.write(line);
							}
							stringWriter.flush();
							if (stringWriter != null) {
								stringWriter.close();
							}

							stringReader = new StringReader(stringWriter
									.getBuffer().toString());

							return new InputSource(stringReader);
						} catch (URISyntaxException e) {
							getLog().error(e);
						} catch (IOException e) {
							getLog().error(e);
						} finally {
							try {
								if (br != null) {
									br.close();
								}

								if (is != null) {
									is.close();
								}
							} catch (IOException e) {
								getLog().error(e);
							}
						}

						// If no match, returning null makes process continue
						// normally
						return null;
					}
				});

				parser.setErrorHandler(new ErrorHandler() {
					public void warning(SAXParseException exception)
							throws SAXException {
						getLog().warn(exception.getMessage());
					}

					public void fatalError(SAXParseException exception)
							throws SAXException {
						getLog().warn(exception.getMessage());
					}

					public void error(SAXParseException exception)
							throws SAXException {
						getLog().error(exception.getMessage());
					}
				});

				Document document = parser.parse(serviceDescriptorFile);

				// parse dom into service ids
				ServiceIds serviceIds = ServiceIds.parse(document
						.getDocumentElement());

				for (ServiceID serviceId : serviceIds.getIds()) {

					String xml = "\r\n\t\t<antcall target=\"activate-service\">\r\n"
							+ "\t\t\t<param name=\"service.id\" value=\""
							+ serviceId + "\" />\r\n" + "\t\t</antcall>\r\n";

					// add this to the deployElements tail
					deployElements.addLast(xml);

					xml = "\r\n\t\t<antcall target=\"deactivate-service\">\r\n"
							+ "\t\t\t<param name=\"service.id\" value=\""
							+ serviceId + "\" />\r\n" + "\t\t</antcall>\r\n";

					// add this to the undeployElements head
					undeployElements.addFirst(xml);
				}
			} catch (Exception e) {
				throw new MojoExecutionException(
						"failed to parse service descriptor " + fileName, e);
			}
		}

		// now lets glue everything and write the build.xml script

		String xml = header + "\r\n\t<property name=\"du.filename\" value=\""
				+ duFileName + "\" />\r\n"
				+ "\r\n\t<target name=\"deploy-jmx\">\r\n"
				+ "\r\n\t\t<antcall target=\"install-DU\" />\r\n";

		for (String s : deployElements) {
			xml += s;
		}

		xml += "\r\n\t</target>\r\n"
				+ "\r\n\t<target name=\"undeploy-jmx\">\r\n";

		for (String s : undeployElements) {
			xml += s;
		}

		xml += footer;

		return xml;
	}

	private Set<String> collectFiles(File inputDirectory, String suffix) {

		if (getLog().isDebugEnabled()) {
			getLog().debug(
					"Collecting non hidden files with " + suffix
							+ " name sufix from directory "
							+ inputDirectory.getAbsolutePath());
		}

		if (inputDirectory == null || !inputDirectory.exists()
				|| !inputDirectory.isDirectory()) {
			return Collections.emptySet();
		}

		if (getLog().isDebugEnabled()) {
			getLog().debug(
					"Directory " + inputDirectory.getAbsolutePath()
							+ " sucessfully validated.");
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
