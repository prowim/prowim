/*==============================================================================
 * File $Id: SecurityInterceptor.java 4510 2010-08-02 10:37:49Z wiesner $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/aspects/security/interceptors/SecurityInterceptor.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 25.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.aspects.security.interceptors;

import javax.interceptor.InvocationContext;

import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * A request Interceptor is designed to intercept request operations called from users to provide authorization functionality on the prowim server.<br>
 * 
 * To write a server-side security Interceptor, implement the SecurityInterceptor interface.
 * 
 * @author Saad Wardi
 * @version $Revision: 4510 $
 * @since 2.0.0
 */
public interface SecurityInterceptor
{
    /**
     * Intercepts a method call.
     * 
     * @param context method invocation context.
     * @return result of the method.
     * @throws Exception thrown if the method have to be intercepted.
     */
    Object onMethodCall(InvocationContext context) throws Exception;

    /**
     * Called to intercept the call if the user is not authorized.
     * 
     * @param methodname not null methodname.
     * @param classname not null classname.
     * @param parameters not null parameters.
     * @return true if the user can call the method.
     * @throws OntologyErrorException if error occurs in ontology back end
     */
    boolean intercept(String methodname, String classname, Object[] parameters) throws OntologyErrorException;

}
