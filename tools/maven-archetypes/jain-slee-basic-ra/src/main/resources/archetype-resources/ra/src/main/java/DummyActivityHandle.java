package ${package};

import javax.slee.resource.ActivityHandle;
import ${package}.DummyActivity;

public class DummyActivityHandle implements ActivityHandle {
    
    private String handle = null;
    private DummyActivity activity;
    
    public DummyActivityHandle(String id) {
        this.handle = id;
    }

    public DummyActivityHandle(DummyActivity activity) {
        this.handle = activity.getId();
        this.activity = activity;
    }
    
    public DummyActivity getActivity() {
        return activity;
    }
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((DummyActivityHandle)o).handle.equals(this.handle);
		}
		else {
			return false;
		}
    }
       
    public int hashCode() {
        return handle.hashCode();
    }       
    
}
