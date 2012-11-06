/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-17 15:26:47 +0100 (Do, 17 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/CommonHelper.java $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.services.coreengine;

import javax.ejb.Local;

import org.prowim.datamodel.collections.InstancePropertyArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.editor.InstancePropertiesNames;
import org.prowim.datamodel.editor.InstancePropertiesValues;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.commons.CommonRemote;



/**
 * Common functions that can be used from every engine.
 * 
 * @author Saad Wardi
 * @version $Revision: 5045 $
 */
@Local
public interface CommonHelper
{

    /**
     * Sets the denotation value.<br>
     * NOTE: The denotation value is equals the name.
     * 
     * @param frameID not null frame ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setDenotation(final String frameID) throws OntologyErrorException;

    /**
     * Gets the display name defined for the object with the given id.
     * 
     * @param id the instance id.
     * @return the display name.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getName(final String id) throws OntologyErrorException;

    /**
     * {@link CommonRemote#setDescription(String, String)}.
     * 
     * @param id not null element ID.
     * @param description not null description.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setDescription(final String id, final String description) throws OntologyErrorException;

    /**
     * {@link CommonRemote#getDescription(String)}.
     * 
     * @param id not null element ID.
     * @return not null description.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getDescription(final String id) throws OntologyErrorException;

    /**
     * Renames the instance name.
     * 
     * @param id instance ID.
     * @param newName the new name to set for the given ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void rename(final String id, final String newName) throws OntologyErrorException;

    /**
     * Gets the ID of the template to a relation.<br/>
     * NOTE: the relation can be a template or an instance.
     * 
     * @param relationID the ID of the relation (instance).
     * @return the not null template ID.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getTemplateID(final String relationID) throws OntologyErrorException;

    /**
     * {@link CommonRemote#getProperties(String)}.
     * 
     * @param instanceID not null instance ID.
     * @return not null {@link InstancePropertyArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    InstancePropertyArray getProperties(final String instanceID) throws OntologyErrorException;

    /**
     * {@link CommonRemote#getRelations(String)}.
     * 
     * @param instanceID not null instance ID.
     * @return not null {@link InstancePropertyArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    InstancePropertyArray getRelations(final String instanceID) throws OntologyErrorException;

    /**
     * Clears a slot.
     * 
     * @param frameID the not null frame ID.
     * @param slotID the not null slot ID.
     * @return SUCCEEDDED or FAILED.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String clearRelationValue(final String frameID, final String slotID) throws OntologyErrorException;

    /**
     * Set the value of a slot.
     * 
     * @param frameID not null frame ID.
     * @param slotID not null slot ID.
     * @param value not null value
     * @return SUCCEEDDED or FAILED.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String setSlotValue(final String frameID, final String slotID, final String value) throws OntologyErrorException;

    /**
     * Get the value of a slot.
     * 
     * @param instanceID not null frame ID
     * @param slotID not null slot ID
     * @return the value, may be null if query fails
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getSlotValue(final String instanceID, final String slotID) throws OntologyErrorException;

    /**
     * Deletes an instance/element in ontology. It deletes only the element itself and not the relation of element to other elements.
     * 
     * @param instanceID ID of the instance/element. Not null.
     * @return status of the delete. "FAILED" or "SUCCESSFULL"
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String deleteInstance(final String instanceID) throws OntologyErrorException;

    /**
     * Gets the process elements of a process. That means that the process gets only the elements with out the instances.<br>
     * For example: You can receive all possible elements with this method. After invoking you can receive the instances with {@link CommonHelper#getProcessElementsInstances(String, String)}. Then
     * 
     * 
     * @return not null {@link ObjectArray}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getProcessElements() throws OntologyErrorException;

    /**
     * Gets the process elements instances.
     * 
     * @param processID not null process ID.
     * @param dataObjectClassName not null data object class name. It will be returned in an ObjectArray from {@link CommonHelper#getProcessElements()}.
     * @return not null {@link ObjectArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ObjectArray getProcessElementsInstances(final String processID, final String dataObjectClassName) throws OntologyErrorException;

    /**
     * returns the class of the selected element.
     * 
     * @param id the element ID.
     * @return not null direct class.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getDirectClassOfInstance(final String id) throws OntologyErrorException;

    /**
     * Gets the ID, name and createTime attribute to a template.
     * 
     * @param processlementInstanceID the ID of the ProcessElement Instanz.
     * @return {@link ProcessElement}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProcessElement getProcessElementTemplateInformation(final String processlementInstanceID) throws OntologyErrorException;

    /**
     * Gets the ontology version.
     * 
     * @return not null ontology version.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    String getOntologyVersion() throws OntologyErrorException;

    /**
     * Gets the properties to an instance.
     * 
     * @param id the instance ID.
     * @return {@link InstancePropertiesNames}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    InstancePropertiesNames getInstancePropertiesNames(String id) throws OntologyErrorException;

    /**
     * Gets the properties to an instance.
     * 
     * @param id the instance ID.
     * @return {@link InstancePropertiesNames}
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    InstancePropertiesValues getInstancePropertiesValues(String id) throws OntologyErrorException;

    /**
     * Checks if the instance is global.
     * 
     * @param instanceID the ID of an instance.
     * @return true if the instance is global.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    boolean isGlobal(String instanceID) throws OntologyErrorException;

    /**
     * {@link ProcessHelper#setParameterValue(org.prowim.datamodel.prowim.Parameter)}.
     * 
     * @param paramArray not null parameterArray. An empta array is permited.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setParameter(ParameterArray paramArray) throws OntologyErrorException;

}
