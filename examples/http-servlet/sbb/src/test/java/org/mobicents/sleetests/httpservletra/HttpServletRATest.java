package org.mobicents.sleetests.httpservletra;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;

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

}
