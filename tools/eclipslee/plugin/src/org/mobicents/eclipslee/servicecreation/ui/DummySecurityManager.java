/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.mobicents.eclipslee.servicecreation.ui;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 * This security manager just allows everything. It's needed for
 * successful RMI while deploying, undeploying, etc.
 * 
 * @author Vladimir Ralev
 *
 */
public class DummySecurityManager extends SecurityManager
{

	public void checkAccept(String arg0, int arg1) {
	}

	public void checkAccess(Thread arg0) {
	}

	@Override
	public void checkAccess(ThreadGroup arg0) {
	}

	@Override
	public void checkAwtEventQueueAccess() {
	}

	@Override
	public void checkConnect(String arg0, int arg1, Object arg2) {
	}

	@Override
	public void checkConnect(String arg0, int arg1) {
	}

	@Override
	public void checkCreateClassLoader() {
	}

	@Override
	public void checkDelete(String arg0) {
	}

	@Override
	public void checkExec(String arg0) {
	}

	@Override
	public void checkExit(int arg0) {
	}

	@Override
	public void checkLink(String arg0) {}

	@Override
	public void checkListen(int arg0) {
	}

	@Override
	public void checkMemberAccess(Class<?> arg0, int arg1) {
	}

	@Override
	public void checkMulticast(InetAddress arg0, byte arg1) {
	}

	@Override
	public void checkMulticast(InetAddress arg0) {
	}

	@Override
	public void checkPackageAccess(String arg0) {
	}

	@Override
	public void checkPackageDefinition(String arg0) {
	}

	@Override
	public void checkPermission(Permission arg0, Object arg1) {
	}

	@Override
	public void checkPermission(Permission arg0) {
	}

	@Override
	public void checkPrintJobAccess() {
	}

	@Override
	public void checkPropertiesAccess() {
	}

	@Override
	public void checkPropertyAccess(String arg0) {
	}

	@Override
	public void checkRead(FileDescriptor arg0) {
	}

	@Override
	public void checkRead(String arg0, Object arg1) {
	}

	@Override
	public void checkRead(String arg0) {
	}

	@Override
	public void checkSecurityAccess(String arg0) {
	}

	@Override
	public void checkSetFactory() {
	}

	@Override
	public void checkSystemClipboardAccess() {
	}

	@Override
	public boolean checkTopLevelWindow(Object arg0) {
		// TODO Auto-generated method stub
		return super.checkTopLevelWindow(arg0);
	}

	@Override
	public void checkWrite(FileDescriptor arg0) {
	}

	@Override
	public void checkWrite(String arg0) {
	}

	@Override
	protected int classDepth(String arg0) {
		// TODO Auto-generated method stub
		return super.classDepth(arg0);
	}

	@Override
	protected int classLoaderDepth() {
		// TODO Auto-generated method stub
		return super.classLoaderDepth();
	}

	@Override
	protected ClassLoader currentClassLoader() {
		// TODO Auto-generated method stub
		return super.currentClassLoader();
	}

	@Override
	protected Class<?> currentLoadedClass() {
		// TODO Auto-generated method stub
		return super.currentLoadedClass();
	}

	@Override
	protected Class[] getClassContext() {
		// TODO Auto-generated method stub
		return super.getClassContext();
	}

	@Override
	public boolean getInCheck() {
		// TODO Auto-generated method stub
		return super.getInCheck();
	}

	@Override
	public Object getSecurityContext() {
		// TODO Auto-generated method stub
		return super.getSecurityContext();
	}

	@Override
	public ThreadGroup getThreadGroup() {
		// TODO Auto-generated method stub
		return super.getThreadGroup();
	}

	@Override
	protected boolean inClass(String arg0) {
		// TODO Auto-generated method stub
		return super.inClass(arg0);
	}

	@Override
	protected boolean inClassLoader() {
		// TODO Auto-generated method stub
		return super.inClassLoader();
	}

}