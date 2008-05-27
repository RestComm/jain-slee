package org.mobicents.sleetests.junit.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.opencloud.sleetck.lib.AbstractSleeTCKTest;

import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;

import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;

import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JunitWrapperTest extends AbstractSleeTCKTest {

	public void setUp() throws Exception {
		super.setUp();

		getLog()
				.info(
						"\n========================\nConnecting to resource\n========================\n");
		TCKResourceListener resourceListener = new TestResourceListenerImpl();
		setResourceListener(resourceListener);
		/*
		 * Properties props = new Properties(); try {
		 * props.load(getClass().getResourceAsStream("sipStack.properties")); }
		 * catch (IOException IOE) { logger.info("FAILED TO LOAD:
		 * sipStack.properties"); }
		 */

	}

	protected FutureResult result;

	/*
	 * protected void setResultPassed(String msg) throws Exception {
	 * logger.info("Success: " + msg);
	 * 
	 * HashMap sbbData = new HashMap(); sbbData.put("result", Boolean.TRUE);
	 * sbbData.put("message", msg);
	 * TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData); }
	 * 
	 * protected void setResultFailed(String msg) throws Exception {
	 * logger.info("Failed: " + msg);
	 * 
	 * HashMap sbbData = new HashMap(); sbbData.put("result", Boolean.FALSE);
	 * sbbData.put("message", msg);
	 * TCKSbbUtils.getResourceInterface().sendSbbMessage(sbbData); }
	 */

	private class TestResourceListenerImpl extends BaseTCKResourceListener {

		public synchronized void onSbbMessage(TCKSbbMessage message,
				TCKActivityID calledActivity) throws RemoteException {
			Map sbbData = (Map) message.getMessage();
			Boolean sbbPassed = (Boolean) sbbData.get("result");
			String sbbTestMessage = (String) sbbData.get("message");

			getLog().info(
					"Received message from SBB: passed=" + sbbPassed
							+ ", message=" + sbbTestMessage);

			if (sbbPassed.booleanValue()) {
				result.setPassed();
			} else {
				result.setFailed(0, sbbTestMessage);
			}
		}

		public void onException(Exception exception) throws RemoteException {
			getLog().warning("Received exception from SBB or resource:");
			getLog().warning(exception);
			result.setError(exception);
		}
	}

	private TCKActivityID tckActivityID = null;

	private String activityName = null;

	// DEFAULT FILE NAME TO READ
	private final String defaultFileName = "junit-test.xml";

	// PROPS FILE, CAN CONTAIN DIFFERENT THAN DEFAULT FILE NAME
	private final String propsFileName = "test.properties";

	// SOME CONSTANTS
	private final String FAILURE = "FAILED";

	private final String PASS = "PASSED";

	private final String ERROR = "ERROR";

	public void run(FutureResult result) throws Exception {
		this.result = result;

		TCKResourceTestInterface resource = utils().getResourceInterface();
		activityName = utils().getTestParams().getProperty("activityName");
		// tckActivityID = resource.createActivity(activityName);

		// HOME SHOULDNT BE SINCE WE RUN MC TESTS, RIGHT?
		String home = System.getProperty("MOBICENTS_HOME");

		if (home == null) {
			// BUT IN CASE...
			result
					.setError("ENV variable MOBIENTS_HOME cant be null, how You even managed to get this far?");
		}

		InputStream IS = this.getClass().getResourceAsStream(propsFileName);

		String fileNameToRead = null;
		if (IS == null) {
			// WE WILL USE DEFAULT;
			fileNameToRead = defaultFileName;
		} else {

			// LETS READ
			Properties props = new Properties();
			try {
				props.load(IS);
				fileNameToRead = props
						.getProperty("org.mobicents.sleetests.cache.junit.Test_Result_File");

				if (fileNameToRead == null)
					fileNameToRead = defaultFileName;

			} catch (IOException e) {
				// WE WILL USE DEFAULT;
				fileNameToRead = defaultFileName;
			}

		}

		// LETS READ XML and PLAY WITH DOM
		String fileURL = home + "/" + fileNameToRead;
		File file = new File(fileURL);

		Document xmlDocument = null;

		// tests order should be maintained, right?
		// HERE WE WILL STORE ResultHolders
		ArrayList results = new ArrayList();
		// NOW HUGE try to cover all that stuff with parsing, dom and other
		// related

		int failed = 0;
		int errors = 0;
		int passed = 0;
		int totalTests = 0;
		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream docIS = new BufferedInputStream(new FileInputStream(
					file));
			xmlDocument = builder.parse(docIS);

			// NOW WE HAVE OUR DOC, LETS DO SOMETHING

			NodeList nlist = xmlDocument.getDocumentElement()
					.getElementsByTagName("testcase");

			getLog().info("TOTAL TESTS[" + nlist.getLength() + "]");
			totalTests = nlist.getLength();

			for (int i = 0; i < nlist.getLength(); i++) {

				Node node = nlist.item(i);
				ResultHolder holder = new ResultHolder();
				if (node.getChildNodes().getLength() > 0) {
					for (int j = 0; j < node.getChildNodes().getLength(); j++) {

						if (node.getChildNodes().item(j).getNodeName().equals(
								"failure")) {
							// WE HAVE A FAILURE
							failed++;
							holder.result = FAILURE;
							//holder.message = node.getChildNodes().item(j).getTextContent();
							holder.message = node.getChildNodes().item(j).getNodeValue();

						} else if (node.getChildNodes().item(j).getNodeName()
								.equals("error")) {

							errors++;
							holder.result = ERROR;
							//holder.message = node.getChildNodes().item(j).getTextContent();
							holder.message = node.getChildNodes().item(j).getNodeValue();

						} else {
							// IT DID PASS ?
						}

					}

				}

				holder.testClassName = node.getAttributes().getNamedItem(
						"classname").getNodeValue();
				holder.testName = node.getAttributes().getNamedItem("name")
						.getNodeValue();
				results.add(holder);
			}
		} catch (SAXException sxe) {
			// Error generated during parsing
			Exception x = sxe;
			if (sxe.getException() != null)
				x = sxe.getException();
			x.printStackTrace();

		} catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();

		} catch (IOException ioe) {
			// I/O error
			ioe.printStackTrace();
		}

		// NOW LETS CREATE REPORT
		passed = totalTests - errors - failed;

		Iterator it = results.iterator();

		StringBuffer sb = new StringBuffer(1000);
		sb.append("\n");
		sb
				.append("=====================================================================================\n");
		sb.append("| TOTAL TESTS: " + totalTests + " |PASSED: " + passed
				+ " |FAILED: " + failed + " |ERRORS: " + errors + "\n");
		sb
				.append("=====================================================================================\n");
		while (it.hasNext()) {
			ResultHolder holder = (ResultHolder) it.next();
			sb.append("\n***********");
			sb.append("| TESTCLASS: " + holder.testClassName + " \n|TEST: "
					+ holder.testName + " \n|RESULT: " + holder.result
					+ " \n|MESSAGE: " + holder.message + "");
			sb.append("\n***********");
		}
		sb.append("\n");
		sb.append("=====================================================================================\n");

		// LETS REPORT;
		if (errors > 0) {
			result.setError(sb.toString());
		} else if (failed > 0) {
			result.setFailed(0, sb.toString());
		} else {
			getLog().info(sb.toString());
			result.setPassed();
		}

	}

	class ResultHolder {
		String testClassName = null;

		String testName = null;

		// DEFAULT VALUE IS PASS
		String result = PASS;

		// THIS IS USED ONLY WHEN TEST FAILED OR HAD ERROR (?)
		String message = null;
	}

}
