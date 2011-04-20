/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
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
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * @author cath
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorTypeFinder extends BaseFinder {

  @Override
  protected DTDXML loadJar(JarFile file, JarEntry entry, String jarLocation) throws Exception {
    return new ResourceAdaptorTypeJarXML(file, entry, jarLocation);
  }

  private static ResourceAdaptorTypeFinder raTypeFinder = new ResourceAdaptorTypeFinder();

  public static ResourceAdaptorTypeFinder getDefault() {
    return raTypeFinder;
  }

  public DTDXML loadJar(JarFile jar, JarEntry entry) throws Exception {
    return new ResourceAdaptorTypeJarXML(jar, entry, null);
  }

  public DTDXML loadFile(IFile file) throws Exception {
    return new ResourceAdaptorTypeJarXML(file);
  }

  protected DTDXML getInnerXML(DTDXML outerXML, String className) throws Exception {
    if (outerXML instanceof ResourceAdaptorTypeJarXML) {
      ResourceAdaptorTypeJarXML ResourceAdaptorTypeJar = (ResourceAdaptorTypeJarXML) outerXML;
      ResourceAdaptorTypeXML raType = ResourceAdaptorTypeJar.getResourceAdaptorType(className);
      return raType;
    }

    return null;
  }

  /**
   * ResourceAdaptorTypeJarXML getResourceAdaptorTypeJarXML(ICompilationUnit)
   * IFile getResourceAdaptorTypeJarXMLFile(ICompilationUnit)
   * 
   * IFile getResourceAdaptorTypeActivityContextInterfaceFactoryFile(ICompilationUnit)
   * IFile getResourceAdaptorTypeActivityContextInterfaceFactoryFile(IFile xmlFile, ...)
   * 
   * IFile getResourceAdaptorTypeResourceAdaptorInterfaceFile(ICompilationUnit)
   * IFile getResourceAdaptorTypeResourceAdaptorInterfaceFile(IFile xmlFile, ...)
   * 
   * IFile[] getResourceAdaptorTypeActivityTypeFiles(ICompilationUnit)
   * IFile[] getResourceAdaptorTypeActivityTypeFiles(IFile xmlFile, ...)
   */

  public static ResourceAdaptorTypeJarXML getResourceAdaptorTypeJarXML(ICompilationUnit unit) {
    try {
      return new ResourceAdaptorTypeJarXML(getResourceAdaptorTypeJarXMLFile(unit));
    }
    catch (Exception e) {
      return null;
    }
  }

  public static IFile getResourceAdaptorTypeJarXMLFile(ICompilationUnit unit) {

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

        if (file.getName().endsWith("resource-adaptor-type-jar.xml")) {
          try {       
            ResourceAdaptorTypeJarXML xml = new ResourceAdaptorTypeJarXML(file);
            xml.getResourceAdaptorType(clazzName);
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

  public static IFile getResourceAdaptorTypeActivityContextInterfaceFactoryFile(ICompilationUnit unit) {    
    try {
      String clazzName = EclipseUtil.getClassName(unit);
      ResourceAdaptorTypeJarXML raTypeJarXML = getResourceAdaptorTypeJarXML(unit);
      ResourceAdaptorTypeXML raType = raTypeJarXML.getResourceAdaptorType(clazzName);
      String name = raType.getResourceAdaptorTypeClasses().getActivityContextInterfaceFactoryInterface();
      IPath path = new Path(name.replaceAll("\\.", "/") + ".java");
      IFolder folder = getSourceFolder(unit);
      IFile file = folder.getFile(path);
      if (file.exists()) {
        return file;
      }
      return null;
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static IFile getResourceAdaptorTypeActivityContextInterfaceFactoryFile(IFile xmlFile, String name, String vendor, String version) {
    try {
      ResourceAdaptorTypeJarXML raTypeJarXML = new ResourceAdaptorTypeJarXML(xmlFile);
      ResourceAdaptorTypeXML raType = raTypeJarXML.getResourceAdaptorType(name, vendor, version);
      String className = raType.getResourceAdaptorTypeClasses().getActivityContextInterfaceFactoryInterface();
      IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
      IFolder folder = getSourceFolder(xmlFile);
      // ammendonca: we are at xxx/src/main/resources. move to xxx/src/main/java
      folder = folder.getFolder(".." + File.separator + "java");
      IFile file = folder.getFile(path);
      if (file.exists()) {
        return file;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static IFile getResourceAdaptorTypeResourceAdaptorInterfaceFile(ICompilationUnit unit) {    
    try {
      String clazzName = EclipseUtil.getClassName(unit);
      ResourceAdaptorTypeJarXML raTypeJarXML = getResourceAdaptorTypeJarXML(unit);
      ResourceAdaptorTypeXML raType = raTypeJarXML.getResourceAdaptorType(clazzName);
      String name = raType.getResourceAdaptorTypeClasses().getResourceAdaptorInterface();
      IPath path = new Path(name.replaceAll("\\.", "/") + ".java");
      IFolder folder = getSourceFolder(unit);
      IFile file = folder.getFile(path);
      if (file.exists()) {
        return file;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static IFile getResourceAdaptorTypeResourceAdaptorInterfaceFile(IFile xmlFile, String name, String vendor, String version) {
    try {
      ResourceAdaptorTypeJarXML raTypeJarXML = new ResourceAdaptorTypeJarXML(xmlFile);
      ResourceAdaptorTypeXML raType = raTypeJarXML.getResourceAdaptorType(name, vendor, version);
      String className = raType.getResourceAdaptorTypeClasses().getActivityContextInterfaceFactoryInterface();
      IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
      IFolder folder = getSourceFolder(xmlFile);
      // ammendonca: we are at xxx/src/main/resources. move to xxx/src/main/java
      folder = folder.getFolder(".." + File.separator + "java");
      IFile file = folder.getFile(path);
      if (file.exists()) {
        return file;
      }
      return null;
    }
    catch (Exception e) {
      return null;
    }
  }

  public static IFile[] getResourceAdaptorTypeActivityTypeFiles(ICompilationUnit unit) {    
    ArrayList<IFile> files = new ArrayList<IFile>();
    try {
      String clazzName = EclipseUtil.getClassName(unit);
      ResourceAdaptorTypeJarXML raTypeJarXML = getResourceAdaptorTypeJarXML(unit);
      ResourceAdaptorTypeXML raType = raTypeJarXML.getResourceAdaptorType(clazzName);
      for(String name : raType.getResourceAdaptorTypeClasses().getActivityTypes()) {
        IPath path = new Path(name.replaceAll("\\.", "/") + ".java");
        IFolder folder = getSourceFolder(unit);
        IFile file = folder.getFile(path);
        if (file.exists()) {
          files.add(file);
        }
      }
      return files.toArray(new IFile[]{});
    }
    catch (Exception e) {
      return files.toArray(new IFile[]{});
    }
  }

  public static IFile[] getResourceAdaptorTypeActivityTypeFiles(IFile xmlFile, String name, String vendor, String version) {
    ArrayList<IFile> files = new ArrayList<IFile>();
    try {
      ResourceAdaptorTypeJarXML raTypeJarXML = new ResourceAdaptorTypeJarXML(xmlFile);
      ResourceAdaptorTypeXML raType = raTypeJarXML.getResourceAdaptorType(name, vendor, version);
      for(String className : raType.getResourceAdaptorTypeClasses().getActivityTypes()) {
        IPath path = new Path(className.replaceAll("\\.", "/") + ".java");
        IFolder folder = getSourceFolder(xmlFile);
        // ammendonca: we are at xxx/src/main/resources. move to xxx/src/main/java
        folder = folder.getFolder(".." + File.separator + "java");
        IFile file = folder.getFile(path);
        if (file.exists()) {
          files.add(file);
        }
      }
      return files.toArray(new IFile[]{});
    }
    catch (Exception e) {
      return files.toArray(new IFile[]{});
    }
  }

}
