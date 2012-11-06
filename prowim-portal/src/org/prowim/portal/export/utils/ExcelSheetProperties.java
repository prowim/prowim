/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 15.02.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

package org.prowim.portal.export.utils;

import java.util.Map;


/**
 * To implement functions to get and set the properties of a excel sheet.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.1.0
 */
public interface ExcelSheetProperties
{
    /**
     * 
     * The denotation or names of the header in order of column index.
     * 
     * @param index index of column
     * @param name name of cell
     */
    void addHeaderName(int index, String name);

    /**
     * 
     * Returns the names of header.
     * 
     * @return A list with the names of header cells
     */
    Map<Integer, String> getHeaderName();

    /**
     * 
     * The style of cells in order of the column index.
     * 
     * @param index index of cell
     * @param cellType type of cell. HSSFCell
     */
    void setCellType(int index, int cellType);

    /**
     * 
     * Get the style of cells.
     * 
     * @return A list with the types of cells
     */
    Map<Integer, Integer> getCellTypes();
}
