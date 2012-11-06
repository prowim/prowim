/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-28 10:00:57 +0100 (Mo, 28 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/ContentService.java $
 * $LastChangedRevision: 5048 $
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
package org.prowim.dms.alfresco;

import javax.ejb.Local;

import org.alfresco.webservice.authoring.VersionResult;
import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.documents.DocumentRemote;



/**
 * The service which provides access to the content of the alfresco DMS.
 * 
 * @author Thomas Wiesner
 * @version $Revision: 5048 $
 * @since 2.0.0a8
 */
@Local
public interface ContentService extends ContentManager
{
    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentManager#writeContent(byte[], java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    boolean writeContent(byte[] content, String name, String format, String frameID, String username) throws OntologyErrorException, DMSException;

    /**
     * Creates a new version with the given name Description.
     * 
     * @param name name of the document, may not be null
     * @param username the user who creates the version
     * @param uuid the UUID of the document in alfresco
     * @return {@link VersionResult}, can be null, if an error occurs
     * @throws OntologyErrorException if an error in the ontology occurs
     * @throws DMSException if an error in the DMS occurs
     */
    VersionResult createNewVersion(String name, String username, String uuid) throws OntologyErrorException, DMSException;

    /**
     * {@link ContentManager#updateContent(byte[], String, String, String, String, boolean)}
     * 
     * @param content the content of a document in DMS, may not be null
     * @param name the name of the document, may not be null
     * @param format the format used, may not be null, format is same as returned by {@link Document#getContentType()}
     * @param frameID the ID of an ontology frame. In prowim-system a DMS is needed to store documents, that are defined for KnowledgeLink objects.<br>
     *        The frameID is also a KnowledgeLink object ID. Other frame, which store documents in dms could use this method to store and updates documents in DMS<br>
     *        and IDs in the ontology. May not be null
     * @param username the name of the user which updates the document
     * @param createNewVersion if <code>true</code>, a new version will be created, otherwise no version will be created
     * @return {@link VersionResult} if a new version has been created, otherwise null
     * @throws OntologyErrorException if an error in the ontology backend occurs
     * @throws DMSException if an error in the DMS backend occurs
     * @see org.prowim.dms.alfresco.ContentManager#updateContent(byte[], java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    VersionResult updateContent(byte[] content, String name, String format, String frameID, String username, boolean createNewVersion)
                                                                                                                                      throws OntologyErrorException,
                                                                                                                                      DMSException;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentManager#deleteDocument(java.lang.String)
     */
    void deleteDocument(String frameID) throws OntologyErrorException, DMSException;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentManager#deleteContent(java.lang.String)
     */
    void deleteContent(String uuid) throws DMSException;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentManager#downloadDocument(java.lang.String)
     */
    Document downloadDocument(String frameID) throws OntologyErrorException, DMSException;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentManager#downloadDocumentInVersion(java.lang.String, java.lang.String)
     */
    Document downloadDocumentInVersion(String id, String versionLabel) throws OntologyErrorException, DMSException;

    /**
     * Download a {@link Document} from the DMS with a given uuid.
     * 
     * @param id the ID of the document in the DMS.<br>
     *        At the time the below frames are supported : {@link KnowledgeLink}, {@link Parameter} from <code>InformationType</code><br>
     *        Document.
     * @param versionLabel the not null version label. Use {@link DMSConstants#INITIAL_VERSION_LABEL} for first version. Null is not allowed.
     * @return not null {@link Document}.
     * @throws DMSException if an error occurs in DMS back end
     */
    Document downloadDocumentInVersionFromID(String id, String versionLabel) throws DMSException;

    /**
     * Gets the version history of a document stored in the alfresco DMS.
     * 
     * @param uuid the ID of a the document.
     * @return {@link org.prowim.datamodel.dms.VersionHistory}, never null, if no documents were found, an empty version history is returned
     * @throws DMSException if an error occurs in DMS back end
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    org.prowim.datamodel.dms.VersionHistory getDocumentVersionHistory(String uuid) throws DMSException, OntologyErrorException;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#getVersionHistory(java.lang.String)
     */
    org.prowim.datamodel.dms.VersionHistory getVersionHistory(String frameID) throws OntologyErrorException, DMSException;

    /**
     * {@link DocumentRemote#revertVersion(String, String)}
     * 
     * @param documentID ID of the document node in the DMS, may not be null
     * @param versionLabel the version label, may not be null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void revertVersion(String documentID, String versionLabel) throws OntologyErrorException;

    /**
     * Gets the documentname.
     * 
     * @param frameID not null frameID.
     * @return not null documentname.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getDocumentName(String frameID) throws OntologyErrorException;

    /**
     * Gets the properties of all stored documents in the DMS.
     * 
     * @return not null {@link DocumentContentPropertiesArray}. If no item exists an empty array is returned.
     * @throws DMSException if an error occurs in DMS back end occurs
     */
    DocumentContentPropertiesArray getAllDocuments() throws DMSException;

    /**
     * 
     * Searches the given key word (expression) in the DMS.
     * 
     * @param keyWord the key word to search in all items of DMS
     * @return not null {@link DocumentContentPropertiesArray}. If no items exist an empty list is returned.
     * @throws DMSException if an error occurs in DMS back end occurs
     */
    DocumentContentPropertiesArray searchFullText(String keyWord) throws DMSException;

    /**
     * Returns the {@link org.prowim.datamodel.dms.VersionHistory} of the given process (in alfresco versions).
     * 
     * @param processID the process ID (frame!) to return the version history for, may not be null
     * @return {@link VersionHistory} for the process id, never null
     * @throws OntologyErrorException if an error occurs in the ontology backend
     * @throws DMSException if an error occurs in the dms backend
     */
    org.prowim.datamodel.dms.VersionHistory getDocumentVersionHistoryFromProcessID(String processID) throws OntologyErrorException, DMSException;

    /**
     * 
     * Update the given document with given uuid. This used bz migration the DMS to set the content type of a {@link Document}
     * 
     * @param document document
     * @param uuid id of document in Alfresco
     * @throws DMSException if an error occurs in the dms backend
     */
    void updateDMSDocument(Document document, String uuid) throws DMSException;

    /**
     * 
     * Update the given document with given and set the version
     * 
     * @param document document
     * @param userName id of document in Alfresco
     * @throws DMSException if an error occurs in the dms backend
     */
    void updateDocument(Document document, String userName) throws DMSException;

    /**
     * Upload a {@link Document} in DMS.
     * 
     * @param document {@link Document} to upload
     * @param userName TODO
     * @return <code>true</code> if upload is successful, else <code>false</code>
     * @throws DMSException if an error occurs in the dms backend
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    boolean uploadDocument(Document document, String userName) throws DMSException, OntologyErrorException;

}