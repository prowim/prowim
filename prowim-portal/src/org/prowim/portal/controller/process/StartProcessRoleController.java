/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 17.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.controller.process;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Controller to fill a table and its model with data from database for roles. This will set for any <br/>
 * cell name and the data of cell.
 * 
 * @author Maziar khodaei
 * @version $Revision$
 */
public class StartProcessRoleController
{
    private final static Logger LOG           = Logger.getLogger(StartProcessRoleController.class);
    private RoleArray           roleArray     = null;
    private final int           sizeOfColumns = 2;

    /**
     * Description.
     * 
     * @param processID ID of actual process
     */
    public StartProcessRoleController(final String processID)
    {
        try
        {
            this.roleArray = MainController.getInstance().getProcessRoles(processID);
        }
        catch (Exception e)
        {
            LOG.error("Error by creating StartProcessRoleController: ", e);
        }
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
        columnNames[1] = Resources.Frames.Global.Texts.VALUE.getText();

        return columnNames;
    }

    /**
     * 
     * Method to create a model.
     * 
     * @return Object[][]list of objects. Is different optional to information type
     */
    public Object[][] getTableModel()
    {

        Object[][] rowsData = null;

        try
        {
            rowsData = new Object[this.roleArray.size()][sizeOfColumns];
            Iterator<Role> it = this.roleArray.iterator();
            int row = 0, col = 0;
            while (it.hasNext())
            {
                Role role = it.next();

                rowsData[row][col++] = role;
                rowsData[row][col++] = role.showPersonList();
                col = 0;
                row++;
            }
        }
        catch (Exception e)
        {
            LOG.error("Error by creating StartProcessRoleController: ", e);
        }
        return rowsData;
    }

}
