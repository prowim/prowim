/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
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

import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Includes all methods related to a workflow.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a8
 */
@Local
public interface WorkFlowHelper
{

    /**
     * Sets the activity to be finished.
     * 
     * @param activityID the activity ID.
     * @param status the activity status.
     * @param userID the ID of the user
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void finishActivity(String activityID, String status, String userID) throws OntologyErrorException;

    /**
     * Creates all instances related to a process from the template with the id = templateID.
     * 
     * @param processTemplateID the process-template id.
     * @param userID the ID of the user, assigned to the start role.
     * @return the ID of the process instance non null.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    String createWorkflow(String processTemplateID, String userID) throws OntologyErrorException;

    /**
     * Rolls back a transaction.
     */
    void rollBack();

    /**
     * Starts a process. This method is called after The Workflow was created.<br/>
     * 
     * @param processInstanceID not null process instance id.
     * @param roles not null roles.
     * @param processInstanceName not null new process name
     * @param parameterList not null parameter list.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void startProcess(String processInstanceID, String processInstanceName, RoleArray roles, ParameterArray parameterList)
                                                                                                                          throws OntologyErrorException;

    /**
     * Sets the activity status.
     * 
     * @param activityID the activity ID
     * @param status status
     * @param userID the user ID
     * @return true or false
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    boolean setActivityStatus(String activityID, String status, String userID) throws OntologyErrorException;

    /**
     * 
     * SEt user, who finishes the activity
     * 
     * @param activityID ID of activity
     * @param userID ID of user
     * @return Integer
     * @throws OntologyErrorException if an error occurs in the ontology back end
     * @TODO In deisen Teil soll klar gestellt werden, ob man das ganze überhaupt braucht und wenn ja, was die beiden Regeln überhaupt <br>
     *       machen sollen. Für mich sind beide Regeln eigentlich gleich.
     */
    int applyAndSetRoles(String activityID, String userID) throws OntologyErrorException;

    /**
     * applyForAssignedPersons
     * 
     * @TODO In deisen Teil soll klar gestellt werden, ob man das ganze überhaupt braucht und wenn ja, was die beiden Regeln überhaupt <br>
     *       machen sollen. Für mich sind beide Regeln eigentlich gleich. Description.
     * 
     * 
     * @param activity activity
     * @param userID user ID
     * @return integer integer
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */

    int applyForAssignedPersons(String activity, String userID) throws OntologyErrorException;

    /**
     * 
     * Get value of given slot for given instance.
     * 
     * @param instanceID ID of instance
     * @param attributeName Name of slot
     * @return list of rules
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    String[] getInstanceAttribute(String instanceID, String attributeName) throws OntologyErrorException;

    /**
     * Get all parameters of a decision "Entscheidung" shape, which came after a activity.
     * 
     * @param activityID ID of activity
     * @return ParameterArray List of {@link Parameter}
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    ParameterArray getDecisionParameter(String activityID) throws OntologyErrorException;

    /**
     * Set the Attribute "hat_Submodel" for created instance.
     * 
     * @param subProcessID Id of of sub process. Not null.
     * @param activityID Id of activity, which call the sub process. Not null.
     * @throws OntologyErrorException if an error occurs in the ontology back end
     */
    void setHasSubModelAttr(String subProcessID, String activityID) throws OntologyErrorException;

}