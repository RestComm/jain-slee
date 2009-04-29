package org.mobicents.slee.container.deployment.profile.jpa;

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
  public static final String CMP_HANDLER = "org.mobicents.slee.container.profile.ProfileCmpHandler";

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
  public static CtConstructor generateConstructorWithParameters(CtClass concreteClass, Class[] parameterClasses, String[] parameterNames, boolean[] isTransient)
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

  /*
  CtConstructor constructorWithParameter = new CtConstructor(parameters, concreteClass);
  String constructorBody = "{";

  // "this();";
  for (int i = 0; i < parameters.length; i++) {

    try {
      CtField ctField = new CtField(parameters[i], parameterNames[i], concreteClass);
      if (ctField.getName().equals("java.lang.Object"))
        ctField.setModifiers(Modifier.PUBLIC);
      else
        ctField.setModifiers(Modifier.PRIVATE);
      concreteClass.addField(ctField);
    } catch (CannotCompileException cce) {
      cce.printStackTrace();
    }
    int paramNumber = i + 1;
    constructorBody += parameterNames[i] + "=$" + paramNumber + ";";
  }

  constructorBody += "}";
  try {
    concreteClass.addConstructor(constructorWithParameter);
    constructorWithParameter.setBody(constructorBody);
    if (logger.isDebugEnabled()) {
      logger.debug("ConstructorWithParameter created: " + constructorBody);
    }
  } catch (CannotCompileException e) {

    throw new DeploymentException("Failed to instrument constructor.", e);
  }
   */

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
   * Generates a getter for the field (get<FieldName>) and adds it to the declaring class.
   * 
   * @param field
   * @return
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  public static CtMethod generateCMPGetter(CtField field) throws NotFoundException, CannotCompileException
  {
    CtMethod getter = CtNewMethod.getter( "get" + capitalize(field.getName()), field );
    CtClass classToBeInstrumented = field.getDeclaringClass();
    
    if (logger.isDebugEnabled()) {
      logger.debug("About to instrument: " + getter.getName() + ", into: " + classToBeInstrumented.getName());
    }

    String getterBody = "{ return ($r) " + CMP_HANDLER + ".getCmpField(profileObject, \"" + field.getName() + "\"); }";

    getter.setBody(getterBody);
    classToBeInstrumented.addMethod(getter);

    CtMethod getterJPA = CtNewMethod.getter( "get" + capitalize(field.getName()) + "JPA", field );
    classToBeInstrumented.addMethod(getterJPA);
    
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
   * Generates a getter for the field (get<FieldName>) and adds it to the declaring class.
   * 
   * @param field
   * @return
   * @throws NotFoundException
   * @throws CannotCompileException
   */
  public static CtMethod generateCMPSetter(CtField field) throws NotFoundException, CannotCompileException
  {
    CtMethod setter = CtNewMethod.setter( "set" + capitalize(field.getName()), field );
    CtClass classToBeInstrumented = field.getDeclaringClass();
    
    if (logger.isDebugEnabled()) {
      logger.debug("About to instrument: " + setter.getName() + ", into: " + classToBeInstrumented.getName());
    }

    String setterBody = "{ Object o = ($w) $1; " + CMP_HANDLER + ".setCmpField(profileObject, \"" + field.getName() + "\", o); }";

    setter.setBody(setterBody);
    classToBeInstrumented.addMethod(setter);

    CtMethod setterJPA =  null;
    if(field.getType().isPrimitive())
    {
      CtPrimitiveType primType = ((CtPrimitiveType)field.getType());
      
      setterJPA = CtNewMethod.make( "public void set" + capitalize(field.getName()) + "JPA(" + primType.getWrapperName() + " arg0) { this." + field.getName() + " = arg0." + primType.getGetMethodName() + "(); }", classToBeInstrumented );
    }
    else
    {
      setterJPA = CtNewMethod.setter( "set" + capitalize(field.getName()) + "JPA", field );
    }

    classToBeInstrumented.addMethod(setterJPA);

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

  public static void generateCMPHandlers(CtField field) throws NotFoundException, CannotCompileException
  {
    generateCMPGetter(field);
    generateCMPSetter(field);
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

  public static Map getInterfaceMethodsFromInterface(String interfaceClassName)
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
    "System.out.println(\"Calling " + method.getName() + "\");" +
    "Thread t = Thread.currentThread();"+
    "ClassLoader oldClassLoader = t.getContextClassLoader();"+
    "t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());"+
    "System.out.println(\"profileManagementHandler[\" + this.profileManagementHandler + \"]\");" +
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
      "    e.printStackTrace();"+
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
      "    e.printStackTrace();"+
      "    throw new javax.slee.SLEEException(\"System level failure.\",e);"+ 
      "  }"+ 
      "}" +
      "  t.setContextClassLoader(oldClassLoader);"+
      "}"+
      "}";

    if(logger.isDebugEnabled())
    {
      logger.info("Instrumented method, name:"+method.getName()+", with body:\n"+body);
    }

    CtMethod newMethod = CtNewMethod.make(method.getReturnType(), method.getName(), method.getParameterTypes(), method.getExceptionTypes(), body, concreteClass);
    newMethod.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
    
    concreteClass.addMethod(newMethod);
  }

  public static void generateDelegateMethod(CtClass classToBeInstrumented, CtMethod method, String interceptorAccess, boolean recordTxData) throws CannotCompileException, NotFoundException
  {
    // FIXME: should we add check for concrete methods from profileManagementAbstractClass and do clone?

    if (logger.isDebugEnabled()) {
      logger.debug("About to instrument: " + method.getName() + ", into: " + classToBeInstrumented.getName());
    }
    
    method = CtNewMethod.copy(method, classToBeInstrumented, null);
    method.setModifiers(method.getModifiers() & ~Modifier.ABSTRACT);
    String retStatement = "";
    try {
      if (method.getReturnType().toString().compareTo("void") == 0) {

      } else {
        retStatement = "return ($r) ";
      }
    } catch (NotFoundException e) {
      // this should nto happen
      throw e;
    }
    String body = 
      "{" +
      "  try {" + 
      (recordTxData ? ProfileCallRecorderTransactionData.class.getName() + ".addProfileCall(this);" : "") + 
      retStatement +
      interceptorAccess + "." + method.getName() + "(" + (interceptorAccess.equals("super") ? "" : "profileObject, ") + "$$);" +
      "  }" +
      "  finally {" +
      (recordTxData ? ProfileCallRecorderTransactionData.class.getName() + ".removeProfileCall(this);" : "") + 
      "  }" + 
      "}";
    
    if (logger.isDebugEnabled()) {
      logger.debug("About to instrumented: " + method.getName() + ", body: " + body);
    }
    method.setBody(body);
    classToBeInstrumented.addMethod(method);
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

      MemberValue mv;

      if(mvValue instanceof String)
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
        mv = new ClassMemberValue(((Class)mvValue).getName(), cp);
      } 
      else if(mvValue instanceof Double)
      {
        mv = new DoubleMemberValue((Double)mvValue, cp);
      } 
      else if(mvValue instanceof Enum)
      {
        // FIXME: How to use this?
        mv = new EnumMemberValue(((Enum)mvValue).ordinal(), ((Enum)mvValue).ordinal(), cp);
      } 
      else if(mvValue instanceof Float)
      {
        mv = new FloatMemberValue((Float)mvValue, cp);
      } 
      else if(mvValue instanceof Integer)
      {
        mv = new IntegerMemberValue((Integer)mvValue, cp);
      } 
      else if(mvValue instanceof Long)
      {
        mv = new LongMemberValue((Long)mvValue, null);
      } 
      else if(mvValue instanceof Short)
      {
        mv = new ShortMemberValue((Short)mvValue, null);
      } 
      else
      {
        throw new UnsupportedOperationException("Unknown object type: " + mvValue.getClass());
      }

      annotation.addMemberValue( mvName, mv );
    }
  }

  /**
   * 
   * @param s
   * @return
   */
  private static String capitalize(String s)
  {
    return s.length() > 0 ? s.substring(0, 1).toUpperCase() + s.substring(1) : s;
  }

  /**
   * 
   * @param s
   * @return
   */
  private static String decapitalize(String s)
  {
    return s.length() > 0 ? s.substring(0, 1).toLowerCase() + s.substring(1) : s;
  }
  
  public static void setClassPool(ClassPool classPool)
  {
    ClassGeneratorUtils.classPool = classPool;
  }

}
