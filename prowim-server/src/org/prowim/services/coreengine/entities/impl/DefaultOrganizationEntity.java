/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-10-18 18:27:07 +0200 (Di, 18 Okt 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultOrganizationEntity.java $
 * $LastChangedRevision: 5100 $
 *------------------------------------------------------------------------------
 * (c) 02.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.entities.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RightsRoleArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.datamodel.prowim.Role;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.AdministrationEngine;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.coreengine.entities.RoleEntity;

import de.ebcot.tools.logging.Logger;


/**
 * Creates a new organization, and get the organization data. Implements the interface {@link OrganizationEntity}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5100 $
 */
@Stateless(name = "DefaultOrganizationEntity")
public class DefaultOrganizationEntity implements OrganizationEntity
{
    private static final Logger  LOG = Logger.getLogger(DefaultOrganizationEntity.class);

    @IgnoreDependency
    @EJB
    private CommonEngine         commonEngine;

    @IgnoreDependency
    @EJB
    private ProcessEngine        processEngine;

    @IgnoreDependency
    @EJB
    private AdministrationEngine adminEngine;

    @IgnoreDependency
    @EJB
    private RoleEntity           roleEntity;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#createOrganization(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public Organization createOrganization(String name, String address, String email, String telefon, String description)
                                                                                                                         throws OntologyErrorException
    {
        Validate.notNull(name);
        return createNewOrganization(name, address, email, telefon, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#deleteOrganization(java.lang.String)
     */
    public void deleteOrganization(String organizationID) throws OntologyErrorException
    {
        commonEngine.deleteInstance(organizationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#createOrganization(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Organization createOrganization(String name, String address, String email, String telefon, String description, String parentOrgaID)
                                                                                                                                              throws OntologyErrorException
    {
        Validate.notNull(parentOrgaID);
        Validate.isTrue( !parentOrgaID.equals(ProcessEngineConstants.Variables.Common.EMPTY));
        Organization organization = createOrganization(name, address, email, telefon, description);
        if (organization != null)
        {
            if (commonEngine.setSlotValue(parentOrgaID, Relation.Slots.SUB_ORGA, organization.getID()).getResult().equals(AlgernonConstants.OK))
            {
                LOG.debug("createOrganization :  creates a sub organization with id = " + organization.getID() + " parent organization is : "
                        + parentOrgaID);
            }
            else
            {
                LOG.error("createOrganization :  could not create a sub organization with id = " + organization.getID()
                        + " parent organization is : " + parentOrgaID);
                throw new IllegalStateException("createOrganization :  could not create a sub organization with id = " + organization.getID()
                        + " parent organization is : " + parentOrgaID);
            }
        }

        return organization;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getOrganization(java.lang.String)
     */
    @Override
    public Organization getOrganization(String id) throws OntologyErrorException
    {

        Validate.notNull(id);
        final RecordSet organisationRecordset = processEngine.getOrganisation(id);

        if (organisationRecordset.getNoOfRecords() > 0)
        {
            Hashtable<String, String> organisationRecord = organisationRecordset.getRecords()[0];

            String name = organisationRecord.get(ProcessEngineConstants.Variables.Common.NAME);
            String createTime = organisationRecord.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            String address = organisationRecord.get(ProcessEngineConstants.Variables.Organisation.ADDRESS);
            String telefon = organisationRecord.get(ProcessEngineConstants.Variables.Organisation.TELEFON);
            String email = organisationRecord.get(ProcessEngineConstants.Variables.Organisation.EMAIL);
            String description = organisationRecord.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
            final Organization result = DefaultDataObjectFactory.createOrganisation(id, name, createTime, address, email, telefon, description);
            return result;
        }
        else
        {
            throw new IllegalStateException("Could not GET organization with id =  " + id);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getOrganizations()
     */
    public OrganisationArray getOrganizations() throws OntologyErrorException
    {
        RecordSet records = processEngine.getOrganisations();
        OrganisationArray organisationList = new OrganisationArray();
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> organisationsRecord = records.getRecords()[i];
                String id = organisationsRecord.get(ProcessEngineConstants.Variables.Common.ID);
                Organization organisation = getOrganization(id);
                organisationList.add(organisation);
            }
        }
        return organisationList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getTopOrganizations()
     */
    @Override
    public OrganisationArray getTopOrganizations() throws OntologyErrorException
    {
        RecordSet records = processEngine.getTopOrganisations();
        OrganisationArray organisationList = new OrganisationArray();
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> organisationsRecord = records.getRecords()[i];
                String id = organisationsRecord.get(ProcessEngineConstants.Variables.Common.ID);
                Organization organisation = getOrganization(id);
                organisationList.add(organisation);
            }
        }
        return organisationList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getSubOrganizations(java.lang.String)
     */
    @Override
    public OrganisationArray getSubOrganizations(String orgaID) throws OntologyErrorException
    {
        RecordSet records = processEngine.getSubOrganisations(orgaID);
        OrganisationArray organisationList = new OrganisationArray();
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> organisationsRecord = records.getRecords()[i];
                String id = organisationsRecord.get(ProcessEngineConstants.Variables.Common.ID);
                Organization organisation = getOrganization(id);
                organisationList.add(organisation);
            }
        }
        return organisationList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getPerson(java.lang.String)
     */
    public Person getPerson(final String userID) throws OntologyErrorException
    {
        Validate.notNull(userID);

        final RecordSet result = processEngine.getPerson(userID);

        if (result.getNoOfRecords() == 1)
        {
            final Hashtable<String, String> personRecord = result.getRecords()[0];
            final Person person = DefaultDataObjectFactory.createPerson(personRecord
                    .get(ProcessEngineConstants.Variables.Organisation.Person.PERSON_ID), personRecord
                    .get(ProcessEngineConstants.Variables.Organisation.Person.FIRSTNAME), personRecord
                    .get(ProcessEngineConstants.Variables.Organisation.Person.LASTNAME), personRecord
                    .get(ProcessEngineConstants.Variables.Common.NAME), personRecord
                    .get(ProcessEngineConstants.Variables.Organisation.Person.PASSWORD));

            person.setAddress(personRecord.get(ProcessEngineConstants.Variables.Organisation.ADDRESS));
            person.setEmailAddress(personRecord.get(ProcessEngineConstants.Variables.Organisation.EMAIL));
            person.setTelefon(personRecord.get(ProcessEngineConstants.Variables.Organisation.TELEFON));
            person.setOrganisation(personRecord.get(ProcessEngineConstants.Variables.Organisation.Person.ORG));
            person.setTitle(personRecord.get(ProcessEngineConstants.Variables.Organisation.Person.TITLE));
            person.setDescription(personRecord.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT));

            return person;
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getMembers(java.lang.String)
     */
    public PersonArray getMembers(String organizationID) throws OntologyErrorException
    {
        /** get the knowledgelinks. */
        final RecordSet personRecords = processEngine.getMembers(organizationID);
        PersonArray persons = new PersonArray();
        if (personRecords != null && personRecords.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < personRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> personRecord = personRecords.getRecords()[i];
                String personID = personRecord.get(ProcessEngineConstants.Variables.Common.ID);
                if (personID != null)
                {
                    Person person = this.getPerson(personID);
                    if (person != null)
                    {
                        persons.add(person);
                    }
                }
            }
        }
        return persons;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Person createUser(String name, String firstName, String lastName, String address, String email, String telefon, String organizationID,
                             String password) throws OntologyErrorException
    {
        Validate.notNull(name);
        Validate.notNull(password);
        if ( !adminEngine.usernameExists(name).getResult().equals(AlgernonConstants.OK))
        {
            return createNewUser(name, firstName, lastName, address, email, telefon, organizationID, password);
        }
        else
        {
            throw new IllegalStateException("Username exists already " + name);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#updateUserInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Person updateUserInfo(String userID, String name, String firstName, String lastName, String address, String email, String telefon,
                                 String description, String password) throws OntologyErrorException
    {
        Validate.notNull(userID);
        Validate.notNull(name);
        Validate.notNull(firstName);
        Validate.notNull(lastName);

        boolean clearStatus = commonEngine.clearRelationValue(userID, Relation.Slots.FIRSTNAME).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(userID, Relation.Slots.LASTNAME).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(userID, Relation.Slots.ADDRESS).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(userID, Relation.Slots.EMAIL).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(userID, Relation.Slots.TELEFON).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(userID, Relation.Slots.DESCRIPTION).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(userID, Relation.Slots.NAME).getResult().equals(AlgernonConstants.OK);
        if (password != null)
        {
            commonEngine.clearRelationValue(userID, Relation.Slots.PASSWORD).getResult().equals(AlgernonConstants.OK);
            commonEngine.setSlotValue(userID, Relation.Slots.PASSWORD, password).getResult().equals(AlgernonConstants.OK);
        }

        boolean setStatus = commonEngine.setSlotValue(userID, Relation.Slots.NAME, name).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(userID, Relation.Slots.FIRSTNAME, firstName).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(userID, Relation.Slots.LASTNAME, lastName).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(userID, Relation.Slots.ADDRESS, address).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(userID, Relation.Slots.EMAIL, email).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(userID, Relation.Slots.TELEFON, telefon).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(userID, Relation.Slots.DESCRIPTION, description).getResult().equals(AlgernonConstants.OK);
        if (clearStatus && setStatus)
        {
            LOG.debug("All attributes sets for instance with id = " + userID);
        }
        else
        {
            throw new IllegalStateException("Could not set attributes for instance with id = " + userID);
        }

        Person person = getPerson(userID);
        return person;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getPreSelection(java.lang.String)
     */
    @Override
    public PersonArray getPreSelection(final String roleID) throws OntologyErrorException
    {
        final RecordSet records = commonEngine.getPreSelection(roleID);
        final PersonArray persons = new PersonArray();
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ID);
                Person person = getPerson(id);
                if (person != null)
                {
                    persons.add(person);
                }
            }
        }
        return persons;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#setPreSelection(java.lang.String, org.prowim.datamodel.collections.PersonArray)
     */
    @Override
    public void setPreSelection(final String roleID, final PersonArray presSelection) throws OntologyErrorException
    {
        Validate.notNull(roleID);
        Validate.notNull(presSelection);
        Iterator<Person> it = presSelection.iterator();
        commonEngine.clearRelationValue(roleID, Relation.Slots.COULD_BE_OCCUPIED_FROM);
        while (it.hasNext())
        {
            Person person = it.next();
            commonEngine.setSlotValue(roleID, Relation.Slots.COULD_BE_OCCUPIED_FROM, person.getID());
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#setPersonAsMember(java.lang.String, java.lang.String)
     */
    @Override
    public void setPersonAsMember(String userID, String organizationID) throws OntologyErrorException
    {
        Validate.notNull(userID);
        Validate.notNull(organizationID);
        Person person = getPerson(userID);
        String oldOrganizationID = person.getOrganisation();
        boolean clearStatus = commonEngine.clearRelationValue(userID, Relation.Slots.IS_MEMBER).getResult().equals(AlgernonConstants.OK);
        boolean setStatus = commonEngine.setSlotValue(userID, Relation.Slots.IS_MEMBER, organizationID).getResult().equals(AlgernonConstants.OK);
        if (clearStatus && setStatus)
        {
            LOG.debug("User " + userID + "  changed to " + organizationID);
        }
        else
        {
            commonEngine.clearRelationValue(userID, Relation.Slots.IS_MEMBER);
            commonEngine.setSlotValue(userID, Relation.Slots.IS_MEMBER, oldOrganizationID);
            throw new IllegalStateException("Could not change person organization ! " + userID + " to  " + organizationID);
        }
    }

    /********** PRIVATE METHODS ********************************************************************/
    /**
     * {@link OrganizationEntity#createNewOrganization(String, String, String, String)}.
     * 
     * @param name not null name.
     * @param address not null adress.
     * @param email not null email.
     * @param telefon not null telefon.
     * @param description not null description.
     * @return not null {@link OrganizationEntity}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private Organization createNewOrganization(String name, String address, String email, String telefon, String description)
                                                                                                                             throws OntologyErrorException
    {
        Validate.notNull(name);
        final RecordSet createRecords = commonEngine.addInstance(Relation.Classes.ORGANIZATION, name);
        final String id;
        if (createRecords.getResult().equals(AlgernonConstants.OK))
        {
            id = createRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.ID);

            commonEngine.setSlotValue(id, Relation.Slots.ADDRESS, address).getResult().equals(AlgernonConstants.OK);
            commonEngine.setSlotValue(id, Relation.Slots.EMAIL, email).getResult().equals(AlgernonConstants.OK);
            commonEngine.setSlotValue(id, Relation.Slots.TELEFON, telefon).getResult().equals(AlgernonConstants.OK);
            commonEngine.setSlotValue(id, Relation.Slots.DESCRIPTION, description).getResult().equals(AlgernonConstants.OK);

            if (id != null)
            {
                LOG.debug("Created new Organization with ID :   " + id + "   and name:  " + name);
                return getOrganization(id);
            }

            else
            {
                LOG.debug("Remove the organization with ID :   " + id + "   and name:  " + name);
                commonEngine.deleteInstance(id);
            }
        }

        throw new IllegalStateException("Could not create organization!  ");
    }

    /**
     * Creates a new User.
     * 
     * @param name not null user name.
     * @param firstName not null first name.
     * @param lastName not null last name.
     * @param address not null address.
     * @param email not null email.
     * @param telefon not null telephone.
     * @param organizationID not null organizationID.
     * @param password not null password.
     * @return new Person, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private Person createNewUser(String name, String firstName, String lastName, String address, String email, String telefon, String organizationID,
                                 String password) throws OntologyErrorException
    {
        final RecordSet createRecords = commonEngine.addInstance(Relation.Classes.PERSON, name);
        final String id;
        if (createRecords.getResult().equals(AlgernonConstants.OK))
        {
            id = createRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.ID);

            boolean status = commonEngine.setSlotValue(id, Relation.Slots.ADDRESS, address).getResult().equals(AlgernonConstants.OK);
            commonEngine.setSlotValue(id, Relation.Slots.EMAIL, email).getResult().equals(AlgernonConstants.OK);
            commonEngine.setSlotValue(id, Relation.Slots.TELEFON, telefon).getResult().equals(AlgernonConstants.OK);

            addMember(id, organizationID);

            status = commonEngine.setSlotValue(id, Relation.Slots.FIRSTNAME, firstName).getResult().equals(AlgernonConstants.OK);
            status = status && commonEngine.setSlotValue(id, Relation.Slots.LASTNAME, lastName).getResult().equals(AlgernonConstants.OK);
            status = status && commonEngine.setSlotValue(id, Relation.Slots.PASSWORD, password).getResult().equals(AlgernonConstants.OK);

            if (status)
            {
                LOG.debug("A new Person with ID :   " + id + " and name:  " + name + " was created");
                return getPerson(id);

            }
            else
            {
                LOG.debug("Remove the Person with ID :   " + id + "   and name:  " + name);
                commonEngine.deleteInstance(id);
            }
        }

        throw new IllegalStateException("Could not create person!  ");
    }

    /**
     * add a person to an organization.
     * 
     * @param userID not null user ID.
     * @param organizationID not null organization ID.
     * @return true if it succeedds, false otherwise.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private boolean addMember(String userID, String organizationID) throws OntologyErrorException
    {
        return commonEngine.setSlotValue(userID, Relation.Slots.IS_MEMBER, organizationID).getResult().equals(AlgernonConstants.OK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#searchPersons(java.lang.String)
     */
    @Override
    public PersonArray searchPersons(String pattern) throws OntologyErrorException
    {
        RecordSet records = adminEngine.searchPersons(pattern);
        final PersonArray persons = new PersonArray();
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ID);
                persons.add(getPerson(id));
            }
        }
        return persons;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#changePassword(java.lang.String, java.lang.String)
     */
    @Override
    public void changePassword(String userID, String newPassword) throws OntologyErrorException
    {
        RecordSet records = adminEngine.changePassword(userID, newPassword);
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug(this.getClass().getName() + " changePassword " + userID + "  " + records.getResult());
        }
        else
        {
            throw new IllegalStateException("Could not change Password for user " + userID);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#updateOrgaInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Organization updateOrgaInfo(String orgaID, String name, String address, String email, String telefon, String description)
                                                                                                                                    throws OntologyErrorException
    {
        Validate.notNull(orgaID);
        Validate.notNull(name);

        boolean clearStatus = commonEngine.clearRelationValue(orgaID, Relation.Slots.NAME).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(orgaID, Relation.Slots.ADDRESS).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(orgaID, Relation.Slots.EMAIL).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(orgaID, Relation.Slots.TELEFON).getResult().equals(AlgernonConstants.OK);
        clearStatus = clearStatus && commonEngine.clearRelationValue(orgaID, Relation.Slots.DESCRIPTION).getResult().equals(AlgernonConstants.OK);

        boolean setStatus = commonEngine.setSlotValue(orgaID, Relation.Slots.NAME, name).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(orgaID, Relation.Slots.ADDRESS, address).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(orgaID, Relation.Slots.EMAIL, email).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(orgaID, Relation.Slots.TELEFON, telefon).getResult().equals(AlgernonConstants.OK);
        setStatus = setStatus && commonEngine.setSlotValue(orgaID, Relation.Slots.DESCRIPTION, description).getResult().equals(AlgernonConstants.OK);

        if (clearStatus && setStatus)
        {
            LOG.debug("All attributes sets for instance with id = " + orgaID);
        }
        else
        {
            throw new IllegalStateException("Could not set attributes for instance with id = " + orgaID);
        }

        Organization organization = getOrganization(orgaID);
        return organization;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getUser(java.lang.String)
     */
    @Override
    public Person getUser(String username) throws OntologyErrorException
    {
        Validate.notNull(username);
        RecordSet records = adminEngine.getUser(username);
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            String id = records.getRecords()[0].get(EditorEngineConstants.Variables.Common.ID);
            Person result = getPerson(id);
            return result;
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getRightsRoles(java.lang.String)
     */
    @Override
    public RightsRoleArray getRightsRoles(String userID) throws OntologyErrorException
    {
        RecordSet records = adminEngine.getRightsRoles(userID);
        final RightsRoleArray rightsRoles = new RightsRoleArray();
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ID);
                String name = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.NAME);
                String description = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                String createTime = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                RightsRole role = new RightsRole(id, createTime, name);
                role.setDescription(description);
                rightsRoles.add(role);
            }
        }
        return rightsRoles;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getRightsRoles()
     */
    @Override
    public RightsRoleArray getRightsRoles() throws OntologyErrorException
    {
        RecordSet records = adminEngine.getRightsRoles();
        final RightsRoleArray rightsRoles = new RightsRoleArray();
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ID);
                String name = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.NAME);
                String description = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                String createTime = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                RightsRole role = new RightsRole(id, createTime, name);
                role.setDescription(description);
                rightsRoles.add(role);
            }
        }
        return rightsRoles;
    }

    /**
     * Assignes rights roles to user.
     * 
     * @param userID not null user ID.
     * @param rightsRoles not null rights roles array.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public void assigneRightsRolesToUser(String userID, RightsRoleArray rightsRoles) throws OntologyErrorException
    {
        Validate.notNull(userID);
        Validate.notNull(rightsRoles);
        List<String> notAssigned = new ArrayList<String>();
        boolean clearStatus = commonEngine.clearRelationValue(userID, Relation.Slots.RIGHTS_ROLES).getResult().equals(AlgernonConstants.OK);

        if (clearStatus)
        {
            Iterator<RightsRole> it = rightsRoles.iterator();
            while (it.hasNext())
            {
                RightsRole role = it.next();
                boolean setStatus = commonEngine.setSlotValue(userID, Relation.Slots.RIGHTS_ROLES, role.getID()).getResult()
                        .equals(AlgernonConstants.OK);

                if ( !setStatus)
                {
                    notAssigned.add(role.getID());
                    throw new IllegalStateException("Could not assigne RightRole " + role.getName() + "  ID " + role.getID());
                }
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#addRoleToOrganization(java.lang.String, java.lang.String)
     */
    @Override
    public void addRoleToOrganization(String roleID, String organizationID) throws OntologyErrorException
    {
        if (processEngine.addRoleToOrganization(roleID, organizationID).getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("Role " + roleID + "  added to organization " + organizationID);
        }
        else
        {
            throw new IllegalStateException("Could not add role to organization " + roleID + " " + organizationID);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getRolesToOrganization(java.lang.String)
     */
    @Override
    public RoleArray getRolesToOrganization(String organizationID) throws OntologyErrorException
    {
        final RoleArray result = new RoleArray();
        final RecordSet records = processEngine.getRolesToOrganization(organizationID);
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String roleID = records.getRecords()[i].get(ProcessEngineConstants.Variables.Role.ROLLE_ID);
                Role role = roleEntity.getRole(roleID);
                if (role != null)
                    result.add(role);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#removeRolesFromOrganization(org.prowim.datamodel.collections.ObjectArray, java.lang.String)
     */
    public void removeRolesFromOrganization(ObjectArray roles, String organizationID) throws OntologyErrorException
    {
        final RoleArray existingRoles = getRolesToOrganization(organizationID);
        boolean clearStatus = commonEngine.clearRelationValue(organizationID, Relation.Slots.HAS_ROLES).getResult().equals(AlgernonConstants.OK);
        Iterator<Object> inputIt = roles.iterator();
        Iterator<Role> existingIt = existingRoles.iterator();
        if (clearStatus)
        {
            while (inputIt.hasNext())
            {
                String toDelete = (String) inputIt.next();
                while (existingIt.hasNext())
                {
                    Role existingRole = existingIt.next();
                    if (existingRole.getID().equals(toDelete))
                    {
                        existingIt.remove();
                    }
                }
            }
            existingIt = existingRoles.iterator();
            while (existingIt.hasNext())
            {
                Role toAdd = existingIt.next();
                addRoleToOrganization(toAdd.getID(), organizationID);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#moveOrganization(java.lang.String, java.lang.String)
     */
    public void moveOrganization(String selectedOrganizationID, String parentOrganizationID) throws OntologyErrorException
    {
        /**
         * We can not allow that an organization sub organization of it self. That causes an endless actions on opening the sub organizations. The is no semantic on this usecase.
         * */

        if (selectedOrganizationID.equals(parentOrganizationID))
        {
            throw new IllegalStateException("moveOrganization :  organization as suborganization of itself is not allowed" + selectedOrganizationID
                    + " to the parent organization : " + parentOrganizationID + "");
        }
        else
        {
            if (commonEngine.setSlotValue(parentOrganizationID, Relation.Slots.SUB_ORGA, selectedOrganizationID).getResult()
                    .equals(AlgernonConstants.OK))
            {
                LOG.debug("moveOrganization :  Moved a sub organization with id = " + selectedOrganizationID + " parent organization is : "
                        + selectedOrganizationID);
            }
            else
            {
                LOG.error("moveOrganization :  could not move the organization with id = " + selectedOrganizationID
                        + " to the parent organization : " + parentOrganizationID);
                throw new IllegalStateException("moveOrganization :  could not move the organization with id = " + selectedOrganizationID
                        + " to the parent organization : " + parentOrganizationID);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#moveOrganizationToTop(java.lang.String)
     */
    @Override
    public void moveOrganizationToTop(String organizationID) throws OntologyErrorException
    {
        commonEngine.clearRelationValue(organizationID, Relation.Slots.BELONGS_TO);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.OrganizationEntity#getPersonsOfRightRoles(java.lang.String)
     */
    @Override
    public PersonArray getPersonsOfRightRoles(String rightsRoleName) throws OntologyErrorException
    {
        RecordSet records = adminEngine.getPersonsOfRightRoles(rightsRoleName);
        final PersonArray persons = new PersonArray();
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EngineConstants.Variables.Person.PERSON_ID);
                Person person = getPerson(id);
                persons.add(person);
            }
        }
        return persons;
    }
}
