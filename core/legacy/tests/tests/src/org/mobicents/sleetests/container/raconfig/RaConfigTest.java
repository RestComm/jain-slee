/*
 * RaConfigTest.java
 *
 * Created on 14 Декабрь 2006 г., 9:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.sleetests.container.raconfig;

import com.opencloud.sleetck.lib.resource.events.TCKResourceEventX;
import java.io.DataInputStream;
import java.io.File;
import java.net.Socket;
import java.net.URL;
import java.rmi.RemoteException;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;




import com.opencloud.sleetck.lib.AbstractSleeTCKTest;

import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.TCKSbbMessage;

import com.opencloud.sleetck.lib.resource.testapi.TCKResourceListener;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;

import com.opencloud.sleetck.lib.testutils.BaseTCKResourceListener;
import com.opencloud.sleetck.lib.testutils.FutureResult;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;


/**
 *
 * @author Oleg Kulikov
 */
public class RaConfigTest extends AbstractSleeTCKTest {
    
    protected FutureResult result;
    
    private String ra = "ResourceAdaptorID[DummyResourceAdaptor#org.mobicents#1.0]";
    private String raLink = "test-link";
    
    SleeCommandInterface sleeCommandInterface = null;
    String path = null;
    
    /** Creates a new instance of RaConfigTest */
    public RaConfigTest() {
    }
    
    public void setUp() throws Exception {
        super.setUp();
    
        String home = System.getProperty("user.dir");
        home=home.replace('\\', '/');
        System.out.println("***** HOME*****\n" + home);
        
        sleeCommandInterface = new SleeCommandInterface("jnp://127.0.0.1:1099");
        path="file:"+ home + "/tests/lib/container/";

        getLog().info("\n========================\nConnecting to resource\n========================\n");
        TCKResourceListener resourceListener = new TestResourceListenerImpl();
        setResourceListener(resourceListener);
    }
    
    private class TestResourceListenerImpl extends BaseTCKResourceListener {
        
        public synchronized void onSbbMessage(TCKSbbMessage message,
                TCKActivityID calledActivity) throws RemoteException {
            Map sbbData = (Map) message.getMessage();
            Boolean sbbPassed = (Boolean) sbbData.get("result");
            String sbbTestMessage = (String) sbbData.get("message");
            
            getLog().info(
                    "Received message from SBB: passed=" + sbbPassed
                    + ", message=" + sbbTestMessage);
            
            if (sbbPassed.booleanValue()) {
                result.setPassed();
            } else {
                result.setFailed(0, sbbTestMessage);
            }
        }
        
        public void onException(Exception exception) throws RemoteException {
            getLog().warning("Received exception from SBB or resource:");
            getLog().warning(exception);
            result.setError(exception);
        }
    }
    
    public void run(FutureResult result) throws Exception {
        this.result = result;
        
        TCKResourceTestInterface resource = utils().getResourceInterface();
        String activityName = utils().getTestParams().getProperty("activityName");
        TCKActivityID activityID = resource.createActivity(activityName);
        
        Thread.currentThread().sleep(500);
        // check configuration arguments passed to the RA via its deployment descriptor
        checkDefaultConfig();
        
        // check overriding config arguments passed to the RA via external properties file at the time when a new entitity is created.  
        checkProvisionedConfig();
        
        utils().getLog().fine("Firing TCKResourceEventX.X1 on activity " + activityName);
        resource.fireEvent(TCKResourceEventX.X1, TCKResourceEventX.X1, activityID, null);
    }
    
    public void checkDefaultConfig() throws Exception {
        boolean ratypeInstalled = false;
        boolean raInstalled = false;
        boolean entityCreated = false;
        boolean entityActivated = false;
        boolean linkCreated = false;
        
        Object opResult=null;
        Socket socket = null;

        try {
            getLog().info("== DEPLOYING DUMMY RA TYPE ==");
            
            opResult = sleeCommandInterface.invokeOperation("-install", path + "dummy-ratype-DU.jar", null, null);
            getLog().info("Result: " + opResult);
            ratypeInstalled = true;
            
            getLog().info("== DEPLOYING DUMMY RA ==");
            opResult=sleeCommandInterface.invokeOperation("-install", path + "dummy-ra-DU.jar", null, null);
            getLog().info(" == DEPLOYED RA:"+ opResult + " ==");
            raInstalled = true;
            
            getLog().info(" == CREATING RA ENTITY:"+ra  +" ==");
            opResult = sleeCommandInterface.invokeOperation("-createRaEntity", ra, raLink, null);
            getLog().info(" == RA ENTITY CREATED:"+opResult+" ==");
            entityCreated = true;
            
            getLog().info(" == ACTIVATING RA ENTITY:"+ra+" ==");
            opResult = sleeCommandInterface.invokeOperation("-activateRaEntity", raLink, null, null);
            getLog().info(" == RA ENTITY ACTIVATED:"+opResult+" ==");
            entityActivated = true;
            
            getLog().info(" == CREATING RA LINK:"+raLink+" ==");
            opResult = sleeCommandInterface.invokeOperation("-createRaLink", raLink, raLink, null);
            getLog().info(" == RA LINK CREATED:"+opResult+" ==");
            linkCreated = true;
            
            socket = new Socket("localhost", 9201);
            socket.getOutputStream().write(1);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            
            boolean booleanProperty = in.readBoolean();
            if (!booleanProperty) result.setFailed(0, "Boolean value is wrong. Should be true");
            
            byte byteProperty = in.readByte();
            if (byteProperty != 1)  result.setFailed(0, "Default byte value should be equal to 1. Instead it is " + byteProperty);
            
            double doubleProperty = in.readDouble();
            if (doubleProperty != 1d)  result.setFailed(0, "Default double value should be equal to 1. Instead it is " + doubleProperty);
            
            float floatProperty = in.readFloat();
            if (floatProperty != 1f)  result.setFailed(0, "Default float value should be equal to 1. Instead it is " + floatProperty);
            
            int intProperty = in.readInt();
            if (intProperty != 1)  result.setFailed(0, "Default int value should be equal to 1. Instead it is " + intProperty);
            
            long longProperty = in.readLong();
            if (longProperty != 1)  result.setFailed(0, "Default long value should be equal to 1. Instead it is " + longProperty);
            
            short shortProperty = in.readShort();
            if (shortProperty != 1)  result.setFailed(0, "Default short value should be equal to 1. Instead it is " + shortProperty);
            
            String stringProperty = in.readUTF();
            if (!stringProperty.equals("text1"))  result.setFailed(0, "Default string value should be equal to 'text1'. Instead it is '" + stringProperty + "'");            
        } finally {
            if (linkCreated) {
                opResult=sleeCommandInterface.invokeOperation("-removeRaLink", raLink, null, null);
                getLog().info(" == RA LINK REMOVED:"+opResult+" ==");
            }
            
            if (entityActivated) {
                opResult=sleeCommandInterface.invokeOperation("-deactivateRaEntity", raLink, null, null);
                getLog().info(" == RA ENTITY DEACTIVATED:"+opResult+" ==");
            }
            
            if (entityCreated) {
                opResult=sleeCommandInterface.invokeOperation("-removeRaEntity",raLink,null,null);
                getLog().info(" == RA ENTITY REMOVED:"+opResult+" ==");
            }
            
            if(raInstalled) {
                opResult=sleeCommandInterface.invokeOperation("-uninstall",path + "dummy-ra-DU.jar",null,null);
                getLog().info(" == RA UNINSTALLED:"+opResult+" ==");
            }
            
            if(ratypeInstalled) {
                opResult=sleeCommandInterface.invokeOperation("-uninstall",path + "dummy-ratype-DU.jar",null,null);
                getLog().info(" == RA TYPE UNINSTALLED:"+opResult+" ==");
            }
            
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    
                }
            }
        }
    }
    
    public void checkProvisionedConfig() throws Exception {
        boolean ratypeInstalled = false;
        boolean raInstalled = false;
        boolean entityCreated = false;
        boolean entityActivated = false;
        boolean linkCreated = false;
        String provisionedLink = raLink+"-provisioned";
        
        Object opResult=null;
        Socket socket = null;
               
        try {
            getLog().info("== DEPOYING DUMMY RA TYPE ==");
            
            opResult = sleeCommandInterface.invokeOperation("-install", path + "dummy-ratype-DU.jar", null, null);
            getLog().info("Result: " + opResult);
            ratypeInstalled = true;
            
            getLog().info("== DEPOYING DUMMY RA ==");
            opResult=sleeCommandInterface.invokeOperation("-install", path + "dummy-ra-DU.jar", null, null);
            getLog().info(" == DEPLOYED RA:"+ opResult + " ==");
            raInstalled = true;
            
            getLog().info(" == CREATING RA ENTITY:"+ra  +" ==");
            opResult = sleeCommandInterface.invokeOperation("-createRaEntity", ra, provisionedLink , path + "dummy-ra.properties");
            getLog().info(" == RA ENTITY CREATED:"+opResult+" ==");
            entityCreated = true;
            
            getLog().info(" == ACTIVATING RA ENTITY:"+ra+" ==");
            opResult = sleeCommandInterface.invokeOperation("-activateRaEntity", provisionedLink, null, null);
            getLog().info(" == RA ENTITY ACTIVATED:"+opResult+" ==");
            entityActivated = true;
            
            getLog().info(" == CREATING RA LINK:"+raLink+" ==");
            opResult = sleeCommandInterface.invokeOperation("-createRaLink", provisionedLink, provisionedLink, null);
            getLog().info(" == RA LINK CREATED:"+opResult+" ==");
            linkCreated = true;
            
            socket = new Socket("localhost", 9201);
            socket.getOutputStream().write(1);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            
            boolean booleanProperty = in.readBoolean();
            if (booleanProperty) result.setFailed(0, "Provisioned boolean value is wrong. Should be false.");
            
            byte byteProperty = in.readByte();
            if (byteProperty != 2) result.setFailed(0, "Provisioned byte value should be equal to 2. Instead it is " + byteProperty);
            
            double doubleProperty = in.readDouble();
            if (doubleProperty != 2d)  result.setFailed(0, "Provisioned double value should be equal to 2. Instead it is " + doubleProperty);
            
            float floatProperty = in.readFloat();
            if (floatProperty != 2f)  result.setFailed(0, "Provisioned float value should be equal to 2. Instead it is " + floatProperty);
            
            int intProperty = in.readInt();
            if (intProperty != 2)  result.setFailed(0, "Provisioned int value should be equal to 2. Instead it is " + intProperty);
            
            long longProperty = in.readLong();
            if (longProperty != 2)  result.setFailed(0, "Provisioned long value should be equal to 2. Instead it is " + longProperty);
            
            short shortProperty = in.readShort();
            if (shortProperty != 2)  result.setFailed(0, "Provisioned short value should be equal to 2. Instead it is " + shortProperty);
            
            String stringProperty = in.readUTF();
            if (!stringProperty.equals("text2"))  result.setFailed(0, "Provisioned string value should be equal to 'text2'. Instead it is '" + stringProperty + "'");
            
        } finally {
            if (linkCreated) {
                opResult=sleeCommandInterface.invokeOperation("-removeRaLink", provisionedLink, null, null);
                getLog().info(" == RA LINK REMOVED:"+opResult+" ==");
            }
            
            if (entityActivated) {
                opResult=sleeCommandInterface.invokeOperation("-deactivateRaEntity", provisionedLink, null, null);
                getLog().info(" == RA ENTITY DEACTIVATED:"+opResult+" ==");
            }
            
            if (entityCreated) {
                opResult=sleeCommandInterface.invokeOperation("-removeRaEntity",provisionedLink,null,null);
                getLog().info(" == RA ENTITY REMOVED:"+opResult+" ==");
            }
            
            if(raInstalled) {
                opResult=sleeCommandInterface.invokeOperation("-uninstall",path + "dummy-ra-DU.jar",null,null);
                getLog().info(" == RA UNINSTALLED:"+opResult+" ==");
            }
            
            if(ratypeInstalled) {
                opResult=sleeCommandInterface.invokeOperation("-uninstall",path + "dummy-ratype-DU.jar",null,null);
                getLog().info(" == RA TYPE UNINSTALLED:"+opResult+" ==");
            }
            
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    
                }
            }
        }
    }
    
}
