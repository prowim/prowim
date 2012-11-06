/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *This file is part of ProWim.

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
 *
 */

package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.ProcessEngineConstants.Rules.Process.Subprocess;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.ParameterConstraint;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.editor.EditorRemote;
import org.prowim.services.ejb.process.ProcessRemote;



/**
 * This defines the prowim server-application APIs that operate on the ontology to provide process management functionality.
 * 
 * @author Saad Wardi
 * 
 */
@Local
public interface ProcessEngine
{
    /**
     * Gets process type.
     * 
     * @param processID the id.
     * @return process type for the process id
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProcessType(String processID) throws OntologyErrorException;

    /**
     * <p>
     * Returns the process ID for the given process template ID and version string.
     * </p>
     * 
     * <p>
     * The given process template ID has to be the initial template version. You can retrieve that by calling {@link ProcessEngine#getTemplateID(String)}.
     * </p>
     * 
     * @param initialTemplateID the ID of the initial process template, may not be null
     * @param version the version to return the ID for, may not be null, the version is the user defined version, not the alfresco version!
     * @return String the process ID, may be null, then the given version was not found
     * @throws OntologyErrorException if an error occurs in the ontology backend
     * @see Version#getUserDefinedVersion()
     */
    String getProcessIDForVersion(String initialTemplateID, String version) throws OntologyErrorException;

    /**
     * 
     * Gets the startRole of the given {@link Process}.
     * 
     * @param processID cannot be null
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getStartRole(String processID) throws OntologyErrorException;

    /**
     * Get all process types of system.
     * 
     * @return list of process types.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getAllProcessTypes() throws OntologyErrorException;

    /**
     * Create a new process type .
     * 
     * @param name Name of process type. Not null.
     * @param description Description of process type. Not null.
     * 
     * @return list of process types.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet createProcessType(String name, String description) throws OntologyErrorException;

    /**
     * Sets the parent type to a processtype.
     * 
     * @param typeID the not null ProcessType ID.
     * @param parentTypeID null is possible if the parent ProcessTypeID is the root.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet setProcessTypeParent(String typeID, String parentTypeID) throws OntologyErrorException;

    /**
     * Set the process type of one process.
     * 
     * @param processTypeID ID of process type
     * @param processID ID of process
     * 
     * @return list of process types.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet setProcessType(String processTypeID, String processID) throws OntologyErrorException;

    /**
     * Gets all processes.
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getAlleProzesse() throws OntologyErrorException;

    /**
     * Gets knowledge objects.
     * 
     * @param id the id of a process or an activity.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getInstanceKnowledgeObjects(String id) throws OntologyErrorException;

    /**
     * Gets all activities.
     * 
     * @param template id.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActivities(String template) throws OntologyErrorException;

    /**
     * Gets the top domains
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getToplevelDomaenen() throws OntologyErrorException;

    /**
     * Description.
     * 
     * @param topDomain domain id
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getSubdomaenen(String topDomain) throws OntologyErrorException;

    /**
     * Description.
     * 
     * @param domainID domain id
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeObjectsDomains(String domainID) throws OntologyErrorException;

    /**
     * Gets pending activities.
     * 
     * @param user a user.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getMyActiveActivities(String user) throws OntologyErrorException;

    /**
     * 
     * Gets the running processes for the user with the id = userID.
     * 
     * @param userID the id of the user, who is authorized to start this processes
     * @return {@link RecordSet} processID, processIdent, processType, reference, createTime
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getExecutableProcesses(String userID) throws OntologyErrorException;

    /**
     * Gets the running processes for the user with the id = userID..
     * 
     * @param userID a user id
     * @return {@link RecordSet} processID, processIdent, starttime, starter
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getRunningProcesses(String userID) throws OntologyErrorException;

    /**
     * Gets the activ activities on the process with the id = processID.
     * 
     * @param processID a process id.
     * @return {@link RecordSet} activityID, title, starttime, user
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActiveActivities(String processID) throws OntologyErrorException;

    /**
     * Gets the registered users.
     * 
     * @return {@link RecordSet} userID, Username
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getUsers() throws OntologyErrorException;

    /**
     * Gets the registered roles.
     * 
     * @param processID the process id
     * @return {@link RecordSet} ID, title, Person, Activity, user
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getRoles(String processID) throws OntologyErrorException;

    /**
     * Gets the registered roles.
     * 
     * @param activityID the activity id
     * @return {@link RecordSet} ID, title, Person, Activity, user
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActivityRoles(String activityID) throws OntologyErrorException;

    /**
     * Gets the the data to an instance of PROCESSINFORMATION_CHART.
     * 
     * @param processInformationID the ID.
     * @return {@link RecordSet} value, List, process, typ, name
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getParameter(String processInformationID) throws OntologyErrorException;

    /**
     * Delete a workflow (a running process).
     * 
     * @param processInstanceID theid of the running process.
     * @return status
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet deleteProcess(String processInstanceID) throws OntologyErrorException;

    /**
     * Delete the instances of a process template.
     * 
     * @param processID the process template ID.
     * @return status
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet deleteProcessInstances(String processID) throws OntologyErrorException;

    /**
     * Gets the finished workflows. Description.
     * 
     * @param userID the userID.
     * @return record of all finished processes.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getFinishedProcesses(String userID) throws OntologyErrorException;

    /**
     * Gets the information types registered in the ontology.
     * 
     * @return records of all information types.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getInformationTypes() throws OntologyErrorException;

    /**
     * Gets the process parameters.
     * 
     * @param processInstanceID process instance id.
     * @return process parameters.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProcessParameter(String processInstanceID) throws OntologyErrorException;

    /**
     * Gets the process-template parameters.
     * 
     * @param processID process-template id.
     * @return process parameters.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProcessTemplateParameter(String processID) throws OntologyErrorException;

    /**
     * Gets all registered organisations.
     * 
     * @return organisations.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getOrganisations() throws OntologyErrorException;

    /**
     * Gets all top registered organizations.
     * 
     * @return organizations.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getTopOrganisations() throws OntologyErrorException;

    /**
     * Gets all sub registered organizations to given organization ID.
     * 
     * @param orgaID ID of given organization ID.
     * 
     * @return organizations.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getSubOrganisations(String orgaID) throws OntologyErrorException;

    /**
     * Gets the assigned person to the role with the id = roleID.
     * 
     * @param roleID the id of the role.
     * @return not null persons record, empty if not found.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getAssignedPersonsToRole(String roleID) throws OntologyErrorException;

    /**
     * Gets the process name.
     * 
     * @param instance the process instance id.
     * @return the name of the process id.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @Deprecated
    String getFrameName(String instance) throws OntologyErrorException;

    /**
     * Gets the person with id=userID.
     * 
     * @param userID the user id
     * @return Person
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getPerson(String userID) throws OntologyErrorException;

    /**
     * Gets the selectList to a Parameter.
     * 
     * @param parameterID the parameterID
     * @return selection list
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getParameterValue(String parameterID) throws OntologyErrorException;

    /**
     * Gets the organisation.
     * 
     * @param organisationID the organisation id.
     * @return RecordSet
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getOrganisation(String organisationID) throws OntologyErrorException;

    /**
     * Gets the parameter scalar value.
     * 
     * @param parameterID the parameter id.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getParameterScalarValue(String parameterID) throws OntologyErrorException;

    /**
     * Gets the parameter possible selection list.
     * 
     * @param parameterID the parameter id.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getParameterSelectList(String parameterID) throws OntologyErrorException;

    /**
     * Generates the possible selection list to a process information instance {@link Parameter}.
     * 
     * @param parameterID the parameter id.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getSelectionAttribute(String parameterID) throws OntologyErrorException;

    /**
     * Gets the the rulename from the InformationType to be executed to generate the selection list to be applied on the {@link Parameter}.
     * 
     * 
     * @param parameterID the parameter id.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getSelectionAttributeFromInfoType(String parameterID) throws OntologyErrorException;

    /**
     * Executes the possible selection list.
     * 
     * @param ruleName the rule name
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet executeSelectionAttributeRule(String ruleName) throws OntologyErrorException;

    /**
     * Executes the possible selection list.
     * 
     * @param ruleName the rule name.
     * @param activityID the activity id.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet executeSelectionAttributeRuleForControlFlow(String ruleName, String activityID) throws OntologyErrorException;

    /**
     * Gets the id of the frame where the processinformation withe given ID is defined.
     * 
     * @param processInformationID the ID of the processinformation
     * @return the ID of the frame.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getFrameToProcessInformation(String processInformationID) throws OntologyErrorException;

    /**
     * <p>
     * Sets the value of a slot.
     * </p>
     * 
     * <p>
     * IMPORTANT: You have to clear a slot value before you can set it. Use {@link #clearSlotValue(String, String, String)} or {@link #clearSlot(String, String)}.
     * </p>
     * 
     * @param frameID parameter id.
     * @param slotID slot id.
     * @param value the slot value, can only be strings! No booleans working
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException;

    /**
     * Clears a slot value.
     * 
     * @param parameterID the parameter id
     * @param slotID the slot id
     * @param whereValue the whereValue(the value to be cleared)
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void clearSlotValue(String parameterID, String slotID, String whereValue) throws OntologyErrorException;

    /**
     * Clears all values in the given slot for the given instance ID.
     * 
     * @param frameID The frame which has the slot
     * @param slotID the ID of the slot on the frame
     * @return status
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet clearSlot(String frameID, String slotID) throws OntologyErrorException;

    /**
     * Gets the ControlFlowFlow with id = controlFlowID .
     * 
     * @param controlFlowID the control flow id.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getControlFlow(String controlFlowID) throws OntologyErrorException;

    /**
     * Gets the {@link ParameterConstraint} object for a {@link Parameter}.
     * 
     * @param parameterID the parameter id.
     * @return the {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getParameterConstraint(String parameterID) throws OntologyErrorException;

    /**
     * Gets the {@link Activity} Input {@link Parameter}.
     * 
     * @param activityID the activity ID.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActivityInput(String activityID) throws OntologyErrorException;

    /**
     * Gets the {@link Activity} Output.
     * 
     * @param activityID the activity ID.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActivityOutput(String activityID) throws OntologyErrorException;

    /**
     * Gets the process information instances defined for the product with the id = productID.
     * 
     * @param productID ID des products.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProductProcessInformations(String productID) throws OntologyErrorException;

    /**
     * Gets the value of the Slot "Freigabetyp".
     * 
     * @param activityID the ID of the {@link Activity}.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActivityEnableType(String activityID) throws OntologyErrorException;

    /**
     * Sets the status of an {@link Activity}.
     * 
     * @param activityID ID of the {@link Activity}
     * @param status the status to be set.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setActivityStatus(String activityID, String status) throws OntologyErrorException;

    /**
     * Gets the timed activities.
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getTimedActivities() throws OntologyErrorException;

    /**
     * Generates a new KnowledgeObject and put it toa ProcessElement.
     * 
     * @param relationID the ProcessElement instance.
     * @param name the name of the knowledgeobject
     * @param userID the ID of the user that creates the knowledgeobject.
     * @return the ID of the created knowledgeobject
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet genKnowledgeObject(String relationID, String name, String userID) throws OntologyErrorException;

    /**
     * Generates a new KnowledgeObject.
     * 
     * @param name the name of the knowledgeobject
     * @param userID the ID of the user that creates the knowledgeobject.
     * @return the ID of the created knowledgeobject
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet genKnowledgeObject(String name, String userID) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID the ID of the {@link KnowledgeObject}
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets the Creator of the {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID ID of the KnowledgeObject
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeObjectCreatorNumber(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets the Creator of the {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID ID of the KnowledgeObject
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeObjectCreator(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Adds an existing KnowledgeObject to a processElement.
     * 
     * @param knowledgeObjectID ID of the {@link KnowledgeObject}.
     * @param processElementID ID of a ProcessElement like : Process, Activity, Person, ...
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet addKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException;

    /**
     * Gets the ID of the template to a relation.<br/>
     * NOTE: the relation can be a template or an instance.
     * 
     * @param relationID the ID of the relation (instance).
     * @return the template ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getTemplateID(String relationID) throws OntologyErrorException;

    /**
     * Gets all {@link KnowledgeRepository} registered in the ontology.
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeRepositories() throws OntologyErrorException;

    /**
     * Creates a new KnowledgeObject and add it to a ProcessElement relation..
     * 
     * @param knowledgeObjectID the ID of the knowledgeObject.
     * @param name the name of the KnowledgeLink.
     * @param wsID ID of repository.
     * @param link the link.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet genKnowledgeLink(String knowledgeObjectID, String name, String wsID, String link) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeLink}.
     * 
     * @param knowledgeLinkID the ID of the {@link KnowledgeLink}
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeLink(String knowledgeLinkID) throws OntologyErrorException;

    /**
     * Gets the count of the Knowledgelinks.
     * 
     * @param knowledgeObjectID the ID of the KnowledgeObject.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    int getCountOfKnowledgeLinks(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets the business domains assigned to the process instance with the ID = processInstanceID..
     * 
     * @param processInstanceID the ID of the process instance.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getBusinessDomains(String processInstanceID) throws OntologyErrorException;

    /**
     * Gets a {@link KnowledgeDomain}.
     * 
     * @param domainID ID of the domain.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeDomain(String domainID) throws OntologyErrorException;

    /**
     * Gets the dominators to a {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID not null ID of the {@link KnowledgeObject}
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeObjectDominators(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets the links to a {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID not null ID of the {@link KnowledgeObject}
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeObjectLinks(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets all {@link KnowledgeObject}.
     * 
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getAllKnowledgeObjects() throws OntologyErrorException;

    /**
     * Searchs in the name of KnowledgeLinks for the given pattern.
     * 
     * @param pattern the pattern to be searched.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet searchLinks(String pattern) throws OntologyErrorException;

    /**
     * Searchs in the name of the knowledgeobjects for the given pattern.
     * 
     * @param pattern the pattern to be searched
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet searchKnowledgeObjects(String pattern) throws OntologyErrorException;

    /**
     * Searchs in the name of the knowledgeDomains for the given pattern.
     * 
     * @param pattern the pattern to be searched. The pattern could be "milk" and everything is returned which holds "milk", i.e. "cowmilk" etc.
     * @return {@link RecordSet} IDs of {@link KnowledgeDomain} , never null, record sets can be empty, if not found.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet searchKnowledgeDomains(String pattern) throws OntologyErrorException;

    /**
     * Gets the IDs of the links used in the given KnowledgeObject.
     * 
     * @param knowledgeObjectID the ID of the KnowledgeObject.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getKnowledgeLinkForKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets the path of the generated Html folder on the machine, from where the model was updated.
     * 
     * @param processID the ID of the process.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProcessBrowserChartPath(String processID) throws OntologyErrorException;

    /**
     * Generates a processinformation.
     * 
     * @param name the name of the processinformation
     * @param frameID the ID of the frame that assigne the processinformation
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet genProcessInformation(String name, String frameID) throws OntologyErrorException;

    /**
     * Gets the ID of the PROCESSINFORMATION_CHART that is not editable.
     * 
     * @param processID the ID of the process.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getNotEditableProcessInformations(String processID) throws OntologyErrorException;

    /**
     * Gets the ID of the activty.
     * 
     * @param productID the ID of the process.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getFrameToOutputProduct(String productID) throws OntologyErrorException;

    /**
     * True if the frameID is an ID of a product.
     * 
     * @param frameID the frame ID
     * @return true if the frameID is an ID of a Product
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isProduct(String frameID) throws OntologyErrorException;

    /**
     * Sends an email when the activity is activated.
     * 
     * @param activityID the ID of the activity.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void sendEmail(String activityID) throws OntologyErrorException;

    /**
     * Gets the ID of the startactivity to a process.
     * 
     * @param processInstanceID ID of the process instance.
     * @return ID of the Startactivity.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getStartActivity(String processInstanceID) throws OntologyErrorException;

    /**
     * Checks if a process ID ois of a process Template.
     * 
     * @param processID not null process ID.
     * @return True if the process is template, false otherwise.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isProcessTemplate(String processID) throws OntologyErrorException;

    /**
     * Gets the members of an organization.
     * 
     * @param organizationID not null organization ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getMembers(String organizationID) throws OntologyErrorException;

    /**
     * Gets the activity.
     * 
     * @param id not nhll activity ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActivity(String id) throws OntologyErrorException;

    /**
     * Gets the fund.
     * 
     * @param meanID not null fund ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getMean(String meanID) throws OntologyErrorException;

    /**
     * Gets the role.
     * 
     * @param roleID not null role ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getRole(String roleID) throws OntologyErrorException;

    /**
     * Gets the processinformation.
     * 
     * @param processinformationID not null processinformation ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProcessinformation(String processinformationID) throws OntologyErrorException;

    /**
     * Gets the resultsMemory.
     * 
     * @param resultsMemoryID not null resultsMemory ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getResultsMemory(String resultsMemoryID) throws OntologyErrorException;

    /**
     * Gets the work
     * 
     * @param workID not null work ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getWork(String workID) throws OntologyErrorException;

    /**
     * Gets the function
     * 
     * @param functionID not null function ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getFunction(String functionID) throws OntologyErrorException;

    /**
     * Gets the product
     * 
     * @param productID not null product ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProduct(String productID) throws OntologyErrorException;

    /**
     * Gets the productway
     * 
     * @param productWayID not null product ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getProductWay(String productWayID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getModels()}.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getModels() throws OntologyErrorException;

    /**
     * Gets the informationtype.
     * 
     * @param id not null ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getInformationType(String id) throws OntologyErrorException;

    /**
     * Gets the information type ID..
     * 
     * @param referenceID a frame that ist_vom_informationtype
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getInformationTypeID(String referenceID) throws OntologyErrorException;

    /**
     * Returns all exists sub processes.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getAllExistSubProcesses() throws OntologyErrorException;

    /**
     * Check if given process is a sub process.
     * 
     * @param processID Id of given process
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet isSubProcess(String processID) throws OntologyErrorException;

    /**
     * Adds a role to an organization.
     * 
     * @param roleID not null role ID.
     * @param organizationID not null organization ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet addRoleToOrganization(String roleID, String organizationID) throws OntologyErrorException;

    /**
     * Gets the roles defined in an organization.
     * 
     * @param organizationID not null organization ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getRolesToOrganization(String organizationID) throws OntologyErrorException;

    /**
     * Get the set of roles, which are define global and are not a instance of other roles (templates).
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getGlobalRoles() throws OntologyErrorException;

    /**
     * Get the set of means, which are define global and are not a instance of other means (templates).
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getGlobalMeans() throws OntologyErrorException;

    /**
     * Get the set of results memory, which are define global and are not a instance of other results memory (templates).
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getGlobalResultsMem() throws OntologyErrorException;

    /**
     * Get sub processes of given process.
     * 
     * @param processID ID of process.
     * @return list of sub processes
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getSubprocesses(String processID) throws OntologyErrorException;

    /**
     * {@link ProcessRemote#getSubProcessTypes}.
     * 
     * @param processTypeID not null processtype ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getSubProcessTypes(String processTypeID) throws OntologyErrorException;

    /**
     * {@link ProcessRemote#getTypeProcesses}.
     * 
     * @param processTypeID not null processtype ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getTypeProcesses(String processTypeID) throws OntologyErrorException;

    /**
     * {@link ProcessRemote#getEnabledProcesses(String)}.
     * 
     * @param processTypeID not null processtype ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getEnabledProcessesOfType(String processTypeID) throws OntologyErrorException;

    /**
     * {@link ProcessRemote#getAllTopProcessTypes()}.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getAllTopProcessTypes() throws OntologyErrorException;

    /**
     * Returns the ID of the process template instance which is the initial version.
     * 
     * This method takes either an process instance ID or a process template ID.
     * 
     * @param processID the process id to return the initial template version for, may not be null
     * @return ID of the initial template version, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getInitialTemplateID(String processID) throws OntologyErrorException;

    /**
     * Returns the ID of the instance which is the initial version.
     * 
     * This method takes any process element instance ID.
     * 
     * @param instanceID the instance id to return the initial version for, may not be null
     * @return ID of the initial instance version, if no version set, returns the id itself, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getInitialVersionID(String instanceID) throws OntologyErrorException;

    /**
     * Returns the IDs of all process templates which exist as a version of the given initial template ID.
     * 
     * Use {@link #getInitialTemplateID(String)} to retrieve the initial template ID this method expects.
     * 
     * @param initialTemplateID the ID of the initial process template (first version), may not be null
     * @return {@link RecordSet} with all process template IDs, never null, in case of error
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     */
    RecordSet getTemplateVersions(String initialTemplateID) throws OntologyErrorException;

    /**
     * 
     * Returns all {@link ProcessElement}s for given {@link Process}id.
     * 
     * @param processID ID of given {@link Process}. Not null.
     * 
     * @return {@link RecordSet}. Not null.
     * @throws OntologyErrorException if an error occurs in ontolgy back end
     */
    RecordSet getElementsOfProcess(String processID) throws OntologyErrorException;

    /**
     * Gets the activity ID that call the input subprocess.
     * 
     * @param subprocessID not null subprocess ID.
     * @return the ID of the activity that call the subprocess. Not null.
     * @throws OntologyErrorException if an error occurs in the ontology back end.
     */
    RecordSet getSubprocessCaller(String subprocessID) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject} `s to given {@link Process}id. This included also the knowledge objects of {@link ProcessElement}s.
     * 
     * @param processID not null {@link Process} id.
     * @return {@link RecordSet}. Not null.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet getKnowledgeObjectsOfProcess(String processID) throws OntologyErrorException;

    /**
     * Gets the id of {@link Subprocess} to given {@link Activity}id.
     * 
     * @param activityID not null {@link Activity} id.
     * @return {@link RecordSet}. Not null.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet getSubProcessOfActivity(String activityID) throws OntologyErrorException;

    /**
     * Gets the registered {@link Mean}s of given {@link Activity}.
     * 
     * @param activityID the {@link Activity} id
     * @return {@link RecordSet} IDs of {@link Mean}s
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RecordSet getActivityMeans(String activityID) throws OntologyErrorException;

    /**
     * Gets the possible selection of a process information from info type MultipleList or SingleList.
     * 
     * @param processInformationID not null Process information id.
     * @return {@link RecordSet}. Not null.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet getPossibleSelection(String processInformationID) throws OntologyErrorException;

    /**
     * Returns all {@link ProcessElement}s, which use given {@link KnowledgeObject} or are in relation with this. <br>
     * The relation is "wird_benoetigt_fuer"
     * 
     * @param knowldgeObjectID id of given {@link KnowledgeObject}. Not null.
     * 
     * @return not null {@link ObjectArray}. It can be a list of {@link ProcessElement}s. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet getRelationsOfKnowledgeObject(String knowldgeObjectID) throws OntologyErrorException;

    /**
     * Returns the attributes or propeties of {@link Process} with given process id.
     * 
     * @param processID the ID of the process, may not be null.
     * @return not null {@link ObjectArray}. It can be a list of {@link Process}s atrributes or properties. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet getProcess(String processID) throws OntologyErrorException;

    /**
     * Search the given pattern in name field of {@link Process}es.
     * 
     * @param pattern A string to search in {@link Process}
     * @return not null {@link ObjectArray}. It can be a list of {@link Process}s ids, which have this pattern in there name. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet searchProcess(String pattern) throws OntologyErrorException;

    /**
     * Search in key words of {@link KnowledgeObject}s for given pattern.
     * 
     * @param pattern A word to search in a key words of {@link KnowledgeObject}s
     * @return not null {@link ObjectArray}. It can be a list of key words, which have this pattern in there name. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet searchKnowledgeObjectsKeyWords(String pattern) throws OntologyErrorException;

    /**
     * Get the key words of {@link KnowledgeObject}s.
     * 
     * @param knowledgeObjectID ID of given {@link KnowledgeObject}.. Not null.
     * @return not null {@link ObjectArray}. It can be a list of key words. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet getKnowledgeKeyWords(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Check if the {@link Process} is defied as process landscape.
     * 
     * @param processID ID of given {@link Process}. Not null.
     * @return not null {@link ObjectArray}. It can be a list of key words. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet isProcessLandscape(String processID) throws OntologyErrorException;
}
