/*==============================================================================
 * File $Id: DocumentVersionController.java 4713 2010-08-27 16:12:35Z wiesner $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-27 18:12:35 +0200 (Fr, 27 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/controller/dialog/DocumentVersionController.java $
 * $LastChangedRevision: 4713 $
 *------------------------------------------------------------------------------
 * (c) 01.04.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.controller.dialog;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.Version;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Provides a model for the versions history tables.
 * 
 * @author Saad Wardi
 * @version $Revision: 4713 $
 * @since 2.0
 */
public class DocumentVersionController
{
    private final static Logger LOG           = Logger.getLogger(RightsRoleController.class);
    private final int           sizeOfColumns = 3;

    /**
     * Constructs this.
     */
    public DocumentVersionController()
    {
    }

    /**
     * Creates the header for the table.
     * 
     * @return non null array of header String
     */
    public String[] getColumns()
    {
        String[] columnNames = new String[sizeOfColumns];
        columnNames[0] = Resources.Frames.Global.Texts.LABEL.getText();
        columnNames[1] = Resources.Frames.Global.Texts.AUTHOR.getText();
        columnNames[2] = Resources.Frames.Global.Texts.CREATED_AT.getText();
        return columnNames;
    }

    /**
     * Creates the header for the table.
     * 
     * @return non null array of header String
     */
    public String[] getColumnsForDocuments()
    {
        String[] columnNames = new String[3];
        columnNames[0] = "title";
        columnNames[1] = "beschreibung";
        columnNames[2] = "ID";
        // columnNames[2] = Resources.Frames.Global.Texts.CREATED_AM.getText();
        return columnNames;
    }

    /**
     * 
     * Method to create a model.
     * 
     * @param values show this list, if it is not null.
     * 
     * @return Object[][]list of objects. Is different optional to information type
     */
    public Object[][] getTableModel(final List<Version> values)
    {
        Object[][] rowsData = null;
        if (values != null)
        {
            rowsData = new Object[values.size()][sizeOfColumns];
            Iterator<Version> it = values.iterator();
            int row = 0, col = 0;

            while (it.hasNext())
            {
                Version version = it.next();
                rowsData[row][col++] = version;
                rowsData[row][col++] = version.getCreator();
                rowsData[row][col++] = version.getCreateTime();
                col = 0;
                row++;
            }
        }
        else
        {
            rowsData = new Object[0][0];

        }
        return rowsData;
    }

    /**
     * 
     * Method to create a model by getting documents data from server.
     * 
     * 
     * @return Object[][] not null data buffer.
     */
    public Object[][] getTableModel()
    {
        Object[][] rowsData = null;
        try
        {
            List<DocumentContentProperties> propsList = MainController.getInstance().getAllDocuments();
            rowsData = new Object[propsList.size()][3];
            Iterator<DocumentContentProperties> it = propsList.iterator();
            int row = 0, col = 0;
            while (it.hasNext())
            {
                DocumentContentProperties prop = it.next();
                rowsData[row][col++] = prop;
                rowsData[row][col++] = prop.getTitle();
                rowsData[row][col++] = prop.getID();
                col = 0;
                row++;

            }
        }
        catch (Exception e)
        {
            LOG.error("Error by creating rights roles data buffer: ", e);
        }
        return rowsData;
    }
}
