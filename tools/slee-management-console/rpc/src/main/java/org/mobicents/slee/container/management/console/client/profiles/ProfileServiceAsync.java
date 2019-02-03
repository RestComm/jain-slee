package org.mobicents.slee.container.management.console.client.profiles;

import org.mobicents.slee.container.management.console.client.PropertiesInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {

	public void createProfileTable(String profileSpecId, String tableName, AsyncCallback callback);

	public void createProfile(String tableName, String profileName, AsyncCallback callback);

	public void createProfile(String tableName, String profileName, PropertiesInfo propertiesInfo,
			AsyncCallback callback);

	public void getProfileTables(String profileSpecificationId, AsyncCallback callback);

	public void getProfiles(String tableName, AsyncCallback callback);

	// public void getProfilesByIndexedAttribute(String arg0, String arg1,
	// Object arg2, AsyncCallback callback);

	public void removeProfile(String tableName, String profileName, AsyncCallback callback);

	public void removeProfileTable(String tableName, AsyncCallback callback);

	public void renameProfileTable(String currentTableName, String newTableName, AsyncCallback callback);

	public void getProfileSpecifications(AsyncCallback callback);

	public void restoreProfile(String tableName, String profileName, AsyncCallback callback);

	public void getProfileAttributes(String tableName, String profileName, AsyncCallback callback);

	public void setProfileAttributes(String tableName, String profileName, PropertiesInfo propertiesInfo,
			AsyncCallback callback);

}
