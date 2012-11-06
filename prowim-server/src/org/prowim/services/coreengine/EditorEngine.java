/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-14 13:29:20 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/EditorEngine.java $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.prowim.Process;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.editor.EditorRemote;



/**
 * This defines the prowim server-application APIs that operate on the ontology to provide process modeling functionality.
 * 
 * @author Saad Wardi
 * @version $Revision: 5075 $
 */
@Local
public interface EditorEngine
{
    /**
     * Tests the scope of an object before create its instance.
     * 
     * @param oid not null object ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet testScope(String oid) throws OntologyErrorException;

    /**
     * Returns a recordset contains the type ID. Currently We distinguish 2 types : <br>
     * SYS_COMPONENT-CLASS and SYS_CONNECTION-CLASS
     * 
     * @see EditorEngineConstants
     * 
     * @param oid the object ID his typeID to be fetched.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getTypeID(String oid) throws OntologyErrorException;

    /**
     * Tests if the given ID is an ID of an existing frame in the ontology.
     * 
     * @param oid not null object ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet isFrame(String oid) throws OntologyErrorException;

    /**
     * Creates a new instance in the ontology.
     * 
     * @param oid not null object ID.
     * @param name not null name.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet addInstance(String oid, String name) throws OntologyErrorException;

    /**
     * Deletes an instance of a relation.
     * 
     * @param instanceID ID of the relation-instance.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet deleteInstance(String instanceID) throws OntologyErrorException;

    /**
     * Gets the count of instances.
     * 
     * @param oid object ID.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getInstanceCount(String oid) throws OntologyErrorException;

    /**
     * Sets the slot value with the elementID to the model with id = modelID.
     * 
     * @param slot not null slot.
     * @param modelID not null model ID.
     * @param elementID not null element ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setElementToModel(String slot, String modelID, String elementID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#isProcessApproved(String)}.
     * 
     * @param processID the model ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet isProcessApproved(String processID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#approveProcess(String)}.
     * 
     * @param processID the model ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet approveProcess(String processID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#disapproveProcess(String)}.
     * 
     * @param processID the model ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet disapproveProcess(String processID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getModels()}.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getModels() throws OntologyErrorException;

    /**
     * {@link EditorRemote#connectActivityControlFlow(String, String, String)}.
     * 
     * @param sourceID the not null source activity ID, from where proceeds the controlflow.
     * @param targetID the not null target activity ID, where the controlflow goes.
     * @param controlFlowID the not null controlflow ID, that connects the both activities.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet connectActivityControlFlow(String sourceID, String targetID, String controlFlowID) throws OntologyErrorException;

    /**
     * Sets the scope of an instance.
     * 
     * @param instanceID the not null instance ID.
     * @param scope the not null scope value. ["Local" , "Global"]
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setScope(String instanceID, String scope) throws OntologyErrorException;

    /**
     * {@link EditorRemote#connectActivityRole(String, String, String)}.
     * 
     * @param activityID not null activity ID.
     * @param roleID not null role ID.
     * @param taskID not null task (Tätigkeit) ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet connectActivityRole(String activityID, String roleID, String taskID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getPossibleRelations(String, String)}.
     * 
     * @param sourceID not null ID of the source object.
     * @param targetID not null ID of the target object.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getRelations(String sourceID, String targetID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#setRelationValue(String, String, String)}.
     * 
     * @param instanceID not null instance ID.
     * @param slot not null relation name.
     * @param relationID not null instance ID or relation instance ID as value for the relation (slot).
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setRelationValue(String instanceID, String slot, String relationID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#setProduct(String, String, String)}.
     * 
     * @param source not null source activity ID.
     * @param target not null target activity ID.
     * @param productID not null product ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setProduct(String source, String target, String productID) throws OntologyErrorException;

    /**
     * Checks if the processinformation has a valid DMS ID.
     * 
     * @param processInformationID not null processinformation ID
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet checkDMSIDforProcessInformation(String processInformationID) throws OntologyErrorException;

    /**
     * Description.
     * 
     * @param processInformationID not null information ID
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet isInformationType(String processInformationID) throws OntologyErrorException;

    /**
     * getControlFlowsCount.
     * 
     * @param targetID not null target activity ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getControlFlowsCount(String targetID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#removeRelationValue(String, String)}.
     * 
     * @param instanceID not null instance ID.
     * @param slot not null slot.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void removeRelationValue(String instanceID, String slot) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getPossibleCombinationRules()}.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getCombinationRules() throws OntologyErrorException;

    /**
     * {@link EditorRemote#setCombinationRule(String, String)}.
     * 
     * @param controlFlowID not null control flow ID.
     * @param combinationRule not null combination rule ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setCombinationRule(String controlFlowID, String combinationRule) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getPossibleActivationRules()}.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getActivationRules() throws OntologyErrorException;

    /**
     * {@link EditorRemote#setActivationRule(String, String)}
     * 
     * @param objectID not null object ID.
     * @param activationRule not null combination rule.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setActivationRule(String objectID, String activationRule) throws OntologyErrorException;

    /**
     * {@link EditorRemote#connectActivityMittel(String, String, String)}.
     * 
     * @param activityID not null activity ID.
     * @param mittelID not null mittel ID.
     * @param functionID not null function ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet connectActivityMittel(String activityID, String mittelID, String functionID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#getCombinationRule(String)}.
     * 
     * @param controlflowID not null control flow ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet getCombinationRule(String controlflowID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#setSubProcessFlagForActivity(String)}.
     * 
     * @param activityID ID of activity. not null.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setSubProcessFlagForActivity(String activityID) throws OntologyErrorException;

    /**
     * {@link EditorRemote#setSubProcessFlagForActivity(String)}.
     * 
     * @param processID ID of process. not null.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setSubProcessFlagForProcess(String processID) throws OntologyErrorException;

    /**
     * Set the slot "hat_submodel" for a activity.
     * 
     * @param subProcessID ID of sub process. Not null.
     * @param activityID Id of activity. Not null
     * @return RecordSet
     * @throws OntologyErrorException if an error occurs in the ontology back end
     * 
     */
    RecordSet setSubProcessOfActivity(String subProcessID, String activityID) throws OntologyErrorException;

    /**
     * Set activity as auto.
     * 
     * @param activityID id of activity
     * @param flag Can be true or false
     * @return RecordSet
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet setActivityOperation(String activityID, String flag) throws OntologyErrorException;

    /**
     * Deletes the relation between a element and its model. Model is a process
     * 
     * @param processID id of process.
     * @param elementID id of element.
     * @return RecordSet
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    RecordSet deleteElementFromModel(String processID, String elementID) throws OntologyErrorException;

    /**
     * Sets a {@link Process} as landscape or not depend on flag.
     * 
     * @param processID id of {@link Process}. Not null
     * @param flag <code>true</code> to set a {@link Process} as landscape, <code>false</code> to set it back.
     * 
     * @return <code>true</code> if the command is successfully, else <code>false</code>
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    RecordSet setProcessLandscapeFlag(String processID, boolean flag) throws OntologyErrorException;

}
