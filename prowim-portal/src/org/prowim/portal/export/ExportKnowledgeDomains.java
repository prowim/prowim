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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;



/**
 * Exports all knowledge domains. Excel sheet included at least one row: <br>
 * {@link KnowledgeDomain}
 * 
 * If it´s exist sub domains, these will be add in next columns. <br>
 * 
 * For Example: <br>
 * 
 * {@link KnowledgeDomain}, {@link KnowledgeDomain} (2. level), {@link KnowledgeDomain}(3. level), ....
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public class ExportKnowledgeDomains
{

    /**
     * Export to excel.
     * 
     * @see #ExportKnowledgeDomains()
     */
    public void exportToExcel()
    {
        List<KnowledgeDomain> knowledgeDomains = MainController.getInstance().getTopDomainKnowledge();
        int index = 0;
        ArrayList arrayList = new ArrayList<HashMap>();
        ExcelRow excelRow = new ExcelRow();
        for (KnowledgeDomain knowledgeDomain : knowledgeDomains)
        {
            HashMap<Integer, HashMap> rowList = new HashMap<Integer, HashMap>();

            getSubKnowledgeDomains(rowList, knowledgeDomain, 0, excelRow);
            arrayList.add(index, rowList);
            index++;
        }

        ExcelExport excelExport = new ExcelExport();
        excelExport.createSheet(Resources.Frames.Knowledge.Texts.KNOW_DOMAINS.getText());
        // Create excel
        ExcelRow headerRow = initHeader(excelRow.getColCount());

        excelExport.setHeader(headerRow, 0);
        excelExport.setRows(excelRow, 0, 1, 0);

        excelExport.saveFile(Resources.Frames.Knowledge.Texts.KNOW_DOMAINS.getText());
    }

    /**
     * 
     * Get sub domains
     * 
     * @param rowList actually row.
     * @param knowledgeDomain actually {@link KnowledgeDomain}.
     * @param index index of recursion.
     * @param excelRow actual row in {@link ExcelRow}.
     */
    private void getSubKnowledgeDomains(HashMap<Integer, HashMap> rowList, KnowledgeDomain knowledgeDomain, int index, ExcelRow excelRow)
    {
        List<KnowledgeDomain> subNodes = MainController.getInstance().getSubDomainKnow(knowledgeDomain.getID());

        HashMap list = rowList.get(index - 1);
        HashMap temp = new HashMap();

        if (list != null)
        {
            for (int i = 0; i < list.size(); i++)
            {
                temp.put(i, list.get(i));
            }
        }

        temp.put(temp.size(), knowledgeDomain.getName());
        rowList.put(rowList.size(), temp);
        excelRow.addRow(temp);

        if ( !subNodes.isEmpty())
        {
            for (KnowledgeDomain knowledgeDomain2 : subNodes)
            {
                getSubKnowledgeDomains(rowList, knowledgeDomain2, index + 1, excelRow);
            }
        }
    }

    /**
     * 
     * Initialize the header for this export.
     * 
     * @param colCount Maximal count of columns
     * @return {@link ExcelRow}
     */
    private ExcelRow initHeader(int colCount)
    {
        ExcelRow headerRow = new ExcelRow();

        HashMap<Integer, String> headerMap = new HashMap<Integer, String>();
        for (int i = 0; i < colCount; i++)
        {
            headerMap.put(i,
                          Resources.Frames.Knowledge.Texts.KNOW_DOMAIN.getText() + "(" + (i + 1) + ". "
                                  + Resources.Frames.Global.Texts.LEVEL.getText() + ")");
        }

        headerRow.addRow(headerMap);
        return headerRow;
    }
}
