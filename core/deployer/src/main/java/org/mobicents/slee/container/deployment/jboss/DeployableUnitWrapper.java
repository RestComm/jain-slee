package org.mobicents.slee.container.deployment.jboss;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.deployers.structure.spi.DeploymentUnit;

public class DeployableUnitWrapper
{

  private URL url;
  
  private boolean isDirectory;
  
  private String fileName;
  
  private String fullPath;
  
  private DeploymentUnit du;
  
  public DeployableUnitWrapper(URL url)
  {
    try {
      gatherInfoFromURL(url);
    }
    catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public DeployableUnitWrapper(DeploymentUnit du)
  {
    try {
      this.du = du;
      
      gatherInfoFromURL(new URL(du.getName()));
    }
    catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  // Private ------------------------------------------------------- 
  
  private void gatherInfoFromURL(URL url) throws MalformedURLException
  {
    // Weird VFS behavior... returns jar:file:...jar!/
    if(url.getProtocol().equals("jar")) {
      this.url = new URL(url.getFile().replaceFirst("!/", "/"));
    }
    else {
      this.url = url;
    }

    this.fullPath = this.url.getFile();
    
    this.fileName = getFileNameInternal(fullPath);
    
    this.isDirectory = isDirectoryInternal(url);    
  }
  
  private boolean isDirectoryInternal(URL url)
  {
    return new File(url.getFile()).isDirectory();
  }
  
  private String getFileNameInternal(String fullPath)
  {
    String fileName = fullPath;
    
    if (fileName.endsWith("/"))
    {
      fileName = fileName.substring(0, fileName.length() - 1);
    }
     
    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
    
    return fileName;
  }

  // Getters and Setters -------------------------------------------
  
  public URL getUrl()
  {
    return url;
  }

  public boolean isDirectory()
  {
    return isDirectory;
  }

  public String getFileName()
  {
    return fileName;
  }

  public String getFullPath()
  {
    return fullPath;
  }

  public DeploymentUnit getDeploymentUnit()
  {
    return du;
  }
  
  public URL getEntry(String path)
  {
    URL retURL = null;
    
    if(du != null)
    {
      retURL = du.getResourceLoader().getResource(path);
    }
    else
    {
      try
      {
        retURL = new URL(this.url + "/" + path);
        
        try
        {
          retURL.openStream().close();
        }
        catch ( IOException e )
        {
          retURL = null;
        }
      }
      catch ( MalformedURLException e )
      {
        e.printStackTrace();
      }
    }
    
    return  retURL;
  }
}
