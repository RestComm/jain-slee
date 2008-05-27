package org.mobicents.sleetests.httpservletra;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.TraceMethod;

public class HttpServletRATest extends TestCase {

	private static final String HTTP_RA_URL = "http://localhost:8080/mobicents/";

	public void testOnGet() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		GetMethod get = new GetMethod(HTTP_RA_URL);

		try {
			int iGetResultCode = client.executeMethod(get);

			assertEquals(iGetResultCode, HttpStatus.SC_OK);

			String strGetResponseBody = get.getResponseBodyAsString();
			System.out.println("Response Body for Get = " + strGetResponseBody);

		} finally {
			get.releaseConnection();
		}
	}

	public void testOnPost() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		PostMethod post = new PostMethod(HTTP_RA_URL);

		try {
			int iGetResultCode = client.executeMethod(post);

			assertEquals(iGetResultCode, HttpStatus.SC_OK);

			String strGetResponseBody = post.getResponseBodyAsString();
			System.out
					.println("Response Body for Post = " + strGetResponseBody);

		} finally {
			post.releaseConnection();
		}
	}

	public void testOnHead() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		HeadMethod head = new HeadMethod(HTTP_RA_URL);

		try {
			int iGetResultCode = client.executeMethod(head);

			assertEquals(iGetResultCode, HttpStatus.SC_OK);

		} finally {
			head.releaseConnection();
		}
	}

	public void testOnPut() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		PutMethod put = new PutMethod(HTTP_RA_URL);

		try {
			int iGetResultCode = client.executeMethod(put);

			assertEquals(iGetResultCode, HttpStatus.SC_OK);

			String strGetResponseBody = put.getResponseBodyAsString();
			System.out.println("Response Body for Put = " + strGetResponseBody);

		} finally {
			put.releaseConnection();
		}
	}

	public void testOnDelete() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		DeleteMethod delete = new DeleteMethod(HTTP_RA_URL);

		try {
			int iGetResultCode = client.executeMethod(delete);

			assertEquals(iGetResultCode, HttpStatus.SC_OK);

			String strGetResponseBody = delete.getResponseBodyAsString();
			System.out.println("Response Body for Delete = "
					+ strGetResponseBody);

		} finally {
			delete.releaseConnection();
		}
	}

	public void testOnOptions() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		OptionsMethod options = new OptionsMethod(HTTP_RA_URL);

		try {
			int iGetResultCode = client.executeMethod(options);

			assertEquals(iGetResultCode, HttpStatus.SC_OK);

			String strGetResponseBody = options.getResponseBodyAsString();
			System.out.println("Response Body for Options = "
					+ strGetResponseBody);

		} finally {
			options.releaseConnection();
		}
	}

	/**
	 * Note : By default allowTrace is set to false in Tomcat and hence this test will fail.
	 * Set allowTrace="true" in jboss-3.2.x/server/all/deploy/jbossweb-tomcat50.sar/server.xml
	 * For example
	 *       <Connector port="8080" address="${jboss.bind.address}"
     *    maxThreads="150" minSpareThreads="25" maxSpareThreads="75"
     *    enableLookups="false" redirectPort="8443" acceptCount="100"
     *    connectionTimeout="20000" disableUploadTimeout="true" allowTrace="true" />
	 */
/*	public void testOnTrace() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(5000);

		TraceMethod trace = new TraceMethod(HTTP_RA_URL);

		try {
			int iGetResultCode = client.executeMethod(trace);

			assertEquals(iGetResultCode, HttpStatus.SC_OK);

			String strGetResponseBody = trace.getResponseBodyAsString();
			System.out.println("Response Body for Options = "
					+ strGetResponseBody);

		} finally {
			trace.releaseConnection();
		}
	}*/	

}
