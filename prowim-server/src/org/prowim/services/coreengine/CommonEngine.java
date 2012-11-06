/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-17 15:26:47 +0100 (Do, 17 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/CommonEngine.java $
 * $LastChangedRevision: 5045 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.admin.AdminRemote;
import org.prowim.services.ejb.commons.CommonRemote;
import org.prowim.services.ejb.organization.OrganizationRemote;



/**
 * This interface contains all the functions that can be shared by other engines.
 * 
 * @author Saad Wardi
 * @version $Revision: 5045 $
 */
@Local
public interface CommonEngine
{

    /**
     * Creates a new instance in the ontology.
     * 
     * @param oid not null object ID.
     * @param name not null name.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet addInstance(String oid, String name) throws OntologyErrorException;

    /**
     * Clears all values from the given relation of a given frame.
     * 
     * @param frameID The frame on which the relation occurs.
     * @param slotID the ID of the slot on the frame.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet clearRelationValue(String frameID, String slotID) throws OntologyErrorException;

    /**
     * Sets the value of a slot.
     * 
     * @param frameID not null frame id.
     * @param slotID not null slot id.
     * @param value not null slot value.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet setSlotValue(String frameID, String slotID, String value) throws OntologyErrorException;

    /**
     * Get the value from the given relation of a given frame.
     * 
     * The slot has to have only a single cardinality! Otherwise null is returned.
     * 
     * @param instanceID The element on which the relation occurs.
     * @param slot the name of the slot on the frame.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getSlotValue(String instanceID, String slot) throws OntologyErrorException;

    /**
     * Gets the display name for an instance..
     * 
     * @param id the instance ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getName(String id) throws OntologyErrorException;

    /**
     * {@link CommonRemote#setDescription(String, String)}.
     * 
     * @param id not null element instance ID.
     * @param value not null description.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet setDescription(String id, String value) throws OntologyErrorException;

    /**
     * {@link CommonRemote#getDescription(String)}.
     * 
     * @param id not null element ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getDescription(String id) throws OntologyErrorException;

    /**
     * Gets the rule name.
     * 
     * @param slotID the ID o the slot.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getConstraintRule(String slotID) throws OntologyErrorException;

    /**
     * Gets the attributes and the relations defined for the object with the given ID.
     * 
     * @param id not null object ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getInstanceProperties(String id) throws OntologyErrorException;

    /**
     * Gets the attributes and the relations defined for the object with the given ID.
     * 
     * @param id not null object ID.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet[] getInstanceRelations(String id) throws OntologyErrorException;

    /**
     * Gets the process elements.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getProcessElements() throws OntologyErrorException;

    /**
     * Gets the process elements instances.
     * 
     * @param processID not null process ID.
     * @param element not null element name.
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getProcessElementsInstances(String processID, String element) throws OntologyErrorException;

    /**
     * Deletes an instance/element in ontology. It deletes only the element itself and not the relation of element to other elements.
     * 
     * @param instanceID ID of the instance/element. Not null.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet deleteInstance(String instanceID) throws OntologyErrorException;

    /**
     * Gets the direct class of an element.
     * 
     * @param id not null instance ID.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getDirectClassOfInstance(String id) throws OntologyErrorException;

    /**
     * Gets the ID, name and createTime attribute to a template.
     * 
     * @param processlementInstanceID the ID of the ProcessElement Instanz.
     * @return {@link RecordSet}
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getProcessElementTemplateInformation(String processlementInstanceID) throws OntologyErrorException;

    /**
     * {@link OrganizationRemote#getPreSelection(String)}.
     * 
     * @param roleID not null role ID.
     * @return presSelection not null {@link PersonArray}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getPreSelection(final String roleID) throws OntologyErrorException;

    /**
     * {@link AdminRemote#getOntologyVersion()}.
     * 
     * @return {@link RecordSet}.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    RecordSet getOntologyVersion() throws OntologyErrorException;

    /**
     * True if the instance is global.
     * 
     * @param instanceID the frame ID
     * @return True if the instance is global.
     * @throws OntologyErrorException if an error occurs on the ontology back end
     */
    boolean isGlobal(String instanceID) throws OntologyErrorException;

}
