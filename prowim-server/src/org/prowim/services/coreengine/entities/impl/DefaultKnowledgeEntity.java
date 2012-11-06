/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-10-18 18:27:07 +0200 (Di, 18 Okt 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/impl/DefaultKnowledgeEntity.java $
 * $LastChangedRevision: 5100 $
 *------------------------------------------------------------------------------
 * (c) 02.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.lang.Validate;
import org.jboss.ejb3.annotation.IgnoreDependency;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.DomainKnowledgeArray;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.Relation;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.CommonEngine;
import org.prowim.services.coreengine.CommonHelper;
import org.prowim.services.coreengine.EditorEngineConstants;
import org.prowim.services.coreengine.EngineConstants;
import org.prowim.services.coreengine.ProcessEngine;
import org.prowim.services.coreengine.entities.KnowledgeEntity;
import org.prowim.services.coreengine.entities.OrganizationEntity;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the interface {@link KnowledgeEntity}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5100 $
 */
@Stateless(name = "DefaultKnowledgeEntity")
public class DefaultKnowledgeEntity implements KnowledgeEntity
{
    private static final Logger LOG = Logger.getLogger(DefaultKnowledgeEntity.class);

    @IgnoreDependency
    @EJB
    private ProcessEngine       processEngine;

    @IgnoreDependency
    @EJB
    private CommonEngine        commonEngine;

    @IgnoreDependency
    @EJB
    private CommonHelper        common;

    @IgnoreDependency
    @EJB
    private OrganizationEntity  organizationEntity;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeObjects(java.lang.String)
     */
    public KnowledgeObjectArray getKnowledgeObjects(String id) throws OntologyErrorException
    {
        final RecordSet result = processEngine.getInstanceKnowledgeObjects(id);
        KnowledgeObjectArray knowledgeObjects = new KnowledgeObjectArray();
        if (result != null && result.getNoOfRecords() > 0)
        {
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String knowledgeObjectID = record.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID);
                final String name = record.get(ProcessEngineConstants.Variables.Common.NAME);
                final String createTime = record.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                final String description = record.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                final String creatorID = record.get(ProcessEngineConstants.Variables.Organisation.Person.ID);

                KnowledgeObject knowledgeObject = DefaultDataObjectFactory.createKnowledgeObject(knowledgeObjectID, name, createTime);
                knowledgeObject.setOwner(creatorID);
                knowledgeObject.setDescription(description);

                knowledgeObjects.add(this.getKnowledgeObject(knowledgeObject));
            }
        }

        Collections.sort(knowledgeObjects.getItem());
        return knowledgeObjects;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeObject(java.lang.String)
     */
    public KnowledgeObject getKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException
    {
        final RecordSet knowledgeObjectRecords = processEngine.getKnowledgeObject(knowledgeObjectID);
        KnowledgeObject knowledgeObject = null;
        if (knowledgeObjectRecords != null && knowledgeObjectRecords.getNoOfRecords() > 0)
        {
            /** Get the sub class ProcessElement attributes. */
            final String id = knowledgeObjectID;
            final String name = knowledgeObjectRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.NAME);
            final String createTime = knowledgeObjectRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            final String description = knowledgeObjectRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);

            LOG.debug("KnowledgeObject IS:   " + name + "    " + id);

            String creatorID = this.getKnowledgeObjectCreatorID(knowledgeObjectID);
            knowledgeObject = DefaultDataObjectFactory.createKnowledgeObject(id, name, createTime);
            knowledgeObject.setOwner(creatorID);
            knowledgeObject.setDescription(description);

            /** get the dominators. */
            final RecordSet knowledgeObjectDominatorRecords = processEngine.getKnowledgeObjectDominators(knowledgeObjectID);
            PersonArray dominators = new PersonArray();
            if (knowledgeObjectDominatorRecords != null && knowledgeObjectDominatorRecords.getNoOfRecords() > 0)
            {
                for (int i = 0; i < knowledgeObjectDominatorRecords.getNoOfRecords(); i++)
                {
                    final Hashtable<String, String> knowledgeObjectDominatorRecord = knowledgeObjectDominatorRecords.getRecords()[i];
                    String personID = knowledgeObjectDominatorRecord.get(ProcessEngineConstants.Variables.Organisation.Person.ID);
                    if (personID != null && !personID.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
                    {
                        Person person = organizationEntity.getPerson(personID);
                        if (person != null)
                        {
                            dominators.add(person);
                        }
                    }
                }
            }
            /** get the knowledgelinks. */
            final RecordSet knowledgeLinkRecords = processEngine.getKnowledgeObjectLinks(knowledgeObjectID);
            List<KnowledgeLink> links = new ArrayList<KnowledgeLink>();
            if (knowledgeLinkRecords != null && knowledgeLinkRecords.getNoOfRecords() > 0)
            {
                for (int i = 0; i < knowledgeLinkRecords.getNoOfRecords(); i++)
                {
                    final Hashtable<String, String> knowledgeLinkRecord = knowledgeLinkRecords.getRecords()[i];
                    String linkID = knowledgeLinkRecord.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGELINK_ID);
                    if (linkID != null && !linkID.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
                    {
                        KnowledgeLink link = this.getKnowledgeLink(linkID);
                        if (link != null)
                        {
                            links.add(link);
                        }

                    }
                }
            }

            // Set the data
            knowledgeObject.setResponsiblePersons(dominators);
            knowledgeObject.setKnowledgeLinks(links);

            /** Get the key words of knowledge object */
            final RecordSet keyWordsRecords = processEngine.getKnowledgeKeyWords(knowledgeObjectID);
            if (keyWordsRecords != null && keyWordsRecords.getNoOfRecords() > 0)
            {
                for (int i = 0; i < keyWordsRecords.getNoOfRecords(); i++)
                {
                    final Hashtable<String, String> keyWordsRecord = keyWordsRecords.getRecords()[i];
                    String keyWord = keyWordsRecord.get(EngineConstants.Variables.Common.KEY_WORDS).trim();
                    if ( !keyWord.isEmpty())
                        knowledgeObject.addKeyWord(keyWord);
                }
            }

        }
        return knowledgeObject;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeObjectCreatorID(java.lang.String)
     */
    public String getKnowledgeObjectCreatorID(String knowledgeObjectID) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObjectID);
        String result = null;
        if (this.getKnowledgeObjectCreatorNumber(knowledgeObjectID).equals(ProcessEngineConstants.Variables.Common.ONE))
        {
            final RecordSet knowledgeObjectCreatorRecords = processEngine.getKnowledgeObjectCreator(knowledgeObjectID);
            if (knowledgeObjectCreatorRecords.getNoOfRecords() > 0)
            {
                for (int i = 0; i < knowledgeObjectCreatorRecords.getNoOfRecords(); i++)
                {
                    final Hashtable<String, String> knowledgeObjectCreatorRecord = knowledgeObjectCreatorRecords.getRecords()[i];
                    final String personID = knowledgeObjectCreatorRecord.get(ProcessEngineConstants.Variables.Organisation.Person.ID);
                    result = personID;
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeLink(java.lang.String)
     */
    public KnowledgeLink getKnowledgeLink(String knowledgeLinkID) throws OntologyErrorException
    {
        final RecordSet knowledgeLinkRecords = processEngine.getKnowledgeLink(knowledgeLinkID);
        KnowledgeLink knowledgeLink = null;
        if (knowledgeLinkRecords != null && knowledgeLinkRecords.getNoOfRecords() == 1)
        {
            final String id = knowledgeLinkID;
            final String name = knowledgeLinkRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.NAME);
            final String createTime = knowledgeLinkRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            final String link = knowledgeLinkRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Knowledge.LINK);
            final String repository = knowledgeLinkRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Knowledge.REPOSITORY);
            final String description = knowledgeLinkRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
            final String knwoledgeObjectID = knowledgeLinkRecords.getRecords()[0]
                    .get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN);
            knowledgeLink = DefaultDataObjectFactory.createKnowledgeLink(id, name, createTime);
            knowledgeLink.setHyperlink(link);
            knowledgeLink.setRepository(repository);
            knowledgeLink.setDescription(description);
            knowledgeLink.setKnwoledgeObject(knwoledgeObjectID);
        }
        return knowledgeLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#deleteKnowledgeObject(java.lang.String, java.lang.String)
     */
    public void deleteKnowledgeObject(String knowledgeObjectID, String userID) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObjectID);
        String creatorID = this.getKnowledgeObject(knowledgeObjectID).getOwner();
        if ((creatorID != null && (creatorID.equals(userID)) || DefaultSecurityManager.getInstance().isPersonAdminUserWithID(userID)))
        {
            KnowledgeObject knowledgeObject = this.getKnowledgeObject(knowledgeObjectID);
            List<KnowledgeLink> links = knowledgeObject.getKnowledgeLinks();
            Iterator<KnowledgeLink> itLinks = links.iterator();
            while (itLinks.hasNext())
            {
                KnowledgeLink link = itLinks.next();
                String deleteStatus = common.deleteInstance(link.getID());
                LOG.info("delete knowledgelink with id : " + link.getID() + "    " + deleteStatus);
            }
            String clearStatus = common.clearRelationValue(knowledgeObjectID, Relation.Slots.REFERS_TO);
            if (clearStatus.equals(AlgernonConstants.OK))
            {
                String deleteStatus = common.deleteInstance(knowledgeObjectID);
                LOG.info("delete knowledgeobject with id : " + knowledgeObjectID + "    " + deleteStatus);
                if ( !deleteStatus.equals(AlgernonConstants.OK))
                {
                    throw new IllegalStateException("Can not delete instance with id :  " + knowledgeObjectID);
                }
            }
            else
            {
                throw new IllegalStateException("Can not clear relation with id :  " + knowledgeObjectID);
            }
        }
        else
        {
            throw new IllegalStateException("The user " + userID + "   can not delete knowledgeobject with id :  " + knowledgeObjectID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeObjects()
     */
    @Override
    public KnowledgeObjectArray getKnowledgeObjects() throws OntologyErrorException
    {
        final RecordSet knowledgesRecords = processEngine.getAllKnowledgeObjects();
        final KnowledgeObjectArray knowledgeObjects = new KnowledgeObjectArray();
        if (knowledgesRecords.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < knowledgesRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgesRecord = knowledgesRecords.getRecords()[i];
                final String knowledgeObjectID = knowledgesRecord.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN);
                final String name = knowledgesRecord.get(ProcessEngineConstants.Variables.Common.NAME);
                final String createTime = knowledgesRecord.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                final String description = knowledgesRecord.get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                final String creatorID = knowledgesRecord.get(ProcessEngineConstants.Variables.Organisation.Person.ID);

                KnowledgeObject knowledgeObject = DefaultDataObjectFactory.createKnowledgeObject(knowledgeObjectID, name, createTime);
                knowledgeObject.setOwner(creatorID);
                knowledgeObject.setDescription(description);

                knowledgeObjects.add(this.getKnowledgeObject(knowledgeObject));
            }
        }

        Collections.sort(knowledgeObjects.getItem());
        return knowledgeObjects;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeObject(org.prowim.datamodel.prowim.KnowledgeObject)
     */
    @Override
    public KnowledgeObject getKnowledgeObject(KnowledgeObject knowledgeObject) throws OntologyErrorException
    {
        Validate.notNull(knowledgeObject, "Knowledge object can not be null");

        /** get the dominators. */
        final RecordSet knowledgeObjectDominatorRecords = processEngine.getKnowledgeObjectDominators(knowledgeObject.getID());
        PersonArray dominators = new PersonArray();
        if (knowledgeObjectDominatorRecords != null && knowledgeObjectDominatorRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < knowledgeObjectDominatorRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeObjectDominatorRecord = knowledgeObjectDominatorRecords.getRecords()[i];
                String personID = knowledgeObjectDominatorRecord.get(ProcessEngineConstants.Variables.Organisation.Person.ID);
                if (personID != null && !personID.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
                {
                    Person person = organizationEntity.getPerson(personID);
                    if (person != null)
                    {
                        dominators.add(person);
                    }
                }
            }
        }
        knowledgeObject.setResponsiblePersons(dominators);

        /** get the knowledgelinks. */
        final RecordSet knowledgeLinkRecords = processEngine.getKnowledgeObjectLinks(knowledgeObject.getID());
        List<KnowledgeLink> links = new ArrayList<KnowledgeLink>();
        if (knowledgeLinkRecords != null && knowledgeLinkRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < knowledgeLinkRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeLinkRecord = knowledgeLinkRecords.getRecords()[i];
                String linkID = knowledgeLinkRecord.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGELINK_ID);
                if (linkID != null && !linkID.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
                {
                    KnowledgeLink link = this.getKnowledgeLink(linkID);
                    if (link != null)
                    {
                        links.add(link);
                    }
                }
            }
        }
        knowledgeObject.setKnowledgeLinks(links);

        /** Get the key words of knowledge object */
        final RecordSet keyWordsRecords = processEngine.getKnowledgeKeyWords(knowledgeObject.getID());
        if (keyWordsRecords != null && keyWordsRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < keyWordsRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> keyWordsRecord = keyWordsRecords.getRecords()[i];
                String keyWord = keyWordsRecord.get(EngineConstants.Variables.Common.KEY_WORDS).trim();
                if ( !keyWord.isEmpty())
                    knowledgeObject.addKeyWord(keyWord);
            }
        }
        return knowledgeObject;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#createKnowledgeDomain(java.lang.String, java.lang.String)
     */
    @Override
    public KnowledgeDomain createKnowledgeDomain(String name, String description) throws OntologyErrorException
    {
        final RecordSet createRecords = commonEngine.addInstance(Relation.Classes.KNOWLEDGE_DOMAIN, name);
        final String id;
        if (createRecords.getResult().equals(AlgernonConstants.OK))
        {
            id = createRecords.getRecords()[0].get(EditorEngineConstants.Variables.Common.ID);

            boolean setNameStatus = commonEngine.setSlotValue(id, Relation.Slots.NAME, name).getResult().equals(AlgernonConstants.OK);
            commonEngine.setSlotValue(id, Relation.Slots.DESCRIPTION, description).getResult().equals(AlgernonConstants.OK);
            if (setNameStatus && id != null)
            {
                LOG.debug("Created new Organization with ID :   " + id + "   and name:  " + name);
                KnowledgeDomain knowledgeDomain = this.getKnowledgeDomain(id);
                if (knowledgeDomain != null)
                    return knowledgeDomain;
                else
                    throw new IllegalStateException("Could not create KnowledgeDomain!");
            }
            else
            {
                LOG.debug("Remove the knowledgedomain with ID :   " + id + "   and name:  " + name);
                commonEngine.deleteInstance(id);
            }
        }

        throw new IllegalStateException("Could not create KnowledgeDomain!  ");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#createKnowledgeDomain(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public KnowledgeDomain createKnowledgeDomain(String name, String parentDomainID, String description) throws OntologyErrorException
    {
        Validate.notNull(name);
        Validate.notNull(parentDomainID);
        Validate.notNull(description);
        final KnowledgeDomain domain = createKnowledgeDomain(name, description);
        if (domain != null)
        {
            if (commonEngine.setSlotValue(parentDomainID, Relation.Slots.SUB_DOMAINS, domain.getID()).getResult().equals(AlgernonConstants.OK))
            {
                LOG.debug("createKnowledgeDomain :  creates a subdomain with id = " + domain.getID() + " parentDomain is : " + parentDomainID);
            }
            else
            {
                LOG.error("createKnowledgeDomain :  could not create a subdomain with id = " + domain.getID() + " parentDomain is : "
                        + parentDomainID);
                throw new IllegalStateException("createKnowledgeDomain :  could not create a subdomain with id = " + domain.getID()
                        + " parentDomain is : " + parentDomainID);
            }
        }
        return domain;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeDomain(java.lang.String)
     */
    public KnowledgeDomain getKnowledgeDomain(String domainKnowledgeID) throws OntologyErrorException
    {
        Validate.notNull(domainKnowledgeID);
        final RecordSet domainKnowledgeRecords = processEngine.getKnowledgeDomain(domainKnowledgeID);
        KnowledgeDomain domainKnowledge = null;
        if (domainKnowledgeRecords.getNoOfRecords() == 1)
        {
            final String id = domainKnowledgeID;
            final String name = domainKnowledgeRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.NAME);
            final String createTime = domainKnowledgeRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
            final String description = domainKnowledgeRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
            domainKnowledge = DefaultDataObjectFactory.createKnowledgeDomain(id, name, createTime);
            domainKnowledge.setDescription(description);
            return domainKnowledge;
        }
        else if (domainKnowledgeRecords.getNoOfRecords() == 0)
        {
            return null;
        }
        else
        {
            throw new IllegalStateException("More than 1 domain exists with id  " + domainKnowledgeID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getBusinessDomains(java.lang.String)
     */
    public DomainKnowledgeArray getBusinessDomains(String processInstanceID) throws OntologyErrorException
    {
        Validate.notNull(processInstanceID);
        final RecordSet bussinesDomainRecords = processEngine.getBusinessDomains(processInstanceID);
        final DomainKnowledgeArray bussinesDomains = new DomainKnowledgeArray();
        if (bussinesDomainRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < bussinesDomainRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> bussinesDomainRecord = bussinesDomainRecords.getRecords()[i];
                final String id = bussinesDomainRecord.get(ProcessEngineConstants.Variables.Common.ID);
                final KnowledgeDomain domainKnowledge = this.getKnowledgeDomain(id);
                // if domain could be found
                if (domainKnowledge != null)
                    bussinesDomains.add(domainKnowledge);
            }
        }
        return bussinesDomains;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeRepositories()
     */
    public KnowledgeRepositoryArray getKnowledgeRepositories() throws OntologyErrorException
    {
        RecordSet knowledgeRepositoriesRecords = processEngine.getKnowledgeRepositories();
        KnowledgeRepositoryArray repositories = new KnowledgeRepositoryArray();
        if (knowledgeRepositoriesRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < knowledgeRepositoriesRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeRepositoriesRecord = knowledgeRepositoriesRecords.getRecords()[i];
                final String id = knowledgeRepositoriesRecord.get(ProcessEngineConstants.Variables.Knowledge.REPOSITORY_ID);
                final String name = knowledgeRepositoriesRecord.get(ProcessEngineConstants.Variables.Common.NAME_EN);
                final String createTime = knowledgeRepositoriesRecord.get(ProcessEngineConstants.Variables.Common.CREATE_TIME);
                final String repository = knowledgeRepositoriesRecord.get(ProcessEngineConstants.Variables.Knowledge.REPOSITORY);
                KnowledgeRepository knowledgeRepository = DefaultDataObjectFactory.createKnowledgeRepository(id, name, createTime);
                knowledgeRepository.setStorage(repository);
                repositories.add(knowledgeRepository);
            }
        }
        return repositories;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getLinks(java.lang.String)
     */
    public List<KnowledgeLink> getLinks(String knowledgeObjectID) throws OntologyErrorException
    {
        /** get the knowledgelinks. */
        final RecordSet knowledgeLinkRecords = processEngine.getKnowledgeObjectLinks(knowledgeObjectID);
        List<KnowledgeLink> links = new ArrayList<KnowledgeLink>();
        if (knowledgeLinkRecords != null && knowledgeLinkRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < knowledgeLinkRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeLinkRecord = knowledgeLinkRecords.getRecords()[i];
                String linkID = knowledgeLinkRecord.get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGELINK_ID);
                if (linkID != null && !linkID.equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
                {
                    KnowledgeLink link = this.getKnowledgeLink(linkID);
                    if (link != null)
                    {
                        links.add(link);
                    }
                }
            }
        }
        return links;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#addKnowledgeObject(java.lang.String, java.lang.String)
     */
    public void addKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException
    {
        RecordSet records = processEngine.addKnowledgeObject(knowledgeObjectID, processElementID);
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            LOG.debug("KnowledgeObject" + knowledgeObjectID + " was susscessfully added to processelement with id   " + processElementID);
        }
        else
        {
            LOG.error("Fail to add KnowledgeObject  " + knowledgeObjectID + "  to   processelement with id   " + processElementID);
            throw new IllegalStateException("Could not add knowledgeobject with id = " + knowledgeObjectID + "  to the element  with id  = "
                    + processElementID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#removeKnowledgeObject(java.lang.String, java.lang.String)
     */
    @Override
    public void removeKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException
    {
        KnowledgeObjectArray existingObjects = getKnowledgeObjects(processElementID);
        commonEngine.clearRelationValue(processElementID, Relation.Slots.HAS_KNOWLEDGEOBJECTS);
        Iterator<KnowledgeObject> it = existingObjects.iterator();
        while (it.hasNext())
        {
            KnowledgeObject object = it.next();
            if (object.getID().equals(knowledgeObjectID))
            {
                it.remove();
                break;
            }
        }
        it = existingObjects.iterator();
        while (it.hasNext())
        {
            KnowledgeObject object = it.next();
            this.addKnowledgeObject(object.getID(), processElementID);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#createKnowledgeObject(java.lang.String, java.lang.String, java.lang.String)
     */
    public KnowledgeObject createKnowledgeObject(String relationID, String name, String userID) throws OntologyErrorException
    {
        Validate.notNull(relationID);
        Validate.notNull(name);
        Validate.notNull(userID);
        String relation = relationID;
        LOG.debug("createKnowledgeObject relation ID is: " + relationID);
        String knowledgeObjectID = null;
        /** check if relationID is the ID of a template. If not checks if is an instance. */
        ProcessElement relationTemplate = common.getProcessElementTemplateInformation(relationID);
        if (relationTemplate != null && !relationTemplate.getID().equals(ProcessEngineConstants.Variables.Common.NULL_VALUE))
        {
            relation = relationTemplate.getID();
        }
        RecordSet knowledgeObjectRecords = processEngine.genKnowledgeObject(relation, name, userID);
        if (knowledgeObjectRecords != null && knowledgeObjectRecords.getNoOfRecords() > 0)
        {
            knowledgeObjectID = knowledgeObjectRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN);
        }
        return this.getKnowledgeObject(knowledgeObjectID);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#createKnowledgeObject(java.lang.String, java.lang.String)
     */
    public KnowledgeObject createKnowledgeObject(String name, String userID) throws OntologyErrorException
    {

        Validate.notNull(name);
        Validate.notNull(userID);
        String knowledgeObjectID = null;

        RecordSet knowledgeObjectRecords = processEngine.genKnowledgeObject(name, userID);
        if (knowledgeObjectRecords != null && knowledgeObjectRecords.getNoOfRecords() > 0)
        {
            knowledgeObjectID = knowledgeObjectRecords.getRecords()[0].get(ProcessEngineConstants.Variables.Knowledge.KNOWLEDGEOBJECT_ID_EN);
        }
        return this.getKnowledgeObject(knowledgeObjectID);
    }

    /************
     * PRIVATE METHODS
     * 
     ************************************************************/

    private String getKnowledgeObjectCreatorNumber(String knowledgeObjectID) throws OntologyErrorException
    {
        String result = null;
        final RecordSet knowledgeObjectCreatorRecords = processEngine.getKnowledgeObjectCreatorNumber(knowledgeObjectID);
        if (knowledgeObjectCreatorRecords.getNoOfRecords() > 0)
        {
            for (int i = 0; i < knowledgeObjectCreatorRecords.getNoOfRecords(); i++)
            {
                final Hashtable<String, String> knowledgeObjectCreatorRecord = knowledgeObjectCreatorRecords.getRecords()[i];
                final String personID = knowledgeObjectCreatorRecord.get(ProcessEngineConstants.Variables.Common.NUMBER_PERSON);
                result = personID;
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getKnowledgeObjectsOfProcess(java.lang.String)
     */
    @Override
    public KnowledgeObjectArray getKnowledgeObjectsOfProcess(String processID) throws OntologyErrorException
    {
        final RecordSet result = processEngine.getKnowledgeObjectsOfProcess(processID);
        KnowledgeObjectArray knowledgeObjects = new KnowledgeObjectArray();
        if (result != null && result.getNoOfRecords() > 0)
        {
            for (int i = 0; i < result.getNoOfRecords(); i++)
            {
                Hashtable<String, String> record = result.getRecords()[i];
                String knowledgeObjectID = record.get(EngineConstants.Variables.KnowledgeObject.KNOWLEDGE_OBJECT_ID);
                KnowledgeObject knowledgeObject = this.getKnowledgeObject(knowledgeObjectID);
                knowledgeObjects.add(knowledgeObject);
            }
        }
        return knowledgeObjects;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.coreengine.entities.KnowledgeEntity#getRelationsOfKnowledgeObject(java.lang.String)
     */
    @Override
    public ObjectArray getRelationsOfKnowledgeObject(String knowldgeObjectID) throws OntologyErrorException
    {
        ObjectArray results = new ObjectArray();
        final RecordSet records = processEngine.getRelationsOfKnowledgeObject(knowldgeObjectID);
        if (records.getResult().equals(AlgernonConstants.OK))
        {
            for (int i = 0; i < records.getNoOfRecords(); i++)
            {
                String elementID = records.getRecords()[i].get(EngineConstants.Variables.Common.ELEMENT_ID);
                String name = records.getRecords()[i].get(EngineConstants.Variables.Common.NAME);
                String description = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.DESCRIPTION_DEFAULT);
                String className = records.getRecords()[i].get(EngineConstants.Variables.Common.CLASS);
                String createTime = records.getRecords()[i].get(ProcessEngineConstants.Variables.Common.CREATE_TIME);

                ProcessElement processElement = DefaultDataObjectFactory.createProcessElement(elementID, name, createTime);
                processElement.setDescription(description);
                processElement.setClassName(className);
                results.add(processElement);
            }
        }

        return results;
    }

}
