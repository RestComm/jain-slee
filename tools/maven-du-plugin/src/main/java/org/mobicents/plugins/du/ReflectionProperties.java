package org.mobicents.plugins.du;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.introspection.ReflectionValueExtractor;

import java.util.Properties;


/**
 * @author Andreas Hoheneder (ahoh_at_inode.at)
 * @version $Id$
 */
public class ReflectionProperties
    extends Properties
{

    private MavenProject project;

    boolean escapedBackslashesInFilePath;

    public ReflectionProperties( MavenProject aProject, boolean escapedBackslashesInFilePath )
    {
       super();

       project = aProject;

       this.escapedBackslashesInFilePath = escapedBackslashesInFilePath;
    }

    public Object get( Object key )
    {
        Object value = null;
        try
        {
            value = ReflectionValueExtractor.evaluate( "" + key , project );

            if ( escapedBackslashesInFilePath && value != null &&
                "java.lang.String".equals( value.getClass().getName() ) )
            {
                String val = (String) value;

                // Check if it's a windows path
                if ( val.indexOf( ":\\" ) == 1 )
                {
                    value = StringUtils.replace( (String)value, "\\", "\\\\" );
                    value = StringUtils.replace( (String)value, ":", "\\:" );
                }
            }
        }
        catch ( Exception e )
        {
            //TODO: remove the try-catch block when ReflectionValueExtractor.evaluate() throws no more exceptions
        }
        return value;
    }
}