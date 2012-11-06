/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-07 14:55:21 +0200 (Di, 07 Sep 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/DocumentManagementHelperBean.java $
 * $LastChangedRevision: 4772 $
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
package org.prowim.services.coreengine.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.dms.DocumentIdentification;
import org.prowim.datamodel.prowim.InformationTypesConstants;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.dms.alfresco.ContentService;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.DocumentEngine;
import org.prowim.services.coreengine.DocumentManagementHelper;

import de.ebcot.tools.logging.Logger;


/**
 * The implementation for {@link DocumentManagementHelper document management}<br>
 * 
 * @author Saad Wardi
 * @version $Revision: 4772 $
 */
@Stateless
public class DocumentManagementHelperBean implements DocumentManagementHelper
{
    private static final Logger LOG = Logger.getLogger(DocumentManagementHelperBean.class);

    @IgnoreDependency
    @EJB
    private DocumentEngine      documentEngine;

    @IgnoreDependency
    @EJB
    private CommonEngine        commonEngine;

    @IgnoreDependency
    @EJB
    private ContentService      contentService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#setDocumentIdentification(java.lang.String, java.lang.String, java.lang.String)
     */
    public String setDocumentIdentification(String uuid, String path, String knowledgeLinkID) throws OntologyErrorException
    {

        LOG.info("setDocumentIdentification " + uuid + "  " + knowledgeLinkID);
        this.clearDocumentIdentificationForPI(knowledgeLinkID);
        String result = AlgernonConstants.FAILED;
        RecordSet documentIdentificationRecords = documentEngine.setDocumentIdentificationForProcessInformation(uuid, path, knowledgeLinkID);
        if (documentIdentificationRecords.getResult().equals(AlgernonConstants.OK))
        {
            result = documentIdentificationRecords.getResult();
            LOG.info("setDocumentIdentification = SUCCEEDD");
        }
        else
        {
            LOG.info("setDocumentIdentification = FAILED");
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#getDocumentIdentification(java.lang.String)
     */
    public DocumentIdentification getDocumentIdentification(String frameID) throws OntologyErrorException
    {
        RecordSet documentIdentificationRecords = documentEngine.getDocumentIdentificationForProcessInformation(frameID);
        if (documentIdentificationRecords.getNoOfRecords() == 1)
        {
            String uuid = documentIdentificationRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Document.UUID);
            if (uuid != null)
            {
                return new DocumentIdentification(uuid, null);
            }
            else
            {
                throw new IllegalArgumentException("The DMS UUID is null   :   " + frameID);
            }
        }
        else
        {
            throw new IllegalArgumentException("Frame does not have a document in the DMS 2 :   " + frameID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#clearDocumentIdentification(java.lang.String)
     */
    public void clearDocumentIdentification(String frameID) throws OntologyErrorException
    {
        documentEngine.clearDocumentIdentification(frameID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#clearDocumentIdentificationForPI(java.lang.String)
     */
    public void clearDocumentIdentificationForPI(String frameID) throws OntologyErrorException
    {
        documentEngine.clearDocumentIdentificationForProcessInformation(frameID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#bindDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void bindDocument(String documentID, String documentName, String frameID, String versionLabel) throws OntologyErrorException
    {
        commonEngine.clearRelationValue(frameID, Relation.Slots.DOCUMENT_VERSION);
        commonEngine.clearRelationValue(frameID, InformationTypesConstants.ContentSlots.CONTENT_STRING);
        commonEngine.clearRelationValue(frameID, Relation.Slots.HYPERLINK);
        commonEngine.clearRelationValue(frameID, Relation.Slots.REFER_OF);
        documentEngine.bindDocument(documentID, documentName, frameID, versionLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#getStoredVersionLabel(java.lang.String)
     */
    public String getStoredVersionLabel(String frameID) throws OntologyErrorException
    {
        String result = null;
        RecordSet documentIdentificationRecords = commonEngine.getSlotValue(frameID, Relation.Slots.DOCUMENT_VERSION);
        if (documentIdentificationRecords.getNoOfRecords() == 1)
        {
            String versionLabel = documentIdentificationRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.VALUE);
            if (versionLabel != null)
            {
                return versionLabel;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#getXMLDocumentIDForProcess(java.lang.String)
     */
    @Override
    public String getXMLDocumentIDForProcess(String processInformationID) throws OntologyErrorException
    {
        return getDocumentIdentification(processInformationID).getUuid();
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.coreengine.DocumentManagementHelper#revertVersion(java.lang.String, java.lang.String)
     */
    @Override
    public void revertVersion(String documentID, String versionLabel) throws OntologyErrorException
    {
        contentService.revertVersion(documentID, versionLabel);
    }

}
