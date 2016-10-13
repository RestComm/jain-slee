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

package org.mobicents.slee.container.deployment.profile;

import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.deployment.ClassUtils;
import org.mobicents.slee.container.profile.ProfileCallRecorderTransactionData;
import org.mobicents.slee.container.profile.ProfileCmpHandler;
import org.mobicents.slee.container.profile.ProfileManagementHandler;
import org.mobicents.slee.container.security.Utility;

/**
 * 
 * ClassGeneratorUtils.java
 *
 * <br>Project:  mobicents
 * <br>9:22:57 AM Mar 26, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ClassGeneratorUtils {

  private static final Logger logger = Logger.getLogger(ClassGeneratorUtils.class);

  private static ClassPool classPool = null;

  public static final String _PLO_PO_ALLOCATION = "";//"allocateProfileObject();";

  public static final String CMP_HANDLER = ProfileCmpHandler.class.getName();
  public static final String MANAGEMENT_HANDLER = ProfileManagementHandler.class.getName();

  /**
   * Creates a class with the desired name and linked to the mentioned interfaces.
   * 
   * @param className
   * @param interfaces
   * @return
   * @throws Exception
   */
  public static CtClass createClass(String className, String[] interfaces) throws Exception
  {
    if(className == null)
    {
      throw new NullPointerException("Class name cannot be null");
    }

    CtClass clazz = classPool.makeClass(className);

    if(interfaces != null && interfaces.length > 0)
    {
      clazz.setInterfaces( classPool.get( interfaces ) );
    }
    
    return clazz;
  }

  /**
   * Gets the desired class from the pool.
   * 
   * @param className
   * @return
   * @throws Exception
   */
  public static CtClass getClass(String className) throws Exception
  {
    return classPool.get(className);
  }

  /**
   * Create the links with possible interfaces
   * 
   * @param concreteClass
   * @param interfaces
   */
  public static void createInterfaceLinks(CtClass concreteClass, String[] interfaceNames)
  {
    if(interfaceNames != null && interfaceNames.length > 0)
    {
      try
      {
        for(String interfaceName : interfaceNames)
        {
          boolean found = false;
          for(CtClass existingInterfaces : concreteClass.getInterfaces())
          {
            if(existingInterfaces.getName().equals(interfaceName))
              found = true;
          }
          
          if(!found)
            concreteClass.addInterface(classPool.get(interfaceName));
        }
      }
      catch ( NotFoundException e ) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Create the inheritance link with the absract class provided by the developer
   * 
   * @param concreteClass the concrete class to which to add the inheritance link
   * @param superClass the superClass to set
   */
  public static void createInheritanceLink(CtClass concreteClass, String superClassName)
  {
    if(superClassName != null && superClassName.length() >= 0)
    {
      try
      {
        concreteClass.setSuperclass(classPool.get(superClassName));
      }
      catch ( CannotCompileException e ) {
        e.printStackTrace();
      }
      catch ( NotFoundException e ) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 
   * @param concreteClass
   * @return
   */
  public static CtConstructor generateDefaultConstructor(CtClass concreteClass)
  {
    CtConstructor constructor = null;

    try
    {
      constructor = CtNewConstructor.defaultConstructor(concreteClass);
      concreteClass.addConstructor(constructor);
    }
    catch ( CannotCompileException e ) {
      e.printStackTrace();
    }

    return constructor;
  }

  /**
   * 
   * @param concreteClass
   * @param parameterClasses
   * @param parameterNames
   * @return
   */
  public static CtConstructor generateConstructorWithParameters(CtClass concreteClass, Class<?>[] parameterClasses, String[] parameterNames, boolean[] isTransient)
  {
    CtConstructor constructor = null;
    CtClass[] parameters = new CtClass[parameterClasses.length];

    String constructorBody = "{";

    for (int i = 0; i < parameterClasses.length; i++)
    {
      try
      {
        CtField ctField = null;
        
        parameters[i] = classPool.get(parameterClasses[i].getName());

        try
        {
          ctField = concreteClass.getField(parameterNames[i]);
        }
        catch (NotFoundException nfe)
        {
          ctField = new CtField(parameters[i], parameterNames[i], concreteClass);

          if (ctField.getName().equals("java.lang.Object"))
          {
            ctField.setModifiers(Modifier.PUBLIC);
          }
          else
          {
            ctField.setModifiers(Modifier.PRIVATE);
          }

          concreteClass.addField(ctField);
        }
        
        if(isTransient[i])
        {
          addAnnotation( "javax.persistence.Transient", null, ctField );
        }
      }
      catch (Exception cce) {
        cce.printStackTrace();
      }

      constructorBody += "this." + parameterNames[i] + "=$" + (i+1) + ";";
    }

    constructorBody += "}";

    try {
      constructor = CtNewConstructor.make(parameters, new CtClass[]{}, constructorBody, concreteClass);
      concreteClass.addConstructor( constructor );
    } catch (CannotCompileException e) {
      e.printStackTrace();
    }

    return constructor;
  }

  /**
   * Adds a field of the desired type to the declaring class.
   * 
   * @param fieldType
   * @param fieldName
   * @param declaringClass
   * @return
   * @throws CannotCompileException
   */
  public static CtField addField(CtClass fieldType, String fieldName, CtClass declaringClass) throws CannotCompileException
  {
    return addField( fieldType, fieldName, declaringClass, Modifier.PRIVATE );
  }

  /**
   * Adds a field of the desired type to the declaring class.
   * 
   * @param fieldType
   * @param fieldName
   * @param declaringClass
   * @return
   * @throws CannotCompileException
   */
  public static CtField addField(CtClass fieldType, String fieldName, CtClass declaringClass, int modifier) throws CannotCompileException
  {
    return addField( fieldType, fieldName, declaringClass, modifier, null );
  }

  /**
   * Adds a field of the desired type to the declaring class.
   * 
   * @param fieldType
   * @param fieldName
   * @param declaringClass
   * @param modifier
   * @return
   * @throws CannotCompileException
   */
  public static CtField addField(CtClass fieldType, String fieldName, CtClass declaringClass, int modifier, String initializerExpr) throws CannotCompileException
  {
    CtField field = new CtField( fieldType, decapitalize(fieldName), declaringClass );
    field.setModifiers(modifier);
    
    if(initializerExpr != null)
    {
      declaringClass.addField(field, CtField.Initializer.byExpr(initializerExpr));
    }
    else
    {
      declaringClass.addField(field);
    }
    
    return field;
  }
  
  /**
   * Generates a getter for the field (get<FieldName>) and adds it to the declaring class.
   * 
   * @param field
   * @param interceptorAccess 
   * @return
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  public static CtMethod generateGetter(CtField field, String interceptorAccess) throws NotFoundException, CannotCompileException
  {
    String getterName = "get" + capitalize(field.getName());
    
    CtMethod getter = CtNewMethod.getter( getterName, field );

    if(interceptorAccess != null)
      getter.setBody( interceptorAccess + "." + getterName + "($$);" );

    field.getDeclaringClass().addMethod(getter);

    return getter;
  }
  
  /**
   * Generates a setter for the field (get<FieldName>) and adds it to the declaring class.
   * 
   * @param field
   * @return
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  public static CtMethod generateSetter(CtField field, String interceptorAccess) throws NotFoundException, CannotCompileException
  {
    String setterName = "set" + capitalize(field.getName());
    
    CtMethod setter = CtNewMethod.setter( setterName, field );
    
    if(interceptorAccess != null)
      setter.setBody( interceptorAccess + "." + setterName + "($$);" );
    
    field.getDeclaringClass().addMethod(setter);

    return setter;
  }

  /**
   * Generates getter and setter for the field (get/set<FieldName>) and adds them to the declaring class.
   * 
   * @param field
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  public static void generateGetterAndSetter(CtField field, String interceptorAccess) throws NotFoundException, CannotCompileException
  {
    generateGetter(field, interceptorAccess);
    generateSetter(field, interceptorAccess);
  }

 
  /**
   * Retrieves the sufix to add to set/get and obtain the profile cmp acessors method names in the profile pojo
   * @param fieldName
   * @return
   */
  public static String getPojoCmpAccessorSufix(String fieldName) {
	  return "C" + fieldName; 
  }
  
  /**
   * Adds the selected annotation to the Object, along with the specified memberValues.
   * 
   * @param annotation the FQDN of the annotation 
   * @param memberValues the member values HashMap (name=value)
   * @param toAnnotate the object to be annotated
   */
  public static void addAnnotation(String annotation, LinkedHashMap<String, Object> memberValues, Object toAnnotate)
  {
    if(toAnnotate instanceof CtClass)
    {
      CtClass classToAnnotate = (CtClass) toAnnotate;

      ClassFile cf = classToAnnotate.getClassFile();
      ConstPool cp = cf.getConstPool();

      AnnotationsAttribute attr = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);

      if(attr == null)
      {
        attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
      }

      Annotation a = new Annotation(annotation, cp);

      if(memberValues != null)
      {
        addMemberValuesToAnnotation(a, cp, memberValues);
      }

      attr.addAnnotation( a );

      cf.addAttribute( attr );
    }
    else if(toAnnotate instanceof CtMethod)
    {
      CtMethod methodToAnnotate = (CtMethod) toAnnotate;

      MethodInfo mi = methodToAnnotate.getMethodInfo();
      ConstPool cp = mi.getConstPool();

      AnnotationsAttribute attr = (AnnotationsAttribute) mi.getAttribute(AnnotationsAttribute.visibleTag);

      if(attr == null)
      {
        attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
      }

      Annotation a = new Annotation(annotation, cp);

      if(memberValues != null)
      {
        addMemberValuesToAnnotation(a, cp, memberValues);
      }

      attr.addAnnotation( a );

      mi.addAttribute( attr );
    }
    else if(toAnnotate instanceof CtField)
    {
      CtField fieldToAnnotate = (CtField) toAnnotate;

      FieldInfo fi = fieldToAnnotate.getFieldInfo();
      ConstPool cp = fi.getConstPool();

      AnnotationsAttribute attr = (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.visibleTag);

      if(attr == null)
      {
        attr = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
      }

      Annotation a = new Annotation(annotation, cp);

      if(memberValues != null)
      {
        addMemberValuesToAnnotation(a, cp, memberValues);
      }

      attr.addAnnotation( a );

      fi.addAttribute( attr );
    }
    else
    {
      throw new UnsupportedOperationException("Unknown object type: " + toAnnotate.getClass());
    }
  }

  public static Map<?,?> getInterfaceMethodsFromInterface(String interfaceClassName)
  {
    HashMap<String, CtMethod> interfaceMethods = new HashMap<String, CtMethod>();

    try
    {
      CtClass interfaceClass = classPool.get(interfaceClassName);

      CtMethod[] methods = interfaceClass.getDeclaredMethods();

      for (int i = 0; i < methods.length; i++) {
        interfaceMethods.put(ClassUtils.getMethodKey(methods[i]), methods[i]);
      }

      interfaceMethods.putAll(ClassUtils.getSuperClassesAbstractMethodsFromInterface(interfaceClass));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    return interfaceMethods;
  }

  public static void instrumentBussinesMethod(CtClass concreteClass, CtMethod method, String interceptorAccess) throws Exception
  {
    boolean hasReturnValue = method.getReturnType().toString().equals("void");

    String body = "{" +
    _PLO_PO_ALLOCATION +
//    "System.out.println(\"Calling " + method.getName() + "\");" +
    "Thread t = Thread.currentThread();"+
    "ClassLoader oldClassLoader = t.getContextClassLoader();"+
    "t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());"+
//    "System.out.println(\"profileManagementHandler[\" + this.profileManagementHandler + \"]\");" +
    "try {";

    if(hasReturnValue)
      body += "Object result = " + interceptorAccess + "." + method.getName() + "($$);";
    else
      body += interceptorAccess + "." + method.getName() + "($$);";

    body += hasReturnValue ? "return ($r)result;" : "return;";    

    body+=  
      "}" +
      "catch(java.lang.RuntimeException re)"+
      "{"+
      "  try {"+
      "    this.sleeTransactionManager.rollback();"+
      "    throw new javax.slee.TransactionRolledbackLocalException(\"ProfileLocalObject invocation results in RuntimeException, rolling back.\",re);"+
      "  }" +
      "  catch (Exception e) {"+
//      "    e.printStackTrace();"+
      "    throw new javax.slee.SLEEException(\"System level failure.\",e);"+ 
      "  }"+ 
      "}" +
      "finally"+
      "{"+
      "  if(this.getProfileObject().getProfileContext().getRollbackOnly()){" +
      "  try {"+
      "    this.sleeTransactionManager.rollback();"+
      "  }" +
      "  catch (Exception e) {"+
//      "    e.printStackTrace();"+
      "    throw new javax.slee.SLEEException(\"System level failure.\",e);"+ 
      "  }"+ 
      "}" +
      "  t.setContextClassLoader(oldClassLoader);"+
      "}"+
      "}";

    if(logger.isTraceEnabled())
    {
      logger.trace("Instrumented method, name:"+method.getName()+", with body:\n"+body);
    }

    CtMethod newMethod = CtNewMethod.make(method.getReturnType(), method.getName(), method.getParameterTypes(), method.getExceptionTypes(), body, concreteClass);
    newMethod.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
    
    concreteClass.addMethod(newMethod);
  }

  public static void generateDelegateMethod(CtClass classToBeInstrumented, CtMethod method, String interceptorAccess, boolean recordTxData) throws CannotCompileException, NotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
  {
    // FIXME: should we add check for concrete methods from profileManagementAbstractClass and do clone?

    if (logger.isTraceEnabled()) {
      logger.trace("About to instrument: " + method.getName() + ", into: " + classToBeInstrumented.getName());
    }
    
    method = CtNewMethod.copy(method, classToBeInstrumented, null);
    method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);

    String retStatement = null;
    String retType = null;
    
    try
    {
      if(method.getReturnType() != CtClass.voidType)
      {
        retStatement = "return ($r) result;";
      }
    }
    catch (NotFoundException e) {
      throw e;
    }
    
    boolean hasImpl = interceptorAccess.equals("super");
    
    String body = "{ " 

    	+ClassLoader.class.getName()+ " cl = "+Utility.class.getName()+".switchSafelyClassLoader(null,profileObject);" +
    	//ClassLoader.class.getName()+ " cl = "+Thread.class.getName()+".currentThread().getContextClassLoader();" +
    	///Thread.class.getName()+".currentThread().setContextClassLoader(profileObject.getProfileTable().getProfileSpecificationComponent().getClassLoader());"+
        "  try {" + 
      (recordTxData ? ProfileCallRecorderTransactionData.class.getName() + ".addProfileCall(profileObject);" : "");
        
    if(retStatement != null)
    {
      if(method.getReturnType().isPrimitive())
        retType = ((Class<?>)Class.forName( ((CtPrimitiveType)method.getReturnType()).getWrapperName() ).getField( "TYPE" ).get( null )).getName();
      else
        retType = Class.forName(method.getReturnType().getClassFile().getName()).getName();
    }
    
    body += (retStatement != null ? retType + " result = " : "") + interceptorAccess + "." + method.getName() + "(" + (hasImpl ? "" : "profileObject, ") + "$$);";
    body += retStatement != null ? retStatement : "";
    
    body += "  }" +
      "  finally {" +
      Utility.class.getName()+".switchSafelyClassLoader(cl,null);" +
      //Thread.class.getName()+".currentThread().setContextClassLoader(cl);" +
      (recordTxData ? ProfileCallRecorderTransactionData.class.getName() + ".removeProfileCall(profileObject);" : "") + 
      "  }" + 
      "}";
    
    if(logger.isTraceEnabled())
    {
      logger.trace("Instrumented method, name:"+method.getName()+", with body:\n"+body);
    }
    
    method.setBody(body);
    classToBeInstrumented.addMethod(method);
  }

  private static MemberValue getMemberValue(Object mvValue,ConstPool cp) {
	  
	  MemberValue mv = null;
      
	  if(mvValue instanceof MemberValue){
		  mv = (MemberValue) mvValue;
	  }
	  else if(mvValue instanceof String)
      {
        mv = new StringMemberValue((String)mvValue, cp);
      } 
      else if(mvValue instanceof Annotation)
      {
        mv = new AnnotationMemberValue((Annotation)mvValue, cp);
      } 
      else if(mvValue instanceof Boolean)
      {
        mv = new BooleanMemberValue((Boolean)mvValue, cp);
      } 
      else if(mvValue instanceof Byte)
      {
        mv = new ByteMemberValue((Byte)mvValue, cp);
      } 
      else if(mvValue instanceof Character)
      {
        mv = new CharMemberValue((Character)mvValue, cp);
      } 
      else if(mvValue instanceof Class)
      {
        mv = new ClassMemberValue(((Class<?>)mvValue).getName(), cp);
      } 
      else if(mvValue instanceof Double)
      {
        mv = new DoubleMemberValue((Double)mvValue, cp);
      } 
      else if(mvValue instanceof Enum)
      {
        EnumMemberValue emv = new EnumMemberValue(cp);
    	emv.setType(((Enum<?>)mvValue).getClass().getName());
    	emv.setValue(((Enum<?>)mvValue).name());
        mv = emv;        
      } 
      else if(mvValue instanceof Float)
      {
        mv = new FloatMemberValue((Float)mvValue, cp);
      } 
      else if(mvValue instanceof Integer)
      {
        IntegerMemberValue imv = new IntegerMemberValue(cp);
        imv.setValue(((Integer)mvValue).intValue());
        mv = imv;
      } 
      else if(mvValue instanceof Long)
      {
        mv = new LongMemberValue((Long)mvValue, null);
      } 
      else if(mvValue instanceof Short)
      {
        mv = new ShortMemberValue((Short)mvValue, null);
      } 
      else if(mvValue.getClass().isArray() ) {    	  
    	  ArrayMemberValue amv = new ArrayMemberValue(cp);
    	  MemberValue[] elements = new MemberValue[Array.getLength(mvValue)];
    	  for(int i=0;i<elements.length;i++) {
    		  elements[i] = getMemberValue(Array.get(mvValue, i), cp); 
    	  }
    	  amv.setValue(elements);
    	  mv = amv;
      }
      else
      {
        throw new UnsupportedOperationException("Unknown object type: " + mvValue.getClass());
      }

	  return mv;
  }
  
  /**
   * Private method to add member values to annotation
   * 
   * @param annotation
   * @param cp
   * @param memberValues
   */
  private static void addMemberValuesToAnnotation(Annotation annotation, ConstPool cp, LinkedHashMap<String, Object> memberValues)
  {
    // Get the member value object
    for(String mvName : memberValues.keySet())
    {
      Object mvValue = memberValues.get(mvName);
      MemberValue mv = getMemberValue(mvValue, cp);
      annotation.addMemberValue( mvName, mv );
    }
  }

  /**
   * 
   * @param s
   * @return
   */
  public static String capitalize(String s)
  {
    return s.length() > 0 ? s.substring(0, 1).toUpperCase() + s.substring(1) : s;
  }

  /**
   * 
   * @param s
   * @return
   */
  public static String decapitalize(String s)
  {
    return s.length() > 0 ? s.substring(0, 1).toLowerCase() + s.substring(1) : s;
  }
  
  public static void setClassPool(ClassPool classPool)
  {
    ClassGeneratorUtils.classPool = classPool;
  }

}
