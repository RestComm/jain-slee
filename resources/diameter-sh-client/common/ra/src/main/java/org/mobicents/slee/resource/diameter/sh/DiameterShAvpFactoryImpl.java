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

package org.mobicents.slee.resource.diameter.sh;

import java.io.ByteArrayInputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.SupportedApplicationsAvp;
import net.java.slee.resource.diameter.sh.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.diameter.sh.events.avp.SupportedApplicationsAvpImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.SupportedFeaturesAvpImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * Implementation of {@link DiameterShAvpFactory}.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see DiameterShAvpFactory
 */
public class DiameterShAvpFactoryImpl implements DiameterShAvpFactory {

  protected DiameterAvpFactory baseAvpFactory = null;
  private DocumentBuilder docBuilder = null;
  protected final transient Logger logger = Logger.getLogger(this.getClass());

  /**
   * 
   * @param baseAvpFactory
   */
  public DiameterShAvpFactoryImpl(DiameterAvpFactory baseAvpFactory) {
    super();
    this.baseAvpFactory = baseAvpFactory;

    try {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(DiameterShAvpFactoryImpl.class.getClassLoader().getResource("ShDataType.xsd"));

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      //factory.setValidating(true);
      factory.setSchema(schema);

      docBuilder = factory.newDocumentBuilder();
      docBuilder.setErrorHandler(new ErrorHandler() {

        public void error(SAXParseException exception) throws SAXException { throw exception; }

        public void fatalError(SAXParseException exception) throws SAXException { throw exception; }

        public void warning(SAXParseException exception) throws SAXException { throw exception; }
      });
    }
    catch (Exception e) {
      logger.error("Failed to initialize Sh-Data schema validator. No validation will be available.", e);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.DiameterShAvpFactory#createSupportedApplications(long, long, net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp)
   */
  public SupportedApplicationsAvp createSupportedApplications(long authApplicationId, long acctApplicationId, VendorSpecificApplicationIdAvp vendorSpecificApplicationId) {
    // Create the empty AVP
    SupportedApplicationsAvp avp = createSupportedApplications();

    // Set the provided AVP values
    avp.setAuthApplicationId(authApplicationId);
    avp.setAcctApplicationId(acctApplicationId);
    avp.setVendorSpecificApplicationId(vendorSpecificApplicationId);

    return avp;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.DiameterShAvpFactory#createSupportedApplications()
   */
  public SupportedApplicationsAvp createSupportedApplications() {
    return (SupportedApplicationsAvp) AvpUtilities.createAvp( DiameterShAvpCodes.SUPPORTED_APPLICATIONS, DiameterShAvpCodes.SH_VENDOR_ID, null, SupportedApplicationsAvpImpl.class );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.DiameterShAvpFactory#createSupportedFeatures(long, long, long)
   */
  public SupportedFeaturesAvp createSupportedFeatures(long vendorId, long featureListId, long featureList) {
    // Create the empty AVP
    SupportedFeaturesAvp avp = createSupportedFeatures();

    // Set the provided AVP values
    avp.setVendorId( vendorId );
    avp.setFeatureListId( featureListId );
    avp.setFeatureList( featureList );

    return avp;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.DiameterShAvpFactory#createSupportedFeatures()
   */
  public SupportedFeaturesAvp createSupportedFeatures() {
    return (SupportedFeaturesAvp) AvpUtilities.createAvp( DiameterShAvpCodes.SUPPORTED_FEATURES, DiameterShAvpCodes.SH_VENDOR_ID, null, SupportedFeaturesAvpImpl.class );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.DiameterShAvpFactory#createUserIdentity()
   */
  public UserIdentityAvp createUserIdentity() {
    return (UserIdentityAvp) AvpUtilities.createAvp( DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, null, UserIdentityAvpImpl.class );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.DiameterShAvpFactory#getBaseFactory()
   */
  public DiameterAvpFactory getBaseFactory() {
    return this.baseAvpFactory;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.DiameterShAvpFactory#validateUserData(byte[])
   */
  public boolean validateUserData(byte[] userData) {
    if (docBuilder != null && userData!=null)
    {
      try {
        docBuilder.parse(new ByteArrayInputStream(userData));
        return true;
      }
      catch (Throwable e) {
        logger.error("Failure while validating User-Data:", e);
      }
    }
    return false;
  }

}
