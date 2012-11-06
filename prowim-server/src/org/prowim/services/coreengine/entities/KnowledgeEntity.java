/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-10-18 18:27:07 +0200 (Di, 18 Okt 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/KnowledgeEntity.java $
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
package org.prowim.services.coreengine.entities;

import java.util.List;

import javax.ejb.Local;

import org.prowim.datamodel.collections.DomainKnowledgeArray;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.knowledge.KnowledgeRemote;



/**
 * Methods to create, deletes, get attributes, search and fetch data for Knowledges.
 * 
 * @author Saad Wardi
 * @version $Revision: 5100 $
 */
@Local
public interface KnowledgeEntity
{
    /**
     * Gets the {@link KnowledgeObject} to a relation.
     * 
     * @param id the relation ID.
     * @return not null array of {@link KnowledgeObject}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObjectArray getKnowledgeObjects(String id) throws OntologyErrorException;

    /**
     * Gets {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID the ID of the {@link KnowledgeObject}
     * @return {@link KnowledgeObject} or null, if no object exists with the ID = knowledgeLinkID
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObject getKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets {@link KnowledgeObject}.
     * 
     * @param knowledgeObject the ID of the {@link KnowledgeObject}
     * @return {@link KnowledgeObject} or null, if no object exists with the ID = knowledgeLinkID
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObject getKnowledgeObject(KnowledgeObject knowledgeObject) throws OntologyErrorException;

    /**
     * Gets the knowledgeobject creater ID.
     * 
     * @param knowledgeObjectID not null knowledgeobject ID.
     * @return not null creater person ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getKnowledgeObjectCreatorID(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeLink}.
     * 
     * @param knowledgeLinkID ID of the {@link KnowledgeLink}.
     * @return {@link KnowledgeLink} or null, if no object exists with the ID = knowledgeLinkID
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeLink getKnowledgeLink(String knowledgeLinkID) throws OntologyErrorException;

    /**
     * Deletes an instance KnowledgeObject.
     * 
     * @param knowledgeObjectID ID of the {@link KnowledgeObject}
     * @param userID the ID of the user.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void deleteKnowledgeObject(String knowledgeObjectID, String userID) throws OntologyErrorException;

    /**
     * Gets all knowledgeobjects.
     * 
     * @return not null {@link KnowledgeObjectArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObjectArray getKnowledgeObjects() throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeDomain}.
     * 
     * @param domainKnowledgeID ID of {@link KnowledgeDomain}
     * @return {@link KnowledgeDomain} or null if no item with the domainKnowledgeID exists.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeDomain getKnowledgeDomain(String domainKnowledgeID) throws OntologyErrorException;

    /**
     * Gets the business domains assigned to the process instance with the ID = processInstanceID.
     * 
     * @param processInstanceID ID of the process instance.
     * @return not null {@link DomainKnowledgeArray}. If no item exists an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    DomainKnowledgeArray getBusinessDomains(String processInstanceID) throws OntologyErrorException;

    /**
     * {@link KnowledgeRemote#createKnowledgeDomain(String name, String description)}.
     * 
     * @param name not null domain name.
     * @param description not null description.
     * @return not null {@link KnowledgeDomain}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeDomain createKnowledgeDomain(String name, String description) throws OntologyErrorException;

    /**
     * {@link KnowledgeRemote#createKnowledgeDomain(String name, String description)}.
     * 
     * @param name not null domain name.
     * @param description not null description.
     * @param parentDomainID not null parent domain.
     * @return not null {@link KnowledgeDomain}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeDomain createKnowledgeDomain(String name, String parentDomainID, String description) throws OntologyErrorException;

    /**
     * Gets all {@link KnowledgeRepository} registered in the ontology.
     * 
     * @return not null {@link List} of KnowledgeRepository objects. If no item exists an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeRepositoryArray getKnowledgeRepositories() throws OntologyErrorException;

    /**
     * Gets the links for a {@link KnowledgeLink}.
     * 
     * @param knowledgeObjectID the knowledgeObjetc ID.
     * @return not null {@link List} of {@link KnowledgeLink} objects.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    List<KnowledgeLink> getLinks(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * {@link KnowledgeRemote#addKnowledgeObject(String, String)}.
     * 
     * @param knowledgeObjectID not null knowledgeobject ID.
     * @param processElementID not null element ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void addKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException;

    /**
     * {@link KnowledgeRemote#removeKnowledgeObject(String, String)}.
     * 
     * @param knowledgeObjectID not null knowledgeobject ID.
     * @param processElementID not null element ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void removeKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException;

    /**
     * Creates a new KnowledgeObject and add it to a ProcessElement relation..
     * 
     * @param relationID the ID of the ProcessElement relation.
     * @param name the name of the KnowledgeObject.
     * @param userID the ID of the user that create it.
     * @return the ID of the created KnowledgeObject
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObject createKnowledgeObject(String relationID, String name, String userID) throws OntologyErrorException;

    /**
     * Creates a new KnowledgeObject and add it to a ProcessElement relation..
     * 
     * @param name the name of the KnowledgeObject.
     * @param userID the ID of the user that create it.
     * @return the ID of the created KnowledgeObject
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObject createKnowledgeObject(String name, String userID) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject} `s to given {@link Process}id. This included also the knowledge objects of {@link ProcessElement}s.
     * 
     * @param processID not null {@link Process} id.
     * @return array of {@link KnowledgeObject}
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    KnowledgeObjectArray getKnowledgeObjectsOfProcess(String processID) throws OntologyErrorException;

    /**
     * Returns all {@link ProcessElement}s, which use given {@link KnowledgeObject} or are in relation with this. <br>
     * The relation is "wird_benoetigt_fuer"
     * 
     * @param knowldgeObjectID id of given {@link KnowledgeObject}. Not null.
     * 
     * @return not null {@link ObjectArray}. It can be a list of {@link ProcessElement}s. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    ObjectArray getRelationsOfKnowledgeObject(String knowldgeObjectID) throws OntologyErrorException;
}
