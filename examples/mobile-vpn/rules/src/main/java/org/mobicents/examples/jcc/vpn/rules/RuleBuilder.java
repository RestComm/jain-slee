/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.examples.jcc.vpn.rules;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collection;
import java.util.Set;
import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;

/**
 *
 * @author kulikov
 */
public class RuleBuilder {

    private Logger logger = Logger.getLogger(RuleBuilder.class);
    
    public RuleBase build(Collection<String> files ) throws Exception {
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        final PackageBuilder builder = new PackageBuilder();

        int i = 0;
        for (String file : files) {
            logger.info("Compiling spreadsheet " + file);
            String drl = compiler.compile(load(file), InputType.XLS);
            drl = renameRules(drl, "spreadheet_" + (i++));
            builder.addPackageFromDrl(new StringReader(drl));
        }

        final RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        if (builder.getPackage() != null) {
            ruleBase.addPackage(builder.getPackage());
        }
        return ruleBase;
    }
    
    private  String renameRules(String drl, String prefix) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(drl));
        
        String line = null;
        String res = "";
        
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("rule")) {
                String tokens[] = line.split(" ");
                String name = tokens[1].substring(1, tokens[1].length() - 1);
                
                line = "rule \"" + prefix + name + "\""; 
            }
            res += line + "\n";
        }
        
        return res;
    }
    
    private InputStream load(String name) throws IOException {
        FileInputStream in = new FileInputStream(name);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        int b = -1;
        while ((b = in.read()) != -1) {
            bout.write(b);
        }

        in.close();
        return new ByteArrayInputStream(bout.toByteArray());
    }
    
}
