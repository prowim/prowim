package org.prowim.dms.alfresco;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.alfresco.webservice.util.AuthenticationUtils;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.DMSFault;
import org.prowim.resources.ResourcesLocator;
import org.prowim.utils.ServerProperties;


/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 14.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/

/**
 * A interceptor which provides the authentication for alfresco webservices access.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a8
 */
public class AuthenticationInterceptor
{

    /**
     * 
     * Starts at each method call a session for alfresco webservices and ends the session after the method is proceeded.
     * 
     * @param invocationContext the context for method invocation
     * @return the result of method invocation
     * @throws Exception if an error occurs while intercepting the called method
     */
    @AroundInvoke
    public Object onMethodCall(InvocationContext invocationContext) throws Exception
    {
        try
        {
            AuthenticationUtils.startSession(ServerProperties.getDMSUser(), ServerProperties.getDMSPassword());
        }
        catch (Exception e)
        {
            String message = "An error occurs while starting a session with alfresco webservices";
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message + "\n" + e.getMessage());
            throw new DMSException(message, dmsFault, e);
        }

        try
        {
            return invocationContext.proceed();
        }
        finally
        {
            AuthenticationUtils.endSession();
        }
    }
}
