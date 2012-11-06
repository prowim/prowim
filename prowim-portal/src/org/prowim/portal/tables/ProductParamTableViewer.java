/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 20.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.portal.tables;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.portal.controller.dialog.InfoTypeEditSupp;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.table.DefaultContentProvider;
import org.prowim.rap.framework.components.table.DefaultTableLabelProvider;
import org.prowim.rap.framework.components.table.DefaultTableSorter;
import org.prowim.rap.framework.components.table.DefaultTableViewer;



/**
 * Table viewer for the product parameter.
 * 
 * @author Saad Wardi
 * @version $Revision$
 * @since 2.0.0
 */
public class ProductParamTableViewer extends DefaultTableViewer
{
    /** Columns name */
    protected String[] columnNames = new String[] { Resources.Frames.Global.Texts.PRODUCT_PARAM.getText(),
            Resources.Frames.Global.Texts.TYP.getText() };

    /**
     * Creates a new {@link TableViewer} for viewing {@link Activity} entities.
     * 
     * @param control the {@link Composite} parent control
     * @param border the border style
     */
    public ProductParamTableViewer(Composite control, int border)
    {
        super(control, border);

        initColumns(columnNames);

        setContentProvider(new DefaultContentProvider()
        {
            /**
             * {@inheritDoc}
             * 
             * @see org.prowim.rap.framework.components.table.DefaultContentProvider#getElements(java.lang.Object)
             */
            @Override
            public Object[] getElements(Object inputElement)
            {
                List<ProcessInformation> list = (List<ProcessInformation>) inputElement;
                return list.toArray();
            }
        });
        setLabelProvider(new DefaultTableLabelProvider()
        {
            @Override
            public String getColumnText(Object element, int columnIndex)
            {
                if (element instanceof ProcessInformation)
                {
                    ProcessInformation processInformation = (ProcessInformation) element;
                    switch (columnIndex)
                    {
                        case 0:
                            return processInformation.getName();
                        case 1:
                            return processInformation.getInfoType().getDenotation();
                        default:
                            return "";
                    }
                }
                else
                    return "";
            }
        });
        setTableSorter(new DefaultTableSorter()
        {
            /**
             * {@inheritDoc}
             * 
             * @see org.prowim.rap.framework.components.table.DefaultTableSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
             */
            @Override
            public int compare(Viewer viewer, Object e1, Object e2)
            {
                ProcessInformation a1 = (ProcessInformation) e1;
                ProcessInformation a2 = (ProcessInformation) e2;
                int rc = 0;
                switch (propertyIndex)
                {
                    case 0:
                        rc = a1.getName().compareTo(a2.getName());
                        break;
                    case 1:
                        rc = a1.getInfoType().getDenotation().compareTo(a2.getInfoType().getDenotation());
                        break;
                    default:
                        rc = 0;
                }
                // If descending order, flip the direction
                if (direction == DESCENDING)
                {
                    rc = -rc;
                }
                return rc;
            }
        });
        for (int i = 0; i < this.getTable().getColumnCount(); i++)
        {
            TableColumn viewerCol = this.getTable().getColumn(i);
            TableViewerColumn columnViewer = (TableViewerColumn) viewerCol.getData(GlobalConstants.COLUMN_VIEWER_KEY);
            columnViewer.setEditingSupport(new InfoTypeEditSupp(this, i));
        }
    }
}
