package javax.slee.management;

/**
 * The <code>VendorExtensions</code> interface provides a means for SLEE vendors
 * to incorporate additional data into various SLEE object types used by SLEE
 * managament clients, without requiring extensions to the SLEE API.  A SLEE
 * vendor that includes vendor-specific data in the SLEE objects it generates
 * for clients should clearly document the type and structure of the data.
 * <p>
 * Serialization and deserialization of vendor-specific data is controlled by the
 * following static methods declared on the classes that implement this interface:
 * <ul>
 *   <li><code>public static void enableVendorDataSerialization() { ... }</code>
 *   <li><code>public static void disableVendorDataSerialization() { ... }</code>
 *   <p>These methods enable and disable the serialization of vendor-specific data.
 *       They are typically used by a SLEE implementation to control the export of
 *       the vendor-specific data.
 *   <li><code>public static void enableVendorDataDeserialization() { ... }</code>
 *   <li><code>public static void disableVendorDataDeserialization() { ... }</code>
 *   <p>These methods enable and disable the deserialization of vendor-specific data.
 *       They are typically used by management clients to control the import of the
 *       vendor-specific data.
 * </ul>
 * By default, both serialization and deserialization of vendor-specific data is
 * disabled for all classes.
 * @since SLEE 1.1
 */
public interface VendorExtensions {
    /**
     * Set the vendor-specific data.
     * @param vendorData the vendor-specific data.
     */
    public void setVendorData(Object vendorData);

    /**
     * Get the vendor-specific data.
     * @return the vendor-specific data.
     */
    public Object getVendorData();
}
