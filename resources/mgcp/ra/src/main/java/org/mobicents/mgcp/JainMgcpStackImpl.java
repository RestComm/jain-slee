/*
 * File Name     : JainMgcpStackImpl.java
 *
 * The JAIN MGCP API implementaion.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.mgcp;

import jain.protocol.ip.mgcp.CreateProviderException;
import jain.protocol.ip.mgcp.DeleteProviderException;
import jain.protocol.ip.mgcp.JainMgcpProvider;
import jain.protocol.ip.mgcp.JainMgcpStack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JainMgcpStackImpl extends Thread implements JainMgcpStack {
    
    private String protocolVersion = "1.0";
    protected int port = 2727;
    
    private DatagramSocket socket;
    
    private boolean stopped = true;
    
    private Logger logger = Logger.getLogger(JainMgcpStackImpl.class);
    
    private ExecutorService pool;
    
    protected JainMgcpProviderImpl provider;
    
    /**
     * holds current active transactions (RFC 3435 [$3.2.1.2]: for tx sent &
     * received).
     * 
     */
    protected ConcurrentHashMap<Integer, TransactionHandler> sTransactions = new ConcurrentHashMap<Integer, TransactionHandler>();
    protected ConcurrentHashMap<ReceivedTransactionID, TransactionHandler> rTransactions = new ConcurrentHashMap<ReceivedTransactionID, TransactionHandler>();
    
    /** Creates a new instance of JainMgcpStackImpl */
    public JainMgcpStackImpl(JainMgcpProviderImpl provider, int port) {
    	this.port = port;
    	if (socket == null) {
    		while(true) {
    			try {
    				socket = new DatagramSocket(this.port);
    				logger.info("Jain Mgcp stack bound to local UDP port " + this.port);
    				break;
    			} catch (SocketException e) {
    				logger.error("Failed to bound to local port " + this.port + ". Caused by", e);
    				if (this.port != port+10) {
    					this.port++;
    				}
    				else {
    					throw new RuntimeException("Failed to find a local port to bound stack");
    				}
    			}
    		}
        }
    	stopped = false;
        if(logger.isDebugEnabled())
        	logger.debug("Starting main thread " + this);
        this.provider = provider;
        start();
    }
    
    /**
     * Closes the stack and it's underlying resources.
     */
    public void close() {
          stopped = true;
          try {
              logger.debug("Closing socket");
              socket.close();
              pool.shutdown();
          } catch (Exception e) {
              logger.warn("Could not gracefully close socket", e);
          }
    }
    
    public JainMgcpProvider createProvider() throws CreateProviderException {
       throw new CreateProviderException("this stack impl does not support creation of providers");
    }
    
    public void deleteProvider(JainMgcpProvider provider) throws DeleteProviderException {
      // do nothing
    }
    
    public void setPort(int port) {
        throw new UnsupportedOperationException("this stack impl doesn't support port reconfiguring");
    }
    
    public int getPort() {
        return port;
    }
    
    public String getProtocolVersion() {
        return protocolVersion;
    }
    
    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    
    protected synchronized void send(DatagramPacket packet) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Sending " + packet.getLength() + " bytes to " +
                        packet.getAddress());
            }
            socket.send(packet);
        } catch (IOException e) {
            logger.error("I/O Exception uccured, caused by", e);
        }
    }
    
    public boolean isRequest(String header) {
        return header.matches("[\\w]{4}(\\s|\\S)*");
    }
    
    public void run() {
    	
    	pool = Executors.newFixedThreadPool(5);
    	
        if (logger.isDebugEnabled()) {
            logger.debug("MGCP stack started successfully on " +
                    socket.getLocalAddress() + ":" + socket.getLocalPort());
        }
        
        byte[] buffer = new byte[86400];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        while (!stopped) {
            try {
                if (logger.isDebugEnabled()) {
                    logger.debug("Waiting for packet delivery");
                }
                socket.receive(packet);
            } catch (IOException e) {
                if (stopped) break;
                logger.error("I/O exception occured:", e);
                continue;
            }
            
            if (logger.isDebugEnabled()) {
                logger.debug("Receive " + packet.getLength() + " bytes from " +
                        packet.getAddress() + ":" + packet.getPort());
            }
            
            // uses now the actual data length from the DatagramPacket
            // instead of the length of the  byte[] buffer
            byte[] data = new byte[packet.getLength()];
            System.arraycopy(packet.getData(), 0, data, 0, data.length);
            
            MessageHandler handler = new MessageHandler(this, data,
                    packet.getAddress(), packet.getPort());
            
            pool.execute(handler);
            
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("MGCP stack stopped gracefully");
        }
    }
    
    
}
