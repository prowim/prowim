/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-05-12 14:23:30 +0200 (Do, 12 Mai 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/ContentBase.java $
 * $LastChangedRevision: 5085 $
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

import java.rmi.RemoteException;

import javax.activation.MimetypesFileTypeMap;

import org.alfresco.webservice.authoring.AuthoringServiceSoapBindingStub;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLAddAspect;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.ContentFormat;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.prowim.datamodel.dms.DMSConstants;

import de.ebcot.tools.logging.Logger;


/**
 * Creates a session and the parent folder "ProWim" and returns its UUID, Gets the alfresco webservices end points.
 * 
 * @author Saad Wardi
 * @version $Revision: 5085 $
 */
public abstract class ContentBase implements ContentManager
{

    /** Admin user name and password used to connect to the repository */
    protected static final String    USER_NAME               = "admin";
    /** Password. */
    protected static final String    PASSWORD                = "xxx";
    /** A Folder to store the prowim contents. */
    protected static final Reference PROWIM_FOLDER_REFERENCE = new Reference(DMSStoreRegistry.STORE_REF, null, "/app:company_home/cm:sample_folder");
    /** Folder name. */
    private static final String      PROWIM_FOLDER_NAME      = "ProWim";
    private static final Logger      LOG                     = Logger.getLogger(ContentServiceBean.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#createProWimFolder()
     */
    public void createProWimFolder() throws Exception
    {
        try
        {
            /** Check to see if the "ProWim" folder has already been created or not. **/
            WebServiceFactory.getRepositoryService()
                    .get(new Predicate(new Reference[] { PROWIM_FOLDER_REFERENCE }, DMSStoreRegistry.STORE_REF, null));
        }
        catch (Exception exception)
        {
            /** Create parent reference to company home. */
            ParentReference parentReference = new ParentReference(DMSStoreRegistry.STORE_REF, null, "/app:company_home", Constants.ASSOC_CONTAINS,
                                                                  Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, "sample_folder"));

            /** Create the "ProWim" folder. */
            NamedValue[] properties = new NamedValue[] { Utils.createNamedValue(Constants.PROP_NAME, PROWIM_FOLDER_NAME) };
            CMLCreate create = new CMLCreate("1", parentReference, null, null, null, Constants.TYPE_FOLDER, properties);
            CML cml = new CML();
            cml.setCreate(new CMLCreate[] { create });
            UpdateResult[] results = WebServiceFactory.getRepositoryService().update(cml);

            /** Create parent reference to "ProWim" folder. */
            Reference sampleFolder = results[0].getDestination();

            ParentReference parentReference2 = new ParentReference(DMSStoreRegistry.STORE_REF, sampleFolder.getUuid(), null,
                                                                   Constants.ASSOC_CONTAINS,
                                                                   Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL, "sample_content"));

            /** Create content. */
            NamedValue[] properties2 = new NamedValue[] { Utils.createNamedValue(Constants.PROP_NAME, "ProWim.txt") };
            CMLCreate create2 = new CMLCreate("1", parentReference2, null, null, null, Constants.TYPE_CONTENT, properties2);
            CML cml2 = new CML();
            cml2.setCreate(new CMLCreate[] { create2 });
            UpdateResult[] results2 = WebServiceFactory.getRepositoryService().update(cml2);

            // Set content
            ContentFormat format = new ContentFormat(Constants.MIMETYPE_TEXT_PLAIN, "UTF-8");
            byte[] content = "This is a test content provided by ProWim development team!".getBytes();
            WebServiceFactory.getContentService().write(results2[0].getDestination(), Constants.PROP_CONTENT, content, format);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#getContentService()
     */
    public ContentServiceSoapBindingStub getContentService()
    {
        return WebServiceFactory.getContentService();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#getRepositoryService()
     */
    public RepositoryServiceSoapBindingStub getRepositoryService()
    {
        return WebServiceFactory.getRepositoryService();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#getAuthoringService()
     */
    public AuthoringServiceSoapBindingStub getAuthoringService()
    {
        return WebServiceFactory.getAuthoringService();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#createReference(java.lang.String, java.lang.String)
     */
    public Reference createReference(final String contentName, final String userID) throws RemoteException
    {
        // Create a reference to the parent where we want to create content
        ParentReference companyHomeParent = new ParentReference(DMSStoreRegistry.STORE_REF, DMSConstants.getCustomerFolderID(),
                                                                DMSConstants.COMPANY_HOME, Constants.ASSOC_CONTAINS,
                                                                Constants.createQNameString(Constants.NAMESPACE_CONTENT_MODEL,
                                                                                            DMSConstants.PROWIM_SPACE_NAME));

        // Assign name
        companyHomeParent.setChildName(DMSConstants.CM + contentName);
        /**
         * Construct CML statement to create content node<br>
         * Note: Assign "1" as a local id, so we can refer to it in subsequent CML statements within the same CML block.
         */
        NamedValue[] contentProps = new NamedValue[1];
        contentProps[0] = Utils.createNamedValue(Constants.PROP_NAME, contentName);
        CMLCreate create = new CMLCreate(DMSConstants.WHERE_ID, companyHomeParent, null, null, null, Constants.TYPE_CONTENT, contentProps);

        // Construct CML statement to add titled aspect
        NamedValue[] titledProps = new NamedValue[3];
        titledProps[0] = Utils.createNamedValue(Constants.PROP_TITLE, contentName);
        titledProps[1] = Utils.createNamedValue(DMSConstants.Content.CREATOR_PROP, userID);
        titledProps[2] = Utils.createNamedValue(DMSConstants.Content.AUTHOR_PROP, userID);

        CMLAddAspect addAspect = new CMLAddAspect(Constants.ASPECT_TITLED, titledProps, null, DMSConstants.WHERE_ID);

        // Construct CML Block
        CML cml = new CML();
        cml.setCreate(new CMLCreate[] { create });
        cml.setAddAspect(new CMLAddAspect[] { addAspect });

        /**
         * Issue CML statement via Repository Web Service and retrieve result<br>
         * Note: Batching of multiple statements into a single web call.
         */
        UpdateResult[] result;
        Reference content = null;
        result = WebServiceFactory.getRepositoryService().update(cml);

        content = result[0].getDestination();

        return content;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#getReference(java.lang.String, java.lang.String, org.alfresco.webservice.types.Store)
     */
    public Reference getReference(String path, String uuid, Store storeRef)
    {
        return new Reference(storeRef, uuid, path);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Content#getMimeType(java.lang.String)
     */
    public String getMimeType(String filename)
    {
        LOG.debug("THE FILE NAME:     " + filename + "  mime type is:  " + new MimetypesFileTypeMap().getContentType(filename));
        return new MimetypesFileTypeMap().getContentType(filename);
    }
}
