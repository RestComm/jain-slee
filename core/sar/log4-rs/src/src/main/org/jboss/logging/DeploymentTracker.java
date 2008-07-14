/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.logging;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.MBeanServer;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.xml.DOMConfigurator;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.metadata.WebMetaData;
import org.jboss.util.stream.Streams;

/**
 * This class tracks the deployments and the ClassLoaders associated with them.
 * 
 * TODO: store the repository only in the di.context.
 * 
 * @version <tt>$Revision:  $</tt>
 * @author <a href="mailto:smil@dev.hu">Tamas Cserveny</a>
 * @author <a href="mailto:abhayani@redhat.com">Amit Bhayani</a>
 */
public class DeploymentTracker {
	/** Max number of failed classloader-idents to learn. */
	private static final int MAX_LIST_LENGTH = 10;

	private Timer timer = new Timer(true);

	/** Contains all deployments with the data. */
	private Map deployments = new HashMap();

	private Map<DeploymentInfo, TimerTask> deploymentTimersMap = new HashMap<DeploymentInfo, TimerTask>();

	/** Maps the CL to the data. */
	private Map classloaderCache = new IdentityHashMap();

	/** List of CL ids where we failed the lookup. */
	private LinkedList classloaderBlacklist = new LinkedList();

	private Logger log;
	private MBeanServer server;


	public DeploymentTracker(MBeanServer server, Logger log) {
		this.server = server;
		this.log = log;
	}

	public synchronized void start(DeploymentInfo di, int refreshPeriod) {

		ClassLoader cl = di.localCl;
		URL configURL = cl.getResource("META-INF/log4j.xml");
		if (configURL == null) {
			configURL = cl.getResource("META-INF/log4j.properties");
		}

		if (configURL != null) {
			URLWatchTimerTask timerTask = new URLWatchTimerTask(di, configURL);
			timerTask.run();
			timer.schedule(timerTask, 1000 * refreshPeriod, 1000 * refreshPeriod);
			deploymentTimersMap.put(di, timerTask);
		}
	}

	/**
	 * Adds a new deployment into the tracker.
	 */
	public synchronized void trackDeployment(DeploymentInfo di, final Object data) {
		deployments.put(di, data);

		// Register all already known classloaders
		// Not all classloaders are available at this stage.
		visitClassLoaders(di, new ClassLoaderVisitor() {
			public void visit(ClassLoader cl) {
				classloaderCache.put(cl, data);
			}
		});
	}

	public synchronized Object stop(DeploymentInfo di) {
		TimerTask timeTrask = deploymentTimersMap.get(di);

		if (timeTrask != null) {
			timeTrask.cancel();
		}

		deploymentTimersMap.remove(di);

		return removeDeployment(di);
	}

	/**
	 * Removes the deployment from the tracker.
	 */
	public synchronized Object removeDeployment(DeploymentInfo di) {
		Object data = deployments.remove(di);

		// Remove all cached classloaders
		if (data != null) {
			for (Iterator i = classloaderCache.values().iterator(); i.hasNext();) {
				Object obj = i.next();

				if (data == obj) {
					i.remove();
				}

			}
		}

		return data;
	}

	/**
	 * Tries to get the object associated with the deployment called this
	 * method. May return null, if the deployment could not be identified.
	 */
	public synchronized Object get() {
		// Do we track any deployments?
		if (deployments.isEmpty())
			return null;

		ClassLoader ccl = Thread.currentThread().getContextClassLoader();
		ClassLoader p = ccl;

		// Is the classloader blacklisted?
		if (isClassLoaderBlacklisted(ccl))
			return null;

		Object data;

		// Try to find the classloader or its parents in the deployments.
		do {
			data = findDataForClassLoader(p);
			p = p.getParent();
		} while (p != null && data == null);

		if (data == null) {
			log.debug("Unknown classloader : " + printIdent(ccl) + ", " + "type : " + ccl.getClass().toString());

			// Don't bother any more.
			blacklistClassLoader(ccl);
		} else {
			// Put the _original_ classloader into the cache.
			classloaderCache.put(ccl, data);
		}

		return data;
	}

	/**
	 * Prints some usage statistics and the UCLs tracked.
	 */
	public void printStats() {
		if (log.isDebugEnabled()) {
			// Sync only if debug is turned on.
			synchronized (this) {
				log.debug("Stats : deps=" + deployments.size() + " cached=" + classloaderCache.size());

				final StringBuffer sb = new StringBuffer();
				for (Iterator i = deployments.keySet().iterator(); i.hasNext();) {
					DeploymentInfo di = (DeploymentInfo) i.next();
					visitClassLoaders(di, new ClassLoaderVisitor() {
						public void visit(ClassLoader cl) {
							sb.append(' ').append(printIdent(cl));
						}
					});
				}
				log.debug("UCLs :" + sb.toString());
			}
		}
	}

	/**
	 * Matches the classloader to the ones found in the deployments or in the
	 * cache. May return null.
	 */
	private Object findDataForClassLoader(ClassLoader ccl) {
		Object data = null;

		// Check in cache
		if (classloaderCache.containsKey(ccl)) {
			data = classloaderCache.get(ccl);
		} else {
			// Find it in the deployments
			for (Iterator i = deployments.keySet().iterator(); i.hasNext();) {
				DeploymentInfo di = (DeploymentInfo) i.next();
				final Set clset = new HashSet();

				visitClassLoaders(di, new ClassLoaderVisitor() {
					public void visit(ClassLoader cl) {
						clset.add(cl);
					}
				});

				if (clset.contains(ccl)) {
					data = deployments.get(di);
					classloaderCache.put(ccl, data);

					log.debug("Deployment : " + di.shortName + " found logger.");
					break;
				}
			}
		}

		return data;
	}

	/**
	 * Adds a new unknown class loader.
	 */
	private void blacklistClassLoader(ClassLoader cl) {
		// Keep only a limited amount, to avoid leak.
		if (classloaderBlacklist.size() > MAX_LIST_LENGTH) {
			classloaderBlacklist.removeFirst();
		}

		// Store only a string representation to omit having a hard reference
		// to the given classloader.
		classloaderBlacklist.add(printIdent(cl));
	}

	/**
	 * Check whether we already failed to find a deployment for this loader.
	 */
	private boolean isClassLoaderBlacklisted(ClassLoader cl) {
		return classloaderBlacklist.contains(printIdent(cl));
	}

	/**
	 * Returns a string representation of the classloader.
	 */
	private String printIdent(ClassLoader ccl) {
		return Integer.toHexString(System.identityHashCode(ccl));
	}

	/**
	 * Visitor will go past all available classloader in the deployment.
	 */
	private void visitClassLoaders(DeploymentInfo di, ClassLoaderVisitor clv) {
		visit(clv, di.ucl);
		visit(clv, di.localCl);

		if (di.metaData instanceof WebMetaData) {
			WebMetaData wmd = (WebMetaData) di.metaData;
			visit(clv, wmd.getENCLoader());
			visit(clv, wmd.getContextLoader());
		}
	}

	private void visit(ClassLoaderVisitor clv, ClassLoader cl) {
		if (cl != null) {
			clv.visit(cl);
		}
	}

	private interface ClassLoaderVisitor {
		void visit(ClassLoader cl);
	}

	// /////////////////////////////////////////////////////////////////////////
	// URL Watching Timer Task //
	// /////////////////////////////////////////////////////////////////////////

	/**
	 * A timer task to check when a URL changes (based on last modified time)
	 * and reconfigure Log4j.
	 */
	private class URLWatchTimerTask extends TimerTask {

		DeploymentInfo di = null;
		URL configURL = null;
		private boolean log4jQuietMode = true;

		URLWatchTimerTask(DeploymentInfo di, URL configURL) {
			this.di = di;
			this.configURL = configURL;
		}

		private Logger log = Logger.getLogger(URLWatchTimerTask.class);

		private long lastConfigured = -1;

		public void run() {
			log.trace("Checking if configuration changed");

			boolean trace = log.isTraceEnabled();

			try {
				URLConnection conn = configURL.openConnection();
				if (trace)
					log.trace("connection: " + conn);

				long lastModified = conn.getLastModified();
				if (trace) {
					log.trace("last configured: " + lastConfigured);
					log.trace("last modified: " + lastModified);
				}

				if (lastConfigured < lastModified) {
					reconfigure(conn);
				}
			} catch (Exception e) {
				log.warn("Failed to check URL: " + configURL, e);
			}
		}

		public void reconfigure() throws IOException {
			URLConnection conn = configURL.openConnection();
			reconfigure(conn);
		}

		private void reconfigure(final URLConnection conn) throws IOException {
			log.info("Configuring from URL: " + configURL);

			Hierarchy hierarchy = null;

			boolean xml = false;
			boolean trace = log.isTraceEnabled();

			// check if the url is xml
			String contentType = conn.getContentType();
			if (trace)
				log.trace("content type: " + contentType);

			if (contentType == null) {
				String filename = configURL.getFile().toLowerCase();
				if (trace)
					log.trace("filename: " + filename);

				xml = filename.endsWith(".xml");
			} else {
				xml = contentType.equalsIgnoreCase("text/xml");
				xml |= contentType.equalsIgnoreCase("application/xml");
			}
			if (trace)
				log.trace("reconfiguring; xml=" + xml);

			java.io.InputStream is = null;

			is = conn.getInputStream();

			// Dump our config if trace is enabled
			if (trace) {
				try {
					Streams.copy(is, System.out);
				} catch (Exception e) {
					log.error("Failed to dump config", e);
				}
			}

			if (is != null) {
				
				hierarchy = (Hierarchy) deployments.get(di);
				if (hierarchy == null) {
					hierarchy = new Hierarchy(new RootCategory(Level.INFO));
				} else {
					hierarchy.resetConfiguration();
					hierarchy.clear();
				}

				if (xml) {
					DOMConfigurator conf = new DOMConfigurator();
					conf.doConfigure(is, hierarchy);
				} else {

					PropertyConfigurator conf = new PropertyConfigurator();
					Properties prop = new Properties();
					try {
						prop.load(is);
					} catch (IOException e) {
						e.printStackTrace();
					}
					conf.doConfigure(prop, hierarchy);
				}

			
			}

			/*
			 * Set the LogLog.QuietMode. As of log4j1.2.8 this needs to be set
			 * to avoid deadlock on exception at the appender level. See
			 * bug#696819.
			 */
			// LogLog.setQuietMode(log4jQuietMode);
			// but make sure they get reinstalled again

			// and then remeber when we were last changed
			lastConfigured = System.currentTimeMillis();

			if (hierarchy != null && !deployments.containsKey(di)) {
				trackDeployment(di, hierarchy);
			} 
		}
	}
}
