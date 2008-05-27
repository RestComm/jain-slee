package org.mobicents.slee.management.rules;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public interface RulesScannerMBean extends org.jboss.system.ServiceMBean {

	// TODO Expose few of the Management attributes for WorkingMemory

	// Attributes ----------------------------------------------------

	void setRecursiveSearch(boolean recurse);

	boolean getRecursiveSearch();

	void setFilter(String classname) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException;

	String getFilter();

	/**
	 * Set the scan period for the scanner.
	 * 
	 * @param period
	 *            This is the time in milliseconds between scans.
	 * @throws IllegalArgumentException
	 *             Period value out of range.
	 */
	void setScanPeriod(long period);

	/**
	 * Disable or enable the period based deployment scanning.
	 * <p>
	 * Manual scanning can still be performed by calling {@link #scan}.
	 * 
	 * @param flag
	 *            True to enable or false to disable period based scanning.
	 */
	void setScanEnabled(boolean flag);

	/**
	 * Check if period based scanning is enabled.
	 * 
	 * @return True if enabled, false if disabled.
	 */
	boolean isScanEnabled();

	/**
	 * Scan for changes in Rules files .drl, .xls, .cvs.
	 * 
	 * @throws IllegalStateException
	 *             Not initialized.
	 * @throws Exception
	 *             Scan failed.
	 */
	void scan() throws java.lang.Exception;

	void setURLList(List list);

	List getURLList();

	void setURLs(String listspec) throws MalformedURLException;

	// Operations ----------------------------------------------------

	void addURL(URL url);

	void removeURL(URL url);

	boolean hasURL(URL url);

	void addURL(String urlspec) throws MalformedURLException;

	void removeURL(String urlspec) throws MalformedURLException;

	boolean hasURL(String urlspec) throws MalformedURLException;

}
