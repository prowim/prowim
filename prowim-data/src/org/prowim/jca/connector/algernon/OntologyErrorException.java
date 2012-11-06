/*==============================================================================
 * File $Id$
 * Project: LISA
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.jca.connector.algernon;

import javax.xml.ws.WebFault;

import org.prowim.services.ejb.ServiceConstants;



/**
 * An exception which holds information about errors from ontology backend.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 */
@WebFault(name = ServiceConstants.ONTOLOGY_ERROR, targetNamespace = ServiceConstants.ONTOLOGY_ERROR_NAMESPACE, faultBean = "org.prowim.jca.connector.algernon.OntologyFault")
public class OntologyErrorException extends Exception
{

    private static final long   serialVersionUID = -7858639334552244994L;

    /**
     * The fault bean which holds detailed informations baout the exception
     */
    private final OntologyFault ontologyFault;

    /**
     * Constructs the exception with the message and the cause of exception.
     * 
     * @param message the message to display
     * @param ontologyFault the fault bean which holds detailed informations
     * @param cause the cause of this exception
     */
    public OntologyErrorException(String message, OntologyFault ontologyFault, Throwable cause)
    {
        super(message, cause);
        this.ontologyFault = ontologyFault;
    }

    /**
     * Constructs this exception with only the message display.
     * 
     * @param message the message to display
     * @param ontologyFault the fault bean which holds detailed informations
     */
    public OntologyErrorException(String message, OntologyFault ontologyFault)
    {
        super(message);
        this.ontologyFault = ontologyFault;
    }

    /**
     * Returns the detailed fault of this exception
     * 
     * @return the ontologyFault, can not be <code>NULL</code>
     */
    public OntologyFault getFaultInfo()
    {
        return ontologyFault;
    }

}
