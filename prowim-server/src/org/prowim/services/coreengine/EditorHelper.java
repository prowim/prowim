/*==============================================================================
 * Project: ProWim
 * File $Id$
 *
 * $LastChangedDate: 2011-04-14 13:29:20 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/EditorHelper.java $
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
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.editor.InstancePropertiesMap;
import org.prowim.datamodel.editor.InstancePropertiesNames;
import org.prowim.datamodel.editor.InstancePropertiesValues;
import org.prowim.datamodel.prowim.Process;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.editor.EditorBean;
import org.prowim.services.ejb.editor.EditorRemote;



/**
 * This interface calls the {@link EditorEngine} interface to creates process models.
 * 
 * @author Saad Wardi
 * @version $Revision: 5075 $
 */
@Local
public interface EditorHelper
{
    /**
     * Creates a prowim model editor object.<br>
     * Prowim object IDs are those: Prozess, Aktivität, Mittel, ProzessInformation, etc
     * 
     * @param oid the selected object ID to be created.
     * @param modelID The ID of the model, where the elements must be added. Null is possible. <br>
     *        Null can occur if the model itself must be created.
     * @return the generated ID from the ontology.
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    String createObject(String oid, String modelID) throws OntologyErrorException;

    /**
     * Deletes an instance from the ontology.
     * 
     * @param instanceID the instance ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void deleteInstance(String instanceID) throws OntologyErrorException;

    /**
     * Gets the relations names to an instance.
     * 
     * @param id the instance ID.
     * @return {@link InstancePropertiesNames}
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    InstancePropertiesMap getInstanceRelations(String id) throws OntologyErrorException;

    /**
     * Gets the relations values to an instance.
     * 
     * @param id the instance ID.
     * @return {@link InstancePropertiesNames}
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    InstancePropertiesValues getInstanceRelationsValues(String id) throws OntologyErrorException;

    /**
     * Sets the element with id = elementID to the model with id = modelID.<br>
     * A modelID is for example: the process ID for process management
     * 
     * @param modelID not null ID. For example: a process ID for process management
     * @param elementID not null ID. For example: an activity ID.
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    void setElementToModel(String modelID, String elementID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#isProcessApproved(String)}.
     * 
     * @param processID the not null model ID.
     * @return true, if model is set to "free" state
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    boolean isProcessApproved(String processID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#approveProcess(String)}.
     * 
     * @param processID the not null model ID.
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    void approveProcess(String processID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#disapproveProcess(String)}.
     * 
     * @param processID the not null model ID.
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    void disapproveProcess(String processID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getModels()}.
     * 
     * @return not null model ID and Name array. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error in the ontology backend occurs
     */
    String[] getModels() throws OntologyErrorException;

    /**
     * {@link EditorRemote#saveModelAsXML(String, String, Boolean, String)}.
     * 
     * @param modelID the not null model ID.
     * @param xml the not null model representation as xml.
     * @param username the username logged in.
     * @param createNewVersion if set to <code>true</code>, a new version of the model is created and set to active
     * @param versionName the name of the version to create if createNewVersion is true, may be null, if createNewVersion is false
     * @return if a new version is created the process ID of the new version is being returned, otherwise null
     * @throws DMSException if an error in the DMS backend occurs
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String saveModelAsXML(String modelID, String xml, String username, boolean createNewVersion, String versionName) throws DMSException,
                                                                                                                    OntologyErrorException;

    /**
     * {@link EditorRemote#loadModelAsXML(String)}.
     * 
     * @param modelID the not null model ID.
     * @return not null model representation as xml.
     * @throws DMSException if an error occurs in the DMS backend
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String loadModelAsXML(String modelID) throws DMSException, OntologyErrorException;

    /**
     * {@link EditorRemote#connectActivityControlFlow(String, String, String)}.
     * 
     * @param sourceID not null source activity ID.
     * @param targetID not null target activity ID.
     * @param controlFlowID not null controlflow ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void connectActivityControlFlow(String sourceID, String targetID, String controlFlowID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#connectActivityRole(String, String, String)}.
     * 
     * @param activityID not null activity ID.
     * @param roleID not null role ID.
     * @param taskID not null task (Tätigkeit) ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void connectActivityRole(String activityID, String roleID, String taskID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getPossibleRelations(String, String)}.
     * 
     * @param sourceID not null source ID.
     * @param targetID not null target ID.
     * @return not null array of relations. IF no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String[] getRelations(String sourceID, String targetID) throws OntologyErrorException;

    /**
     * Sets a relation to an object.
     * 
     * @param instanceID not null instance ID.
     * @param slot not null slot.
     * @param relationID not null relation ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setRelationValue(String instanceID, String slot, String relationID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#setProduct(String, String, String)}.
     * 
     * @param source not null source activity ID.
     * @param target not null target activity ID.
     * @param productID not null product ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setProduct(String source, String target, String productID) throws OntologyErrorException;

    /**
     * Removes a relation to an object.
     * 
     * @param instanceID not null instance ID.
     * @param slot not null slot.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void removeRelationValue(String instanceID, String slot) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getPossibleCombinationRules()}.
     * 
     * @return a not null array of combination rules. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String[] getCombinationRules() throws OntologyErrorException;

    /**
     * {@link EditorRemote#setCombinationRule(String, String)}
     * 
     * @param controlFlowID not null controlflow ID.
     * @param combinationRule not null combination rule.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setCombinationRule(String controlFlowID, String combinationRule) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getPossibleActivationRules()}.
     * 
     * @return a not null array of activation rules. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String[] getActivationRules() throws OntologyErrorException;

    /**
     * {@link EditorRemote#setActivationRule(String, String)}
     * 
     * @param objectID not null object ID.
     * @param activationRule not null combination rule.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setActivationRule(String objectID, String activationRule) throws OntologyErrorException;

    /**
     * {@link EditorRemote#connectActivityMittel(String, String, String)}.
     * 
     * @param activityID not null activity ID.
     * @param mittelID not null mittel ID.
     * @param functionID not null function ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void connectActivityMittel(String activityID, String mittelID, String functionID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getPossibleOrganizationalUnits()}.
     * 
     * @return not null array of organisations.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String[] getOrganizationalUnits() throws OntologyErrorException;

    /**
     * {@link EditorRemote#getCombinationRule(String)}.
     * 
     * @param controlflowID not null control flow ID.
     * @return combination rule.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
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
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String bendControlFlow(String controlflowID, String oldSource, String oldTarget, String newSource, String newTarget)
                                                                                                                        throws OntologyErrorException;

    /**
     * Sets the scope to an instance.
     * 
     * @param instanceID not null instance ID.
     * @param scope the not null scope. Possible values: <global, local>
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setScope(String instanceID, String scope) throws OntologyErrorException;

    /**
     * Gets the count of the controlflows.
     * 
     * @param targetID the not null ellement ID.
     * @return not null count of controlflows.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    String getControlFlowCount(String targetID) throws OntologyErrorException;

    /**
     * Gets the existing subprocesses.
     * 
     * @return not null subprocesses.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    ProcessArray getAllExistSubProcesses() throws OntologyErrorException;

    /**
     * Sets an activity as subprocess.
     * 
     * @param activityID the not null activity ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setSubProcessFlagForActivity(String activityID) throws OntologyErrorException;

    /**
     * Sets a process as subprocess.
     * 
     * @param processID the not null process ID.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setSubProcessFlagForProcess(String processID) throws OntologyErrorException;

    /**
     * Sets a activity as subprocess.
     * 
     * @param subProcessID id of sub process. Not null.
     * @param activityID id of activity. Not null.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setSubProcessOfActivity(String subProcessID, String activityID) throws OntologyErrorException;

    /**
     * Set a activity slot "Autovorgang" as true or false.
     * 
     * @param activityID Activity id. Not null.
     * @param string Can be true or false. Not null.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void setActivityOperation(String activityID, String string) throws OntologyErrorException;

    /**
     * Deletes the relation between a element and its model. Model is a process.
     * 
     * @param processID id of process. Not null.
     * @param elementID id of element. Not null.
     * @throws OntologyErrorException if an error occurs in the ontology backend
     */
    void deleteElementFromModel(String processID, String elementID) throws OntologyErrorException;

    /**
     * Creates an object without assign it to a process.
     * 
     * @param oid ID of the object class "Activity, Role, Mean etc". Not null.
     * @param name the instance name. Not null.
     * @param isGlobal the visibility of the instance. Not null.
     * @return The Id of the created object.
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     * @see EditorBean#createObject(String, String)
     */
    String createObject(String oid, String name, boolean isGlobal) throws OntologyErrorException;

    /**
     * Sets a {@link Process} as landscape or not depend on flag.
     * 
     * @param processID id of {@link Process}. Not null
     * @param flag <code>true</code> to set a {@link Process} as landscape, <code>false</code> to set it back.
     * 
     * @return <code>true</code> if the command is successfully, else <code>false</code>
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    boolean setProcessLandscapeFlag(String processID, boolean flag) throws OntologyErrorException;
}
