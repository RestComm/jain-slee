/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceAdaptor {

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
	boolean ignoreRATypeEventTypeCheck() default false;
	
	/**
	 * 
	 * @return
	 */
	boolean supportsActiveReconfiguration() default false;
	
	/**
	 * 
	 * @return
	 */
	Class<?> usageParametersInterface() default Object.class;
	
	/**
	 * 
	 * @return
	 */
	ConfigProperty[] properties() default {};
	
	/**
	 * 
	 * @return
	 */
	ResourceAdaptorTypeRef[] raTypeRefs();
	
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
	String securityPermissions() default "";
}
