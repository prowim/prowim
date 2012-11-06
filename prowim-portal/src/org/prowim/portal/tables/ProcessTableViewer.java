/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 10.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.prowim.datamodel.prowim.Process;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.sorter.ProcessTableSorter;
import org.prowim.rap.framework.components.table.DefaultContentProvider;
import org.prowim.rap.framework.components.table.DefaultTableLabelProvider;
import org.prowim.rap.framework.components.table.DefaultTableViewer;



/**
 * Tables viewer shows processes in a table.
 * 
 * @author Saad Wardi
 * @version $Revision$
 * @since 2.0.0
 */

public class ProcessTableViewer extends DefaultTableViewer
{
    /** Columns name */
    protected String[] columnNames = new String[] { Resources.Frames.Global.Texts.NAME.getText(), Resources.Frames.Global.Texts.DESCRIPTION.getText() };

    /**
     * Creates a new {@link TableViewer} for viewing {@link Process} entities.
     * 
     * @param control the {@link Composite} parent control
     * @param border the border style
     */
    public ProcessTableViewer(Composite control, int border)
    {
        super(control, border);

        initColumns(columnNames);

        setContentProvider(new DefaultContentProvider()
        {
            @Override
            public Object[] getElements(Object inputElement)
            {
                List<Process> list = (List<Process>) inputElement;
                return list.toArray();
            }
        });
        setLabelProvider(new DefaultTableLabelProvider()
        {

            @Override
            public String getColumnText(Object element, int columnIndex)
            {
                Process process = (Process) element;
                switch (columnIndex)
                {
                    case 0:
                        return process.getName();
                    case 1:
                        return MainController.getInstance().getDescription(process.getTemplateID());
                    default:
                        return "";
                }
            }
        });

        tableSorter = new ProcessTableSorter();
        setSorter(tableSorter);

    }
}
