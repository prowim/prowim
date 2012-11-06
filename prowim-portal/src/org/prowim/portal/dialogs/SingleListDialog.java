/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-29 09:49:06 +0200 (Fr, 29 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/SingleListDialog.java $
 * $LastChangedRevision: 5010 $
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
 * To create this dialog call openDialog method. Input are a list of objects. Output is a object.
 * 
 * @see org.prowim.rap.framework.components.Constraint
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5010 $
 * @param <ObjectType> the Type of the possible objects
 */
public final class SingleListDialog<ObjectType> extends DefaultDialog
{

    private ListLimit               singleList;
    private final List<ObjectType>  listValues;
    private ObjectType              selectedValue;
    private final DefaultConstraint constraint;

    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description TODO
     * @param listValues List of objects to fill select list. Not null.
     * @param preSelectedValue This is one preselected values, which are preselected by modeling or by user.
     * @param defaultConstraint Included the properties min, max and required. Not null
     * 
     */
    public SingleListDialog(Shell parentShell, Action action, String description, final List<ObjectType> listValues,
            final ObjectType preSelectedValue, DefaultConstraint defaultConstraint)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage());

        Validate.notNull(listValues, "listValues can not be null");
        Validate.notNull(defaultConstraint, "Constraint can not be null");

        this.listValues = listValues;
        this.selectedValue = preSelectedValue;
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

        singleList = new ListLimit(control, SWT.SINGLE, constraint);
        singleList.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        List<ObjectType> items = this.listValues;

        Iterator<ObjectType> it = items.iterator();

        int idx = 0;
        while (it.hasNext())
        {
            ObjectType obj = it.next();

            singleList.add(obj.toString());
            singleList.setData(obj.toString(), obj);
            if (selectedValue != null)
            {
                if (obj.toString().equals(this.selectedValue.toString()))
                {
                    singleList.select(idx);
                }
            }
            idx++;
        }

        if ((constraint.isRequired()) && (singleList.getSelectionIndex() < 0))
            singleList.setSelection(0);

        singleList.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                selectedValue = (ObjectType) singleList.getData(singleList.getItem(singleList.getSelectionIndex()));
            }
        });

        // If list is not empty, select the first element.
        if (singleList.getItemCount() > 0)
        {
            singleList.setSelection(0);
            selectedValue = (ObjectType) singleList.getData(singleList.getItem(singleList.getSelectionIndex()));
        }

        return control;
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

        verified.setErrorState(validator.checkList(singleList.getSelectionCount()));
        if ( !verified.hasError())
        {
            close();
        }
    }

    /**
     * 
     * Returns the selected values in dialog.
     * 
     * @return A non null {@link Object}.
     */
    public Object getSelectedValue()
    {
        return this.selectedValue;
    }

}
