package ${package};

import java.io.Serializable;

public class DummyEvent implements Serializable {
    
    private int id;
    
    /** Creates a new instance of Event */
    public DummyEvent() {
    }
    
    public int getId() {
        return id;
    }
}
