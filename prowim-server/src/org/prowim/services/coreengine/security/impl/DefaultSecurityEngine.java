/*==============================================================================
 * File $Id: DefaultSecurityEngine.java 4934 2010-10-20 15:24:29Z specht $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-20 17:24:29 +0200 (Mi, 20 Okt 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/security/impl/DefaultSecurityEngine.java $
 * $LastChangedRevision: 4934 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine.security.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.security.SecurityEngine;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;


/**
 * Title and description.
 * 
 * @author Saad Wardi
 * @version $Revision: 4934 $
 * @since !!VERSION!!
 */
@Stateless
public class DefaultSecurityEngine implements SecurityEngine
{

    /** the logger. */
    private static final Logger LOG = Logger.getLogger(DefaultSecurityEngine.class);
    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#login(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet login(String userName, String password) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.NAME_EN, new AlgernonValue(userName, true));
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.PASSWORD, new AlgernonValue(password, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.CHECK_USER_PASSWORD, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.CHECK_USER_PASSWORD, result, this.getClass());
        LOG.debug(this.getClass().getName() + " login: " + userName + "  " + result.getResult());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#getRightsRoles(java.lang.String)
     */
    @Override
    public RecordSet getRightsRoles(String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_RIGHTS_ROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_RIGHTS_ROLES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#getRoleAuthorization(java.lang.String)
     */
    @Override
    public RecordSet getRoleAuthorization(String roleID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(roleID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_ROLE_AUTHORIZATIONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_ROLE_AUTHORIZATIONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#getRoleAuthorization(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet getRoleAuthorization(String roleID, String methodname) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(roleID, true));
        tab.put(ProcessEngineConstants.Variables.Common.CALL, new AlgernonValue(methodname, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.CHECK_CALL_ALLOWED, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.CHECK_CALL_ALLOWED, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#getUserPassword(java.lang.String)
     */
    @Override
    public RecordSet getUserPassword(String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        LOG.info("Get user password for " + userID + " with " + myService);
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_USER_PASSWORD, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_USER_PASSWORD, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#checkPersonCanModifyEntity(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet checkPersonCanModifyEntity(String entityID, String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(entityID, true));
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.CHECK_PERSON_CAN_MODIFIY_ENTITY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.CHECK_PERSON_CAN_MODIFIY_ENTITY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#setUserCanModifyEntity(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setUserCanModifyEntity(String entityID, String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(entityID, false));
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.SET_PERSON_CAN_MODIFIY_ENTITY, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.SET_PERSON_CAN_MODIFIY_ENTITY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#isPersonModeler(java.lang.String)
     */
    @Override
    public RecordSet isPersonModeler(String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.IS_PERSON_MODELER, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.IS_PERSON_MODELER, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#isPersonModeler(java.lang.String)
     */
    @Override
    public RecordSet isPersonAdmin(String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.IS_PERSON_ADMIN, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.IS_PERSON_ADMIN, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#isPersonUser(java.lang.String)
     */
    @Override
    public RecordSet isPersonUser(String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.IS_PERSON_ADMIN, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.IS_PERSON_ADMIN, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#isPersonReader(java.lang.String)
     */
    @Override
    public RecordSet isPersonReader(String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.IS_PERSON_READER, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.IS_PERSON_READER, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#getElementCanModifyEntity(java.lang.String)
     */
    @Override
    public RecordSet getElementCanModifyEntity(String entityID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(entityID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_PERSON_CAN_MODIFIY_ENTITY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_PERSON_CAN_MODIFIY_ENTITY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#getOrganizationsCanModifyEntity(java.lang.String)
     */
    @Override
    public RecordSet getOrganizationsCanModifyEntity(String entityID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(entityID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_ORGANIZATIONS_CAN_MODIFIY_ENTITY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_ORGANIZATIONS_CAN_MODIFIY_ENTITY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.security.SecurityEngine#getParentProcess(java.lang.String)
     */
    @Override
    public RecordSet getParentProcess(String entityID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(entityID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_PARENT_PROCESS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_PARENT_PROCESS, result, this.getClass());
        return result;
    }

}
