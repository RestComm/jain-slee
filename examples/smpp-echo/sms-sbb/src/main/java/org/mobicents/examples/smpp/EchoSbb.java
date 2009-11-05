/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mobicents.examples.smpp;

import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import net.java.slee.resource.smpp.ActivityContextInterfaceFactory;
import net.java.slee.resource.smpp.ClientTransaction;
import net.java.slee.resource.smpp.Dialog;
import net.java.slee.resource.smpp.RequestEvent;
import net.java.slee.resource.smpp.ResponseEvent;
import net.java.slee.resource.smpp.ShortMessage;
import net.java.slee.resource.smpp.SmppProvider;
import net.java.slee.resource.smpp.Transaction;
//import org.apache.log4j.Logger;

/**
 *
 * @author kulikov
 */
public abstract class EchoSbb implements Sbb {

    private SbbContext sbbContext;
    private SmppProvider smppProvider;
    private ActivityContextInterfaceFactory smppAcif;
    
//    private final static Logger logger = Logger.getLogger(EchoSbb.class);

    public void onSmsMessage(RequestEvent event, ActivityContextInterface aci) {
        try {
            event.getTransaction().respond(Transaction.STATUS_OK);
        } catch (Exception e) {
        }
        
        ShortMessage smsMessage = event.getMessage();
        String text = smsMessage.getText();

        if (text != null) {
            text = text.trim().toLowerCase();
        }

        String user = smsMessage.getOriginator().substring(1);

 //       logger.info("User address = " + user + ", text= " + text);

        reply(user, text);
        System.out.println("Sending response: " + text);
        
    }

    private void reply(String msidn, String text) {
        Dialog dialog = smppProvider.getDialog(msidn, "0020");
        ShortMessage response = dialog.createMessage();
        response.setOriginator("0020");
        response.setRecipient(msidn);
        response.setText(text);
        ClientTransaction tx = dialog.createSubmitSmTransaction();
        try {
            tx.send(response);
        } catch (IOException e) {
//            logger.error("Unexpected IOError", e);
        }
    }

    public void onSmsReport(ResponseEvent event, ActivityContextInterface aci) {
//        logger.info("SMS message delivered: " + aci.getActivity());
    }
    
    public void setSbbContext(SbbContext sbbContext) {
        this.sbbContext = sbbContext;
        try {
//            logger.info("Called setSbbContext PtinAudioConf!!!");
            Context myEnv = (Context) new InitialContext().lookup("java:comp/env");
            smppProvider = (SmppProvider) myEnv.lookup("slee/resources/smpp/3.4/smppinterface");
            smppAcif = (ActivityContextInterfaceFactory) myEnv.lookup("slee/resources/smpp/3.4/factoryprovider");
        } catch (NamingException ne) {
//            logger.warn("Could not set SBB context:" + ne.getMessage());
        }
    }

    public void unsetSbbContext() {
    }

    public void sbbCreate() throws CreateException {
    }

    public void sbbPostCreate() throws CreateException {
    }

    public void sbbActivate() {
    }

    public void sbbPassivate() {
    }

    public void sbbLoad() {
    }

    public void sbbStore() {
    }

    public void sbbRemove() {
    }

    public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
    }

    public void sbbRolledBack(RolledBackContext arg0) {
    }
}
