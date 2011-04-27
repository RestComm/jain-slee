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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class DebugInputStreamReader extends InputStreamReader {

	public DebugInputStreamReader(InputStream in, String charsetName) throws UnsupportedEncodingException {
		super(in, charsetName);
	}

	public DebugInputStreamReader(InputStream in, CharsetDecoder dec) {
		super(in, dec);
	}

	public DebugInputStreamReader(InputStream in, Charset cs) {
		super(in, cs);		
	}

	public DebugInputStreamReader(InputStream in) {
		super(in);
	}
	
	public int read() throws IOException {
		int ch = super.read();
		System.out.println("DebugInputStreamReader #"+hashCode()+": "+(char)ch);
		return ch;
	}
	
	public int read(char[] cbuf) throws IOException {
		int rtn = super.read(cbuf);
		System.out.println("DebugInputStreamReader #"+hashCode()+": "+String.copyValueOf(cbuf));
		return rtn;
	}
	
	public int read(char[] cbuf, int offset, int length) throws IOException {
		int rtn = super.read(cbuf, offset, length);
		System.out.println("DebugInputStreamReader #"+hashCode()+": "+String.copyValueOf(cbuf,offset,length));
		return rtn;
	}
	
}
