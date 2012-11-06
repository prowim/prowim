/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-30 13:50:09 +0200 (Mi, 30 Jun 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/tables/TableModel.java $
 * $LastChangedRevision: 4173 $
 *------------------------------------------------------------------------------
 * (c) 18.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.prowim.rap.framework.components.table.DefaultTableViewer;


/**
 * Title and description.
 * 
 * @author Saad Wardi
 * @version $Revision: 4173 $
 * @deprecated Use {@link DefaultTableViewer}
 */
@Deprecated
public interface TableModel
{
    /**
     * Returns the number of rows in the model. A <code>org.eclipse.swt.widgets.Table</code> uses this method to determine how many rows it should display. This method should be quick, as it is called frequently during rendering.
     * 
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    int getRowCount();

    /**
     * Returns the number of columns in the model. A <code>org.eclipse.swt.widgets.Table</code> uses this method to determine how many columns it should create and display by default.
     * 
     * @return the number of columns in the model
     * @see #getRowCount
     */
    int getColumnCount();

    /**
     * Returns the name of the column at <code>columnIndex</code>. This is used to initialize the table's column header name. Note: this name does not need to be unique; two columns in a table can have the same name.
     * 
     * @param columnIndex the index of the column
     * @return the name of the column
     */
    String getColumnName(int columnIndex);

    /**
     * Returns the most specific superclass for all the cell values in the column. This is used by the <code>JTable</code> to set up a default renderer and editor for the column.
     * 
     * @param columnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    Class<? > getColumnClass(int columnIndex);

    /**
     * Returns true if the cell at <code>rowIndex</code> and <code>columnIndex</code> is editable. Otherwise, <code>setValueAt</code> on the cell will not change the value of that cell.
     * 
     * @param rowIndex the row whose value to be queried
     * @param columnIndex the column whose value to be queried
     * @return true if the cell is editable
     * @see #setValueAt
     */
    boolean isCellEditable(int rowIndex, int columnIndex);

    /**
     * Returns the value for the cell at <code>columnIndex</code> and <code>rowIndex</code>.
     * 
     * @param rowIndex the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    Object getValueAt(int rowIndex, int columnIndex);

    /**
     * Sets the value in the cell at <code>columnIndex</code> and <code>rowIndex</code> to <code>aValue</code>.
     * 
     * @param aValue the new value
     * @param rowIndex the row whose value is to be changed
     * @param columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    void setValueAt(Object aValue, int rowIndex, int columnIndex);

    /**
     * Description.
     * 
     * @param rows the data
     */
    void setRowData(Object[][] rows);

    /**
     * Returns the row value at rowIndex.
     * 
     * @param rowIndex the rowIndex
     * @return row
     */
    Object[] getValueAt(int rowIndex);

}
