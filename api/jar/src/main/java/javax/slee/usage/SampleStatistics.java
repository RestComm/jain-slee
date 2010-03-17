package javax.slee.usage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.slee.management.VendorExtensionUtils;
import javax.slee.management.VendorExtensions;

/**
 * This class provides basic statistical information for a sample-type usage parameter.
 */
public final class SampleStatistics implements VendorExtensions, Serializable {
    /**
     * Create a new <code>SampleStatistics</code> object for containing statistical
     * information for a sample-type usage parameter in the case where no samples
     * have been counted yet.
     * <p>
     * This constructor is equivalent to {@link #SampleStatistics(long, long, long, double) SampleStatistics(0, Long.MAX_VALUE, Long.MIN_VALUE, 0.0)}.</ul>
     */
    public SampleStatistics() {
        this(0, Long.MAX_VALUE, Long.MIN_VALUE, 0.0);
    }

    /**
     * Create a new <code>SampleStatistics</code> object containing statistical
     * information for a sample-type usage parameter.
     * @param sampleCount the number of samples recorded for the usage parameter.
     * @param min the minimum sample value recorded.
     * @param max the maximum sample value recorded.
     * @param mean the mean of the sample values recorded.
     */
    public SampleStatistics(long sampleCount, long min, long max, double mean) {
        this.sampleCount = sampleCount;
        this.min = min;
        this.max = max;
        this.mean = mean;
    }

    /**
     * Get the number of samples recorded for the usage parameter since either the service
     * was deployed or the usage parameter was last reset (whichever occurred most recently).
     * @return the number of samples recorded.
     */
    public long getSampleCount() {
        return sampleCount;
    }

    /**
     * Get the minimum sample value recorded for the usage parameter since either the service
     * was deployed or the usage parameter was last reset (whichever occurred most recently).
     * @return the minimum sample value.  This will be equal to <code>Long.MAX_VALUE</code>
     *        if no samples have yet been recorded.
     */
    public long getMinimum() {
        return min;
    }

    /**
     * Get the maximum sample value recorded for the usage parameter since either the service
     * was deployed or the usage parameter was last reset (whichever occurred most recently).
     * @return the maximum sample value.  This will be equal to <code>Long.MIN_VALUE</code>
     *        if no samples have yet been recorded.
     */
    public long getMaximum() {
        return max;
    }

    /**
     * Get the mean of sample values recorded for the usage parameter since either the service
     * was deployed or the usage parameter was last reset (whichever occurred most recently).
     * @return the mean of sample values.  This will be equal to <tt>0.0</tt> if no samples
     *        have yet been recorded.
     */
    public double getMean() {
        return mean;
    }

    /**
     * Enable the serialization of vendor-specific data for objects of this class.
     * This method is typically used by a SLEE implementation that wants to export
     * vendor-specific data with objects of this class to management clients.
     * <p>
     * By default, any vendor-specific data included in an object of this class will
     * not be included in the serialization stream when the object is serialized.
     * Invoking this method changes this behavior so that vendor-specific data is
     * included in the serialization stream when an object of this class is serialized.
     * <p>
     * This method should only be invoked if the vendor-specific data is serializable
     * via standard Java serialization means.
     * @since SLEE 1.1
     * @see #disableVendorDataSerialization
     * @see #setVendorData
     */
    public static void enableVendorDataSerialization() {
        vendorDataSerializationEnabled = true;
    }

    /**
     * Disable the serialization of vendor-specific data for objects of this class.
     * <p>
     * If the serialization of vendor-specific data for objects of this class has
     * been enabled via the {@link #enableVendorDataSerialization} method, this
     * method disables that behavior again.
     * @since SLEE 1.1
     */
    public static void disableVendorDataSerialization() {
        vendorDataSerializationEnabled = false;
    }

    /**
     * Enable the deserialization of vendor-specific data for objects of this class.
     * This method is typically used by a management client that wants to obtain any
     * vendor-specific data included in the serialization stream of objects of this
     * class.
     * <p>
     * By default, any vendor-specific data included in the serialization stream of
     * objects of this class is discarded upon deserialization.  Invoking this method
     * changes that behavior so that the vendor-specific data is also deserialized
     * when an object of this class is deserialized.  A management client that enables
     * the deserialization of vendor-specific data must ensure that any necessary
     * classes required to deserialize that data is available in the relevant
     * classloader.
     * @since SLEE 1.1
     * @see #disableVendorDataDeserialization
     * @see #getVendorData
     */
    public static void enableVendorDataDeserialization() {
        vendorDataDeserializationEnabled = true;
    }

    /**
     * Disable the deserialization of vendor-specific data for objects of this class.
     * <p>
     * If the deserialization of vendor-specific data for objects of this class has
     * been enabled via the {@link #enableVendorDataDeserialization} method, this
     * method disables that behavior again.
     * @since SLEE 1.1
     */
    public static void disableVendorDataDeserialization() {
        vendorDataDeserializationEnabled = false;
    }

    /**
     * Set the vendor-specific data.
     * @param vendorData the vendor-specific data.
     * @since SLEE 1.1
     */
    public void setVendorData(Object vendorData) {
        this.vendorData = vendorData;
    }

    /**
     * Get the vendor-specific data.
     * @return the vendor-specific data.
     * @since SLEE 1.1
     */
    public Object getVendorData() {
        return vendorData;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("SampleStatistics[sample count=").append(sampleCount);
        if (sampleCount > 0) {
            buf.append(",min=").append(min).
                append(",max=").append(max).
                append(",mean=").append(mean);
        }
        if (vendorData != null) buf.append(",vendor data=").append(vendorData);
        buf.append(']');
        return buf.toString();
    }

    // special handling of serialization
    private void writeObject(ObjectOutputStream out) throws IOException {
        VendorExtensionUtils.writeObject(out, vendorDataSerializationEnabled ? vendorData : null);
    }

    // special handling of deserialization
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        vendorData = VendorExtensionUtils.readObject(in, vendorDataDeserializationEnabled);
    }


    private final long sampleCount;
    private final long min;
    private final long max;
    private final double mean;

    private static volatile boolean vendorDataSerializationEnabled = false;
    private static volatile boolean vendorDataDeserializationEnabled = false;
    private transient Object vendorData;
}

