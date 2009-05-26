package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

import javax.slee.SLEEException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.ClassPool;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.MProfileCMPInterface;
import org.mobicents.slee.container.deployment.profile.SleeProfileClassCodeGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * Generates the AbstractProfilePojo impl for a specific Profile Specification
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
  private ProfileSpecificationDescriptorImpl profileDescriptor;

  private int profileCombination = -1;
  
  private String deployDir;

  public ConcreteProfileEntityGenerator(ProfileSpecificationComponent profileComponent)
  {
    this.profileComponent = profileComponent;
    this.profileDescriptor = profileComponent.getDescriptor();

    this.profileCombination = SleeProfileClassCodeGenerator.checkCombination(profileComponent);
    logger.info( "Profile combination for " + profileComponent.getProfileSpecificationID() + " = " + this.profileCombination );

    this.deployDir = profileComponent.getDeploymentDir().getAbsolutePath();

    ClassGeneratorUtils.setClassPool( this.profileComponent.getClassPool().getClassPool() );
  }

  public Class generateClass()
  {
    Class clazz = null;
    
    try
    {
      
      MProfileCMPInterface cmpInterface = this.profileDescriptor.getProfileCMPInterface();

      String concreteClassName = cmpInterface.getProfileCmpInterfaceName() + "Pojo";

      // Create the Impl class which implements the profile cmp interface and extends AbstractProfilePojo
      CtClass profileConcreteClass = ClassGeneratorUtils.createClass(concreteClassName, new String[]{cmpInterface.getProfileCmpInterfaceName(), Serializable.class.getName()});      
      ClassGeneratorUtils.createInheritanceLink(profileConcreteClass, ProfileEntity.class.getName());
      
      // Annotate class with @Entity
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Entity", new LinkedHashMap<String, Object>(), profileConcreteClass );
      
      // Annotate class with @IdClass
      LinkedHashMap<String, Object> idClassMVs = new LinkedHashMap<String, Object>();
      idClassMVs.put( "value", JPAProfileId.class );
      ClassGeneratorUtils.addAnnotation( "javax.persistence.IdClass", idClassMVs, profileConcreteClass );

      // Add the table name to map it to ProfileSpecification ID
      LinkedHashMap<String, Object> tableMVs = new LinkedHashMap<String, Object>();
      tableMVs.put( "name",  profileComponent.getProfileCmpInterfaceClass().getSimpleName()+ Math.abs(profileDescriptor.getProfileSpecificationID().hashCode()));
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Table", tableMVs, profileConcreteClass );
     
      // override @id getter methods 
      String getSafeProfileNameMethodSrc = "public String getSafeProfileName() { return super.getSafeProfileName(); }";
      CtMethod getSafeProfileNameMethod = CtNewMethod.make(getSafeProfileNameMethodSrc, profileConcreteClass);
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Id", new LinkedHashMap<String, Object>(), getSafeProfileNameMethod);
      profileConcreteClass.addMethod(getSafeProfileNameMethod);      
      
      String getTableNameMethodSrc = "public String getTableName() { return super.getTableName(); }";
      CtMethod getTableNameMethod = CtNewMethod.make(getTableNameMethodSrc, profileConcreteClass);
      ClassGeneratorUtils.addAnnotation( "javax.persistence.Id", new LinkedHashMap<String, Object>(), getTableNameMethod);
      profileConcreteClass.addMethod(getTableNameMethod);
      
      // CMP fields getters and setters
      generateCMPFieldsWithGettersAndSetters(profileConcreteClass);

      generateConstructors(profileConcreteClass);
      
      profileConcreteClass.getClassFile().setVersionToJava5();
            
      logger.info( "Writing PROFILE CONCRETE CLASS to: " + deployDir );
      
      profileConcreteClass.writeFile( deployDir );
      
      clazz = Thread.currentThread().getContextClassLoader().loadClass(profileConcreteClass.getName());
      
      profileConcreteClass.defrost();
      
    }
    catch ( Exception e ) {
      throw new SLEEException(e.getMessage(),e);
    }
    
    return clazz;
  }

  private void generateConstructors(CtClass profileConcreteClass)
  {
    ClassGeneratorUtils.generateDefaultConstructor(profileConcreteClass);
  }
   
  private void generateCMPFieldsWithGettersAndSetters(CtClass profileConcreteClass) throws Exception
  {
    // Get the CMP interface to generate the getters/setters
    MProfileCMPInterface cmpInterface = profileDescriptor.getProfileCMPInterface();
    
    ClassPool pool = profileComponent.getClassPool();
    
    CtClass cmpInterfaceClass = pool.get( cmpInterface.getProfileCmpInterfaceName() );
    CtClass objectClass = pool.get( Object.class.getName() );
    
    HashMap<String, CtClass> fieldNames = new HashMap<String, CtClass>(); 

    for(CtMethod method : cmpInterfaceClass.getMethods())
    {
      if(!method.getDeclaringClass().equals(objectClass) && method.getName().startsWith( "get" ))
      {
        String fieldName = method.getName().replaceFirst( "get", "" );

        if(!fieldNames.containsKey( fieldName ))
        {
          fieldNames.put( fieldName, method.getReturnType() );          
          CtField genField = ClassGeneratorUtils.addField( method.getReturnType(), fieldName, profileConcreteClass );      
          
          String pojoCmpAccessorSufix = ClassGeneratorUtils.getPojoCmpAccessorSufix(genField.getName());
          CtMethod ctMethod = CtNewMethod.getter( "get" + pojoCmpAccessorSufix, genField );
          profileConcreteClass.addMethod(ctMethod);
          ctMethod = CtNewMethod.setter( "set" + pojoCmpAccessorSufix, genField );
//          if(!genField.getType().isPrimitive())
//            ctMethod.setBody("{this." + genField.getName() +" = " + ProfileEntity.class.getName() + ".makeDeepCopy($1);}");
          profileConcreteClass.addMethod(ctMethod);
        }
      }
    }
  }
  
  public void addClassToPersistenceXML(String className)
  {
    try {
      // Create a builder factory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(false);

      // Create the builder and parse the file
      InputStream is = this.getClass().getClassLoader().getResourceAsStream( "META-INF/persistence.xml" );
      Document doc = factory.newDocumentBuilder().parse(is);

      Element classElement = doc.createElement( "class" );
      classElement.setTextContent( className );

      doc.getElementsByTagName( "persistence-unit" ).item(0).appendChild( classElement );

      javax.xml.transform.TransformerFactory tfactory = TransformerFactory.newInstance();
      javax.xml.transform.Transformer xform = tfactory.newTransformer();
      javax.xml.transform.Source src = new DOMSource(doc);
      java.io.StringWriter writer = new StringWriter();
      Result results = new StreamResult(writer);
      xform.transform(src, results);
      System.out.println(writer.toString());


      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      //initialize StreamResult with File object to save to file
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(doc);
      transformer.transform(source, result);

      // Write to file
      File file = new File(this.getClass().getClassLoader().getResource( "META-INF/persistence.xml" ).toURI());
      Result resultx = new StreamResult(file);

      // Write the DOM document to the file
      Transformer xformer = TransformerFactory.newInstance().newTransformer();
      xformer.transform(source, resultx);
    }
    catch (Exception e) {
    	throw new SLEEException(e.getMessage(),e);
    }
  }

}
