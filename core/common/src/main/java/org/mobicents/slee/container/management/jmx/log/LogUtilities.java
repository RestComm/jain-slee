package org.mobicents.slee.container.management.jmx.log;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LogUtilities {

		/**
		 * Takes throwable and generates message identical to Throwable.printStackTrace();
		 * @param t
		 * @return
		 */
		public static String doMessage(Throwable t)
		{
			StringBuffer sb=new StringBuffer();
			int tick = 0;
			Throwable e = t;
			do {
				StackTraceElement[] trace = e.getStackTrace();
				if (tick++ == 0)
					sb.append(e.getClass().getCanonicalName() + ":"
							+ e.getLocalizedMessage()+"\n");
				else
					sb.append("Caused by: "+e.getClass().getCanonicalName() + ":"
							+ e.getLocalizedMessage()+"\n");
				
				for (StackTraceElement ste : trace)
					sb.append("\t"+ste+"\n");
	
				e = e.getCause();
			} while (e != null);
			
			return sb.toString();
			
		}

}
