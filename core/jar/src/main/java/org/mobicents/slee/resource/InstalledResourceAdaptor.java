/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.resource;

import java.util.HashSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.jboss.util.naming.Util;


/**
 * Represents an Installed resource adaptor. Is indexed in the SleeContainer using the
 * ComponentKey of the resource type. 
 * Each Resource adaptor type maps to 0 or more resource adaptor IDs
 * Each resource adaptor ID maps to 0 or more installed resource adaptor entities.
 * 
 * ResourceAdaptorType ->* ResourceAdaptorID ->* resourceAdaptorEntities.
 * 
 * @author F.Moggia
 * @author M. Ranganathan ( Hacks )
 * 
 */
public class InstalledResourceAdaptor  implements java.io.Serializable {
    private static final String CTX_JAVA_SLEE_RESOURCES = "java:slee/resources";
    private ResourceAdaptorIDImpl resourceAdaptorId;
    private Class resourceAdaptorClass;
    private String raAciName;
    private String[] activityInterfaceNames;
    private String raInterfaceFactory;
    
    private HashSet<ResourceAdaptorEntity> resourceAdaptorEntities;
    
    private ResourceAdaptorDescriptorImpl descriptor;
    
    private ResourceAdaptorType raType;
    
    private static transient Logger log;
    
    static {
        log = Logger.getLogger(InstalledResourceAdaptor.class);
    }
    
    public InstalledResourceAdaptor(){
        this.resourceAdaptorEntities = new HashSet<ResourceAdaptorEntity>();
        
    }
    
    public HashSet<ResourceAdaptorEntity> getResourceAdaptorEntities() {
        return this.resourceAdaptorEntities;
    }
    /**
     * Get the descriptor
     * @return descriptor
     */
    public ResourceAdaptorDescriptorImpl getDescriptor() {
        return this.descriptor;
    }
    

    /**
     * @return Returns the key.
     */
    public ResourceAdaptorIDImpl getKey() {
        return this.resourceAdaptorId;
    }
    /**
     * @return Returns the raAciName.
     */
    public String getRaAciName() {
        return raAciName;
    }
    /**
     * @return Returns the raInterfaceFactory.
     */
    public String getRaInterfaceFactory() {
        return raInterfaceFactory;
    }
    /**
     * @return Returns the raType.
     */
    public ResourceAdaptorType getRaType() {
        return raType;
    }
    /**
     * @return Returns the resourceAdaptorClass.
     */
    public Class getResourceAdaptorClass() {
        return resourceAdaptorClass;
    }
    
    /**
     * Cleans up resources occupied by the RA
     *
     */
    public void uninstall() {
    	this.raType.getResourceAdaptorIDs().remove(descriptor.getID());
    	unbindFromJndi();
    }
    
    public InstalledResourceAdaptor(SleeContainer container, ResourceAdaptorDescriptorImpl raDescr,
            ResourceAdaptorIDImpl resourceAdaptorId ) throws ClassNotFoundException {
        this.resourceAdaptorEntities = new HashSet();
        this.resourceAdaptorId = resourceAdaptorId;
        
        this.resourceAdaptorClass = Thread.currentThread().getContextClassLoader().loadClass(raDescr.getResourceAdaptorClasses().getResourceAdaptorClass());
        this.descriptor = raDescr;
        ResourceAdaptorTypeIDImpl ratRef = 
            (ResourceAdaptorTypeIDImpl) raDescr.getResourceAdaptorType();
        //ComponentKey k = new ComponentKey(ratRef.getName(), ratRef.getVendor(), ratRef.getVersion());
        //logger.debug("InstalledResourceAdaptor looking for RaType: " + k);
        this.raType = container.getResourceManagement().getResourceAdaptorType(ratRef);
        ResourceAdaptorTypeClassEntry classEntry = raType.getRaTypeDescr().getRaTypeClassEntry();  
        raAciName = classEntry.getAcifInterfaceEntry().getInterfaceName();
        this.raInterfaceFactory = classEntry.getRaInterfaceFactoryEntry().getName();
        ActivityTypeEntry[] activityEntries = classEntry.getActivityTypeEntries();
        this.activityInterfaceNames = new String[activityEntries.length];
        for(int i=0;i<activityEntries.length;i++){
            this.activityInterfaceNames[i] = activityEntries[i].getActivityTypeName();
        }
        bindInJndi();
        
    }
    
    private void bindInJndi() {
        
        try {           
            ComponentKey key = resourceAdaptorId.getComponentKey();
            context = Util.createSubcontext(new InitialContext(), CTX_JAVA_SLEE_RESOURCES + "/" + key.getName() + "/" + key.getVendor() + "/" + key.getVersion());
        } catch (NamingException e) {
            log.error("Failed binding RA in JNDI. RA ID: " + resourceAdaptorId, e);
        }
    }
    
    private void unbindFromJndi() {
       
        try {
           context.close();
        } catch (NamingException e) {
            log.error("Failed unbinding RA in JNDI. RA ID: " + resourceAdaptorId, e);
        }
    }
    
    private Context context = null;
}
