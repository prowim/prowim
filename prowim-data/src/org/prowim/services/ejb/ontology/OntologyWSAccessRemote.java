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

package org.prowim.services.ejb.ontology;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.algernon.Result;
import org.prowim.datamodel.factory.impl.AlgernonDataObjectFactory;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Collects all methods to access the ontology.
 * 
 * @author Saad Wardi
 * @version $Revision: 4106 $
 * 
 * 
 */
@WebService(name = ServiceConstants.ONTOLOGYWSACCESS_REMOTE, targetNamespace = ServiceConstants.ONTOLOGYWSACCESS_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@XmlSeeAlso({ AlgernonDataObjectFactory.class })
public interface OntologyWSAccessRemote
{

    /**
     * get the path of the Knowledge project file.
     * 
     * @return returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    String getKB();

    /**
     * Reinitialize the algernon with the knowledge base.
     * 
     * @return status from the initialization. returned values are : (SUCCESSED or FAILED)
     */
    @WebMethod
    @WebResult(partName = "return")
    String reloadKB();

    /**
     * Save the content of the Knowledge base.
     * 
     * @return returns java.lang.String
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    String saveKB() throws OntologyErrorException;

    /**
     * Execute an algernon query.
     * 
     * @param query query.
     * @param askTell ask or tell
     * @return returns de.ebcot.prowim.services.algernon.Result
     * @throws OntologyErrorException if error occurs in ontology back end
     */

    @WebMethod
    @WebResult(partName = "return")
    Result start(String query, String askTell) throws OntologyErrorException;

    /**
     * Execute an algernon query.
     * 
     * @param askTell ASK or TELL.
     * @param query the query.
     * @return returns de.ebcot.prowim.services.algernon.RecordsetKV
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RecordsetKV startKV(String query, String askTell) throws OntologyErrorException;

    /**
     * Execute an algernon query.
     * 
     * @param arg1 ASK or TELL.
     * @param arg0 the query.
     * @return returns de.ebcot.prowim.services.algernon.RecordsetKV
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    @WebMethod
    @WebResult(partName = "return")
    RecordSet startRS(String arg0, String arg1) throws OntologyErrorException;

}
