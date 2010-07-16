package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 *
 */
public interface ErrorAddress extends Address {
	
    public int getError();
    public void setError(int error);

}
