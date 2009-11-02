package org.mobicents.sleetests.httpservletra;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpServletRAGetLoadTest extends TestCase {

	private static final String HTTP_RA_URL = "http://localhost:8080/mobicents/";
	
	private AtomicLong okCounter = new AtomicLong(0);
	private AtomicLong nOkCounter = new AtomicLong(0);
	
	private static Timer timer = new Timer();
	
	public void testGetLoadWitoutHttpSession() throws Exception {
				
		final int executorsSize = 200;
		final int requestsPerExecutor = 5000;
		
		Runnable task = new Runnable() {
			public void run() {
				try {
					HttpClient client = new HttpClient();
					client.getHttpConnectionManager().getParams()
							.setConnectionTimeout(5000);
					for (int i = 0; i < requestsPerExecutor; i++) {
					
						GetMethod get = new GetMethod(HTTP_RA_URL);
						try {
							if (client.executeMethod(get) == HttpStatus.SC_OK) {
								okCounter.incrementAndGet();
							}
							else {
								nOkCounter.incrementAndGet();
							}
						} finally {
							get.releaseConnection();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		ExecutorService[] executors = new ExecutorService[executorsSize];
		for (int i=0;i<executorsSize;i++) {
			executors[i] = Executors.newSingleThreadExecutor();
		}
		
		final long startTime = System.currentTimeMillis();
		
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				long totalTestTime = System.currentTimeMillis()-startTime;
				System.out.println("\n-- TEST RESULTS after "+totalTestTime/1000+" seconds");
				System.out.println("-- OK requests: "+okCounter.get());
				System.out.println("-- Not OK requests: "+nOkCounter.get());
				long totalRequestsSent = okCounter.get()+nOkCounter.get();
				System.out.println("-- Total requests: "+totalRequestsSent);
				System.out.println("-- Requests per second: "+(totalTestTime == 0 ? "n/a" : ((totalRequestsSent*1000)/totalTestTime)));
				System.out.println("-- Average request time in ms: "+(totalRequestsSent == 0 ? "n/a" : totalTestTime/totalRequestsSent));
				System.out.println("--------------------------------------------------------------\n");
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, 60*1000);
		
		for (ExecutorService executor:executors) {
			executor.submit(task);
		}
		for (ExecutorService executor:executors) {
			executor.shutdown();
		}
		for (ExecutorService executor:executors) {
			executor.awaitTermination(60*60*12, TimeUnit.SECONDS);
		}
		
		timerTask.cancel();
		
		System.out.println("\n-- FINAL TEST RESULTS");
		System.out.println("-- Executor threads: "+executorsSize);
		System.out.println("-- OK requests: "+okCounter.get());
		System.out.println("-- Not OK requests: "+nOkCounter.get());
		long totalRequestsSent = okCounter.get()+nOkCounter.get();
		System.out.println("-- Total requests: "+totalRequestsSent);
		long totalTestTime = System.currentTimeMillis()-startTime;
		System.out.println("-- Test time in seconds: "+(totalRequestsSent == 0 ? "n/a" : totalTestTime/1000));
		System.out.println("-- Requests per second: "+(totalTestTime == 0 ? "n/a" : ((totalRequestsSent*1000)/totalTestTime)));
		System.out.println("-- Average request time in ms: "+(totalRequestsSent == 0 ? "n/a" : totalTestTime/totalRequestsSent));
		System.out.println("--------------------------------------------------------------\n");
	}
}
