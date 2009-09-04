package net.java.client.slee.resource.http;

import java.io.IOException;

import javax.slee.resource.StartActivityException;

import net.java.client.slee.resource.http.event.Response;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.params.HttpClientParams;

/**
 * Provides SBB with the interface to interact with Http Client Resource
 * Adaptor. HttpClientResourceAdaptorSbbInterface is wrapper over
 * org.apache.commons.httpclient.HttpClient and exposes most commonly used
 * methods of HttpClient
 * 
 * @author abhayani
 * 
 */
public interface HttpClientResourceAdaptorSbbInterface {

	/**
	 * Method to create the HttpMethod that will used by service to send the
	 * Request
	 * 
	 * @param methodName
	 *       
	 * @param uri
	 *            The URI where the request has to be sent
	 * @return instance of HttpMethod
	 */
	public HttpMethod createHttpMethod(HttpMethodName methodName, String uri);

	/**
	 * Service that wants to send the Request synchronously uses this Method
	 * 
	 * @param method
	 *            instance of HttpMethod obtained by calling createHttpMethod()
	 * @return Response object containing either the response body or Exception
	 *         if something went wrong
	 * @throws HttpException 
	 * @throws IOException 
	 */
	public Response executeMethod(HttpMethod method) throws HttpException, IOException;

	/**
	 * @return Returns HTTP protocol parameters associated with this HttpClient.
	 */
	public HttpClientParams getParams();

	/**
	 * @return Returns HTTP state associated with the HttpClient.
	 */
	public HttpState getState();

	/**
	 * @param params
	 *            Assigns HTTP protocol parameters for this HttpClient.
	 */

	public void setParams(HttpClientParams params);

	/**
	 * @param state
	 *            Assigns HTTP state for the HttpClient.
	 */
	public void setState(HttpState state);

	/**
	 * Creates instance of HttpClientActivity for service that wants to send the
	 * Request asynchronously. The endOnReceivingResponse value is set to false
	 * by default and service has to explicitly end this Activity
	 * 
	 * @return instance of HttpClientActivity
	 * @throws StartActivityException 
	 */
	public HttpClientActivity createHttpClientActivity() throws StartActivityException;

	/**
	 * Creates instance of HttpClientActivity for service that wants to send the
	 * Request asynchronously
	 * 
	 * @param endOnReceivingResponse
	 *            if true Activity ends automatically as soon as the
	 *            ResponseEvent is sent by ResourceAdaptor. If false the service
	 *            has to explicitly end activity
	 * @return instance of HttpClientActivity
	 * @throws StartActivityException 
	 */
	public HttpClientActivity createHttpClientActivity(
			boolean endOnReceivingResponse) throws StartActivityException;

}
