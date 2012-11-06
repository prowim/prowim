/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-08-26 16:35:40 +0200 (Mi, 26 Aug 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/dms/DMSException.java $
 * $LastChangedRevision: 2248 $
 *------------------------------------------------------------------------------
 * (c) 14.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
 *This file is part of ProWim.

ProWim is free soimport javax.xml.ws.WebFault;

import org.prowim.services.ejb.ServiceConstants;
lic License as published by
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

import javax.xml.ws.WebFault;

import org.prowim.services.ejb.ServiceConstants;



/**
 * Creates a DMSException that wrappes the alfresco webservice exceptions. Use this exception for all access of DMS services.
 * 
 * @author Saad Wardi
 * @version $Revision: 2248 $
 */

@WebFault(name = ServiceConstants.DMS_EXCEPTION_NAME, targetNamespace = ServiceConstants.DMS_EXCEPTION_NAMESPACE, faultBean = "org.prowim.datamodel.dms.DMSFault")
public class DMSException extends Exception
{

    /**
     * The instance of the fault bean, which handles the message of this exception
     */
    private final DMSFault dmsFault;

    /**
     * Creates an instance of this exception with the given message.
     * 
     * @param message the detailed message of this exception, can not be <code>NULL</code>
     * @param dmsFault the fault bean to handle the message , can not be <code>NULL</code>
     */
    public DMSException(String message, DMSFault dmsFault)
    {
        super(message);
        this.dmsFault = dmsFault;
    }

    /**
     * Creates an instance with a {@link Throwable} cause.
     * 
     * @param message the message., can not be <code>NULL</code>
     * @param dmsFault the fault bean to handle the message, can not be <code>NULL</code>
     * @param cause not null {@link Throwable}
     */
    public DMSException(String message, DMSFault dmsFault, Throwable cause)
    {
        super(message, cause);
        this.dmsFault = dmsFault;
    }

    /**
     * Returns the fault bean of this exception
     * 
     * @return the dmsFault, can not be <code>NULL</code>
     */
    public DMSFault getFaultInfo()
    {
        return dmsFault;
    }

}
