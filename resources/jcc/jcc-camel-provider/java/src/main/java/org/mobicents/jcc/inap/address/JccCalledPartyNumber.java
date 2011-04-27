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

package org.mobicents.jcc.inap.address;



import javax.csapi.cc.jcc.JccAddress;
import javax.csapi.cc.jcc.JccProvider;
import org.mobicents.jcc.inap.JccInapProviderImpl;

import org.mobicents.jcc.inap.protocol.parms.CalledPartyNumber;
import org.mobicents.jcc.inap.protocol.parms.NoA;

/**
 * JCC style address for Called Party Number.
 *
 * @author Oleg Kulikov
 */
public class JccCalledPartyNumber implements JccAddress {

    private JccInapProviderImpl provider = null;
    private int type = JccAddress.E164_MOBILE;
    
    private CalledPartyNumber localNumber;
    
    
    /** Creates a new instance of JccCallImpl */
    public JccCalledPartyNumber(JccInapProviderImpl provider, CalledPartyNumber cpn) {
        this.provider = provider;
        localNumber = cpn;
    }    

    public JccCalledPartyNumber(JccInapProviderImpl provider, String address) {
        this.provider = provider;
        localNumber = new CalledPartyNumber(CalledPartyNumber.NATIONAL, 0,1, address);
    }    
        
    public void setName(String digits) {
        String[] tokens = digits.split(",");
        localNumber.setAddress(tokens[0]);
        
        for (int i = 1; i < tokens.length; i++) {
            String s = tokens[i].toLowerCase();
            if (s.startsWith("noa")) {
                int ind = NoA.parse(s);
                localNumber.setNai(ind);
            }
        }
    }
    
    public String getName() {
        return localNumber.getAddress() + ",NoA=" + NoA.toString(localNumber.getNai());
    }

    public JccProvider getProvider() {
        return provider;
    }

    public int getType() {
        return type;
    }
    
    public CalledPartyNumber getRouteAddress() {
        return localNumber;
    }
        
    @Override
    public String toString() {
        return "E164_MOBILE:" + localNumber.getAddress();
    }
}
