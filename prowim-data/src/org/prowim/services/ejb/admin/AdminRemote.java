/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
 *
 */

package org.prowim.services.ejb.admin;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OntologyVersionArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RightsRoleArray;
import org.prowim.datamodel.collections.UpdateFrameArray;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.UpdateFrame;
import org.prowim.datamodel.prowim.UpdateItem;
import org.prowim.datamodel.prowim.UpdateWork;
import org.prowim.datamodel.prowim.UpdatesLog;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * The remote interface for the system info ws.
 * 
 * @author Saad Wardi
 * @version $Revision: 3983 $
 */
@WebService(name = ServiceConstants.PROWIM_ADMIN_REMOTE, targetNamespace = ServiceConstants.PROWIM_ADMIN_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface AdminRemote
{
    /**
     * Gets the prowim-ontology version.
     * 
     * @return prowim system version
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getOntologyVersion() throws OntologyErrorException;

    /**
     * Gets the prowim-services version.
     * 
     * @return not null version.
     */
    @WebMethod
    @WebResult(partName = "return")
    String getServerVersion();

    /**
     * Gets the prowim-model-editor version.
     * 
     * @return not null version.
     */
    @WebMethod
    @WebResult(partName = "return")
    String getModelEditorVersion();

    /**
     * Gets all versions.
     * 
     * @return not null {@link OntologyVersionArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    OntologyVersionArray getVersions() throws OntologyErrorException;

    /**
     * Sets the version as invalid.
     * 
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setVersionInvalid() throws OntologyErrorException;

    /**
     * Sets the version as invalid.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     * 
     */
    @WebMethod
    @WebResult(partName = "return")
    void setVersionValid() throws OntologyErrorException;

    /**
     * Sets the version as invalid.
     * 
     * @return true if the version is valid otherwise false.
     */
    @WebMethod
    @WebResult(partName = "return")
    boolean isVersionValid();

    /**
     * Gets the update frames.
     * 
     * @param versionID not null version ID.
     * @return not null {@link ObjectArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    UpdateFrameArray getUpdateFrames(String versionID) throws OntologyErrorException;

    /**
     * Gets the rules that will be executed to do an update of the prowimcore ontology to the selected version.
     * 
     * @param versionID not null version ID.
     * @return not null {@link UpdateWork}.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    UpdateWork getUpdateWork(String versionID) throws OntologyErrorException;

    /**
     * Executes the Updates on the ontology to make migration.
     * 
     * @param updateWork not null {@link UpdateWork}.
     * @return Not null {@link ObjectArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray executeUpdate(UpdateWork updateWork) throws OntologyErrorException;

    /**
     * Updates the ontology to the seleted version with id = versionID.
     * 
     * @param updateScript not null version ID.
     * @return Not null {@link ObjectArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod(operationName = "executeUpdateFromScript")
    @WebResult(partName = "return")
    boolean executeUpdate(String updateScript) throws OntologyErrorException;

    /**
     * Cleans the ontology.
     * 
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void clean() throws OntologyErrorException;

    /**
     * init the knowledgebase.
     * 
     */
    @WebMethod
    @WebResult(partName = "return")
    void initKnowledgeBase();

    /**
     * Searchs in for the persons their name contains the keyword.
     * 
     * @param keyWord not null keyword
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    PersonArray searchPerson(String keyWord) throws OntologyErrorException;

    /**
     * Changes the user password.
     * 
     * @param userID not null user ID.
     * @param newPassword not null user new password.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void changePassword(String userID, String newPassword) throws OntologyErrorException;

    /**
     * Logs user into the ProWim system.
     * 
     * @param userName not null username.
     * @param password not null password.
     * @return <code>true</code> if login was successful, else <code>false</code>
     * @throws OntologyErrorException if error occurs in ontology back end
     */

    @WebMethod
    @WebResult(partName = "return")
    boolean login(String userName, String password) throws OntologyErrorException;

    /**
     * Gets a user with a given username.
     * 
     * @param userName not null username.
     * @return {@link Person} or null if user was not found
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    Person getUserWithName(String userName) throws OntologyErrorException;

    /**
     * Gets a user with a given userID.
     * 
     * @param userID not null userID.
     * @return not null {@link Person}.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    Person getUserWithID(String userID) throws OntologyErrorException;

    /**
     * Adds a webservice into the webservices instances.
     * 
     * @param name the name of the webservice is the classname#methodname
     * @param description the name of the webservice is the classname#methodname
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void addWebservice(String name, String description) throws OntologyErrorException;

    /**
     * Gets rights roles assigned to the user with the given ID.
     * 
     * @param userID not null userID.
     * @return not null {@link RightsRoleArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RightsRoleArray getRightsRoles(String userID) throws OntologyErrorException;

    /**
     * Gets rights roles.
     * 
     * @return not null {@link RightsRoleArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RightsRoleArray getExistingRightsRoles() throws OntologyErrorException;

    /**
     * Gets {@link Person}s of given {@link RightsRole}. Returns a empty list, if no {@link Person}s are in this {@link RightsRole}
     * 
     * @param rightsRoleName the name of {@link RightsRole}. This can be a string in German because of ontology. NOT NULL <br>
     *        For existing {@link RightsRole} in ontology show please in ontology in "EXPERT-CLASS->Organisatin->Organbisatiosnelement->Rechrolle"
     * 
     * @return not null {@link RightsRoleArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    PersonArray getPersonsOfRightRoles(String rightsRoleName) throws OntologyErrorException;

    /**
     * Assignes rights roles to user.
     * 
     * @param userID the not null user ID.
     * @param rightsRoles not null rights role array.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void assigneRightsRolesToUser(String userID, RightsRoleArray rightsRoles) throws OntologyErrorException;

    /**
     * Sets the users that can modify the entity.
     * 
     * @param entityID the selected entity.
     * @param elements the list of users names that can modify the entity.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void setElementCanModifyEntity(String entityID, ObjectArray elements) throws OntologyErrorException;

    /**
     * Gets the persons that can modify the entity.
     * 
     * @param entityID the entity ID.
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    PersonArray getPersonCanModifyEntity(String entityID) throws OntologyErrorException;

    /**
     * Gets the organizations that can modify the entity.
     * 
     * @param entityID the entity ID.
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    OrganisationArray getOrganizationsCanModifyEntity(String entityID) throws OntologyErrorException;

    /**
     * Checks if a user can modify an entity.
     * 
     * @param entityID not null entityID.
     * @param personID not null personID.
     * @return true if the person can modify the entity, otherwise false.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    boolean canPersonModifyEntity(String entityID, String personID) throws OntologyErrorException;

    /**
     * Gets the wiki URL.
     * 
     * @return not null wiki URL configured in the properties file.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getWikiURL() throws OntologyErrorException;

    /**
     * 
     * Sends the content to all receivers, using a mail service.
     * 
     * @param content the content of the message
     * @param receiver the list of receivers
     * @param subject the subject of the message
     * @param footer the footer of the message
     */
    @WebMethod
    void sendMessage(String content, String[] receiver, String subject, String footer);

    /**
     * * Get JAX-RPC happy.
     * 
     * @return not to be used.
     */
    @Deprecated
    UpdateFrame getUpdates();

    /**
     * Get JAX-RPC happy.
     * 
     * @return not to be used.
     */
    @Deprecated
    UpdateWork getWork();

    /**
     * Get JAX-RPC happy.
     * 
     * @return not to be used.
     */
    @Deprecated
    UpdateItem getItem();

    /**
     * Get JAX-RPC happy.
     * 
     * @return not to be used.
     */
    @Deprecated
    UpdatesLog getLog();

    /**
     * Get JAX-RPC happy.
     * 
     * @return not to be used.
     */
    @Deprecated
    RightsRole getPermissions();

    /**
     * 
     * Encrypts passwords with SHA-1.
     * 
     * @return always true should be changed - no sense!
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @Deprecated
    boolean encryptPasswords() throws OntologyErrorException;

}
