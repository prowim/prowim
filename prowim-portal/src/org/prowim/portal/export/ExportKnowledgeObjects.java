/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.export;

import java.util.HashMap;
import java.util.List;

import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Exports all knowledge objects. The excel file has two columns: <br>
 * 
 * {@link KnowledgeObject} and {@link KnowledgeLink}
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public class ExportKnowledgeObjects
{
    /**
     * Export knowledge objects
     * 
     * @see ExportKnowledgeObjects
     */
    public void exportToExcel()
    {
        KnowledgeObjectArray knowledgeObjectArray = MainController.getInstance().getKnowledgeObjects();

        ExcelRow excelRow = new ExcelRow();

        for (KnowledgeObject knowledgeObject : knowledgeObjectArray)
        {
            HashMap map = new HashMap();
            map.put(0, knowledgeObject.getName());
            excelRow.addRow(map);

            List<KnowledgeLink> knowledgeLinks = knowledgeObject.getKnowledgeLinks();

            for (KnowledgeLink knowledgeLink : knowledgeLinks)
            {
                int idy = 0;
                map = new HashMap();
                map.put(idy++, knowledgeObject.getName());
                map.put(idy, knowledgeLink.getName());
                excelRow.addRow(map);

            }
        }

        // Create excel
        ExcelExport excelExport = new ExcelExport();
        excelExport.createSheet(Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText());
        excelExport.initSheet(0);
        excelExport.setRows(excelRow, 0, 1, 0);
        excelExport.saveFile(Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText());
    }
}
