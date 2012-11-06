/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-27 15:19:03 +0200 (Mi, 27 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/ProWimCommonEngine.java $
 * $LastChangedRevision: 5003 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.prowim.InformationTypesConstants;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;
import de.ebcot.tools.string.EscapeFunctions;


/**
 * Implements the {@link CommonEngine}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5003 $
 */
@Stateless
public class ProWimCommonEngine implements CommonEngine
{

    private static final Logger LOG = Logger.getLogger(ProWimCommonEngine.class);

    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    /**
     * Create the engine and the algernon instance.
     */
    public ProWimCommonEngine()
    {
        LOG.debug("Starting prowim editor engine with algernon service " + myService);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#addInstance(java.lang.String, java.lang.String)
     */
    public RecordSet addInstance(String oid, String name) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.OID, new AlgernonValue(oid, true));
        tab.put(EditorEngineConstants.Variables.Common.NAME, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(name), true));

        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.CREATE_NEW_INSTANCE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.CREATE_NEW_INSTANCE, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#clearRelationValue(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet clearRelationValue(String frameID, String slotID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(slotID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#setSlotValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException
    {
        Validate.notNull(frameID, "frameID is null");
        Validate.notNull(slotID, "slotID is null");
        Validate.notNull(value, "value is null");

        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(slotID, true));

        if (slotID.equals(InformationTypesConstants.ContentSlots.CONTENT_INTEGER)
                || slotID.equals(InformationTypesConstants.ContentSlots.CONTENT_FLOAT) || value.equals(EngineConstants.Consts.TRUE)
                || value.equals(EngineConstants.Consts.FALSE))
        {
            tab.put(ProcessEngineConstants.Variables.Common.VALUE, new AlgernonValue(value, false));
        }
        else
        {
            tab.put(ProcessEngineConstants.Variables.Common.VALUE, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(value), true));
        }

        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.SET_SLOT_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.SET_SLOT_VALUE, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getName(java.lang.String)
     */
    @Override
    public RecordSet getName(String id) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ID, new AlgernonValue(id, true));

        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_INSTANCE_NAME, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_INSTANCE_NAME, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#setDescription(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setDescription(String id, String value) throws OntologyErrorException
    {
        RecordSet clearRecord = clearRelationValue(id, Relation.Slots.DESCRIPTION);
        RecordSet setRecord = setSlotValue(id, Relation.Slots.DESCRIPTION, value);

        if (clearRecord.getResult().equals(AlgernonConstants.OK) && setRecord.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("Description was set for instance with id = " + id);
        }
        else
        {
            throw new IllegalStateException("Could not set the description for instance with id = " + id);
        }
        return setRecord;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getDescription(java.lang.String)
     */
    @Override
    public RecordSet getDescription(String id) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ID, new AlgernonValue(id, true));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_DESCRIPTION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_DESCRIPTION, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getConstraintRule(java.lang.String)
     */
    @Override
    public RecordSet getConstraintRule(String slotID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.SLOT, new AlgernonValue(slotID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_SLOT_CONSTRAINT, tab, AlgernonConstants.ASK);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getInstanceProperties(java.lang.String)
     */
    @Override
    public RecordSet getInstanceProperties(String id) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ID, new AlgernonValue(id, true));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_INSTANCE_PROPERTIES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_INSTANCE_PROPERTIES, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getInstanceRelations(java.lang.String)
     */
    @Override
    public RecordSet[] getInstanceRelations(String id) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ID, new AlgernonValue(id, false));

        RecordSet[] result = new RecordSet[2];
        result[0] = myService.executeRule(EditorEngineConstants.Rules.Common.GET_INSTANCE_RELATIONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_INSTANCE_RELATIONS, result[0], this.getClass());

        result[1] = myService.executeRule(EditorEngineConstants.Rules.Common.GET_INSTANCE_RELATIONS_VALUES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_INSTANCE_RELATIONS_VALUES, result[1], this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getProcessElements()
     */
    @Override
    public RecordSet getProcessElements() throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_PROCESSELEMENTS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_PROCESSELEMENTS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#deleteInstance(java.lang.String)
     */
    @Override
    public RecordSet deleteInstance(String instanceID) throws OntologyErrorException
    {
        final Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Common.CLASS_ID, instanceID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.DELETE_INSTANCE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.DELETE_INSTANCE, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getProcessElementsInstances(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet getProcessElementsInstances(String processID, String element) throws OntologyErrorException
    {
        final Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, processID);
        tab.put(ProcessEngineConstants.Variables.Common.ELEMENT, element);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_PROCESSELEMENTS_INSTANCES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_PROCESSELEMENTS_INSTANCES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getDirectClassOfInstance(java.lang.String)
     */
    @Override
    public RecordSet getDirectClassOfInstance(String id) throws OntologyErrorException
    {
        final Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(EditorEngineConstants.Variables.Common.ID, id);
        final RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_DIRECTCLASSOFINSTANCE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_DIRECTCLASSOFINSTANCE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getProcessElementTemplateInformation(java.lang.String)
     */
    @Override
    public RecordSet getProcessElementTemplateInformation(String processlementInstanceID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PROCESS_ELEMENT_INSTANCE_ID, new AlgernonValue(processlementInstanceID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_TEMPLATE_INFORMATION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_TEMPLATE_INFORMATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getPreSelection(java.lang.String)
     */
    @Override
    public RecordSet getPreSelection(String roleID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Role.ROLLE_ID, new AlgernonValue(roleID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Role.GET_PRE_SELECTION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Role.GET_PRE_SELECTION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getOntologyVersion()
     */
    @Override
    public RecordSet getOntologyVersion() throws OntologyErrorException
    {
        final Hashtable<Object, Object> parameterMap = new Hashtable<Object, Object>();
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Admin.GET_PROWIM_VERSION, parameterMap, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Admin.GET_PROWIM_VERSION, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#getSlotValue(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet getSlotValue(String instanceID, String slot) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Common.SLOT, new AlgernonValue(slot, false));
        tab.put(EngineConstants.Variables.Common.INSTANCE_ID, new AlgernonValue(instanceID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_SLOT_VALUE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_SLOT_VALUE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonEngine#isGlobal(java.lang.String)
     */
    @Override
    public boolean isGlobal(String instanceID) throws OntologyErrorException
    {
        String status = AlgernonConstants.FAILED;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Common.INSTANCE_ID, new AlgernonValue(instanceID, false));
        RecordSet result = myService.executeRule(EngineConstants.Rules.Common.IS_GLOBAL, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Common.IS_GLOBAL, result, this.getClass());
        if (result != null && result.getNoOfRecords() > 0)
        {
            status = result.getResult();
        }

        return status.equals(AlgernonConstants.OK);
    }

}
