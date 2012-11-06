/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-14 13:29:20 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/editor/EditorBean.java $
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
package org.prowim.services.ejb.editor;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.security.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EditorHelper;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.ejb.editor.EditorRemote;
import org.prowim.services.interceptors.ChangeProcessInterceptor;



/**
 * Implements the {@link EditorRemote} methods.
 * 
 * @author Saad Wardi
 * @version $Revision: 5075 $
 */
@Stateless
@WebService(name = "EditorService", serviceName = "EditorService", endpointInterface = "org.prowim.services.ejb.editor.EditorRemote")
@WebContext(contextRoot = "/ProWimServices")
@SecurityDomain("JBossWS3")
@PermitAll
public class EditorBean implements EditorRemote
{
    @Resource
    private SessionContext context;

    @IgnoreDependency
    @EJB
    private EditorHelper   editor;

    @IgnoreDependency
    @EJB
    private CommonHelper   common;

    @IgnoreDependency
    @EJB
    private ProcessHelper  processHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#createObject(java.lang.String, java.lang.String)
     */
    // @Interceptors(BeansSecurityInterceptor.class)
    @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public String createObject(String modelID, String oid) throws OntologyErrorException
    {
        Validate.notNull(oid);
        Validate.notNull(modelID);
        return editor.createObject(modelID, oid);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#deleteObject(java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void deleteObject(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        editor.deleteInstance(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#approveProcess(java.lang.String)
     */
    @Override
    public void approveProcess(String modelID) throws OntologyErrorException
    {
        Validate.notNull(modelID);
        editor.approveProcess(modelID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#disapproveProcess(java.lang.String)
     */
    @Override
    public void disapproveProcess(String modelID) throws OntologyErrorException
    {
        Validate.notNull(modelID);
        editor.disapproveProcess(modelID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#getModels()
     */
    @Override
    public String[] getModels() throws OntologyErrorException
    {
        return editor.getModels();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#saveModelAsXML(java.lang.String, java.lang.String, Boolean, String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public String saveModelAsXML(String modelID, String xml, Boolean createNewVersion, String versionName) throws DMSException,
                                                                                                          OntologyErrorException
    {
        Validate.notNull(modelID);
        Validate.notNull(xml);
        Validate.isTrue(( !createNewVersion && versionName == null) || (createNewVersion && versionName != null));
        return editor.saveModelAsXML(modelID, xml, context.getCallerPrincipal().getName(), createNewVersion, versionName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#loadModelAsXML(java.lang.String)
     */
    @Override
    public String loadModelAsXML(String modelID) throws OntologyErrorException, DMSException
    {
        Validate.notNull(modelID);
        return editor.loadModelAsXML(modelID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#connectActivityControlFlow(java.lang.String, java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void connectActivityControlFlow(String sourceID, String targetID, String controlFlowID) throws OntologyErrorException
    {
        editor.connectActivityControlFlow(sourceID, targetID, controlFlowID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#connectActivityRole(java.lang.String, java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void connectActivityRole(String activityID, String roleID, String taskID) throws OntologyErrorException
    {
        editor.connectActivityRole(activityID, roleID, taskID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#getPossibleRelations(java.lang.String, java.lang.String)
     */
    @Override
    public String[] getPossibleRelations(String sourceID, String targetID) throws OntologyErrorException
    {
        return editor.getRelations(sourceID, targetID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setRelationValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    // @Interceptors(ChangeProcessInterceptor.class)
    public void setRelationValue(String source, String relation, String value) throws OntologyErrorException
    {
        editor.setRelationValue(source, relation, value);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setProduct(java.lang.String, java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void setProduct(String source, String target, String productID) throws OntologyErrorException
    {
        editor.setProduct(source, target, productID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see EditorHelper#getControlFlowCount(String)
     */

    @Override
    public String controlFlowCount(String targetID) throws OntologyErrorException
    {
        return editor.getControlFlowCount(targetID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#removeRelationValue(java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void removeRelationValue(String source, String relation) throws OntologyErrorException
    {
        editor.removeRelationValue(source, relation);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#getPossibleCombinationRules()
     */
    @Override
    public String[] getPossibleCombinationRules() throws OntologyErrorException
    {
        return editor.getCombinationRules();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setCombinationRule(java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void setCombinationRule(String controlFlowID, String combinationRule) throws OntologyErrorException
    {
        editor.setCombinationRule(controlFlowID, combinationRule);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#getPossibleActivationRules()
     */
    @Override
    public String[] getPossibleActivationRules() throws OntologyErrorException
    {
        return editor.getActivationRules();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setActivationRule(java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void setActivationRule(String objectID, String activationRule) throws OntologyErrorException
    {
        Validate.notNull(objectID);
        Validate.notNull(activationRule);
        editor.setActivationRule(objectID, activationRule);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#connectActivityMittel(java.lang.String, java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void connectActivityMittel(String activityID, String mittelID, String functionID) throws OntologyErrorException
    {
        editor.connectActivityMittel(activityID, mittelID, functionID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#getPossibleOrganizationalUnits()
     */
    @Override
    public String[] getPossibleOrganizationalUnits() throws OntologyErrorException
    {
        return editor.getOrganizationalUnits();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#getCombinationRule(java.lang.String)
     */
    @Override
    public String getCombinationRule(String controlflowID) throws OntologyErrorException
    {
        return editor.getCombinationRule(controlflowID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#bendControlFlow(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String bendControlFlow(String controlflowID, String oldSource, String oldTarget, String newSource, String newTarget)
                                                                                                                               throws OntologyErrorException
    {
        return editor.bendControlFlow(controlflowID, oldSource, oldTarget, newSource, newTarget);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setProcessType(java.lang.String, java.lang.String)
     */
    // @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public String setProcessType(String processTypeID, String processID) throws OntologyErrorException
    {
        return processHelper.setProcessType(processTypeID, processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#getProcessInformationID(java.lang.String)
     */
    @Override
    public String getProcessInformationID(String processID) throws OntologyErrorException
    {
        ObjectArray resultArray = processHelper.getProductProcessInformation(processID);
        if (resultArray.size() == 1)
        {
            return ((ProcessInformation) resultArray.getItem().get(0)).getID();
        }

        throw new IllegalArgumentException(
                                           "The process information of a process must contain exactly one element (The DMS storage id). Number found: "
                                                   + resultArray.size() + " for process ID " + processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see EditorHelper#getAllExistSubProcesses()
     */
    @Override
    public ProcessArray getAllExistSubProcesses() throws OntologyErrorException
    {
        return editor.getAllExistSubProcesses();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setSubProcessFlagForActivity(java.lang.String)
     */
    @Override
    public void setSubProcessFlagForActivity(String activityID) throws OntologyErrorException
    {
        editor.setSubProcessFlagForActivity(activityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setSubProcessFlagForProcess(java.lang.String)
     */
    @Override
    public void setSubProcessFlagForProcess(String processID) throws OntologyErrorException
    {
        editor.setSubProcessFlagForProcess(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setSubProcessOfActivity(java.lang.String, java.lang.String)
     */
    @Override
    public void setSubProcessOfActivity(String subProcessID, String activityID) throws OntologyErrorException
    {
        editor.setSubProcessOfActivity(subProcessID, activityID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setActivityAsAuto(java.lang.String)
     */
    @Override
    public void setActivityAsAuto(String activityID) throws OntologyErrorException
    {
        editor.setActivityOperation(activityID, EngineConstants.Consts.TRUE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setActivityAsManual(java.lang.String)
     */
    @Override
    public void setActivityAsManual(String activityID) throws OntologyErrorException
    {
        editor.setActivityOperation(activityID, EngineConstants.Consts.FALSE);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#deleteElementFromModel(java.lang.String, java.lang.String)
     */
    @Override
    public void deleteElementFromModel(String processID, String elementID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        Validate.notNull(elementID);
        editor.deleteElementFromModel(processID, elementID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#isProcessApproved(java.lang.String)
     */
    @Override
    public boolean isProcessApproved(String modelID) throws OntologyErrorException
    {
        return editor.isProcessApproved(modelID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#isActivityManual(java.lang.String)
     */
    @Override
    public boolean isActivityManual(String activityId) throws OntologyErrorException
    {
        return EngineConstants.Consts.FALSE.equals(common.getSlotValue(activityId, EngineConstants.Slots.Activity.IS_AUTOMATIC));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#setProcessLandscapeFlag(java.lang.String, boolean)
     */
    @Override
    public boolean setProcessLandscapeFlag(String processID, boolean flag) throws OntologyErrorException
    {
        return editor.setProcessLandscapeFlag(processID, flag);
    }
}
