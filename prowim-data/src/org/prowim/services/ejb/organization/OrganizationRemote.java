/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-04-27 13:40:09 +0200 (Di, 27 Apr 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/organization/OrganizationRemote.java $
 * $LastChangedRevision: 3712 $
 *------------------------------------------------------------------------------
 * (c) 01.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.services.ejb.organization;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.factory.ProwimDataObjectFactory;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Collects all methods to receive, creates and edit organization and person objects.
 * 
 * @author Saad Wardi
 * @version $Revision: 3712 $
 */
@WebService(name = ServiceConstants.PROWIM_ORGANIZATION_REMOTE, targetNamespace = ServiceConstants.PROWIM_ORGANIZATION_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({ ProwimDataObjectFactory.class })
// @WebContext(authMethod = "BASIC")
public interface OrganizationRemote
{
    /**
     * Gets registered users.
     * 
     * @return PersonArray. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    PersonArray getUsers() throws OntologyErrorException;

    /**
     * Gets the registered organisations.
     * 
     * @return not null {@link OrganisationArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    OrganisationArray getOrganizations() throws OntologyErrorException;

    /**
     * Gets the top registered organizations.
     * 
     * @return not null {@link OrganisationArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    OrganisationArray getTopOrganizations() throws OntologyErrorException;

    /**
     * Gets the sub organizations of given organization.
     * 
     * @param orgaID ID of given organization.
     * 
     * @return not null {@link OrganisationArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    OrganisationArray getSubOrganizations(String orgaID) throws OntologyErrorException;

    /**
     * Creates a new organization.
     * 
     * @param name not null organization name.
     * @param adress not null adress.
     * @param email not null email adress.
     * @param telefon not null telefon.
     * @param description not null description.
     * @return not null created {@link Organization}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod(operationName = "createOrganization")
    @WebResult(partName = "return")
    Organization createOrganization(String name, String adress, String email, String telefon, String description) throws OntologyErrorException;

    /**
     * Creates a new organization.
     * 
     * @param name not null organization name.
     * @param adress not null adress.
     * @param email not null email adress.
     * @param telefon not null telefon.
     * @param description not null description.
     * @param parentOrgaID not null parent Organization
     * @return not null created {@link Organization}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod(operationName = "createSubOrganization")
    @WebResult(partName = "return")
    Organization createOrganization(String name, String adress, String email, String telefon, String description, String parentOrgaID)
                                                                                                                                      throws OntologyErrorException;

    /**
     * Deletes an organization.
     * 
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void deleteOrganization(String organizationID) throws OntologyErrorException;

    /**
     * Updates the organization data.
     * 
     * @param orgaID ID of organization.
     * 
     * @param name not null organization name.
     * @param address not null address.
     * @param email not null email.
     * @param telefon not null telephone.
     * @param description description of user.
     * @return not null created {@link Organization}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    Organization updateOrganization(String orgaID, String name, String address, String email, String telefon, String description)
                                                                                                                                 throws OntologyErrorException;

    /**
     * Gets the members of an organization.
     * 
     * @param organizationID not null organization ID.
     * @return not null {@link PersonArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    PersonArray getMembers(String organizationID) throws OntologyErrorException;

    /**
     * Creates a new user.
     * 
     * @param name not null organization name.
     * @param firstName not null first name.
     * @param lastName not null last name.
     * @param adress not null adress.
     * @param email not null email.
     * @param telefon not null telefon.
     * @param organizationID not null organization ID.
     * @param password the user password. Not null. If no password is required for the user, an empty string is required.
     * @return not null created {@link Person}
     * @throws OntologyErrorException if an error occurs in ontology back end
     * 
     */
    @WebMethod
    @WebResult(partName = "return")
    Person createUser(String name, String firstName, String lastName, String adress, String email, String telefon, String organizationID,
                      String password) throws OntologyErrorException;

    /**
     * Updates the user data.
     * 
     * @param userID ID of user.
     * 
     * @param name not null organization name.
     * @param firstName not null first name.
     * @param lastName not null last name.
     * @param address not null address.
     * @param email not null email.
     * @param telefon not null telephone.
     * @param description description of user.
     * @param password the user password.
     * @return not null created {@link Person}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    Person updateUser(String userID, String name, String firstName, String lastName, String address, String email, String telefon,
                      String description, String password) throws OntologyErrorException;

    /**
     * Sets the set of person, that can occupy the role.
     * 
     * @param roleID not null role ID.
     * @param presSelection not null list of persons.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setPreSelection(String roleID, PersonArray presSelection) throws OntologyErrorException;

    /**
     * Gets the list of person, that can occupy the role.
     * 
     * @param roleID not null role ID.
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    PersonArray getPreSelection(String roleID) throws OntologyErrorException;

    /**
     * Sets a person with ID = userID as member of the organization with ID = organizationID.
     * 
     * @param userID not null user ID.
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setPersonAsMember(String userID, String organizationID) throws OntologyErrorException;

    /**
     * Adds a role to an organization.
     * 
     * @param roleID not null role ID.
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void addRoleToOrganization(String roleID, String organizationID) throws OntologyErrorException;

    /**
     * Gets the roles defined in an organization.
     * 
     * @param organizationID not null organization ID.
     * @return not null {@link RoleArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RoleArray getRolesToOrganization(String organizationID) throws OntologyErrorException;

    /**
     * Removes roles from an organization.
     * 
     * @param organizationID not null organization ID.
     * @param roles not null list of roles.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void removeRolesFromOrganization(final ObjectArray roles, String organizationID) throws OntologyErrorException;

    /**
     * Moves organization as sub organization of another organization.
     * 
     * @param selectedOrganizationID not null organization ID.
     * @param parentOrganizationID not null parent Organization ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void moveOrganization(String selectedOrganizationID, String parentOrganizationID) throws OntologyErrorException;

    /**
     * Moves organization as sub organization of another organization.
     * 
     * @param organizationID not null organization ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void moveOrganizationToTop(String organizationID) throws OntologyErrorException;

}
