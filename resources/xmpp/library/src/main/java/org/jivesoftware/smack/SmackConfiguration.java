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

package org.jivesoftware.smack;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 * Represents the configuration of Smack. The configuration is used for:
 * <ul>
 *      <li> Initializing classes by loading them at start-up.
 *      <li> Getting the current Smack version.
 *      <li> Getting and setting global library behavior, such as the period of time
 *          to wait for replies to packets from the server. Note: setting these values
 *          via the API will override settings in the configuration file.
 * </ul>
 *
 * Configuration settings are stored in META-INF/smack-config.xml (typically inside the
 * smack.jar file).
 * 
 * @author Gaston Dombiak
 */
public final class SmackConfiguration {

    private static final String SMACK_VERSION = "2.2.0";

    private static int packetReplyTimeout = 5000;
    private static int keepAliveInterval = 30000;

    private SmackConfiguration() {
    }

    /**
     * Loads the configuration from the smack-config.xml file.<p>
     * 
     * So far this means that:
     * 1) a set of classes will be loaded in order to execute their static init block
     * 2) retrieve and set the current Smack release
     */
    static {
        try {
            // Get an array of class loaders to try loading the providers files from.
            ClassLoader[] classLoaders = getClassLoaders();
            for (int i = 0; i < classLoaders.length; i++) {
            	// Eduardo Martins: changed path to resouce
                Enumeration configEnum = classLoaders[i].getResources("org/jivesoftware/smack/smack-config.xml");
                while (configEnum.hasMoreElements()) {
                    URL url = (URL) configEnum.nextElement();
                    InputStream systemStream = null;
                    try {
                        systemStream = url.openStream();
                        XmlPullParser parser = new MXParser();
                        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
                        parser.setInput(systemStream, "UTF-8");
                        int eventType = parser.getEventType();
                        do {
                            if (eventType == XmlPullParser.START_TAG) {
                                if (parser.getName().equals("className")) {
                                    // Attempt to load the class so that the class can get initialized
                                    parseClassToLoad(parser);
                                }
                                else if (parser.getName().equals("packetReplyTimeout")) {
                                    packetReplyTimeout = parseIntProperty(parser, packetReplyTimeout);
                                }
                                else if (parser.getName().equals("keepAliveInterval")) {
                                    keepAliveInterval = parseIntProperty(parser, keepAliveInterval);
                                }
                            }
                            eventType = parser.next();
                        }
                        while (eventType != XmlPullParser.END_DOCUMENT);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        try {
                            systemStream.close();
                        }
                        catch (Exception e) {
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Smack version information, eg "1.3.0".
     * 
     * @return the Smack version information.
     */
    public static String getVersion() {
        return SMACK_VERSION;
    }

    /**
     * Returns the number of milliseconds to wait for a response from
     * the server. The default value is 5000 ms.
     * 
     * @return the milliseconds to wait for a response from the server
     */
    public static int getPacketReplyTimeout() {
        // The timeout value must be greater than 0 otherwise we will answer the default value
        if (packetReplyTimeout <= 0) {
            packetReplyTimeout = 5000; 
        }
        return packetReplyTimeout;
    }

    /**
     * Sets the number of milliseconds to wait for a response from
     * the server.
     * 
     * @param timeout the milliseconds to wait for a response from the server
     */
    public static void setPacketReplyTimeout(int timeout) {
        if (timeout <= 0) {
            throw new IllegalArgumentException();
        }
        packetReplyTimeout = timeout;
    }

    /**
     * Returns the number of milleseconds delay between sending keep-alive
     * requests to the server. The default value is 30000 ms. A value of -1
     * mean no keep-alive requests will be sent to the server.
     *
     * @return the milliseconds to wait between keep-alive requests, or -1 if
     *      no keep-alive should be sent.
     */
    public static int getKeepAliveInterval() {
        return keepAliveInterval;
    }

    /**
     * Sets the number of milleseconds delay between sending keep-alive
     * requests to the server. The default value is 30000 ms. A value of -1
     * mean no keep-alive requests will be sent to the server.
     *
     * @param interval the milliseconds to wait between keep-alive requests,
     *      or -1 if no keep-alive should be sent.
     */
    public static void setKeepAliveInterval(int interval) {
        keepAliveInterval = interval;
    }

    private static void parseClassToLoad(XmlPullParser parser) throws Exception {
        String className = parser.nextText();
        // Attempt to load the class so that the class can get initialized
        try {
            Class.forName(className);
        }
        catch (ClassNotFoundException cnfe) {
            System.err.println("Error! A startup class specified in smack-config.xml could " +
                    "not be loaded: " + className);
        }
    }

    private static int parseIntProperty(XmlPullParser parser, int defaultValue)
            throws Exception
    {
        try {
            return Integer.parseInt(parser.nextText());
        }
        catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * Returns an array of class loaders to load resources from.
     *
     * @return an array of ClassLoader instances.
     */
    private static ClassLoader[] getClassLoaders() {
        ClassLoader[] classLoaders = new ClassLoader[2];
        classLoaders[0] = new SmackConfiguration().getClass().getClassLoader();
        classLoaders[1] = Thread.currentThread().getContextClassLoader();
        return classLoaders;
    }
}
