package javax.slee.management;

import java.util.Arrays;

/**
 * This class provides access to deployment-specific attributes that describe an
 * installed library.
 * @since SLEE 1.1
 */
public class LibraryDescriptor extends ComponentDescriptor {
    /**
     * Create a new library component descriptor.
     * @param component the identifier of the component.
     * @param deployableUnit the identifier of the deployable unit from which the
     *        component was installed.
     * @param source the source object (component jar or service XML file) within the
     *        deployable unit from which this component was installed.
     * @param libraries the identifiers of the libraries that the component depends on.
     * @param libraryJars the names of any jars that are included in the library.
     *        These names are as they appeared in the library component's deployment
     *        descriptor.
     * @throws NullPointerException if any argument is <code>null</code>.
     */
    public LibraryDescriptor(LibraryID component, DeployableUnitID deployableUnit, String source, LibraryID[] libraries, String[] libraryJars) {
        super(component, deployableUnit, source, libraries);
        if (libraryJars == null) throw new NullPointerException("libraryJars is null");
        this.libraryJars = libraryJars;
    }

    /**
     * Get the names of the jars that are included in the library.
     * @return the names of the jars that are included in the library.
     */
    public final String[] getLibraryJars() { return libraryJars; }

    /**
     * Get a string representation for this library component descriptor.
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Library[");
        super.toString(buf);
        buf.append(",library jars=").append(Arrays.asList(libraryJars)).
            append(']');
        return buf.toString();
    }


    private final String[] libraryJars;
}