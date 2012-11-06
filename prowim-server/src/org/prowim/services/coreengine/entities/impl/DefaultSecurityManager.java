/*==============================================================================
 * File $Id: DefaultSecurityManager.java 5032 2011-02-02 15:44:50Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:50 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultSecurityManager.java $
 * $LastChangedRevision: 5032 $
 *------------------------------------------------------------------------------
 * (c) 12.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.entities.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.datamodel.security.Authorization;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.SecurityManager;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.coreengine.security.SecurityEngine;
import org.prowim.services.coreengine.security.impl.DefaultSecurityEngine;

import de.ebcot.tools.crypt.Encrypter;
import de.ebcot.tools.logging.Logger;


/**
 * Implements {@link SecurityManager}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5032 $
 * @since 2.0.0
 */
public class DefaultSecurityManager implements SecurityManager
{
    /** the logger. */
    private static final Logger                        LOG              = Logger.getLogger(DefaultSecurityManager.class);
    private static DefaultSecurityManager              instance;
    private final HashMap<String, List<Authorization>> authorizationMap = new HashMap<String, List<Authorization>>();

    private SecurityEngine                             securityEngine;

    private OrganizationEntity                         organizationEntity;

    /**
     * Creates this and the engines.
     */
    protected DefaultSecurityManager()
    {
        String securityEngineJNDIname = null;
        String organizationEntityJNDIname = null;
        LOG.info("Lookup the security engine while initialising the security manager");
        try
        {
            InitialContext initialContext = new InitialContext();
            LOG.info("Initial Context created " + initialContext);

            securityEngineJNDIname = "prowimservices/" + DefaultSecurityEngine.class.getSimpleName() + "/local";
            Object ref = initialContext.lookup(securityEngineJNDIname);
            securityEngine = (SecurityEngine) PortableRemoteObject.narrow(ref, SecurityEngine.class);

            LOG.info("Security Engine initialised " + securityEngine);

            organizationEntityJNDIname = "prowimservices/" + DefaultOrganizationEntity.class.getSimpleName() + "/local";
            ref = initialContext.lookup(organizationEntityJNDIname);
            organizationEntity = (OrganizationEntity) PortableRemoteObject.narrow(ref, OrganizationEntity.class);

            LOG.info("Organization Entity initialised " + organizationEntity);
        }
        catch (NamingException e)
        {
            LOG.error("Could not lookup the security engine : " + securityEngine + " with name: " + securityEngineJNDIname, e);
            LOG.error("Could not lookup the Organization entity : " + organizationEntity + " with name: " + organizationEntityJNDIname, e);
        }
    }

    /**
     * 
     * Gets the instance this.
     * 
     * @return a singleton instance of this.
     */
    public static DefaultSecurityManager getInstance()
    {
        if (instance == null)
        {
            instance = new DefaultSecurityManager();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.coreengine.SecurityManager#login(java.lang.String, java.lang.String)
     */
    @Override
    public boolean login(String userName, String password) throws OntologyErrorException
    {
        String pass = Encrypter.encryptMessage(password.toCharArray());
        RecordSet records = securityEngine.login(userName, pass);
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            /** only user with rights roles can login. */
            if (this.getRightsRoles(this.getUserID(userName)).length > 0)
            {
                LOG.debug(this.getClass().getName() + " login " + userName + "  " + records.getResult());
                return true;
            }
            else
            {
                throw new IllegalStateException("No rights roles for user " + userName);
            }
        }
        else if (records.getResult().equals(AlgernonConstants.FAILED))
        {
            return false;
        }
        else
        {
            throw new IllegalStateException("Could not login user " + userName);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#getRightsRoles(java.lang.String)
     */
    @Override
    public RightsRole[] getRightsRoles(String userID) throws OntologyErrorException
    {
        RecordSet records = securityEngine.getRightsRoles(userID);
        RightsRole[] result = new RightsRole[0];
        if (records != null && records.getNoOfRecords() > 0)
        {
            result = new RightsRole[records.getNoOfRecords()];
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];
                String roleID = record.get(ProcessEngineConstants.Variables.Common.ID);
                String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                String description = record.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                String name = record.get(ProcessEngineConstants.Variables.Common.NAME_EN);
                RightsRole role = new RightsRole(roleID, createTime, name);
                role.setDescription(description);

                RecordSet autorizationRecords = securityEngine.getRoleAuthorization(roleID);
                if (autorizationRecords != null && autorizationRecords.getNoOfRecords() > 0)
                {
                    ObjectArray allowedServices = new ObjectArray();
                    result = new RightsRole[autorizationRecords.getNoOfRecords()];
                    for (int j = 0; j < autorizationRecords.getNoOfRecords(); j++)
                    {
                        String servicename = autorizationRecords.getRecords()[j].get(ProcessEngineConstants.Variables.Common.SERVICE_NAME);
                        allowedServices.add(servicename);
                    }
                    role.setAllowedServices(allowedServices);
                }
                result[i] = role;
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#isMethodIncluded(java.lang.String, java.lang.String)
     */
    @Override
    public boolean isMethodIncluded(String userID, String methodname) throws OntologyErrorException
    {
        boolean result = false;
        RecordSet records = securityEngine.getRightsRoles(userID);
        if (records != null && records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];
                String roleID = record.get(ProcessEngineConstants.Variables.Common.ID);
                // String name = record.get(ProcessEngineConstants.Variables.Common.NAME_EN);

                if (securityEngine.getRoleAuthorization(roleID, methodname).getResult().equals(AlgernonConstants.OK))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#createAuthorization(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Authorization createAuthorization(String userID, String methodname, String classname) throws OntologyErrorException
    {
        RecordSet records = securityEngine.getRightsRoles(userID);
        Authorization result = null;

        if (records != null && records.getNoOfRecords() > 0)
        {
            boolean isModeler = isPersonModelerUserWithID(userID);
            boolean isAdmin = isPersonAdminUserWithID(userID);

            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];
                String roleID = record.get(ProcessEngineConstants.Variables.Common.ID);
                String name = record.get(ProcessEngineConstants.Variables.Common.NAME_EN);

                if (securityEngine.getRoleAuthorization(roleID, classname + "#" + methodname).getResult().equals(AlgernonConstants.OK))
                {
                    LOG.debug("Authorizating USER to call " + classname + "#" + methodname + "  with role:  " + name);

                    if (isAdmin)
                    {
                        result = new Authorization(userID, roleID, "Administrator", classname, methodname, true);
                        break;
                    }

                    else if (isModeler)
                    {
                        result = new Authorization(userID, roleID, "Modellierer", classname, methodname, true);
                        break;
                    }
                    else
                        result = new Authorization(userID, roleID, name, classname, methodname, true);
                    System.out.println("The AUTHORIZATION:    " + result);
                    cacheAuthorization(result, userID);

                }
                else
                {
                    result = new Authorization(userID, roleID, name, classname, methodname, false);
                    cacheAuthorization(result, userID);

                }
            }
        }
        return result;
    }

    /**
     * Caches authorizations.
     * 
     * @param autorization not null authorization.
     * @param userID not null user ID.
     */
    private void cacheAuthorization(final Authorization autorization, final String userID)
    {
        List<Authorization> autorizations = authorizationMap.get(userID);
        if (autorizations == null)
        {
            autorizations = new ArrayList<Authorization>();
        }
        autorizations.add(autorization);
        authorizationMap.put(userID, autorizations);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#getUserPassword(java.lang.String)
     */
    @Override
    public String getUserPassword(String username) throws OntologyErrorException
    {
        Validate.notNull(username);
        LOG.debug("Get password for user " + username);

        Person person = organizationEntity.getUser(username);

        LOG.debug("Found person " + person + " for user " + username);

        String result = ProcessEngineConstants.Variables.Common.EMPTY;
        if (person != null)
        {
            RecordSet records = securityEngine.getUserPassword(person.getID());

            if (records != null && records.getNoOfRecords() > 0)
            {
                for (int i = 0; i < records.getNoOfRecords(); i++)
                {
                    Hashtable<String, String> record = records.getRecords()[i];
                    result = record.get(ProcessEngineConstants.Variables.Organisation.Person.PASSWORD);
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#isPersonAdminUser(java.lang.String)
     */
    @Override
    public boolean isPersonAdminUser(String username) throws OntologyErrorException
    {
        String userID = getUserID(username);
        if (userID != null)
        {
            return securityEngine.isPersonAdmin(userID).getResult().equals(AlgernonConstants.OK);
        }

        return false;
    }

    /**
     * Description.
     * 
     * @param userID the userID.
     * @return true if the person with ID = userID admin.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public boolean isPersonAdminUserWithID(String userID) throws OntologyErrorException
    {
        return securityEngine.isPersonAdmin(userID).getResult().equals(AlgernonConstants.OK);
    }

    /**
     * Description.
     * 
     * @param userID the userID.
     * @return true if the person with ID = userID admin.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public boolean isPersonModelerUserWithID(String userID) throws OntologyErrorException
    {
        return securityEngine.isPersonModeler(userID).getResult().equals(AlgernonConstants.OK);
    }

    /**
     * Description.
     * 
     * @param userID the userID.
     * @return true if the person with ID = userID admin.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public boolean isPersonBenutzerWithID(String userID) throws OntologyErrorException
    {
        return securityEngine.isPersonAdmin(userID).getResult().equals(AlgernonConstants.OK);
    }

    /**
     * Description.
     * 
     * @param userID the userID.
     * @return true if the person with ID = userID admin.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public boolean isPersonReaderWithID(String userID) throws OntologyErrorException
    {
        if (userID != null && !userID.equals(""))
        {
            return securityEngine.isPersonReader(userID).getResult().equals(AlgernonConstants.OK);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#isPersonModelerUser(java.lang.String)
     */
    @Override
    public boolean isPersonModelerUser(String username) throws OntologyErrorException
    {
        String userID = getUserID(username);

        if (userID != null)
        {
            return securityEngine.isPersonModeler(userID).getResult().equals(AlgernonConstants.OK);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#isPersonModelerUser(java.lang.String)
     */
    @Override
    public boolean isPersonReaderUser(String username) throws OntologyErrorException
    {
        String userID = getUserID(username);

        if (userID != null)
        {
            return securityEngine.isPersonReader(userID).getResult().equals(AlgernonConstants.OK);
        }

        return false;
    }

    /**
     * Returns a userID with given username.
     * 
     * @param username not null username.
     * @return userID, can be null, if user not found
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public String getUserID(String username) throws OntologyErrorException
    {
        Person person = organizationEntity.getUser(username);
        if (person != null)
        {
            return person.getID();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#checkPersonCanModifyEntity(java.lang.String, java.lang.String)
     */
    @Override
    public boolean checkPersonCanModifyEntity(String entityID, String userID) throws OntologyErrorException
    {
        if (userID == null)
        {
            return false;
        }

        if (this.isPersonAdminUserWithID(userID))
        {
            return true;
        }
        return securityEngine.checkPersonCanModifyEntity(entityID, userID).getResult().equals(AlgernonConstants.OK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#setUserCanModifyEntity(java.lang.String, java.lang.String)
     */
    @Override
    public void setUserCanModifyEntity(String entityID, String elementID) throws OntologyErrorException
    {
        securityEngine.setUserCanModifyEntity(entityID, elementID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#getPersonCanModifyEntity(java.lang.String)
     */
    @Override
    public PersonArray getPersonCanModifyEntity(String entityID) throws OntologyErrorException
    {
        final RecordSet records = securityEngine.getElementCanModifyEntity(entityID);
        final HashMap<String, Boolean> uniqueIDMAP = new HashMap<String, Boolean>();
        final PersonArray result = new PersonArray();
        if (records != null && records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];
                String userID = record.get(ProcessEngineConstants.Variables.Common.ELEMENT_ID);
                String classID = record.get(ProcessEngineConstants.Variables.Common.CLASS_ID);
                if (classID.equals(Relation.Classes.PERSON) && !uniqueIDMAP.containsKey(userID))
                {
                    LOG.debug("Add Person: " + userID);
                    result.add(organizationEntity.getPerson(userID));
                    uniqueIDMAP.put(userID, new Boolean(true));
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#getOrganizationsCanModifyEntity(java.lang.String)
     */
    @Override
    public OrganisationArray getOrganizationsCanModifyEntity(String entityID) throws OntologyErrorException
    {
        RecordSet records = securityEngine.getElementCanModifyEntity(entityID);
        HashMap<String, Boolean> uniqueIDMAP = new HashMap<String, Boolean>();
        OrganisationArray result = new OrganisationArray();
        if (records != null && records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];
                String orgID = record.get(ProcessEngineConstants.Variables.Common.ELEMENT_ID);
                String classID = record.get(ProcessEngineConstants.Variables.Common.CLASS_ID);
                if (classID.equals(Relation.Classes.ORGANIZATION) && !uniqueIDMAP.containsKey(orgID))
                {
                    LOG.debug("Add organization: " + orgID);
                    result.add(organizationEntity.getOrganization(orgID));
                    uniqueIDMAP.put(orgID, new Boolean(true));
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.SecurityManager#getParentProcess(java.lang.String)
     */
    @Override
    public String getParentProcess(String entityID) throws OntologyErrorException
    {
        RecordSet records = securityEngine.getParentProcess(entityID);
        String result = null;
        if (records != null && records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];
                result = record.get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
            }
        }

        return result;
    }

}
