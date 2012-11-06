/*==============================================================================
 * File $Id: ChangeOrganizationElementInterceptor.java 4510 2010-08-02 10:37:49Z wiesner $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/interceptors/ChangeOrganizationElementInterceptor.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 23.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.interceptors;

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
 * A request Interceptor is designed to intercept write operations called from users that are not allowed to change users and organizations data.<br>
 * NOTE: Not administrator users can get a permission to change an entity by configuring such an entity and set the user ID in the slot "darf_geaendert_werden_von" of the organization entity (user or organization).
 * 
 * @author !!YOUR NAME!!
 * @version $Revision: 4510 $
 * @since !!VERSION!!
 */
public class ChangeOrganizationElementInterceptor implements SecurityInterceptor
{

    /** the logger. */
    private static final Logger LOG = Logger.getLogger(ChangeOrganizationElementInterceptor.class);
    /**
     * the session context.
     */
    @Resource
    private SessionContext      sessionCoontext;

    @EJB
    private OrganizationEntity  organizationEntity;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.interceptors.SecurityInterceptor#onMethodCall(javax.interceptor.InvocationContext)
     */
    @AroundInvoke
    public Object onMethodCall(InvocationContext context) throws Exception
    {
        Object[] params = context.getParameters();
        for (int i = 0; i < params.length; i++)
        {
            LOG.debug("PARAM :   " + i + "  " + params[i]);
        }
        LOG.debug("Invoking class: " + context.getMethod().getDeclaringClass().getSimpleName());
        LOG.debug("Invoking method: " + context.getMethod().getName());
        System.out.println("CALLER PRINCIPAL   " + sessionCoontext.getCallerPrincipal());
        if ( !intercept(context.getMethod().getName(), context.getMethod().getDeclaringClass().getSimpleName(), params))
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
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.interceptors.SecurityInterceptor#intercept(java.lang.String, java.lang.String, java.lang.Object[])
     */
    public boolean intercept(String methodname, String classname, Object[] parameters) throws OntologyErrorException
    {

        Authorization authorization = getCallerAuthorization(methodname, classname);
        LOG.debug("INTERCEPT :  " + methodname + "    authswardi  " + authorization);

        if (authorization != null)
        {

            LOG.debug("AUTHORIZATIONS:  " + authorization);

            if (authorization.getRolename().equals("Administrator"))
            {
                return !authorization.isAuthorized();
            } // other user have to make this step but not the admin
            else
            {
                boolean check1 = authorization.isAuthorized();
                boolean check2 = this.checkNotAdminUser(methodname, classname, parameters);
                LOG.debug("Caller is not Administrator:  " + sessionCoontext.getCallerPrincipal());
                LOG.debug("Caller authorization:    " + check1);
                LOG.debug("Caller checkNotAdmin  " + check2);
                return !(check1 && check2);
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Description.
     * 
     * @return
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private boolean checkNotAdminUser(String methodname, String classname, Object[] parameters) throws OntologyErrorException
    {

        if (parameters.length > 0)
        {
            String userID = (String) parameters[0];

            // TODO : check if the caller can create a new process
            if (DefaultSecurityManager.getInstance().isPersonAdminUser(sessionCoontext.getCallerPrincipal().getName()))
            {
                LOG.debug("An admin user " + userID + " is permitted to change data of user:  " + sessionCoontext.getCallerPrincipal().getName());
                return true;
            }

            else
            {
                /** a user can only update his data and not data of other user. */
                // TODO: This will create a new process. SO check if the caller is : authorized to change user data. It means : check him if he exists in the slot darf_geaendert_werden_von
                if (DefaultSecurityManager.getInstance()
                        .checkPersonCanModifyEntity(userID,
                                                    DefaultSecurityManager.getInstance().getUserID(sessionCoontext.getCallerPrincipal().getName())))
                {
                    LOG.debug("A user " + userID + " is permitted to change data of user:  " + sessionCoontext.getCallerPrincipal().getName());
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the caller authorization.
     * 
     * @param methodname not null methodname.
     * @param classname not null classname.
     * @return {@link Authorization}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private Authorization getCallerAuthorization(String methodname, String classname) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().createAuthorization(organizationEntity.getUser(sessionCoontext.getCallerPrincipal().getName())
                                                                                .getID(), methodname, classname);
    }

}
