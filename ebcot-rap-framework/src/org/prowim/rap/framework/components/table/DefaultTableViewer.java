/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.components.table;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;


/**
 * Default implementation of the jface {@link TableViewer}.
 * 
 * Has to be overridden. An own {@link IContentProvider} and an own {@link ILabelProvider} have to be implemented.
 * 
 * Please derive the default implementations {@link DefaultContentProvider} and {@link DefaultTableLabelProvider}.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 2.0.0alpha8
 */
public class DefaultTableViewer extends TableViewer
{
    /** This has to be overridden by derived classes to provide the column names */
    protected String[]           columnNames = new String[] {};

    /** Sorter for the table */
    protected DefaultTableSorter tableSorter = new DefaultTableSorter();

    /**
     * Creates a new {@link DefaultTableViewer} for the given {@link Composite} control
     * 
     * @param control the {@link Composite} the table viewer will be created in, may not be null
     * @param style style bits to use, may not be null, the style bits have to be passed like this: "SWT.V_SCROLL | SWT.H_SCROLL"
     * @see SWT#SINGLE
     * @see SWT#MULTI
     * @see SWT#V_SCROLL
     * @see SWT#H_SCROLL
     */
    public DefaultTableViewer(final Composite control, int style)
    {
        super(control, style | SWT.V_SCROLL | SWT.H_SCROLL);

        // default style settings
        getTable().setHeaderVisible(true);
        getTable().setLinesVisible(true);

        this.getTable().addControlListener(new ControlAdapter()
        {
            @Override
            public void controlResized(ControlEvent e)
            {
                for (int i = 0; i < DefaultTableViewer.this.getTable().getColumnCount(); i++)
                {
                    TableColumn column = DefaultTableViewer.this.getTable().getColumn(i);
                    column.setWidth((control.getClientArea().width / DefaultTableViewer.this.getTable().getColumnCount()) - 10);
                }
            }
        });

        setSorter(tableSorter);
    }

    /**
     * Sets the table sorter for this table viewer
     * 
     * @param sorter {@link ViewerSorter}, may not be null
     */
    public void setTableSorter(DefaultTableSorter sorter)
    {
        Validate.notNull(sorter);
        this.tableSorter = sorter;
        setSorter(this.tableSorter);
    }

    /**
     * Initializes the column names for the table.
     * 
     * @param columnNames String[] of column names, may not be null
     */
    protected void initColumns(String[] columnNames)
    {
        // column names
        int[] bounds = { 100, 100, 100 };

        for (int i = 0; i < columnNames.length; i++)
        {
            final int index = i;
            TableViewerColumn tableViewerColumn = new TableViewerColumn(this, SWT.FILL);
            final TableColumn column = tableViewerColumn.getColumn();

            column.setText(columnNames[i]);
            column.setWidth(bounds[i]);
            column.setResizable(true);
            column.setMoveable(true);

            // Setting the right sorter
            column.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    tableSorter.setColumn(index);
                    int dir = getTable().getSortDirection();
                    if (getTable().getSortColumn() == column)
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
                    getTable().setSortDirection(dir);
                    getTable().setSortColumn(column);
                    refresh();
                }
            });
        }
    }
}
