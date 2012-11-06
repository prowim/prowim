/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-13 11:47:38 +0200 (Fr, 13 Aug 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/DocumentEngine.java $
 * $LastChangedRevision: 4605 $
 *------------------------------------------------------------------------------
 * (c) 14.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.documents.DocumentRemote;



/**
 * Title and description.
 * 
 * @author Saad Wardi
 * @version $Revision: 4605 $
 */
@Local
public interface DocumentEngine
{

    /**
     * Sets the data to a document in the ontology.
     * 
     * @param uuid the document management system content ID.
     * @param path the document management system content path.
     * @param knowledgeLinkID the ID of the knowledgelink, that links a document in DMS.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException is something goes wrong
     */
    RecordSet setDocumentIdentification(String uuid, String path, String knowledgeLinkID) throws OntologyErrorException;

    /**
     * Gets the uuid and the path to the document defined to the {@link KnowledgeLink}.
     * 
     * @param knowledgeLinkID the KnowledgeLink ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException is something goes wrong
     */
    RecordSet getDocumentIdentification(String knowledgeLinkID) throws OntologyErrorException;

    /**
     * Clears the stored uuid of a document stored in the ontology.<br>
     * This is done because, wehn the document have been deleted.
     * 
     * @param frameID the ID of the frame, his document have to be deleted.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException is something goes wrong
     */
    RecordSet clearDocumentIdentification(String frameID) throws OntologyErrorException;

    /**
     * Sets the data to a document in the ontology.
     * 
     * @param uuid the document management system content ID.
     * @param path the document management system content path.
     * @param frameID the ID of the knowledgelink, that links a document in DMS.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException is something goes wrong
     */
    RecordSet setDocumentIdentificationForProcessInformation(String uuid, String path, String frameID) throws OntologyErrorException;

    /**
     * Gets the uuid and the path to the document defined to the {@link Parameter}.
     * 
     * @param frameID the frame ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException is something goes wrong
     */
    RecordSet getDocumentIdentificationForProcessInformation(String frameID) throws OntologyErrorException;

    /**
     * Clears the stored uuid of a document stored in the ontology.<br>
     * This is done because, when the document has been deleted.
     * 
     * @param frameID the ID of the frame, his document have to be deleted.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException is something goes wrong
     */
    RecordSet clearDocumentIdentificationForProcessInformation(String frameID) throws OntologyErrorException;

    /**
     * Checks if tha frame is "vom_information_type".
     * 
     * @param frameID the ID of the frame.
     * @return true if the frame is "vom_information_type", false otherwise.
     * @throws OntologyErrorException is something goes wrong
     */
    boolean isFrameFromInformationType(String frameID) throws OntologyErrorException;

    /**
     * Checks if tha frame is "vom_information_type" document.
     * 
     * @param frameID the ID of the frame.
     * @return true if the frame is "vom_information_type", false otherwise.
     * @throws OntologyErrorException is something goes wrong
     */
    boolean isInfoTypeDocument(String frameID) throws OntologyErrorException;

    /**
     * {@link DocumentRemote#bindDocument(String, String, String, String)}.
     * 
     * @param documentID ID of the document.
     * @param documentName the name of the document. not null.
     * @param frameID the ID of the frame.
     * @param versionLabel the version label. null is possible when the last version is bound.
     * @throws OntologyErrorException is something goes wrong
     */
    void bindDocument(String documentID, String documentName, String frameID, String versionLabel) throws OntologyErrorException;
}
