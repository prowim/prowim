/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-27 09:41:40 +0200 (Mo, 27 Sep 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/ElementsEntity.java $
 * $LastChangedRevision: 4820 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine.entities;

import javax.ejb.Local;

import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Conjunction;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.Decision;
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
import org.prowim.services.coreengine.entities.impl.DefaultActivityEntity;
import org.prowim.services.coreengine.entities.impl.DefaultEntityManager;
import org.prowim.services.coreengine.entities.impl.DefaultMeanEntity;
import org.prowim.services.coreengine.entities.impl.DefaultProcessInformationEntity;



/**
 DATAOBJECTS_ELEMENTS_MAP.put("Aktivität", "Activity");
 DATAOBJECTS_ELEMENTS_MAP.put("Mittel", "Mean");
 DATAOBJECTS_ELEMENTS_MAP.put("Rolle", "Role");
 DATAOBJECTS_ELEMENTS_MAP.put("Prozessinformation", "ProcessInformation");
 DATAOBJECTS_ELEMENTS_MAP.put("Produktweg", "ProductWay");
 DATAOBJECTS_ELEMENTS_MAP.put("Kontrollfluss", "ControlFlow");
 DATAOBJECTS_ELEMENTS_MAP.put("Ablage", "ResultsMemory");
 DATAOBJECTS_ELEMENTS_MAP.put("Tätigkeit", "Task");
 DATAOBJECTS_ELEMENTS_MAP.put("Funktion", "Function");
 DATAOBJECTS_ELEMENTS_MAP.put("Produkt", "Product");

 */

/**
 * Provides an interface with access to all processelements.<br>
 * This interface is implemented by {@link DefaultEntityManager} to get the process elements objects data.
 * 
 * @author Saad Wardi
 * @version $Revision: 4820 $
 */
@Local
public interface ElementsEntity
{
    /**
     * {@link DefaultActivityEntity#getActivity(String)}.
     * 
     * @param id not null ID.
     * @return not null {@link Activity}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Activity getActivity(String id) throws OntologyErrorException;

    /**
     * {@link DefaultMeanEntity#getMean(String)}.
     * 
     * @param id not null ID.
     * @return not null {@link Mean}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Mean getMean(String id) throws OntologyErrorException;

    /**
     * {@link DefaultProcessInformationEntity#getProcessInformation(String)}.
     * 
     * @param id not null ID.
     * @return not null {@link ProcessInformation}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessInformation getProcessInformation(String id) throws OntologyErrorException;

    /**
     * Gets a control flow.
     * 
     * @param id not null control flow id.
     * @return not null {@link ControlFlow}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Role getRole(String id) throws OntologyErrorException;

    /**
     * Gets the ControlFlowFlow with id = controlFlowID .
     * 
     * @param id the control flow id.
     * @return not null {@link ControlFlow}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ControlFlow getControlFlow(String id) throws OntologyErrorException;

    /**
     * Gets {@link KnowledgeObject}.
     * 
     * @param id the ID of the {@link KnowledgeObject}
     * @return {@link KnowledgeObject} or null, if no object exists with the ID = knowledgeLinkID
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    KnowledgeObject getKnowledgeObject(String id) throws OntologyErrorException;

    /**
     * Gets {@link ResultsMemory}
     * 
     * @param id not null ID.
     * @return not null {@link ResultsMemory}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ResultsMemory getResultsMemory(String id) throws OntologyErrorException;

    /**
     * Gets {@link Work}
     * 
     * @param id not null ID.
     * @return not null {@link Work}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Work getWork(String id) throws OntologyErrorException;

    /**
     * Gets {@link Function}
     * 
     * @param id not null ID.
     * @return not null {@link Function}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Function getFunction(String id) throws OntologyErrorException;

    /**
     * Gets {@link Product}
     * 
     * @param id not null ID.
     * @return not null {@link Product}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    Product getProduct(String id) throws OntologyErrorException;

    /**
     * Gets {@link ProductWay}
     * 
     * @param id not null ID.
     * @return not null {@link ProductWay}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProductWay getProductWay(String id) throws OntologyErrorException;

    /**
     * 
     * Gets {@link Conjunction}.
     * 
     * @param id of the conjunction, can not be <code>NULL</code>
     * @return the conjunction, can not be <code>NULL</code>
     */
    Conjunction getConjunction(String id);

    /**
     * 
     * Gets the {@link Decision}.
     * 
     * @param id the id of the decision, can not be <code>NULL</code>
     * @return the decision, can not be <code>NULL</code>
     */
    Decision getDecision(String id);

}
