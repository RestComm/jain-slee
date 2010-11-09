package org.alcatel.jsce.util.jad;

import java.util.List;

public interface IDecompiler {

	/**
	 * @param root the source directory 
	 * @param packageName the package name
	 * @param className the class name
	 * 	    className = "SipNistSbb";
	 * 		 root ="c:/";
	 * 	     packageName = "test/com/alacatel/sbb/sip";
	 * @return
	 */
	public abstract String getSourceCode(String root, String packageName, String className);

	/**
	 * @return the log error of the last decompilation.
	 */
	public abstract List getLogError();

	/**
	 * @return the log  of the last decompilation.
	 */
	public abstract String getLog();

}