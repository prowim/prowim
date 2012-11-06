/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 28.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.prowim.Process;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.feedback.ConfirmDialog;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;



/**
 * Shows a dialog with all versions of given process model. User can selects a process version and clone this.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public class CloneProcessDialog extends ProcessVersionsDialog
{
    private Process process            = null;
    private boolean successfulClone    = false;
    private Action  actionCloneProcess = null;

    /**
     * Shows a dialog with all versions of given process model
     * 
     * @param parentShell the calling {@link Shell}, can be null to create top level dialog
     * @param action the action to use for texts / images
     * @param process the {@link org.prowim.datamodel.prowim.Process} to show the versions for, may not be null
     * @param description see {@link DefaultDialog}
     */
    public CloneProcessDialog(Shell parentShell, Action action, Process process, String description)
    {
        super(parentShell, action, description, process, false);
        this.process = process;
    }

    @Override
    protected Button getButton()
    {
        // create action to copy
        createActionActivateVersion();
        addActionToMenu(actionCloneProcess);

        // create button
        Button button = new Button(contentCompositeOuter, SWT.PUSH);
        button.setText(Resources.Frames.Dialog.Texts.CLONE_SELECTED_PROCESS_VERSION.getText());
        button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        button.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                actionCloneProcess.runWithEvent(new Event());
            }
        });

        return button;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.ProcessVersionsDialog#setActionButtonEnabled(java.lang.Boolean)
     */
    @Override
    protected void setActionButtonEnabled(Boolean flag)
    {
        super.setActionButtonEnabled(true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.ProcessVersionsDialog#toggleActivateNewestVersionEnabled()
     */
    @Override
    protected void toggleActivateNewestVersionEnabled()
    {
        setActionButtonEnabled(true);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.ProcessVersionsDialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.ProcessVersionsDialog#createActions()
     */
    @Override
    protected void createActions()
    {

    }

    /**
     * 
     * getReturnValue.
     * 
     * @return <code>true</code> if cloning was successful, else <code>false</code>
     */
    public boolean getReturnValue()
    {
        return this.successfulClone;
    }

    private void createActionActivateVersion()
    {
        actionCloneProcess = Resources.Frames.Process.Actions.COPY_PROCESS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IStructuredSelection selection = (IStructuredSelection) processVersionsTable.getSelection();
                if ( !selection.isEmpty())
                {
                    selectedVersion = (Version) selection.getFirstElement();
                }
                else
                {
                    selectedVersion = null;
                }

                // Get a action to open a dialog to edit the name and description of new dialog.
                Action action = Resources.Frames.Process.Actions.SET_PROCESS_NAME.getAction();

                Process actProcess = process;
                String name = process.getName();
                String description = process.getDescription();

                if (selectedVersion != null)
                {
                    actProcess = MainController.getInstance().getProcessForVersion(selectedVersion.getInstanceID(),
                                                                                   selectedVersion.getUserDefinedVersion());

                    if (actProcess != null)
                    {
                        name = actProcess.getName();
                        description = actProcess.getDescription();

                    }
                }

                RenameDialog renameDialog = new RenameDialog(null, action, "", name + " - " + Resources.Frames.Global.Texts.COPY.getText(),
                                                             description);

                if (renameDialog.open() == IDialogConstants.OK_ID)
                {
                    name = renameDialog.getName();
                    description = renameDialog.getDescritption();

                    if (ConfirmDialog.openConfirm(null, Resources.Frames.Dialog.Texts.CLONE_MODEL_PLEASE_WAIT.getText()))
                    {
                        if (MainController.getInstance().cloneProcess(actProcess.getTemplateID(), name, description))
                        {
                            InformationDialog.openInformation(null, Resources.Frames.Dialog.Texts.PROCESS_CLONED.getText());
                            successfulClone = true;
                            close();
                        }
                    }
                }
            }
        });

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.ProcessVersionsDialog#createContextMenu()
     */
    @Override
    protected void createContextMenu()
    {
        super.createContextMenu();
    }

}
