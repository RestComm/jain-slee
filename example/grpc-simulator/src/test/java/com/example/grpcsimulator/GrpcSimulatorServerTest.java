package com.example.grpcsimulator;

import com.example.grpcsimulator.proto.MenuResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.ServerSocket;
import static org.junit.Assert.*;

public class GrpcSimulatorServerTest {
    private GrpcSimulatorServer server;
    private GrpcMenuClient client;

    @Before
    public void setUp() throws Exception {
        int port = freePort();
        server = GrpcSimulatorServer.builder().port(port).build();
        server.start();
        client = new GrpcMenuClient("127.0.0.1", port);
    }

    @After
    public void tearDown() {
        if (client != null) client.close();
        if (server != null) server.close();
    }

    @Test
    public void rootMenuReturnsWelcome() {
        MenuResponse resp = client.resolveMenu("251911000001", "*123#", "");
        assertEquals("OK", resp.getStatus());
        assertNotNull(resp.getSessionId());
        assertTrue(resp.getMenuText().contains("Welcome"));
    }

    @Test
    public void sessionIdPreservedAcrossTurns() {
        MenuResponse r1 = client.resolveMenu("251911000001", "*123#", "sess-X");
        assertEquals("sess-X", r1.getSessionId());
        MenuResponse r2 = client.resolveMenu("251911000001", "1", "sess-X");
        assertEquals("sess-X", r2.getSessionId());
        assertTrue(r2.getMenuText().contains("Balance"));
    }

    @Test
    public void multiLevelNavigateAndEnd() {
        String sid = "nav-test";
        client.resolveMenu("x", "*123#", sid);     // root
        client.resolveMenu("x", "1", sid);          // Balance
        MenuResponse r3 = client.resolveMenu("x", "1", sid); // Top up (leaf)
        assertEquals("END", r3.getStatus());
        assertTrue(r3.getMenuText().contains("Top-up"));
    }

    @Test
    public void backFromRootEndsSession() {
        client.resolveMenu("x", "*123#", "back-test");
        MenuResponse r2 = client.resolveMenu("x", "0", "back-test");
        assertEquals("END", r2.getStatus());
        assertTrue(r2.getMenuText().contains("Goodbye"));
    }

    @Test
    public void invalidChoiceShowsCurrentMenu() {
        client.resolveMenu("x", "*123#", "inv-test");
        MenuResponse r2 = client.resolveMenu("x", "99", "inv-test");
        assertEquals("OK", r2.getStatus());
        assertTrue(r2.getMenuText().contains("Invalid"));
    }

    @Test
    public void deepNavigateBundle5GB() {
        String sid = "bundle-test";
        client.resolveMenu("x", "*123#", sid);
        client.resolveMenu("x", "2", sid);
        MenuResponse r3 = client.resolveMenu("x", "3", sid);
        assertTrue(r3.getMenuText().contains("$20"));
        MenuResponse r4 = client.resolveMenu("x", "1", sid);
        assertEquals("END", r4.getStatus());
        assertTrue(r4.getMenuText().contains("$20 charged"));
    }

    @Test
    public void closeIsClean() {
        server.close(); server.close();
        client.close(); client.close();
    }

    private static int freePort() throws Exception {
        try (ServerSocket s = new ServerSocket(0)) { return s.getLocalPort(); }
    }
}
