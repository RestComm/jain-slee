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
 * Annotation for a JAIN SLEE 1.1 Profile Spec Collator.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProfileSpecCollator {

	/**
	 * @return
	 */
	String localeLanguage();
	
	/**
	 * @return
	 */
	String localeCountry() default "";
	
	/**
	 * @return
	 */
	String localeVariant() default "";
	
	/**
	 * @return
	 */
	String alias();
}
