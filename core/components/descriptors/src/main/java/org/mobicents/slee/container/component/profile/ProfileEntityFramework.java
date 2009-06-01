package org.mobicents.slee.container.component.profile;

import java.util.Collection;

import javax.slee.InvalidArgumentException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedQueryNameException;
import javax.slee.profile.query.QueryExpression;

/**
 * A profile entity framework is responsible to implement concrete details about
 * profile entity management, including class generation, persistent data
 * storage, query parsing, etc.
 *  
 * @author martins
 * 
 */
public interface ProfileEntityFramework {
	  
	  /**
	 * 
	 * @param profileTable
	 * @return
	 */
	public Collection<ProfileEntity> findAll(String profileTable);

	/**
	 * 
	 * @param profileTable
	 * @param profileName
	 * @return
	 */
	public ProfileEntity findProfile(String profileTable,
			String profileName);

	/**
	 * 
	 * @param profileTable
	 * @param profileAttribute
	 * @param attributeValue
	 * @return
	 */
	public Collection<ProfileEntity> findProfilesByAttribute(
			String profileTable, ProfileAttribute profileAttribute,
			Object attributeValue);

	/**
	 * 
	 * @return
	 */
	public Class<?> getProfileEntityClass();

	/**
	 * 
	 * @return
	 */
	public ProfileEntityFactory getProfileEntityFactory();

	/**
	 * 
	 * @param profileTableName
	 * @param expr
	 * @return
	 */
	public Collection<ProfileEntity> getProfilesByDynamicQuery(
			String profileTable, QueryExpression expr)
			throws UnrecognizedAttributeException,
			AttributeTypeMismatchException;

	/**
	 * 
	 * @param profileTableName
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public Collection<ProfileEntity> getProfilesByStaticQuery(
			String profileTable, String queryName, Object[] parameters)
			throws NullPointerException, UnrecognizedQueryNameException,
			AttributeTypeMismatchException, InvalidArgumentException;

	/**
	 * 
	 * @param component
	 */
	public void install();

	/**
	 * 
	 * @param profileEntity
	 */
	public void persistProfile(ProfileEntity profileEntity);

	/**
	 * 
	 * @param profileEntity
	 */
	public void removeprofile(ProfileEntity profileEntity);

	/**
	 * 
	 * @param profileTable
	 * @param profileName
	 * @return
	 */
	public ProfileEntity retrieveProfile(String profileTable,
			String profileName);

	/**
	 * 
	 * @param component
	 */
	public void uninstall();

}
