/*==============================================================================
 * File $Id: SecurityManager.java 5032 2011-02-02 15:44:50Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:50 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/SecurityManager.java $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine;

import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.security.Authorization;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.admin.AdminRemote;



/**
 * An interfaces that defines methods to manage the security aspects to authorize and login users.
 * 
 * @author Saad Wardi
 * @version $Revision: 5032 $
 * @since 2.0
 */
public interface SecurityManager
{

    /**
     * {@link AdminRemote#login(String, String)}.
     * 
     * @param userName not null username .
     * @param password not null password.
     * @return <code>true</code> if login was successful, else <code>false</code>
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean login(String userName, String password) throws OntologyErrorException;

    /**
     * Gets the right role for a user.
     * 
     * @param userID the user ID.
     * @return {@link RightsRole}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RightsRole[] getRightsRoles(String userID) throws OntologyErrorException;

    /**
     * Checks if a method is included in the alloed methods, that the user can call.
     * 
     * @param userID the user ID.
     * @param methodname the methodname.
     * @return {@link RightsRole}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isMethodIncluded(String userID, String methodname) throws OntologyErrorException;

    /**
     * Checks if a method is included in the alloed methods, that the user can call.
     * 
     * @param userID the user ID.
     * @param methodname the methodname.
     * @param classname the classname.
     * @return {@link Authorization}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Authorization createAuthorization(String userID, String methodname, String classname) throws OntologyErrorException;

    /**
     * Gets the user password.
     * 
     * @param username not null username.
     * @return password.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getUserPassword(String username) throws OntologyErrorException;

    /**
     * Checks if a person admin user.
     * 
     * @param username the not null username.
     * @return true if the person admin user.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isPersonAdminUser(String username) throws OntologyErrorException;

    /**
     * Checks if a person modeler user.
     * 
     * @param username the not null username.
     * @return true if the person admin user.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isPersonModelerUser(String username) throws OntologyErrorException;

    /**
     * Checks if a person reader user.
     * 
     * @param username the not null username.
     * @return true if the person admin user.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isPersonReaderUser(String username) throws OntologyErrorException;

    /**
     * Checks if a person with username can modify an entity with id = entityID.
     * 
     * @param entityID the not null entity ID.
     * @param userID the userID, can be null
     * @return true if the person is included. False otherwise.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean checkPersonCanModifyEntity(String entityID, String userID) throws OntologyErrorException;

    /**
     * Set a person with username can modify an entity with id = entityID.
     * 
     * @param entityID the not null entity ID.
     * @param elementID not null userID, organizationID or roleID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setUserCanModifyEntity(String entityID, String elementID) throws OntologyErrorException;

    /**
     * Gets the persons can modify the entity with id = entityID.
     * 
     * @param entityID the not null entity ID.
     * @return not null {@link PersonArray}.If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    PersonArray getPersonCanModifyEntity(String entityID) throws OntologyErrorException;

    /**
     * Gets the process, where the entity is defined.
     * 
     * @param entityID the not null entity ID.
     * @return process ID or null if the entity is not assigned to any process.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getParentProcess(String entityID) throws OntologyErrorException;

    /**
     * Gets the organizations that can modify the entity.
     * 
     * @param entityID the entity ID.
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    OrganisationArray getOrganizationsCanModifyEntity(String entityID) throws OntologyErrorException;

}
