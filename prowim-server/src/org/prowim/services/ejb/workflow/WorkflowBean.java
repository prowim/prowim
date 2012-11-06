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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.ejb.workflow;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.WorkFlowHelper;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.workflow.WorkflowRemote;



/**
 * Implements the {@link WorkflowRemote} methods.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */

@Stateless(name = ServiceConstants.PROWIM_WORKFLOW_BEAN)
@WebService(name = ServiceConstants.PROWIM_WORKFLOW_SERVICE_NAME, serviceName = ServiceConstants.PROWIM_WORKFLOW_SERVICE_NAME, endpointInterface = ServiceConstants.PROWIM_WORKFLOW_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT)
public class WorkflowBean implements WorkflowRemote
{

    @IgnoreDependency
    @EJB
    private WorkFlowHelper workFlowHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.workflow.WorkflowRemote#createWorkflow(java.lang.String, java.lang.String)
     */
    @Override
    public String createWorkflow(String processTemplateID, String userID) throws OntologyErrorException
    {
        return workFlowHelper.createWorkflow(processTemplateID, userID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.workflow.WorkflowRemote#startProcess(java.lang.String, java.lang.String, org.prowim.datamodel.collections.RoleArray, org.prowim.datamodel.collections.ParameterArray)
     */
    @Override
    public void startProcess(String processInstanceID, String processInstanceName, RoleArray roles, ParameterArray parameterList)
                                                                                                                                 throws OntologyErrorException
    {
        workFlowHelper.startProcess(processInstanceID, processInstanceName, roles, parameterList);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.workflow.WorkflowRemote#finishActivity(java.lang.String, String)
     */
    @Override
    public void finishActivity(String activityID, String userID) throws OntologyErrorException
    {
        workFlowHelper.finishActivity(activityID, EngineConstants.Variables.Activity.STATUS_DONE, userID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.workflow.WorkflowRemote#getDecisionParameter(java.lang.String)
     */
    @Override
    public ParameterArray getDecisionParameter(String activityID) throws OntologyErrorException
    {
        return workFlowHelper.getDecisionParameter(activityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.workflow.WorkflowRemote#bindingControlFow()
     */
    @Override
    public ControlFlow bindingControlFow()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.workflow.WorkflowRemote#setHasSubModelAttr(java.lang.String, java.lang.String)
     */
    @Override
    public void setHasSubModelAttr(String subProcessID, String activityID) throws OntologyErrorException
    {
        workFlowHelper.setHasSubModelAttr(subProcessID, activityID);
    }

}
