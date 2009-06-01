package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import javax.slee.SLEEException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.profile.ProfileAttribute;
import org.mobicents.slee.container.component.profile.ProfileEntity;
import org.mobicents.slee.container.component.profile.ProfileEntityFactory;
import org.mobicents.slee.container.deployment.profile.ClassGeneratorUtils;

/**
 * 
 * Generates the concrete profile entity factory class for a profile spec
 * @author martins
 */
public class ConcreteProfileEntityFactoryGenerator {

  private static final Logger logger = Logger.getLogger(ConcreteProfileEntityFactoryGenerator.class);

  private final ProfileSpecificationComponent profileComponent;
  private final Class<?> profileEntityClass;
  private final Map<String, Class<?>> profileEntityAttributeArrayValueClasses;
  
  public ConcreteProfileEntityFactoryGenerator(ProfileSpecificationComponent profileComponent, Class<?> profileEntityClass, Map<String, Class<?>> profileEntityAttributeArrayValueClasses) {
	  this.profileComponent = profileComponent;	  
	  this.profileEntityClass = profileEntityClass;
	  this.profileEntityAttributeArrayValueClasses = profileEntityAttributeArrayValueClasses;
  }

  /*
   * Generates the profile entity factory class
   */
  public Class<?> generateClass() {
    	  
	  CtClass ctClass = null;
	  
	  try {      
    	  
		  ClassPool classPool = profileComponent.getClassPool();
		  Collection<ProfileAttribute> profileAttributes = profileComponent.getProfileAttributes().values();

		  String className = profileEntityClass.getName() + "F";

		  ctClass = classPool.makeClass(className);

		  CtClass profileEntityFactoryClass = classPool.get(ProfileEntityFactory.class.getName());
		  CtClass[] interfaces = new CtClass[] { profileEntityFactoryClass };
		  ctClass.setInterfaces(interfaces);

		  // copy newInstance method from interface and generate body
		  CtMethod newInstanceMethod = profileEntityFactoryClass.getDeclaredMethod("newInstance");
		  CtMethod newInstanceMethodCopy = CtNewMethod.copy(newInstanceMethod, ctClass, null);
		  String newInstanceMethodCopyBody = 
			  "{ "+
			  	profileEntityClass.getName()+" profileEntity = new "+profileEntityClass.getName()+"();" +
			  	" profileEntity.setTableName($1); " +
			  	" profileEntity.setProfileName($2); " +
			  	" return profileEntity; " +
			  "}";
		  if (logger.isDebugEnabled()) {
			  logger.debug( "Adding method named "+newInstanceMethodCopy.getName()+" with body "+newInstanceMethodCopy+" , into class " + className );
		  }
		  newInstanceMethodCopy.setBody(newInstanceMethodCopyBody);
		  ctClass.addMethod(newInstanceMethodCopy);
		
		  // copy cloneInstance method from interface and generate body
		  CtMethod cloneInstanceMethod = profileEntityFactoryClass.getDeclaredMethod("cloneInstance");
		  CtMethod cloneInstanceMethodCopy = CtNewMethod.copy(cloneInstanceMethod, ctClass, null);
		  // body header
		  String cloneInstanceMethodCopyBody = 
			  "{ "+
			  	profileEntityClass.getName()+" newProfileEntity = new "+profileEntityClass.getName()+"(); " +
			  	" newProfileEntity.setProfileName($1); " +
			  	" newProfileEntity.setTableName($2.getTableName()); " +
			  	profileEntityClass.getName()+" oldProfileEntity = ("+profileEntityClass.getName()+") $2; ";
		  // process fields copy
		  String profileEntityAttributeArrayValueClassName = null;
		  for (ProfileAttribute profileAttribute : profileAttributes) {
			  String accessorMethodSufix = ClassGeneratorUtils.getPojoCmpAccessorSufix(profileAttribute.getName());
			  if (profileAttribute.isArray()) {
				  profileEntityAttributeArrayValueClassName = profileEntityAttributeArrayValueClasses.get(profileAttribute.getName()).getName();
				  cloneInstanceMethodCopyBody += 
					  // new linked list instance
					  List.class.getName() + " new"+ profileAttribute.getName() + " = new " + LinkedList.class.getName() + "(); " +
					  // extract list to copy
					  List.class.getName() + " old"+ profileAttribute.getName() + " = oldProfileEntity.get" + accessorMethodSufix + "(); " +
					  // iterate each list element
					  "for ("+Iterator.class.getName()+" i = old"+profileAttribute.getName()+".iterator(); i.hasNext();) { " +
					  profileEntityAttributeArrayValueClassName+" oldArrayValue = ("+profileEntityAttributeArrayValueClassName+") i.next(); " +
					  profileEntityAttributeArrayValueClassName+" newArrayValue = new "+profileEntityAttributeArrayValueClassName+"(); " +
					  // copy fields
					  "newArrayValue.setString( oldArrayValue.getString() ); " +
					  "newArrayValue.setSerializable( ("+Serializable.class.getName()+") "+ProfileEntity.class.getName()+".makeDeepCopy(oldArrayValue.getSerializable()) ); " +
					  "new"+profileAttribute.getName()+".add(newArrayValue); " +
					  "} " +
					  "newProfileEntity.set"+accessorMethodSufix+"(new"+profileAttribute.getName()+"); ";					  
			  }
			  else {
				  if (profileAttribute.isPrimitive()) {
					  // just copy value
					  cloneInstanceMethodCopyBody += 
						  " newProfileEntity.set"+accessorMethodSufix+"(oldProfileEntity.get"+accessorMethodSufix+"()); ";
				  }
				  else {
					// just copy value but do a deep copy
					  cloneInstanceMethodCopyBody += 
						  " newProfileEntity.set"+accessorMethodSufix+"(("+profileAttribute.getType().getName()+")"+ProfileEntity.class.getName()+".makeDeepCopy(oldProfileEntity.get"+accessorMethodSufix+"())); ";
				  }
			  }
		  }
		  // body footer
		  cloneInstanceMethodCopyBody += 
			  " return newProfileEntity; " +
			  "}";
		  if (logger.isDebugEnabled()) {
			  logger.debug( "Adding method named "+cloneInstanceMethodCopy.getName()+" with body "+cloneInstanceMethodCopyBody+" , into class " + className );
		  }
		  cloneInstanceMethodCopy.setBody(cloneInstanceMethodCopyBody);
		  ctClass.addMethod(cloneInstanceMethodCopy);
		  
      String deployDir = profileComponent.getDeploymentDir().getAbsolutePath();
      
      // write class
      if (logger.isDebugEnabled())
    	  logger.debug( "Writing ProfileEntityFactory generated class ( "+ctClass.getName()+" ) to: " + deployDir );
      ctClass.writeFile( deployDir );
      return Thread.currentThread().getContextClassLoader().loadClass(className);      
      
    } catch (Throwable e) {
    	throw new SLEEException(e.getMessage(),e);
    }
    
    finally {
    	
    	if (ctClass != null) {
    		try {
    			ctClass.defrost();
    		}
    		catch (Throwable e) {
				logger.error(e.getMessage(),e);
			}
    	}
    	
    }
    
  }
  
}
