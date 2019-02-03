package org.mobicents.slee.container.management.console.client.profiles;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.PropertiesInfo;
import org.mobicents.slee.container.management.console.client.components.info.ProfileSpecificationInfo;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ProfileService extends RemoteService {

	public void createProfileTable(String profileSpecId, String tableName) throws ManagementConsoleException;

	public void createProfile(String tableName, String profileName) throws ManagementConsoleException;

	public void createProfile(String tableName, String profileName, PropertiesInfo propertiesInfo)
			throws ManagementConsoleException;

	public String[] getProfileTables(String profileSpecificationId) throws ManagementConsoleException;

	public String[] getProfiles(String tableName) throws ManagementConsoleException;

	// public String[] getProfilesByIndexedAttribute(String arg0, String arg1,
	// Object arg2)throws ManagementConsoleException;

	public void removeProfile(String tableName, String profileName) throws ManagementConsoleException;

	public void removeProfileTable(String tableName) throws ManagementConsoleException;

	public void renameProfileTable(String currentTableName, String newTableName) throws ManagementConsoleException;


	public String[] getProfileSpecifications() throws ManagementConsoleException;

	public void restoreProfile(String tableName, String profileName) throws ManagementConsoleException;

	public PropertiesInfo getProfileAttributes(String tableName, String profileName) throws ManagementConsoleException;

	public void setProfileAttributes(String tableName, String profileName, PropertiesInfo propertiesInfo)
			throws ManagementConsoleException;

}
