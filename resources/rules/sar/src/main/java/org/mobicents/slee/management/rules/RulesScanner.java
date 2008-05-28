package org.mobicents.slee.management.rules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.rule.Package;
import org.jboss.logging.Logger;
import org.jboss.naming.Util;
import org.jboss.net.protocol.URLLister;
import org.jboss.net.protocol.URLListerFactory;
import org.jboss.net.protocol.URLLister.URLFilter;
import org.jboss.system.ServiceMBeanSupport;
import org.jboss.system.server.ServerConfig;
import org.jboss.system.server.ServerConfigLocator;
import org.jboss.util.NullArgumentException;
import org.jboss.util.StringPropertyReplacer;

import EDU.oswego.cs.dl.util.concurrent.SynchronizedBoolean;

/**
 * A URL-based scanner. Supports local directory scanning for file-based urls.
 * This MBean searchs for rules file like .xls, .XLS, .csv, .CSV and .drl if
 * found in the directory specified by user, this MBean will create RuleBase and
 * will bind to JNDI with the name of file as jndi name under subcontext
 * java:rulebase/
 * 
 * for example for MyRules.xls rule file, RuleBase will be bound at
 * java:rulebase/MyRules.xls
 * 
 */

public class RulesScanner extends ServiceMBeanSupport implements
		RulesScannerMBean {

	public static final String RULES_BASE_JNDI_PREFIX = "java:rulebase/";

	/** The stop timeout */
	protected long stopTimeOut = 60000;

	/** The scan period in milliseconds */
	protected long scanPeriod = 5000;

	/** True if period based scanning is enabled. */
	protected boolean scanEnabled = true;

	protected URL serverHomeURL;

	/** The list of URLs to scan. */
	protected List urlList = Collections.synchronizedList(new ArrayList());

	/** A set of scanned urls which have been bounded to JNDI. */
	protected Set boundSet = Collections.synchronizedSet(new HashSet());

	/** Helper for listing local/remote directory URLs */
	protected URLListerFactory listerFactory = new URLListerFactory();

	/** Whether to search inside directories whose names containing no dots */
	protected boolean doRecursiveSearch = true;

	protected ScannerThread scannerThread;

	/** Allow a filter for scanned directories */
	protected URLFilter filter;

	/**
	 * HACK: Shutdown hook to get around problems with system service shutdown
	 * ordering.
	 */
	private Thread shutdownHook;

	/**
	 * @jmx:managed-attribute
	 */
	public void setRecursiveSearch(boolean recurse) {
		doRecursiveSearch = recurse;
	}

	/**
	 * @jmx:managed-attribute
	 */
	public boolean getRecursiveSearch() {
		return doRecursiveSearch;
	}

	public void addURL(URL url) {
		if (url == null)
			throw new NullArgumentException("url");

		try {
			// check if this is a valid url
			url.openConnection().connect();
		} catch (IOException e) {
			// either a bad configuration (non-existent url) or a transient i/o
			// error
			log.warn("addURL(), caught " + e.getClass().getName() + ": "
					+ e.getMessage());
		}
		urlList.add(url);

		log.debug("Added url: " + url);

	}

	public void addURL(String urlspec) throws MalformedURLException {
		addURL(makeURL(urlspec));

	}

	public List getURLList() {
		return new ArrayList(urlList);
	}

	public boolean hasURL(URL url) {
		if (url == null)
			throw new NullArgumentException("url");

		return urlList.contains(url);
	}

	public boolean hasURL(String urlspec) throws MalformedURLException {
		return hasURL(makeURL(urlspec));
	}

	public boolean isScanEnabled() {
		return scanEnabled;
	}

	public void setScanEnabled(final boolean flag) {
		this.scanEnabled = flag;
	}

	public void removeURL(URL url) {
		if (url == null)
			throw new NullArgumentException("url");

		boolean success = urlList.remove(url);
		if (success) {
			log.debug("Removed url: " + url);
		}

	}

	public void removeURL(String urlspec) throws MalformedURLException {
		removeURL(makeURL(urlspec));
	}

	public void setFilter(String classname) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException {
		Class filterClass = Thread.currentThread().getContextClassLoader()
				.loadClass(classname);
		filter = (URLFilter) filterClass.newInstance();
	}

	/**
	 * @jmx:managed-attribute
	 */
	public String getFilter() {
		if (filter == null)
			return null;
		return filter.getClass().getName();
	}

	public void scan() throws Exception {
		log.debug("scan() of RulesManagement called");

		boolean trace = log.isTraceEnabled();

		List urlsToBind = new LinkedList();

		synchronized (urlList) {
			for (Iterator i = urlList.iterator(); i.hasNext();) {
				URL url = (URL) i.next();
				try {
					if (url.toString().endsWith("/")) {
						// treat URL as a collection
						URLLister lister = listerFactory.createURLLister(url);

						// listMembers() will throw an IOException if collection
						// url does not exist
						urlsToBind.addAll(lister.listMembers(url, filter,
								doRecursiveSearch));
					} else {
						// treat URL as a deployable unit

						// throws IOException if this URL does not exist
						url.openConnection().connect();
						urlsToBind.add(url);
					}
				} catch (IOException e) {
					// Either one of the configured URLs is bad, i.e. points to
					// a non-existent
					// location, or it ends with a '/' but it is not a directory
					// (so it
					// is really user's fault), OR some other hopefully
					// transient I/O error
					// happened (e.g. out of file descriptors?) so log a
					// warning.
					log.warn("Scan URL, caught " + e.getClass().getName()
							+ ": " + e.getMessage());

					// We need to return because at least one of the listed URLs
					// will
					// return no results, and so all deployments starting from
					// that point
					// (e.g. deploy/) will get undeployed, see JBAS-3107.
					// On the other hand, in case of a bad configuration nothing
					// will get
					// deployed. If really want independence of e.g. 2 deploy
					// urls, more
					// than one URLDeploymentScanners can be setup.
					return;
				}
			}
		}

		if (urlsToBind.size() == 0) {
			log.info("No Rules files found");
		}

		List urlsToRemove = new LinkedList();
		List urlsToCheckForUpdate = new LinkedList();

		// check if Undeploy or Redeploy if modified
		for (Iterator i = boundSet.iterator(); i.hasNext();) {
			BindURL boundURL = (BindURL) i.next();

			if (urlsToBind.contains(boundURL.url)) {
				urlsToCheckForUpdate.add(boundURL);
			} else {
				urlsToRemove.add(boundURL);
			}
		}

		// **********
		// Undeploy
		// **********
		for (Iterator i = urlsToRemove.iterator(); i.hasNext();) {
			BindURL boundURL = (BindURL) i.next();
			if (trace) {
				log.trace("Removing " + boundURL.url);
			}
			unbind(boundURL);
		}

		// **********
		// ReDeploy
		// **********
		for (Iterator i = urlsToCheckForUpdate.iterator(); i.hasNext();) {
			BindURL boundURL = (BindURL) i.next();
			if (boundURL.isModified()) {
				if (trace) {
					log.trace("Re-binding " + boundURL.url);
				}
				bind(boundURL, true);
			}
		}

		// **********
		// Deploy new ones found
		// **********
		for (Iterator i = urlsToBind.iterator(); i.hasNext();) {
			URL url = (URL) i.next();
			BindURL bindURL = new BindURL(url);
			if (boundSet.contains(bindURL) == false) {
				if (trace) {
					log.trace("binding new" + bindURL.url);
				}
				bind(bindURL, false);

			}
		}

	}

	protected void unbind(final BindURL du) {
		// Degister from JNDI
		String jndiName = du.getFile().getName();
		jndiName = RULES_BASE_JNDI_PREFIX + jndiName;

		try {
			Context context = new InitialContext();
			Util.unbind(context, jndiName);

			boundSet.remove(du);
			log.debug("Removed RuleBase successfully from JNDI " + jndiName);
		} catch (NamingException nameExce) {
			log.error("Failed to unbind " + jndiName, nameExce);
		}
	}

	/**
	 * A helper to deploy the given URL with the deployer.
	 */
	protected void bind(final BindURL du, boolean rebind) {

		// Create RuleBase from rules file else simply ignore
		if (du.isFile()) {
			try {
				String jndiName = du.getFile().getName();

				PackageBuilder builder = new PackageBuilder();
				builder.addPackageFromDrl(du.getReader());

				Package pkg = builder.getPackage();

				RuleBase rb = RuleBaseFactory.newRuleBase();
				rb.addPackage(pkg);

				// Register to JNDI
				Context context = new InitialContext();
				jndiName = RULES_BASE_JNDI_PREFIX + jndiName;
				if (rebind) {
					Util.rebind(context, jndiName, (Object) rb);
					log.debug("rebind RuleBase successfully to JNDI "
							+ jndiName);
				} else {
					Util.bind(context, jndiName, (Object) rb);
					log.debug("bind RuleBase successfully to JNDI " + jndiName);
				}

				// set the modifiedTime
				du.bound();

				// If everything goes right we add this du in deployedSet
				if (!boundSet.contains(du)) {
					boundSet.add(du);
				}
			} catch (FileNotFoundException fileNotFoundExce) {
				log.error(fileNotFoundExce.getMessage(), fileNotFoundExce);

			} catch (IOException ioExce) {
				log.error(ioExce.getMessage(), ioExce);

			} catch (DroolsParserException droolsParserExc) {
				log.error(droolsParserExc.getMessage(), droolsParserExc);

			} catch (Exception ex) {
				log.error("Generic Exception in deploy() of RulesScanner", ex);
			}
		} else {
			log.info(" Passed DeployedURL " + du.url + "is not file");
		}
	}

	public void setScanPeriod(long period) {
		this.scanPeriod = period;

	}

	public void setURLList(List list) {
		if (list == null)
			throw new NullArgumentException("list");

		// start out with a fresh list
		urlList.clear();

		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			URL url = (URL) iter.next();
			if (url == null)
				throw new NullArgumentException("list element");

			addURL(url);
		}

		log.debug("URL list: " + urlList);

	}

	public void setURLs(String listspec) throws MalformedURLException {
		if (listspec == null)
			throw new NullArgumentException("listspec");

		List list = new LinkedList();

		StringTokenizer stok = new StringTokenizer(listspec, ",");
		while (stok.hasMoreTokens()) {
			String urlspec = stok.nextToken().trim();
			log.debug("Adding URL from spec: " + urlspec);

			URL url = makeURL(urlspec);
			log.debug("URL: " + url);

			list.add(url);
		}

		setURLList(list);

	}

	/**
	 * A helper to make a URL from a full url, or a filespec.
	 */
	protected URL makeURL(String urlspec) throws MalformedURLException {
		// First replace URL with appropriate properties
		//
		urlspec = StringPropertyReplacer.replaceProperties(urlspec);
		return new URL(serverHomeURL, urlspec);
	}

	// ///////////////////////////////////////////////////////////////////////
	// Service/ServiceMBeanSupport //
	// ///////////////////////////////////////////////////////////////////////

	public ObjectName preRegister(MBeanServer server, ObjectName name)
			throws Exception {
		// get server's home for relative paths, need this for setting
		// attribute final values, so we need to do it here
		ServerConfig serverConfig = ServerConfigLocator.locate();
		// serverHome = serverConfig.getServerHomeDir();
		serverHomeURL = serverConfig.getServerHomeURL();

		return super.preRegister(server, name);
	}

	protected void createService() throws Exception {

		scannerThread = new ScannerThread(false);
		scannerThread.setDaemon(true);
		scannerThread.start();
		log.debug("Scanner thread started");

		// HACK
		// 
		// install a shutdown hook, as the current system service shutdown
		// mechanism will not call this until all other services have stopped.
		// we need to know soon, so we can stop scanning to try to avoid
		// starting new services when shutting down

		final ScannerThread _scannerThread = scannerThread;
		shutdownHook = new Thread("Rules DeploymentScanner Shutdown Hook") {
			ScannerThread thread = _scannerThread;

			public void run() {
				thread.shutdown();
			}
		};

		try {
			Runtime.getRuntime().addShutdownHook(shutdownHook);
		} catch (Exception e) {
			log.warn("Failed to add shutdown hook", e);
		}

	}

	protected void startService() throws Exception {

		// Before Starting the service let us create the Subcontext
		Context ctx = new InitialContext();
		try {
			Util.createSubcontext(ctx, RULES_BASE_JNDI_PREFIX);
		} catch (NamingException e) {
			log.warn("Context, " + RULES_BASE_JNDI_PREFIX
					+ " might have been bound.");
			log.warn(e);
		}

		synchronized (scannerThread) {
			// scan before we enable the thread, so JBoss version shows up
			// afterwards
			scannerThread.doScan();

			// enable scanner thread if we are enabled
			scannerThread.setEnabled(scanEnabled);
		}
	}

	protected void stopService() throws Exception {

		// Before stoping lets clean the JNDI Tree
		Context ctx = new InitialContext();
		try {
			Util.unbind(ctx, RULES_BASE_JNDI_PREFIX);
		} catch (NamingException e) {
			log.warn("Failed to unbind context, " + RULES_BASE_JNDI_PREFIX);
			log.warn(e);
		}

		// disable scanner thread
		if (scannerThread != null) {
			scannerThread.setEnabled(false);
			scannerThread.waitForInactive();
		}
	}

	protected void destroyService() throws Exception {

		// shutdown scanner thread
		if (scannerThread != null) {
			synchronized (scannerThread) {
				scannerThread.shutdown();
			}
		}

		// HACK
		// 
		// remove the shutdown hook, we don't need it anymore
		try {
			Runtime.getRuntime().removeShutdownHook(shutdownHook);
		} catch (Exception ignore) {
		} // who cares really

		// help gc
		shutdownHook = null;
		scannerThread = null;
	}

	// ///////////////////////////////////////////////////////////////////////
	// Scanner Thread //
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * Should use Timer/TimerTask instead? This has some issues with interaction
	 * with ScanEnabled attribute. ScanEnabled works only when
	 * starting/stopping.
	 */
	public class ScannerThread extends Thread {
		/** We get our own logger. */
		protected Logger scannerLog = Logger.getLogger(ScannerThread.class);

		/** True if the scan loop should run. */
		protected SynchronizedBoolean enabled = new SynchronizedBoolean(false);

		/** True if we are shutting down. */
		protected SynchronizedBoolean shuttingDown = new SynchronizedBoolean(
				false);

		/** Lock/notify object. */
		protected Object lock = new Object();

		/** Active synchronization. */
		protected SynchronizedBoolean active = new SynchronizedBoolean(false);

		public ScannerThread(boolean enabled) {
			super("Rules ScannerThread");

			this.enabled.set(enabled);
		}

		public void setEnabled(boolean enabled) {
			this.enabled.set(enabled);

			synchronized (lock) {
				lock.notifyAll();
			}

			scannerLog.debug("Notified that enabled: " + enabled);
		}

		public void shutdown() {
			enabled.set(false);
			shuttingDown.set(true);

			synchronized (lock) {
				lock.notifyAll();
			}

			scannerLog.debug("Notified to shutdown");

			// jason: shall we also interrupt this thread?
		}

		public void run() {
			scannerLog.debug("Running");

			active.set(true);
			try {
				while (shuttingDown.get() == false) {
					// If we are not enabled, then wait
					if (enabled.get() == false) {
						synchronized (active) {
							active.set(false);
							active.notifyAll();
						}
						try {
							scannerLog
									.debug("Disabled, waiting for notification");
							synchronized (lock) {
								lock.wait();
							}
						} catch (InterruptedException ignore) {
						}
						active.set(true);
					}

					loop();
				}
			} finally {
				synchronized (active) {
					active.set(false);
					active.notifyAll();
				}
			}

			scannerLog.debug("Shutdown");
		}

		protected void waitForInactive() {
			boolean interrupted = false;
			synchronized (active) {
				try {
					if (active.get() && stopTimeOut > 0)
						active.wait(stopTimeOut);
				} catch (InterruptedException ignored) {
					interrupted = true;
				}
			}
			if (interrupted)
				Thread.currentThread().interrupt();
		}

		public void doScan() {
			// Scan for new/removed/changed/whatever
			try {
				scan();
			} catch (Exception e) {
				scannerLog.error("Scanning failed; continuing", e);
			}
		}

		protected void loop() {
			while (enabled.get() && shuttingDown.get() == false) {
				doScan();

				// Sleep for scan period
				try {
					scannerLog.trace("Sleeping...");
					Thread.sleep(scanPeriod);
				} catch (InterruptedException ignore) {
				}
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////
	// DeployedURL //
	// ///////////////////////////////////////////////////////////////////////

	/**
	 * A container and help class for a bind URL. should be static at this
	 * point, with the explicit scanner ref.
	 */
	protected class BindURL {
		public URL url;

		public File file;

		/** The url to check to decide if we need to redeploy */

		public long bindLastModified;

		public BindURL(final URL url) {
			this.url = url;
			file = new File(url.getFile());
		}

		public void bound() {
			bindLastModified = getLastModified();
		}

		public boolean isFile() {
			return this.file.isFile();
		}

		public File getFile() {
			return file;
		}

		public Reader getReader() throws FileNotFoundException {
			Reader reader = null;
			FileInputStream fileInputStream = new FileInputStream(this.file);
			if (this.file.getName().endsWith(".xls")) {
				SpreadsheetCompiler converter = new SpreadsheetCompiler();
				String drl = converter.compile(fileInputStream, InputType.XLS);
				reader = new StringReader(drl);
			} else if (this.file.getName().endsWith(".csv")) {
				SpreadsheetCompiler converter = new SpreadsheetCompiler();
				String drl = converter.compile(fileInputStream, InputType.CSV);
				reader = new StringReader(drl);
			} else {
				// safe to assume that its .drl as it has been filtered by
				// RulesFileFilter
				reader = new InputStreamReader(fileInputStream);
			}

			return reader;
		}

		public boolean isRemoved() {
			if (isFile()) {
				File file = getFile();
				return !file.exists();
			}
			return false;
		}

		public long getLastModified() {

			try {

				URLConnection connection = url.openConnection();

				// no need to do special checks for files...
				// org.jboss.net.protocol.file.FileURLConnection correctly
				// implements the getLastModified method.
				long lastModified = connection.getLastModified();

				return lastModified;
			} catch (java.io.IOException e) {
				log.warn(
						"Failed to check modification of deployed url: " + url,
						e);
			}
			return -1;
		}

		public boolean isModified() {
			long lastModified = getLastModified();
			return bindLastModified != lastModified;
		}

		public int hashCode() {
			return url.hashCode();
		}

		public boolean equals(final Object other) {
			if (other instanceof BindURL) {
				return ((BindURL) other).url.equals(this.url);
			}
			return false;
		}

		public String toString() {
			return super.toString() + "{ url=" + url
					+ ", deployedLastModified=" + bindLastModified + " }";
		}
	}

}
