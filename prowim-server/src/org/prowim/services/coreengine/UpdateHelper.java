/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-16 16:15:00 +0200 (Mo, 16 Aug 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/UpdateHelper.java $
 * $LastChangedRevision: 4645 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine;

import java.util.List;

import javax.ejb.Local;

import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OntologyVersionArray;
import org.prowim.datamodel.collections.UpdateFrameArray;
import org.prowim.datamodel.prowim.UpdateWork;
import org.prowim.datamodel.prowim.UpdatesLog;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Methods to update the prowimcore ontology.
 * 
 * @author Saad Wardi
 * @version $Revision: 4645 $
 * @since !!VERSION!!
 */
@Local
public interface UpdateHelper
{
    /**
     * Gets all version of the ontology.
     * 
     * @return not null {@link OntologyVersionArray}. If no item exists , an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    OntologyVersionArray getVersions() throws OntologyErrorException;

    /**
     * Gets the frames to be updated.
     * 
     * @param versionID not null version ID.
     * @return not null {@link List} of frames IDs, that has to be updated. If no frames exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    UpdateFrameArray getUpdateFrames(String versionID) throws OntologyErrorException;

    /**
     * Updates the ontology to the given version.
     * 
     * @param versionID not null version ID.
     * @return not null {@link ObjectArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    UpdateWork getUpdateWork(String versionID) throws OntologyErrorException;

    /**
     * Executes updates of the ontology from the inout UpdateWork that contains rules and logs objects.
     * 
     * @param updateWork not null {@link UpdateWork}.
     * @return not null {@link UpdatesLog}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray executeUpdate(UpdateWork updateWork) throws OntologyErrorException;

    /**
     * Executes updates of the ontology.
     * 
     * @param updateScript not null update script. Such script is formatted as follows in the gramar:<br>
     *        (1)<START/> ::=<Rules/>+ <br>
     *        (2)<Rules/> ::=<RuleBody/>|<END/> <br>
     *        (3)<RuleBody/>::="((*))" <br>
     *        (4)<END/> ::="<EOR/>"
     * 
     * @return true if the update succeedded.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean executeUpdateFromScript(String updateScript) throws OntologyErrorException;

    /**
     * Cleans the ontology.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void clean() throws OntologyErrorException;

    /**
     * init the knowledgebase.
     * 
     */
    void initKnowledgeBase();

    /**
     * Sets the version as invalid.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     * 
     */
    void setVersionInvalid() throws OntologyErrorException;

    /**
     * Sets the version as invalid.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     * 
     */
    void setVersionValid() throws OntologyErrorException;

    /**
     * Sets the version as invalid.
     * 
     * @return true if the version is valid.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isVersionValid() throws OntologyErrorException;

    /**
     * adds a webservice instance in the ontology.
     * 
     * @param name the not null webservice name.
     * @param description the not null webservice description.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void addWebservice(String name, String description) throws OntologyErrorException;

}
