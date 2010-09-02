/**
 * 
 */
package org.mobicents.slee.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Annotation for a CMP Field belonging to JAIN SLEE 1.1 Sbb abstract class.
 * @author martins
 *
 */
@Documented
@Target(value={ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CMPField {
	
	String sbbAliasRef() default "";
	
}
