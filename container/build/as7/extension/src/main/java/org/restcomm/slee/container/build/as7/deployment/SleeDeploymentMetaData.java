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

package org.restcomm.slee.container.build.as7.deployment;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.logging.Logger;
import org.jboss.vfs.VirtualFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Set;

//import org.apache.log4j.Logger;

/**
 * JAIN SLEE Deployable Unit MetaData for JBoss 7.x Deployment
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeDeploymentMetaData
{
    //private static final Logger logger = Logger.getLogger(SleeDeploymentMetaData.class);
    Logger log = Logger.getLogger(SleeDeploymentMetaData.class);

    protected static enum ComponentType {
        UNKNOWN(-1),
        EVENT(1),
        SBB(2),
        PROFILE(3),
        RATYPE(4),
        RA(5),
        SERVICE(6),
        LIBRARY(7),
        DU(9);

        private ComponentType(int value) {
            this.value = value;
        }

        int value;

        public int getValue() {
            return this.value;
        }

    }

    // Variables -----------------------------------------------------

    protected ComponentType componentType = ComponentType.UNKNOWN;

    protected ArrayList<String> duContents = new ArrayList<String>();

    private boolean dependencyItemsPassed = true;

    public boolean isDependencyItemsPassed() {
        return dependencyItemsPassed;
    }

    // Constructors --------------------------------------------------

    public SleeDeploymentMetaData(DeploymentUnit du, boolean isDeploy)
    {
        final ResourceRoot deploymentRoot = du.getAttachment(Attachments.DEPLOYMENT_ROOT);
        log.info("METADATA deploymentRoot: "+deploymentRoot);
        final VirtualFile rootFile = deploymentRoot.getRoot();

        parseRootFile(rootFile, isDeploy);
    }

    public SleeDeploymentMetaData(VirtualFile rootFile, boolean isDeploy) {
        parseRootFile(rootFile, isDeploy);
    }

    private void parseRootFile(VirtualFile rootFile, boolean isDeploy) {
        log.debug("METADATA rootFile: "+rootFile);        

        try {        	
            if (rootFile.getChild("META-INF/deployable-unit.xml").exists()) {
                // This is a SLEE DU
                this.componentType = ComponentType.DU;
                InputStream is = rootFile.getChild("META-INF/deployable-unit.xml").openStream();
                this.parseDUContents(rootFile, is, isDeploy);
            } else if (rootFile.getChild("META-INF/event-jar.xml").exists()) {
                this.componentType = ComponentType.EVENT;
            } else if (rootFile.getChild("META-INF/sbb-jar.xml").exists()) {
                this.componentType = ComponentType.SBB;
            } else if (rootFile.getChild("META-INF/profile-spec-jar.xml").exists()) {
                this.componentType = ComponentType.PROFILE;
            } else if (rootFile.getChild("META-INF/resource-adaptor-type-jar.xml").exists()) {
                this.componentType = ComponentType.RATYPE;
            } else if (rootFile.getChild("META-INF/resource-adaptor-jar.xml").exists()) {
                this.componentType = ComponentType.RA;
            } else if (rootFile.getChild("META-INF/library-jar.xml").exists()) {
                this.componentType = ComponentType.LIBRARY;
            }
        } catch (IOException e) {
            log.info("Cannot open stream (META-INF): " + e.getLocalizedMessage());
        }
    }

    private void parseDUContents(VirtualFile rootFile, InputStream is, boolean isDeploy)
    {
        try {
            log.trace("parse DU contents");
            // Parse the DU to see which jars we should process
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            // Set the Entity Resolver
            docBuilder.setEntityResolver(SleeDeployerEntityResolver.INSTANCE);

            Document doc = docBuilder.parse (is);

            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for(int i = 0; i < nodeList.getLength(); i++) {
                if(nodeList.item(i) instanceof Element) {
                    Element elem = (Element) nodeList.item(i);
                    if(elem.getNodeName().equals("jar") || elem.getNodeName().equals("service-xml")) {
                        log.trace("duContents add: "+elem.getTextContent());
                        duContents.add(elem.getTextContent());
                    }
                }
            }

            if (isDeploy)
                checkDependency(rootFile);
        }
        catch (Exception ex) {
            log.error("Error parsing Deployable Unit to build SLEE Deployer MetaData.", ex);
        }
    }

    public void checkDependency(VirtualFile rootFile) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            
            if (rootFile.getChild("META-INF/jboss-dependency.xml").exists()) {
                log.trace("parse jboss-dependency.xml");
                InputStream dependsInputStream = rootFile.getChild("META-INF/jboss-dependency.xml").openStream();                            
                DocumentBuilder dependsDocBuilder = docBuilderFactory.newDocumentBuilder();
                Document dependsDoc = dependsDocBuilder.parse(dependsInputStream);

                NodeList dependsNodeList = dependsDoc.getDocumentElement().getChildNodes();
                boolean checkItems = true;
                for (int i = 0; i < dependsNodeList.getLength(); i++) {
                    if (dependsNodeList.item(i) instanceof Element) {
                        Element elem = (Element) dependsNodeList.item(i);
                        if (elem.getNodeName().equals("item")) {
                            log.trace("Item [" + i + "]: " + elem.getTextContent());
                            if (!elem.getTextContent().isEmpty()) {
                                checkItems = checkItems && checkDependencyItem(elem.getTextContent());
                            }
                        }
                    }
                }
                this.dependencyItemsPassed = checkItems;
            }
            /*else {
            	log.warn("Deployment \""+rootFile.getName()+
            			"\"  is in error due to the following reason(s): ** NOT FOUND Dependency **." +
                        "See META-INF/jboss-dependency.xml.");
            }*/
        } catch (IOException e) {
            log.info("Cannot open stream (META-INF): " + e.getLocalizedMessage());
        } catch (Exception ex) {
            log.error("Error parsing Deployable Unit to build SLEE Deployer MetaData.", ex);
        }
    }

    private boolean checkDependencyItem(String itemName) {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        Set mbeans = mbeanServer.queryNames(null, null);
        ObjectName mbeanName = null;
        for (Object mbean : mbeans) {
            mbeanName = (ObjectName)mbean;
            if (mbeanName.getCanonicalName().contains(itemName)) {
                try {
                    Object object = mbeanServer.getAttribute(mbeanName, "Started");
                    if (object != null && object instanceof Boolean) {
                        return (Boolean)object;
                    }
                } catch(Exception e) {
                    log.debug("Cant get attribute Started for MBean: "+mbeanName, e);
                }
                return true;
            }
        }

        return false;
    }
}