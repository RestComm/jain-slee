/*
 * The Open SLEE project.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.component.deployment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.slee.SLEEException;
import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.ComponentContainer;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;

/**
 * The DeploymentManager class is where all deployment starts. Using it comes
 * down to calling the deployUnit method by handing it an url pointing to the
 * location of a deployable unit jar. The jar would then be copied to a the
 * location pointed by the value of the system property:
 * 
 * org.mobicents.slee.container.management.deployment.DEPLOYMENT_DIRECTORY
 * 
 * and would be installed in the SLEE.
 * 
 * @author Emil Ivov
 */

public class DeploymentManager {
    private static Logger logger;

    private static byte buffer[];

    static {
        logger = Logger.getLogger(DeploymentManager.class);
        buffer = new byte[8192];
    }

    /**
     * Creates a new instance of the DeploymentManager.
     */
    public DeploymentManager() {
    }

    /**
     * Retrieves the deployable unit pointed to by the unitUrlStr.
     * 
     * @param sourceUrl
     *            the location of the deployable unit.
     * @param deploymentRootDir
     *            the location where the unit should be unjarred.
     * @param the
     *            container where new components should be installed.
     * @param classpathDirectory
     *            target directory for component classes
     * @throws NullPointerException
     *             if the specified url string is null
     * @throws DeploymentException
     *             if deployment fails for any reason
     * @return DeployableUnitID the newly created id corresponding to the
     *         deployable unit.
     */
    public DeployableUnitID deployUnit(URL sourceUrl, File deploymentRootDir,
            File classpathDirectory, ComponentContainer componentContainer)
            throws NullPointerException, DeploymentException , AlreadyDeployedException {

        DeployableUnitIDImpl deployableUnitID;
        if (sourceUrl == null || deploymentRootDir == null || 
                classpathDirectory == null || componentContainer == null) {
            throw new NullPointerException("null arg!");
        }

        //make sure we unpack stuff in directories with unique names so that we
        //don't overwrite when we extract the jars (sbb.jar profile.jar ...)
        //from the deployable unit.
        
        File tempDUJarsDeploymentDir = createTempDUJarsDeploymentDir(deploymentRootDir, sourceUrl);

        //Get the DU jar file and put it in the local DU directory.
        File unitJarFile;//keep a reference so that we may delete it lately
        JarFile unitJar;
        try {
            unitJarFile = retrieveFile(sourceUrl, tempDUJarsDeploymentDir);
        } catch (IOException ioe) {
            /** @todo rollback the transaction */
            throw new DeploymentException("Error retrieving file from URL=["
                    + sourceUrl + "] to local storage", ioe);
        }
        
        if(logger.isDebugEnabled()) {
        	logger.debug("Deploying from " + unitJarFile.getAbsolutePath());
        }

        //extract and deploy all jars found in the deployable unit.
        try {
 
            deployableUnitID = (DeployableUnitIDImpl) DeployableUnitDeployer
                    .deploy(sourceUrl, unitJarFile, tempDUJarsDeploymentDir, componentContainer);

         if(logger.isDebugEnabled()) {
            logger.debug("Installation of deployable unit successful");
         }

            return deployableUnitID;
        } catch (AlreadyDeployedException ex) {
            // clean up the mess here.
            unitJarFile.delete();
            throw ex;
        } catch (Exception ex) {
            // clean up the mess here.
            unitJarFile.delete();
            throw new DeploymentException("Could not deploy: " + ex.getMessage(), ex);
        } 
    }

    /**
     * Deploy all jars units from a given directory.
     * 
     * @param srcPath
     * @param classpathDirectoryStr
     * @param componentContainer
     * @throws NullPointerException
     * @throws DeploymentException
     */
    public void deployAllUnitsAtLocation(String srcPath,
            String classpathDirectoryStr, ComponentContainer componentContainer)
            throws NullPointerException, DeploymentException {

        logger.debug("srcPath " + srcPath + " classPathDirectoryStr "
                + classpathDirectoryStr);

        //Directory where the jar files for deployment are to be found.
        File srcDir = new File(srcPath);

        if (srcDir.exists()) {
            if (!srcDir.isDirectory())
                throw new DeploymentException("Specified Destination "
                        + srcPath + " is not a directory");
        } else {
            throw new DeploymentException("Specified location " + srcPath
                    + " not found");
        }

        //Init the directory where component classes will be deployed
        File classpathDirectory = new File(classpathDirectoryStr);

        //get a list of all files in the specified source directory
        File[] deployableUnits = srcDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.endsWith("jar"))
                    return true;
                else
                    return false;
            }
        });

        //deploy all files found in the specified directory
        for (int i = 0; i < deployableUnits.length; i++) {
            File unitJarFile = (File) deployableUnits[i];
            try {
                //extract and deploy all jars found in the deployable unit.
                /** @todo keep references to deployed units */
                DeployableUnitDeployer.deploy(deployableUnits[i].toURL(),
                        unitJarFile, srcDir, componentContainer);
            } catch (MalformedURLException e1) {
                logger.fatal("unexpected exception ");
                e1.printStackTrace();
            }
        }
    }

    /**
     * Deploys all jars in <code>sourceUrlStr</code> to
     * <code>destinationPath</code> and installs them in the specified
     * container.
     * 
     * @param sourceUrlStr
     *            an url (i.e. a file://... thing) that points to a location
     *            containing urls.
     * @param destinationPath
     *            a path (i.e. a /usr/lib thing and not and url) that specifies
     *            where the units are to be deployed
     * @param container
     *            ComponentContainer the container that wou
     * @param classpathDirectory
     *            a target directory for component classes.
     * @throws NullPointerException
     * @throws DeploymentException
     */
    public void deployAllUnitsAtLocation(String sourceUrlStr,
            String destinationPath, String classpathDirectoryStr,
            ComponentContainer componentContainer) throws NullPointerException,
            DeploymentException {
        URL sourceUrl;
        try {
            sourceUrl = new URL(sourceUrlStr);
        } catch (MalformedURLException exc) {
            throw new DeploymentException("Invalid source url.", exc);
        }

        logger.info("Deploying all units in location" + sourceUrlStr
                + " to location " + destinationPath);

        File srcDir = new File(sourceUrl.getFile());
        if (!srcDir.exists() || !srcDir.isDirectory())
            throw new DeploymentException("Source Directory " + sourceUrlStr
                    + " does not exist or is not a directory");

        //init the directory where the deployable-unit will be unzipped
        File destinationDir = new File(destinationPath);

        if (destinationDir.exists())
            if (!destinationDir.isDirectory())
                throw new DeploymentException("Specified Destination "
                        + destinationPath + " is not a directory");
            else {
                //create the destination directory
                if (!destinationDir.mkdirs())
                    throw new DeploymentException(
                            "Failed to create destination directory: "
                                    + destinationPath);
            }

        //Init the directory where component classes will be deployed
        File classpathDirectory = new File(classpathDirectoryStr);

        if (destinationDir.exists())
            if (!destinationDir.isDirectory())
                throw new DeploymentException("Specified Destination "
                        + destinationPath + " is not a directory");
            else {
                //create the destination directory
                if (!destinationDir.mkdirs())
                    throw new DeploymentException(
                            "Failed to create destination directory: "
                                    + destinationPath);
            }

        //get a list of all files in the specified source directory
        File[] deployableUnits = srcDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name.matches("*.jar"))
                    return true;
                else
                    return false;
            }
        });

        //deploy all files found in the specified directory
        for (int i = 0; i < deployableUnits.length; i++) {
            try {
                deployUnit(deployableUnits[i].toURL(), destinationDir,
                        classpathDirectory, componentContainer);
            } catch (MalformedURLException ex) {
                //shouldn't happen as we are constructing urls from existing
                // files
                throw new DeploymentException(deployableUnits[i].toString()
                        + " is not a valid url.", ex);
            }
        }

    }

    /**
     * Fill IN!!!! This is a method that should start monitoring a specific
     * location and deploy all units that appear in it
     * 
     * @param sourceURL
     *            String
     * @param deploymentPath
     *            String
     * @param container
     *            ComponentContainer
     * @throws NullPointerException
     * @throws DeploymentException
     */
    public void startDeployingLocation(String sourceURL, String deploymentPath,
            ComponentContainer container) throws NullPointerException,
            DeploymentException {
        /** @todo implement */
    }

    //============================= STATIC UTILITIES
    // ===============================

    /**
     * Retrieves the file that the specified <code>url</code> points to,
     * records it to <code>localDstDir</code> and returns a JarFile instance
     * corresponding to that file.
     * 
     * @param url
     *            the location where the file is found.
     * @param localDstDir
     *            the local directory where this method is supposed to record
     *            the du file.
     * @throws NullPointerException
     *             if a null argument is passed to the method.
     * @throws IOException
     *             if retrieving the file and recording locally fails with an
     *             IOException
     * @return a JarFile instance corresponding to the retrieved file.
     */
    static JarFile retrieveJar(URL url, File localDstDir)
            throws NullPointerException, IOException {
        return new JarFile(retrieveFile(url, localDstDir));
    }

    /**
     * Retrieves the file that the specified <code>url</code> points to,
     * records it to <code>localDstDir</code> and returns a JarFile instance
     * corresponding to that file.
     * 
     * @param url
     *            the location where the file is found.
     * @param localDstDir
     *            the local directory where this method is supposed to record
     *            the du file.
     * @throws NullPointerException
     *             if a null argument is passed to the method.
     * @throws IOException
     *             if retrieving the file and recording locally fails with an
     *             IOException
     * @return a JarFile instance corresponding to the retrieved file.
     */
    static File retrieveFile(URL url, File localDstDir)
            throws NullPointerException, IOException {
        if (url == null)
            throw new NullPointerException("NULL url");
        if (localDstDir == null)
            throw new NullPointerException("NULL file");

        File destFile = new File(localDstDir, (new File(url.getFile()))
                .getName());
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destFile);

        //copy the file locally
        pipeStream(is, os);

        return destFile;
    }

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
    static void pipeStream(InputStream is, OutputStream os) throws IOException {

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
     * @throws IOException
     *             if reading the archive or storing the extracted file fails
     * @return a <code>java.io.File</code> reference to the extracted file.
     */
    static File extractFile(String fileName, JarFile containingJar, File dstDir)
            throws IOException {
   
        ZipEntry zipFileEntry = containingJar.getEntry(fileName);
        logger.debug("Extracting file " + fileName + " from " + containingJar.getName());
        if (zipFileEntry == null) {
            logger.debug("Could not extract jar file entry " + fileName + " from " + containingJar.getName());
            return null;
        }
        
        File extractedFile = new File(dstDir, new File(zipFileEntry.getName()).getName());
        
        pipeStream(containingJar.getInputStream(zipFileEntry),
                new FileOutputStream(extractedFile));
        logger.debug("Extracted file " + extractedFile.getName() );
        return extractedFile;
    }
    
    /**
     * This method will extract all the files in the jar file
     * @param jarFile the jar file
     * @param dstDir the destination where files in the jar file be extracted
     * @throws IOException failed to extract files
     */
    public static void extractJar(JarFile jarFile, File dstDir) throws IOException
	{
	  
	    //Extract jar contents to a classpath location
        JarInputStream jarIs = new JarInputStream(new BufferedInputStream(new FileInputStream( jarFile.getName())));
	    
	    for ( JarEntry entry = jarIs.getNextJarEntry();  jarIs.available()>0 && entry != null; entry = jarIs.getNextJarEntry())
	    {
	       
	        logger.debug("zipEntry = " + entry.getName());
	       
	        //if(entry.getName().indexOf("META-INF") != -1){
	        //	logger.info("[###] UnPacking META-INF");
	            //continue;
	        //}

	        if( entry.isDirectory() )
	        {
	            //Create jar directories.
	            File dir = new File(dstDir, entry.getName());
	            if (!dir.exists() ) 
	            {
	                if ( !dir.mkdirs()) {
	                    logger.debug("Failed to create directory " + dir.getAbsolutePath());
	                    throw new IOException("Failed to create directory " +
	                                              dir.getAbsolutePath());
	                }
	            }
	            else
	                logger.debug("Created directory" + dir.getAbsolutePath());
	        }
	        else // unzip files
	        {
	        	File dir = new File(dstDir, entry.getName()).getParentFile();

	            if (!dir.exists() ) 
	            {
	                if ( !dir.mkdirs()) {
	                    logger.debug("Failed to create directory " + dir.getAbsolutePath());
	                    throw new IOException("Failed to create directory " +
	                                              dir.getAbsolutePath());
	                } else
	                	logger.debug("Created directory" + dir.getAbsolutePath());
	            }

			   
	            DeploymentManager.pipeStream(jarFile.getInputStream(entry),
	                                         new FileOutputStream(new File(
	                dstDir, entry.getName()))
	                                         );
	        }
	    }
	    jarIs.close();
	    jarFile.close();
	}
    

    /**
     * Remove only stuff associated with a deployable unit id.
     * 
     * @param deployableUnitID --
     *            the ID of the deployable unit to undeplow.
     */

    public void undeployUnit(DeployableUnitIDImpl deployableUnitID) {

        // Delete the unjarred mess
        for(Iterator it = deployableUnitID.getDeployedFiles(); it.hasNext();) {
            File f = new File((String) it.next());
            if (f.isFile()) {
                f.delete();
                if(logger.isDebugEnabled()) {
                	logger.debug("Deleted file " + f.getAbsolutePath());
                }
            }
        }
        // Delete the jar file itself.
        URI deploymentURI = deployableUnitID.getSourceURI();
        if (deploymentURI != null) {
            File srcFile = new File(deploymentURI);
            srcFile.delete();
            if(logger.isDebugEnabled()) {
            	logger.debug("Deleted DU jar file " + srcFile.getAbsolutePath());
            }
        }
        
        // TODO: Delete the extraxted files from the temp dir
        
        deployableUnitID.getDUDeployer().undeploy();
        
    }

    /**
     * 
     * Sets the directory that will be used for unpacking the child jars for a given DU. 
     * @TODO: make sure to remove the temp directory on undeploy
     * 
     * @param jarName The name of the jarFile which will be used as component in the temp deployment dir name
     * @throws IOException if the temp dir cannot be created
     */
    private File createTempDUJarsDeploymentDir(File rootDir, URL sourceUrl) {
        String jarName = new File(sourceUrl.getFile()).getName();
        try {
            // first create a dummy file to gurantee uniqueness. I would have been nice if the File class had a createTempDir() method
            // IVELIN -- do not use jarName here because windows cannot see the path (exceeds system limit)
            File tempDeploymentFile = File.createTempFile("tmpDUJars", "", rootDir);
            tempDeploymentFile.deleteOnExit();
            
            File tempDUJarsDeploymentDir = new File(tempDeploymentFile.getAbsolutePath() + "-contents");
            if (!tempDUJarsDeploymentDir.exists()) tempDUJarsDeploymentDir.mkdirs();
            tempDUJarsDeploymentDir.deleteOnExit();
            return tempDUJarsDeploymentDir;
            
        } catch (IOException e) {
            logger.error("Temp Deployment Directory could not be created for SLEE DU: " + jarName);
            throw new SLEEException("Failed to create temp deployment dir", e);
        }
    }

    

}