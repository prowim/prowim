/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/UpdateEngine.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 22.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.Result;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Methods that calls the rules for the ontology update.
 * 
 * @author Saad Wardi
 * @version $Revision: 4510 $
 * @since !!VERSION!!
 */
@Local
public interface UpdateEngine
{
    /**
     * Gets the frames to be updated.
     * 
     * @param versionID the not null version ID.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getUpdateFrames(String versionID) throws OntologyErrorException;

    /**
     * Gets all versions.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getAllVersions() throws OntologyErrorException;

    /**
     * Gets the update frame properties.
     * 
     * @param frameID not null frame ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getUpdateFrameProperties(String frameID) throws OntologyErrorException;

    /**
     * Gets the frame type class.
     * 
     * @param frameID not null frame ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet isFrameClass(String frameID) throws OntologyErrorException;

    /**
     * Gets the frame type slot.
     * 
     * @param frameID not null frame ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet isFrameSlot(String frameID) throws OntologyErrorException;

    /**
     * Gets the frame type instance.
     * 
     * @param frameID not null frame ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet isFrameInstance(String frameID) throws OntologyErrorException;

    /**
     * Executes an update query.
     * 
     * @param query not null query.
     * @return {@link Result}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Result executeUpdateQuery(String query) throws OntologyErrorException;

    /**
     * Executes an update query.
     * 
     * @param query not null query.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet executeQuery(String query) throws OntologyErrorException;

    /**
     * Gets the version properties.
     * 
     * @param versionID not null version ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getVersionProperties(String versionID) throws OntologyErrorException;

    /**
     * Cleans the ontology.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet clean() throws OntologyErrorException;

    /**
     * Gets the delete items.
     * 
     * @param versionID not null version ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getDeleteFrames(String versionID) throws OntologyErrorException;

    /**
     * Adds a new webservice.
     * 
     * @param name the webservice name
     * @param description the webservice description
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet addWebservice(String name, String description) throws OntologyErrorException;

    /**
     * Sets the version as invalid.
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet setVersionInvalid() throws OntologyErrorException;

    /**
     * Sets the version as valid.
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet setVersionValid() throws OntologyErrorException;

    /**
     * Gets true if the version is valid otherwise false.
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet isVersionValid() throws OntologyErrorException;

}
