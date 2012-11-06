/*==============================================================================
 * File $Id: BeansSecurityInterceptor.java 4510 2010-08-02 10:37:49Z wiesner $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/aspects/security/interceptors/BeansSecurityInterceptor.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 10.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.aspects.security.interceptors;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.prowim.datamodel.security.Authorization;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.coreengine.entities.impl.DefaultSecurityManager;

import de.ebcot.tools.logging.Logger;


/**
 * Authorize user.
 * 
 * @author Saad Wardi
 * @version $Revision: 4510 $
 * @since 2.0
 */
public class BeansSecurityInterceptor
{
    /** the logger. */
    private static final Logger LOG   = Logger.getLogger(BeansSecurityInterceptor.class);
    /** the administrator. */
    private final static String ADMIN = "Administrator";

    /**
     * the session context.
     */
    @Resource
    private SessionContext      sessionCoontext;

    @EJB
    private OrganizationEntity  organizationEntity;

    /**
     * Intercepts when a method is called to provide the security aspect.
     * 
     * @param context the invocation context.
     * @return an Object.
     * @throws Exception to be thrown.
     */

    @AroundInvoke
    public Object onMethodCall(InvocationContext context) throws Exception
    {
        context.getTarget();
        if ( !intercept(context.getMethod().getName(), context.getMethod().getDeclaringClass().getSimpleName()))
        {
            LOG.debug("BeansSecurityInterceptor:  allow call method < " + context.getMethod().getDeclaringClass().getSimpleName() + "#"
                    + context.getMethod().getName() + " >");

            return context.proceed();
        }
        else
        {
            throw new IllegalStateException("No Permission to call this method for user. " + sessionCoontext.getCallerPrincipal());
        }
    }

    /**
     * Called to intercept the call if the user is not authorized.
     * 
     * @param methodname not null methodname.
     * @param classname not null classname.
     * @return true if the user can call the method.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    private boolean intercept(String methodname, String classname) throws OntologyErrorException
    {
        Authorization authorization = getCallerAuthorization(methodname, classname);
        if (authorization != null)
        {
            if (authorization.getRolename().equals(ADMIN))
            {
                return !authorization.isAuthorized();
            } // other user have to make this step but not the admin
            else
            {
                return !authorization.isAuthorized();
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Gets the caller authorization.
     * 
     * @param methodname not null methodname.
     * @param classname not null classname.
     * @return {@link Authorization}.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    private Authorization getCallerAuthorization(String methodname, String classname) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().createAuthorization(organizationEntity.getUser(sessionCoontext.getCallerPrincipal().getName())
                                                                                .getID(), methodname, classname);
    }
}
