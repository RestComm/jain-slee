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

        InputStream is = null;

        try {
            if ((is = rootFile.getChild("META-INF/deployable-unit.xml").openStream()) != null) {
                // This is a SLEE DU
                this.componentType = ComponentType.DU;

                this.parseDUContents(rootFile, is, isDeploy);
            } else if (rootFile.getChild("META-INF/event-jar.xml").openStream() != null) {
                this.componentType = ComponentType.EVENT;
            } else if (rootFile.getChild("META-INF/sbb-jar.xml").openStream() != null) {
                this.componentType = ComponentType.SBB;
            } else if (rootFile.getChild("META-INF/profile-spec-jar.xml").openStream() != null) {
                this.componentType = ComponentType.PROFILE;
            } else if (rootFile.getChild("META-INF/resource-adaptor-type-jar.xml").openStream() != null) {
                this.componentType = ComponentType.RATYPE;
            } else if (rootFile.getChild("META-INF/resource-adaptor-jar.xml").openStream() != null) {
                this.componentType = ComponentType.RA;
            } else if (rootFile.getChild("META-INF/library-jar.xml").openStream() != null) {
                this.componentType = ComponentType.LIBRARY;
            }
        } catch (IOException e) {
            log.info("Cannot open stream (META-INF): " + e.getLocalizedMessage());
        }
    }

    private void parseDUContents(VirtualFile rootFile, InputStream is, boolean isDeploy)
    {
        try {
            log.debug("parse DU contents");
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
                        log.debug("duContents add: "+elem.getTextContent());
                        duContents.add(elem.getTextContent());
                    }
                }
            }

            if (isDeploy) {
                InputStream dependsInputStream = null;
                try {
                    dependsInputStream = rootFile.getChild("META-INF/jboss-dependency.xml").openStream();
                    if (dependsInputStream != null) {
                        log.debug("parse jboss-dependency.xml");

                        DocumentBuilder dependsDocBuilder = docBuilderFactory.newDocumentBuilder();
                        Document dependsDoc = dependsDocBuilder.parse(dependsInputStream);

                        NodeList dependsNodeList = dependsDoc.getDocumentElement().getChildNodes();
                        // <item whenRequired="Real" dependentState="Installed">CAPSS7Service</item>
                        boolean checkItems = true;
                        for (int i = 0; i < dependsNodeList.getLength(); i++) {
                            if (dependsNodeList.item(i) instanceof Element) {
                                Element elem = (Element) dependsNodeList.item(i);
                                if (elem.getNodeName().equals("item")) {
                                    log.debug("Item [" + i + "]: " + elem.getTextContent());
                                    if (!elem.getTextContent().isEmpty()) {
                                        checkItems = checkItems && checkDependencyItem(elem.getTextContent());
                                    }
                                }
                            }
                        }
                        this.dependencyItemsPassed = checkItems;
                        //log.debug("dependencyItemsPassed: "+this.dependencyItemsPassed);
                    }
                } catch (IOException e) {
                    log.info("Cannot open stream (META-INF): " + e.getLocalizedMessage());
                }
            }

        }
        catch (Exception e) {
            log.error("Error parsing Deployable Unit to build SLEE Deployer MetaData.", e);
        }
    }

    private boolean checkDependencyItem(String itemName) {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        Set mbeans = mbeanServer.queryNames(null, null);
        ObjectName mbeanName = null;
        for (Object mbean : mbeans) {
            mbeanName = (ObjectName)mbean;
            if (mbeanName.getCanonicalName().contains(itemName)) {
                //log.debug("MBean: " + mbeanName.getCanonicalName());
                try {
                    Object object = mbeanServer.getAttribute(mbeanName, "Started");
                    if (object != null && object instanceof Boolean) {
                        return (Boolean)object;
                    }
                } catch(Exception e) {
                    log.warn("Cant get attribute Started for MBean: "+mbeanName, e);
                }
                return true;
            }
        }

        log.warn("Deployment \""+itemName+"\"  is in error due to the following reason(s): ** NOT FOUND Depends on '"+itemName+"' **");
        return false;
    }
}