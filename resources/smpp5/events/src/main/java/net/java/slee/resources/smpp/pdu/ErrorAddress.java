package net.java.slee.resources.smpp.pdu;

public interface ErrorAddress extends Address {
	
    public int getError();
    public void setError(int error);

}
