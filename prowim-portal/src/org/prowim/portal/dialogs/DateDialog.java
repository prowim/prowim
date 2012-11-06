/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 14:14:29 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/DateDialog.java $
 * $LastChangedRevision: 4960 $
 *------------------------------------------------------------------------------
 * (c) 16.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */
package org.prowim.portal.dialogs;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.resource.ColorManager;
import org.prowim.rap.framework.validation.ErrorState;
import org.prowim.rap.framework.validation.Validator;



/**
 * This will decided between normal date and date and time(timestamp). If it should only shows <br/>
 * date, the user can than not change time. By timestamp user can time. <br/>
 * Constraint set the validation for dialog. Min means the former date, which is possible. <br/>
 * Max set the latest date, which is possible.
 * 
 * Required will here not evaluated. It will set always the selected date or timestamp. <br/>
 * 
 * NOTE: The flag for decision for setting date or timestamp will deliver by parameter values.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4960 $
 */
public final class DateDialog extends DefaultDialog
{

    /** Formatter of date output */
    private DateTimeFormatter pattern = null;

    // calendar, time
    private org.eclipse.swt.widgets.DateTime selecteDate, selectedTime;

    private final ErrorState                 verified;

    private final Validator                  validator;
    private int                              yearOut, monthOut, dayOut, hoursOut, minutesOut = 0;
    private Label                            dateLbl;                                            // This label shows the current selected date

    private Color                            backColor = ColorManager.BACKGROUND_COLOR;

    private String                           timeStamp = "";

    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     * @param dateOrTimeStamp Valid data are date or time stamp. Not null.
     * @param defaultConstraint Included the properties min, max and required
     */
    public DateDialog(final Shell parentShell, final Action action, String description, final String dateOrTimeStamp,
            final DefaultConstraint defaultConstraint)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DATE.getImage());

        Validate.notEmpty(dateOrTimeStamp);
        Validate.notNull(defaultConstraint, "default Constraint can not be null");

        this.timeStamp = dateOrTimeStamp;

        // Check validation to valid values
        verified = new ErrorState();
        validator = new Validator(defaultConstraint);
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
        control.setLayout(new GridLayout(2, false));
        selecteDate = new org.eclipse.swt.widgets.DateTime(control, SWT.CALENDAR | SWT.BORDER);

        selecteDate.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                dateLbl.setText(getDate().toString(pattern));

                // Set Validation
                verified.setErrorState(validator.checkDate(Long.toString(getDate().getMillis())));
                if (verified.hasError())
                    dateLbl.setBackground(ColorManager.REQUIRED_FIELD);
                else
                    dateLbl.setBackground(backColor);
            }
        });

        Composite container = new Composite(control, SWT.TOP | SWT.RIGHT);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label lblText = new Label(container, SWT.TOP | SWT.RIGHT);
        lblText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lblText.setText(Resources.Frames.Dialog.Texts.SELECTED_DATE.getText() + ": ");

        dateLbl = new Label(container, SWT.TOP);
        dateLbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        backColor = dateLbl.getBackground();

        Label lblTime = new Label(container, SWT.TOP | SWT.RIGHT);
        lblTime.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lblTime.setText(Resources.Frames.Dialog.Texts.SELECTED_TIME.getText() + ": ");

        selectedTime = new org.eclipse.swt.widgets.DateTime(container, SWT.TIME | SWT.BORDER);

        selectedTime.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                dateLbl.setText(getDate().toString(pattern));
            }
        });

        // Set time selection component enabled false, if only date is to show
        if (timeStamp.toString().equals(GlobalConstants.DATE))
        {
            selectedTime.setVisible(false);
            lblTime.setVisible(false);
            pattern = DateTimeFormat.forPattern(GlobalConstants.DATE_GR_PATTERN);
        }
        else
            pattern = DateTimeFormat.forPattern(GlobalConstants.DATE_TIME_GR_PATTERN);

        dateLbl.setText(getDate().toString(pattern));

        return control;
    }

    /**
     * 
     * Returns the selected date or date and time. Format will set by dateTime.
     * 
     * @return org.joda.time.DateTime.
     */
    private DateTime getDate()
    {
        DateTime dateTime = new DateTime(yearOut, monthOut + 1, dayOut, hoursOut, minutesOut, 0, 0);

        return dateTime;
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

        verified.setErrorState(validator.checkDate(Long.toString(getDate().getMillis())));
        if ( !verified.hasError())
        {
            setValuesForOutPut();
            close();
        }
    }

    /**
     * 
     * Set values, which are necessary to handle after dialog is closed.
     */
    private void setValuesForOutPut()
    {
        yearOut = selecteDate.getYear();
        monthOut = selecteDate.getMonth();
        dayOut = selecteDate.getDay();
        hoursOut = selectedTime.getHours();
        minutesOut = selectedTime.getMinutes();
    }

    /**
     * 
     * Returns selected time in string. Time stamp is created in {@link DateTime} format
     * 
     * @return Not null String. Time stamp is created in {@link DateTime} format
     */
    public String getTime()
    {
        return Long.toString(getDate().getMillis());
    }
}
