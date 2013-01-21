/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack.engine;

import com.google.inject.BindingAnnotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author mbarnas
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.PARAMETER})    
@BindingAnnotation
public @interface Peek {
	
}
