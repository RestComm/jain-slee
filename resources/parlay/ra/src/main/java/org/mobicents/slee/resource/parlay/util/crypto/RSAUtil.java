package org.mobicents.slee.resource.parlay.util.crypto;

//Java Crypto imports


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.openssl.PEMReader;

/**
 * This class provides utility methods for the creation, use and storage of RSA
 * public and private keys.
 */
public class RSAUtil {

    private static final Log logger =
        LogFactory.getLog(RSAUtil.class);

    // Encryption constants
    private static final String CIPHER_ALGORITHM_PROVIDER = "BC";
    
    private static final String IO_EXCEPTION = "IOException";

	//KeyStore holds certificates and private keys
    private static KeyStore keyStore = null;
    
    //password to open keystore
    private static char[] password = "password".toCharArray();

    public RSAUtil() {
        super();
    }
    
    /**
     *  Generates an RSA key pair
     *
     *@param  keySize  the desired RSA key size
     *@return          the generated key pair
     * @exception RSAUtilException
     */
    public static KeyPair generateRSAKeyPair(final int keySize)
        throws RSAUtilException {
        KeyPair keyPair = null;
        try {
            // Generate and RSA key pair
            if (logger.isDebugEnabled()) {
                logger.debug("Getting a KeyPairGenerator");
                logger.debug("Algorithm = RSA");
                logger.debug("Provider  = BC");
            }
            final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
            kpg.initialize(keySize);

            if (logger.isDebugEnabled()) {
                logger.debug("Generating a Key Pair of size " + keySize + " ...");

            }
            keyPair = kpg.generateKeyPair();
            if (logger.isDebugEnabled()) {
                logger.debug("Key pairs generated.");
            }
        }
        catch (GeneralSecurityException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exception:", ex.fillInStackTrace());
            }
            throw new RSAUtilException("GeneralSecurityException", ex);
        }

        return keyPair;
    }
    
    /**
     * @param file
     * @param clientID
     * @return
     * @throws RSAUtilException
     */
    public static RSAPublicKey getPublicKey(final String file, final String clientID) throws RSAUtilException {
        final X509Certificate cert = getCertificate(file, clientID);
        
        return (RSAPublicKey)cert.getPublicKey();
    }
    
    /**
     * @param file
     * @param clientID
     * @return
     * @throws RSAUtilException
     */
    private static X509Certificate getCertificate(final String file, final String clientID)
            throws RSAUtilException {
        if (logger.isDebugEnabled()) {
            logger.debug("Retreiving certificate for client: "+clientID);
        }
        X509Certificate result = null;
        try {
            if (keyStore.containsAlias(clientID+"CSWAY")) {

                result = (X509Certificate)keyStore.getCertificate(clientID);
                
                if (logger.isDebugEnabled()) {
                    logger.debug("Certificate obtained in keyStore.");
                }

            } else {
                
                result = readPEM(file);
                
                keyStore.setCertificateEntry(clientID+"CSWAY", result);
                if (logger.isDebugEnabled()) {
                    logger.debug("Obtained certificate from file.");
                }
            }
        } catch (KeyStoreException e) {
            throw new RSAUtilException("KeyStoreException", e);
        } catch (RSAUtilException e) {
            throw new RSAUtilException("RSAUtilException", e);
        }

        return result;
    }
    
    /**
     * @param clientID
     * @return
     * @throws RSAUtilException
     */
    public static PrivateKey getPrivateKey(final String clientID) throws RSAUtilException {
    	if (logger.isInfoEnabled()) {
        	logger.info("getPrivateKey");
    	}
        PrivateKey result = null;
        
        try {
            
            final Key tmp = keyStore.getKey(clientID, password);
            
            if (tmp instanceof PrivateKey) {

                result = (PrivateKey)tmp;
            } else {
                logger.info("CLASS: "+tmp.getClass());
                throw new RSAUtilException("No private key for clientID: "+clientID);
            }
        } catch (KeyStoreException e) {
            throw new RSAUtilException("KeyStoreException " + e);
        } catch (NoSuchAlgorithmException e) {
            throw new RSAUtilException("NoSuchAlgorithmException " + e);
        } catch (UnrecoverableKeyException e) {
            throw new RSAUtilException("UnrecoverableKeyException " + e);
        }

        return result;
    }

    
    /**
     * @param certFile
     * @return
     * @throws RSAUtilException
     */
    private static X509Certificate readPEM(final String certFile) throws RSAUtilException {
        if (logger.isDebugEnabled()) {
            logger.debug("Reading PEM file: "+certFile);
        }
        
        X509Certificate cert = null;
        PEMReader pemRd = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        
        try {
            fis = new FileInputStream(certFile);
            isr = new InputStreamReader(fis);

            pemRd = new PEMReader(isr);
            
            final Object o= pemRd.readObject();
 
            if (o instanceof X509Certificate) {
                    cert = (X509Certificate)o;
            } else {
                if(logger.isDebugEnabled()) {
                    logger.debug("Error reading PEM file.");
                }
            }

        } catch (FileNotFoundException e) {
            throw new RSAUtilException("File (" + certFile + ") not found.", e);
        } catch (IOException e) {
            throw new RSAUtilException(IO_EXCEPTION, e);
        } finally {
            try {
                fis.close();
                isr.close();
                pemRd.close();
            } catch (IOException e1) {
                throw new RSAUtilException(IO_EXCEPTION, e1);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Read PEM file: "+certFile);
        }
        return cert;
    }
    
    
    /**
     * Encrypts a message using the private key.
     *
     *@param  message the message to be encrypted
     *@param  publicKey the key for the encryption
     *
     *@return the encrypted message
     * @exception RSAUtilException
     */
    public static byte[] encryptMessage(
            final byte[] message,
            final RSAPublicKey publicKey,
            final String cipherAlgorithm)
        throws RSAUtilException {
        byte[] result = null;


        if (logger.isDebugEnabled()) {
            logger.debug("Ciper Algorithm: "+cipherAlgorithm);
        }

        try {
            // Set up the Cipher
            // Algorithm = RSA
            // Mode = Electronic Code Book
            // Padding = PKCS1Padding for RSA ciphers
            final Cipher cipher =
                Cipher.getInstance(cipherAlgorithm, CIPHER_ALGORITHM_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Encrypt the message with the Public Key
            result = cipher.doFinal(message);
        } catch (GeneralSecurityException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exception:", ex.fillInStackTrace());
            }
            throw new RSAUtilException("Invalid key", ex);
        }

        return result;
    }

    /**
     * Decrypts a message using the private key.
     *
     *@param  message the message to be encrypted
     *@param  privateKey the key for the decryption
     *
     *@return the encrypted message
     * @exception RSAUtilException
     */
    public static byte[] decryptMessage(
            final byte[] message,
            final PrivateKey privateKey,
            final String cipherAlgorithm)
        throws RSAUtilException {
        byte[] result = null;

        try {
            // Decrypt the message with the Private Key
            final Cipher cipher =
                Cipher.getInstance(cipherAlgorithm, CIPHER_ALGORITHM_PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = cipher.doFinal(message);
        }
        catch (GeneralSecurityException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exception:", ex.fillInStackTrace());
            }
            throw new RSAUtilException("GeneralSecurityException", ex);
        }

        return result;
    }


    /**
     * loads a keyStore from a .jks file or creates a new one.
     * @param vault
     * @param file
     * @throws RSAUtilException
     */
    public static void loadKeyStore(final String vault, final String file) throws RSAUtilException {

        final String keyStoreLocation = vault+"/"+file;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempting to open keyStore >>"+keyStoreLocation+"<<");
            }
            
            keyStore = KeyStore.getInstance("BKS", "BC");

            try {                    
                keyStore.load(new FileInputStream(keyStoreLocation), password);
                //If keyStore cannot be loaded then initialise new keyStore
            } catch (NoSuchAlgorithmException e1) {
                logger.warn("NoSuchAlgorithmException, keyStore not found creating new keyStore");
                keyStore.load(null, password);
            } catch (CertificateException e1) {
                logger.warn("CertificateException, keyStore not found creating new keyStore");               
                keyStore.load(null, password);
            } catch (FileNotFoundException e1) {
                logger.warn("FileNotFoundException, keyStore not found creating new keyStore");               
                keyStore.load(null, password);
            } catch (IOException e1) {
                logger.warn("IOException, keyStore not found creating new keyStore");                
                keyStore.load(null, password);
            }
        } catch (KeyStoreException e) {
            throw new RSAUtilException("KeyStoreException", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RSAUtilException("NoSuchAlgorithmException", e);
        } catch (CertificateException e) {
            throw new RSAUtilException("CertificateException", e);
        } catch (IOException e) {
            throw new RSAUtilException(IO_EXCEPTION, e);
        } catch (NoSuchProviderException e) {
            throw new RSAUtilException("NoSuchProviderException", e);
        } 
    }

    /**
     * Saves the keyStore to a password protected file.
     * @throws RSAUtilException
     */
    public static void saveKeyStore(final String vault) throws RSAUtilException {
        try {
            keyStore.store(new FileOutputStream(vault+"/mobicents-parlay-ra.jks"), password);
        } catch (KeyStoreException e) {
            throw new RSAUtilException(IO_EXCEPTION, e);
        } catch (NoSuchAlgorithmException e) {
            throw new RSAUtilException(IO_EXCEPTION, e);
        } catch (CertificateException e) {
            throw new RSAUtilException(IO_EXCEPTION, e);
        } catch (FileNotFoundException e) {
            throw new RSAUtilException(IO_EXCEPTION, e);
        } catch (IOException e) {
            throw new RSAUtilException(IO_EXCEPTION, e);
        }
    }

}
