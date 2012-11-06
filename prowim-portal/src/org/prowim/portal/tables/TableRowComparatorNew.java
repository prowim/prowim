/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 15.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.tables;

import org.prowim.rap.framework.components.table.DefaultTableSorter;
import org.prowim.rap.framework.components.table.DefaultTableViewer;


/**
 * This class used as comparator for DefaultTable class.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @deprecated Use {@link DefaultTableViewer} and {@link DefaultTableSorter}
 */
@Deprecated
public class TableRowComparatorNew implements Comparable<Object>
{
    private final Object[] objectBuffer;
    private final int      oldRowIndex;
    private final boolean  sortFlag;
    private final int      columnIndex;

    /**
     * Constructor.
     * 
     * @param objectBuffer a objectBuffer
     * @param oldRowIndex the old row
     * @param sortFlag sort flag
     * @param columnIndex column position
     */
    public TableRowComparatorNew(Object[] objectBuffer, int oldRowIndex, boolean sortFlag, int columnIndex)
    {
        this.objectBuffer = objectBuffer;
        this.oldRowIndex = oldRowIndex;
        this.sortFlag = sortFlag;
        this.columnIndex = columnIndex;

    }

    /**
     * Gets the textBuffer attribute.
     * 
     * @return the textBuffer
     */
    public Object[] getObjectBuffer()
    {
        return objectBuffer;
    }

    /**
     * Gets the oldRowColumn attribute
     * 
     * @return the oldRowIndex the old column
     */
    public int getOldRowIndex()
    {
        return oldRowIndex;
    }

    /**
     * The sorting order of the table.
     * 
     * @return the sortFlag, if true the sorting order is ascending, if false the sorting order is descending
     */
    public boolean isSortFlag()
    {
        return sortFlag;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o)
    {
        int ret = this.objectBuffer[this.columnIndex].toString().compareTo(((TableRowComparatorNew) o).objectBuffer[this.columnIndex].toString());
        if (this.sortFlag)
            ret *= -1;
        return ret;
    }

}
