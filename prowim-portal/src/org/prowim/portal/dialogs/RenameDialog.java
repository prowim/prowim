/*==============================================================================
 * File $Id: RenameDialog.java 5002 2010-10-27 13:18:52Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-27 15:18:52 +0200 (Mi, 27 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/RenameDialog.java $
 * $LastChangedRevision: 5002 $
 *------------------------------------------------------------------------------
 * (c) 21.04.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidatedTextArea;



/**
 * Dialog to set name and description of a {@link ProcessElement}.
 * 
 * @author Saad Wardi, Maziar Khodaei
 * @version $Revision: 5002 $
 * @since 2.0
 */
public class RenameDialog extends DefaultDialog
{
    // Text field to set the name of a element
    private ValidatedTextArea nameText;

    // Text field to set the description of a element
    private Text              descriptionText;

    private String            description = "";
    private String            name        = "";

    /**
     * Constructor.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null.
     * @param action Action to call this dialog.
     * @param description see {@link DefaultDialog}
     * @param name the not null name of the edited element<Activity, Role, Process...>
     * @param elementDescription the not null description of the edited element<Activity, Role, Process...>
     * 
     */
    public RenameDialog(Shell parentShell, Action action, String description, String name, String elementDescription)
    {
        super(parentShell, action, description);
        Validate.notNull(name);
        Validate.notNull(elementDescription);
        this.name = name;
        this.description = elementDescription;
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
        final Composite control = super.createCustomArea(parent);

        createDenotArea(control);

        createDescArea(control);
        return control;
    }

    /**
     * create denotation area.
     */
    private void createDenotArea(Control control)
    {
        Group denotGroup = new Group((Composite) control, SWT.SHADOW_IN | SWT.RIGHT);
        denotGroup.setText(Resources.Frames.Global.Texts.NAME.getText());
        denotGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        denotGroup.setLayout(new GridLayout(1, false));

        nameText = new ValidatedTextArea(denotGroup, new DefaultConstraint(new Long(1), new Long(1000), true));
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.minimumHeight = 50;

        nameText.setLayoutData(gd);
        nameText.setText(name);

        nameText.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                name = nameText.getText();
            }
        });
    }

    /**
     * create description area.
     */
    private void createDescArea(Control control)
    {
        Group descGroup = new Group((Composite) control, SWT.SHADOW_IN | SWT.RIGHT);
        descGroup.setText(Resources.Frames.Global.Texts.DESCRIPTION.getText());
        descGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        descGroup.setLayout(new GridLayout());

        descriptionText = new Text(descGroup, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.minimumHeight = 50;

        descriptionText.setText(description);

        descriptionText.setLayoutData(gd);

        descriptionText.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                description = descriptionText.getText();
            }
        });
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
        if (getVerifingFileds())
        {
            close();
        }
        else
            InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.REQUIRED_ERROR_DIALOG.getTooltip());
    }

    /**
     * 
     * Checks the verifying of fields.
     * 
     * @return true, if all are OK, else false.
     */
    private boolean getVerifingFileds()
    {
        return nameText.isVerified();
    }

    /**
     * 
     * Description.
     * 
     * @return name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * Description.
     * 
     * @return description
     */
    public String getDescritption()
    {
        return this.description;
    }
}
