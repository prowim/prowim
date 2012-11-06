/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 05.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.WorkFlowHelper;
import org.prowim.services.coreengine.WorkflowEngine;
import org.prowim.services.coreengine.EngineConstants.Variables.Process;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the methods of {@link WorkflowEngine}.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
@Stateless
public class ProWimWorkflowEngine implements WorkflowEngine
{
    private final static Logger LOG = Logger.getLogger(ProWimWorkflowEngine.class);

    @IgnoreDependency
    @EJB
    private ProcessHelper       processHelper;

    @IgnoreDependency
    @EJB
    private CommonHelper        common;

    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    @IgnoreDependency
    @EJB
    private WorkFlowHelper      workFlowHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createWorkflow(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet createWorkflow(String templateID, String userID) throws OntologyErrorException
    {
        String result = AlgernonConstants.FAILED;
        String processInstanceID = null;
        final RecordSet processInstanceRecords = createProcessInstance(templateID, common.getName(templateID));
        result = processInstanceRecords.getResult();
        if (result.equals(AlgernonConstants.OK))
        {
            if (processInstanceRecords.getNoOfRecords() > 0)
            {
                Hashtable<String, String> pinsRecord = processInstanceRecords.getRecords()[0];
                processInstanceID = pinsRecord.get(Process.PROCESS_INST_ID);
                result = processInstanceID;

                final RecordSet elementsInstanceRecords = createInstanceElements(processInstanceID);
                result = elementsInstanceRecords.getResult();

                if (result.equals(AlgernonConstants.OK))
                {
                    final RecordSet instanceRelationsGlobalRecords = createProcessInstanceRelationsGlobal(processInstanceID);
                    result = instanceRelationsGlobalRecords.getResult();
                    if (result.equals(AlgernonConstants.OK))
                    {
                        final RecordSet instanceRelationsLocalRecords = createProcessInstanceRelationsLocal(processInstanceID);
                        result = instanceRelationsLocalRecords.getResult();

                        if (result.equals(AlgernonConstants.OK))
                        {
                            final RecordSet processRelationsRecords = createProcessRelations(processInstanceID);
                            result = processRelationsRecords.getResult();
                            if (result.equals(AlgernonConstants.OK))
                            {
                                final RecordSet processStarterRecords = setProcessStarter(processInstanceID, userID);
                                result = processStarterRecords.getResult();
                                if (result.equals(AlgernonConstants.OK))
                                {
                                    if ( !processHelper.isSubProcess(processInstanceID))
                                        activateStartActivityOfProcess(processInstanceID);

                                    // Create subprocess of given process
                                    createSubprocWorkflow(processInstanceID, userID);
                                }
                                else
                                {
                                    LOG.error("Could not set process starter to userID " + userID + " for process instance " + processInstanceID
                                            + " when creating a process instance for template " + templateID);
                                    workFlowHelper.rollBack();
                                }
                            }
                            else
                            {
                                LOG.error("Could not create process relations for process instance " + processInstanceID
                                        + " when creating a process instance for template " + templateID);
                                workFlowHelper.rollBack();
                            }
                        }
                        else
                        {
                            LOG.error("Could not create process relations local for process instance " + processInstanceID
                                    + " when creating a process instance for template " + templateID);
                            workFlowHelper.rollBack();
                        }
                    }
                    else
                    {
                        LOG.error("Could not create process relations global for process instance " + processInstanceID
                                + " when creating a process instance for template " + templateID);
                        workFlowHelper.rollBack();
                    }
                }
                else
                {
                    LOG.error("Could not create instance elements for process instance " + processInstanceID
                            + " when creating a process instance for template " + templateID);
                    workFlowHelper.rollBack();
                }
            }
            else
            {
                LOG.error("Could not create process instance when creating a process instance for template " + templateID);
                workFlowHelper.rollBack();
            }
        }
        else
        {
            workFlowHelper.rollBack();
        }

        this.assignRolesByRuleExecution(processInstanceID);

        // TODO get all roles instances and set them local
        return processInstanceRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#clearTemplateSlots(java.lang.String)
     */
    @Override
    public void clearTemplateSlots(String processID) throws OntologyErrorException
    {
        ObjectArray allElements = common.getProcessElements();
        for (Object elementClass : allElements)
        {
            LOG.debug("Checking element class " + elementClass);
            if ( !elementClass.toString().equals(EngineConstants.Consts.ProcessElementTypes.PROCESS_INFORMATION_CLASS))
            {
                ObjectArray elements = common.getProcessElementsInstances(processID, elementClass.toString());
                for (Object element : elements)
                {
                    LOG.debug("Clearing element " + element);
                    ProcessElement processElement = (ProcessElement) element;
                    common.clearRelationValue(processElement.getID(), EngineConstants.Slots.FROM_TEMPLATE);
                    common.clearRelationValue(processElement.getID(), EngineConstants.Slots.HAS_TEMPLATE);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createWorkflow(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet cloneWorkflow(String templateID, String userID) throws OntologyErrorException
    {
        Validate.notNull(templateID);

        LOG.debug("Cloning process: " + templateID + " for user ID: " + userID);
        String result = AlgernonConstants.ERROR;
        String processInstanceID = null;
        final RecordSet processInstanceRecords = cloneProcessTemplate(templateID, common.getName(templateID));
        result = processInstanceRecords.getResult();
        if ( !result.equals(AlgernonConstants.ERROR))
        {
            if (processInstanceRecords.getNoOfRecords() > 0)
            {
                Hashtable<String, String> pinsRecord = processInstanceRecords.getRecords()[0];
                processInstanceID = pinsRecord.get(Process.PROCESS_INST_ID);

                // set the DENOTATION field
                common.setDenotation(processInstanceID);
                result = processInstanceID;

                final RecordSet elementsInstanceRecords = createProcessElementsForCloning(processInstanceID);

                result = elementsInstanceRecords.getResult();

                if ( !result.equals(AlgernonConstants.ERROR))
                {
                    // Instance elements created.
                    final RecordSet instanceRelationsGlobalRecords = createProcessInstanceRelationsGlobal(processInstanceID);
                    result = instanceRelationsGlobalRecords.getResult();
                    if ( !result.equals(AlgernonConstants.ERROR))
                    {
                        // Global process instance relations created.
                        final RecordSet instanceRelationsLocalRecords = createProcessInstanceRelationsLocal(processInstanceID);
                        result = instanceRelationsLocalRecords.getResult();

                        if ( !result.equals(AlgernonConstants.ERROR))
                        {
                            // Local process instance relations created.
                            final RecordSet processRelationsRecords = createProcessRelations(processInstanceID);
                            result = processRelationsRecords.getResult();
                            if ( !result.equals(AlgernonConstants.ERROR))
                            {
                                final RecordSet statusValues = setRelationOfGlobalElementsToProcess(processInstanceID);
                                result = statusValues.getResult();
                                if ( !result.equals(AlgernonConstants.ERROR))
                                {
                                    // Process relations created
                                    if (userID != null)
                                    {
                                        final RecordSet processStarterRecords = setProcessStarter(processInstanceID, userID);
                                        result = processStarterRecords.getResult();
                                        if ( !result.equals(AlgernonConstants.ERROR))
                                        {
                                            if ( !processHelper.isSubProcess(processInstanceID))
                                                activateStartActivityOfProcess(processInstanceID);

                                            // Create subprocess of given process
                                            createSubprocWorkflow(processInstanceID, userID);
                                        }
                                    }
                                    else
                                    {
                                        LOG.warn("No user ID given for process instance " + processInstanceID
                                                + " when cloning the process template with ID " + templateID);
                                    }
                                }
                                else
                                {
                                    LOG.error("Could not set the relation between global process elements and the process " + processInstanceID
                                            + " when creating a process instance for template " + templateID);
                                    workFlowHelper.rollBack();
                                }
                            }
                            else
                            {
                                LOG.error("Could not create process relations for process instance " + processInstanceID
                                        + " when creating a process instance for template " + templateID);
                                workFlowHelper.rollBack();
                            }
                        }
                        else
                        {
                            LOG.error("Could not create process relations local for process instance " + processInstanceID
                                    + " when creating a process instance for template " + templateID);
                            workFlowHelper.rollBack();
                        }
                    }
                    else
                    {
                        LOG.error("Could not create process relations global for process instance " + processInstanceID
                                + " when creating a process instance for template " + templateID);
                        workFlowHelper.rollBack();
                    }
                }
                else
                {
                    LOG.error("Could not find instance elements for process instance " + processInstanceID
                            + " when creating a process instance for template " + templateID + ". Skipping.");
                }
            }
            else
            {
                LOG.error("Could not create process instance when creating a process instance for template " + templateID);
                workFlowHelper.rollBack();
            }
        }
        else
        {
            LOG.error("Could not clone process template for id " + templateID);
            workFlowHelper.rollBack();
        }

        this.assignRolesByRuleExecution(processInstanceID);

        return processInstanceRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createProcessInstance(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet createProcessInstance(String templateID, String processName) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> mainHash = new Hashtable<String, AlgernonValue>();
        mainHash.put(ProcessEngineConstants.Variables.Common.TEMPLATE_ID, new AlgernonValue(templateID, true));
        mainHash.put(ProcessEngineConstants.Variables.Common.NAME, new AlgernonValue(processName, true));
        final RecordSet result = myService
                .executeRule(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_INSTANCE, mainHash, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_INSTANCE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#cloneProcessTemplate(String, String)
     */
    @Override
    public RecordSet cloneProcessTemplate(String templateID, String processName) throws OntologyErrorException
    {
        Validate.notNull(templateID);
        Validate.notNull(processName);

        final Hashtable<String, AlgernonValue> mainHash = new Hashtable<String, AlgernonValue>();
        mainHash.put(ProcessEngineConstants.Variables.Common.TEMPLATE_ID, new AlgernonValue(templateID, true));
        mainHash.put(ProcessEngineConstants.Variables.Common.NAME, new AlgernonValue(processName, true));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.CLONE_PROCESS_TEMPLATE, mainHash, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CLONE_PROCESS_TEMPLATE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createInstanceElements(java.lang.String)
     */
    @Override
    public RecordSet createInstanceElements(String processInstanceID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> pinsHash = new Hashtable<String, AlgernonValue>();
        pinsHash.put(Process.PROCESS_INST_ID, new AlgernonValue(processInstanceID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_INSTANCE_ELEMENTS, pinsHash,
                                                 AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_INSTANCE_ELEMENTS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createProcessElementsForCloning(java.lang.String)
     */
    @Override
    public RecordSet createProcessElementsForCloning(String processInstanceID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> pinsHash = new Hashtable<String, AlgernonValue>();
        pinsHash.put(Process.PROCESS_INST_ID, new AlgernonValue(processInstanceID, true));
        RecordSet result = myService.executeRule(EngineConstants.Rules.Workflow.CREATE_PROCESS_ELEMENTS_FOR_CLONING, pinsHash, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.CREATE_PROCESS_ELEMENTS_FOR_CLONING, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createInstanceElementReferences(String, String)
     */
    @Override
    public RecordSet createInstanceElementReferences(String processInstanceID, String targetID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> pinsHash = new Hashtable<String, AlgernonValue>();
        pinsHash.put(Process.PROCESS_INST_ID, new AlgernonValue(processInstanceID, true));
        pinsHash.put(Process.TARGET_PROCESS_ID, new AlgernonValue(targetID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_INSTANCE_ELEMENT_REFERENCES, pinsHash,
                                                 AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_INSTANCE_ELEMENT_REFERENCES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createProcessInstanceRelationsGlobal(java.lang.String)
     */
    @Override
    public RecordSet createProcessInstanceRelationsGlobal(String processInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> pinsHash = new Hashtable<String, AlgernonValue>();
        pinsHash.put(ProcessEngineConstants.Variables.Process.PROCESS_INSTANCE_ID, new AlgernonValue(processInstanceID, true));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.CREATE_GLOBAL_PROC_REL_AND_ATTR, pinsHash,
                                                       AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CREATE_GLOBAL_PROC_REL_AND_ATTR, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createProcessInstanceRelationsLocal(java.lang.String)
     */
    @Override
    public RecordSet createProcessInstanceRelationsLocal(String processInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> pinsHash = new Hashtable<String, AlgernonValue>();
        pinsHash.put(ProcessEngineConstants.Variables.Process.PROCESS_INSTANCE_ID, new AlgernonValue(processInstanceID, true));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.CREATE_LOCAL_PROC_RELATIONS, pinsHash,
                                                       AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CREATE_LOCAL_PROC_RELATIONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#createProcessRelations(java.lang.String)
     */
    @Override
    public RecordSet createProcessRelations(String processInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> pinsHash = new Hashtable<String, AlgernonValue>();
        pinsHash.put(ProcessEngineConstants.Variables.Process.PROCESS_INSTANCE_ID, new AlgernonValue(processInstanceID, true));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_RELATIONS,
                                                       ProcessEngineConstants.Variables.Process.PROCESS_INSTANCE_ID, processInstanceID, null, null);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_RELATIONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#setRelationOfGlobalElementsToProcess(java.lang.String)
     */
    @Override
    public RecordSet setRelationOfGlobalElementsToProcess(String processInstanceID) throws OntologyErrorException
    {

        final Hashtable<String, AlgernonValue> pinsHash = new Hashtable<String, AlgernonValue>();
        pinsHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processInstanceID, true));
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Workflow.SET_RELATION_OF_GLOBAL_ELOEMENTS_TO_PROCESS, pinsHash,
                                                       AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.SET_RELATION_OF_GLOBAL_ELOEMENTS_TO_PROCESS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#setProcessStarter(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setProcessStarter(String processInstanceID, String userID) throws OntologyErrorException
    {
        final Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, userID);
        parameters.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, processInstanceID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.SET_PROCESS_STARTER, parameters, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.SET_PROCESS_STARTER, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#activateStartActivityOfProcess(java.lang.String)
     */
    @Override
    public RecordSet activateStartActivityOfProcess(String processInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(ProcessEngineConstants.Variables.Process.PROCESS_INSTANCE_ID, processInstanceID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.ACTIVATE_START_ACTIVITY, parameters,
                                                       AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.ACTIVATE_START_ACTIVITY, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#assignRolesByRuleExecution(java.lang.String)
     */
    @Override
    public void assignRolesByRuleExecution(String processInstanceID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processInstanceID, true));
        RecordSet roles = myService.executeRule(EngineConstants.Rules.Role.GET_ASSIGNMENT_ROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Role.GET_ASSIGNMENT_ROLES, roles, this.getClass());
        if (roles != null && roles.getNoOfRecords() > 0)
        {
            for (int i = 0; i < roles.getNoOfRecords(); i++)
            {
                /**
                 * 1. Execute the ProcessEngineConstants.Rules.Role.GET_ASSIGENEMT_RULE <br>
                 * to get the defined roles for this process.
                 */
                Hashtable<String, String> role = roles.getRecords()[i];
                String roleID = role.get(EngineConstants.Variables.Role.ROLE_ID);
                String ruleName = role.get(EngineConstants.Variables.Role.ASSIGNMENT_RULE);

                /** 2. Execute the rule defined for this role to assign it. */
                if ( !ruleName.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    tab = new Hashtable<String, AlgernonValue>();
                    tab.put(ProcessEngineConstants.Variables.Common.INS, new AlgernonValue(processInstanceID, true));
                    tab.put(ProcessEngineConstants.Variables.Role.NAME_LARGE, new AlgernonValue(roleID, true));
                    myService.executeRule(ruleName, tab, AlgernonConstants.TELL);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#checkForAutomatics()
     */
    @Override
    public void checkForAutomatics() throws OntologyErrorException
    {
        final RecordSet result = this.getAutoActivities();
        if (result.getNoOfRecords() > 0)
        {
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> record = result.getRecords()[i];
                final String instance = record.get(EngineConstants.Variables.Activity.ACTIVITY_ID);
                final String verweis = record.get(EngineConstants.Variables.Activity.REFERENCE);

                if ( !verweis.equals(EngineConstants.Variables.Common.TIMER))
                {
                    this.setActivityStatus(instance, ProcessEngineConstants.Variables.Activity.STATUS_DONE);
                }
            }
        }
    }

    /**
     * 
     * Get all activities, which runs automatically .
     * 
     * @return List of activities
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private RecordSet getAutoActivities() throws OntologyErrorException
    {
        final Hashtable<String, String> tab = new Hashtable<String, String>();
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Workflow.GET_ACTIVE_AUTO_ACTIVITIES, tab, AlgernonConstants.ASK);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#setActivityStatus(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean setActivityStatus(String activityID, String status, String userID) throws OntologyErrorException
    {
        int done = 0;
        boolean noerror = true;
        // Get eneablType "freigabetype", who can set the activity finished
        String enableTyp = EngineConstants.Variables.Activity.ENABLE_EVERYBODY;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(activityID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_ENABLE_TYPE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_ENABLE_TYPE, result, this.getClass());

        if (result.getNoOfRecords() > 0)
        {
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                enableTyp = record.get(ProcessEngineConstants.Variables.Common.TYPE);
            }
        }
        else
        {
            LOG.info("Enable type not defined:    " + enableTyp);
            enableTyp = EngineConstants.Variables.Activity.ENABLE_NOBODY;
            noerror = false;

        }

        // TODO DAs sollte klar gestellt werden, ob diese abfrage hier überhaupt sinn macht oder ob man immer in if für ENABLE_EVERYBODY reinspringt.
        if (enableTyp.equalsIgnoreCase(EngineConstants.Variables.Activity.ENABLE_ALL))
        {
            done = workFlowHelper.applyAndSetRoles(activityID, userID);
        }
        else if (enableTyp.equalsIgnoreCase(EngineConstants.Variables.Activity.ENABLE_EVERYBODY))
        {
            done = 1;
        }
        else if (enableTyp.equalsIgnoreCase(EngineConstants.Variables.Activity.ENABLE_ASSIGNEDPERSON))
        {
            done = workFlowHelper.applyForAssignedPersons(activityID, userID);
        }
        else
        {
            throw new IllegalStateException("Enable type is not supported " + enableTyp);
        }

        // If all with enabletype is OK, than set the status of activity
        if (done == 1)
        {
            this.setActivityStatus(activityID, status);
        }
        if (done < 0)
        {
            noerror = false;
        }

        return noerror;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#setActivityStatus(java.lang.String, java.lang.String)
     */
    @Override
    public void setActivityStatus(String activityID, String status) throws OntologyErrorException
    {
        String isSubProcess = null;
        String subProcessID = null;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();

        // Set the status of given activity
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, new AlgernonValue(activityID, false));
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_STATUS, new AlgernonValue(status, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.SET_ACTIVITY_STATE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.SET_ACTIVITY_STATE, result, this.getClass());

        // remain, if this activity the last in a subprocess.
        if (result != null && result.getNoOfRecords() > 0)
        {
            Hashtable<String, String> tmpSub = result.getRecords()[0];
            isSubProcess = tmpSub.get(ProcessEngineConstants.Variables.Activity.DEPENDENCY);
            subProcessID = tmpSub.get(ProcessEngineConstants.Variables.Common.ID);
        }

        // Check, if status is of finish "fertig", than check for done rules and execute it
        if (status.equals(EngineConstants.Variables.Activity.STATUS_DONE))
        {
            tab.put(EngineConstants.Variables.Common.INSTANCE_ID, new AlgernonValue(activityID, false));
            RecordSet doneRules = myService.executeRule(EngineConstants.Rules.Workflow.GET_DONE_RULE, tab, AlgernonConstants.ASK);
            if (doneRules.getNoOfRecords() != 0)
            {
                Hashtable<String, String> record = doneRules.getRecords()[0];
                String regel = record.get(EngineConstants.Variables.Common.DONE_RULE_ID);
                if (regel != null && !regel.equals(EngineConstants.Variables.Common.EMPTY))
                {
                    Hashtable<String, AlgernonValue> tabinstance = new Hashtable<String, AlgernonValue>();
                    tabinstance.put(ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, new AlgernonValue(activityID, false));
                    myService.executeActivityRule(regel, tabinstance);
                }
            }

            // Clears the value of the slot "fertiggesetzt von".
            Hashtable<String, AlgernonValue> tabl = new Hashtable<String, AlgernonValue>();
            tabl.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
            result = myService.executeRule(EngineConstants.Rules.Activity.CLREAR_DONE_FROM_REL_OF_ACTIVIY, tabl, AlgernonConstants.ASK);
            LoggingUtility.logResult(EngineConstants.Rules.Activity.CLREAR_DONE_FROM_REL_OF_ACTIVIY, result, this.getClass());

            // See if the activity has an activation rule if no activation rule is defined use the standard rule.
            Hashtable<String, AlgernonValue> tab2 = new Hashtable<String, AlgernonValue>();
            tab2.put(EngineConstants.Variables.Common.INSTANCE_ID, new AlgernonValue(activityID, false));
            result = myService.executeRule(EngineConstants.Rules.Workflow.GET_ACTIVATE_RULE, tab2, AlgernonConstants.ASK);
            if (result.getNoOfRecords() == 0)
            {
                result = myService.executeRule(EngineConstants.Rules.Workflow.DEFAULT_ACTIVATE_RULE, EngineConstants.Variables.Activity.ACTIVITY_ID,
                                               activityID, null, null);
                LOG.debug("Standard rule " + EngineConstants.Rules.Workflow.DEFAULT_ACTIVATE_RULE);
                LoggingUtility.logResult(EngineConstants.Rules.Workflow.DEFAULT_ACTIVATE_RULE, result, this.getClass());
            }
            // An activation rule is defined, execute it !.
            else
            {
                Hashtable<String, String> record = result.getRecords()[0];
                String regel = record.get(EngineConstants.Variables.Activity.ACTIVATE_RULE);
                result = myService.executeBusinessRule(regel, ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, activityID, null, null);
                LOG.debug("activation rule  " + regel + " will be executed");
                LoggingUtility.logResult(EngineConstants.Variables.Activity.ACTIVATE_RULE, result, this.getClass());
            }

            if (result.getNoOfRecords() > 0)
            {
                // Handle ControlFlows.
                int size = result.getNoOfRecords();
                for (int i = 0; i < size; i++)
                {
                    Hashtable<String, String> controlFlowRecord = result.getRecords()[i];
                    String controlFlow = controlFlowRecord.get(EngineConstants.Variables.Controlflow.CONTROLFLOW_ID);
                    setControlFlow(controlFlow);
                }
            }
        }

        // check, if this is the last activity in a subprocess. if yes: set the state for the parent
        if (isSubProcess.equals(EngineConstants.Variables.Common.ONE))
        {
            Hashtable<String, String> tabCaller = new Hashtable<String, String>();
            tabCaller.put(EngineConstants.Variables.Process.SUB_PROCESS_ID, subProcessID);
            RecordSet callerResult = myService.executeRule(EngineConstants.Rules.SubProcess.GET_SUB_PROCESS_CALLER, tabCaller, AlgernonConstants.ASK);
            LoggingUtility.logResult(EngineConstants.Rules.SubProcess.GET_SUB_PROCESS_CALLER, callerResult, this.getClass());
            if (callerResult != null && callerResult.getNoOfRecords() > 0)
            {
                setActivityStatus(callerResult.getRecords()[0].get(EngineConstants.Variables.Activity.ACTIVITY_ID), status);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#setHasSubModelAttr(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setHasSubModelAttr(String subProcessID, String activityID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(subProcessID, false));
        processInstanceHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        final RecordSet subprocessRecords = myService.executeRule(EngineConstants.Rules.Workflow.SET_HAS_SUB_MODEL_ATTR, processInstanceHash,
                                                                  AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.SET_HAS_SUB_MODEL_ATTR, subprocessRecords, this.getClass());

        return subprocessRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#finishRestActiveActivities(java.lang.String)
     */
    @Override
    public RecordSet finishRestActiveActivities(String processID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> inputHash = new Hashtable<String, AlgernonValue>();
        inputHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processID, false));
        final RecordSet processInfoRecords = myService.executeRule(EngineConstants.Rules.Workflow.FINISH_REST_ACTIVITIES, inputHash,
                                                                   AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.FINISH_REST_ACTIVITIES, processInfoRecords, this.getClass());
        return processInfoRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#checkEndActivity(java.lang.String)
     */
    @Override
    public RecordSet checkEndActivity(String activityID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> inputHash = new Hashtable<String, AlgernonValue>();
        inputHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        final RecordSet processInfoRecords = myService.executeRule(EngineConstants.Rules.Workflow.IS_ENDACTIVITY, inputHash, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.IS_ENDACTIVITY, processInfoRecords, this.getClass());

        return processInfoRecords;
    }

    /**
     * 
     * Set the control flow and its follower active.
     * 
     * @param controlFlowID
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    private void setControlFlow(String controlFlowID) throws OntologyErrorException
    {
        // Set control flow active
        Hashtable<String, AlgernonValue> hashTable = new Hashtable<String, AlgernonValue>();
        hashTable.put(EngineConstants.Variables.Controlflow.CONTROLFLOW_ID, new AlgernonValue(controlFlowID, false));
        myService.executeRule(EngineConstants.Rules.Workflow.SET_CONTROLFLOW_ACTIVE, hashTable, AlgernonConstants.TELL);

        // Get follower of given control flow
        hashTable.clear();
        hashTable.put(EngineConstants.Variables.Controlflow.CONTROLFLOW_ID, new AlgernonValue(controlFlowID, false));
        RecordSet listOfFollower = myService.executeRule(EngineConstants.Rules.Controlflow.GET_FOLLOWER_OF_CONTROLFLOW, hashTable,
                                                         AlgernonConstants.ASK);

        for (int i = 0; i < listOfFollower.getNoOfRecords(); i++)
        {
            // Get class name and id of follower
            Hashtable<String, String> followerHash = listOfFollower.getRecords()[i];
            String followerClassName = followerHash.get(EngineConstants.Variables.Controlflow.CLASS_NAME_OF_FOLLOWER);
            String followerID = followerHash.get(EngineConstants.Variables.Controlflow.FOLLOWER_ID);

            if (followerClassName.equals(Relation.Classes.ACTIVITY))
            {
                // Set ControlFlows to inactive
                Hashtable<String, AlgernonValue> tabControlFlows = new Hashtable<String, AlgernonValue>();
                tabControlFlows.put(EngineConstants.Variables.Controlflow.CONTROLFLOW_ID, new AlgernonValue(controlFlowID, false));
                myService.executeRule(EngineConstants.Rules.Workflow.SET_CONTROLFLOW_INACTIVE, tabControlFlows, AlgernonConstants.TELL);

                // Set follow activity active
                this.setActivityStatus(followerID, EngineConstants.Variables.Common.ACTIVE);
                this.checkForAutomatics();
                // Check for a existing activating rule
                checkForActivateRule(followerID);

                // TODO Sendmail muss noch gemacht werden
                // this.processEngine.sendEmail(followerID);

                this.checkForSubProcess(followerID);

            }
            else if (followerClassName.equals(Relation.Classes.DECISION))
            {
                // Set ControlFlows to inactive
                Hashtable<String, AlgernonValue> tabControlFlows = new Hashtable<String, AlgernonValue>();
                tabControlFlows.put(EngineConstants.Variables.Controlflow.CONTROLFLOW_ID, new AlgernonValue(controlFlowID, false));
                myService.executeRule(EngineConstants.Rules.Workflow.SET_CONTROLFLOW_INACTIVE, tabControlFlows, AlgernonConstants.TELL);

                // See if the decision has an activation rule if no activation rule is defined use the standard rule.
                Hashtable<String, AlgernonValue> hashTab = new Hashtable<String, AlgernonValue>();
                hashTab.put(EngineConstants.Variables.Common.INSTANCE_ID, new AlgernonValue(followerID, false));
                RecordSet result = myService.executeRule(EngineConstants.Rules.Workflow.GET_ACTIVATE_RULE, hashTab, AlgernonConstants.ASK);

                if (result.getNoOfRecords() > 0)
                {
                    Hashtable<String, String> record = result.getRecords()[0];
                    String ruleName = record.get(EngineConstants.Variables.Activity.ACTIVATE_RULE);
                    Hashtable<String, AlgernonValue> hashTabDecID = new Hashtable<String, AlgernonValue>();
                    hashTabDecID.put(EngineConstants.Variables.Decision.DECISION_ID, new AlgernonValue(followerID, false));

                    result = myService.executeBusinessRule(ruleName, hashTabDecID);
                    LoggingUtility.logResult(EngineConstants.Variables.Activity.ACTIVATE_RULE, result, this.getClass());

                    for (int j = 0; j < result.getNoOfRecords(); j++)
                    {
                        Hashtable<String, String> controlFlowRecord = result.getRecords()[j];
                        String controlFlow = controlFlowRecord.get(EngineConstants.Variables.Common.INSTANCE_ID);
                        setControlFlow(controlFlow);
                    }
                }
                else
                    LOG.info("ERROR: No existing rule for given DECISION Shape:  " + followerID);
            }
            else if (followerClassName.equals(Relation.Classes.CONJUNCTION))
            {
                // Check for connection rule for the given conjunction.
                Hashtable<String, AlgernonValue> hashTab = new Hashtable<String, AlgernonValue>();
                hashTab.put(EngineConstants.Variables.Common.INSTANCE_ID, new AlgernonValue(followerID, false));
                RecordSet result = myService.executeRule(EngineConstants.Rules.Workflow.GET_CONNECTION_RULE, hashTab, AlgernonConstants.ASK);

                if (result.getNoOfRecords() > 0)
                {
                    Hashtable<String, String> record = result.getRecords()[0];
                    String ruleName = record.get(EngineConstants.Variables.Controlflow.CONNECTION_RULE);
                    Hashtable<String, AlgernonValue> hashTabDecID = new Hashtable<String, AlgernonValue>();
                    hashTabDecID.put(EngineConstants.Variables.Conjunction.CONJUNCTION_ID, new AlgernonValue(followerID, false));
                    result = myService.executeBusinessRule(ruleName, hashTabDecID);
                    LoggingUtility.logResult(EngineConstants.Variables.Activity.ACTIVATE_RULE, result, this.getClass());

                    for (int j = 0; j < result.getNoOfRecords(); j++)
                    {
                        Hashtable<String, String> controlFlowRecord = result.getRecords()[j];
                        String controlFlow = controlFlowRecord.get(EngineConstants.Variables.Controlflow.CONTROLFLOW_ID);
                        LOG.debug("controlFlow:  " + controlFlow);
                        setControlFlow(controlFlow);
                    }
                }
                else
                {
                    LOG.error("ERROR: No existing rule for given CONJUNCTION Shape:  " + followerID);
                }

            }
        }
    }

    /**
     * 
     * Execute activate rule if it exists "hat_Vorgangsregel"
     * 
     * @param instance
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void checkForActivateRule(String instance) throws OntologyErrorException
    {
        // Get all activate rules
        String[] rules = workFlowHelper.getInstanceAttribute(instance, Relation.Slots.HAS_RULE);
        if (rules != null && rules.length > 0)
        {
            for (int i = 0; i < rules.length; i++)
            {
                Hashtable<String, AlgernonValue> tabinstance = new Hashtable<String, AlgernonValue>();
                tabinstance.put(ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, new AlgernonValue(instance, false));
                RecordSet result = myService.executeActivityRule(rules[i], tabinstance);
                LoggingUtility.logResult(rules[i], result, this.getClass());
            }
        }
    }

    /**
     * Create workflow for sub process.
     * 
     * @param processInstanceID Id of main process
     * @param userID user id
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void createSubprocWorkflow(String processInstanceID, String userID) throws OntologyErrorException
    {
        /** 1. Get the subprocesses. */
        final RecordSet subprocessesRecords = getSubprocesses(processInstanceID);

        if (subprocessesRecords != null && subprocessesRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < subprocessesRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> subprocessesRecord = subprocessesRecords.getRecords()[i];
                String subTemplateID = subprocessesRecord.get(EngineConstants.Variables.Process.SUB_PROCESS_ID);
                String partProcessTemplateID = subprocessesRecord.get(EngineConstants.Variables.Activity.ACTIVITY_ID);

                // Create workflow of sub process
                RecordSet subProcessInstanceRecords = createWorkflow(subTemplateID, userID);

                if (subProcessInstanceRecords != null && subProcessInstanceRecords.getNoOfRecords() > 0)
                {
                    for (int j = 0; j < subProcessInstanceRecords.getNoOfRecords(); j++)
                    {
                        Hashtable<String, String> subProcessInstanceRecord = subProcessInstanceRecords.getRecords()[j];
                        String subProcessInstanceID = subProcessInstanceRecord.get(Process.PROCESS_INST_ID);

                        setHasSubModelAttr(subProcessInstanceID, partProcessTemplateID);
                        setSubprocessInput(subProcessInstanceID, partProcessTemplateID);
                        setSubprocessOutput(subProcessInstanceID, partProcessTemplateID);
                        setSubprocessRole(subProcessInstanceID, partProcessTemplateID);
                        setSubprocessElements(processInstanceID, subProcessInstanceID);
                    }
                }
            }
        }
    }

    /**
     * Get sub processes of given process.
     * 
     * @param processInstanceID ID of process instance
     * @return list of sub processes
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private RecordSet getSubprocesses(String processInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processInstanceID, false));
        final RecordSet subprocessRecords = myService.executeRule(EngineConstants.Rules.Workflow.GET_SUB_PROCESSES, processInstanceHash,
                                                                  AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.GET_SUB_PROCESSES, subprocessRecords, this.getClass());

        return subprocessRecords;
    }

    /**
     * Sets inputs of sup process instance.
     * 
     * @param subProcessInstanceID Id of of sub process
     * @param partProcessTemplateID Id of activity, which call the sub process
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void setSubprocessInput(String subProcessInstanceID, String partProcessInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(subProcessInstanceID, false));
        processInstanceHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(partProcessInstanceID, false));

        final RecordSet subprocessInputRecords = myService.executeRule(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_INPUTS, processInstanceHash,
                                                                       AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_INPUTS, subprocessInputRecords, this.getClass());
    }

    /**
     * Set the outputs of subprocess in instances.
     * 
     * @param subProcessInstanceID Id of of sub process
     * @param partProcessTemplateID Id of activity, which call the sub process
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void setSubprocessOutput(String subProcessInstanceID, String partProcessInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(subProcessInstanceID, false));
        processInstanceHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(partProcessInstanceID, false));

        final RecordSet subprocessConnectOutputRecords = myService.executeRule(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_OUTPUTS,
                                                                               processInstanceHash, AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_OUTPUTS, subprocessConnectOutputRecords, this.getClass());
    }

    /**
     * Sets the roles of created sub process.
     * 
     * @param subProcessInstanceID Id of of sub process
     * @param partProcessTemplateID Id of activity, which call the sub process
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void setSubprocessRole(String subProcessInstanceID, String partProcessInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(subProcessInstanceID, false));
        processInstanceHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(partProcessInstanceID, false));

        final RecordSet subprocessConnectRoleRecords = myService.executeRule(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_ROLES,
                                                                             processInstanceHash, AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_ROLES, subprocessConnectRoleRecords, this.getClass());
    }

    /**
     * Set all Elements of sub process as a part of main process.
     * 
     * @param processInstanceID
     * @param subProcessInstanceID
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void setSubprocessElements(String mainProcessInstanceID, String subProcessInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(mainProcessInstanceID, false));
        processInstanceHash.put(EngineConstants.Variables.Process.SUB_PROCESS_ID, new AlgernonValue(subProcessInstanceID, false));

        final RecordSet subprocessElementsRecords = myService.executeRule(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_ELEMENTS,
                                                                          processInstanceHash, AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.SET_SUB_PROCESS_ELEMENTS, subprocessElementsRecords, this.getClass());
    }

    /**
     * Check if the given activity has a sub process and if yes set this active.
     * 
     * @param activityID ID of activity
     * @throws OntologyErrorException if an error occurs in ontology back ends
     */
    private void checkForSubProcess(String activityID) throws OntologyErrorException
    {
        // Get the sub process, which calls with given activity
        Hashtable<String, AlgernonValue> tabnachfolger = new Hashtable<String, AlgernonValue>();
        tabnachfolger.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        RecordSet subProcessesRecords = myService.executeRule(EngineConstants.Rules.Activity.GET_SUB_PROCESS_OF_ACTIVIY, tabnachfolger,
                                                              AlgernonConstants.ASK);

        /** Set the state of the start activity of the subprocesses to active */
        if (subProcessesRecords != null && subProcessesRecords.getNoOfRecords() > 0)
        {
            Hashtable<String, String> subProcessesHash = subProcessesRecords.getRecords()[0];
            String subProcessID = subProcessesHash.get(EngineConstants.Variables.Process.SUB_PROCESS_ID);
            RecordSet startsActivityRecords = this.activateStartActivityOfProcess(subProcessID);

            if (startsActivityRecords != null && startsActivityRecords.getNoOfRecords() > 0)
            {
                Hashtable<String, String> startsActivityRecord = startsActivityRecords.getRecords()[0];
                String startActivity = startsActivityRecord.get(EngineConstants.Variables.Activity.ACTIVITY_ID);

                this.checkForActivateRule(startActivity);
                // TODO das senden von mail muss noch gemacht werden
                // sendEmail(firstActivity);
                // Hashtable<String, String> sendActivityTab = new Hashtable<String, String>();
                // sendActivityTab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, startActivity);
                // myService.executeRule(ProcessEngineConstants.Rules.Activity.SEND_EMAIL_FOR_ACTIVITY, sendActivityTab, AlgernonConstants.ASK);

            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#getDecisionParameter(java.lang.String)
     */
    @Override
    public RecordSet getDecisionParameter(String activityID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> inputHash = new Hashtable<String, AlgernonValue>();
        inputHash.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        final RecordSet processInfoRecords = myService.executeRule(EngineConstants.Rules.Workflow.GET_INFO_TYPE_OF_FOLLOW_DECISION, inputHash,
                                                                   AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.GET_INFO_TYPE_OF_FOLLOW_DECISION, processInfoRecords, this.getClass());

        return processInfoRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#setAsActiveVersion(java.lang.String)
     */
    @Override
    public RecordSet setAsActiveVersion(String templateID) throws OntologyErrorException
    {
        Validate.notNull(templateID);

        final Hashtable<String, AlgernonValue> mainHash = new Hashtable<String, AlgernonValue>();
        mainHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(templateID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.SET_AS_ACTIVE_VERSION, mainHash, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.SET_AS_ACTIVE_VERSION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkflowEngine#setAsInactiveVersion(java.lang.String)
     */
    @Override
    public RecordSet setAsInactiveVersion(String templateID) throws OntologyErrorException
    {
        Validate.notNull(templateID);

        final Hashtable<String, AlgernonValue> mainHash = new Hashtable<String, AlgernonValue>();
        mainHash.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(templateID, false));
        final RecordSet result = myService
                .executeRule(ProcessEngineConstants.Rules.Process.SET_AS_INACTIVE_VERSION, mainHash, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.SET_AS_INACTIVE_VERSION, result, this.getClass());
        return result;
    }

}
