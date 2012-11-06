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

import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ActivityArray;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.ParameterComparator;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.collections.ProductArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.InformationType;
import org.prowim.datamodel.prowim.InformationTypesConstants;
import org.prowim.datamodel.prowim.Knowledge;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.ParameterConstraint;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.datamodel.prowim.Product;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EditorHelper;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ParameterHelper;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.ProcessEngineConstants;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.WorkflowEngine;
import org.prowim.services.coreengine.EngineConstants.Slots;
import org.prowim.services.coreengine.entities.KnowledgeEntity;
import org.prowim.services.coreengine.entities.MeanEntity;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.coreengine.entities.ProcessInformationEntity;
import org.prowim.services.coreengine.entities.ResultsMemoryEntity;
import org.prowim.services.coreengine.entities.RoleEntity;
import org.prowim.services.coreengine.entities.impl.DefaultSecurityManager;
import org.prowim.services.coreengine.security.SecurityEngine;
import org.prowim.services.ejb.editor.EditorRemote;
import org.prowim.utils.StringConverter;
import org.prowim.utils.xml.XMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.ebcot.tools.logging.Logger;


/**
 * The implementation for the {@link ProcessHelper process helper}
 * 
 * @author Saad Wardi
 * @version $Revision: 5100 $
 */
@Stateless
public class ProcessHelperBean implements ProcessHelper
{
    private static final Logger      LOG = Logger.getLogger(ProcessHelperBean.class);

    @Resource
    private SessionContext           sessionContext;

    @IgnoreDependency
    @EJB
    private ProcessEngine            processEngine;

    @IgnoreDependency
    @EJB
    private WorkflowEngine           workflowEngine;

    @IgnoreDependency
    @EJB
    private CommonHelper             commonHelper;

    @IgnoreDependency
    @EJB
    private KnowledgeEntity          knowledgeEntity;

    @IgnoreDependency
    @EJB
    private OrganizationEntity       organizationEntity;

    @IgnoreDependency
    @EJB
    private ProcessInformationEntity processInformationEntity;

    @IgnoreDependency
    @EJB
    private RoleEntity               roleEntity;

    @IgnoreDependency
    @EJB
    private MeanEntity               meanEntity;

    @IgnoreDependency
    @EJB
    private ResultsMemoryEntity      resultsMemoryEntity;

    @IgnoreDependency
    @EJB
    private ParameterHelper          parameterHelper;

    @IgnoreDependency
    @EJB
    private EditorHelper             editor;

    @IgnoreDependency
    @EJB
    private SecurityEngine           securityEngine;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getMyActiveActivities(java.lang.String)
     */
    public Activity[] getMyActiveActivities(String userID) throws OntologyErrorException
    {
        Validate.notNull(userID);
        final RecordSet result = processEngine.getMyActiveActivities(userID);
        Activity[] activities = new Activity[0];
        if (result.getNoOfRecords() > 0)
        {
            activities = new Activity[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {

                Hashtable<String, String> activity = result.getRecords()[i];
                String activityID = activity.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_ID);
                String createTime = activity.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                String automatic = activity.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_AUTOMATIC);
                boolean automaticValue = false;
                if (automatic != null && automatic.equals(ProcessEngineConstants.Variables.Common.ALGERNON_TRUE))
                {
                    automaticValue = true;
                }
                String status = activity.get(ProcessEngineConstants.Variables.Activity.STATUS);
                String description = (activity.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT));
                String name = activity.get(ProcessEngineConstants.Variables.Common.DESCRIPTION);

                activities[i] = DefaultDataObjectFactory.createActivity(activityID, createTime, name);
                activities[i].setAutomatic(automaticValue);
                activities[i].setStatus(status);
                activities[i].setDescription(description);

                activities[i].setProcessName(activity.get(ProcessEngineConstants.Variables.Process.PROCESS));
                String startTime = activity.get(ProcessEngineConstants.Variables.Common.START);
                if (startTime == null || startTime.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    startTime = ProcessEngineConstants.Variables.Common.ZERO;
                }
                activities[i].setStartTime(startTime);
                boolean finishedValue = false;
                String finished = activity.get(ProcessEngineConstants.Variables.Activity.FINISHED);
                String processID = activity.get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
                if (finished != null && finished.equalsIgnoreCase(ProcessEngineConstants.Variables.Common.OK))
                {
                    finished = ProcessEngineConstants.Variables.Common.TRUE;
                    finishedValue = true;
                }
                else
                {
                    finished = ProcessEngineConstants.Variables.Common.FALSE;
                    finishedValue = false;
                }
                activities[i].setFinished(finishedValue);
                activities[i].setProcessID(processID);
            }
        }

        return activities;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getAllProcesses()
     */
    public Process[] getAllProcesses() throws OntologyErrorException
    {
        final RecordSet result = processEngine.getAlleProzesse();
        Process[] processes = null;
        if (result.getNoOfRecords() > 0)
        {
            processes = new Process[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];

                String name = record.get(ProcessEngineConstants.Variables.Process.PROCESS_IDENT);
                String templateID = record.get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
                String processtype = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE);
                LOG.debug("THE PROCESS TYPE :  " + processtype);
                processes[i] = DefaultDataObjectFactory.createProwimProcessTemplate(templateID, name);
                if (processtype != null)
                {
                    processes[i].setType(processtype);
                }
                else
                {
                    processes[i].setType(ProcessEngineConstants.Variables.Process.CORE_PROCESS);
                }

                processes[i].setProcessLandscape(isProcessLandscape(templateID));
            }
            Arrays.sort(processes);
        }
        return processes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessKnowledge(org.prowim.datamodel.prowim.Process[])
     */
    public Knowledge[] getProcessKnowledge(Process[] processes) throws OntologyErrorException
    {
        Validate.notNull(processes);
        Knowledge[] processKnowledges = new Knowledge[0];
        if (processes.length > 0)
        {
            processKnowledges = new Knowledge[processes.length];
            for (int i = 0; i < processes.length; i++)
            {
                processKnowledges[i] = DefaultDataObjectFactory.createKnowledge();
                processKnowledges[i].setProcess(processes[i]);
                KnowledgeObjectArray knowledgeObjects = knowledgeEntity.getKnowledgeObjects(processes[i].getTemplateID());
                if (knowledgeObjects != null)
                {
                    LinkedList<KnowledgeObject> kolist = new LinkedList<KnowledgeObject>();
                    for (int j = 0; j < knowledgeObjects.size(); j++)
                    {
                        kolist.add(knowledgeObjects.getItem().get(j));
                    }
                    processKnowledges[i].setKnowledgeObjectsList(kolist);
                }
            }
        }
        return processKnowledges;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getActivityKnowledge(java.lang.String)
     */
    public KnowledgeObjectArray getActivityKnowledge(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        KnowledgeObjectArray knowledgeobjectArray = knowledgeEntity.getKnowledgeObjects(activityID);
        if (knowledgeobjectArray != null)
        {
            return knowledgeobjectArray;
        }
        return new KnowledgeObjectArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getActivities(java.lang.String)
     */
    public Activity[] getActivities(String template) throws OntologyErrorException
    {
        Validate.notNull(template);
        final RecordSet result = processEngine.getActivities(template);
        Activity[] activities = new Activity[0];
        if (result.getNoOfRecords() > 0)
        {
            activities = new Activity[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String id = record.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_ID);
                String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                String automatic = record.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_AUTOMATIC);
                String status = record.get(ProcessEngineConstants.Variables.Activity.STATUS);
                boolean automaticValue = false;
                if (automatic.equals(ProcessEngineConstants.Variables.Common.ALGERNON_FALSE))
                {
                    automaticValue = false;
                }
                else if (automatic.equals(ProcessEngineConstants.Variables.Common.ALGERNON_TRUE))
                {
                    automaticValue = true;
                }

                String description = record.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                String name = record.get(ProcessEngineConstants.Variables.Activity.DESCRIPTION_ACTIVITY);

                activities[i] = DefaultDataObjectFactory.createActivity(id, createTime, name);
                activities[i].setAutomatic(automaticValue);
                activities[i].setStatus(status);
                activities[i].setDescription(description);
            }

            Arrays.sort(activities);
        }
        return activities;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getTopDomainKnowledge()
     */
    public KnowledgeDomain[] getTopDomainKnowledge() throws OntologyErrorException
    {
        KnowledgeDomain[] dkn = new KnowledgeDomain[0];
        RecordSet domains = processEngine.getToplevelDomaenen();
        if (domains != null && domains.getNoOfRecords() > 0)
        {
            dkn = new KnowledgeDomain[domains.getNoOfRecords()];
            for (int i = 0; i < domains.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = domains.getRecords()[i];
                String domainName = record.get(ProcessEngineConstants.Variables.Common.NAME);
                String instance = record.get(ProcessEngineConstants.Variables.Common.ID);
                String anzsub = record.get(ProcessEngineConstants.Variables.Knowledge.SUBDOMAINS_COUNT);
                String anzwob = record.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECTS_COUNT);
                String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                KnowledgeDomain domain = DefaultDataObjectFactory.createKnowledgeDomain(instance, domainName, createTime);
                domain.setSubdomains(this.getSubKnowledgeDomain(domain.getID()));
                try
                {
                    domain.setCountSubdomains(new Integer(anzsub).intValue());
                    domain.setCountKnowledge(new Integer(anzwob).intValue());
                    domain.setHasSubDomains(new Integer(anzsub).intValue());
                }
                catch (NumberFormatException e)
                {
                    LOG.error("Wrong type", e);
                }

                dkn[i] = domain;
            }
        }
        if (dkn != null)
        {
            Arrays.sort(dkn);
        }

        return dkn;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getSubKnowledgeDomain(java.lang.String)
     */
    public KnowledgeDomain[] getSubKnowledgeDomain(String topDomain) throws OntologyErrorException
    {
        Validate.notNull(topDomain);
        KnowledgeDomain[] domainKnowledges = new KnowledgeDomain[0];
        final RecordSet domains = processEngine.getSubdomaenen(topDomain);
        if (domains != null && domains.getNoOfRecords() > 0)
        {
            domainKnowledges = new KnowledgeDomain[domains.getNoOfRecords()];
            for (int i = 0; i < domains.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = domains.getRecords()[i];
                String domainName = record.get(ProcessEngineConstants.Variables.Common.NAME);
                String instance = record.get(ProcessEngineConstants.Variables.Common.ID);
                String countSubdomains = record.get(ProcessEngineConstants.Variables.Knowledge.SUBDOMAINS_COUNT);
                String countKnowledgeObjects = record.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECTS_COUNT);
                String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                KnowledgeDomain domain = DefaultDataObjectFactory.createKnowledgeDomain(instance, domainName, createTime);

                try
                {
                    domain.setCountSubdomains(new Integer(countSubdomains).intValue());
                    domain.setCountKnowledge(new Integer(countKnowledgeObjects).intValue());
                    domain.setHasSubDomains(new Integer(countSubdomains).intValue());
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
                domainKnowledges[i] = domain;
            }
        }
        return domainKnowledges;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getDomainKnowledgeObjects(java.lang.String)
     */
    public List<KnowledgeObject> getDomainKnowledgeObjects(String domainID) throws OntologyErrorException
    {
        Validate.notNull(domainID);
        final RecordSet result = processEngine.getKnowledgeObjectsDomains(domainID);
        List<KnowledgeObject> knowledgeObjects = new ArrayList<KnowledgeObject>();
        if (result != null && result.getNoOfRecords() > 0)
        {
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String knowledgeObjectID = record.get(ProcessEngineConstants.Variables.Common.ID);
                final String name = record.get(ProcessEngineConstants.Variables.Common.NAME);
                final String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                final String description = record.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                final String creatorID = record.get(ProcessEngineConstants.Variables.Organisation.Person.ID);

                KnowledgeObject knowledgeObject = DefaultDataObjectFactory.createKnowledgeObject(knowledgeObjectID, name, createTime);
                knowledgeObject.setOwner(creatorID);
                knowledgeObject.setDescription(description);
                knowledgeObjects.add(knowledgeEntity.getKnowledgeObject(knowledgeObject));
            }
        }
        return knowledgeObjects;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessType(java.lang.String)
     */
    public ProcessType getProcessType(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        final RecordSet result = processEngine.getProcessType(processID);
        ProcessType processType = null;
        if (result != null && result.getNoOfRecords() == 1)
        {
            Hashtable<String, String> record = result.getRecords()[0];
            String id = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID);
            String name = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_NAME);
            String description = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_DESC);

            processType = DefaultDataObjectFactory.createProcessType(id, name, description);
        }
        return processType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getAllProcessTypes()
     */
    public ProcessType[] getAllProcessTypes() throws OntologyErrorException
    {
        final RecordSet result = processEngine.getAllProcessTypes();
        ProcessType[] processType = new ProcessType[0];
        if (result.getNoOfRecords() > 0)
        {
            processType = new ProcessType[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String id = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID);
                String name = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_NAME);
                String description = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_DESC);

                processType[i] = DefaultDataObjectFactory.createProcessType(id, name, description);
            }

            Arrays.sort(processType);
        }
        return processType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getAllTopProcessTypes()
     */
    public ProcessType[] getAllTopProcessTypes() throws OntologyErrorException
    {
        final RecordSet result = processEngine.getAllTopProcessTypes();
        ProcessType[] processType = new ProcessType[0];
        if (result.getNoOfRecords() > 0)
        {
            processType = new ProcessType[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String id = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID);
                String name = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_NAME);
                String description = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_DESC);

                processType[i] = DefaultDataObjectFactory.createProcessType(id, name, description);
            }

            Arrays.sort(processType);
        }
        return processType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#setProcessType(java.lang.String, java.lang.String)
     */
    public String setProcessType(String processTypeID, String processID) throws OntologyErrorException
    {
        Validate.notNull(processTypeID);
        Validate.notNull(processID);

        processEngine.setProcessType(processTypeID, processID);
        return AlgernonConstants.OK;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#createProcessType(java.lang.String, java.lang.String)
     */
    public String createProcessType(String name, String description) throws OntologyErrorException
    {
        Validate.notNull(name);
        Validate.notNull(description);

        final RecordSet result = processEngine.createProcessType(name, description);
        String processTypeID = null;
        if (result.getNoOfRecords() > 0)
        {
            processTypeID = result.getRecords()[0].get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID);
        }
        if (processTypeID == null)
            throw new IllegalStateException("Could not create process type for name: " + name + " and description: " + description);

        return processTypeID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#setProcessTypeParent(java.lang.String, java.lang.String)
     */
    public void setProcessTypeParent(String typeID, String parentTypeID) throws OntologyErrorException
    {
        Validate.notNull(typeID);

        final RecordSet result = processEngine.setProcessTypeParent(typeID, parentTypeID);
        if (result.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("ProcessType with ID: " + typeID + " seted as subcategory of " + parentTypeID);
        }
        else
        {
            throw new IllegalStateException("Could not set ProcessTypeParent with ID: " + typeID + " as subcategory of " + parentTypeID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getExecutableProcesses(java.lang.String)
     */
    public Process[] getExecutableProcesses(String userID) throws OntologyErrorException
    {
        Validate.notNull(userID);
        // Gets all actual processes
        final RecordSet result = processEngine.getExecutableProcesses(userID);
        Process[] processes = new Process[0];
        if (result.getNoOfRecords() > 0)
        {
            processes = new Process[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String name = (record.get(ProcessEngineConstants.Variables.Process.PROCESS_IDENT));
                String templateID = (record.get(ProcessEngineConstants.Variables.Process.PROCESS_ID));
                processes[i] = DefaultDataObjectFactory.createProwimProcessTemplate(templateID, name);
                processes[i].setType(record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE));
                processes[i].setDescription(record.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT));
            }
            Arrays.sort(processes);
        }

        return processes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getRunningProcesses(java.lang.String)
     */
    public Process[] getRunningProcesses(final String userID) throws OntologyErrorException
    {
        Validate.notNull(userID);
        final RecordSet result = processEngine.getRunningProcesses(userID);
        Process[] processes = new Process[0];

        if (result.getNoOfRecords() > 0)
        {
            processes = new Process[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> process = result.getRecords()[i];

                String name = process.get(ProcessEngineConstants.Variables.Common.VAR_VARIABLE);
                String instanceID = process.get(ProcessEngineConstants.Variables.Common.ID);
                String templateID = process.get(ProcessEngineConstants.Variables.Common.TEMPLATE_ID);

                processes[i] = DefaultDataObjectFactory.createProwimProcessInstance(templateID, instanceID, name);
                processes[i].setDescription(process.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT));
                processes[i].setStarter(organizationEntity.getPerson(process.get(ProcessEngineConstants.Variables.Process.STARTER)));
                String startTimestamp = process.get(ProcessEngineConstants.Variables.Common.START);
                if (startTimestamp == null || startTimestamp.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    startTimestamp = ProcessEngineConstants.Variables.Common.ZERO;
                }
                processes[i].setStartTime(startTimestamp);
                String processtype = process.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE);
                processes[i].setType(processtype);

                // get the currently active activities for the running process
                final RecordSet recordSet = processEngine.getActiveActivities(process.get(ProcessEngineConstants.Variables.Common.INS));
                Activity[] activities = new Activity[0];

                if (recordSet.getNoOfRecords() > 0)
                {
                    activities = new Activity[recordSet.getNoOfRecords()];
                    for (int j = 0; j < recordSet.getNoOfRecords(); j++)
                    {
                        Hashtable<String, String> activity = recordSet.getRecords()[j];
                        String activityID = activity.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_ID);
                        String createTime = activity.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                        String automatic = activity.get(ProcessEngineConstants.Variables.Activity.ACTIVITY_AUTOMATIC);
                        boolean automaticValue = false;
                        if (automatic.equals(ProcessEngineConstants.Variables.Common.ALGERNON_TRUE))
                        {
                            automaticValue = true;
                        }
                        String status = activity.get(ProcessEngineConstants.Variables.Activity.STATUS);
                        String description = activity.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                        String activityName = activity.get(ProcessEngineConstants.Variables.Common.DESCRIPTION);
                        activities[j] = DefaultDataObjectFactory.createActivity(activityID, createTime, activityName);
                        activities[j].setAutomatic(automaticValue);
                        activities[j].setStatus(status);
                        activities[j].setDescription(description);

                        String person = activity.get(ProcessEngineConstants.Variables.Organisation.Person.PERSON);
                        String auto = activity.get(ProcessEngineConstants.Variables.Common.AUTO);
                        if (automaticValue)
                        {
                            person = ProcessEngineConstants.Variables.Activity.AUTOMATIC;
                        }
                        else if (auto != null && auto.equalsIgnoreCase(ProcessEngineConstants.Variables.Common.ALGERNON_FALSE)
                                && (person == null || person.equals(ProcessEngineConstants.Variables.Common.EMPTY)))
                        {
                            person = ProcessEngineConstants.Variables.Common.UNKNOWN;
                        }
                        activities[j].setUserID(person);
                        startTimestamp = activity.get(ProcessEngineConstants.Variables.Common.START);
                        if (startTimestamp == null || startTimestamp.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                        {
                            startTimestamp = ProcessEngineConstants.Variables.Common.ZERO;
                        }
                        activities[j].setStartTime(startTimestamp);
                        if ( !automaticValue)
                        {
                            String finished = activity.get(ProcessEngineConstants.Variables.Activity.FINISHED);
                            boolean finishedValue = false;
                            if (finished != null && finished.equalsIgnoreCase(ProcessEngineConstants.Variables.Common.OK))
                            {
                                finished = ProcessEngineConstants.Variables.Common.TRUE;
                                finishedValue = true;
                            }
                            else
                            {
                                finished = ProcessEngineConstants.Variables.Common.FALSE;
                                finishedValue = false;
                            }
                            activities[j].setFinished(finishedValue);
                        }

                        LOG.debug("Process: " + processes[i].getName() + " has numb. of activities: " + recordSet.getNoOfRecords());
                        LOG.debug("Process " + processes[i].getName() + ": " + j + "the activity: " + activities[j].getName() + " auto: " + auto);
                    }
                    Arrays.sort(activities);
                }
                processes[i].setActivities(Arrays.asList(activities));
            }
            Arrays.sort(processes);
        }

        return processes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getUsers()
     */
    public Person[] getUsers() throws OntologyErrorException
    {
        Person[] users = new Person[0];
        final RecordSet result = processEngine.getUsers();
        if (result.getNoOfRecords() > 0)
        {
            users = new Person[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String id = record.get(ProcessEngineConstants.Variables.Common.ID);
                users[i] = organizationEntity.getPerson(id);
            }
            if (users != null)
            {
                Arrays.sort(users);
            }
        }
        return users;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getRolesForWorkFlow(java.lang.String)
     */
    public RoleArray getRolesForWorkFlow(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        final RecordSet result = processEngine.getRoles(processID);
        final RoleArray theRoles = new RoleArray();
        final HashMap<String, Role> roleMap = new HashMap<String, Role>();

        for (int i = 0; i < result.getNoOfRecords(); i++)
        {
            Hashtable<String, String> record = result.getRecords()[i];

            String id = record.get(ProcessEngineConstants.Variables.Common.ID);
            String name = record.get(ProcessEngineConstants.Variables.Role.NAME);
            String processid = record.get(ProcessEngineConstants.Variables.Common.INS);
            String type = record.get(ProcessEngineConstants.Variables.Common.TYPE);
            String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);

            Role role = DefaultDataObjectFactory.createRole(id, name, createTime, processid, type, new ArrayList<Person>());

            /** collect the list of persons that will be assigned to the role and setPersonList. */
            final RecordSet personRecords = processEngine.getAssignedPersonsToRole(id);
            final List<Person> assignedPersons = new ArrayList<Person>();
            for (int j = 0; j < personRecords.getNoOfRecords(); j++)
            {
                Hashtable<String, String> personRecord = personRecords.getRecords()[j];
                String personID = personRecord.get(ProcessEngineConstants.Variables.Organisation.Person.ID);
                Person person = organizationEntity.getPerson(personID);
                if (person != null)
                {
                    assignedPersons.add(person);
                }
            }
            role.setPersonsList(assignedPersons);

            // store only roles, which are not processed before -> unique role list
            if ( !roleMap.containsKey(role.getID()))
            {
                roleMap.put(role.getID(), role);
                theRoles.add(role);
            }
        }

        return theRoles;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#deleteProcess(java.lang.String)
     */
    public String deleteProcess(String processInstanceID) throws OntologyErrorException
    {
        Validate.notNull(processInstanceID);
        editor.deleteInstance(processInstanceID);
        return AlgernonConstants.OK;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getFinishedProcesses(java.lang.String)
     */
    public Process[] getFinishedProcesses(final String userID) throws OntologyErrorException
    {
        Validate.notNull(userID);
        final RecordSet records = processEngine.getFinishedProcesses(userID);
        Process[] finishedProcesses = new Process[0];
        if (records.getNoOfRecords() > 0)
        {
            finishedProcesses = new Process[records.getNoOfRecords()];
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> process = records.getRecords()[i];

                String name = process.get(ProcessEngineConstants.Variables.Common.VAR_VARIABLE);
                String instanceID = process.get(ProcessEngineConstants.Variables.Common.ID);
                finishedProcesses[i] = DefaultDataObjectFactory.createProwimProcessInstance(ProcessEngineConstants.Variables.Common.EMPTY,
                                                                                            instanceID, name);
                finishedProcesses[i].setStarter(organizationEntity.getPerson(process.get(ProcessEngineConstants.Variables.Process.STARTER)));
                String startTimestamp = process.get(ProcessEngineConstants.Variables.Common.START);
                if (startTimestamp == null || startTimestamp.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    startTimestamp = ProcessEngineConstants.Variables.Common.ZERO;
                }
                String endTimestamp = process.get(ProcessEngineConstants.Variables.Common.END);
                if (endTimestamp == null || endTimestamp.equals(ProcessEngineConstants.Variables.Common.EMPTY))
                {
                    endTimestamp = ProcessEngineConstants.Variables.Common.ZERO;
                }
                finishedProcesses[i].setStartTime(startTimestamp);
                finishedProcesses[i].setEndTime(endTimestamp);
                finishedProcesses[i].setType(process.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE));
            }
        }
        return finishedProcesses;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getInformationTypes()
     */
    public InformationType[] getInformationTypes() throws OntologyErrorException
    {
        final RecordSet records = processEngine.getInformationTypes();
        final InformationType[] infoTypes = new InformationType[records.getNoOfRecords()];
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> infoTypesRecord = records.getRecords()[i];

                String id = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_NAME);
                String denotation = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.DENOTATION);
                String max = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.MAX_VALUE);
                String min = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.MIN_VALUE);

                String contentString = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.CONTENT_STRING);
                Long minValue = null;
                Long maxValue = null;
                if (id.equals(InformationTypesConstants.DATE))
                {
                    minValue = StringConverter.datestr2Long(min);
                    maxValue = StringConverter.datestr2Long(max);
                }
                else if (id.equals(InformationTypesConstants.TIMESTAMP))
                {
                    minValue = StringConverter.timestampstr2Long(min);
                    maxValue = StringConverter.timestampstr2Long(max);
                }
                else
                {
                    minValue = StringConverter.strToLong(min);
                    maxValue = StringConverter.strToLong(max);
                }
                infoTypes[i] = DefaultDataObjectFactory.createInformationType(id, denotation, minValue, maxValue, contentString);
            }
        }
        return infoTypes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessInformation(java.lang.String, java.lang.String)
     */
    public Parameter getProcessInformation(String processInformationID, String referenceID) throws OntologyErrorException
    {
        Validate.notNull(processInformationID);
        Validate.notNull(referenceID);
        ObjectArray selectedValues = new ObjectArray();
        ObjectArray possibleSelection = new ObjectArray();
        final RecordSet records = processEngine.getParameter(processInformationID);

        Parameter parameter = null;
        for (int i = 0; i < records.getNoOfRecords(); i++)
        {
            final Hashtable<String, String> parametersRecord = records.getRecords()[i];
            /** Now fetch data to create a Parameter object */
            final String referenceId = referenceID;
            final String name = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_NAME);
            final String parameterTemplateID = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_TEMPLATE_ID);
            final String informationTypeID = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_NAME);
            final String order = parametersRecord.get(ProcessEngineConstants.Variables.Common.ORDER);
            final String contentType = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_CONTENT_ID);

            // all selected values of the parameters
            selectedValues = getParameterValue(processInformationID, informationTypeID, contentType);

            /** Get the constraint object. */
            ParameterConstraint constraint = parameterHelper.getConstraint(processInformationID, informationTypeID);
            parameter = DefaultDataObjectFactory.createParameter(processInformationID, name, referenceId, informationTypeID, selectedValues,
                                                                 constraint);

            /** Now collect the possible selection list to each parameter. */
            possibleSelection = parameterHelper.getParameterSelectList(processInformationID, informationTypeID);

            /** Set the other parameter attributes. */
            parameter.setTemplateID(parameterTemplateID);
            parameter.setContentType(contentType);
            if (possibleSelection != null)
                parameter.setPossibleSelection(possibleSelection);
            else
                parameter.setPossibleSelection(new ObjectArray());

            parameter.setOrder(StringConverter.strToInteger(order));

        }
        if (records == null || ( !records.getResult().equals(AlgernonConstants.OK)))
            throw new IllegalArgumentException("processinformation does not exist: " + processInformationID);
        else
            return parameter;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessParameters(java.lang.String)
     */
    public ParameterArray getProcessParameters(String processInstanceID) throws OntologyErrorException
    {
        Validate.notNull(processInstanceID);

        ObjectArray selectedValues = new ObjectArray();
        ObjectArray possibleSelection = new ObjectArray();
        // a unique hash map to access only a parameter once a time
        final HashMap<String, Integer> parameterMap = new HashMap<String, Integer>();
        boolean uniqueness = true;
        final RecordSet records = processEngine.getProcessParameter(processInstanceID);
        final ParameterArray parameterList = new ParameterArray();

        for (int i = 0; i < records.getNoOfRecords(); i++)
        {
            Hashtable<String, String> parametersRecord = records.getRecords()[i];
            /** Now fetch data to create a Parameter object */
            String id = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_INSTANCE_ID);
            String name = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_NAME);
            String processInstanceid = processInstanceID;
            String parameterTemplateID = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_TEMPLATE_ID);
            String informationTypeID = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_NAME);
            String order = parametersRecord.get(ProcessEngineConstants.Variables.Common.ORDER);
            String contentType = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_CONTENT_ID);
            String editable = parametersRecord.get(ProcessEngineConstants.Variables.Common.EDITABLE);
            boolean editableBooleanValue = StringConverter.strToBool(editable);
            LOG.debug("EDITABLE    " + editable + "   bool   " + editableBooleanValue + "   id =  " + id + "   informationTypeID    "
                    + informationTypeID);
            if (editableBooleanValue)
            {
                // all selected values of the parameters
                selectedValues = getParameterValue(id, informationTypeID, contentType);

                /** Get the constraint object. */
                ParameterConstraint constraint = parameterHelper.getConstraint(id, informationTypeID);
                Parameter param = DefaultDataObjectFactory
                        .createParameter(id, name, processInstanceid, informationTypeID, selectedValues, constraint);

                /** Now collect the possible selection list to each parameter. */
                possibleSelection = parameterHelper.getParameterSelectList(id, informationTypeID);

                /** Set the other parameter attributes. */
                param.setTemplateID(parameterTemplateID);
                param.setContentType(contentType);
                param.setPossibleSelection(possibleSelection);
                param.setOrder(StringConverter.strToInteger(order));
                param.setEditable(editableBooleanValue);

                if ( !parameterMap.containsKey(id))
                {
                    parameterMap.put(id, param.getOrder());
                    if (param.isEditable())
                    {
                        parameterList.add(param);
                    }
                }
            }
        }
        return resortAlphabetically(parameterList, uniqueness);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessTemplateParameters(java.lang.String)
     */
    public ParameterArray getProcessTemplateParameters(String processTemplateID) throws OntologyErrorException
    {
        Validate.notNull(processTemplateID);
        RecordSet records = processEngine.getProcessTemplateParameter(processTemplateID);
        ParameterArray parameterList = new ParameterArray();
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> parametersRecord = records.getRecords()[i];
                String name = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_NAME);
                String parameterTemplateID = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.PARAMETER_TEMPLATE_ID);
                String informationTypeID = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_NAME);
                ObjectArray values = parameterHelper.getParameterValue(parameterTemplateID);
                String processid = processTemplateID;
                String contentType = parametersRecord.get(ProcessEngineConstants.Variables.Common.InformationType.INFORMATION_TYPE_CONTENT_FIELD);
                String editable = parametersRecord.get(ProcessEngineConstants.Variables.Common.EDITABLE);
                boolean editableBooleanValue = StringConverter.strToBool(editable);

                /** set the parameter constraint object */
                Parameter param = DefaultDataObjectFactory.createParameter(parameterTemplateID, name, processid, informationTypeID, values, null);
                param.setContentType(contentType);
                param.setTemplateID(parameterTemplateID);
                param.setEditable(editableBooleanValue);
                if (param.isEditable())
                {
                    parameterList.add(param);
                }
            }
        }

        return parameterList;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessBrowserChartPath(java.lang.String)
     */
    public String getProcessBrowserChartPath(String processID) throws OntologyErrorException
    {
        RecordSet pathRecords = processEngine.getProcessBrowserChartPath(processID);
        String path = ProcessEngineConstants.Variables.Common.NULL_VALUE;
        if (pathRecords.getNoOfRecords() == 1)
        {
            for (int i = 0; i < pathRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> pathRecord = pathRecords.getRecords()[i];
                path = pathRecord.get(ProcessEngineConstants.Variables.Common.PATH);
            }
        }
        return path;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#setProcessInformationValue(java.lang.String, java.lang.String, org.prowim.datamodel.collections.ObjectArray, java.lang.String)
     */
    public void setProcessInformationValue(String parameterID, String slotID, ObjectArray value, String infoType) throws OntologyErrorException
    {
        Validate.notNull(parameterID);
        Validate.notNull(slotID);
        Validate.notNull(value);
        String clearStatus = this.clearSlot(parameterID, slotID);
        LOG.debug("CLEAR_STATUS  " + clearStatus);
        ObjectArray theValues = value;
        Iterator<Object> it = theValues.iterator();
        while (it.hasNext())
        {
            Object avalue = it.next();
            String valueToset = null;
            if (avalue instanceof Person)
            {
                valueToset = ((Person) avalue).getID();
            }
            else if (avalue instanceof Organization)
            {
                valueToset = ((Organization) avalue).getID();
            }
            else if (avalue instanceof ControlFlow)
            {
                valueToset = ((ControlFlow) avalue).getID();
            }
            else if (infoType.equals(InformationTypesConstants.INTEGER))
            {
                if (avalue instanceof Integer)
                {
                    valueToset = ((Integer) avalue).toString();
                }
            }
            else if (infoType.equals(InformationTypesConstants.FLOAT))
            {
                if (avalue instanceof Float)
                {
                    valueToset = ((Float) avalue).toString();
                }
            }
            else if (avalue instanceof String)
            {
                if (infoType.equals(InformationTypesConstants.DATE))
                {
                    String longValueAsString = (String) avalue;
                    String dateValueAsString = StringConverter.longStringToDateString(longValueAsString);
                    valueToset = dateValueAsString;
                }
                else if (infoType.equals(InformationTypesConstants.TIMESTAMP))
                {
                    String longValueAsString = (String) avalue;
                    String timestampValueAsString = StringConverter.longStringToTimestampString(longValueAsString);
                    valueToset = timestampValueAsString;
                }
                else
                {
                    valueToset = (String) avalue;
                }
            }
            else
                throw new IllegalArgumentException("Unknown information type: " + infoType + " with the value " + avalue);

            processEngine.setSlotValue(parameterID, slotID, valueToset);
        }

    }

    private String clearSlot(String frameID, String slotID) throws OntologyErrorException
    {
        RecordSet clearsRecords = processEngine.clearSlot(frameID, slotID);
        return clearsRecords.getResult();
    }

    /**
     * Gets the parameter value.
     * 
     * @throws OntologyErrorException if an error in ontology back end occurs
     **/
    private ObjectArray getParameterValue(String parameterID, String informationTypeID, String contentType) throws OntologyErrorException
    {
        ObjectArray selectedValues = new ObjectArray();
        ObjectArray value = parameterHelper.getParameterValue(contentType, parameterID);
        if (LOG.isDebugEnabled())
            for (Object argument : value)
            {
                LOG.debug("invocation for argument: '" + argument + "'");
            }
        // Get all selected values of the parameters
        try
        {
            selectedValues = invoke(ProcessEngineConstants.Variables.Common.CONVERT + informationTypeID, value);
        }
        catch (ParameterInvocationException e)
        {
            LOG.error(e.getMessage());
            throw new IllegalStateException("Could not Invoke request InformationType: " + informationTypeID);
        }
        return selectedValues;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#clearSlotValue(java.lang.String, java.lang.String, java.lang.String)
     */
    public void clearSlotValue(String instanceID, String slotID, String oldValue) throws OntologyErrorException
    {
        Validate.notNull(instanceID);
        Validate.notNull(slotID);
        Validate.notNull(oldValue);
        processEngine.clearSlotValue(instanceID, slotID, oldValue);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getActivityInput(java.lang.String)
     */
    public ProductArray getActivityInput(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);

        return buildProducts(activityID, processEngine.getActivityInput(activityID));

    }

    /**
     * Build the {@link ProductArray} from the given {@link RecordSet}.
     * 
     * @param activityID not null
     * @param activityRecords may be null
     * @return never null
     * @throws OntologyErrorException if something bad happens
     */
    public ProductArray buildProducts(String activityID, final RecordSet activityRecords) throws OntologyErrorException
    {
        final ProductArray products = new ProductArray();
        if (activityRecords != null && activityRecords.getRecords() != null)
        {
            for (Hashtable<String, String> activityRecord : activityRecords.getRecords())
            {
                final String productID = activityRecord.get(ProcessEngineConstants.Variables.Common.ID);
                final String name = activityRecord.get(ProcessEngineConstants.Variables.Common.NAME);
                final String createTime = activityRecord.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);

                Product product = DefaultDataObjectFactory.createProduct(productID, name, createTime, activityID);
                final ParameterArray parameters = new ParameterArray();

                final ObjectArray parametersIDs = getProductProcessInformation(productID);
                final Iterator<Object> itParameterIDs = parametersIDs.iterator();
                while (itParameterIDs.hasNext())
                {
                    ProcessInformation processInformation = (ProcessInformation) itParameterIDs.next();
                    final String parameterID = processInformation.getID();
                    final Parameter param = getProcessInformation(parameterID, productID);
                    parameters.add(param);
                }
                product.setParameters(parameters);
                products.add(product);
            }
        }
        return products;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getActivityOutput(java.lang.String)
     */
    public ProductArray getActivityOutput(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);

        return buildProducts(activityID, processEngine.getActivityOutput(activityID));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProductProcessInformation(java.lang.String)
     */
    public ObjectArray getProductProcessInformation(String referenceID) throws OntologyErrorException
    {
        Validate.notNull(referenceID);
        final RecordSet processInformationRecords = processEngine.getProductProcessInformations(referenceID);
        final ObjectArray processInformations = new ObjectArray();
        if (processInformationRecords.getRecords() != null)
        {
            for (Hashtable<String, String> processInformationRecord : processInformationRecords.getRecords())
            {
                final String processInformationID = processInformationRecord
                        .get(ProcessEngineConstants.Variables.Common.InformationType.PROCESSINFORMATION_ID);

                ProcessInformation processInformation = processInformationEntity.getProcessInformation(processInformationID);
                DenoationID infoTypeID = this.getInformationTypeID(processInformationID);
                if (infoTypeID != null)
                {
                    InformationType infoType = this.getInformationType(infoTypeID.id);
                    infoType.setDenotation(infoTypeID.denotation);
                    processInformation.setInfoType(infoType);
                    processInformations.add(processInformation);
                }
            }
        }
        return processInformations;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getInformationTypeID(java.lang.String)
     */
    public DenoationID getInformationTypeID(String referenceID) throws OntologyErrorException
    {
        Validate.notNull(referenceID);
        final RecordSet informationTypeRecords = processEngine.getInformationTypeID(referenceID);
        DenoationID informationTypeDenID = null;
        if (informationTypeRecords.getNoOfRecords() > 0)
        {
            final Hashtable<String, String> informationtTypeRecord = informationTypeRecords.getRecords()[0];
            String informationTypeID = informationtTypeRecord.get(ProcessEngineConstants.Variables.Common.ID);
            String informationTypeDen = informationtTypeRecord.get(ProcessEngineConstants.Variables.Common.InformationType.DENOTATION);
            informationTypeDenID = new DenoationID(informationTypeID, informationTypeDen);
        }
        return informationTypeDenID;
    }

    /**
     * Stores the id and the denoation of an InformationType object.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 5100 $
     * @since !!VERSION!!
     */
    public final class DenoationID
    {
        private final String id;
        private final String denotation;

        /**
         * Contructs this.
         * 
         * @param id the InformationType ID.
         * @param denotation the denotation.
         */
        private DenoationID(String id, String denotation)
        {
            this.id = id;
            this.denotation = denotation;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getInformationType(java.lang.String)
     */
    public InformationType getInformationType(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        final RecordSet records = processEngine.getInformationType(id);
        InformationType infoType = null;
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> infoTypesRecord = records.getRecords()[i];

                // String denotation = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.DENOTATION);
                String max = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.MAX_VALUE);
                String min = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.MIN_VALUE);

                String contentString = infoTypesRecord.get(ProcessEngineConstants.Variables.Common.InformationType.CONTENT_STRING);
                Long minValue = new Long(0);
                Long maxValue = new Long(0);
                if (min != null && max != null)
                    if ( !min.equals("") && !max.equals(""))
                        if (id.equals(InformationTypesConstants.DATE))
                        {
                            minValue = StringConverter.datestr2Long(min);
                            maxValue = StringConverter.datestr2Long(max);
                        }
                        else if (id.equals(InformationTypesConstants.TIMESTAMP))
                        {
                            minValue = StringConverter.timestampstr2Long(min);
                            maxValue = StringConverter.timestampstr2Long(max);
                        }
                        else
                        {
                            minValue = StringConverter.strToLong(min);
                            maxValue = StringConverter.strToLong(max);
                        }

                infoType = DefaultDataObjectFactory.createInformationType(id, "", minValue, maxValue, contentString);
            }
            return infoType;
        }
        else
        {
            throw new IllegalArgumentException("InformationType with id = " + id + " was not found!");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#createKnowledgeLink(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public KnowledgeLink createKnowledgeLink(String knowledgeObjectID, String name, String wsID, String link) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObjectID);
        Validate.notNull(name);
        Validate.notNull(wsID);
        Validate.notNull(link);
        String knowledgeLinkID = null;
        RecordSet knowledgeObjectRecords = processEngine.genKnowledgeLink(knowledgeObjectID, name, wsID, link);
        if (knowledgeObjectRecords.getResult().equals(AlgernonConstants.OK) && knowledgeObjectRecords != null
                && knowledgeObjectRecords.getNoOfRecords() > 0)
        {
            knowledgeLinkID = knowledgeObjectRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGELINK_ID);
            return knowledgeEntity.getKnowledgeLink(knowledgeLinkID);
        }
        else
        {
            throw new IllegalStateException("Could not create knowledgelink from :  " + knowledgeObjectID + " , " + name + "  , " + wsID + " , "
                    + link);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#editKnowledgeObject(java.lang.String, java.lang.String, org.prowim.datamodel.collections.ObjectArray, org.prowim.datamodel.collections.PersonArray)
     */
    public void editKnowledgeObject(String knowledgeObjectID, String name, ObjectArray knowledgeLinks, PersonArray dominators)
                                                                                                                              throws OntologyErrorException
    {
        /** 1. First set the name. */
        Validate.notNull(knowledgeObjectID);
        Validate.notNull(name);
        Validate.notNull(knowledgeLinks);
        Validate.notNull(dominators);
        processEngine.clearSlotValue(knowledgeObjectID, Relation.Slots.NAME, commonHelper.getName(knowledgeObjectID));
        processEngine.setSlotValue(knowledgeObjectID, Relation.Slots.NAME, name);

        processEngine.clearSlot(knowledgeObjectID, Relation.Slots.DOMINATED_FROM);
        /** 2. Set the dominators persons. */
        if (dominators != null)
        {
            Iterator<Person> itPersons = dominators.iterator();
            while (itPersons.hasNext())
            {
                Person person = itPersons.next();
                processEngine.setSlotValue(knowledgeObjectID, Relation.Slots.DOMINATED_FROM, person.getID());
            }
        }

        /** 3. delete the unassigned links. */
        // get the knowledge links to the knowledgeObject and compare wich one was removed then delete it.
        List<String> toBedeletedLinksIDs = new ArrayList<String>();
        List<KnowledgeLink> links = knowledgeEntity.getLinks(knowledgeObjectID);
        Iterator<KnowledgeLink> itLinks = links.iterator();
        while (itLinks.hasNext())
        {
            KnowledgeLink link = itLinks.next();

            Iterator<Object> itKnowledgeLinks = knowledgeLinks.iterator();
            boolean exist = false;
            while (itKnowledgeLinks.hasNext())
            {
                KnowledgeLink clientLink = (KnowledgeLink) itKnowledgeLinks.next();
                if (link.getID().equals(clientLink.getID()))
                {
                    exist = true;
                }
            }
            if ( !exist)
            {
                toBedeletedLinksIDs.add(link.getID());
                commonHelper.deleteInstance(link.getID());
            }
        }

        /** 4. Set the KnowledgeLinks. */
        Iterator<Object> itKnowledgeLinks = knowledgeLinks.iterator();
        while (itKnowledgeLinks.hasNext())
        {
            KnowledgeLink knowledgeLink = (KnowledgeLink) itKnowledgeLinks.next();
            /** 1. Updates the KnowledgeLink. */
            LOG.debug("UPDATE_KnowledgeLink    " + knowledgeLink.getID() + "   " + knowledgeLink.getName() + "   " + knowledgeLink.getRepository()
                    + "   " + knowledgeLink.getHyperlink());

            this.editKnowledgeLink(knowledgeLink.getID(), knowledgeLink.getName(), knowledgeLink.getRepository(), knowledgeLink.getHyperlink());

            /** 2. Assigne the KnowledgeLink to the updated KnowledgeObject. */
            processEngine.setSlotValue(knowledgeObjectID, Relation.Slots.REFERS_TO, knowledgeLink.getID());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#updateKnowledgeObject(org.prowim.datamodel.prowim.KnowledgeObject)
     */
    public void updateKnowledgeObject(KnowledgeObject knowledgeObject) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObject);
        /** 1. First set the name. */
        processEngine.clearSlotValue(knowledgeObject.getID(), Relation.Slots.NAME, commonHelper.getName(knowledgeObject.getID()));
        processEngine.setSlotValue(knowledgeObject.getID(), Relation.Slots.NAME, knowledgeObject.getName());

        /** 2. Set the description. */
        processEngine.clearSlot(knowledgeObject.getID(), Relation.Slots.DESCRIPTION);
        String description = "";
        if (knowledgeObject.getDescription() != null)
            description = knowledgeObject.getDescription();
        processEngine.setSlotValue(knowledgeObject.getID(), Relation.Slots.DESCRIPTION, description);

        LOG.debug("DOMINATORS SIZE " + knowledgeObject.getResponsiblePersons().size());
        for (int i = 0; i < knowledgeObject.getResponsiblePersons().size(); i++)
        {
            LOG.debug("DOMINATOR    " + knowledgeObject.getResponsiblePersons().getItem().get(i).getFirstName());
        }

        processEngine.clearSlot(knowledgeObject.getID(), Relation.Slots.DOMINATED_FROM);
        /** 2. Set the dominators persons. */
        PersonArray dominators = knowledgeObject.getResponsiblePersons();
        Iterator<Person> itPersons = dominators.iterator();
        while (itPersons.hasNext())
        {
            Person person = itPersons.next();
            processEngine.setSlotValue(knowledgeObject.getID(), Relation.Slots.DOMINATED_FROM, person.getID());
        }

        /** 3. delete the unassigned links. */
        // get the knowledge links to the knowledgeObject and compare wich one was removed then delete it.
        List<String> toBedeletedLinksIDs = new ArrayList<String>();
        List<KnowledgeLink> links = knowledgeEntity.getLinks(knowledgeObject.getID());
        Iterator<KnowledgeLink> itLinks = links.iterator();
        while (itLinks.hasNext())
        {
            KnowledgeLink link = itLinks.next();

            List<KnowledgeLink> knowledgeLinks = knowledgeObject.getKnowledgeLinks();
            Iterator<KnowledgeLink> itKnowledgeLinks = knowledgeLinks.iterator();
            boolean exist = false;
            while (itKnowledgeLinks.hasNext())
            {
                KnowledgeLink clientLink = itKnowledgeLinks.next();
                if (link.getID().equals(clientLink.getID()))
                {
                    exist = true;
                }
            }
            if ( !exist)
            {
                toBedeletedLinksIDs.add(link.getID());
                commonHelper.deleteInstance(link.getID());
            }
        }

        /** 4. Set the KnowledgeLinks. */
        List<KnowledgeLink> knowledgeLinks = knowledgeObject.getKnowledgeLinks();
        LOG.debug("INCOMING LINKS " + knowledgeLinks.size());
        Iterator<KnowledgeLink> itKnowledgeLinks = knowledgeLinks.iterator();
        while (itKnowledgeLinks.hasNext())
        {
            KnowledgeLink knowledgeLink = itKnowledgeLinks.next();
            LOG.debug("INCOMING REPOSITORY ID " + knowledgeLink.getRepository());
            /** 1. Updates the KnowledgeLink. */
            this.editKnowledgeLink(knowledgeLink.getID(), knowledgeLink.getName(), knowledgeLink.getRepository(), knowledgeLink.getHyperlink());
            /** 2. Assigne the KnowledgeLink to the updated KnowledgeObject. */
            processEngine.setSlotValue(knowledgeObject.getID(), Relation.Slots.REFERS_TO, knowledgeLink.getID());
        }

        /** 5. Set the key words. */
        processEngine.clearSlot(knowledgeObject.getID(), EngineConstants.Slots.KEY_WORDS);
        StringArray keyWords = knowledgeObject.getKeyWords();
        for (String keyWord : keyWords)
        {
            processEngine.setSlotValue(knowledgeObject.getID(), EngineConstants.Slots.KEY_WORDS, keyWord);
        }
    }

    /**
     * Changes the name, repository and the hyperlink to a KnowledgeLink.
     * 
     * @param knowledgeLinkID the ID of the KnowledgeLink.
     * @param name the name of the KnowledgeLink.
     * @param ws the repository of the KnowledgeLink.
     * @param hyperLink the hyperlink of the KnowledgeLink.
     * @throws OntologyErrorException if an error occurs in ontologe back end
     */
    private void editKnowledgeLink(String knowledgeLinkID, String name, String ws, String hyperLink) throws OntologyErrorException
    {
        Validate.notNull(knowledgeLinkID);
        Validate.notNull(name);
        Validate.isTrue(ws.startsWith(ProcessEngineConstants.Variables.Common.REPOSITORY_ID_PATTERN));
        Validate.notNull(hyperLink);

        processEngine.clearSlot(knowledgeLinkID, Relation.Slots.NAME);
        processEngine.setSlotValue(knowledgeLinkID, Relation.Slots.NAME, name);
        processEngine.clearSlot(knowledgeLinkID, Relation.Slots.REFER_OF);
        processEngine.setSlotValue(knowledgeLinkID, Relation.Slots.REFER_OF, ws);
        processEngine.clearSlot(knowledgeLinkID, Relation.Slots.HYPERLINK);
        processEngine.setSlotValue(knowledgeLinkID, Relation.Slots.HYPERLINK, hyperLink);
        setInformationTypeForLink(knowledgeLinkID, ws);
    }

    /**
     * Sets the informationtype to document if the ID of the repository (wsID) fromtype "ProWIm DMS".
     * 
     * @param knowledgeLinkID the ID of the KnowledgeLink.
     * @param wsID the repository ID.
     * @throws OntologyErrorException if an error occurs in ontology backend
     */
    private void setInformationTypeForLink(String knowledgeLinkID, String wsID) throws OntologyErrorException
    {
        String repositoryName = commonHelper.getName(wsID);
        LOG.debug("REPOSITORY NAME " + repositoryName);
        LOG.debug("REPOSITORY ID " + wsID);
        if (repositoryName != null)
        {

            if (repositoryName.equalsIgnoreCase(Relation.Instances.Repository.DMS))
            {
                processEngine.setSlotValue(knowledgeLinkID, Relation.Slots.INFORMATION_TYPE, InformationTypesConstants.DOCUMENT);
            }
        }
        else
        {
            LOG.debug("REPOSITORY NAME " + repositoryName);
            LOG.debug("REPOSITORY ID " + wsID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#assigneRole(java.lang.String, java.lang.String)
     */
    public void assigneRole(String roleID, String personID) throws OntologyErrorException
    {
        Validate.notNull(roleID);
        Validate.notNull(personID);
        processEngine.setSlotValue(roleID, Relation.Slots.ASSIGNED_TO, personID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#setProcessStarter(java.lang.String, org.prowim.datamodel.collections.PersonArray)
     */
    public void setProcessStarter(String roleID, PersonArray processStarter) throws OntologyErrorException
    {
        Validate.notNull(roleID);
        Validate.notNull(processStarter);
        processEngine.clearSlot(roleID, Relation.Slots.ASSIGNED_TO);
        Iterator<Person> it = processStarter.iterator();
        while (it.hasNext())
        {
            Person person = it.next();
            assigneRole(roleID, person.getID());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessStarter(java.lang.String)
     */
    public PersonArray getProcessStarter(String roleID) throws OntologyErrorException
    {
        Validate.notNull(roleID);
        PersonArray personArray = new PersonArray();
        RecordSet records = processEngine.getAssignedPersonsToRole(roleID);
        if (records != null && records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.PERSON_ID);
                personArray.add(organizationEntity.getPerson(id));
            }
        }

        return personArray;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#setParameterValue(org.prowim.datamodel.prowim.Parameter)
     */
    public void setParameterValue(Parameter param) throws OntologyErrorException
    {
        Validate.notNull(param);
        Parameter parameter = param;
        ObjectArray values = parameter.getSelectedValues();
        String contentType = parameter.getContentType();
        String infoType = parameter.getInfoTypeID();
        String paramID = parameter.getID();
        setProcessInformationValue(paramID, contentType, values, infoType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#saveActivityOutputProducts(org.prowim.datamodel.collections.ProductArray)
     */
    public void saveActivityOutputProducts(ProductArray outputProducts) throws OntologyErrorException
    {
        Validate.notNull(outputProducts);
        ProductArray products = outputProducts;
        Iterator<Product> itProducts = products.iterator();
        while (itProducts.hasNext())
        {
            Product product = itProducts.next();
            ParameterArray parameters = product.getParameters();
            Iterator<Parameter> itParameters = parameters.iterator();
            while (itParameters.hasNext())
            {
                Parameter parameter = itParameters.next();
                this.setParameterValue(parameter);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#searchKeyWord(java.lang.String)
     */
    public ObjectArray searchKeyWord(String pattern) throws OntologyErrorException
    {
        Validate.notNull(pattern);
        /** Search in the name of the KnowledgeLinks. */
        RecordSet searchRecords = processEngine.searchLinks(pattern);
        ObjectArray result = new ObjectArray();
        List<String> knowledgeObjects = new ArrayList<String>();

        if (searchRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < searchRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> searchRecord = searchRecords.getRecords()[i];
                final String knowledgeLinkID = searchRecord.get(ProcessEngineConstants.Variables.Common.ID);
                KnowledgeLink linkHit = knowledgeEntity.getKnowledgeLink(knowledgeLinkID);
                if (linkHit != null)
                    result.add(linkHit);

            }
        }

        /** Search in the name of the KnowledgeObjects. */
        searchRecords = processEngine.searchKnowledgeObjects(pattern);
        if (searchRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < searchRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> searchRecord = searchRecords.getRecords()[i];
                final String knowledgeObjectID = searchRecord.get(ProcessEngineConstants.Variables.Common.ID);
                KnowledgeObject knowledgeObjectHit = knowledgeEntity.getKnowledgeObject(knowledgeObjectID);
                if (knowledgeObjectHit != null)
                {
                    result.add(knowledgeObjectHit);
                    knowledgeObjects.add(knowledgeObjectID);
                }
            }
        }

        /** Search in the name of the KnowledgeObjects. */
        searchRecords = processEngine.searchKnowledgeObjectsKeyWords(pattern);
        if (searchRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < searchRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> searchRecord = searchRecords.getRecords()[i];
                final String knowledgeObjectID = searchRecord.get(EngineConstants.Variables.KnowledgeObject.KNOWLEDGE_OBJECT_ID);
                KnowledgeObject knowledgeObjectHit = knowledgeEntity.getKnowledgeObject(knowledgeObjectID);
                if ((knowledgeObjectHit != null) && ( !knowledgeObjects.contains(knowledgeObjectID)))
                {
                    result.add(knowledgeObjectHit);
                }
            }
        }

        /** Search in the name of the processes. */
        final RecordSet processes = processEngine.searchProcess(pattern);
        if (processes.getNoOfRecords() > 0)
        {
            for (int i = 0; i < processes.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeDomain = processes.getRecords()[i];
                final String processID = knowledgeDomain.get(EngineConstants.Variables.Process.PROCESS_ID);
                Process process = getProcess(processID);
                // if domain could be found
                if (process != null)
                    result.add(process);
            }
        }

        /** Search in the name of the KnowledgeDomains. */
        final RecordSet knowledgeDomiains = processEngine.searchKnowledgeDomains(pattern);
        if (knowledgeDomiains.getNoOfRecords() > 0)
        {
            for (int i = 0; i < knowledgeDomiains.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeDomain = knowledgeDomiains.getRecords()[i];
                final String knowledgeDomainID = knowledgeDomain.get(ProcessEngineConstants.Variables.Common.ID);
                KnowledgeDomain knowledgeDomainHit = knowledgeEntity.getKnowledgeDomain(knowledgeDomainID);
                // if domain could be found
                if (knowledgeDomainHit != null)
                    result.add(knowledgeDomainHit);
            }
        }

        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#searchLinks(java.lang.String)
     */
    public KnowledgeObjectArray searchLinks(String pattern) throws OntologyErrorException
    {
        Validate.notNull(pattern);
        /** Search in the name of the KnowledgeLinks. */
        RecordSet searchRecords = processEngine.searchLinks(pattern);
        KnowledgeObjectArray result = new KnowledgeObjectArray();

        // a unique hash map to access only a knowledgeobject once a time
        final HashMap<String, ObjectArray> knowledgeObjectsMap = new HashMap<String, ObjectArray>();
        if (searchRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < searchRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> searchRecord = searchRecords.getRecords()[i];
                final String knowledgeObjectID = searchRecord.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN);
                final String knowledgeLinkID = searchRecord.get(ProcessEngineConstants.Variables.Common.ID);
                if ( !knowledgeObjectsMap.containsKey(knowledgeObjectID))
                {
                    ObjectArray linksIds = new ObjectArray();
                    linksIds.add(knowledgeLinkID);
                    knowledgeObjectsMap.put(knowledgeObjectID, linksIds);
                }
                else
                {
                    ObjectArray linksIds = knowledgeObjectsMap.get(knowledgeObjectID);
                    linksIds.add(knowledgeLinkID);
                    knowledgeObjectsMap.put(knowledgeObjectID, linksIds);
                }
            }

            /** Search in the name of the KnowledgeObjects. */
            RecordSet knowledgeObjectsRecords = processEngine.searchKnowledgeObjects(pattern);
            for (int i = 0; i < knowledgeObjectsRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> searchRecord = knowledgeObjectsRecords.getRecords()[i];
                final String knowledgeObjectID = searchRecord.get(ProcessEngineConstants.Variables.Common.ID);
                LOG.debug("ID :   " + knowledgeObjectID);
                RecordSet knowledgeLinksRecords = processEngine.getKnowledgeLinkForKnowledgeObject(knowledgeObjectID);

                for (int j = 0; j < knowledgeLinksRecords.getNoOfRecords(); j++)
                {
                    final Hashtable<String, String> linksRecord = knowledgeLinksRecords.getRecords()[j];
                    final String knowledgeLinkID = linksRecord.get(ProcessEngineConstants.Variables.Common.ID);
                    LOG.debug("LINKS ID :   " + knowledgeObjectID);
                    if ( !knowledgeObjectsMap.containsKey(knowledgeObjectID))
                    {
                        ObjectArray linksIds = new ObjectArray();
                        linksIds.add(knowledgeLinkID);
                        knowledgeObjectsMap.put(knowledgeObjectID, linksIds);
                    }
                    else
                    {
                        ObjectArray linksIds = knowledgeObjectsMap.get(knowledgeObjectID);
                        linksIds.add(knowledgeLinkID);
                        knowledgeObjectsMap.put(knowledgeObjectID, linksIds);
                    }
                }
            }

            /** Now create the KnowledgeObjects. */
            Set<String> knowledgeObjectIDs = knowledgeObjectsMap.keySet();
            Iterator<String> itKnowledgeObjects = knowledgeObjectIDs.iterator();
            while (itKnowledgeObjects.hasNext())
            {
                String knowledgeObjectID = itKnowledgeObjects.next();
                KnowledgeObject knowledgeObject = knowledgeEntity.getKnowledgeObject(knowledgeObjectID);
                ObjectArray linksIDs = knowledgeObjectsMap.get(knowledgeObjectID);
                Iterator<Object> itLinksIDs = linksIDs.iterator();
                List<KnowledgeLink> knowledgeLinks = new ArrayList<KnowledgeLink>();
                while (itLinksIDs.hasNext())
                {
                    String linksID = (String) itLinksIDs.next();
                    KnowledgeLink link = knowledgeEntity.getKnowledgeLink(linksID);
                    knowledgeLinks.add(link);
                }
                knowledgeObject.setKnowledgeLinks(knowledgeLinks);
                result.add(knowledgeObject);
            }
        }
        return result;
    }

    /**
     * Sets all fields order uniquely in ascending order. The default order is alphabetically by parameter name.
     * 
     * @param parameterList to resort
     * @param uniqueness if true, the list will be sorted by the given order, otherwise it will be sorted as default
     * @return non null sorted list with new order fields
     */
    private ParameterArray resortAlphabetically(final ParameterArray parameterList, boolean uniqueness)
    {
        // sort array depending order
        ParameterArray result = parameterList;
        Collections.sort(result.getItem(), new ParameterComparator(false));
        return result;
    }

    @SuppressWarnings("unused")
    private void logResult(String rulename, RecordSet result)
    {
        if (result != null && result.getResult().equals(AlgernonConstants.ERROR))
        {
            LOG.info(rulename + " failed:" + result.getResultString());
        }
        if (result == null)
        {
            LOG.info(rulename + " failed:" + "result is null");
        }
    }

    /**
     * 
     * The method invoks methods 'convert'+informationtypeID that are implemented in {@link ParameterHelperBean}.<br>
     * valide Informationtypes see {@link InformationTypesConstants}.
     * 
     * 
     * @see ParameterHelperBean#convertShortText(ObjectArray)
     * 
     * @param methodName the methodname
     * @param values the parameter to invoke the method
     * @return not null {@link ObjectArray}. If not values exists an empty list is returned.
     * @throws ParameterInvocationException
     */
    private ObjectArray invoke(String methodName, ObjectArray values) throws ParameterInvocationException
    {
        Validate.notNull(values);
        LOG.debug("invoking method: " + methodName);
        ObjectArray result = new ObjectArray();
        Class<ParameterHelper> clazz = ParameterHelper.class;
        Class<? >[] parameterTypes = new Class<? >[] { ObjectArray.class };
        Method convertMethod;
        Object[] arguments = new Object[] { values };
        try
        {
            convertMethod = clazz.getMethod(methodName, parameterTypes);
        }
        catch (SecurityException e)
        {
            LOG.error("Cannot found method: " + methodName, e);
            throw new ParameterInvocationException("SecurityException:  " + methodName, e);
        }
        catch (NoSuchMethodException e)
        {
            LOG.error("Cannot found method: " + methodName, e);
            throw new ParameterInvocationException("NoSuchMethodException: " + methodName, e);
        }
        try
        {
            result = (ObjectArray) convertMethod.invoke(parameterHelper, arguments);
        }
        catch (IllegalArgumentException e)
        {
            LOG.error("Cannot invoke method: " + methodName, e);
            throw new ParameterInvocationException("IllegalArgumentException:  " + methodName, e);
        }
        catch (IllegalAccessException e)
        {
            LOG.error("Cannot invoke method: " + methodName, e);
            throw new ParameterInvocationException("IllegalAccessException:  " + methodName, e);
        }
        catch (InvocationTargetException e)
        {
            LOG.error("Cannot invoke method: " + methodName, e);
            throw new ParameterInvocationException("IllegalAccessException:  " + methodName, e);
        }
        return result;
    }

    /**
     * Title and description.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 5100 $
     */
    private class ParameterInvocationException extends Exception
    {
        /**
         * Description.
         */
        private static final long serialVersionUID = 1L;

        protected ParameterInvocationException(String message, Throwable throwable)
        {
            super(message, throwable);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#generateProcessInformation(java.lang.String)
     */
    public String generateProcessInformation(String frameID) throws OntologyErrorException
    {
        Validate.notNull(frameID);
        String processInformationID = this.getChartProcessInformationID(frameID);
        if (processInformationID != null)
        {
            LOG.debug(processInformationID);
            return processInformationID;
        }
        else
        {
            String name = commonHelper.getName(frameID) + ProcessEngineConstants.Variables.Common.PROCESSINFORMATION_CHART;
            final RecordSet processInformationRecords = processEngine.genProcessInformation(name, frameID);
            processInformationID = null;
            if (processInformationRecords != null && processInformationRecords.getNoOfRecords() > 0)
            {
                for (int i = 0; i < processInformationRecords.getNoOfRecords(); i++)
                {
                    final Hashtable<String, String> processInformationRecord = processInformationRecords.getRecords()[i];
                    processInformationID = processInformationRecord
                            .get(ProcessEngineConstants.Variables.Common.InformationType.PROCESSINFORMATION_ID);
                    LOG.debug(processInformationID);
                }
            }
            if (processInformationID != null)
            {
                return processInformationID;
            }
            else
            {
                throw new IllegalStateException("Could not generate the PROCESSINFORMATION for the frame id :  " + frameID);
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getChartProcessInformationID(java.lang.String)
     */
    public String getChartProcessInformationID(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        final RecordSet processInformationRecords = processEngine.getNotEditableProcessInformations(processID);
        String processInformationID = null;
        /** There is a Processinformation defined for this process. So fetch the its ID. and return it */
        if (processInformationRecords.getResult().equals(AlgernonConstants.OK))
        {
            if (processInformationRecords.getNoOfRecords() == 1)
            {
                for (int i = 0; i < processInformationRecords.getNoOfRecords(); i++)
                {
                    final Hashtable<String, String> processInformationRecord = processInformationRecords.getRecords()[i];
                    processInformationID = processInformationRecord
                            .get(ProcessEngineConstants.Variables.Common.InformationType.PROCESSINFORMATION_ID);
                }
            }
            return processInformationID;
        }
        else if (processInformationRecords.getResult().equals(AlgernonConstants.FAILED))
            return null;
        else if (processInformationRecords.getResult().equals(AlgernonConstants.ERROR))
            throw new IllegalArgumentException("Rule faild, because processID does not exist: " + processID + " or processInformation is missing");
        else
            throw new IllegalStateException("Unknonw algernon result: " + processInformationRecords.getResult());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getModels()
     */
    public Process[] getModels() throws OntologyErrorException
    {
        return getProcessModels();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#setInformationType(java.lang.String, java.lang.String)
     */
    public void setInformationType(String referenceID, String informationTypeID) throws OntologyErrorException
    {
        this.processEngine.clearSlot(referenceID, Relation.Slots.INFORMATION_TYPE);
        this.processEngine.setSlotValue(referenceID, Relation.Slots.INFORMATION_TYPE, informationTypeID);

    }

    /**
     * {@link EditorRemote#getProcessModels}.
     * 
     * @return the array that contains the items: modelID + EditorEngineConstants.Consts.ID_NAME_SPLIT_TOKEN + name.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private Process[] getProcessModels() throws OntologyErrorException
    {
        final RecordSet result = processEngine.getModels();
        Process[] processes = new Process[0];
        if (result.getNoOfRecords() > 0)
        {
            processes = new Process[result.getNoOfRecords()];
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];

                String name = record.get(EditorEngineConstants.Variables.Common.NAME);
                String templateID = record.get(EditorEngineConstants.Variables.Common.ID);
                String processtype = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE);
                Process process = DefaultDataObjectFactory.createProwimProcessTemplate(templateID, name);
                process.setAlfrescoVersion(getAlfrescoVersion(templateID));
                process.setUserDefinedVersion(getUserDefinedVersion(templateID));
                process.setProcessLandscape(isProcessLandscape(templateID));
                processes[i] = process;
                if (processtype != null)
                {
                    processes[i].setType(processtype);
                }
                else
                {
                    processes[i].setType(ProcessEngineConstants.Variables.Process.CORE_PROCESS);
                }
            }
            Arrays.sort(processes);
        }
        return processes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#isSubProcess(java.lang.String)
     */
    public boolean isSubProcess(String processID) throws OntologyErrorException
    {
        final RecordSet result = processEngine.isSubProcess(processID);
        if (result.getResult().equals(AlgernonConstants.OK))
        {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getGlobalRoles()
     */
    public RoleArray getGlobalRoles() throws OntologyErrorException
    {
        final RoleArray result = new RoleArray();
        final RecordSet records = processEngine.getGlobalRoles();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String roleID = records.getRecords()[i].get(EngineConstants.Variables.Role.ROLE_ID);
                Role role = roleEntity.getRole(roleID);
                if (role != null)
                    result.add(role);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getSubProcesses(java.lang.String)
     */
    public ProcessArray getSubProcesses(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        final ProcessArray result = new ProcessArray();
        final RecordSet records = processEngine.getSubprocesses(processID);
        if (records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];

                String name = record.get(ProcessEngineConstants.Variables.Process.PROCESS_IDENT);
                String templateID = record.get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
                String processtype = record.get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE);
                LOG.debug("THE PROCESS TYPE :  " + processtype);
                Process process = DefaultDataObjectFactory.createProwimProcessTemplate(templateID, name);
                process.setProcessLandscape(isProcessLandscape(templateID));
                if (processtype != null)
                {
                    process.setType(processtype);
                }
                else
                {
                    process.setType(ProcessEngineConstants.Variables.Process.CORE_PROCESS);
                }
                result.add(process);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getGlobalMeans()
     */
    public ObjectArray getGlobalMeans() throws OntologyErrorException
    {
        final ObjectArray result = new ObjectArray();
        final RecordSet records = processEngine.getGlobalMeans();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String meanID = records.getRecords()[i].get(EngineConstants.Variables.Mean.MEAN_ID);
                Mean mean = meanEntity.getMean(meanID);
                if (mean != null)
                    result.add(mean);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getGlobalResultsMem()
     */
    public ObjectArray getGlobalResultsMem() throws OntologyErrorException
    {
        final ObjectArray result = new ObjectArray();
        final RecordSet records = processEngine.getGlobalResultsMem();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String memoryID = records.getRecords()[i].get(EngineConstants.Variables.ResultsMemory.MEMORY_ID);
                ResultsMemory mean = resultsMemoryEntity.getResultsMemory(memoryID);
                if (mean != null)
                    result.add(mean);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getSubProcessTypes(java.lang.String)
     */
    public ProcessTypeArray getSubProcessTypes(String processTypeID) throws OntologyErrorException
    {
        Validate.notNull(processTypeID);

        final RecordSet records = processEngine.getSubProcessTypes(processTypeID);
        final ProcessTypeArray result = new ProcessTypeArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_ID);
                String name = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_NAME);
                String description = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE_DESC);
                LOG.debug("ProcessType with ID: " + processTypeID + " has subcategory of " + name);
                result.add(DefaultDataObjectFactory.createProcessType(id, name, description));
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessesOfType(java.lang.String)
     */
    public ProcessArray getProcessesOfType(String processTypeID) throws OntologyErrorException
    {
        final RecordSet records = processEngine.getTypeProcesses(processTypeID);
        final ProcessArray result = new ProcessArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {

                String name = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_IDENT);
                String templateID = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
                String processtype = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE);
                LOG.debug("THE PROCESS TYPE :  " + processtype);
                Process process = DefaultDataObjectFactory.createProwimProcessTemplate(templateID, name);
                process.setProcessLandscape(isProcessLandscape(templateID));
                if (processtype != null)
                {
                    process.setType(processtype);
                }
                else
                {
                    process.setType(ProcessEngineConstants.Variables.Process.CORE_PROCESS);
                }
                result.add(process);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getEnabledProcesses(java.lang.String)
     */
    @Override
    public ProcessArray getEnabledProcesses(String processTypeID) throws OntologyErrorException
    {
        final RecordSet records = processEngine.getEnabledProcessesOfType(processTypeID);
        final ProcessArray result = new ProcessArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {

                String name = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_IDENT);
                String templateID = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_ID);
                String processtype = records.getRecords()[i].get(ProcessEngineConstants.Variables.Process.PROCESS_TYPE);
                LOG.debug("THE PROCESS TYPE :  " + processtype);
                Process process = DefaultDataObjectFactory.createProwimProcessTemplate(templateID, name);
                process.setType(processtype);
                process.setProcessLandscape(isProcessLandscape(templateID));
                result.add(process);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getStartingRole(java.lang.String)
     */
    @Override
    public String getStartingRole(String processID) throws OntologyErrorException
    {
        final RecordSet records = processEngine.getStartRole(processID);
        if (records.getNoOfRecords() == 1)
        {
            return records.getSingleResult(EngineConstants.Variables.Role.ROLE_ID);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#isProcess(java.lang.String)
     */
    @Override
    public boolean isProcess(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        ProcessType category = getProcessType(id);
        return category != null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#createNewVersion(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String createNewVersion(String templateID, String versionName, String alfrescoVersion) throws OntologyErrorException
    {
        // get the user who can start a new process, if no user exists, do not enter one
        PersonArray starterPersons = getProcessStartersForProcess(templateID);

        String processInstanceID = null;

        String userID = null;
        if ( !starterPersons.isEmpty())
        {
            userID = starterPersons.getItem().get(0).getID();
        }

        // 1. clone the process template from the template
        RecordSet recordSet = workflowEngine.cloneWorkflow(templateID, userID);
        String result = recordSet.getResult();
        // TODO hier sollte der Wert grosser als 1 geprüft werden.
        if ( !result.equals(AlgernonConstants.ERROR) && recordSet.getNoOfRecords() == 1)
        {
            Hashtable<String, String> processInfo = recordSet.getRecords()[0];

            // 2. get the initial template ID of the process before clearing it
            String initialTemplateID = processEngine.getInitialTemplateID(templateID);

            // 2. get the process id and clear the template relation by clearing the slot for the template ID
            // afterwards the process is no longer handled as an instance but as a template
            // Additionally the slot with the connected process instances is cleared because they belong to the old version of the process template
            processInstanceID = processInfo.get(org.prowim.services.coreengine.EngineConstants.Variables.Process.PROCESS_INST_ID);
            clearSlot(processInstanceID, Slots.FROM_TEMPLATE);
            clearSlot(processInstanceID, Slots.HAS_TEMPLATE);

            LOG.debug("Clearing template slot of elements");
            workflowEngine.clearTemplateSlots(processInstanceID);

            // 3. set the new version to the active version and the other / older version to inactive
            // one could probably create a rule for that which checks all template versions for the flag
            workflowEngine.setAsActiveVersion(processInstanceID);
            workflowEngine.setAsInactiveVersion(templateID);

            // 4. Set the alfresco version and the version entered by the user to the "snapshot" version and the default EDITABLE labels to the new version
            processEngine.clearSlot(templateID, Slots.ALFRESCO_VERSION);
            processEngine.clearSlot(templateID, Slots.USER_DEFINED_VERSION);
            processEngine.setSlotValue(templateID, Slots.ALFRESCO_VERSION, alfrescoVersion);
            processEngine.setSlotValue(templateID, Slots.USER_DEFINED_VERSION, versionName);

            processEngine.clearSlot(processInstanceID, Slots.ALFRESCO_VERSION);
            processEngine.clearSlot(processInstanceID, Slots.USER_DEFINED_VERSION);
            processEngine.setSlotValue(processInstanceID, Slots.ALFRESCO_VERSION, EngineConstants.Consts.EDITABLE_ALFRESCO_VERSION_LABEL);
            processEngine.setSlotValue(processInstanceID, Slots.USER_DEFINED_VERSION, EngineConstants.Consts.EDITABLE_USER_VERSION_LABEL);

            // 5. Check, if model was open and close the original, open the new version
            if (editor.isProcessApproved(templateID))
            {
                editor.disapproveProcess(templateID);
                editor.approveProcess(processInstanceID);
            }

            // 6. set the initial version instance
            if (initialTemplateID != null)
            {
                processEngine.clearSlot(processInstanceID, Slots.IS_VERSION_OF);
                processEngine.setSlotValue(processInstanceID, Slots.IS_VERSION_OF, initialTemplateID);
            }
            else
            {
                throw new IllegalStateException("Could not retrieve initial template ID for template " + templateID);
            }

        }
        else
        {
            throw new IllegalStateException("Could not create new workflow / process instance for template " + templateID);
        }

        return processInstanceID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#activateVersion(String, String, boolean)
     */
    @Override
    public String activateVersion(String templateID, String versionName, boolean makeEditable) throws OntologyErrorException, DMSException
    {
        Validate.notNull(templateID);
        Validate.notNull(versionName);

        LOG.debug("Activating version " + versionName + " for template ID " + templateID);

        // 1. get the initial template version
        String initialTemplateID = processEngine.getInitialTemplateID(templateID);
        LOG.debug("Initial template ID: " + initialTemplateID);
        if (initialTemplateID != null)
        {
            // 2. get all process versions to iterate them and activate the one with the given version name
            ProcessArray processes = getProcessVersions(initialTemplateID);

            // 3. set all processes (including the initialTemplateID to inactive version and activate the given template id
            RecordSet setResult = null;
            String processIDToActivate = null;
            for (Iterator iterator = processes.iterator(); iterator.hasNext();)
            {
                Process process = (Process) iterator.next();
                LOG.debug("Checking process with ID " + process.getTemplateID());
                // activate if versionName is initial version name or version name equals version name of process
                if (process.getUserDefinedVersion() != null && process.getUserDefinedVersion().equals(versionName))
                {
                    LOG.debug("ACTIVATING! process.getUserDefinedVersion(): " + process.getUserDefinedVersion());

                    // get document id for process model
                    String processInformationID = getChartProcessInformationID(process.getTemplateID());
                    if (processInformationID == null)
                    {
                        throw new IllegalStateException("Could not find process information for process with ID: " + process.getTemplateID());
                    }

                    processIDToActivate = process.getTemplateID();
                    setResult = workflowEngine.setAsActiveVersion(processIDToActivate);
                    editor.approveProcess(processIDToActivate);
                    if (setResult.getResult() == AlgernonConstants.ERROR)
                    {
                        throw new IllegalStateException("Could not set the process template with id " + process.getTemplateID()
                                + " for initial template with ID " + initialTemplateID + " to active version.");
                    }
                }
                else
                {
                    LOG.debug("INACTIVATING! process.getUserDefinedVersion(): " + process.getUserDefinedVersion());
                    setResult = workflowEngine.setAsInactiveVersion(process.getTemplateID());
                    editor.disapproveProcess(process.getTemplateID());
                    if (setResult.getResult() == AlgernonConstants.ERROR)
                    {
                        throw new IllegalStateException("Could not set the process template with id " + process.getTemplateID()
                                + " for initial template with ID " + initialTemplateID + " to non active version.");
                    }
                }

            }

            // replace the document in the DMS
            if (makeEditable && processIDToActivate != null)
            {
                // delete the current editable version
                Process processToDelete = processes.getProcessForAlfrescoVersion(EngineConstants.Consts.EDITABLE_ALFRESCO_VERSION_LABEL);
                processEngine.deleteProcessInstances(processToDelete.getTemplateID());
                processEngine.deleteProcess(processToDelete.getTemplateID()).getResult();

                // create a new version in the ontology which results in
                String newProcessVersionID = createNewVersion(processIDToActivate, getUserDefinedVersion(processIDToActivate),
                                                              getAlfrescoVersion(processIDToActivate));

                // get the old xml
                String xmltoActivate = editor.loadModelAsXML(processIDToActivate);

                // map the xml elements to the newly created process elements
                String newXmlString = mapXMLElements(newProcessVersionID, xmltoActivate);

                // save the mapped xml
                editor.saveModelAsXML(newProcessVersionID, newXmlString, sessionContext.getCallerPrincipal().getName(), false, null);

                workflowEngine.setAsActiveVersion(processIDToActivate);
                workflowEngine.setAsInactiveVersion(newProcessVersionID);
                editor.approveProcess(processIDToActivate);
                editor.disapproveProcess(newProcessVersionID);
            }

            return AlgernonConstants.OK;
        }
        else
        {
            throw new IllegalStateException("Could not retrieve initial template ID for template " + templateID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessVersions(java.lang.String)
     */
    @Override
    public ProcessArray getProcessVersions(String instanceID) throws OntologyErrorException
    {
        Validate.notNull(instanceID);

        ProcessArray returnValue = new ProcessArray();

        String initialTemplateID = processEngine.getInitialTemplateID(instanceID);

        RecordSet recordSet = processEngine.getTemplateVersions(initialTemplateID);
        if (recordSet.getResult().equals(AlgernonConstants.ERROR))
        {
            throw new IllegalStateException("Could not retrieve template versions!");
        }

        for (int i = 0; i < recordSet.getNoOfRecords(); i++)
        {
            Hashtable<String, String> record = recordSet.getRecords()[i];

            String name = record.get(EditorEngineConstants.Variables.Common.NAME);
            String templateID = record.get(EditorEngineConstants.Variables.Common.ID);
            Process processToAdd = DefaultDataObjectFactory.createProwimProcessTemplate(templateID, name);
            processToAdd.setUserDefinedVersion(getUserDefinedVersion(templateID));
            processToAdd.setAlfrescoVersion(getAlfrescoVersion(templateID));

            processToAdd.setIsActiveVersion(false);
            String isActiveVersion = commonHelper.getSlotValue(templateID, EngineConstants.Slots.IS_ACTIVE_VERSION);
            if (isActiveVersion.equals(EngineConstants.Consts.TRUE))
            {
                processToAdd.setIsActiveVersion(true);
            }
            returnValue.add(processToAdd);
        }

        // retrieve the initial template to add it to the result
        String initialTemplateName = commonHelper.getName(initialTemplateID);
        Process initialProcess = DefaultDataObjectFactory.createProwimProcessTemplate(initialTemplateID, initialTemplateName);

        // if we have no versions at all, set the alfresco version to EDITABLE_ALFRESCO_VERSION_LABEL, otherwise use the alfresco version stored (usually "1.1")
        // additionally set the user defined version to EDITABLE_USER_VERSION_LABEL or to the correct slot
        if (returnValue.size() == 0)
        {
            initialProcess.setAlfrescoVersion(EngineConstants.Consts.EDITABLE_ALFRESCO_VERSION_LABEL);
            initialProcess.setUserDefinedVersion(EngineConstants.Consts.EDITABLE_USER_VERSION_LABEL);
        }
        else
        {
            initialProcess.setAlfrescoVersion(getAlfrescoVersion(initialTemplateID));
            initialProcess.setUserDefinedVersion(getUserDefinedVersion(initialTemplateID));
        }
        initialProcess.setIsActiveVersion(false);
        String isActiveVersion = commonHelper.getSlotValue(initialTemplateID, EngineConstants.Slots.IS_ACTIVE_VERSION);

        // this could be an error in the ontology, we fix it then...
        if (isActiveVersion == null)
        {
            LOG.warn("The process template with id " + initialTemplateID + " has an empty slot for the active version, trying to fix it.");
            // if there is only one version (the initial), set this version to active, otherwise there is a "real" error and we throw an exception
            if (returnValue.size() == 0)
            {
                LOG.warn("fixed active version slot for process template ID " + initialTemplateID);
                workflowEngine.setAsActiveVersion(initialTemplateID);
                isActiveVersion = EngineConstants.Consts.TRUE;
            }
            else
            {
                throw new IllegalStateException("Slot for active version not set for process template ID " + initialTemplateID
                        + ". Could not fix because more than one version found and so decision for active version could not be made.");
            }
        }

        if (isActiveVersion.equals(EngineConstants.Consts.TRUE))
        {
            initialProcess.setIsActiveVersion(true);
        }
        returnValue.add(initialProcess);

        return returnValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#isInitialTemplate(java.lang.String)
     */
    @Override
    public boolean isInitialTemplate(String processID) throws OntologyErrorException
    {
        String initialTemplateID = processEngine.getInitialTemplateID(processID);

        return initialTemplateID.equals(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getAlfrescoVersion(String)
     */
    @Override
    public String getAlfrescoVersion(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);

        String returnValue = commonHelper.getSlotValue(processID, EngineConstants.Slots.ALFRESCO_VERSION);
        if (returnValue == null && isInitialTemplate(processID))
        {
            return EngineConstants.Consts.INITIAL_ALFRESCO_VERSION;
        }

        return returnValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getUserDefinedVersion(String)
     */
    @Override
    public String getUserDefinedVersion(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);

        String returnValue = commonHelper.getSlotValue(processID, EngineConstants.Slots.USER_DEFINED_VERSION);
        if (returnValue == null && isInitialTemplate(processID))
        {
            return EngineConstants.Consts.EDITABLE_USER_VERSION_LABEL;
        }

        return returnValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getElementsOfProcess(java.lang.String)
     */
    @Override
    public ObjectArray getElementsOfProcess(String processID) throws OntologyErrorException
    {
        ObjectArray results = new ObjectArray();
        final RecordSet records = processEngine.getElementsOfProcess(processID);
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String elementID = records.getRecords()[i].get(EngineConstants.Variables.Common.ELEMENT_ID);
                String name = records.getRecords()[i].get(EngineConstants.Variables.Common.NAME);
                String description = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                String className = records.getRecords()[i].get(EngineConstants.Variables.Common.CLASS);
                String createTime = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);

                if (className.equalsIgnoreCase(Relation.Classes.ACTIVITY))
                {

                    String numberOfRoles = records.getRecords()[i]
                            .get(org.prowim.datamodel.algernon.ProcessEngineConstants.Variables.Activity.NUM_OF_RELATION_ROLES);
                    String numberOfKnowledgeObjects = records.getRecords()[i]
                            .get(org.prowim.datamodel.algernon.ProcessEngineConstants.Variables.Activity.NUM_OF_RELATION_KNOWLEDGES);
                    String numberOfMeans = records.getRecords()[i]
                            .get(org.prowim.datamodel.algernon.ProcessEngineConstants.Variables.Activity.NUM_OF_RELATION_MEANS);

                    Activity resultActivity = DefaultDataObjectFactory.createActivity(elementID, createTime, name);
                    resultActivity.setDescription(description);
                    resultActivity.setClassName(className);

                    if (Integer.valueOf(numberOfRoles) > 0 || Integer.valueOf(numberOfKnowledgeObjects) > 0 || Integer.valueOf(numberOfMeans) > 0)
                    {
                        resultActivity.setHasRelations(true);
                    }

                    results.add(resultActivity);

                }
                else
                {
                    ProcessElement processElement = DefaultDataObjectFactory.createProcessElement(elementID, name, createTime);
                    processElement.setDescription(description);
                    processElement.setClassName(className);
                    results.add(processElement);
                }

            }
        }

        return results;
    }

    private StringArray getElementIDsOfProcess(String processID) throws OntologyErrorException
    {
        StringArray results = new StringArray();
        final RecordSet records = processEngine.getElementsOfProcess(processID);
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String elementID = records.getRecords()[i].get(EngineConstants.Variables.Common.ELEMENT_ID);

                results.add(elementID);
            }
        }

        return results;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getParentProcess(java.lang.String)
     */
    @Override
    public StringArray getParentProcess(String entityID) throws OntologyErrorException
    {
        StringArray results = new StringArray();
        RecordSet records = securityEngine.getParentProcess(entityID);
        if (records != null && records.getNoOfRecords() > 0)
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = records.getRecords()[i];
                results.add(record.get(ProcessEngineConstants.Variables.Process.PROCESS_ID));
            }
        }

        return results;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#setSlotValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException
    {
        processEngine.setSlotValue(frameID, slotID, value);
    }

    /**
     * Returns a hash map containing the mappings for the process elements of a process template for two different versions.
     * 
     * The hash map contains the original version ID as key and the new element as value.
     * 
     * @param templateID the template ID of the newly created process template to get the mapping for
     * @return {@link HashMap}, never null
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     */
    public HashMap<String, String> getProcessElementVersionMapping(String templateID) throws OntologyErrorException
    {
        HashMap<String, String> returnValue = new HashMap<String, String>();

        StringArray currentElementIDs = getElementIDsOfProcess(templateID);

        for (String elementID : currentElementIDs)
        {
            returnValue.put(processEngine.getInitialVersionID(elementID), elementID);
        }

        return returnValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#removeActivatedStateFromXML(java.lang.String)
     */
    @Override
    public String removeActivatedStateFromXML(String xml)
    {
        Document doc = XMLParser.getInstance().parse(xml);
        NodeList rootNodes = doc.getElementsByTagName("root");
        for (int i = 0; i < rootNodes.getLength(); i++)
        {
            Node rootNode = rootNodes.item(i);
            NodeList elements = rootNode.getChildNodes();
            for (int elementIndex = 0; elementIndex < elements.getLength(); elementIndex++)
            {
                Node element = elements.item(elementIndex);
                NamedNodeMap attributes = element.getAttributes();

                if (attributes != null)
                {
                    String prowimType = null;

                    for (int attributeIndex = 0; attributeIndex < attributes.getLength(); attributeIndex++)
                    {
                        Node node = attributes.item(attributeIndex);
                        String attributeName = node.getNodeName();
                        String attributeValue = node.getNodeValue();

                        if (attributeName.equals("prowimtype"))
                        {
                            prowimType = attributeValue;
                        }
                    }

                    if (prowimType != null)
                    {
                        if (prowimType.equals(EngineConstants.Consts.XMLElementTypes.PROCESS))
                        {
                            NodeList processChildNodes = element.getChildNodes();
                            for (int processChildNodeIndex = 0; processChildNodeIndex < processChildNodes.getLength(); processChildNodeIndex++)
                            {
                                Node cellElement = processChildNodes.item(processChildNodeIndex);
                                NamedNodeMap processCellAttributes = cellElement.getAttributes();
                                if (processCellAttributes != null)
                                {
                                    for (int processCellAttributeIndex = 0; processCellAttributeIndex < processCellAttributes.getLength(); processCellAttributeIndex++)
                                    {
                                        Node node = processCellAttributes.item(processCellAttributeIndex);
                                        String attributeName = node.getNodeName();

                                        if (attributeName.equals("style"))
                                        {
                                            node.setNodeValue("rounded=0");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return getStringFromDoc(doc);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#mapXMLElements(java.lang.String, java.lang.String)
     */
    @Override
    public String mapXMLElements(String newProcessVersionID, String xml) throws OntologyErrorException
    {
        Document doc = XMLParser.getInstance().parse(xml);
        NodeList rootNodes = doc.getElementsByTagName("root");
        HashMap<String, String> elementVersionMapping = getProcessElementVersionMapping(newProcessVersionID);

        for (int i = 0; i < rootNodes.getLength(); i++)
        {
            Node rootNode = rootNodes.item(i);
            NodeList elements = rootNode.getChildNodes();
            for (int elementIndex = 0; elementIndex < elements.getLength(); elementIndex++)
            {
                Node element = elements.item(elementIndex);
                NamedNodeMap attributes = element.getAttributes();

                if (attributes != null)
                {
                    String prowimID = null;
                    String prowimType = null;
                    Node prowimIDNode = null;

                    for (int attributeIndex = 0; attributeIndex < attributes.getLength(); attributeIndex++)
                    {
                        Node node = attributes.item(attributeIndex);
                        String attributeName = node.getNodeName();
                        String attributeValue = node.getNodeValue();

                        if (attributeName.equals("prowimid"))
                        {
                            prowimIDNode = node;
                            prowimID = attributeValue;
                        }

                        if (attributeName.equals("prowimtype"))
                        {
                            prowimType = attributeValue;
                        }
                    }

                    if (prowimType != null && prowimID != null)
                    {
                        LOG.debug("Found : " + prowimID + " for element type " + prowimType + ". Mapping to current ID is : "
                                + elementVersionMapping.get(prowimID));

                        if (hasElementTypeToBeMapped(prowimType) && elementVersionMapping.get(prowimID) != null
                                && !elementVersionMapping.get(prowimID).equals(""))
                        {
                            LOG.debug("Has to be mapped!");
                            prowimIDNode.setNodeValue(elementVersionMapping.get(prowimID));
                        }
                        else if (prowimType.equals(EngineConstants.Consts.XMLElementTypes.PROCESS))
                        {
                            prowimIDNode.setNodeValue(newProcessVersionID);
                        }
                    }
                }
            }
        }

        return getStringFromDoc(doc);
    }

    /**
     * Returns the given {@link Document} as string.
     * 
     * @param doc the {@link Document} to transform to a string
     * @return the string value, never null
     */
    private String getStringFromDoc(Document doc)
    {
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult;
        try
        {
            StringWriter stringWriter = new StringWriter();
            streamResult = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

            return stringWriter.toString();
        }
        catch (TransformerConfigurationException e)
        {
            LOG.error("Could not configure transformer to write mapped xml");
            throw new IllegalStateException("Could not configure transformer to write mapped xml");
        }
        catch (TransformerException e)
        {
            LOG.error("Could not transform xml");
            throw new IllegalStateException("Could not transform xml");
        }
    }

    /**
     * Returns <code>true</code>, if the given element type has to be mapped to a new ID.
     * 
     * @param elementType the element type from the XML to check
     * @return <code>true</code>, if the element type has to be mapped to a new ID
     */
    private boolean hasElementTypeToBeMapped(String elementType)
    {
        Validate.notNull(elementType);

        List<String> typesNotToBeMapped = new ArrayList<String>();
        typesNotToBeMapped.add(EngineConstants.Consts.XMLElementTypes.PROCESS);
        typesNotToBeMapped.add(EngineConstants.Consts.XMLElementTypes.PROCESS_POINT);
        typesNotToBeMapped.add(EngineConstants.Consts.XMLElementTypes.GROUP);
        typesNotToBeMapped.add(EngineConstants.Consts.XMLElementTypes.DEPOT);
        typesNotToBeMapped.add(EngineConstants.Consts.XMLElementTypes.MEAN);
        typesNotToBeMapped.add(EngineConstants.Consts.XMLElementTypes.GROUP);

        return !typesNotToBeMapped.contains(elementType);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getSubProcessOfActivity(java.lang.String)
     */
    @Override
    public String getSubProcessOfActivity(String activityID) throws OntologyErrorException
    {
        final RecordSet records = processEngine.getSubProcessOfActivity(activityID);
        if (records.getRecords() != null)
            return records.getSingleResult(EngineConstants.Variables.Process.SUB_PROCESS_ID);
        else
            return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcessStartersForProcess(java.lang.String)
     */
    @Override
    public PersonArray getProcessStartersForProcess(String processID) throws OntologyErrorException
    {
        String startingRole = getStartingRole(processID);
        if (startingRole != null)
        {
            return getProcessStarter(startingRole);
        }

        return new PersonArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getSubprocessCaller(java.lang.String)
     */
    public StringArray getSubprocessCaller(String subprocessID) throws OntologyErrorException
    {
        StringArray result = new StringArray();
        RecordSet suprocessCaller = processEngine.getSubprocessCaller(subprocessID);
        if (suprocessCaller != null && suprocessCaller.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < suprocessCaller.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> processRecord = suprocessCaller.getRecords()[i];
                result.add(processRecord.get(EngineConstants.Variables.Activity.ACTIVITY_ID));
            }
        }
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @throws OntologyErrorException
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#addProcessInformationToProduct(java.lang.String)
     */
    @Override
    public void addProcessInformationToProduct(String productID) throws OntologyErrorException
    {
        StringArray parentProcess = getParentProcess(productID);
        if (parentProcess != null && !parentProcess.isEmpty())
        {
            String modelID = parentProcess.getItem().get(0);
            String processInformationID = editor.createObject(Relation.Classes.PROCESSINFORMATION, EngineConstants.Consts.PRODUCT_PARAMETER, false);
            processEngine.setSlotValue(productID, Relation.Slots.HAS_PROCESS_INFORMATION, processInformationID);
            editor.setRelationValue(processInformationID, Relation.Slots.INFORMATION_TYPE, Relation.Classes.SHORT_TEXT);
            editor.setElementToModel(modelID, processInformationID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#cloneProcess(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean cloneProcess(String templateID, String newProcessName, String newPrcessDescription) throws OntologyErrorException, DMSException
    {
        boolean returnFlag = false;
        // get the user who can start a new process, if no user exists, do not enter one
        PersonArray starterPersons = getProcessStartersForProcess(templateID);

        String processInstanceID = null;

        String userID = null;
        if ( !starterPersons.isEmpty())
        {
            userID = starterPersons.getItem().get(0).getID();
        }

        // 1. clone the process template from the template
        RecordSet recordSet = workflowEngine.cloneWorkflow(templateID, userID);
        String result = recordSet.getResult();
        if ( !result.equals(AlgernonConstants.ERROR) && recordSet.getNoOfRecords() == 1)
        {
            Hashtable<String, String> processInfo = recordSet.getRecords()[0];

            // 2. get the process id and clear the template relation by clearing the slot for the template ID
            // afterwards the process is no longer handled as an instance but as a template
            // Additionally the slot with the connected process instances is cleared because they belong to the old version of the process template
            processInstanceID = processInfo.get(org.prowim.services.coreengine.EngineConstants.Variables.Process.PROCESS_INST_ID);
            clearSlot(processInstanceID, Slots.FROM_TEMPLATE);
            clearSlot(processInstanceID, Slots.HAS_TEMPLATE);

            LOG.debug("Clearing template slot of elements");
            workflowEngine.clearTemplateSlots(processInstanceID);

            // 3. set the new version to the active version
            workflowEngine.setAsActiveVersion(processInstanceID);

            // 4. Set the alfresco version and the version entered by the user to the "snapshot" version and the default EDITABLE labels to the new version
            processEngine.clearSlot(processInstanceID, Slots.ALFRESCO_VERSION);
            processEngine.clearSlot(processInstanceID, Slots.USER_DEFINED_VERSION);
            processEngine.setSlotValue(processInstanceID, Slots.ALFRESCO_VERSION, EngineConstants.Consts.EDITABLE_ALFRESCO_VERSION_LABEL);
            processEngine.setSlotValue(processInstanceID, Slots.USER_DEFINED_VERSION, EngineConstants.Consts.EDITABLE_USER_VERSION_LABEL);

            // 5. Set the new name and new description of new process
            processEngine.clearSlot(processInstanceID, Slots.DENOTATION);
            processEngine.clearSlot(processInstanceID, Slots.DESCRIPTION);
            processEngine.setSlotValue(processInstanceID, Slots.DENOTATION, newProcessName);
            processEngine.setSlotValue(processInstanceID, Slots.DESCRIPTION, newPrcessDescription);

            // 6. Load old model xml and store it for the new process

            // get the old xml
            String xmltoActivate = editor.loadModelAsXML(templateID);

            // map the xml elements to the newly created process elements
            String newXmlString = mapXMLElements(processInstanceID, xmltoActivate);

            String processInformationID = getChartProcessInformationID(processInstanceID);

            editor.deleteInstance(processInformationID);
            // save the mapped xml
            editor.saveModelAsXML(processInstanceID, newXmlString, sessionContext.getCallerPrincipal().getName(), false, null);

            returnFlag = true;
        }
        else
        {
            throw new IllegalStateException("Could not clone process instance for template " + templateID);
        }

        return returnFlag;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#getProcess(java.lang.String)
     */
    @Override
    public Process getProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID, "ID of process can not be null.");
        RecordSet processSet = processEngine.getProcess(processID);
        Process process = null;
        if (processSet != null && processSet.getResult().equals(AlgernonConstants.OK))
        {
            process = DefaultDataObjectFactory.createProwimProcessTemplate(processID,
                                                                           processSet.getRecords()[0].get(EngineConstants.Variables.Common.NAME));
            process.setDescription(processSet.getRecords()[0].get(EngineConstants.Variables.Common.DESCRIPTION));
            process.setUserDefinedVersion(processSet.getRecords()[0].get(EngineConstants.Variables.Common.VERSION));
            process.setCreateTime(processSet.getRecords()[0].get(EngineConstants.Variables.Common.CREATE_TIME));
            process.setProcessType(getProcessType(processID));
            Activity[] activities = getActivities(processID);
            ActivityArray activityArray = new ActivityArray();
            activityArray.addArray(activities);
            process.setActivities(activityArray.getItem());
            List<Person> owners = DefaultSecurityManager.getInstance().getPersonCanModifyEntity(processID).getItem();
            process.setOwners(owners);
            process.setProcessLandscape(isProcessLandscape(processID));
        }
        return process;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.ProcessHelper#isProcessLandscape(java.lang.String)
     */
    @Override
    public boolean isProcessLandscape(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID, "ID of process can not be null.");
        final RecordSet result = processEngine.isProcessLandscape(processID);
        if (result.getResult().equals(AlgernonConstants.OK))
        {
            return true;
        }
        return false;
    }
}