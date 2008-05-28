package org.mobicents.ant;

import org.apache.tools.ant.Project;
import java.io.File;

public interface Component {
    File getComponentFile(Project project);    
}
