/*==============================================================================
 * File $Id: PermissionExceptionsHandler.java 4958 2010-10-21 12:14:12Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 14:14:12 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/PermissionExceptionsHandler.java $
 * $LastChangedRevision: 4958 $
 *------------------------------------------------------------------------------
 * (c) 23.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.prowim.portal.i18n.Resources;


/**
 * Parses the headerTitle of the returned IllegalStateException from the server<br>
 * and creates a headerTitle that will be schown to user.
 * 
 * @author Saad Wardi
 * @version $Revision: 4958 $
 * @since 2.0.0
 */
public final class PermissionExceptionsHandler
{
    private final static String NO_PERMISSION                 = "No Permission";
    private final static String NO_RIGHTS_ROLES               = "No rights roles";
    private final static String ERROR_ON_LOGIN                = "Password Incorrect";
    private final static String COULD_NOT_GET_DMS_CONTENT     = "could not get dms content";
    private final static String HAS_NO_DMS_CONTENT            = "Frame does not have a document in the DMS";
    private final static String FAIL_ADD_ROLE_TO_ORGANIZATION = "Could not add role to organization";
    private final static String MOVE_ORGANIZATION_TO_ITSELF   = "organization as suborganization of itself is not allowed";
    private final static String SET_PROCESS_TYPE_PARENT       = "Could not set ProcessTypeParent with ID";

    private PermissionExceptionsHandler()
    {
    }

    /**
     * 
     * Returns possible messages to given {@link Exception}.
     * 
     * @param e {@link Exception}. Can be null.
     * @return Message to given {@link Exception}. Empty string if headerTitle {@link Exception} is null.
     */
    protected static String getMessage(Exception e)
    {
        String result = "";
        if (e.getMessage() == null)
        {
            result = Resources.Frames.Global.Texts.LOGIN_FAILED.getText();
        }
        else if (e.getMessage().contains(NO_PERMISSION))
        {
            result = Resources.Frames.Global.Texts.NO_PERMISSION.getText();
        }
        else if (e.getMessage().contains(NO_RIGHTS_ROLES))
        {
            result = Resources.Frames.Global.Texts.NO_RIGHTS_ROLES.getText();
        }
        else if (e.getMessage().contains(ERROR_ON_LOGIN))
        {
            result = Resources.Frames.Global.Texts.LOGIN_FAILED.getText();
        }
        else if (e.getMessage().contains(COULD_NOT_GET_DMS_CONTENT))
        {
            result = Resources.Frames.Global.Texts.HAS_NO_DMS_CONTENT.getText();
        }
        else if (e.getMessage().contains(HAS_NO_DMS_CONTENT))
        {
            result = Resources.Frames.Global.Texts.HAS_NO_DMS_CONTENT.getText();
        }
        else if (e.getMessage().contains(FAIL_ADD_ROLE_TO_ORGANIZATION))
        {
            result = Resources.Frames.Global.Texts.ERROR_ON_ADD_ROLE_TO_ORGANIZATION.getText();
        }
        else if (e.getMessage().contains(MOVE_ORGANIZATION_TO_ITSELF))
        {
            result = Resources.Frames.Global.Texts.ERROR_ON_MOVE_ORGANIZATION.getText();
        }
        else if (e.getMessage().contains(SET_PROCESS_TYPE_PARENT))
        {
            result = Resources.Frames.Global.Texts.ERROR_ON_SET_PROCESSTYPE_PARENT.getText();
        }

        return result;
    }
}
