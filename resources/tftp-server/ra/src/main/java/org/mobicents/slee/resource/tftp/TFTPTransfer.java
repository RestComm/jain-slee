/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.tftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.SocketTimeoutException;

import javax.slee.facilities.Tracer;

import net.java.slee.resource.tftp.TransferActivity;
import net.java.slee.resource.tftp.events.RequestEvent;

import org.apache.commons.net.io.FromNetASCIIOutputStream;
import org.apache.commons.net.io.ToNetASCIIInputStream;
import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPAckPacket;
import org.apache.commons.net.tftp.TFTPDataPacket;
import org.apache.commons.net.tftp.TFTPErrorPacket;
import org.apache.commons.net.tftp.TFTPPacket;
import org.apache.commons.net.tftp.TFTPReadRequestPacket;
import org.apache.commons.net.tftp.TFTPWriteRequestPacket;
import org.mobicents.slee.resource.tftp.TftpServerResourceAdaptor.ServerMode;
import org.mobicents.slee.resource.tftp.events.RequestEventImpl;

/**
 * Handle an actual tftp transfer (server side).
 * Copied and modified from Apache Commons Net (http://commons.apache.org/net).
 * <P>
 * Original code by Dan Ambrust.
 *
 * @author tuijldert
 */
public class TFTPTransfer implements Runnable {
	private transient Tracer	trc;

	private volatile boolean threadSuspended = true;

	protected void suspend(long timeout) {
		if (threadSuspended) {
			try {
				synchronized(this) {
					while (threadSuspended)
						wait(timeout);
				}
			} catch (InterruptedException e) {}
		}
	}

	protected synchronized void resume() {
		threadSuspended = !threadSuspended;
		if (!threadSuspended)
			notify();
	}

	private TftpServerResourceAdaptor ra_;
    private ServerMode			mode_;
    private int 				maxTimeoutRetries_;
    private int 				socketTimeout_;
    private boolean				shutdownTransfer = false;

    private TFTPPacket			tftpPacket_;
    private TransferActivity	activity_;
    private TFTP				transferTftp_ = null;
    private InputStream			sbbIs, is_;
    private OutputStream		sbbOs, os_;

    public TFTPTransfer(TFTPPacket tftpPacket, TransferActivity activity,
    		ServerMode mode, int maxTimeoutRetries, int socketTimeout,
    		TftpServerResourceAdaptor ra, Tracer trc) {
        tftpPacket_ = tftpPacket;
        activity_ = activity;
        mode_ = mode;
        maxTimeoutRetries_ = maxTimeoutRetries;
        socketTimeout_ = socketTimeout;
        ra_ = ra;
        this.trc = trc;
    }

    public void shutdown() {
    	if (trc.isFineEnabled())
    		trc.fine("Transfer ended for thread: " + Thread.currentThread().getName());
    	
        shutdownTransfer = true;
        try {
            transferTftp_.close();
        } catch (RuntimeException e) {
            // noop
        }
        resume();
    }

    public boolean isRunning() {
    	return !shutdownTransfer;
    }

    public void run() {
        try {
        	if (trc.isFineEnabled())
        		trc.fine("Transfer started in thread: " + Thread.currentThread().getName());

        	transferTftp_ = new TFTP();

            transferTftp_.beginBufferedOps();
            transferTftp_.setDefaultTimeout(socketTimeout_);

            transferTftp_.open();

            if (isRead()) {
                handleRead(((TFTPReadRequestPacket) tftpPacket_));
            } else if (isWrite()) {
                handleWrite((TFTPWriteRequestPacket) tftpPacket_);
            } else {
                trc.warning("Unsupported TFTP request (" + tftpPacket_ + ") - ignored.");
            }
        } catch (Exception e) {
            if (!shutdownTransfer) {
            	trc.severe("Unexpected Error in during TFTP file transfer.  Transfer aborted. "
                            + e);
            }
        } finally {
            try {
                if (transferTftp_ != null && transferTftp_.isOpen()) {
                    transferTftp_.endBufferedOps();
                    transferTftp_.close();
                }
            } catch (Exception e) {
                // noop
            }
            ra_.endTransferRequestActivity(activity_);
        }
    }

	protected boolean isRead() {
		return tftpPacket_ instanceof TFTPReadRequestPacket;
	}

	protected boolean isWrite() {
		return tftpPacket_ instanceof TFTPWriteRequestPacket;
	}

	public void sendError(int errorCode, String reason) {
        try {
        	if (transferTftp_ != null && transferTftp_.isOpen()) {
            	if (trc.isFineEnabled())
            		trc.fine(String.format("Send error - code[%d] reason[%s]", errorCode, reason));

                transferTftp_.bufferedSend(new TFTPErrorPacket(tftpPacket_.getAddress(),
                								tftpPacket_.getPort(), errorCode, reason));
                transferTftp_.endBufferedOps();
            }
        } catch (Exception e) { }
        shutdown();
    }

    /*
     * Handle a tftp read request.
     */
    private void handleRead(TFTPReadRequestPacket trrp) throws Exception {
    	long totalBytesSent = 0;
        try {
            if (mode_ == ServerMode.PUT_ONLY) {
                transferTftp_.bufferedSend(new TFTPErrorPacket(trrp.getAddress(),
                		trrp.getPort(), TFTPErrorPacket.ILLEGAL_OPERATION,
                        "Read not allowed by server."));
                return;
            }
            if (trc.isFineEnabled())
            	trc.fine("READ request received, get cracking");
        	fireEvent(trrp);
            suspend(0);					// TODO: do we really need to wait forever?

            if (trrp.getMode() == TFTP.NETASCII_MODE) {
                is_ = new ToNetASCIIInputStream(is_);
            }
            byte[] temp = new byte[TFTPDataPacket.MAX_DATA_LENGTH];
            TFTPPacket answer;
            int block = 1;
            boolean sendNext = true;
            int readLength = TFTPDataPacket.MAX_DATA_LENGTH;
            TFTPDataPacket lastSentData = null;

            // We are reading a file, so when we read less than the
            // requested bytes, we know that we are at the end of the file.
            while (readLength == TFTPDataPacket.MAX_DATA_LENGTH && !shutdownTransfer) {
                if (sendNext) {
                    readLength = is_.read(temp);
                    if (readLength == -1) {
                        readLength = 0;
                    }
                    lastSentData = new TFTPDataPacket(trrp.getAddress(), trrp.getPort(), block,
                            temp, 0, readLength);
                    transferTftp_.bufferedSend(lastSentData);
                    totalBytesSent += readLength;
                }
                answer = null;
                int timeoutCount = 0;
                while (!shutdownTransfer
                        && (answer == null || !answer.getAddress().equals(trrp.getAddress()) || answer
                                .getPort() != trrp.getPort())) {
                    // listen for an answer.
                    if (answer != null) {
                        // The answer that we got didn't come from the
                        // expected source, fire back an error, and continue
                        // listening.
                        trc.warning("TFTP Server ignoring message from unexpected source.");
                        transferTftp_.bufferedSend(new TFTPErrorPacket(answer.getAddress(),
                                answer.getPort(), TFTPErrorPacket.UNKNOWN_TID,
                                "Unexpected Host or Port"));
                    }
                    try {
                        answer = transferTftp_.bufferedReceive();
                    } catch (SocketTimeoutException e) {
                        if (timeoutCount >= maxTimeoutRetries_) {
                            throw e;
                        }
                        // didn't get an ack for this data. need to resend
                        // it.
                        timeoutCount++;
                        transferTftp_.bufferedSend(lastSentData);
                        continue;
                    }
                }
                if (answer == null || !(answer instanceof TFTPAckPacket)) {
                    if (!shutdownTransfer) {
                    	trc.severe("Unexpected response from tftp client during transfer ("
                                        + answer + ").  Transfer aborted.");
                    }
                    break;
                } else {
                    // once we get here, we know we have an answer packet
                    // from the correct host.
                    TFTPAckPacket ack = (TFTPAckPacket) answer;
                    if (ack.getBlockNumber() != block) {
                        /*
                         * The origional tftp spec would have called on us to resend the
                         * previous data here, however, that causes the SAS Syndrome.
                         * http://www.faqs.org/rfcs/rfc1123.html section 4.2.3.1 The modified
                         * spec says that we ignore a duplicate ack. If the packet was really
                         * lost, we will time out on receive, and resend the previous data at
                         * that point.
                         */
                        sendNext = false;
                    } else {
                        // send the next block
                        block++;
                        if (block > 65535) {
                            // wrap the block number
                            block = 0;
                        }
                        sendNext = true;
                    }
                }
            }
        } finally {
        	if (trc.isFineEnabled())
        		trc.fine("Bytes sent = " + totalBytesSent);
            try {
                if (is_ != null) is_.close();
                if (sbbOs != null) sbbOs.close();
            } catch (IOException e) {
                // noop
            }
        }
    }

	private void fireEvent(TFTPPacket packet) throws Exception {
		RequestEvent event = new RequestEventImpl(packet, this);
		try {
			ra_.fireEvent(event, activity_, packet.getAddress().toString());
		} catch (Exception e) {
		    transferTftp_.bufferedSend(new TFTPErrorPacket(packet.getAddress(), packet
		            .getPort(), TFTPErrorPacket.UNDEFINED, e.getMessage()));
		    throw e;
		}
	}

    /*
     * handle a tftp write request.
     */
    private void handleWrite(TFTPWriteRequestPacket twrp) throws Exception {
    	long totalBytesReceived = 0;
        try {
            if (mode_ == ServerMode.GET_ONLY) {
                transferTftp_.bufferedSend(new TFTPErrorPacket(twrp.getAddress(), twrp
                        .getPort(), TFTPErrorPacket.ILLEGAL_OPERATION,
                        "Write not allowed by server."));
                return;
            }
            if (trc.isFineEnabled())
            	trc.fine("WRITE request received, get cracking");
            fireEvent(twrp);
            suspend(0);

            if (twrp.getMode() == TFTP.NETASCII_MODE) {
                os_ = new FromNetASCIIOutputStream(os_);
            }
            TFTPAckPacket lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), 0);
            transferTftp_.bufferedSend(lastSentAck);

            int lastBlock = 0;
            while (true) {
                // get the response - ensure it is from the right place.
                TFTPPacket dataPacket = null;
                int timeoutCount = 0;

                while (!shutdownTransfer &&
                			(dataPacket == null ||
        					!dataPacket.getAddress().equals(twrp.getAddress()) ||
        					dataPacket.getPort() != twrp.getPort())) {
                    // listen for an answer.
                    if (dataPacket != null) {
                        // The data that we got didn't come from the
                        // expected source, fire back an error, and continue
                        // listening.
                    	trc.warning("TFTP Server ignoring message from unexpected source.");
                        transferTftp_.bufferedSend(new TFTPErrorPacket(dataPacket.getAddress(),
                                dataPacket.getPort(), TFTPErrorPacket.UNKNOWN_TID,
                                "Unexpected Host or Port"));
                    }
                    try {
                        dataPacket = transferTftp_.bufferedReceive();
                    } catch (SocketTimeoutException e) {
                        if (timeoutCount >= maxTimeoutRetries_) {
                            throw e;
                        }
                        // It didn't get our ack. Resend it.
                        transferTftp_.bufferedSend(lastSentAck);
                        timeoutCount++;
                        continue;
                    }
                }
                if (dataPacket != null && dataPacket instanceof TFTPWriteRequestPacket) {
                    // it must have missed our initial ack. Send another.
                    lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), 0);
                    transferTftp_.bufferedSend(lastSentAck);
                } else if (dataPacket == null || !(dataPacket instanceof TFTPDataPacket)) {
                    if (!shutdownTransfer) {
                    	trc.severe("Unexpected response from tftp client during transfer ("
                                        + dataPacket + ").  Transfer aborted.");
                    }
                    break;
                } else {
                    int block = ((TFTPDataPacket) dataPacket).getBlockNumber();
                    byte[] data = ((TFTPDataPacket) dataPacket).getData();
                    int dataLength = ((TFTPDataPacket) dataPacket).getDataLength();
                    int dataOffset = ((TFTPDataPacket) dataPacket).getDataOffset();

                    if (block > lastBlock || (lastBlock == 65535 && block == 0)) {
                        // it might resend a data block if it missed our ack
                        // - don't rewrite the block.
                        os_.write(data, dataOffset, dataLength);
                        lastBlock = block;
                        fireEvent(dataPacket);
                        totalBytesReceived += dataLength;
                    }
                    lastSentAck = new TFTPAckPacket(twrp.getAddress(), twrp.getPort(), block);
                    transferTftp_.bufferedSend(lastSentAck);
                    if (dataLength < TFTPDataPacket.MAX_DATA_LENGTH) {
                        // end of stream signal - The tranfer is complete.
                    	if (trc.isFineEnabled())
                    		trc.fine("Bytes received = " + totalBytesReceived);
                        os_.close();

                        // But my ack may be lost - so listen to see if I
                        // need to resend the ack.
                        for (int i = 0; i < maxTimeoutRetries_; i++) {
                            try {
                                dataPacket = transferTftp_.bufferedReceive();
                            } catch (SocketTimeoutException e) {
                                // this is the expected route - the client
                                // shouldn't be sending any more packets.
                                break;
                            }
                            if (dataPacket != null &&
                            		(!dataPacket.getAddress().equals(twrp.getAddress()) ||
                            				dataPacket.getPort() != twrp.getPort())) {
                                // make sure it was from the right client...
                                transferTftp_.bufferedSend(new TFTPErrorPacket(dataPacket
                                                .getAddress(), dataPacket.getPort(),
                                                TFTPErrorPacket.UNKNOWN_TID,
                                                "Unexpected Host or Port"));
                            } else {
                                // This means they sent us the last
                                // datapacket again, must have missed our
                                // ack. resend it.
                                transferTftp_.bufferedSend(lastSentAck);
                            }
                        }
                        // all done.
                        break;
                    }
                }
            }
        } finally {
            if (sbbIs != null) sbbIs.close();
            if (os_ != null) os_.close();
        }
    }

    /* Routines implementing the transfer activity. */

    protected void receiveFile(String filename) throws FileNotFoundException, IOException {
		receiveFile(new File(filename));
	}

	protected void receiveFile(File file) throws FileNotFoundException, IOException {
		if (!isWrite())
			throw new IOException("No write request pending");
		if (os_ == null) {
	    	os_ = new BufferedOutputStream(new FileOutputStream(file));
			resume();
		}
	}

	protected InputStream getInputStream() throws IOException {
		if (!isWrite())
			throw new IOException("No write request pending");
		if (sbbIs == null && os_ == null) {
        	os_ = new PipedOutputStream();
        	sbbIs = new PipedInputStream((PipedOutputStream) os_);
			resume();
		}
		return sbbIs;
	}

	protected void sendFile(String filename) throws FileNotFoundException, IOException {
		sendFile(new File(filename));
	}

	protected void sendFile(File file) throws FileNotFoundException, IOException {
		if (!isRead())
			throw new IOException("No read request pending");
		if (is_ == null) {
	        is_ = new BufferedInputStream(new FileInputStream(file));
			resume();
		}
	}

	protected OutputStream getOutputStream() throws IOException {
		if (!isRead())
			throw new IOException("No read request pending");
		if (sbbOs == null && is_ == null) {
            sbbOs = new PipedOutputStream();
        	is_ = new PipedInputStream((PipedOutputStream) sbbOs);
			resume();
		}
		return sbbOs;
	}
}
