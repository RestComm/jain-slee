package org.mobicents.slee.resource.parlay.util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides utility methods for the creation, use and storage of RSA
 * public and private keys.
 */
public class CHAPUtil {

    // CONSTANTS
    // .......................................................
    private static final Log logger =
        LogFactory.getLog(CHAPUtil.class);
    
    private static final int HEADER_TOTAL = 5;
    
    private static final int RESPONSE = 2;
    
    private static final int REQUEST = 1;

    // identifer for CHAP Packet
    private transient byte identifier = 0;
    
    private MessageDigest md5Digest;
    
    public CHAPUtil() {
        super();
    }
    
    /**
     * used to build the CHAP Request Packet
     * 
     * @param challenge
     * @return
     */
    public byte[] generateCHAPRequestPacket(final byte[] challenge) {
    	byte[] chapPacket = new byte[5 + challenge.length];
    	
    	if(logger.isDebugEnabled()) {
    		logger.debug("generateCHAPRequestPacket() - start");
    	}
    	
    	chapPacket[0] = REQUEST;    	
    	chapPacket[1] = getNextIdentifier();
    	chapPacket[2] = (byte)(chapPacket.length / 256);
    	chapPacket[3] = (byte)(chapPacket.length % 256);
    	chapPacket[4] = (byte)(challenge.length);
    	
    	for(int i = HEADER_TOTAL; i < chapPacket.length; i++) {
    		chapPacket[i] = challenge[i-HEADER_TOTAL];
    	}
    	
    	if(logger.isDebugEnabled()) {
    		logger.debug("generateCHAPRequestPacket() - end");
    	}
    	
    	return chapPacket;
    }
    
    /**
     * used to build the CHAP response packet
     * 
     * @param identifier
     * @param challenge
     * @param responseName
     * @return
     */
    public byte[] generateCHAPResponsePacket(final byte identifier, final byte[] challenge, final byte[] responseName) {
    	byte[] chapPacket = new byte[HEADER_TOTAL + challenge.length + responseName.length];
    	
    	if(logger.isDebugEnabled()) {
    		logger.debug("generateCHAPResponsePacket() - start");
    	}
    	
    	chapPacket[0] = RESPONSE;    	
    	chapPacket[1] = identifier;
    	chapPacket[2] = (byte)(chapPacket.length / 256);
    	chapPacket[3] = (byte)(chapPacket.length % 256);
    	chapPacket[4] = (byte)(challenge.length);
    	
    	for(int i = 0; i < challenge.length; i++) {
    		chapPacket[i + HEADER_TOTAL] = challenge[i];
    	}    	
    	for(int j = 0; j < responseName.length; j++) {
    		chapPacket[j + challenge.length + HEADER_TOTAL] = responseName[j];
    	}
    	
    	if(logger.isDebugEnabled()) {
    		logger.debug("generateCHAPResponsePacket() - end");
    	}
    	
    	return chapPacket;
    }
    
    /**
     * used to generate the next identifier as it must differ for each challenge
     * 
     * @return
     */
    private byte getNextIdentifier() {
    	identifier++;
    	if (identifier > 255) {
    		identifier = 0;
    	}
    	return identifier;
    }
    
    /**
     * Takes the challenge id, shared secret and the challenge and performs a MD5 Hash
     * operation on it
     * 
     * @param request_id
     * @param sharedSecret
     * @param challenge
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] generateMD5HashChallenge(final byte request_id, final String sharedSecret, final byte[] challenge) throws NoSuchAlgorithmException {
        final byte[] sSecret = sharedSecret.getBytes();
    	byte[] toDigest = new byte[1 + sSecret.length + challenge.length];
    	
    	if(logger.isDebugEnabled()) {
    		logger.debug("generateMD5HashChallenge() - start");
    	}
    	
    	// create the message to be digested
    	toDigest[0] = request_id;

        System.arraycopy(sSecret, 0, toDigest, 0, sSecret.length);
    	
    	for(int j = 1 + sSecret.length; j < toDigest.length; j++) {
    		toDigest[j] = challenge[j - (1 + sSecret.length)];
    	}
    	
    	// perform the message digest
    	MessageDigest md5 = getMd5Digest();
		md5.reset();
		
		if(logger.isDebugEnabled()) {
    		logger.debug("generateMD5HashChallenge() - end");
    	}
		
        return md5.digest(toDigest);
    }
    
    /**
	 * Returns a MD5 digest.
	 * @return MessageDigest object
     * @throws NoSuchAlgorithmException
	 */
	protected MessageDigest getMd5Digest() throws NoSuchAlgorithmException {
		if (md5Digest == null) {
			md5Digest = MessageDigest.getInstance("MD5");			
	    }
		return md5Digest;
	}

}
