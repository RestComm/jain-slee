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
    this.url = url;
    
    this.fullPath = url.getFile();
    
    this.fileName = getFileNameInternal(fullPath);
    
    this.isDirectory = isDirectoryInternal(url);
  }
  
  public DeployableUnitWrapper(DeploymentUnit du)
  {
    try
    {
      this.du = du;
      
      this.url = new URL(du.getName());

      this.fullPath = url.getFile();
      
      this.fileName = getFileNameInternal(fullPath);
      
      this.isDirectory = isDirectoryInternal(url);
    }
    catch ( Exception e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  // Private ------------------------------------------------------- 
  
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
