package org.mobicents.slee.runtime.cache.tests;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CacheAllTests {

	//pattern - ZNameTest.class
	public static final String testClassNamePattern="Z\\w+Test\\.class";
	public static void main(String[] args)
	{
		CacheAllTests.suite();
	}
	public static Test suite()
	{
		//HERE WE HAVE TO MAKE LIST OF ALL FILES Z*Test.java 
		//AND RETRIEVE  THEIR suite() - agregated in one suite
		URL url=CacheAllTests.class.getResource("CacheAllTests.class");
		
		File file=new File(url.getFile());
		File dir=file.getParentFile();
		String[] files=dir.list();
		
		TestSuite suite=new TestSuite();
		
		for(int i=0;i<files.length;i++)
		{
			//LETS SEARCH FOR TEST Z*Test.java
			
			if(Pattern.matches(testClassNamePattern,files[i]))
			{
				System.out.println("TEST["+CacheAllTests.class.getPackage().getName()+"."+files[i]+"]");
				String className=files[i].split("\\.")[0];
		
				//WE NEED INSTANCE OF THIS CLASS
				Class testClass=null;
				try {
					testClass=CacheAllTests.class.forName(CacheAllTests.class.getPackage().getName()+"."+className);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(testClass==null)
				{
					System.out.println("TESTS CLASS IS NULL");
					continue;
				}
				System.out.println("ADDDING["+testClass+"]");
				//LETS LOAD SUITE, this can be done automaticaly, we just have to specify class
				suite.addTestSuite(testClass);
				
			}
			else
			{
				//DO NOTHING
			}
			
			
		}
		
		return suite;
	}
}
