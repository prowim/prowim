/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.prowim.datamodel.dms.DocumentIdentification;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.documents.DocumentRemote;



/**
 * Stores, reads and updates a document id in the ontology.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a8
 */
@Local
public interface DocumentManagementHelper
{
    /**
     * Sets the data to a document in the ontology.
     * 
     * @param uuid the document management system content ID.
     * @param path the document management system content path.
     * @param knowledgeLinkID the ID of the knowledgelink, that links a document in DMS.
     * @return SUCCEDDED if every thing is all right or FAILED on error.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String setDocumentIdentification(String uuid, String path, String knowledgeLinkID) throws OntologyErrorException;

    /**
     * Gets the uuid and the path to the document of the given {@link KnowledgeLink} or {@link ProcessElement} ID.
     * 
     * @param frameID the ID of the {@link KnowledgeLink} or {@link ProcessElement}, may not be null
     * @return {@link DocumentIdentification}, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    DocumentIdentification getDocumentIdentification(String frameID) throws OntologyErrorException;

    /**
     * Clears the document identification from the slot Hyperlink"".
     * 
     * @param frameID the ID of the frame to be cleared at slot "Hyperlink".
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void clearDocumentIdentification(String frameID) throws OntologyErrorException;

    /**
     * Clears the document identification from the slot Inhalt_String"".
     * 
     * @param frameID the ID of the frame to be cleared at slot "Inhalt_String".
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void clearDocumentIdentificationForPI(String frameID) throws OntologyErrorException;

    /**
     * {@link DocumentRemote#bindDocument(String, String, String, String)}.
     * 
     * @param documentID ID of the document. not null.
     * @param documentName the name of the document. not null.
     * @param frameID the ID of the frame. not null.
     * @param versionLabel the version label. null is possible when the last version is bound.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void bindDocument(String documentID, String documentName, String frameID, String versionLabel) throws OntologyErrorException;

    /**
     * Gets the value of the slot Document_Version.
     * 
     * @param linkID not null link ID.
     * @return the document version label. null is possible.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getStoredVersionLabel(String linkID) throws OntologyErrorException;

    /**
     * Returns the Alfresco DMS ID for the XML file storing the editor model of the given process information ID.
     * 
     * @param processInformationID the process information ID, may not be null
     * @return the document UUID, can be null
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     * @see DocumentRemote#getXMLDocumentIDForProcess(String)
     */
    String getXMLDocumentIDForProcess(String processInformationID) throws OntologyErrorException;

    /**
     * <p>
     * Reverts the document with the given ID to the given version.
     * </p>
     * 
     * <p>
     * <b>WARNING: This method has to be validated! Do not use until clarified if the method does delete / overwrite versions when reverting!</b>
     * </p>
     * 
     * @param documentID the ID of the document node in the DMS, may not be null
     * @param versionLabel the version to restore, may not be null
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     * @see DocumentRemote#revertVersion(String, String)
     */
    void revertVersion(String documentID, String versionLabel) throws OntologyErrorException;

}