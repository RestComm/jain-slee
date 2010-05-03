/*
 * The Java Call Control API for CAMEL 2
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

package org.mobicents.jcc.inap.protocol.parms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public class CalledPartyNumber implements Serializable {
    public final static int SPARE = 0;
    public final static int SUBSCRIBER = 1;
    public final static int UNKNOWN = 2;
    public final static int NATIONAL = 3;
    public final static int INTERNATIONAL = 4;
    
    private String address;

    private int nai = 4; //Nature of address indicator
    private int inni = 0; //internal network numbering indicator
    private int npi = 1; //numbering plan indicator
    
    public CalledPartyNumber(int nai, int inni, int npi, String address) {
        this.nai = nai;
        this.inni = inni;
        this.npi = npi;
        this.address = address;
    }
    
    public CalledPartyNumber(String address) {
        this.address = address;
        this.nai = NATIONAL;
    }
    
    /** Creates a new instance of CalledPartyNumber */
    public CalledPartyNumber(byte[] bin) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bin);
        int length = bin.length;
        
        int b = in.read() & 0xff;
        
        int oddFlag = (b & 0x80) >> 7;
        nai = b & 0x7f;
        
        b = in.read() & 0xff;
        
        inni = (b & 0x80) >> 7;
        npi = (b & 0x70) >> 4;
        
        length -= 2;
        address = "";
        
        while (length - 1 > 0) {
            b = in.read() & 0xff;
            
            int d1 = b & 0x0f;
            int d2 = (b & 0xf0) >> 4;
            
            address += Integer.toHexString(d1) + Integer.toHexString(d2);
            
            length--;
        }
        
        b = in.read() & 0xff;
        address += Integer.toHexString((b & 0x0f));
        
        if (oddFlag != 1) {
            address += Integer.toHexString((b & 0xf0) >> 4);
        }
    }
 
    public int getNai() {
        return nai;
    }
    
    public void setNai(int nai) {
        this.nai = nai;
    }
    
    public int getInni() {
        return inni;
    }
    
    public int getNpi() {
        return npi;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
                
        boolean isOdd = (address.length() % 2 != 0);
        int b = nai;
        
        if (isOdd) b |= 0x80;
        out.write(b);
        
        int c = npi << 4;
        c |= (inni << 7);
        out.write(c);

        int count = (!isOdd) ? address.length() : address.length() - 1;
        
        for (int i = 0; i < count - 1; i += 2) {
            String ds1 = address.substring(i, i + 1);
            String ds2 = address.substring(i + 1, i + 2);
            
            int d1 = Integer.parseInt(ds1,16);
            int d2 = Integer.parseInt(ds2, 16);
            
            b = (byte) (d2 << 4 | d1);
            out.write(b);
        }
        
        if (isOdd) {
            String ds1 = address.substring(count, count + 1);
            int d = Integer.parseInt(ds1);
            
            b = (byte)(d & 0x0f);
            out.write(b);
        }
        
        return out.toByteArray();
        
    }
    
    public String toString() {
        return nai + ":" + address;
    }
    
}