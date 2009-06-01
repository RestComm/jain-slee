package org.mobicents.slee.container.deployment.profile.jpa;

import java.beans.Introspector;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.slee.SLEEException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileCMPInterface;
import org.mobicents.slee.container.component.profile.ProfileEntity;
import org.mobicents.slee.container.deployment.profile.ClassGeneratorUtils;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;


/**
 * 
 * Generates the concrete profile entity and attribute array value classes, which hold the profile persistent attributes
 * 
 * <br>Project:  mobicents
 * <br>11:16:57 AM Mar 23, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author martins
 */
public class ConcreteProfileEntityGenerator {

  private static final Logger logger = Logger.getLogger(ConcreteProfileEntityGenerator.class);

  private ProfileSpecificationComponent profileComponent;
  private final JPAProfileEntityFramework jpaProfileDataSource;
  
  private final int profileCombination;
  
  public ConcreteProfileEntityGenerator(ProfileSpecificationComponent profileComponent,JPAProfileEntityFramework jpaProfileDataSource)
  {
	  this.profileComponent = profileComponent;
	  this.jpaProfileDataSource = jpaProfileDataSource;
	  this.profileCombination = SleeProfileClassCodeGenerator.checkCombination(profileComponent);
	  
	  logger.info( "Profile combination for " + profileComponent.getProfileSpecificationID() + " = " + this.profileCombination );
	
	  ClassGeneratorUtils.setClassPool( this.profileComponent.getClassPool().getClassPool() );
  }

  /**
   * Generates the concrete profile entity class and a profile entity array
   * attr value class for each attribute that is an array
   */
  public void generateClasses() {
    
    try {      
      
      ProfileSpecificationDescriptorImpl profileDescriptor = profileComponent.getDescriptor();
      String deployDir = profileComponent.getDeploymentDir().getAbsolutePath();
      
      MProfileCMPInterface cmpInterface = profileDescriptor.getProfileCMPInterface();
      
      // define the concrete profile entity class name
      String concreteProfileEntityClassName = cmpInterface.getProfileCmpInterfaceName() + "_PE";
      
      // create javassist class
      CtClass concreteProfileEntityClass = ClassGeneratorUtils.createClass(concreteProfileEntityClassName, new String[]{cmpInterface.getProfileCmpInterfaceName(), Serializable.class.getName()});
      
      // set inheritance
      ClassGeneratorUtils.createInheritanceLink(concreteProfileEntityClass, ProfileEntity.class.getName());
      
      // annotate with @Entity
      ClassGeneratorUtils.addAnnotation( Entity.class.getName(), new LinkedHashMap<String, Object>(), concreteProfileEntityClass );
      
      // annotate the @IdClass
      LinkedHashMap<String, Object> idClassMVs = new LinkedHashMap<String, Object>();
      idClassMVs.put( "value", JPAProfileId.class );
      ClassGeneratorUtils.addAnnotation( IdClass.class.getName(), idClassMVs, concreteProfileEntityClass );
      
      // add the table name to map it to ProfileSpecification ID
      LinkedHashMap<String, Object> tableMVs1 = new LinkedHashMap<String, Object>();      
      tableMVs1.put( "name", "SLEE_PE_"+profileComponent.getProfileCmpInterfaceClass().getSimpleName() + "_" + Math.abs(profileComponent.getComponentID().hashCode()) );
      ClassGeneratorUtils.addAnnotation( Table.class.getName(), tableMVs1, concreteProfileEntityClass );
     
      // override @id & @basic getter methods
      String getSafeProfileNameMethodSrc = "public String getSafeProfileName() { return super.getSafeProfileName(); }";
      CtMethod getSafeProfileNameMethod = CtNewMethod.make(getSafeProfileNameMethodSrc, concreteProfileEntityClass);
      ClassGeneratorUtils.addAnnotation( Id.class.getName(), new LinkedHashMap<String, Object>(), getSafeProfileNameMethod);
      concreteProfileEntityClass.addMethod(getSafeProfileNameMethod);      
      String getTableNameMethodSrc = "public String getTableName() { return super.getTableName(); }";
      CtMethod getTableNameMethod = CtNewMethod.make(getTableNameMethodSrc, concreteProfileEntityClass);
      ClassGeneratorUtils.addAnnotation( Id.class.getName(), new LinkedHashMap<String, Object>(), getTableNameMethod);
      concreteProfileEntityClass.addMethod(getTableNameMethod);
      
      // generate the getters/setters in the profile entity
      // gather the fieldNames of array type attributes
      ClassPool pool = profileComponent.getClassPool();
      CtClass cmpInterfaceClass = pool.get(cmpInterface.getProfileCmpInterfaceName());
      CtClass objectClass = pool.get(Object.class.getName());
      CtClass listClass = pool.get(List.class.getName());

      Map<String,Class<?>> profileEntityArrayAttrValueClassMap = new HashMap<String, Class<?>>();
      
      for(CtMethod method : cmpInterfaceClass.getMethods()) {
    	  if(!method.getDeclaringClass().equals(objectClass) && method.getName().startsWith( "get" )) {
    		  String fieldName = Introspector.decapitalize(method.getName().replaceFirst( "get", "" ));
    		  CtClass returnType = method.getReturnType().isArray() ? listClass : method.getReturnType();
    		  CtField genField = ClassGeneratorUtils.addField( returnType, fieldName, concreteProfileEntityClass );
    		  String pojoCmpAccessorSufix = ClassGeneratorUtils.getPojoCmpAccessorSufix(genField.getName());
    		  CtMethod ctMethod = CtNewMethod.getter( "get" + pojoCmpAccessorSufix, genField );
    		  concreteProfileEntityClass.addMethod(ctMethod);
    		  if (method.getReturnType().isArray()) {
    			  // we need to generate a class for this attribute, to hold the one to many relation
    			  Class<?> profileAttributeArrayValueClass = generateProfileAttributeArrayValueClass(concreteProfileEntityClass,fieldName);
    			  profileEntityArrayAttrValueClassMap.put(fieldName, profileAttributeArrayValueClass);
    			  // add the annotations of one to many association with array attr value class
    			  LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
    			  map.put("targetEntity", profileAttributeArrayValueClass);
    			  // FIXME see comment on generation of avv, it is possible to work wituout a join table but needs more work
    			  // THE MAPPEDBY IS REQUIRED FOR THE RELATION WITHOUT A JOIN TABLE
    			  // map.put("mappedBy", "pe");
    			  map.put("cascade",new CascadeType[]{CascadeType.ALL});
    			  //Annotation a = new Annotation(JoinColumns.class.getName(), ctMethod.getMethodInfo().getConstPool());
    			  //Annotation a1 = new Annotation(JoinColumn.class.getName(), ctMethod.getMethodInfo().getConstPool());
    			  ClassGeneratorUtils.addAnnotation( OneToMany.class.getName(), map, ctMethod);    		      
    		  }
    		  ctMethod = CtNewMethod.setter( "set" + pojoCmpAccessorSufix, genField );
    		  concreteProfileEntityClass.addMethod(ctMethod);
    	  }
      }
      
      jpaProfileDataSource.setProfileEntityArrayAttrValueClassMap(profileEntityArrayAttrValueClassMap);
      
      // write and load profile entity class
	  if (logger.isDebugEnabled())
    	  logger.debug( "Writing PROFILE ENTITY CONCRETE CLASS ( "+concreteProfileEntityClass.getName()+" ) to: " + deployDir );
      concreteProfileEntityClass.writeFile( deployDir );
      jpaProfileDataSource.setProfileEntityClass(Thread.currentThread().getContextClassLoader().loadClass(concreteProfileEntityClass.getName()));
      concreteProfileEntityClass.defrost();
      
    }
    catch ( Throwable e ) {
      throw new SLEEException(e.getMessage(),e);
    }
    
  }

  /**
   * Generates a class that extends {@link ProfileEntityArrayAttributeValue} for a specific entity attribute of array type value
   * @param concreteProfileEntityClass
   * @param profileAttributeName
   * @return
   */
  private Class<?> generateProfileAttributeArrayValueClass(CtClass concreteProfileEntityClass, String profileAttributeName) {
	
	  CtClass concreteArrayValueClass = null;
	  
	  try {
		  
		  // define the concrete profile attribute array value class name
		  String concreteArrayValueClassName = profileComponent.getProfileCmpInterfaceClass().getName() + "PEAAV_"+ClassGeneratorUtils.capitalize(profileAttributeName);   

		  // create javassist class
		  concreteArrayValueClass = ClassGeneratorUtils.createClass(concreteArrayValueClassName, new String[]{Serializable.class.getName()});

		  // set inheritance
		  ClassGeneratorUtils.createInheritanceLink(concreteArrayValueClass, ProfileEntityArrayAttributeValue.class.getName());

		  // annotate class with @Entity
		  ClassGeneratorUtils.addAnnotation( Entity.class.getName(), new LinkedHashMap<String, Object>(), concreteArrayValueClass );    

		  // generate a random table name
		  LinkedHashMap<String, Object> tableMVs = new LinkedHashMap<String, Object>();      
		  tableMVs.put( "name",  "SLEE_PEAAV_"+profileComponent.getProfileCmpInterfaceClass().getSimpleName() + "_" + Math.abs(profileAttributeName.hashCode()) );
		  ClassGeneratorUtils.addAnnotation( Table.class.getName(), tableMVs, concreteArrayValueClass );

		  // override @id
		  String getIdNameMethodSrc = "public long getId() { return super.getId(); }";
		  CtMethod getIdNameMethod = CtNewMethod.make(getIdNameMethodSrc, concreteArrayValueClass);
		  ClassGeneratorUtils.addAnnotation( Id.class.getName(), new LinkedHashMap<String, Object>(), getIdNameMethod);
		  ClassGeneratorUtils.addAnnotation( GeneratedValue.class.getName(), new LinkedHashMap<String, Object>(), getIdNameMethod);
		  concreteArrayValueClass.addMethod(getIdNameMethod);
		  
		  // override  @basic getter methods
		  String getSerializableMethodSrc = "public "+Serializable.class.getName()+" getSerializable() { return super.getSerializable(); }";
		  CtMethod getSerializableMethod = CtNewMethod.make(getSerializableMethodSrc, concreteArrayValueClass);
		  ClassGeneratorUtils.addAnnotation(Basic.class.getName(), new LinkedHashMap<String, Object>(), getSerializableMethod);
		  concreteArrayValueClass.addMethod(getSerializableMethod);
		  String getStringMethodSrc = "public String getString() { return super.getString(); }";
		  CtMethod getStringMethod = CtNewMethod.make(getStringMethodSrc, concreteArrayValueClass);
		  ClassGeneratorUtils.addAnnotation(Basic.class.getName(), new LinkedHashMap<String, Object>(), getStringMethod);
		  concreteArrayValueClass.addMethod(getStringMethod);

		  // FIXME add join columns here or in profile entity class to make
		  // the relation without a join table, atm if this is changed, the
		  // inserts on this table go with profile and table name as null %)
		  // THE PROFILENTITY FIELD IN AAV CLASS IS REQUIRED FOR THE RELATION WITH PROFILE ENTITY CLASS WITHOUT A JOIN TABLE
		  // add join column regarding the relation from array attr value to profile entity
		  /*CtField ctField = ClassGeneratorUtils.addField(concreteProfileEntityClass, "pe", concreteArrayValueClass);
		  ClassGeneratorUtils.generateSetter(ctField,null);
		  CtMethod getter = ClassGeneratorUtils.generateGetter(ctField,null);
		  ClassGeneratorUtils.addAnnotation(ManyToOne.class.getName(), new LinkedHashMap<String, Object>(), getter);
		  */
		  
		  // write and load the attr array value class
		  String deployDir = profileComponent.getDeploymentDir().getAbsolutePath();
		  if (logger.isDebugEnabled()) {
			  logger.debug( "Writing PROFILE ATTR ARRAY VALUE CONCRETE CLASS ( "+concreteArrayValueClass.getName()+" ) to: " + deployDir );
		  }
		   concreteArrayValueClass.writeFile( deployDir );
		  
		  return Thread.currentThread().getContextClassLoader().loadClass(concreteArrayValueClass.getName());      
	  }
	  catch (Throwable e) {
		  throw new SLEEException(e.getMessage(),e);
	  }
	  finally {
		  if (concreteArrayValueClass != null) {
			  concreteArrayValueClass.defrost();
		  }
	  }
  }
  
}