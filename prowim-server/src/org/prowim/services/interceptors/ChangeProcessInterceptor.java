/*==============================================================================
 * File $Id: ChangeProcessInterceptor.java 4934 2010-10-20 15:24:29Z specht $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-20 17:24:29 +0200 (Mi, 20 Okt 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/interceptors/ChangeProcessInterceptor.java $
 * $LastChangedRevision: 4934 $
 *------------------------------------------------------------------------------
 * (c) 15.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 * A request Interceptor is designed to intercept write operations called from users that are not allowed to change a process.<br>
 * NOTE: Not administrator users can get a permission to change a process by configuring such a process and set the user ID in the slot "darf_geaendert_werden_von" of the process.
 * 
 * @author Saad Wardi
 * @version $Revision: 4934 $
 * @since 2.0.0
 */
public class ChangeProcessInterceptor implements SecurityInterceptor
{
    /** the logger. */
    private static final Logger LOG = Logger.getLogger(ChangeProcessInterceptor.class);
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
            if (context.getMethod().getName().equals("createObject"))
            {
                Object returnedObject = context.proceed();
                if (returnedObject != null)
                {
                    if (params[0].equals("model"))
                    {
                        DefaultSecurityManager.getInstance()
                                .setUserCanModifyEntity((String) returnedObject,
                                                        DefaultSecurityManager.getInstance()
                                                                .getUserID(sessionCoontext.getCallerPrincipal().getName()));
                    }
                }
                return returnedObject;
            }
            else
            {
                return context.proceed();
            }
        }
        else
        {
            throw new IllegalStateException("No Permission to call this method for user. " + sessionCoontext.getCallerPrincipal());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.interceptors.SecurityInterceptor#intercept(java.lang.String, java.lang.String, java.lang.Object[])
     */
    public boolean intercept(String methodname, String classname, Object[] parameters) throws OntologyErrorException
    {

        Authorization authorization = getCallerAuthorization(methodname, classname);
        LOG.debug("INTERCEPT :  " + methodname + "    authorization " + authorization);

        if (authorization != null)
        {
            LOG.debug("AUTHORIZATIONS:  " + authorization);

            if (authorization.getRolename().equals("Administrator"))
            {
                return !authorization.isAuthorized();
            }
            // other user have to make this step but not the admin

            else
            {
                if (checkCreateModelForModeler(methodname, parameters, authorization.getRolename()))
                {
                    LOG.debug("NO INTERCEPT for createObject Modeler Role :");
                    return false;
                }
                if (methodname.equals("setProduct"))
                {
                    /**
                     * TODO: param[2]= productID -> get the process ID where this product is defined and check if <br>
                     * the caller can modify this process
                     */
                    return !(checkModificationAccessBySlot((String) parameters[2]) && authorization.isAuthorized());
                }
                if (methodname.equals("removeRelationValue") || methodname.equals("setCombinationRule") || methodname.equals("setActivationRule"))
                {
                    return !(checkModificationAccessBySlot((String) parameters[0]) && authorization.isAuthorized());
                }

                if (methodname.equals("setProcessType"))
                {
                    return !(checkModificationAccessBySlot((String) parameters[1]) && authorization.isAuthorized());
                }

                if (methodname.equals("setRelationValue") || methodname.equals("setProcessStarter"))
                {
                    return !(checkModificationAccessBySlot((String) parameters[0]) && authorization.isAuthorized());
                }

                if (methodname.equals("deleteObject") || methodname.equals("setPersonCanModifyEntity"))
                {
                    return !(checkModificationAccessBySlot((String) parameters[0]) && authorization.isAuthorized());
                }
                if (methodname.equals("connectActivityControlFlow") || methodname.equals("connectActivityMittel")
                        || methodname.equals("connectActivityRole"))
                {
                    return !(checkModificationAccessBySlot((String) parameters[0]) && authorization.isAuthorized());
                }

                /** for all other methods use this check. */
                boolean check1 = authorization.isAuthorized();
                boolean check2 = this.checkNotAdminUser(parameters);
                LOG.debug("Caller is not Administrator:  " + sessionCoontext.getCallerPrincipal());
                LOG.debug("Caller authorization:    " + check1);
                LOG.debug("Caller checkNotAdmin  " + check2);

                return !(check1 && check2);
            }
        }

        return true;

    }

    private boolean checkModificationAccessBySlot(String productID) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().checkPersonCanModifyEntity(DefaultSecurityManager.getInstance().getParentProcess(productID),
                                                                               DefaultSecurityManager.getInstance()
                                                                                       .getUserID(sessionCoontext.getCallerPrincipal().getName()));

    }

    /**
     * This check is called , when the caller is a modellierer and the method is createObject that is called to create a new process.
     * 
     * @param methodname the called methodname.
     * @param parameters the call parameter.
     * @param role the caller role.
     * @return true if the caller is modellierer.
     */
    private boolean checkCreateModelForModeler(String methodname, Object[] parameters, String role)
    {
        if (methodname.equals("createObject"))
        {
            if (role.equals("Modellierer"))
            {
                if (parameters != null && parameters.length >= 1)
                {
                    if (parameters[0].equals("model"))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This checks every not administrator caller if he can change the process model. <br>
     * this returns true if the process (in this context call) can be changed from the caller.
     * 
     * @param parameters the method parameter.
     * @return true if the caller can change the process.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private boolean checkNotAdminUser(Object[] parameters) throws OntologyErrorException
    {

        String modelID = (String) parameters[0];
        LOG.debug("checkNotAdminUser PARAM[0]   " + modelID);
        return DefaultSecurityManager.getInstance().checkPersonCanModifyEntity(modelID,
                                                                               organizationEntity.getUser(sessionCoontext.getCallerPrincipal()
                                                                                                                  .getName()).getID());
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
