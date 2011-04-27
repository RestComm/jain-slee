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
