/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-01 10:37:55 +0200 (Fr, 01 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/DefaultAdministrationEngine.java $
 * $LastChangedRevision: 5071 $
 *------------------------------------------------------------------------------
 * (c) 24.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.AdministrationEngine;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;
import de.ebcot.tools.string.EscapeFunctions;


/**
 * Title and description.
 * 
 * @author !!YOUR NAME!!
 * @version $Revision: 5071 $
 * @since !!VERSION!!
 */
@Stateless
public class DefaultAdministrationEngine implements AdministrationEngine
{
    /** the logger. */
    private static final Logger LOG = Logger.getLogger(DefaultUpdateEngine.class);

    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.AdministrationEngine#searchPersons(java.lang.String)
     */
    @Override
    public RecordSet searchPersons(String pattern) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PATTERN, new AlgernonValue(pattern, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.SEARCH_PERSONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.SEARCH_PERSONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.AdministrationEngine#changePassword(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet changePassword(String userID, String newPassword) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.ID, new AlgernonValue(userID, true));
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.PASSWORD, new AlgernonValue(newPassword, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.CHANGE_USER_PASSWORD, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.CHANGE_USER_PASSWORD, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.AdministrationEngine#getUser(java.lang.String)
     */
    @Override
    public RecordSet getUser(String username) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.NAME_EN, new AlgernonValue(username, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_USER_WITH_NAME, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_USER_WITH_NAME, result, this.getClass());
        LOG.debug(this.getClass().getName() + " login: " + username + "  " + result.getResult());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.AdministrationEngine#usernameExists(java.lang.String)
     */
    @Override
    public RecordSet usernameExists(String username) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.NAME_EN, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(username), true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.CHECK_USERNAME, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.CHECK_USERNAME, result, this.getClass());
        LOG.debug(this.getClass().getName() + " usernameExists: " + username + "  " + result.getResult());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.AdministrationEngine#getRightsRoles(java.lang.String)
     */
    @Override
    public RecordSet getRightsRoles(String userID) throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_RIGHTSROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_RIGHTSROLES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.AdministrationEngine#getRightsRoles()
     */
    @Override
    public RecordSet getRightsRoles() throws OntologyErrorException
    {
        /** 1. Get the user with the given username. */
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_ALL_RIGHTSROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_ALL_RIGHTSROLES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.AdministrationEngine#getPersonsOfRightRoles(java.lang.String)
     */
    @Override
    public RecordSet getPersonsOfRightRoles(String rightsRoleName) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Role.ROLE_NAME, new AlgernonValue(rightsRoleName, true));
        RecordSet result = myService.executeRule(EngineConstants.Rules.Admin.GET_PERSONS_OF_RIGHT_ROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Admin.GET_PERSONS_OF_RIGHT_ROLES, result, this.getClass());
        return result;
    }

}
