/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.examples.jcc.vpn.rules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.drools.RuleBase;
import org.drools.StatelessSession;

/**
 *
 * @author kulikov
 */
public class Test {

    private SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private ArrayList report = new ArrayList();
    private int executed = 0;
    private int failed = 0;
    
    private void assertEquals(String expected, String actual) throws IllegalArgumentException {
        if (expected == null && actual == null) {
            return;
        }
        if (expected != null && actual != null && expected.equalsIgnoreCase(actual)) {
            return;
        }
        throw new IllegalArgumentException("Expected " + expected + " but was found " + actual);
    }
    
    private void start(int testNo) {
        System.out.print("test # " + testNo +"..............");
    }
    
    private void fail(String testCase, int testNo, String msg) {
        System.out.println("Failed");
        failed++;
        executed++;
        report.add("test case: " + testCase + ", test #" + testNo + ", failed: " + msg);
    }
    
    private void completed() {
        executed++;
        System.out.println("Passed");
    }
    
    private Date getDate(String param) throws ParseException {
        return param.length() > 0 ? dateParser.parse(param) : new Date();
    }
    
    protected void perform(String url) throws Exception {
        File dir = new File(url);
        File[] files = dir.listFiles();
        
        ArrayList<String> rules = new ArrayList();
        ArrayList<String> tests = new ArrayList();
        
        for (int i = 0; i < files.length; i++) {
            String name = files[i].getAbsolutePath();
            if (name.endsWith(".xls")) {
                rules.add(name);
            } else if (name.endsWith(".tst")) {
                tests.add(name);
            }
        }
        
        RuleBuilder builder = new RuleBuilder();
        RuleBase ruleBase = builder.build(rules);
        
        for (String testName : tests) {
            BufferedReader reader = new BufferedReader(new FileReader(testName));
            
            String line = null;            
            System.out.println("Run test case: " + testName);
            
            int i = 0;
            while ((line = reader.readLine()) != null) {  
                line = line.trim();
                if (line.startsWith("#") || line.length() == 0 || line.indexOf("#") > 0) {
                    continue;
                }
                start(++i);
                
                //split RHS and LHS
                
                String[] parts = line.split("==");
                if (parts.length != 2) {
                    fail(testName, i, "wrong test format");
                    continue;
                }
                
                String[] parms = parts[0].split(";");
                if (parms.length != 4) {
                    fail(testName, i, "wrong format of left hand side");
                    continue;
                }
                
                Date time = null;
                try {
                    time = getDate(parms[0]);
                } catch (ParseException e) {
                    fail(testName, i, "Wrong date format");
                    continue;
                }
                
                Call call = new Call(parms[1], parms[2], parms[3]);
                
                try {
                    StatelessSession session = ruleBase.newStatelessSession();
                    session.execute(call);
                    session.execute(call);
                } catch (Exception e) {
                    fail(testName, i, e.getMessage());
                    continue;
                }
                
                String[] results = parts[1].split(";");                
                
                String destination = results.length >= 1 ? results[0] :  null;
                String callerID = results.length == 2 ? results[1] : null;
                
                try {
                    assertEquals(destination, call.getRouteAddress());
                } catch (Exception e) {
                    fail(testName, i, "Destination: " + e.getMessage() + 
                            ", DEBUG: GroupName=" + call.getGroupName() + ", VPN=" + call.getNetworkID());
                    continue;
                }
                
                try {
                    assertEquals(callerID, call.getCallerID());
                } catch (Exception e) {
                    fail(testName, i, "CallerID: " + e.getMessage());
                    continue;
                }
                
                completed();
            }
        }
        
    }
    
    protected void report() {
        System.out.println("==========================================");
        System.out.println("Executed: " + executed + " tests");
        System.out.println("Failed: " + failed + " tests");
        System.out.println("==========================================");
        System.out.println("Summary: ");
        
        for (int i = 0; i < report.size(); i++) {
            System.out.println(report.get(i));
        }        
    }
    
    public static void main(String[] args) throws Exception {
        String url = args.length > 0 ? args[0] : null;
        if (url == null) {
            url = System.getenv("VPNTEST_HOME") + "/tables";
        }
        //url = "c:\\projects\\volgograd\\voicevpn\\dt\\";
        if (url == null) {
            System.err.println("Path to tests and rule base must be specified");
            return;
        }
        
        Test test = new Test();
        test.perform(url);
        test.report();
    }
}
