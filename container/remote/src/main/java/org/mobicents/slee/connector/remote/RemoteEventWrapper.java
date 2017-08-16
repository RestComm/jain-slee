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
		// default constructor, so JVM can create instance.
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
		out.writeInt(this.serializedEvent.length);
		out.write(this.serializedEvent);
        out.flush();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int len = in.readInt();
		this.serializedEvent = new byte[len];
		in.readFully(serializedEvent);
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
		}

		/**
		 * @param in
		 * @throws IOException
		 */
		public TCCLObjectInputStream(InputStream in) throws IOException {
			super(in);
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
