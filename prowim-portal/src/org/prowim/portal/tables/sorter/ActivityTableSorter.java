/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 29.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.tables.sorter;

import org.eclipse.jface.viewers.Viewer;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.rap.framework.components.table.DefaultTableSorter;


/**
 * Implementation of {@link DefaultTableSorter} for Activities.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 7.3a
 */
public class ActivityTableSorter extends DefaultTableSorter
{

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.table.DefaultTableSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {
        Activity a1 = (Activity) e1;
        Activity a2 = (Activity) e2;
        int rc = 0;
        switch (propertyIndex)
        {
            case 0:
                rc = a1.getProcessName().compareTo(a2.getProcessName());
                break;
            case 1:
                rc = a1.getName().compareTo(a2.getName());
                break;
            case 2:
                rc = a1.getStartTime().compareTo(a2.getStartTime());
                break;
            default:
                rc = 0;
        }
        // If descending order, flip the direction
        if (direction == DESCENDING)
        {
            rc = -rc;
        }
        return rc;
    }

}
