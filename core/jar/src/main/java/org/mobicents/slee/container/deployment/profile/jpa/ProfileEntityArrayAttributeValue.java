package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;

import javax.slee.Address;

import org.mobicents.slee.container.component.profile.ProfileEntity;

/**
 * Abstract base class of a profile entity array attribute value (jpa entity) wrapper.
 * 
 * @author martins
 * 
 */
public abstract class ProfileEntityArrayAttributeValue {
	
	// FIXME replace long with uuid to work on cluster env
	/**
	 * the id of the jpa entity
	 */
	protected long id;
	
	/**
	 * holds binary elements of the array
	 */
	protected Serializable serializable;
	
	/**
	 * holds all primitive, primitive wrappers + string and address elements of
	 * an array, those can be sued in queries by attr value	 
	 */
	protected String string;
	
	/**
	 * Retrieves the id of the jpa entity
	 * @return
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the id of the jpa entity
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Retrieves the serializable binary field of the entity
	 * @return
	 */
	public Serializable getSerializable() {
		return serializable;
	}
	
	/**
	 * Sets the serializable binary field of the entity
	 * @param serializable
	 */
	public void setSerializable(Serializable serializable) {
		this.serializable = serializable;
	}
	
	/**
	 * Retrieves the string field of the entity
	 * @return
	 */
	public String getString() {
		return string;
	}
	
	/**
	 * Sets the string field of the entity
	 * @param string
	 */
	public void setString(String string) {
		this.string = string;
	}

	/**
	 * NOTE: when adding an instance of this class to a profile entity, it is
	 * required to invoke this method with the owner, otherwise the insert on
	 * jpa is created by hibernate with the entity ids as null.
	 * 
	 * @param profileEntity
	 */
	public abstract void setProfileEntity(ProfileEntity profileEntity); 
	
	// --- AUX METHODS to avoid autoboxing
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public boolean getboolean() {
		return Boolean.parseBoolean(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setboolean(boolean attrValue) {
		setString(Boolean.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public byte getbyte() {
		return Byte.parseByte(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setbyte(byte attrValue) {
		setString(Byte.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public char getchar() {
		return getString().charAt(0);
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setchar(char attrValue) {
		setString(Character.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public double getdouble() {
		return Double.parseDouble(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setdouble(double attrValue) {
		setString(Double.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public float getfloat() {
		return Float.parseFloat(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setfloat(float attrValue) {
		setString(Float.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public int getint() {
		return Integer.parseInt(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setint(int attrValue) {
		setString(Integer.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public long getlong() {
		return Long.parseLong(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setlong(long attrValue) {
		setString(Long.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public short getshort() {
		return Short.parseShort(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setshort(short attrValue) {
		setString(Short.toString(attrValue));
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Boolean getBoolean() {
		return Boolean.valueOf(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setBoolean(Boolean attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Byte getByte() {
		return Byte.valueOf(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setByte(Byte attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Character getCharacter() {
		return Character.valueOf(getString().charAt(0));
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setCharacter(Character attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Double getDouble() {
		return Double.valueOf(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setDouble(Double attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Float getFloat() {
		return Float.valueOf(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setFloat(Float attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Integer getInteger() {
		return Integer.valueOf(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setInteger(Integer attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Long getLong() {
		return Long.valueOf(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setLong(Long attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Short getShort() {
		return Short.valueOf(getString());
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setShort(Short attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
	}
	
	/**
	 * Convenient method to retrieve a specific type stored as string.
	 * 
	 * Note: there are no guarantees of type safety, those should be used by
	 * generate code, free of type checking/safety issues
	 * 
	 * @return
	 */
	public Address getAddress() {
		return (Address) getSerializable();
	}
	
	/**
	 * Stores the attr value, converting and storing in the string field of the
	 * entity
	 * 
	 * @param attrValue
	 */
	public void setAddress(Address attrValue) {
		if (attrValue != null) {
			setString(attrValue.toString());
		}
		else {
			setString(null);
		}
		setSerializable(attrValue);
	}

	@Override
	public int hashCode() {
		return (int)id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ProfileEntityArrayAttributeValue)obj).id == this.id;
		}
		else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "ProfileEntityArrayAttributeValue[ id = "+getId()+" , string = "+getString()+" , serializable = "+getSerializable()+" ]";
	}
	
}
