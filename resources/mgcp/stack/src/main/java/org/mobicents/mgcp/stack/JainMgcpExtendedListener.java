package org.mobicents.mgcp.stack;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;
import jain.protocol.ip.mgcp.JainMgcpListener;

public interface JainMgcpExtendedListener extends JainMgcpListener {

	
	void transactionRxTimedOut(JainMgcpCommandEvent command);
	void transactionTxTimedOut(JainMgcpCommandEvent command);
	void transactionEnded(int handle);
	
	
}
