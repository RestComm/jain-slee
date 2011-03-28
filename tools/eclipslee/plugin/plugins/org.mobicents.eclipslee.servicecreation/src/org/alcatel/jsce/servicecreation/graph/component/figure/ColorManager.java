/** Copyright 2004 Universite Libre de Bruxelles Department of Informatics and
 * Networks- Faculty of Engineering BioMaze Project.
 */
package org.alcatel.jsce.servicecreation.graph.component.figure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.swt.graphics.Color;



/**
 * @author Skhiri dit Gabouje Sabri
 * BioMaze, ULB
 * 
 * <p>
 * Provide an easy way to build a color. This class maintains an cache of colour, if a color that was
 * already asked, it does'nt rebuilt it.
 * </p>
 */
public class ColorManager {
    private static ColorManager instance = null;
	private Map colorMap = null;
	
	private ColorManager() {
		colorMap = new HashMap();
		colorMap.put(IGlyph.COLOR_NONE, new Color(null, 255, 255, 255));
	}
	public Color getColor(String colorName) {
		Color color = (Color) colorMap.get(colorName);
		if (color == null) {
			color = createColor(colorName);
		}
		return color;
	}
	
	public Color createColor(int r, int g, int b){
		String colorDef = r+","+g+","+b;
		Color temp = (Color) colorMap.get(colorDef);
		if(temp ==null){
			temp = new Color(null, r, g,b );
			colorMap.put(colorDef, temp);
		}
		return temp;
	}
	
	/**
	 * @param colorName The nolor name
	 * @return The corresponding @link Color 
	 */
	private Color createColor(String colorName) {
		Color color;
		if(colorName.charAt(0)== '#'){
		    color = extractcode(colorName.substring(1));
		   if(color==null){
		       color = (Color) colorMap.get(IGlyph.COLOR_NONE);
				System.out.println("***************Color Manager No color could be set !! for "+ colorName);
		   }else{
		       colorMap.put(colorName, color);
		   }
		}else{
		    if (colorName.equals(IGlyph.COLOR_GREEN)) {
				color = new Color(null, 85, 201, 85);
				colorMap.put(IGlyph.COLOR_GREEN, color);
			} else {
				if (colorName.equals(IGlyph.COLOR_RED)) {
					color = new Color(null, 249, 133, 133);
					colorMap.put(IGlyph.COLOR_RED, color);
				} else {
					if (colorName.equals(IGlyph.COLOR_BLACK)) {
						color = new Color(null, 0, 0, 0);
						colorMap.put(IGlyph.COLOR_BLACK, color);
					} else {
						if (colorName.equals(IGlyph.COLOR_CYAN)) {
							color = new Color(null, 140, 140, 248);
							colorMap.put(IGlyph.COLOR_CYAN, color);
						} else {
							if (colorName.equals(IGlyph.COLOR_BLUE)) {
								color = new Color(null, 0, 40, 202);
								colorMap.put(IGlyph.COLOR_BLUE, color);
							} else {
								if (colorName.equals(IGlyph.COLOR_GRAY)) {
									color = new Color(null, 156, 156, 156);
									colorMap.put(IGlyph.COLOR_GRAY, color);
								} else {
									color = (Color) colorMap.get(IGlyph.COLOR_NONE);
									System.out.println("***************Color Manager No color could be set !! for "+ colorName);
								}
							}
						}
					}
				}
			}
		}
		
		return color;
	}
	
	/**
     *@param string
     *@return 
     */
    private Color extractcode(String code) {
        /*int index1 = code.indexOf(' ');
        String tmp = code.substring(index1);
        int index2 = tmp.indexOf(' ');
        String redS = code.substring(0,index1);
        String greenS = code.substring(index1, index2);
        String blueS = code.substring(index2, code.length());*/
       StringTokenizer st = new java.util.StringTokenizer(code);
       List colors = new ArrayList();
        while(st.hasMoreTokens()) {
            String tmp = st.nextToken();
            int indexO = tmp.indexOf('0');
            if(indexO >-1){
                tmp.replace('0', ' ');
            }
            colors.add(tmp);
        }
        if(colors.size()>2){
            String redS = (String) colors.get(0);
            String greenS =  (String) colors.get(1);
            String blueS =  (String) colors.get(2);
            //System.out.println(" COLOR MANAGER : " +  Integer.parseInt(redS) + "  "+  Integer.parseInt(greenS) + "  "+  Integer.parseInt(blueS) );
            return new Color(null, Integer.parseInt(redS),Integer.parseInt(greenS), Integer.parseInt(blueS));
        }else return null;
        
    }
    /**
	 * Delete and dispose all the colour.
	 */
	public void dispose() {
		Set keys = colorMap.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			((Color) colorMap.get(key)).dispose();
		}
		colorMap.clear();
	}
	
	public static ColorManager getInstance(){
	    if(instance==null){
	        instance = new ColorManager();
	    }
	    return instance;
	}
}