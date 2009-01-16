package javax.slee.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.MarshalledObject;

/**
 * This class provides serialization and deserialization utility methods used by
 * classes that implement the {@link VendorExtensions} interface.
 */
public class VendorExtensionUtils {
    /**
     * Write the current object and vendor-specific data to the output stream.
     * @param out the object output stream that the current object is being written to.
     * @param vendorData the optional vendor data to include in the serialization stream.
     * @throws IOException if an I/O error occurs while writing to the output stream.
     */
    public static void writeObject(ObjectOutputStream out, Object vendorData) throws IOException {
        // write non-transient fields
        out.defaultWriteObject();
        // check if should we serialize vendor data?
        if (vendorData != null) {
            // serialize the vendor data
            out.writeBoolean(true);
            // write the vendor data in a marshalled object so deserialization can be deferred
            out.writeObject(new MarshalledObject(vendorData));
        }
        else out.writeBoolean(false);
    }

    /**
     * Read the current object in the input stream from the stream, optionally
     * deserializing any vendor-specific data in the stream.
     * @param in the object input stream that the current object is being read from.
     * @param vendorDataDeserializationEnabled boolean flag indicating whether or not
     *        any vendor data in the serialization stream should be deserialized also.
     * @return the vendor-specific data, or <code>null</code> if there isn't any or the
     *        value of the <code>vendorDataDeserializationEnabled</code> parameter was
     *        <code>false</code>.
     * @throws IOException if an I/O error occurs while reading from the input stream.
     * @throws ClassNotFoundException if a class of the serialized objects could not
     *        be found.
     */
    public static Object readObject(ObjectInputStream in, boolean vendorDataDeserializationEnabled) throws IOException, ClassNotFoundException {
        // read non-transient fields
        in.defaultReadObject();
        // read any possible marshalled vendor data from the stream
        MarshalledObject vendorData = in.readBoolean()
            ? (MarshalledObject)in.readObject()
            : null;
        // now figure out what to return
        return (vendorData != null && vendorDataDeserializationEnabled) ? vendorData.get() : null;
    }
}
