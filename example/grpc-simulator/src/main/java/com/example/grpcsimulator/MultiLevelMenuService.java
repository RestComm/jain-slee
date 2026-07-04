package com.example.grpcsimulator;

import com.example.grpcsimulator.proto.MenuRequest;
import com.example.grpcsimulator.proto.MenuResponse;
import com.example.grpcsimulator.proto.UssdMenuServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MultiLevelMenuService extends UssdMenuServiceGrpc.UssdMenuServiceImplBase {
    private static final Logger LOG = LogManager.getLogger(MultiLevelMenuService.class);
    private static final String ROOT = "*123#";
    private final Map<String,Session> sessions = new ConcurrentHashMap<>();
    private final Node rootMenu;

    public MultiLevelMenuService() { this(buildDefaultTree()); }
    public MultiLevelMenuService(Node root) { this.rootMenu = root; }

    @Override
    public void resolveMenu(MenuRequest req, StreamObserver<MenuResponse> obs) {
        String sid = req.getSessionId().isEmpty() ? UUID.randomUUID().toString() : req.getSessionId();
        Session s = sessions.computeIfAbsent(sid, id -> new Session(rootMenu));
        MenuResponse resp;
        if (ROOT.equals(req.getUssdString())) { s.reset(rootMenu); resp = ok(sid, s.node.text); }
        else if ("0".equals(req.getUssdString())) { resp = back(sid, s); }
        else { resp = choose(sid, s, req.getUssdString()); }
        if ("END".equals(resp.getStatus())) sessions.remove(sid);
        obs.onNext(resp); obs.onCompleted();
    }

    private MenuResponse back(String sid, Session s) {
        if (s.history.isEmpty()) return end(sid, "Goodbye!");
        s.node = s.history.remove(s.history.size()-1);
        return ok(sid, s.node.text);
    }
    private MenuResponse choose(String sid, Session s, String key) {
        Node child = s.node.children.get(key);
        if (child == null) return ok(sid, "Invalid. " + s.node.text);
        s.history.add(s.node);
        s.node = child;
        return child.children.isEmpty() ? end(sid, child.text) : ok(sid, child.text);
    }

    static MenuResponse ok(String sid, String text) { return MenuResponse.newBuilder().setSessionId(sid).setStatus("OK").setMenuText(text).build(); }
    static MenuResponse end(String sid, String text) { return MenuResponse.newBuilder().setSessionId(sid).setStatus("END").setMenuText(text).build(); }

    public static class Node {
        final String text;
        final Map<String,Node> children = new ConcurrentHashMap<>();
        public Node(String t) { this.text = t; }
        public Node child(String k, String t) { Node n = new Node(t); children.put(k,n); return n; }
    }

    static class Session { Node node; final java.util.List<Node> history = new java.util.ArrayList<>();
        Session(Node n) { this.node = n; } void reset(Node n) { this.node = n; history.clear(); } }

    static Node buildDefaultTree() {
        Node r = new Node("Welcome!\n1. Balance\n2. Bundle\n3. Settings\n0. Exit");
        Node bal = r.child("1","Balance: $5.00\n1. Top up\n0. Back");
        bal.child("1","Top-up OK!\n0. Back");
        Node bun = r.child("2","Bundle:\n1. 100MB $1\n2. 1GB $5\n3. 5GB $20\n0. Back");
        bun.child("1","100MB active!\n0. Back");
        bun.child("2","1GB active!\n0. Back");
        Node m5 = bun.child("3","5GB confirm:\n1. Buy $20\n0. Back");
        m5.child("1","5GB active! $20 charged.\n0. Back");
        Node set = r.child("3","Settings:\n1. Language\n2. PIN\n0. Back");
        set.child("1","Language: EN\n0. Back");
        set.child("2","PIN changed.\n0. Back");
        return r;
    }
}
