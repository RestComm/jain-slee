
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
package org.alcatel.jsce.util.jad;

/**
 *  Description:
 * <p>
 *  Class coming from the JADClipse package.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
import java.io.*;

class StreamRedirectThread extends Thread
{

    private final Reader in;
    private final Writer out;
    private Exception ex;
    private static final int BUFFER_SIZE = 2048;

    StreamRedirectThread(String name, InputStream in, Writer out)
    {
        super(name);
        this.in = new InputStreamReader(in);
        this.out = out;
        setPriority(9);
    }

    public void run()
    {
        try
        {
            char cbuf[] = new char[2048];
            int count;
            while((count = in.read(cbuf, 0, 2048)) >= 0) 
            {
                out.write(cbuf, 0, count);
                out.flush();
            }
        }
        catch(IOException exc)
        {
            ex = exc;
        }
    }

    public Exception getException()
    {
        return ex;
    }
}
