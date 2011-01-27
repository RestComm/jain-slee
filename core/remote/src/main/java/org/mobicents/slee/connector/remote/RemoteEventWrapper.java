/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.connector.remote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Small class to wrap event fired from remote server. It exists to hide serialization of real event
 * and not to pass byte[] as argument.
 * 
 * @author baranowb
 * 
 */
public class RemoteEventWrapper implements Externalizable {

	protected transient byte[] serializedEvent;

	public RemoteEventWrapper() {
		super();
		// deff consturctor, so JVM can create instance.
	}

	// add more.
	public RemoteEventWrapper(Serializable event) throws IOException {
		setEvent(event);
	}

	public void setEvent(Serializable event) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(event);
		this.serializedEvent = bos.toByteArray(); // do that in APP thread :)
	}

	public Object getEvent() throws IOException, ClassNotFoundException {

		ByteArrayInputStream bis = new ByteArrayInputStream(this.serializedEvent);
		TCCLObjectInputStream ois = new TCCLObjectInputStream(bis);
		return ois.readObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {

		if(this.serializedEvent == null)
		{
			throw new IOException("No serialized event set.");
		}
		// add id?
		out.write(this.serializedEvent.length);
		out.write(this.serializedEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int len = in.read();
		this.serializedEvent = new byte[len];
		in.read(serializedEvent);

	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(serializedEvent);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoteEventWrapper other = (RemoteEventWrapper) obj;
		if (!Arrays.equals(serializedEvent, other.serializedEvent))
			return false;
		return true;
	}

	
	/**
	 * Small class with hack to make ObjectInputStream aware of Thread Context Class Loader
	 * @author baranowb
	 *
	 */
	private class TCCLObjectInputStream extends ObjectInputStream
	{
		//possibly this should be moved to more generic package, so we can use it, ie. Marshaler objects of RA??
		
		
		
		/**
		 * @throws IOException
		 * @throws SecurityException
		 */
		public TCCLObjectInputStream() throws IOException, SecurityException {
			super();
			// TODO Auto-generated constructor stub
		}

		/**
		 * @param in
		 * @throws IOException
		 */
		public TCCLObjectInputStream(InputStream in) throws IOException {
			super(in);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
			ClassLoader currentTccl = null;
			try {
				currentTccl = Thread.currentThread().getContextClassLoader();
				return currentTccl.loadClass(desc.getName());
			} catch (Exception e) {

			}

			return super.resolveClass(desc);
		}

	}
}
