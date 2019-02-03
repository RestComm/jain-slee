package org.mobicents.slee.container.management.console.server.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.management.Attribute;
import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.PropertiesInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.profiles.ProfileService;
import org.mobicents.slee.container.management.console.client.profiles.ProfileTableInfo;
import org.mobicents.slee.container.management.console.server.Logger;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.components.ComponentInfoUtils;
import org.mobicents.slee.container.management.console.server.mbeans.DeploymentMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.ProfileMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.ProfileProvisioningMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {

	private ManagementConsole managementConsole = ManagementConsole.getInstance();

	private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

	private ProfileProvisioningMBeanUtils profileProvisioningMBean = sleeConnection.getSleeManagementMBeanUtils()
			.getProfileProvisioningMBeanUtils();

	private DeploymentMBeanUtils deploymentMBean = sleeConnection.getSleeManagementMBeanUtils()
			.getDeploymentMBeanUtils();

	private ProfileMBeanUtils profileMBean = sleeConnection.getSleeManagementMBeanUtils().getProfileMBeanUtils();

	public static final String ARRAY_SEPARATOR = ",";

	public static final String NAME_TYPE_SEPARATOR = " ::: ";

	public void createProfileTable(String profileSpecId, String tableName) throws ManagementConsoleException {
		ProfileSpecificationID profileSpecificationID = (ProfileSpecificationID) managementConsole.getComponentIDMap()
				.get(profileSpecId);
		profileProvisioningMBean.createProfileTable(profileSpecificationID, tableName);
	}

	public void createProfile(String tableName, String profileName) throws ManagementConsoleException {

		ObjectName objectName = profileProvisioningMBean.createProfile(tableName, profileName);
		profileMBean.commitProfile(objectName);
	}

	public void createProfile(String tableName, String profileName, PropertiesInfo propertiesInfo)
			throws ManagementConsoleException {
		ObjectName objectName = profileProvisioningMBean.createProfile(tableName, profileName);

		String[] names = new String[propertiesInfo.size()];
		String[] values = new String[propertiesInfo.size()];
		String[] types = new String[propertiesInfo.size()];

		Iterator<String> iterator = propertiesInfo.keySet().iterator();
		int row = 0;
		while (iterator.hasNext()) {
			String name = iterator.next();
			names[row] = name.split(NAME_TYPE_SEPARATOR)[0];
			values[row] = propertiesInfo.getProperty(name);
			types[row] = name.split(NAME_TYPE_SEPARATOR)[1];
			if (values[row] == null || values[row].isEmpty()) {
				profileMBean.setAttribute(objectName, new Attribute(names[row], null), types[row]);
			} else {
				profileMBean.setAttribute(objectName, new Attribute(names[row], values[row]), types[row]);
			}
			row++;
		}
		profileMBean.commitProfile(objectName);
	}

	public ProfileTableInfo[] getProfileTables() throws ManagementConsoleException {

		Collection<String> profileTablesCollection = profileProvisioningMBean.getProfileTables();
		ArrayList<String> tablesNames = new ArrayList<String>(profileTablesCollection);

		ProfileSpecificationID[] profileSpecId = new ProfileSpecificationID[tablesNames.size()];

		ArrayList<ProfileTableInfo> profileTableInfos = new ArrayList<ProfileTableInfo>();
		for (int i = 0; i < tablesNames.size(); i++) {

			profileSpecId[i] = profileProvisioningMBean.getProfileSpecification(tablesNames.get(i));

			if (profileSpecId[i] == null) {
				Logger.error("Profile-Table : " + tablesNames.get(i)
						+ "  exists in h2database but it has no profile Specifications deployed .. ProfileSpecification need to be deployed or adminstratively remove it");
			} else {

				ProfileTableInfo profileTableInfo = new ProfileTableInfo(tablesNames.get(i), profileSpecId[i].getName(),
						profileSpecId[i].getVendor(), profileSpecId[i].getVersion());
				profileTableInfos.add(profileTableInfo);
			}
		}
		return profileTableInfos.toArray(new ProfileTableInfo[profileTableInfos.size()]);
	}

	public String[] getProfileTables(String profileSpecificationId) throws ManagementConsoleException {
		Collection<String> profileTablesCollection = profileProvisioningMBean
				.getProfileTables(getProfileSpecificationIDFromString(profileSpecificationId));
		return profileTablesCollection.toArray(new String[profileTablesCollection.size()]);

	}

	public String[] getProfiles(String tableName) throws ManagementConsoleException {
		Collection<ProfileID> profilesCollection = profileProvisioningMBean.getProfiles(tableName);
		ArrayList<ProfileID> profileIDsList = new ArrayList<ProfileID>(profilesCollection);

		String[] profiles = new String[profileIDsList.size()];

		for (int i = 0; i < profileIDsList.size(); i++) {
			profiles[i] = profileIDsList.get(i).getProfileName();
		}
		return profiles;
	}

	public void removeProfile(String tableName, String profileName) throws ManagementConsoleException {
		profileProvisioningMBean.removeProfile(tableName, profileName);
	}

	public void removeProfileTable(String tableName) throws ManagementConsoleException {
		profileProvisioningMBean.removeProfileTable(tableName);
	}

	public void renameProfileTable(String currentTableName, String newTableName) throws ManagementConsoleException {
		profileProvisioningMBean.renameProfileTable(currentTableName, newTableName);
	}

	public String[] getProfileSpecifications() throws ManagementConsoleException {

		ComponentID[] componentIDs;
		ComponentDescriptor[] componentDescriptors;
		ComponentInfo[] componentInfos;

		componentIDs = deploymentMBean.getProfileSpecifications();
		componentDescriptors = deploymentMBean.getDescriptors(componentIDs);

		componentInfos = new ComponentInfo[componentDescriptors.length];

		String[] specificationsIds = new String[componentDescriptors.length];
		for (int i = 0; i < componentDescriptors.length; i++) {
			componentInfos[i] = ComponentInfoUtils.toComponentInfo(componentDescriptors[i]);

			String id = new String("ProfileSpecificationID[name=" + componentInfos[i].getName() + ",vendor="
					+ componentInfos[i].getVendor() + ",version=" + componentInfos[i].getVersion() + "]")
							.replaceAll("\\s", "");
			specificationsIds[i] = id;
		}

		return specificationsIds;
	}

	public void restoreProfile(String tableName, String profileName) throws ManagementConsoleException {
		profileMBean.restoreProfile(profileProvisioningMBean.getProfile(tableName, profileName));
	}

	public String[] getAttributesValues(String tableName, String profileName, String[] attributesNames)
			throws ManagementConsoleException {
		String names[] = new String[attributesNames.length];
		for (int i = 0; i < attributesNames.length; i++) {
			names[i] = attributesNames[i].split(NAME_TYPE_SEPARATOR)[0];
		}
		if (profileName == null) {

			String[] values = profileMBean.getAttributes(profileProvisioningMBean.getDefaultProfile(tableName), names);
			return values;
		} else {
			ObjectName objectName = profileProvisioningMBean.getProfile(tableName, profileName);
			return profileMBean.getAttributes(objectName, names);
		}
	}

	public String[] getAttributesNames(String tableName, String profileName) throws ManagementConsoleException {
		if (profileName == null) {
			//getting default profile
			return removeRedundancyAttributes(
					profileMBean.getAllAttributes(profileProvisioningMBean.getDefaultProfile(tableName)));
		} else {
			return removeRedundancyAttributes(
					profileMBean.getAllAttributes(profileProvisioningMBean.getProfile(tableName, profileName)));
		}
	}

	private String[] removeRedundancyAttributes(String[] attributes) {
		int newLength = attributes.length;
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].contains("ProfileDirty") || attributes[i].contains("ProfileWriteable")) {
				newLength--;
			}
		}
		String[] result = new String[newLength];
		int count = 0; 
		for (int i = 0; i < attributes.length; i++)
													
		{
			if (!attributes[i].contains("ProfileDirty") && !attributes[i].contains("ProfileWriteable")) {
				result[count] = attributes[i];
				count++;
			}
		}
		return result;
	}

	private ProfileSpecificationID getProfileSpecificationIDFromString(String componentId) {
		String componentString = componentId;
		String componentType = componentString.substring(0, componentString.indexOf('['));
		componentString = componentString.substring(componentType.length() + 1, componentString.length() - 1);
		String[] componentStringParts = componentString.split(",");
		String componentName = componentStringParts[0].substring("name=".length());
		String componentVendor = componentStringParts[1].substring("vendor=".length());
		String componentVersion = componentStringParts[2].substring("version=".length());
		return new ProfileSpecificationID(componentName, componentVendor, componentVersion);
	}

	public PropertiesInfo getProfileAttributes(String tableName, String profileName) throws ManagementConsoleException {
		String[] names = getAttributesNames(tableName, profileName);
		String[] values = getAttributesValues(tableName, profileName, names);

		PropertiesInfo propertiesInfo = new PropertiesInfo();

		for (int i = 0; i < names.length; i++) {
			propertiesInfo.setProperty(names[i], values[i]);
		}
		return propertiesInfo;
	}

	public void setProfileAttributes(String tableName, String profileName, PropertiesInfo propertiesInfo)
			throws ManagementConsoleException {
		profileMBean.editProfile(profileProvisioningMBean.getProfile(tableName, profileName));

		String[] names = new String[propertiesInfo.size()];
		String[] values = new String[propertiesInfo.size()];
		String[] types = new String[propertiesInfo.size()];

		Iterator<String> iterator = propertiesInfo.keySet().iterator();
		int row = 0;
		while (iterator.hasNext()) {
			String name = iterator.next();
			names[row] = name.split(NAME_TYPE_SEPARATOR)[0];
			values[row] = propertiesInfo.getProperty(name);
			types[row] = name.split(NAME_TYPE_SEPARATOR)[1];
			if (values[row] == null || values[row].isEmpty()) {
				profileMBean.setAttribute(profileProvisioningMBean.getProfile(tableName, profileName),
						new Attribute(names[row], null), types[row]);
			} else {
				profileMBean.setAttribute(profileProvisioningMBean.getProfile(tableName, profileName),
						new Attribute(names[row], values[row]), types[row]);
			}
			row++;
		}
		profileMBean.commitProfile(profileProvisioningMBean.getProfile(tableName, profileName));
	}

}
