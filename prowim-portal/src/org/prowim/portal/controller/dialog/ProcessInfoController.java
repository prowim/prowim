/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 03.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Controller to get process informations and set this in a table.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class ProcessInfoController
{
    private static final int    PROCESS_INFORMATION = 0;
    private static final int    INFORMATION_TYPE    = 1;

    private final static Logger LOG                 = Logger.getLogger(ProcessInfoController.class);
    private final int           sizeOfColumns       = 2;

    /**
     * Default constructor.
     */
    public ProcessInfoController()
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
        columnNames[PROCESS_INFORMATION] = Resources.Frames.Global.Texts.PRODUCT_PARAM.getText();
        columnNames[INFORMATION_TYPE] = Resources.Frames.Global.Texts.INFORMATION_TYPE.getText();

        return columnNames;
    }

    /**
     * 
     * Method to create a model.
     * 
     * @param referenceID id of {@link ProcessElement}s. Not null.
     * 
     * @return Object[][]list of objects. Is different optional to information type
     */
    public Object[][] getTableModel(String referenceID)
    {
        Object[][] rowsData = null;

        try
        {
            ObjectArray objectArray = MainController.getInstance().getProductsOfActivity(referenceID);

            List<Object> objectList = objectArray.getItem();
            rowsData = new Object[objectList.size()][sizeOfColumns + 1];
            Iterator<Object> it = objectList.iterator();
            int row = 0;
            while (it.hasNext())
            {
                ProcessInformation processInfo = (ProcessInformation) it.next();
                rowsData[row][PROCESS_INFORMATION] = processInfo;
                rowsData[row][INFORMATION_TYPE] = processInfo.getInfoType().getDenotation();
                row++;
            }

        }
        catch (Exception e)
        {
            LOG.error("Error by ProcessInfoController: ", e);
        }
        return rowsData;
    }

    /**
     * 
     * Method to create a model.
     * 
     * @param referenceID id of {@link ProcessElement}s. Not null.
     * 
     * @return Object[][]list of objects. Is different optional to information type
     */
    public Object[][] addRowToTableModel(String referenceID)
    {
        Object[][] rowsData = null;

        try
        {

            MainController.getInstance().addProcessInformationToProduct(referenceID);
            ObjectArray objectArray = MainController.getInstance().getProductsOfActivity(referenceID);

            List<Object> objectList = objectArray.getItem();
            rowsData = new Object[objectList.size()][sizeOfColumns];
            Iterator<Object> it = objectList.iterator();
            int row = 0;
            while (it.hasNext())
            {
                ProcessInformation processInfo = (ProcessInformation) it.next();
                rowsData[row][PROCESS_INFORMATION] = processInfo;
                rowsData[row][INFORMATION_TYPE] = processInfo.getInfoType().getDenotation();
                row++;
            }
            // ProcessInformation pi = new ProcessInformation("123", "ADDED", "2010");
            // pi.setInfoType(new InformationType("34", "dummy", new Long(1223), new Long(1223), "Inhalt_dummy"));
            // rowsData[row][PROCESS_INFORMATION] = pi;
            // rowsData[row][INFORMATION_TYPE] = "pitype";
        }
        catch (Exception e)
        {
            LOG.error("Error by ProcessInfoController: ", e);
        }
        return rowsData;
    }
}
