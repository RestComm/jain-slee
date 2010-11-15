/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.jcc.inap;

import java.util.Properties;
import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author kulikov
 */
public class SccpAddressDesc {
    public SccpAddress load(Properties p) {
        String pcs = p.getProperty("sccp.pointcode");
        if (pcs != null) {
            int pc = Integer.parseInt(pcs);
            int ssn = Integer.parseInt(p.getProperty("sccp.ssn"));
            return new SccpAddress(pc,ssn);
        }
        
        int ssn = 0;
        if (p.getProperty("sccp.ssn") != null) {
            ssn = Integer.parseInt(p.getProperty("sccp.ssn"));
        }
        
        //construct global title
        NatureOfAddress noa = null;
        if (p.getProperty("sccp.noa") != null) {
            noa = NatureOfAddress.valueOf(p.getProperty("sccp.noa"));
        }
        
        NumberingPlan np = null;
        if (p.getProperty("sccp.np") != null) {
            np = NumberingPlan.valueOf(p.getProperty("sccp.np"));
        }
        
        int tt = -1;
        if (p.getProperty("sccp.tt") != null) {
            tt = Integer.parseInt(p.getProperty("sccp.tt"));
        }
        
        String digits = p.getProperty("sccp.gt");
        if (noa != null && np != null & tt != -1) {
            GlobalTitle gt = GlobalTitle.getInstance(tt, np, noa, digits);
            return new SccpAddress(gt, ssn);
        } else if (noa != null && np == null && tt == -1) {
            GlobalTitle gt = GlobalTitle.getInstance(noa, digits);
            return new SccpAddress(gt, ssn);
        } else if (noa == null && np != null & tt != -1) {
            GlobalTitle gt = GlobalTitle.getInstance(tt, np, digits);
            return new SccpAddress(gt, ssn);
        }
        return null;
    }
}
