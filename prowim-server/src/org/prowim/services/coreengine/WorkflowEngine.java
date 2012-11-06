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
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * This is the interface between ontology and Prowim services to implement workfolw functions.
 * 
 * @author Saad Wardi, Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
@Local
public interface WorkflowEngine
{
    /**
     * Creates a new workflow from a given process template.
     * 
     * The new workflow includes all related roles, knowledge objects etc.
     * 
     * @param templateID the process template id.
     * @param userID the user id.
     * @return status, processInstanceID.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createWorkflow(String templateID, String userID) throws OntologyErrorException;

    /**
     * Clones the given process template.
     * 
     * @param templateID the process template id.
     * @param userID the user id of the user who can start the process/workflow, can be null, if no user specified
     * @return {@link RecordSet} containing the status and the new processTemplateID, never null
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    RecordSet cloneWorkflow(String templateID, String userID) throws OntologyErrorException;

    /**
     * Creates the process id.
     * 
     * @param templateID the process template id.
     * @param processName the process instance name.
     * @return the process id id.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createProcessInstance(String templateID, String processName) throws OntologyErrorException;

    /**
     * Creates instances of elements for the given process id.
     * 
     * @param processInstanceID the process id .
     * @return status.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createInstanceElements(String processInstanceID) throws OntologyErrorException;

    /**
     * Creates instances of {@link ProcessElement}s, which are included in given {@link Process}. <br>
     * This method is to used for cloning a process and not for create a workflow. <br>
     * The different to {@link #createInstanceElements(String)} is, that this method dose not create a instance of global {@link ProcessElement}s <br>
     * like {@link Role}s, {@link Mean}s, {@link ResultsMemory}s
     * 
     * @param processInstanceID the process id .
     * @return status.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createProcessElementsForCloning(String processInstanceID) throws OntologyErrorException;

    /**
     * 
     * Set relation between global {@link ProcessElement}s and the given {@link org.prowim.datamodel.prowim.Process}.
     * 
     * @param processInstanceID id of given {@link org.prowim.datamodel.prowim.Process}
     * @return status
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setRelationOfGlobalElementsToProcess(String processInstanceID) throws OntologyErrorException;

    /**
     * Copies the instance element references from the given process ID to the process with the given target ID.
     * 
     * @param processInstanceID the process id to copy from, may not be null
     * @param targetID the process id to copy to, may not be null
     * @return {@link RecordSet} containing the status of the query
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createInstanceElementReferences(String processInstanceID, String targetID) throws OntologyErrorException;

    /**
     * Creates the process id relations global.
     * 
     * @param processInstanceID the process id.
     * @return status.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createProcessInstanceRelationsGlobal(String processInstanceID) throws OntologyErrorException;

    /**
     * Creates the local relation in the given process.
     * 
     * @param processInstanceID the process id.
     * @return status.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createProcessInstanceRelationsLocal(String processInstanceID) throws OntologyErrorException;

    /**
     * Creates the process relations.
     * 
     * @param processInstanceID the process id.
     * @return status.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet createProcessRelations(String processInstanceID) throws OntologyErrorException;

    /**
     * Sets the user which is allowed to start the process.
     * 
     * @param processInstanceID process id.
     * @param userID the user id.
     * @return status.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setProcessStarter(String processInstanceID, String userID) throws OntologyErrorException;

    /**
     * Get get the start activity of given process and set this active.
     * 
     * @param processInstanceID the process id id.
     * @return status.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet activateStartActivityOfProcess(String processInstanceID) throws OntologyErrorException;

    /**
     * Assignes the roles of a process by executing the defined rule. The rule assigns the roles.
     * 
     * @param processInstanceID the ID of a process instance.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void assignRolesByRuleExecution(String processInstanceID) throws OntologyErrorException;

    /**
     * Checks if there are automatic activities.
     * 
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void checkForAutomatics() throws OntologyErrorException;

    /**
     * Sets the activity status.
     * 
     * @param activity the activity ID
     * @param status status
     * @param userID the user ID
     * @return true or false
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    boolean setActivityStatus(String activity, String status, String userID) throws OntologyErrorException;

    /**
     * Sets the status of an {@link Activity}.
     * 
     * @param activityID ID of the {@link Activity}
     * @param status the status to be set.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void setActivityStatus(String activityID, String status) throws OntologyErrorException;

    /**
     * Sets the status of an {@link Activity}.
     * 
     * @param activityID ID of the {@link Activity}
     * @return List of process informations and informations types of this
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getDecisionParameter(String activityID) throws OntologyErrorException;

    /**
     * Set the Attribute "hat_Submodel" for created instance.
     * 
     * @param subProcessID Id of of sub process. not null.
     * @param activityID Id of activity, which call the sub process. not null
     * @return state
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setHasSubModelAttr(String subProcessID, String activityID) throws OntologyErrorException;

    /**
     * Checks if an activity is the end activity.
     * 
     * @param activityID the activity ID.
     * @return processID, the ID of the process.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet checkEndActivity(String activityID) throws OntologyErrorException;

    /**
     * Sets the status of the active activities to "ruht", after the work flow is finished.
     * 
     * @param processID the process that is finished.
     * @return RecordSet status of the activities to "ruht". is needed to debug (know), witch activities was active after th work flow is finished.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet finishRestActiveActivities(String processID) throws OntologyErrorException;

    /**
     * Clones the process template with the given ID.
     * 
     * @param templateID the template ID to clone, may not be null
     * @param processName the name of the process (Slot "Bezeichnung"), may not be null
     * @return {@link RecordSet} with ID of cloned template, never null
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     */
    RecordSet cloneProcessTemplate(String templateID, String processName) throws OntologyErrorException;

    /**
     * Sets the given template as active version.
     * 
     * @param templateID the template ID to set as active version, may not be null
     * @return {@link RecordSet} with result of operation, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet setAsActiveVersion(String templateID) throws OntologyErrorException;

    /**
     * Sets the given template as not active version.
     * 
     * @param templateID the template ID to set as inactive version, may not be null
     * @return {@link RecordSet} with result of operation, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet setAsInactiveVersion(String templateID) throws OntologyErrorException;

    /**
     * Clears the template slots for the given process ID
     * 
     * @param processID the processID to use, may not be null
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void clearTemplateSlots(String processID) throws OntologyErrorException;
}
