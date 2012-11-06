/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-01 10:37:55 +0200 (Fr, 01 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/OrganizationEntity.java $
 * $LastChangedRevision: 5071 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.services.coreengine.entities;

import javax.ejb.Local;

import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RightsRoleArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.organization.OrganizationRemote;



/**
 * Creates a new organization, and get the organization data.
 * 
 * @author Saad Wardi
 * @version $Revision: 5071 $
 */
@Local
public interface OrganizationEntity
{
    /**
     * Creates a new organization.
     * 
     * @param name not null organization name.
     * @param address not null adress.
     * @param email not null email.
     * @param telefon not null telefon.
     * @param description not null description.
     * @return not null created {@link OrganizationEntity}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Organization createOrganization(String name, String address, String email, String telefon, String description) throws OntologyErrorException;

    /**
     * Deletes an organization.
     * 
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void deleteOrganization(String organizationID) throws OntologyErrorException;

    /**
     * Creates a new organization.
     * 
     * @param name not null organization name.
     * @param address not null adress.
     * @param email not null email.
     * @param telefon not null telefon.
     * @param description not null description.
     * @param parentOrgaID not null parent organization.
     * @return not null created {@link OrganizationEntity}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Organization createOrganization(String name, String address, String email, String telefon, String description, String parentOrgaID)
                                                                                                                                       throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#updateOrganization(String, String, String, String, String, String)}.
     * 
     * @param orgaID ID of organization.
     * 
     * @param name not null organization name.
     * @param address not null address.
     * @param email not null email.
     * @param telefon not null telephone.
     * @param description description of user.
     * @return not null created {@link OrganizationEntity}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Organization updateOrgaInfo(String orgaID, String name, String address, String email, String telefon, String description)
                                                                                                                             throws OntologyErrorException;

    /**
     * Gets the organization with the ID.
     * 
     * @param id not null ID.
     * @return not null {@link OrganizationEntity}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Organization getOrganization(String id) throws OntologyErrorException;

    /**
     * Gets all organizations.
     * 
     * @return not null {@link OrganisationArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    OrganisationArray getOrganizations() throws OntologyErrorException;

    /**
     * Gets all top organizations.
     * 
     * @return not null {@link OrganisationArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    OrganisationArray getTopOrganizations() throws OntologyErrorException;

    /**
     * Gets all sub organizations to a given organization ID.
     * 
     * @param orgaID ID of given organization.
     * 
     * @return not null {@link OrganisationArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    OrganisationArray getSubOrganizations(String orgaID) throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#createUser(String, String, String, String, String, String, String, String)}.
     * 
     * @param name not null organization name.
     * @param firstName not null first name.
     * @param lastName not null last name.
     * @param address not null adress.
     * @param email not null email.
     * @param telefon not null telefon.
     * @param organizationID not null organization ID.
     * @param password not null password. Empty string is required if no password is used.
     * @return not null created {@link OrganizationEntity}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Person createUser(String name, String firstName, String lastName, String address, String email, String telefon, String organizationID,
                      String password) throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#updateUser(String, String, String, String, String, String, String, String, String)}.
     * 
     * @param userID ID of user. not null.
     * @param name organization name. null is possible.
     * @param firstName not null first name.
     * @param lastName not null last name.
     * @param address not null adress.
     * @param email not null email.
     * @param telefon null is possible telefon.
     * @param description description of user. null is possible.
     * @param password the user password. can be null if the user data updated without password.
     * @return not null created {@link OrganizationEntity}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Person updateUserInfo(String userID, String name, String firstName, String lastName, String address, String email, String telefon,
                          String description, String password) throws OntologyErrorException;

    /**
     * Gets a Person with id = userID.
     * 
     * @param userID the not null userID.
     * @return {@link Person} is returned. If no Person with the userID exists, null is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Person getPerson(String userID) throws OntologyErrorException;

    /**
     * Gets a Person with username.
     * 
     * @param username the not null username.
     * @return {@link Person} is returned. If no Person with the userID exists, null is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    Person getUser(String username) throws OntologyErrorException;

    /**
     * Gets the members of an organization.
     * 
     * @param organizationID not null organization ID.
     * @return not null {@link PersonArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    PersonArray getMembers(String organizationID) throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#setPreSelection(String , PersonArray )}.
     * 
     * @param roleID not null role ID.
     * @param presSelection not null {@link PersonArray}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void setPreSelection(final String roleID, final PersonArray presSelection) throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#getPreSelection(String)}.
     * 
     * @param roleID not null role ID.
     * @return presSelection not null {@link PersonArray}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    PersonArray getPreSelection(final String roleID) throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#getPreSelection(String)}.
     * 
     * @param userID not null user ID.
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void setPersonAsMember(String userID, String organizationID) throws OntologyErrorException;

    /**
     * Searchs for persons with a given keyword.
     * 
     * @param pattern not null pattern.
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    PersonArray searchPersons(String pattern) throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#getPreSelection(String)}.
     * 
     * @param userID not null user ID.
     * @param newPassword not null password.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void changePassword(String userID, String newPassword) throws OntologyErrorException;

    /**
     * Gets the rights roles to a user.
     * 
     * @param userID not null user ID.
     * @return not null {@link RightsRoleArray}. IF no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RightsRoleArray getRightsRoles(String userID) throws OntologyErrorException;

    /**
     * Gets all the rights roles.
     * 
     * @return not null {@link RightsRoleArray}. IF no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RightsRoleArray getRightsRoles() throws OntologyErrorException;

    /**
     * Adds a role to an organization.
     * 
     * @param roleID not null role ID.
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void addRoleToOrganization(String roleID, String organizationID) throws OntologyErrorException;

    /**
     * Gets the roles defined in an organization.
     * 
     * @param organizationID not null organization ID.
     * @return not null {@link RoleArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RoleArray getRolesToOrganization(String organizationID) throws OntologyErrorException;

    /**
     * Removes roles from an organization.
     * 
     * @param organizationID not null organization ID.
     * @param roles not null list of roles IDs.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void removeRolesFromOrganization(ObjectArray roles, String organizationID) throws OntologyErrorException;

    /**
     * Moves organization as sub organization of another organization.
     * 
     * @param selectedOrganizationID not null organization ID.
     * @param parentOrganizationID not null parent Organization ID.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void moveOrganization(String selectedOrganizationID, String parentOrganizationID) throws OntologyErrorException;

    /**
     * Moves organization as sub organization of another organization.
     * 
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void moveOrganizationToTop(String organizationID) throws OntologyErrorException;

    /**
     * Assignes a {@link RightsRole} to a user.
     * 
     * @param userID not null userID.
     * @param rightsRoles the not null {@link RightsRoleArray} to be assigned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void assigneRightsRolesToUser(String userID, RightsRoleArray rightsRoles) throws OntologyErrorException;

    /**
     * Gets {@link Person}s of given {@link RightsRole}. Returns a empty list, if no {@link Person}s are in this {@link RightsRole}
     * 
     * @param rightsRoleName the name of {@link RightsRole}. This can be a string in German because of ontology. NOT NULL <br>
     *        For existing {@link RightsRole} in ontology show please in ontology in "EXPERT-CLASS->Organisatin->Organbisatiosnelement->Rechrolle"
     * 
     * @return not null {@link RightsRoleArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    PersonArray getPersonsOfRightRoles(String rightsRoleName) throws OntologyErrorException;

}
