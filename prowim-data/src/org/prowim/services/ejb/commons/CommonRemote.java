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

package org.prowim.services.ejb.commons;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.prowim.datamodel.collections.InstancePropertyArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ParameterArray;
import org.prowim.datamodel.editor.InstancePropertiesNames;
import org.prowim.datamodel.editor.InstancePropertiesValues;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.Function;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.Product;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.datamodel.prowim.Work;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 ** Collects all methods to receive commons objects. Here all functionalities access to different objects from ontology, i. e. activity, person, role etc.
 * 
 * Independent from the type of object common methods are provided here, like {@link #rename(String, String)}.
 * 
 * @author Saad Wardi
 * @version $Revision: 3734 $
 */

@WebService(name = ServiceConstants.PROWIM_COMMON_REMOTE, targetNamespace = ServiceConstants.PROWIM_COMMON_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({ Activity.class, Role.class })
public interface CommonRemote
{

    /**
     * Renames an instance.
     * 
     * @param id not null ID of the instance.
     * @param newName the not null new name.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void rename(String id, String newName) throws OntologyErrorException;

    /**
     * Renames a version name.
     * 
     * @param instanceID not null ID of the instance.
     * @param newName the not null new name.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void renameVersion(String instanceID, String newName) throws OntologyErrorException;

    /**
     * Gets the display name for an instance..
     * 
     * @param id not null instance ID.
     * @return not null display name.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getName(String id) throws OntologyErrorException;

    /**
     * Sets the description to an element. Elements are entities from the ontology like: Prozess, Aktivität, Rolle, etc.
     * 
     * @param id not null id of an element instance.
     * @param description not null description. This is the description, which must be set to the selected element instance.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void setDescription(String id, String description) throws OntologyErrorException;

    /**
     * Gets the description of an element instance.
     * 
     * @param id not null element instance ID.
     * @return not null description or an empty String if no description available.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getDescription(String id) throws OntologyErrorException;

    /**
     * Gets the properties of an instance. An instance could be every element from the ontology. For example : Acitvity, Person, Role etc.
     * 
     * @param instanceID not null instance ID.
     * @return not null {@link InstancePropertyArray}. IF no item exists an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    InstancePropertyArray getProperties(String instanceID) throws OntologyErrorException;

    /**
     * Gets the relations of an instance. An instance could be every element from the ontology. For example : Acitvity, Person, Role etc.
     * 
     * @param instanceID not null instance ID.
     * @return not null {@link InstancePropertyArray}. IF no item exists an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    InstancePropertyArray getRelations(String instanceID) throws OntologyErrorException;

    /**
     * Gets the the relations defined for the object with the given ID.
     * 
     * @param id not null object ID.
     * @return {@link InstancePropertiesNames}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    InstancePropertiesNames getInstancePropertiesNames(String id) throws OntologyErrorException;

    /**
     * Gets the attributes defined for the object with the given ID.
     * 
     * @param id not null object ID.
     * @return {@link InstancePropertiesNames}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    InstancePropertiesValues getInstancePropertiesValues(String id) throws OntologyErrorException;

    /**
     * Gets the relations defined for the object with the given ID.
     * 
     * @param id not null object ID.
     * @return {@link InstancePropertiesNames}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    InstancePropertiesNames getInstanceRelationsNames(String id) throws OntologyErrorException;

    /**
     * Gets the the relations defined for the object with the given ID.
     * 
     * @param id not null object ID.
     * @return {@link InstancePropertiesNames}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    InstancePropertiesValues getInstanceRelationsValues(String id) throws OntologyErrorException;

    /**
     * Gets the ProcessElements.
     * 
     * @return not null {@link ObjectArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray getProcessElements() throws OntologyErrorException;

    /**
     * Gets the processelements instances.
     * 
     * @param processID not null process ID.
     * @param element not null element ID.
     * @return not null {@link ObjectArray}. If no item exists, an empty array is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ObjectArray getProcessElementsInstances(String processID, String element) throws OntologyErrorException;

    /**
     * Deletes an instance/element in ontology. It deletes only the element itself and not the relation of element to other elements.
     * 
     * @param id ID of the instance/element. Not null.
     * @return status.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String deleteInstance(String id) throws OntologyErrorException;

    /**
     * Parses the JNLP file prowim.jnlp and replace the param (file path to be opened).
     * 
     * @param param the file path.
     */
    @WebMethod
    @WebResult(partName = "return")
    void rewriteJNLP(String param);

    /**
     * Sets or saves the values of given parameter
     * 
     * @param paramArray list of parameter.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setParameter(ParameterArray paramArray) throws OntologyErrorException;

    /**
     * True if the instance is global.
     * 
     * @param instanceID the frame ID
     * @return True if the instance is global.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    boolean isGlobal(String instanceID) throws OntologyErrorException;

    /**
     * Sets the scope of an object.
     * 
     * @param instanceID not null instance ID.
     * @param scope not null scope : Local or Global values are permitted.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    void setScope(String instanceID, String scope) throws OntologyErrorException;

    /**
     * Sets the value of the given slot.
     * 
     * @param frameID the ID of the frame to set the slot value for, may not be null
     * @param slotName the name of the slot to set, may not be null
     * @param value the value of the slot to set, may not be null
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    void setSlotValue(String frameID, String slotName, String value) throws OntologyErrorException;

    /**
     * returns the class of the selected element.
     * 
     * @param id the element ID.
     * @return not null direct class.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String getDirectClassOfInstance(final String id) throws OntologyErrorException;

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link Role}
     * @deprecated dont call this
     */
    @Deprecated
    Role getRole();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link Activity}
     * @deprecated dont call this
     */
    @Deprecated
    Activity getActivity();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link ControlFlow}
     * @deprecated dont call this
     */
    @Deprecated
    ControlFlow getControlFlow();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link Work}
     * @deprecated dont call this
     */
    @Deprecated
    Work getWork();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link Function}
     * @deprecated dont call this
     */
    @Deprecated
    Function getFunction();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link Mean}
     * @deprecated dont call this
     */
    @Deprecated
    Mean getMean();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link ResultsMemory}
     * @deprecated dont call this
     */
    @Deprecated
    ResultsMemory getResultsMemory();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link Product}
     * @deprecated dont call this
     */
    @Deprecated
    Product getProduct();

    /**
     * TODO: this is a workarround, to get JAXRpc binding working.<br>
     * Clients dont have to call this method.
     * 
     * @return {@link ProcessInformation}
     * @deprecated dont call this
     */
    @Deprecated
    ProcessInformation getProcessInformation();
}
