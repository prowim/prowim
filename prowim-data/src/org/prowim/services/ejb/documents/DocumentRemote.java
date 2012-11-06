/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-21 14:29:53 +0200 (Wed, 21 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/documents/DocumentRemote.java $
 * $LastChangedRevision: 4377 $
 *------------------------------------------------------------------------------
 * (c) 13.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
 *This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.services.ejb.documents;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.datamodel.factory.ProwimDataObjectFactory;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Collects all methods to store delete and update documents and content and retrieve them from the Alfresco DMS.
 * 
 * @author Saad Wardi
 * @version $Revision: 4377 $
 */

@WebService(name = ServiceConstants.PROWIM_DOCUMENTS_REMOTE, targetNamespace = ServiceConstants.PROWIM_DOCUMENTS_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({ ProwimDataObjectFactory.class })
public interface DocumentRemote
{

    /**
     * Uploads a binary file to the document management system. This creates a new document in DMS.
     * 
     * @param document not null {@link Document}.
     * @param frameID ID of the knowledge link.
     * @param userName Name of actually logged user. Not null.
     * @return <code>true</code> if writing the document in DMS was successful else <code>false</code>.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    boolean uploadDocumentForKnowledgeLink(Document document, String frameID, String userName) throws OntologyErrorException, DMSException;

    /**
     * Uploads a binary file to the document management system. This creates a new document in DMS.
     * 
     * @param document not null {@link Document}.
     * @param userName Name of actually logged user. Not null.
     * @return <code>true</code> if writing the document in DMS was successful else <code>false</code>.
     * @throws DMSException if an error in DMS back end occurs
     * @throws OntologyErrorException f error occurs in ontology back ends
     */
    @WebMethod
    boolean uploadDocument(Document document, String userName) throws DMSException, OntologyErrorException;

    /**
     * Deletes a content from alfresco DMS.
     * 
     * @param frameID the ID of the {@link KnowledgeLink}, that you wannt to delete his document.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     * 
     */
    @WebMethod
    void deleteDocument(String frameID) throws OntologyErrorException, DMSException;

    /**
     * Updates a document in the document management system.
     * 
     * @param document not null {@link Document}.
     * @param frameID ID of the knowledgelink.
     * @param userName Name of actually logged user. Not null.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    void updateDocumentForKnowledgeLink(Document document, String frameID, String userName) throws OntologyErrorException, DMSException;

    /**
     * Downloads a {@link Document} from the DMS.
     * 
     * @param frameID the ID of the frame in the ontology, that store his content as document in DMS.<br>
     *        At the time the below frames are supported : {@link KnowledgeLink}, {@link Parameter} from <code>InformationType</code><br>
     *        Dokument.
     * @return not null {@link Document}
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     * 
     */
    @WebMethod
    @WebResult(partName = "return")
    Document downloadDocument(String frameID) throws OntologyErrorException, DMSException;

    /**
     * Downloads a {@link Document} from the DMS.
     * 
     * @param frameID the ID of the frame in the ontology, that store his content as document in DMS.<br>
     *        At the time the below frames are supported : {@link KnowledgeLink}, {@link Parameter} from <code>InformationType</code><br>
     *        Dokument.
     * @param versionLabel the version label to be downloaded. Use {@link DMSConstants#INITIAL_VERSION_LABEL} for first version. Null is not allowed.
     * @return not null {@link Document} or an IllegalStateException occurs if no content or no document for the given frameID, or if the version label does not exist.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     * 
     */
    @WebMethod
    @WebResult(partName = "return")
    Document downloadDocumentInVersion(String frameID, String versionLabel) throws OntologyErrorException, DMSException;

    /**
     * Gets the properties of all stored documents in the DMS.
     * 
     * @return not null {@link DocumentContentPropertiesArray}. If no item exists an empty array is returned.
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    DocumentContentPropertiesArray getAllDocuments() throws DMSException;

    /**
     * Gets the documentname.
     * 
     * @param frameID ID of the frame that contains type "ist_vom_informationtype".
     * @return not null documentname.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    String getDocumentName(String frameID) throws OntologyErrorException, DMSException;

    /**
     * Gets the document ID.
     * 
     * @param knowledgeLinkID ID of the knowledgeLink".
     * @return not null document ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getLinkDocumentID(String knowledgeLinkID) throws OntologyErrorException;

    /**
     * * Binds a document to a frame. If this method success, the frame with the id = frameID has a reference<br>
     * to a document in DMS. Typically frames are {@link KnowledgeLink}.<br>
     * <br>
     * Example usage with {@link KnowledgeLink}:<br>
     * To bind a {@link KnowledgeLink} to a {@link Document} follow the approach:<br>
     * 1. Call the service out all documents stored in the "ProWim space".<br>
     * 2. Select a document wich must have been bound to a <code>Knowledgelink</code><br>
     * 3. Call the service <code>{@link DocumentRemote#bindDocument(String, String, String, String)}</code>. The frameID is the id of the knowledgeLink to be bounded and the documentID is the id of the {@link DocumentContentProperties} returned from the service call (1).<br>
     * <br>
     * by now you can download , update and delete this document using his reference bounded with the <code>KnowledgeLink</code>
     * 
     * @param documentID the ID of the document that must be bounded to a frame. not null.
     * @param documentName the name of the document. not null.
     * @param frameID the ID of the frame. not null.
     * @param versionLabel the version label. null is possible when the last version is bound.
     * @throws OntologyErrorException if error occurs in ontology back end
     * 
     */
    @WebMethod
    @WebResult(partName = "return")
    void bindDocument(String documentID, String documentName, String frameID, String versionLabel) throws OntologyErrorException;

    /**
     * Gets the history of existing versions to a document.
     * 
     * @param frameID the id of a frame that stores a document in alfresco DMS. not null.
     * @return not null {@link VersionHistory} object.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error occurs in the DMS backend
     */
    @WebMethod
    @WebResult(partName = "return")
    VersionHistory getVersionHistory(String frameID) throws OntologyErrorException, DMSException;

    /**
     * Gets the history of existing versions to a document.
     * 
     * @param id the id of a node that stores a document in alfresco DMS.
     * @return not null {@link VersionHistory} object.
     * @throws DMSException if an error in the DMS backend occurs
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    @WebMethod
    @WebResult(partName = "return")
    VersionHistory getVersionHistoryFromNodeID(String id) throws DMSException, OntologyErrorException;

    /**
     * Gets the history of existing versions to a process.
     * 
     * @param processID the id of the process, may not be null
     * @return not null {@link VersionHistory} object.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    VersionHistory getVersionHistoryFromProcessID(String processID) throws OntologyErrorException, DMSException;

    /**
     * Downloads a {@link Document} from the DMS.
     * 
     * @param id the ID of the document in DMS.<br>
     * @param versionLabel the version label.
     * @return not null {@link Document}
     * @throws DMSException if an error in DMS back end occurs
     * 
     */
    @WebMethod
    @WebResult(partName = "return")
    Document downloadDocumentInVersionFromID(String id, String versionLabel) throws DMSException;

    /**
     * Gets a the bounded version of a document to the link withid = linkID from the DMS.
     * 
     * @param linkID the ID of the KnowledgeLink.<br>
     * @return version label. null is possible if the last version is added.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getStoredDocumentVersionLabel(String linkID) throws OntologyErrorException;

    /**
     * 
     * Searches the given key word (expression) in the DMS.
     * 
     * @param keyWord the key word to search in all items of DMS
     * @return not null {@link DocumentContentPropertiesArray}. If no items exist an empty list is returned.
     * @throws DMSException if an error occurs in DMS back end occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    DocumentContentPropertiesArray searchFullText(String keyWord) throws DMSException;

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
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void revertVersion(String documentID, String versionLabel) throws OntologyErrorException;

    /**
     * Returns the Alfresco DMS ID for the XML file storing the editor model of the given process information ID.
     * 
     * @param processInformationID the process information ID, may not be null
     * @return the document UUID, can be null
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getXMLDocumentIDForProcess(String processInformationID) throws OntologyErrorException;

    /**
     * 
     * Search for a folder or content in DMS. In case of searching for a content the content name is the complete name of file like example.pdf.
     * 
     * @param contentName Name/title of a content. Not null.
     * @return uuid of element, if it exists, else null.
     * @throws DMSException if an error occurs in DMS back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String findFolderOrContent(String contentName) throws DMSException;

    /**
     * Updates a document in the document management system. It will not changes anything in DB (ontology)
     * 
     * @param document not null {@link Document}.
     * @param uuid ID of the {@link Document} in document mamagent system.
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    void updateDMSDocument(Document document, String uuid) throws DMSException;

    /**
     * Updates a document in the document management system. It will not changes anything in DB (ontology)
     * 
     * @param document not null {@link Document}.
     * @param userName Name of actually logged user. Not null.
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    void updateDocument(Document document, String userName) throws DMSException;

}
