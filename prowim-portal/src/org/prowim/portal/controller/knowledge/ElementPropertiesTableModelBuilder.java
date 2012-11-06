/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 01.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.controller.knowledge;

import org.apache.log4j.Logger;
import org.prowim.datamodel.collections.InstancePropertyArray;
import org.prowim.datamodel.prowim.InstanceProperty;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Controller to get attributes of given object.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ElementPropertiesTableModelBuilder
{

    @SuppressWarnings("unused")
    private final static Logger LOG          = Logger.getLogger(ElementPropertiesTableModelBuilder.class);

    private final static int    COLUMN_COUNT = 2;

    private final String[]      columnNames;

    private final Object[][]    tableModel;

    /**
     * The constructor returns all attributes to given ID.
     * 
     * @param objectID ID of given object
     */
    public ElementPropertiesTableModelBuilder(final String objectID)
    {
        InstancePropertyArray properties = MainController.getInstance().getProperties(objectID);

        // build column names
        this.columnNames = new String[COLUMN_COUNT];
        columnNames[0] = Resources.Frames.Global.Texts.DENOTATION.getText();
        columnNames[1] = Resources.Frames.Global.Texts.VALUE.getText();

        // build table model
        tableModel = new Object[properties.size()][COLUMN_COUNT];
        int row = 0;
        for (InstanceProperty knowObj : properties)
        {

            tableModel[row][0] = knowObj;
            tableModel[row][1] = knowObj.getValue();

            row++;
        }
    }

    /**
     * Creates the header for the table.
     * 
     * @return non null array of header String
     */
    public String[] getColumns()
    {
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
        return tableModel;
    }

}
