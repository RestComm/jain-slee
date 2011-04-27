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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class DebugOutputStreamWriter extends OutputStreamWriter {

	public DebugOutputStreamWriter(OutputStream out, String charsetName) throws UnsupportedEncodingException {
		super(out, charsetName);
		// TODO Auto-generated constructor stub
	}
	
	public DebugOutputStreamWriter(OutputStream out, CharsetEncoder enc) {
		super(out, enc);
		// TODO Auto-generated constructor stub
	}
	
	public DebugOutputStreamWriter(OutputStream out, Charset cs) {
		super(out, cs);
		// TODO Auto-generated constructor stub
	}

	
	public DebugOutputStreamWriter(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	public void write(char[] cbuf) throws IOException {
		System.out.println("DebugOutputWriter #"+hashCode()+": "+String.copyValueOf(cbuf));
		super.write(cbuf);
	}
	
	public void write(char[] cbuf, int off, int len) throws IOException {
		System.out.println("DebugOutputWriter #"+hashCode()+": "+String.copyValueOf(cbuf, off, len));
		super.write(cbuf, off, len);
	}
	
	public void write(int c) throws IOException {
		System.out.println("DebugOutputWriter #"+hashCode()+": "+(char)c);
		super.write(c);
	}
	
	public void write(String str) throws IOException {
		System.out.println("DebugOutputWriter #"+hashCode()+": "+str);
		super.write(str);
	}
	
	public void write(String str, int off, int len) throws IOException {
		System.out.println("DebugOutputWriter #"+hashCode()+": "+str.substring(off,off+len));
		super.write(str, off, len);
	}
	
}
