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
public class CallingPartyNumber implements Serializable {
    
    private int nai; //nature of address indicator
    private int nii; //number incomplete indicator
    private int npi; //numbering plan indicator
    private int apri; //address presentation resdrict indicator
    private int si; //scrinning indicator
    
    private String address;
    
    /** Creates a new instance of CallingPartyNumber */
    public CallingPartyNumber(int nai, int nii, int npi, int apri, int si, String address) {
        this.nai = nai;
        this.nii = nii;
        this.npi = npi;
        this.apri = apri;
        this.si = si;
        this.address = address;
    }
    
    public CallingPartyNumber(byte[] bin) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bin);
        int length = bin.length;
        
        int b = in.read() & 0xff;
        
        int oddFlag = (b & 0x80) >> 7;
        nai = b & 0x7f;
        
        b = in.read() & 0xff;
        
        nii = (b & 0x80) >> 7;
        npi = (b & 0x70) >> 4;
        apri = (b & 0x0c) >> 2;
        si= b & 0x03;
        
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
    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getApri() {
        return apri;
    }

    public void setApri(int apri) {
        this.apri = apri;
    }
        
    public int getNai() {
        return nai;
    }

    public void setNai(int nai) {
        this.nai = nai;
    }
    
    public int getNii() {
        return nii;
    }

    public void setNii(int nii) {
        this.nii = nii;
    }
    
    public int getNpi() {
        return npi;
    }

    public void setNpi(int npi) {
        this.npi = npi;
    }
    
    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }
    
    public String toString() {
        return address;
    }
    
    public byte[] toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        
        boolean isOdd = (address.length() % 2 != 0);
        int b = nai;
        
        if (isOdd) {
            b |= 0x80;
        }
        
        out.write(b);
        
        int c = npi << 4;
        c |= (nii << 7);
        c |= (apri << 2);
        c |= (si);
        
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
}
