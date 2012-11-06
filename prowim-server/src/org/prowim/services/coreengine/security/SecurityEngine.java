/*==============================================================================
 * File $Id: SecurityEngine.java 4934 2010-10-20 15:24:29Z specht $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-20 17:24:29 +0200 (Mi, 20 Okt 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/security/SecurityEngine.java $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.services.coreengine.security;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * The security engine.
 * 
 * @author Thomas Wiesner
 * @version $Revision: 4934 $
 * @since !!VERSION!!
 */
@Local
public interface SecurityEngine
{

    /**
     * logs user in.
     * 
     * @param userName the not null username
     * @param password not null password.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet login(String userName, String password) throws OntologyErrorException;

    /**
     * Gets the rights roles.
     * 
     * @param userID username.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet getRightsRoles(String userID) throws OntologyErrorException;

    /**
     * Gets the role authorizations.
     * 
     * @param roleID not null role ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet getRoleAuthorization(String roleID) throws OntologyErrorException;

    /**
     * Gets the role authorizations.
     * 
     * @param roleID not null role ID.
     * @param methodname the methodname.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet getRoleAuthorization(String roleID, String methodname) throws OntologyErrorException;

    /**
     * Gets the user password.
     * 
     * @param userID not null user ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet getUserPassword(String userID) throws OntologyErrorException;

    /**
     * Checks if entities can be modified from a user with username..
     * 
     * @param entityID not null entitiy ID.
     * @param userID not null username.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet checkPersonCanModifyEntity(String entityID, String userID) throws OntologyErrorException;

    /**
     * Checks if entities can be modified from a user with username..
     * 
     * @param entityID not null entitiy ID.
     * @param userID not null userID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet setUserCanModifyEntity(String entityID, String userID) throws OntologyErrorException;

    /**
     * Checks if a person is modeler.
     * 
     * @param userID the username.
     * @return @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet isPersonModeler(String userID) throws OntologyErrorException;

    /**
     * Checks if a person is admin.
     * 
     * @param userID the user ID, may not be null
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet isPersonAdmin(String userID) throws OntologyErrorException;

    /**
     * Checks if a person is normal user.
     * 
     * @param userID the user ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet isPersonUser(String userID) throws OntologyErrorException;

    /**
     * Checks if a person is reader user.
     * 
     * @param userID the user ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet isPersonReader(String userID) throws OntologyErrorException;

    /**
     * Gets the persons can modify the entity with id = entityID.
     * 
     * @param entityID the not null entity ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet getElementCanModifyEntity(String entityID) throws OntologyErrorException;

    /**
     * Gets the organizations can modify the entity with id = entityID.
     * 
     * @param entityID the not null entity ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet getOrganizationsCanModifyEntity(String entityID) throws OntologyErrorException;

    /**
     * Gets the process where the entity is defined.
     * 
     * @param entityID the not null entity ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    RecordSet getParentProcess(String entityID) throws OntologyErrorException;

}
