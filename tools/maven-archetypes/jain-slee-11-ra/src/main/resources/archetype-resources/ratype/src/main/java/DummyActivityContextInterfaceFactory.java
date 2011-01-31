package ${package};

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

public interface DummyActivityContextInterfaceFactory {
       public ActivityContextInterface getActivityContextInterface(DummyActivity activity)
            throws NullPointerException, UnrecognizedActivityException, FactoryException;
}
