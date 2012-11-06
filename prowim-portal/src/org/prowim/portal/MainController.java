/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-10-18 18:27:22 +0200 (Di, 18 Okt 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/MainController.java $
 * $LastChangedRevision: 5101 $
 *------------------------------------------------------------------------------
 * (c) 08.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
package org.prowim.portal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.prowim.data.MediaWikiHTTPClient;
import org.prowim.data.RemoteClient;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants.Rules.Process.Subprocess;
import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.collections.DomainKnowledgeArray;
import org.prowim.datamodel.collections.InformationTypeArray;
import org.prowim.datamodel.collections.InstancePropertyArray;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.collections.MeanArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.collections.RightsRoleArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.datamodel.editor.InstancePropertiesNames;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.Function;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.datamodel.prowim.Product;
import org.prowim.datamodel.prowim.Relation.Slots;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.datamodel.prowim.Work;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.datamodel.wiki.WikiContentProperties;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.portal.dialogs.SupportMailDialog;
import org.prowim.portal.dialogs.feedback.ErrorDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.i18n.Resources.Frames.Global.Texts;
import org.prowim.portal.update.UpdateRegistry;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.browserfunctions.AbstractModelEditorFunction;
import org.prowim.services.ejb.activity.ActivityRemote;
import org.prowim.services.ejb.admin.AdminRemote;
import org.prowim.services.ejb.commons.CommonRemote;
import org.prowim.services.ejb.documents.DocumentRemote;
import org.prowim.services.ejb.editor.EditorRemote;
import org.prowim.services.ejb.knowledge.KnowledgeRemote;
import org.prowim.services.ejb.organization.OrganizationRemote;
import org.prowim.services.ejb.process.ProcessRemote;
import org.prowim.services.ejb.workflow.WorkflowRemote;



/**
 * This controller is necessary to manage actions, which are used outer of a class or frame. So can classes send it´s actions to this controller
 * 
 * and this will get it and after handle this, send it to a other class, which execute this action
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5101 $
 */
public final class MainController
{
    private final static Logger            LOG                   = Logger.getLogger(MainController.class);

    // instance of class. To create a single instance of this.
    private static volatile MainController mainController;

    private final ProcessRemote            processService;
    private final KnowledgeRemote          knowledgeService;
    private final ActivityRemote           activityService;
    private final DocumentRemote           documentService;
    private final OrganizationRemote       organizationService;
    private final CommonRemote             commonService;
    private final AdminRemote              adminService;
    private final WorkflowRemote           workflowService;
    private final EditorRemote             editorService;

    private final MediaWikiHTTPClient      wikiClient;
    private KnowledgeRepositoryArray       knowledgeRepositories = null;

    /**
     * Constructor
     */
    private MainController()
    {
        LOG.debug("Initialize the ProWim remote services ...");
        RemoteClient.getInstance().getAlgernonService().reloadKB();
        processService = RemoteClient.getInstance().getProcessService();
        knowledgeService = RemoteClient.getInstance().getKnowledgeService();
        activityService = RemoteClient.getInstance().getActivityService();
        documentService = RemoteClient.getInstance().getDocumentService();
        organizationService = RemoteClient.getInstance().getOrganizationService();
        commonService = RemoteClient.getInstance().getCommonService();
        adminService = RemoteClient.getInstance().getAdminService();
        workflowService = RemoteClient.getInstance().getWorkflowService();
        editorService = RemoteClient.getInstance().getEditorService();

        LOG.debug("ProWim remote services initialized with success.");
        wikiClient = new MediaWikiHTTPClient();
    }

    /**
     * 
     * Check, if the instance of this class exist. If yes, return this instance. else, return a new instance
     * 
     * @return MainController
     */
    public static MainController getInstance()
    {
        if (mainController == null)
        {
            synchronized (MainController.class)
            {
                if (mainController == null)
                {
                    mainController = new MainController();

                    return mainController;
                }
            }
        }
        return mainController;
    }

    /**
     * 
     * load a process XML model.
     * 
     * @param processId ID to given {@link Process}
     * @return the xml model
     */
    public String loadProcessAsXML(String processId)
    {
        Validate.notNull(processId);

        try
        {
            return editorService.loadModelAsXML(processId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Returns <code>true</code> if the given version is an editable version.
     * 
     * This only compares the given version string against {@link AlgernonConstants#EDITABLE_ALFRESCO_VERSION_LABEL} or empty string!
     * 
     * @param version the version string to check, may not be null
     * @return true, if version is editable version
     */
    public Boolean isEditableVersion(String version)
    {
        Validate.notNull(version);
        return version.equals(AlgernonConstants.EDITABLE_ALFRESCO_VERSION_LABEL) || version.equals("");
    }

    /**
     * Returns the alfresco version for the given process template.
     * 
     * @param processID the ID of the process template to return the alfresco version for
     * @return the alfresco version, can be null in case of an exception
     */
    public String getAlfrescoVersion(String processID)
    {
        try
        {
            return processService.getAlfrescoVersion(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * Returns the ID of the newest process model version for the given process ID.
     * 
     * @param processID the process ID to use, may not be null
     * @return the ID of the newest process model version, never null
     */
    public Process getNewestProcessModelVersionID(String processID)
    {
        Validate.notNull(processID);

        Process returnValue = null;

        try
        {
            ProcessArray processVersions = processService.getProcessVersions(processID);
            returnValue = processVersions.getProcessForAlfrescoVersion(AlgernonConstants.EDITABLE_ALFRESCO_VERSION_LABEL);
            if (returnValue == null)
            {
                handleException(new IllegalStateException("Could not find an editable version for process ID " + processID));
            }
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }

        return returnValue;
    }

    /**
     * Returns the ID of the newest process model version for the given process ID.
     * 
     * @param instanceID the instance ID of the version, may not be null
     * @param newName the new name of the version to set, may not be null
     */
    public void renameVersion(String instanceID, String newName)
    {
        Validate.notNull(instanceID);

        try
        {
            commonService.renameVersion(instanceID, newName);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
    }

    /**
     * Returns the user defined for the given process template.
     * 
     * @param processID the ID of the process template to return the alfresco version for
     * @return the alfresco version, can be null in case of an exception
     */
    public String getUserDefinedVersion(String processID)
    {
        try
        {
            return processService.getUserDefinedVersion(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * Save the updated XML model of a process
     * 
     * @param processId the ID of the process to store, may not be null
     * @param modelXML the XML string of the model which will be stored, may not be null
     * @param createNewVersion if <code>true</code>, a new version of the given process model will be created
     * @param versionName the name of the version, if createNewVersion is true, can be null, if createNewVersion is false
     * @return the ID of the newly created process model, if createNewVersion was true, otherwise null
     */
    public String saveProcessAsXML(String processId, String modelXML, boolean createNewVersion, String versionName)
    {
        Validate.notNull(processId);
        Validate.notNull(modelXML);

        try
        {
            String newProcessModelID = editorService.saveModelAsXML(processId, modelXML, createNewVersion, versionName);
            if (versionName != null)
                fireUpdateEvent(EntityType.PROCESS, processId);
            return newProcessModelID;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * Forward update information to the {@link UpdateRegistry}.
     * 
     * @param type The type of the updated entity
     * @param instanceId id(s) of the updated entities
     */
    private void fireUpdateEvent(final EntityType type, final String... instanceId)
    {
        fireUpdateEvent(EnumSet.of(type), instanceId);
    }

    /**
     * Forward update information to the {@link UpdateRegistry}.
     * 
     * @param type The type of the updated entity
     * @param instanceId id(s) of the updated entities
     */
    private void fireUpdateEvent(final EnumSet<EntityType> types, final String... instanceId)
    {
        UpdateRegistry.getInstance().addUpdate(types, instanceId);
    }

    /**
     * WorkflowException
     * 
     * Set a activity finished
     * 
     * @param activityID ID of given Activity
     * 
     * @return List#Parameter
     */
    public List<Parameter> getDecisionParameter(final String activityID)
    {
        Validate.notNull(activityID);
        try
        {
            return workflowService.getDecisionParameter(activityID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    // Services for process ************************************************************************************************************+
    /**
     * ProWimCoreException
     * 
     * This method get the id of one template and delete this. This delete all attributes and relations, which belong to one <br/>
     * process template.
     * 
     * @param processID Id of process template
     */
    public void deleteProcess(final String processID)
    {
        Validate.notNull(processID);

        try
        {
            processService.deleteProcess(processID);
            fireUpdateEvent(EntityType.PROCESS, processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * ProWimCoreException
     * 
     * This method return all information types, which exist in database (Ontology). <br/>
     * Return value is a array of this types. Here you can find informations e.g. minimal value, maximal value, requirement, <br/>
     * and other information how, where return values of information type should saved in ontology.
     * 
     * @return InformationTypeArray if return value exist else empty object of InformationTypeArray
     */
    public InformationTypeArray getInformationTypes()
    {
        try
        {
            return processService.getInformationTypes();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new InformationTypeArray();
    }

    /**
     * ProWimCoreException
     * 
     * Get url to a given process ID. URL is to a html file, which shows the process model.
     * 
     * @param processID ID of process
     * @return String
     */
    public String getProcessBrowserChartURL(final String processID)
    {
        Validate.notNull(processID);

        try
        {
            return this.processService.getProcessBrowserChartURL(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * ProWimCoreException Return the parameter to given process.
     * 
     * @param processInstanceID ID of process
     * @return list of parameter
     */
    public List<Parameter> getProcessParameters(final String processInstanceID)
    {
        Validate.notNull(processInstanceID);
        try
        {
            return this.processService.getProcessParameters(processInstanceID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * ProWimCoreException Get all models, which are actually in system.
     * 
     * @return ProcessArray
     */
    public ProcessArray getModels()
    {
        try
        {
            return this.processService.getModels();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * ProWimCoreException Get all models, which are actually in system.
     * 
     * @param processID ID of process.
     * 
     * @return ProcessTypeArray
     */
    public ProcessType getProcessType(String processID)
    {
        try
        {
            return this.processService.getProcessType(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * ProWimCoreException {@link ProcessRemote#getSubProcessTypes}.
     * 
     * @param processTypeID not null process type ID.
     * @return not null {@link ProcessTypeArray}. If no item exists, an empty list is returned.
     */
    public ProcessTypeArray getProcessCategories(String processTypeID)
    {
        try
        {
            return this.processService.getSubProcessTypes(processTypeID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * {@link ProcessRemote#getTypeProcesses}.
     * 
     * @param processTypeID not null process type ID.
     * @return not null {@link ProcessArray}. If no item exists, an empty list is returned.
     */
    public ProcessArray getTypeProcesses(String processTypeID)
    {
        try
        {
            return this.processService.getTypeProcesses(processTypeID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Get all enabled process for the given category.
     * 
     * @see ProcessRemote#getTypeProcesses
     * @param category not null process type ID.
     * @return not null {@link ProcessArray}. If no item exists, an empty list will returned.
     */
    public ProcessArray getEnabledProcesses(String category)
    {
        Validate.notNull(category, "Category can not be null");

        try
        {
            return this.processService.getEnabledProcesses(category);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ProcessArray();
    }

    /**
     * 
     * Set process type to given process .
     * 
     * @param processTypeID ID of process type
     * @param processID ID of process.
     * 
     */
    public void setProcessType(final String processTypeID, final String processID)
    {
        Validate.notNull(processTypeID);
        Validate.notNull(processID);

        try
        {
            this.processService.setProcessType(processTypeID, processID);
            fireUpdateEvent(EnumSet.of(EntityType.PROCESS), processID, processTypeID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by setProcessType: ", e);
            handlePermissionException(e);
        }
    }

    /**
     * 
     * Get all models, which are actually in system.
     * 
     * 
     * @return ProcessTypeArray
     */
    public ProcessTypeArray getAllProcessTypes()
    {
        try
        {
            return this.processService.getAllProcessTypes();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Get all models, which are actually in system.
     * 
     * 
     * @return ProcessTypeArray
     */
    public ProcessTypeArray getAllTopProcessTypes()
    {
        try
        {
            return this.processService.getAllTopProcessTypes();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Create a new process type.
     * 
     * @param name Name of process type. Not null.
     * @param description Description of process type. Not null.
     * 
     * @return ProcessTypeArray
     */
    public String createProcessType(final String name, final String description)
    {
        Validate.notNull(name);

        try
        {
            final String processId = this.processService.createProcessType(name, description);
            fireUpdateEvent(EnumSet.of(EntityType.PROCESS, EntityType.PROCESSCATEGORY), processId);
            return processId;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by createProcessType: ", e);
            handlePermissionException(e);
        }
        return null;
    }

    /**
     * Sets the parent type to a processtype.
     * 
     * @param typeID the not null ProcessType ID.
     * @param parentTypeID not null parent ProcessTypeID.
     */

    public void setProcessTypeParent(String typeID, String parentTypeID)
    {
        Validate.notNull(typeID);

        try
        {
            this.processService.setProcessTypeParent(typeID, parentTypeID);
            fireUpdateEvent(EnumSet.of(EntityType.PROCESS, EntityType.PROCESSCATEGORY), typeID, parentTypeID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by setProcessTypeparent: ", e);
            handlePermissionException(e);
        }
    }

    /**
     * Find the {@link Product}s of an {@link Activity}
     * 
     * @param referenceID Id of {@link Activity}
     * 
     * @return ObjectArray
     */
    public ObjectArray getProductsOfActivity(final String referenceID)
    {
        Validate.notNull(referenceID);

        try
        {
            return this.processService.getProductsOfActivity(referenceID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Set the information type for given referenceID.
     * 
     * @param referenceID Id of reference object
     * @param informationTypeID ID of information type
     */
    public void setInformationType(final String referenceID, final String informationTypeID)
    {
        Validate.notNull(referenceID);
        Validate.notNull(informationTypeID);

        try
        {
            this.processService.setInformationType(referenceID, informationTypeID);
            fireUpdateEvent(EntityType.PROCESS, referenceID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Get the person, which can start the process of given role.
     * 
     * ???
     * 
     * @param processID ID of the start role for given process
     * @return PersonArray
     * 
     */
    public PersonArray getProcessStartersForProcessID(final String processID)
    {
        Validate.notNull(processID);

        try
        {
            return this.processService.getProcessStartersForProcess(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);

        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Get the set of roles, which are define global and are not a instance of other roles (templates).
     * 
     * @return not null {@link RoleArray}. If no item exists, an empty list is returned.
     */
    public RoleArray getGlobalRoles()
    {
        try
        {
            return this.processService.getGlobalRoles();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;

    }

    /**
     * Get the set of means, which are define global and are not a instance of other means (templates).
     * 
     * @return not null {@link ObjectArray}. If no item exists, an empty list is returned.
     */
    public ObjectArray getGlobalMeans()
    {
        try
        {
            return this.processService.getGlobalMeans();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * Get the set of results memory, which are define global and are not a instance of other results memory (templates).
     * 
     * @return not null {@link ObjectArray}. If no item exists, an empty list is returned.
     */
    public ObjectArray getGlobalResultsMem()
    {
        try
        {
            return this.processService.getGlobalResultsMem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);

        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;

    }

    /**
     * Sets the element with id = elementID to the model with id = modelID.<br>
     * A modelID is for example: the process ID for process management
     * 
     * @param modelID not null ID. For example: a process ID for process management
     * @param elementID not null ID. For example: an activity ID.
     */
    public void setElementToModel(String modelID, String elementID)
    {
        Validate.notNull(modelID);
        Validate.notNull(elementID);

        try
        {
            this.processService.setElementToModel(modelID, elementID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

    }

    // ***********************************************************************************************************************************

    // Services for knowledge ************************************************************************************************************
    /**
     * 
     * This method return all domains and those knowledges
     * 
     * @return List of DomainKnowledge
     */
    public List<KnowledgeDomain> getTopDomainKnowledge()
    {
        try
        {
            return knowledgeService.getTopDomainKnowledge().getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<KnowledgeDomain>();
    }

    /**
     * 
     * This method return all domains and those knowledges
     * 
     * @param domainID ID of given domain
     * 
     * @return List of DomainKnowledge
     */
    public List<KnowledgeDomain> getSubDomainKnow(final String domainID)
    {
        Validate.notNull(domainID);

        try
        {
            return knowledgeService.getSubDomainKnowledge(domainID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<KnowledgeDomain>();
    }

    /**
     * 
     * return knowledge objects to a domain
     * 
     * @param domainID ID of domain
     * @return List of KnowledgeObject
     */
    public List<KnowledgeObject> getDomainKnowObj(final String domainID)
    {
        Validate.notNull(domainID);

        try
        {
            return knowledgeService.getDomainKnowledgeObjects(domainID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<KnowledgeObject>();
    }

    /**
     * 
     * Get WOBs of given object.
     * 
     * @param objectID ID of object
     * @return list of WOBs. Is not null.
     */
    public List<KnowledgeObject> getKnowledgeObjects(final String objectID)
    {
        Validate.notNull(objectID);
        try
        {
            long currentTimeMillis = System.currentTimeMillis();

            List<KnowledgeObject> item = this.knowledgeService.getKnowledgeObjects(objectID).getItem();
            LOG.info("'getKnowledgeObjects'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");
            return item;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<KnowledgeObject>();
    }

    /**
     * Gets the {@link KnowledgeObject} `s to given {@link Process}id. This included also the knowledge objects of {@link ProcessElement}s.
     * 
     * @param processID not null {@link Process} id.
     * @return {@link List} of {@link KnowledgeObject}. Not null.
     */
    public List<KnowledgeObject> getKnowledgeObjectsOfProcess(final String processID)
    {
        Validate.notNull(processID);
        try
        {
            return this.knowledgeService.getKnowledgeObjectsOfProcess(processID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<KnowledgeObject>();
    }

    /**
     * Gets the id of {@link Subprocess} to given {@link Activity}id.
     * 
     * @param activityID not null {@link Activity} id.
     * @return {@link String}. null, if no sub process exists. Else id of sub process.
     */
    public String getSubProcessOfActivity(final String activityID)
    {
        Validate.notNull(activityID);
        try
        {
            return this.processService.getSubProcessOfActivity(activityID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * 
     * Get all activities of given process.
     * 
     * @param processID ID of process
     * @return list of activity. Is not null.
     */
    public List<Activity> getActivities(final String processID)
    {
        Validate.notNull(processID);
        try
        {
            return this.knowledgeService.getActivities(processID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<Activity>();
    }

    /**
     * 
     * Get the {@link Process} for given process id.
     * 
     * @param processID id of process. Not null
     * @return {@link Process}. If for given id no {@link Process} exits, returns null.
     */
    public Process getProcess(final String processID)
    {
        Validate.notNull(processID, "id of process can not be null");

        try
        {
            return this.processService.getProcess(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Add a WOB to given object.
     * 
     * @param objectID ID of object, which became this WOB
     * @param name name of WOB
     * @return A knowledge object. Can be null if exception.
     */
    public KnowledgeObject createKnowObj(final String objectID, final String name)
    {
        Validate.notNull(objectID);
        Validate.notNull(name);
        try
        {
            final KnowledgeObject knowledgeObject = this.knowledgeService.createKnowledgeObject(objectID, name);
            fireUpdateEvent(EntityType.KNOWLEDGE, objectID);
            return knowledgeObject;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * 
     * Add a knowledge object.
     * 
     * @param name name of WOB
     * @return A knowledge object. Can be null if exception.
     */
    public KnowledgeObject createKnowObj(final String name)
    {
        Validate.notNull(name);
        try
        {
            final KnowledgeObject ko = this.knowledgeService.createKnowledgeObject(name);
            fireUpdateEvent(EntityType.KNOWLEDGE, ko.getID());
            return ko;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * Gets all knowledge repositories from the ontology.
     * 
     * @return KnowledgeRepositoryArray. Is null by exception.
     * @see <a href="http://mediawiki.ebcot.info/index.php/Wissenselemente">http://mediawiki.ebcot.info/index.php/Wissenselemente</a>
     */
    public KnowledgeRepositoryArray getKnowledgeRepositories()
    {
        if (knowledgeRepositories == null)
        {
            try
            {
                knowledgeRepositories = this.knowledgeService.getKnowledgeRepositories();
            }
            catch (OntologyErrorException e)
            {
                handleException(e);
            }
            catch (Exception e)
            {
                handleException(e);
            }

        }

        return knowledgeRepositories;
    }

    /**
     * 
     * Create a knowledge link for given knowledge object.
     * 
     * @param knowledgeObjectID the not null ID of the KnowledgeObject
     * @param name the not null name of the KnowledgeLink
     * @param repositoryID the not null ID of a repository (Wissensspeicher) KnowledgeRepository.
     * @param link the not null hyperlink.
     * 
     * @return KnowledgeRepositoryArray. Is null by exception.
     */
    public KnowledgeLink createKnowLink(final String knowledgeObjectID, final String name, final String repositoryID, final String link)
    {
        Validate.notNull(knowledgeObjectID);
        Validate.notNull(name);
        Validate.notNull(repositoryID);
        Validate.notNull(link);
        try
        {
            final KnowledgeLink knowledgeLink = this.knowledgeService.createKnowledgeLink(knowledgeObjectID, name, repositoryID, link);
            fireUpdateEvent(EntityType.KNOWLEDGE, knowledgeObjectID, repositoryID, knowledgeLink.getID());
            return knowledgeLink;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * 
     * This method updates the given knowledge object.
     * 
     * @param knowledgeObject selected knowledgeobject
     */
    public void saveKnowledgeObject(final KnowledgeObject knowledgeObject)
    {
        Validate.notNull(knowledgeObject);
        try
        {
            this.knowledgeService.saveKnowledgeObject(knowledgeObject);
            fireUpdateEvent(EnumSet.of(EntityType.KNOWLEDGE, EntityType.KNOWLEDGELINK, EntityType.KNOWLEDGEDOMAIN), knowledgeObject.getID());
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * This method returns to given process ID the knowledge arrays.
     * 
     * @param processID ID of process
     * @return DomainKnowledgeArray
     */
    public DomainKnowledgeArray getBusinessDomains(final String processID)
    {
        Validate.notNull(processID);
        try
        {
            return this.knowledgeService.getBusinessDomains(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * 
     * This method delete a knowledge object and all its included relations.
     * 
     * @param knowledgeObjID ID of knowledge object
     * 
     */
    public void deleteKnowObj(final String knowledgeObjID)
    {
        Validate.notNull(knowledgeObjID);
        try
        {
            this.knowledgeService.deleteKnowledgeObject(knowledgeObjID);
            fireUpdateEvent(EntityType.KNOWLEDGE, knowledgeObjID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * This method delete a knowledge object and all its included relations.
     * 
     * @param objectID ID of knowledge object
     * 
     */
    public void deleteObject(final String objectID)
    {
        Validate.notNull(objectID);
        try
        {
            this.knowledgeService.deleteObject(objectID);
            fireUpdateEvent(EnumSet.of(EntityType.PROCESS, EntityType.ACTIVITY, EntityType.MEAN, EntityType.ROLE, EntityType.RESULTSMEMORY,
                                       EntityType.KNOWLEDGE, EntityType.KNOWLEDGEDOMAIN, EntityType.KNOWLEDGELINK), objectID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by deleteObject: ", e);
            handlePermissionException(e);
        }
    }

    /**
     * 
     * Description.
     * 
     * @param knowledgeObjID ID of object
     * @return KnowledgeObject
     */
    public KnowledgeObject getKnowlegdeObj(final String knowledgeObjID)
    {
        Validate.notNull(knowledgeObjID);
        try
        {
            return this.knowledgeService.getKnowledgeObject(knowledgeObjID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Adds an existing KnowledgeObject to a processElement.
     * 
     * @param knowledgeObjectID ID of knowledge object
     * @param processElementID ID of element
     * 
     */
    public void addKnowledgeObject(final String knowledgeObjectID, final String processElementID)
    {
        Validate.notNull(knowledgeObjectID);
        Validate.notNull(processElementID);
        try
        {
            this.knowledgeService.addKnowledgeObject(knowledgeObjectID, processElementID);
            fireUpdateEvent(EnumSet.of(EntityType.KNOWLEDGEDOMAIN, EntityType.PROCESS), knowledgeObjectID, processElementID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * Removes an existing KnowledgeObject from a processElement.
     * 
     * @param knowledgeObjectID ID of knowledge object
     * @param processElementID ID of element
     * 
     */
    public void removeKnowledgeObject(final String knowledgeObjectID, final String processElementID)
    {
        Validate.notNull(knowledgeObjectID);
        Validate.notNull(processElementID);
        try
        {
            this.knowledgeService.removeKnowledgeObject(knowledgeObjectID, processElementID);
            fireUpdateEvent(EntityType.KNOWLEDGE, knowledgeObjectID, processElementID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * This method get a key word and returns a KnowledgeObjectArray, which included all knowledge objects, which have this word in their name
     * 
     * @param keyWord That is the search world for knowledge
     * 
     * @return KnowledgeObjectArray
     */
    public ObjectArray searchKeyWord(final String keyWord)
    {
        Validate.notNull(keyWord);
        try
        {
            return this.knowledgeService.searchKeyWord(keyWord);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * This method returns all knowledge objects.
     * 
     * @return KnowledgeObjectArray
     */
    public KnowledgeObjectArray getKnowledgeObjects()
    {
        KnowledgeObjectArray knowledgeObjects = null;
        try
        {
            knowledgeObjects = this.knowledgeService.getKnowledgeObjects();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return knowledgeObjects;
    }

    /**
     * 
     * This method create a knowledge domain.
     * 
     * @param name Name of domain
     * @param description Description of domain
     * 
     */
    public void createKnowledgeDomain(final String name, final String description)
    {
        Validate.notNull(name);
        Validate.notNull(description);

        try
        {
            KnowledgeDomain kd = this.knowledgeService.createKnowledgeDomain(name, description);
            fireUpdateEvent(EntityType.KNOWLEDGEDOMAIN, kd.getID());
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * This method create a sub knowledge domain.
     * 
     * @param name Name of domain
     * @param parentDomainID ID of parent domain
     * @param description Description of domain
     * 
     */
    public void createKnowledgeDomain(final String name, final String parentDomainID, final String description)
    {
        Validate.notNull(name);
        Validate.notNull(parentDomainID);
        Validate.notNull(description);

        try
        {
            KnowledgeDomain kd = this.knowledgeService.createKnowledgeDomain(name, parentDomainID, description);
            fireUpdateEvent(EntityType.KNOWLEDGEDOMAIN, parentDomainID, kd.getID());
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    // **********************************************************************************************************************************

    // Services for activity ************************************************************************************************************
    /**
     * 
     * get Input parameters to activity from Database
     * 
     * @param activityInstID instance ID of activity
     * @return list of parameter
     */
    public List<Product> getActivityInputProduct(final String activityInstID)
    {
        Validate.notNull(activityInstID);

        try
        {
            return activityService.getActivityInputProduct(activityInstID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<Product>();
    }

    /**
     * 
     * Get output parameter to a activity
     * 
     * @param activityInstID instance Id of activity
     * @return list of parameter
     */
    public List<Product> getActivityOutputProduct(final String activityInstID)
    {
        Validate.notNull(activityInstID);
        try
        {
            return activityService.getActivityOututProduct(activityInstID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return new ArrayList<Product>();
    }

    /**
     * 
     * Get all activities to given process.
     * 
     * @return list of activity
     */
    public List<Activity> getCurrentTasks()
    {
        try
        {
            return this.activityService.getCurrentTasks(LoggedInUserInfo.getInstance().getPerson().getID()).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Returns roles to given process.
     * 
     * @param processID ID of process
     * @return RoleArray
     */
    public RoleArray getProcessRoles(final String processID)
    {
        Validate.notNull(processID);
        try
        {
            return this.activityService.getProcessRoles(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Returns roles to given activity.
     * 
     * @param activityID ID of activity
     * 
     * @return RoleArray
     */
    public RoleArray getActivityRoles(final String activityID)
    {
        Validate.notNull(activityID);
        try
        {
            long currentTimeMillis = System.currentTimeMillis();

            RoleArray activityRoles = this.activityService.getActivityRoles(activityID);

            LOG.info("'getActivityRoles'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");

            return activityRoles;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Returns {@link Mean}s to given {@link Activity}.
     * 
     * @param activityID ID of {@link Activity}
     * 
     * @return MeanArray List of {@link Mean}s
     */
    public MeanArray getActivityMeans(final String activityID)
    {
        Validate.notNull(activityID);
        try
        {
            long currentTimeMillis = System.currentTimeMillis();

            MeanArray activityMeans = this.activityService.getActivityMeans(activityID);
            LOG.info("'getActivityMeans'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");

            return activityMeans;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    // **********************************************************************************************************************************

    // Services for document ************************************************************************************************************

    /**
     * 
     * Upload a document to DMS.
     * 
     * @param document {@link DocumentRemote#uploadDocument}
     * @param frameID ID of Knowledge link
     * @return <code>true</code> if writing the document in DMS was successful else <code>false</code>.
     */
    public boolean uploadDocument(final Document document, final String frameID)
    {
        Validate.notNull(document);
        Validate.notNull(frameID);

        try
        {
            if (this.documentService.uploadDocumentForKnowledgeLink(document, frameID, LoggedInUserInfo.getInstance().getPerson().getUserName()))
            {
                fireUpdateEvent(EntityType.KNOWLEDGE, frameID);
                return true;
            }
            else
                return false;

        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * 
     * Upload a document to DMS.
     * 
     * @param document {@link DocumentRemote#uploadDocument}
     * @return <code>true</code> if writing the document in DMS was successful else <code>false</code>.
     */
    public boolean uploadDocument(final Document document)
    {
        Validate.notNull(document);

        try
        {
            return this.documentService.uploadDocument(document, LoggedInUserInfo.getInstance().getPerson().getUserName());
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * 
     * Search for a folder or content in DMS. In case of searching for a content the content name is the complete name of file like example.pdf.
     * 
     * @param contentName Name/title of a content. Not null.
     * @return uuid of element, if it exists, else null.
     */
    public String findFolderOrContent(final String contentName)
    {
        Validate.notNull(contentName);

        try
        {
            return this.documentService.findFolderOrContent(contentName);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * 
     * Upload a document to DMS.
     * 
     * @param document {@link DocumentRemote#uploadDocument}
     * @param frameID ID of Knowledge link
     */
    public void updateDocument(final Document document, final String frameID)
    {
        Validate.notNull(document);
        Validate.notNull(frameID);
        try
        {
            this.documentService.updateDocumentForKnowledgeLink(document, frameID, LoggedInUserInfo.getInstance().getPerson().getUserName());
            fireUpdateEvent(EntityType.KNOWLEDGE, frameID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

    }

    /**
     * 
     * Upload a document to DMS.
     * 
     * @param document {@link DocumentRemote#uploadDocument}
     * @param uuid ID of {@link Document} in DMS
     */
    public void updateDMSDocument(final Document document, final String uuid)
    {
        Validate.notNull(document);
        Validate.notNull(uuid);
        try
        {
            this.documentService.updateDMSDocument(document, uuid);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

    }

    /**
     * 
     * Upload a document to DMS.
     * 
     * @param document {@link DocumentRemote#uploadDocument}
     */
    public void updateDocument(final Document document)
    {
        Validate.notNull(document);
        try
        {
            this.documentService.updateDocument(document, LoggedInUserInfo.getInstance().getPerson().getUserName());
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * Download a document from DMS.
     * 
     * @param frameID ID of Knowledge link
     * @return Document
     */
    public Document downloadDocument(final String frameID)
    {
        Validate.notNull(frameID);
        try
        {
            return this.documentService.downloadDocument(frameID);
        }
        catch (DMSException e)
        {
            Action action = Resources.Frames.Dialog.Actions.SUPPORT_MAIL_DIALOG.getAction();
            SupportMailDialog supportMailDialog = new SupportMailDialog(null, action,
                                                                        Resources.Frames.Dialog.Texts.SUPPORT_MAIL_DLG_DESCRIPTION_FOR_DMS.getText(),
                                                                        e.toString());

            if (supportMailDialog.open() == IDialogConstants.OK_ID)
            {
                String subject = "[" + StringUtils.capitalize(DMSConstants.getCustomerFolder()) + "] ";
                MainController.getInstance().sendMessage(e.toString(), new String[] { Texts.ERROR_EMAIL_RECEIVER.getText() },
                                                         subject + Texts.ERROR_MODEL_EDITOR_EMAIL_SUBJECT.getText(),
                                                         Texts.ERROR_EMAIL_FOOTER.getText());
            }
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception exception)
        {
            handleException(exception);
        }
        return null;

    }

    /**
     * 
     * Returns the name of given document id.
     * 
     * @param documentID ID of document in Alfesco DMS. Not null
     * @return String Name of document.
     */
    public String getDocumentName(final String documentID)
    {
        Validate.notNull(documentID);
        try
        {
            return this.documentService.getDocumentName(documentID);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;

    }

    // **************************************************************************************************************************************

    // Services for organization ************************************************************************************************************
    /**
     * 
     * Get all persons, which are actually in system.
     * 
     * @return PersonArray
     */
    public PersonArray getUsers()
    {
        try
        {
            return this.organizationService.getUsers();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Get all organizations, which are actually in system.
     * 
     * @return PersonArray
     */
    public OrganisationArray getOrganizations()
    {
        try
        {
            return this.organizationService.getOrganizations();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Get all top organizations, which are actually in system.
     * 
     * @return OrganisationArray
     */
    public OrganisationArray getTopOrganizations()
    {
        try
        {
            return this.organizationService.getTopOrganizations();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            String message = "Error by getTopOrganizations: ";
            LOG.error(message, e);
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Get all top organizations, which are actually in system.
     * 
     * @param organizationID ID of given organization.
     * 
     * @return OrganisationArray
     */
    public OrganisationArray getSubOrganizations(String organizationID)
    {
        Validate.notNull(organizationID);
        try
        {
            return this.organizationService.getSubOrganizations(organizationID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Get all persons, which are actually in system.
     * 
     * @param organizationID ID of giben organization
     * 
     * @return PersonArray
     */
    public PersonArray getMembers(final String organizationID)
    {
        Validate.notNull(organizationID);
        try
        {
            return this.organizationService.getMembers(organizationID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);

        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Create a new sub organization.
     * 
     * @param organization Not null {@link Organization} model.
     * @param parentOrgaID Id of parent {@link Organization} if it exists. Else <code>null</code>
     * 
     * @return Organization
     */
    public Organization createOrganization(final Organization organization, String parentOrgaID)

    {
        Validate.notNull(organization);
        try
        {
            Organization org = null;
            if (parentOrgaID == null)
                org = this.organizationService.createOrganization(organization.getName(), organization.getAddress(), organization.getEmail(),
                                                                  organization.getTelefon(), organization.getDescription());
            else
                org = this.organizationService.createOrganization(organization.getName(), organization.getAddress(), organization.getEmail(),
                                                                  organization.getTelefon(), organization.getDescription(), parentOrgaID);
            fireUpdateEvent(EntityType.ORGANIZATION, org.getID(), parentOrgaID);
            return org;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by createOrganization: ", e);
            handlePermissionException(e);
        }
        return null;
    }

    /**
     * 
     * Create a new person.
     * 
     * @param person person which should created new
     * 
     * @return Person
     */
    public Person createUser(Person person)
    {
        Validate.notNull(person);

        try
        {
            final Person createdPersaon = this.organizationService.createUser(person.getUserName(), person.getFirstName(), person.getLastName(),
                                                                              person.getAddress(), person.getEmailAddress(), person.getTelefon(),
                                                                              person.getOrganisation(), person.getPassword());

            setDescription(createdPersaon.getID(), person.getDescription());
            fireUpdateEvent(EnumSet.of(EntityType.ORGANIZATION, EntityType.PERSON), person.getOrganisation(), person.getID());
            return createdPersaon;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by createUser: ", e);
            handlePermissionException(e);
        }
        return null;
    }

    /**
     * 
     * Get person, who belong to a role.
     * 
     * @param roleID ID of Role
     * 
     * @return PersonArray
     */
    public PersonArray getPreSelection(final String roleID)
    {
        Validate.notNull(roleID);
        try
        {
            return this.organizationService.getPreSelection(roleID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Set persons, who should belong to a role.
     * 
     * @param roleID ID of Role
     * @param personArray List of persons
     * 
     */
    public void setPreSelection(final String roleID, final PersonArray personArray)
    {
        Validate.notNull(roleID);
        Validate.notNull(personArray);
        try
        {
            this.organizationService.setPreSelection(roleID, personArray);
            fireUpdateEvent(EnumSet.of(EntityType.ROLE, EntityType.PERSON), roleID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * Set persons as member of a another organization.
     * 
     * @param userID ID of user
     * @param organizationID ID of organization
     * 
     */
    public void setPersonAsMember(final String userID, final String organizationID)
    {
        Validate.notNull(userID);
        Validate.notNull(organizationID);
        try
        {
            this.organizationService.setPersonAsMember(userID, organizationID);
            fireUpdateEvent(EntityType.ORGANIZATION, organizationID, userID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * Set persons as member of a another organization.
     * 
     * @param person {@link Person}, who should updates. Not null
     * 
     */
    public void updateUserInfo(Person person)
    {
        Validate.notNull(person);
        try
        {
            this.organizationService.updateUser(person.getID(), person.getUserName(), person.getFirstName(), person.getLastName(),
                                                person.getAddress(), person.getEmailAddress(), person.getTelefon(), person.getDescription(),
                                                person.getPassword());

            // Set the organization of person
            if ( !person.getOrganisation().equals(""))
                setPersonAsMember(person.getID(), person.getOrganisation());

            fireUpdateEvent(EntityType.ORGANIZATION, person.getID());
            fireUpdateEvent(EntityType.PERSON, person.getID());
        }
        catch (OntologyErrorException e)
        {
            handleException(e);

        }
        catch (Exception e)
        {
            LOG.error("Error by updateUserInfo: ", e);
            handlePermissionException(e);
        }
    }

    /**
     * 
     * Update the data of given organization.
     * 
     * @param organization TODO
     * 
     */
    public void updateOrgaInfo(Organization organization)
    {
        Validate.notNull(organization);

        try
        {
            this.organizationService.updateOrganization(organization.getID(), organization.getName(), organization.getAddress(),
                                                        organization.getEmail(), organization.getTelefon(), organization.getDescription());
            fireUpdateEvent(EntityType.ORGANIZATION, organization.getID());
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by updateOrgaInfo: ", e);
            handlePermissionException(e);
        }
    }

    // *****************************************************************************************************************************

    // General services ************************************************************************************************************
    /**
     * 
     * Get all properties to given instanceID.
     * 
     * @param instanceID ID of instance. Not null.
     * 
     * @return InstancePropertyArray
     */
    public InstancePropertyArray getProperties(final String instanceID)
    {
        Validate.notNull(instanceID);

        try
        {
            return this.commonService.getProperties(instanceID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Get description to given instanceID.
     * 
     * @param instanceID ID of instance. Not null.
     * 
     * @return String, can be empty, never null
     */
    public String getDescription(final String instanceID)
    {
        Validate.notNull(instanceID);

        try
        {
            long currentTimeMillis = System.currentTimeMillis();

            String description = this.commonService.getDescription(instanceID);
            LOG.info("'getDescription'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");
            return description;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Get all relations to given instanceID.
     * 
     * @param instanceID ID of instance. Not null.
     * 
     * @return InstancePropertyArray
     */
    public InstancePropertyArray getRelations(final String instanceID)
    {
        Validate.notNull(instanceID);

        try
        {
            return this.commonService.getRelations(instanceID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Returns all process elements of the given element class.
     * 
     * @param processID ID of process, may not be null
     * @param elementClass the class of the element, may not be null, see types below.
     * 
     *        Just now only following types of classes are supported:
     * 
     *        <li>GlobalConstants.ACTIVITY</li>
     * 
     *        <li>GlobalConstants.ROLE</li>
     * 
     *        <li>GlobalConstants.MEAN</li>
     * 
     *        <li>GlobalConstants.RESULTS_MEMORY</li>
     * 
     * @see Activity
     * @see Role
     * @see Mean
     * @see ResultsMemory
     * 
     * @return ObjectArray non null, it returns all instances of the given type as elementClass. if no instance is found the return result is empty.
     */
    public ObjectArray getProcessElementsInstances(final String processID, final String elementClass)
    {
        Validate.notNull(processID);
        Validate.notNull(elementClass);

        try
        {
            return this.commonService.getProcessElementsInstances(processID, elementClass);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return new ObjectArray();
    }

    /**
     * 
     * Set description for given instanceID.
     * 
     * @param instanceID ID of instance. Not null.
     * @param description Description of object, not null.
     * 
     */
    public void setDescription(final String instanceID, final String description)
    {
        Validate.notNull(instanceID);
        Validate.notNull(description);

        try
        {
            this.commonService.setDescription(instanceID, description);
            fireUpdateEvent(EnumSet.of(EntityType.PROCESS, EntityType.PROCESSCATEGORY, EntityType.ACTIVITY, EntityType.MEAN, EntityType.ROLE,
                                       EntityType.RESULTSMEMORY, EntityType.KNOWLEDGE, EntityType.KNOWLEDGEDOMAIN, EntityType.KNOWLEDGELINK),
                            instanceID, description);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * Get name for given instanceID.
     * 
     * @param instanceID ID of instance. Not null.
     * @return String
     * 
     */
    public String getName(final String instanceID)
    {
        Validate.notNull(instanceID);

        try
        {
            long currentTimeMillis = System.currentTimeMillis();

            String name = this.commonService.getName(instanceID);

            LOG.info("'getName'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");

            return name;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (WebServiceException e)
        {
            handleException(e);
        }

        throw new IllegalStateException();

    }

    /**
     * 
     * Rename element of given instanceID.
     * 
     * @param instanceID ID of instance. Not null.
     * @param newName New name of element. Not null.
     */
    public void rename(final String instanceID, final String newName)
    {
        Validate.notNull(instanceID);
        Validate.notNull(newName);

        try
        {
            this.commonService.rename(instanceID, newName);
            if ( !newName.equals(GlobalConstants.NEW_PROCESS))
            {
                fireUpdateEvent(EnumSet.of(EntityType.PROCESS, EntityType.PROCESSCATEGORY, EntityType.ACTIVITY, EntityType.MEAN, EntityType.ROLE,
                                           EntityType.RESULTSMEMORY, EntityType.KNOWLEDGE, EntityType.KNOWLEDGEDOMAIN, EntityType.KNOWLEDGELINK,
                                           EntityType.ORGANIZATION), instanceID);
            }
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * Deletes an instance/element in ontology. It deletes only the element itself and not the relation of element to other elements.
     * 
     * @param instanceID ID of the instance/element.
     */
    public void deleteInstance(final String instanceID)
    {
        Validate.notNull(instanceID);

        try
        {
            this.commonService.deleteInstance(instanceID);
            fireUpdateEvent(EnumSet.of(EntityType.PROCESS, EntityType.ACTIVITY, EntityType.MEAN, EntityType.ROLE, EntityType.RESULTSMEMORY,
                                       EntityType.KNOWLEDGE, EntityType.KNOWLEDGEDOMAIN, EntityType.KNOWLEDGELINK), instanceID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by instanceID: ", e);
            handlePermissionException(e);
        }
    }

    /**
     * 
     * Sets the values of given parameter.
     * 
     * @param instanceID not null instance ID.
     * @param scope not null scope : Local or Global values are permitted.
     * 
     */
    public void setScope(String instanceID, String scope)
    {
        Validate.notNull(instanceID);
        Validate.notNull(scope);

        try
        {
            this.commonService.setScope(instanceID, scope);
            fireUpdateEvent(EntityType.PROCESS, instanceID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * This method get a key word and returns a KnowledgeObjectArray, which included all knowledge objects, which have this word in their name
     * 
     * @param keyWord That is the search world for knowledge
     * 
     * @return KnowledgeObjectArray
     */
    public PersonArray searchPersons(final String keyWord)
    {
        Validate.notNull(keyWord);
        try
        {
            return this.adminService.searchPerson(keyWord);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * login.
     * 
     * @param userName the not null userName.
     * @param password the not null password.
     * @return true if succeeded.
     */
    public boolean login(String userName, String password)
    {
        Validate.notNull(userName);
        Validate.notNull(password);
        try
        {
            return this.adminService.login(userName, password);
        }
        catch (OntologyErrorException e)
        {
            return false;
        }
    }

    /**
     * Gets a user with a given userName.
     * 
     * @param userName the not null userName.
     * @return Person or null if user with given username was not found
     */
    public Person getUserWithName(String userName)
    {
        Validate.notNull(userName);
        try
        {
            return this.adminService.getUserWithName(userName);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        throw new IllegalStateException("Someting went totally wrong with the retrieval of user with username " + userName);
    }

    /**
     * Gets a user with a given userID.
     * 
     * @param userID the not null userID.
     * @return the person found or null, if no person was found
     */
    public Person getUserWithID(String userID)
    {
        Validate.notNull(userID);

        try
        {
            return this.adminService.getUserWithID(userID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        throw new IllegalStateException("Something went totally wrong with the retrieval of user with id " + userID);
    }

    /**
     * Gets the rights roles.
     * 
     * @param userID not null user ID.
     * @return not null list of {@link RightsRole}
     */
    public List<RightsRole> getRightsRoles(String userID)
    {
        try
        {
            return this.adminService.getRightsRoles(userID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by getting rights roles for user : " + userID, e);
            handlePermissionException(e);
        }
        throw new IllegalStateException("Error by getRightsRoles " + userID);
    }

    /**
     * Gets the existing rights roles.
     * 
     * @return not null list of {@link RightsRole}
     */
    public List<RightsRole> getExistingRightsRoles()
    {
        try
        {
            return this.adminService.getExistingRightsRoles().getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by getting existing rights roles : ", e);
            handlePermissionException(e);
        }
        throw new IllegalStateException("Error by getExistingRightsRoles ");
    }

    /**
     * 
     * See below link
     * 
     * @see org.prowim.services.ejb.admin.AdminRemote#getPersonsOfRightRoles(java.lang.String)
     * @param rightsRoleName @see de.ebcot.prowim.services.ejb.admin.AdminRemote#getPersonsOfRightRoles(java.lang.String)
     * 
     * @return not null list of {@link RightsRole}
     */
    public PersonArray getPersonsOfRightRoles(String rightsRoleName)
    {
        Validate.notNull(rightsRoleName);
        try
        {
            return this.adminService.getPersonsOfRightRoles(rightsRoleName);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by getting perons of given right role: ", e);
            handlePermissionException(e);
        }
        throw new IllegalStateException("Error by getPersonsOfRightRoles ");
    }

    /**
     * Assigne RightsRoles to a user.
     * 
     * @param userID ID of the user.
     * @param rightsRoles not null list of {@link RightsRole}
     */
    public void assigneRightsRolesToUser(String userID, List<RightsRole> rightsRoles)
    {
        Validate.notNull(userID);
        Validate.notNull(rightsRoles);
        try
        {
            RightsRoleArray param = new RightsRoleArray();
            param.addAll(rightsRoles);
            this.adminService.assigneRightsRolesToUser(userID, param);
            fireUpdateEvent(EnumSet.of(EntityType.ROLE, EntityType.PERSON), userID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by assigning rights roles : ", e);
            handlePermissionException(e);
        }

    }

    /**
     * Sets the users that can modify the entity.
     * 
     * @param entityID not null selected entity.
     * @param users not null list of users names that can modify the entity.
     */
    public void setElementCanModifyEntity(String entityID, ObjectArray users)
    {
        try
        {
            this.adminService.setElementCanModifyEntity(entityID, users);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

    }

    /**
     * Gets the persons that can modify the entity.
     * 
     * @param entityID not null entity ID.
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     */
    public List<Person> getPersonsCanModifyEntity(String entityID)
    {
        Validate.notNull(entityID);
        List<Person> result = new ArrayList<Person>();
        try
        {
            return this.adminService.getPersonCanModifyEntity(entityID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handlePermissionException(e);
            LOG.error("Error by setting modification rights to persons : ", e);
        }

        return result;

    }

    /**
     * Gets the persons that can modify the entity.
     * 
     * @param entityID the entity ID.
     * @return not null {@link PersonArray}. If no item exists, an empty list is returned.
     */
    public List<Organization> getOrganizationsCanModifyEntity(String entityID)
    {
        Validate.notNull(entityID);
        List<Organization> result = new ArrayList<Organization>();
        try
        {
            return this.adminService.getOrganizationsCanModifyEntity(entityID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handlePermissionException(e);
            LOG.error("Error by setting modification rights to organisations : ", e);
        }

        return result;

    }

    /**
     * Checks if a user can modify an entity.
     * 
     * @param entityID not null entityID.
     * @param personID not null personID.
     * @return true if the person can modify the entity, otherwise false.
     */
    public boolean canPersonModifyEntity(String entityID, String personID)
    {
        Validate.notNull(entityID);
        Validate.notNull(personID);
        try
        {
            return this.adminService.canPersonModifyEntity(entityID, personID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handlePermissionException(e);
            LOG.error("Error by getting permissions to modify entity : " + entityID + "  from user " + personID, e);
        }

        return true;
    }

    /**
     * Gets the wiki URL.
     * 
     * @return the wiki URL.
     */
    public String getWikiURL()
    {
        try
        {
            return this.adminService.getWikiURL();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * Gets the version history to a document that is stored in alfresco DMS.
     * 
     * @param linkID the knowledgelink ID (frame ID, not the DMS node ID!), may not be null
     * @return {@link VersionHistory}.
     */
    public VersionHistory getKnowledgeLinkVersionHistory(String linkID)
    {
        Validate.notNull(linkID);

        try
        {
            return this.documentService.getVersionHistory(linkID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            String message = "Error by getting document version history : ";
            LOG.error(message, e);
            handleException(e);
        }
        return null;
    }

    /**
     * Adds a role to a selected organization.
     * 
     * @param roleID not null role ID.
     * @param organizationID not null organization ID.
     */
    public void addRoleToOrganization(String roleID, String organizationID)
    {
        Validate.notNull(roleID);
        Validate.notNull(organizationID);
        try
        {
            this.organizationService.addRoleToOrganization(roleID, organizationID);
            fireUpdateEvent(EntityType.ORGANIZATION, roleID, organizationID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handlePermissionException(e);
            LOG.error("Error by adding a role to an organization : ", e);
        }
    }

    /**
     * Gets the roles defined in an organization.
     * 
     * @param organizationID not null organization ID.
     * @return not null {@link RoleArray}. If no item exists, an empty list is returned.
     */
    public List<Role> getRolesToOrganization(String organizationID)
    {
        Validate.notNull(organizationID);
        try
        {
            return this.organizationService.getRolesToOrganization(organizationID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;

    }

    /**
     * Removes roles from an organization.
     * 
     * @param roles not null role list.
     * @param organizationID not null organization ID.
     */
    public void removeRolesFromOrganization(ObjectArray roles, String organizationID)
    {
        Validate.notNull(roles);
        Validate.notNull(organizationID);
        try
        {
            this.organizationService.removeRolesFromOrganization(roles, organizationID);

            fireUpdateEvent(EntityType.ORGANIZATION, organizationID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Creates a new global Role.
     * 
     * @param classname name of class in ontology
     * @param name name of class, which shows in GUI
     * @param isGlobal flag, if the class is global or local
     * @param description description of element.
     * @return role ID.
     */
    public String createProcessElement(String classname, String name, boolean isGlobal, String description)
    {
        Validate.notNull(classname);
        Validate.notNull(name);
        Validate.notNull(isGlobal);
        Validate.notNull(description);

        try
        {
            String elementID = this.processService.createProcessElement(classname, name, isGlobal);
            commonService.setDescription(elementID, description);

            fireUpdateEvent(EntityType.PROCESS, elementID);
            return elementID;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;

    }

    /**
     * Creates a prowim model editor object.<br>
     * Prowim object IDs are those: Prozess, Aktivität, Mittel, ProzessInformation, etc
     * 
     * 
     * @param modelID The ID of the model, where the elements must be added. Null is possible. <br>
     *        Null is, if the model itself must be created.
     * @param oid the selected object ID to be created. Not null.
     * @return the generated ID from the ontology.
     */
    public String createObject(String modelID, String oid)
    {
        Validate.notNull(modelID);
        Validate.notNull(oid);
        try
        {
            return this.editorService.createObject(modelID, oid);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * 
     * Deletes given {@link Organization}.
     * 
     * @param organizationID Id of {@link Organization}. Not null.
     */
    public void deleteOrganization(String organizationID)
    {
        Validate.notNull(organizationID);
        try
        {
            this.organizationService.deleteOrganization(organizationID);
            fireUpdateEvent(EntityType.ORGANIZATION, organizationID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Moves organization as sub organization of another organization.
     * 
     * @param selectedOrganizationID not null organization ID.
     * @param parentOrganizationID not null parent Organization ID.
     */
    public void moveOrganization(String selectedOrganizationID, String parentOrganizationID)
    {

        Validate.notNull(selectedOrganizationID);
        Validate.notNull(parentOrganizationID);

        try
        {
            if (parentOrganizationID.equals(GlobalConstants.ROOT_MODEL))
            {
                this.moveOrganizationToTop(selectedOrganizationID);
            }
            else
            {
                this.organizationService.moveOrganization(selectedOrganizationID, parentOrganizationID);
            }
            fireUpdateEvent(EntityType.ORGANIZATION, selectedOrganizationID, parentOrganizationID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            LOG.error("Error by moving the organization with ID : " + selectedOrganizationID + " to the organization " + parentOrganizationID, e);
            handlePermissionException(e);
        }
    }

    /**
     * Moves organization to top
     * 
     * @param organizationID not null organization ID.
     */
    public void moveOrganizationToTop(String organizationID)
    {
        moveOrganization(organizationID, GlobalConstants.ROOT_MODEL);
    }

    /**
     * Gets the subprocesses list.
     * 
     * @param processID not null process ID.
     * @return not null {@link List}. If no item exists, an empty list is returned.
     */
    public List<Process> getSubProcesses(String processID)
    {
        Validate.notNull(processID);
        try
        {
            return this.processService.getSubProcesses(processID).getItem();

        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Opens a model and set it free to be started and updated.
     * 
     * @param modelId id of {@link Process}. not null.
     * @see EditorRemote#approveProcess(String)
     */
    public void approveProcess(String modelId)
    {
        try
        {
            this.editorService.approveProcess(modelId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Sets the {@link Process} invisible. The {@link Process} can not started as workflow.
     * 
     * @param modelId Id of {@link Process}. not null.
     * @see EditorRemote#disapproveProcess(String)
     */
    public void disapproveProcess(String modelId)
    {
        try
        {
            this.editorService.disapproveProcess(modelId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Check if the {@link Process}is approved or not.
     * 
     * @param processID Id of {@link Process}. not null.
     * @return <code>true</code> if {@link Process} is approved. else <code>false</code>
     */
    public boolean isProcessApproved(String processID)
    {
        try
        {
            return this.editorService.isProcessApproved(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException("Error by isProcessApproved.");
    }

    /**
     * Connects the two {@link Activity}s.
     * 
     * @param sourceID id of source {@link Activity}. Not null.
     * @param targetID if of target {@link Activity}. Not null.
     * @param controlFlowID id of {@link ControlFlow} between both {@link Activity}s. not null.
     * @see EditorRemote#connectActivityControlFlow(String, String, String)
     */
    public void connectActivityControlFlow(String sourceID, String targetID, String controlFlowID)
    {
        try
        {
            this.editorService.connectActivityControlFlow(sourceID, targetID, controlFlowID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Connects activity and role with a task= "Tätigkeit".
     * 
     * 
     * @param activityID id of {@link Activity}. Not null.
     * @param roleID id of {@link Role}. Not null.
     * @param taskID id of {@link Work} "Tätigkeit". Not null.
     * @see EditorRemote#connectActivityRole(String, String, String)
     */
    public void connectActivityRole(String activityID, String roleID, String taskID)
    {
        try
        {
            this.editorService.connectActivityRole(activityID, roleID, taskID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Gets all possible relations to the both objects.
     * 
     * @param sourceId id of source element. not null.
     * @param targetId id of target element. not null.
     * @return not null
     * @see EditorRemote#getPossibleRelations(String, String)
     */
    public String[] getPossibleRelations(String sourceId, String targetId)
    {
        try
        {
            return editorService.getPossibleRelations(sourceId, targetId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Set a relation between to elements or a value for a given slot for given element. See documentation below.
     * 
     * @param instanceId id of element, which includes the slot. E.g. process, activity, etc.
     * @param slot name of slot, e.g. darf_gesetzt_werden_von. This is the slot of given instance, which you want to set.
     * @param relationId id or value´, which should to set in the slot. <br>
     *        if you want to set a activity in relation to a process, you can call the method so : setRelationValue("process_123", "besteht_aus", "activity_12345"). <br>
     *        if you want to define a Role as local you have to call: setRelationValue("Rolle_12334", "Geltungsbereich", "Local")<br>
     * 
     *        TODO: Ich habe die Methode so wie es verstanden habe dukumentiert. Bitte nochmal checken, ob alles OK ist
     * @see EditorRemote#setRelationValue(String, String, String)
     */
    public void setRelationValue(String instanceId, String slot, String relationId)
    {
        try
        {
            editorService.setRelationValue(instanceId, slot, relationId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Removes the value of given {@link Slots} for given element.
     * 
     * @param instanceId id of element, which has the given slot.
     * @param slot name of slot
     * @see EditorRemote#removeRelationValue(String, String)
     */
    public void removeRelationValue(String instanceId, String slot)
    {
        try
        {
            editorService.removeRelationValue(instanceId, slot);

        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Sets a {@link Product} from the source {@link Activity} to the target {@link Activity}.
     * 
     * @param source id of source {@link Activity}. not null.
     * @param target id of target {@link Activity}. Not null.
     * @param productID id of {@link Product}. Not null.
     * @see EditorRemote#setProduct(String, String, String)
     */
    public void setProduct(String source, String target, String productID)
    {
        try
        {
            editorService.setProduct(source, target, productID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Adds a new process information to a product.
     * 
     * @param productID not null product ID.
     */
    public void addProcessInformationToProduct(String productID)
    {
        Validate.notNull(productID);
        try
        {
            processService.addProcessInformationToProduct(productID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Count of {@link ControlFlow}s which are defined as input with given {@link Activity}.
     * 
     * @param targetID id of {@link Activity}. Not null.
     * @return not null
     * @see EditorRemote#controlFlowCount(String)
     */
    public String controlFlowCount(String targetID)
    {
        try
        {
            return editorService.controlFlowCount(targetID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Gets the combination rule for a {@link ControlFlow}.
     * 
     * @param controlflowID id of {@link ControlFlow}.
     * @return not null
     * @see EditorRemote#getCombinationRule(String)
     */
    public String getCombinationRule(String controlflowID)
    {
        try
        {
            return editorService.getCombinationRule(controlflowID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Bends a {@link ControlFlow} from an {@link Activity} to another.
     * 
     * @param controlflowID id of {@link ControlFlow}. Not null.
     * @param oldSource id of actual {@link Activity} which is defined as source.
     * @param oldTarget id of actual {@link Activity} which is defined as target.
     * @param newSource id of new {@link Activity} which should be define as source.
     * @param newTarget id of new {@link Activity} which should be define as target.
     * @return SUCCEEDED or FAILED
     * @see EditorRemote#bendControlFlow(String, String, String, String, String)
     */
    public String bendControlFlow(String controlflowID, String oldSource, String oldTarget, String newSource, String newTarget)
    {
        try
        {
            return editorService.bendControlFlow(controlflowID, oldSource, oldTarget, newSource, newTarget);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Gets the registered combination rules.
     * 
     * @return not null
     * @see EditorRemote#getPossibleCombinationRules()
     */
    public String[] getPossibleCombinationRules()
    {
        try
        {
            return editorService.getPossibleCombinationRules();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Sets a combination rule to a {@link ControlFlow}. The combination rule is saved in ontology.
     * 
     * 
     * @param controlFlowID id of {@link ControlFlow}. Not null.
     * @param combinationRule name of combination rule.
     * @see EditorRemote#setCombinationRule(String, String)
     */
    public void setCombinationRule(String controlFlowID, String combinationRule)
    {
        try
        {
            editorService.setCombinationRule(controlFlowID, combinationRule);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Gets the registered activation rules.
     * 
     * 
     * @return not null
     * @see EditorRemote#getPossibleActivationRules()
     */
    public String[] getPossibleActivationRules()
    {
        try
        {
            return editorService.getPossibleActivationRules();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Sets a activation rule to a object. This can be class, which has the slot "hat_Aktivierungsregel" like "aktivität" or "Entscheidung"
     * 
     * 
     * @param objectID id of {@link ProcessElement}. not null.
     * @param activationRule name of rule, which is saved in ontology. Not null.
     * @see EditorRemote#setActivationRule(String, String)
     */
    public void setActivationRule(String objectID, String activationRule)
    {
        try
        {
            editorService.setActivationRule(objectID, activationRule);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Connects a {@link Activity} with a {@link Mean} "Mittel"
     * 
     * @param activityID id of {@link Activity}. Not null.
     * @param mittelID id of {@link Mean}. Not null.
     * @param functionID id of {@link Function}. Not null.
     * @see EditorRemote#connectActivityMittel(String, String, String)
     */
    public void connectActivityMean(String activityID, String mittelID, String functionID)
    {
        try
        {
            editorService.connectActivityMittel(activityID, mittelID, functionID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Get the ID of the {@link ProcessInformation} holding the DMS UUID for the XML model of the given {@link Process}.
     * 
     * @param processId id of {@link Process}. not null.
     * @return the ID of the {@link ProcessInformation} or null if no {@link ProcessInformation} was found
     * @see EditorRemote#getProcessInformationID(String)
     */
    public String getProcessInformationID(String processId)
    {
        try
        {
            return editorService.getProcessInformationID(processId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Gets all exists sub processes.
     * 
     * @return not null
     * @see EditorRemote#getAllExistSubProcesses()
     */
    public ProcessArray getAllExistSubProcesses()
    {
        try
        {
            return editorService.getAllExistSubProcesses();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Set flag of a activity "Vorgangstype" as sub process "Teilprozess".
     * 
     * 
     * @param activityID id of {@link Activity}. Not null.
     * @see EditorRemote#setSubProcessFlagForActivity(String)
     */
    public void setSubProcessFlagForActivity(String activityID)
    {
        try
        {
            editorService.setSubProcessFlagForActivity(activityID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 
     * Set the {@link Process} as {@link Subprocess}
     * 
     * @param processID ID of given {@link Process}.
     * @see EditorRemote#setSubProcessFlagForProcess(String)
     */
    public void setSubProcessFlagForProcess(String processID)
    {
        try
        {
            editorService.setSubProcessFlagForProcess(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Set the slot "Autovorgang" for a {@link Activity} as true.
     * 
     * 
     * @param activityID id of {@link Activity}. Not null.
     * @see EditorRemote#setActivityAsAuto(String)
     */
    public void setActivityAsAuto(String activityID)
    {
        try
        {
            editorService.setActivityAsAuto(activityID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Set the slot "Autovorgang" for a activity as false.
     * 
     * 
     * @param activityID id of {@link Activity}. Not null.
     * @see EditorRemote#setActivityAsAuto(String)
     */
    public void setActivityAsManual(String activityID)
    {
        try
        {
            editorService.setActivityAsManual(activityID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Deletes the given {@link ProcessElement} from {@link Process}
     * 
     * @param processID id of {@link Process}
     * @param elementID id of to deletes {@link ProcessElement}
     * @see EditorRemote#deleteElementFromModel(String, String)
     */
    public void deleteElementFromModel(String processID, String elementID)
    {
        try
        {
            editorService.deleteElementFromModel(processID, elementID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Set the slot "hat_submodel" for a activity. This defines the {@link Activity} as a start point for the included {@link Subprocess}
     * 
     * 
     * @param subProcessID id of {@link Subprocess}. Not null.
     * @param activityID id of {@link Activity}. Not null.
     * @see EditorRemote#setSubProcessOfActivity(String, String)
     */
    public void setSubProcessOfActivity(String subProcessID, String activityID)
    {
        try
        {
            editorService.setSubProcessOfActivity(subProcessID, activityID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Gets the name of properties defined for the object with the given ID.
     * 
     * @param instanceId id of given element. not null.
     * @return not null
     * @see CommonRemote#getInstancePropertiesNames(String)
     */
    public InstancePropertiesNames getInstancePropertiesNames(String instanceId)
    {
        try
        {
            return commonService.getInstancePropertiesNames(instanceId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Gets all documents stored in alfresco DMS.
     * 
     * @return a list of document properties.<name, create time, version etc.>
     */
    public List<DocumentContentProperties> getAllDocuments()
    {
        List<DocumentContentProperties> allDocuments = null;
        try
        {
            allDocuments = this.documentService.getAllDocuments().getList();
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return allDocuments;
    }

    /**
     * Gets all documents stored in alfresco DMS.
     * 
     * @param documentID the not null selected document ID.
     * @param frameID the not null frame ID.
     * @param documentName the name of the document.
     * @param versionLabel the version label. null is possible.
     */
    public void bindDocument(String documentID, String documentName, String frameID, String versionLabel)
    {
        try
        {
            this.documentService.bindDocument(documentID, documentName, frameID, versionLabel);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Gets all documents stored in alfresco DMS.
     * 
     * @param documentID the not null selected document ID.
     * @param versionLabel the not null ID.
     * @return {@link Document}. not null.
     */
    public Document downloadDocumentFromID(String documentID, String versionLabel)
    {
        Validate.notNull(documentID);
        Validate.notNull(versionLabel);
        Document document = null;
        try
        {
            document = this.documentService.downloadDocumentInVersionFromID(documentID, versionLabel);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return document;
    }

    /**
     * Downloads a document from a given uuid.
     * 
     * @param id the not null frameID.
     * @param versionLabel the not null version label.
     * @return not null {@link Document}.
     */
    public Document downloadDocumentInVersion(String id, String versionLabel)
    {
        Validate.notNull(id);
        Validate.notNull(versionLabel);
        try
        {
            return this.documentService.downloadDocumentInVersion(id, versionLabel);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return null;
    }

    /**
     * Gets the version history from a given uuid.
     * 
     * @param id the not null uuid.
     * @return the not null {@link VersionHistory}
     */
    public VersionHistory getVersionHistoryFromNodeID(String id)
    {
        Validate.notNull(id);

        try
        {
            return this.documentService.getVersionHistoryFromNodeID(id);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        throw new IllegalStateException();
    }

    /**
     * Sets the given process template in the given version as the active process template.
     * 
     * @param instanceID the process template to activate, does not need to be the initial version of the template
     * @param versionName the version to activate
     * @param makeEditable if true, the version will be editable (means, the newest version will be replaced in the DMS)
     * @return true, if the version has been activated, false otherwise
     */
    public boolean activateVersion(String instanceID, String versionName, boolean makeEditable)
    {
        try
        {
            // change active version in the ontology
            String status = processService.activateVersion(instanceID, versionName, makeEditable);

            final boolean wasSuccessful = AlgernonConstants.OK.equals(status);
            if (wasSuccessful)
            {
                fireUpdateEvent(EntityType.PROCESS, instanceID);
            }
            else
            {
                ErrorDialog.openError(null, Resources.Frames.Dialog.Texts.ERROR_ACTIVATE_VERSION.getText());
            }

            return wasSuccessful;
        }
        catch (OntologyErrorException ontologyErrorEx)
        {
            handleException(ontologyErrorEx);
        }
        catch (DMSException dmsEx)
        {
            handleException(dmsEx);
        }
        throw new IllegalStateException();
    }

    /**
     * 
     * Gets {@link Process} to given version for {@link Process}id
     * 
     * @param processID Id of given {@link Process}. Not null.
     * @param version version of {@link Process}. Not null
     * @return {@link Process} Returns null if no process for this version is fined
     */
    public Process getProcessForVersion(String processID, String version)
    {
        Validate.notNull(processID, "Id of process could not be null");
        Validate.notNull(version, "Version could not be null");

        try
        {
            ProcessArray processVersions = processService.getProcessVersions(processID);
            for (Process process : processVersions)
            {
                if (process.getUserDefinedVersion().equals(version))
                    return process;

            }
        }
        catch (OntologyErrorException ontologyErrorEx)
        {
            handleException(ontologyErrorEx);
        }
        throw new IllegalStateException();

    }

    /**
     * Gets the version history from a given process (frame) ID.
     * 
     * @param processID the ID of the process frame (has to be a template), may not be null
     * @return the not null {@link VersionHistory}
     */
    public VersionHistory getVersionHistoryFromProcessID(String processID)
    {
        Validate.notNull(processID);
        try
        {
            List<Version> versionList = new ArrayList<Version>();
            VersionHistory versionHistoryTmp = this.documentService.getVersionHistoryFromProcessID(processID);

            ProcessArray processVersions = processService.getProcessVersions(processID);
            for (Process process : processVersions.getItem())
            {
                if ( !isEditableVersion(process.getAlfrescoVersion()))
                {
                    Version version = null;
                    version = versionHistoryTmp.getVersion(process.getAlfrescoVersion());

                    if (version == null)
                    {
                        LOG.error("Could not find a document version for process with ID " + process.getTemplateID() + " and alfresco version "
                                + process.getAlfrescoVersion());
                    }
                    else
                    {

                        // default value
                        String userDefinedVersion = "Unbekannt";

                        if (process.getUserDefinedVersion() != null)
                        {
                            userDefinedVersion = process.getUserDefinedVersion();
                        }
                        version.setUserDefinedVersion(userDefinedVersion);
                        version.setIsActiveVersion(process.getIsActiveVersion());
                        version.setInstanceID(process.getTemplateID());

                        versionList.add(version);
                    }
                }
            }

            versionHistoryTmp.setVersions(versionList);

            return versionHistoryTmp;

        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (DMSException e)
        {
            handleException(e);
        }

        return null;
    }

    /**
     * Gets the document uuid used in alfresco.
     * 
     * @param id the not null id.
     * @return not null document ID.
     */
    public String getLinkDocumentID(String id)
    {
        Validate.notNull(id);
        try
        {
            return this.documentService.getLinkDocumentID(id);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Gets the document version label.
     * 
     * @param linkID not null link ID.
     * @return the not null version label.
     */
    public String getDocumentVersionLabel(String linkID)
    {
        try
        {
            return this.documentService.getStoredDocumentVersionLabel(linkID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException();
    }

    /**
     * Checks if the given element is defined as global.
     * 
     * @param instanceId id of element. not null.
     * @return a boolean True if the instance is global.
     * 
     * @see CommonRemote#isGlobal(String)
     */
    public boolean isGlobal(String instanceId)
    {
        try
        {
            return commonService.isGlobal(instanceId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return false;
    }

    /**
     * Handle's a {@link Exception exception} and displays it in a simple {@link ErrorDialog error dialog}.
     * 
     * @param e the exception to handle
     */
    public void handleException(Exception e)
    {
        // Only show MessageBox, if the method was not called from a BrowserFunction.
        // This is a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=321389
        boolean calledFromBrowserFunction = false;
        for (StackTraceElement ste : Thread.currentThread().getStackTrace())
        {
            try
            {
                Class<? > callingClass = Class.forName(ste.getClassName());
                if (AbstractModelEditorFunction.class.isAssignableFrom(callingClass))
                {
                    calledFromBrowserFunction = true;
                    break;
                }
            }
            catch (ClassNotFoundException e1)
            {
                // should not be possible. But, you know: OSGi
                LOG.error("Could not load class from stack. ", e1);
            }
        }

        LOG.error("An ERROR occurred while excuting a maincontroller method: ", e);
        if ( !calledFromBrowserFunction)
        {
            ErrorDialog.openException(e);
        }
    }

    /**
     * Handles {@link Exception exceptions} containing permission information.
     * 
     * @param e the exception to handle
     */
    private void handlePermissionException(Exception e)
    {
        ErrorDialog.openError(null, PermissionExceptionsHandler.getMessage(e));
    }

    /**
     * 
     * Returns all {@link ProcessElement}s for given {@link Process}id..
     * 
     * @param processID ID of given {@link Process}. Not null.
     * @return {@link ArrayList}. Not null,
     */
    public List<ProcessElement> getElementsOfProcess(String processID)
    {
        Validate.notNull(processID);
        List<ProcessElement> elements = new ArrayList<ProcessElement>();
        try
        {
            long currentTimeMillis = System.currentTimeMillis();

            ObjectArray item = this.processService.getElementsOfProcess(processID);
            LOG.info("'getElementsOfProcess'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");
            List<Object> items = item.getItem();

            for (Object object : items)
            {
                elements.add((ProcessElement) object);
            }
            return elements;

        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (WebServiceException e)
        {
            handleException(e);
        }

        throw new IllegalStateException("Error getElementsOfProcess " + processID);
    }

    /**
     * Gets the process, where the entity is defined.
     * 
     * @param entityID the not null entity ID.
     * @return process ID or null if the entity is not assigned to any process.
     */
    public List<String> getParentProcess(String entityID)
    {
        Validate.notNull(entityID);
        try
        {
            return this.processService.getParentProcess(entityID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException("Error by getParentProcess " + entityID);
    }

    /**
     * returns the class of the selected element.
     * 
     * @param id the element ID.
     * @return not null direct class.
     */
    public String getDirectClassOfInstance(final String id)
    {
        Validate.notNull(id);
        try
        {
            long currentTimeMillis = System.currentTimeMillis();

            String directClassOfInstance = this.commonService.getDirectClassOfInstance(id);

            LOG.info("'getDirectClassOfInstance'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");

            return directClassOfInstance;
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException("Error by getDirectClassOfInstance " + id);
    }

    /**
     * 
     * Searches for the given key word in all documents in DMS which are not processes.
     * 
     * @param keyWord the key word to search
     * @return a list of document properties.<name, create time, version etc.>
     */
    public List<DocumentContentProperties> searchDMSFullText(String keyWord)
    {
        Validate.notNull(keyWord, "The key word to search in DMS must not be null");

        try
        {
            DocumentContentPropertiesArray searchFullText = this.documentService.searchFullText(keyWord);
            return searchFullText.getList();
        }
        catch (DMSException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return Collections.EMPTY_LIST;

    }

    /**
     * 
     * Searches the given key word in all wiki articles in the media wiki.
     * 
     * @param keyWord the key word to search
     * @return a list of wiki articles (name, hyperlink)
     */
    public List<WikiContentProperties> searchWikiFullText(String keyWord)
    {
        try
        {
            wikiClient.login(this.getWikiURL());
            return wikiClient.searchFullText(keyWord);
        }
        catch (IOException e)
        {
            handleException(e);
        }
        return Collections.EMPTY_LIST;

    }

    /**
     * Gets the main processes that use the input subprocess.
     * 
     * @param subprocessID not null subprocessID.
     * @return {@link List} of main processes.
     */
    public List<Process> getSubprocessReferences(String subprocessID)
    {
        Validate.notNull(subprocessID);
        try
        {
            return this.processService.getSubprocessReferences(subprocessID).getItem();
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException("Error by getSubprocessReferences " + subprocessID);
    }

    /**
     * Checks if a process is subprocess.
     * 
     * @param processID the not null ID of a process.
     * @return true if the process is a subprocess.
     */
    public boolean isSubprocess(String processID)
    {
        Validate.notNull(processID);
        try
        {
            return this.processService.isSubProcess(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        throw new IllegalStateException("Error by isSubprocess " + processID);
    }

    /**
     * 
     * Checks whether the activity is manual or not
     * 
     * @param activityId not null
     * @return the old 0 or 1 game
     */
    public Boolean isActivityManual(String activityId)
    {
        Validate.notNull(activityId);

        try
        {
            return this.editorService.isActivityManual(activityId);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException("Error by isActivityManual " + activityId);
    }

    /**
     * Get the alfrescoUUID for the process with the given id.<br/>
     * The alfresco UUID is an id which identifies all versions of the same process
     * 
     * @param processId the id of the process. not null.
     * @return never null. Each process must have an uuid
     */
    public String getAlfrescoProcessModelUuid(String processId)
    {
        Validate.notNull(processId);
        return getLinkDocumentID(getProcessInformationID(processId));

    }

    /**
     * 
     * Clone the given process template id. The new process is a complete clone of the old process. <br>
     * 
     * @param templateID id of given process. Not null.
     * @param newProcessName Name of given new process. Not null.
     * @param newPrcessDescription Description of new process. Not null.
     * @return <code>true</code> if the action is successful, else <code>false</code>
     */
    public boolean cloneProcess(String templateID, String newProcessName, String newPrcessDescription)
    {
        Validate.notNull(templateID, "ID of given process is null.");
        Validate.notNull(newProcessName, "Name to set for new process is null.");
        Validate.notNull(newPrcessDescription, "Description to set for new process is null.");

        try
        {
            return this.processService.cloneProcess(templateID, newProcessName, newPrcessDescription);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        throw new IllegalStateException("Error by cloneProcess " + templateID);

    }

    /**
     * 
     * Sends an email message with given content to all receivers.
     * 
     * @param content the content of the message
     * @param receiver the list of receivers of the address
     * @param subject the subject of the message
     * @param footer the footer of the message
     */
    public void sendMessage(String content, String[] receiver, String subject, String footer)
    {
        this.adminService.sendMessage(content, receiver, subject, footer);
    }

    /**
     * 
     * {@link KnowledgeRemote#getRelationsOfKnowledgeObject(String)}.
     * 
     * @param knowledgeObjectID {@link KnowledgeRemote#getRelationsOfKnowledgeObject(String)}
     * @return {@link KnowledgeRemote#getRelationsOfKnowledgeObject(String)}
     */
    public ObjectArray getRelationsOfKnowledgeObject(String knowledgeObjectID)
    {
        Validate.notNull(knowledgeObjectID, "ID of knowledge object can not be null!");

        try
        {
            return this.knowledgeService.getRelationsOfKnowledgeObject(knowledgeObjectID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }

        throw new IllegalStateException("Error by getRelationsOfKnowledgeObject " + knowledgeObjectID);
    }

    /**
     * 
     * Sets a {@link Process} as landscape or not depend on flag.
     * 
     * @param processID id of {@link Process}. Not null
     * @param flag <code>true</code> to set a {@link Process} as landscape, <code>false</code> to set it back.
     * 
     * @return <code>true</code> if the command is successfully, else <code>false</code>
     */
    public boolean setProcessLandscapeFlag(String processID, boolean flag)
    {
        Validate.notNull(processID, "processID can not be null!");
        Validate.notNull(flag, "flag can not be null!");

        try
        {
            return this.editorService.setProcessLandscapeFlag(processID, flag);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException("Error by setProcessLandscapeFlag " + processID);
    }

    /**
     * 
     * Check if the {@link Process} is defied as process landscape.
     * 
     * @param processID ID of given {@link Process}. Not null.
     * @return <code>true</code> if the {@link Process} is landscape, else <code>false</code>
     */
    public boolean isProcessLandscape(String processID)
    {
        Validate.notNull(processID, "processID can not be null!");

        try
        {
            return this.processService.isProcessLandscape(processID);
        }
        catch (OntologyErrorException e)
        {
            handleException(e);
        }
        catch (Exception e)
        {
            handleException(e);
        }
        throw new IllegalStateException("Error by isProcessLandscape " + processID);
    }

}
