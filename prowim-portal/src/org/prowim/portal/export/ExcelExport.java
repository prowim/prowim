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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.prowim.portal.view.knowledge.open.DownloadFile;



/**
 * Creates a excel page. With {@link #setRows(ExcelRow, int, int, int)} you can create rows for given sheet id. <br>
 * For saving the file you have to call {@link #saveFile(String)}. This call a "save as" dialog to download or open the created file.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public class ExcelExport
{

    private final static Logger LOG         = Logger.getLogger(ExcelExport.class);
    private final HSSFWorkbook  workBook;
    private final int           columnWidth = new Integer(30 * 256);

    /**
     * Constructor.
     */
    public ExcelExport()
    {
        this.workBook = new HSSFWorkbook();
    }

    /**
     * 
     * Create a sheet..
     * 
     * @param name name of sheet
     */
    public void createSheet(String name)
    {
        this.workBook.createSheet(name);
    }

    /**
     * 
     * Get sheet with given index.
     * 
     * @param index index of sheet
     * @return HSSFSheet
     */
    public HSSFSheet getSheetAt(int index)
    {
        return this.workBook.getSheetAt(index);
    }

    /**
     * 
     * Open a "save as" dialog to open or download the file.
     * 
     * @param name name of file
     */
    public void saveFile(String name)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            workBook.write(baos);
        }
        catch (IOException e)
        {
            LOG.error("I/O Exception by saving the excel file: " + name, e);
        }

        DownloadFile.download(baos.toByteArray(), name + ".xls");
    }

    /**
     * 
     * Builds the XML file. ExcelRow should before fills with the data.
     * 
     * @param excelRow row of sheet
     * @param sheetIndex index of sheet
     * @param headerIndex 1 if a header is given else 0
     * @param columnIndex TODO
     * 
     */
    public void setRows(ExcelRow excelRow, int sheetIndex, int headerIndex, int columnIndex)
    {
        Validate.notNull(excelRow, "ExcelRow is null.");
        Validate.notNull(sheetIndex, "Index of sheet is null.");

        HSSFSheet sheetData = this.workBook.getSheetAt(sheetIndex);

        for (int i = 0; i < excelRow.getColCount(); i++)
        {
            setColumnWith(sheetData, i);
        }

        int idx = headerIndex;
        for (int i = 0; i < excelRow.getSize(); i++)
        {

            HashMap map = excelRow.getRow(i);

            HSSFRow row = sheetData.createRow((short) idx);
            int colIndex = columnIndex;
            for (int j = 0; j < map.size(); j++)
            {
                String st = (String) map.get(j);
                HSSFCell cell = row.createCell(colIndex++, HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(st);
                if (headerIndex == 0)
                {
                    cell.setCellStyle(initHeaderStyle());
                }
                else
                    cell.setCellStyle(getDefaultStyle());

            }
            idx++;
        }
    }

    /**
     * 
     * Initialize header style
     * 
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle initHeaderStyle()
    {
        final HSSFCellStyle csHeader = this.workBook.createCellStyle();
        final HSSFFont fontHeader = this.workBook.createFont();
        // create header fonts
        fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        fontHeader.setFontHeightInPoints((short) 12);

        // set header CellStyle
        csHeader.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        csHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        csHeader.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csHeader.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csHeader.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csHeader.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csHeader.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        csHeader.setFont(fontHeader);

        return csHeader;
    }

    /**
     * 
     * Description.
     * 
     * @return {@link HSSFCellStyle}
     */
    public HSSFCellStyle getDefaultStyle()
    {
        HSSFCellStyle createCellStyle = this.workBook.createCellStyle();
        createCellStyle.setWrapText(true);
        createCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        return createCellStyle;
    }

    /**
     * 
     * Sets header of excel sheet.
     * 
     * @param excelRow header row
     * @param sheetIndex index of sheet
     */
    public void setHeader(ExcelRow excelRow, int sheetIndex)
    {
        setRows(excelRow, sheetIndex, 0, 0);
    }

    /**
     * Initializes the sheet header columns.
     * 
     * @param sheetIndex index of sheet
     * 
     */
    public void initSheet(int sheetIndex)
    {
        HSSFSheet sheet = this.workBook.getSheetAt(sheetIndex);

        Map<Integer, String> headerNames = ExcelSheetDefinition.getHeaderNames(sheet.getSheetName());
        HSSFRow headerRow = sheet.createRow((short) 0);
        if (headerNames != null)
        {
            for (Integer col : headerNames.keySet())
            {
                setColumnWith(sheet, col);
                HSSFCell cell = headerRow.createCell(col, HSSFCell.CELL_TYPE_STRING);
                cell.setCellValue(headerNames.get(col));
                cell.setCellStyle(initHeaderStyle());
            }
        }
    }

    /**
     * Initializes the sheet header columns.
     * 
     * @param sheetIndex index of sheet
     * @param headerNames name of header
     * @param rowIndex index of row
     * 
     */
    public void initSheet(int sheetIndex, Map<Integer, String> headerNames, int rowIndex)
    {
        Validate.notNull(headerNames, "The list of headers can not be null.");
        HSSFSheet sheet = this.workBook.getSheetAt(sheetIndex);

        HSSFRow headerRow = sheet.createRow(rowIndex);
        for (Integer col : headerNames.keySet())
        {
            setColumnWith(sheet, col);
            HSSFCell cell = headerRow.createCell(col, HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(headerNames.get(col));
            cell.setCellStyle(initHeaderStyle());
        }
    }

    /**
     * 
     * Creates a cell in a excel sheet in given position (row and column) and given style. You can use it if you don´t want create several rows <br>
     * with using {@link ExcelRow}. You have to use it if you want create individual cells with a specially style and value. <br>
     * Use {@link #setRows(ExcelRow, int, int, int)} with {@link ExcelRow} if you want to write/set a long list of data with the same schema.
     * 
     * @param sheetIndex index of sheet. Not null.
     * @param rowIndex index of row. Not null.
     * @param columnIndex index of column. Not null.
     * @param cellType type of cell {@link HSSFCell}. Not null.
     * @param cellValue value of cell. Not null.
     * @param cellStyle style of cell {@link HSSFCellStyle}. Not null.
     */
    public void createCell(int sheetIndex, int rowIndex, int columnIndex, int cellType, String cellValue, HSSFCellStyle cellStyle)
    {
        HSSFSheet sheet = this.workBook.getSheetAt(sheetIndex);

        HSSFRow headerRow = sheet.getRow(rowIndex);
        if (headerRow == null)
            headerRow = sheet.createRow(rowIndex);

        setColumnWith(sheet, columnIndex);
        HSSFCell cell = headerRow.createCell(columnIndex, HSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(cellValue);
        cell.setCellStyle(cellStyle);
    }

    // Set the column width in the given sheet for the given index.
    private void setColumnWith(HSSFSheet sheet, int index)
    {
        sheet.setColumnWidth(index, this.columnWidth);
    }
}
