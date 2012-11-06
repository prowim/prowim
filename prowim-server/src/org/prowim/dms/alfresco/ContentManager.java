/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:50 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/ContentManager.java $
 * $LastChangedRevision: 5032 $
 *------------------------------------------------------------------------------
 * (c) 11.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.alfresco.webservice.authoring.VersionResult;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Manages contents or (documents) in alfresco DMS.<br>
 * calls the alfresco server APIs to create, update and delete documents in DMS.
 * 
 * @author Saad Wardi
 * @version $Revision: 5032 $
 */
public interface ContentManager extends Content
{
    /**
     * Creates a new content.
     * 
     * @param content the content of a document in DMS.
     * @param name the name of the document.
     * @param format the format used.
     * 
     * @param frameID the ID of an ontology frame. In prowim-system a DMS is needed to store documents, <br>
     *        that are defined for KnowledgeLink objects. The frameID is also a KnowledgeLink object ID.<br>
     *        Other frame, wich store documents in dms could use this method to store and updates documents in DMS<br>
     *        and IDs in the ontology.
     * @param username the not null username that logged in.
     * @return <code>true</code> if writing the document in DMS was successful else <code>false</code>.
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error occurs in DMS back end
     */
    boolean writeContent(byte[] content, String name, String format, String frameID, String username) throws OntologyErrorException, DMSException;

    /**
     * Updates a content in the DMS.
     * 
     * @param content the content of a document in DMS, may not be null
     * @param name the name of the document, may not be null
     * @param format the format used, may not be null, format is same as returned by {@link Document#getContentType()}
     * @param frameID the ID of an ontology frame. In prowim-system a DMS is needed to store documents, that are defined for KnowledgeLink objects.<br>
     *        The frameID is also a KnowledgeLink object ID. Other frame, which store documents in dms could use this method to store and updates documents in DMS<br>
     *        and IDs in the ontology. May not be null
     * @param username the name of the user which updates the document
     * @param createNewVersion if <code>true</code>, a new version will be created, otherwise no version will be create
     * @return {@link VersionResult} if a new version has been created, otherwise null
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     * @see ContentService#updateContent(byte[], String, String, String, String, boolean)
     */
    VersionResult updateContent(byte[] content, String name, String format, String frameID, String username, boolean createNewVersion)
                                                                                                                                      throws OntologyErrorException,
                                                                                                                                      DMSException;

    /**
     * Deletes a content from alfresco DMS.
     * 
     * @param frameID the ID of the ontology frame, his document in DMS have to be deleted.
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error occurs in DMS back end
     */
    void deleteDocument(String frameID) throws OntologyErrorException, DMSException;

    /**
     * Deletes a content from alfresco DMS.
     * 
     * @param uuid the content ID.
     * @throws DMSException if an error occurs in DMS back end
     */
    void deleteContent(String uuid) throws DMSException;

    /**
     * Downloads a {@link Document} from the DMS.
     * 
     * @param frameID the ID of the frame in the ontology, that store his content as document in DMS.<br>
     *        At the time the below frames are supported : {@link KnowledgeLink}, {@link Parameter} from <code>InformationType</code><br>
     *        Dokument.
     * @return not null {@link Document}
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error occurs in DMS back end
     */
    Document downloadDocument(String frameID) throws OntologyErrorException, DMSException;

    /**
     * Downloads a {@link Document} from the DMS.
     * 
     * @param frameID the ID of the frame in the ontology, that store his content as document in DMS.<br>
     *        At the time the below frames are supported : {@link KnowledgeLink}, {@link Parameter} from <code>InformationType</code><br>
     *        Document.
     * @param versionLabel the not null version label. Use {@link DMSConstants#INITIAL_VERSION_LABEL} for first version. Null is not allowed.
     * @return not null {@link Document}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error occurs in DMS back end
     */
    Document downloadDocumentInVersion(String frameID, String versionLabel) throws OntologyErrorException, DMSException;

    /**
     * 
     * Search for a folder or content in DMS. In case of searching for a content the content name is the complete name of file like example.pdf.
     * 
     * @param contentName Name/title of a content. Not null.
     * @return uuid of element, if it exists, else null.
     * @throws DMSException if an error occurs in DMS back end
     */
    String findFolderOrContent(String contentName) throws DMSException;
}
