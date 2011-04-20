/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
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
package org.mobicents.eclipslee.servicecreation.util;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorClassXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorJarXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorFinder extends BaseFinder {

  @Override
  protected DTDXML loadJar(JarFile file, JarEntry entry, String jarLocation) throws Exception {
    return new ResourceAdaptorJarXML(file, entry, jarLocation);
  }

  private static ResourceAdaptorFinder raTypeFinder = new ResourceAdaptorFinder();

  public static ResourceAdaptorFinder getDefault() {
    return raTypeFinder;
  }

  public DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {
    return new ResourceAdaptorJarXML(jar, entry, null);
  }

  public DTDXML loadFile(IFile file) throws Exception {
    return new ResourceAdaptorJarXML(file);
  }

  protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
    if (outerXML instanceof ResourceAdaptorJarXML) {
      ResourceAdaptorJarXML ResourceAdaptorJar = (ResourceAdaptorJarXML) outerXML;
      ResourceAdaptorXML resourceAdaptor = ResourceAdaptorJar.getResourceAdaptor(className);
      return resourceAdaptor;
    }

    return null;
  }

  /**
   * ResourceAdaptorJarXML getResourceAdaptorJarXML(ICompilationUnit)
   * IFile getResourceAdaptorJarXMLFile(ICompilationUnit)
   * 
   * IFile[] getResourceAdaptorClassFiles(ICompilationUnit)
   * IFile[] getResourceAdaptorClassFiles(IFile xmlFile, ...)
   */

  public static ResourceAdaptorJarXML getResourceAdaptorJarXML(ICompilationUnit unit) {
    try {
      return new ResourceAdaptorJarXML(getResourceAdaptorJarXMLFile(unit));
    }
    catch (Exception e) {
      return null;
    }
  }

  public static IFile getResourceAdaptorJarXMLFile(ICompilationUnit unit) {

    String clazzName = EclipseUtil.getClassName(unit);

    try {
      IContainer folder = unit.getCorrespondingResource().getParent();
      // ammendonca: for maven we have XML at .../src/main/resources/META-INF, we are at .../src/main/java/<package>/...
      while(!folder.getName().equals("main")) {
        folder = folder.getParent();
      }
      folder = folder.getFolder(new Path("resources/META-INF"));
      IResource children[] = folder.members(IResource.FILE);

      for (int i = 0;i < children.length;i++) {
        // For some reason directories come in also...
        if(!(children[i] instanceof IFile)) {
          continue;
        }

        IFile file = (IFile) children[i];

        if (file.getName().endsWith("resource-adaptor-jar.xml")) {
          try {       
            ResourceAdaptorJarXML xml = new ResourceAdaptorJarXML(file);
            xml.getResourceAdaptor(clazzName);
          }
          catch (Exception e) {
            //e.printStackTrace();
            //System.err.println(e.getMessage());
            continue;
          }
          return file;
        }
      }     
    }
    catch (JavaModelException e) {
      return null;
    }
    catch (CoreException e) {
      return null;
    }

    return null;
  }

  public static IFile[] getResourceAdaptorClassFiles(ICompilationUnit unit) {    
    try {
      String clazzName = EclipseUtil.getClassName(unit);
      ResourceAdaptorJarXML resourceAdaptorJarXML = getResourceAdaptorJarXML(unit);
      ResourceAdaptorXML resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(clazzName);
      ResourceAdaptorClassXML[] resourceAdaptorClasses = resourceAdaptor.getResourceAdaptorClasses().getResourceAdaptorClasses();
      ArrayList<IFile> files = new ArrayList<IFile>();
      for(ResourceAdaptorClassXML resourceAdaptorClass : resourceAdaptorClasses) {
        String resourceAdaptorClassName = resourceAdaptorClass.getResourceAdaptorClassName();
        IPath path = new Path(resourceAdaptorClassName.replaceAll("\\.", "/") + ".java");
        IFolder folder = getSourceFolder(unit);
        IFile file = folder.getFile(path);
        if (file.exists()) {
          files.add(file);
        }
      }
      return files.toArray(new IFile[files.size()]);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static IFile[] getResourceAdaptorClassFiles(IFile xmlFile, String name, String vendor, String version) {
    try {
      ResourceAdaptorJarXML resourceAdaptorJarXML = new ResourceAdaptorJarXML(xmlFile);
      ResourceAdaptorXML resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(name, vendor, version);
      ResourceAdaptorClassXML[] resourceAdaptorClasses = resourceAdaptor.getResourceAdaptorClasses().getResourceAdaptorClasses();
      ArrayList<IFile> files = new ArrayList<IFile>();
      for(ResourceAdaptorClassXML resourceAdaptorClass : resourceAdaptorClasses) {
        String resourceAdaptorClassName = resourceAdaptorClass.getResourceAdaptorClassName();
        IPath path = new Path(resourceAdaptorClassName.replaceAll("\\.", "/") + ".java");
        IFolder folder = getSourceFolder(xmlFile);
        // ammendonca: we are at xxx/src/main/resources. move to xxx/src/main/java
        folder = folder.getFolder(".." + File.separator + "java");
        IFile file = folder.getFile(path);
        if (file.exists()) {
          files.add(file);
        }
      }
      return files.toArray(new IFile[files.size()]);
    }
    catch (Exception e) {
      return null;
    }
  }

  public static IFile[] getResourceAdaptorMarshalerClassFiles(ICompilationUnit unit) {
    try {
      String clazzName = EclipseUtil.getClassName(unit);
      ResourceAdaptorJarXML resourceAdaptorJarXML = getResourceAdaptorJarXML(unit);
      ResourceAdaptorXML resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(clazzName);
      ResourceAdaptorClassXML[] resourceAdaptorClasses = resourceAdaptor.getResourceAdaptorClasses().getResourceAdaptorClasses();
      ArrayList<IFile> files = new ArrayList<IFile>();
      for(ResourceAdaptorClassXML resourceAdaptorClass : resourceAdaptorClasses) {
        String resourceAdaptorMarshalerClassName = resourceAdaptorClass.getResourceAdaptorClassName().replace("ResourceAdaptor", "Marshaler");
        IPath path = new Path(resourceAdaptorMarshalerClassName.replaceAll("\\.", "/") + ".java");
        IFolder folder = getSourceFolder(unit);
        IFile file = folder.getFile(path);
        if (file.exists()) {
          files.add(file);
        }
      }
      return files.toArray(new IFile[files.size()]);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static IFile[] getResourceAdaptorMarshalerClassFiles(IFile xmlFile, String name, String vendor, String version) {
    ArrayList<IFile> files = new ArrayList<IFile>();
    try {
      ResourceAdaptorJarXML resourceAdaptorJarXML = new ResourceAdaptorJarXML(xmlFile);
      ResourceAdaptorXML resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(name, vendor, version);
      ResourceAdaptorClassXML[] resourceAdaptorClasses = resourceAdaptor.getResourceAdaptorClasses().getResourceAdaptorClasses();
      for(ResourceAdaptorClassXML resourceAdaptorClass : resourceAdaptorClasses) {
        String resourceAdaptorMarshalerClassName = resourceAdaptorClass.getResourceAdaptorClassName().replace("ResourceAdaptor", "Marshaler");
        IPath path = new Path(resourceAdaptorMarshalerClassName.replaceAll("\\.", "/") + ".java");
        IFolder folder = getSourceFolder(xmlFile);
        // ammendonca: we are at xxx/src/main/resources. move to xxx/src/main/java
        folder = folder.getFolder(".." + File.separator + "java");
        IFile file = folder.getFile(path);
        if (file.exists()) {
          files.add(file);
        }
      }
      return files.toArray(new IFile[files.size()]);
    }
    catch (Exception e) {
      return files.toArray(new IFile[files.size()]);
    }
  }

  public static IFile[] getResourceAdaptorProviderImplClassFiles(ICompilationUnit unit) {
    ArrayList<IFile> files = new ArrayList<IFile>();
    try {
      String clazzName = EclipseUtil.getClassName(unit);
      ResourceAdaptorJarXML resourceAdaptorJarXML = getResourceAdaptorJarXML(unit);
      ResourceAdaptorXML resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(clazzName);
      DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.ALL/* BINARY */, unit.getJavaProject().getProject().getName());
      for (int i = 0; i < xml.length; i++) {
        ResourceAdaptorTypeJarXML raTypeJar = (ResourceAdaptorTypeJarXML) xml[i];
        ResourceAdaptorTypeXML[] raTypes = raTypeJar.getResourceAdaptorTypes();
        for(ResourceAdaptorTypeXML raType : raTypes) {
          for(ResourceAdaptorResourceAdaptorTypeXML raRaType : resourceAdaptor.getResourceAdaptorTypes()) {
            if(raType.getName().equals(raRaType.getName()) && raType.getVendor().equals(raRaType.getVendor()) && raType.getVersion().equals(raRaType.getVersion())) {
              String resourceAdaptorProviderImplClassName = raType.getResourceAdaptorTypeClasses().getResourceAdaptorInterface() + "Impl";
              IPath path = new Path(resourceAdaptorProviderImplClassName.replaceAll("\\.", "/") + ".java");
              IFolder folder = getSourceFolder(unit);
              IFile file = folder.getFile(path);
              if (file.exists()) {
                files.add(file);
              }
            }
          }
        }
      }
      return files.toArray(new IFile[files.size()]);
    }
    catch (Exception e) {
      return files.toArray(new IFile[files.size()]);
    }
  }

  public static IFile[] getResourceAdaptorProviderImplClassFiles(IFile xmlFile, String name, String vendor, String version) {
    ArrayList<IFile> files = new ArrayList<IFile>();
    try {
      ResourceAdaptorJarXML resourceAdaptorJarXML = new ResourceAdaptorJarXML(xmlFile);
      ResourceAdaptorXML resourceAdaptor = resourceAdaptorJarXML.getResourceAdaptor(name, vendor, version);
      DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.ALL/* BINARY */, xmlFile.getProject().getName());
      for (int i = 0; i < xml.length; i++) {
        ResourceAdaptorTypeJarXML raTypeJar = (ResourceAdaptorTypeJarXML) xml[i];
        ResourceAdaptorTypeXML[] raTypes = raTypeJar.getResourceAdaptorTypes();
        for(ResourceAdaptorTypeXML raType : raTypes) {
          for(ResourceAdaptorResourceAdaptorTypeXML raRaType : resourceAdaptor.getResourceAdaptorTypes()) {
            if(raType.getName().equals(raRaType.getName()) && raType.getVendor().equals(raRaType.getVendor()) && raType.getVersion().equals(raRaType.getVersion())) {
              String resourceAdaptorProviderImplClassName = raType.getResourceAdaptorTypeClasses().getResourceAdaptorInterface() + "Impl";
              IPath path = new Path(resourceAdaptorProviderImplClassName.replaceAll("\\.", "/") + ".java");
              IFolder folder = getSourceFolder(xmlFile);
              // ammendonca: we are at xxx/src/main/resources. move to xxx/src/main/java
              folder = folder.getFolder(".." + File.separator + "java");
              IFile file = folder.getFile(path);
              if (file.exists()) {
                files.add(file);
              }
            }
          }
        }
      }
      return files.toArray(new IFile[files.size()]);
    }
    catch (Exception e) {
      return files.toArray(new IFile[files.size()]);
    }
  }

}
