/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-05-12 14:23:30 +0200 (Do, 12 Mai 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/documents/DocumentBean.java $
 * $LastChangedRevision: 5085 $
 *------------------------------------------------------------------------------
 * (c) 13.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.ejb.documents;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.dms.alfresco.ContentService;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.DocumentManagementHelper;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.documents.DocumentRemote;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the {@link DocumentRemote}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5085 $
 */

@Stateless(name = ServiceConstants.DOCUMENTS_BEAN)
@WebService(name = ServiceConstants.PROWIM_DOCUMENTS_SERVICE_NAME, serviceName = ServiceConstants.PROWIM_DOCUMENTS_SERVICE_NAME, endpointInterface = ServiceConstants.PROWIM_DOCUMENTS_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT)
public class DocumentBean implements DocumentRemote
{

    private static final Logger      LOG = Logger.getLogger(DocumentBean.class);

    /** the content service that operates on the alfresco DMS. */
    @IgnoreDependency
    @EJB
    private ContentService           contentService;

    @IgnoreDependency
    @EJB
    private DocumentManagementHelper documentManagement;

    @IgnoreDependency
    @EJB
    private CommonHelper             commonService;

    @Resource
    private SessionContext           context;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#uploadDocumentForKnowledgeLink(org.prowim.datamodel.dms.Document, java.lang.String, String)
     */
    @Override
    public boolean uploadDocumentForKnowledgeLink(Document document, String knowledgeLinkID, String userName) throws OntologyErrorException,
                                                                                                             DMSException
    {
        return contentService.writeContent(document.getContent(), document.getName(), document.getContentType(), knowledgeLinkID, userName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#updateDocumentForKnowledgeLink(org.prowim.datamodel.dms.Document, java.lang.String, String)
     */
    @Override
    public void updateDocumentForKnowledgeLink(Document document, String frameID, String userName) throws OntologyErrorException, DMSException
    {
        contentService.updateContent(document.getContent(), document.getName(), document.getContentType(), frameID, userName, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws OntologyErrorException
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#uploadDocument(org.prowim.datamodel.dms.Document, String)
     */
    @Override
    public boolean uploadDocument(Document document, String userName) throws DMSException, OntologyErrorException
    {
        return contentService.uploadDocument(document, userName);
        // context.getCallerPrincipal().getName()
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#deleteDocument(java.lang.String)
     */
    @Override
    public void deleteDocument(String frameID) throws OntologyErrorException, DMSException
    {
        contentService.deleteDocument(frameID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#downloadDocument(java.lang.String)
     */
    @Override
    public Document downloadDocument(String frameID) throws OntologyErrorException, DMSException
    {
        // 1. check if the document is knowledgelink
        if (commonService.getDirectClassOfInstance(frameID).equals(Relation.Classes.KNOWLEDGE_LINK))
        {
            String versionLabel = documentManagement.getStoredVersionLabel(frameID);
            // 2. if yes : check if there is a version label stored
            if (versionLabel != null)
            {
                // 3. if yes : call contentService.downloadDocumentInVersion(frameID, versionLabel)
                return contentService.downloadDocumentInVersion(frameID, versionLabel);
            }
        }

        return contentService.downloadDocument(frameID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getAllDocuments()
     */
    public DocumentContentPropertiesArray getAllDocuments() throws DMSException
    {
        return contentService.getAllDocuments();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#bindDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void bindDocument(String documentID, String documentName, String frameID, String versionLabel) throws OntologyErrorException
    {
        documentManagement.bindDocument(documentID, documentName, frameID, versionLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getVersionHistory(java.lang.String)
     */
    @Override
    public VersionHistory getVersionHistory(String frameID) throws OntologyErrorException, DMSException
    {
        return contentService.getVersionHistory(frameID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#downloadDocumentInVersion(java.lang.String, java.lang.String)
     */
    @Override
    public Document downloadDocumentInVersion(String frameID, String versionLabel) throws OntologyErrorException, DMSException
    {
        return contentService.downloadDocumentInVersion(frameID, versionLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getDocumentName(java.lang.String)
     */
    @Override
    public String getDocumentName(String frameID) throws OntologyErrorException
    {
        return contentService.getDocumentName(frameID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#downloadDocumentInVersionFromID(java.lang.String, java.lang.String)
     */
    @Override
    public Document downloadDocumentInVersionFromID(String id, String versionLabel) throws DMSException
    {
        return contentService.downloadDocumentInVersionFromID(id, versionLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getVersionHistoryFromNodeID(java.lang.String)
     */
    @Override
    public VersionHistory getVersionHistoryFromNodeID(String id) throws DMSException, OntologyErrorException
    {
        return contentService.getDocumentVersionHistory(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getLinkDocumentID(java.lang.String)
     */
    @Override
    public String getLinkDocumentID(String knowledgeLinkID) throws OntologyErrorException
    {
        return documentManagement.getDocumentIdentification(knowledgeLinkID).getUuid();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getStoredDocumentVersionLabel(java.lang.String)
     */
    @Override
    public String getStoredDocumentVersionLabel(String linkID) throws OntologyErrorException
    {
        return documentManagement.getStoredVersionLabel(linkID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#searchFullText(java.lang.String)
     */
    @Override
    public DocumentContentPropertiesArray searchFullText(String keyWord) throws DMSException
    {
        return contentService.searchFullText(keyWord);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#revertVersion(java.lang.String, java.lang.String)
     */
    @Override
    public void revertVersion(String documentID, String versionLabel) throws OntologyErrorException
    {
        Validate.notNull(documentID);
        Validate.notNull(versionLabel);

        documentManagement.revertVersion(documentID, versionLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getVersionHistoryFromProcessID(java.lang.String)
     */
    @Override
    public VersionHistory getVersionHistoryFromProcessID(String processID) throws OntologyErrorException, DMSException
    {
        return contentService.getDocumentVersionHistoryFromProcessID(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#getXMLDocumentIDForProcess(java.lang.String)
     */
    public String getXMLDocumentIDForProcess(String processInformationID) throws OntologyErrorException
    {
        return documentManagement.getDocumentIdentification(processInformationID).getUuid();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#findFolderOrContent(java.lang.String)
     */
    @Override
    public String findFolderOrContent(String contentName) throws DMSException
    {
        return contentService.findFolderOrContent(contentName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#updateDMSDocument(org.prowim.datamodel.dms.Document, java.lang.String)
     */
    @Override
    public void updateDMSDocument(Document document, String uuid) throws DMSException
    {
        contentService.updateDMSDocument(document, uuid);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.documents.DocumentRemote#updateDocument(org.prowim.datamodel.dms.Document, String)
     */
    @Override
    public void updateDocument(Document document, String userName) throws DMSException
    {
        contentService.updateDocument(document, userName);

    }

}
