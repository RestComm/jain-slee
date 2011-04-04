package org.mobicents.slee.resource.tftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import net.java.slee.resource.tftp.TransferActivity;

public class TransferActivityImpl implements TransferActivity {

	private static final long serialVersionUID = 1L;

	private String id_;
	private TFTPTransfer tt_;

	public TransferActivityImpl() {
		id_ = UUID.randomUUID().toString();
	}

	@Override
	public int hashCode() {
		return id_.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj.getClass()==this.getClass()) {
			return ((TransferActivityImpl)obj).id_.equals(this.id_);
		} else {
			return false;
		}
	}

	public TFTPTransfer getSource() {
		return tt_;
	}

	public void setSource(TFTPTransfer source) {
		tt_ = source;
	}

	public void sendError(int errorCode, String reason) {
		tt_.sendError(errorCode, reason);
	}

	public void receiveFile(String  filename) throws FileNotFoundException, IOException {
		tt_.receiveFile(filename);
	}

	public void receiveFile(File  file) throws FileNotFoundException, IOException {
		tt_.receiveFile(file);
	}

	public void sendFile(String  filename) throws FileNotFoundException, IOException {
		tt_.sendFile(filename);
	}

	public void sendFile(File  file) throws FileNotFoundException, IOException {
		tt_.sendFile(file);
	}

	public InputStream getInputStream() throws IOException {
		return tt_.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		return tt_.getOutputStream();
	}

	public String getTransferID() {
		return id_;
	}
}
