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
@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigPropertiesField {

}
