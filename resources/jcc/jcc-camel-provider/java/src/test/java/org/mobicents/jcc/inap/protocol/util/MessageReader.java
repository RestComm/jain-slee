/*
 */

package org.mobicents.jcc.inap.protocol.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Oleg Kulikov
 */
public class MessageReader {
    private ByteArrayOutputStream bout = new ByteArrayOutputStream();
    
    /** Creates a new instance of MessageReader */
    public MessageReader(String path) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(path)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] hexDigits = line.split(" ");
            for (int i = 0; i < hexDigits.length; i++) {
                bout.write(Integer.parseInt(hexDigits[i], 16));
            }
        }
    }
    
    public byte[] getData() {
        return bout.toByteArray();
    }
    
    public static void main(String[] args) throws Exception {
        MessageReader mr = new MessageReader("/org/itech/jcc/inap/protocol/data/begin-idp.txt");
        byte[] data = mr.getData();
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
    }
}
