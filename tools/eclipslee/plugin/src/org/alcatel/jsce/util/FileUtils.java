
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

import java.io.*;
import java.net.*;

/**
 * <p>Title: JAIN Slee for the OSP</p>
 * <p>Description: JAIN Slee implementation on top of the Alcatel OSP platform</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Alcatel Namur</p>
 * @author Dominique Gallot
 * @version 1.0
 */

public class FileUtils
{
    private FileUtils()
    {}

    public static boolean safeDelTree ( File directory )
    {
        try
        {
            delTree(directory);
            return true;
        }
        catch (IOException ex)
        {
            return false;
        }
    }

    public static void delTree ( File directory ) throws IOException
    {
        if ( !directory.isDirectory() )
        {
            throw new IOException("The file '"+directory+"' is not a directory" );
        }
        File[] fileArray = directory.listFiles();
        if ( fileArray == null )
        {
            throw new IOException("Unable to list the file in the directory : " + directory);
        }

        for (int i = 0; i < fileArray.length; i++)
        {
            if (fileArray[i].isFile())
            {
                fileArray[i].delete();
            } else
            {
                delTree(fileArray[i]);
                fileArray[i].delete();
            }
        }
        directory.delete();
    }


    public static String getFileName(String path)
    {
        return getFileName(path, File.separatorChar);
    }

    public static String getFileName(File file)
    {
        return file.getName();
    }

    public static String getFileName(String path, char delimiter)
    {
        int i = path.lastIndexOf(delimiter);
        String filename = path.substring(i > 0 ? i : 0);
        return filename;
    }

    public static String removeFileExt(String filename)
    {
        int index = filename.lastIndexOf('.');
        if (index == 0)
            return filename;
        return filename.substring(0, index);
    }

    public static String getFileExt(String filename)
    {
        int index = filename.lastIndexOf('.');
        if (index == 0)
            return "";
        return filename.substring(index + 1);
    }

    /**
     *  Will copy a file to another location
     *
     *@param  file             The resource file name
     *@param  destFile         The destination file name
     *@exception  IOException  on any IO Error
     */
    public static void copyFileTo(String fileName, String destFile) throws IOException
    {
        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(fileName));
        copyTo(fin, destFile);
        fin.close();
    }

    /**
     *  Will copy a file to another location
     *
     *@param  file             The resource file
     *@param  destFile         The destination file name
     *@exception  IOException  on any IO Error
     */
    public static void copyFileTo(File file, String destFile) throws IOException
    {
        BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
        copyTo(fin, destFile);
        fin.close();
    }

    /**
     *  Write the input stream to a file
     *
     *@param  in               The input stream
     *@param  destFile         The file to be written
     *@exception  IOException  on any IO Error
     */
    public static void copyTo(InputStream in, String destFile) throws IOException
    {
        // copy the stream to [extractto]/resourceName
        File f = new File(destFile);
        copyTo(in, f);
    }

    /**
     *  Write the input stream to a file
     *
     *@param  in               The input stream
     *@param  destFile         The file to be written
     *@exception  IOException  on any IO Error
     */
    public static void copyTo(InputStream in, File destFile) throws IOException
    {
        // copy the stream to [extractto]/resourceName
        File p = destFile.getParentFile();
        if (p != null)
        {
            if (!p.exists())
            {
                p.mkdirs();
            }
        }
        BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(destFile));
        try
        {
            StreamUtils.copyStream(in, fout);
        }
        finally
        {
            fout.close();
        }
    }

    public static byte[] loadFile(String aFileName) throws IOException
    {
        FileInputStream fin = new FileInputStream(aFileName);
        try
        {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            try
            {
                StreamUtils.copyStream(fin, bout);
                byte[] result = bout.toByteArray();
                return result;
            }
            finally
            {
                StreamUtils.safeClose(bout);
            }
        }
        finally
        {
            StreamUtils.safeClose(fin);
        }
    }

    public static byte[] loadFile(InputStream aStream) throws IOException
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try
        {
            StreamUtils.copyStream(aStream, bout);
            return bout.toByteArray();
        }
        finally
        {
            StreamUtils.safeClose(bout);
        }
    }

    public static void saveFile(String aFileName, byte[] aDatas) throws IOException
    {
        FileOutputStream fout = new FileOutputStream(aFileName);
        try
        {
            fout.write(aDatas);
            fout.flush();
        }
        finally
        {
            StreamUtils.safeClose(fout);
        }
    }

    public static void saveFile(File aFile, byte[] aDatas) throws IOException
    {
        FileOutputStream fout = new FileOutputStream(aFile);
        try
        {
            fout.write(aDatas);
            fout.flush();
        }
        finally
        {
            StreamUtils.safeClose(fout);
        }
    }

    public static URL toURL ( File aFile ) throws IllegalArgumentException
    {
        try
        {
            return aFile.toURL();
        }
        catch (MalformedURLException ex)
        {
            throw new IllegalArgumentException("Unable to convert a File to a URL ! File: " + aFile);
        }
    }

    public static File fromURL(URL aUrl) throws IOException
    {
        if (!StringUtils.safeEquals(aUrl.getProtocol(), "file", false))
        {
            throw new IOException("The url protocol must be 'file'");
        }

        String path = aUrl.getFile();
        if (File.separatorChar != '/')
        {
            path = path.replace('/', File.separatorChar);
        }
//    if (path.startsWith("/"))
//    {
//      path = path.substring(1);
//    }
        if (path.endsWith("/"))
        {
            path = path.substring(0, path.length() - 2);
        }
        return new File(path);
    }

    public static URL getBase(URL url)
    {
        String basePath = url.getFile();
        int i = basePath.lastIndexOf('/');
        basePath = basePath.substring(0, i + 1);
        try
        {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), basePath);
        }
        catch (MalformedURLException ex)
        {
            throw new IllegalArgumentException("The provided url is not valid !!");
        }
    }

    public static URL getFileInBase(URL url, String file)
    {
        String basePath = url.getFile();
        int i = basePath.lastIndexOf('/');
        basePath = basePath.substring(0, i + 1);
        if (basePath.length() > 1)
            file = basePath + '/' + file;
        try
        {
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), file);
        }
        catch (MalformedURLException ex)
        {
            throw new IllegalArgumentException("The provided url is not valid !!");
        }
    }

    public static String getFileName(URL url)
    {
        String file = url.getFile();
        if (file == null)
            return null;
        int index = file.lastIndexOf('/');
        String filename = file.substring(index + 1);
        return filename;
    }
}
