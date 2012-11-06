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

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.prowim.datamodel.collections.ActivityArray;
import org.prowim.datamodel.collections.DomainKnowledgeArray;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessKnowledgeArray;
import org.prowim.datamodel.collections.ProcessTypesArray;
import org.prowim.datamodel.factory.ProwimDataObjectFactory;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Knowledge;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Collects all methods to receive {@link Process} , {@link Knowledge}, {@link Activity}, {@link KnowledgeObject}, {@link KnowledgeDomain} and ProcessTypes.
 * 
 * @author Saad Wardi
 * @version $Revision: 3646 $
 */
@WebService(name = ServiceConstants.PROWIM_KNOWLEDGE_REMOTE, targetNamespace = ServiceConstants.PROWIM_KNOWLEDGE_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({ ProwimDataObjectFactory.class })
public interface KnowledgeRemote
{

    /**
     * Gets all processes.
     * 
     * @return not null {@link ProcessArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessArray getAllProcesses() throws OntologyErrorException;

    /**
     * a method that returns all ProwimProcessKnowledge beans
     * 
     * @return returns org.prowim.services.ejb.knowledge.ProcessKnowledgeArray. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessKnowledgeArray getProcessKnowledge() throws OntologyErrorException;;

    /**
     * returns all Activities
     * 
     * @param template the activity template id.
     * 
     * @return returns org.prowim.services.ejb.knowledge.ProcessKnowledgeArray. If no items exist, an empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ActivityArray getActivities(String template) throws OntologyErrorException;

    /**
     * a method that returns all Knowledge objects to an activity id. .
     * 
     * @param activityID the activity id.
     * @return array of KnowledgeObject.If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    KnowledgeObjectArray getActivityKnowledge(String activityID) throws OntologyErrorException;

    /**
     * a method that returns an array of top domains.
     * 
     * @return array of domain.
     * @throws OntologyErrorException if error occurs in ontology back end
     */

    @WebMethod
    @WebResult(partName = "return")
    DomainKnowledgeArray getTopDomainKnowledge() throws OntologyErrorException;;

    /**
     * Gets Knowledgeobjects to a domain.
     * 
     * @param domainID the domain ID.
     * @return not null {@link KnowledgeObjectArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    KnowledgeObjectArray getDomainKnowledgeObjects(String domainID) throws OntologyErrorException;

    /**
     * Gets the subdomains.
     * 
     * @param domainID the domain ID. If no items exist, a new empty list is returned.
     * 
     * 
     * @return not null {@link DomainKnowledgeArray}. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    DomainKnowledgeArray getSubDomainKnowledge(String domainID) throws OntologyErrorException;

    /**
     * Gets all registered processtypes.
     * 
     * @return array of process types. If no items exist, a new empty list is returned.
     */
    @WebMethod
    @WebResult(partName = "return")
    ProcessTypesArray getProcessTypes();

    /**
     * Creates a new {@link KnowledgeObject} and add it to the relation with the id = relationID.
     * 
     * @param relationID the ID of the ProcessElement
     * @param name the name of the {@link KnowledgeObject}
     * @return not null {@link KnowledgeObject}
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod(operationName = "createKnowledgeObjectWithElement")
    @WebResult(partName = "return")
    KnowledgeObject createKnowledgeObject(String relationID, String name) throws OntologyErrorException;

    /**
     * Creates a new {@link KnowledgeObject}.
     * 
     * @param name the name of the {@link KnowledgeObject}
     * @return not null {@link KnowledgeObject}
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod(operationName = "createKnowledgeObject")
    @WebResult(partName = "return")
    KnowledgeObject createKnowledgeObject(String name) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID ID of the {@link KnowledgeObject}.
     * @return not null, if the knowledgeobject with the ID exists, otherwise exception.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    KnowledgeObject getKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Creates a new {@link KnowledgeLink}.
     * 
     * @param knowledgeObjectID the not null ID of the {@link KnowledgeObject}
     * @param name the not null name of the {@link KnowledgeLink}
     * @param wsID the not null ID of a repository (Wissensspeicher) {@link KnowledgeRepository}.
     * @param link the not null hyperlink.
     * 
     * @return not null {@link KnowledgeLink}
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    KnowledgeLink createKnowledgeLink(String knowledgeObjectID, String name, String wsID, String link) throws OntologyErrorException;

    /**
     * Gets all {@link KnowledgeRepository} registered in the ontology.
     * 
     * @return not null {@link KnowledgeRepositoryArray} of KnowledgeRepository objects. If not item exists an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    KnowledgeRepositoryArray getKnowledgeRepositories() throws OntologyErrorException;

    /**
     * Saves a {@link KnowledgeObject} and overrides all attributes of this knowledgeObject, so this object must exist. Renames, Sets the list of dominators persons and the {@link KnowledgeLink}.
     * 
     * @param knowledgeObject the {@link KnowledgeLink}.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void saveKnowledgeObject(KnowledgeObject knowledgeObject) throws OntologyErrorException;

    /**
     * Updates a {@link KnowledgeObject} with the given parameters. Renames, Sets the list of dominators persons and the {@link KnowledgeLink}.<br>
     * 
     * 
     * @param knowledgeObjectID not null ID of the {@link KnowledgeObject}.
     * @param name not null new name of knowledgeObject.
     * @param knowledgeLinks not null list of the {@link KnowledgeLink} to be assigned to this {@link KnowledgeObject}.
     * @param dominators a not null list of the dominators persons.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void updateKnowledgeObject(String knowledgeObjectID, String name, ObjectArray knowledgeLinks, PersonArray dominators)
                                                                                                                         throws OntologyErrorException;

    /**
     * Deletes an instance of {@link KnowledgeObject}.
     * 
     * @param knowledgeObjectID the ID of the knowledgeobject.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void deleteKnowledgeObject(String knowledgeObjectID) throws OntologyErrorException;

    /**
     * Gets the business domains assigned to the process instance with the ID = processInstanceID.
     * 
     * @param processInstanceID ID of the process instance.
     * @return not null {@link DomainKnowledgeArray}. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    DomainKnowledgeArray getBusinessDomains(String processInstanceID) throws OntologyErrorException;

    /**
     * Search for {@link KnowledgeObject} that contains the keyWord in his name or in the name of their inculded {@link KnowledgeLink}'s.
     * 
     * @param keyWord the key word
     * @return not null {@link KnowledgeObjectArray}. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    KnowledgeObjectArray searchKnowledges(String keyWord) throws OntologyErrorException;

    /**
     * Search for objects that contains the keyWord in his name or in the name of their inculded {@link KnowledgeLink}'s.
     * 
     * @param keyWord the key word
     * @return not null {@link ObjectArray}. If no item exists an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray searchKeyWord(String keyWord) throws OntologyErrorException;

    /**
     * Deletes an instance from the ontology.
     * 
     * @param id instance ID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void deleteObject(String id) throws OntologyErrorException;

    /**
     * Gets all KnowledgeObjects stored in the database.
     * 
     * @return not null {@link KnowledgeObjectArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    KnowledgeObjectArray getKnowledgeObjects() throws OntologyErrorException;

    /**
     * Creates a knowledge domain.
     * 
     * @param name not null domain name.
     * @param description not null description.
     * @return not null {@link KnowledgeDomain}.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod(operationName = "createKnowledgeDomain")
    @WebResult(partName = "return")
    KnowledgeDomain createKnowledgeDomain(String name, String description) throws OntologyErrorException;

    /**
     * Creates a knowledge domain.
     * 
     * @param name not null domain name.
     * @param description not null description.
     * @param parentDomainID not null parent domain ID.
     * @return not null {@link KnowledgeDomain}.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod(operationName = "createChildKnowledgeDomain")
    @WebResult(partName = "return")
    KnowledgeDomain createKnowledgeDomain(String name, String parentDomainID, String description) throws OntologyErrorException;

    /**
     * Adds an existing KnowledgeObject to a processElement.
     * 
     * @param knowledgeObjectID not null ID of the {@link KnowledgeObject}.
     * @param processElementID not null ID of a ProcessElement like : Process, Activity, Person, etc.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void addKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException;

    /**
     * Removes an existing KnowledgeObject to a processElement.
     * 
     * @param knowledgeObjectID not null ID of the {@link KnowledgeObject}.
     * @param processElementID not null ID of a ProcessElement like : Process, Domain, Activity, Person, etc.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void removeKnowledgeObject(String knowledgeObjectID, String processElementID) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject} `s to a processelement.
     * 
     * @param processElementID not null processelement id.
     * @return array of {@link KnowledgeObject}
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod(operationName = "getKnowledgeObjectsToProcessElements")
    @WebResult(partName = "return")
    KnowledgeObjectArray getKnowledgeObjects(String processElementID) throws OntologyErrorException;

    /**
     * Gets the {@link KnowledgeObject} `s to given {@link Process}id. This included also the knowledge objects of {@link ProcessElement}s.
     * 
     * @param processID not null {@link Process} id.
     * @return array of {@link KnowledgeObject}
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod(operationName = "getKnowledgeObjectsOfProcess")
    @WebResult(partName = "return")
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
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray getRelationsOfKnowledgeObject(String knowldgeObjectID) throws OntologyErrorException;

}
