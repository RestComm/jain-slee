
package org.mobicents.slee.resource.diameter.base.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;
import org.apache.log4j.Logger;
import org.jdiameter.client.impl.parser.MessageParser;


/**
 * 
 * Super project:  mobicents
 * 12:51:53 2008-05-08	
 * @author <a href="mailto:bbaranow@redhat.com">baranowb - Bartosz Baranowski </a>
 * @author Eric Svenson
 */
public class DiameterAvpImpl implements DiameterAvp {

    protected static final Logger log = Logger.getLogger(DiameterAvpImpl.class);

    protected long vendorId;
    protected int code, mnd, prt;
    protected String name = "undefined";
    //FIXME: baranowb; isnt this wrong?
    protected DiameterAvpType type = DiameterAvpType.DIAMETER_IDENTITY;
    protected MessageParser parser = new MessageParser(null);
    
    private byte[] value;

    public DiameterAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value, DiameterAvpType type) {
        this.code = code;
        this.vendorId = vendorId;
        this.mnd = mnd;
        this.prt = prt;
        this.value = value;
        this.type=type;
    }

    public int getCode() {
        return code;
    }

    public long getVendorID() {
        return vendorId;
    }

    public String getName() {
        return name;
    }

    public DiameterAvpType getType() {
        return type;
    }

    public int getMandatoryRule() {
        return mnd;
    }

    public int getProtectedRule() {
        return prt;
    }

    public double doubleValue() {
        try {
            return parser.bytesToDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public float floatValue() {
        try {
            return parser.bytesToFloat(value);
        } catch (Exception e) {
            log.debug(e);
            return 0;
        }
    }

    public int intValue() {
        try {
            return parser.bytesToInt(value);
        } catch (Exception e) {
            log.debug(e);
            return 0;
        }
    }

    public long longValue() {
        try {
            return  parser.bytesToLong(value);
        } catch (Exception e) {
            log.debug(e);
            return 0;
        }
    }

    public String stringValue() {
        try {
            return parser.bytesToUtf8String(value);
        } catch (Exception e) {
            log.debug(e);
            return null;
        }
    }

    public byte[] byteArrayValue() {
        return value;
    }

    public Object clone() {
        return new DiameterAvpImpl(code, vendorId, mnd, prt, value,type);
    }
        
    
}

