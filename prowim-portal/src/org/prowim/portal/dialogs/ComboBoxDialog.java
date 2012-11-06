/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 14:14:29 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/ComboBoxDialog.java $
 * $LastChangedRevision: 4960 $
 *------------------------------------------------------------------------------
 * (c) 19.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.dialogs;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidateComboBox;
import org.prowim.rap.framework.validation.ErrorState;
import org.prowim.rap.framework.validation.Validator;



/**
 * 
 * This class allow you to get items from a ComboBox. To open this dialog call openDialog method. <br/>
 * Input are a list of objects. Output is a object.
 * 
 * 
 * @see org.prowim.rap.framework.components.Constraint
 * 
 * @author Maziar khodaei
 * @version $Revision: 4960 $
 */
public final class ComboBoxDialog extends DefaultDialog
{
    /************ Variable *****************/

    private final DefaultConstraint selectionConstraint;

    private final List<Object>      elements;

    private final ObjectArray       preselectedValue;

    private ValidateComboBox        comboBox;

    private Object                  output;

    /************ Methods *****************/
    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     * @param selectionConstraint Validation of selected value. Not null.
     * @param selectList List of elements to display in combo box. Not null. {@link Object#toString()} will be used to display the elements in the comboBox
     * @param selectedValues preselected values. Warning: Only the FIRST element in the {@link ObjectArray} will be selected. Must not be null
     */
    public ComboBoxDialog(Shell parentShell, Action action, String description, DefaultConstraint selectionConstraint, List<Object> selectList,
            ObjectArray selectedValues)
    {
        super(parentShell, action, description);

        Validate.notNull(selectionConstraint);
        Validate.notNull(selectList);
        Validate.notNull(selectedValues);

        this.selectionConstraint = selectionConstraint;
        this.elements = selectList;
        this.preselectedValue = selectedValues;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Composite createCustomArea(Composite parent)
    {
        Composite control = super.createCustomArea(parent);

        comboBox = new ValidateComboBox(control);
        comboBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        int idx = 0;
        if ( !this.selectionConstraint.isRequired())
        {
            comboBox.add(Resources.Frames.Dialog.Texts.NO_SELECTION.getText(), 0);
            comboBox.setData(Resources.Frames.Dialog.Texts.NO_SELECTION.getText(), "");
            comboBox.select(0);
            idx++;
        }

        for (Object obj : this.elements)
        {
            comboBox.add(obj.toString());
            comboBox.setData(obj.toString(), obj);
            if ((getPreselectedValue() != null) && (obj.toString().equals(getPreselectedValue().toString())))
            {
                comboBox.select(idx);
            }
            idx++;
        }

        if ((this.selectionConstraint.isRequired()) && (comboBox.getSelectionIndex() < 0))
            comboBox.select(0);
        else if (comboBox.getSelectionIndex() < 0)
        {
            comboBox.add(Resources.Frames.Dialog.Texts.NO_SELECTION.getText(), 0);
            comboBox.select(0);
        }

        comboBox.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                int selectionIndex = comboBox.getSelectionIndex();
                if (selectionIndex >= 0)
                {
                    output = comboBox.getData(comboBox.getItem(comboBox.getSelectionIndex()));
                }
            }
        });

        return control;
    }

    /**
     * 
     * This method call the constructor of this dialog and get the results and gives these back to the main frame
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param selectList List of objects to fill select list
     * @param selectedValue This are preselected values, which are preselected by modeling or by user.
     * @param defaultConstraint Included the properties min, max and required
     * @return A object, that can indeed more values. If no return value exist, than return null.
     */
    public static Object openDialog(final Shell parentShell, final Action action, final java.util.List<Object> selectList,
                                    final Object selectedValue, final DefaultConstraint defaultConstraint)
    {
        Validate.notNull(defaultConstraint);
        Validate.notNull(selectList);

        // setSelectionList(selectList);
        // setSelectValue(selectedValue);
        //
        // // constraint = defaultConstraint;
        //
        // if (new ComboBoxDialog(parentShell, action).open() == ComboBoxDialog.OK)
        // return outValue;
        // else
        return null;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        ErrorState verified = new ErrorState();
        final Validator validator = new Validator(selectionConstraint);

        verified.setErrorState(validator.checkList(comboBox.getSelectionIndex()));
        if ( !verified.hasError())
        {
            close();
        }
        else
            InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.REQUIRED_ERROR_DIALOG.getTooltip());

    }

    /**
     * getSelectValue
     * 
     * @return the selectValue. may return null
     */
    private Object getPreselectedValue()
    {
        ObjectArray objArray = preselectedValue;
        if ( !objArray.getItem().isEmpty())
            return objArray.getItem().get(0);
        else
            return null;
    }

    /**
     * 
     * Get the current selection.
     * 
     * @return the selected object. null if no item is selected.
     */
    public Object getSelection()
    {
        return this.output;
    }
}
