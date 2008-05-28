package org.mobicents.slee.parlay.callblocking;

import javax.slee.Address;
import javax.slee.profile.AddressProfileCMP;

public interface CallBlockingAddressProfileCMP extends AddressProfileCMP {
    public void setBlockedAddresses(Address[] addresses);
    public Address[] getBlockedAddresses();
}
