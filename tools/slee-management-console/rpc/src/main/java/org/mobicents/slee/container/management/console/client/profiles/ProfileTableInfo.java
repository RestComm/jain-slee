package org.mobicents.slee.container.management.console.client.profiles;


import com.google.gwt.user.client.rpc.IsSerializable;

public class ProfileTableInfo implements IsSerializable{
	private String tableName;
	private String profileSpecName;
	private String profileSpecVendor;
	private String profileSpecVersion;
	
	public ProfileTableInfo() {
		super();
	}

	public ProfileTableInfo(String tableName, String profileSpecName, String profileSpecVendor,
			String profileSpecVersion) {
		super();
		this.tableName = tableName;
		this.profileSpecName = profileSpecName;
		this.profileSpecVendor = profileSpecVendor;
		this.profileSpecVersion = profileSpecVersion;
	}

	public String getTableName() {
		return tableName;
	}

	public String getProfileSpecName() {
		return profileSpecName;
	}

	public String getProfileSpecVendor() {
		return profileSpecVendor;
	}

	public String getProfileSpecVersion() {
		return profileSpecVersion;
	}

	@Override
	public String toString() {
		return "ProfileTableInfo [tableName=" + tableName + ", profileSpecName=" + profileSpecName
				+ ", profileSpecVendor=" + profileSpecVendor + ", profileSpecVersion=" + profileSpecVersion + "]";
	}
	
}
