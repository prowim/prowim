/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-14 13:29:20 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/ProWimEditorEngine.java $
 * $LastChangedRevision: 5075 $
 *------------------------------------------------------------------------------
 * (c) 16.10.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.joda.time.DateTime;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.prowim.InformationTypesConstants;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.EditorEngine;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;


/**
 * Implements {@link EditorEngine} to executes the rules related to documents.
 * 
 * @author Saad Wardi
 * @version $Revision: 5075 $
 */
@Stateless
public class ProWimEditorEngine implements EditorEngine
{
    private static final Logger LOG = Logger.getLogger(ProWimEditorEngine.class);

    @IgnoreDependency
    @EJB
    private CommonEngine        commonEngine;

    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#testScope(java.lang.String)
     */
    @Override
    public RecordSet testScope(String oid) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.OID, new AlgernonValue(oid, true));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.TEST_SCOPE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.TEST_SCOPE, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#getTypeID(java.lang.String)
     */
    @Override
    public RecordSet getTypeID(String oid) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.OID, new AlgernonValue(oid, true));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_TYPE_ID, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_TYPE_ID, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#isFrame(java.lang.String)
     */
    @Override
    public RecordSet isFrame(String oid) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.OID, new AlgernonValue(oid, true));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.IS_FRAME, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.IS_FRAME, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#addInstance(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet addInstance(String oid, String name) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.OID, new AlgernonValue(oid, true));
        tab.put(EditorEngineConstants.Variables.Common.NAME, new AlgernonValue(name, true));

        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.CREATE_NEW_INSTANCE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.CREATE_NEW_INSTANCE, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#deleteInstance(java.lang.String)
     */
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
     * @see org.prowim.services.coreengine.EditorEngine#getInstanceCount(java.lang.String)
     */
    @Override
    public RecordSet getInstanceCount(String oid) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.OID, new AlgernonValue(oid, true));

        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_INSTANCE_COUNT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_INSTANCE_COUNT, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setElementToModel(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setElementToModel(String slot, String modelID, String elementID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.SLOT, new AlgernonValue(slot, true));
        tab.put(EditorEngineConstants.Variables.Common.MODEL_ID, new AlgernonValue(modelID, false));
        tab.put(EditorEngineConstants.Variables.Common.ELEMENT_ID, new AlgernonValue(elementID, true));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.SET_ELEMENT_TO_MODEL, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.SET_ELEMENT_TO_MODEL, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#approveProcess(java.lang.String)
     */
    @Override
    public RecordSet approveProcess(String modelID) throws OntologyErrorException
    {
        /** Calculate the actual timestamp value. */
        DateTime dateTime = new DateTime();
        long actualTimestamp = dateTime.getMillis();
        /** Clear the old value if the model was already opened before. */
        commonEngine.clearRelationValue(modelID, Relation.Slots.START);
        /** Set the Start slot value. */
        LOG.debug("The model with id = " + modelID + " opened at " + actualTimestamp);
        return commonEngine.setSlotValue(modelID, Relation.Slots.START, new Long(actualTimestamp).toString());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#disapproveProcess(java.lang.String)
     */
    public RecordSet disapproveProcess(String modelID) throws OntologyErrorException
    {
        /** Calculate the actual timestamp value. */
        DateTime dateTime = new DateTime();
        long actualTimestamp = dateTime.getMillis();
        /** Disable the model. */
        LOG.debug("The model with id = " + modelID + " closed at " + actualTimestamp);
        return commonEngine.clearRelationValue(modelID, Relation.Slots.START);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#getModels()
     */
    @Override
    public RecordSet getModels() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_MODELS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_MODELS, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#connectActivityControlFlow(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet connectActivityControlFlow(String sourceID, String targetID, String controlFlowID) throws OntologyErrorException
    {

        /** 1. Activate the control flow. */
        Hashtable<String, AlgernonValue> activateTab = new Hashtable<String, AlgernonValue>();
        activateTab.put(EditorEngineConstants.Variables.Common.SOURCE_ID, new AlgernonValue(sourceID, false));
        activateTab.put(EditorEngineConstants.Variables.Common.CONNECTOR_ID, new AlgernonValue(controlFlowID, false));
        RecordSet activateControlFlowResult = myService.executeRule(EditorEngineConstants.Rules.Common.ACTIVATE_CONTROLFLOW, activateTab,
                                                                    AlgernonConstants.TELL);
        /** 2. Set the "setzt_aktiv" slot value. */
        if (activateControlFlowResult.getResult().equals(AlgernonConstants.OK))
        {
            activateTab = new Hashtable<String, AlgernonValue>();
            activateTab.put(EditorEngineConstants.Variables.Common.CONNECTOR_ID, new AlgernonValue(controlFlowID, false));
            activateTab.put(EditorEngineConstants.Variables.Common.TARGET_ID, new AlgernonValue(targetID, false));
            return myService.executeRule(EditorEngineConstants.Rules.Common.SET_ACTIVITY_ACTIVE, activateTab, AlgernonConstants.TELL);
        }
        else
        {
            LOG.error("Could connect the source activity with id = " + sourceID + " to the activity with id = " + targetID
                    + " with the controlflow id = " + controlFlowID);
            throw new IllegalStateException("Could connect the source activity with id = " + sourceID + " to the target activity with id = "
                    + targetID + " with the controlflow id = " + controlFlowID);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setScope(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setScope(String instanceID, String scope) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ID, new AlgernonValue(instanceID, false));
        tab.put(EditorEngineConstants.Variables.Common.SCOPE, new AlgernonValue(scope, true));

        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.SET_SCOPE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.SET_SCOPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#connectActivityRole(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet connectActivityRole(String activityID, String roleID, String taskID) throws OntologyErrorException
    {
        LOG.debug("connectActivityRole :   " + "actID " + activityID + "  roleID  " + roleID + "  taskID  " + taskID);
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ACTIVITY_ID, new AlgernonValue(activityID, false));
        tab.put(EditorEngineConstants.Variables.Common.ROLE_ID, new AlgernonValue(roleID, false));
        tab.put(EditorEngineConstants.Variables.Common.TASK_ID, new AlgernonValue(taskID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.CONNECT_ACTIVITY_ROLE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.CONNECT_ACTIVITY_ROLE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#getRelations(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet getRelations(String sourceID, String targetID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.SOURCE_ID, new AlgernonValue(sourceID, false));
        tab.put(EditorEngineConstants.Variables.Common.TARGET_ID, new AlgernonValue(targetID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_RELATIONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_RELATIONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setRelationValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setRelationValue(String instanceID, String slot, String relationID) throws OntologyErrorException
    {
        RecordSet clearRecords = commonEngine.clearRelationValue(instanceID, slot);
        if (clearRecords.getResult().equals(AlgernonConstants.OK))
        {
            RecordSet setRecords = commonEngine.setSlotValue(instanceID, slot, relationID);
            if (setRecords.getResult().equals(AlgernonConstants.OK))
            {
                LOG.debug("Slot value was successfully set");
                return setRecords;
            }
            else
            {
                throw new IllegalStateException("Could not set the relation with id  " + relationID + "  to object " + instanceID + " at slot "
                        + slot);
            }
        }
        else
        {
            throw new IllegalStateException("Could not clear the relation with id  " + relationID + "  to object " + instanceID + " at slot " + slot);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setProduct(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setProduct(String source, String target, String productID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ACTIVITY_ID, new AlgernonValue(source, false));
        tab.put(EditorEngineConstants.Variables.Common.PRODUCT_ID, new AlgernonValue(productID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.SET_PRODUCT_OUT, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.SET_PRODUCT_OUT, result, this.getClass());

        if (result.getResult().equals(AlgernonConstants.OK))
        {
            tab = new Hashtable<String, AlgernonValue>();
            tab.put(EditorEngineConstants.Variables.Common.ACTIVITY_ID, new AlgernonValue(target, false));
            tab.put(EditorEngineConstants.Variables.Common.PRODUCT_ID, new AlgernonValue(productID, false));
            result = myService.executeRule(EditorEngineConstants.Rules.Common.SET_PRODUCT_IN, tab, AlgernonConstants.TELL);
            LoggingUtility.logResult(EditorEngineConstants.Rules.Common.SET_PRODUCT_IN, result, this.getClass());

            if (result.getResult().equals(AlgernonConstants.OK))
            {
                LOG.debug("SET_PRODUCT_IN connect the source activity with id   " + source + "  to the target activity with id " + target
                        + " with the product " + productID + "   :  " + result.getResult());
            }
            else
            {
                LOG.error("Could not connect the source activity with id  " + source + "  to the target activity with id " + target
                        + " with the product " + productID);
                throw new IllegalStateException("Could not connect the source activity with id  " + source + "  to the target activity with id "
                        + target + " with the product " + productID);
            }
        }
        else
        {
            LOG.error("SET_PRODUCT_OUT Could not connect the source activity with id  " + source + "  to the target activity with id " + target
                    + " with the product " + productID);
            throw new IllegalStateException("Could not connect the source activity with id  " + source + "  to the target activity with id " + target
                    + " with the product " + productID);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#checkDMSIDforProcessInformation(java.lang.String)
     */
    @Override
    public RecordSet checkDMSIDforProcessInformation(String processInformationID) throws OntologyErrorException
    {
        return commonEngine.getSlotValue(processInformationID, InformationTypesConstants.ContentSlots.CONTENT_STRING);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#isInformationType(java.lang.String)
     */
    @Override
    public RecordSet isInformationType(String processInformationID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ID, new AlgernonValue(processInformationID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.IS_INFORMATION_TYPE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.IS_INFORMATION_TYPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#getControlFlowsCount(java.lang.String)
     */
    @Override
    public RecordSet getControlFlowsCount(String targetID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ID, new AlgernonValue(targetID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_CONTROLFLOWS_COUNT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_CONTROLFLOWS_COUNT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#removeRelationValue(java.lang.String, java.lang.String)
     */
    @Override
    public void removeRelationValue(String instanceID, String slot) throws OntologyErrorException
    {
        RecordSet clearRecords = commonEngine.clearRelationValue(instanceID, slot);
        if (clearRecords.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("removeRelationValue  : " + instanceID + "  " + slot);
        }
        else
        {
            throw new IllegalStateException("Could not remove the relation to object " + instanceID + " at slot " + slot);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#getCombinationRules()
     */
    @Override
    public RecordSet getCombinationRules() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_COMBINATION_RULES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_COMBINATION_RULES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setCombinationRule(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setCombinationRule(String controlFlowID, String combinationRule) throws OntologyErrorException
    {
        RecordSet result = commonEngine.setSlotValue(controlFlowID, Relation.Slots.HAS_COMBINATION_RULE, combinationRule);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#getActivationRules()
     */
    @Override
    public RecordSet getActivationRules() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_ACTIVATION_RULES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_ACTIVATION_RULES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setActivationRule(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setActivationRule(String objectID, String activationRule) throws OntologyErrorException
    {
        commonEngine.clearRelationValue(objectID, EditorEngineConstants.Slots.Common.HAS_ACTIVATION_RULE);
        RecordSet result = commonEngine.setSlotValue(objectID, EditorEngineConstants.Slots.Common.HAS_ACTIVATION_RULE, activationRule);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#connectActivityMittel(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet connectActivityMittel(String activityID, String mittelID, String functionID) throws OntologyErrorException
    {

        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EditorEngineConstants.Variables.Common.ACTIVITY_ID, new AlgernonValue(activityID, false));
        tab.put(EditorEngineConstants.Variables.Common.MITTEL_ID, new AlgernonValue(mittelID, false));
        tab.put(EditorEngineConstants.Variables.Common.FUNCTION_ID, new AlgernonValue(functionID, false));
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.CONNECT_ACTIVITY_MITTEL, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.CONNECT_ACTIVITY_MITTEL, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#getCombinationRule(java.lang.String)
     */
    @Override
    public RecordSet getCombinationRule(String controlflowID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_CONTROLFLOWS, new AlgernonValue(controlflowID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_CONTROLFLOW_RULE, tab, AlgernonConstants.ASK);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setSubProcessFlagForActivity(java.lang.String)
     */
    @Override
    public RecordSet setSubProcessFlagForActivity(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        return myService.executeRule(EngineConstants.Rules.SubProcess.SET_SUB_PROECESS_FLAG_FOR_ACTIVITY, tab, AlgernonConstants.TELL);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setSubProcessFlagForProcess(java.lang.String)
     */
    @Override
    public RecordSet setSubProcessFlagForProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processID, false));
        return myService.executeRule(EngineConstants.Rules.SubProcess.SET_SUB_PROECESS_FLAG_FOR_PROCESS, tab, AlgernonConstants.TELL);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setSubProcessOfActivity(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setSubProcessOfActivity(String subProcessID, String activityID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(subProcessID, false));
        processInstanceHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        final RecordSet subprocessRecords = myService.executeRule(EngineConstants.Rules.Activity.SET_SUB_PROCESS_OF_ACTIVIY, processInstanceHash,
                                                                  AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Activity.SET_SUB_PROCESS_OF_ACTIVIY, subprocessRecords, this.getClass());

        return subprocessRecords;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setActivityOperation(java.lang.String, String)
     */
    @Override
    public RecordSet setActivityOperation(String activityID, String flag) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        processInstanceHash.put(EngineConstants.Variables.Activity.AUTO_ACTIVITY_FLAG, new AlgernonValue(flag, false));
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Activity.SET_ACTIVIY_AS_AUTO, processInstanceHash,
                                                       AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Activity.SET_ACTIVIY_AS_AUTO, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#deleteElementFromModel(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet deleteElementFromModel(String processID, String elementID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processID, false));
        processInstanceHash.put(EngineConstants.Variables.Common.INSTANCE_ID, new AlgernonValue(elementID, false));
        final RecordSet subprocessRecords = myService.executeRule(EngineConstants.Rules.Editor.DELETE_ELEMENT_FROM_MODEL, processInstanceHash,
                                                                  AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Editor.DELETE_ELEMENT_FROM_MODEL, subprocessRecords, this.getClass());

        return subprocessRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#isProcessApproved(java.lang.String)
     */
    @Override
    public RecordSet isProcessApproved(String modelID) throws OntologyErrorException
    {
        return commonEngine.getSlotValue(modelID, Relation.Slots.START);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorEngine#setProcessLandscapeFlag(java.lang.String, boolean)
     */
    @Override
    public RecordSet setProcessLandscapeFlag(String processID, boolean flag) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> hashTable = new Hashtable<String, AlgernonValue>();
        hashTable.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processID, false));
        if (flag)
            hashTable.put(EngineConstants.Variables.Common.VALUE, new AlgernonValue(EngineConstants.Consts.TRUE, false));
        else
            hashTable.put(EngineConstants.Variables.Common.VALUE, new AlgernonValue(EngineConstants.Consts.FALSE, false));

        final RecordSet recordSet = myService.executeRule(EngineConstants.Rules.Editor.SET_PROCESS_LANDSCAPE_FLAG, hashTable, AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Editor.SET_PROCESS_LANDSCAPE_FLAG, recordSet, this.getClass());

        return recordSet;
    }

}
