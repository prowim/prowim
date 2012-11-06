/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-14 13:29:20 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/process/ProcessBean.java $
 * $LastChangedRevision: 5075 $
 *------------------------------------------------------------------------------
 * (c) 26.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.ejb.process;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.InformationTypeArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.InformationType;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.dms.alfresco.ContentService;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.aspects.security.interceptors.ChangeProcessInterceptor;
import org.prowim.services.coreengine.EditorHelper;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.impl.ParameterHelperBean;
import org.prowim.services.ejb.process.ProcessRemote;



/**
 * Implements the {@link ProcessRemote} methods.
 * 
 * @author Saad Wardi
 * @version $Revision: 5075 $
 */

@Stateless
@WebService(name = "ProcessService", serviceName = "ProcessService", endpointInterface = "org.prowim.services.ejb.process.ProcessRemote")
@WebContext(contextRoot = "/ProWimServices")
@XmlSeeAlso({ Organization.class, ParameterHelperBean.class, ControlFlow.class })
public class ProcessBean implements ProcessRemote
{
    @IgnoreDependency
    @EJB
    private ProcessHelper  processHelper;

    @IgnoreDependency
    @EJB
    private EditorHelper   editor;

    @IgnoreDependency
    @EJB
    private ContentService contentService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getExecutableProcesses(java.lang.String)
     */
    @Override
    public ProcessArray getExecutableProcesses(String userID) throws OntologyErrorException
    {
        ProcessArray procArray = new ProcessArray();
        Process[] processes = processHelper.getExecutableProcesses(userID);
        if (processes != null)
        {
            procArray.setItem(Arrays.asList(processes));
        }
        return procArray;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getRunningProcesses(String)
     */
    @Override
    public ProcessArray getRunningProcesses(String userID) throws OntologyErrorException
    {
        ProcessArray procArray = new ProcessArray();
        Process[] processes = processHelper.getRunningProcesses(userID);
        procArray.setItem(Arrays.asList(processes));

        return procArray;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#deleteProcess(java.lang.String)
     */
    @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public String deleteProcess(String processInstanceID) throws OntologyErrorException
    {
        return processHelper.deleteProcess(processInstanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getFinishedProcesses(String)
     */
    @Override
    public ProcessArray getFinishedProcesses(String userID) throws OntologyErrorException
    {
        ProcessArray procArray = new ProcessArray();
        Process[] processes = processHelper.getFinishedProcesses(userID);
        if (processes != null)
        {
            procArray.setItem(Arrays.asList(processes));
        }

        return procArray;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getInformationTypes()
     */
    @Override
    public InformationTypeArray getInformationTypes() throws OntologyErrorException
    {
        InformationTypeArray infoTypes = new InformationTypeArray();
        InformationType[] infotypesArray = processHelper.getInformationTypes();
        if (infotypesArray != null)
        {
            infoTypes.setItem(Arrays.asList(infotypesArray));
        }
        return infoTypes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessParameters(java.lang.String)
     */
    @Override
    public ParameterArray getProcessParameters(String processInstanceID) throws OntologyErrorException
    {
        return processHelper.getProcessParameters(processInstanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessTemplateParameters(java.lang.String)
     */
    @Override
    public ParameterArray getProcessTemplateParameters(String processTemplateID) throws OntologyErrorException
    {
        return processHelper.getProcessTemplateParameters(processTemplateID);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessBrowserChartPath(java.lang.String)
     */
    @Override
    public String getProcessBrowserChartPath(String processID) throws OntologyErrorException
    {
        return processHelper.getProcessBrowserChartPath(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getBrowserProcessInformationID(java.lang.String)
     */
    @Override
    public String getBrowserProcessInformationID(String frameID) throws OntologyErrorException
    {
        return processHelper.generateProcessInformation(frameID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessBrowserChartURL(java.lang.String)
     */
    @Override
    public String getProcessBrowserChartURL(String processID) throws OntologyErrorException, DMSException
    {
        /** the content service that operates on the alfresco DMS. */
        String processInformationID = processHelper.getChartProcessInformationID(processID);
        Document document = contentService.downloadDocument(processInformationID);
        return new RepositoryHelper(document).generateChartURL();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getModels()
     */
    @Override
    public ProcessArray getModels() throws OntologyErrorException
    {
        ProcessArray procArray = new ProcessArray();
        Process[] processes = processHelper.getModels();
        if (processes != null)
        {
            procArray.setItem(Arrays.asList(processes));
        }

        return procArray;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessType(java.lang.String)
     */
    @Override
    public ProcessType getProcessType(String processID) throws OntologyErrorException
    {
        return processHelper.getProcessType(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getAllProcessTypes()
     */
    @Override
    public ProcessTypeArray getAllProcessTypes() throws OntologyErrorException
    {
        ProcessTypeArray processTypeArray = new ProcessTypeArray();
        ProcessType[] procType = processHelper.getAllProcessTypes();
        if (procType != null)
            processTypeArray.setItem(Arrays.asList(procType));

        return processTypeArray;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getAllTopProcessTypes()
     */
    @Override
    public ProcessTypeArray getAllTopProcessTypes() throws OntologyErrorException
    {
        ProcessTypeArray processTypeArray = new ProcessTypeArray();
        ProcessType[] procType = processHelper.getAllTopProcessTypes();
        if (procType != null)
            processTypeArray.setItem(Arrays.asList(procType));

        return processTypeArray;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#createProcessType(java.lang.String, java.lang.String)
     */
    @Override
    public String createProcessType(String name, String description) throws OntologyErrorException
    {
        return processHelper.createProcessType(name, description);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#setProcessTypeParent(java.lang.String, java.lang.String)
     */
    @Override
    public void setProcessTypeParent(String typeID, String parentTypeID) throws OntologyErrorException
    {
        processHelper.setProcessTypeParent(typeID, parentTypeID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getSubProcessTypes(java.lang.String)
     */
    @Override
    public ProcessTypeArray getSubProcessTypes(String processTypeID) throws OntologyErrorException
    {
        return processHelper.getSubProcessTypes(processTypeID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getTypeProcesses(java.lang.String)
     */
    @Override
    public ProcessArray getTypeProcesses(String processTypeID) throws OntologyErrorException
    {
        Validate.notNull(processTypeID, "processTypeID is null");
        return processHelper.getProcessesOfType(processTypeID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getEnabledProcesses(java.lang.String)
     */
    @Override
    public ProcessArray getEnabledProcesses(String processTypeID) throws OntologyErrorException
    {
        Validate.notNull(processTypeID, "processTypeID is null");
        return processHelper.getEnabledProcesses(processTypeID);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#setProcessType(java.lang.String, java.lang.String)
     */
    @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public String setProcessType(String processTypeID, String processID) throws OntologyErrorException
    {
        return processHelper.setProcessType(processTypeID, processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProductsOfActivity(java.lang.String)
     */
    @Override
    public ObjectArray getProductsOfActivity(String referenceID) throws OntologyErrorException
    {
        return processHelper.getProductProcessInformation(referenceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#setInformationType(java.lang.String, java.lang.String)
     */
    @Override
    public void setInformationType(String referenceID, String informationTypeID) throws OntologyErrorException
    {
        processHelper.setInformationType(referenceID, informationTypeID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#setProcessStarter(java.lang.String, org.prowim.datamodel.collections.PersonArray)
     */
    @Interceptors(ChangeProcessInterceptor.class)
    @Override
    public void setProcessStarter(String roleID, PersonArray processStarter) throws OntologyErrorException
    {
        processHelper.setProcessStarter(roleID, processStarter);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessStarter(java.lang.String)
     */
    @Override
    public PersonArray getProcessStarter(String roleID) throws OntologyErrorException
    {
        return processHelper.getProcessStarter(roleID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getControlFlow()
     */
    @Override
    public ControlFlow getControlFlow()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#isSubProcess(String)
     */
    @Override
    public boolean isSubProcess(String processID) throws OntologyErrorException
    {
        return processHelper.isSubProcess(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#createRole(java.lang.String, java.lang.String)
     */
    @Override
    public String createRole(String processID, String activityID) throws OntologyErrorException
    {
        /**
         * 1. create the role and the Work.
         */
        String roleID = editor.createObject(processID, Relation.Classes.ROLE);
        String workID = editor.createObject(processID, Relation.Classes.WORK);
        /**
         * 2. Bind the role and the Activity.
         */

        editor.connectActivityRole(activityID, roleID, workID);

        return roleID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#createMean(java.lang.String, java.lang.String)
     */
    @Override
    public String createMean(String processID, String activityID) throws OntologyErrorException
    {
        /** 1. create the mean and the function. */
        String meanID = editor.createObject(processID, Relation.Classes.MEAN);
        String functionID = editor.createObject(processID, Relation.Classes.FUNCTION);

        /** 2. Bind the mean and the activity with the created function. */
        editor.connectActivityMittel(activityID, meanID, functionID);

        return meanID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getSubProcesses(java.lang.String)
     */
    @Override
    public ProcessArray getSubProcesses(String processID) throws OntologyErrorException
    {
        return processHelper.getSubProcesses(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#processInformation()
     */
    @Override
    public org.prowim.datamodel.prowim.ProcessInformation processInformation()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getGlobalRoles()
     */
    @Override
    public RoleArray getGlobalRoles() throws OntologyErrorException
    {
        return processHelper.getGlobalRoles();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#setElementToModel(java.lang.String, java.lang.String)
     */
    @Override
    public void setElementToModel(String modelID, String elementID) throws OntologyErrorException
    {
        Validate.notNull(modelID);
        Validate.notNull(elementID);

        editor.setElementToModel(modelID, elementID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getGlobalMeans()
     */
    @Override
    public ObjectArray getGlobalMeans() throws OntologyErrorException
    {
        return processHelper.getGlobalMeans();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getGlobalResultsMem()
     */
    @Override
    public ObjectArray getGlobalResultsMem() throws OntologyErrorException
    {
        return processHelper.getGlobalResultsMem();
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#resultsMemory()
     */
    @Override
    public org.prowim.datamodel.prowim.ResultsMemory resultsMemory()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#mean()
     */
    @Override
    public Mean mean()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#createProcessElement(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public String createProcessElement(String classname, String name, boolean isGlobal) throws OntologyErrorException
    {
        return editor.createObject(classname, name, isGlobal);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#createNewVersion(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String createNewVersion(String templateID, String versionName, String alfrescoVersion) throws OntologyErrorException
    {
        return processHelper.createNewVersion(templateID, versionName, alfrescoVersion);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#activateVersion(String, String, boolean)
     */
    @Override
    public String activateVersion(String instanceID, String versionName, boolean makeEditable) throws OntologyErrorException, DMSException
    {
        return processHelper.activateVersion(instanceID, versionName, makeEditable);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessVersions(java.lang.String)
     */
    @Override
    public ProcessArray getProcessVersions(String instanceID) throws OntologyErrorException
    {
        Validate.notNull(instanceID);
        return processHelper.getProcessVersions(instanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getElementsOfProcess(java.lang.String)
     */
    @Override
    public ObjectArray getElementsOfProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        return processHelper.getElementsOfProcess(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getParentProcess(java.lang.String)
     */
    @Override
    public StringArray getParentProcess(String entityID) throws OntologyErrorException
    {
        Validate.notNull(entityID);
        return processHelper.getParentProcess(entityID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getSubprocessReferences(java.lang.String)
     */
    @Override
    public ProcessArray getSubprocessReferences(String subprocessID) throws OntologyErrorException
    {
        final ProcessArray result = new ProcessArray();
        final HashMap<String, Process> processesMap = new HashMap<String, Process>();
        StringArray activities = processHelper.getSubprocessCaller(subprocessID);
        Iterator<String> activitiesIt = activities.iterator();
        while (activitiesIt.hasNext())
        {
            String activityID = activitiesIt.next();
            if (activityID != null)
            {
                final Process[] allprocesses = processHelper.getModels();
                if (allprocesses != null)
                {
                    for (int i = 0; i < allprocesses.length; i++)
                    {
                        processesMap.put(allprocesses[i].getTemplateID(), allprocesses[i]);
                    }
                }
                StringArray processes = processHelper.getParentProcess(activityID);
                Iterator<String> processesIDsIT = processes.iterator();
                while (processesIDsIT.hasNext())
                {
                    String processID = processesIDsIT.next();
                    if (processesMap.containsKey(processID))
                    {
                        Process process = processesMap.get(processID);
                        if (process != null)
                        {
                            result.add(process);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getAlfrescoVersion(java.lang.String)
     */
    @Override
    public String getAlfrescoVersion(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        return processHelper.getAlfrescoVersion(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getUserDefinedVersion(String)
     */
    @Override
    public String getUserDefinedVersion(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        return processHelper.getUserDefinedVersion(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getSubProcessOfActivity(java.lang.String)
     */
    @Override
    public String getSubProcessOfActivity(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        return processHelper.getSubProcessOfActivity(activityID);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcessStartersForProcess(java.lang.String)
     */
    @Override
    public PersonArray getProcessStartersForProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);

        return processHelper.getProcessStartersForProcess(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#addProcessInformationToProduct(java.lang.String)
     */
    @Override
    public void addProcessInformationToProduct(String productID) throws OntologyErrorException
    {
        Validate.notNull(productID);
        processHelper.addProcessInformationToProduct(productID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#cloneProcess(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean cloneProcess(String templateID, String newProcessName, String newPrcessDescription) throws OntologyErrorException, DMSException
    {
        Validate.notNull(templateID, "ID of given process is null.");
        Validate.notNull(newProcessName, "Name to set for new process is null.");
        Validate.notNull(newPrcessDescription, "Description to set for new process is null.");
        return processHelper.cloneProcess(templateID, newProcessName, newPrcessDescription);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#getProcess(java.lang.String)
     */
    @Override
    public Process getProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID, "ID of given process is null.");
        return processHelper.getProcess(processID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.process.ProcessRemote#isProcessLandscape(java.lang.String)
     */
    @Override
    public boolean isProcessLandscape(String processID) throws OntologyErrorException
    {
        return processHelper.isProcessLandscape(processID);
    }
}
