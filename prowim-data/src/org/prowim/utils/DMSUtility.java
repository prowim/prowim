/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-09-18 13:55:40 +0200 (Fr, 18 Sep 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/utils/DMSUtility.java $
 * $LastChangedRevision: 2414 $
 *------------------------------------------------------------------------------
 * (c) 17.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *This file is part of ProWim.

ProWim is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ProWim is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ProWim.  If not, see <http://www.gnu.org/licenses/>.

Diese Datei ist Teil von ProWim.

ProWim ist Freie Software: Sie können es unter den Bedingungen
der GNU General Public License, wie von der Free Software Foundation,
Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
veröffentlichten Version, weiterverbreiten und/oder modifizieren.

ProWim wird in der Hoffnung, dass es nützlich sein wird, aber
OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
Siehe die GNU General Public License für weitere Details.

Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.utils;

import java.util.HashMap;


/**
 * Helper methods to get a content name, file extension, print version history.
 * 
 * @author Saad Wardi
 * @version $Revision: 2414 $
 */
public class DMSUtility
{
    /**
     * The extensions to match.
     */
    private final HashMap<String, String> extensionsMap;

    /**
     * Default constructor.
     */
    public DMSUtility()
    {
        extensionsMap = new HashMap<String, String>();
    }

    /**
     * Returns the extension of a file.
     * 
     * @param filename the filename
     * @return the file extension.
     */
    public static String getExtension(String filename)
    {
        String extension = filename;
        int baddotindex = extension.lastIndexOf('.');
        if (baddotindex >= 0)
        {
            extension = extension.substring(baddotindex + 1);
        }
        return extension;
    }

    /**
     * Returns the extension of a file. Example if the input url is : /myClass.java it returns .java
     * 
     * @param url the url
     * @return the file extension.
     */
    public static String getName(String url)
    {
        String extension = url;
        int baddotindex = extension.lastIndexOf('/');
        if (baddotindex >= 0)
        {
            extension = extension.substring(baddotindex + 1);
        }
        return extension;
    }

    /**
     * Returns true if the URL requst path (e.g.: /foo/cow.html) contains an extension (e.g.: "html") included in the list of matching extensions.
     * 
     * @param path the file path.
     * @return true if the extension of the file selected is stored in the extensions map.
     */
    public boolean matches(String path)
    {
        String ext = "";

        int dirindex = path.lastIndexOf('/');

        if (dirindex >= 0)
        {
            String leaf = path.substring(dirindex + 1);
            int extindex = leaf.lastIndexOf('.');

            if (extindex >= 0)
            {
                ext = leaf.substring(extindex + 1).toLowerCase();
            }
        }

        return extensionsMap.containsKey(ext);
    }
}
