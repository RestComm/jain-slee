package net.java.slee.resource.diameter;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import net.java.slee.resource.diameter.activities.BaseProtocolActivity;
import net.java.slee.resource.diameter.activities.ShInterfaceActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class DiameterRAActivityContextInterfaceFactoryImpl implements
        DiameterRAActivityContextInterfaceFactory {
    private static Logger logger=Logger.getLogger(DiameterResourceAdaptor.class);
    
    //OUR SLEE CONTAINER
    private SleeContainer serviceContainer=null;
    //OUR JNDI NAME
    private String jndiName=null;
    //ACIF
    private ActivityContextFactory factory=null;
    private String raEntityName=null;
    public DiameterRAActivityContextInterfaceFactoryImpl(SleeContainer serviceContainer, String name)
    {
        logger.info("=========== CREATING ACIFactory ===================");
        this.serviceContainer=serviceContainer;
        this.jndiName="java:slee/resources/" + name + "/diameterraacif";
        factory=serviceContainer.getActivityContextFactory();
        this.raEntityName = name;
    }
    
    /*
    public ActivityContextInterface getActivityContextInterface(String id)
            throws NullPointerException, UnrecognizedActivityException,
            FactoryException {
        logger.info("==================== DiamerRAActivityContextInterfaceFactoryImpl.getActivityContextInterface(" + id + ") called. ==================");
        if (id == null)
            throw new NullPointerException("DiamerRAActivityContextInterfaceFactoryImpl.getActivityContextInterface(): id is null.");
        return new ActivityContextInterfaceImpl(this.serviceContainer, id); 
    }
*/
    public String getJndiName() {
        return jndiName;
    }

/*
    public ActivityContextInterface getActivityContextInterface(BaseProtocolActivity acivity) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        // TODO Auto-generated method stub
        return null;
    }
*/

    public ActivityContextInterface getActivityContextInterface(ShInterfaceActivity acivity) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        if(acivity==null)
            throw new NullPointerException("=====RECEIVED NULL IN ACIF=====");
        return  new ActivityContextInterfaceImpl(this.serviceContainer,  this.factory.getActivityContext(new SleeActivityHandle(raEntityName, new DiameterRAActivityHandle(acivity.getSessionID()), serviceContainer)).getActivityContextId());
    }

    
    
}
