/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 27.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.datamodel.prowim.Process;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.feedback.ConfirmDialog;
import org.prowim.portal.dialogs.feedback.ErrorDialog;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.DocumentVersionHistoryTableViewer;
import org.prowim.portal.tables.labelprovider.ProcessVersionHistoryLabelProvider;
import org.prowim.portal.tables.sorter.ProcessVersionHistoryTableSorter;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.process.ModelEditorView;
import org.prowim.portal.view.process.support.HijackModelEditorView;
import org.prowim.portal.view.process.support.LockingEditorInfo;
import org.prowim.portal.view.process.support.ModelEditorLockingManager;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidatedTextField;
import org.prowim.rap.framework.resource.FontManager;



/**
 * Shows a dialog with all versions of a process model (template).
 * 
 * Provides actions to set the active version of the process models.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 2.0.0alpha9
 */
public class ProcessVersionsDialog extends DefaultDialog implements HijackModelEditorView
{
    /**
     * Description.
     */
    private static final long  LOCKED_EDITOR_CONFIRM_TIMEOUT = 30000L;

    /** Table to show versions of process */
    protected TableViewer      processVersionsTable;
    /** The {@link Version} which has been selected */
    protected Version          selectedVersion               = null;
    /** The {@link org.prowim.datamodel.prowim.Process} template model the versions are shown for */
    protected Process          process                       = null;

    /** Composite which comprised the GUI elements */
    protected Composite        contentCompositeOuter         = null;

    /** Holds the version history */
    private VersionHistory     versions                      = null;

    private ScrolledComposite  scrollComp;

    private Action             actionActivateVersion         = null;
    private Action             actionViewVersion             = null;
    private Action             actionRenameVersion           = null;

    // This button will be used to set a version active or clone the selected version from table.
    private Button             buttonMainAction              = null;
    private final List<Action> menuActions                   = new ArrayList<Action>();
    private ValidatedTextField textFieldVersionName;
    private String             versionName                   = null;
    private boolean            editableName                  = false;
    private String             starterPersonsString          = "";
    private final Button       buttonActivateNewestVersion   = null;

    /**
     * Creates a new process version dialog.
     * 
     * @param parentShell the calling {@link Shell}, can be null to create top level dialog
     * @param action the action to use for texts / images
     * @param description see {@link DefaultDialog}
     * @param process the {@link org.prowim.datamodel.prowim.Process} to show the versions for, may not be null
     * @param editableName if set to <code>true</code> the name of the new version can be entered
     */
    public ProcessVersionsDialog(Shell parentShell, Action action, String description, Process process, boolean editableName)
    {
        super(parentShell, action, description);
        Validate.notNull(process, "The given process is null.");
        this.process = process;
        this.process.setDescription(MainController.getInstance().getDescription(process.getTemplateID()));
        PersonArray starterPersons = MainController.getInstance().getProcessStartersForProcessID(process.getTemplateID());
        starterPersonsString = StringUtils.join(starterPersons.iterator(), ", ");
        this.editableName = editableName;

        init();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        if (editableName)
        {
            setReturnCode(OK);
            if (textFieldVersionName.isVerified())
            {
                versionName = textFieldVersionName.getText();
                if (isDuplicateVersionName(versionName))
                {
                    InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.TEXT_LENGTH_ERROR_DIALOG.getTooltip());
                }
                close();
            }
            else
            {
                InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.TEXT_LENGTH_ERROR_DIALOG.getTooltip());
            }
        }
        else
        {
            super.okPressed();
        }
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
     * Fill context menu with actions. This actions are the same action as in toolbar.
     * 
     * @param MenuManager Included all menu items in this view
     */
    private void fillContextMenu(IMenuManager mgr)
    {
        if (selectedVersion != null)
        {
            for (Action action : menuActions)
            {
                mgr.add(action);
            }
        }
    }

    /**
     * Creates the context menu which is filled later by {@link #fillContextMenu(IMenuManager)}.
     */
    protected void createContextMenu()
    {
        // Create menu manager.
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener()
        {
            public void menuAboutToShow(IMenuManager mgr)
            {
                if ( !MainController.getInstance().isEditableVersion(selectedVersion.getUserDefinedVersion()))
                {
                    fillContextMenu(mgr);
                }
            }
        });

        // Create menu.
        Menu menu = menuMgr.createContextMenu(processVersionsTable.getTable());
        processVersionsTable.getTable().setMenu(menu);
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

        contentCompositeOuter = new Composite(control, SWT.NONE);
        contentCompositeOuter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        contentCompositeOuter.setLayout(new GridLayout(1, false));

        // header **********************************************************************************

        final Group generalInformationGroup = new Group(contentCompositeOuter, SWT.SHADOW_IN | SWT.RIGHT);
        generalInformationGroup.setText(Resources.Frames.Global.Texts.GENERAL.getText());
        generalInformationGroup.setFont(FontManager.DIALOG_HEADER);
        generalInformationGroup.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        generalInformationGroup.setLayout(new GridLayout(1, false));

        // label for the process name
        Label labelName = new Label(generalInformationGroup, SWT.FILL);
        labelName.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false));
        labelName.setText(Resources.Frames.Global.Texts.NAME.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE2 + process.getName());

        Label labelUsername = new Label(generalInformationGroup, SWT.FILL);
        labelUsername.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false));
        labelUsername.setText(Resources.Frames.Global.Texts.CAN_BE_STARTED_FROM.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE2
                + starterPersonsString);

        Label labelDescription = new Label(generalInformationGroup, SWT.FILL);
        labelDescription.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false));
        String description = "";
        if (process.getDescription() != null)
        {
            description = process.getDescription();
        }
        labelDescription.setText(Resources.Frames.Global.Texts.DESCRIPTION.getText() + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE2
                + description);

        buttonMainAction = getButton();

        final Group versionTableGroup = new Group(contentCompositeOuter, SWT.SHADOW_IN | SWT.RIGHT);
        versionTableGroup.setFont(FontManager.DIALOG_HEADER);
        versionTableGroup.setText(Resources.Frames.Global.Texts.VERSION_HISTORY.getText());
        versionTableGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        versionTableGroup.setLayout(new GridLayout(1, false));
        scrollComp = new ScrolledComposite(versionTableGroup, SWT.H_SCROLL | SWT.V_SCROLL);
        scrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        scrollComp.setLayout(new GridLayout());
        scrollComp.setExpandHorizontal(true);
        scrollComp.setExpandVertical(true);
        createTable();
        processVersionsTable.refresh();

        createContextMenu();

        // if editable name, add name text field
        if (editableName)
        {
            Label labelEditName = new Label(contentCompositeOuter, SWT.LEFT);
            labelEditName.setLayoutData(new GridData(GridData.FILL, SWT.CENTER, true, false));
            labelEditName.setText(Resources.Frames.Global.Texts.VERSION_NAME.getText() + GlobalConstants.DOUBLE_POINT);

            textFieldVersionName = new ValidatedTextField(contentCompositeOuter, new DefaultConstraint(5l, 50l, true));
            textFieldVersionName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        }

        toggleActivateNewestVersionEnabled();
        return control;
    }

    /**
     * 
     * Description.
     * 
     * @param enable <code>true</code> if button should be enable, else <code>false</code>
     */
    protected void setActionButtonEnabled(Boolean enable)
    {

        buttonMainAction.setEnabled(enable);
    }

    /**
     * 
     * Get a button to set the newest Version. For a new dialog you can override this method and set a new button.
     * 
     * @return {@link Button}
     */
    protected Button getButton()
    {
        Button button = new Button(contentCompositeOuter, SWT.PUSH);
        button.setText("Neueste Version aktivieren");
        button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        button.addSelectionListener(new SelectionAdapter()
        {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                activateEditableVersion();
            }
        });

        return button;
    }

    private void activateEditableVersion()
    {
        boolean tabsOpen = checkTabsOpen();
        if (tabsOpen)
        {
            return;
        }
        // we do not need to ask if the version has to be made editable because it is already...
        boolean success = MainController.getInstance().activateVersion(process.getTemplateID(), AlgernonConstants.EDITABLE_USER_VERSION_LABEL, false);

        if (success)
        {
            InformationDialog.openInformation(null, Resources.Frames.Dialog.Texts.SUCCESS_ACTIVATE_VERSION.getText());
            close();
        }
    }

    /**
     * Creates the table which shows the process versions.
     */
    private void createTable()
    {
        processVersionsTable = new DocumentVersionHistoryTableViewer(scrollComp, SWT.SINGLE);

        processVersionsTable.setLabelProvider(new ProcessVersionHistoryLabelProvider());
        processVersionsTable.setSorter(new ProcessVersionHistoryTableSorter());

        processVersionsTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        processVersionsTable.getTable().setLinesVisible(true);

        processVersionsTable.setInput(versions.getVersions());
        processVersionsTable.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
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
            }
        });

        if (processVersionsTable.getTable().getItemCount() > 0)
        {
            processVersionsTable.getTable().select(0);
        }

        scrollComp.setContent(processVersionsTable.getControl());
    }

    private void createActionRenameVersion()
    {
        actionRenameVersion = Resources.Frames.Dialog.Actions.RENAME_VERSION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (selectedVersion != null)
                {
                    String oldName = selectedVersion.getUserDefinedVersion();

                    ShortTextDialog shortTextDialog = new ShortTextDialog(null, actionRenameVersion, "", selectedVersion.getUserDefinedVersion(),
                                                                          new DefaultConstraint(1l, 255l, true), true);

                    if (shortTextDialog.open() == IDialogConstants.OK_ID)
                    {
                        if ( !shortTextDialog.getText().equals(oldName))
                        {
                            MainController.getInstance().renameVersion(selectedVersion.getInstanceID(), shortTextDialog.getText());
                            versions = MainController.getInstance().getVersionHistoryFromProcessID(process.getTemplateID());
                            processVersionsTable.setInput(versions.getVersions());
                            processVersionsTable.refresh();
                        }
                    }
                }
            }
        });
    }

    private void createActionActivateVersion()
    {
        actionActivateVersion = Resources.Frames.Dialog.Actions.ACTIVATE_VERSION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (selectedVersion != null)
                {
                    if (MainController.getInstance().isEditableVersion(selectedVersion.getUserDefinedVersion()))
                    {
                        activateEditableVersion();
                    }
                    else
                    {
                        activateVersion(selectedVersion.getUserDefinedVersion());
                    }
                }
            }
        });
    }

    private void createActionViewVersion()
    {
        actionViewVersion = Resources.Frames.Dialog.Actions.VIEW_VERSION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (selectedVersion != null)
                {
                    ModelEditorView modelEditorView = null;
                    try
                    {
                        modelEditorView = (ModelEditorView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                                .showView(ModelEditorView.ID, selectedVersion.getInstanceID(), IWorkbenchPage.VIEW_ACTIVATE);
                        modelEditorView.setProcessId(selectedVersion.getInstanceID());
                    }
                    catch (PartInitException e)
                    {
                        throw new RuntimeException("Cannot open model editor view", e);

                    }
                    close();
                }
            }
        });
    }

    /**
     * 
     * Create actions for dialog
     */
    protected void createActions()
    {
        createActionActivateVersion();
        createActionViewVersion();
        createActionRenameVersion();
        menuActions.add(actionActivateVersion);
        menuActions.add(actionViewVersion);
        menuActions.add(actionRenameVersion);
    }

    /**
     * Returns true, if the given process ID is the process ID of a version.
     * 
     * @param processID the process ID to check, may not be null
     * @return true, if given process ID is the process ID of a version of this process
     */
    private boolean isProcessIDInVersions(String processID)
    {
        Process newestProcess = MainController.getInstance().getNewestProcessModelVersionID(process.getTemplateID());
        for (Version version : versions.getVersions())
        {
            if (version.getInstanceID().equals(processID) || newestProcess.getTemplateID().equals(processID))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * Description.
     * 
     * @return sd
     */
    protected boolean checkTabsOpen()
    {
        // check, if some model editors are open with process versions of this process
        for (IViewReference reference : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences())
        {
            if (reference.getId().equals(ModelEditorView.ID) && isProcessIDInVersions(reference.getSecondaryId()))
            {
                InformationDialog
                        .openInformation(null,
                                         "Eine Version dieses Prozesses ist noch geöffnet oder wird noch bearbeitet. Bitte schliessen Sie erst die entsprechenden Reiter.");
                return true;
            }
        }

        return false;
    }

    private void activateVersion(String versionName)
    {
        boolean tabsOpen = checkTabsOpen();
        if (tabsOpen)
        {
            return;
        }

        YesNoAbortDialog yesNoAbortDialog = new YesNoAbortDialog(null, "", Resources.Frames.Process.Texts.PROCESS_VERSION_ACTIVATE_QUESTION.getText());
        int makeEditable = yesNoAbortDialog.open();

        if (makeEditable != IDialogConstants.CANCEL_ID)
        {
            boolean reallyActivate = (makeEditable == IDialogConstants.OK_ID);

            if (reallyActivate)
            {
                String modelId = process.getTemplateID();
                String uuid = MainController.getInstance().getAlfrescoProcessModelUuid(modelId);

                // We do not really register
                synchronized (ModelEditorLockingManager.getInstance())
                {

                    if (ModelEditorView.isProcessLockedByEditor(modelId))
                    {
                        final LockingEditorInfo lockingEditorInfo = ModelEditorLockingManager.getInstance().getLockingEditorInfoForUUID(uuid);

                        if (ConfirmDialog.openConfirm(null, Resources.Frames.Process.Texts.PROCESS_LOCKED_VERSION_DIALOG_QUESTION.getText()))
                        {
                            if (lockingEditorInfo.getView().hijack(lockingEditorInfo))
                            {
                                ModelEditorLockingManager.getInstance().registerLockerForProcess(modelId, this);
                            }
                            else
                            {
                                Display.getCurrent().asyncExec(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        ErrorDialog.openError(null, Resources.Frames.Process.Texts.PROCESS_HIJACK_FAILED.getText());
                                    }
                                });
                                close();
                                return;
                            }
                        }
                        else
                        {
                            close();
                            return;
                        }
                    }
                }

                boolean success = MainController.getInstance().activateVersion(process.getTemplateID(), versionName, reallyActivate);

                synchronized (ModelEditorLockingManager.getInstance())
                {
                    ModelEditorLockingManager.getInstance().deregisterLockerForUUID(uuid, this);
                }

                if (success)
                {
                    InformationDialog.openInformation(null, Resources.Frames.Dialog.Texts.SUCCESS_ACTIVATE_VERSION.getText());
                    close();
                }
            }
        }
    }

    private void init()
    {
        versions = MainController.getInstance().getVersionHistoryFromProcessID(this.process.getTemplateID());
        createActions();
    }

    /**
     * Returns the name of the version text field.
     * 
     * @return the name of the version, can be null
     */
    public String getVersionName()
    {
        return versionName;
    }

    /**
     * Returns true, if the given version name is already contained in the versions of the process.
     * 
     * @param versionName the version name to check
     * @return true, if version name is already set in a version
     * @see Version#getUserDefinedVersion()
     */
    private boolean isDuplicateVersionName(String versionName)
    {
        for (Version version : versions.getVersions())
        {
            if (version.getUserDefinedVersion().equals(versionName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Toggles the enabled state of the button to activate the newest version.
     * 
     * If the newest version is already active, the button is disabled, otherwise enabled
     */
    protected void toggleActivateNewestVersionEnabled()
    {
        boolean enable = false;
        for (Version version : versions.getVersions())
        {
            if (version.getIsActiveVersion())
            {
                enable = true;
            }
        }

        buttonMainAction.setEnabled(enable);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.process.support.HijackModelEditorView#hijack(org.prowim.portal.view.process.support.LockingEditorInfo)
     */
    @Override
    public boolean hijack(LockingEditorInfo lockingEditorInfo)
    {
        // forbid other dialogs to hijack this one, since it locks the model only for a very short amount of time.
        return false;
    }

    /**
     * 
     * Description.
     * 
     * @param action action
     */
    public void addActionToMenu(Action action)
    {
        this.menuActions.add(action);
    }

}
