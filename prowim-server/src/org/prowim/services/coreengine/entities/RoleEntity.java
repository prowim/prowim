/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/RoleEntity.java $
 * $LastChangedRevision: 4510 $
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
package org.prowim.services.coreengine.entities;

import javax.ejb.Local;

import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Role;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.impl.DefaultRoleEntity;



/**
 * Provides an interface with access to the role data stored in the ontology.
 * 
 * This interface is implemented by the {@link DefaultRoleEntity} to provide the implementations of the methods specific to change states of a role instance in the ontology.
 * 
 * @author Saad Wardi
 * @version $Revision: 4510 $
 */
@Local
public interface RoleEntity
{
    /**
     * Gets a role.
     * 
     * @param id not null role ID.
     * @return not null {@link Role}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Role getRole(String id) throws OntologyErrorException;

    /**
     * Gets the defined roles for an activity.
     * 
     * @param activityID not null activity ID.
     * @return not null {@link RoleArray}, if no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RoleArray getActivityRoles(String activityID) throws OntologyErrorException;

}
