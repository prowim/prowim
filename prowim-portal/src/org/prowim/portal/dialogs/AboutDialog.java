/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 14:14:29 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/AboutDialog.java $
 * $LastChangedRevision: 4960 $
 *------------------------------------------------------------------------------
 * (c) 23.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.resource.FontManager;



/**
 * Display the ProWim System Versions.
 * 
 * @author Saad Wardi
 * @version $Revision: 4960 $
 * @since 2.0
 */
public class AboutDialog extends DefaultDialog
{

    /**
     * Constructor of this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog, not null.
     * @param description see {@link DefaultDialog}
     */
    public AboutDialog(Shell parentShell, Action action, String description)
    {
        super(parentShell, action, Resources.Frames.Dialog.Texts.ABOUT_DESCRIPTION.getText());
        setHeaderImage(Resources.Frames.Header.Images.IMAGE_PROWIM_LOGO.getImage());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        createButton(parent, IDialogConstants.OK_ID, Resources.Frames.Global.Texts.OK.getText(), true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Composite createCustomArea(Composite parent)
    {
        Composite control = super.createCustomArea(parent);

        Composite composite = new Composite(control, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(1, false));

        // header **********************************************************************************
        Label nameLbl = new Label(composite, SWT.LEFT);
        nameLbl.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        nameLbl.setText(Resources.Frames.Global.Texts.PROWIM.getText() + " " + Resources.Frames.Global.Texts.VERSION.getText()
                + GlobalConstants.DOUBLE_POINT + " " + Platform.getBundle(GlobalConstants.PORTAL_BUNDLE_NAME).getVersion().toString());
        nameLbl.setFont(FontManager.DIALOG_HEADER);

        final Group composite3 = new Group(control, SWT.SHADOW_IN | SWT.RIGHT);
        composite3.setFont(FontManager.DIALOG_HEADER);
        composite3.setText(Resources.Frames.Global.Texts.RESPONSABLE.getText());
        composite3.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite3.setLayout(new GridLayout(1, false));

        Label nameLbl6 = new Label(composite3, SWT.LEFT);
        nameLbl6.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        nameLbl6.setText(Resources.Frames.Global.Texts.CONTACTS_PERSON_NAME.getText());

        Label nameLbl7 = new Label(composite3, SWT.LEFT);
        nameLbl7.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        nameLbl7.setText(Resources.Frames.Global.Texts.TEL_LABEL.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE2
                + Resources.Frames.Global.Texts.CONTACTS_PERSON_TEL.getText());

        Label faxLbl = new Label(composite3, SWT.LEFT);
        faxLbl.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        faxLbl.setText(Resources.Frames.Global.Texts.FAX_LABEL.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE2
                + Resources.Frames.Global.Texts.CONTACTS_PERSON_FAX.getText());

        Label webLbl = new Label(composite3, SWT.LEFT);
        webLbl.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        webLbl.setText(Resources.Frames.Global.Texts.WEB_LABEL.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE2
                + Resources.Frames.Global.Texts.EBCOT_WEB.getText());

        Label mailLbl = new Label(composite3, SWT.LEFT);
        mailLbl.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        mailLbl.setText(Resources.Frames.Global.Texts.MAIL_LABEL.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE2
                + Resources.Frames.Global.Texts.CONTACTS_PERSON_MAIL.getText());

        setBlankBtn(composite3);

        Label copyRightsLbl = new Label(composite3, SWT.LEFT);
        copyRightsLbl.setLayoutData(new GridData(GridData.BEGINNING, SWT.LEFT, false, false));
        copyRightsLbl.setText(Resources.Frames.Global.Texts.COPY_RIGHT.getText() + " " + Resources.Frames.Global.Texts.EBCOT.getText());

        Label rightsLbl = new Label(composite3, SWT.LEFT);
        rightsLbl.setLayoutData(new GridData(GridData.BEGINNING, SWT.LEFT, false, false));
        rightsLbl.setText(Resources.Frames.Global.Texts.RIGHT_RESERVED_TEXT.getText());

        return control;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        setReturnCode(OK);
        close();
    }

    /**
     * 
     * set a blank button to design the with tree widgets in a line
     * 
     * @param composite Composite
     */
    private void setBlankBtn(Composite composite)
    {
        Button blankBtn = new Button(composite, SWT.PUSH);
        blankBtn.setVisible(false);
    }
}
