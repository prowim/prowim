/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 30.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.dms;

import javax.xml.bind.annotation.XmlType;

import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * The Java type that goes as soapenv:Fault detail element. Used in web services exceptions, fault beans just hold the details of the SOAP fault. This one is used by the {@link OntologyErrorException}.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a9
 */
@XmlType(name = "dmsFault", namespace = "org.prowim.datamodel.dms", propOrder = { "message" })
public class DMSFault
{
    private String message;

    /**
     * Constructs this fault.
     */
    public DMSFault()
    {
    }

    /**
     * Sets the message of this fault
     * 
     * @param message the message to set, can not be <code>NULL</code>
     */
    public void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Returns the message of this fault
     * 
     * @return the message, can not be <code>NULL</code>
     */
    public String getMessage()
    {
        return message;
    }

}
