package ${package};

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;


public class DummyActivityHandle implements ActivityHandle, Serializable {
    
    private String handleId = null;
    private DummyActivity activity;
    
    public DummyActivityHandle(String id) {
        this.handleId = id;
    }

    public DummyActivity getActivity() {
        return activity;
    }
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((DummyActivityHandle)o).handleId.equals(this.handleId);
		}
		else {
			return false;
		}
    }
       
    public int hashCode() {
        return handleId.hashCode();
    }

	/**
	 * @return the handleId
	 */
	public String getHandleId() {
		return handleId;
	}       
    
}
