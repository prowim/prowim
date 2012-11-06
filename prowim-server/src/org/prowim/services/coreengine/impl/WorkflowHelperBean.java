/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 04.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.datamodel.prowim.Role;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.WorkFlowHelper;
import org.prowim.services.coreengine.WorkflowEngine;
import org.prowim.services.coreengine.EngineConstants.Variables.Process;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;


/**
 * The implementation for the {@link WorkFlowHelper work flow helper}
 * 
 * @author Saad Wardi , Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
@Stateless
public class WorkflowHelperBean implements WorkFlowHelper
{
    private static final Logger LOG = Logger.getLogger(WorkflowHelperBean.class);

    @IgnoreDependency
    @EJB
    private WorkflowEngine      workflowEngine;

    @IgnoreDependency
    @EJB
    private ProcessEngine       processEngine;

    @IgnoreDependency
    @EJB
    private CommonEngine        commonEngine;

    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    @IgnoreDependency
    @EJB
    private CommonHelper        common;

    @IgnoreDependency
    @EJB
    private ProcessHelper       processHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#finishActivity(java.lang.String, java.lang.String, java.lang.String)
     */
    public void finishActivity(String activityID, String status, String userID) throws OntologyErrorException
    {
        setActivityStatus(activityID, status, userID);
        /**
         * <br>
         * 1. check if the activity that will be finished is the end activity of the process <br>
         * 2. Get all active activities<NOT the end activity> and set them to status "ruht"
         * */
        finishActiveActivitiesAfterEnd(activityID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#createWorkflow(java.lang.String, java.lang.String)
     */
    public String createWorkflow(String processTemplateID, String userID) throws OntologyErrorException
    {
        Validate.notNull(processTemplateID);
        Validate.notNull(userID);

        final RecordSet records = workflowEngine.createWorkflow(processTemplateID, userID);

        String processInstanceID = null;
        if (records.getNoOfRecords() > 0)
        {
            processInstanceID = records.getRecords()[0].get(Process.PROCESS_INST_ID);
        }
        if (processInstanceID == null)
            throw new IllegalStateException("Could not create workflow for template: " + processTemplateID + " and user: " + userID);
        return processInstanceID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#rollBack()
     */
    public void rollBack()
    {
        LOG.error("rolling back transaction");
        throw new IllegalStateException("rolling back transaction");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#startProcess(java.lang.String, java.lang.String, org.prowim.datamodel.collections.RoleArray, org.prowim.datamodel.collections.ParameterArray)
     */
    public void startProcess(String processInstanceID, String processInstanceName, RoleArray roles, ParameterArray parameterList)
                                                                                                                                 throws OntologyErrorException
    {
        // First validate not null
        Validate.notNull(processInstanceID);
        Validate.notNull(processInstanceName);
        Validate.notNull(roles);
        Validate.notNull(parameterList);

        /** 1. Change the process instance name. */
        setProcessInstanceName(processInstanceID, processInstanceName);
        /** 2. Set the selected values to the parameters. */
        setProcessParameters(parameterList);
        /** 3. Set the roles. */
        assignRoles(roles);

    }

    /**
     * Changes the process instance name.
     * 
     * @param processInstanceID not null process instance id.
     * @param processInstanceName not null the new name.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void setProcessInstanceName(String processInstanceID, String processInstanceName) throws OntologyErrorException
    {
        Validate.notNull(processInstanceID);
        Validate.notNull(processInstanceName);
        final String processOldName = common.getName(processInstanceID);
        processEngine.clearSlotValue(processInstanceID, Relation.Slots.NAME, processOldName);
        processEngine.setSlotValue(processInstanceID, Relation.Slots.NAME, processInstanceName);
    }

    /**
     * Sets the selected value to each parameter in the list.<br/>
     * 
     * @param parameterList the parameter list to be set.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    private void setProcessParameters(ParameterArray parameterList) throws OntologyErrorException
    {
        Validate.notNull(parameterList);
        final ParameterArray paramArray = parameterList;
        final Iterator<Parameter> it = paramArray.iterator();
        while (it.hasNext())
        {
            Parameter parameter = it.next();
            processHelper.setParameterValue(parameter);
        }
    }

    /**
     * Assignes the selected persons to each role in the roles array.
     * 
     * @param roles not null array of role to be assigned.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    private void assignRoles(RoleArray roles) throws OntologyErrorException
    {
        Validate.notNull(roles);
        final RoleArray roleArray = roles;
        final Iterator<Role> itRoles = roleArray.iterator();

        while (itRoles.hasNext())
        {
            Role role = itRoles.next();
            /** 1. First clear the old values. */
            this.clearRoleAssignment(role.getID());

            List<Person> personsToAssign = role.getPersonsList();
            /**
             * 2. Iterates the persons in the personsList and assigne each person at current role from <br/>
             * the first iteration
             */
            Iterator<Person> itPersons = personsToAssign.iterator();
            while (itPersons.hasNext())
            {
                Person person = itPersons.next();
                processHelper.assigneRole(role.getID(), person.getID());
            }
        }
    }

    /**
     * 
     * Clean the roles, which are for this process to set
     * 
     * @param roleID ID of role
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    private void clearRoleAssignment(String roleID) throws OntologyErrorException
    {
        final RecordSet actualAssignedPersons = processEngine.getAssignedPersonsToRole(roleID);
        if (actualAssignedPersons.getNoOfRecords() > 0)
        {
            for (int i = 0; i < actualAssignedPersons.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> actualAssignedPerson = actualAssignedPersons.getRecords()[i];
                final String personID = actualAssignedPerson.get(ProcessEngineConstants.Variables.Organisation.Person.ID);
                processEngine.clearSlotValue(roleID, Relation.Slots.ASSIGNED_TO, personID);

            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#setActivityStatus(java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean setActivityStatus(String activityID, String status, String userID) throws OntologyErrorException
    {
        workflowEngine.checkForAutomatics();
        boolean result = workflowEngine.setActivityStatus(activityID, status, userID);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#applyAndSetRoles(java.lang.String, java.lang.String)
     */
    public int applyAndSetRoles(String activityID, String userID) throws OntologyErrorException
    {
        int done = 0;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        tab.put(EngineConstants.Variables.Person.PERSON_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(EngineConstants.Rules.Workflow.SET_JOB_FINISH, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.SET_JOB_FINISH, result, this.getClass());
        if ( !result.getResult().equalsIgnoreCase(AlgernonConstants.OK))
        {
            LOG.info("User " + userID + " can not finish activity " + activityID);
            done = -1;
        }
        result = myService.executeRule(EngineConstants.Rules.Workflow.GET_PERSONS_FINISHED_ACTIVITY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.GET_PERSONS_FINISHED_ACTIVITY, result, this.getClass());
        int responsiblePersons = result.getNoOfRecords();
        result = this.getResponsiblePersons(activityID);
        int personsCount = result.getNoOfRecords();
        if (responsiblePersons == personsCount)
            done = 1;
        return done;
    }

    /**
     * 
     * Get the persons, who are responsible for given activity
     * 
     * @param activityID ID of activity
     * @return List of persons, can be empty
     * @throws OntologyErrorException if an error occurs in the ontology back end
     * 
     * @TODO In deisen Teil soll klar gestellt werden, ob man das ganze überhaupt braucht und wenn ja, was die beiden Regeln überhaupt <br>
     *       machen sollen. Für mich sind beide Regeln eigentlich gleich. Wird von der obere Methode aufgerufen.
     */
    private RecordSet getResponsiblePersons(String activityID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, new AlgernonValue(activityID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_ASSIGNEDPERSONS_INTO_ROLES, tab,
                                                       AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_ASSIGNEDPERSONS_INTO_ROLES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#applyForAssignedPersons(java.lang.String, java.lang.String)
     */

    public int applyForAssignedPersons(String activity, String userID) throws OntologyErrorException
    {
        int done = 0;
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, new AlgernonValue(activity, false));
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.PERSON, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.SET_ACTION_DONE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.SET_ACTION_DONE, result, this.getClass());
        if ( !result.getResult().equalsIgnoreCase(AlgernonConstants.OK))
        {
            LOG.info("User " + userID + " can not finish activity " + activity);
            done = -1;
        }
        result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_RESPONSIBLE_PERSONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_RESPONSIBLE_PERSONS, result, this.getClass());
        String responsiblePersonID = null;
        if (result.getNoOfRecords() == 1)
        {

            final Hashtable<String, String> record = result.getRecords()[0];
            responsiblePersonID = record.get(ProcessEngineConstants.Variables.Organisation.Person.ID);
            if (responsiblePersonID.equals(userID))
            {
                done = 1;
            }
        }
        return done;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#getInstanceAttribute(java.lang.String, java.lang.String)
     */
    public String[] getInstanceAttribute(String instanceID, String attributeName) throws OntologyErrorException
    {
        String[] attributes = null;

        RecordSet result = this.commonEngine.getSlotValue(instanceID, attributeName);
        if (result != null && result.getNoOfRecords() > 0)
        {
            attributes = new String[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String attribute = new String(record.get(EngineConstants.Variables.Common.VALUE));
                attributes[i] = attribute;
            }
        }
        return attributes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#getDecisionParameter(java.lang.String)
     */
    public ParameterArray getDecisionParameter(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        final RecordSet records = workflowEngine.getDecisionParameter(activityID);
        ParameterArray parameters = new ParameterArray();
        for (int i = 0; i < records.getNoOfRecords(); i++)
        {
            final Hashtable<String, String> processInfoHash = records.getRecords()[i];
            final String decisionID = processInfoHash.get(EngineConstants.Variables.Decision.DECISION_ID);
            final String processInformationID = processInfoHash.get(EngineConstants.Variables.ProcessInformation.PROCESS_INFORMATION_ID);
            Parameter parameter = processHelper.getProcessInformation(processInformationID, decisionID);
            parameters.add(parameter);
        }
        return parameters;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.WorkFlowHelper#setHasSubModelAttr(java.lang.String, java.lang.String)
     */
    public void setHasSubModelAttr(String subProcessID, String activityID) throws OntologyErrorException
    {
        Validate.notNull(subProcessID);
        Validate.notNull(activityID);
        workflowEngine.setHasSubModelAttr(subProcessID, activityID);
    }

    /**
     * Checks if an activity is the end acivity.
     * 
     * @param activityID the activity ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void finishActiveActivitiesAfterEnd(String activityID) throws OntologyErrorException
    {
        /** 1. checks if this is the end activity. */
        RecordSet records = workflowEngine.checkEndActivity(activityID);
        String result = null;
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            if (records.getNoOfRecords() > 0)
            {
                for (int i = 0; i < records.getNoOfRecords(); i++)
                {
                    final Hashtable<String, String> processRecord = records.getRecords()[i];
                    result = processRecord.get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
                }
            }
        }
        /** 2. Get all active activities and set their status to "ruht". */
        if (result != null)
        {

            // call "finishRestActiveActivities" to set the status to "ruht";; input is result = processID
            RecordSet activitiesRecords = workflowEngine.finishRestActiveActivities(result);
            // LOG the result
            if (activitiesRecords.getResult().equals(AlgernonConstants.OK))
            {
                if (activitiesRecords.getNoOfRecords() > 0)
                {
                    for (int i = 0; i < activitiesRecords.getNoOfRecords(); i++)
                    {
                        final Hashtable<String, String> activityRecord = activitiesRecords.getRecords()[i];
                        LOG.debug("SET Status to \"ruht\" after finish workflow" + activityRecord.get(ProcessEngineConstants.Variables.Common.ID));
                    }
                }
            }
        }
    }

}
