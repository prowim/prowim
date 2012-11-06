/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-28 16:44:20 +0200 (Di, 28 Sep 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/LoggedInUserInfo.java $
 * $LastChangedRevision: 4826 $
 *------------------------------------------------------------------------------
 * (c) 24.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
package org.prowim.portal;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.eclipse.rwt.SessionSingletonBase;
import org.prowim.datamodel.prowim.Person;


/**
 * Stores the login data of the current loged in user.
 * 
 * @author Saad Wardi
 * @version $Revision: 4826 $
 * @since 2.0
 */

public final class LoggedInUserInfo
{
    private final static Logger LOG         = Logger.getLogger(LoggedInUserInfo.class);

    private String              currentUser = null;
    private Person              person      = null;

    private LoggedInUserInfo()
    {
    }

    /**
     * 
     * Set the current {@link Person}, which is logged in portal. Can only be called once.
     * 
     * @param userName Name of {@link Person}.
     * @param password Password of {@link Person}.
     */
    public void setCurrentUser(String userName, String password)
    {
        Validate.isTrue(currentUser == null);
        Validate.isTrue(person == null);

        currentUser = userName;
        person = MainController.getInstance().getUserWithName(userName);
        LOG.debug("IS Admin " + person.isAdmin());
        LOG.debug("IS Modeler " + person.isModeler());
        LOG.debug("IS Reader " + person.isReader());
    }

    /**
     * Gets the current user as Person object.
     * 
     * @return {@link Person} object.
     */
    public Person getPerson()
    {
        return person;
    }

    /**
     * Gets the current user.
     * 
     * @return current user.
     */

    public String getCurrentUser()
    {
        return currentUser;
    }

    /**
     * Checks if the current user is modeler .
     * 
     * @return true if the current user is modeler.
     */

    public boolean isCurrentUserAdmin()
    {
        return person.isAdmin();
    }

    /**
     * Checks if the current user is modeler .
     * 
     * @return true if the current user is modeler.
     */
    public boolean isCurrentUserModeler()
    {
        return person.isModeler();
    }

    /**
     * Checks if the current user is reader .
     * 
     * @return true if the current user is reader.
     */
    public boolean isCurrentUserReader()
    {
        return person.isReader();
    }

    /**
     * 
     * Get the session-specific instance of the user info
     * 
     * @return never null. An instance per session
     */
    public static LoggedInUserInfo getInstance()
    {
        return (LoggedInUserInfo) SessionSingletonBase.getInstance(LoggedInUserInfo.class);
    }

}
