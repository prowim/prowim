/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-03-08 09:19:55 +0100 (Di, 08 Mrz 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/impl/CommonHelperBean.java $
 * $LastChangedRevision: 5055 $
 *------------------------------------------------------------------------------
 * (c) 01.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.impl;

import java.util.Hashtable;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.InstancePropertyArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.editor.InstancePropertiesNames;
import org.prowim.datamodel.editor.InstancePropertiesValues;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.InstanceProperty;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.entities.impl.DefaultEntityManager;
import org.prowim.services.ejb.commons.CommonRemote;

import de.ebcot.tools.logging.Logger;


/**
 * Provides methods, that can be shared from the other helper classes.<br>
 * Such methods: rename, set/Get descriptions, getProperties, etc.
 * 
 * Those functionalites are used i.e. from {@link CommonRemote}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5055 $
 */
@Stateless
public class CommonHelperBean implements CommonHelper
{
    private static final Logger LOG = Logger.getLogger(CommonHelperBean.class);

    @IgnoreDependency
    @EJB
    private CommonEngine        commonEngine;

    @IgnoreDependency
    @EJB
    private ProcessEngine       processEngine;

    @IgnoreDependency
    @EJB
    private ProcessHelper       processHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#setDenotation(java.lang.String)
     */
    @Override
    public void setDenotation(final String frameID) throws OntologyErrorException
    {
        commonEngine.clearRelationValue(frameID, Relation.Slots.DENOTATION);
        commonEngine.setSlotValue(frameID, Relation.Slots.DENOTATION, this.getName(frameID));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getName(java.lang.String)
     */
    @Override
    public String getName(final String id) throws OntologyErrorException
    {
        String result = null;
        RecordSet testScopeRecords = commonEngine.getName(id);
        if (testScopeRecords.getResult().equals(AlgernonConstants.OK))
        {
            String name = testScopeRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.NAME);
            if (name != null)
            {
                result = name;
            }
            else
            {
                throw new IllegalArgumentException("Selected instance has no valide display name " + id);
            }

        }
        LOG.debug(" Name of the object with id =  " + id + " :  " + result);
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#setDescription(java.lang.String, java.lang.String)
     */
    @Override
    public void setDescription(final String id, final String description) throws OntologyErrorException
    {
        commonEngine.setDescription(id, description);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getDescription(java.lang.String)
     */
    @Override
    public String getDescription(final String id) throws OntologyErrorException
    {
        final RecordSet descriptionRecord = commonEngine.getDescription(id);
        if (descriptionRecord.getResult().equals(AlgernonConstants.OK))
        {
            return descriptionRecord.getRecords()[0].get(EditorEngineConstants.Variables.Common.VALUE);
        }
        else
        {
            return EditorEngineConstants.Consts.EMPTY_STRING;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#rename(java.lang.String, java.lang.String)
     */
    @Override
    public void rename(final String id, final String newName) throws OntologyErrorException
    {
        LOG.debug("rename the instance with id : " + id + " the new name is " + newName);
        commonEngine.clearRelationValue(id, Relation.Slots.NAME);
        commonEngine.setSlotValue(id, Relation.Slots.NAME, newName);
        setDenotation(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getTemplateID(java.lang.String)
     */
    @Override
    public String getTemplateID(final String relationID) throws OntologyErrorException
    {
        RecordSet processElementTemplateRecords = processEngine.getTemplateID(relationID);
        String result = relationID;
        if (processElementTemplateRecords != null && processElementTemplateRecords.getResult().equals(AlgernonConstants.OK)
                && processElementTemplateRecords.getNoOfRecords() == 1)
        {
            final Hashtable<String, String> processElementTemplateRecord = processElementTemplateRecords.getRecords()[0];
            final String id = processElementTemplateRecord.get(ProcessEngineConstants.Variables.Common.TEMPLATE_ID_EN);
            if (id != null && !id.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
            {
                result = id;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getProperties(java.lang.String)
     */
    @Override
    public InstancePropertyArray getProperties(final String instanceID) throws OntologyErrorException
    {
        Validate.notNull(instanceID);
        final RecordSet records = commonEngine.getInstanceProperties(instanceID);
        InstancePropertyArray result = new InstancePropertyArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String name = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ATTRIBUTE);
                String value = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.VALUE);
                InstanceProperty property = DefaultDataObjectFactory.createInstanceProperty(name, value);
                result.add(property);
            }

        }
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getRelations(java.lang.String)
     */
    @Override
    public InstancePropertyArray getRelations(final String instanceID) throws OntologyErrorException
    {
        Validate.notNull(instanceID);
        RecordSet[] records = commonEngine.getInstanceRelations(instanceID);
        InstancePropertyArray result = new InstancePropertyArray();

        if (records[1].getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records[1].getNoOfRecords(); i++)
            {
                String name = records[1].getRecords()[i].get(EditorEngineConstants.Variables.Common.RELATION);
                String value = records[1].getRecords()[i].get(EditorEngineConstants.Variables.Common.VALUE);
                InstanceProperty relation = DefaultDataObjectFactory.createInstanceProperty(name, value);
                result.add(relation);
            }
        }

        if (records[0].getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records[0].getNoOfRecords(); i++)
            {
                String name = records[0].getRecords()[i].get(EditorEngineConstants.Variables.Common.RELATION);
                String value = records[0].getRecords()[i].get(EditorEngineConstants.Variables.Common.VALUE);
                InstanceProperty relation = DefaultDataObjectFactory.createInstanceProperty(name, value);
                result.add(relation);

            }

        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#clearRelationValue(java.lang.String, java.lang.String)
     */
    public String clearRelationValue(final String frameID, final String slotID) throws OntologyErrorException
    {
        String result = AlgernonConstants.FAILED;
        RecordSet clearsRecords = processEngine.clearSlot(frameID, slotID);
        result = clearsRecords.getResult();
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#setSlotValue(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException
    {
        return commonEngine.setSlotValue(frameID, slotID, value).getResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see CommonEngine#getSlotValue(String, String)
     */
    public String getSlotValue(String instanceID, String slotID) throws OntologyErrorException
    {
        RecordSet recordSet = commonEngine.getSlotValue(instanceID, slotID);
        String returnValue = null;
        if (recordSet.getResult().equals(AlgernonConstants.OK) && recordSet.getNoOfRecords() == 1)
        {
            returnValue = recordSet.getSingleResult(EngineConstants.Variables.Common.VALUE);
        }

        return returnValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#deleteInstance(java.lang.String)
     */
    public String deleteInstance(final String instanceID) throws OntologyErrorException
    {
        String result = AlgernonConstants.FAILED;
        RecordSet clearsRecords = commonEngine.deleteInstance(instanceID);
        result = clearsRecords.getResult();
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getProcessElements()
     */
    @Override
    public ObjectArray getProcessElements() throws OntologyErrorException
    {
        final RecordSet records = commonEngine.getProcessElements();
        final ObjectArray result = new ObjectArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ID);
                LOG.debug("The Element:  " + records.getRecords()[i]);
                if (DefaultEntityManager.getInstance().containsElement(id))
                {
                    result.add(DefaultEntityManager.getInstance().getDataObjectClassName(id));
                    LOG.debug("Add OBJECT:  " + DefaultEntityManager.getInstance().getDataObjectClassName(id));
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getProcessElementsInstances(java.lang.String, java.lang.String)
     */
    @Override
    public ObjectArray getProcessElementsInstances(final String processID, final String dataObjectClassName) throws OntologyErrorException
    {
        final RecordSet records = commonEngine.getProcessElementsInstances(processID,
                                                                           DefaultEntityManager.getInstance()
                                                                                   .getOntologyClassName(dataObjectClassName));
        final ObjectArray result = new ObjectArray();
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String id = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ID);
                LOG.debug("getProcessElementsInstances :     process :  " + processID + ",   Element :     " + dataObjectClassName);
                Object o = DefaultEntityManager.getInstance().getObject(id);
                LOG.debug("DataObject Classname :    " + o.getClass().getName());
                result.add(o);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getDirectClassOfInstance(java.lang.String)
     */
    public String getDirectClassOfInstance(final String id) throws OntologyErrorException
    {
        final RecordSet directClassRecord = commonEngine.getDirectClassOfInstance(id);
        if (directClassRecord.getResult().equals(AlgernonConstants.OK))
        {
            return directClassRecord.getRecords()[0].get(EditorEngineConstants.Variables.Common.DCID);
        }
        else
        {
            throw new IllegalStateException("Could not get the class id for instance with id = " + id);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getProcessElementTemplateInformation(java.lang.String)
     */
    public ProcessElement getProcessElementTemplateInformation(final String processlementInstanceID) throws OntologyErrorException
    {
        RecordSet processElementTemplateRecords = commonEngine.getProcessElementTemplateInformation(processlementInstanceID);
        ProcessElement processelementTemplate = null;
        if (processElementTemplateRecords.getNoOfRecords() == 1)
        {
            final Hashtable<String, String> processElementTemplateRecord = processElementTemplateRecords.getRecords()[0];
            final String id = processElementTemplateRecord.get(ProcessEngineConstants.Variables.Common.TEMPLATE_ID_EN);
            final String name = processElementTemplateRecord.get(ProcessEngineConstants.Variables.Common.NAME_EN);
            final String createTime = processElementTemplateRecord.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            processelementTemplate = DefaultDataObjectFactory.createProcessElement(id, name, createTime);
        }
        return processelementTemplate;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getOntologyVersion()
     */
    @Override
    public String getOntologyVersion() throws OntologyErrorException
    {
        final RecordSet result = commonEngine.getOntologyVersion();
        String version = AlgernonConstants.ERROR;
        if (result != null && result.getResult().equals(AlgernonConstants.OK) && result.getNoOfRecords() == 1)
        {
            Hashtable<String, String> record = result.getRecords()[0];
            version = record.get(ProcessEngineConstants.Variables.Common.VAR_VERSION);
            return version;
        }
        else
        {
            throw new IllegalStateException("Could not read version from the ontology! ");
        }

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#setParameter(org.prowim.datamodel.collections.ParameterArray)
     */
    public void setParameter(ParameterArray paramArray) throws OntologyErrorException
    {
        Validate.notNull(paramArray);
        ParameterArray parameters = paramArray;
        Iterator<Parameter> itParameters = parameters.iterator();
        while (itParameters.hasNext())
        {
            Parameter parameter = itParameters.next();
            processHelper.setParameterValue(parameter);
        }
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#isGlobal(java.lang.String)
     */
    public boolean isGlobal(String instanceID) throws OntologyErrorException
    {
        Validate.notNull(instanceID);
        return commonEngine.isGlobal(instanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getInstancePropertiesNames(java.lang.String)
     */
    @Override
    public InstancePropertiesNames getInstancePropertiesNames(String id) throws OntologyErrorException
    {
        RecordSet records = commonEngine.getInstanceProperties(id);
        InstancePropertiesNames result = null;
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            String[] keyvalue = new String[records.getNoOfRecords()];
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String key = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.ATTRIBUTE);
                keyvalue[i] = key;
            }
            result = new InstancePropertiesNames(keyvalue);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.CommonHelper#getInstancePropertiesValues(java.lang.String)
     */
    @Override
    public InstancePropertiesValues getInstancePropertiesValues(String id) throws OntologyErrorException
    {
        RecordSet records = commonEngine.getInstanceProperties(id);
        InstancePropertiesValues result = null;
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            String[] values = new String[records.getNoOfRecords()];
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String value = records.getRecords()[i].get(EditorEngineConstants.Variables.Common.VALUE);
                values[i] = value;
            }
            result = new InstancePropertiesValues(values);
        }
        return result;
    }

}
