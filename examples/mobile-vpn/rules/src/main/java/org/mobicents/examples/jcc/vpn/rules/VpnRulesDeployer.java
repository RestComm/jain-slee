/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.examples.jcc.vpn.rules;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.drools.RuleBase;

import org.jboss.system.ServiceMBeanSupport;

/**
 *
 * @author Oleg Kulikov
 */
public class VpnRulesDeployer extends ServiceMBeanSupport implements VpnRulesDeployerMBean {

    private String jndiName;
    private String url;
    private boolean started = false;
    private Thread monitorThread;
    private HashMap spreadsheets = new HashMap();
    private int scanPeriod;
    
    private Logger logger = Logger.getLogger(VpnRulesDeployer.class);

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setDeploymentDirectory(String url) {
        this.url = url.startsWith("file") ? url.substring(6) : url;
    }

    public String getDeploymentDirectory() {
        return "file:/" + url;
    }

    public void setScanPeriod(Integer period) {
        this.scanPeriod = period;
    }
    public Integer getScanPeriod() {
        return scanPeriod;
    }
    
    @Override
    public void startService() throws Exception {
        started = true;        
        monitorThread = new Thread(new Monitor());
        monitorThread.start();
        logger.info("Started VPN route rules Deployer service");
    }

    @Override
    public void stopService() {
        started = false;
        try {
            unbind(jndiName);
        } catch (NamingException e) {
            logger.error("Unexpected error during unbinding rule base from JNDI ", e);
        }
        logger.info("Stopped VPN route rules Deployer service");
    }

    private boolean hasNew(File[] files) {
        int count = 0;
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getAbsolutePath();
            if (!spreadsheets.containsKey(name)) {
                logger.info("Deploying speadsheet " + name);
                spreadsheets.put(name, files[i].lastModified());
                count++;
            }
        }
        return count > 0;
    }

    private boolean hasRemoved(File[] files) {
        List<String> removed = new ArrayList();
        Set<String> names = spreadsheets.keySet();

        //searching deployed spreadsheets on the disk,
        //if spreadsheet is not found append it to list of removed
        for (String name : names) {
            boolean found = false;
            for (int i = 0; i < files.length; i++) {
                if (name.equals(files[i].getAbsolutePath())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                removed.add(name);
            }
        }

        for (String name : removed) {
            logger.info("Undeploying speadsheet " + name);
            spreadsheets.remove(name);
        }

        return removed.size() > 0;
    }

    private RuleBase rebuild(HashMap spreadsheets) throws Exception {
        RuleBuilder builder = new RuleBuilder();
        Set<String> files = spreadsheets.keySet();
        return builder.build(files);
    }

    /**
     * Binds trunk object to the JNDI under the jndiName.
     */
    private void rebind(RuleBase ruleBase) throws NamingException {
        Context ctx = new InitialContext();
        String tokens[] = jndiName.split("/");

        for (int i = 0; i < tokens.length - 1; i++) {
            if (tokens[i].trim().length() > 0) {
                try {
                    ctx = (Context) ctx.lookup(tokens[i]);
                } catch (NamingException e) {
                    ctx = ctx.createSubcontext(tokens[i]);
                }
            }
        }

        ctx.bind(tokens[tokens.length - 1], ruleBase);
    }

    /**
     * Unbounds object under specified name.
     *
     * @param jndiName the JNDI name of the object to be unbound.
     */
    private void unbind(String jndiName) throws NamingException {
        InitialContext initialContext = new InitialContext();
        initialContext.unbind(jndiName);
        logger.info("Unbind rule base from " + jndiName);
    }

    private boolean hasUpdates(File[] files) {
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getAbsolutePath();
            if (spreadsheets.containsKey(name)) {
                long lastModified = (Long) spreadsheets.get(name);
                if (lastModified < files[i].lastModified()) {
                    spreadsheets.put(name, files[i].lastModified());

                    logger.info("Undeploying speadsheet " + name);
                    logger.info("Deploying speadsheet " + name);

                    return true;
                }
            }
        }
        return false;
    }
    
    private class Monitor implements Runnable {

        @SuppressWarnings("static-access")
        public void run() {
            while (started) {
                File dir = new File(url);
                File[] files = dir.listFiles();
                if (files != null && (hasNew(files) || hasRemoved(files) || hasUpdates(files))) {
                    RuleBase ruleBase = null;
                    try {
                        logger.info("Rebuilding rule base[" + jndiName + "]");
                        ruleBase = rebuild(spreadsheets);
                    } catch (Exception e) {
                        logger.error("Could not rebuild rule base ", e);
                    }

                    try {
                        unbind(jndiName);
                    } catch (NameNotFoundException e) {
                    } catch (NamingException e) {
                        logger.error("Unexpected error during unbinding rule base from JNDI ", e);
                        return;
                    }

                    try {
                        rebind(ruleBase);
                        logger.info("Bound rule base to " + jndiName);
                    } catch (Exception e) {
                        logger.error("Coul not bound to JNDI Naming service", e);
                    }
                }

                try {
                    Thread.currentThread().sleep(scanPeriod);
                } catch (InterruptedException e) {
                    return;
                }
            }
        //           logger.info("Monitor thread terminated");
        }
    }
}
