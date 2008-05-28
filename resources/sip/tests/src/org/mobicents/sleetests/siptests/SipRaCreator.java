package org.mobicents.sleetests.siptests;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

import com.opencloud.sleetck.lib.testutils.FutureResult;

public class SipRaCreator implements Serializable {

	// private static SipRaCreator creator=null;

	private static Logger logger = Logger.getLogger(SipRaCreator.class
			.toString());

	protected String jnpHostURL = "jnp://127.0.0.1:1099";

	//protected String raTypeDUName = "sip-ra-type.jar";

	protected String raDUName = "sip-local-ra.jar";

	protected String raLINK = "SipRA";

	protected String raID = "JSIP v1.2#net.java.slee.sip#1.2";

	private String fileURL = null;

	protected ComponentKey raId = new ComponentKey(raID);

	protected ResourceAdaptorIDImpl ra = new ResourceAdaptorIDImpl(raId);

	protected boolean raTypeInstalled, raInstalled, raEntityCreated,
			raEntityActivated, raLinkCreated;

	protected String infoMSG = null;

	protected transient SleeCommandInterface SCI = null;

	protected boolean raSETUP = false;

	protected transient InitialContext ic = null;

	protected static String jndiNAME = "SIPRATestsRABINDER";

	public static SipRaCreator getInstance() throws Exception {
		SipRaCreator creator = null;
		creator = new SipRaCreator();

		SipRaCreator cr = null;
		InitialContext ic = creator.getContext();

		try {
			cr = (SipRaCreator) ic.lookup(jndiNAME);
		} catch (NamingException ne) {
			logger.info(ne.getMessage());
		}

		if (cr == null) {
			ic.bind(jndiNAME, creator);
			logger.info(" BINDING TO CLEAN:"+creator);
		} else {
			creator = cr;
			logger.info(" GOT FROM JNDI:"+creator);
		}
		ic.close();
		//Thread.currentThread().sleep(1500);
		return creator;

	}

	private InitialContext getContext() throws NamingException {
		logger.info( "getContext");
		if(ic!=null)
			ic.close();
		ic=null;
		
			Hashtable props = new Hashtable();

			props.put(InitialContext.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			props.put(InitialContext.PROVIDER_URL, jnpHostURL);

			// This establishes the security for authorization/authentication
			// props.put(InitialContext.SECURITY_PRINCIPAL,"username");
			// props.put(InitialContext.SECURITY_CREDENTIALS,"password");

			InitialContext initialContext = new InitialContext(props);
			ic = initialContext;
		
		return ic;
	}

	private SipRaCreator() throws Exception {
		logger.entering(this.toString(), "SipRaCreator");
		logger.info("3. raSETUP: " + raSETUP);
		Properties props = new Properties();
		try {
			// getLog().info(" == LOADING PROPS FROM: test.properties ==");
			props.load(getClass().getResourceAsStream("test.properties"));
			jnpHostURL = props.getProperty("jnpHost", jnpHostURL);
			//raTypeDUName = props.getProperty("raTypeDUName", raTypeDUName);
			raDUName = props.getProperty("raDUName", raDUName);
			raLINK = props.getProperty("raLINK", raLINK);
			
			raID = props.getProperty("raID", raID);

			raId = new ComponentKey(raID);
			ra = new ResourceAdaptorIDImpl(raId);

			Object o=getClass().getResource("test.properties");
			//slee-sip-ra/tests/classes/sleetests/org/mobicents/sleetests/siptests/test.properties
			String file=o.toString();
			;
			for(int i=0;i<7;i++)
			{
				int index=file.lastIndexOf("/");
				file=file.substring(0,index);
				
			}
			
	
			fileURL=file.replace("file:/", "");
		} catch (Exception IOE) {
			// getLog().info("FAILED TO LOAD: test.properties");
			IOE.printStackTrace();
			logger.severe(IOE.getMessage());
		}

		if (fileURL == null)
			throw new NullPointerException(" fileURL CANT BE NULL ");

		try {
			SCI = new SleeCommandInterface(jnpHostURL);
		} catch (Exception e1) {

			e1.printStackTrace();
			throw e1;
		}
		logger.info("4. raSETUP: " + raSETUP);
	}

	public void setUpRa(FutureResult result) {
		logger.entering(this.toString(), "setUpRa");
		logger.info("5. raSETUP: " + raSETUP);
		unSetUpRa(result);
		logger.info("6. raSETUP: " + raSETUP);
		/*try {
			Thread.currentThread().sleep(2500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		String dusPATH = fileURL.replace("file:/", "");
		logger.info(" == STARTIGN DEPLOYMENT["+dusPATH+"] ==");
		if (SCI == null)
			try {
				SCI = new SleeCommandInterface(jnpHostURL);
			} catch (Exception e1) {

				e1.printStackTrace();
				logger.info(e1.getMessage());
			}

		raTypeInstalled = raInstalled = raEntityActivated = raEntityCreated = raLinkCreated  = false;
		try {
			Object opResult = null;

			//opResult = SCI.invokeOperation("-install", dusPATH +"/ratype/jars/"+ raTypeDUName,
			//		null, null);

			//raTypeInstalled = true;

			opResult = SCI.invokeOperation("-install", getInDUModulePath(dusPATH),
					null, null);

			raInstalled = true;

			opResult = SCI.invokeOperation("-createRaEntity", ra.toString(),
					raLINK, null);

			raEntityCreated = true;

			opResult = SCI.invokeOperation("-activateRaEntity", raLINK, null,
					null);

			raEntityActivated = true;

			opResult = SCI.invokeOperation("-createRaLink", raLINK, raLINK,
					null);

			raLinkCreated = true;
			raSETUP = true;
			InitialContext ic = this.getContext();
			
			ic.unbind(jndiNAME);
			ic.bind(jndiNAME, this);
			logger.info("BOUND THIS:"+this);
			// ic.close();
		} catch (Exception e) {
			e.printStackTrace();
			if(result!=null)
				result.setError(e);
			
		}
		
		logger.info("7. raSETUP: " + raSETUP);

	}

	private void unSetUpRa(FutureResult result) {
		logger.info(" unSetUpRa1");
		if(!raSETUP)
			return;
		if (SCI == null)
			try {
				SCI = new SleeCommandInterface(jnpHostURL);
			} catch (Exception e1) {

				e1.printStackTrace();
				logger.info(e1.getMessage());
			}
		
		try {
			String dusPATH = fileURL;
			Object opResult = null;

			if (raLinkCreated) {
				opResult = SCI.invokeOperation("-removeRaLink", raLINK, null,
						null);
				raLinkCreated = !raLinkCreated;
				logger.info(" == RA LINK REMOVED:" + opResult + " ==");
			}
			if (raEntityActivated) {
				opResult = SCI.invokeOperation("-deactivateRaEntity", raLINK,
						null, null);
				raEntityActivated = !raEntityActivated;
				logger.info(" == RA ENTITY DEACTIVATED:" + opResult + " ==");
			}
			if (raEntityCreated) {
				opResult = SCI.invokeOperation("-removeRaEntity", raLINK, null,
						null);
				raEntityCreated = !raEntityCreated;
				logger.info(" == RA ENTITY REMOVED:" + opResult + " ==");
			}
			if (raInstalled) {
				opResult = SCI.invokeOperation("-uninstall",
						getInDUModulePath(dusPATH), null, null);
				raInstalled = !raInstalled;
				logger.info(" == RA UNINSTALLED:" + opResult + " ==");
			}
			//if (raTypeInstalled) {
			//	opResult = SCI.invokeOperation("-uninstall", dusPATH
			//			+"/ratype/jars/"+ raTypeDUName, null, null);
			//	raTypeInstalled = !raTypeInstalled;
			//	logger.info(" == RA TYPE UNINSTALLED:" + opResult + " ==");
			//}

			InitialContext ic = this.getContext();
			ic.unbind(jndiNAME);
			raSETUP = false;
			ic.bind(jndiNAME, this);
			//logger.info("BOUND THIS:"+this);
			// ic.close();
		} catch (Exception e) {
			if(result!=null)
				result.setError(e);
			return;
		}
		
	}

	
	private String getInDUModulePath(String dusPATH)
	{
		
		
		String base=dusPATH+"/du/target";
		
		File dir=new File(base);
		logger.info("---->BASE["+base+"]["+dir.exists()+"]["+dir.mkdir()+"]");
		String names[]=dir.list();
		for(int i=0;i<names.length;i++)
		{
			String name=names[i];
			if(name.startsWith("sip-ra-DU") && name.endsWith("jar"))
			{
				logger.info(" getInDUModulePath["+dusPATH+"/"+name+"]");
				return new File(dir,name).toURI().toString();
			}
		}
		logger.info(" getInDUModulePath["+dusPATH+"]["+null+"]");
		return null;
	}
	
	
	private void _unSetUpRa() {
		logger.info("unSetUpRa2");
		if(!raSETUP)
			return;
		if (SCI == null)
			try {
				SCI = new SleeCommandInterface(jnpHostURL);
			} catch (Exception e1) {

				e1.printStackTrace();
				logger.info(e1.getMessage());
			}
		
		try {
			Object opResult = null;

			if (raLinkCreated) {
				opResult = SCI.invokeOperation("-removeRaLink", raLINK, null,
						null);
				raLinkCreated = !raLinkCreated;
				logger.info(" == RA LINK REMOVED:" + opResult + " ==");
			}
			if (raEntityActivated) {
				opResult = SCI.invokeOperation("-deactivateRaEntity", raLINK,
						null, null);
				raEntityActivated = !raEntityActivated;
				logger.info(" == RA ENTITY DEACTIVATED:" + opResult + " ==");
			}
			if (raEntityCreated) {
				opResult = SCI.invokeOperation("-removeRaEntity", raLINK, null,
						null);
				raEntityCreated = !raEntityCreated;
				logger.info(" == RA ENTITY REMOVED:" + opResult + " ==");
			}
			if (raInstalled) {
				opResult = SCI.invokeOperation("-uninstall",
						fileURL + raDUName, null, null);
				raInstalled = !raInstalled;
				logger.info(" == RA UNINSTALLED:" + opResult + " ==");
			}
			//if (raTypeInstalled) {
			//	opResult = SCI.invokeOperation("-uninstall", fileURL
			//			+ raTypeDUName, null, null);
			//	raTypeInstalled = !raTypeInstalled;
			//	logger.info(" == RA TYPE UNINSTALLED:" + opResult + " ==");
			//}
			InitialContext ic = this.getContext();
			ic.unbind(jndiNAME);
			raSETUP = false;
			ic.bind(jndiNAME, this);
			//logger.info("BOUND THIS:"+this);
			// ic.close();
		} catch (Exception e) {

			return;
		}
		
	}
	
	 public void finalize() { 
		 unSetUpRa(null);
		 /*try {
			getContext();
			ic.unbind(jndiNAME);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		 
	  }
	 
	
	public String toString()
	{

		return "SipRaCreator[ "+super.toString()+" ]-> raSETUP[ "+raSETUP+" ],raTypeInstalled[ "+raTypeInstalled+" ],raInstalled[ "+raInstalled+" ],raEntityActivated[ "+raEntityActivated+" ],raEntityCreated[ "+raEntityCreated+" ],raLinkCreated[ "+raLinkCreated+" ]";
	}
}
