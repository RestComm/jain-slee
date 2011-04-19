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

package org.mobicents.slee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The <code>ConfigProperties</code> class represents a set of configuration properties
 * for a Resource Adaptor or resource adaptor entity.
 * <p>
 * Configuration properties that start with "<code>javax.slee:</code>" are reserved for
 * use by the SLEE specification.  The following standard configuration properties are defined
 * by the specification and may be used by resource adaptors:
 * <ul>
 *   <li><code>javax.slee:name</code> - a <code>java.lang.String</code> that identifies the
 *       name of the SLEE implementation.
 *   <li><code>javax.slee:vendor</code> - a <code>java.lang.String</code> that identifies the
 *       vendor of the SLEE implementation.
 *   <li><code>javax.slee:version</code> - a <code>java.lang.String</code> that identifies the
 *       version of the SLEE implementation.
 * </ul>
 * SLEE vendor defined configuration properties should be prefixed with "<code>slee-vendor:</code>"
 * and the configuration property name should utilize Java package name conventions.  For example,
 * properties from a SLEE vendor "mycompany" should be prefixed with "<code>slee-vendor:com.mycompany.</code>".
 * @since SLEE 1.1
 */
public final class ConfigProperties implements Serializable, Cloneable {
    /**
     * Create a new <code>ConfigProperties</code> object with an initially empty set of properties.
     */
    public ConfigProperties() {}

    /**
     * Create a new <code>ConfigProperties</code> object prepopulated with a set of properties.
     * @param properties the properties to add to the created <code>ConfigProperties</code> object.
     * @throws NullPointerException if <code>properties</code> is <code>null</code>
     *        or containes null elements.
     */
    public ConfigProperties(Property[] properties) {
        if (properties == null) throw new NullPointerException("properties is null");
        for (int i=0; i<properties.length; i++) addProperty(properties[i]);
    }


    /**
     * Get all properties.
     * @return an array of {@link Property} objects.
     */
    public Property[] getProperties() {
        return (Property[])properties.values().toArray(new Property[properties.size()]);
    }

    /**
     * Get the property with the specified name.
     * @param name the name of the property.
     * @return the {@link Property}, or <code>null</code> if the property does not exist in
     *        this <code>ConfigProperties</code> object.
     */
    public Property getProperty(String name) {
        return (Property)properties.get(name);
    }

    /**
     * Add a property to this <code>ConfigProperties</code> object.
     * @param property the property to add.
     * @throws NullPointerException if <code>property</code> is <code>null</code>.
     * @throws IllegalArgumentException if a property with the same name already
     *        exists.
     */
    public void addProperty(Property property) {
        if (property == null) throw new NullPointerException("property is null");
        if (properties.containsKey(property.getName())) throw new IllegalArgumentException("A property with the name " + property.getName() + " is already present");
        properties.put(property.getName(), property);
    }

    /**
     * Get a string representation for this <code>ConfigProperties</code> object.
     * @return a string representation for this <code>ConfigProperties</code> object.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append('[');
        for (Iterator i=properties.values().iterator(); i.hasNext(); ) {
            Property property = (Property)i.next();
            buf.append('(').
                append(property.getName()).
                append(':').append(property.getType()).
                append('=').append(property.getValue()).
                append(')');
            if (i.hasNext()) buf.append(',');
        }
        buf.append(']');
        return buf.toString();
    }

    /**
     * Creates and returns a deep copy of this <code>ConfigProperties</code> object.
     * Changes to the properties of the object returned from this method will not
     * affect the properties stored by the original object cloned.
     * @return a deep copy of this <code>ConfigProperties</code> object.
     */
    public Object clone() {
        ConfigProperties clone = new ConfigProperties();
        for (Iterator i=properties.values().iterator(); i.hasNext(); ) {
            Property property = (Property)i.next();
            clone.addProperty(new Property(property.getName(), property.getType(), property.getValue()));
        }
        return clone;
    }


    /**
     * The <code>Property</code> class represents a single property.
     */
    public static final class Property implements Serializable {
        /**
         * Create a new property object.
         * @param name the name of the property.
         * @param type the Java class type of the property.
         * @param value the value of the property.  If <code>value</code> is not null, then
         *        <code>value.getClass().getName().equals(type)</code> must hold true.
         * @throws NullPointerException if <code>name</code> or <code>type</code> is <code>null</code>.
         * @throws IllegalArgumentException if <code>value.getClass().getName().equals(type)</code>
         *        is not true.
         */
        public Property(String name, String type, Object value) throws IllegalArgumentException {
            if (name == null) throw new NullPointerException("name is null");
            if (type == null) throw new NullPointerException("type is null");
            this.name = name;
            this.type = type;
            setValue(value);
        }

        /**
         * Get the name of the property.
         * @return the name of the property.
         */
        public String getName() { return name; }

        /**
         * Get the Java class type of the property.
         * @return the Java class type of the property.
         */
        public String getType() { return type; }

        /**
         * Get the current value of the property.
         * @return the current value of the property.  The current value may be <code>null</code>.
         */
        public Object getValue() { return value; }

        /**
         * Set a new value for the property.
         * @param value the value of the property.  If <code>value</code> is not null, then
         *        <code>value.getClass().getName().equals(getType())</code> must hold true.
         * @throws IllegalArgumentException if <code>value.getClass().getName().equals(getType())</code>
         *        is not true.
         */
        public void setValue(Object value) throws IllegalArgumentException {
            if (value == null) {
                this.value = null;
                return;
            }

            if (isAssignable(value.getClass(), type)) {
                this.value = value;
            }
            else {
                throw new IllegalArgumentException("Value is of type " + value.getClass().getName() + ", required type is " + type);
            }
        }

        /**
         * Compare this property for equality with another.
         * @param obj the object to compare this with.
         * @return <code>true</code> if <code>obj</code> is an instance of this class with
         *        the same property name, type, and value as this, <code>false</code> otherwise.
         */
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof Property)) return false;

            Property that = (Property)obj;
            return this.name.equals(that.name)
                && this.type.equals(that.type)
                && this.value == null ? that.value == null : this.value.equals(that.value);
        }

        /**
         * Get a hash code for this property.
         * @return a hash code for this property.  The hash code is based on the property's name.
         */
        public int hashCode() {
            return name.hashCode();
        }

        /**
         * Get a string representation of this property object.
         * @return a string representation of this property object.
         */
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("Property[").append(name).append(':').append(type).append('=').append(value).append(']');
            return buf.toString();
        }

        /**
         * Utility method to convert the string representation of a value to a value object
         * of a specified type.  For example <code>toObject("java.lang.Integer","5") returns a
         * <code>java.lang.Integer</code> object containing the value <code>5</code>.
         * <p>
         * The following types can be converted by this method:
         * <ul>
         *   <li>java.lang.Integer
         *   <li>java.lang.Long
         *   <li>java.lang.Double
         *   <li>java.lang.Float
         *   <li>java.lang.Short
         *   <li>java.lang.Byte
         *   <li>java.lang.Character
         *   <li>java.lang.Boolean
         *   <li>java.lang.String
         * </ul>
         * These types are the supported types of SLEE resource adaptor configuration properties,
         * SBB environment entries, etc.
         * @param type the type of object to return.
         * @param value a string representation of the value to convert.
         * @return an object of the specified type, containing the value represented by the
         *        <code>value</code> argument.
         * @throws NullPointerException if either argument is <code>null</code>.
         * @throws IllegalArgumentException if <code>type</code> is not supported by this method,
         *        or if <code>value</code> is not a legal value for the type.
         */
        public static Object toObject(String type, String value) throws IllegalArgumentException {
            if (type == null) throw new NullPointerException("type is null");
            if (value == null) throw new NullPointerException("value is null");
            try {
                if (type.equals("java.lang.Integer")) { return new Integer(value); }
                else if (type.equals("java.lang.Long")) { return new Long(value); }
                else if (type.equals("java.lang.Double")) { return new Double(value); }
                else if (type.equals("java.lang.Float")) { return new Float(value); }
                else if (type.equals("java.lang.Short")) { return new Short(value); }
                else if (type.equals("java.lang.Byte")) { return new Byte(value); }
                else if (type.equals("java.lang.Character")) {
                    if (value.length() != 1) throw new IllegalArgumentException("Invalid value for java.lang.Character type: " + value);
                    return new Character(value.charAt(0));
                }
                else if (type.equals("java.lang.Boolean")) {
                    if (value.equalsIgnoreCase("true")) return Boolean.TRUE;
                    else if (value.equalsIgnoreCase("false")) return Boolean.FALSE;
                    else throw new IllegalArgumentException("Invalid value for java.lang.Boolean type: " + value);
                }
                else if (type.equals("java.lang.String")) { return value; }
                else throw new IllegalArgumentException("Unsupported type: " + type);
            }
            catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid value for " + type + " type: " + value);
            }
        }

        private boolean isAssignable(Class c, String type) {
            // fast path the usual case
            if (c.getName().equals(type)) return true;

            // check if any superclass and/or implemented interface is of the expected type
            ArrayList checked = new ArrayList();
            ArrayList toCheck = new ArrayList();
            toCheck.add(c);
            while (!toCheck.isEmpty()) {
                c = (Class)toCheck.remove(0);
                if (c.getName().equals(type)) return true;
                if (!checked.add(c)) continue;

                // get superclass & implementented interfaces
                Class superclass = c.getSuperclass();
                if (superclass != null && !superclass.equals(Object.class)) toCheck.add(superclass);
                toCheck.addAll(Arrays.asList(c.getInterfaces()));
            }
            return false;
        }


        private final String name;
        private final String type;
        private Object value;
    }

    private final HashMap properties = new HashMap();
}
