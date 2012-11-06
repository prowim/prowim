/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 14:14:29 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/MultiListDialog.java $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.prowim.portal.i18n.Resources;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ListLimit;
import org.prowim.rap.framework.validation.ErrorState;
import org.prowim.rap.framework.validation.Validator;



/**
 * This class allow you to get multiple items from a list. To open this dialog call openDialog method. Input value should be a List of <br/>
 * objects.
 * 
 * @see org.prowim.rap.framework.components.Constraint
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4960 $
 */
public final class MultiListDialog extends DefaultDialog
{
    /************ Variable *****************/
    private ListLimit               multiList;
    private final List<Object>      listValues;
    private List<Object>            selectedValues = new ArrayList<Object>();
    private final DefaultConstraint constraint;

    /************ Methods *****************/

    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     * @param listValues List of objects to fill select list. Not null.
     * @param preSelectedValues This are preselected values, which are preselected by modeling or by user.
     * @param defaultConstraint Included the properties min, max and required
     */
    public MultiListDialog(Shell parentShell, Action action, String description, final java.util.List<Object> listValues,
            final java.util.List<Object> preSelectedValues, DefaultConstraint defaultConstraint)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage());

        Validate.notNull(listValues, "listValues can not be null");
        Validate.notNull(defaultConstraint, "Constraint can not be null");

        this.listValues = listValues;
        this.selectedValues = preSelectedValues;
        this.constraint = defaultConstraint;
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

        multiList = new ListLimit(control, SWT.MULTI, constraint);
        multiList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        List<Object> items = this.listValues;
        Iterator<Object> it = items.iterator();

        int idx = 0;
        while (it.hasNext())
        {
            Object obj = it.next();

            multiList.add(obj.toString());
            multiList.setData(obj.toString(), obj);
            if (isValueInList(obj))
                multiList.select(idx);
            idx++;
        }

        if ((constraint.isRequired()) && (multiList.getSelectionIndex() < 0))
            multiList.setSelection(0);

        multiList.setFocus();

        multiList.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if ( !isValueInList(e))
                    selectedValues = returnValue();
            }
        });

        return control;
    }

    /**
     * 
     * Get list of objects of selected item
     * 
     * @return Object. If object not found return empty.
     */
    private java.util.List<Object> returnValue()
    {
        // Create temporary list
        java.util.List<Object> tempList = new ArrayList<Object>();

        int[] selectedIndexes = multiList.getSelectionIndices();
        for (int i = 0; i < multiList.getSelectionCount(); i++)
        {
            tempList.add(multiList.getData(multiList.getItem(selectedIndexes[i])));
        }
        return tempList;
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
        setReturnCode(OK);

        ErrorState verified = new ErrorState();
        final Validator validator = new Validator(constraint);

        verified.setErrorState(validator.checkList(multiList.getSelectionIndex()));
        if ( !verified.hasError())
        {
            close();
        }
    }

    /**
     * 
     * This method return true, if the given object is included in selected values. Else return false.
     * 
     * @param object A object which are in selection list
     * @return true, if object is in in selected value list, else false.
     */
    private boolean isValueInList(Object object)
    {
        List<Object> selectValues = this.selectedValues;

        Iterator<Object> it = selectValues.iterator();

        while (it.hasNext())
        {
            Object obj = it.next();

            if (obj.toString().equals(object.toString()))
                return true;
        }

        return false;
    }

    /**
     * 
     * Returns the selected values in dialog.
     * 
     * @return A non null list of selected values. If no item is selected the list is empty
     */
    public java.util.List<Object> getSelectedValues()
    {
        return this.selectedValues;
    }
}
