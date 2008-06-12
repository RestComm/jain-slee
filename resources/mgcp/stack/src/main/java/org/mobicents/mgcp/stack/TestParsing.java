/*
 * TestParsing.java
 *
 * Created on 8 Март 2007 г., 21:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.mgcp.stack;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.InetAddress;


/**
 *
 * @author 1
 */
public class TestParsing {
    
    /** Creates a new instance of TestParsing */
    public TestParsing() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JainMgcpStackImpl stack = new JainMgcpStackImpl(2728);
        InetAddress address = InetAddress.getLocalHost();
        
        FileInputStream in = new FileInputStream("d:\\projects\\createcon.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        int b = -1;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        
        String msg = new String(out.toByteArray());
        TransactionHandler handle = new CreateConnectionHandler(stack, address, 9201);
        
        System.out.println(handle.decodeCommand(msg));
    }
    
}
