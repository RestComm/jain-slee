/*
 * The Short Message Service resource adaptor type
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package net.java.slee.resource.smpp;

/**
 * Generic SMS provider.
 *
 * @author Oleg Kulikov
 */
public interface SmppProvider {
    
    /**
     * Gets the dialog associated between two entities.
     * If there is no associted dialogs the new one will be created.
     *
     * @param origination the address of the origination party of the dialog 
     * in E164 format.
     * @param destination the address of the destination party of the dialog
     * in E164 format.
     */
    public Dialog getDialog(String origination, String destination);
    
}
