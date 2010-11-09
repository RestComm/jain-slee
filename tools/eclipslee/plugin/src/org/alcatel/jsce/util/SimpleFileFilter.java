
/**
 *   Copyright 2006 Alcatel, OSP.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.alcatel.jsce.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * <p>Title: JAIN Slee for the OSP</p>
 * <p>Description: JAIN Slee implementation on top of the Alcatel OSP platform</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Alcatel Namur</p>
 * @author not attributable
 * @version 1.0
 *
 * A Filename Filter implementation, which filter the filename base on the extenstion
 *
 */

public class SimpleFileFilter implements FilenameFilter
{
  private String[] extensions;


  /**
   * Contructs the filter for one extension
   *
   * @param  ext  Description of the Parameter
   */
  public SimpleFileFilter(String ext)
  {
    this(new String[]{ext});
  }


  /**
   * Contructs the filter for several extension
   *
   * @param  exts  Description of the Parameter
   */
  public SimpleFileFilter(String[] exts)
  {
    extensions = new String[exts.length];
    for (int i = 0; i < exts.length; i++)
    {
      extensions[i] = exts[i].toLowerCase();
    }
  }


  /**
   *  filenamefilter interface method
   *
   * @param  dir    Description of the Parameter
   * @param  _name  Description of the Parameter
   * @return        Description of the Return Value
   */
  public boolean accept(File dir, String _name)
  {
    String name = _name.toLowerCase();
    for (int i = 0; i < extensions.length; i++)
    {
      if (name.endsWith(extensions[i]))
      {
        return true;
      }
    }
    return false;
  }


  /**
   *  this method checks to see if an asterisk is imbedded in the filename, if
   *  it is, it does an "ls" or "dir" of the parent directory returning a list
   *  of files that match eg. /usr/home/mjennings/*.jar would expand out to all
   *  of the files with a .jar extension in the /usr/home/mjennings directory
   *
   * @param  f  Description of the Parameter
   * @return    Description of the Return Value
   */
  public static String[] fileOrFiles(File f)
  {
    if (f == null)
    {
      return null;
    }
    File parent = new File(f.getParent());
    String fname = f.getName();
    String[] files;
    if (fname.charAt(0) == '*')
    {
      String filter = fname.substring(1, fname.length());
      files = parent.list(new SimpleFileFilter(filter));
      return files;
    } else
    {
      files = new String[1];
      files[0] = f.getPath();
      // was:fname;
      return files;
    }
  }
}
