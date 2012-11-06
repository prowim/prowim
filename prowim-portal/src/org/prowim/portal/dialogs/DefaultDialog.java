/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-22 19:23:23 +0200 (Fr, 22 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/DefaultDialog.java $
 * $LastChangedRevision: 4985 $
 *------------------------------------------------------------------------------
 * (c) 10.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.rwt.lifecycle.UICallBack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.prowim.portal.i18n.Resources;
import org.prowim.rap.framework.resource.ColorManager;
import org.prowim.rap.framework.resource.FontManager;



/**
 * <p>
 * This a default dialog to construct other dialogs from this. This is extended from Dialog class.
 * </p>
 * 
 * <p>
 * It serves as a default dialog implementation, so that all extended dialogs will have the same look.
 * </p>
 * 
 * <p>
 * You can override the createCustomArea method, when you want to set other elements in the dialog.
 * </p>
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4985 $
 */
public abstract class DefaultDialog extends Dialog
{

    /**
     * Message (a localized string).
     */
    protected final String headerTitle;

    /**
     * Message label is the label the headerTitle is shown on.
     */
    protected Label        labelHeaderTitel;

    /**
     * Return the label for the image.
     */
    protected Label        imageLabel;

    /** The action which is only used for text and description etc. */
    protected Action       action      = null;

    /**
     * Title of dialog (a localized string).
     */
    private final String   dialogTitle;

    /**
     * Image of dialog.
     */
    private final Image    dialogImage;

    /**
     * Image for header area.
     */
    private Image          headerImage = Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage();

    private final String   headerDescription;

    private Label          labelHeaderDescription;

    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell composite, where this dialog will be shown. Can be null if no any relation to a widget is needed.
     * @param action action which is used for the dialogTitle, image and headerTitle (~action tooltip) text, may not be null. The headerTitle is shown bold. The image is shown with right alignment.
     * @param description describes what the dialog exactly does, may not be null. The description will be not bold shown under the headerTitle.
     */
    protected DefaultDialog(Shell parentShell, Action action, String description)
    {
        super(parentShell);

        Validate.notNull(action);
        Validate.notNull(description);

        this.action = action;
        this.dialogTitle = action.getText();
        this.dialogImage = action.getImageDescriptor().createImage();
        this.headerTitle = action.getToolTipText();
        this.headerDescription = description;
    }

    /**
     * Constructor of this dialog. It is better to use openDialog-Method to call this dialog.
     * 
     * @param parentShell composite, where this dialog will be shown. Can be null
     * @param dialogTitle dialogTitle of dialog
     * @param dialogTitleImage dialogTitle image of dialog
     * @param headerTitle description of dialog
     * @param heaerDescription description of dialog, may not be null
     */
    protected DefaultDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String headerTitle, String heaerDescription)
    {
        super(parentShell);
        int style = SWT.TITLE | SWT.BORDER | SWT.CLOSE;
        setShellStyle(style | SWT.APPLICATION_MODAL | SWT.RESIZE);

        Validate.notNull(dialogTitle);
        Validate.notNull(dialogTitleImage);
        Validate.notNull(headerTitle);
        Validate.notNull(heaerDescription);

        this.dialogImage = dialogTitleImage;
        this.dialogTitle = dialogTitle;
        this.headerTitle = headerTitle;
        this.headerDescription = heaerDescription;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        // Set the frame
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.marginLeft = 0;
        layout.marginTop = 0;
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;

        container.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        container.setLayoutData(data);

        // Set header of dialog
        createHeader(container);

        // Set separator between header and custom area
        Label seperateLbl = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
        seperateLbl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Set custom area
        createCustomArea(container);

        return container;
    }

    /**
     * Create header of dialog. This contains the dialog title, dialog description and header image
     * 
     * @param container Composite, where header is shown
     */
    private Control createHeader(Composite parent)
    {
        // Controller for header
        Composite container = new Composite(parent, SWT.TOP);
        container.setLayout(new GridLayout(2, false));
        container.setBackground(ColorManager.BACKGROUND_COLOR);
        container.setBackgroundMode(SWT.INHERIT_DEFAULT);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // container for description area of header ***********************************************************************************
        Composite descContainer = new Composite(container, SWT.TOP);
        descContainer.setLayout(new GridLayout(1, false));
        descContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // create headerTitle
        labelHeaderTitel = new Label(descContainer, SWT.WRAP);
        labelHeaderTitel.setText(this.headerTitle);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH), SWT.DEFAULT).applyTo(labelHeaderTitel);
        labelHeaderTitel.setFont(FontManager.DIALOG_HEADER);

        // create header description
        labelHeaderDescription = new Label(descContainer, SWT.WRAP);
        labelHeaderDescription.setText(this.headerDescription);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH), SWT.DEFAULT).applyTo(labelHeaderDescription);

        // Set image for header. This should added to main container *******************************************************************
        imageLabel = new Label(container, SWT.TOP);
        imageLabel.setImage(this.headerImage);
        GridDataFactory.fillDefaults().align(SWT.CENTER, SWT.BEGINNING).applyTo(imageLabel);

        return container;
    }

    /**
     * <p>
     * This method creates the custom area of this dialog. You have to override this method to implement and design your dialog.
     * </p>
     * 
     * <p>
     * Please note that you have to call the default implementation to get the {@link Control} of this dialog, so that the dialog has the same look:<br>
     * <br>
     * <code>Control control = super.createCustomArea(parent);</code>
     * </p>
     * 
     * @param parent Composite, where header is shown
     * @return Control, which included a frame, where you can set your own components
     */
    protected Composite createCustomArea(Composite parent)
    {
        // Controller for header
        GridLayout layout = new GridLayout();
        layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
        layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
        layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);

        Composite container = new Composite(parent, SWT.NONE | SWT.END);
        container.setLayout(layout);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        return container;
    }

    /**
     * 
     * Set description for dialog in header.
     * 
     * @param text not null string
     */
    public void setHeaderDescription(String text)
    {
        Validate.notNull(text, "text can not be null");
        labelHeaderDescription.setText(text);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        // create OK and Cancel buttons by default
        createButton(parent, IDialogConstants.OK_ID, Resources.Frames.Global.Texts.OK.getText(), true);
        createButton(parent, IDialogConstants.CANCEL_ID, Resources.Frames.Global.Texts.CANCEL.getText(), false);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
        newShell.setText(this.dialogTitle);
        newShell.setImage(this.dialogImage);
    }

    /**
     * 
     * This method set the minimum width of headerTitle area
     * 
     * @return int
     */
    protected int getMinimumMessageWidth()
    {
        return convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
    }

    /**
     * Get the image of header
     * 
     * @return the headerImage
     */
    public Image getHeaderImage()
    {
        return headerImage;
    }

    /**
     * Set the image of header
     * 
     * @param headerImage the headerImage to set
     */
    public void setHeaderImage(Image headerImage)
    {
        Validate.notNull(headerImage, "Imgae can not be null");
        this.headerImage = headerImage;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#isResizable()
     */
    @Override
    protected boolean isResizable()
    {
        return true;
    }

    /**
     * 
     * Opens the dialog with a default timeout. when the timeout is over, the {@link Shell#getDefaultButton()} is triggered
     * 
     * @param millis the timeout in ms
     * @param timeoutButtonId The id which shall be returned if the tiemout is reached
     * @return The id of the pressed/triggered button
     */
    public int open(final long millis, final int timeoutButtonId)
    {
        Validate.isTrue(millis > 0);

        final Display currentDisplay = Display.getCurrent();
        currentDisplay.asyncExec(new Runnable()
        {

            @Override
            public void run()
            {
                if ( !DefaultDialog.this.getShell().isDisposed())
                {
                    final Button defaultButton = getButton(timeoutButtonId);
                    Validate.notNull(defaultButton);

                    final String defaultButtonLabel = defaultButton.getText();
                    final long destinationTimestamp = System.currentTimeMillis() + millis;

                    final String callBackId = "DialogTimeoutCallback_" + System.nanoTime();
                    UICallBack.activate(callBackId);

                    final Timer buttonLabelUpdateTimer = new Timer();
                    buttonLabelUpdateTimer.schedule(new TimerTask()
                    {

                        @Override
                        public void run()
                        {
                            currentDisplay.asyncExec(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    long deltaMillis = destinationTimestamp - System.currentTimeMillis();
                                    if ( !defaultButton.isDisposed())
                                    {
                                        defaultButton.setText(defaultButtonLabel + " (" + deltaMillis / 1000 + ")");
                                    }
                                }
                            });
                        }
                    }, 0, 1000);

                    new Timer().schedule(new TimerTask()
                    {

                        @Override
                        public void run()
                        {
                            currentDisplay.asyncExec(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    buttonLabelUpdateTimer.cancel();
                                    if ( !defaultButton.isDisposed())
                                    {
                                        DefaultDialog.this.buttonPressed(timeoutButtonId);
                                    }

                                    UICallBack.deactivate(callBackId);

                                }
                            });
                        }
                    }, millis);
                }
            }
        });

        return super.open();
    }
}
