package org.mobicents.eclipslee.util.maven;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.maven.model.Build;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.mobicents.eclipslee.servicecreation.util.ProjectModules;

public class MavenProjectUtils {

  // Constants -----------------------------------------------------------

  private static final String DEFAULT_GROUP_ID = "org.mobicents";
  private static final String DEFAULT_ARTIFACT_ID = "mobicents-example";
  private static final String DEFAULT_VERSION = "1.0";

  private static final String EVENTS_MODULE_ARTIFACT_ID_PREFIX = "";
  private static final String EVENTS_MODULE_ARTIFACT_ID_SUFFIX = "-events";

  private static final String SBB_MODULE_ARTIFACT_ID_PREFIX = "";
  private static final String SBB_MODULE_ARTIFACT_ID_SUFFIX = "-sbb";

  private static final String PROFILE_SPEC_MODULE_ARTIFACT_ID_PREFIX = "";
  private static final String PROFILE_SPEC_MODULE_ARTIFACT_ID_SUFFIX = "-profile-spec";

  //private static final String SERVICE_MODULE_ARTIFACT_ID_PREFIX = "";
  //private static final String SERVICE_MODULE_ARTIFACT_ID_SUFFIX = "-service";

  private static final String RA_TYPE_MODULE_ARTIFACT_ID_PREFIX = "";
  private static final String RA_TYPE_MODULE_ARTIFACT_ID_SUFFIX = "-ratype";

  private static final String RA_MODULE_ARTIFACT_ID_PREFIX = "";
  private static final String RA_MODULE_ARTIFACT_ID_SUFFIX = "-ra";

  private static final String LIBRARY_MODULE_ARTIFACT_ID_PREFIX = "";
  private static final String LIBRARY_MODULE_ARTIFACT_ID_SUFFIX = "-library";

  private static final String DEPLOYABLE_UNIT_MODULE_ARTIFACT_ID_PREFIX = "";
  private static final String DEPLOYABLE_UNIT_MODULE_ARTIFACT_ID_SUFFIX = "-DU";

  private static final String LIBRARY_PLUGIN_GROUP_ID = "org.mobicents.tools";
  private static final String LIBRARY_PLUGIN_ARTIFACT_ID = "maven-library-plugin";

  private static final String DU_PLUGIN_GROUP_ID = "org.mobicents.tools";
  private static final String DU_PLUGIN_ARTIFACT_ID = "maven-du-plugin";

  private static final Dependency JAIN_SLEE_DEPENDENCY = new Dependency();

  static {
    JAIN_SLEE_DEPENDENCY.setGroupId("javax.slee");
    JAIN_SLEE_DEPENDENCY.setArtifactId("jain-slee");
  }

  public static void generateMavenPomFiles(IProject project, ProjectModules projectModules) {
    // Parent pom is always needed
    generateParentPomFile(project, projectModules);

    if(projectModules.hasEvents()) {
      generateEventsModulePomFile(project, projectModules, null);
    }
    if(projectModules.hasSBB()) {
      generateSBBModulePomFile(project, projectModules, null);
    }
    if(projectModules.hasProfileSpec()) {
      generateProfileSpecModulePomFile(project, projectModules, null);
    }
    if(projectModules.hasRAType()) {
      generateRATypeModulePomFile(project, projectModules, null);
    }
    if(projectModules.hasRA()) {
      generateRAModulePomFile(project, projectModules, null);
    }
    if(projectModules.hasLibrary()) {
      generateLibraryModulePomFile(project, projectModules, null);
    }
    if(projectModules.hasDeployableUnit()) {
      generateDeployableUnitModulePomFile(project, projectModules);
    }
  }

  public static void generateParentPomFile(IProject project, ProjectModules projectModules) {
    Model model = new Model();

    model.setModelVersion("4.0.0");
    model.setPackaging("pom");

    // Set the parent to org.mobicents:mobicents-parent:2.17
    // TODO: Get parent version from SVN ? ...
    Parent parent = new Parent();
    parent.setGroupId("org.mobicents");
    parent.setArtifactId("mobicents-parent");
    parent.setVersion("2.17");
    model.setParent(parent);

    // TODO: Ask for these values in wizard?
    model.setGroupId(DEFAULT_GROUP_ID);
    model.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    model.setVersion(DEFAULT_VERSION);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Set child modules
    for(String module : projectModules.getModules()) {
      model.addModule(module);
    }

    writePomFile(model, project.getLocation().append("pom.xml").toString());
  }

  public static void generateEventsModulePomFile(IProject project, ProjectModules projectModules, String customName) {
    Model model = new Model();

    model.setModelVersion("4.0.0");

    // Set the parent
    Parent parent = new Parent();
    parent.setGroupId(DEFAULT_GROUP_ID);
    parent.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    parent.setVersion(DEFAULT_VERSION);
    model.setParent(parent);

    model.setArtifactId(EVENTS_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + (customName != null ? ("-" + customName) : "") + EVENTS_MODULE_ARTIFACT_ID_SUFFIX);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Add JAIN SLEE Dependency
    addDependency(model, JAIN_SLEE_DEPENDENCY);

    // Add inner dependencies
    if(projectModules != null) {
      if(projectModules.hasLibrary()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(LIBRARY_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + LIBRARY_MODULE_ARTIFACT_ID_SUFFIX);
        dependency.setVersion("1.0"); // FIXME dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }
    }

    // Write the pom file
    writePomFile(model, project.getLocation().append((customName != null ? customName + "-" : "") + "events/pom.xml").toString());
  }

  public static void generateSBBModulePomFile(IProject project, ProjectModules projectModules, String customName) {
    Model model = new Model();

    model.setModelVersion("4.0.0");

    // Set the parent
    Parent parent = new Parent();
    parent.setGroupId(DEFAULT_GROUP_ID);
    parent.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    parent.setVersion(DEFAULT_VERSION);
    model.setParent(parent);

    model.setArtifactId(SBB_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + (customName != null ? ("-" + customName) : "") + SBB_MODULE_ARTIFACT_ID_SUFFIX);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Add JAIN SLEE Dependency
    addDependency(model, JAIN_SLEE_DEPENDENCY);

    // Add inner dependencies
    if(projectModules != null) {
      if(projectModules.hasLibrary()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(LIBRARY_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + LIBRARY_MODULE_ARTIFACT_ID_SUFFIX);
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }

      if(projectModules.hasEvents()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(EVENTS_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + EVENTS_MODULE_ARTIFACT_ID_SUFFIX);
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }

      if(projectModules.hasProfileSpec()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(PROFILE_SPEC_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + PROFILE_SPEC_MODULE_ARTIFACT_ID_SUFFIX);
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }
    }
    // Write the pom file
    writePomFile(model, project.getLocation().append((customName != null ? customName + "-" : "") + "sbb/pom.xml").toString());
  }

  public static void generateProfileSpecModulePomFile(IProject project, ProjectModules projectModules, String customName) {
    Model model = new Model();

    model.setModelVersion("4.0.0");

    // Set the parent
    Parent parent = new Parent();
    parent.setGroupId(DEFAULT_GROUP_ID);
    parent.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    parent.setVersion(DEFAULT_VERSION);
    model.setParent(parent);

    model.setArtifactId(PROFILE_SPEC_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + (customName != null ? ("-" + customName) : "") + PROFILE_SPEC_MODULE_ARTIFACT_ID_SUFFIX);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Add JAIN SLEE Dependency
    addDependency(model, JAIN_SLEE_DEPENDENCY);

    // Add inner dependencies
    if(projectModules != null) {
      if(projectModules.hasLibrary()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(LIBRARY_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + LIBRARY_MODULE_ARTIFACT_ID_SUFFIX);
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }
    }

    // Write the pom file
    writePomFile(model, project.getLocation().append((customName != null ? customName + "-" : "") + "profile-spec/pom.xml").toString());
  }

  public static void generateRATypeModulePomFile(IProject project, ProjectModules projectModules, String customName) {
    Model model = new Model();

    model.setModelVersion("4.0.0");

    // Set the parent
    Parent parent = new Parent();
    parent.setGroupId(DEFAULT_GROUP_ID);
    parent.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    parent.setVersion(DEFAULT_VERSION);
    model.setParent(parent);

    model.setArtifactId(RA_TYPE_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + (customName != null ? ("-" + customName) : "") + RA_TYPE_MODULE_ARTIFACT_ID_SUFFIX);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Add JAIN SLEE Dependency
    addDependency(model, JAIN_SLEE_DEPENDENCY);

    // Add inner dependencies
    if(projectModules != null) {
      if(projectModules.hasLibrary()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(LIBRARY_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + LIBRARY_MODULE_ARTIFACT_ID_SUFFIX);
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }

      if(projectModules.hasEvents()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(parent.getArtifactId() + "-events");
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }
    }
    // Write the pom file
    writePomFile(model, project.getLocation().append((customName != null ? customName + "-" : "") + "ratype/pom.xml").toString());
  }

  public static void generateRAModulePomFile(IProject project, ProjectModules projectModules, String customName) {
    Model model = new Model();

    model.setModelVersion("4.0.0");

    // Set the parent
    Parent parent = new Parent();
    parent.setGroupId(DEFAULT_GROUP_ID);
    parent.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    parent.setVersion(DEFAULT_VERSION);
    model.setParent(parent);

    model.setArtifactId(RA_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + (customName != null ? ("-" + customName) : "") + RA_MODULE_ARTIFACT_ID_SUFFIX);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Add JAIN SLEE Dependency
    addDependency(model, JAIN_SLEE_DEPENDENCY);

    // Add inner dependencies
    if(projectModules != null) {
      if(projectModules.hasLibrary()) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(parent.getArtifactId() + "-ratype");
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }
    }

    // Write the pom file
    writePomFile(model, project.getLocation().append((customName != null ? customName + "-" : "") + "ra/pom.xml").toString());
  }

  public static void generateLibraryModulePomFile(IProject project, ProjectModules projectModules, String customName) {
    Model model = new Model();

    model.setModelVersion("4.0.0");
    model.setPackaging("jainslee-library");

    // Set the parent
    Parent parent = new Parent();
    parent.setGroupId(DEFAULT_GROUP_ID);
    parent.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    parent.setVersion(DEFAULT_VERSION);
    model.setParent(parent);

    model.setArtifactId(LIBRARY_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + (customName != null ? ("-" + customName) : "") + LIBRARY_MODULE_ARTIFACT_ID_SUFFIX);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Add build
    Build build = new Build();

    // Add DU Plugin
    Plugin libraryPlugin = new Plugin();
    libraryPlugin.setGroupId(LIBRARY_PLUGIN_GROUP_ID);
    libraryPlugin.setArtifactId(LIBRARY_PLUGIN_ARTIFACT_ID);
    libraryPlugin.setExtensions(true);

    // Build configuration as Xpp3Dom object
    Xpp3Dom libraryPluginConfiguration = new Xpp3Dom("configuration");
    Xpp3Dom libraryPluginConfigurationLibraryName = new Xpp3Dom("library-name");
    libraryPluginConfigurationLibraryName.setValue(project.getName() + "-library");
    Xpp3Dom libraryPluginConfigurationLibraryVendor = new Xpp3Dom("library-vendor");
    libraryPluginConfigurationLibraryVendor.setValue(DEFAULT_GROUP_ID);
    Xpp3Dom libraryPluginConfigurationLibraryVersion = new Xpp3Dom("library-version");
    libraryPluginConfigurationLibraryVersion.setValue(DEFAULT_VERSION);

    libraryPluginConfiguration.addChild(libraryPluginConfigurationLibraryName);
    libraryPluginConfiguration.addChild(libraryPluginConfigurationLibraryVendor);
    libraryPluginConfiguration.addChild(libraryPluginConfigurationLibraryVersion);

    libraryPlugin.setConfiguration(libraryPluginConfiguration);

    build.addPlugin(libraryPlugin);
    model.setBuild(build);

    // Write the pom file
    writePomFile(model, project.getLocation().append((customName != null ? customName + "-" : "") + "library/pom.xml").toString());
  }

  public static void generateDeployableUnitModulePomFile(IProject project, ProjectModules projectModules) {
    Model model = new Model();

    model.setModelVersion("4.0.0");

    // Set the parent
    Parent parent = new Parent();
    parent.setGroupId(DEFAULT_GROUP_ID);
    parent.setArtifactId(project.getName() != null ? project.getName() : DEFAULT_ARTIFACT_ID);
    parent.setVersion(DEFAULT_VERSION);
    model.setParent(parent);

    model.setArtifactId(DEPLOYABLE_UNIT_MODULE_ARTIFACT_ID_PREFIX + parent.getArtifactId() + DEPLOYABLE_UNIT_MODULE_ARTIFACT_ID_SUFFIX);

    model.setName("Mobicents :: ${pom.artifactId} v${pom.version}");

    // Add other modules as dependencies
    for(String projectModule : projectModules.getModules()) {
      if(!projectModule.equals("du")) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(parent.getGroupId());
        dependency.setArtifactId(parent.getArtifactId() + "-" + projectModule);
        dependency.setVersion(parent.getVersion()); // FIXME dependency.setVersion("${pom.version}");
        model.addDependency(dependency);
      }
    }

    // Add build
    Build build = new Build();

    // Add DU Plugin
    Plugin duPlugin = new Plugin();
    duPlugin.setGroupId(DU_PLUGIN_GROUP_ID);
    duPlugin.setArtifactId(DU_PLUGIN_ARTIFACT_ID);

    PluginExecution duPluginExecution = new PluginExecution();
    duPluginExecution.addGoal("copy-dependencies");
    duPluginExecution.addGoal("generate-descriptor");
    duPluginExecution.addGoal("generate-ant-management-script");

    ArrayList<PluginExecution> duPluginExecutions = new ArrayList<PluginExecution>();
    duPluginExecutions.add(duPluginExecution);

    duPlugin.setExecutions(duPluginExecutions);

    build.addPlugin(duPlugin);

    // Add the antrun plugin for deploying
    Plugin antRunPlugin = new Plugin();
    antRunPlugin.setArtifactId("maven-antrun-plugin");

    PluginExecution deployDUPluginExecution = new PluginExecution();
    deployDUPluginExecution.setId("deploy-DU");
    deployDUPluginExecution.setPhase("install");
    deployDUPluginExecution.addGoal("run");

    // Build configuration as Xpp3Dom object
    Xpp3Dom deployDUPluginConfiguration = new Xpp3Dom("configuration");
    Xpp3Dom deployDUPluginConfigurationTasks = new Xpp3Dom("tasks");
    Xpp3Dom deployDUPluginConfigurationTasksCopy = new Xpp3Dom("copy");
    deployDUPluginConfigurationTasksCopy.setAttribute("overwrite", "true");
    deployDUPluginConfigurationTasksCopy.setAttribute("file", "target/${project.build.finalName}.jar");
    deployDUPluginConfigurationTasksCopy.setAttribute("todir", "${jboss.home}/server/${node}/deploy");
    deployDUPluginConfigurationTasks.addChild(deployDUPluginConfigurationTasksCopy);
    deployDUPluginConfiguration.addChild(deployDUPluginConfigurationTasks);

    deployDUPluginExecution.setConfiguration(deployDUPluginConfiguration);

    antRunPlugin.addExecution(deployDUPluginExecution);

    // Add the antrun plugin for undeploying
    PluginExecution undeployDUPluginExecution = new PluginExecution();
    undeployDUPluginExecution.setId("undeploy-DU");
    undeployDUPluginExecution.setPhase("clean");
    undeployDUPluginExecution.addGoal("run");

    // Build configuration as Xpp3Dom object
    Xpp3Dom undeployDUPluginConfiguration = new Xpp3Dom("configuration");
    Xpp3Dom undeployDUPluginConfigurationTasks = new Xpp3Dom("tasks");
    Xpp3Dom undeployDUPluginConfigurationTasksCopy = new Xpp3Dom("delete");
    undeployDUPluginConfigurationTasksCopy.setAttribute("file", "${jboss.home}/server/${node}/deploy/${project.build.finalName}.jar");
    undeployDUPluginConfigurationTasks.addChild(undeployDUPluginConfigurationTasksCopy);
    undeployDUPluginConfiguration.addChild(undeployDUPluginConfigurationTasks);

    undeployDUPluginExecution.setConfiguration(undeployDUPluginConfiguration);

    antRunPlugin.addExecution(undeployDUPluginExecution);

    build.addPlugin(antRunPlugin);

    model.setBuild(build);

    // Write the pom file
    writePomFile(model, project.getLocation().append("du/pom.xml").toString());
  }

  public static void writePomFile(Model model, String path) {
    try {
      Writer fileWriter = new FileWriter(path);
      new MavenXpp3Writer().write(fileWriter, model);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Model readPomFile(IProject project, String moduleName) {
    try {
      MavenXpp3Reader reader = new MavenXpp3Reader();
      IFile pomFile = moduleName == null ? project.getFile("pom.xml") : project.getFolder(moduleName).getFile("pom.xml");
      Model model = reader.read(new InputStreamReader(pomFile.getContents()));
      return model;
    }
    catch (Exception e) {
      return null;
    }
  }
  
  public static String getArtifactId(Model model) {
    return model.getArtifactId();
  }

  public static String getGroupId(Model model) {
    return model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId();
  }

  public static String getVersion(Model model) {
    return model.getVersion() == null ? model.getParent().getVersion() : model.getVersion();
  }

  public static boolean hasDependency(Model model, Dependency dep) {
    for(Dependency modelDep : model.getDependencies()) {
      if(modelDep.getArtifactId().equals(dep.getArtifactId()) && modelDep.getGroupId().equals(dep.getGroupId()) && modelDep.getVersion().equals(dep.getVersion())) {
        return true;
      }
    }
    return false;
  }

  public static boolean addDependency(Model model, Dependency dep) {
    if(!hasDependency(model, dep)) {
      model.getDependencies().add(dep);
      return true;
    }
    return false;
  }

  public static boolean removeDependency(Model model, Dependency dep) {
    for(Dependency modelDep : model.getDependencies()) {
      if(modelDep.getArtifactId().equals(dep.getArtifactId()) && modelDep.getGroupId().equals(dep.getGroupId()) && modelDep.getVersion().equals(dep.getVersion())) {
        model.removeDependency(modelDep);
        return true;
      }
    }
    return false;
  }

}
