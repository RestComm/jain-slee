package org.mobicents.slee.resource.diameter.base.events.avp;


import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eric Svenson
 *
 */
public class GroupedAvpImpl extends DiameterAvpImpl implements GroupedAvp {

	protected AvpSet avpSet;

	public GroupedAvpImpl(int code, long vendorId, int mnd, int prt,
			byte[] value) {
		super(code, vendorId, mnd, prt, null, DiameterAvpType.GROUPED);
		//FIXME: baranowb; in my version - 1.5.1 this is protected!s
		//avpSet = parser.decodeAvpSet(value);

	}

	public void setExtensionAvps(DiameterAvp[] extensions) throws AvpNotAllowedException {
		try {
			for (DiameterAvp i : extensions)
				avpSet.addAvp(i.getCode(), i.byteArrayValue(), i.getVendorID(),
						i.getMandatoryRule() == 1, i.getProtectedRule() == 1);
		} catch (Exception e) {
			log.debug(e);
		}
	}

	public DiameterAvp[] getExtensionAvps() {
		List<DiameterAvp> acc = new ArrayList<DiameterAvp>();
		try {
			for (Avp a : avpSet) {
				acc.add(new DiameterAvpImpl(a.getCode(), a.getVendorId(), a
						.isMandatory() ? 1 : 0, a.isEncrypted() ? 1 : 0, a
						.getRaw(), null));
				//FIXME: baranowb; how can we determine type? dictionary?
			}
		} catch (Exception e) {
			log.debug(e);
		}
		return acc.toArray(new DiameterAvp[0]);
	}

	public double doubleValue() {
		throw new IllegalArgumentException();
	}

	public float floatValue() {
		throw new IllegalArgumentException();
	}

	public int intValue() {
		throw new IllegalArgumentException();
	}

	public long longValue() {
		throw new IllegalArgumentException();
	}

	public String stringValue() {
		throw new IllegalArgumentException();
	}

	public boolean hasExtensionAvps() {
		return getExtensionAvps().length > 0;
	}

	public byte[] byteArrayValue() {
		//FIXME: baranowb; in my version - 1.5.1 this is protected!s
		return null; //parser.encodeAvpSet(avpSet);
	}

	public Object clone() {
		return new GroupedAvpImpl(code, vendorId, mnd, prt, byteArrayValue());
	}

	// todo set/get Avp methods is duplicate @see DiameterMessageImpl

	protected void setAvpAsIdentity(int code, String value, boolean octet,
			boolean mandatory, boolean... remove) {
		if (remove.length == 0 || remove[0])
			avpSet.removeAvp(code);
		avpSet.addAvp(code, value, octet, mandatory, false);
	}

	protected DiameterIdentityAvp getAvpAsIdentity(int code) {
		try {
			Avp rawAvp = avpSet.getAvp(code);
			if (rawAvp != null) {
				int mndr = rawAvp.isMandatory() ? 1 : 0;
				// FIXME: baranowb; how to set prt here?
				return new DiameterIdentityAvpImpl(rawAvp.getCode(), rawAvp
						.getVendorId(), mndr, 1, rawAvp.getRaw());
			}
			return null;
		} catch (Exception e) {
			log.warn(e);
			return null;
		}
	}

	protected void setAvpAsByteArray(int code, byte[] value, boolean mandatory) {
		avpSet.addAvp(code, value, mandatory, false);
	}

	protected void setAvpAsUInt32(int code, long value, boolean mandatory,
			boolean... remove) {
		if (remove.length == 0 || remove[0])
			avpSet.removeAvp(code);
		avpSet.addAvp(code, (int) value, mandatory, false);
	}

	protected long getAvpAsUInt32(int code) {
		try {
			return avpSet.getAvp(code).getUnsigned32();
		} catch (Exception e) {
			log.warn(e);
			return 0;
		}
	}

	protected long[] getAllAvpAsUInt32(int code) {
		AvpSet all = avpSet.getAvps(code);
		long[] acc = new long[all.size()];
		for (int i = 0; i < acc.length; i++)
			try {
				acc[i] = all.getAvpByIndex(i).getUnsigned32();
			} catch (Exception e) {
				log.warn(e);
			}
		return acc;
	}

}
