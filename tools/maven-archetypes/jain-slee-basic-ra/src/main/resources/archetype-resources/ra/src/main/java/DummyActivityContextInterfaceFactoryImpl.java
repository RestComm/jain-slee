package ${package};

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;

import ${package}.DummyActivity;
import ${package}.DummyActivityContextInterfaceFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

public class DummyActivityContextInterfaceFactoryImpl 
        implements DummyActivityContextInterfaceFactory, ResourceAdaptorActivityContextInterfaceFactory {

    // reference to the SLEE for further usage
    private SleeContainer sleeContainer;
    // the JNDI name of the ActivityContextInterfaceFactory object
    private final String jndiName;
    
    /** Creates a new instance of DummyActivityContextInterfaceFactoryImpl */
    public DummyActivityContextInterfaceFactoryImpl(SleeContainer sleeContainer, String name) {
        this.sleeContainer = sleeContainer;
        this.jndiName = "java:slee/resources/" + name + "/dummyacif";
    }

    public String getJndiName() {
        return jndiName;
    }

    public ActivityContextInterface getActivityContextInterface(DummyActivity activity) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        return new ActivityContextInterfaceImpl(this.sleeContainer, activity.getId());
    }
    
}
