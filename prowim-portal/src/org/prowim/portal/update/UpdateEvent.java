/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.widgets.Display;
import org.prowim.portal.update.UpdateRegistry.EntityType;



/**
 * 
 * An UpdateEvent stores information about altered (created/updated/deleted) Entities.<br/>
 * Normally this only includes one Entity, but it also may contain more than one, it several entities were batch processed
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a8
 */
public class UpdateEvent
{
    private final long               timestamp;
    private final EntityType         entityType;
    private final Collection<String> entityIds;
    private final Display            creator;

    /**
     * Creates a new UpdateEvent.
     * 
     * @param entityType type of entities
     * @param entityIds id of entities
     */
    UpdateEvent(EntityType entityType, String... entityIds)
    {
        Validate.notNull(entityIds);

        this.timestamp = System.nanoTime();
        this.entityType = entityType;
        this.entityIds = Arrays.asList(entityIds);

        this.creator = Display.getCurrent();
    }

    /**
     * getEntityIds
     * 
     * @return the entityIds
     */
    public Collection<String> getEntityIds()
    {
        return entityIds;
    }

    /**
     * getTimestamp
     * 
     * @return the timestamp
     */
    public long getTimestamp()
    {
        return timestamp;
    }

    /**
     * getEntityType
     * 
     * @return the entityType
     */
    public EntityType getEntityType()
    {
        return entityType;
    }

    /**
     * getCreator
     * 
     * @return the display,on which the UpdateEvent was created. May be <code>null</code> if updateEvent was not triggered in UIThread
     */
    public Display getCreator()
    {
        return creator;
    }

    /**
     * 
     * Was the entity with the given id handled on this {@link UpdateEvent}
     * 
     * @param id id of entity
     * @return true or false
     */
    public boolean isContainingEntityId(String id)
    {
        Validate.notNull(id);
        return this.entityIds.contains(id.trim());
    }
}