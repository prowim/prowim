/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-07-01 10:21:05 +0200 (Fr, 01 Jul 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/ContentServiceBean.java $
 * $LastChangedRevision: 5095 $
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
package org.prowim.dms.alfresco;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.alfresco.webservice.authoring.AuthoringFault;
import org.alfresco.webservice.authoring.AuthoringServiceSoapBindingStub;
import org.alfresco.webservice.authoring.VersionResult;
import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentFault;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddAspect;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.CMLUpdate;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.types.VersionHistory;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.ContentUtils;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.DMSFault;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.DocumentIdentification;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.resources.ResourcesLocator;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.DocumentManagementHelper;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.utils.DMSUtility;
import org.prowim.utils.DateUtility;

import de.ebcot.tools.logging.Logger;


/**
 * The ContentService calls the APIs of the alfresco server to create, update and delete documents in DMS.
 * 
 * @author Saad Wardi
 * @version $Revision: 5095 $
 */
@Stateless
public class ContentServiceBean extends ContentBase implements ContentService
{
    private static final Logger      LOG = Logger.getLogger(ContentServiceBean.class);

    @EJB
    private ProcessHelper            processHelper;

    @EJB
    private OrganizationEntity       organizationEntity;

    @EJB
    private DocumentManagementHelper documentManagement;

    @EJB
    private CommonHelper             commonBean;

    /**
     * Creates a session to DMS.
     */
    public ContentServiceBean()
    {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#writeContent(byte[], java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public boolean writeContent(byte[] content, String name, String mimeType, String frameID, String username) throws OntologyErrorException,
                                                                                                              DMSException
    {
        Validate.notNull(content);
        Validate.notEmpty(name);
        Validate.notNull(mimeType);
        Validate.notNull(frameID);

        ContentServiceSoapBindingStub contentService = getContentService();
        ContentFormat contentFormat = new ContentFormat(mimeType, DMSConstants.ENCODING);
        DMSFault dmsFault;
        try
        {
            if (findFolderOrContent(name) == null)
            {
                Reference contentReference = this.createReference(name, organizationEntity.getUser(username).getID());

                Content contentRef = contentService.write(contentReference, Constants.PROP_CONTENT, content, contentFormat);
                makeVersionable(contentReference);
                documentManagement.setDocumentIdentification(contentRef.getNode().getUuid(), name, frameID);
                if (this.commonBean.getDirectClassOfInstance(frameID).equals(Relation.Classes.KNOWLEDGE_LINK))
                    documentManagement.bindDocument(contentRef.getNode().getUuid(), name, frameID, getLastVersionLabel(contentRef.getNode()));
                return true;
            }
            else
                return false;
        }
        catch (ContentFault e)
        {
            String message = "Could not create content: ";
            LOG.error(message, e);
            dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
        catch (RemoteException e)
        {
            String message = "Could not create connection: ";
            LOG.error(message, e);
            dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#createNewVersion(java.lang.String, java.lang.String, java.lang.String)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public VersionResult createNewVersion(String name, String username, String uuid) throws OntologyErrorException, DMSException
    {
        // get the user
        String userID = organizationEntity.getUser(username).getID();

        AuthoringServiceSoapBindingStub authoringService = WebServiceFactory.getAuthoringService();
        final Reference reference = new Reference(DMSStoreRegistry.STORE_REF, uuid, null);
        final Predicate predicate = new Predicate(new Reference[] { reference }, null, null);
        NamedValue[] comments = new NamedValue[] { Utils.createNamedValue("description", "User description"),
                Utils.createNamedValue("versionType", "MINOR"), Utils.createNamedValue(DMSConstants.Content.AUTHOR_PROP, userID) };
        VersionResult vr;
        try
        {
            vr = authoringService.createVersion(predicate, comments, true);
            String descriptionNew = "This is a sample description for " + name;
            Predicate pred = new Predicate(vr.getNodes(), null, null);

            NamedValue[] titledProps = new NamedValue[2];
            titledProps[0] = Utils.createNamedValue(Constants.PROP_NAME, name);
            titledProps[1] = Utils.createNamedValue(Constants.PROP_DESCRIPTION, descriptionNew);

            CMLUpdate update = new CMLUpdate(titledProps, pred, null);
            CML cml = new CML();
            cml.setUpdate(new CMLUpdate[] { update });

            WebServiceFactory.getRepositoryService().update(cml);

            return vr;
        }
        catch (AuthoringFault e)
        {
            LOG.error("Could not create new version in DMS for user " + username + " and UUID " + uuid, e);
        }
        catch (RemoteException e)
        {
            LOG.error("Could not create new version in DMS for user " + username + " and UUID " + uuid, e);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#updateContent(byte[], java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public VersionResult updateContent(byte[] content, String name, String mimeType, String frameID, String username, boolean createNewVersion)
                                                                                                                                               throws OntologyErrorException,
                                                                                                                                               DMSException
    {
        Validate.notNull(content);
        Validate.notNull(name);
        Validate.notNull(mimeType);
        Validate.notNull(frameID);
        ContentFormat contentFormat = new ContentFormat(mimeType, DMSConstants.ENCODING);
        try
        {
            DocumentIdentification documentIdentification = documentManagement.getDocumentIdentification(frameID);
            Content contentRef = getContentService().write(this.getReference(documentIdentification.getPath(), documentIdentification.getUuid(),
                                                                             DMSStoreRegistry.STORE_REF), Constants.PROP_CONTENT, content,
                                                           contentFormat);

            if (createNewVersion)
            {
                return createNewVersion(name, username, contentRef.getNode().getUuid());
            }

            /** Set the document identification. */

            documentManagement.setDocumentIdentification(contentRef.getNode().getUuid(), null, frameID);
            if (this.commonBean.getDirectClassOfInstance(frameID).equals(Relation.Classes.KNOWLEDGE_LINK))
                documentManagement.bindDocument(contentRef.getNode().getUuid(), name, frameID, getLastVersionLabel(contentRef.getNode()));
            return null;
        }
        catch (ContentFault e)
        {
            String message = "Could not update content: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
        catch (RemoteException e)
        {
            String message = "Could not create connection: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
    }

    /**
     * Gets the last version label.
     * 
     * @param reference the node Reference.
     * @return the version label.
     */
    @Interceptors(AuthenticationInterceptor.class)
    private String getLastVersionLabel(Reference reference) throws DMSException, OntologyErrorException
    {
        String result = DMSConstants.INITIAL_VERSION_LABEL;
        Reference ref = reference;
        org.prowim.datamodel.dms.VersionHistory history = getDocumentVersionHistory(ref.getUuid());
        List<Version> versions = history.getVersions();
        if (versions != null && versions.size() > 0)
        {
            result = versions.get(0).getLabel();
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#deleteDocument(java.lang.String)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public void deleteDocument(String frameID) throws OntologyErrorException, DMSException
    {
        Validate.notNull(frameID);
        DocumentIdentification documentIdentification = documentManagement.getDocumentIdentification(frameID);
        documentManagement.clearDocumentIdentificationForPI(frameID);
        String uuid = documentIdentification.getUuid();
        if (uuid != null && !uuid.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
        {
            this.deleteContent(uuid);
        }
        else
        {
            String message = "The ID of the document in the ontology is NULL ";
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#deleteContent(java.lang.String)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public void deleteContent(String uuid) throws DMSException
    {
        Validate.notNull(uuid);
        this.getContentService();
        final Reference reference = new Reference(DMSStoreRegistry.STORE_REF, uuid, null);
        final Predicate predicate = new Predicate(new Reference[] { reference }, null, null);
        final CMLDelete delete = new CMLDelete(predicate);
        final CML cml = new CML();
        cml.setDelete(new CMLDelete[] { delete });
        try
        {
            WebServiceFactory.getRepositoryService().update(cml);
        }
        catch (RepositoryFault e)
        {
            String message = "Could not delete content: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
        catch (RemoteException e)
        {
            String message = "Could not create connection: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#downloadDocument(java.lang.String)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public Document downloadDocument(String frameID) throws OntologyErrorException, DMSException
    {
        Validate.notNull(frameID, "frame: " + frameID);
        Document document = null;
        DocumentIdentification documentIdentification = documentManagement.getDocumentIdentification(frameID);
        String uuid = documentIdentification.getUuid();
        Reference contentReference = new Reference(DMSStoreRegistry.STORE_REF, uuid, null);

        try
        {
            Content[] readResult = this.getContentService()
                    .read(new Predicate(new Reference[] { contentReference }, DMSStoreRegistry.STORE_REF, null), Constants.PROP_CONTENT);
            /** fetch the content. */
            Content content = readResult[0];
            document = getContent(content.getNode(), DMSStoreRegistry.STORE_REF);

        }
        catch (ContentFault e)
        {
            String message = "could not get dms content: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e);
        }
        catch (RemoteException e)
        {
            String message = "RemoteException: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException("Remote Excpetion : ", dmsFault, e);

        }
        if (document == null)
            throw new IllegalStateException("Cannot get the documents content: " + frameID);
        return document;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#downloadDocumentInVersion(java.lang.String, java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public Document downloadDocumentInVersion(String frameID, String versionLabel) throws OntologyErrorException, DMSException
    {
        Validate.notNull(frameID);
        Validate.notNull(versionLabel);
        String uuid = getUUID(frameID);
        try
        {
            return getDocumentInVersion(uuid, versionLabel);
        }
        catch (RemoteException e)
        {
            LOG.error("Could not download document :  " + frameID, e);
            throw new IllegalStateException("Could not download document  " + frameID);
        }
    }

    /**
     * Gets the content from Alfresco DMS.
     * 
     * @param reference not null {@link Reference}
     * @param store the store.
     * @return not null {@link Document}
     * @throws RemoteException If the {@link Reference} is not equivalent to an existing {@link Node}.
     * @throws DMSException if an error occurs in DMS back end
     */
    private Document getContent(Reference reference, Store store) throws RemoteException, DMSException
    {
        Validate.notNull(reference);
        Validate.notNull(store);

        /** Get the content service. */
        final ContentServiceSoapBindingStub contentService = this.getContentService();
        Document result = null;

        /** Read the content from the respository. */
        Content[] readResult;
        readResult = contentService.read(new Predicate(new Reference[] { reference }, store, null), Constants.PROP_CONTENT);
        Content content = readResult[0];

        if (readResult != null && content != null && content.getUrl() != null)
        {
            final String fileName = this.getName(content.getUrl());
            File tempFile = new File(ResourcesLocator.getResourcesTempDir() + fileName);

            /** Get the content from the download servlet using the URL and display it. */
            /** Copy content in a temporary direcoty. */

            ContentUtils.copyContentToFile(content, tempFile);

            try
            {
                FileInputStream fin;
                fin = new FileInputStream(tempFile);
                int bytesCount = (int) tempFile.length();
                byte[] contentBytes = new byte[bytesCount];
                fin.read(contentBytes);
                result = new Document(fileName, content.getFormat().getMimetype(), contentBytes);
                fin.close();
                this.deleteTempFile(tempFile);
            }
            catch (IOException e)
            {
                String message = "IO Error: ";
                LOG.error(message, e);
                DMSFault dmsFault = new DMSFault();
                dmsFault.setMessage(message);
                throw new DMSException(message, dmsFault, e);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#getVersionHistory(java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public org.prowim.datamodel.dms.VersionHistory getVersionHistory(String frameID) throws OntologyErrorException, DMSException
    {
        Validate.notNull(frameID);
        String uuid = getUUID(frameID);
        if (uuid != null)
        {
            return getDocumentVersionHistory(uuid);
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#getDocumentName(java.lang.String)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public String getDocumentName(String frameID) throws OntologyErrorException
    {
        SearchService searchService = new SearchService();
        DocumentContentProperties props = searchService.retrieveMetaData(getUUID(frameID));
        return props.getContentName();
    }

    /**
     * Gets the file name from the url.
     * 
     * @param url the url.
     * @return the file name.
     */
    private String getName(String url)
    {
        return DMSUtility.getName(url);
    }

    /**
     * Helper method to make apply the versionable aspect to a given reference
     * <p>
     * 
     * @param reference the reference
     * @throws Exception can occurs.
     */
    private void makeVersionable(Reference reference) throws RemoteException
    {
        Validate.notNull(reference);
        /** 1. the add aspect query object. */
        NamedValue[] comments = new NamedValue[] { Utils.createNamedValue("versionType", "MINOR"), Utils.createNamedValue("autoVersion", "false") };
        // MAJOR
        Predicate predicate = new Predicate(new Reference[] { reference }, null, null);
        CMLAddAspect addAspect = new CMLAddAspect(Constants.ASPECT_VERSIONABLE, comments, predicate, null); // ASPECT_VERSIONABLE

        /** 2. Create the content management language query. */
        CML cml = new CML();
        cml.setAddAspect(new CMLAddAspect[] { addAspect });

        /** 3. Execute the query, which will add the versionable aspect to the node is question. */
        this.getRepositoryService().update(cml);
    }

    /**
     * Gets the uuid to a document stored for the frame with id ) frameID.
     * 
     * @param frameID the not null ID of the frame.
     * @return the uuid, if the frame has a document stored in DMS otherwise null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private String getUUID(String frameID) throws OntologyErrorException
    {
        Validate.notNull(frameID);
        DocumentIdentification documentIdentification = documentManagement.getDocumentIdentification(frameID);
        if (documentIdentification != null)
        {
            return documentIdentification.getUuid();
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#getDocumentVersionHistory(java.lang.String)
     */
    @Interceptors(AuthenticationInterceptor.class)
    public org.prowim.datamodel.dms.VersionHistory getDocumentVersionHistory(String uuid) throws DMSException, OntologyErrorException
    {
        Validate.notNull(uuid);
        VersionHistory versionHistory = null;
        ArrayList<org.prowim.datamodel.dms.Version> versionList = new ArrayList<org.prowim.datamodel.dms.Version>();
        try
        {
            versionHistory = getAuthoringService().getVersionHistory(getReference(null, uuid, DMSStoreRegistry.STORE_REF));
        }
        catch (AuthoringFault e)
        {
            LOG.error("Authoring fault: ", e);
        }
        catch (RemoteException e)
        {
            LOG.error("Remote exception: ", e);
        }

        if (versionHistory != null && versionHistory.getVersions() != null)
        {
            for (org.alfresco.webservice.types.Version version : versionHistory.getVersions())
            {
                String author = "";
                long createdAsLong = version.getCreated().getTime().getTime();
                org.prowim.datamodel.dms.Version clientVersion = new org.prowim.datamodel.dms.Version(
                                                                                                                DateUtility
                                                                                                                        .getDateTimeString(new DateTime(
                                                                                                                                                        createdAsLong)),
                                                                                                                version.getCreator(), version
                                                                                                                        .getLabel());

                clientVersion.setInstanceID(uuid);
                NamedValue[] nv = version.getCommentaries();
                boolean hasAuthorProp = false;
                for (int i = 0; i < nv.length; i++)
                {
                    if (nv[i].getName().equals(DMSConstants.Content.AUTHOR_PROP))
                    {
                        if (nv[i].getValue().contains(DMSConstants.Content.USER_ID_PATTERN))
                        {
                            author = organizationEntity.getPerson(nv[i].getValue()).toString();
                            clientVersion.setCreator(author);
                        }
                        hasAuthorProp = true;
                        break;
                    }
                }
                if ( !hasAuthorProp)
                {
                    for (int i = 0; i < nv.length; i++)
                    {
                        if (nv[i].getName().equals(DMSConstants.Content.AUTHOR_CONST))
                        {
                            if ((nv[i].getValue() != null) && (nv[i].getValue().contains(DMSConstants.Content.USER_ID_PATTERN)))
                            {
                                author = organizationEntity.getPerson(nv[i].getValue()).toString();

                                clientVersion.setCreator(author);
                                LOG.debug(DMSConstants.Content.AUTHOR_PROP + "   --> " + nv[i].getValue());
                                break;
                            }
                        }
                    }
                }
                versionList.add(clientVersion);
            }
        }
        return new org.prowim.datamodel.dms.VersionHistory(uuid, versionList);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#getDocumentVersionHistoryFromProcessID(java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public org.prowim.datamodel.dms.VersionHistory getDocumentVersionHistoryFromProcessID(String processID) throws OntologyErrorException,
                                                                                                                DMSException
    {
        // get the alfresco node id
        String processInformationID = processHelper.getChartProcessInformationID(processID);

        if (processInformationID == null)
        {
            throw new IllegalStateException("Could not find process information for process with ID: " + processID);
        }

        DocumentIdentification documentIdentification = documentManagement.getDocumentIdentification(processInformationID);
        String uuid = documentIdentification.getUuid();

        // return the version history
        return getDocumentVersionHistory(uuid);
    }

    /**
     * Downloads a {@link Document} from the DMS.
     * 
     * @param uuid the ID of the document in DMS.<br>
     * @param versionLabel the version label to be downloaded. Use {@link DMSConstants#INITIAL_VERSION_LABEL} for first version. Null is not allowed.
     * 
     * @return not null {@link Document} or Exception occurs if no content or no document for the given frameID, or if the version label does not exist.
     * @throws RemoteException Communication Exception.
     * @throws DMSException if an error occurs in DMS back end
     * 
     */
    @Interceptors(AuthenticationInterceptor.class)
    public Document getDocumentInVersion(String uuid, String versionLabel) throws RemoteException, DMSException
    {
        Validate.notNull(uuid);
        Validate.notNull(versionLabel);
        /** Get the version history. */
        VersionHistory versionHistory = null;
        Document document = null;

        versionHistory = getAuthoringService().getVersionHistory(getReference(null, uuid, DMSStoreRegistry.STORE_REF));

        for (org.alfresco.webservice.types.Version version : versionHistory.getVersions())
        {
            Reference reference = version.getId();
            if (version.getLabel().equals(versionLabel))
            {
                document = getContent(reference, DMSStoreRegistry.VERSION_STORE_REF);
                if (document != null)
                {
                    return document;
                }
            }
        }

        throw new IllegalStateException("Could not download the version :  " + versionLabel);
    }

    /**
     * Removes the temporarily written file.
     * 
     * @param tempFile name
     */
    private void deleteTempFile(File tempFile)
    {
        if ( !tempFile.exists())
        {
            LOG.error("no such file or directory: ");
            throw new IllegalArgumentException("Delete: no such file or directory: " + tempFile.getName());
        }

        if ( !tempFile.canWrite())
        {
            LOG.error("write protected: ");
            throw new IllegalArgumentException("Delete: write protected: " + tempFile.getName());
        }

        // If it is a directory, make sure it is empty
        if (tempFile.isDirectory())
        {
            String[] files = tempFile.list();
            if (files.length > 0)
            {
                LOG.error("directory not empty: ");
                throw new IllegalArgumentException("Delete: directory not empty: " + tempFile.getName());
            }
        }

        // Attempt to delete it
        boolean success = tempFile.delete();

        if ( !success)
        {
            LOG.error("Delete: deletion failed: ");
            throw new IllegalArgumentException("Delete: deletion failed: ");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#downloadDocumentInVersionFromID(java.lang.String, java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public Document downloadDocumentInVersionFromID(String id, String versionLabel) throws DMSException
    {
        Document result = null;

        String message = "Could not download document from DMS :  id ";
        try
        {
            result = getDocumentInVersion(id, versionLabel);
        }
        catch (RemoteException e)
        {
            LOG.error(message + id, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e);
        }
        if (result != null)
            return result;
        else
            throw new IllegalStateException(message + id);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#getAllDocuments()
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public DocumentContentPropertiesArray getAllDocuments() throws DMSException
    {
        SearchService searcher = new SearchService();
        try
        {
            return searcher.getAllDocuments(DMSConstants.getCustomerFolderID());
        }
        catch (RemoteException e)
        {
            String message = "An remote exception occurs while searching the DMS";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#searchFullText(java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public DocumentContentPropertiesArray searchFullText(String keyWord) throws DMSException
    {
        SearchService searcher = new SearchService();

        try
        {
            return searcher.searchFullText(keyWord, DMSConstants.getCustomerFolder());
        }
        catch (RemoteException e)
        {
            String message = "An remote exception occurs while searching the DMS";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#revertVersion(java.lang.String, java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public void revertVersion(String documentID, String versionLabel)
    {
        Validate.notNull(documentID);
        Validate.notNull(versionLabel);

        try
        {
            getAuthoringService().revertVersion(getReference(null, documentID, DMSStoreRegistry.STORE_REF), versionLabel);
        }
        catch (AuthoringFault e)
        {
            LOG.error("Your log-message: ", e);
        }
        catch (RemoteException e)
        {
            LOG.error("Your log-message: ", e);
        }
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#findFolderOrContent(String)
     * 
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public String findFolderOrContent(String contentName)
    {
        Validate.notNull(contentName, "The name of content or a folder can not be null");

        String idReference = null;
        RepositoryServiceSoapBindingStub repoService = WebServiceFactory.getRepositoryService();
        try
        {
            QueryResult results = repoService.queryChildren(new Reference(DMSStoreRegistry.STORE_REF, DMSConstants.getCustomerFolderID(), null));
            ResultSetRow[] rows = results.getResultSet().getRows();
            ResultSetRow currentRow;
            NamedValue[] colums;
            String currentName;
            String currentValue;
            if (rows != null)
            {
                for (int i = 0; i < rows.length; i++)
                {
                    currentRow = rows[i];
                    colums = currentRow.getColumns();
                    for (int y = 0; y < colums.length; y++)
                    {
                        currentName = colums[y].getName();
                        if (currentName.equals(Constants.PROP_NAME))
                        {
                            currentValue = colums[y].getValue();
                            if (currentValue.equals(contentName))
                            {
                                idReference = currentRow.getNode().getId();
                            }
                        }
                    }
                }
            }
        }
        catch (RepositoryFault e)
        {
            LOG.error("RepositoryFault-Error by search a item in DMS", e);
        }
        catch (RemoteException e)
        {
            LOG.error("RemoteException-Error by search a item in DMS", e);
        }
        return idReference;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.dms.alfresco.ContentService#updateDMSDocument(org.prowim.datamodel.dms.Document, java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public void updateDMSDocument(Document document, String uuid) throws DMSException
    {
        Validate.notNull(document);
        Validate.notNull(uuid);

        ContentFormat contentFormat = new ContentFormat(document.getContentType(), DMSConstants.ENCODING);
        try
        {
            DocumentIdentification documentIdentification = new DocumentIdentification(uuid, null);
            getContentService().write(this.getReference(documentIdentification.getPath(), documentIdentification.getUuid(),
                                                        DMSStoreRegistry.STORE_REF), Constants.PROP_CONTENT, document.getContent(), contentFormat);

        }
        catch (ContentFault e)
        {
            String message = "Could not update content: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
        catch (RemoteException e)
        {
            String message = "Could not create connection: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.ContentService#updateDocument(org.prowim.datamodel.dms.Document, java.lang.String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public void updateDocument(Document document, String userName) throws DMSException
    {
        Validate.notNull(document);
        Validate.notNull(userName);

        ContentFormat contentFormat = new ContentFormat(document.getContentType(), DMSConstants.ENCODING);
        try
        {
            String uuid = findFolderOrContent(document.getName());
            DocumentIdentification documentIdentification = new DocumentIdentification(uuid, null);
            Content contentRef = getContentService().write(this.getReference(documentIdentification.getPath(), documentIdentification.getUuid(),
                                                                             DMSStoreRegistry.STORE_REF), Constants.PROP_CONTENT,
                                                           document.getContent(), contentFormat);

            createNewVersion(document.getName(), userName, contentRef.getNode().getUuid());
        }
        catch (ContentFault e)
        {
            String message = "Could not update content: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
        catch (RemoteException e)
        {
            String message = "Could not create connection: ";
            LOG.error(message, e);
            DMSFault dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
        catch (OntologyErrorException e)
        {
            // TODO $Author: khodaei $ Auto-generated catch block
            LOG.error("Your log-message: ", e);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @throws DMSException
     * @throws OntologyErrorException
     * 
     * @see org.prowim.dms.alfresco.ContentService#uploadDocument(org.prowim.datamodel.dms.Document, String)
     */
    @Override
    @Interceptors(AuthenticationInterceptor.class)
    public boolean uploadDocument(Document document, String userName) throws DMSException, OntologyErrorException
    {
        Validate.notNull(document);

        ContentServiceSoapBindingStub contentService = getContentService();
        ContentFormat contentFormat = new ContentFormat(document.getContentType(), DMSConstants.ENCODING);
        DMSFault dmsFault;

        try
        {
            if (findFolderOrContent(document.getName()) == null)
            {
                Reference contentReference = this.createReference(document.getName(), organizationEntity.getUser(userName).getID());

                contentService.write(contentReference, Constants.PROP_CONTENT, document.getContent(), contentFormat);
                makeVersionable(contentReference);
                return true;
            }
            else
                return false;
        }
        catch (ContentFault e)
        {
            String message = "Could not create content: ";
            LOG.error(message, e);
            dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
        catch (RemoteException e)
        {
            String message = "Could not create connection: ";
            LOG.error(message, e);
            dmsFault = new DMSFault();
            dmsFault.setMessage(message);
            throw new DMSException(message, dmsFault, e.getCause());
        }
    }

}
