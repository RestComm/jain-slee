package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.slee.InvalidArgumentException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;
import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;

public interface ProfileDataSource {

	  public static ProfileDataSource INSTANCE = JPAUtils.INSTANCE;
	  
	  /**
	   * 
	   * @param component
	   */
	  public void install(ProfileSpecificationComponent component);
	  
	  /**
	   * 
	   * @param component
	   */
	  public void uninstall(ProfileSpecificationComponent component);
	  
	  /**
	   * 
	   * @param profileTable
	   * @return
	   */
	  public Collection<ProfileEntity> findAll(ProfileTableImpl profileTable);
	  
	  /**
	   * 
	   * @param profileTable
	   * @param attributeName
	   * @param attributeValue
	   * @return
	   */
	  public Collection<ProfileEntity> findProfilesByAttribute(ProfileTableImpl profileTable, String attributeName, Object attributeValue);
	  
	  /**
	   * 
	   * @param profileTable
	   * @param profileName
	   * @return
	   */
	  public ProfileEntity findProfile(ProfileTableImpl profileTable, String profileName);
	  
	  /**
	   * 
	   * @param profileTableName
	   * @param queryName
	   * @param parameters
	   * @return
	   */
	  public Collection<ProfileEntity> getProfilesByStaticQuery(ProfileTableImpl profileTable, String queryName, Object[] parameters) throws NullPointerException, UnrecognizedQueryNameException,AttributeTypeMismatchException,InvalidArgumentException;

	  /**
	   * 
	   * @param profileTableName
	   * @param expr
	   * @return
	   */
	  public Collection<ProfileEntity> getProfilesByDynamicQuery(ProfileTableImpl profileTable, QueryExpression expr) throws UnrecognizedAttributeException,AttributeTypeMismatchException;

	  /**
	   * 
	   * @param profileObject
	   */
	  public void persistProfile(ProfileObject profileObject);
	  
	  /**
	   * 
	   * @param profileTable
	   * @param profileName
	   * @return
	   */
	  public ProfileEntity retrieveProfile(ProfileTableImpl profileTable, String profileName);

	  /**
	   * 
	   * @param profileObject
	   */
	  public void removeprofile(ProfileObject profileObject);

}
