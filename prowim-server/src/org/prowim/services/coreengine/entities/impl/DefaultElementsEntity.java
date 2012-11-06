/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-27 09:41:40 +0200 (Mo, 27 Sep 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultElementsEntity.java $
 * $LastChangedRevision: 4820 $
 *------------------------------------------------------------------------------
 * (c) 08.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.entities.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Conjunction;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.Decision;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Function;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.Product;
import org.prowim.datamodel.prowim.ProductWay;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.datamodel.prowim.Work;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.ActivityEntity;
import org.prowim.services.coreengine.entities.ControlFlowEntity;
import org.prowim.services.coreengine.entities.ElementsEntity;
import org.prowim.services.coreengine.entities.FunctionEntity;
import org.prowim.services.coreengine.entities.KnowledgeEntity;
import org.prowim.services.coreengine.entities.MeanEntity;
import org.prowim.services.coreengine.entities.ProcessInformationEntity;
import org.prowim.services.coreengine.entities.ProductEntity;
import org.prowim.services.coreengine.entities.ProductWayEntity;
import org.prowim.services.coreengine.entities.ResultsMemoryEntity;
import org.prowim.services.coreengine.entities.RoleEntity;
import org.prowim.services.coreengine.entities.WorkEntity;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the interface {@link ElementsEntity}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4820 $
 */
@Stateless
public class DefaultElementsEntity implements ElementsEntity
{
    private static final Logger      LOG              = Logger.getLogger(DefaultElementsEntity.class);

    private static final String      NO_ONTOLOGY_READ = "Not read from ontology, see Mantis: 4605";

    @IgnoreDependency
    @EJB
    private ActivityEntity           activityEntity;

    @IgnoreDependency
    @EJB
    private ControlFlowEntity        controlFlowEntity;

    @IgnoreDependency
    @EJB
    private FunctionEntity           functionEntity;

    @IgnoreDependency
    @EJB
    private KnowledgeEntity          knowledgeEntity;

    @IgnoreDependency
    @EJB
    private MeanEntity               meanEntity;

    @IgnoreDependency
    @EJB
    private RoleEntity               roleEntity;

    @IgnoreDependency
    @EJB
    private ProcessInformationEntity processInformationEntity;

    @IgnoreDependency
    @EJB
    private ResultsMemoryEntity      resultsMemoryEntity;

    @IgnoreDependency
    @EJB
    private WorkEntity               workEntity;

    @IgnoreDependency
    @EJB
    private ProductEntity            productEntity;

    @IgnoreDependency
    @EJB
    private ProductWayEntity         productWayEntity;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getActivity(java.lang.String)
     */
    public Activity getActivity(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getActivity  " + id);
        return activityEntity.getActivity(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getMean(java.lang.String)
     */
    @Override
    public Mean getMean(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getFund  " + id);
        return meanEntity.getMean(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getRole(java.lang.String)
     */
    @Override
    public Role getRole(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getRole  " + id);
        return roleEntity.getRole(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getControlFlow(java.lang.String)
     */
    @Override
    public ControlFlow getControlFlow(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getControlFlow  " + id);
        return controlFlowEntity.getControlFlow(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getKnowledgeObject(java.lang.String)
     */
    @Override
    public KnowledgeObject getKnowledgeObject(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getKnowledgeObject  " + id);
        return knowledgeEntity.getKnowledgeObject(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getProcessInformation(java.lang.String)
     */
    @Override
    public ProcessInformation getProcessInformation(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getProcessInformation  " + id);
        return processInformationEntity.getProcessInformation(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getResultsMemory(java.lang.String)
     */
    @Override
    public ResultsMemory getResultsMemory(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getResultsMemory  " + id);
        return resultsMemoryEntity.getResultsMemory(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getWork(java.lang.String)
     */
    @Override
    public Work getWork(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getWork  " + id);
        return workEntity.getWork(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getFunction(java.lang.String)
     */
    @Override
    public Function getFunction(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getFunction  " + id);
        // TODO Implement here the entity access to read it from ontology
        // functionEntity.getFunction(id); fails because of no rule 'getFunction'
        return DefaultDataObjectFactory.createFunction(id, NO_ONTOLOGY_READ, NO_ONTOLOGY_READ);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getProduct(java.lang.String)
     */
    @Override
    public Product getProduct(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getProduct  " + id);
        return productEntity.getProduct(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getProductWay(java.lang.String)
     */
    @Override
    public ProductWay getProductWay(String id) throws OntologyErrorException
    {
        LOG.debug("Call ProcessElements  :  getProductWay  " + id);
        return productWayEntity.getProductWay(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getConjunction(java.lang.String)
     */
    @Override
    public Conjunction getConjunction(String id)
    {
        // TODO Implement here the entity access to read it from ontology
        return new Conjunction(id, NO_ONTOLOGY_READ, NO_ONTOLOGY_READ);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.ElementsEntity#getDecision(java.lang.String)
     */
    @Override
    public Decision getDecision(String id)
    {
        // TODO Implement here the entity access to read it from ontology
        return new Decision(id, NO_ONTOLOGY_READ, NO_ONTOLOGY_READ);
    }
}