/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-07 14:55:21 +0200 (Di, 07 Sep 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/ProWimDocumentEngine.java $
 * $LastChangedRevision: 4772 $
 *------------------------------------------------------------------------------
 * (c) 01.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.prowim.InformationTypesConstants;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.DocumentEngine;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.utils.LoggingUtility;



/**
 * Implements {@link DocumentEngine} to executes the rules related to documents.
 * 
 * @author Saad Wardi
 * @version $Revision: 4772 $
 */
@Stateless
public class ProWimDocumentEngine implements DocumentEngine
{

    /** the algernon service. */
    @EJB
    private AlgernonService myService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#setDocumentIdentification(java.lang.String, java.lang.String, java.lang.String)
     */
    public RecordSet setDocumentIdentification(String uuid, String path, String knowledgeLinkID) throws OntologyErrorException
    {
        RecordSet documentParametersRecords = this.setSlotValue(knowledgeLinkID, Relation.Slots.HYPERLINK, uuid);
        return documentParametersRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#getDocumentIdentification(java.lang.String)
     */
    @Override
    public RecordSet getDocumentIdentification(String knowledgeLinkID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(knowledgeLinkID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Document.GET_DOCUMENT_IDENTIFICATION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Document.GET_DOCUMENT_IDENTIFICATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#clearDocumentIdentification(java.lang.String)
     */
    @Override
    public RecordSet clearDocumentIdentification(String frameID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(Relation.Slots.HYPERLINK, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#clearDocumentIdentificationForProcessInformation(java.lang.String)
     */
    @Override
    public RecordSet clearDocumentIdentificationForProcessInformation(String frameID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(InformationTypesConstants.ContentSlots.CONTENT_STRING, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#getDocumentIdentificationForProcessInformation(java.lang.String)
     */
    @Override
    public RecordSet getDocumentIdentificationForProcessInformation(String frameID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Document.GET_DOCUMENT_IDENTIFICATION_PI, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Document.GET_DOCUMENT_IDENTIFICATION_PI, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#setDocumentIdentificationForProcessInformation(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setDocumentIdentificationForProcessInformation(String uuid, String path, String frameID) throws OntologyErrorException
    {
        if (isFrameFromInformationType(frameID))
        {
            RecordSet documentParametersRecords = this.setSlotValue(frameID, InformationTypesConstants.ContentSlots.CONTENT_STRING, uuid);

            return documentParametersRecords;
        }
        else
        {
            throw new IllegalStateException("Could not set the document identification  : " + frameID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#isFrameFromInformationType(java.lang.String)
     */
    @Override
    public boolean isFrameFromInformationType(String frameID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.ProcessInformation.IS_FRAME_FROMINFOTYPE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.ProcessInformation.IS_FRAME_FROMINFOTYPE, result, this.getClass());

        if (result.getResult().equals(AlgernonConstants.FAILED))
        {
            return false;
        }
        else if (result.getResult().equals(AlgernonConstants.OK))
        {
            if ( !isInfoTypeDocument(frameID))
            {
                setSlotValue(frameID, Relation.Slots.INFORMATION_TYPE, InformationTypesConstants.DOCUMENT);
            }
            return true;
        }
        else
        {
            throw new IllegalStateException("Result status not known :    " + result.getResult());
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#isInfoTypeDocument(java.lang.String)
     */
    public boolean isInfoTypeDocument(String frameID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.ProcessInformation.IS_FRAME_FROMINFOTYPE_DOCUMENT, tab,
                                                 AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.ProcessInformation.IS_FRAME_FROMINFOTYPE_DOCUMENT, result, this.getClass());

        return (result.getResult().equals(AlgernonConstants.OK));
    }

    /**
     * Sets the value of a slot.
     * 
     * @param frameID the ID of the frame.
     * @param slotID the ID of the slot.
     * @param value the value.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private RecordSet setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(slotID, true));
        tab.put(ProcessEngineConstants.Variables.Common.VALUE, new AlgernonValue(value, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.SET_SLOT_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.SET_SLOT_VALUE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.DocumentEngine#bindDocument(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void bindDocument(String documentID, String documentName, String frameID, String versionLabel) throws OntologyErrorException
    {
        if (isFrameFromInformationType(frameID))
        {
            setSlotValue(frameID, InformationTypesConstants.ContentSlots.CONTENT_STRING, documentID);
            setSlotValue(frameID, Relation.Slots.HYPERLINK, documentName);
            setSlotValue(frameID, Relation.Slots.REFER_OF, EngineConstants.Consts.DMS_REPOSITORY_INSTANCEID);
            if (versionLabel != null)
            {
                setSlotValue(frameID, Relation.Slots.DOCUMENT_VERSION, versionLabel);
            }

        }
        else
        {
            throw new IllegalStateException("Could not bind , the frame " + frameID + "  is not vom_information_type = document!");
        }
    }
}
