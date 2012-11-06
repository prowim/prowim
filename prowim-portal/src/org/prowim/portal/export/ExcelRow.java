/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
 *
 */
package org.prowim.portal.export;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Creates row of a excel sheet. Each row is a {@link ArrayList}, which included a {@link HashMap}.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public class ExcelRow
{
    private final ArrayList rowList;
    private int             colCount = 0;

    /**
     * 
     * Constructor.
     */
    public ExcelRow()
    {
        this.rowList = new ArrayList<HashMap>();
    }

    /**
     * 
     * Adds a new row.
     * 
     * @param row one row
     */
    public void addRow(HashMap row)
    {
        setColumnCount(row);
        this.rowList.add(row);
    }

    /**
     * 
     * Get size of row.
     * 
     * @return Size of list
     */
    public int getSize()
    {
        return this.rowList.size();
    }

    /**
     * 
     * Returns row of given index.
     * 
     * @param index index of row
     * @return List of rows
     */
    public HashMap getRow(int index)
    {
        return (HashMap) this.rowList.get(index);
    }

    /**
     * Get count of columns
     * 
     * @return count of columns
     */
    public int getColCount()
    {
        return colCount;
    }

    /**
     * 
     * Set count of columns
     * 
     * @param map map of rows
     */
    public void setColumnCount(HashMap map)
    {
        if (this.colCount < map.size())
            this.colCount = map.size();
    }
}
