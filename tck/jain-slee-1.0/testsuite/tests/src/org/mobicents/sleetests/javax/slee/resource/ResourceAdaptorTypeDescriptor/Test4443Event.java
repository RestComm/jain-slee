package org.mobicents.sleetests.javax.slee.resource.ResourceAdaptorTypeDescriptor;

import java.io.Serializable;
import java.util.Random;
                                                                                
public final class Test4443Event implements Serializable {

	private static final long serialVersionUID = -7321273558394034455L;

	public Test4443Event() {
        // generate random id
        id = new Random().nextLong() ^ System.currentTimeMillis();
    }
                                                                                
    public boolean equals(Object o) {
        if (o == this) return true;
        return (o instanceof Test4443Event)
            && ((Test4443Event)o).id == id;
    }
                                                                                
    public int hashCode() {
        return (int)id;
    }
                                                                                
    public String toString() {
        return "Test4443Event[" + id + "]";
    }
                                                                                
                                                                                
    private final long id;
}
