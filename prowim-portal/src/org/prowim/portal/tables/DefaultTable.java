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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.portal.tables;

import java.util.Arrays;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.table.DefaultTableViewer;
import org.prowim.rap.framework.resource.ColorManager;
import org.prowim.rap.framework.resource.FontManager;



/**
 * This class implements the table view using the RAP {@link org.eclipse.swt.widgets.Table}. <br/>
 * Table will initialize in the method init() with values from model. It adds in each cell <br/>
 * the name of object and the object self in data of cell.
 * 
 * 
 * 
 * @author Mayiar Khodaei
 * @version $Revision$
 * @deprecated Use {@link DefaultTableViewer}
 */
@Deprecated
public class DefaultTable extends Table
{

    private TableModel  tableModel;
    private boolean     autoCreateRowSorter;
    /** currently selected cell in this table */
    private final int[] selectedCell   = { 0, 0 };
    private TableItem   tableItem;                    // current row
    private Object      cellDataObject = new Object();

    /**
     * The default constructor.
     * 
     * @param parent the parent composite
     * @param model the model
     * @param style {@link SWT#MULTI} or {@link SWT#SINGLE}
     */
    public DefaultTable(final Composite parent, TableModel model, int style)
    {
        super(parent, parent.getStyle() | SWT.V_SCROLL | SWT.H_SCROLL | style);

        Validate.notNull(model);
        this.setSize(parent.getSize());
        this.setLinesVisible(true);
        this.setHeaderVisible(true);

        setTableModel(model);
        addListener(parent);
    }

    /**
     * Adds the listener to this table.
     */
    private void addListener(final Composite parent)
    {
        // Listener added to control resize of Table. It is not more necessary to define this out of this class
        this.addControlListener(new ControlAdapter()
        {
            @Override
            public void controlResized(ControlEvent e)
            {
                for (int i = 0; i < getColumnCount(); i++)
                {
                    TableColumn column = getColumn(i);
                    column.setWidth((parent.getClientArea().width / getColumnCount()) - 5);
                }
            }
        });

        // Get the selected cell of table
        this.addListener(SWT.MouseDown, new Listener()
        {
            public void handleEvent(Event event)
            {
                Rectangle clientArea = getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = getTopIndex();
                while (index < getItemCount())
                {
                    boolean visible = false;
                    TableItem item = getItem(index);
                    for (int i = 0; i < getColumnCount(); i++)
                    {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(pt))
                        {
                            selectedCell[0] = index;
                            selectedCell[1] = i;
                            // Get the data of actual cell
                            cellDataObject = item.getData(String.valueOf(i));
                        }
                        if ( !visible && rect.intersects(clientArea))
                        {
                            visible = true;
                        }
                    }
                    if ( !visible)
                        return;
                    index++;

                }
            }
        });
    }

    /**
     * Sorts the table content with keys from the Column at the columnIndex.
     * 
     * @param columnIndex the columnIndex
     */
    public void sortAtColumn(int columnIndex)
    {
        sort(columnIndex, true);
        TableColumn[] columns = this.getColumns();

        if (columns[columnIndex] != null)
        {
            Object data = columns[columnIndex].getData();
            if (data == null)
            {
                columns[columnIndex].setData(Resources.Frames.Table.Actions.TABLE_SORT_UP.getText());
                columns[columnIndex].setImage(Resources.Frames.Table.Actions.TABLE_SORT_UP.getImage());
                sort(columnIndex, false);
            }
            else if (data.equals(Resources.Frames.Table.Actions.TABLE_SORT_UP.getText()))
            {
                columns[columnIndex].setData(Resources.Frames.Table.Actions.TABLE_SORT_DOWN.getText());
                columns[columnIndex].setImage(Resources.Frames.Table.Actions.TABLE_SORT_DOWN.getImage());
                sort(columnIndex, true);
            }
            else if (data.equals(Resources.Frames.Table.Actions.TABLE_SORT_DOWN.getText()))
            {
                columns[columnIndex].setData(Resources.Frames.Table.Actions.TABLE_SORT_UP.getText());
                columns[columnIndex].setImage(Resources.Frames.Table.Actions.TABLE_SORT_UP.getImage());
                sort(columnIndex, false);
            }

            resetNotSelectedColumns(columnIndex);
        }

    }

    /**
     * resets all columns sorting except the column at the selectedIndex.
     * 
     * @param selectedIndex the selected column
     */
    private void resetNotSelectedColumns(int selectedIndex)
    {

        TableColumn[] columns = this.getColumns();
        for (int i = 0; i < columns.length; i++)
        {
            if (i != selectedIndex)
            {
                columns[i].setImage(null);
                columns[i].setData(null);
            }
        }
    }

    /**
     * Initializes the complete table with all values, that means every cell will be updated (added) with the content of model.
     */
    public void init()
    {
        int columnCount = tableModel.getColumnCount();

        // set for every column the header
        for (int currentColumn = 0; currentColumn < columnCount; currentColumn++)
        {
            TableColumn column = new TableColumn(this, SWT.NONE);
            column.setMoveable(true);
            column.setText(tableModel.getColumnName(currentColumn));
            column.setWidth(GlobalConstants.TABLE_COLUMN_DEFAULT_WIDTH);
        }

        // iterates every row and set every cell with the object from the model
        for (int currentRow = 0; currentRow < tableModel.getRowCount(); currentRow++)
        {
            tableItem = new TableItem(this, SWT.NONE);
            final Object[] row = tableModel.getValueAt(currentRow);

            // iterates for every column and set the cell
            for (int currentColumn = 0; currentColumn < row.length; currentColumn++)
            {
                tableItem.setText(currentColumn, row[currentColumn].toString());
                tableItem.setData(String.valueOf(currentColumn), row[currentColumn]);
            }

            // color every second line
            if ((currentRow % 2) == 0)
                tableItem.setBackground(ColorManager.COLOR_ORANGE);
        }
    }

    /**
     * Set if table is sortable or not.
     * 
     * @param value if true the table sorts the items otherwise items are not sorted
     */
    public void setSortable(boolean value)
    {
        if (value)
        {
            final TableColumn[] columns = this.getColumns();
            for (int i = 0; i < columns.length; i++)
            {
                final int index = i;
                columns[i].addListener(SWT.Selection, new Listener()
                {
                    private int     colPos = -1;
                    private boolean sortFlag;
                    {
                        colPos = index;
                    }

                    public void handleEvent(Event event)
                    {
                        sortFlag = !sortFlag;
                        // Sort
                        sort(colPos, sortFlag);
                        sortAtColumn(colPos);

                    }

                });
            }
        }
    }

    /**
     * Gets the tablemodel property
     * 
     * @return the tableModel
     */
    public TableModel getTableModel()
    {
        return tableModel;
    }

    /**
     * Sets the tableModel property
     * 
     * @param tableModel the tableModel to set
     */
    public void setTableModel(TableModel tableModel)
    {
        Validate.notNull(tableModel);
        clear();
        this.tableModel = tableModel;

        init();
        this.setSortable(true);
        this.pack();
        this.setFont(FontManager.FONT_VERDANA_12_NORMAL);
    }

    /**
     * Clears the current table and removes all items in table.
     */
    private void clear()
    {
        this.clearAll();
        this.removeAll();
        TableColumn[] columns = this.getColumns();
        for (int i = 0; i < columns.length; i++)
        {
            columns[i].dispose();
        }
    }

    /**
     * Gets the auto create row sorter.
     * 
     * @return true if selected.
     */
    public boolean getAutoCreateRowSorter()
    {
        return autoCreateRowSorter;
    }

    /**
     * Sorts the table.
     * 
     * @param colPos column position
     * @param sortFlag sort flag
     */
    public void sort(int colPos, boolean sortFlag)
    {
        Validate.isTrue(colPos >= 0);
        TableItem[] tableItems = this.getItems();

        int columnsCount = this.getColumnCount();
        TableRowComparatorNew[] comparators = new TableRowComparatorNew[tableItems.length];
        Object[] objectBuffer = null;

        for (int i = 0; i < tableItems.length; i++)
        {
            objectBuffer = new Object[columnsCount];

            int idx = 0;
            for (int j = 0; j < objectBuffer.length; j++)
            {
                if (tableItems[i].getData(Integer.toString(j)) != null)
                    objectBuffer[j] = tableItems[i].getData(Integer.toString(j));
                else
                    objectBuffer[j] = "";

                idx++;
            }

            comparators[i] = new TableRowComparatorNew(objectBuffer, i, sortFlag, colPos);
        }

        Arrays.sort(comparators);
        Object[] tmpObject;
        for (int i = 0; i < comparators.length; i++)
        {
            int rowIndex = comparators[i].getOldRowIndex();
            tmpObject = comparators[i].getObjectBuffer();

            for (int j = 0; j < columnsCount; j++)
            {
                tableItems[rowIndex].setText(j, comparators[rowIndex].getObjectBuffer()[j].toString());
                tableItems[rowIndex].setData(String.valueOf(j), comparators[rowIndex].getObjectBuffer()[j]);

                tableItems[i].setText(j, tmpObject[j].toString());
                tableItems[i].setData(String.valueOf(j), tmpObject[j]);
            }
        }
    }

    /**
     * 
     * Get the current selected cell
     * 
     * @return int[] Return value give you the coordinate of a cell in a table, no selected return
     */
    public int[] getSelectedCell()
    {
        return selectedCell;
    }

    /**
     * 
     * Gets the current selected stored object.
     * 
     * @return Object complete object of a cell, which is stored.
     */
    public Object getCellDataObject()
    {
        return this.cellDataObject;
    }

}
