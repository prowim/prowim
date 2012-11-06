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

package org.prowim.services.ejb.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.collections.InstancePropertyArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.editor.InstancePropertiesNames;
import org.prowim.datamodel.editor.InstancePropertiesValues;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.Function;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.Product;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.datamodel.prowim.Work;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.resources.ResourcesLocator;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EditorHelper;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.commons.CommonRemote;

import de.ebcot.tools.logging.Logger;
import de.ebcot.tools.parser.ParserJNLP;


/**
 * Implements the functions for {@link CommonRemote}.
 * 
 * @author Saad Wardi
 * 
 */
@Stateless(name = ServiceConstants.PROWIM_COMMON_BEAN)
@WebService(name = ServiceConstants.PROWIM_COMMON_SERVICE_NAME, serviceName = ServiceConstants.PROWIM_COMMON_SERVICE_NAME, endpointInterface = ServiceConstants.PROWIM_COMMON_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT)
@XmlSeeAlso({ Activity.class, Role.class })
public class CommonBean implements CommonRemote
{
    private static final Logger LOG = Logger.getLogger(CommonBean.class);

    @IgnoreDependency
    @EJB
    private CommonHelper        common;

    @IgnoreDependency
    @EJB
    private EditorHelper        editor;

    @IgnoreDependency
    @EJB
    private ProcessHelper       processHelper;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getDescription(java.lang.String)
     */
    @Override
    public String getDescription(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        return common.getDescription(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#rename(java.lang.String, java.lang.String)
     */
    @Override
    public void rename(String id, String newName) throws OntologyErrorException
    {
        Validate.notNull(id);
        Validate.notNull(newName);
        common.rename(id, newName);

        // rename related start role of process if the renamed object was a process
        boolean isProcess = processHelper.isProcess(id);
        if (isProcess)
        {
            LOG.debug("A process and starting role will be renamed to (" + isProcess + "): " + newName);
            String roleID = processHelper.getStartingRole(id);
            LOG.debug("A startRole will be renamed to (" + roleID + "): " + "Startrecht_" + newName);
            // TODO i18n
            common.rename(roleID, "Startrecht_" + newName);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getName(java.lang.String)
     */
    @Override
    public String getName(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        return common.getName(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#setDescription(java.lang.String, java.lang.String)
     */
    @Override
    public void setDescription(String id, String description) throws OntologyErrorException
    {
        Validate.notNull(id);
        Validate.notNull(description);
        common.setDescription(id, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getProperties(java.lang.String)
     */
    @Override
    public InstancePropertyArray getProperties(String instanceID) throws OntologyErrorException
    {
        return common.getProperties(instanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getRelations(java.lang.String)
     */
    @Override
    public InstancePropertyArray getRelations(String instanceID) throws OntologyErrorException
    {
        return common.getRelations(instanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getInstancePropertiesNames(java.lang.String)
     */
    @Override
    public InstancePropertiesNames getInstancePropertiesNames(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        return common.getInstancePropertiesNames(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getInstancePropertiesValues(java.lang.String)
     */
    @Override
    public InstancePropertiesValues getInstancePropertiesValues(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        return common.getInstancePropertiesValues(id);
    }

    /**
     * {@inheritDoc}
     * 
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getInstanceRelationsNames(java.lang.String)
     */
    @Override
    public InstancePropertiesNames getInstanceRelationsNames(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        return editor.getInstanceRelations(id).getNames();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getInstanceRelationsValues(java.lang.String)
     */
    @Override
    public InstancePropertiesValues getInstanceRelationsValues(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        return editor.getInstanceRelations(id).getValues();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getProcessElements()
     */
    @Override
    public ObjectArray getProcessElements() throws OntologyErrorException
    {
        return common.getProcessElements();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getProcessElementsInstances(java.lang.String, java.lang.String)
     */
    @Override
    public ObjectArray getProcessElementsInstances(String processID, String element) throws OntologyErrorException
    {
        return common.getProcessElementsInstances(processID, element);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#deleteInstance(java.lang.String)
     */
    @Override
    public String deleteInstance(String id) throws OntologyErrorException
    {
        return common.deleteInstance(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#rewriteJNLP(java.lang.String)
     */
    @Override
    public void rewriteJNLP(String param)
    {
        try
        {

            File jnlpTemp = new File(ResourcesLocator.getJNLPPath() + ".tmp");
            if ( !jnlpTemp.exists())
            {
                jnlpTemp.createNewFile();
            }
            else
            {
                jnlpTemp.delete();
                jnlpTemp.createNewFile();
            }

            InputStream inputStream = new FileInputStream(ResourcesLocator.getJNLPPath());
            OutputStream out = new FileOutputStream(jnlpTemp);
            ParserJNLP parserJNLP = new ParserJNLP(inputStream, param);
            parserJNLP.writeXmlFile(out);
            out.flush();
            inputStream.close();
            out.close();
        }
        catch (IOException e)
        {
            // LOG.error("Cannot write the JNLP file for ticket: " + ticket, e);
            throw new IllegalStateException("Cannot write the JNLP file for ticket: " + param);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getRole()
     */
    public Role getRole()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getActivity()
     */
    @Override
    public Activity getActivity()
    {
        // TODO $Author: khodaei $ Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getControlFlow()
     */
    @Override
    public ControlFlow getControlFlow()
    {
        // TODO $Author: khodaei $ Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getWork()
     */
    public Work getWork()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getFunction()
     */
    public Function getFunction()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getMean()
     */
    public Mean getMean()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getResultsMemory()
     */
    public ResultsMemory getResultsMemory()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getProduct()
     */
    public Product getProduct()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getProcessInformation()
     */
    public ProcessInformation getProcessInformation()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#setParameter(org.prowim.datamodel.collections.ParameterArray)
     */
    @Override
    public void setParameter(ParameterArray paramArray) throws OntologyErrorException
    {
        common.setParameter(paramArray);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#isGlobal(java.lang.String)
     */
    @Override
    public boolean isGlobal(String instanceID) throws OntologyErrorException
    {
        return common.isGlobal(instanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#setScope(java.lang.String, java.lang.String)
     */
    @Override
    public void setScope(String instanceID, String scope) throws OntologyErrorException
    {
        Validate.notNull(instanceID);
        Validate.notNull(scope);
        editor.setScope(instanceID, scope);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#getDirectClassOfInstance(java.lang.String)
     */
    @Override
    public String getDirectClassOfInstance(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        return this.common.getDirectClassOfInstance(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#setSlotValue(String, String, String)
     */
    @Override
    public void setSlotValue(String frameID, String slotName, String value) throws OntologyErrorException
    {
        Validate.notNull(slotName);
        Validate.notNull(value);

        this.common.setSlotValue(frameID, slotName, value);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.commons.CommonRemote#renameVersion(java.lang.String, java.lang.String)
     */
    @Override
    public void renameVersion(String instanceID, String newName) throws OntologyErrorException
    {
        this.common.clearRelationValue(instanceID, AlgernonConstants.Slots.USER_DEFINED_VERSION);
        this.common.setSlotValue(instanceID, AlgernonConstants.Slots.USER_DEFINED_VERSION, newName);
    }
}
