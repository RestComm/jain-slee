/**
 *   Copyright 2005 Alcatel, OSP.
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

package org.alcatel.jsce.util.image;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.alcatel.jsce.util.log.SCELogger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 * Description:
 * <p>
 * Manages the extraction and the creation of images. It also maintains a cache which enables more efficiency in load.
 * <br>
 * <b>Singleton pattern.</b>
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 * 
 */
public class ImageManager {
	/** Singleton instance */
	private static ImageManager instance = null;
	/** The image location cache */
	private HashMap imageCache = null;
	/** The image cache */
	private HashMap imageFileCache = null;

	/**
	 * Constructor, initializes the cache.
	 */
	private ImageManager() {
		imageCache = new HashMap();
		imageFileCache = new HashMap();
	}

	/**
	 * @return the singleton instance.
	 */
	public static ImageManager getInstance() {
		if (instance == null) {
			instance = new ImageManager();
		}
		return instance;
	}

	/**
	 * @param location
	 *            the relative location of the image in the icon dir, e.g., "image.gif"
	 * @return the
	 * @link ImageDescriptor corresponding to the specified image.
	 */
	public ImageDescriptor getImgeDescriptor(String location) {
		if (imageCache.containsKey(location)) {
			return (ImageDescriptor) imageCache.get(location);
		}

		URL iconURL = FileLocator.find(ServiceCreationPlugin.getDefault().getBundle(), new Path("/icons/" + location), null);
		ImageDescriptor image = ImageDescriptor.createFromURL(iconURL);
		if (image != null) {
			imageCache.put(location, image);
		}
		return image;
	}

	public Image getImage(String location) {
		ImageDescriptor imageDescriptor = getImgeDescriptor(location);
		if (imageDescriptor != null) {
			Image image = (Image) imageFileCache.get(imageDescriptor);
			if (image == null) {
				image = imageDescriptor.createImage();
				imageFileCache.put(imageDescriptor, image);
			}
			return image;
		} else {
			SCELogger.logError("The image location is not valid", new IllegalStateException(
					"The Image descriptor is null"));
		}
		return null;
	}

	public void disposeImages() {
		Set keys = imageFileCache.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			ImageDescriptor descriptor = (ImageDescriptor) iter.next();
			Image image_i = (Image) imageFileCache.get(descriptor);
			image_i.dispose();
		}
	}

}
