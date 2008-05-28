package org.mobicents.plugins.du;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.installer.ArtifactInstallationException;
import org.apache.maven.artifact.installer.ArtifactInstaller;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Build;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.project.artifact.ActiveProjectArtifact;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;
import org.mobicents.plugins.du.deployconfig.DeployConfig;
import org.mobicents.plugins.du.deployconfig.RAEntity;
import org.mobicents.plugins.du.servicexml.ServiceIds;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Base class for creating a deployable unit.
 * 
 * @author <a href="michele.laporta@gmail.com">Michele La Porta</a>
 * @author eduardomartins
 * @version $Id$
 */
public abstract class AbstractDuMojo extends AbstractMojo {

	/**
	 * Base directory.
	 * 
	 * @parameter expression="${basedir}"
	 * @required
	 * @readonly
	 */
	private File basedir;

	// ///////////////////////////////////////////////
	// REACTOR
	// ///////////////////////////////////////////////

	/**
	 * Contains the full list of projects in the reactor.
	 * 
	 * @parameter default-value="${reactorProjects}"
	 * @required
	 * @readonly
	 */
	private List reactorProjects;

	/**
	 * Project builder
	 * 
	 * @component
	 */
	protected MavenProjectBuilder mavenProjectBuilder;

	/**
	 * The local repository.
	 * 
	 * @parameter expression="${localRepository}"
	 */
	protected ArtifactRepository localRepository;
	
    /**
     * Used to look up Artifacts in the remote repository.
     * 
     * @parameter expression="${component.org.apache.maven.artifact.factory.ArtifactFactory}"
     * @required
     * @readonly
     */
    protected org.apache.maven.artifact.factory.ArtifactFactory factory;
	

    /**
     * Used to look up Artifacts in the remote repository.
     * 
     * @parameter expression="${component.org.apache.maven.artifact.resolver.ArtifactResolver}"
     * @required
     * @readonly
     */
    protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;

    /**
     * List of Remote Repositories used by the resolver
     * 
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     * @required
     */
    protected java.util.List remoteRepos;

	// ///////////////////////////////////////////////
	// RESOURCES
	// ///////////////////////////////////////////////
	/**
	 * The list of resources we want to transfer.
	 * 
	 * @parameter expression="${project.resources}"
	 * @required
	 */
	private List resources;

	/**
	 * The character encoding scheme to be applied.
	 * 
	 * @parameter
	 */
	private String encoding;

	private Properties filterProperties;

	private static final String[] EMPTY_STRING_ARRAY = {};

	/**
	 * The list of additional key-value pairs aside from that of the System, and that of the project, which would be
	 * used for the filtering.
	 * 
	 * @parameter expression="${project.build.filters}"
	 */
	private List filters;

	/**
	 * @parameter expression="${component.org.apache.maven.artifact.installer.ArtifactInstaller}"
	 * @required @ readonly
	 */
	protected ArtifactInstaller installer;

	/**
	 * Default artifact handler.
	 * 
	 * @parameter expression="${component.org.apache.maven.artifact.handler.ArtifactHandler}" @ readonly
	 * @required
	 */
	protected org.apache.maven.artifact.handler.ArtifactHandler artifactHandler;

	/**
	 * The location of the deployable-unit.xml file to be used within the deployable unit.
	 * 
	 * @parameter expression="${basedir}/src/main/resources/META-INF/deployable-unit.xml"
	 */
	private File duFile;

	// /////////////////////////////////////////////////////////////////////
	// JAR
	// /////////////////////////////////////////////////////////////////////

	private static final String[] DEFAULT_EXCLUDES = new String[] { "**/package.html" };

	private static final String[] DEFAULT_INCLUDES = new String[] { "**/**" };

	/**
	 * List of files to include. Specified as fileset patterns.
	 * 
	 * @parameter
	 */
	private String[] includes;

	/**
	 * List of files to exclude. Specified as fileset patterns.
	 * 
	 * @parameter
	 */
	private String[] excludes;

	/**
	 * Directory containing the generated JAR.
	 * 
	 * @parameter expression="${project.build.outputDirectory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * Directory target.
	 * 
	 * @parameter expression="${project.build.output}"
	 */
	private File targetDirectory;

	/**
	 * Name of the generated JAR.
	 * 
	 * @parameter alias="jarName" expression="${jar.finalName}" default-value="${project.build.finalName}"
	 * @required
	 */
	private String finalName;

	/**
	 * The Jar archiver.
	 * 
	 * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
	 * @required
	 */
	private JarArchiver jarArchiver;

	/**
	 * The Maven project.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * The archive configuration to use. See <a href="http://maven.apache.org/shared/maven-archiver/index.html">the
	 * documentation for Maven Archiver</a>.
	 * 
	 * @parameter
	 */
	private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();

	/**
	 * Path to the default MANIFEST file to use. It will be used if <code>useDefaultManifestFile</code> is set to
	 * <code>true</code>.
	 * 
	 * @parameter expression="${project.build.outputDirectory}/META-INF/MANIFEST.MF"
	 * @required
	 * @readonly
	 * @since 2.2
	 */
	private File defaultManifestFile;

	/**
	 * Set this to <code>true</code> to enable the use of the <code>defaultManifestFile</code>.
	 * 
	 * @parameter expression="${jar.useDefaultManifestFile}" default-value="false"
	 * @since 2.2
	 */
	private boolean useDefaultManifestFile;

	/**
	 * @component
	 */
	private MavenProjectHelper projectHelper;

	/**
	 * Whether creating the archive should be forced.
	 * 
	 * @parameter expression="${jar.forceCreation}" default-value="false"
	 */
	private boolean forceCreation;

	/**
	 * Return the specific output directory to serve as the root for the archive.
	 */
	// protected abstract File getClassesDirectory();
	protected final MavenProject getProject() {
		return project;
	}

	/**
	 * Overload this to produce a jar with another classifier, for example a test-jar.
	 */
	protected abstract String getClassifier();

	/**
	 * Overload this to produce a test-jar, for example.
	 */
	protected abstract String getType();

	protected static File getJarFile(File basedir, String finalName,
			String classifier) {
		if (classifier == null) {
			classifier = "";
		} else if (classifier.trim().length() > 0
				&& !classifier.startsWith("-")) {
			classifier = "-" + classifier;
		}

		return new File(basedir, finalName + classifier + ".jar");
	}

	/**
	 * Default Manifest location. Can point to a non existing file. Cannot return null.
	 */
	protected File getDefaultManifestFile() {
		return defaultManifestFile;
	}
	
	/**
	 * Generates the DU.
	 * 
	 * @todo Add license files in META-INF directory.
	 */
	public File createArchive()
			throws MojoExecutionException {

		if (targetDirectory == null) {
			targetDirectory = new File(basedir.getAbsolutePath(), "target");
		}
		if (!targetDirectory.exists()) {
			targetDirectory.mkdir();
		}

		File jarFile = getJarFile(targetDirectory, finalName, getClassifier());

		MavenArchiver archiver = new MavenArchiver();
		archiver.setArchiver(jarArchiver);
		archiver.setOutputFile(jarFile);
		archive.setForced(forceCreation);
		
		try {

			if (outputDirectory == null) {
				outputDirectory = new File(basedir.getAbsolutePath(),
						"target/classes");
			}	
			if (!outputDirectory.exists()) {
				outputDirectory.mkdir();
			}

			// resource processing
			List includedResources = copyResources(resources, outputDirectory);		
			
			// http://code.google.com/p/mobicents/issues/detail?id=100			
			
			getLog().info("***************************************************************");
			getLog().info("Analizing " + project.getArtifactId()+ " for deployable-unit.xml");
			getLog().info("***************************************************************");
			
			List<String> componentFilenames = new ArrayList<String>();
			
			// get dependency with compile scope 
			for (Iterator iter = project.getDependencyArtifacts().iterator(); iter.hasNext();) {
				
				Artifact projectDependencyArtifact = (Artifact) iter.next();
				
				if (projectDependencyArtifact.getScope().equals(Artifact.SCOPE_COMPILE)) {
					
			        Artifact pomArtifact = this.factory.createArtifact( projectDependencyArtifact.getGroupId(), projectDependencyArtifact.getArtifactId(), projectDependencyArtifact.getVersion(), "", "pom" );			        
			        this.resolver.resolve( pomArtifact, remoteRepos, this.localRepository );
			        MavenProject projectDependencyArtifactMavenProject = mavenProjectBuilder.buildWithDependencies( pomArtifact.getFile(), this.localRepository,null);

			        for (Iterator iterator = projectDependencyArtifactMavenProject.getDependencyArtifacts().iterator(); iterator.hasNext();) {
			        	
			        	Artifact dependencyArtifact = (Artifact) iterator.next();
			        	
		        		for (Iterator itera = project.getDependencyArtifacts().iterator(); itera.hasNext();) {
		        		
		        			Artifact anArtifact = (Artifact)itera.next();
		        			
		        			if(anArtifact.getArtifactId().equals(dependencyArtifact.getArtifactId()) &&
		        					anArtifact.getGroupId().equals(dependencyArtifact.getGroupId()) &&
		        					anArtifact.getVersion().equals(dependencyArtifact.getVersion()) &&
		        					!anArtifact.getScope().equals("runtime") ){
		        				
		        				// add artifacts with dependencies first
		        				if(!componentFilenames.contains(anArtifact.getFile().getName())){
		    		        		getLog().info("" + projectDependencyArtifact.getArtifactId() + " depends on [" + dependencyArtifact.getArtifactId() +"]");
		        					componentFilenames.add(anArtifact.getFile().getName());
		        				}
		        			}
		        		}
			        }
				}
			}
			
			// process dependencies
			for (Iterator iter = project.getDependencyArtifacts().iterator(); iter.hasNext();) {
				
				Artifact artifact = (Artifact) iter.next();
				
				if (artifact.getScope().equals(Artifact.SCOPE_COMPILE)) {
					getLog().info(
							"Adding component artifact: "
									+ artifact.getFile().getName());
					archiver.getArchiver().addFile(artifact.getFile(),artifact.getFile().getName());
					
					// second add artifacts without dependencies,for the deployable-unit generation later
					if(!componentFilenames.contains(artifact.getFile().getName()))
						componentFilenames.add(artifact.getFile().getName());
				}
				
				else if (artifact.getScope().equals(Artifact.SCOPE_RUNTIME)) {
					getLog().info(
							"Adding library artifact: "
									+ artifact.getFile().getName());
					archiver.getArchiver().addFile(artifact.getFile(),
							"library/" + artifact.getFile().getName());
				}
				
				else {
					getLog().warn("Ignoring dependency of unsupported scope: "+ artifact.getFile().getName());
				}
			}
			
			// generate a deployable-unit.xml to add to du archive only for pom packaging
			generateDeployableUnitDescriptor(componentFilenames, includedResources);			
			
			// generate a build.xml to manage the DU without maven
			generateManagementAntScript(includedResources);
			
			// add output directory
			archiver.getArchiver().addDirectory(outputDirectory);
			// create archive
			archiver.createArchive(project, archive);
			return jarFile;

		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new MojoExecutionException("Error assembling DU", e);
		}
	}

	
	/**
	 * Generates the DU.
	 * 
	 * @todo Add license files in META-INF directory.
	 */
	public void execute() throws MojoExecutionException {
		
		getLog().debug("executing Maven DU plugin");
		
		File duFile = createArchive();

		// verify is works
		String classifier = getClassifier();
		if (classifier != null) {
			projectHelper.attachArtifact(getProject(), getType(), classifier,
					duFile);
		} else {
			getProject().getArtifact().setFile(duFile);
		}

		// install in local repo
		try {
			Artifact duArtifact = new DefaultArtifact(project.getGroupId(),
					project.getArtifactId(), VersionRange
							.createFromVersion(project.getVersion()),
					Artifact.SCOPE_RUNTIME, "jar", "", artifactHandler);

			duArtifact.setFile(duFile);

			installer.install(duFile, duArtifact, localRepository);

		} catch (ArtifactInstallationException e) {
			throw new MojoExecutionException(
					"Cannot install du in local repository", e);
		}

	}

	/**
	 * Copy resources with filtering if enable
	 * 
	 * @param resources
	 * @param outputDirectory
	 * @return
	 * @throws MojoExecutionException
	 */
	protected List copyResources(List resources, File outputDirectory)
			throws MojoExecutionException {
		initializeFiltering();

		if (encoding == null || encoding.length() < 1) {
			getLog().info("Using default encoding to copy filtered resources.");
		} else {
			getLog().info(
					"Using '" + encoding + "' to copy filtered resources.");
		}

		List includedFiles = new ArrayList();

		for (Iterator i = resources.iterator(); i.hasNext();) {
			Resource resource = (Resource) i.next();

			String targetPath = resource.getTargetPath();

			File resourceDirectory = new File(resource.getDirectory());
			if (!resourceDirectory.isAbsolute()) {
				resourceDirectory = new File(project.getBasedir(),
						resourceDirectory.getPath());
			}

			if (!resourceDirectory.exists()) {
				getLog().info(
						"Resource directory does not exist: "
								+ resourceDirectory);
				continue;
			}

			// this part is required in case the user specified "../something"
			// as destination
			// see MNG-1345
			if (!outputDirectory.exists()) {
				if (!outputDirectory.mkdirs()) {
					throw new MojoExecutionException(
							"Cannot create resource output directory: "
									+ outputDirectory);
				}
			}

			DirectoryScanner scanner = new DirectoryScanner();

			scanner.setBasedir(resourceDirectory);
			if (resource.getIncludes() != null
					&& !resource.getIncludes().isEmpty()) {
				scanner.setIncludes((String[]) resource.getIncludes().toArray(
						EMPTY_STRING_ARRAY));
			} else {
				scanner.setIncludes(DEFAULT_INCLUDES);
			}

			if (resource.getExcludes() != null
					&& !resource.getExcludes().isEmpty()) {
				scanner.setExcludes((String[]) resource.getExcludes().toArray(
						EMPTY_STRING_ARRAY));
			}

			scanner.addDefaultExcludes();
			scanner.scan();

			includedFiles = Arrays.asList(scanner.getIncludedFiles());

			getLog().info(
					"Copying " + includedFiles.size() + " resource "
							+ (includedFiles.size() > 1 ? "s" : "")
							+ (targetPath == null ? "" : " to " + targetPath));

			for (Iterator j = includedFiles.iterator(); j.hasNext();) {
				String name = (String) j.next();

				String destination = name;

				if (targetPath != null) {
					destination = targetPath + "/" + name;
				}

				File source = new File(resourceDirectory, name);

				File destinationFile = new File(outputDirectory, destination);

				if (!destinationFile.getParentFile().exists()) {
					destinationFile.getParentFile().mkdirs();
				}

				try {
					copyFile(source, destinationFile, resource.isFiltering());
				} catch (IOException e) {
					throw new MojoExecutionException("Error copying resource "
							+ source, e);
				}
			}
		}
		return includedFiles;
	}

	/**
	 * @throws MojoExecutionException
	 */
	private void initializeFiltering() throws MojoExecutionException {
		filterProperties = new Properties();

		// System properties
		filterProperties.putAll(System.getProperties());

		// Project properties
		filterProperties.putAll(project.getProperties());

		// Take a copy of filterProperties to ensure that evaluated filterTokens
		// are not propagated
		// to subsequent filter files. NB this replicates current behaviour and
		// seems to make sense.
		final Properties baseProps = new Properties();
		baseProps.putAll(this.filterProperties);

		for (Iterator i = filters.iterator(); i.hasNext();) {
			String filtersfile = (String) i.next();

			try {
				Properties properties = PropertyUtils.loadPropertyFile(
						new File(filtersfile), baseProps);

				filterProperties.putAll(properties);
			} catch (IOException e) {
				throw new MojoExecutionException(
						"Error loading property file '" + filtersfile + "'", e);
			}
		}
	}

	/**
	 * Copy file with filtering
	 * 
	 * @param from
	 * @param to
	 * @param filtering
	 * @throws IOException
	 */
	private void copyFile(File from, final File to, boolean filtering)
			throws IOException {
		FileUtils.FilterWrapper[] wrappers = null;
		if (filtering) {
			wrappers = new FileUtils.FilterWrapper[] {
			// support ${token}
					new FileUtils.FilterWrapper() {
						public Reader getReader(Reader reader) {
							return new InterpolationFilterReader(reader,
									filterProperties, "${", "}");
						}
					},
					// support @token@
					new FileUtils.FilterWrapper() {
						public Reader getReader(Reader reader) {
							return new InterpolationFilterReader(reader,
									filterProperties, "@", "@");
						}
					},

					new FileUtils.FilterWrapper() {
						public Reader getReader(Reader reader) {
							boolean isPropertiesFile = false;

							if (to.isFile()
									&& to.getName().endsWith(".properties")) {
								isPropertiesFile = true;
							}

							return new InterpolationFilterReader(reader,
									new ReflectionProperties(project,
											isPropertiesFile), "${", "}");
						}
					} };
		}

		FileUtils.copyFile(from, to, encoding, wrappers);

	}

	private void generateManagementAntScript(
			List includedFiles) throws MojoExecutionException,
			DependencyResolutionRequiredException {

		getLog().info("Generating ant script for management without maven...");

		File buildFile = new File(targetDirectory,"build.xml");

		PrintWriter printWriter = null;
		try {
			// read header and footer
			String header = "";
			String footer = "";
			LinkedList<String> deployElements = new LinkedList<String>();
			LinkedList<String> undeployElements = new LinkedList<String>();
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						this.getClass().getResourceAsStream("build.header")));
				String str = "";
				while ((str = in.readLine()) != null) {
					header += str + "\n";
				}
				in = new BufferedReader(new InputStreamReader(this.getClass()
						.getResourceAsStream("build.footer")));
				while ((str = in.readLine()) != null) {
					footer += str + "\n";
				}
			} catch (IOException e) {
				throw new MojoExecutionException(
						"failed to read header and footer of build.xml file", e);
			}

			// first process deploy-config file
			File deployConfigFile = new File(outputDirectory, "META-INF"
					+ File.separator + "deploy-config.xml");
			if (deployConfigFile.exists()) {

				try {
					getLog().info("Parsing deploy-config.xml without validation");
					// parse doc into dom
					DocumentBuilderFactory factory = DocumentBuilderFactory
							.newInstance();
					DocumentBuilder parser = factory.newDocumentBuilder();
					Document document = parser.parse(deployConfigFile);

					// parse dom into DeployConfig
					DeployConfig deployConfig = DeployConfig.parse(document
							.getDocumentElement());

					for (RAEntity raEntity : deployConfig.getRaEntities()) {

						// let's start by processing properties
						Properties properties = new Properties();
						if (!raEntity.getPropertiesFile().equals("")) {
							// we have a properties file in META-INF
							File propertiesFile = new File(outputDirectory,
									"META-INF" + File.separator
											+ raEntity.getPropertiesFile());
							if (propertiesFile.exists()) {
								properties.load(new FileInputStream(
										propertiesFile));
							}
						}
						// put all properties defined in the deploy-config also
						properties.putAll(raEntity.getProperties());

						// ok, now we do the strings for the ant script
						// the ant call to create and activate the ra entity
						String xml = "\t\t<antcall target=\"activate-raentity\">\n"
								+ "\t\t\t<param name=\"ra.entity\" value=\""
								+ raEntity.getEntityName()
								+ "\" />\n"
								+ "\t\t\t<param name=\"ra.id\" value=\""
								+ raEntity.getResourceAdaptorId() + "\" />\n";
						if (!properties.isEmpty()) {
							// generate file in target and add property to ant call
							xml += "\t\t\t<param name=\"ra.entity.properties.filename\" value=\""
									+ raEntity.getEntityName()
									+ ".properties\" />\n";
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
						xml += "\t\t</antcall>\n";
						// add this to the deployElements tail
						deployElements.addLast(xml);

						// the ant call to deactivate and remove the ra entity

						xml = "\t\t<antcall target=\"deactivate-raentity\">\n"
								+ "\t\t\t<param name=\"ra.entity\" value=\""
								+ raEntity.getEntityName() + "\" />\n"
								+ "\t\t\t<param name=\"ra.id\" value=\""
								+ raEntity.getResourceAdaptorId() + "\" />\n"
								+ "\t\t</antcall>\n";

						// add this to the undeployElements head
						undeployElements.addFirst(xml);

						// second we bind/unbind ra links
						for (String raLink : raEntity.getRaLinks()) {
							xml = "\t\t<antcall target=\"bind-ralink\">\n"
									+ "\t\t\t<param name=\"ra.entity\" value=\""
									+ raEntity.getEntityName() + "\" />\n"
									+ "\t\t\t<param name=\"ra.link\" value=\""
									+ raLink + "\" />\n" + "\t\t</antcall>\n";
							// add this to the deployElements tail
							deployElements.addLast(xml);

							xml = "\t\t<antcall target=\"unbind-ralink\">\n"
									+ "\t\t\t<param name=\"ra.entity\" value=\""
									+ raEntity.getEntityName() + "\" />\n"
									+ "\t\t\t<param name=\"ra.link\" value=\""
									+ raLink + "\" />\n" + "\t\t</antcall>\n";
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

			for (Iterator i = includedFiles.iterator(); i.hasNext();) {
				String fileName = (String) i.next();
				if (fileName.startsWith("services" + File.separator)) {

					File serviceDescriptorFile = new File(outputDirectory,
							fileName);
					try {
						// http://code.google.com/p/mobicents/issues/detail?id=104
						getLog().info("Parsing " + fileName +" with validation");
						
						// parse doc into dom
						DocumentBuilderFactory factory = DocumentBuilderFactory
								.newInstance();
						factory.setValidating(true);
						DocumentBuilder parser = factory.newDocumentBuilder();
						
						parser.setEntityResolver(new EntityResolver() {
							public InputSource resolveEntity(String publicID,String systemID) throws SAXException {
								
								InputStream is = null;
							    BufferedReader br = null;
							    String line;
							    StringWriter stringWriter = null;
							    StringReader stringReader = null;
								try {
									URI uri = new URI(systemID);
									getLog().info("Resolving " + systemID +" locally from " +uri);
									String filename = uri.toString().substring(uri.toString().lastIndexOf("/")+1,uri.toString().length());
									getLog().info("Resolved filename " + filename);
									stringWriter = new StringWriter();
									is = getClass().getClassLoader().getResourceAsStream(filename);
									br = new BufferedReader(new InputStreamReader(is));
								    while (null != (line = br.readLine())) {
								    	stringWriter.write(line);
								    }
								    stringWriter.flush();
								    if (stringWriter != null) stringWriter.close();
								    
								    stringReader = new StringReader(stringWriter.getBuffer().toString());

								    return new InputSource(stringReader);
										
								} catch (URISyntaxException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								}finally {
									try {
									    if (br != null) br.close();
								    	if (is != null) is.close();
								    }catch (IOException e) {
										e.printStackTrace();
								    }
								}

								// If no match, returning null makes process continue normally
								return null;
							}
						});
						
						parser.setErrorHandler(new ErrorHandler() {
							public void warning(SAXParseException exception) throws SAXException {
								getLog().warn(exception.getMessage());
							}
							public void fatalError(SAXParseException exception) throws SAXException {
								getLog().warn(exception.getMessage());
							}
							public void error(SAXParseException exception) throws SAXException {
								getLog().error(exception.getMessage());
							}
						});
						
						Document document = parser.parse(serviceDescriptorFile);

						// parse dom into service ids
						ServiceIds serviceIds = ServiceIds.parse(document
								.getDocumentElement());

						for (String serviceId : serviceIds.getIds()) {

							String xml = "\n\t\t<antcall target=\"activate-service\">\n"
									+ "\t\t\t<param name=\"service.id\" value=\""
									+ serviceId
									+ "\" />\n"
									+ "\t\t</antcall>\n";

							// add this to the deployElements tail
							deployElements.addLast(xml);

							xml = "\n\t\t<antcall target=\"deactivate-service\">\n"
									+ "\t\t\t<param name=\"service.id\" value=\""
									+ serviceId
									+ "\" />\n"
									+ "\t\t</antcall>\n";

							// add this to the undeployElements head
							undeployElements.addFirst(xml);
						}

					} catch (Exception e) {
						e.printStackTrace();
						throw new MojoExecutionException(
								"failed to parse service descriptor "
										+ fileName, e);
					}
				}
			}

			// now lets glue everything and write the build.xml script

			String xml = header + "\n\t<property name=\"du.filename\" value=\""
					+ project.getBuild().getFinalName()
					+ ".jar\" />\n" + "\n\t<target name=\"deploy-jmx\">\n"
					+ "\n\t\t<antcall target=\"install-DU\" />\n";

			for (String s : deployElements) {
				xml += s;
			}

			xml += "\n\t</target>\n" + "\n\t<target name=\"undeploy-jmx\">\n";

			for (String s : undeployElements) {
				xml += s;
			}

			xml += footer;

			printWriter = new PrintWriter(new FileWriter(buildFile));
			printWriter.write(xml);
			getLog().info("build.xml generated in target dir");

		} catch (IOException e) {
			throw new MojoExecutionException("Error preparing the build.xml: "
					+ e.getMessage(), e);
		} finally {
			IOUtil.close(printWriter);
		}

	}

	/**
	 * Generate deployable-unit.xml
	 * 
	 * @param anArchiver
	 * @param includedResources
	 * @throws MojoExecutionException
	 * @throws DependencyResolutionRequiredException
	 */
	private void generateDeployableUnitDescriptor(List componentfilenames,
			List includedResources) throws MojoExecutionException,
			DependencyResolutionRequiredException {

		File deployableUnitDescriptorDir = new File(outputDirectory, "META-INF");
		if (!deployableUnitDescriptorDir.exists()) {
			deployableUnitDescriptorDir.mkdirs();
		}
		File duFile = new File(deployableUnitDescriptorDir.getAbsolutePath(),
				"deployable-unit.xml");

		PrintWriter printWriter = null;
		try {
			StringBuffer xml = new StringBuffer(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE deployable-unit PUBLIC \"-//Sun Microsystems, Inc.//DTD JAIN SLEE Deployable Unit 1.0//EN\" \"http://java.sun.com/dtd/slee-deployable-unit_1_0.dtd\">\n<deployable-unit>\n");

			// add modules dependencies generated artifacts
			for (Iterator i = componentfilenames.iterator(); i.hasNext();) {
				String componentFilename =  (String) i.next();
				xml.append("\t<jar>" + componentFilename + "</jar>\n");
				getLog().info("Added DU component jar " + componentFilename);
			}

			for (Iterator i = includedResources.iterator(); i.hasNext();) {
				String fileName = (String) i.next();
				getLog().info("file " + fileName);

				if (fileName.startsWith("services" + File.separator)) {
					// in the form services/service.xml
					if (fileName.contains("\\")) {
						fileName = fileName.replace("\\", "/");
					}

					xml.append("\t<service-xml>" + fileName
							+ "</service-xml>\n");
					getLog().info("Added DU service descriptor " + fileName);
				}
			}

			xml.append("</deployable-unit>");

			printWriter = new PrintWriter(new FileWriter(duFile));
			printWriter.write(xml.toString());
			getLog().info(
					"Generated DU descriptor: " + duFile.getAbsolutePath()
							+ "\n" + xml + "\n");

		} catch (IOException e) {
			throw new MojoExecutionException(
					"Error preparing the deployable-unit.xml: "
							+ e.getMessage(), e);
		} finally {
			IOUtil.close(printWriter);
			this.duFile = duFile;

		}

	}

}
