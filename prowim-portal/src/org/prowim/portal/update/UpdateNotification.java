/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 16.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.update;

import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.view.DefaultView;


/**
 * A class to forward collected {@link UpdateEvent}s to the {@link DefaultView}
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0.a9
 */
public class UpdateNotification extends UpdateEvent
{

    private final boolean isContainingForeignEvents;

    /**
     * Create a new {@link UpdateNotification}
     * 
     * @param entityType type of entities
     * @param entityIds id of entities
     * @param isContainingForeignEvents flag if any events merged into this {@link UpdateNotification} are received from foreign clients
     */
    UpdateNotification(EntityType entityType, String[] entityIds, boolean isContainingForeignEvents)
    {
        super(entityType, entityIds);
        this.isContainingForeignEvents = isContainingForeignEvents;
    }

    /**
     * Returns true, if event contains a foreign event, else false
     * 
     * @return the containsForeignEvents
     */
    public boolean isContainingForeignEvents()
    {
        return isContainingForeignEvents;
    }
}
