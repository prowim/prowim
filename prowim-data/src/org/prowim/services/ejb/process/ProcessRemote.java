/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-07 17:07:13 +0200 (Mi, 07 Jul 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/process/ProcessRemote.java $
 * $LastChangedRevision: 4243 $
 *------------------------------------------------------------------------------
 * (c) 26.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 */
package org.prowim.services.ejb.process;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.InformationTypeArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.collections.ProcessTypesArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.factory.ProwimDataObjectFactory;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Collects all methods to start, stop, delete and receive {@link Process} Objects and process metadata.
 * 
 * @author Saad Wardi
 * @version $Revision: 4243 $
 */

@WebService(name = ServiceConstants.PROWIM_PROCESS_REMOTE, targetNamespace = ServiceConstants.PROWIM_PROCESS_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({ ProwimDataObjectFactory.class, Organization.class })
public interface ProcessRemote
{

    /**
     * 
     * Gets all processes, where the start role is assigned to currently logged in user.
     * 
     * @param userID Id of logged user
     * @return allowed value {@link ProcessArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getExecutableProcesses(String userID) throws OntologyErrorException;

    /**
     * 
     * Running processes are the processes, wich the workflow started and still running. <br/>
     * It is filtered by the currently logged in user, who was involved in those processes.<br/>
     * Independent from the currently process state.
     * 
     * @param userID TODO
     * 
     * @return allowed value {@link ProcessArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getRunningProcesses(String userID) throws OntologyErrorException;

    /**
     * Delete a workflow (a running process).
     * 
     * @param processInstanceID theid of the running process.
     * @return status
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String deleteProcess(String processInstanceID) throws OntologyErrorException;

    /**
     * Finished processes are the processes, wich the workflow finished. <br/>
     * It is filtered by the currently logged in user, who was involved in those processes.
     * 
     * @param userID TODO
     * 
     * @return an array of finished processes. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getFinishedProcesses(String userID) throws OntologyErrorException;

    /**
     * Gets the list of informationtype registered in the ontology.
     * 
     * @return an array of informationtyp. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    InformationTypeArray getInformationTypes() throws OntologyErrorException;

    /**
     * Gets the process {@link Parameter}.
     * 
     * @param processInstanceID the process instance id.
     * @return an array of process parameter. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ParameterArray getProcessParameters(String processInstanceID) throws OntologyErrorException;

    /**
     * Gets the process template parameters.
     * 
     * @param processTemplateID the process template id.
     * @return an array of {@link Parameter}. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ParameterArray getProcessTemplateParameters(String processTemplateID) throws OntologyErrorException;

    /**
     * Gets the path of the generated Html folder on the machine, from where the model was updated.
     * 
     * @param processID the ID of the process.
     * @return the path.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getProcessBrowserChartPath(String processID) throws OntologyErrorException;

    /**
     * Generates a processinformation.
     * 
     * @param processID not null ID of the process that assigne the processinformation
     * @return not null ID of the generated PROCESSINFORMATION_CHART or throws {@link IllegalStateException}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getBrowserProcessInformationID(String processID) throws OntologyErrorException;

    /**
     * Gets the URL of the chart used on the process-browser for a process.
     * 
     * @param processID ID of the process.
     * @return not null URL or an exception is thrown if for example no html data exists.
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    String getProcessBrowserChartURL(String processID) throws OntologyErrorException, DMSException;

    /**
     * Binding the JAXB.
     * 
     * @return {@link ControlFlow}
     */
    @WebMethod
    @WebResult(partName = "return")
    ControlFlow getControlFlow();

    /**
     * Gets the ID and the name of all models templates. The function returns a list of strings. In each list item is the ID and the name of a model.
     * 
     * @return not null model ID and Name array. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getModels() throws OntologyErrorException;

    /**
     * Gets the process type of given process ID
     * 
     * @param processID ID of the process. Not null.
     * @return process type of given process. Null is returned if not found.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessType getProcessType(String processID) throws OntologyErrorException;

    /**
     * Gets all process types which are included in system.
     * 
     * @return process types. Not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessTypeArray getAllProcessTypes() throws OntologyErrorException;

    /**
     * Gets all top processtypes which are included in the system.
     * 
     * @return process types. Not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessTypeArray getAllTopProcessTypes() throws OntologyErrorException;

    /**
     * Gets the subprocesstypes to a given type.
     * 
     * @param processTypeID not null ID of the parent type.
     * @return not null {@link ProcessTypesArray}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessTypeArray getSubProcessTypes(String processTypeID) throws OntologyErrorException;

    /**
     * Gets the process that have the type with id = processTypeID.
     * 
     * @param processTypeID not null ID of a processtype.
     * @return not null {@link ProcessArray}. If no items exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessArray getTypeProcesses(String processTypeID) throws OntologyErrorException;

    /**
     * Gets the enabled process that have the type with id = processTypeID.
     * 
     * @param processTypeID not null ID of a {@link ProcessType}.
     * @return not null {@link ProcessArray}. If no items exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessArray getEnabledProcesses(String processTypeID) throws OntologyErrorException;

    /**
     * 
     * Create a new process type .
     * 
     * @param name Name of process type. Not null.
     * @param description Description of process type. Not null.
     * 
     * @return process types. Not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String createProcessType(String name, String description) throws OntologyErrorException;

    /**
     * Sets the parent type to a processtype.
     * 
     * @param typeID the not null ProcessType ID.
     * @param parentTypeID not null parent ProcessTypeID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void setProcessTypeParent(String typeID, String parentTypeID) throws OntologyErrorException;

    /**
     * Sets the process type of given process ID
     * 
     * @param processTypeID ID of the process type. Not null.
     * @param processID ID of the process. Not null.
     * 
     * @return process type of given process. Not null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String setProcessType(String processTypeID, String processID) throws OntologyErrorException;

    /**
     * Gets the process information of given object ID. This can be e.g "Entscheidung" or "Produkt"
     * 
     * @param referenceID ID of the process type. Not null.
     * 
     * @return not null list of processinformations. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray getProductsOfActivity(String referenceID) throws OntologyErrorException;

    /**
     * Sets the informationtype to an object.
     * 
     * @param referenceID the not null ID of a frame.
     * @param informationTypeID the not null ID of an InformationType class.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setInformationType(String referenceID, String informationTypeID) throws OntologyErrorException;

    /**
     * Set the set of person, that can start the process.
     * 
     * @param roleID not null role ID.
     * @param processStarter not null list of persons.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setProcessStarter(String roleID, PersonArray processStarter) throws OntologyErrorException;

    /**
     * Get the set of person, that can start the process.
     * 
     * @param roleID not null role ID.
     * @return PersonArray Person, which can start a process.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    PersonArray getProcessStarter(String roleID) throws OntologyErrorException;

    /**
     * Get the set of roles, which are define global and are not a instance of other roles (templates).
     * 
     * @return RoleArray Roles, which are global.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RoleArray getGlobalRoles() throws OntologyErrorException;

    /**
     * Get the set of means, which are define global and are not a instance of other means (templates).
     * 
     * @return ObjectArray Means, which are global.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray getGlobalMeans() throws OntologyErrorException;

    /**
     * Get the set of results memory, which are define global and are not a instance of other results memory (templates).
     * 
     * @return ObjectArray results memory, which are global.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray getGlobalResultsMem() throws OntologyErrorException;

    /**
     * Check if given process is a sub process.
     * 
     * @param processID Id of given process
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    boolean isSubProcess(String processID) throws OntologyErrorException;

    /**
     * Creates a new Role and bind it to an activity.
     * 
     * @param processID not null process ID.
     * @param activityID not null activity ID.
     * @return not null role ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String createRole(String processID, String activityID) throws OntologyErrorException;

    /**
     * Creates a new Mean and bind it to an activity.
     * 
     * @param processID not null process ID.
     * @param activityID not null activity ID.
     * @return not null mean ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String createMean(String processID, String activityID) throws OntologyErrorException;

    /**
     * Creates a new instnace of role and set it global.
     * 
     * @param name the instance name.
     * @param classname the class name.
     * @param isGlobal flag is true if the instance is global.
     * @return not null mean ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */

    @WebMethod
    @WebResult(partName = "return")
    String createProcessElement(String classname, String name, boolean isGlobal) throws OntologyErrorException;

    /**
     * Gets the subprocesses list.
     * 
     * @param processID not null process ID.
     * @return not null {@link ProcessArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getSubProcesses(String processID) throws OntologyErrorException;

    /**
     * Sets the element with id = elementID to the model with id = modelID.<br>
     * A modelID is for example: the process ID for process management
     * 
     * @param modelID not null ID. For example: a process ID for process management
     * @param elementID not null ID. For example: an activity ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void setElementToModel(String modelID, String elementID) throws OntologyErrorException;

    /**
     * 
     * Returns all {@link ProcessElement}s for given {@link Process}id.
     * 
     * @param processID ID of given {@link Process}. Not null.
     * @return {@link StringArray}. Not Null.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray getElementsOfProcess(String processID) throws OntologyErrorException;

    /**
     * Gets the process, where the entity is defined.
     * 
     * @param entityID the not null entity ID.
     * @return process ID or null if the entity is not assigned to any process.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    StringArray getParentProcess(String entityID) throws OntologyErrorException;

    /**
     * Gets the processes that includes the input subprocess.
     * 
     * @param subprocessID not null subprocess ID.
     * @return not null list of processes IDs. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getSubprocessReferences(String subprocessID) throws OntologyErrorException;

    /**
     * Gets the id of Subprocess to given {@link Activity}id.
     * 
     * @param activityID not null {@link Activity} id.
     * @return {@link String}. null, if not subporcess find. Else id of subprocess
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getSubProcessOfActivity(String activityID) throws OntologyErrorException;

    /**
     * Adds a new process information to a product.
     * 
     * @param productID not null product ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void addProcessInformationToProduct(String productID) throws OntologyErrorException;

    /**
     * 
     * 
     * This method is only to import the data model {@link ProcessInformation} and should not used.
     * 
     * @return ProcessInformation
     */
    @WebMethod
    @WebResult(partName = "return")
    @Deprecated
    ProcessInformation processInformation();

    /**
     * 
     * 
     * This method is only to import the data model {@link ResultsMemory} and should not used.
     * 
     * @return ResultsMemory
     */
    @WebMethod
    @WebResult(partName = "return")
    @Deprecated
    ResultsMemory resultsMemory();

    /**
     * 
     * 
     * This method is only to import the data model {@link Mean} and should not used.
     * 
     * @return Mean
     */
    @WebMethod
    @WebResult(partName = "return")
    @Deprecated
    Mean mean();

    /**
     * Creates a new version of the given process template ID.
     * 
     * @param templateID the ID of the process template to create a new version for, may not be null
     * @param versionName the name of the new version given by the user, may not be null
     * @param alfrescoVersion the version in the alfresco dms, may not be null
     * @return the status, never null
     * @see AlgernonConstants#OK
     * @see AlgernonConstants#FAILED
     * @see AlgernonConstants#ERROR
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    @WebMethod
    @WebResult(partName = "return")
    String createNewVersion(String templateID, String versionName, String alfrescoVersion) throws OntologyErrorException;

    /**
     * <p>
     * Reactivates a version for the given process template.
     * </p>
     * 
     * <p>
     * The method retrieves the correct version, deactivates the current version and activates the given version.
     * </p>
     * 
     * <p>
     * Throws an {@link IllegalStateException} on errors.
     * </p>
     * 
     * @param instanceID the ID of the process instance to activate an old version for, the template ID does not need to be the initial version, may not be null
     * @param versionName the name of the new version to reactivate, may not be null
     * @param makeEditable if true, the version will be editable (means, the newest version will be replaced in the DMS)
     * @return the status, never null
     * @throws OntologyErrorException if an error occurs in the ontology backend
     * @throws DMSException if an error occurs in the DMS backend
     * @see AlgernonConstants#OK
     * @see AlgernonConstants#FAILED
     */
    @WebMethod
    @WebResult(partName = "return")
    String activateVersion(String instanceID, String versionName, boolean makeEditable) throws OntologyErrorException, DMSException;

    /**
     * Returns a {@link ProcessArray} containing all versions of given process template ID.
     * 
     * @param instanceID the ID of the process template instance, may not be null
     * @return the {@link ProcessArray}, never null
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getProcessVersions(String instanceID) throws OntologyErrorException;

    /**
     * Returns the alfresco DMS version for the given process ID.
     * 
     * @param processID the ID of the process to return the alfresco version for, may not be null
     * @return the alfresco version string
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    String getAlfrescoVersion(String processID) throws OntologyErrorException;

    /**
     * Returns the user defined version for the given process ID.
     * 
     * @param processID the ID of the process to return the user defined version for, may not be null
     * @return the user defined version string
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    String getUserDefinedVersion(String processID) throws OntologyErrorException;

    /**
     * Returns the persons who are allowed to start the given process.
     * 
     * @param processID the process ID to return the starters for, may not be null
     * @return {@link PersonArray} with all persons who are allowed to start the given process, never null
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    PersonArray getProcessStartersForProcess(String processID) throws OntologyErrorException;

    /**
     * 
     * Clone the given process template id. The new process is a complete clone of the old process. <br>
     * 
     * @param templateID id of given process. Not null.
     * @param newProcessName Name of given new process. Not null.
     * @param newPrcessDescription Description of new process. Not null.
     * @return <code>true</code> if the action is successful, else <code>false</code>
     * @throws OntologyErrorException if an error in the ontology backend occurs
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    boolean cloneProcess(String templateID, String newProcessName, String newPrcessDescription) throws OntologyErrorException, DMSException;

    /**
     * Returns a {@link Process} if it exists. else null.
     * 
     * @param processID the ID of the process, may not be null
     * @return the {@link Process}. null, if process does not exist.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    @WebMethod
    @WebResult(partName = "return")
    Process getProcess(String processID) throws OntologyErrorException;

    /**
     * 
     * Check if the {@link Process} is defied as process landscape.
     * 
     * @param processID ID of given {@link Process}. Not null.
     * @return <code>true</code> if the {@link Process} is landscape, else <code>false</code>
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    boolean isProcessLandscape(String processID) throws OntologyErrorException;

}
