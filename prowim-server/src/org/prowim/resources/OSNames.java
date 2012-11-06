/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-02-09 13:34:44 +0100 (Di, 09 Feb 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/resources/OSNames.java $
 * $LastChangedRevision: 3262 $
 *------------------------------------------------------------------------------
 * (c) 10.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.resources;

/**
 * Provides the names of the operating systems supported.
 * 
 * @author Saad Wardi
 * @version $Revision: 3262 $
 */
public final class OSNames
{
    /** linux */
    protected final static String LINUX   = "linux";
    /** windows */
    protected final static String WINDOWS = "windows";

    private OSNames()
    {
    }

    /**
     * Gets the os name.
     * 
     * @return the os name or null if not supported.
     */
    public static String getOsName()
    {
        String os = "";
        if (System.getProperty("os.name").toLowerCase().indexOf(WINDOWS) > -1)
        {
            os = WINDOWS;
        }
        else if (System.getProperty("os.name").toLowerCase().indexOf(LINUX) > -1)
        {
            os = LINUX;
        }

        return os;
    }
}
