package org.mobicents.slee.resource.diameter.base.events.avp;

import org.jdiameter.api.Avp;
import org.jdiameter.api.AvpDataException;

import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;

public class ExperimentalResultAvpImpl extends GroupedAvpImpl implements
		ExperimentalResultAvp {

	public ExperimentalResultAvpImpl(int code, long vendorId, int mnd, int prt,
			byte[] value) {
		super(code, vendorId, mnd, prt, value);
		
	}

	public long getExperimentalResultCode() {

		
		if (!hasExperimentalResultCode())
			return 0;
		else
			try {
				Avp rawAvp = super.avpSet.getAvp(Avp.EXPERIMENTAL_RESULT_CODE);
				return rawAvp.getUnsigned32();
			} catch (AvpDataException e) {
	
				e.printStackTrace();
				return -1;
			}
	}

	public boolean hasExperimentalResultCode() {
		Avp rawAvp = super.avpSet.getAvp(Avp.EXPERIMENTAL_RESULT_CODE);
		return rawAvp!=null;
		
	}

	public boolean hasVendorId() {
		Avp rawAvp = super.avpSet.getAvp(Avp.VENDOR_ID);
		return rawAvp!=null;
	}

	public void setExperimentalResultCode(long experimentalResultCode) {

		if(hasExperimentalResultCode())
			throw new IllegalStateException("Cant set result code again!!!!");
		super.setAvpAsUInt32(Avp.EXPERIMENTAL_RESULT_CODE, experimentalResultCode, true,true);

	}

	public void setVendorId(long vendorId) {
		if(hasExperimentalResultCode())
			throw new IllegalStateException("Cant set vendor Id again!!!!");
		super.setAvpAsUInt32(Avp.VENDOR_ID, vendorId, true, true);

	}

}
