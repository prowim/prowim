/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 28.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;


/**
 * This class implements the table view using the RAP {@link org.eclipse.jface.viewers.TableViewer}. <br/>
 * Table will initialize in the method init() with values from model. It adds in each cell <br/>
 * the name of object and the object self in data of cell.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 * @deprecated Use {@link DefaultTableViewer} instead
 */
@Deprecated
public class DefTableViewer extends TableViewer
{

    private TableModel         tableModel;
    private DefaultTableSorter tableSorter;

    /**
     * Description.
     * 
     * @param parent Parent of component.
     * @param style Style of table.
     */
    public DefTableViewer(final Composite parent, int style)
    {
        super(parent, style | SWT.V_SCROLL | SWT.H_SCROLL);
        this.getTable().setHeaderVisible(true);
        this.getTable().setLinesVisible(true);

        this.setContentProvider(new DefContentProvider());

        // Set the sorter for the table
        tableSorter = new DefaultTableSorter();
        this.setSorter(tableSorter);

        this.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                // StructuredSelection sel = (StructuredSelection) event.getSelection();
                // Object[] firstElement = (Object[]) sel.getFirstElement();

            }
        });

        this.getTable().addControlListener(new ControlListener()
        {
            @Override
            public void controlResized(ControlEvent e)
            {
                for (int i = 0; i < DefTableViewer.this.getTable().getColumnCount(); i++)
                {
                    TableColumn column = DefTableViewer.this.getTable().getColumn(i);
                    column.setWidth((parent.getClientArea().width / DefTableViewer.this.getTable().getColumnCount()) - 10);
                }
            }

            @Override
            public void controlMoved(ControlEvent e)
            {
            }
        });
    }

    /**
     * Sets the tableModel property
     * 
     * @param tableModel the tableModel to set
     * @param labelProvider Label provider to initialize each cell of table, e.g. color, font and image. <br>
     *        For default set {@link DefLabelProvider}, else create your own provider. Can be null. <br>
     *        If null sets {@link DefLabelProvider} as default, else set the given provider.
     */
    public void setModel(TableModel tableModel, IBaseLabelProvider labelProvider)
    {
        Validate.notNull(tableModel);

        this.tableModel = tableModel;
        createColumns(tableModel);

        setTableLableProvider(labelProvider);

        initTable(tableModel);
        // Set the sorter for the table
        tableSorter = new DefaultTableSorter();
        this.setSorter(tableSorter);
    }

    /**
     * 
     * Description.
     * 
     * @param model
     */
    private void initTable(TableModel model)
    {
        clear();
        List<Object[]> elements = new ArrayList<Object[]>();
        for (int currentRow = 0; currentRow < model.getRowCount(); currentRow++)
        {
            final Object[] row = model.getValueAt(currentRow);
            Object[] element = new Object[model.getColumnCount()];
            // iterates for every column and set the cell
            for (int currentColumn = 0; currentColumn < row.length; currentColumn++)
            {
                element[currentColumn] = row[currentColumn];
            }
            elements.add(element);
        }
        this.setInput(elements);
    }

    /**
     * 
     * Set the label provider of table.
     * 
     * @param labelProvider {@link IBaseLabelProvider}
     */
    public void setTableLableProvider(IBaseLabelProvider labelProvider)
    {
        if (labelProvider != null)
            this.setLabelProvider(labelProvider);
        else
            this.setLabelProvider(new DefLabelProvider());
    }

    /**
     * Clears the current table and removes all items in table.
     */
    private void clear()
    {
        this.doClearAll();
        this.doRemoveAll();
        // TableColumn[] columns = this.getTable().getColumns();
        // for (int i = 0; i < columns.length; i++)
        // {
        // columns[i].dispose();
        // }
    }

    private TableViewerColumn createColumns(final TableModel model)
    {
        TableViewerColumn result = null;
        int columnCount = tableModel.getColumnCount();

        // set for every column the header
        for (int currentColumn = 0; currentColumn < columnCount; currentColumn++)
        {
            final int sortInt = currentColumn;
            result = new TableViewerColumn(this, SWT.NONE);
            final TableColumn column = result.getColumn();
            column.setText(tableModel.getColumnName(currentColumn));
            column.setMoveable(true);
            column.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(final SelectionEvent event)
                {
                    tableSorter.setColumn(sortInt);
                    sort(column);
                }
            });
        }

        return result;
    }

    /**
     * 
     * Get the column of given index.
     * 
     * @param index index of column.
     * @return Widget
     */
    public Widget getViwerColumn(int index)
    {
        return this.getColumnViewerOwner(index);
    }

    /**
     * 
     * Sorts the table of given column.
     * 
     * @param column {@link TableColumn} Given column.
     */
    private void sort(final TableColumn column)
    {
        int dir = DefTableViewer.this.getTable().getSortDirection();
        if (DefTableViewer.this.getTable().getSortColumn() == column)
        {
            if (dir == SWT.UP)
                dir = SWT.DOWN;
            else
                dir = SWT.UP;
        }
        else
        {
            dir = SWT.DOWN;
        }
        DefTableViewer.this.getTable().setSortDirection(dir);
        DefTableViewer.this.getTable().setSortColumn(column);
        DefTableViewer.this.refresh();
    }

    /**
     * 
     * Sorts the table of given column index. the first column has index 0.
     * 
     * @param index index of column.
     * 
     */
    public void sortAtIndex(int index)
    {
        sort((TableColumn) this.doGetColumn(index));
    }

}
