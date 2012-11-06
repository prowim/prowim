/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 02.10.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.controller.dialog;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.process.StartProcessRoleController;
import org.prowim.portal.i18n.Resources;



/**
 * Controller to fill table with organizations unit. The data can come from a array or from database. <br>
 * If parameter is null gets the informations from database, else gets informations from array in parameter
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class OrgaController
{
    private final static Logger LOG           = Logger.getLogger(StartProcessRoleController.class);
    private final int           sizeOfColumns = 1;
    private List<Organization>  orgaList;

    /**
     * Description.
     */
    public OrgaController()
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
        columnNames[0] = Resources.Frames.Global.Texts.ORGANISATION_UNIT.getText();

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
    public Object[][] getTableModel(final List<Organization> values)
    {
        Object[][] rowsData = null;

        if (values != null)
        {
            rowsData = new Object[values.size()][sizeOfColumns];
            Iterator<Organization> it = values.iterator();
            int row = 0, col = 0;

            while (it.hasNext())
            {
                Organization orga = it.next();
                rowsData[row][col++] = orga;
                col = 0;
                row++;
            }
        }
        else
        {
            try
            {
                // OrganizationRemote organisationService = new OrganizationService().getOrganizationServicePort();
                OrganisationArray orgaArray = MainController.getInstance().getOrganizations();
                orgaList = orgaArray.getItem();
                rowsData = new Object[orgaList.size()][sizeOfColumns];
                Iterator<Organization> it = orgaList.iterator();
                int row = 0, col = 0;

                while (it.hasNext())
                {
                    Organization orga = it.next();
                    rowsData[row][col++] = orga;
                    col = 0;
                    row++;
                }
            }
            catch (Exception e)
            {
                LOG.error("Error by creating OrgaController: ", e);
            }

        }
        return rowsData;
    }

    /**
     * 
     * Method to create a model.
     * 
     * @param selectedValues show this list, if it is not null.
     * 
     * @return Object[][]list of objects. Is different optional to information type
     */
    public Object[][] getTableModelUnique(final List<Organization> selectedValues)
    {
        Object[][] rowsData = null;

        try
        {
            OrganisationArray orgaArray = MainController.getInstance().getOrganizations();
            orgaList = orgaArray.getItem();
            rowsData = new Object[orgaList.size() - selectedValues.size()][sizeOfColumns];
            Iterator<Organization> it = orgaList.iterator();
            int row = 0, col = 0;

            while (it.hasNext())
            {
                Organization orga = it.next();
                Iterator<Organization> selectedIt = selectedValues.iterator();
                boolean exist = false;
                while (selectedIt.hasNext())
                {
                    Organization selectedOrga = selectedIt.next();
                    String selectedOrgID = selectedOrga.getID();
                    if (orga.getID().equals(selectedOrgID))
                    {
                        exist = true;
                        break;
                    }
                }

                if ( !exist)
                {
                    rowsData[row][col++] = orga;
                    col = 0;
                    row++;
                }
            }
        }
        catch (Exception e)
        {
            LOG.error("Error by creating OrgaController: ", e);
        }

        return rowsData;
    }

    /**
     * Get the list of persons back, which are in database
     * 
     * @return the pesonList
     */
    public List<Organization> getOrganizationList()
    {
        return this.orgaList;
    }

}
