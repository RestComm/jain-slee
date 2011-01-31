package ${package};

import java.io.Serializable;


/**
 * @author ${package}
 */
public class DummyActivity implements Serializable {
    
    private DummyActivityHandle id;
    private DummyProvider dummyProvider;
    //some fields
    private String localAddress;
    private String localPort;
    /** Creates a new instance of DummyActivity 
     * @param dummyProviderImpl */
    public DummyActivity(String id, DummyProvider dummyProvider) {
        this.id = new DummyActivityHandle(id);
        this.dummyProvider = dummyProvider;
    }
    
    public DummyActivityHandle getHandle() {
        return id;
    }
    public void end()
    {
    	this.dummyProvider.terminateActivity(this);
    }

	/**
	 * @return the localAddress
	 */
	public String getLocalAddress() {
		return localAddress;
	}

	/**
	 * @param localAddress the localAddress to set
	 */
	void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	/**
	 * @return the localPort
	 */
	public String getLocalPort() {
		return localPort;
	}

	/**
	 * @param localPort the localPort to set
	 */
	void setLocalPort(String localPort) {
		this.localPort = localPort;
	}
    
}
