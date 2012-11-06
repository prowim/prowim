/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-17 15:26:47 +0100 (Do, 17 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultRoleEntity.java $
 * $LastChangedRevision: 5045 $
 *------------------------------------------------------------------------------
 * (c) 07.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Role;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.coreengine.entities.RoleEntity;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the interface {@link RoleEntity}.
 * 
 * @author Saad Wardi.
 * @version $Revision: 5045 $
 */
@Stateless
public class DefaultRoleEntity implements RoleEntity
{

    private static final Logger LOG = Logger.getLogger(DefaultRoleEntity.class);

    @IgnoreDependency
    @EJB
    private ProcessEngine       processEngine;

    @IgnoreDependency
    @EJB
    private OrganizationEntity  organizationEntity;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.RoleEntity#getRole(java.lang.String)
     */
    @Override
    public Role getRole(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        final RecordSet records = processEngine.getRole(id);

        if (records.getNoOfRecords() > 0)
        {
            Hashtable<String, String> record = records.getRecords()[0];

            String name = record.get(ProcessEngineConstants.Variables.Common.NAME_EN);
            String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            String processID = record.get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
            String description = record.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);

            Role role = DefaultDataObjectFactory.createRole(id, name, createTime);
            role.setDescription(description);
            role.setPersonsList(getAssignedPersons(id));
            role.setStartRole(processID != null);
            return role;

        }
        else
        {
            throw new IllegalStateException("Could not create role with id =  " + id);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.RoleEntity#getActivityRoles(java.lang.String)
     */
    public RoleArray getActivityRoles(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        final RecordSet roleIDsRecords = processEngine.getActivityRoles(activityID);
        final RoleArray roles = new RoleArray();
        if (roleIDsRecords != null && roleIDsRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < roleIDsRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> roleIDsRecord = roleIDsRecords.getRecords()[i];
                String roleID = roleIDsRecord.get(ProcessEngineConstants.Variables.Role.ROLLE_ID);
                LOG.debug("getActivityRoles   " + roleID);
                if (roleID != null)
                {
                    Role role = getRole(roleID);
                    roles.add(role);
                }
            }
        }

        Collections.sort(roles.getItem());
        return roles;
    }

    /**
     * Returns the assigned persons to a role.
     * 
     * @param roleID not null role ID.
     * @return not null {@link PersonArray}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private List<Person> getAssignedPersons(String roleID) throws OntologyErrorException
    {
        /** collect the list of persons that will be assigned to the role and setPersonList. */
        final RecordSet personRecords = processEngine.getAssignedPersonsToRole(roleID);
        final List<Person> assignedPersons = new ArrayList<Person>();
        for (int j = 0; j < personRecords.getNoOfRecords(); j++)
        {
            Hashtable<String, String> personRecord = personRecords.getRecords()[j];
            String personID = personRecord.get(ProcessEngineConstants.Variables.Organisation.Person.ID);
            Person person = organizationEntity.getPerson(personID);
            if (person != null)
            {
                assignedPersons.add(person);
            }
        }
        return assignedPersons;
    }

}
