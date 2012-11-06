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

package org.prowim.services.ejb.knowledge;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.collections.ActivityArray;
import org.prowim.datamodel.collections.DomainKnowledgeArray;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessKnowledgeArray;
import org.prowim.datamodel.collections.ProcessTypesArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Knowledge;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Process;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.EditorHelper;
import org.prowim.services.coreengine.ProcessHelper;
import org.prowim.services.coreengine.entities.KnowledgeEntity;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.knowledge.KnowledgeRemote;
import org.prowim.services.ejb.utils.ServicesUtils;



/**
 * Implements the functions for {@link KnowledgeRemote}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5100 $
 */
@Stateless(name = ServiceConstants.KNOWLEDGE_BEAN)
@WebService(name = ServiceConstants.PROWIM_KNOWLEDGE_SERVICE_NAME, serviceName = ServiceConstants.PROWIM_KNOWLEDGE_SERVICE_NAME, endpointInterface = ServiceConstants.PROWIM_KNOWLEDGE_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT)
public class KnowledgeBean implements KnowledgeRemote
{
    @Resource
    private SessionContext  sessionContext;

    @IgnoreDependency
    @EJB
    private ProcessHelper   processHelper;

    @IgnoreDependency
    @EJB
    private KnowledgeEntity knowledgeEntity;

    @IgnoreDependency
    @EJB
    private EditorHelper    editor;

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getAllProcesses()
     */
    public ProcessArray getAllProcesses() throws OntologyErrorException
    {
        ProcessArray res = new ProcessArray();
        Process[] processes = processHelper.getAllProcesses();
        if (processes != null)
        {
            res.setItem(Arrays.asList(processes));
        }

        return res;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getProcessKnowledge()
     */
    public ProcessKnowledgeArray getProcessKnowledge() throws OntologyErrorException
    {
        ProcessKnowledgeArray res = new ProcessKnowledgeArray();
        Knowledge[] processknowledges = processHelper.getProcessKnowledge((Process[]) getAllProcesses().getItem().toArray());
        if (processknowledges != null)
        {
            res.setItem(Arrays.asList(processknowledges));
        }
        return res;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getActivityKnowledge(java.lang.String)
     */
    public KnowledgeObjectArray getActivityKnowledge(String activityID) throws OntologyErrorException
    {
        KnowledgeObjectArray knowledges = processHelper.getActivityKnowledge(activityID);
        return knowledges;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getActivities(java.lang.String)
     */
    public ActivityArray getActivities(String template) throws OntologyErrorException
    {
        ActivityArray res = new ActivityArray();
        Activity[] activities = processHelper.getActivities(template);
        if (activities != null)
        {
            res.addArray(activities);
        }

        return res;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getTopDomainKnowledge()
     */
    public DomainKnowledgeArray getTopDomainKnowledge() throws OntologyErrorException
    {
        DomainKnowledgeArray res = new DomainKnowledgeArray();
        KnowledgeDomain[] domains = processHelper.getTopDomainKnowledge();

        if (domains != null)
        {
            res.setItem(Arrays.asList(domains));
        }
        return res;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getDomainKnowledgeObjects(java.lang.String)
     */
    public KnowledgeObjectArray getDomainKnowledgeObjects(String domainID) throws OntologyErrorException
    {
        KnowledgeObjectArray res = new KnowledgeObjectArray();
        List<KnowledgeObject> knowledges = processHelper.getDomainKnowledgeObjects(domainID);
        if (knowledges != null)
        {
            res.setItem(knowledges);
        }
        return res;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getSubDomainKnowledge(java.lang.String)
     */
    public DomainKnowledgeArray getSubDomainKnowledge(String domainID) throws OntologyErrorException
    {
        DomainKnowledgeArray res = new DomainKnowledgeArray();
        KnowledgeDomain[] domains = processHelper.getSubKnowledgeDomain(domainID);

        if (domains != null)
        {
            res.setItem(Arrays.asList(domains));
        }
        return res;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getProcessTypes()
     */
    public ProcessTypesArray getProcessTypes()
    {
        ProcessTypesArray res = new ProcessTypesArray();
        String[] types = new String[] { "Nebenprozesse", "Unterstuetzungsprozesse", "Wissensprozesse", "Kernprozesse" };
        if (types != null)
        {
            res.setItem(Arrays.asList(types));
        }
        return res;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#createKnowledgeObject(java.lang.String, java.lang.String)
     */
    @Override
    public KnowledgeObject createKnowledgeObject(String relationID, String name) throws OntologyErrorException
    {
        return knowledgeEntity.createKnowledgeObject(relationID, name, ServicesUtils.getCallerUserID(sessionContext.getCallerPrincipal().getName()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#createKnowledgeObject(java.lang.String)
     */
    @Override
    public KnowledgeObject createKnowledgeObject(String name) throws OntologyErrorException
    {
        return knowledgeEntity.createKnowledgeObject(name, ServicesUtils.getCallerUserID(sessionContext.getCallerPrincipal().getName()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getKnowledgeRepositories()
     */
    @Override
    public KnowledgeRepositoryArray getKnowledgeRepositories() throws OntologyErrorException
    {
        return knowledgeEntity.getKnowledgeRepositories();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#createKnowledgeLink(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public KnowledgeLink createKnowledgeLink(String knowledgeObjectID, String name, String wsID, String link) throws OntologyErrorException
    {

        return processHelper.createKnowledgeLink(knowledgeObjectID, name, wsID, link);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#saveKnowledgeObject(org.prowim.datamodel.prowim.KnowledgeObject)
     */
    @Override
    public void saveKnowledgeObject(KnowledgeObject knowledgeObject) throws OntologyErrorException
    {
        processHelper.updateKnowledgeObject(knowledgeObject);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#updateKnowledgeObject(java.lang.String, java.lang.String, org.prowim.datamodel.collections.ObjectArray, org.prowim.datamodel.collections.PersonArray)
     */
    public void updateKnowledgeObject(String knowledgeObjectID, String name, ObjectArray knowledgeLinks, PersonArray dominators)
                                                                                                                                throws OntologyErrorException
    {
        processHelper.editKnowledgeObject(knowledgeObjectID, name, knowledgeLinks, dominators);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#deleteKnowledgeObject(java.lang.String)
     */
    @Override
    public void deleteKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException
    {
        knowledgeEntity.deleteKnowledgeObject(knowledgeObjectID, ServicesUtils.getCallerUserID(sessionContext.getCallerPrincipal().getName()));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getKnowledgeObject(java.lang.String)
     */
    @Override
    public KnowledgeObject getKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObjectID, "knowledgeObjectID is null");
        return knowledgeEntity.getKnowledgeObject(knowledgeObjectID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getBusinessDomains(java.lang.String)
     */
    @Override
    public DomainKnowledgeArray getBusinessDomains(String processInstanceID) throws OntologyErrorException
    {
        return knowledgeEntity.getBusinessDomains(processInstanceID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#searchKnowledges(java.lang.String)
     */
    @Override
    public KnowledgeObjectArray searchKnowledges(String keyWord) throws OntologyErrorException
    {
        return processHelper.searchLinks(keyWord);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#searchKeyWord(java.lang.String)
     */
    @Override
    public ObjectArray searchKeyWord(String keyWord) throws OntologyErrorException
    {
        return processHelper.searchKeyWord(keyWord);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.editor.EditorRemote#deleteObject(java.lang.String)
     */
    @Override
    public void deleteObject(String id) throws OntologyErrorException
    {
        Validate.notNull(id);
        editor.deleteInstance(id);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getKnowledgeObjects()
     */
    @Override
    public KnowledgeObjectArray getKnowledgeObjects() throws OntologyErrorException
    {
        return knowledgeEntity.getKnowledgeObjects();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#createKnowledgeDomain(java.lang.String, java.lang.String)
     */
    @Override
    public KnowledgeDomain createKnowledgeDomain(String name, String description) throws OntologyErrorException
    {
        return knowledgeEntity.createKnowledgeDomain(name, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#addKnowledgeObject(java.lang.String, java.lang.String)
     */
    @Override
    public void addKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObjectID);
        Validate.notNull(processElementID);
        knowledgeEntity.addKnowledgeObject(knowledgeObjectID, processElementID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#removeKnowledgeObject(java.lang.String, java.lang.String)
     */
    @Override
    public void removeKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObjectID);
        Validate.notNull(processElementID);
        knowledgeEntity.removeKnowledgeObject(knowledgeObjectID, processElementID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#createKnowledgeDomain(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public KnowledgeDomain createKnowledgeDomain(String name, String parentDomainID, String description) throws OntologyErrorException
    {
        return knowledgeEntity.createKnowledgeDomain(name, parentDomainID, description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getKnowledgeObjects(java.lang.String)
     */
    public KnowledgeObjectArray getKnowledgeObjects(String processElementID) throws OntologyErrorException
    {
        Validate.notNull(processElementID);
        KnowledgeObjectArray knowledgeobjects = knowledgeEntity.getKnowledgeObjects(processElementID);

        return knowledgeobjects;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getKnowledgeObjectsOfProcess(java.lang.String)
     */
    @Override
    public KnowledgeObjectArray getKnowledgeObjectsOfProcess(String processID) throws OntologyErrorException
    {
        Validate.notNull(processID);
        KnowledgeObjectArray knowledgeobjects = knowledgeEntity.getKnowledgeObjectsOfProcess(processID);

        return knowledgeobjects;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.knowledge.KnowledgeRemote#getRelationsOfKnowledgeObject(java.lang.String)
     */
    @Override
    public ObjectArray getRelationsOfKnowledgeObject(String knowldgeObjectID) throws OntologyErrorException
    {
        Validate.notNull(knowldgeObjectID, "ID of knowledge object can not be null!");
        return knowledgeEntity.getRelationsOfKnowledgeObject(knowldgeObjectID);
    }
}
