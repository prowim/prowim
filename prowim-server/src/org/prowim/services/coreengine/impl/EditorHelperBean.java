/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-14 13:29:20 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/EditorHelperBean.java $
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
package org.prowim.services.coreengine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.alfresco.webservice.authoring.VersionResult;
import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentIdentification;
import org.prowim.datamodel.editor.InstancePropertiesMap;
import org.prowim.datamodel.editor.InstancePropertiesNames;
import org.prowim.datamodel.editor.InstancePropertiesValues;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.dms.alfresco.ContentService;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.DocumentManagementHelper;
import org.prowim.services.coreengine.EditorEngine;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EditorHelper;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.WorkflowEngine;
import org.prowim.services.coreengine.EngineConstants.Slots;
import org.prowim.services.coreengine.entities.OrganizationEntity;
import org.prowim.services.ejb.editor.EditorRemote;

import de.ebcot.tools.logging.Logger;


/**
 * Helper used to execute the algernon rules related to the model editor.
 * 
 * @author Saad Wardi
 * @version $Revision: 5075 $
 */
@Stateless
public class EditorHelperBean implements EditorHelper
{
    private static final Logger                              LOG = Logger.getLogger(EditorHelperBean.class);
    private static HashMap<String, HashMap<String, Integer>> instanceIndexMap;

    @IgnoreDependency
    @EJB
    private EditorEngine                                     editorEngine;

    @IgnoreDependency
    @EJB
    private ProcessEngine                                    processEngine;

    @IgnoreDependency
    @EJB
    private CommonEngine                                     commonEngine;

    @IgnoreDependency
    @EJB
    private CommonHelper                                     common;

    @IgnoreDependency
    @EJB
    private OrganizationEntity                               organizationEntity;

    @IgnoreDependency
    @EJB
    private ProcessHelper                                    processHelper;

    @IgnoreDependency
    @EJB
    private WorkflowEngine                                   workflowEngine;

    @IgnoreDependency
    @EJB
    private ContentService                                   contentService;

    @IgnoreDependency
    @EJB
    private DocumentManagementHelper                         documentManagement;

    /**
     * Constructor.
     */
    public EditorHelperBean()
    {
        instanceIndexMap = new HashMap<String, HashMap<String, Integer>>();
    }

    private static int getInstanceIndex(String modelID, String element)
    {
        Integer elementIndex = 0;
        HashMap<String, Integer> elementIndexMap = instanceIndexMap.get(modelID);
        if (elementIndexMap == null)
        {
            elementIndexMap = new HashMap<String, Integer>();
            elementIndexMap.put(element, elementIndex);
            instanceIndexMap.put(modelID, elementIndexMap);
        }
        else
        {
            elementIndex = elementIndexMap.get(element);
            if (elementIndex == null)
            {
                elementIndex = 0;
                elementIndexMap.put(element, elementIndex);
            }
            else
            {
                elementIndex++;
                elementIndexMap.put(element, elementIndex);
            }
        }
        int result = elementIndex.intValue();
        return result;
    }

    @SuppressWarnings("unused")
    private void incrementInstanceIndex(String modelID, String element)
    {
        HashMap<String, Integer> elementIndexMap = instanceIndexMap.get(modelID);
        Integer elementIndex = elementIndexMap.get(element);
        elementIndexMap.put(element, elementIndex++);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#createObject(java.lang.String, java.lang.String)
     */
    public String createObject(String modelID, String oid) throws OntologyErrorException
    {
        LOG.debug(" Model ID : " + modelID);
        String result = "";
        /** 1. test the validity (local or global) of the class. */

        /** 2. test if it is a model class [Prozess, Risikobereich, etc .. ]. */
        if (modelID.equals(EditorEngineConstants.Consts.MODEL))
        {
            if (testModelClass(oid))
            {
                result = addInstance(oid, 0);
            }
            else
            {
                throw new IllegalArgumentException("The model with name " + oid + " is not supported");
            }
        }
        else
        {
            result = addInstance(oid, EditorHelperBean.getInstanceIndex(modelID, oid));
            setElementToModel(modelID, result);
        }
        if ( !testScope(oid))
        {
            setScope(result, "Local");
        }

        return result;
    }

    /**
     * Creates an object without assign it to a process.
     * 
     * @param oid ID of the object class "Activity, Role, Mean etc". Not null.
     * @param name the instance name. Not null.
     * @param isGlobal the visibility of the instance. Not null.
     * @return instance ID.
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     */
    public String createObject(String oid, String name, boolean isGlobal) throws OntologyErrorException

    {
        Validate.notNull(oid);
        Validate.notNull(name);
        Validate.notNull(isGlobal);

        String result = "";
        result = addInstance(oid, name);

        if (isGlobal)
        {
            setScope(result, "Global");
        }
        else
        {
            setScope(result, "Local");
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#deleteInstance(java.lang.String)
     */
    public void deleteInstance(String instanceID) throws OntologyErrorException
    {
        String status = AlgernonConstants.OK;
        String directClass = common.getDirectClassOfInstance(instanceID);
        if (directClass.equals(Relation.Models.PROZESS))
        {
            if (processEngine.isProcessTemplate(instanceID))
            {
                try
                {
                    String processInformationID = generateProcessInformationForModel(instanceID);
                    contentService.deleteDocument(processInformationID);
                }
                catch (Exception e)
                {
                    LOG.error("Could not delete graphic for process " + instanceID);
                }

                // delete all versions of process
                ProcessArray processVersions = processHelper.getProcessVersions(instanceID);
                for (Iterator iterator = processVersions.iterator(); iterator.hasNext();)
                {
                    org.prowim.datamodel.prowim.Process process = (org.prowim.datamodel.prowim.Process) iterator.next();
                    processEngine.deleteProcessInstances(process.getTemplateID());
                    status = processEngine.deleteProcess(process.getTemplateID()).getResult();
                    if ( !status.equals(AlgernonConstants.OK))
                    {
                        throw new IllegalStateException("Could not delete instance ! id = " + process.getTemplateID());
                    }
                }
            }

            processEngine.deleteProcessInstances(instanceID);
            status = processEngine.deleteProcess(instanceID).getResult();
            if ( !status.equals(AlgernonConstants.OK))
            {
                throw new IllegalStateException("Could not delete instance ! id = " + instanceID);
            }

        }
        else
        {
            status = editorEngine.deleteInstance(instanceID).getResult();
        }

        if ( !status.equals(AlgernonConstants.OK))
        {
            throw new IllegalStateException("Could not delete instance ! id = " + instanceID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getInstanceRelations(java.lang.String)
     */
    @Override
    public InstancePropertiesMap getInstanceRelations(String id) throws OntologyErrorException
    {
        RecordSet[] records = commonEngine.getInstanceRelations(id);
        InstancePropertiesNames namesResult = null;
        InstancePropertiesValues valuesResult = null;

        List<String> namesList = new ArrayList<String>();
        List<String> valuesList = new ArrayList<String>();
        if (records[1].getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records[1].getNoOfRecords(); i++)
            {
                String key = records[1].getRecords()[i].get(EditorEngineConstants.Variables.Common.RELATION);
                String value = records[1].getRecords()[i].get(EditorEngineConstants.Variables.Common.VALUE);

                namesList.add(key);
                valuesList.add(value);
                LOG.debug("RECORDS [1]:   " + key);
            }
        }

        if (records[0].getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records[0].getNoOfRecords(); i++)
            {
                String key = records[0].getRecords()[i].get(EditorEngineConstants.Variables.Common.RELATION);
                String value = records[0].getRecords()[i].get(EditorEngineConstants.Variables.Common.VALUE);
                LOG.debug("RECORDS [0]:  " + key);
                namesList.add(key);
                valuesList.add(value);
            }

        }
        String[] names = new String[namesList.size()];
        String[] values = new String[valuesList.size()];
        Object[] namesObjects = namesList.toArray();
        Object[] valuesObjects = valuesList.toArray();
        for (int i = 0; i < namesObjects.length; i++)
        {
            names[i] = (String) namesObjects[i];
            values[i] = (String) valuesObjects[i];
        }
        namesResult = new InstancePropertiesNames(names);
        valuesResult = new InstancePropertiesValues(values);
        InstancePropertiesMap result = new InstancePropertiesMap(namesResult, valuesResult);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getInstanceRelationsValues(java.lang.String)
     */
    @Override
    public InstancePropertiesValues getInstanceRelationsValues(String id)
    {
        // RecordSet[] records = editorEngine.getInstanceRelations(id);
        // InstancePropertiesValues result = null;
        // if (records.getResult().equals(AlgernonConstants.OK))
        // {
        // String[] values = new String[records.getNoOfRecords()];
        // for (int i = 0; i < records.getNoOfRecords(); i++)
        // {
        // String value = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.VALUE);
        // values[i] = value;
        // LOG.debug("Relation value:  " + value);
        // }
        // result = new InstancePropertiesValues(values);
        // }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setElementToModel(java.lang.String, java.lang.String)
     */
    @Override
    public void setElementToModel(String modelID, String elementID) throws OntologyErrorException
    {
        /** 1. Get the direct class. */
        String classID = common.getDirectClassOfInstance(modelID);

        /** 2. Get the slot to this model. */
        String slot = this.getModelSlot(classID);

        /** 3. Set the element to the model. */
        editorEngine.setElementToModel(slot, modelID, elementID);

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#isProcessApproved(java.lang.String)
     */
    @Override
    public boolean isProcessApproved(String modelID) throws OntologyErrorException
    {
        RecordSet records = editorEngine.isProcessApproved(modelID);

        if (records.getResult().equals(AlgernonConstants.OK))
        {
            String start = records.getSingleResult(ProcessEngineConstants.Variables.Common.VALUE);
            if (start != null && !start.equals(""))
            {
                return true;
            }

            return false;
        }
        else if (records.getResult().equals(AlgernonConstants.FAILED))
        {
            return false;
        }
        else
        {
            throw new IllegalStateException("could not retrieve START slot!");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#approveProcess(java.lang.String)
     */
    @Override
    public void approveProcess(String modelID) throws OntologyErrorException
    {
        editorEngine.approveProcess(modelID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#disapproveProcess(java.lang.String)
     */
    @Override
    public void disapproveProcess(String modelID) throws OntologyErrorException
    {
        editorEngine.disapproveProcess(modelID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getModels()
     */
    @Override
    public String[] getModels() throws OntologyErrorException
    {
        return getProcessModels();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#saveModelAsXML(java.lang.String, java.lang.String, java.lang.String, boolean, String)
     */
    @Override
    public String saveModelAsXML(String modelID, String xml, String username, boolean createNewVersion, String versionName) throws DMSException,
                                                                                                                           OntologyErrorException
    {
        Validate.isTrue(( !createNewVersion && versionName == null) || (createNewVersion && versionName != null));

        /**
         * 1. Get the ID of the processinformation instance where the UUID of the DMS have to be stored <br>
         * or generate a new instance if no one exists for this process.
         */
        String processInformationID = generateProcessInformationForModel(modelID);
        Validate.notNull(processInformationID);

        Document document = new Document(generateContentNameForModel(modelID), EditorEngineConstants.Consts.CONTENT_FORMAT_FOR_XML, xml.getBytes());
        /** 2. check if the processinformation stores already a DMS ID. IF yes : call updatedocument if no call writecontent. */

        if ( !checkDMSIDforProcessInformation(processInformationID))
        {
            /** Store a new content for this model in the DMS. */
            LOG.debug("saveModelAsXML  UUID does not exists -> create new chart in DMS for the model :  " + modelID);
            contentService.writeContent(xml.getBytes(), document.getName(), document.getContentType(), processInformationID, username);
            // initial is always active version
            workflowEngine.setAsActiveVersion(modelID);
        }
        else
        {
            String newProcessVersionID = null;

            LOG.debug("Updating XML for model with ID:  " + modelID);

            // if a new version will be created, we have to parse the xml and set the new IDs to it after storing the new process template
            if (createNewVersion)
            {
                // create a new version in the ontology
                newProcessVersionID = processHelper.createNewVersion(modelID, versionName, "Dummyversion");

                // map the xml elements to the newly created process elements
                String newXmlString = processHelper.mapXMLElements(newProcessVersionID, xml);

                // Create new version in alfresco of the current xml
                DocumentIdentification documentIdentification = documentManagement.getDocumentIdentification(processInformationID);

                // remove the created state from the old xml stored
                String oldXml = processHelper.removeActivatedStateFromXML(xml);

                // Update the content for the old process in the DMS with the xml where the style is removed
                contentService.updateContent(oldXml.getBytes(), document.getName(), document.getContentType(), processInformationID, username, false);
                VersionResult versionResultNewVersion = contentService.createNewVersion(document.getName(), username,
                                                                                        documentIdentification.getUuid());

                // After storing the new version in the alfresco, store the alfresco version in the process template which has been versioned (not the new one!)
                commonEngine.clearRelationValue(modelID, Slots.ALFRESCO_VERSION);
                processHelper.setSlotValue(modelID, Slots.ALFRESCO_VERSION,
                                           versionResultNewVersion.getVersions(versionResultNewVersion.getVersions().length - 1).getLabel());

                // Update the content for the process in the DMS with the mapped xml
                contentService.updateContent(newXmlString.getBytes(), document.getName(), document.getContentType(), processInformationID, username,
                                             false);

                return newProcessVersionID;
            }
            else
            {
                // Update the content for the process in the DMS.
                contentService.updateContent(xml.getBytes(), document.getName(), document.getContentType(), processInformationID, username, false);
            }
        }

        return null;
    }

    /**
     * Description.
     * 
     * @param processInformationID
     * @return
     */
    private boolean checkDMSIDforProcessInformation(String processInformationID) throws OntologyErrorException
    {
        RecordSet isInformationType = editorEngine.isInformationType(processInformationID);
        if (isInformationType.getResult().equals(AlgernonConstants.OK))
        {
            RecordSet dmsIDRecords = editorEngine.checkDMSIDforProcessInformation(processInformationID);
            if (dmsIDRecords.getResult().equals(AlgernonConstants.OK))
            {
                String dmsID = dmsIDRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.VALUE);
                if (dmsID != null)
                {
                    LOG.debug("The UUID IS: " + dmsID);
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#loadModelAsXML(java.lang.String)
     */
    @Override
    public String loadModelAsXML(String modelID) throws DMSException, OntologyErrorException
    {
        String result = EditorEngineConstants.Consts.EMPTY_STRING;
        String processInformationID = processHelper.getChartProcessInformationID(modelID);
        String alfrescoVersion = processHelper.getAlfrescoVersion(modelID);

        if (alfrescoVersion == null)
        {
            throw new IllegalStateException("Could not find correct alfresco version for process model with ID " + modelID);
        }

        Document document = null;
        if (alfrescoVersion.equals(EngineConstants.Consts.EDITABLE_ALFRESCO_VERSION_LABEL))
        {
            document = contentService.downloadDocument(processInformationID);
        }
        else
        {
            document = contentService.downloadDocumentInVersion(processInformationID, alfrescoVersion);
        }

        if (document != null)
        {
            byte[] contentBytes = document.getContent();
            result = new String(contentBytes);
        }
        else
        {
            result = EditorEngineConstants.Consts.NOT_AVAILABLE;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#connectActivityControlFlow(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void connectActivityControlFlow(String sourceID, String targetID, String controlFlowID) throws OntologyErrorException
    {
        editorEngine.connectActivityControlFlow(sourceID, targetID, controlFlowID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#connectActivityRole(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void connectActivityRole(String activityID, String roleID, String taskID) throws OntologyErrorException
    {
        editorEngine.connectActivityRole(activityID, roleID, taskID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getRelations(java.lang.String, java.lang.String)
     */
    @Override
    public String[] getRelations(String sourceID, String targetID) throws OntologyErrorException
    {
        return this.getTheRelations(sourceID, targetID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setRelationValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setRelationValue(String instanceID, String slot, String relationID) throws OntologyErrorException
    {
        editorEngine.setRelationValue(instanceID, slot, relationID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setProduct(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setProduct(String source, String target, String productID) throws OntologyErrorException
    {
        editorEngine.setProduct(source, target, productID);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#removeRelationValue(java.lang.String, java.lang.String)
     */
    @Override
    public void removeRelationValue(String instanceID, String slot) throws OntologyErrorException
    {
        editorEngine.removeRelationValue(instanceID, slot);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getCombinationRules()
     */
    @Override
    public String[] getCombinationRules() throws OntologyErrorException
    {
        final RecordSet combinationRulesRecords = editorEngine.getCombinationRules();
        if (combinationRulesRecords.getResult().equals(AlgernonConstants.OK))
        {
            String[] rules = new String[combinationRulesRecords.getNoOfRecords()];
            for (int i = 0; i < combinationRulesRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> combinationRulesRecord = combinationRulesRecords.getRecords()[i];
                if (combinationRulesRecord != null)
                {
                    String ruleName = combinationRulesRecord.get(EditorEngineConstants.Variables.Common.NAME);
                    rules[i] = ruleName;
                }
            }
            return rules;

        }
        else
        {
            return new String[0];
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setCombinationRule(java.lang.String, java.lang.String)
     */
    @Override
    public void setCombinationRule(String controlFlowID, String combinationRule) throws OntologyErrorException
    {
        LOG.debug("setCombinationRule:    " + controlFlowID + "   " + combinationRule);
        commonEngine.clearRelationValue(controlFlowID, "hat_Verknüpfungsregel");
        editorEngine.setCombinationRule(controlFlowID, combinationRule);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getActivationRules()
     */
    @Override
    public String[] getActivationRules() throws OntologyErrorException
    {
        final RecordSet activationRulesRecords = editorEngine.getCombinationRules();
        if (activationRulesRecords.getResult().equals(AlgernonConstants.OK))
        {
            String[] rules = new String[activationRulesRecords.getNoOfRecords()];
            for (int i = 0; i < activationRulesRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> activationRulesRecord = activationRulesRecords.getRecords()[i];
                if (activationRulesRecord != null)
                {
                    String ruleName = activationRulesRecord.get(EditorEngineConstants.Variables.Common.NAME);
                    rules[i] = ruleName;
                }
            }
            return rules;

        }
        else
        {
            return new String[0];
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setActivationRule(java.lang.String, java.lang.String)
     */
    @Override
    public void setActivationRule(String objectID, String activationRule) throws OntologyErrorException
    {
        Validate.notNull(objectID);
        Validate.notNull(activationRule);
        editorEngine.setActivationRule(objectID, activationRule);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#connectActivityMittel(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void connectActivityMittel(String activityID, String mittelID, String functionID) throws OntologyErrorException
    {
        editorEngine.connectActivityMittel(activityID, mittelID, functionID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getOrganizationalUnits()
     */
    @Override
    public String[] getOrganizationalUnits() throws OntologyErrorException
    {
        OrganisationArray organizations = organizationEntity.getOrganizations();
        String[] result = new String[organizations.size()];
        Iterator<Organization> it = organizations.iterator();
        int i = 0;
        while (it.hasNext())
        {
            String organisationName = it.next().getName();
            result[i++] = organisationName;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getCombinationRule(java.lang.String)
     */
    @Override
    public String getCombinationRule(String controlflowID) throws OntologyErrorException
    {
        String result = EditorEngineConstants.Consts.EMPTY_STRING;
        RecordSet combinationRuleRecords = editorEngine.getCombinationRule(controlflowID);
        if (combinationRuleRecords.getResult().equals(AlgernonConstants.OK))
        {
            String ruleName = combinationRuleRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.COMBINATION_RULE);
            result = ruleName;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#bendControlFlow(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String bendControlFlow(String controlflowID, String oldSource, String oldTarget, String newSource, String newTarget)
                                                                                                                               throws OntologyErrorException
    {
        if (oldSource.equals(newSource))
        {
            commonEngine.clearRelationValue(controlflowID, Relation.Slots.SET_ACTIVE);
            this.connectActivityControlFlow(newSource, newTarget, controlflowID);
        }
        return "OK";
    }

    /************************ PRIVATE METHODS ***************/

    /**
     * Description.
     * 
     * @param sourceID
     * @param targetID
     * @return
     */
    private String[] getTheRelations(String sourceID, String targetID) throws OntologyErrorException
    {
        /** 1. get the direct class of the instances. */
        String sourceDirectClass = common.getDirectClassOfInstance(sourceID);
        String targetDirectClass = common.getDirectClassOfInstance(targetID);

        /** 2. get the relations. */
        RecordSet relationsRecords = editorEngine.getRelations(sourceDirectClass, targetDirectClass);
        if (relationsRecords.getResult().equals(AlgernonConstants.OK))
        {
            String[] relations = new String[relationsRecords.getNoOfRecords()];
            for (int i = 0; i < relationsRecords.getNoOfRecords(); i++)
            {
                Hashtable<String, String> modelRecord = relationsRecords.getRecords()[i];
                String name = modelRecord.get(EditorEngineConstants.Variables.Common.RELATION);
                relations[i] = name;
            }
            return relations;
        }
        else
        {
            return new String[0];
        }
    }

    /**
     * Generates the content name for the model with id = modelID.
     * 
     * @param modelID the not null modelID.
     * @return not null content name.
     */
    private String generateContentNameForModel(String modelID)
    {
        return modelID + EditorEngineConstants.Consts.MODEL_DMS_CONTENT_FILE_EXTENSION;
    }

    /**
     * {@link EditorRemote#getProcessModels}.
     * 
     * @return the array that contains the items: modelID + EditorEngineConstants.Consts.ID_NAME_SPLIT_TOKEN + name.
     */
    private String[] getProcessModels() throws OntologyErrorException
    {
        final RecordSet modelsRecord = editorEngine.getModels();

        if (modelsRecord.getResult().equals(AlgernonConstants.OK))
        {
            String[] models = new String[modelsRecord.getNoOfRecords()];
            for (int i = 0; i < modelsRecord.getNoOfRecords(); i++)
            {
                Hashtable<String, String> modelRecord = modelsRecord.getRecords()[i];
                String id = modelRecord.get(EditorEngineConstants.Variables.Common.ID);
                String name = modelRecord.get(EditorEngineConstants.Variables.Common.NAME);
                models[i] = id + EditorEngineConstants.Consts.ID_NAME_SPLIT_TOKEN + name;
            }
            return models;
        }
        else
        {
            return new String[0];
        }
    }

    /**
     * Tests the scope of an object before create its instance.
     * 
     * @param oid not null object ID.
     * @return true if it is valide, false otherwise.
     */
    private boolean testScope(String oid) throws OntologyErrorException
    {
        LOG.debug("test the validity for the object   " + oid);
        boolean result = false;
        RecordSet testScopeRecords = editorEngine.testScope(oid);
        if (testScopeRecords.getResult().equals(AlgernonConstants.OK))
        {
            String scope = testScopeRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.SCOPE);
            if (scope != null)
            {
                result = !scope.equals(EditorEngineConstants.Consts.VALIDITY);
            }
        }
        LOG.debug("test the validity for the object  returns  " + result);
        return result;
    }

    /**
     * Tests the scope of an object before create its instance.
     * 
     * @param instanceID not null instance ID.
     * @param scope not null scope : Local or Global values are permitted.
     * @throws OntologyErrorException if an ontology error occurs in the ontology backend
     */
    public void setScope(String instanceID, String scope) throws OntologyErrorException
    {
        Validate.notNull(instanceID);
        Validate.notNull(scope);
        LOG.debug("Set the validity for the instance   " + instanceID);
        RecordSet setScopeRecords = editorEngine.setScope(instanceID, scope);
        if (setScopeRecords.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("The validity for the instance " + instanceID + " is set to  " + scope);
        }
        else
        {
            LOG.debug("Could not set the validity for the instance  " + instanceID + " to " + scope);
            throw new IllegalStateException("Could not set the validity for the instance  " + instanceID + " to " + scope);
        }

    }

    /**
     * {@link EditorEngine#getTypeID(String)}.
     * 
     * @param oid not null object ID.
     * @return the object type.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @SuppressWarnings("unused")
    private String getTypeID(String oid) throws OntologyErrorException
    {
        String result = null;
        RecordSet testScopeRecords = editorEngine.getTypeID(oid);
        if (testScopeRecords.getResult().equals(AlgernonConstants.OK))
        {
            String id = testScopeRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.ID);
            if (id != null)
            {
                if (id.equals(EditorEngineConstants.Consts.TYPE_COMPONENT))
                {
                    result = EditorEngineConstants.Consts.COMPONENT;
                }
                else if (id.equals(EditorEngineConstants.Consts.TYPE_CONNECTION))
                {
                    result = EditorEngineConstants.Consts.CONNECTION;
                }
                else
                {
                    throw new IllegalArgumentException("Selected Object is not valid " + oid);
                }
            }
        }
        LOG.debug(" Type ID for the object   " + oid + " :  " + result);
        return result;
    }

    /**
     * {@link EditorEngine#isFrame(String)}.
     * 
     * @param oid not null object ID.
     * @return true if the object is frame, false otherwise.
     * @throws OntologyErrorException if an error occurs in onotolgy back end
     */
    private boolean isFrame(String oid) throws OntologyErrorException
    {
        LOG.debug("test isFrame for the object   " + oid);
        boolean result = false;
        RecordSet testScopeRecords = editorEngine.testScope(oid);
        result = testScopeRecords.getResult().equals(AlgernonConstants.OK);
        LOG.debug("test isFrame for the object  returns  " + result);
        return result;
    }

    /**
     * {@link EditorEngine#addInstance(String, String)}.
     * 
     * @param oid object ID.
     * @param name instance name.
     * @return instance ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private String addInstance(String oid, String name) throws OntologyErrorException
    {
        LOG.debug("Create a new instance for the object   " + oid);
        String result = null;
        RecordSet addInstanceRecords = editorEngine.addInstance(oid, name);
        if (addInstanceRecords.getResult().equals(AlgernonConstants.OK))
        {
            String id = addInstanceRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.ID);
            result = id;
        }
        LOG.debug("Created object has ID :   " + result);
        return result;
    }

    /**
     * Adds an instance into the ontology.
     * 
     * @param oid the selected object ID.
     * @param index the instance index.
     * @return the instance ID.
     * @throws OntologyErrorException if an error occurs in the ontolgy back end
     */
    private String addInstance(String oid, int index) throws OntologyErrorException
    {
        if (isFrame(oid))
        {

            String name = oid + index;
            if (oid.equals("Kontrollfluss"))
            {
                name = "K" + index;
            }
            return addInstance(oid, name);
        }
        else
        {
            throw new IllegalArgumentException("The object is not a frame " + oid);
        }
    }

    /**
     * Gets the slot name where the element must be added.
     * 
     * @param classID the ID of the model class. For example Prozess for process management.
     * @return not null model slot.
     */
    private String getModelSlot(String classID)
    {
        String modelSlot = null;
        if (classID.equals(Relation.Models.PROZESS))
            modelSlot = Relation.Slots.BESTEHT_AUS;
        else if (classID.equals(Relation.Models.TOPOPORTFOLIO))
            modelSlot = Relation.Slots.BESTEHT_AUS_TOPELEMENT;
        else if (classID.equals(Relation.Models.RISIKOBEREICH))
            modelSlot = Relation.Slots.HAT_RISIKOELEMENT;
        else if (classID.equals(Relation.Models.PRODUCTFAMILIE))
            modelSlot = Relation.Slots.HAT_ANWENDUNGSELEMENT;
        else if (classID.equals(Relation.Models.ANWENDUNGSBEREICH))
            modelSlot = Relation.Slots.BESTHET_AUS_ANFORDERUNGSELEMENT;
        else
        {
            throw new IllegalArgumentException("Model class not known  " + classID);
        }
        return modelSlot;
    }

    /**
     * Checks if the given class is a model and is suported at the runtime.
     * 
     * @param oid the class ID.
     * @return true if the oid = classID is registered, false otherwise.
     */
    private boolean testModelClass(String oid)
    {
        boolean threeFirstModel = (oid.equals(Relation.Models.PROZESS)) || (oid.equals(Relation.Models.TOPOPORTFOLIO))
                || (oid.equals(Relation.Models.RISIKOBEREICH)) || (oid.equals(Relation.Models.PRODUCTFAMILIE));
        return threeFirstModel || (oid.equals(Relation.Models.ANWENDUNGSBEREICH) || oid.equals("Webservice"));
    }

    /**
     * {@link AlgernonServiceWrapper#generateProcessInformation(String)}.
     * 
     * @param modelID the not null model ID.
     * @return the not null ID of the generated processinformation.
     * @throws OntologyErrorException if an error in ontology back end occurs
     */
    private String generateProcessInformationForModel(String modelID) throws OntologyErrorException
    {
        String processInformationID = processHelper.generateProcessInformation(modelID);
        common.setDescription(processInformationID, EditorEngineConstants.Consts.PROCESS_INFORMATION_UUID_DESCRIPTION);
        /** add the generated processinformation to the model. */
        setElementToModel(modelID, processInformationID);
        return processInformationID;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getControlFlowCount(java.lang.String)
     */
    public String getControlFlowCount(String targetID) throws OntologyErrorException
    {
        final RecordSet controlFlowsRecord = editorEngine.getControlFlowsCount(targetID);
        if (controlFlowsRecord.getResult().equals(AlgernonConstants.OK))
        {
            return controlFlowsRecord.getRecords()[0].get(EditorEngineConstants.Variables.Common.COUNT);
        }
        else
        {
            throw new IllegalStateException("Could not get the count of controlflows for activity with id = " + targetID);
        }
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#getAllExistSubProcesses()
     */
    public ProcessArray getAllExistSubProcesses() throws OntologyErrorException
    {
        final ProcessArray processArray = new ProcessArray();
        final RecordSet subProcesses = this.processEngine.getAllExistSubProcesses();
        if (subProcesses.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < subProcesses.getNoOfRecords(); i++)
            {
                Hashtable<String, String> modelRecord = subProcesses.getRecords()[i];
                String id = modelRecord.get(EngineConstants.Variables.Process.SUB_PROCESS_ID);
                String name = modelRecord.get(EngineConstants.Variables.Common.NAME);
                Process process = DefaultDataObjectFactory.createProwimProcessTemplate(id, name);
                processArray.add(process);
            }
        }

        return processArray;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setSubProcessFlagForActivity(java.lang.String)
     */
    public void setSubProcessFlagForActivity(String activityID) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        RecordSet setScopeRecords = editorEngine.setSubProcessFlagForActivity(activityID);
        if ( !setScopeRecords.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("Could not set the flag for for sub process for activity  " + activityID);
            throw new IllegalStateException("Could not set the flag for for sub process for activity  " + activityID);
        }

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setSubProcessFlagForProcess(java.lang.String)
     */
    public void setSubProcessFlagForProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        RecordSet setScopeRecords = editorEngine.setSubProcessFlagForProcess(processID);
        if ( !setScopeRecords.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("Could not set the flag for for sub process for process  " + processID);
            throw new IllegalStateException("Could not set the flag for for sub process for process  " + processID);
        }

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setSubProcessOfActivity(java.lang.String, java.lang.String)
     */
    public void setSubProcessOfActivity(String subProcessID, String activityID) throws OntologyErrorException
    {
        Validate.notNull(subProcessID);
        Validate.notNull(activityID);
        editorEngine.setSubProcessOfActivity(subProcessID, activityID);

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setActivityOperation(java.lang.String, java.lang.String)
     */
    public void setActivityOperation(String activityID, String flag) throws OntologyErrorException
    {
        Validate.notNull(activityID);
        Validate.notNull(flag);

        this.editorEngine.setActivityOperation(activityID, flag);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#deleteElementFromModel(java.lang.String, java.lang.String)
     */
    public void deleteElementFromModel(String processID, String elementID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        Validate.notNull(elementID);

        RecordSet setScopeRecords = editorEngine.deleteElementFromModel(processID, elementID);
        if ( !setScopeRecords.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("Could not delete the realtion between model " + processID + " and element  " + elementID);
            throw new IllegalStateException("Could not delete the realtion between model " + processID + " and element  " + elementID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.EditorHelper#setProcessLandscapeFlag(java.lang.String, boolean)
     */
    @Override
    public boolean setProcessLandscapeFlag(String processID, boolean flag) throws OntologyErrorException
    {
        Validate.notNull(processID);
        Validate.notNull(flag);

        RecordSet recordSet = editorEngine.setProcessLandscapeFlag(processID, flag);
        return recordSet.getResult().equals(AlgernonConstants.OK);
    }
}
