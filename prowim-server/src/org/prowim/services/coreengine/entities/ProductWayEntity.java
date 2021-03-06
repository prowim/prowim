/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-02 12:37:49 +0200 (Mo, 02 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/entities/ProductWayEntity.java $
 * $LastChangedRevision: 4510 $
 *------------------------------------------------------------------------------
 * (c) 14.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine.entities;

import javax.ejb.Local;

import org.prowim.datamodel.prowim.ProductWay;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Provides an interface with access to the ProductWay (ProductWeg) data stored in the ontology.
 * 
 * @author Saad Wardi
 * @version $Revision: 4510 $
 */
@Local
public interface ProductWayEntity
{
    /**
     * Gets a productway.
     * 
     * @param id not null productway ID.
     * @return not null {@link ProductWay}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    ProductWay getProductWay(String id) throws OntologyErrorException;

}
