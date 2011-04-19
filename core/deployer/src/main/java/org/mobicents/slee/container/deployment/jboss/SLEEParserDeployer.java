/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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

package org.mobicents.slee.container.deployment.jboss;

import org.apache.log4j.Logger;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData10;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.vfs.spi.deployer.AbstractVFSParsingDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.virtual.VirtualFile;

/**
 * 
 * JAIN SLEE Parser Deployer for JBoss AS 5.x
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SLEEParserDeployer extends AbstractVFSParsingDeployer<SLEEDeploymentMetaData>
{

  private static Logger logger = Logger.getLogger( SLEEParserDeployer.class );

  public SLEEParserDeployer() {
    super(SLEEDeploymentMetaData.class);
    setSuffix(".jar");
    setStage(DeploymentStages.PRE_DESCRIBE);
    logger.debug("Mobicents SLEE Parser Deployer initialized.");    
  }

  protected SLEEDeploymentMetaData parse(VFSDeploymentUnit vfsDU, VirtualFile virtualFile, SLEEDeploymentMetaData sdmd) throws Exception {    
    return new SLEEDeploymentMetaData(vfsDU);
  }

  protected SLEEDeploymentMetaData parse(DeploymentUnit du, String str1, String str2, SLEEDeploymentMetaData sdmd) throws Exception {
    if(logger.isTraceEnabled()) {
      logger.trace("SLEEParserDeployer 'parse' called:");
      logger.trace("du................." + du);
      logger.trace("str1..............." + str1);
      logger.trace("str2..............." + str2);
      logger.trace("sdmd..............." + sdmd);
    }

    SLEEDeploymentMetaData _sdmd = new SLEEDeploymentMetaData(du);

    if(_sdmd.componentType == SLEEDeploymentMetaData.ComponentType.DU) {
      ClassLoadingMetaData classLoadingMetaData = du.getAttachment(ClassLoadingMetaData.class);

      if(logger.isTraceEnabled()) {
        logger.trace("Got Classloading MetaData: " + classLoadingMetaData);
      }

      classLoadingMetaData = new ClassLoadingMetaData10();
      classLoadingMetaData.setName(du.getSimpleName());
      classLoadingMetaData.setIncludedPackages("");

      if(logger.isTraceEnabled()) {
        logger.trace("Set Classloading MetaData: " + classLoadingMetaData);
      }

      du.addAttachment(ClassLoadingMetaData.class, classLoadingMetaData);

      return _sdmd;
    }

    return null;
  }
}