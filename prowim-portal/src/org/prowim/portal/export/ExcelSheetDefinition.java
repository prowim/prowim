/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.Validate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.prowim.portal.i18n.Resources;



/**
 * Constants of a excel sheet. You can find here the name of headers or the name of sheets and there type.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public final class ExcelSheetDefinition
{

    /** Map which stores the cell names and cell types for the different excel sheets */
    private static Map<String, Map<Integer, String>>  cellHeader = new HashMap<String, Map<Integer, String>>();
    private static Map<String, Map<Integer, Integer>> cellTypes  = new HashMap<String, Map<Integer, Integer>>();

    /**
     * 
     * Default constructor.
     */
    private ExcelSheetDefinition()
    {
    }

    static
    {
        // create cell header maps
        // For knowledge objects export
        Map<Integer, String> knowledgeObjectDataHeader = new TreeMap<Integer, String>();
        Map<Integer, Integer> knowledgeObjectDataTypes = new TreeMap<Integer, Integer>();

        knowledgeObjectDataHeader.put(0, Resources.Frames.Knowledge.Texts.KNOW_OBJECT.getText());
        knowledgeObjectDataHeader.put(1, Resources.Frames.Knowledge.Texts.KNOW_LINK.getText());

        knowledgeObjectDataTypes.put(0, HSSFCell.CELL_TYPE_STRING);
        knowledgeObjectDataTypes.put(1, HSSFCell.CELL_TYPE_STRING);

        cellHeader.put(Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText(), knowledgeObjectDataHeader);
        cellTypes.put(Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText(), knowledgeObjectDataTypes);

        // For document export
        Map<Integer, String> documentDataHeader = new TreeMap<Integer, String>();
        Map<Integer, Integer> documntDataTypes = new TreeMap<Integer, Integer>();

        documentDataHeader.put(0, Resources.Frames.Global.Texts.DOCUMENT.getText());

        documntDataTypes.put(0, HSSFCell.CELL_TYPE_STRING);

        cellHeader.put(Resources.Frames.Global.Texts.DOCUMENTS.getText(), documentDataHeader);
        cellTypes.put(Resources.Frames.Global.Texts.DOCUMENTS.getText(), documntDataTypes);

        // For process category
        Map<Integer, String> processElementsDataHeader = new TreeMap<Integer, String>();
        Map<Integer, Integer> processElementsDataTypes = new TreeMap<Integer, Integer>();

        processElementsDataHeader.put(0, Resources.Frames.Process.Texts.PROCESS_TYPE.getText());
        processElementsDataHeader.put(1, Resources.Frames.Process.Texts.PROCESS.getText());

        processElementsDataTypes.put(0, HSSFCell.CELL_TYPE_STRING);

        cellHeader.put(Resources.Frames.Global.Texts.PROCESS_ELEMENTS.getText(), processElementsDataHeader);
        cellTypes.put(Resources.Frames.Global.Texts.PROCESS_ELEMENTS.getText(), processElementsDataTypes);

    }

    /**
     * Returns the column header names for the requested sheet.
     * 
     * @param sheetName the name of the sheet, may not be null
     * @return {@link Map} or null if sheet name is not known
     */
    public static Map<Integer, String> getHeaderNames(String sheetName)
    {
        Validate.notNull(sheetName);

        return cellHeader.get(sheetName);
    }

}
