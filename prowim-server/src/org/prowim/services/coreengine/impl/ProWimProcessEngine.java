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

package org.prowim.services.coreengine.impl;

import java.util.Hashtable;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.AlgernonValue;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.datamodel.prowim.InformationTypesConstants.ContentSlots;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.EngineConstants.Variables.Process;
import org.prowim.utils.LoggingUtility;

import de.ebcot.tools.logging.Logger;
import de.ebcot.tools.string.EscapeFunctions;


/**
 * Access the ontology with {@link AlgernonService algernon service}.
 * 
 * @author Maziar Khodaei, Saad Wardi
 * 
 */
@Stateless
public class ProWimProcessEngine implements ProcessEngine
{
    private final static Logger LOG = Logger.getLogger(ProWimProcessEngine.class);

    /** the algernon service. */
    @EJB
    private AlgernonService     myService;

    @IgnoreDependency
    @EJB
    private CommonHelper        commonHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getMyActiveActivities(java.lang.String)
     */
    @Override
    public RecordSet getMyActiveActivities(String user) throws OntologyErrorException
    {

        final Hashtable<String, AlgernonValue> para = new Hashtable<String, AlgernonValue>();
        para.put(ProcessEngineConstants.Variables.Organisation.Person.ID, new AlgernonValue(user, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_MYACTIVEACTIVITIES, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_MYACTIVEACTIVITIES, result, this.getClass());
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProcessType(java.lang.String)
     */
    public RecordSet getProcessType(String processID) throws OntologyErrorException
    {
        LOG.info("\n\n" + processID + "\n\n");
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, processID);

        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PROCESS_TYPE, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PROCESS_TYPE, result, this.getClass());
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getAllProcessTypes()
     */
    public RecordSet getAllProcessTypes() throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_ALL_PROCESS_TYPES, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_ALL_PROCESS_TYPES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getAllTopProcessTypes()
     */
    @Override
    public RecordSet getAllTopProcessTypes() throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_ALL_TOP_PROCESS_TYPES, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_ALL_TOP_PROCESS_TYPES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#setProcessType(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setProcessType(String processTypeID, String processID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID, processTypeID);
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, processID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.SET_PROCESS_TYPE, para, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.SET_PROCESS_TYPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#createProcessType(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet createProcessType(String name, String description) throws OntologyErrorException
    {
        Hashtable<Object, AlgernonValue> para = new Hashtable<Object, AlgernonValue>();
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_NAME, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(name),
                                                                                               true));
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_DESC,
                 new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(description), true));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_TYPE, para, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.CREATE_PROCESS_TYPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#setProcessTypeParent(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet setProcessTypeParent(String typeID, String parentTypeID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID, typeID);

        para.put(ProcessEngineConstants.Variables.Process.PARENT, parentTypeID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.SET_PROCESS_TYPE_PARENT, para, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.SET_PROCESS_TYPE_PARENT, result, this.getClass());
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getAlleProzesse()
     */
    public RecordSet getAlleProzesse() throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_ALL_PROCESSES, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_ALL_PROCESSES, result, this.getClass());
        return result;
    }

    /**
     * Gets the output parameter to a given rule.
     * 
     * @param ruleName the rule name
     * @return an array of output parameter or an empty array.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @SuppressWarnings("unused")
    private String[] getRuleOutputParameter(String ruleName) throws OntologyErrorException
    {
        String[] res = null;
        Hashtable<String, AlgernonValue> para = new Hashtable<String, AlgernonValue>();
        para.put(ProcessEngineConstants.Variables.Common.RULE_NAME, new AlgernonValue(ruleName, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_RULE_OUTPUT, para, AlgernonConstants.ASK);

        if (result.getNoOfRecords() > 0)
        {
            res = new String[result.getNoOfRecords()];

            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<? , ? > record = result.getRecords()[i];
                res[i] = (String) record.get(ProcessEngineConstants.Variables.Common.OUTPUT_PARAM);
            }
        }
        return res;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getInstanceKnowledgeObjects(java.lang.String)
     */
    @Override
    public RecordSet getInstanceKnowledgeObjects(String instance) throws OntologyErrorException
    {
        final Hashtable<String, String> parameterMap = new Hashtable<String, String>();
        parameterMap.put(ProcessEngineConstants.Variables.Common.INS, instance);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_INSTANCE_KNOWLEDGEOBJECTS, parameterMap,
                                                       AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_INSTANCE_KNOWLEDGEOBJECTS, result, this.getClass());
        return result;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActivities(java.lang.String)
     */
    @Override
    public RecordSet getActivities(String template) throws OntologyErrorException
    {
        final Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(ProcessEngineConstants.Variables.Common.ID, template);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_ACTIVITIES, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_ACTIVITIES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getToplevelDomaenen()
     */
    @Override
    public RecordSet getToplevelDomaenen() throws OntologyErrorException
    {
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_TOPLEVELDOMAINS, null, null, null, null);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_TOPLEVELDOMAINS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSubdomaenen(java.lang.String)
     */
    @Override
    public RecordSet getSubdomaenen(String topDomainID) throws OntologyErrorException
    {
        final Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(ProcessEngineConstants.Variables.Knowledge.DOM, topDomainID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_SUBDOMAINS, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_SUBDOMAINS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeObjectsDomains(java.lang.String)
     */
    public RecordSet getKnowledgeObjectsDomains(String domainID) throws OntologyErrorException
    {
        final Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(ProcessEngineConstants.Variables.Knowledge.DOM, domainID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGEOBJECTS_DOMAINS, parameters,
                                                 AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGEOBJECTS_DOMAINS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getExecutableProcesses(java.lang.String)
     */
    @Override
    public RecordSet getExecutableProcesses(String userID) throws OntologyErrorException
    {
        final Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(ProcessEngineConstants.Variables.Organisation.Person.ID, userID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_EXECUTABLE_PROCESSES, parameters,
                                                       AlgernonConstants.ASK);

        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_EXECUTABLE_PROCESSES, result, this.getClass());

        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getPerson(java.lang.String)
     */
    public RecordSet getPerson(String userID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.PERSON_ID, userID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_PERSON, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Organisation.GET_PERSON, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getRunningProcesses(java.lang.String)
     */
    public RecordSet getRunningProcesses(String userID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.USER_ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_RUNNING_PROCESSES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_RUNNING_PROCESSES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActiveActivities(java.lang.String)
     */
    public RecordSet getActiveActivities(String processID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Common.INS, processID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_ACTIVE_ACTIVITIES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_ACTIVE_ACTIVITIES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getUsers()
     */
    @Override
    public RecordSet getUsers() throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_USERS, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Organisation.GET_USERS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getUsers()
     */
    @Override
    public RecordSet getRoles(String processID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Common.INS, processID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Role.GET_ROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Role.GET_ROLES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActivityRoles(java.lang.String)
     */
    @Override
    public RecordSet getActivityRoles(String activityID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, activityID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Role.GET_ACTIVITY_ROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Role.GET_ACTIVITY_ROLES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getParameter(java.lang.String)
     */
    public RecordSet getParameter(String processInformationID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_INSTANCE_ID, processInformationID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PROCESS_INFORMATION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PROCESS_INFORMATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getFrameName(java.lang.String)
     * @deprecated replace with {@link CommonEngine#getName(String)} .
     */
    @Deprecated
    public String getFrameName(String processID) throws OntologyErrorException
    {
        String ret = null;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(processID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PROCESS_INSTANCE_IDENT, tab, "ASK");
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PROCESS_INSTANCE_IDENT, result, this.getClass());
        if (result.getNoOfRecords() > 0)
        {
            Hashtable<String, String> record = result.getRecords()[0];
            ret = record.get(ProcessEngineConstants.Variables.Common.IDENT);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#deleteProcess(java.lang.String)
     */
    public RecordSet deleteProcess(String processInstanceID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Process.DELETE_PROCESS_ID, processInstanceID);

        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.DELETE_PROCESS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.DELETE_PROCESS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#deleteProcessInstances(java.lang.String)
     */
    public RecordSet deleteProcessInstances(String processID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Process.DELETE_PROCESS_ID, processID);

        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.DELETE_PROCESS_INSTANCES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.DELETE_PROCESS_INSTANCES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getFinishedProcesses(java.lang.String)
     */
    @Override
    public RecordSet getFinishedProcesses(String userID) throws OntologyErrorException
    {

        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();

        tab.put(ProcessEngineConstants.Variables.Organisation.Person.SHORTNAME, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_FINISHED_PROCESSES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_FINISHED_PROCESSES, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getInformationTypes()
     */
    @Override
    public RecordSet getInformationTypes() throws OntologyErrorException
    {
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_INFORMATION_TYPES, null, null, null, null);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_INFORMATION_TYPES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProcessParameter(java.lang.String)
     */
    @Override
    public RecordSet getProcessParameter(String processInstanceID) throws OntologyErrorException
    {

        Hashtable<String, String> tab = new Hashtable<String, String>();

        tab.put(Process.PROCESS_INST_ID, processInstanceID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PROCESS_PARAMETERS, tab, AlgernonConstants.ASK);

        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PROCESS_PARAMETERS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProcessTemplateParameter(java.lang.String)
     */
    @Override
    public RecordSet getProcessTemplateParameter(String processInstanceID) throws OntologyErrorException
    {

        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();

        tab.put(ProcessEngineConstants.Variables.Common.TEMPLATE_ID, new AlgernonValue(processInstanceID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PROCESS_TEMPLATE_PARAMETERS, tab, AlgernonConstants.ASK);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getOrganisations()
     */
    @Override
    public RecordSet getOrganisations() throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_ORGANISATIONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Organisation.GET_ORGANISATIONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getTopOrganisations()
     */
    @Override
    public RecordSet getTopOrganisations() throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> param = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_TOP_ORGANIZATIONS, param, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Organisation.GET_TOP_ORGANIZATIONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSubOrganisations(java.lang.String)
     */
    @Override
    public RecordSet getSubOrganisations(String orgaID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> param = new Hashtable<String, AlgernonValue>();
        param.put(ProcessEngineConstants.Variables.Organisation.ORGANISATION_ID, new AlgernonValue(orgaID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_SUB_ORGANIZATIONS, param, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Organisation.GET_SUB_ORGANIZATIONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getAssignedPersonsToRole(java.lang.String)
     */
    @Override
    public RecordSet getAssignedPersonsToRole(String roleID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Role.ROLLE_ID, roleID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Role.GET_ASSIGNED_PERSONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Role.GET_ASSIGNED_PERSONS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc} getParameterScalarValue
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getParameterValue(java.lang.String)
     */
    public RecordSet getParameterValue(String parameterID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(parameterID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PARAMETER_VALUE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PARAMETER_VALUE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getParameterScalarValue(java.lang.String)
     */
    public RecordSet getParameterScalarValue(String parameterID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(parameterID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PARAMETER_SCALAR_VALUE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PARAMETER_SCALAR_VALUE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getParameterSelectList(java.lang.String)
     */
    public RecordSet getParameterSelectList(String parameterID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(parameterID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PARAMETER_SELECT_LIST, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PARAMETER_SELECT_LIST, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getOrganisation(java.lang.String)
     */
    public RecordSet getOrganisation(String organisationID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.ORGANISATION_ID, new AlgernonValue(organisationID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_ORGANISATION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Organisation.GET_ORGANISATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#setSlotValue(java.lang.String, java.lang.String, java.lang.String)
     */
    public RecordSet setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(slotID, true));
        if (slotID.equals("Inhalt_Ganzzahl") || slotID.equals("Inhalt_Festkommazahl"))
        {
            tab.put(ProcessEngineConstants.Variables.Common.VALUE, new AlgernonValue(value, false));
        }
        else
            tab.put(ProcessEngineConstants.Variables.Common.VALUE, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(value), true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.SET_SLOT_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.SET_SLOT_VALUE, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#clearSlotValue(java.lang.String, java.lang.String, java.lang.String)
     */
    public void clearSlotValue(String frameID, String slotID, String whereValue) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(slotID, true));
        if (slotID.equals(ContentSlots.CONTENT_INTEGER) || slotID.equals(ContentSlots.CONTENT_FLOAT))
            tab.put(ProcessEngineConstants.Variables.Common.VALUE, new AlgernonValue(whereValue, true));
        else
            tab.put(ProcessEngineConstants.Variables.Common.VALUE, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(whereValue), true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.CLEAR_SLOT_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.CLEAR_SLOT_VALUE, result, this.getClass());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#clearSlot(java.lang.String, java.lang.String)
     */
    public RecordSet clearSlot(String frameID, String slotID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));
        tab.put(ProcessEngineConstants.Variables.Common.SLOT_ID, new AlgernonValue(slotID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.CLEAR_RELATION_VALUE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSelectionAttribute(java.lang.String)
     */
    @Override
    public RecordSet getSelectionAttribute(String parameterID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(parameterID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_SELECTION_ATTRIBUTE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_SELECTION_ATTRIBUTE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSelectionAttributeFromInfoType(java.lang.String)
     */
    public RecordSet getSelectionAttributeFromInfoType(String parameterID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, new AlgernonValue(parameterID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_SELECTION_ATTRIBUTE_FROM_INFOTYPE, tab,
                                                 AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_SELECTION_ATTRIBUTE_FROM_INFOTYPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#executeSelectionAttributeRule(java.lang.String)
     */
    @Override
    public RecordSet executeSelectionAttributeRule(String ruleName) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        final RecordSet result = myService.executeRule(ruleName, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ruleName, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#executeSelectionAttributeRuleForControlFlow(java.lang.String, java.lang.String)
     */
    public RecordSet executeSelectionAttributeRuleForControlFlow(String ruleName, String activityID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Activity.VID, activityID);
        final RecordSet result = myService.executeRule(ruleName, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ruleName, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getFrameToProcessInformation(java.lang.String)
     */
    public String getFrameToProcessInformation(String processInformationID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Common.InformationType.PROCESSINFORMATION_ID, processInformationID);
        final RecordSet records = myService.executeRule(ProcessEngineConstants.Rules.ProcessInformation.GET_FRAME_ID_TO_PROCESSINFORMATION, para,
                                                        AlgernonConstants.ASK);
        if (records != null && records.getNoOfRecords() > 0)
        {
            Hashtable<String, String> frameIDRecord = records.getRecords()[0];
            String id = frameIDRecord.get(ProcessEngineConstants.Variables.Common.FRAME_ID);
            return id;
        }
        else
        {
            throw new IllegalArgumentException("The Processinformation withe id  " + processInformationID + "  is in no frame defined.");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getControlFlow(java.lang.String)
     */
    @Override
    public RecordSet getControlFlow(String controlFlowID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.ControlFlow.ID, controlFlowID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.ControlFlow.GET_CONTROLFLOW, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.ControlFlow.GET_CONTROLFLOW, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getParameterConstraint(java.lang.String)
     */
    @Override
    public RecordSet getParameterConstraint(String parameterID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Common.PARAMETER_ID, parameterID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_PROCESSINFORMATION_CONSTRAINT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_PROCESSINFORMATION_CONSTRAINT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActivityInput(java.lang.String)
     */
    @Override
    public RecordSet getActivityInput(String activityID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_INPUT_PRODUCTS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_INPUT_PRODUCTS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActivityOutput(java.lang.String)
     */
    public RecordSet getActivityOutput(String activityID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_OUTPUT_PRODUCTS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_OUTPUT_PRODUCTS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProductProcessInformations(java.lang.String)
     */
    @Override
    public RecordSet getProductProcessInformations(String productID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.PRODUCT_ID, new AlgernonValue(productID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_PRODUCT_PROCESSINFORMATIONS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_PRODUCT_PROCESSINFORMATIONS, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActivityEnableType(java.lang.String)
     */
    @Override
    public RecordSet getActivityEnableType(String activityID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(activityID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_ENABLE_TYPE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_ENABLE_TYPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#setActivityStatus(java.lang.String, java.lang.String)
     */
    @Override
    public void setActivityStatus(String activityID, String status) throws OntologyErrorException
    {
        String isSubProzess = null;
        String subProzessId = null;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        if (status.equals(ProcessEngineConstants.Variables.Activity.STATUS_DONE))
        {
            tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_FINISH, new AlgernonValue(activityID, false));
            RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_DONE_RULE, tab, AlgernonConstants.ASK);
            if (result.getNoOfRecords() != 0)
            {
                Hashtable<String, String> record = result.getRecords()[0];
                String regel = record.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_RULE);
                if (regel != null && !regel.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    Hashtable<String, AlgernonValue> tabinstance = new Hashtable<String, AlgernonValue>();
                    tabinstance.put(ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, new AlgernonValue(activityID, false));
                    result = myService.executeActivityRule(regel, tabinstance);

                }
            }
        }
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, new AlgernonValue(activityID, false));
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_STATUS, new AlgernonValue(status, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.SET_ACTIVITY_STATE, tab, AlgernonConstants.TELL);
        // marke, if this activity the last in a subprocess.
        if (result != null && result.getNoOfRecords() > 0)
        {
            Hashtable<String, String> tmpSub = result.getRecords()[0];
            isSubProzess = tmpSub.get(ProcessEngineConstants.Variables.Activity.DEPENDENCY);
            subProzessId = tmpSub.get(ProcessEngineConstants.Variables.Common.ID);
        }
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.SET_ACTIVITY_STATE, result, this.getClass());

        // Clears the value of the slot "fertiggesetzt von".
        Hashtable<String, AlgernonValue> tabl = new Hashtable<String, AlgernonValue>();
        tabl.put(ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, new AlgernonValue(activityID, false));
        result = myService.executeRule(ProcessEngineConstants.Rules.Activity.CLEARS_DONE, tabl, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.CLEARS_DONE, result, this.getClass());

        // See if the activity has an activation rule if no activation rule is defined use the standard rule.
        Hashtable<String, AlgernonValue> tab2 = new Hashtable<String, AlgernonValue>();
        tab2.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_FINISH, new AlgernonValue(activityID, false));
        result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_ACTIVATE_RULE, tab2, AlgernonConstants.ASK);
        if (result.getNoOfRecords() == 0)
        {
            result = myService.executeRule(ProcessEngineConstants.Rules.Activity.STANDARD_ACTIVATION,
                                           ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, activityID, null, null);
            LOG.info("Standard rule " + ProcessEngineConstants.Rules.Activity.STANDARD_ACTIVATION);
            LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.STANDARD_ACTIVATION, result, this.getClass());
        }
        // An activation rule is defined, execute it !.
        else
        {
            Hashtable<String, String> record = result.getRecords()[0];
            String regel = record.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_RULE);
            result = myService.executeBusinessRule(regel, ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, activityID, null, null);
            LOG.info("activation rule  " + regel + " will be executed");
            LoggingUtility.logResult(ProcessEngineConstants.Variables.Activity.ACTIVITY_RULE, result, this.getClass());
        }

        if (result.getNoOfRecords() > 0)
        {
            // Handle ControlFlows.
            int size = result.getNoOfRecords();
            for (int i = 0; i < size; i++)
            {
                Hashtable<String, String> controlFlowRecord = result.getRecords()[i];
                String controlFlow = controlFlowRecord.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_CONTROLFLOWS);
                tab = new Hashtable<String, AlgernonValue>();
                tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_CONTROLFLOWS, new AlgernonValue(controlFlow, false));
                myService.executeRule(ProcessEngineConstants.Rules.ControlFlow.SET_CONTROLFLOW_ACTIV, tab, AlgernonConstants.TELL);
                setControlFlow(controlFlow);
            }
        }

        // check, if this is the last activity in a subprocess.
        // if yes: set the state for the parent
        if (isSubProzess.equals(ProcessEngineConstants.Variables.Common.ONE))
        {
            Hashtable<String, String> tabCaller = new Hashtable<String, String>();
            tabCaller.put(ProcessEngineConstants.Variables.Common.ID, subProzessId);
            RecordSet myresult = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_CALL_PROCESS, tabCaller, AlgernonConstants.ASK);
            if (myresult != null && myresult.getNoOfRecords() > 0)
            {
                String caller = myresult.getRecords()[0].get(ProcessEngineConstants.Variables.Activity.ACTIVITY_CALLER);
                setActivityStatus(caller, status);
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#addKnowledgeObject(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet addKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException
    {
        Hashtable<String, String> tab = new Hashtable<String, String>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.WO, knowledgeObjectID);
        tab.put(ProcessEngineConstants.Variables.Knowledge.PROCESS_ELEMENT, processElementID);
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.ADD_KNOWLEDGEOBJECT_TO_PROCESS_ELEMENT, tab,
                                                 AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.ADD_KNOWLEDGEOBJECT_TO_PROCESS_ELEMENT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#genKnowledgeObject(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet genKnowledgeObject(String relationID, String name, String userID) throws OntologyErrorException
    {

        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.NAME_EN, new AlgernonValue(name, true));
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.ID, new AlgernonValue(userID, true));
        tab.put(ProcessEngineConstants.Variables.Common.PROCESS_ELEMENT_ID, new AlgernonValue(relationID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GENERATES_KNOWLEDGE_OBJECT_WITH_ELEMENT, tab,
                                                 AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GENERATES_KNOWLEDGE_OBJECT_WITH_ELEMENT, result, this.getClass());

        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#genKnowledgeObject(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet genKnowledgeObject(String name, String userID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.NAME_EN, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(name), true));
        tab.put(ProcessEngineConstants.Variables.Organisation.Person.ID, new AlgernonValue(userID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GENERATES_KNOWLEDGE_OBJECT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GENERATES_KNOWLEDGE_OBJECT, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#genKnowledgeLink(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet genKnowledgeLink(String knowledgeObjectID, String name, String wsID, String link) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
        tab.put(ProcessEngineConstants.Variables.Knowledge.REPOSITORY_ID, new AlgernonValue(wsID, true));
        tab.put(ProcessEngineConstants.Variables.Common.NAME_EN, new AlgernonValue(EscapeFunctions.replaceBackslashAndApostrophe(name), true));
        tab.put(ProcessEngineConstants.Variables.Knowledge.LINK, new AlgernonValue(link, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GENERATES_KNOWLEDGE_LINK, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GENERATES_KNOWLEDGE_LINK, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeObject(java.lang.String)
     */
    public RecordSet getKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeLink(java.lang.String)
     */
    public RecordSet getKnowledgeLink(String knowledgeLinkID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGELINK_ID, new AlgernonValue(knowledgeLinkID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_LINK, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_LINK, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeObjectDominators(java.lang.String)
     */
    @Override
    public RecordSet getKnowledgeObjectDominators(String knowledgeObjectID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_DOMINATORS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_DOMINATORS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getTemplateID(java.lang.String)
     */
    @Override
    public RecordSet getTemplateID(String relationID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(relationID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_TEMPLATE_ID, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_TEMPLATE_ID, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeRepositories()
     */
    @Override
    public RecordSet getKnowledgeRepositories() throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_REPOSITORY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_REPOSITORY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getBusinessDomains(java.lang.String)
     */
    @Override
    public RecordSet getBusinessDomains(String processInstanceID) throws OntologyErrorException
    {
        Validate.notNull(processInstanceID);
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processInstanceID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_BUSINESS_DOMAINS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_BUSINESS_DOMAINS, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeDomain(java.lang.String)
     */
    @Override
    public RecordSet getKnowledgeDomain(String domainID) throws OntologyErrorException
    {
        Validate.notNull(domainID);
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.DOMAINKNOWLEDGE_ID, new AlgernonValue(domainID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_DOMAIN, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_DOMAIN, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeObjectLinks(java.lang.String)
     */
    @Override
    public RecordSet getKnowledgeObjectLinks(String knowledgeObjectID) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObjectID);
        int linksCount = this.getCountOfKnowledgeLinks(knowledgeObjectID);
        RecordSet result = null;
        if (linksCount > 0)
        {
            Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
            tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
            result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_LINKS, tab, AlgernonConstants.ASK);
            LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_LINKS, result, this.getClass());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getAllKnowledgeObjects()
     */
    @Override
    public RecordSet getAllKnowledgeObjects() throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();

        // RecordSet result = myService.executeRule("getKnowledeObjectsComplete", tab, AlgernonConstants.ASK);
        // LoggingUtility.logResult("getKnowledeObjectsComplete", result, this.getClass());
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_ALL_KNOWLEDGE_OBJECTS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_ALL_KNOWLEDGE_OBJECTS, result, this.getClass());

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeObjectCreatorNumber(java.lang.String)
     */
    @Override
    public RecordSet getKnowledgeObjectCreatorNumber(String knowledgeObjectID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_CREATOR_NUMBER, tab,
                                                 AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_CREATOR_NUMBER, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getCountOfKnowledgeLinks(java.lang.String)
     */
    public int getCountOfKnowledgeLinks(String knowledgeObjectID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_LINKS_NUMBER, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_LINKS_NUMBER, result, this.getClass());
        int res = 0;
        if (result.getNoOfRecords() > 0)
        {
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeLinksCount = result.getRecords()[i];
                final String count = knowledgeLinksCount.get(ProcessEngineConstants.Variables.Common.NUMBER_LINKS);
                if (count != null)
                {
                    res = new Integer(count).intValue();
                }

            }
        }

        return res;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeObjectCreator(java.lang.String)
     */
    @Override
    public RecordSet getKnowledgeObjectCreator(String knowledgeObjectID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_CREATOR, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGE_OBJECT_CREATOR, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#searchLinks(java.lang.String)
     */
    @Override
    public RecordSet searchLinks(String pattern) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.PATTERN, new AlgernonValue(pattern, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.SEARCH_LINKS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.SEARCH_LINKS, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#searchKnowledgeObjects(java.lang.String)
     */
    public RecordSet searchKnowledgeObjects(String pattern) throws OntologyErrorException
    {

        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.PATTERN, new AlgernonValue(pattern, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.SEARCH_KNOWLEDGEOBJECTS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.SEARCH_KNOWLEDGEOBJECTS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#searchKnowledgeDomains(java.lang.String)
     */
    @Override
    public RecordSet searchKnowledgeDomains(String pattern) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.PATTERN, new AlgernonValue(pattern, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.SEARCH_KNOWLEDGEDOMAINS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.SEARCH_KNOWLEDGEDOMAINS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeLinkForKnowledgeObject(java.lang.String)
     */
    public RecordSet getKnowledgeLinkForKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN, new AlgernonValue(knowledgeObjectID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGELINKS_FOR_OBJECTS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Knowledge.GET_KNOWLEDGELINKS_FOR_OBJECTS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProcessBrowserChartPath(java.lang.String)
     */
    @Override
    public RecordSet getProcessBrowserChartPath(String processID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_PROCESS_BROWSERCHARTPATH, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_PROCESS_BROWSERCHARTPATH, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#genProcessInformation(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet genProcessInformation(String name, String frameID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.NAME_EN, new AlgernonValue(name, true));
        tab.put(ProcessEngineConstants.Variables.Common.FRAME_ID, new AlgernonValue(frameID, true));

        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.ProcessInformation.GENERATE_PROCESSINFORMATION, tab,
                                                 AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.ProcessInformation.GENERATE_PROCESSINFORMATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getNotEditableProcessInformations(java.lang.String)
     */
    @Override
    public RecordSet getNotEditableProcessInformations(String processID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processID, true));
        RecordSet result = myService
                .executeRule(ProcessEngineConstants.Rules.Process.GET_NOT_EDITABLE_PROCESSINFORMATION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_NOT_EDITABLE_PROCESSINFORMATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getFrameToOutputProduct(java.lang.String)
     */
    @Override
    public String getFrameToOutputProduct(String productID) throws OntologyErrorException
    {
        String activityID = null;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.PRODUCT_ID, new AlgernonValue(productID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.ControlFlow.GET_ACTIVITY_ID, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.ControlFlow.GET_ACTIVITY_ID, result, this.getClass());
        if (result != null && result.getNoOfRecords() > 0)
        {
            activityID = result.getRecords()[0].get(ProcessEngineConstants.Variables.Activity.VID);
        }
        return activityID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#isProduct(java.lang.String)
     */
    @Override
    public boolean isProduct(String frameID) throws OntologyErrorException
    {
        String status = AlgernonConstants.FAILED;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.PRODUCT_ID, new AlgernonValue(frameID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.IS_PRODUCT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.IS_PRODUCT, result, this.getClass());
        if (result != null && result.getNoOfRecords() > 0)
        {
            status = result.getResult();
        }

        return status.equals(AlgernonConstants.OK);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getTimedActivities()
     */
    @Override
    public RecordSet getTimedActivities() throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_TIMES_ACTIVITIES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_TIMES_ACTIVITIES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#sendEmail(java.lang.String)
     */
    @Override
    public void sendEmail(String activityID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, new AlgernonValue(activityID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.SEND_EMAIL_FOR_ACTIVITY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.SEND_EMAIL_FOR_ACTIVITY, result, this.getClass());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getStartActivity(java.lang.String)
     */
    @Override
    public String getStartActivity(String processInstanceID) throws OntologyErrorException
    {
        String activityID = null;
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(Process.PROCESS_INST_ID, new AlgernonValue(processInstanceID, true));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_START_ACTIVITY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_START_ACTIVITY, result, this.getClass());
        if (result != null && result.getNoOfRecords() == 1)
        {
            Hashtable<String, String> record = result.getRecords()[0];
            activityID = record.get(ProcessEngineConstants.Variables.Activity.VID);
        }
        if (activityID != null)
        {
            return activityID;
        }
        else
        {
            throw new IllegalArgumentException("No Start Activity for the process instance : " + processInstanceID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#isProcessTemplate(java.lang.String)
     */
    public boolean isProcessTemplate(String processID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(processID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.IS_PROCESS_TEMPLATE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.IS_PROCESS_TEMPLATE, result, this.getClass());
        if (result != null)
        {
            return !result.getResult().equals(AlgernonConstants.OK);

        }
        else
        {
            return false;
        }
    }

    /* ********************* Helper functions. ************************************************ */
    private void setControlFlow(String kf) throws OntologyErrorException
    {
        // Hat Verknüpfungsregeln
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_CONTROLFLOWS, new AlgernonValue(kf, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_CONTROLFLOW_RULE, tab, AlgernonConstants.ASK);
        if (result.getNoOfRecords() == 0)
        {
            // No rule is defined use the standard rule.
            result = myService.executeRule(ProcessEngineConstants.Rules.Activity.STANDARD_ACTIVATION_RULE,
                                           ProcessEngineConstants.Variables.Activity.ACTIVITY_CONTROLFLOWS, kf, null, null);
        }
        else
        {
            // Execute the defined rule for the activation
            Hashtable<String, String> record = result.getRecords()[0];
            String regel = record.get(ProcessEngineConstants.Variables.Activity.VR);
            result = myService.executeBusinessRule(regel, ProcessEngineConstants.Variables.Activity.ACTIVITY_CONTROLFLOWS, kf, null, null);
        }
        int activationRulesCount = result.getNoOfRecords();
        if (result.getNoOfRecords() > 0)
        {
            Hashtable<String, String> recordnf = result.getRecords()[0];
            String nachfolger = recordnf.get(ProcessEngineConstants.Variables.Common.ID);
            int anz = Integer.parseInt(recordnf.get(ProcessEngineConstants.Variables.Common.COUNT));
            if (anz == 1 || anz == activationRulesCount)
            {
                // Set ControlFlows to activ
                Hashtable<String, AlgernonValue> tabControlFlows = new Hashtable<String, AlgernonValue>();
                tabControlFlows.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_CONTROLFLOWS, new AlgernonValue(kf, false));
                result = myService.executeRule(ProcessEngineConstants.Rules.ControlFlow.SET_CONTROLFLOW_INACTIV, tabControlFlows,
                                               AlgernonConstants.TELL);

                Hashtable<String, AlgernonValue> tabSuccessor = new Hashtable<String, AlgernonValue>();
                tabSuccessor.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, new AlgernonValue(nachfolger, false));
                tabSuccessor.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_STATUS,
                                 new AlgernonValue(ProcessEngineConstants.Variables.Common.ACTIV, true));
                result = myService.executeRule(ProcessEngineConstants.Rules.Activity.SET_ACTIVITY_STATE, tabSuccessor, AlgernonConstants.TELL);
                checkForActivityRule(nachfolger);

                // result = myService.executeRule(ProcessEngineConstants.Rules.Activity.SEND_EMAIL_FOR_ACTIVITY, tabSuccessor, AlgernonConstants.ASK);
                this.sendEmail(nachfolger);
                this.handleSubProcess(nachfolger);
            }
        }
    }

    private void checkForActivityRule(String instance) throws OntologyErrorException
    {
        String[] rules = this.getInstanceAttribute(instance, Relation.Slots.HAS_RULE);
        LOG.debug("Checking Activity rules for instance " + instance);
        if (rules != null && rules.length > 0)
        {
            for (int i = 0; i < rules.length; i++)
            {
                LOG.debug("rule name is " + rules[i]);
                Hashtable<String, AlgernonValue> tabinstance = new Hashtable<String, AlgernonValue>();
                tabinstance.put(ProcessEngineConstants.Variables.Common.VAR_VARIABLE_V, new AlgernonValue(instance, false));
                RecordSet result = myService.executeActivityRule(rules[i], tabinstance);
                LoggingUtility.logResult(rules[i], result, this.getClass());
            }
        }
    }

    private String[] getInstanceAttribute(String instance, String attributename) throws OntologyErrorException
    {
        String[] attributes = null;
        Hashtable<String, AlgernonValue> para = new Hashtable<String, AlgernonValue>();
        para.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(instance, false));
        para.put(ProcessEngineConstants.Variables.Activity.ATTRIBUT_VALUE, new AlgernonValue(attributename, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_VALUE, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_VALUE, result, this.getClass());
        if (result != null && result.getNoOfRecords() > 0)
        {
            attributes = new String[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String attribute = new String(record.get(ProcessEngineConstants.Variables.Common.ATTRIBUTE_VALUE));
                attributes[i] = attribute;
            }
        }
        return attributes;
    }

    private void handleSubProcess(String activity) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tabnachfolger = new Hashtable<String, AlgernonValue>();
        tabnachfolger.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(activity, false));
        RecordSet subProcessesActivitieRecords = myService.executeRule(ProcessEngineConstants.Rules.Process.Subprocess.GET_SUBPROCESS_ACITVITY,
                                                                       tabnachfolger, AlgernonConstants.ASK);
        /** Set the state of the start activity of the subprocesses to activ */
        if (subProcessesActivitieRecords != null && subProcessesActivitieRecords.getNoOfRecords() > 0)
        {
            Hashtable<String, String> subProcessesActivitieRecord = subProcessesActivitieRecords.getRecords()[0];
            String nsub = subProcessesActivitieRecord.get(ProcessEngineConstants.Variables.Process.SUBPROCESS_NUMBER);
            Hashtable<String, String> subtab = new Hashtable<String, String>();
            subtab.put(ProcessEngineConstants.Variables.Process.SUBPROCESS_ID, nsub);
            RecordSet startsActivityRecords = myService.executeRule(ProcessEngineConstants.Rules.Process.Subprocess.START_ACTIVITY_SUB_PROCESS,
                                                                    subtab, AlgernonConstants.TELL);
            if (startsActivityRecords != null && startsActivityRecords.getNoOfRecords() > 0)
            {
                Hashtable<String, String> startsActivityRecord = startsActivityRecords.getRecords()[0];
                String firstActivity = startsActivityRecord.get(ProcessEngineConstants.Variables.Common.ID);
                Hashtable<String, String> sendActivityTab = new Hashtable<String, String>();
                sendActivityTab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_DE, firstActivity);
                this.checkForActivityRule(firstActivity);
                // sendEmail(firstActivity);
                // myService.executeRule(ProcessEngineConstants.Rules.Activity.SEND_EMAIL_FOR_ACTIVITY, sendActivityTab, AlgernonConstants.ASK);

            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getMembers(java.lang.String)
     */
    @Override
    public RecordSet getMembers(String organizationID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.ORGANISATION_ID, new AlgernonValue(organizationID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Organisation.GET_MEMBERS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Organisation.GET_MEMBERS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActivity(java.lang.String)
     */
    @Override
    public RecordSet getActivity(String id) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(id, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Activity.GET_ACTIVITY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Activity.GET_ACTIVITY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getMean(java.lang.String)
     */
    @Override
    public RecordSet getMean(String fundID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Fund.FUND_ID, new AlgernonValue(fundID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Mean.GET_FUND, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Mean.GET_FUND, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getRole(java.lang.String)
     */
    @Override
    public RecordSet getRole(String roleID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Role.ROLLE_ID, new AlgernonValue(roleID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Role.GET_ROLE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Role.GET_ROLE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProcessinformation(java.lang.String)
     */
    @Override
    public RecordSet getProcessinformation(String processinformationID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.InformationType.PROCESSINFORMATION_ID, new AlgernonValue(processinformationID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.ProcessInformation.GET_PROCESSINFORMATION, tab,
                                                       AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.ProcessInformation.GET_PROCESSINFORMATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getResultsMemory(java.lang.String)
     */
    @Override
    public RecordSet getResultsMemory(String resultsMemoryID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(resultsMemoryID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.ResultsMemory.GET_RESULTSMEMORY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.ResultsMemory.GET_RESULTSMEMORY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getWork(java.lang.String)
     */
    @Override
    public RecordSet getWork(String workID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(workID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Work.GET_WORK, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Work.GET_WORK, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getFunction(java.lang.String)
     */
    @Override
    public RecordSet getFunction(String functionID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(functionID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Function.GET_FUNCTION, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Function.GET_FUNCTION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProduct(java.lang.String)
     */
    @Override
    public RecordSet getProduct(String productID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(productID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Product.GET_PRODUCT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Product.GET_PRODUCT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProductWay(java.lang.String)
     */
    @Override
    public RecordSet getProductWay(String productWayID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(productWayID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Product.GET_PRODUCTWAY, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Product.GET_PRODUCTWAY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getModels()
     */
    @Override
    public RecordSet getModels() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(EditorEngineConstants.Rules.Common.GET_MODELS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EditorEngineConstants.Rules.Common.GET_MODELS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getInformationType(java.lang.String)
     */
    @Override
    public RecordSet getInformationType(String id) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(id, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_INFORMATION_TYPE, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_INFORMATION_TYPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getInformationTypeID(java.lang.String)
     */
    @Override
    public RecordSet getInformationTypeID(String referenceID) throws OntologyErrorException
    {
        LOG.debug("java:getInformationTypeID  referenceID : " + referenceID);
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Common.InformationType.REFERENCE_ID, new AlgernonValue(referenceID, false));
        RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Common.GET_INFORMATION_TYPE_ID, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Common.GET_INFORMATION_TYPE_ID, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getAllExistSubProcesses()
     */
    @Override
    public RecordSet getAllExistSubProcesses() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        RecordSet result = myService.executeRule(EngineConstants.Rules.SubProcess.GET_ALL_EXIST_SUB_PROECESSES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.SubProcess.GET_ALL_EXIST_SUB_PROECESSES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#isSubProcess(String)
     */
    @Override
    public RecordSet isSubProcess(String processID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(processID, false));
        RecordSet result = myService.executeRule(EngineConstants.Rules.SubProcess.IS_SUB_PROECESS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.SubProcess.IS_SUB_PROECESS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#addRoleToOrganization(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet addRoleToOrganization(String roleID, String organizationID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Role.ROLLE_ID, new AlgernonValue(roleID, false));
        tab.put(ProcessEngineConstants.Variables.Organisation.ORGANISATION_ID, new AlgernonValue(organizationID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Role.ADD_ROLE_TO_ORGANIZATION, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Role.ADD_ROLE_TO_ORGANIZATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getRolesToOrganization(java.lang.String)
     */
    @Override
    public RecordSet getRolesToOrganization(String organizationID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Organisation.ORGANISATION_ID, new AlgernonValue(organizationID, false));
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Role.GET_ROLES_TO_ORGANIZATION, tab, AlgernonConstants.TELL);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Role.GET_ROLES_TO_ORGANIZATION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getGlobalRoles()
     */
    @Override
    public RecordSet getGlobalRoles() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Role.GET_GLOBAL_ROLES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Role.GET_GLOBAL_ROLES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSubprocesses(java.lang.String)
     */
    public RecordSet getSubprocesses(String processInstanceID) throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> processInstanceHash = new Hashtable<String, AlgernonValue>();
        processInstanceHash.put(ProcessEngineConstants.Variables.Common.ID, new AlgernonValue(processInstanceID, false));
        final RecordSet subprocessRecords = this.myService.executeRule(ProcessEngineConstants.Rules.Process.Subprocess.GET_SUBPROCESSES,
                                                                       processInstanceHash, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.Subprocess.GET_SUBPROCESSES, subprocessRecords, this.getClass());

        return subprocessRecords;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getGlobalMeans()
     */
    @Override
    public RecordSet getGlobalMeans() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Mean.GET_GLOBAL_MEANS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Mean.GET_GLOBAL_MEANS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getGlobalResultsMem()
     */
    @Override
    public RecordSet getGlobalResultsMem() throws OntologyErrorException
    {
        final Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        final RecordSet result = myService.executeRule(EngineConstants.Rules.ResultsMemory.GET_GLOBAL_RESULTS_MEM, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.ResultsMemory.GET_GLOBAL_RESULTS_MEM, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSubProcessTypes(java.lang.String)
     */
    @Override
    public RecordSet getSubProcessTypes(String processTypeID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Process.PARENT, processTypeID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_SUB_PROCESS_TYPES, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_SUB_PROCESS_TYPES, result, this.getClass());
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getTypeProcesses(java.lang.String)
     */
    @Override
    public RecordSet getTypeProcesses(String processTypeID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID, processTypeID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_TYPE_PROCESSES, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_TYPE_PROCESSES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getEnabledProcessesOfType(java.lang.String)
     */
    @Override
    public RecordSet getEnabledProcessesOfType(String processTypeID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID, processTypeID);
        final RecordSet result = myService.executeRule(ProcessEngineConstants.Rules.Process.GET_ENABLED_PROCESSESES_OF_TYPE, para,
                                                       AlgernonConstants.ASK);
        LoggingUtility.logResult(ProcessEngineConstants.Rules.Process.GET_ENABLED_PROCESSESES_OF_TYPE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getStartRole(java.lang.String)
     */
    @Override
    public RecordSet getStartRole(String processID) throws OntologyErrorException
    {
        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(EngineConstants.Variables.Process.PROCESS_ID, processID);
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Process.GET_START_ROLE, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.GET_START_ROLE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws OntologyErrorException
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getInitialTemplateID(java.lang.String)
     */
    @Override
    public String getInitialTemplateID(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);

        String templateID = commonHelper.getTemplateID(processID);

        RecordSet initialVersionSlotResult = getInitialTemplate(templateID);

        if (initialVersionSlotResult.getResult().equals(AlgernonConstants.ERROR))
        {
            throw new IllegalStateException("Error retrieving the initial template ID for process / template with ID " + processID);
        }

        // check, if the template is already the initial version (FAILED means that the slot is empty!)
        if (initialVersionSlotResult.getResult().equals(AlgernonConstants.FAILED)
                || initialVersionSlotResult.getResult().equals(AlgernonConstants.OK) && initialVersionSlotResult.getNoOfRecords() == 0)
        {
            return templateID;
        }

        // the template is not the initial version, so return the initial id
        return initialVersionSlotResult.getSingleResult(ProcessEngineConstants.Variables.Common.TEMPLATE_ID_EN);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProcessIDForVersion(java.lang.String, java.lang.String)
     */
    @Override
    public String getProcessIDForVersion(String initialTemplateID, String version) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> para = new Hashtable<String, AlgernonValue>();
        para.put(ProcessEngineConstants.Variables.Process.PROCESS_ID, new AlgernonValue(initialTemplateID, false));
        para.put(EngineConstants.Variables.Process.USER_DEFINED_VERSION, new AlgernonValue(version, true));
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Process.GET_PROCESS_FOR_VERSION, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.GET_PROCESS_FOR_VERSION, result, this.getClass());
        if (result.getResult().equals(AlgernonConstants.OK) && result.getNoOfRecords() == 1)
        {
            return result.getSingleResult(ProcessEngineConstants.Variables.Common.TEMPLATE_ID_EN);
        }
        else if (result.getResult().equals(AlgernonConstants.ERROR))
        {
            throw new IllegalStateException("Error while retrieving version " + version + " for initial process template ID " + initialTemplateID);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getTemplateVersions(java.lang.String)
     */
    @Override
    public RecordSet getTemplateVersions(String initialTemplateID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(EngineConstants.Variables.Process.TEMPLATE_ID, initialTemplateID);
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Process.GET_TEMPLATE_VERSIONS, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.GET_TEMPLATE_VERSIONS, result, this.getClass());
        return result;
    }

    /**
     * Returns the initial template ID for the given template.
     * 
     * If the given template ID has no value in the slot {@link org.prowim.services.coreengine.EngineConstants.Slots#IS_VERSION_OF}, the returned {@link RecordSet} has a failed state.
     * 
     * @param templateID the template ID to return the initial template for
     * @return {@link RecordSet} with info, never null
     * @throws OntologyErrorException if an error occurs in the ontology
     */
    public RecordSet getInitialTemplate(String templateID) throws OntologyErrorException
    {
        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Common.ID, templateID);
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Process.GET_INITIAL_TEMPLATE, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.GET_INITIAL_TEMPLATE, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getElementsOfProcess(java.lang.String)
     */
    @Override
    public RecordSet getElementsOfProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);

        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(EngineConstants.Variables.Process.PROCESS_ID, processID);
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Process.GET_ELEMENTS_OF_PROCESS, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.GET_ELEMENTS_OF_PROCESS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getInitialVersionID(java.lang.String)
     */
    @Override
    public String getInitialVersionID(String instanceID) throws OntologyErrorException
    {
        Validate.notNull(instanceID);

        Hashtable<String, String> para = new Hashtable<String, String>();
        para.put(ProcessEngineConstants.Variables.Common.ID, instanceID);
        final RecordSet result = myService.executeRule(EngineConstants.Rules.Process.GET_INITIAL_VERSION, para, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.GET_INITIAL_VERSION, result, this.getClass());

        if (result.getResult().equals(AlgernonConstants.ERROR))
        {
            throw new IllegalStateException("Error retrieving the initial version ID for process element with ID " + instanceID);
        }

        // check, if the given ID is already the initial version (FAILED means that the slot is empty!)
        if (result.getResult().equals(AlgernonConstants.FAILED) || result.getResult().equals(AlgernonConstants.OK) && result.getNoOfRecords() == 0)
        {
            return instanceID;
        }

        // the template is not the initial version, so return the initial id
        return result.getSingleResult(ProcessEngineConstants.Variables.Common.INITIAL_VERSION_ID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeObjectsOfProcess(java.lang.String)
     */
    @Override
    public RecordSet getKnowledgeObjectsOfProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);

        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(EngineConstants.Variables.Process.PROCESS_ID, processID);
        RecordSet result = myService.executeRule(EngineConstants.Rules.KnowledgeObject.GET_KNOWLEDGE_OBJECT_OF_PROCESS, parameters,
                                                 AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.KnowledgeObject.GET_KNOWLEDGE_OBJECT_OF_PROCESS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSubProcessOfActivity(java.lang.String)
     */
    @Override
    public RecordSet getSubProcessOfActivity(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);

        Hashtable<String, AlgernonValue> parameters = new Hashtable<String, AlgernonValue>();
        parameters.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        RecordSet result = myService.executeRule(EngineConstants.Rules.Activity.GET_SUB_PROCESS_OF_ACTIVIY, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Activity.GET_SUB_PROCESS_OF_ACTIVIY, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getActivityMeans(java.lang.String)
     */
    @Override
    public RecordSet getActivityMeans(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);

        Hashtable<String, AlgernonValue> parameters = new Hashtable<String, AlgernonValue>();
        parameters.put(EngineConstants.Variables.Activity.ACTIVITY_ID, new AlgernonValue(activityID, false));
        RecordSet result = myService.executeRule(EngineConstants.Rules.Activity.GET_ACTIVIY_MEANS, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Activity.GET_ACTIVIY_MEANS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getSubprocessCaller(java.lang.String)
     */
    public RecordSet getSubprocessCaller(String subprocessID) throws OntologyErrorException
    {
        Hashtable<String, String> tabCaller = new Hashtable<String, String>();
        tabCaller.put(EngineConstants.Variables.Process.SUB_PROCESS_ID, subprocessID);
        RecordSet callerResult = myService.executeRule(EngineConstants.Rules.SubProcess.GET_SUB_PROCESS_CALLER, tabCaller, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.SubProcess.GET_SUB_PROCESS_CALLER, callerResult, this.getClass());
        return callerResult;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getPossibleSelection(java.lang.String)
     */
    @Override
    public RecordSet getPossibleSelection(String processInformationID) throws OntologyErrorException
    {
        Hashtable<String, String> processInformationTab = new Hashtable<String, String>();
        processInformationTab.put(EngineConstants.Variables.Common.ELEMENT_ID, processInformationID);
        RecordSet result = myService.executeRule(EngineConstants.Rules.Workflow.GET_POSSIBLE_SELECTION, processInformationTab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Workflow.GET_POSSIBLE_SELECTION, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getRelationsOfKnowledgeObject(java.lang.String)
     */
    @Override
    public RecordSet getRelationsOfKnowledgeObject(String knowldgeObjectID) throws OntologyErrorException
    {
        Validate.notNull(knowldgeObjectID);

        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(EngineConstants.Variables.KnowledgeObject.KNOWLEDGE_OBJECT_ID, knowldgeObjectID);
        final RecordSet result = myService.executeRule(EngineConstants.Rules.KnowledgeObject.GET_RELATIONS_OF_KNOWLEDGE_OBJECT, parameters,
                                                       AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.KnowledgeObject.GET_RELATIONS_OF_KNOWLEDGE_OBJECT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getProcess(java.lang.String)
     */
    @Override
    public RecordSet getProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);

        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(EngineConstants.Variables.Process.PROCESS_ID, processID);
        RecordSet result = myService.executeRule(EngineConstants.Rules.Process.GET_PROCESS, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.GET_PROCESS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#searchProcess(java.lang.String)
     */
    @Override
    public RecordSet searchProcess(String pattern) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.PATTERN, new AlgernonValue(pattern, true));
        RecordSet result = myService.executeRule(EngineConstants.Rules.Process.SEARCH_PROCESSES, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.SEARCH_PROCESSES, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#searchKnowledgeObjectsKeyWords(java.lang.String)
     */
    @Override
    public RecordSet searchKnowledgeObjectsKeyWords(String pattern) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(ProcessEngineConstants.Variables.Knowledge.PATTERN, new AlgernonValue(pattern, true));
        RecordSet result = myService.executeRule(EngineConstants.Rules.KnowledgeObject.SEARCH_KNOWLEDGEOBJECT_KEY_WORDS, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.KnowledgeObject.SEARCH_KNOWLEDGEOBJECT_KEY_WORDS, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#getKnowledgeKeyWords(java.lang.String)
     */
    @Override
    public RecordSet getKnowledgeKeyWords(String knowledgeObjectID) throws OntologyErrorException
    {
        Hashtable<String, AlgernonValue> tab = new Hashtable<String, AlgernonValue>();
        tab.put(EngineConstants.Variables.KnowledgeObject.KNOWLEDGE_OBJECT_ID, new AlgernonValue(knowledgeObjectID, true));
        RecordSet result = myService.executeRule(EngineConstants.Rules.KnowledgeObject.GET_KEY_WORDS_OF_KNOWLEDGE_OBJECT, tab, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.KnowledgeObject.GET_KEY_WORDS_OF_KNOWLEDGE_OBJECT, result, this.getClass());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessEngine#isProcessLandscape(java.lang.String)
     */
    @Override
    public RecordSet isProcessLandscape(String processID) throws OntologyErrorException
    {
        Hashtable<String, String> parameters = new Hashtable<String, String>();
        parameters.put(EngineConstants.Variables.Process.PROCESS_ID, processID);
        RecordSet result = myService.executeRule(EngineConstants.Rules.Process.IS_PROCESS_LANDSCAPE, parameters, AlgernonConstants.ASK);
        LoggingUtility.logResult(EngineConstants.Rules.Process.IS_PROCESS_LANDSCAPE, result, this.getClass());
        return result;
    }

}