package org.mobicents.slee.services.sip.balancer.mbean;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.tools.sip.balancer.NodeRegisterRMIStub;
import org.mobicents.tools.sip.balancer.SIPNode;

public class BalancerNodeMonitorMBeanImpl extends ServiceMBeanSupport implements
		BalancerNodeMonitorMBeanImplMBean {

	private static final Logger logger = Logger
			.getLogger(BalancerNodeMonitorMBeanImpl.class.getCanonicalName());

    //the balancers to send heartbeat to and our health info
	private String balancers;
    //the balancers names to send heartbeat to and our health info
	private List<String> balancerNames = new ArrayList<String>();
	private Map<String, InetAddress> register = new HashMap<String, InetAddress>();
	private List<String> mbeanRegexes = new CopyOnWriteArrayList<String>();

	//heartbeat interval, can be modified through JMX
	private long heartBeatInterval = 5000;
	private Timer heartBeatTimer = new Timer();
	private TimerTask hearBeatTaskToRun = null;

    private boolean started = false;
    
    private boolean displayBalancerWarining = true;
    private boolean displayBalancerFound = true;
    
    public BalancerNodeMonitorMBeanImpl() {
		super(BalancerNodeMonitorMBeanImplMBean.class);

	}

	@Override
	protected void startService() throws Exception {

		super.startService();

		if (!started) {

			Properties props = new Properties();

			try {
				props.load(this.getClass().getResourceAsStream(
						"bean.properties"));
			} catch (IOException ioe) {
				logger
						.error("!! Failed to load properties from file [bean.properties]");
			}

			if (props.containsKey("balancers")) {

				String balancers = props.getProperty("balancers");
				if (balancers != null && balancers.length() > 0) {
					String[] balancerAddresses = balancers.split(";");
					for (String balancerAddress : balancerAddresses) {
						if(Inet6Util.isValidIP6Address(balancerAddress) || Inet6Util.isValidIPV4Address(balancerAddress)) {
							try {
								this.addBalancerAddress(InetAddress.getByName(balancerAddress).getHostAddress());
							} catch (UnknownHostException e) {
								throw new Exception("Impossible to parse the following sip balancer address " + balancerAddress, e);
							}
						} else {
							this.addBalancerAddress(balancerAddress, 0);
						}
					}
				}		

			}
			if(logger.isDebugEnabled()) {
				logger.debug("[Bean] Fetching regex");
				logger.debug("[Bean] " + props);
			}
			if (props.containsKey("bean.regex")) {
				String[] tmp = ((String) props.get("bean.regex")).split(";");
				if(logger.isDebugEnabled()) {
					logger.debug("[Bean] Regex tab:" + tmp);
				}
				for (String regex : tmp) {
					if(logger.isDebugEnabled()) {
						logger.debug("[Bean] Trying to add regex[" + regex + "]");
					}
					try {
						// ObjectName on = new ObjectName(regex);
						this.addRAMBeanRegex(regex);

					} catch (Exception e) {
						logger.error("Cant add [" + regex
								+ "] as it seems to be of wrong format: "
								+ e.getMessage());
						continue;
					}

				}

			}

			started = true;
		}

		this.hearBeatTaskToRun = new BalancerPingTimerTask(super.server);
		this.heartBeatTimer.scheduleAtFixedRate(this.hearBeatTaskToRun, 0,
				this.heartBeatInterval);
		if(logger.isDebugEnabled()) {
			logger.debug("Created and scheduled tasks for sending heartbeats to the sip balancer.");
		}
		if(logger.isInfoEnabled()) {
			logger.info("Service Started");
		}
	}

	@Override
	protected void stopService() throws Exception {

		balancerNames.clear();
    	register.clear();
    	this.hearBeatTaskToRun.cancel();
		this.hearBeatTaskToRun = null;
		started = false;
    	super.stopService();
    	if(logger.isInfoEnabled()) {
			logger.info("Service Stopped");
		}
	}
	
	public boolean addRAMBeanRegex(String regex)
			throws IllegalArgumentException, NullPointerException {

		if (regex == null)
			throw new NullPointerException("regex cant be null!!");

		// TODO: Add some more strict checking....
		if (mbeanRegexes.contains(regex))
			return false;
		else {
			mbeanRegexes.add(regex);
			return true;
		}
	}


    public boolean removeRAMBeanRegex(String regex) throws NullPointerException {
        if (regex == null)
                throw new NullPointerException("Arg can not be null!!!!");

        if (this.mbeanRegexes.contains(regex)) {
                this.mbeanRegexes.remove(regex);
                return true;
        } else {
                return false;
        }
    }

    public void removeRAMBeanRegex(int index) throws IllegalArgumentException {
        if (index < 0 || index >= this.mbeanRegexes.size())
                throw new IllegalArgumentException(
                                "Index is wrong, it should be [0]<x<["
                                                + mbeanRegexes.size() + "] and it is [" + index
                                                + "]");
        mbeanRegexes.remove(index);
    }

    /**
     * {@inheritDoc}
     */
	public long getHeartBeatInterval() {
		return heartBeatInterval;
	}
	/**
     * {@inheritDoc}
     */
	public void setHeartBeatInterval(long heartBeatInterval) {
		if (heartBeatInterval < 100)
			return;
		this.heartBeatInterval = heartBeatInterval;
		this.hearBeatTaskToRun.cancel();
		this.hearBeatTaskToRun = new BalancerPingTimerTask(super.server);
		this.heartBeatTimer.scheduleAtFixedRate(this.hearBeatTaskToRun, 0,
				this.heartBeatInterval);

	}

	/**
	 * 
	 * @param hostName
	 * @param index
	 * @return
	 */
	private InetAddress fetchHostAddress(String hostName, int index) {
		if (hostName == null)
			throw new NullPointerException("Host name cant be null!!!");

		InetAddress[] hostAddr = null;
		try {
			hostAddr = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException uhe) {
			throw new IllegalArgumentException(
					"HostName is not a valid host name or it doesnt exists in DNS",
					uhe);
		}

		if (index < 0 || index >= hostAddr.length) {
			throw new IllegalArgumentException(
					"Index in host address array is wrong, it should be [0]<x<["
							+ hostAddr.length + "] and it is [" + index + "]");
		}

		InetAddress address = hostAddr[index];
		return address;
	}

	/**
     * {@inheritDoc}
     */
	public String[] getBalancers() {
		return this.balancerNames.toArray(new String[balancerNames.size()]);
	}

	/**
     * {@inheritDoc}
     */
	public void removeBalancerAddress(int index)
			throws IllegalArgumentException {
		if(logger.isDebugEnabled()) {
			logger.debug("[removeBalancerAddress]");
		}
		if (index < 0 || index >= this.balancerNames.size())
			throw new IllegalArgumentException(
					"Index is wrong, it should be [0]<x<["
							+ balancerNames.size() + "] and it is ["
							+ index + "]");

		// This code is clone, as this is ultimate cleaner method to get rid
		// of
		// errors
		String balancerName = null;

		balancerName = balancerNames.get(index);

		if(logger.isDebugEnabled()) {
			logger.debug("Removing following balancer " + balancerName);
		}
		balancerNames.remove(balancerName);
		register.remove(balancerName);

		// balancerInfoSources.remove(balancerName);
		if(logger.isDebugEnabled()) {
			logger.debug("[removeBalancerAddress] END");
		}
	}

	/**
     * {@inheritDoc}
     */
	public boolean addBalancerAddress(String addr) {
		if (addr == null)
			throw new NullPointerException("addr cant be null!!!");

		InetAddress address = null;
		try {
			address = InetAddress.getByName(addr);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(
					"Somethign wrong with host creation.", e);
		}		
		String balancerName = address.getCanonicalHostName();

		if (balancerNames.contains(balancerName))
			return false;

		if(logger.isDebugEnabled()) {
			logger.debug("Adding following balancer name : " + balancerName +"/address:"+ addr);
		}
		balancerNames.add(balancerName);

		register.put(balancerName, address);

		return true;
	}

	/**
     * {@inheritDoc}
     */
	public boolean addBalancerAddress(String hostName, int index) {
		return this.addBalancerAddress(fetchHostAddress(hostName, index)
				.getHostAddress());
	}

	/**
     * {@inheritDoc}
     */
	public boolean removeBalancerAddress(String addr) {
		if (addr == null)
			throw new NullPointerException("addr cant be null!!!");

		InetAddress address = null;
		try {
			address = InetAddress.getByName(addr);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(
					"Something wrong with host creation.", e);
		}

		String balancerName = address.getCanonicalHostName();

		if(logger.isDebugEnabled()) {
			logger.debug("Removing following balancer name : " + balancerName +"/address:"+ addr);
		}
		if (!balancerNames.contains(balancerName))
			return false;

		this.removeBalancerAddress(this.balancerNames.indexOf(balancerName));

		return true;
	}

	/**
     * {@inheritDoc}
     */
	public boolean removeBalancerAddress(String hostName, int index) {
		InetAddress[] hostAddr = null;
		try {
			hostAddr = InetAddress.getAllByName(hostName);
		} catch (UnknownHostException uhe) {
			throw new IllegalArgumentException(
					"HostName is not a valid host name or it doesnt exists in DNS",
					uhe);
		}

		if (index < 0 || index >= hostAddr.length) {
			throw new IllegalArgumentException(
					"Index in host address array is wrong, it should be [0]<x<["
							+ hostAddr.length + "] and it is [" + index + "]");
		}

		InetAddress address = hostAddr[index];

		return this.removeBalancerAddress(address.getHostAddress());
	}

	/**
	 * 
	 * @author <A HREF="mailto:jean.deruelle@gmail.com">Jean Deruelle</A> 
	 *
	 */
	class BalancerPingTimerTask extends TimerTask {
		private MBeanServer server = null;
//        private ObjectName resourceMgmt = null;
		
		public BalancerPingTimerTask(MBeanServer server) {
            super();
            this.server = server;
//            try {
//                this.resourceMgmt = new ObjectName(
//                                    "slee:name=ResourceManagementMBean");
//            } catch (MalformedObjectNameException e) {
//            	
//            } catch (NullPointerException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//            }
		}

		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			if(logger.isDebugEnabled()) {
				logger.debug("Start");
			}
			Properties props = new Properties();

			try {
				props.load(this.getClass().getResourceAsStream(
						"bean.properties"));
			} catch (IOException ioe) {
				logger.error("!! Failed to load properties from file [bean.properties]", ioe);
			}
			if(logger.isDebugEnabled()) {
				logger.debug("[Bean] Fetching regex");
				logger.debug("[Bean] " + props);
			}
			if (props.containsKey("bean.regex")) {
				String[] tmp = ((String) props.get("bean.regex")).split(";");
				if(logger.isDebugEnabled()) {
					logger.debug("[Bean] Regex tab:" + tmp);
				}
				for (String regex : tmp) {
					if(logger.isDebugEnabled()) {
						logger.debug("[Bean] Trying to add regex[" + regex + "]");
					}
					try {
						// ObjectName on = new ObjectName(regex);
						addRAMBeanRegex(regex);
					} catch (Exception e) {
						logger.error("Cant add [" + regex
								+ "] as it seems to be of wrong format: "
								+ e.getMessage());
						continue;
					}

				}

			}
			if(logger.isDebugEnabled()) {
				logger.debug("[BalancerPingTimerTask] Start");
			}
            ArrayList<SIPNode> info = new ArrayList<SIPNode>();
            if(logger.isDebugEnabled()) {
            	logger.debug("[BalancerPingTimerTask] running for [" + mbeanRegexes
                            + "]");
            }
            // We have to prepare list :]
            // for (String s : mbeanRegexes) {
            for (int i = 0; i < mbeanRegexes.size(); i++) {
                String s = mbeanRegexes.get(i);
                if(logger.isDebugEnabled()) {
                	logger.debug("[BalancerPingTimerTask] Regex[" + s + "]");
                }
                // FIXME:If there is error we dont care..?
                try {
                    ObjectName on = new ObjectName(s);
                    Set<ObjectInstance> mbeans = server.queryMBeans(on, null);
                    if(logger.isDebugEnabled()) {
                    	logger.debug("[BalancerPingTimerTask] Got object instances");
                    }
                    for (ObjectInstance oi : mbeans) {
                    	if(logger.isDebugEnabled()) {
                    		logger.debug("[BalancerPingTimerTask] [" + oi.getObjectName() + "]");
                    	}
                        try {
                                String address = (String) server.invoke(oi
                                                .getObjectName(), "getStackAddress", null,
                                                null);
                                // int getStackPort();
                                int port = (Integer) server.invoke(oi
                                                .getObjectName(), "getStackPort", null,
                                                null);
                                // String[] getTransports();
                                String[] transports = (String[]) server.invoke(oi
                                                .getObjectName(), "getTransports", null,
                                                null);
                                try {
                                	String hostName = null;
                                    InetAddress[] aArray = InetAddress
                                                    .getAllByName(address);
                                    if (aArray != null && aArray.length > 0) {
                                            // Damn it, which one we should pick?
                                            hostName = aArray[0].getCanonicalHostName();
                                    }
                                    SIPNode node = new SIPNode(hostName, address, port,
                                            transports);
                                    if(logger.isDebugEnabled()) {
                                    	logger.debug("node found :" + node);
                                    }
                                    info.add(node);
                                } catch (UnknownHostException e) {
                                	logger.error("Impossible to get the ip address of the host "+ address,e);
                                }
                        } catch (InstanceNotFoundException e) {
                        	logger.error("Cannot access the SIP RA MBean ",e);
                        } catch (MBeanException e) {
                        	logger.error("Cannot access the SIP RA MBean ",e);
                        } catch (ReflectionException e) {
                        	logger.error("Cannot access the SIP RA MBean ",e);
                        }
                    }
                } catch (MalformedObjectNameException e) {
                	logger.error("Cannot access the SIP RA MBean ",e);
                } catch (NullPointerException e) {
                    logger.error("Cannot access the SIP RA MBean ",e);
                }
            }
						
			for(InetAddress ah:new HashSet<InetAddress>(register.values())) {
				try {
					Registry registry = LocateRegistry.getRegistry(ah.getHostAddress(),2000);
					NodeRegisterRMIStub reg=(NodeRegisterRMIStub) registry.lookup("SIPBalancer");
					if(logger.isDebugEnabled()) {
						logger.debug("sending keep alive to laod balancer " + info);
					}
					reg.handlePing(info);
					displayBalancerWarining = true;
					if(displayBalancerFound) {
						logger.info("SIP Load Balancer Found!");
						displayBalancerFound = false;
					}
				} catch (Exception e) {
					if(displayBalancerWarining) {
						logger.warn("Cannot access the SIP load balancer RMI registry: " + e.getMessage() +
								"\nIf you need a cluster configuration make sure the SIP load balancer is running.");
						logger.debug("Cannot access the SIP load balancer RMI registry: " , e);
						displayBalancerWarining = false;
					}
					displayBalancerFound = true;
				}
			}
			if(logger.isDebugEnabled()) {
				logger.debug("Finished gathering");
				logger.debug("Gathered info[" + info + "]");
			}
		}
	}

	/**
	 * @param balancers the balancers to set
	 */
	public void setBalancers(String balancers) {
		this.balancers = balancers;
	}
	
}
