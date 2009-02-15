package org.mobicents.slee.container.component.validator.profile.tableinterface;

import javax.slee.Address;

public interface ProfileCMPInterfaceAddress {

	public void setSimpleAddressCMP(Address b);

	public void setWrapperBooleanCMP(Boolean b);

	public Address getSimpleAddressCMP();

	public Boolean getWrapperBooleanCMP();

	public void setSimpleStringCMP(String t);

	public String getSimpleStringCMP();

	public void setSimpleIntegerCMP(int b);

	public void setWrapperIntegerCMP(Integer b);

	public int getSimpleIntegerCMP();

	public Integer getWrapperIntegerCMP();

}
