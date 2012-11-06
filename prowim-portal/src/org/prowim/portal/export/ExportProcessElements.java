/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 22.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 */
package org.prowim.portal.export;

import java.util.HashMap;

import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Exports process categories and included processes. Excel sheet has tow rows: <br>
 * 
 * {@link ProcessType} and {@link Process}
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public class ExportProcessElements
{

    /**
     * Export {@link Process}
     * 
     * @see #ExportProcessElements()
     */
    public void exportToExcel()
    {
        ExcelRow excelRow = new ExcelRow();

        ProcessTypeArray processTypes = MainController.getInstance().getAllProcessTypes();

        for (ProcessType processType : processTypes)
        {

            HashMap map = new HashMap();
            map.put(0, processType.getName());
            excelRow.addRow(map);

            ProcessArray processes = MainController.getInstance().getTypeProcesses(processType.getID());

            for (Process process : processes)
            {
                int idy = 0;
                map = new HashMap();
                map.put(idy++, processType.getName());
                map.put(idy, process.getName());
                excelRow.addRow(map);
            }
        }

        // Create excel
        ExcelExport excelExport = new ExcelExport();
        excelExport.createSheet(Resources.Frames.Global.Texts.PROCESS_ELEMENTS.getText());
        excelExport.initSheet(0);
        excelExport.setRows(excelRow, 0, 1, 0);
        excelExport.saveFile(Resources.Frames.Global.Texts.PROCESS_ELEMENTS.getText());
    }
}
