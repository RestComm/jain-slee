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

package net.java.slee.resource.tftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a tftp transfer request being handled.
 */
public interface TransferActivity {

	/**
	 * Retrieves the Request's ID.
	 */
	public String getTransferID();

	/**
	 * Get the stream containing the data of the tftp write request.
	 * 
	 * This stream can be read to retrieve the contents of the actual transferred
	 * data.
	 * @return	The stream to read from
	 * @throws IOException	when there is no write request pending.
	 */
	public InputStream getInputStream() throws IOException;

	/**
	 * Get the stream where you can write data to the tftp read request.
	 * 
	 * You can write data to this stream that will be transferred to the client
	 * using tftp.
	 * @return	The stream to write to
	 * @throws IOException	when there is no read request pending.
	 */
	public OutputStream getOutputStream() throws IOException;

	/**
	 * Handle the tftp write request by storing received data in the given file.
	 * A file creation attempt will be done and any received data is stored in it.
	 * 
	 * @param filename	name of the file to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void receiveFile(String filename) throws FileNotFoundException, IOException;

	/**
	 * Handle the tftp write request by storing received data in the given file.
	 * A file creation attempt will be done and any received data is stored in it.
	 * 
	 * @param file		File descriptor to write to.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void receiveFile(File file) throws FileNotFoundException, IOException;

	/**
	 * Handle the tftp read request by transferring content from the given file.
	 * The file will be opened for reading and the content transferred using tftp.
	 * 
	 * @param filename	name of the file to read from.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void sendFile(String filename) throws FileNotFoundException, IOException;

	/**
	 * Handle the tftp read request by transferring content from the given file.
	 * The file will be opened for reading and the content transferred using tftp.
	 * 
	 * @param file		File descriptor to read from.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void sendFile(File file) throws FileNotFoundException, IOException;

	/**
	 * Send the given error to the client and end the transfer.
	 * @param errorCode	the tftp error code ({@link org.apache.commons.net.tftp.TFTPErrorPacket})
	 * @param reason	reason for this error
	 */
	public void sendError(int errorCode, String reason);
}