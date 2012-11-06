/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 12.11.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.utils;

/**
 * Checks if the logout windows should shows or not.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ExitFlag
{

    private static ExitFlag exitFlag;

    /** the flag for logout */
    private static boolean  logoutFlag = false;

    /**
     * Description.
     */
    public ExitFlag()
    {
        ExitFlag.logoutFlag = false;
    }

    /**
     * 
     * Check, if the instance of this class exist. If yes, return this instance. else, return a new instance
     * 
     * @return MainController
     */
    public static ExitFlag getInstance()
    {
        if (exitFlag == null)
        {
            exitFlag = new ExitFlag();
            return exitFlag;
        }
        else
            return exitFlag;
    }

    /**
     * 
     * Set the flag
     * 
     * @param flag is boolean
     */
    public void setLogOutFlag(boolean flag)
    {
        ExitFlag.logoutFlag = flag;
    }

    /**
     * 
     * get logOutFlag
     * 
     * @return boolean
     */
    public boolean getLogOutFlag()
    {
        return ExitFlag.logoutFlag;
    }

}
