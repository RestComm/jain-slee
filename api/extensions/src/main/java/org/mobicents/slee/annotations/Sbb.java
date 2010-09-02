/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.slee.ActivityContextInterface;
import javax.slee.SbbLocalObject;

/**
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sbb {

	/**
	 * 
	 * @return
	 */
	String name();
	
	/**
	 * 
	 * @return
	 */
	String vendor();
	
	/**
	 * 
	 * @return
	 */
	String version();	
	
	/**
	 * 
	 * @return
	 */
	String alias() default "";
	
	/**
	 * 
	 * @return
	 */
	Class<? extends SbbLocalObject> localInterface() default SbbLocalObject.class;
	
	/**
	 * 
	 * @return
	 */
	Class<? extends ActivityContextInterface> activityContextInterface() default ActivityContextInterface.class;
		
	/**
	 * 
	 * @return
	 */
	String addressProfileSpecAliasRef() default "";
	
	/**
	 * 
	 * @return
	 */
	LibraryRef[] libraryRefs() default {};
	
	/**
	 * 
	 * @return
	 */
	SbbRef[] sbbRefs() default {};
	
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
	ActivityContextAttributeAlias[] activityContextAttributeAlias() default {};
	
	/**
	 * 
	 * @return
	 */
	String securityPermissions() default "";
	
	// TODO ejb-ref
	
}
