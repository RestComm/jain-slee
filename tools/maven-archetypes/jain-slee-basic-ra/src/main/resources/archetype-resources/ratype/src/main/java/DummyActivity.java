package ${package};

import java.io.Serializable;

/**
 *
 * @author Oleg Kulikov
 */
public class DummyActivity implements Serializable {
    
    private String id;
    
    /** Creates a new instance of DummyActivity */
    public DummyActivity(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
}
