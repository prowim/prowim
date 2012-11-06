/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 13.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.swt.widgets.Composite;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.contentprovider.KnowledgeSearchContentProvider;
import org.prowim.portal.tables.labelprovider.KnowlegdeSearchLabelProvider;
import org.prowim.portal.tables.sorter.KnowledgeSearchResultSorter;
import org.prowim.rap.framework.components.table.DefaultTableViewer;



/**
 * An implementation of an {@link DefaultTableViewer table viewer} which represents the results of knowledge search.<br>
 * This table viewer supports two columns
 * 
 * <li>
 * The column for the source of the knowledge search e.g. DMS, Wiki, Wissensobjekt</li> <li>
 * The column for the title of the found knowledge</li>
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a10
 */
public class KnowledgeSearchTableViewer extends DefaultTableViewer
{
    /**
     * The column names which are shown in the table header
     */
    protected final String[] columns = new String[] { Resources.Frames.Knowledge.Texts.KNOWLEDGE_SEARCH_TABLE_SOURCE.getText(),
            Resources.Frames.Knowledge.Texts.KNOWLEDGE_SEARCH_TABLE_TITLE.getText(), Resources.Frames.Global.Texts.PART_OF.getText() };

    /**
     * Constructs this table viewer with given control and border style.<br>
     * Initializes the columns, a {@link KnowledgeSearchContentProvider content provider}, a {@link KnowlegdeSearchLabelProvider label provider}, which is responsible for the labels in the table cells, and a {@link KnowledgeSearchResultSorter sorter}
     * 
     * @param control container to show view
     * @param border the border style
     */
    public KnowledgeSearchTableViewer(Composite control, int border)
    {
        super(control, border);
        initColumns(columns);

        setContentProvider(new KnowledgeSearchContentProvider());
        setLabelProvider(new KnowlegdeSearchLabelProvider());
        setSorter(new KnowledgeSearchResultSorter());
    }

}
