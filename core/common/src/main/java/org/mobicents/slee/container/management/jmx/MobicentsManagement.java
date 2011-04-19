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

package org.mobicents.slee.container.management.jmx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.Version;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class MobicentsManagement extends ServiceMBeanSupport implements
		MobicentsManagementMBean {

	// Number of minutes after lingering entities of inactive service will be
	// removed.
	public static double entitiesRemovalDelay = 1;

	// mobicents version
	private String mobicentsVersion = Version.instance.toString();

	public double getEntitiesRemovalDelay() {
		return MobicentsManagement.entitiesRemovalDelay;
	}

	public void setEntitiesRemovalDelay(double entitiesRemovalDelay) {
		MobicentsManagement.entitiesRemovalDelay = entitiesRemovalDelay;
	}

	public String getVersion() {
		return mobicentsVersion;
	}

	public String dumpState() throws ManagementException {

		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager txManager = sleeContainer
				.getTransactionManager();
		try {
			txManager.begin();
			return sleeContainer.dumpState();
		} catch (Exception e) {
			throw new ManagementException("Failed to get container state", e);
		} finally {
			try {
				txManager.commit();
			} catch (Exception e) {
				throw new ManagementException("Failed to get container state",
						e);
			}
		}
	}

    private static String log4jConfigFilePath = System.getProperty("jboss.server.home.dir") + File.separator + "conf" + File.separator + "jboss-log4j.xml";
    private static String log4jTemplatesPath = System.getProperty("jboss.server.home.dir") + File.separator + "deploy" + 
      File.separator + "mobicents-slee" + File.separator + "log4j-templates" + File.separator + "jboss-log4j.xml.";
	
	public String getLoggingConfiguration(String profile) throws ManagementException {
	  try {
	    return readFile(getLog4jPath(profile));
	  }
	  catch (IOException ioe) {
	    throw new ManagementException("Failed to read log4j configuration file for profile '" + profile + "'. Does it exists?", ioe);
	  }
	}

    public void setLoggingConfiguration(String profile, String contents) throws ManagementException {
      try {
        writeFile(getLog4jPath(profile), contents);
      }
      catch (IOException ioe) {
        throw new ManagementException("Failed to update log4j configuration file for profile '" + profile + "'.", ioe);
      }
    }

    public void switchLoggingConfiguration(String newProfile) throws ManagementException {
      try {
        // A direct copy would have better performance, but since we 
        // already have and need to have these methods, let's use them.
        String srcPath = getLog4jPath(newProfile);
        String destPath = getLog4jPath("");
        writeFile(destPath, readFile(srcPath));
      }
      catch (IOException ioe) {
        throw new ManagementException("Failed to update log4j configuration file for profile '" + newProfile + "'.", ioe);
      }
    }

	private String getLog4jPath(String profile) {
	  return (profile == null || profile.equals("") || profile.equalsIgnoreCase("current")) ? log4jConfigFilePath : log4jTemplatesPath + profile;
	}

	private String readFile(String path) throws IOException {
	  FileInputStream stream = new FileInputStream(new File(path));
	  try {
	    FileChannel fc = stream.getChannel();
	    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	    /* Instead of using default, pass in a decoder. */
	    return Charset.defaultCharset().decode(bb).toString();
	  }
	  finally {
	    if(stream != null) {
	      stream.close();
	    }
	  }
	}

	private void writeFile(String path, String contents) throws IOException {
	  BufferedWriter out = new BufferedWriter(new FileWriter(path));
	  try {
	    out.write(contents);
	  }
	  finally {
	    if(out != null) {
	      out.close();
	    }
	  }
	}

}
