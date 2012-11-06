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
package org.prowim.services.ejb.workflow;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Included the methods, which have to do with workflow. This can be the creating of instances or start and close processes also activities.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */

@WebService(name = ServiceConstants.PROWIM_WORKFLOW_REMOTE, targetNamespace = ServiceConstants.PROWIM_WORKFLOW_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface WorkflowRemote
{

    /**
     * Starts a workflow.
     * 
     * @param processTemplateID the process template id.
     * @param userID ID of current user
     * @return status.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String createWorkflow(String processTemplateID, String userID) throws OntologyErrorException;

    /**
     * Sets the process parameters, roles and activates the process with the id = processInstanceID.
     * 
     * @param processInstanceID the id of the process instance.
     * @param roles the process roles.
     * @param processInstanceName the name that the process instance.
     * @param parameterList the process parameters.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void startProcess(String processInstanceID, String processInstanceName, RoleArray roles, ParameterArray parameterList)
                                                                                                                          throws OntologyErrorException;

    /**
     * Sets the activity to be finished.
     * 
     * @param activityID the activity ID.
     * @param userID ID of actual user
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void finishActivity(String activityID, String userID) throws OntologyErrorException;

    /**
     * Get the decision parameter of following decision. This is only important, when after a activity is a decision-Shape. <br>
     * In this case we have to get the process informations of decision-shape and show these in activity, so that the user can <br>
     * choose one of the following controlflows of decision.
     * 
     * @param activityID the activity ID.
     * @return ObjectArray
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ParameterArray getDecisionParameter(String activityID) throws OntologyErrorException;

    /**
     * Set the Attribute "hat_Submodel" for created instance.
     * 
     * @param subProcessID Id of of sub process
     * @param activityID Id of activity, which call the sub process
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setHasSubModelAttr(String subProcessID, String activityID) throws OntologyErrorException;

    /**
     * 
     * This method is only to import the data model {@link ControlFlow} and should not used.
     * 
     * @return ControlFlow
     */
    @WebMethod
    @WebResult(partName = "return")
    @Deprecated
    ControlFlow bindingControlFow();

}
