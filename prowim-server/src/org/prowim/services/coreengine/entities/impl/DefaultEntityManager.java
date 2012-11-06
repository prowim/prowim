/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-29 19:53:01 +0200 (Mi, 29 Sep 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultEntityManager.java $
 * $LastChangedRevision: 4848 $
 *------------------------------------------------------------------------------
 * (c) 07.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.entities.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.entities.ElementsEntity;
import org.prowim.services.coreengine.entities.EntityManager;
import org.prowim.services.coreengine.impl.CommonHelperBean;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the interface {@link EntityManager}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4848 $
 */
public class DefaultEntityManager implements EntityManager
{
    private static final Logger  LOG = Logger.getLogger(DefaultEntityManager.class);
    private static EntityManager instance;
    private ElementsEntity       elementsEntity;
    private CommonHelper         common;

    /**
     * Description.
     */
    public DefaultEntityManager()
    {
        init();
        try
        {
            InitialContext initialContext = new InitialContext();
            String elementsEntityName = "prowimservices/" + DefaultElementsEntity.class.getSimpleName() + "/local";

            Object ref = initialContext.lookup(elementsEntityName);

            elementsEntity = (ElementsEntity) PortableRemoteObject.narrow(ref, ElementsEntity.class);

            String commonHelperName = "prowimservices/" + CommonHelperBean.class.getSimpleName() + "/local";

            Object commonHelperRef = initialContext.lookup(commonHelperName);

            common = (CommonHelper) PortableRemoteObject.narrow(commonHelperRef, CommonHelper.class);
        }
        catch (NamingException e)
        {
            LOG.error("Could not lookup ElementsEntity: ", e);
        }

    }

    private static void init()
    {
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.ACTIVITY, org.prowim.datamodel.prowim.Activity.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.MEAN, org.prowim.datamodel.prowim.Mean.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.ROLE, org.prowim.datamodel.prowim.Role.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.PROCESSINFORMATION, org.prowim.datamodel.prowim.ProcessInformation.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.PRODUCTWAY, org.prowim.datamodel.prowim.ProductWay.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.CONTROLFLOW, org.prowim.datamodel.prowim.ControlFlow.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.RESULTSMEMORY, org.prowim.datamodel.prowim.ResultsMemory.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.WORK, org.prowim.datamodel.prowim.Work.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.FUNCTION, org.prowim.datamodel.prowim.Function.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.PRODUCT, org.prowim.datamodel.prowim.Product.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.CONJUNCTION, org.prowim.datamodel.prowim.Conjunction.class.getSimpleName());
        ELEMENTS_DATAOBJECTS_MAP.put(Relation.Classes.DECISION, org.prowim.datamodel.prowim.Decision.class.getSimpleName());
        //
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Activity.class.getSimpleName(), Relation.Classes.ACTIVITY);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Mean.class.getSimpleName(), Relation.Classes.MEAN);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Role.class.getSimpleName(), Relation.Classes.ROLE);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.ProcessInformation.class.getSimpleName(), Relation.Classes.PROCESSINFORMATION);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.ProductWay.class.getSimpleName(), Relation.Classes.PRODUCTWAY);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.ControlFlow.class.getSimpleName(), Relation.Classes.CONTROLFLOW);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.ResultsMemory.class.getSimpleName(), Relation.Classes.RESULTSMEMORY);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Work.class.getSimpleName(), Relation.Classes.WORK);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Function.class.getSimpleName(), Relation.Classes.FUNCTION);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Product.class.getSimpleName(), Relation.Classes.PRODUCT);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Conjunction.class.getSimpleName(), Relation.Classes.CONJUNCTION);
        DATAOBJECTS_ELEMENTS_MAP.put(org.prowim.datamodel.prowim.Decision.class.getSimpleName(), Relation.Classes.DECISION);

    }

    /**
     * 
     * Gets the instance this.
     * 
     * @return a singleton instance of this.
     */
    public static EntityManager getInstance()
    {
        if (instance == null)
        {
            LOG.debug(" Prowim editor helper will be created");
            instance = new DefaultEntityManager();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.EntityManager#getObject(java.lang.String)
     */
    public Object getObject(String id) throws OntologyErrorException
    {
        String elementClassName = common.getDirectClassOfInstance(id);
        LOG.debug("Create Object from class : " + elementClassName);
        Object result = null;
        try
        {
            if (containsElement(elementClassName))
            {
                LOG.debug("GetObject Invoke :   " + "get" + getDataObjectClassName(elementClassName) + "   " + id);
                result = invoke("get" + getDataObjectClassName(elementClassName), id);
            }
            else
            {
                throw new IllegalStateException("Element is not supported :   " + getDataObjectClassName(elementClassName));
            }
        }
        catch (InvocationException e)
        {
            LOG.error("Methodname :   ", e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.EntityManager#containsElement(java.lang.String)
     */
    @Override
    public boolean containsElement(String element)
    {
        return ELEMENTS_DATAOBJECTS_MAP.containsKey(element);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.EntityManager#containsDataObject(java.lang.String)
     */
    public boolean containsDataObject(String dataObjectClassName)
    {
        return DATAOBJECTS_ELEMENTS_MAP.containsKey(dataObjectClassName);
    }

    /**
     * Title and description.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4848 $
     */
    private class InvocationException extends Exception
    {
        /**
         * Description.
         */
        private static final long serialVersionUID = 1L;

        protected InvocationException(String message, Throwable throwable)
        {
            super(message, throwable);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.EntityManager#getDataObjectClassName(java.lang.String)
     */
    public String getDataObjectClassName(String elementClassName)
    {
        if (ELEMENTS_DATAOBJECTS_MAP.containsKey(elementClassName))
        {
            return ELEMENTS_DATAOBJECTS_MAP.get(elementClassName);
        }
        else
        {
            throw new IllegalStateException("Ontology class is not supported! " + elementClassName);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.EntityManager#getOntologyClassName(java.lang.String)
     */
    public String getOntologyClassName(String dataObjectClassName)
    {
        if (DATAOBJECTS_ELEMENTS_MAP.containsKey(dataObjectClassName))
        {
            return DATAOBJECTS_ELEMENTS_MAP.get(dataObjectClassName);
        }
        else
        {
            throw new IllegalStateException("DataObject class is not supported! " + dataObjectClassName);
        }
    }

    /**
     * Maps the ontology class names to java class names.
     * 
     * @param dataObjectClassName ontology class name
     * @return not null dataobject classname.
     */
    public String getElementClassName(String dataObjectClassName)
    {
        if (DATAOBJECTS_ELEMENTS_MAP.containsKey(dataObjectClassName))
        {
            return DATAOBJECTS_ELEMENTS_MAP.get(dataObjectClassName);
        }
        else
        {
            throw new IllegalStateException("Data Object class is not supported! " + dataObjectClassName);
        }
    }

    private Object invoke(String methodName, String id) throws InvocationException
    {
        LOG.debug("invoking method: " + methodName);
        Object result = new Object();
        Class<ElementsEntity> clazz = ElementsEntity.class;
        Class<? >[] parameterTypes = new Class<? >[] { String.class };
        Method convertMethod;

        Object[] arguments = new Object[] { id };
        try
        {
            convertMethod = clazz.getMethod(methodName, parameterTypes);
        }
        catch (SecurityException e)
        {
            LOG.error("Methodname :   " + methodName);
            throw new InvocationException("SecurityException", e);
        }
        catch (NoSuchMethodException e)
        {
            LOG.error("Methodname :   " + methodName);
            throw new InvocationException("NoSuchMethodException", e);
        }
        try
        {
            result = convertMethod.invoke(elementsEntity, arguments);
        }
        catch (IllegalArgumentException e)
        {
            LOG.error("Methodname :   " + methodName);

            throw new InvocationException("IllegalArgumentException", e);
        }
        catch (IllegalAccessException e)
        {
            LOG.error("Methodname :   " + methodName);
            throw new InvocationException("IllegalAccessException", e);
        }
        catch (InvocationTargetException e)
        {
            LOG.error("Methodname :   " + methodName);
            throw new InvocationException("InvocationTargetException", e);
        }
        return result;
    }

}
