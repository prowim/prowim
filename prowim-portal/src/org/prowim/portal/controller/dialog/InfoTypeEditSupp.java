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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.prowim.datamodel.collections.InformationTypeArray;
import org.prowim.datamodel.prowim.InformationType;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.ProductParamDialog;
import org.prowim.portal.utils.GlobalConstants;



/**
 * This class supports editing the table, which shows the process informations in {@link ProductParamDialog}
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class InfoTypeEditSupp extends EditingSupport
{

    private static final List<String>   INFORMATIONTYPES = new ArrayList<String>();
    private CellEditor                  editor;
    private final int                   column;
    private final String[]              infoType;
    private final List<InformationType> infoTypeList;

    static
    {
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_DATE);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_COMBO_BOX);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_COMBO_BOX_CONTROL_FLOW);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_MULTI_LIST_CONTROL_FLOW);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_TIME_STAMP);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_PERSONS);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_SINGLE_LIST);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_ORGANIZATION_UNIT);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_MULTI_LIST);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_FLOAT);
        INFORMATIONTYPES.add(GlobalConstants.InformationTypes.INFORMATION_TYPE_INTEGER);
    }

    /**
     * Constructor.
     * 
     * @param viewer {@link ColumnViewer} to set the {@link InformationType}s.
     * @param column column
     */
    public InfoTypeEditSupp(ColumnViewer viewer, int column)
    {
        super(viewer);

        InformationTypeArray infoTypeArray = MainController.getInstance().getInformationTypes();

        infoTypeList = infoTypeArray.getItem();

        List<String> tempList = new ArrayList<String>();

        for (int i = 0; i < infoTypeList.size(); i++)
        {
            if ( !isNotSupported(infoTypeList.get(i).getID()))
                tempList.add(infoTypeList.get(i).getDenotation());
        }
        Collections.sort(tempList);
        infoType = new String[tempList.size()];
        for (int i = 0; i < tempList.size(); i++)
        {
            infoType[i] = tempList.get(i);
        }

        // Create the correct editor based on the column index
        switch (column)
        {
            case 0:
                editor = new TextCellEditor(((TableViewer) viewer).getTable());
                break;
            case 1:
                editor = new ComboBoxCellEditor(((TableViewer) viewer).getTable(), infoType, SWT.READ_ONLY);
                break;
            default:
                editor = new TextCellEditor(((TableViewer) viewer).getTable());
        }
        this.column = column;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
     */
    @Override
    protected boolean canEdit(Object element)
    {
        switch (this.column)
        {
            case 0:
                return true;
            case 1:
                return true;
            default:
                return true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#getCellEditor(java.lang.Object)
     */
    @Override
    protected CellEditor getCellEditor(Object element)
    {
        return editor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
     */
    @Override
    protected Object getValue(Object element)
    {
        Object obj = element;

        switch (this.column)
        {
            case 0:
                ProcessInformation processInfo = (ProcessInformation) obj;
                return processInfo.getName();
            case 1:
                for (int i = 0; i < infoType.length; i++)
                {
                    if (obj.toString().equals(infoType[i]))
                        return i;
                }
                return 0;
            default:
                break;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void setValue(Object element, Object value)
    {
        Object obj = element;
        ProcessInformation processInfo = (ProcessInformation) obj;
        switch (this.column)
        {
            case 0:
                processInfo.setName(String.valueOf(value));
                break;
            case 1:
                String infoTypeValue = infoType[(Integer) value];
                processInfo.getInfoType().setDenotation(infoTypeValue);
                processInfo.getInfoType().setID(getID(infoTypeValue));
                obj = infoTypeValue;
                break;
            default:
                break;
        }

        getViewer().update(element, null);
    }

    private String getID(String name)
    {
        for (int i = 0; i < infoTypeList.size(); i++)
        {
            if (name.equals(infoTypeList.get(i).getDenotation()))
                return infoTypeList.get(i).getID();
        }
        return "";
    }

    /**
     * Checks if a type is supported or no.
     * 
     * @param informationType the information type ID.
     * @return true if the type is supported otherwise false.
     */
    private boolean isNotSupported(String informationType)
    {
        return INFORMATIONTYPES.contains(informationType);
    }
}
