package org.mobicents.slee.container.profile;

import java.util.Collection;
import java.util.List;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.profile.query.QueryExpression;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;

public interface ProfileDataSource {

	  public static ProfileDataSource INSTANCE = JPAUtils.INSTANCE;
	  
	  public void install(ProfileSpecificationComponent component);
	  
	  public void uninstall(ProfileSpecificationComponent component);
	  
	  public Object find(String profileTableName, String profileName) throws SLEEException, UnrecognizedProfileTableNameException;

	  public Collection<Object> findAll(String profileTableName);

	  public Object findProfileByAttribute(String profileTableName, String attributeName, Object attributeValue);

	  public Collection<Object> findProfilesByAttribute(String profileTableName, String attributeName, Object attributeValue);

	  public Collection<ProfileID> getProfilesIDs(ProfileTableConcrete ptc);

	  public boolean find(ProfileTableConcrete ptc, String profileName);

	  public List<String> findAllNames(ProfileTableConcrete ptc) throws NullPointerException, TransactionRequiredLocalException, SLEEException;

	  public Collection getProfileTables(ProfileSpecificationID id);

	  public Collection getProfiles(String profileTableName);

	  public Collection getProfilesByAttribute(String profileTableName, String attributeName, Object attributeValue);

	  public Collection getProfilesByStaticQuery(String profileTableName, String queryName, Object[] parameters);

	  public Collection getProfilesByDynamicQuery(String profileTableName, QueryExpression expr);

	  @Deprecated
	  public Collection getProfilesByIndexedAttribute(String profileTableName, String attributeName, Object attributeValue);

	  public void persistProfile(ProfileObject profileObject);
	  
	  public ProfileEntity retrieveProfile(ProfileTableConcrete profileTable, String profileName);

	  public boolean removeprofile(ProfileTableConcrete profileTable, String profileName);
	  
	  public void removeprofile(ProfileObject profileObject);

	  
	  

}
