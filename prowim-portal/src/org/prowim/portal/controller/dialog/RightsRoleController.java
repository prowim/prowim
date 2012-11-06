/*==============================================================================
 * File $Id: RightsRoleController.java 4319 2010-07-19 14:29:13Z wardi $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-19 16:29:13 +0200 (Mo, 19 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/controller/dialog/RightsRoleController.java $
 * $LastChangedRevision: 4319 $
 *------------------------------------------------------------------------------
 * (c) 24.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.datamodel.security.RightsRole;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Title and description.
 * 
 * @author !!YOUR NAME!!
 * @version $Revision: 4319 $
 * @since !!VERSION!!
 */
public class RightsRoleController
{
    private final static Logger LOG           = Logger.getLogger(RightsRoleController.class);
    private final int           sizeOfColumns = 2;

    /**
     * Description.
     */
    public RightsRoleController()
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
        columnNames[0] = Resources.Frames.Global.Texts.NAME.getText();
        columnNames[1] = Resources.Frames.Global.Texts.DESCRIPTION.getText();

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
    public Object[][] getTableModel(final List<RightsRole> values)
    {

        Object[][] rowsData = null;

        if (values != null)
        {
            rowsData = new Object[values.size()][sizeOfColumns];
            Iterator<RightsRole> it = values.iterator();
            int row = 0, col = 0;

            while (it.hasNext())
            {
                RightsRole role = it.next();
                rowsData[row][col++] = role;
                if (role.getDescription() != null)
                    rowsData[row][col++] = role.getDescription();
                else
                    rowsData[row][col++] = "";
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
     * Method to create a model.
     * 
     * @param userID not null user ID.
     * 
     * @return Object[][] not null data buffer.
     */
    public Object[][] getTableModel(final String userID)
    {

        Object[][] rowsData = null;

        try
        {

            List<RightsRole> roleList = MainController.getInstance().getRightsRoles(userID);
            rowsData = new Object[roleList.size()][sizeOfColumns];
            Iterator<RightsRole> it = roleList.iterator();
            int row = 0, col = 0;

            while (it.hasNext())
            {
                RightsRole role = it.next();
                rowsData[row][col++] = role;
                if (role.getDescription() != null)
                    rowsData[row][col++] = role.getDescription();
                else
                    rowsData[row][col++] = "";
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
