/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 15.02.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.export.utils.DefaultExcelSheetProperties;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.model.ProcessCategoryLeaf;
import org.prowim.portal.models.tree.model.ProcessEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessElementsFolder;
import org.prowim.portal.models.tree.model.ProcessStructureLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeRootLeaf;



/**
 * Exports all {@link Process}es and its description, its {@link Activity}s, {@link KnowledgeObject}s, {@link KnowledgeLink}s and {@link Role}s with theres {@link Person}s.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.1.0
 */
public class ExportProcesses
{
    private final ExcelExport excelExport = new ExcelExport();
    private int               sheetIndex  = 0;

    /**
     * Export {@link Process}es
     * 
     * @param leaf leaf, which calls this export
     * 
     * @see #ExportProcesses()
     */
    public void exportToExcel(DefaultLeaf leaf)
    {
        boolean export = false;

        if (leaf instanceof ProcessStructureLeaf || leaf instanceof ProcessEditorLeaf)
        {
            exportProcess(leaf.getID());
            export = true;
        }
        else if (leaf instanceof ProcessCategoryLeaf)
        {
            exportProcessesInCategory(leaf.getID(), true);
            export = true;
        }
        else if (leaf instanceof ProcessTypeEditorLeaf)
        {
            exportProcessesInCategory(leaf.getID(), false);
            export = true;
        }
        else if (leaf instanceof ProcessElementsFolder)
        {
            exportAllProcesses(true);
            export = true;
        }
        else if (leaf instanceof ProcessTypeRootLeaf)
        {
            exportAllProcesses(false);
            export = true;
        }

        if (export)
            excelExport.saveFile(Resources.Frames.Process.Texts.PROCESSES.getText());
    }

    /**
     * Exports all {@link Process}es. It can exports all {@link Process}es or only enabled {@link Process}es depend on enabled flag.
     * 
     * @param enabled <code>true</code> if only the enabled {@link Process}es should exported. <code>false</code> exports all {@link Process}es in databse.
     */
    private void exportAllProcesses(boolean enabled)
    {
        ProcessTypeArray allProcessTypes = MainController.getInstance().getAllProcessTypes();
        for (ProcessType processType : allProcessTypes)
        {
            exportProcessesInCategory(processType.getID(), enabled);
        }
    }

    /**
     * Exports {@link Process}es, which belong to {@link ProcessType}. The enabled flag make the decision for all {@link Process}es or only enabled {@link Process}es.
     * 
     * @param id Id of {@link ProcessType}.
     * @param enabled <code>true</code> if only the enabled {@link Process}es should exported. <code>false</code> exports all {@link Process}es in databse.
     */
    private void exportProcessesInCategory(String id, boolean enabled)
    {
        ProcessArray processes;
        if (enabled)
        {
            processes = MainController.getInstance().getEnabledProcesses(id);
        }
        else
        {
            processes = MainController.getInstance().getTypeProcesses(id);
        }

        for (Process process : processes)
        {
            exportProcess(process.getTemplateID());
        }
    }

    /**
     * Exports the {@link Process} with the given {@link Process} id.
     * 
     * @param processID ID of {@link Process}.
     */
    private void exportProcess(String processID)
    {
        // Get process for given ID
        Process process = MainController.getInstance().getProcess(processID);

        // Create sheet
        excelExport.createSheet((sheetIndex + 1) + ". " + process.getName());
        excelExport.initSheet(sheetIndex, getNameOfHeader(), 10);
        HSSFCellStyle headerStyle = excelExport.initHeaderStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

        // Set the header information for current process
        excelExport.createCell(sheetIndex, 3, 1, HSSFCell.CELL_TYPE_STRING, Resources.Frames.Process.Texts.PROCESS_NAME.getText(), headerStyle);
        excelExport.createCell(sheetIndex, 3, 2, HSSFCell.CELL_TYPE_STRING, process.getName(), excelExport.getDefaultStyle());

        excelExport
                .createCell(sheetIndex, 4, 1, HSSFCell.CELL_TYPE_STRING, Resources.Frames.Process.Texts.PROCESS_DESCRIPTION.getText(), headerStyle);
        excelExport.createCell(sheetIndex, 4, 2, HSSFCell.CELL_TYPE_STRING, process.getDescription(), excelExport.getDefaultStyle());

        excelExport.createCell(sheetIndex, 5, 1, HSSFCell.CELL_TYPE_STRING, Resources.Frames.Global.Texts.VERSION.getText(), headerStyle);
        excelExport.createCell(sheetIndex, 5, 2, HSSFCell.CELL_TYPE_STRING, process.getUserDefinedVersion(), excelExport.getDefaultStyle());

        excelExport.createCell(sheetIndex, 6, 1, HSSFCell.CELL_TYPE_STRING, Resources.Frames.Global.Texts.CREATED_AT.getText(), headerStyle);
        excelExport.createCell(sheetIndex, 6, 2, HSSFCell.CELL_TYPE_STRING, process.getCreateTime(), excelExport.getDefaultStyle());

        excelExport.createCell(sheetIndex, 7, 1, HSSFCell.CELL_TYPE_STRING, Resources.Frames.Global.Texts.CREATED_FROM.getText(), headerStyle);
        excelExport.createCell(sheetIndex, 7, 2, HSSFCell.CELL_TYPE_STRING, process.getOwners().toString(), excelExport.getDefaultStyle());

        excelExport.createCell(sheetIndex, 8, 1, HSSFCell.CELL_TYPE_STRING, Resources.Frames.Process.Texts.PROCESS_TYPE.getText(), headerStyle);
        excelExport.createCell(sheetIndex, 8, 2, HSSFCell.CELL_TYPE_STRING, process.getProcessType().getName(), excelExport.getDefaultStyle());

        // body row
        ExcelRow excelRow = new ExcelRow();
        // Gets activities of given process
        List<Activity> activities = process.getActivities();
        for (Activity activity : activities)
        {
            List<KnowledgeObject> knowledgeObjects = MainController.getInstance().getKnowledgeObjects(activity.getID());
            int sizeOfKnowledge = knowledgeObjects.size();
            RoleArray activityRoles = MainController.getInstance().getActivityRoles(activity.getID());
            List<Role> roles = activityRoles.getItem();
            int sizeOfRoles = activityRoles.size();
            int counter = 0;
            if (sizeOfKnowledge >= sizeOfRoles)
                counter = sizeOfKnowledge;
            else
                counter = sizeOfRoles;

            for (int i = 0; i < counter; i++)
            {
                int idy = 0;
                HashMap map = new HashMap();
                if (i == 0)
                {
                    map.put(0, activity.getName());
                    map.put(1, activity.getDescription());
                    idy = 2;
                }
                else
                {
                    map.put(idy++, "");
                    map.put(idy++, "");
                }
                if (sizeOfKnowledge > i)
                    map.put(idy++, knowledgeObjects.get(i).getName() + " " + knowledgeObjects.get(i).getKnowledgeLinks());
                else
                    map.put(idy++, "");

                if (sizeOfRoles > i)
                    map.put(idy++, roles.get(i).getName() + " " + MainController.getInstance().getPreSelection(roles.get(i).getID()).getItem());
                else
                    map.put(idy++, "");

                map.put(idy, "");
                excelRow.addRow(map);
            }
        }

        excelExport.setRows(excelRow, sheetIndex, 11, 1);
        sheetIndex++;
    }

    /**
     * 
     * Create the bottom header of a excel sheet
     * 
     * @return A {@link Map} of column index and column name
     */
    private Map<Integer, String> getNameOfHeader()
    {
        DefaultExcelSheetProperties properties = new DefaultExcelSheetProperties();
        properties.addHeaderName(1, Resources.Frames.Global.Texts.ACTIVITY.getText());
        properties.addHeaderName(2, Resources.Frames.Global.Texts.DESCRIPTION.getText());
        properties.addHeaderName(3, Resources.Frames.Knowledge.Texts.KNOW_OBJECT.getText());
        properties.addHeaderName(4, Resources.Frames.Role.Texts.ROLE.getText());

        return properties.getHeaderName();
    }
}
