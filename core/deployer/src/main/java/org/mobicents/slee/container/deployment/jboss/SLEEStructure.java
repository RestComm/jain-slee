package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.plugins.structure.AbstractVFSStructureDeployer;
import org.jboss.deployers.vfs.spi.structure.StructureContext;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VirtualFileFilter;
import org.jboss.virtual.VisitorAttributes;
import org.jboss.virtual.plugins.vfs.helpers.SuffixMatchFilter;

public class SLEEStructure extends AbstractVFSStructureDeployer
{

  /** The default filter which allows jars/jar directories */
  public static final VirtualFileFilter DEFAULT_SLEE_FILTER = new SuffixMatchFilter(".jar", VisitorAttributes.DEFAULT);
  
  /** The web-inf/lib filter */
  private VirtualFileFilter sleeFilter = DEFAULT_SLEE_FILTER;

  public SLEEStructure()
  {
    System.out.println("@@@ public SLEEStructure() @@@" );
    
    setRelativeOrder( 350 );
  }
  
  public boolean determineStructure( StructureContext structureContext ) throws DeploymentException
  {
    System.out.println("@@@ public boolean determineStructure( StructureContext structureContext ) throws DeploymentException @@@" );

    VirtualFile file = structureContext.getFile();
    
    try
    {
      if( file.getChild( "META-INF/deployable-unit.xml" ) == null )
      {
        System.out.println("@@@ REJECTING! No META-INF/deployable-unit.xml found! @@@" );
        return false;
      }
    }
    catch ( IOException e )
    {
      e.printStackTrace();
      return false;
    }

    System.out.println("@@@ ACCEPTING! META-INF/deployable-unit.xml found! @@@" );
    return true;
  }


  public VirtualFileFilter getSleeFilter()
  {
    System.out.println("@@@ public VirtualFileFilter getSleeFilter() @@@" );

    return sleeFilter;
  }


  public void setSleeFilter( VirtualFileFilter sleeFilter )
  {
    System.out.println("@@@ public void setSleeFilter( VirtualFileFilter sleeFilter ) @@@" );

    if (sleeFilter == null)
      throw new IllegalArgumentException("Null filter");
    
    this.sleeFilter = sleeFilter;
  }

}
