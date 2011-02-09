package org.mobicents.slee.container.deployment.jboss;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.system.server.ServerConfigLocator;

public class DeploymentManagerMBeanImpl extends StandardMBean implements DeploymentManagerMBean {

  private File tempDeployDir;
  private File deployDir;
  private File farmDeployDir;

  public DeploymentManagerMBeanImpl() throws NotCompliantMBeanException {
    super(DeploymentManagerMBean.class);
    this.tempDeployDir = createTempDUJarsDeploymentRoot();
    this.deployDir = new File(ServerConfigLocator.locate().getServerHomeDir() + File.separator + "deploy");
    this.farmDeployDir = new File(ServerConfigLocator.locate().getServerHomeDir() + File.separator + "farm");
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBean#showStatus()
   */
  public String showStatus() {
    return DeploymentManager.INSTANCE.showStatus();
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBean#persistentInstall(java.net.URL)
   */
  public void persistentInstall(URL deployableUnitURL) throws DeploymentException {
    try {
      doPersistentInstall(deployableUnitURL, deployDir);
    }
    catch (Exception e) {
      throw new DeploymentException(e);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBean#persistentUninstall(java.net.URL)
   */
  public void persistentUninstall(URL deployableUnitURL) throws DeploymentException {
    try {
      // All we really care is for the filename
      String fullPath = deployableUnitURL.getFile();
      String filename = fullPath.substring(fullPath.lastIndexOf('/') + 1);

      // Here's what we want to delete
      String filePath = deployDir + File.separator + filename;

      // Delete it
      if(!new File(filePath).delete()) {
        throw new DeploymentException("Failed to delete " + filePath);
      }
    }
    catch (Exception e) {
      throw new DeploymentException(e);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBean#clusterInstall(java.net.URL)
   */
  public void clusterInstall(URL deployableUnitURL) throws DeploymentException {
    try {
      doPersistentInstall(deployableUnitURL, farmDeployDir);
    }
    catch (Exception e) {
      throw new DeploymentException(e);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.container.deployment.jboss.DeploymentManagerMBean#clusterUninstall(java.net.URL)
   */
  public void clusterUninstall(URL deployableUnitURL) throws DeploymentException {
    try {
      // All we really care is for the filename
      String fullPath = deployableUnitURL.getFile();
      String filename = fullPath.substring(fullPath.lastIndexOf('/') + 1);

      // Here's what we want to delete
      String filePath = farmDeployDir + File.separator + filename;

      // Delete it
      if(!new File(filePath).delete()) {
        throw new DeploymentException("Failed to delete " + filePath);
      }
    }
    catch (Exception e) {
      throw new DeploymentException(e);
    }
  }

  /**
   * 
   * @param deployableUnitURL
   * @param destinationDirectory
   * @throws Exception
   */
  private void doPersistentInstall(URL deployableUnitURL, File destinationDirectory) throws Exception {
    if(!destinationDirectory.exists()) {
      throw new IllegalArgumentException("Deploy folder " + destinationDirectory + " doesn't exist. Aborting.");
    }
    File tempFile = new File(deployableUnitURL.getFile());

    // If it's remote, we need to download it first to a local temporary folder
    if(deployableUnitURL.getProtocol().startsWith("http")) {
      tempFile = this.downloadRemoteDU(deployableUnitURL, tempDeployDir);
      deployableUnitURL = tempFile.toURI().toURL();
    }

    // Copy the file to deploy folder
    copyLocalFile(tempFile, new File(destinationDirectory + File.separator + tempFile.getName()));    
  }
  
  /**
   * Downloads a remote DU to a local folder
   * 
   * @param duURL URL where the DU can be fetched from 
   * @param deploymentRoot the location where it will be saved to
   * @return a File related to the downloaded DU
   * @throws Exception
   */
  private File downloadRemoteDU(URL duURL, File deploymentRoot) throws Exception {
    InputStream in = null;
    OutputStream out = null;

    try {
      // Get the filename out of the URL
      String filename = new File(duURL.getPath()).getName();

      // Prepare for creating the file at deploy folder
      File tempFile = new File(deploymentRoot, filename);

      out = new BufferedOutputStream(new FileOutputStream(tempFile));
      URLConnection conn = duURL.openConnection();
      in = conn.getInputStream();

      // Get the data
      byte[] buffer = new byte[1024];
      int numRead;
      while ((numRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, numRead);
      }

      // Done! Successful.
      return tempFile;
    }
    finally {
      // Do the clean up.
      try {
        if (in != null) {
          in.close();
          in = null;
        }
        if (out != null) {
          out.close();
          out = null;
        }
      }
      catch (IOException ioe) {
        // Shouldn't happen, let's ignore.
      }
    }
  }

  /**
   * Copies a file from a location to another, using NIO FileChannel.
   * 
   * @param sourceFile the source file
   * @param destFile the destination file
   * @throws IOException if it's not possible to copy the file
   */
  private void copyLocalFile(File sourceFile, File destFile) throws IOException {
    if(!destFile.exists()) {
      destFile.createNewFile();
    }

    FileChannel source = null;
    FileChannel destination = null;
    try {
      source = new FileInputStream(sourceFile).getChannel();
      destination = new FileOutputStream(destFile).getChannel();
      destination.transferFrom(source, 0, source.size());
    }
    finally {
      if(source != null) {
        source.close();
      }
      if(destination != null) {
        destination.close();
      }
    }
  }

  /**
   * Initializes the temporary deploy folder, if necessary.
   * 
   * @return a File object pointing to the temporary deploy folder
   */
  private File createTempDUJarsDeploymentRoot() {
    File tempDeploymentRootDir = ServerConfigLocator.locate().getServerTempDeployDir();
    if (!tempDeploymentRootDir.exists()) {
      if (!tempDeploymentRootDir.mkdirs()) {
        throw new RuntimeException("Failed to create temp deployment dir: " + tempDeploymentRootDir);
      }
    }
    return tempDeploymentRootDir;
  }
}
