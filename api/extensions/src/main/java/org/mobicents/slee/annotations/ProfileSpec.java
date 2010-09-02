/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.profile.Profile;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;

/**
 * Annotation for a JAIN SLEE 1.1 Profile Spec.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileSpec {

	/**
	 * the profile spec name
	 * @return
	 */
	String name();
	
	/**
	 * the profile spec vendor
	 * @return
	 */
	String vendor();
	
	/**
	 * the profile spec version
	 * @return
	 */
	String version();
	
	/**
	 * the profile cmp interface
	 * @return
	 */
	Class<?> cmpInterface();
	
	/**
	 * the optional profile local interface
	 * @return
	 */
	Class<? extends ProfileLocalObject> localInterface() default ProfileLocalObject.class;
	
	/**
	 * the optional profile management interface
	 * @return
	 */
	Class<?> managementInterface() default Object.class;
	
	/**
	 * the optional profile abstract class
	 * @return
	 */
	Class<? extends Profile> abstractClass() default Profile.class;
	
	/**
	 * the optional profile table interface
	 * @return
	 */
	Class<? extends ProfileTable> tableInterface() default ProfileTable.class;
	
	/**
	 * 
	 * @return
	 */
	LibraryRef[] libraryRefs() default {};
	
	/**
	 * 
	 * @return
	 */
	ProfileSpecRef[] profileSpecRefs() default {};
	
	/**
	 * 
	 * @return
	 */
	ConfigProperty[] properties() default {};
	
	/**
	 * 
	 * @return
	 */
	String securityPermissions() default "";
	
	// TODO add static queries
	
}
