/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.deployment.profile.jpa;

import java.beans.Introspector;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.slee.SLEEException;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.mobicents.slee.container.component.ClassPool;
import org.mobicents.slee.container.component.profile.ProfileAttribute;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.profile.ProfileSpecificationDescriptor;
import org.mobicents.slee.container.component.profile.cmp.ProfileCMPInterfaceDescriptor;
import org.mobicents.slee.container.deployment.profile.ClassGeneratorUtils;
import org.mobicents.slee.container.profile.entity.ProfileEntity;


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
    
  public ConcreteProfileEntityGenerator(ProfileSpecificationComponent profileComponent,JPAProfileEntityFramework jpaProfileDataSource)
  {
	  this.profileComponent = profileComponent;
	  this.jpaProfileDataSource = jpaProfileDataSource;
	  	
	  ClassGeneratorUtils.setClassPool( this.profileComponent.getClassPool().getClassPool() );
  }

  /**
   * Generates the concrete profile entity class and a profile entity array
   * attr value class for each attribute that is an array
   */
  public void generateClasses() {
    
    try {      
      
      ProfileSpecificationDescriptor profileDescriptor = profileComponent.getDescriptor();
      String deployDir = profileComponent.getDeploymentDir().getAbsolutePath();
      
      ProfileCMPInterfaceDescriptor cmpInterface = profileDescriptor.getProfileCMPInterface();
      
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
      
      Set<String> uniqueConstraints = new HashSet<String>();
      
      // override @id & @basic getter methods
      String getProfileNameMethodSrc = "public String getProfileName() { return super.getProfileName(); }";
      CtMethod getProfileNameMethod = CtNewMethod.make(getProfileNameMethodSrc, concreteProfileEntityClass);
      ClassGeneratorUtils.addAnnotation( Id.class.getName(), new LinkedHashMap<String, Object>(), getProfileNameMethod);
      concreteProfileEntityClass.addMethod(getProfileNameMethod);      
      String getTableNameMethodSrc = "public String getTableName() { return super.getTableName(); }";
      CtMethod getTableNameMethod = CtNewMethod.make(getTableNameMethodSrc, concreteProfileEntityClass);
      ClassGeneratorUtils.addAnnotation( Id.class.getName(), new LinkedHashMap<String, Object>(), getTableNameMethod);
      concreteProfileEntityClass.addMethod(getTableNameMethod);
      
      // generate the getters/setters in the profile entity
      // gather the fieldNames of array type attributes
      ClassPool pool = profileComponent.getClassPool();
      CtClass cmpInterfaceClass = pool.get(cmpInterface.getProfileCmpInterfaceName());
      CtClass listClass = pool.get(List.class.getName());

      Map<String,Class<?>> profileEntityArrayAttrValueClassMap = new HashMap<String, Class<?>>();
      
      for(CtMethod method : cmpInterfaceClass.getMethods()) {
    	  if(!method.getDeclaringClass().getName().equals(Object.class.getName()) && method.getName().startsWith( "get" )) {
    		  String fieldName = Introspector.decapitalize(method.getName().replaceFirst( "get", "" ));
    		  boolean array = method.getReturnType().isArray();
    		  CtClass returnType = array ? listClass : method.getReturnType();
    		  CtField genField = ClassGeneratorUtils.addField( returnType, fieldName, concreteProfileEntityClass );
    		  String pojoCmpAccessorSufix = ClassGeneratorUtils.getPojoCmpAccessorSufix(genField.getName());
    		  // create the getter
    		  CtMethod ctMethod = CtNewMethod.getter( "get" + pojoCmpAccessorSufix, genField );
    		  ProfileAttribute profileAttribute = profileComponent.getProfileAttributes().get(fieldName);    		  
    		  concreteProfileEntityClass.addMethod(ctMethod);
    		  if (array) {
    			  // we need to generate a class for this attribute, to hold the one to many relation
    			  Class<?> profileAttributeArrayValueClass = generateProfileAttributeArrayValueClass(concreteProfileEntityClass,fieldName,profileAttribute.isUnique());
    			  profileEntityArrayAttrValueClassMap.put(fieldName, profileAttributeArrayValueClass);
    			  // add the annotations of one to many association with array attr value class
    			  LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
    			  map.put("targetEntity", profileAttributeArrayValueClass);
    			  // FIXME see comment on generation of avv, it is possible to work wituout a join table but needs more work
    			  // THE MAPPEDBY IS REQUIRED FOR THE RELATION WITHOUT A JOIN TABLE
    			  map.put("mappedBy", "owner");
    			  map.put("cascade",new CascadeType[]{CascadeType.ALL});
    			  ClassGeneratorUtils.addAnnotation( OneToMany.class.getName(), map, ctMethod);
    			  // we need to add a special hibernate annotation because the jpa cascade delete only deletes from join table, not the orphan row at the PEAAV table
    			  map = new LinkedHashMap<String, Object>();
    			  map.put("value",new org.hibernate.annotations.CascadeType[]{org.hibernate.annotations.CascadeType.DELETE_ORPHAN});
    			  ClassGeneratorUtils.addAnnotation( Cascade.class.getName(), map, ctMethod);
    			  // make setter from src
    			  /*String setterSrc = 
    				  "public void set"+ pojoCmpAccessorSufix + "("+List.class.getName()+" value) {" +
    				  "  System.out.println(\"PEAAV setter: "+genField.getName()+" = \"+this."+genField.getName()+"+\" value = \"+value);" +
    				  "  if (this."+genField.getName()+" != null) { " +
    				  "    this."+genField.getName()+".clear(); " +
    				  "    if (value != null) { " +
					  "      for ("+Iterator.class.getName()+" i = value.iterator(); i.hasNext();) { " +
					  "        " + profileAttributeArrayValueClass.getName()+" otherArrayValue = ("+profileAttributeArrayValueClass.getName()+") i.next(); " +
					  "        " + profileAttributeArrayValueClass.getName()+" thisArrayValue = new "+profileAttributeArrayValueClass.getName()+"(); " +
					  "        thisArrayValue.setString( otherArrayValue.getString() ); " +
					  "        thisArrayValue.setSerializable( ("+Serializable.class.getName()+") "+ObjectCloner.class.getName()+".makeDeepCopy(otherArrayValue.getSerializable()) ); " +
					  "        this."+genField.getName()+".add(thisArrayValue); " +   				  
					  "      }" +
					  "    }" +
    		          "  } else { " +
    		          "    this."+genField.getName()+" = value; " +
    		          "  }" +
					  "}";
    			  ctMethod = CtMethod.make(setterSrc, concreteProfileEntityClass);
    			  concreteProfileEntityClass.addMethod(ctMethod);
    			  */
    		  }
    		  else {
    			  // not an array, just add column annotation with or without unique constraint
	    		  if (profileAttribute.isUnique()) {
	    			  // just collect uniqueConstraints attributtes
	    			  uniqueConstraints.add(Introspector.decapitalize(pojoCmpAccessorSufix));	    			  
        		  }	    		  	    		  
	    			
	    		  //String , primitive types , Array , Date will not be modified , only serialized data
	    		  LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();	    		  
	    		  if (!returnType.isPrimitive() && ! returnType.getName().equals("java.lang.String"))
    				  map.put("length", 512);
    			  
    			  ClassGeneratorUtils.addAnnotation(Column.class.getName(), map, ctMethod);    			  
    		  }
    		// add usual setter
    		  ctMethod = CtNewMethod.setter( "set" + pojoCmpAccessorSufix, genField );
    		  concreteProfileEntityClass.addMethod(ctMethod);
    		  
    	  }
      }
      
      String tableName = "SLEE_PE_"+profileComponent.getProfileCmpInterfaceClass().getSimpleName() + "_" + Math.abs((long)profileComponent.getComponentID().hashCode());
      addTableAnnotation(tableName, uniqueConstraints, concreteProfileEntityClass);
      
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

  private void addTableAnnotation(String name, Set<String> uniqueAttributes,CtClass ctClass) {
	  
	  ClassFile cf = ctClass.getClassFile();
	  ConstPool cp = cf.getConstPool();
	  
	  AnnotationsAttribute attr = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
	  if (attr == null) {
		  attr = new AnnotationsAttribute(cp,AnnotationsAttribute.visibleTag);
	  }
	  
	  // create table annotation and set name attribute
	  Annotation table = new Annotation(Table.class.getName(), cp);
	  table.addMemberValue("name", new StringMemberValue(name,cp));
	  
	  if (!uniqueAttributes.isEmpty()) {
		  // we have unique attributes, this in fact transforms in unique constraints of both table name key and the unique attribute
		  // since in one entity there are multiple tables "merged"
		  ArrayMemberValue uniqueConstraintsArrayMemberValue = new ArrayMemberValue(cp);
		  MemberValue[] uniqueConstraintsMemberValues = new MemberValue[uniqueAttributes.size()];
		  int i = 0;
		  for(String uniqueAttr : uniqueAttributes) {
			  // create UniqueConstraint annotation
			  Annotation uniqueConstraint = new Annotation(UniqueConstraint.class.getName(), cp);
			  MemberValue[] uniqueConstraintMemberValues = new MemberValue[2];
			  uniqueConstraintMemberValues[0] = new StringMemberValue(uniqueAttr, cp);
			  uniqueConstraintMemberValues[1] = new StringMemberValue("tableName", cp);
			  ArrayMemberValue uniqueConstraintArrayMemberValue = new ArrayMemberValue(cp);
			  uniqueConstraintArrayMemberValue.setValue(uniqueConstraintMemberValues);
			  // set columnNames field with value {uniqueAttr,"tableName"}
			  uniqueConstraint.addMemberValue("columnNames", uniqueConstraintArrayMemberValue);
			  // add to array of UniqueConstraint annotations
			  uniqueConstraintsMemberValues[i] = new AnnotationMemberValue(uniqueConstraint,cp);
		  }
		  // add to Table the field uniqueConstraints with UniqueConstraint[] value
		  uniqueConstraintsArrayMemberValue.setValue(uniqueConstraintsMemberValues);
		  table.addMemberValue("uniqueConstraints", uniqueConstraintsArrayMemberValue);
	  }
	  
	  // add Table annotation to class
	  attr.addAnnotation(table);
  }
  
  /**
   * Generates a class that extends {@link ProfileEntityArrayAttributeValue} for a specific entity attribute of array type value
   * @param concreteProfileEntityClass
   * @param profileAttributeName
   * @return
   */
  private Class<?> generateProfileAttributeArrayValueClass(CtClass concreteProfileEntityClass, String profileAttributeName, boolean unique) {
	
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
		  addTableAnnotationToPEAAV("SLEE_PEAAV_"+profileComponent.getProfileCmpInterfaceClass().getSimpleName() + "_" + Math.abs((long)profileComponent.getComponentID().hashCode()) + profileAttributeName,unique,concreteArrayValueClass);  
		  		  
		  // override @id
		  String getIdNameMethodSrc = "public long getId() { return super.getId(); }";
		  CtMethod getIdNameMethod = CtNewMethod.make(getIdNameMethodSrc, concreteArrayValueClass);
		  ClassGeneratorUtils.addAnnotation( Id.class.getName(), new LinkedHashMap<String, Object>(), getIdNameMethod);
		  ClassGeneratorUtils.addAnnotation( GeneratedValue.class.getName(), new LinkedHashMap<String, Object>(), getIdNameMethod);
		  concreteArrayValueClass.addMethod(getIdNameMethod);
		  
		  // override getter methods
		  String getSerializableMethodSrc = "public "+Serializable.class.getName()+" getSerializable() { return super.getSerializable(); }";
		  CtMethod getSerializableMethod = CtNewMethod.make(getSerializableMethodSrc, concreteArrayValueClass);
		  LinkedHashMap<String,Object> map = new LinkedHashMap<String, Object>();
		  map.put("name", "serializable");
		  map.put("length", 512);
		  //if (unique)map.put("unique", true);
		  ClassGeneratorUtils.addAnnotation(Column.class.getName(), map, getSerializableMethod);
		  concreteArrayValueClass.addMethod(getSerializableMethod);
		  String getStringMethodSrc = "public String getString() { return super.getString(); }";
		  CtMethod getStringMethod = CtNewMethod.make(getStringMethodSrc, concreteArrayValueClass);
		  map = new LinkedHashMap<String, Object>();
		  map.put("name", "string");
		  //if (unique)map.put("unique", true);
		  ClassGeneratorUtils.addAnnotation(Column.class.getName(), map, getStringMethod);
		  concreteArrayValueClass.addMethod(getStringMethod);

		  // FIXME add join columns here or in profile entity class to make
		  // the relation without a join table, atm if this is changed, the
		  // inserts on this table go with profile and table name as null %)
		  // THE PROFILENTITY FIELD IN AAV CLASS IS REQUIRED FOR THE RELATION WITH PROFILE ENTITY CLASS WITHOUT A JOIN TABLE
		  // add join column regarding the relation from array attr value to profile entity
		  CtField ctField = ClassGeneratorUtils.addField(concreteProfileEntityClass, "owner", concreteArrayValueClass);
		  ClassGeneratorUtils.generateSetter(ctField,null);
		  CtMethod getter = ClassGeneratorUtils.generateGetter(ctField,null);
		  //ClassGeneratorUtils.addAnnotation(ManyToOne.class.getName(), new LinkedHashMap<String, Object>(), getter);
		  // ----
		  ConstPool cp = getter.getMethodInfo().getConstPool();
		  AnnotationsAttribute attr = (AnnotationsAttribute) getter.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
		  if (attr == null) {
			  attr = new AnnotationsAttribute(cp,AnnotationsAttribute.visibleTag);
		  }
		  
		  Annotation manyToOne = new Annotation(ManyToOne.class.getName(), cp);
		  manyToOne.addMemberValue("optional", new BooleanMemberValue(false,cp));
		  attr.addAnnotation(manyToOne);
		  
		  Annotation joinColumns = new Annotation(JoinColumns.class.getName(), cp);
		  Annotation joinColumn1 = new Annotation(JoinColumn.class.getName(), cp);
		  joinColumn1.addMemberValue("name", new StringMemberValue("owner_tableName", cp));
		  joinColumn1.addMemberValue("referencedColumnName", new StringMemberValue("tableName", cp));
		  Annotation joinColumn2 = new Annotation(JoinColumn.class.getName(), cp);
		  joinColumn2.addMemberValue("name", new StringMemberValue("owner_profileName", cp));
		  joinColumn2.addMemberValue("referencedColumnName", new StringMemberValue("profileName", cp));
		  ArrayMemberValue joinColumnsMemberValue = new ArrayMemberValue(cp);
    	  joinColumnsMemberValue.setValue(new MemberValue[] { new AnnotationMemberValue(joinColumn1,cp), new AnnotationMemberValue(joinColumn2,cp)});
    	  joinColumns.addMemberValue("value", joinColumnsMemberValue);
    	  attr.addAnnotation(joinColumns);
    	  
    	  getter.getMethodInfo().addAttribute(attr);
		  
    	  // generate concrete setProfileEntity method
    	  String setProfileEntityMethodSrc = "public void setProfileEntity("+ProfileEntity.class.getName()+" profileEntity){ setOwner(("+concreteProfileEntityClass.getName()+")profileEntity); }";
    	  CtMethod setProfileEntityMethod = CtMethod.make(setProfileEntityMethodSrc, concreteArrayValueClass);
    	  concreteArrayValueClass.addMethod(setProfileEntityMethod);
    	      	  
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
  
  private void addTableAnnotationToPEAAV(String tableName, boolean unique, CtClass ctClass) {
	  
	  if (logger.isTraceEnabled()) {
		 logger.trace("Adding PEAAV table with name "+tableName+" for "+profileComponent+", attribute is unique? "+unique); 
	  }
	  
	  ConstPool cp = ctClass.getClassFile().getConstPool();
	  AnnotationsAttribute attr = (AnnotationsAttribute) ctClass.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);
	  if (attr == null) {
		  attr = new AnnotationsAttribute(cp,AnnotationsAttribute.visibleTag);
	  }

	  Annotation table = new Annotation(Table.class.getName(), cp);
	  
	  table.addMemberValue("name", new StringMemberValue(tableName,cp));
	  
	  if (unique) {
		  ArrayMemberValue columnNamesMemberValue = new ArrayMemberValue(cp);
		  columnNamesMemberValue.setValue(new MemberValue[] { new StringMemberValue("owner_tableName",cp) , new StringMemberValue("string",cp) });
		  Annotation uniqueContraint = new Annotation(UniqueConstraint.class.getName(), cp);
		  uniqueContraint.addMemberValue("columnNames", columnNamesMemberValue);

		  ArrayMemberValue uniqueConstraintsMemberValue = new ArrayMemberValue(cp);
		  uniqueConstraintsMemberValue.setValue(new MemberValue[] { new AnnotationMemberValue(uniqueContraint,cp)});

		  table.addMemberValue("uniqueConstraints", uniqueConstraintsMemberValue);
	  }	  
	  
	  attr.addAnnotation(table);
	  ctClass.getClassFile().addAttribute(attr);
	
  }
  
}