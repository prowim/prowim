/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 01.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;
import org.eclipse.jface.viewers.IContentProvider;


/**
 * This is the default table model for the DefTableViwewer. This should be used to set data in to a table.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 * @deprecated Do not use this table model "proxy". Use the {@link DefaultContentProvider} and map the table model (if a {@link TableModel} is necessary), else you will only need a {@link IContentProvider}
 */
@Deprecated
public class DefTableModel implements TableModel
{
    /** the data to input in this table */
    protected Object[][]   rowsData;
    private final String[] columnNames;

    /**
     * Default empty Constructor creates an empty model.
     */
    public DefTableModel()
    {
        this(new Object[0][0], new String[0]);
    }

    /**
     * Constructor. It returns all parameters and belong values to a process.
     * 
     * @param rowColumns Data for table items
     * @param header Header of table
     */
    public DefTableModel(final Object[][] rowColumns, String[] header)
    {
        rowsData = rowColumns;
        columnNames = header;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.table.TableModel#getColumnClass(int)
     */
    @Override
    public Class<? > getColumnClass(int columnIndex)
    {
        return Object.class;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.table.TableModel#getColumnCount()
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
     * @see org.prowim.rap.framework.components.table.TableModel#getColumnName(int)
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
     * @see org.prowim.rap.framework.components.table.TableModel#getRowCount()
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
     * @see org.prowim.rap.framework.components.table.TableModel#getValueAt(int, int)
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
     * @see org.prowim.rap.framework.components.table.TableModel#getValueAt(int)
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
     * @see org.prowim.rap.framework.components.table.TableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.table.TableModel#setRowData(java.lang.Object[][])
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
     * @see org.prowim.rap.framework.components.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    }

}
