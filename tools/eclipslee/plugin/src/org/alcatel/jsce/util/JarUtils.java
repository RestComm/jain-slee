
/**
 *   Copyright 2006 Alcatel, OSP.
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
package org.alcatel.jsce.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.alcatel.jsce.servicecreation.wizards.ra.xml.ResourceAdaptorJarBase;
import org.alcatel.jsce.servicecreation.wizards.ra.xml.ResourceAdaptorXML;
import org.alcatel.jsce.util.log.SCELogger;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.DeployableUnitXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;
import org.mobicents.eclipslee.xml.SbbJarXML;

// HACKED
//import com.alcatel.osp.smftools.bundleeditor.JarTreatment;


/**
 *  Description:
 * <p>
 * Utility object used to extract, veriffy, etc. component JAR file.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class JarUtils
{
    private static SimpleFileFilter simpleFileFilter = new SimpleFileFilter( new String[]{".zip",".jar"});
    private JarUtils()
    {
    }

    public static boolean fileExists( File jar, String path ) throws IOException
    {
        ZipFile jarFile = new ZipFile( jar );
        try
        {
            return jarFile.getEntry(path) != null;
        } finally
        {
            StreamUtils.safeClose( jarFile );
        }
    }
    
    /**
     * @param jar the jar file
     * @param path the path we are looking for (META-INF/sbb-jar.xml)
     * @param compoID the component ID
     * @return true if the correpsonding deploymenent descriptor is found and if it is valid.
     * @throws IOException
     */
    public static boolean fileExistsAnVeriffyDTD( File jar, String path, int compoID) throws IOException
    {
		JarFile jaredFile = new JarFile(jar);
		try {
			JarEntry entry = jaredFile.getJarEntry(path);
			if (entry != null) {
				switch (compoID) {
				case JainUtils.EVENT_TYPEID:
					try {
						new EventJarXML(jaredFile, entry,jar.getPath());
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.PROFILE_TYPEID:
					try {
						new ProfileSpecJarXML(jaredFile, entry,jar.getPath());
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.DEPLOYABLE_UNIT_TYPEID:
					try {
						new DeployableUnitXML(jaredFile, entry);
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.RES_TYPE_TYPEID:
					try {
						new ResourceAdaptorTypeJarXML(jaredFile, entry, jar.getPath());
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.RES_TYPEID:
					try {
						new ResourceAdaptorJarBase(jaredFile, entry, jar.getPath());
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.SBB_TYPEID:
					try {
						new SbbJarXML(jaredFile, entry, jar.getPath());
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				default:
					break;
				}
			}
			return true;
		} finally {
			StreamUtils.safeClose(jaredFile);
		}
	}

    
    /**
     * Extract the name, version, vendor of all component of this jar file. Keys are NAME, VENDOR, VERSION, TYPE
     * @param jar the jar file
     * @param path the path we are looking for (META-INF/sbb-jar.xml)
     * @param compoID the component ID
     * @return true if the correpsonding deploymenent descriptor is found and if it is valid.
     * @throws IOException
     */
    public static HashMap[] extractXMLInformation( File jar, int compoID) throws IOException
    {
    	List components = new ArrayList();
		JarFile jaredFile = new JarFile(jar);
		String path="?";
		switch (compoID) {
		case JainUtils.EVENT_TYPEID:
			path = JainUtils.EVENT_DESCRIPTOR_FILE;
			break;
		case JainUtils.PROFILE_TYPEID:
			path = JainUtils.PROFILE_SPEC_DESCRIPTOR_FILE;
			break;
		case JainUtils.DEPLOYABLE_UNIT_TYPEID:
			path = JainUtils.DEPLOYABLE_UNIT_DESCRIPTOR_FILE;
			break;
		case JainUtils.RES_TYPE_TYPEID:
			path = JainUtils.RESOURCE_ADAPTOR_TYPE_DESCRIPTOR_FILE;
			break;
		case JainUtils.RES_TYPEID:
				path = JainUtils.RESOURCE_ADAPTOR_DESCRIPTOR_FILE;
			break;
		case JainUtils.SBB_TYPEID:
			path = JainUtils.SBB_DESCRIPTOR_FILE;
			break;
		default:
			break;
		}
		try {
			JarEntry entry = jaredFile.getJarEntry(path);
			if (entry != null) {
				switch (compoID) {
				case JainUtils.EVENT_TYPEID:
					try {
						EventJarXML eventJarXML = new EventJarXML(jaredFile, entry,jar.getPath());
						EventXML[] events =  eventJarXML.getEvents();
						for (int i = 0; i < events.length; i++) {
							EventXML eventXML = events[i];
							HashMap map_i = new HashMap();
							map_i.put("NAME", eventXML.getName());
							map_i.put("VENDOR", eventXML.getVendor());
							map_i.put("VERSION", eventXML.getVersion());
							map_i.put("TYPE", new Integer(JainUtils.EVENT_TYPEID));
							components.add(map_i);
						}
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.PROFILE_TYPEID:
					try {
						ProfileSpecJarXML profileSpecJarXML = new ProfileSpecJarXML(jaredFile, entry,jar.getPath());
						ProfileSpecXML[] profileSpecXML = profileSpecJarXML.getProfileSpecs();
						for (int i = 0; i < profileSpecXML.length; i++) {
							ProfileSpecXML specXML = profileSpecXML[i];
							HashMap map_i = new HashMap();
							map_i.put("NAME", specXML.getName());
							map_i.put("VENDOR", specXML.getVendor());
							map_i.put("VERSION", specXML.getVersion());
							map_i.put("TYPE", new Integer(JainUtils.PROFILE_TYPEID));
							components.add(map_i);
						}
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.DEPLOYABLE_UNIT_TYPEID:
					/*
					try {
						//We will see inside each component the component to add
						DeployableUnitXML deployableUnitXML = new DeployableUnitXML(jaredFile, entry);
						String[] jars = deployableUnitXML.getJars();
						JarTreatment jarTreatment = new JarTreatment(jar);
						jarTreatment.unjar();
						File unzipDir = jarTreatment.getTargetDirectory();
						for (int i = 0; i < jars.length; i++) {
							String path_i = jars[i];
							File compoJar = new File(unzipDir, path_i);
							int compoType = JainUtils.getInstance().getJarType(compoJar);
							HashMap[] compos = extractXMLInformation(compoJar, compoType);
							for (int j = 0; j < compos.length; j++) {
								HashMap map = compos[j];
								components.add(map);
							}
						}
						jarTreatment.clear();
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}*/
					break;
				case JainUtils.RES_TYPE_TYPEID:
					try {
						ResourceAdaptorTypeJarXML resourceAdaptorTypeJarXML = new ResourceAdaptorTypeJarXML(jaredFile, entry, jar.getPath());
						ResourceAdaptorTypeXML[] resourceAdaptorTypeXMLs = resourceAdaptorTypeJarXML.getResourceAdaptorTypes();
						for (int i = 0; i < resourceAdaptorTypeXMLs.length; i++) {
							ResourceAdaptorTypeXML typeXML = resourceAdaptorTypeXMLs[i];
							HashMap map_i = new HashMap();
							map_i.put("NAME", typeXML.getName());
							map_i.put("VENDOR", typeXML.getVendor());
							map_i.put("VERSION", typeXML.getVersion());
							map_i.put("TYPE", new Integer(JainUtils.RES_TYPE_TYPEID));
							components.add(map_i);
						}
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.RES_TYPEID:
					try {
						ResourceAdaptorJarBase resourceAdaptorJarBase = new ResourceAdaptorJarBase(jaredFile, entry, jar.getPath());
						ResourceAdaptorXML[] resourceAdaptorXMLs = resourceAdaptorJarBase.getResourceAdaptors();
						for (int i = 0; i < resourceAdaptorXMLs.length; i++) {
							ResourceAdaptorXML adaptorXML = resourceAdaptorXMLs[i];
							HashMap map_i = new HashMap();
							map_i.put("NAME", adaptorXML.getName());
							map_i.put("VENDOR", adaptorXML.getVendor());
							map_i.put("VERSION", adaptorXML.getVersion());
							map_i.put("TYPE", new Integer(JainUtils.RES_TYPEID));
							components.add(map_i);
						}
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				case JainUtils.SBB_TYPEID:
					try {
						SbbJarXML sbbJarXML = new SbbJarXML(jaredFile, entry, jar.getPath());
						SbbXML[] sbbXMLs = sbbJarXML.getSbbs();
						for (int i = 0; i < sbbXMLs.length; i++) {
							SbbXML sbbXML = sbbXMLs[i];
							HashMap map_i = new HashMap();
							map_i.put("NAME", sbbXML.getName());
							map_i.put("VENDOR", sbbXML.getVendor());
							map_i.put("VERSION", sbbXML.getVersion());
							map_i.put("TYPE", new Integer(JainUtils.SBB_TYPEID));
							components.add(map_i);
						}
					} catch (Exception e) {
						SCELogger.logError("The component in " + jar.getPath() +" is not valid", e);
					}
					break;
				default:
					break;
				}
			}else{
				SCELogger.logError("Error while openening jar entry in "+ jar.getName(), new IllegalStateException("No corresponding entry !"));
			}
			return (HashMap[]) components.toArray(new HashMap[components.size()]);
		} finally {
			StreamUtils.safeClose(jaredFile);
		}
	}
    public static boolean[] fileExists( File jar, String[] path ) throws IOException
    {
        boolean[] result = new boolean[path.length];
        ZipFile jarFile = new ZipFile( jar );
        try
        {
            for ( int i = 0; i < path.length; i++ )
            {
                result[i] = jarFile.getEntry( path[i] ) != null;
            }
            return result;
        } finally
        {
            StreamUtils.safeClose( jarFile );
        }
    }

/*    public static boolean fileExists( String jarUrl, String path ) throws IOException
    {
        String documentUrl = "jar:" + jarUrl + "!/";
        JarURLConnection jar = (JarURLConnection)new URL(documentUrl).openConnection();
        JarFile jarFile = jar.getJarFile();
        return jarFile.getEntry(path) != null;
    }
*/

public static File explodeJarFile(File jarFile, File directory,
                                    boolean localCopy, boolean createSubDirectory) throws IOException
{
    ZipInputStream zipInputStream = null;
    try
    {
        // copy the jar to the file system
        if (localCopy)
        {
            String filename = FileUtils.getFileName(jarFile);
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            File copyfile = new File(directory, filename);
            try
            {
                inputStream = new FileInputStream(jarFile);
                outputStream = new FileOutputStream(copyfile);
                StreamUtils.copyStream(inputStream, outputStream);
            } catch (IOException ex)
            {
                StreamUtils.safeClose(outputStream);
                StreamUtils.safeClose(inputStream);
                throw ex;
            }
            zipInputStream = new ZipInputStream(new BufferedInputStream(new
                FileInputStream(copyfile)));
        } else
        {
            InputStream inputStream = new FileInputStream(jarFile);
            zipInputStream = new ZipInputStream(new BufferedInputStream(
                inputStream));
        }
        if ( createSubDirectory )
        {
            directory = new File( directory, FileUtils.removeFileExt( FileUtils.getFileName(jarFile) ));
            directory.mkdirs();
        }
        while (true)
        {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            if (zipEntry == null)
            {
                break;
            }
            if (zipEntry.isDirectory())
            {
                // create the destination directory
                File newDir = new File(directory, zipEntry.getName());
                newDir.mkdirs();
                if (!newDir.isDirectory())
                {
                    throw new IOException("Unable to create the directory " +
                                          newDir.toString());
                }
                continue;
            }
            File file = new File(directory, zipEntry.getName());
            file.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            try
            {
                StreamUtils.copyStream(zipInputStream, fileOutputStream);
            } finally
            {
                StreamUtils.safeClose(fileOutputStream);
            }
        }
    } finally
    {
        StreamUtils.safeClose(zipInputStream);
    }
    return directory;
}

public static boolean extractEntry(JarFile jarFile, String entryName, String outputFile) throws IOException
{
    // return false if the entry does not exist

    boolean result = true;
    try
    {
       // Get the entry and its input stream.
       JarEntry entry = jarFile.getJarEntry(entryName);
       // If the entry is not null, extract it. Otherwise, print a message.
       if (entry != null)
       {
          // Get an input stream for the entry.
          InputStream entryStream = jarFile.getInputStream(entry);
          try
          {
             // Create the output file (clobbering the file if it exists).
             FileOutputStream file = new FileOutputStream(outputFile);
             try
             {
                // Allocate a buffer for reading the entry data.
                byte[] buffer = new byte[1024];
                int bytesRead;
                // Read the entry data and write it to the output file.
                while ((bytesRead = entryStream.read(buffer)) != -1)
                {
                   file.write(buffer, 0, bytesRead);
                }
             }
             finally
             {
                file.close();
             }
          }
          finally
          {
             entryStream.close();
          }
       }
       else
       {
           result = false;
       }
    }
    finally
    {
      
    }
    return result;
 }


    public static File explodeJarFile(URL url, File directory,
                                        boolean localCopy, boolean createSubDirectory) throws IOException
    {
        ZipInputStream zipInputStream = null;
        try
        {
            // copy the jar to the file system
            if (localCopy)
            {
                String filename = FileUtils.getFileName(url);
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                File copyfile = new File(directory, filename);
                try
                {
                    inputStream = url.openStream();
                    outputStream = new FileOutputStream(copyfile);
                    StreamUtils.copyStream(inputStream, outputStream);
                } catch (IOException ex)
                {
                    StreamUtils.safeClose(outputStream);
                    StreamUtils.safeClose(inputStream);
                    throw ex;
                }
                zipInputStream = new ZipInputStream(new BufferedInputStream(new
                    FileInputStream(copyfile)));
            } else
            {
                InputStream inputStream = url.openStream();
                zipInputStream = new ZipInputStream(new BufferedInputStream(
                    inputStream));
            }
            if ( createSubDirectory )
            {
                directory = new File( directory, FileUtils.removeFileExt( FileUtils.getFileName(url) ));
                directory.mkdirs();
            }
            while (true)
            {
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                if (zipEntry == null)
                {
                    break;
                }
                if (zipEntry.isDirectory())
                {
                    // create the destination directory
                    File newDir = new File(directory, zipEntry.getName());
                    newDir.mkdirs();
                    if (!newDir.isDirectory())
                    {
                        throw new IOException("Unable to create the directory " +
                                              newDir.toString());
                    }
                    continue;
                }
                File file = new File(directory, zipEntry.getName());
                file.getParentFile().mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                try
                {
                    StreamUtils.copyStream(zipInputStream, fileOutputStream);
                } finally
                {
                    StreamUtils.safeClose(fileOutputStream);
                }
            }
        } finally
        {
            StreamUtils.safeClose(zipInputStream);
        }
        return directory;
    }

    /**
     * Test if a file is a ZIP or JAR archive.
     *
     * @param file the file to be tested.
     * @return true if the file is a ZIP/JAR archive, false otherwise.
     */
    public static boolean isZipOrJarArchive(File file)
    {
        boolean isArchive = true;
        ZipFile zipFile = null;

        try
        {
            zipFile = new ZipFile(file);
        }
        catch (ZipException zipCurrupted)
        {
            isArchive = false;
        }
        catch (IOException anyIOError)
        {
            isArchive = false;
        }
        finally
        {
            if (zipFile != null)
            {
                try
                {
                    zipFile.close();
                }
                catch (IOException ignored)
                {}
            }
        }

        return isArchive;
    }

    public static File[] listJarFiles(File file, boolean addDirectoryName) throws IOException
    {
        ArrayList files = new ArrayList();
        Enumeration entries;

        JarFile jarFile = new JarFile(file);
        entries = jarFile.entries();
        while (entries.hasMoreElements())
        {
            JarEntry entry = (JarEntry) entries.nextElement();
            if (entry.isDirectory())continue;
            // keep only jar or zip files
            if (! simpleFileFilter.accept(file.getParentFile(), entry.getName())) continue;
            if (addDirectoryName)
                files.add(new File(file.getParentFile(), entry.getName()));
            else
                files.add(new File(entry.getName()));
        }
        return (File[])files.toArray( new File[files.size()] );
    }
}

