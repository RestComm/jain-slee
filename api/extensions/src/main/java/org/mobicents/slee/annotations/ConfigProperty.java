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
 * Annotation for a JAIN SLEE 1.1 config property.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProperty {

	/**
	 * the config property name
	 * @return
	 */
	String name();
	
	/**
	 * the config property type
	 * @return
	 */
	Class<?> type();
	
	/**
	 * the config property value
	 * @return
	 */
	String value() default "";
	
}
