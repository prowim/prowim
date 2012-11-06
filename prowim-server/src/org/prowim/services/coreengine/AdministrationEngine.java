/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-01 10:37:55 +0200 (Fr, 01 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/AdministrationEngine.java $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.RightsRoleArray;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Title and description.
 * 
 * @author !!YOUR NAME!!
 * @version $Revision: 5071 $
 * @since !!VERSION!!
 */
@Local
public interface AdministrationEngine
{
    /**
     * Searchs for persons with a given pattern.
     * 
     * @param pattern not null pattern.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet searchPersons(String pattern) throws OntologyErrorException;

    /**
     * Changes the user password.
     * 
     * @param userID not null user ID.
     * @param newPassword not null password.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet changePassword(String userID, String newPassword) throws OntologyErrorException;

    /**
     * Gets a user with username.
     * 
     * @param userName not null username.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet getUser(String userName) throws OntologyErrorException;

    /**
     * Checks if a username already exists.
     * 
     * @param username the username to be checked.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet usernameExists(String username) throws OntologyErrorException;

    /**
     * Gets the rights roles assigend to a user.
     * 
     * @param userID not null user ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet getRightsRoles(String userID) throws OntologyErrorException;

    /**
     * Gets the rights roles assigend to a user.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    RecordSet getRightsRoles() throws OntologyErrorException;

    /**
     * Gets {@link Person}s of given {@link RightsRole}. Returns a empty list, if no {@link Person}s are in this {@link RightsRole}
     * 
     * @param rightsRoleName the name of {@link RightsRole}. This can be a string in German because of ontology. NOT NULL <br>
     *        For existing {@link RightsRole} in ontology show please in ontology in "EXPERT-CLASS->Organisatin->Organbisatiosnelement->Rechrolle"
     * 
     * @return not null {@link RightsRoleArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet getPersonsOfRightRoles(String rightsRoleName) throws OntologyErrorException;

}
