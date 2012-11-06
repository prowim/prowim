/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 10.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==================================================
 * This file is part of ProWim.

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
 **/

package org.prowim.services.coreengine.impl;

import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.InformationTypesConstants;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.ParameterConstraint;
import org.prowim.datamodel.prowim.Person;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.ParameterHelper;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.entities.ControlFlowEntity;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.utils.StringConverter;

import de.ebcot.tools.logging.Logger;


/**
 * The implementation of the {@link ParameterHelper parameter helper}
 * 
 * @author Saad Wardi
 * @version $Revision$
 */
@Stateless
public class ParameterHelperBean implements ParameterHelper
{
    private static final Logger LOG = Logger.getLogger(ParameterHelperBean.class);

    @IgnoreDependency
    @EJB
    private ProcessEngine       engine;

    @IgnoreDependency
    @EJB
    private OrganizationEntity  organizationEntity;

    @IgnoreDependency
    @EJB
    private ControlFlowEntity   controlFlowEntity;

    /**
     * Gets the value of the slot Auswahl-Attribute.
     * 
     * @param parameterID the ID of the {@link Parameter}
     * @return the rule to be executed to create a selection list for this {@link Parameter}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    protected String getSelectionAttribute(String parameterID) throws OntologyErrorException
    {
        Validate.notNull(parameterID);
        String result = null;

        RecordSet selectionAttributeRecordset = engine.getSelectionAttribute(parameterID);

        if (selectionAttributeRecordset.getNoOfRecords() > 0)
        {
            Hashtable<String, String> ruleRecord = selectionAttributeRecordset.getRecords()[0];

            String ruleName = ruleRecord.get(ProcessEngineConstants.Variables.Common.RULE_NAME);
            if (ruleName != null && !ruleName.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
            {
                result = ruleName;
                return result;
            }
        }

        /**
         * The Auswahltattribute slot is not defined for this PROCESSINFORMATION_CHART, <br/>
         * so let us try to get the value of the Auswahlattribute slot from the InformationType<br/>
         * of this PROCESSINFORMATION_CHART.
         */

        if (result == null)
        {
            RecordSet selectionAttributeFromInfoTypeRecordset = engine.getSelectionAttributeFromInfoType(parameterID);
            if (selectionAttributeFromInfoTypeRecordset.getNoOfRecords() > 0)
            {
                Hashtable<String, String> ruleRecord = selectionAttributeFromInfoTypeRecordset.getRecords()[0];
                String ruleNameInformationType = ruleRecord.get(ProcessEngineConstants.Variables.Common.RULE_NAME);
                if (ruleNameInformationType != null && !ruleNameInformationType.equals(ProcessEngineConstants.Variables.Common.ZERO))
                {
                    result = ruleNameInformationType;
                }
            }
        }

        return result;

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#getParameterSelectList(java.lang.String, java.lang.String)
     */
    public ObjectArray getParameterSelectList(String parameterID, String infoTypeID) throws OntologyErrorException
    {
        ObjectArray selectList = new ObjectArray();

        /**
         * the possible selection list of the parameter from type Person or OrganizationalUnit is created <br/>
         * from executing the entered rule in the slot Auswahlattribut.
         */

        if (infoTypeID.equals(InformationTypesConstants.PERSONEN))
        {
            String ruleName = this.getSelectionAttribute(parameterID);
            LOG.info("SELECTIONATTRIBUTE-Rule       " + ruleName);

            /**
             * TODO : Now we know the name of the rule to be executed <br/>
             * 1. Execute the rule, generate the selection list and returns it.
             */
            selectList = this.getUsers(ruleName);
        }
        else if (infoTypeID.equals(InformationTypesConstants.ORGANIZATIONALUNIT))
        {
            String ruleName = this.getSelectionAttribute(parameterID);
            LOG.info("SELECTIONATTRIBUTE-Rule       " + ruleName);

            /**
             * TODO : Now we know the name of the rule to be executed <br/>
             * 1. Execute the rule, generate the selection list and returns it.
             */
            selectList = this.getOrganisations(ruleName);
        }
        else if (infoTypeID.equals(InformationTypesConstants.COMBOBOX_CONTROL_FLOW))
        {
            String ruleName = this.getSelectionAttribute(parameterID);
            LOG.info("SELECTIONATTRIBUTE-RuleControlFluss       " + ruleName);
            // TODO get the id of the activity where the parameter is defined
            String frameID = engine.getFrameToProcessInformation(parameterID);
            LOG.info("frameID*****************************       " + frameID);
            if (engine.isProduct(frameID))
            {
                String activityID = engine.getFrameToOutputProduct(frameID);
                LOG.info("SELECTIONATTRIBUTE-RuleControlFluss     FRAME ID " + frameID + "    ACTIVITY_ID    " + activityID);
                selectList = this.getControlFlows(ruleName, activityID);
                Iterator<Object> itSl = selectList.iterator();
                while (itSl.hasNext())
                {
                    ControlFlow ss = (ControlFlow) itSl.next();
                    LOG.debug("controlfluss  " + ss.getID() + "   " + ss.getName());
                }
            }
            else
            {
                selectList = this.getControlFlows(ruleName, frameID);
            }

        }
        /* Gets the entries from the ontology for contentType != OE | Person * */
        else
        {
            RecordSet records = engine.getParameterSelectList(parameterID);

            if (records.getNoOfRecords() > 0)
            {
                for (int i = 0; i < records.getNoOfRecords(); i++)
                {
                    Hashtable<String, String> selectListRecord = records.getRecords()[i];
                    String selectItem = selectListRecord.get(ProcessEngineConstants.Variables.Common.PARAMETER_SELECT_LIST);
                    selectList.add(selectItem);
                }
            }
        }

        return selectList;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#getParameterValue(java.lang.String)
     */
    public ObjectArray getParameterValue(String parameterID) throws OntologyErrorException
    {

        ObjectArray values = new ObjectArray();
        RecordSet records = engine.getParameterValue(parameterID);

        if (records.getNoOfRecords() > 0)
        {
            /** fetch the parameters data from the storage. */
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {

                ParameterValue paramValue = new ParameterValue();
                Hashtable<String, String> selectListRecord = records.getRecords()[i];

                String relationID = selectListRecord.get(ProcessEngineConstants.Variables.Common.PARAMETER_RELATION_ID);
                String relationContentID = selectListRecord.get(ProcessEngineConstants.Variables.Common.PARAMETER_RELATION_CONTENT_ID);
                String aValue = selectListRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_CONTENT_ID);
                paramValue.relationID = relationID;
                paramValue.value = aValue;
                paramValue.relationContentID = relationContentID;

                /**
                 * The Inhalt_Relation differs from the other Inhalt_xx because it is just become an id of a frame in <br/>
                 * in the ontology. That is why we make the difference by setting the values.
                 */

                if (paramValue.value != null)
                {
                    values.add(paramValue);
                }

            }
        }

        return values;
    }

    /**
     * Gets the selected values of a {@link Parameter}, wich is not a Relation (the values are not readed from the field Inhalt_Relation).
     * 
     * @param parameterID the id of {@link Parameter}
     * @return not null {@link ObjectArray}. If no items exists, an emty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    protected ObjectArray getParameterScalarValue(String parameterID) throws OntologyErrorException
    {

        RecordSet records = engine.getParameterScalarValue(parameterID);
        ObjectArray values = new ObjectArray();
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                ParameterValue paramValue = new ParameterValue();
                Hashtable<String, String> selectListRecord = records.getRecords()[i];
                Object aValue = selectListRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_CONTENT);
                paramValue.relationID = selectListRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_NAME);
                paramValue.value = aValue;
                /** base types dont have a relation id. */
                paramValue.relationContentID = ProcessEngineConstants.Variables.Common.EMPTY;
                values.add(paramValue);
            }
        }
        return values;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertShortText(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertShortText(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.SHORTTEXT))
            {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertLongText(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertLongText(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.LONGTEXT))
            {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertFloat(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertFloat(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.FLOAT))
            {
                Float floatNumber = null;
                if ( !value.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    try
                    {
                        floatNumber = new Float(value);
                        result.add(floatNumber);
                    }
                    catch (NumberFormatException e)
                    {
                        LOG.error("Bad Float Format. ", e);
                    }
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertPersonen(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertPersonen(ObjectArray values) throws OntologyErrorException
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String userID = (String) paramValue.value;
            Person person = organizationEntity.getPerson(userID);
            result.add(person);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertOrganizationalUnit(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertOrganizationalUnit(ObjectArray values) throws OntologyErrorException
    {

        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;
        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String orgID = (String) paramValue.value;
            Organization organisation = organizationEntity.getOrganization(orgID);
            if (organisation != null)
            {
                result.add(organisation);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertInteger(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertInteger(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;
        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.INTEGER))
            {

                Integer numberValue = null;
                try
                {
                    numberValue = new Integer(value);
                }
                catch (NumberFormatException e)
                {
                    numberValue = null;
                }
                result.add(numberValue);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertSingleList(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertSingleList(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.SINGLELIST))
            {
                result.add(value);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertMultiList(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertMultiList(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.MULTILIST))
            {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertComboBox(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertComboBox(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.COMBOBOX))
            {
                if (value != null)
                {
                    result.add(value);
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertDocument(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertDocument(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.DOCUMENT))
            {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertDate(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertDate(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.DATE) && !value.equals(ProcessEngineConstants.Variables.Common.EMPTY))
            {
                Long dateAsLong = new Long(StringConverter.datestr2Long(value));
                DateTime dateTime = new DateTime(dateAsLong);
                @SuppressWarnings("unused")
                DateMidnight dateMidnight = dateTime.toDateMidnight();
                // result.add(dateMidnight.toString());
                result.add(dateAsLong.toString());
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertTimeStamp(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertTimeStamp(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.DATE))
            {
                Long dateAsLong = new Long(value);
                DateTime dateTime = new DateTime(dateAsLong);
                DateMidnight dateMidnight = dateTime.toDateMidnight();
                result.add(dateMidnight.toString());
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertLink(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertLink(ObjectArray values)
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String value = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.LINK))
            {
                result.add(value);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertComboBoxControlFlow(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertComboBoxControlFlow(ObjectArray values) throws OntologyErrorException
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String controlFlowID = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.COMBOBOX_CONTROL_FLOW))
            {
                if (controlFlowID != null)
                {
                    ControlFlow controlFlow = controlFlowEntity.getControlFlow(controlFlowID);
                    result.add(controlFlow);
                }
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#convertMultiListControlFlow(org.prowim.datamodel.collections.ObjectArray)
     */
    public ObjectArray convertMultiListControlFlow(ObjectArray values) throws OntologyErrorException
    {
        ObjectArray result = new ObjectArray();
        ObjectArray paramValues = values;

        Iterator<Object> it = paramValues.iterator();
        while (it.hasNext())
        {
            ParameterValue paramValue = (ParameterValue) it.next();
            String relationID = paramValue.relationID;
            String controlFlowID = (String) paramValue.value;

            if (relationID.equals(InformationTypesConstants.COMBOBOX_CONTROL_FLOW))
            {
                if (controlFlowID != null)
                {
                    ControlFlow controlFlow = controlFlowEntity.getControlFlow(controlFlowID);
                    result.add(controlFlow);
                }
            }
        }

        return result;
    }

    /**
     * Gets the registered users.
     * 
     * @param ruleName the name of the rule that returns a list of users.
     * 
     * @return a not null list of {@link Person}. An empty list object is returned if no items exist.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private ObjectArray getUsers(String ruleName) throws OntologyErrorException
    {
        ObjectArray users = new ObjectArray();
        RecordSet result = engine.executeSelectionAttributeRule(ruleName);
        if (result.getNoOfRecords() > 0)
        {

            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];

                String id = record.get(ProcessEngineConstants.Variables.Common.ID);
                Person person = organizationEntity.getPerson(id);
                if (person != null)
                {
                    users.add(person);
                }
            }

        }
        return users;
    }

    /**
     * Executes a rule that gets all organisations registered.
     * 
     * @param ruleName the rulename to be executed.
     * @return a not null list of {@link Organisation} objects. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private ObjectArray getOrganisations(String ruleName) throws OntologyErrorException
    {
        RecordSet organisationsRecords = engine.executeSelectionAttributeRule(ruleName);
        ObjectArray organisationList = new ObjectArray();
        if (organisationsRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < organisationsRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> organisationsRecord = organisationsRecords.getRecords()[i];
                String id = organisationsRecord.get(ProcessEngineConstants.Variables.Common.ID);
                Organization organisation = organizationEntity.getOrganization(id);
                if (organisation != null)
                {
                    organisationList.add(organisation);
                }
            }
        }
        return organisationList;
    }

    private ObjectArray getControlFlows(String ruleName, String activityID) throws OntologyErrorException
    {
        RecordSet controlFlowRecords = engine.executeSelectionAttributeRuleForControlFlow(ruleName, activityID);
        ObjectArray controlFlowList = new ObjectArray();
        if (controlFlowRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < controlFlowRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> controlFlowRecord = controlFlowRecords.getRecords()[i];
                // fetch the ControlFlow data
                String id = controlFlowRecord.get(ProcessEngineConstants.Variables.Common.ID);
                ControlFlow controlFlow = controlFlowEntity.getControlFlow(id);
                controlFlow.setActivityID(activityID);
                controlFlowList.add(controlFlow);
            }
        }
        return controlFlowList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#getConstraint(java.lang.String, java.lang.String)
     */
    public ParameterConstraint getConstraint(String parameterID, String infoType) throws OntologyErrorException
    {
        Validate.notNull(parameterID);
        ParameterConstraint result = null;
        final RecordSet constraintRecords = engine.getParameterConstraint(parameterID);
        if (constraintRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < constraintRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> constraintRecord = constraintRecords.getRecords()[i];

                String min = constraintRecord.get(ProcessEngineConstants.Variables.Common.InformationType.MIN_VALUE);
                String max = constraintRecord.get(ProcessEngineConstants.Variables.Common.InformationType.MAX_VALUE);
                String required = constraintRecord.get(ProcessEngineConstants.Variables.Common.REQUIRED);

                Long minValue = new Long(0);
                Long maxValue = new Long(0);
                if (infoType.equals(InformationTypesConstants.DATE))
                {
                    minValue = StringConverter.datestr2Long(min);
                    maxValue = StringConverter.datestr2Long(max);
                }
                else if (infoType.equals(InformationTypesConstants.TIMESTAMP))
                {
                    minValue = StringConverter.timestampstr2Long(min);
                    maxValue = StringConverter.timestampstr2Long(max);
                }
                else
                {
                    minValue = StringConverter.strToLong(min);
                    maxValue = StringConverter.strToLong(max);
                }
                boolean requiredValue = StringConverter.strToBool(required);

                result = DefaultDataObjectFactory.createParameterConstraint(minValue, maxValue, requiredValue);
            }
        }
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ParameterHelper#getParameterValue(java.lang.String, java.lang.String)
     */
    public ObjectArray getParameterValue(String contentType, String paramID) throws OntologyErrorException
    {
        ObjectArray value = new ObjectArray();
        if (contentType.equals(InformationTypesConstants.ContentSlots.CONTENT_RELATION))
        {
            value = this.getParameterValue(paramID);
        }
        else if (contentType.equals(InformationTypesConstants.ContentSlots.CONTENT_FLOAT))
        {
            value = this.getParameterScalarValue(paramID);
        }
        else if (contentType.equals(InformationTypesConstants.ContentSlots.CONTENT_STRING))
        {
            value = this.getParameterScalarValue(paramID);
        }
        else if (contentType.equals(InformationTypesConstants.ContentSlots.CONTENT_INTEGER))
        {
            value = this.getParameterScalarValue(paramID);
        }
        else if (contentType.equals(InformationTypesConstants.ContentSlots.CONTENT_LIST))
        {
            value = this.getParameterScalarValue(paramID);
        }
        else
            throw new IllegalArgumentException("Unknown information content type: " + contentType + " paramID   " + paramID);
        return value;
    }

    /**
     * Inner class used to store Parameter values.
     * 
     * @author Saad Wardi
     * @version $Revision$
     */
    final class ParameterValue
    {
        /** the relation id. the id of a Relation like Person, OE , etc */
        private String relationID;
        /** the parameter value. the value of the parameter. */
        private Object value;
        /** the relation content id. the id of the content place like (Inhalt_String, Inhalt_Relation , etc) */
        private String relationContentID;

        private ParameterValue()
        {

        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "relationID:   " + relationID + "    relationContentID:   " + relationContentID + "     value:    " + value;
        }
    }

}
