/*==============================================================================

 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-30 13:50:09 +0200 (Mi, 30 Jun 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/tables/DefaultTableModel.java $
 * $LastChangedRevision: 4173 $
 *------------------------------------------------------------------------------
 * (c) 09.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.portal.tables;

import org.apache.commons.lang.Validate;
import org.prowim.rap.framework.components.table.DefaultTableViewer;



/**
 * Model or controller to fill table of start parameter.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4173 $
 * @deprecated Use {@link DefaultTableViewer}
 */
@Deprecated
public class DefaultTableModel implements TableModel
{
    /** the data to input in this table */
    protected Object[][]   rowsData;
    private final String[] columnNames;

    // ****************************Methods*******************************************************************

    /**
     * Default empty Constructor creates an empty model.
     */
    public DefaultTableModel()
    {
        this(new Object[0][0], new String[0]);
    }

    /**
     * Constructor. It returns all parameters and belong values to a process.
     * 
     * @param rowColumns Data for table items
     * @param header Header of table
     */
    public DefaultTableModel(final Object[][] rowColumns, String[] header)
    {
        rowsData = rowColumns;
        columnNames = header;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount()
    {
        if (columnNames != null)
            return this.columnNames.length;
        else
            return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#getRowCount()
     */
    @Override
    public int getRowCount()
    {
        if (rowsData != null)
            return this.rowsData.length;
        else
            return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Validate.isTrue(rowIndex >= 0 && columnIndex >= 0);

        if (rowsData != null)
            return this.rowsData[rowIndex][columnIndex];
        else
            return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#getValueAt(int)
     */
    @Override
    public Object[] getValueAt(int rowIndex)
    {
        Validate.isTrue(rowIndex >= 0);

        if (rowsData != null)
            return this.rowsData[rowIndex];
        else
            return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#setRowData(java.lang.Object[][])
     */
    @Override
    public void setRowData(Object[][] rows)
    {
        Validate.notNull(rows);

        this.rowsData = rows;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int columnIndex)
    {
        Validate.isTrue(columnIndex >= 0);
        return columnNames[columnIndex];
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#getColumnClass(int)
     */
    @Override
    public Class<? > getColumnClass(int columnIndex)
    {
        return Object.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.tables.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    }
}
