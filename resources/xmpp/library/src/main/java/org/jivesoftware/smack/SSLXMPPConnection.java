/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jivesoftware.smack;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Creates an SSL connection to a XMPP server using the legacy dedicated SSL port
 * mechanism. Fully compliant XMPP 1.0 servers (e.g. Wildfire 2.4.0) do not
 * require using a dedicated SSL port. Instead, TLS (a standardized version of SSL 3.0)
 * is dynamically negotiated over the standard XMPP port. Therefore, only use this
 * class to connect to an XMPP server if you know that the server does not support
 * XMPP 1.0 TLS connections. 
 *
 * @author Matt Tucker
 */
public class SSLXMPPConnection extends XMPPConnection {

    private static SocketFactory socketFactory = new DummySSLSocketFactory();

    /**
     * Creates a new SSL connection to the specified host on the default
     * SSL port (5223). The IP address of the server is assumed to match the
     * service name.
     *
     * @param host the XMPP host.
     * @throws XMPPException if an error occurs while trying to establish the connection.
     *      Two possible errors can occur which will be wrapped by an XMPPException --
     *      UnknownHostException (XMPP error code 504), and IOException (XMPP error code
     *      502). The error codes and wrapped exceptions can be used to present more
     *      appropiate error messages to end-users.
     */
    public SSLXMPPConnection(String host) throws XMPPException {
        this(host, 5223);
    }

    /**
     * Creates a new SSL connection to the specified host on the specified port. The IP address
     * of the server is assumed to match the service name.
     *
     * @param host the XMPP host.
     * @param port the port to use for the connection (default XMPP SSL port is 5223).
     * @throws XMPPException if an error occurs while trying to establish the connection.
     *      Two possible errors can occur which will be wrapped by an XMPPException --
     *      UnknownHostException (XMPP error code 504), and IOException (XMPP error code
     *      502). The error codes and wrapped exceptions can be used to present more
     *      appropiate error messages to end-users.
     */
    public SSLXMPPConnection(String host, int port) throws XMPPException {
        this(host, port, host);
    }

    /**
     * Creates a new SSL connection to the specified XMPP server on the given host and port.
     *
     * @param host the host name, or null for the loopback address.
     * @param port the port on the server that should be used (default XMPP SSL port is 5223).
     * @param serviceName the name of the XMPP server to connect to; e.g. <tt>jivesoftware.com</tt>.
     * @throws XMPPException if an error occurs while trying to establish the connection.
     *      Two possible errors can occur which will be wrapped by an XMPPException --
     *      UnknownHostException (XMPP error code 504), and IOException (XMPP error code
     *      502). The error codes and wrapped exceptions can be used to present more
     *      appropiate error messages to end-users.
     */
    public SSLXMPPConnection(String host, int port, String serviceName) throws XMPPException {
        super(host, port, serviceName, socketFactory);
    }

    public boolean isSecureConnection() {
        return true;
    }

    /**
     * An SSL socket factory that will let any certifacte past, even if it's expired or
     * not singed by a root CA.
     */
    private static class DummySSLSocketFactory extends SSLSocketFactory {

        private SSLSocketFactory factory;

        public DummySSLSocketFactory() {

            try {
                SSLContext sslcontent = SSLContext.getInstance("TLS");
                sslcontent.init(null, // KeyManager not required
                            new TrustManager[] { new OpenTrustManager() },
                            new java.security.SecureRandom());
                factory = sslcontent.getSocketFactory();
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }

        public static SocketFactory getDefault() {
            return new DummySSLSocketFactory();
        }

        public Socket createSocket(Socket socket, String s, int i, boolean flag)
                throws IOException
        {
            return factory.createSocket(socket, s, i, flag);
        }

        public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr2, int j)
                throws IOException
        {
            return factory.createSocket(inaddr, i, inaddr2, j);
        }

        public Socket createSocket(InetAddress inaddr, int i) throws IOException {
            return factory.createSocket(inaddr, i);
        }

        public Socket createSocket(String s, int i, InetAddress inaddr, int j) throws IOException {
            return factory.createSocket(s, i, inaddr, j);
        }

        public Socket createSocket(String s, int i) throws IOException {
            return factory.createSocket(s, i);
        }

        public String[] getDefaultCipherSuites() {
            return factory.getSupportedCipherSuites();
        }

        public String[] getSupportedCipherSuites() {
            return factory.getSupportedCipherSuites();
        }
    }
}
