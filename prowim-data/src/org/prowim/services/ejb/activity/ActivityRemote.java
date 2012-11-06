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

package org.prowim.services.ejb.activity;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.collections.ActivityArray;
import org.prowim.datamodel.collections.MeanArray;
import org.prowim.datamodel.collections.ProductArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ControlFlow;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Product;
import org.prowim.datamodel.prowim.Role;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 ** Collects all methods to receive {@link Activity} {@link Person} and {@link Role}.
 * 
 * @author Saad Wardi
 * @version $Revision: 4250 $
 */

@WebService(name = ServiceConstants.PROWIM_ACTIVITY_REMOTE, targetNamespace = ServiceConstants.PROWIM_ACTIVITY_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ActivityRemote
{

    /**
     * Gets all the current activities {@link Activity} for the logged in user.
     * 
     * @param userID TODO
     * 
     * @return returns org.prowim.services.ejb.tasks.ActivityArray. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ActivityArray getCurrentTasks(String userID) throws OntologyErrorException;

    /**
     * Gets defined roles in the process.
     * 
     * @param processID the id of the process.
     * @return RoleArray. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RoleArray getProcessRoles(String processID) throws OntologyErrorException;

    /**
     * Gets defined roles in an activity.
     * 
     * @param activityID the id of the activity.
     * @return RoleArray. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RoleArray getActivityRoles(String activityID) throws OntologyErrorException;

    /**
     * Gets the registered {@link Mean}s of given {@link Activity}.
     * 
     * @param activityID the {@link Activity} id
     * @return {@link RecordSet} IDs of {@link Mean}s
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    MeanArray getActivityMeans(String activityID) throws OntologyErrorException;

    /**
     * Gets the {@link Activity} input product.
     * 
     * @param activityID activity ID.
     * @return not null {@link ProductArray} that contains {@link Product} objects. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProductArray getActivityInputProduct(String activityID) throws OntologyErrorException;

    /**
     * Gets the possible selection for the selected process information from information type MultiList and SingleList.
     * 
     * @param processInformationID the not null processInformationID.
     * @return list of possible values.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    StringArray getPossibleSelection(String processInformationID) throws OntologyErrorException;

    /**
     * Gets the possible selection for the selected process information from information type MultiList and SingleList.
     * 
     * @param values the not null possible values.
     * @param processInformationID the not null processInformationID.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    void setPossibleSelection(String processInformationID, StringArray values) throws OntologyErrorException;

    /**
     * Gets the {@link Activity} output {@link Product}.
     * 
     * @param activityID activity ID.
     * @return not null {@link ProductArray} that contains {@link Product} objects. If no items exist, a new empty list is returned.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    ProductArray getActivityOututProduct(String activityID) throws OntologyErrorException;

    /**
     * Sets the values of the output {@link Product} to the activity.
     * 
     * @param outputProducts not null list of output {@link Product} to be set.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    void saveOutputProducts(ProductArray outputProducts) throws OntologyErrorException;

    /**
     * Binding the JAXB.
     * 
     * @return {@link ControlFlow}
     */
    @WebMethod
    @WebResult(partName = "return")
    ControlFlow getControlFlow();

}
