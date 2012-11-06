/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-10-18 18:27:07 +0200 (Di, 18 Okt 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/ProcessHelper.java $
 * $LastChangedRevision: 5100 $
 *------------------------------------------------------------------------------
 * (c) 23.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.ProcessEngineConstants.Rules.Process.Subprocess;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.collections.ProductArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.InformationType;
import org.prowim.datamodel.prowim.Knowledge;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.datamodel.prowim.Product;
import org.prowim.datamodel.prowim.Role;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.impl.ProcessHelperBean.DenoationID;
import org.prowim.services.ejb.process.ProcessRemote;



/**
 * This class provides implementation of methods defined in {@link ProcessEngine} interface. methods implemented in this class returns the data types that are used from the facade bean.
 * 
 * @author Thomas Wiesner
 * @version $Revision: 5100 $
 * @since 2.0.0a8
 */
@Local
public interface ProcessHelper
{

    /**
     * Gets the active activities, that the user with the id = userID have to finish.
     * 
     * @param userID the user id, cannot be null.
     * @return array of {@link Activity}, if no item exists an empty array is returned, not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Activity[] getMyActiveActivities(String userID) throws OntologyErrorException;

    /**
     * Gets all processes that have been enabled.
     * 
     * @return array of {@link Process}, if no item exists an empty array is returned, not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Process[] getAllProcesses() throws OntologyErrorException;

    /**
     * Gets the {@link Knowledge} objects assigned to a process.
     * 
     * @param processes allowed values are array of {@link Process}, not null
     * @return {@link Knowledge}, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Knowledge[] getProcessKnowledge(Process[] processes) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject} objects assigned to a process.
     * 
     * @param activityID activity id. not null.
     * @return array of {@link KnowledgeObject}, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObjectArray getActivityKnowledge(String activityID) throws OntologyErrorException;

    /**
     * Gets the {@link Activity} objects defined in a process.
     * 
     * @param processTemplateID id of the process template. not null.
     * @return array of {@link Activity}, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Activity[] getActivities(String processTemplateID) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeDomain} objects defined in a process.
     * 
     * @return array of {@link KnowledgeDomain}, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeDomain[] getTopDomainKnowledge() throws OntologyErrorException;

    /**
     * Gets the sub {@link KnowledgeDomain} objects defined in a {@link KnowledgeDomain}.
     * 
     * @param topDomain the parent domain ID. not null.
     * @return {@link KnowledgeDomain}, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeDomain[] getSubKnowledgeDomain(String topDomain) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject} objects assigned to a domain.
     * 
     * @param domainID the domain ID. not null.
     * @return array of {@link KnowledgeObject}, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    java.util.List<KnowledgeObject> getDomainKnowledgeObjects(String domainID) throws OntologyErrorException;

    /**
     * Get the process type to given process.
     * 
     * @param processID process id. not null.
     * @return process type, null if not found
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessType getProcessType(String processID) throws OntologyErrorException;

    /**
     * 
     * Checks if the given ID is a process or not.
     * 
     * @param id to check an object in ontology, cannot be null
     * @return true if the type of the given ID is an {@link org.prowim.datamodel.prowim.Process}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isProcess(String id) throws OntologyErrorException;

    /**
     * Get all process types in system.
     * 
     * @return process types, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessType[] getAllProcessTypes() throws OntologyErrorException;

    /**
     * Get all process types in system.
     * 
     * @return process types , if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessType[] getAllTopProcessTypes() throws OntologyErrorException;

    /**
     * Set the process type to given process.
     * 
     * @param processTypeID ID of process type not null.
     * @param processID ID of process not null.
     * @return String Status not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String setProcessType(String processTypeID, String processID) throws OntologyErrorException;

    /**
     * Create a new process type.
     * 
     * @param name Name of process type. Not null.
     * @param description Description of process type. Not null.
     * 
     * @return process types. throws an IllegaStateException if could not create the type.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String createProcessType(String name, String description) throws OntologyErrorException;

    /**
     * Sets the parent type to a processtype.
     * 
     * @param typeID the not null ProcessType ID.
     * @param parentTypeID null is possible if the parent ProcessTypeID is the root.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setProcessTypeParent(String typeID, String parentTypeID) throws OntologyErrorException;

    /**
     * 
     * Gets all ready processes.
     * 
     * @param userID ID of logged user not null.
     * @return an array of {@link Process}, if no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Process[] getExecutableProcesses(String userID) throws OntologyErrorException;

    /**
     * Gets the running processes.
     * 
     * @param userID the user id. null not possible.
     * @return array of the {@link Process} object or an empty array if no process is running.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Process[] getRunningProcesses(final String userID) throws OntologyErrorException;

    /**
     * Gets the registered users.
     * 
     * @return an array of {@link Person}. an empty array object if no items exist.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Person[] getUsers() throws OntologyErrorException;

    /**
     * Gets an array of stored roles defined in a process.
     * 
     * @param processID process id not null.
     * @return an array of roles or an empty array if no item exist
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RoleArray getRolesForWorkFlow(String processID) throws OntologyErrorException;

    /**
     * Deletes a process.
     * 
     * @param processInstanceID the id of the process. not null.
     * @return status succeeded or failed. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String deleteProcess(String processInstanceID) throws OntologyErrorException;

    /**
     * Gets the finished processes.
     * 
     * @param userID the user id, null not allowed
     * @return an array of finished processes. If no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Process[] getFinishedProcesses(final String userID) throws OntologyErrorException;

    /**
     * Gets the registered information types from the ontology (Version >= 3.0.0).
     * 
     * @return an array of information types. If no items exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    InformationType[] getInformationTypes() throws OntologyErrorException;

    /**
     * Gets the processinformation. A PROCESSINFORMATION_CHART or {@link Parameter}.
     * 
     * @param processInformationID ID of the PROCESSINFORMATION. not null.
     * @param referenceID the relation ID. not null.
     * @return {@link Parameter} null is not possible.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Parameter getProcessInformation(String processInformationID, String referenceID) throws OntologyErrorException;

    /**
     * Gets the process parameter list.
     * 
     * @param processInstanceID the process instance id. not null.
     * @return a parameter list. if no item exists an empty list is returned {@link ParameterArray}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ParameterArray getProcessParameters(String processInstanceID) throws OntologyErrorException;

    /**
     * Gets the process template parameters.
     * 
     * @param processTemplateID the process template id. not null.
     * @return an array of {@link Parameter}. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ParameterArray getProcessTemplateParameters(String processTemplateID) throws OntologyErrorException;

    /**
     * Gets the path of the generated Html folder on the machine, from where the model was updated.
     * 
     * @param processID the ID of the process. not null.
     * @return the path.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getProcessBrowserChartPath(String processID) throws OntologyErrorException;

    /**
     * Sets the value of a {@link Parameter} slot.
     * 
     * @param parameterID parameter id. not null.
     * @param slotID slot id. not null.
     * @param infoType the InformationType ID. not null.
     * @param value the slot value. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setProcessInformationValue(String parameterID, String slotID, ObjectArray value, String infoType) throws OntologyErrorException;

    /**
     * Clears a slot value.
     * 
     * @param instanceID the id of the instance to clear the slot for. not null.
     * @param slotID the slot id. not null.
     * @param oldValue the old value(the value to be cleared) not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void clearSlotValue(String instanceID, String slotID, String oldValue) throws OntologyErrorException;

    /**
     * Gets the activity Input {@link Product}.
     * 
     * @param activityID the ID of the activity. non null
     * @return not null {@link ObjectArray} of input {@link Product}. If no item exists an empty array is returned.
     * 
     *         TODO: die Methode sollte evtl. aufgeräumt werden. Besonderes in den untermethoden sind viele Aufrufe, die nicht gebraucht werden.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProductArray getActivityInput(String activityID) throws OntologyErrorException;

    /**
     * Gets the activity Output {@link Product}.
     * 
     * @param activityID the ID of the activity.
     * @return not null {@link ObjectArray} of input {@link Product}. If no item exists an empty array is returned.
     * 
     *         TODO: die Methode sollte evtl. aufgeräumt werden. Besonderes in den untermethoden sind viele Aufrufe, die nicht gebraucht werden.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProductArray getActivityOutput(String activityID) throws OntologyErrorException;

    /**
     * Gets the PROCESSINFORMATION_CHART {@link Parameter}.
     * 
     * @param referenceID the ID of the {@link Product}, where the {@link Parameter} is defined.
     * @return not null {@link ObjectArray} with the IDs of the {@link Parameter} defined in the {@link Product}. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getProductProcessInformation(String referenceID) throws OntologyErrorException;

    /**
     * Gets the information type ID.
     * 
     * @param referenceID a frame that ist_vom_informationtype not null.
     * @return id. Null is possible, if no the slot is not set.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    DenoationID getInformationTypeID(String referenceID) throws OntologyErrorException;

    /**
     * Gets the informationType from its id.
     * 
     * @param id not null ID.
     * @return {@link InformationType}. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    InformationType getInformationType(String id) throws OntologyErrorException;

    /**
     * Creates a new KnowledgeObject and add it to a ProcessElement relation.
     * 
     * @param knowledgeObjectID the ID of the knowledgeObject. not null.
     * @param name the name of the KnowledgeLink. not null.
     * @param wsID ID of repository. not null.
     * @param link the link. not null.
     * @return {@link KnowledgeLink}. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeLink createKnowledgeLink(String knowledgeObjectID, String name, String wsID, String link) throws OntologyErrorException;

    /**
     * Updates a {@link KnowledgeObject}.Renames, Sets the list of dominators persons and the {@link KnowledgeLink}..
     * 
     * @param knowledgeObjectID not null ID of the {@link KnowledgeObject}.
     * @param name not null new name.
     * @param knowledgeLinks not null list of the {@link KnowledgeLink} to be assigned to this {@link KnowledgeObject}.
     * @param dominators a not null list of the dominators persons.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void editKnowledgeObject(String knowledgeObjectID, String name, ObjectArray knowledgeLinks, PersonArray dominators) throws OntologyErrorException;

    /**
     * Updates a {@link KnowledgeObject}.Renames, Sets the list of dominators persons and the {@link KnowledgeLink}.
     * 
     * @param knowledgeObject the {@link KnowledgeLink}. not null.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    void updateKnowledgeObject(KnowledgeObject knowledgeObject) throws OntologyErrorException;

    /**
     * Assignes the person with id = personID {@link Person}<br/>
     * to the role with the id = roleID {@link Role}.
     * 
     * @param roleID not null roleID.
     * @param personID not null personID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void assigneRole(String roleID, String personID) throws OntologyErrorException;

    /**
     * Assignes the person with id = personID {@link Person}<br/>
     * to the role with the id = roleID {@link Role}.
     * 
     * @param roleID not null roleID.
     * @param processStarter not null personID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setProcessStarter(String roleID, PersonArray processStarter) throws OntologyErrorException;

    /**
     * Assignes the person with id = personID {@link Person}<br/>
     * to the role with the id = roleID {@link Role}.
     * 
     * @param roleID not null roleID.
     * @return personArray List of persons, which can start a process. Not null. if no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    PersonArray getProcessStarter(String roleID) throws OntologyErrorException;

    /**
     * Write the selectedValues of the parameter param in the ontology.
     * 
     * @param param the parameter. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setParameterValue(Parameter param) throws OntologyErrorException;

    /**
     * Sets the values of the output {@link Product} to the activity.
     * 
     * @param outputProducts not null list of output {@link Product} to be set.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void saveActivityOutputProducts(ProductArray outputProducts) throws OntologyErrorException;

    /**
     * Searches for a keyword in the Knowledge Objects, Knowledge Link, Knowledge Repository.
     * 
     * @param pattern not null pattern.
     * @return not null {@link ObjectArray}. If no item exists an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray searchKeyWord(String pattern) throws OntologyErrorException;

    /**
     * Searchs in the name of KnowledgeObjects for the given pattern.
     * 
     * @param pattern the pattern to be searched. not null.
     * @return not null {@link ObjectArray}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObjectArray searchLinks(String pattern) throws OntologyErrorException;

    /**
     * Generates a process information.
     * 
     * @param frameID the ID of the frame that assign the process information. not null.
     * @return the ID of the generated PROCESSINFORMATION. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String generateProcessInformation(String frameID) throws OntologyErrorException;

    /**
     * Gets the ID of the processinformation that stores the uuid. The uuid is the id of the DMS node in Alfresco storing the XML file. <br>
     * containing the Chart of the process.
     * 
     * @param processID the ID of the process. not null
     * @return ID of the processinformation, null is possible if no process information found.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getChartProcessInformationID(String processID) throws OntologyErrorException;

    /**
     * 
     * Get all processes which are stored, independent from state release or not.
     * 
     * @return Process[], If no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Process[] getModels() throws OntologyErrorException;

    /**
     * Sets the informationtype to a reference.
     * 
     * @param referenceID not null referenceID.
     * @param informationTypeID not null informationType ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setInformationType(String referenceID, String informationTypeID) throws OntologyErrorException;

    /**
     * Check, if the given process is a sub process.
     * 
     * @param processID ID of given process.
     * @return true, if the given process is a sub process, else flase
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isSubProcess(String processID) throws OntologyErrorException;

    /**
     * Get the set of roles, which are define global and are not a instance of other roles (templates).
     * 
     * @return RoleArray , If no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    RoleArray getGlobalRoles() throws OntologyErrorException;

    /**
     * Gets the subprocesses list.
     * 
     * @param processID not null process ID.
     * @return not null {@link ProcessArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessArray getSubProcesses(String processID) throws OntologyErrorException;

    /**
     * Get the set of means, which are define global and are not a instance of other means (templates).
     * 
     * @return ObjectArray, If no item exists an empty array is returned. not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getGlobalMeans() throws OntologyErrorException;

    /**
     * Get the set of results memory, which are define global and are not a instance of other results memory (templates).
     * 
     * @return {@link ObjectArray} not null. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getGlobalResultsMem() throws OntologyErrorException;

    /**
     * {@link ProcessRemote#getSubProcessTypes}.
     * 
     * @param processTypeID not null process type ID.
     * @return not null {@link ProcessTypeArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessTypeArray getSubProcessTypes(String processTypeID) throws OntologyErrorException;

    /**
     * {@link ProcessRemote#getTypeProcesses}.
     * 
     * @param processTypeID not null process type ID.
     * @return not null {@link ProcessArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessArray getProcessesOfType(String processTypeID) throws OntologyErrorException;

    /**
     * {@link ProcessRemote#getEnabledProcesses(String)}.
     * 
     * @param processTypeID not null process type ID.
     * @return not null {@link ProcessArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessArray getEnabledProcesses(String processTypeID) throws OntologyErrorException;

    /**
     * See {@link ProcessEngine#getStartRole(String)}
     * 
     * @param processID {@link ProcessEngine#getStartRole(String)}
     * @return {@link ProcessEngine#getStartRole(String)} non null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getStartingRole(String processID) throws OntologyErrorException;

    /**
     * 
     * See {@link ProcessEngine#getElementsOfProcess(String)}
     * 
     * @param processID {@link ProcessEngine#getElementsOfProcess(String)}
     * @return {@link StringArray} containing all element IDs
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getElementsOfProcess(String processID) throws OntologyErrorException;

    /**
     * Gets the process, where the entity is defined.
     * 
     * @param entityID the not null entity ID.
     * @return StringArray List of process id´s or empty if the entity is not assigned to any process.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    StringArray getParentProcess(String entityID) throws OntologyErrorException;

    /**
     * Creates a new version of the given process template ID.
     * 
     * @param templateID the ID of the process template to create a new version for, may not be null
     * @param versionName the name of the new version given by the user, may not be null
     * @param alfrescoVersion the version in the alfresco dms, may not be null
     * @return the new process template ID, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String createNewVersion(String templateID, String versionName, String alfrescoVersion) throws OntologyErrorException;

    /**
     * Reactivates a version for the given process template.
     * 
     * The method retrieves the correct version, deactivates the current version and activates the given version.
     * 
     * @param instanceID the ID of the process instance to activate an old version for, the template ID does not need to be the initial version, may not be null
     * @param versionName the name of the new version to reactivate, may not be null
     * @param makeEditable if true, the version will be editable (means, the newest version will be replaced in the DMS)
     * @return the status
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error occurs in DMS back end
     */
    String activateVersion(String instanceID, String versionName, boolean makeEditable) throws OntologyErrorException, DMSException;

    /**
     * <p>
     * Returns all process versions of a given process template or process instance. The initial process template is added too!
     * </p>
     * 
     * <p>
     * If a template ID is given, the initial template is searched and the versions returned.
     * </p>
     * 
     * <p>
     * If the given ID is a process instance, the template is searched for and then the initial template ID.
     * </p>
     * 
     * <p>
     * The processes returned already contain the correct alfresco version and user defined version fields.
     * </p>
     * 
     * @param instanceID the instance of the process or process template, may not be null
     * @return {@link ProcessArray}, never null, at least the initial template process has to be inside the returned array, otherwise there is a data inconsistency
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessArray getProcessVersions(String instanceID) throws OntologyErrorException;

    /**
     * Returns <code>true</code> if the given process template ID is the initial template.
     * 
     * The checking is done by using the {@link ProcessEngine#getInitialTemplateID(String)} method and comparing the result.
     * 
     * @param processID the process template ID to check, has to be a process template, no instance, may not be null
     * @return true if process is initial template, false otherwise
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isInitialTemplate(String processID) throws OntologyErrorException;

    /**
     * <p>
     * Returns the alfresco version for the given process template ID.
     * </p>
     * 
     * <p>
     * Returns null in case the given ID is not a process template or an error occurred.
     * </p>
     * 
     * @param processID the ID of the process template, may not be null
     * @return the alfresco version, can be null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getAlfrescoVersion(String processID) throws OntologyErrorException;

    /**
     * <p>
     * Returns the user defined version for the given process template ID.
     * </p>
     * 
     * <p>
     * Returns null in case the given ID is not a process template or an error occurred.
     * </p>
     * 
     * @param processID the ID of the process template, may not be null
     * @return the user defined version, can be null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getUserDefinedVersion(String processID) throws OntologyErrorException;

    /**
     * Sets the value of a slot.
     * 
     * @param frameID the frame to set the slot for, may not be null
     * @param slotID slot id, the slot to set the value to, may not be null
     * @param value the slot value, can only be strings! No booleans working
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException;

    /**
     * Maps the xml elements of the given old XML string to the new elements and returns the mapped xml to be stored in the Alfresco DMS.
     * 
     * @param newProcessVersionID the ID of the newly created process, needed to retrieve the process elements, may not be null
     * @param xml the XML string the mapping has to be done to, may not be null
     * @return the new mapped XMl string, never null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String mapXMLElements(String newProcessVersionID, String xml) throws OntologyErrorException;

    /**
     * Gets the id of {@link Subprocess} to given {@link Activity}id.
     * 
     * @param activityID not null {@link Activity} id.
     * @return {@link String}. Not null.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    String getSubProcessOfActivity(String activityID) throws OntologyErrorException;

    /**
     * Removes the "process activated" style element from the given XML.
     * 
     * @param xml the xml string to parse
     * @return the new xml string with removed style, never null
     */
    String removeActivatedStateFromXML(String xml);

    /**
     * Returns the persons who are allowed to start the given process.
     * 
     * @param processID the process ID to return the starters for, may not be null
     * @return {@link PersonArray} with all persons who are allowed to start the given process, never null
     * @throws OntologyErrorException if something goes wrong in the ontology backend
     */
    PersonArray getProcessStartersForProcess(String processID) throws OntologyErrorException;

    /**
     * Gets the activity ID that call the input subprocess.
     * 
     * @param subprocessID not null subprocess ID.
     * @return the ID of the activity that call the subprocess or null if no process uses the selected subprocess.
     * @throws OntologyErrorException if an error occurs in the ontology back end.
     */
    StringArray getSubprocessCaller(String subprocessID) throws OntologyErrorException;

    /**
     * Adds a new ProcessInformation to a product.
     * 
     * @param productID not null product ID.
     * @throws OntologyErrorException if an error occurs in the ontology back end.
     */
    void addProcessInformationToProduct(String productID) throws OntologyErrorException;

    /**
     * 
     * Clone the given process template id. The new process is a complete clone of the old process. <br>
     * 
     * @param templateID id of given process. Not null.
     * @param newProcessName Name of given new process. Not null.
     * @param newPrcessDescription Description of new process. Not null.
     * @return <code>true</code> if the action is successful, else <code>false</code>
     * @throws OntologyErrorException if an error in the ontology backend occurs
     * @throws DMSException if an error occurs in DMS back end
     */
    boolean cloneProcess(String templateID, String newProcessName, String newPrcessDescription) throws OntologyErrorException, DMSException;

    /**
     * Returns a {@link Process} if it exists. else null.
     * 
     * @param processID the ID of the process, may not be null
     * @return the {@link Process}. null, if process does not exist.
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    Process getProcess(String processID) throws OntologyErrorException;

    /**
     * 
     * Check if the {@link Process} is defied as process landscape.
     * 
     * @param processID ID of given {@link Process}. Not null.
     * @return <code>true</code> if the {@link Process} is landscape, else <code>false</code>
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    boolean isProcessLandscape(String processID) throws OntologyErrorException;

}
