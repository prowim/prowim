/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-07 17:07:13 +0200 (Mi, 07 Jul 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/editor/EditorRemote.java $
 * $LastChangedRevision: 4243 $
 *------------------------------------------------------------------------------
 * (c) 16.10.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.ejb.editor;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Collects all methods which are necessary for the model editor, i.e. create objects or connect activities etc.
 * 
 * The model editor gives possibility to creates models like process.
 * 
 * @author Saad Wardi
 * @version $Revision: 4243 $
 */
@WebService(name = ServiceConstants.PROWIM_EDITOR_REMOTE, targetNamespace = ServiceConstants.PROWIM_EDITOR_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface EditorRemote
{
    /**
     * Creates a prowim model editor object.<br>
     * Prowim object IDs are those: Prozess, Aktivität, Mittel, ProzessInformation, etc
     * 
     * @param modelID The ID of the model, where the elements must be added. Must not be null. Give dummy string for new objects
     * @param oid the selected object ID to be created. Not null.
     * @return the generated ID from the ontology.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String createObject(String modelID, String oid) throws OntologyErrorException;

    /**
     * Deletes an instance from the ontology.
     * 
     * @param id instance ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void deleteObject(String id) throws OntologyErrorException;

    /**
     * Returns true, if a model is set to "free" state and can be started and updated.
     * 
     * @param processID not null model ID.
     * @return true, if the given model is open and in "free" state
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    boolean isProcessApproved(String processID) throws OntologyErrorException;

    /**
     * Opens a model and set it free to be started and updated.
     * 
     * @param processID not null model ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void approveProcess(String processID) throws OntologyErrorException;

    /**
     * Closes a model.
     * 
     * @param processID not null model ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void disapproveProcess(String processID) throws OntologyErrorException;

    /**
     * Gets the ID and the name of all models templates. The function returns a list of strings. In each list item is the ID and the name of a model.
     * 
     * @return not null model ID and Name array. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String[] getModels() throws OntologyErrorException;

    /**
     * Saves the model xml as XML.
     * 
     * @param modelID the not null model ID.
     * @param xml the xml representation of the model.
     * @param createNewVersion if <code>true</code>, a new version of the model is created
     * @param versionName the name of the version, if createNewVersion is true, can be null, if createNewVersion is false
     * @return the ID of the newly created process model, if createNewVersion was true, otherwise null
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    String saveModelAsXML(String modelID, String xml, Boolean createNewVersion, String versionName) throws OntologyErrorException, DMSException;

    /**
     * Loads the stored model as XML.
     * 
     * @param modelID the not null model ID.
     * @return not null xml representation of the model.
     * @throws OntologyErrorException if error occurs in ontology back end
     * @throws DMSException if an error in DMS back end occurs
     */
    @WebMethod
    @WebResult(partName = "return")
    String loadModelAsXML(String modelID) throws OntologyErrorException, DMSException;

    /**
     * Connects the activities.
     * 
     * @param sourceID not null source activityID. From where the proceeds controlflow.
     * @param targetID not null target activityID. Where goes the controlflow.
     * @param controlFlowID not null ID of the controlflow that connects the both activities.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void connectActivityControlFlow(String sourceID, String targetID, String controlFlowID) throws OntologyErrorException;

    /**
     * Connects activity and role with a task= "Tätigkeit".
     * 
     * @param activityID not null activityID to be connected.
     * @param roleID not null role ID.
     * @param taskID not null task ID (Tätigkeit ID).
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void connectActivityRole(String activityID, String roleID, String taskID) throws OntologyErrorException;

    /**
     * Gets all possible relations to the both objects.
     * 
     * @param sourceID not null ID of the source object.
     * @param targetID not null ID of the target object.
     * @return not null array of relations. IF no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String[] getPossibleRelations(String sourceID, String targetID) throws OntologyErrorException;

    /**
     * Sets a relation.
     * 
     * @param source not null instance ID.
     * @param relation not null slot.
     * @param value not null relation ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void setRelationValue(String source, String relation, String value) throws OntologyErrorException;

    /**
     * Sets a product from the source activity to the target activity.
     * 
     * @param source not null source activity ID.
     * @param target not null target activity ID.
     * @param productID not null product ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void setProduct(String source, String target, String productID) throws OntologyErrorException;

    /**
     * Description.
     * 
     * @param targetID not null ID of the target activity.
     * @return count of the incoming controlflows.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String controlFlowCount(String targetID) throws OntologyErrorException;

    /**
     * Removes a relation.
     * 
     * @param source not null instance ID.
     * @param relation not null slot.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void removeRelationValue(String source, String relation) throws OntologyErrorException;

    /**
     * Gets the registered combination rules.
     * 
     * @return a not null array of combination rules. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String[] getPossibleCombinationRules() throws OntologyErrorException;

    /**
     * Sets a combination rule to a controlflow.
     * 
     * @param controlFlowID not null controlflow ID.
     * @param combinationRule not null combinatinRule.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setCombinationRule(String controlFlowID, String combinationRule) throws OntologyErrorException;

    /**
     * Gets the registered activation rules.
     * 
     * @return a not null array of activation rules. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String[] getPossibleActivationRules() throws OntologyErrorException;

    /**
     * Sets a activation rule to a object. This can be class, which has the slot "hat_Aktivierungsregel" like "aktivität" or "Entscheidung"
     * 
     * @param objectID not null object ID.
     * @param activationRule not null activationRule.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setActivationRule(String objectID, String activationRule) throws OntologyErrorException;

    /**
     * Connects an activity with mittel.
     * 
     * @param activityID not null activity ID.
     * @param mittelID not null mittel ID.
     * @param functionID not null function ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void connectActivityMittel(String activityID, String mittelID, String functionID) throws OntologyErrorException;

    /**
     * Gets all registered organizations.
     * 
     * @return not null array of organizations names. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String[] getPossibleOrganizationalUnits() throws OntologyErrorException;

    /**
     * Gets the combination rule for a control flow.
     * 
     * @param controlflowID the not null control flow ID.
     * @return not null combination rule.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getCombinationRule(String controlflowID) throws OntologyErrorException;

    /**
     * Bends a control flow from an activity to another.
     * 
     * @param controlflowID not null controlflow ID.
     * @param oldSource not null old source activity ID.
     * @param oldTarget not null old target activity ID.
     * @param newSource not null new source activity ID.
     * @param newTarget not null new target activity ID.
     * @return SUCCEEDED or FAILED.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String bendControlFlow(String controlflowID, String oldSource, String oldTarget, String newSource, String newTarget)
                                                                                                                        throws OntologyErrorException;

    /**
     * Sets the process type of given process ID
     * 
     * @param processTypeID ID of the process type. Not null.
     * @param processID ID of the process. Not null.
     * 
     * @return process type of given process. Not null.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String setProcessType(String processTypeID, String processID) throws OntologyErrorException;

    /**
     * <p>
     * Get the ID of the {@link ProcessInformation} holding the DMS UUID for the XML model of the given {@link Process}.
     * </p>
     * 
     * <p>
     * Throws an IllegalArgumentException if not exactly one {@link ProcessInformation} is found for the specified objectID.
     * </p>
     * 
     * @param processID id of {@link Process}, may not be null
     * @return the ID of the {@link ProcessInformation} or null if no {@link ProcessInformation} was found
     * @see EditorRemote#getProcessInformationID(String)
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getProcessInformationID(String processID) throws OntologyErrorException;

    /**
     * Gets all exists sub processes
     * 
     * @return List of sub processes .
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getAllExistSubProcesses() throws OntologyErrorException;

    /**
     * Set flag of a activity "Vorgangstype" as sub process "Teilprozess".
     * 
     * @param activityID not null activity ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void setSubProcessFlagForActivity(String activityID) throws OntologyErrorException;

    /**
     * Set flag of a process "Subprozess" true.
     * 
     * @param processID not null process ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void setSubProcessFlagForProcess(String processID) throws OntologyErrorException;

    /**
     * Set the slot "hat_submodel" for a activity.
     * 
     * @param subProcessID ID of sub process. Not null.
     * @param activityID Id of activity. Not null
     * @throws OntologyErrorException if error occurs in ontology back end
     * 
     */
    @WebMethod
    void setSubProcessOfActivity(String subProcessID, String activityID) throws OntologyErrorException;

    /**
     * Set the slot "Autovorgang" for a activity as true.
     * 
     * @param activityID Id of activity. Not null
     * @throws OntologyErrorException if error occurs in ontology back end
     * 
     */
    @WebMethod
    void setActivityAsAuto(String activityID) throws OntologyErrorException;

    /**
     * Set the slot "Autovorgang" for a activity as false.
     * 
     * @param activityID Id of activity. Not null
     * @throws OntologyErrorException if error occurs in ontology back end
     * 
     */
    @WebMethod
    void setActivityAsManual(String activityID) throws OntologyErrorException;

    /**
     * 
     * Decides whether an activity is manual or automatic
     * 
     * @param activityId not null
     * @return guess what
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    boolean isActivityManual(String activityId) throws OntologyErrorException;

    /**
     * Deletes the relation between a element and its model. Model is a process
     * 
     * @param processID id of process.
     * @param elementID id of element.
     * @throws OntologyErrorException if error occurs in ontology back end
     * 
     */
    @WebMethod
    void deleteElementFromModel(String processID, String elementID) throws OntologyErrorException;

    /**
     * 
     * Sets a {@link Process} as landscape or not depend on flag.
     * 
     * @param processID id of {@link Process}. Not null
     * @param flag <code>true</code> to set a {@link Process} as landscape, <code>false</code> to set it back.
     * 
     * @return <code>true</code> if the command is successfully, else <code>false</code>
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    boolean setProcessLandscapeFlag(String processID, boolean flag) throws OntologyErrorException;

}
