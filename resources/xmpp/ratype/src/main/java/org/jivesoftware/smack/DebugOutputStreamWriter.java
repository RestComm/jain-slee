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
