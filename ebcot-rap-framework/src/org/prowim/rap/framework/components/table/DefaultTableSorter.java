/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 29.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.components.table;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;


/**
 * Sorts the columns of {@link DefTableViewer}. It compares the visible strings in each column and sort <br>
 * the table. It is set in {@link DefTableViewer} by default so that it is not necessary to set it by each creating of table.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class DefaultTableSorter extends ViewerSorter
{
    /** Sorts as ascending */
    public static final int ASCENDING  = 0;

    /** Sorts as descending */
    public static final int DESCENDING = 1;

    /** Index of column */
    protected int           propertyIndex;

    /** Actual directions */
    protected int           direction  = ASCENDING;

    /**
     * Default Constructor.
     */
    public DefaultTableSorter()
    {
        this.propertyIndex = 0;
        direction = ASCENDING;
    }

    /**
     * Sets the column, which should sorted.
     * 
     * @param column column index
     */
    public void setColumn(int column)
    {
        if (column == this.propertyIndex)
        {
            // Same column as last sort; toggle the direction
            direction = 1 - direction;
        }
        else
        {
            // New column; do an ascending sort
            this.propertyIndex = column;
            direction = ASCENDING;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {
        Object[] p1 = (Object[]) e1;
        Object[] p2 = (Object[]) e2;
        int rc = 0;

        rc = p1[propertyIndex].toString().compareToIgnoreCase(p2[propertyIndex].toString());
        if (direction == DESCENDING)
        {
            rc = -rc;
        }
        return rc;
    }
}
