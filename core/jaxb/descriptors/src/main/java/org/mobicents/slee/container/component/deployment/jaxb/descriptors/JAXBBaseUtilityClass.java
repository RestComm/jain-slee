/**
 * Start time:18:17:21 2009-01-15<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.logging.Logger;

import javax.slee.management.DeploymentException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

/**
 * This class provides some basic method to operate on JAXB pojo
 * Start time:18:17:21 2009-01-15<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class JAXBBaseUtilityClass {

	
	protected Document descriptorDocument=null;
	private boolean is11=false;
	private static final JAXBContext jaxbContext = initJAXBContext();
	//If its not static we can get logger default
	protected Logger logger=Logger.getLogger(this.getClass().getName());
    private static JAXBContext initJAXBContext() {
        try {
            return JAXBContext
                    .newInstance("org.mobicents.slee.container.component.deployment.jaxb.slee.du"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee.event"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee.ratype"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee.ra"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee.sbb"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee.service"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee.profile"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.du"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.event"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.ra"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.service"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.profile"
                            + ":org.mobicents.slee.container.component.deployment.jaxb.slee11.library");
        } catch (JAXBException e) {
        	//Cause logger is not static.
        	Logger.getLogger(JAXBBaseUtilityClass.class.getName()).severe("failed to create jaxb context"+e);
            return null;
        }
    } 
    
    public static Unmarshaller getUnmarshaller() {
        try {
            return jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
        	e.printStackTrace();
        	Logger.getLogger(JAXBBaseUtilityClass.class.getName()).severe("failed to create unmarshaller: " +e);
            return null;
        }
    } 
    
	/**
	 * @throws DeploymentException 
	 * 
	 */
	public JAXBBaseUtilityClass(Document doc) throws DeploymentException {
		// TODO Auto-generated constructor stub
		this.is11=this.isDoctypeSlee11(doc.getDoctype());
		this.descriptorDocument=doc;
		
	}

	public JAXBBaseUtilityClass() {

	}
	
	public boolean isSlee11()
	{
		return is11;
	}
	
	public static boolean isDoctypeSlee11(DocumentType docType)
	{
		//By default this is: http://java.sun.com/dtd/slee-deployable-unit_1_1.dtd
		return docType.getSystemId().endsWith("_1_1.dtd");
	}
	
	/**
	 * This method must be called prior to any other function after creating. It is supposed to create internal structures to cache JAXB pojos, so they can be accessed in proper way.
	 * @throws DeploymentException 
	 * @throws DeploymentException 
	 */
	public abstract void buildDescriptionMap() throws DeploymentException;
	public abstract Object getJAXBDescriptor();
}
