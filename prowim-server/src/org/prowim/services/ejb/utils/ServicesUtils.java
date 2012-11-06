/*==============================================================================
 * File $Id: ServicesUtils.java 4934 2010-10-20 15:24:29Z specht $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-20 17:24:29 +0200 (Mi, 20 Okt 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/utils/ServicesUtils.java $
 * $LastChangedRevision: 4934 $
 *------------------------------------------------------------------------------
 * (c) 07.04.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.ejb.utils;

import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.coreengine.entities.impl.DefaultSecurityManager;



/**
 * Utility methods called from the ejb`s classes.
 * 
 * @author Saad Wardi
 * @version $Revision: 4934 $
 * @since 2.0
 */
public final class ServicesUtils
{

    private ServicesUtils()
    {
    }

    /**
     * Gets the caller user ID.
     * 
     * @param username the caller username.
     * @return the caller user ID, or null if no user was found
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public static String getCallerUserID(String username) throws OntologyErrorException
    {
        return DefaultSecurityManager.getInstance().getUserID(username);
    }

}
