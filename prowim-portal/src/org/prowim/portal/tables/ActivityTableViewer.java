/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 28.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.contentprovider.ActivityContentProvider;
import org.prowim.portal.tables.labelprovider.ActivityLabelProvider;
import org.prowim.portal.tables.sorter.ActivityTableSorter;
import org.prowim.rap.framework.components.table.DefaultTableViewer;



/**
 * Table which shows activities.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 7.3a
 */
public class ActivityTableViewer extends DefaultTableViewer
{
    /** Columns name */
    protected String[] columnNames = new String[] { Resources.Frames.Process.Texts.PROCESS.getText(),
            Resources.Frames.Activity.Texts.DELIVERABLES_ACTIVITY.getText(), Resources.Frames.Activity.Texts.TABLE_HEADER_ACTIVSINCE.getText() };

    /**
     * Creates a new {@link TableViewer} for viewing {@link Activity} entities.
     * 
     * @param control the {@link Composite} parent control
     * @param border the border style
     */
    public ActivityTableViewer(Composite control, int border)
    {
        super(control, border);

        initColumns(columnNames);

        setContentProvider(new ActivityContentProvider());
        setLabelProvider(new ActivityLabelProvider());
        setTableSorter(new ActivityTableSorter());
    }
}
