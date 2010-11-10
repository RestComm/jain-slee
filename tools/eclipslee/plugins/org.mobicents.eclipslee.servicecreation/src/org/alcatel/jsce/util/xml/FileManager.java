/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.util.xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.alcatel.jsce.classloaders.JarClassLoader;
import org.alcatel.jsce.util.IO;
import org.alcatel.jsce.util.xml.ErrorStatus;
import org.alcatel.jsce.util.xml.FileManager;
import org.alcatel.jsce.util.log.SCELogger;
import org.alcatel.jsce.util.xml.XMLErrorHanlder;
import org.apache.xerces.parsers.DOMParser;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;


/**
 *  Description:
 * <p>
 * This object enables client to open files on file system. For instance, it allows to 
 * handle a @link org.w3c.dom.Document of na XML file.<br>
 * <b>Singleton pattern.</b>
 * <p>
 * By <b>expert pattern</b>, this class is expert for the I/O file system. It handle all the demand on file system.
 * @author Skhiri Sabri
 * @stereotype expert
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class FileManager {
	/** The instance of the FileManager*/
	private static FileManager instance = null;

	/** The list of errors from the last parsing*/
	private List parseError = null;

	protected static final String VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

	/**
	 * Private constructor.
	 */
	private FileManager() {
	}

	/**
	 * @return the siggleton instance of the file manager.
	 */
	public static FileManager getInstance() {
		if (instance == null) {
			instance = new FileManager();
		}
		return instance;
	}

	/**
	 * Open an XML file.
	 * @return the @link Document of the XML file. The validation is done in accordance 
	 * with the XML Schema specified as arguement.
	 * WARNING: the import plugin="org.apache.xerces"/ have to be write to the
	 * manifest file.
	 */
	public Document openXMLFile(URL fileLocation, URL schemaSourceLocation) {
		parseError = new ArrayList();
		if (schemaSourceLocation == null) {
			return openModelFile(fileLocation);
		} else {
			Document doc = null;
			try {
				DOMParser theParser = new DOMParser();
				/*theParser
				 .setProperty(
				 "http://apache.org/xml/properties/schema/external-schemaLocation",
				 schemaSourceLocation);
				 theParser.setFeature("http://xml.org/sax/features/validation",
				 true);

				 theParser.setFeature(VALIDATION_FEATURE_ID, true);
				 theParser.setErrorHandler(new XMLErrorHanlder(parseError));*/
				theParser.parse(replaceBlank(fileLocation.getFile()));
				doc = theParser.getDocument();
				return doc;
			} catch (SAXNotRecognizedException e) {
				System.err.print(e.getMessage());
				SCELogger.logError("Unrecognized Feature", e);
				e.printStackTrace();
				return null;
			} catch (SAXNotSupportedException e) {
				System.err.print(e.getMessage());
				e.printStackTrace();
				parseError.add(new ErrorStatus(ErrorStatus.ERROR, e, 0));
				return null;
			} catch (SAXException e) {
				System.err.print(e.getMessage());
				// e.printStackTrace();
				SCELogger.logError("SAx exception ! Maybe your file is not valid. ", e);
				parseError.add(new ErrorStatus(ErrorStatus.ERROR, e, 0));
				return null;
			} catch (IOException e) {
				System.err.print(e.getMessage());
				e.printStackTrace();
				parseError.add(new ErrorStatus(ErrorStatus.ERROR, e, 0));
				return null;
			}
		}
	}

	/**
	 * Open an XML file.
	 * @return the @link Document of the XML file. The validation is done in accordance 
	 * with the XML Schema specified in the header of the XML file..
	 * WARNING: the import plugin="org.apache.xerces"/ have to be write to the
	 * manifest file.
	 */
	private Document openModelFile(URL fileLocation) {
		parseError = new ArrayList();
		Document doc = null;
		try {
			DOMParser theParser = new DOMParser();
			/*theParser.setFeature("http://xml.org/sax/features/validation", true);

			theParser.setFeature(VALIDATION_FEATURE_ID, true);*/
			theParser.setErrorHandler(new XMLErrorHanlder(parseError));
			theParser.parse(replaceBlank(fileLocation.getFile()));
			doc = theParser.getDocument();
			return doc;
		} catch (SAXNotRecognizedException e) {
			SCELogger.logError("Unrecognized Feature", e);
			e.printStackTrace();
			parseError.add(new ErrorStatus(ErrorStatus.ERROR, e, 0));
			return null;
		} catch (SAXNotSupportedException e) {
			e.printStackTrace();
			SCELogger.logError("SAX exception while parsing XML file", e);
			parseError.add(new ErrorStatus(ErrorStatus.ERROR, e, 0));
			return null;
		} catch (SAXException e) {
			SCELogger.logError("SAx exception ! Maybe your file is not valid. ", e);
			parseError.add(new ErrorStatus(ErrorStatus.ERROR, e, 0));
			return null;
		} catch (IOException e) {
			SCELogger.logError("IO exception while parsing XML file", e);
			e.printStackTrace();
			parseError.add(new ErrorStatus(ErrorStatus.ERROR, e, 0));
			return null;
		}

	}

	/**
	 * @param string
	 * @return
	 */
	private String replaceBlank(String string) {
		return string.replaceAll(" ", "%20");

	}

	/**
	 * @return Returns the parseError.
	 */
	public List getParseError() {
		return parseError;
	}

	/**
	 * @param parseError The parseError to set.
	 */
	public void setParseError(List parseError) {
		this.parseError = parseError;
	}

	/**
	 * @param url the parent directory in which we will extract all the xml file.
	 * @return a list of all XML files localized in the specified directory. This list is composed of 
	 * string representing relative pathname.
	 */
	public List getAllXMLFile(URL dir) {
		List xmlFiles = new ArrayList();
		File theDir = new File(dir.getFile());
		if (theDir.isDirectory()) {
			String[] dirList = theDir.list();
			xmlFiles = getXMLMetaDataFile(dirList, dir);
			return xmlFiles;
		} else
			throw new IllegalStateException("The catalog directory is not set properly !");
	}
	
	/**
	 * @param url the parent directory in which we will extract all the xml file.
	 * @param extension the file extension
	 * @return a list of all XML files localized in the specified directory. This list is composed of 
	 * string representing relative pathname.
	 */
	public List getAllFileEndsBy(URL dir, String extension) {
		List xmlFiles = new ArrayList();
		File theDir = new File(dir.getFile());
		if (theDir.isDirectory()) {
			String[] dirList = theDir.list();
			xmlFiles = getXMLMetaDataFile(dirList, dir, extension);
			return xmlFiles;
		} else
			throw new IllegalStateException("The catalog directory is not set properly !");
	}
	
	/**
	 * @param url the parent directory in which we will extract all the xml file.
	 * @param fileName the xml file name
	 * @return the xml file localized in the specified directory or null
	 */
	public File getXMLFileFromDir(URL dir, String fileName) {
		File theDir = new File(dir.getFile());
		List files = FileManager.getInstance().getAllXMLFile(dir);
		if (theDir.isDirectory()) {
			String[] dirList = theDir.list();
			files = getXMLMetaDataFile(dirList, dir);
			for (Iterator iter = files.iterator(); iter.hasNext();) {
				String file_i = (String) iter.next();
				if(file_i.endsWith(fileName)){
					File xmlFile = new File(dir.getPath()+"/"+file_i);
					return xmlFile;
				}
			}
			return null;
		} else
			throw new IllegalStateException("The catalog directory is not set properly !");
	}
	
	/**
	 * @param url the parent directory in which we will extract all the xml file.
	 * @param fileName the xml file name
	 * @param ext the file extension
	 * @return the xml file localized in the specified directory or null
	 */
	public File getXMLFileFromDir(URL dir, String fileName, String ext) {
		File theDir = new File(dir.getFile());
		List files = FileManager.getInstance().getAllXMLFile(dir);
		if (theDir.isDirectory()) {
			String[] dirList = theDir.list();
			files = getXMLMetaDataFile(dirList, dir, ext);
			for (Iterator iter = files.iterator(); iter.hasNext();) {
				String file_i = (String) iter.next();
				if(file_i.endsWith(fileName)){
					File xmlFile = new File(dir.getPath()+"/"+file_i);
					return xmlFile;
				}
			}
			return null;
		} else
			throw new IllegalStateException("The catalog directory is not set properly !");
	}

	/**
	 * @param dirList
	 * @return the list of XML files contained in the parent directory. The
	 *         list is the list of relatives names of file according to
	 *         the specified directory. 
	 *         <br> This method parse recursively all sub directory of the parent.
	 * @param parent.
	 */
	private List getXMLMetaDataFile(String[] files, URL parent) {
		List listModel = new ArrayList();
		String message = ("XML model found :");
		for (int i = 0; i < files.length; i++) {
			try {
				URL fileURL = new URL(parent, files[i]);
				File file = new File(fileURL.getPath());
				if (file.isDirectory()) {
					String[] dirList = file.list();
					file = new File(fileURL.getPath() + "/");
					List listFromDir = getXMLMetaDataFile(dirList, file.toURL());
					if (listFromDir != null) {
						for (Iterator iter = listFromDir.iterator(); iter.hasNext();) {
							String fileName = (String) iter.next();
							listModel.add(files[i] + "/" + fileName);
						}
					}
				} else {
					if ((this.extension(files[i])).equals("xml") || this.extension(files[i]).equals("wsdd") || this.extension(files[i]).equals("xmldesc")) {
						String fileToStore = files[i];
						if (!listModel.contains(fileToStore)) {
							listModel.add(fileToStore);
							message += ", " + fileToStore;
						}
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		//SCELogger.logInfo(message);
		return listModel;
	}

	/**
	 * @param dirList
	 * @return the list file ending by ext  jar files contained in the parent directory. The
	 *         list is the list of relatives names of file according to
	 *         the specified directory. 
	 *         <br> This method parse recursively all sub directory of the parent.
	 * @param parent.
	 */
	private List getXMLMetaDataFile(String[] files, URL parent, String ext) {
		List listModel = new ArrayList();
		String message = ("XML model found :");
		for (int i = 0; i < files.length; i++) {
			try {
				URL fileURL = new URL(parent, files[i]);
				File file = new File(fileURL.getPath());
				if (file.isDirectory()) {
					String[] dirList = file.list();
					file = new File(fileURL.getPath() + "/");
					List listFromDir = getXMLMetaDataFile(dirList, file.toURL(), ext);
					if (listFromDir != null) {
						for (Iterator iter = listFromDir.iterator(); iter.hasNext();) {
							String fileName = (String) iter.next();
							listModel.add(files[i] + "/" + fileName);
						}
					}
				} else {
					if ((this.extension(files[i])).equals(ext)) {
						String fileToStore = files[i];
						if (!listModel.contains(fileToStore)) {
							listModel.add(fileToStore);
							message += ", " + fileToStore;
						}
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		//SCELogger.logInfo(message);
		return listModel;
	}
	
	private String extension(String fullPath) {
		int dot = fullPath.lastIndexOf(".");
		return fullPath.substring(dot + 1);
	}

	/**
	 * @param dir the directory in which wz want looking for a file
	 * @param fileName the relative name of the file
	 * @return the corresponding file
	 */
	public File searchIntoDir(File dir, String fileName) {
		if (dir.isDirectory()) {
			File file = new File(dir.toString() + "\\" + fileName);
			if (file.exists()) {
				return file;
			} else {
				return null;
			}

		} else {
			IllegalStateException ex = new IllegalStateException("The file " + dir.getPath() + " is not a directory");
			SCELogger.logError("Error while searching file " + fileName, ex);
		}
		return null;
	}

	/**
	 * Create recusively all parent file if needed.
	 * @param file the file whose parents need to be created. 
	 * @throws IOException
	 */
	public void createParentFile(File file) throws IOException {
		File parent = file.getParentFile();
		if (parent.exists()) {
			return;
		} else {
			createParentFile(parent);
			parent.mkdir();
		}
	}

	/**
	 * Imports the profile-spec-jar.xml into the current project
	 * 
	 * @param project
	 *            the Eclipse project
	 * @param pakage
	 *            the package name of the interface to copy.
	 * @param interfaceName
	 *            the interface name
	 * @param profileSpecJarXML
	 *            the representation of the XML file
	 */
	public void importXMLFileIntoProject(File rootDir, String pakage, String interfaceName,
			ProfileSpecJarXML profileSpecJarXML) {
		File targetLocationDir = new File(rootDir, pakage.replaceAll("\\.", "/"));
		// 1. Copy XML file
		try {
			File targetProfileXML = new File(targetLocationDir, "profile-spec-jar.xml");
			createParentFile(targetProfileXML);
			if (targetProfileXML.exists()) {
				boolean result = true;
					/*MessageDialog.openConfirm(wizard.getShell(), "Alcatel SCE Warning", "  The profile-spec-jar.xml already exists !"
						+ "\n Do you want to replace it ?");*/
				if (result) {
					// Copy the file
					targetProfileXML.delete();
					targetProfileXML.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(targetProfileXML);
					profileSpecJarXML.save(fileOutputStream);
				}
			} else {
				targetProfileXML.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(targetProfileXML);
				profileSpecJarXML.save(fileOutputStream);
			}
			profileSpecJarXML.getInputStreamFromXML();
		} catch (IOException e) {
			SCELogger.logError("Exception while copying XML profile spec in project", e);
		}

	}

}
