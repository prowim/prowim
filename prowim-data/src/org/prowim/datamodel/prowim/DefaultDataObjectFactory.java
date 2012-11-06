/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-20 11:46:06 +0200 (Tue, 20 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/factory/impl/DefaultDataObjectFactory.java $
 * $LastChangedRevision: 4338 $
 *------------------------------------------------------------------------------
 * (c) 15.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.prowim;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRegistry;

import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.factory.ProwimDataObjectFactory;
import org.prowim.datamodel.search.KnowledgeSearchDMSResult;
import org.prowim.datamodel.search.KnowledgeSearchLinkResult;
import org.prowim.datamodel.search.KnowledgeSearchResult;
import org.prowim.datamodel.search.KnowledgeSearchWikiResult;
import org.prowim.datamodel.search.SearchProcessResult;
import org.prowim.datamodel.search.KnowledgeSearchResult.KnowledgeSource;
import org.prowim.datamodel.search.KnowledgeSearchResult.KnowledgeType;



/**
 * Contains factory methods for each <br/>
 * DataObject in the org.prowim.datamodel package.
 * 
 * @author Saad Wardi
 * 
 */
@XmlRegistry
public final class DefaultDataObjectFactory implements ProwimDataObjectFactory
{

    /**
     * Create a new ProwimDataObjectFactory that can be used to create new instances of Data objects and their collections.
     * 
     */
    private DefaultDataObjectFactory()
    {
    }

    /**
     * Creates a {@link Activity}.
     * 
     * @param id {@link Activity#setID(String)}.
     * @param createTime {@link Activity#setCreateTime(String)}.
     * @param name {@link Activity#setName(String)}.
     * @return an Instance of {@link Activity}.
     */
    public static Activity createActivity(String id, String createTime, String name)
    {
        return new Activity(id, createTime, name);
    }

    /**
     * Creates a {@link Person}.
     * 
     * @param id {@link Person#setID(String)}
     * @param firstName {@link Person#setFirstName(String)}
     * @param lastName {@link Person#setLastName(String)}
     * @param shortName {@link Person#setUserName(String)}
     * @param password {@link Person#setPassword(String)}
     * @return an instance {@link Person}
     */
    public static Person createPerson(String id, String firstName, String lastName, String shortName, String password)
    {
        Person person = new Person(id, firstName, lastName, shortName, password);
        return person;
    }

    /**
     * Create a template of {@link Process}.
     * 
     * @param templateID {@link Process#setTemplateID(String)}.
     * @param name the name. {@link Process#setName(String)}.
     * @return a not null {@link Process}.
     * 
     */
    public static Process createProwimProcessTemplate(String templateID, String name)
    {
        return new Process(templateID, name);
    }

    /**
     * Create an instance of {@link Process }.
     * 
     * @param templateID {@link Process#setTemplateID(String)}.
     * @param instanceID {@link Process#setInstanceID(String)}.
     * @param name {@link Process#setName(String)}.
     * @return a not null {@link Process}.
     */
    public static Process createProwimProcessInstance(String templateID, String instanceID, String name)
    {
        return new Process(templateID, instanceID, name);
    }

    /**
     * Create a {@link KnowledgeObject}.
     * 
     * @param id not null id.
     * @param name not null name.
     * @param createTime not null createTime.
     * @return not null {@link KnowledgeObject}.
     */
    public static KnowledgeObject createKnowledgeObject(String id, String name, String createTime)
    {
        return new KnowledgeObject(id, name, createTime);
    }

    /**
     * Creates a {@link KnowledgeDomain}.
     * 
     * @param id {@link KnowledgeDomain#setID(String)}
     * @param name {@link KnowledgeDomain#setName(String)}
     * @param createTime {@link KnowledgeDomain#setCreateTime(String)}
     * @return an Instance of {@link KnowledgeDomain}.
     */
    public static KnowledgeDomain createKnowledgeDomain(String id, String name, String createTime)
    {
        return new KnowledgeDomain(id, name, createTime);
    }

    /**
     * Create an instance of {@link InformationType}.
     * 
     * @param id see {@link InformationType#setID(String)}
     * @param denotation {@link InformationType#setDenotation(String)}
     * @param minValue {@link InformationType#setMinValue(Long)}
     * @param maxValue {@link InformationType#setMaxValue(Long)}
     * @param contentString {@link InformationType#setContentString(String)}
     * @return an InformationType {@link InformationType}
     */
    public static InformationType createInformationType(String id, String denotation, Long minValue, Long maxValue, String contentString)
    {
        return new InformationType(id, denotation, minValue, maxValue, contentString);
    }

    /**
     * Create a of {@link Parameter }.
     * 
     * @param id the parameter id. {@link Parameter#setID(String) }
     * @param name the parameter name. {@link Parameter#setName(String) }
     * @param referenceID the process instance id. {@link Parameter#setReferenceID(String) }
     * @param infoTypeID the information type id. {@link Parameter#setInfoTypeID(String) }
     * @param value the {@link ObjectArray} value. {@link Parameter#setSelectedValues(ObjectArray) }
     * @param constraint value. {@link ParameterConstraint}
     * @return a not null {@link Parameter}.
     * 
     */
    public static Parameter createParameter(String id, String name, String referenceID, String infoTypeID, ObjectArray value,
                                            ParameterConstraint constraint)

    {
        Parameter parameter = new Parameter(id, name, referenceID, infoTypeID, value);
        parameter.setConstraint(constraint);
        return parameter;
    }

    /**
     * Creates a {@link Role} .
     * 
     * @param id {@link Role#setID(String)}
     * @param name {@link Role#setName(String)}
     * @param processID {@link Role#setReferenceID(String)}
     * @param type {@link Role#setType(String)}
     * @param createTime {@link Role#setCreateTime(String)}
     * @param persons {@link Role#setPersonsList(java.util.List)}
     * @return an Instance of {@link Role}
     */
    public static Role createRole(String id, String name, String createTime, String processID, String type, ArrayList<Person> persons)
    {
        Role role = new Role(id, name, createTime);
        role.setReferenceID(processID);
        role.setType(type);
        role.setPersonsList(persons);
        return role;
    }

    /**
     * Creates a {@link Role} .
     * 
     * @param id {@link Role#setID(String)}
     * @param name {@link Role#setName(String)}
     * @param createTime {@link Role#setCreateTime(String)}
     * @return an Instance of {@link Role}
     */
    public static Role createRole(String id, String name, String createTime)
    {
        Role role = new Role(id, name, createTime);
        return role;
    }

    /**
     * Creates a {@link ProcessElement} .
     * 
     * @param id {@link ProcessElement#setID(String)}
     * @param name {@link ProcessElement#setName(String)}
     * @param createTime {@link ProcessElement#setCreateTime(String)}
     * @return an Instance of {@link Role}
     */
    public static ProcessElement createProcessElement(String id, String name, String createTime)
    {
        ProcessElement processElement = new ProcessElement(id, name, createTime);
        return processElement;
    }

    /**
     * Creates a {@link Organization}.
     * 
     * @param id {@link Organization#setID(String)}
     * @param name {@link Organization#setName(String)}
     * @param createTime {@link Organization#setCreateTime(String)}
     * @param address {@link Organization#setAddress(String)}
     * @param email {@link Organization#setEmail(String)}
     * @param telefon {@link Organization#setTelefon(String)}
     * @param description {@link Organization#setTelefon(String)}
     * @return an Instance of {@link Organization}
     */
    public static Organization createOrganisation(String id, String name, String createTime, String address, String email, String telefon,
                                                  String description)
    {
        Organization organization = new Organization(id, name, createTime);
        organization.setAddress(address);
        organization.setEmail(email);
        organization.setTelefon(telefon);
        organization.setDescription(description);
        return organization;
    }

    /**
     * Creates a {@link Product}.
     * 
     * @param id {@link Product#setID(String)}
     * @param name {@link Product#setName(String)}
     * @param createTime {@link Product#setCreateTime(String)}
     * @param activityID {@link Product#setActivityID(String)}
     * @return an Instance of {@link Product}
     */
    public static Product createProduct(String id, String name, String createTime, String activityID)
    {
        Product product = new Product(id, name, createTime);
        product.setActivityID(activityID);
        return product;
    }

    /**
     * Creates a {@link Product}.
     * 
     * @param id {@link Product#setID(String)}
     * @param name {@link Product#setName(String)}
     * @param createTime {@link Product#setCreateTime(String)}
     * @return an Instance of {@link Product}
     */
    public static Product createProduct(String id, String name, String createTime)
    {
        Product product = new Product(id, name, createTime);
        return product;
    }

    /**
     * Creates a {@link ControlFlow}.
     * 
     * @param id {@link ControlFlow#setID(String)}
     * @param name {@link ControlFlow#setName(String)}
     * @param createTime {@link ControlFlow#setCreateTime(String)}
     * @return an Instance of {@link ControlFlow}
     */
    public static ControlFlow createControlFlow(String id, String name, String createTime)
    {
        return new ControlFlow(id, name, createTime);
    }

    /**
     * Creates a {@link Function}.
     * 
     * @param id {@link Function#setID(String)}
     * @param name {@link Function#setName(String)}
     * @param createTime {@link Function#setCreateTime(String)}
     * @return an Instance of {@link Function}
     */
    public static Function createFunction(String id, String name, String createTime)
    {
        return new Function(id, name, createTime);
    }

    /**
     * Creates a {@link InstanceProperty}.
     * 
     * @param name {@link InstanceProperty#setName(String)}
     * @param value {@link InstanceProperty#setValue(String)}
     * @return an Instance of {@link InstanceProperty}
     */
    public static InstanceProperty createInstanceProperty(String name, String value)
    {
        return new InstanceProperty(name, value);
    }

    /**
     * Creates a {@link Knowledge}.
     * 
     * @return not null {@link Knowledge}.
     */
    public static Knowledge createKnowledge()
    {
        return new Knowledge();
    }

    /**
     * Creates a {@link KnowledgeLink}.
     * 
     * @param id {@link KnowledgeLink#setID(String)}
     * @param name {@link KnowledgeLink#setName(String)}
     * @param createTime {@link KnowledgeLink#setCreateTime(String)}
     * @return not null {@link KnowledgeLink}.
     */
    public static KnowledgeLink createKnowledgeLink(String id, String name, String createTime)
    {
        return new KnowledgeLink(id, name, createTime);
    }

    /**
     * Creates a {@link KnowledgeRepository} .
     * 
     * @param id {@link ProcessElement#setID(String)}
     * @param name {@link ProcessElement#setName(String)}
     * @param createTime {@link ProcessElement#setCreateTime(String)}
     * @return not null {@link KnowledgeRepository}.
     */
    public static KnowledgeRepository createKnowledgeRepository(String id, String name, String createTime)
    {
        return new KnowledgeRepository(id, name, createTime);
    }

    /**
     * Creates a {@link KnowledgeSearchResult} .
     * 
     * @param title {@link KnowledgeSearchResult#setTitle(String)}.
     * @param type {@link KnowledgeSearchResult#setType(KnowledgeType)}.
     * @param source {@link KnowledgeSearchResult#setSource(KnowledgeSource)}.
     * @return not null {@link KnowledgeSearchResult}.
     */
    public static KnowledgeSearchResult createKnowledgeSearchResult(String title, KnowledgeType type, KnowledgeSource source)
    {
        return new KnowledgeSearchResult(title, type, source);
    }

    /**
     * 
     * Creates a {@link KnowledgeSearchDMSResult result of knowledge search in DMS}.
     * 
     * @param title {@link KnowledgeSearchDMSResult#setTitle(String)}
     * @param type {@link KnowledgeSearchDMSResult#setType(KnowledgeType)}
     * @param source {@link KnowledgeSearchDMSResult#setSource(KnowledgeSource)}
     * @param documentId {@link KnowledgeSearchDMSResult#setDocumentId(String)}
     * @param documentVersion {@link KnowledgeSearchDMSResult#setDocumentVersion(String)}
     * @param documentName {@link KnowledgeSearchDMSResult#setDocumentName(String)}
     * @return the result of knowledge search in DMS, can not be <code>NULL</code>
     */
    public static KnowledgeSearchDMSResult createKnowledgeSearchDMSResult(String title, KnowledgeType type, KnowledgeSource source,
                                                                          String documentId, String documentVersion, String documentName)
    {
        return new KnowledgeSearchDMSResult(title, type, source, documentId, documentVersion, documentName);
    }

    /**
     * 
     * Creates a {@link KnowledgeSearchWikiResult result of knowledge search in Wiki}.
     * 
     * @param title {@link KnowledgeSearchWikiResult#setTitle(String)}
     * @param type {@link KnowledgeSearchWikiResult#setType(KnowledgeType)}
     * @param source {@link KnowledgeSearchWikiResult#setSource(KnowledgeSource)}
     * @param hyperLink {@link KnowledgeSearchWikiResult#setHyperLink(String)}
     * @return the result of knowledge search in Wiki, can not be <code>NULL</code>
     */
    public static KnowledgeSearchWikiResult createKnowledgeSearchWikiResult(String title, KnowledgeType type, KnowledgeSource source, String hyperLink)
    {

        return new KnowledgeSearchWikiResult(title, type, source, hyperLink);
    }

    /**
     * 
     * Creates a {@link KnowledgeSearchLinkResult result of knowledge search for knowledge links in Ontology}.
     * 
     * @param title {@link KnowledgeSearchLinkResult#setTitle(String)}
     * @param type {@link KnowledgeSearchLinkResult#setType(KnowledgeType)}
     * @param source {@link KnowledgeSearchLinkResult#setSource(KnowledgeSource)}
     * @param knowledgeLinkId {@link KnowledgeSearchLinkResult#setKnowledgeLinkId(String)}
     * @param knowledgeLinkRepository {@link KnowledgeSearchLinkResult#setKnowledgeLinkRepository(String)}
     * @param knowledgeObject the {@link KnowledgeObject} which this {@link KnowledgeLink} belong to.
     * @return the result of knowledge search for knwoledge links in Ontology
     */
    public static KnowledgeSearchLinkResult createKnowledgeSearchLinkResult(String title, KnowledgeType type, KnowledgeSource source,
                                                                            String knowledgeLinkId, String knowledgeLinkRepository,
                                                                            String knowledgeObject)
    {

        return new KnowledgeSearchLinkResult(title, type, source, knowledgeLinkId, knowledgeLinkRepository, knowledgeObject);
    }

    /**
     * 
     * Creates a {@link KnowledgeSearchLinkResult result of knowledge search for knowledge links in Ontology}.
     * 
     * @param title {@link KnowledgeSearchLinkResult#setTitle(String)}
     * @param type {@link KnowledgeSearchLinkResult#setType(KnowledgeType)}
     * @param source {@link KnowledgeSearchLinkResult#setSource(KnowledgeSource)}
     * @param process Result {@link Process}
     * @return the result of knowledge search for knwoledge links in Ontology
     */
    public static SearchProcessResult createSearchProcessResult(String title, KnowledgeType type, KnowledgeSource source, Process process)
    {

        return new SearchProcessResult(title, type, source, process);
    }

    /**
     * Creates a {@link Mean}.
     * 
     * @param id {@link Mean#setID(String)}.
     * @param name {@link Mean#setName(String)}.
     * @param createTime {@link Mean#setCreateTime(String)}.
     * @return not null {@link Mean}.
     */
    public static Mean createMean(String id, String name, String createTime)
    {
        return new Mean(id, name, createTime);
    }

    /**
     * Creates a {@link OntologyVersion}.
     * 
     * @param id {@link OntologyVersion#setID(String)}.
     * @param label {@link OntologyVersion#setLabel(String)}.
     * @param createTime {@link OntologyVersion#setCreateTime(String)}.
     * @param creator {@link OntologyVersion#setCreator(String)}.
     * 
     * @return not null {@link OntologyVersion}.
     */
    public static OntologyVersion createOntologyVersion(String id, String label, String createTime, String creator)
    {
        return new OntologyVersion(id, label, createTime, creator);
    }

    /**
     * Creates a {@link ParameterConstraint}.
     * 
     * @param min {@link ParameterConstraint#setMin(Long)}
     * @param max {@link ParameterConstraint#setMax(Long)}
     * @param required {@link ParameterConstraint#setRequired(boolean)}
     * 
     * @return not null {@link ParameterConstraint}.
     */
    public static ParameterConstraint createParameterConstraint(Long min, Long max, boolean required)
    {
        return new ParameterConstraint(min, max, required);
    }

    /**
     * Creates a {@link ProcessInformation}.
     * 
     * @param id {@link ProcessInformation#setID(String)}.
     * @param name {@link ProcessInformation#setName(String)}.
     * @param createTime {@link ProcessInformation#setCreateTime(String)}.
     * @return not null {@link ProcessInformation}.
     */
    public static ProcessInformation createProcessInformation(String id, String name, String createTime)
    {
        return new ProcessInformation(id, name, createTime);
    }

    /**
     * Creates a {@link ProcessType}.
     * 
     * @param id {@link ProcessType#setID(String)}
     * @param name {@link ProcessType#setName(String)}
     * @param description {@link ProcessType#setDescription(String)}
     * 
     * @return not null {@link ProcessType}.
     */
    public static ProcessType createProcessType(String id, String name, String description)
    {
        ProcessType type = new ProcessType(id, name);
        type.setDescription(description);
        return type;
    }

    /**
     * Creates a {@link ProductWay}.
     * 
     * @param id {@link ProductWay#getID()}.
     * @param name {@link ProductWay#setName(String)}.
     * @param createTime {@link ProductWay#getCreateTime()}.
     * 
     * @return not null {@link ProductWay}.
     */
    public static ProductWay createProductWay(String id, String name, String createTime)
    {
        return new ProductWay(id, name, createTime);
    }

    /**
     * Creates a {@link ResultsMemory}.
     * 
     * @param id {@link ResultsMemory#getID()}.
     * @param name {@link ResultsMemory#setName(String)}.
     * @param createTime {@link ResultsMemory#getCreateTime()}.
     * 
     * @return not null {@link ResultsMemory}.
     */
    public static ResultsMemory createResultsMemory(String id, String name, String createTime)
    {
        return new ResultsMemory(id, name, createTime);
    }

    /**
     * Creates a {@link UpdateFrame}.
     * 
     * @param id {@link UpdateFrame#setID(String)}.
     * @param name {@link UpdateFrame#setID(String)}.
     * @param type {@link UpdateFrame#setType(String)}.
     * @param isNewFrame {@link UpdateFrame#setID(String)}.
     * 
     * @return not null {@link UpdateFrame}.
     */
    public static UpdateFrame createUpdateFrame(String id, String name, String type, boolean isNewFrame)
    {
        return new UpdateFrame(id, name, type, isNewFrame);
    }

    /**
     * Creates a {@link UpdateItem}.
     * 
     * @param updateFrame {@link UpdateItem#setUpdateFrame(String)}.
     * @param updateRule {@link UpdateItem#setUpdateRule(String)}.
     * 
     * @return not null {@link UpdateItem}.
     */
    public static UpdateItem createUpdateItem(String updateFrame, String updateRule)
    {
        return new UpdateItem(updateFrame, updateRule);
    }

    /**
     * Creates a {@link UpdatesLog}.
     * 
     * @param frameName {@link UpdatesLog#setFrameName(String)}.
     * @param updateScript {@link UpdatesLog#setUpdateScript(String)}.
     * @param status {@link UpdatesLog#setStatus(boolean)}.
     * @return not null {@link UpdatesLog}.
     */
    public static UpdatesLog createUpdatesLog(String frameName, String updateScript, boolean status)
    {
        return new UpdatesLog(frameName, updateScript, status);
    }

    /**
     * Creates a {@link UpdateWork}.
     * 
     * @param updateScript {@link UpdateWork#setUpdateScript(String)}
     * @param updateItems {@link UpdateWork#setUpdateItems(ObjectArray)}
     * @param versionLabel {@link UpdateWork#setVersionLabel(String)}
     * @return not null {@link UpdateWork}.
     */
    public static UpdateWork createUpdateWork(String updateScript, ObjectArray updateItems, String versionLabel)
    {
        return new UpdateWork(updateScript, updateItems, versionLabel);
    }

    /**
     * Creates a {@link Work}.
     * 
     * @param id {@link Work#setID(String)}.
     * @param name {@link Work#setName(String)}.
     * @param createTime {@link Work#setCreateTime(String)}.
     * 
     * @return not null {@link Work}.
     */
    public static Work createWork(String id, String name, String createTime)
    {
        return new Work(id, name, createTime);
    }
}
