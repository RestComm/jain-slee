/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.component.deployment.classloading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import javax.slee.SbbID;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test to ensure fix for https://github.com/RestComm/jain-slee/issues/49
 * 
 * Tries to replicate the same runtime classloader structure, where parent
 * classloader is jboss based, while slee components have their own URLClassLoaderDomain
 * 
 * By setting preferredPackages we ensure the class is loaded from slee classloader,
 * instead of Jboss one.
 * 
 * We also replicate a typical SBB to Lib dependency.
 */
public class URLClassLoaderDomainImplTest {
    
    public URLClassLoaderDomainImplTest() {
    }

    @Test
    public void testPreferredPackages() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
 
        //use dependencies provided by maven, so this test doesnt rely on any external
        //activity. Keep it as unit test as possible (quick and self-contained)
        URL[] jbossURLS = new URL[1];
        String classpath=System.getProperty("surefire.test.class.path");
        String[] paths = classpath.split(String.valueOf(java.io.File.pathSeparatorChar));
        String workingURL = null;
        for (String path : paths)
        {
            if (path.contains("jta")) {
                workingURL = path;
                break;
            }
        }
        jbossURLS[0] = new File(workingURL).toURI().toURL();

        URLClassLoader jbossLoader = new URLClassLoader(jbossURLS,Thread.currentThread().getClass().getClassLoader());

                
        URLClassLoaderDomainImpl libLoader = new URLClassLoaderDomainImpl(jbossURLS,jbossLoader);
        Set<String> preferredPack = new HashSet();
        preferredPack.add("javax.transaction");
        libLoader.setPreferredPackages(preferredPack);
        URLClassLoaderDomainImpl domainLoader = new URLClassLoaderDomainImpl(new URL[0],jbossLoader);
        domainLoader.setPreferredPackages(preferredPack);
        domainLoader.addDirectDependency(libLoader);

        SbbID compId = new SbbID("sbbName", "sbbVendor", "1.0");
        ComponentClassLoaderImpl compLoader = new ComponentClassLoaderImpl(compId, domainLoader);
        Class<?> loadClass = compLoader.loadClass("javax.transaction.Transaction");
        //load class twice to check duplicate errors
        loadClass = compLoader.loadClass("javax.transaction.Transaction");
        //loadClass.newInstance();
        Assert.assertEquals( URLClassLoaderDomainImpl.class, loadClass.getClassLoader().getClass());
    }
    
}
