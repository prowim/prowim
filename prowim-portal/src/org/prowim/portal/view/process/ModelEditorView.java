/*==============================================================================
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 06.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.portal.view.process;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.prowim.portal.LoggedInUserInfo;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.YesNoAbortDialog;
import org.prowim.portal.dialogs.feedback.ErrorDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.update.UpdateNotificationCollection;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.browserfunctions.BFAddKnowledge;
import org.prowim.portal.view.browserfunctions.BFAddProcessElementToActivity;
import org.prowim.portal.view.browserfunctions.BFApproveProcess;
import org.prowim.portal.view.browserfunctions.BFBendControlFlow;
import org.prowim.portal.view.browserfunctions.BFCallPickElementDialog;
import org.prowim.portal.view.browserfunctions.BFConnectActivityMean;
import org.prowim.portal.view.browserfunctions.BFConnectActivityRole;
import org.prowim.portal.view.browserfunctions.BFCreateObject;
import org.prowim.portal.view.browserfunctions.BFDeleteElementFromModel;
import org.prowim.portal.view.browserfunctions.BFDeleteObject;
import org.prowim.portal.view.browserfunctions.BFDisapproveProcess;
import org.prowim.portal.view.browserfunctions.BFEditNameAndDescription;
import org.prowim.portal.view.browserfunctions.BFEditProcess;
import org.prowim.portal.view.browserfunctions.BFGetActivityMeans;
import org.prowim.portal.view.browserfunctions.BFGetActivtiyRoles;
import org.prowim.portal.view.browserfunctions.BFGetAllExistSubProcesses;
import org.prowim.portal.view.browserfunctions.BFGetCombinationRule;
import org.prowim.portal.view.browserfunctions.BFGetDescription;
import org.prowim.portal.view.browserfunctions.BFGetElementsOfProcess;
import org.prowim.portal.view.browserfunctions.BFGetKnowledgeObjects;
import org.prowim.portal.view.browserfunctions.BFGetModels;
import org.prowim.portal.view.browserfunctions.BFGetName;
import org.prowim.portal.view.browserfunctions.BFGetPossibleCombinationRules;
import org.prowim.portal.view.browserfunctions.BFGetPossibleRelations;
import org.prowim.portal.view.browserfunctions.BFGetProcessInfoOfObj;
import org.prowim.portal.view.browserfunctions.BFIsGlobal;
import org.prowim.portal.view.browserfunctions.BFIsProcessApproved;
import org.prowim.portal.view.browserfunctions.BFIsProcessLandscape;
import org.prowim.portal.view.browserfunctions.BFLog;
import org.prowim.portal.view.browserfunctions.BFOpenProductParameters;
import org.prowim.portal.view.browserfunctions.BFRemoveRelationValue;
import org.prowim.portal.view.browserfunctions.BFRename;
import org.prowim.portal.view.browserfunctions.BFSaveProcessAsNewVersion;
import org.prowim.portal.view.browserfunctions.BFSaveProcessModelAsXML;
import org.prowim.portal.view.browserfunctions.BFSelectProcessElement;
import org.prowim.portal.view.browserfunctions.BFSetDescription;
import org.prowim.portal.view.browserfunctions.BFSetProcessLandscapeFlag;
import org.prowim.portal.view.browserfunctions.BFSetProduct;
import org.prowim.portal.view.browserfunctions.BFSetRelationValue;
import org.prowim.portal.view.browserfunctions.BFSetSubProcessFlagForProcess;
import org.prowim.portal.view.browserfunctions.BFShowActivityRelationsDialog;
import org.prowim.portal.view.browserfunctions.BFShowAttributes;
import org.prowim.portal.view.browserfunctions.BFShowErrorDialog;
import org.prowim.portal.view.browserfunctions.BFShowModelURL;
import org.prowim.portal.view.browserfunctions.BFShowProcess;
import org.prowim.portal.view.browserfunctions.ModelEditorFunctionFactory;
import org.prowim.portal.view.menu.MenuBarView;
import org.prowim.portal.view.process.support.HijackModelEditorView;
import org.prowim.portal.view.process.support.LockingEditorInfo;
import org.prowim.portal.view.process.support.ModelEditorLockingManager;
import org.prowim.rap.framework.resource.FontManager;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.logging.Logger;


/**
 * Shows a url in a tab. You can create the view and than set the url with showLink method.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class ModelEditorView extends DefaultView implements HijackModelEditorView
{
    /** ID of view */
    public static final String         ID                         = ModelEditorView.class.getName();

    /**
     * Timeout in ms for the locked editor dialog.
     */
    private static final long          LOCKED_DIALOG_TIMEOUT      = 30000L;

    private static final Logger        LOG                        = Logger.getLogger(ModelEditorView.class);

    private SashForm                   mainSashForm;
    private boolean                    sashOut                    = false;
    private ModelEditor                modelEditor;
    private final Set<BrowserFunction> registeredBrowserFunctions = new HashSet<BrowserFunction>();

    private MenuBarView                menuBar                    = null;

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);

        // Remove toolbar from view
        super.toolbar.dispose();

        // Set title of View
        this.setPartName(Resources.Frames.Process.Actions.PROCESS_MODELLER_NAV.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Process.Actions.PROCESS_MODELLER_NAV.getImage());

    }

    /**
     * 
     * setName.
     * 
     * @param name the part name, as it should be displayed in tabs.
     */
    public void setName(String name)
    {
        this.setPartName(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite container)
    {
        mainSashForm = new SashForm(container, SWT.HORIZONTAL);
        mainSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        createLinkSide(mainSashForm);
        createRightSide(mainSashForm);

        mainSashForm.setWeights(new int[] { 10, 0 });
    }

    /**
     * Create the link side of view.
     * 
     * @param composite
     */
    private void createLinkSide(SashForm container)
    {
        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(2, false));

        modelEditor = new ModelEditor(composite);

        // Button open or close the menu bar in right site
        final Button btn = new Button(composite, SWT.FLAT | SWT.VERTICAL);
        btn.setLayoutData(new GridData(SWT.END, SWT.FILL, false, true));
        btn.setSize(1, SWT.FILL);
        btn.setFont(FontManager.FONT_VERDANA_10_BOLD);
        btn.setText("<");

        btn.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
            }

            @Override
            public void mouseDown(MouseEvent e)
            {
                if (sashOut)
                {
                    mainSashForm.setWeights(new int[] { 10, 0 });
                    sashOut = false;
                    btn.setText("<");
                }
                else
                {
                    /** Initialize the {@link MenuBarView} */
                    menuBar.initTrees(modelEditor.getModelId(), modelEditor.getModelName());

                    mainSashForm.setWeights(new int[] { 8, 2 });
                    sashOut = true;
                    btn.setText(">");
                }
            }

            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
            }
        });
    }

    /**
     * Reinitalize all required {@link BrowserFunction}s
     */
    private void reInitBrowserFunctions()
    {
        unregisterAllBrowserFunctions();
        registerAllBrowserFunctions();
    }

    /**
     * Register all {@link BrowserFunction}s and store them in {@link #registeredBrowserFunctions}
     */
    private void registerAllBrowserFunctions()
    {
        long currentTimeMillis = System.currentTimeMillis();
        this.registeredBrowserFunctions.clear();
        this.registeredBrowserFunctions.addAll(ModelEditorView.initCommonModelEditorBrowserFunctions(modelEditor));
        this.registeredBrowserFunctions.addAll(initEditorBrowserFunctions());

        LOG.info("'registerAllBrowserFunctions()'" + (System.currentTimeMillis() - currentTimeMillis) + " ms");
    }

    /**
     * Remove all {@link BrowserFunction}s stored in {@link #registeredBrowserFunctions}
     */
    private void unregisterAllBrowserFunctions()
    {
        for (BrowserFunction bf : this.registeredBrowserFunctions)
        {
            bf.dispose();
        }
        this.registeredBrowserFunctions.clear();
    }

    /**
     * Description.
     */
    private Collection<BrowserFunction> initEditorBrowserFunctions()
    {
        Set<BrowserFunction> browserFunctions = new HashSet<BrowserFunction>();

        // Open ProductParamDlg
        browserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFOpenProductParameters.class, modelEditor));

        // Show dialog to create or get roles, means and depots
        browserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSelectProcessElement.class, modelEditor));

        browserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowAttributes.class, modelEditor));

        // Edit name and description
        browserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFEditNameAndDescription.class, modelEditor));

        // Save the process model in the editor as a new version using the process version dialog
        browserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSaveProcessAsNewVersion.class, modelEditor));

        return browserFunctions;
    }

    /**
     * Creates the right side of the view.
     * 
     * @param SashForm Container
     */
    private void createRightSide(SashForm container)
    {
        menuBar = new MenuBarView();
        menuBar.createPartControl(container);
    }

    /**
     * Initializes {@link BrowserFunction}s for transaction between RAP and JS(Model editor).
     * 
     * @param modelEditor The {@link ModelEditor} to register the {@link BrowserFunction}s on
     * @return a {@link Set} containing all {@link BrowserFunction}s in this method. Never null.
     */
    public static Set<BrowserFunction> initCommonModelEditorBrowserFunctions(ModelEditor modelEditor)
    {
        Set<BrowserFunction> bfs = new HashSet<BrowserFunction>();

        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSaveProcessModelAsXML.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFCreateObject.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFDeleteObject.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetName.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFApproveProcess.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSaveProcessModelAsXML.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFDisapproveProcess.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFRename.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetDescription.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetDescription.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetModels.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFConnectActivityControlFlow.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetPossibleRelations.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetRelationValue.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFRemoveRelationValue.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFConnectActivityRole.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetProduct.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFControlFlowCount.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetPossibleCombinationRules.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetCombinationRule.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetPossibleActivationRules.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetActivationRule.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFConnectActivityMean.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFIsGlobal.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetCombinationRule.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFBendControlFlow.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFCallPickElementDialog.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetProcessInfoOfObj.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetAllExistSubProcesses.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetSubProcessFlagForProcess.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetActivityAsAuto.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetActivityAsManual.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFDeleteElementFromModel.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowProcess.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFAddKnowledge.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetElementsOfProcess.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSaveProcessAsNewVersion.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFLog.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetDirectClassOfInstance.class, modelEditor));
        // bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFIsActivityManual.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetKnowledgeObjects.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetActivtiyRoles.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetActivityMeans.class, modelEditor));
        // Calls a dialog, which show exists roles, means or depots. User can select one of this elements or create a new one.
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFAddProcessElementToActivity.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFEditProcess.class, modelEditor));

        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowActivityRelationsDialog.class, modelEditor));

        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowErrorDialog.class, modelEditor));

        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowModelURL.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFSetProcessLandscapeFlag.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFIsProcessLandscape.class, modelEditor));
        bfs.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFIsProcessApproved.class, modelEditor));

        return bfs;
    }

    /**
     * Show the given model in either viewer or editor-mode, depending on user rights
     * 
     * @param modelId the model id to use
     */
    public void setProcessId(String modelId)
    {
        modelEditor.setModelName(MainController.getInstance().getName(modelId));
        String alfrescoVersion = MainController.getInstance().getAlfrescoVersion(modelId);

        boolean openEditor = true;
        if (hasCurrentUserSaveRights(modelId) && MainController.getInstance().isEditableVersion(alfrescoVersion))
        {

            // Attention: Make sure this block is left as soon as possible!
            // That is, for example always use asyncExec for showing dialogs. If this is not possible, use setTimeout on the dialog
            synchronized (ModelEditorLockingManager.getInstance())
            {
                Validate.notNull(Display.getCurrent());

                final LockingEditorInfo lockingEditorInfo = ModelEditorLockingManager.getInstance().getLockingEditorInfoForProcess(modelId);

                if (isProcessLockedByEditor(modelId) && lockingEditorInfo.getView() != this)
                {
                    LOG.debug("ModelEditor was locked");

                    YesNoAbortDialog yesNoAbortDialog = new YesNoAbortDialog(null, "",
                                                                             Resources.Frames.Process.Texts.PROCESS_LOCKED_EDITOR_QUESTION.getText());
                    int doEditLockedModelAnswer = yesNoAbortDialog.open(LOCKED_DIALOG_TIMEOUT, IDialogConstants.OK_ID);

                    if (doEditLockedModelAnswer == IDialogConstants.NO_ID)
                    {
                        if ( !lockingEditorInfo.getView().hijack(lockingEditorInfo))
                        {
                            Display.getCurrent().asyncExec(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    ErrorDialog.openError(null, Resources.Frames.Process.Texts.PROCESS_HIJACK_FAILED.getText());
                                }
                            });
                            unregisterAllBrowserFunctions();
                            closeView(modelId);
                            return;
                        }
                    }
                    else if (doEditLockedModelAnswer == IDialogConstants.OK_ID)
                    {
                        openEditor = false;
                        LOG.debug("showing viewer instead of editor since editor is locked");
                    }
                    else
                    {
                        LOG.debug("aborting to show editor on user request since it is locked");
                        unregisterAllBrowserFunctions();
                        closeView(modelId);
                        return;
                    }
                }

                // The lock must be registered from within the Mutex
                if (openEditor)
                {
                    ModelEditorLockingManager.getInstance().registerLockerForProcess(modelId, this);
                }
            }
        }
        else
        {
            openEditor = false;
        }

        reInitBrowserFunctions();

        if (openEditor)
        {
            this.modelEditor.setEditorMode(modelId);
        }
        else
        {
            this.modelEditor.setViewerMode(modelId);
        }

        this.modelEditor.setModelXML(MainController.getInstance().loadProcessAsXML(modelId));
        this.setPartName(Resources.Frames.Process.Texts.PROCESS.getText() + " " + MainController.getInstance().getName(modelId) + " - "
                + getSuffix(modelId));
    }

    /**
     * Returns the suffix.
     * 
     * @param modelId the not null modelId.
     * @return the suffix.
     */
    private static String getSuffix(String modelId)
    {
        String alfrescoVersion = MainController.getInstance().getAlfrescoVersion(modelId);
        String suffix = Resources.Frames.Process.Texts.NEWEST_VERSION.getText();
        if ( !MainController.getInstance().isEditableVersion(alfrescoVersion))
        {
            suffix = MainController.getInstance().getUserDefinedVersion(modelId);
        }
        return suffix;
    }

    /**
     * Check if a process is currently edited.
     * 
     * When checking for locks, {@link ModelEditorLockingManager#getInstance()} should be used to synchronize access
     * 
     * @param modelId non null
     * @return if any version of the given process is locked by the {@link ModelEditorView}
     */
    public static boolean isProcessLockedByEditor(String modelId)
    {
        Validate.notNull(modelId);
        LockingEditorInfo lockingEditorInfo = null;
        synchronized (ModelEditorLockingManager.getInstance())
        {
            lockingEditorInfo = ModelEditorLockingManager.getInstance().getLockingEditorInfoForProcess(modelId);
        }
        return lockingEditorInfo != null && lockingEditorInfo.getDisplay() != null && lockingEditorInfo.getDisplay() != Display.getCurrent()
                && !lockingEditorInfo.getDisplay().isDisposed();

    }

    /**
     * Description.
     * 
     * @param modelId
     * @return
     */
    private boolean hasCurrentUserSaveRights(String modelId)
    {
        return MainController.getInstance().canPersonModifyEntity(modelId, LoggedInUserInfo.getInstance().getPerson().getID());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose()
    {
        final String modelId = this.modelEditor.getModelId();
        if (modelId != null && hasCurrentUserSaveRights(modelId))
        {
            synchronized (ModelEditorLockingManager.getInstance())
            {
                LockingEditorInfo lockingEditorInfo = ModelEditorLockingManager.getInstance().getLockingEditorInfoForProcess(modelId);
                boolean hasInstanceEditLock = lockingEditorInfo != null && lockingEditorInfo.getView() != null && lockingEditorInfo.getView() == this;
                if (hasInstanceEditLock)
                {
                    if (modelEditor.getSaveOnClose())
                    {
                        saveModel();
                    }

                    ModelEditorLockingManager.getInstance().deregisterLockerForProcess(modelId, this);
                }
            }
        }
        modelEditor.dispose();
        menuBar.dispose();
        super.dispose();
    }

    /**
     * Save the XML of the current model if possible. Checks if model is editable version
     */
    private void saveModel()
    {
        if (MainController.getInstance().isEditableVersion(MainController.getInstance().getAlfrescoVersion(this.modelEditor.getModelId())))
        {
            LOG.debug("Saving model");
            MainController.getInstance().saveProcessAsXML(this.modelEditor.getModelId(), this.modelEditor.getModelXML(), false, null);
        }
    }

    /**
     * getMenuBar
     * 
     * @return the menuBar may be null
     */
    public MenuBarView getMenuBar()
    {
        return menuBar;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.PROCESS, EntityType.ACTIVITY, EntityType.ROLE, EntityType.MEAN, EntityType.RESULTSMEMORY);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#handleUpdateEvents(UpdateNotificationCollection)
     */
    @Override
    public void handleUpdateEvents(UpdateNotificationCollection updates)
    {
        super.handleUpdateEvents(updates);

        String modelID = this.modelEditor.getModelId();

        if (updates.isContainingForeignEvents())
        {
            for (String actId : updates.getFirst().getEntityIds())
            {
                if (MainController.getInstance().getParentProcess(actId).contains(modelID))
                {
                    MessageDialog.openInformation(null, Resources.Frames.Global.Texts.UPDATE_DIALOG_TITLE.getText(),
                                                  Resources.Frames.Global.Texts.UPDATE_DIALOG_MESSAGE_MODEL1.getText()
                                                          + MainController.getInstance().getName(modelID)
                                                          + Resources.Frames.Global.Texts.UPDATE_DIALOG_MESSAGE_MODEL2.getText());
                }
            }
        }
    }

    /**
     * Displays attributes of process elements in attributes-table of {@link ModelEditorView} for given modelId.
     * 
     * @param modelId must not be null
     * @param elementID must not be null
     * @param newName must not be null
     */
    public static void changeTabTitle(String modelId, String elementID, String newName)
    {
        for (IViewReference view : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences())
        {
            if (view.getId().equals(ModelEditorView.ID) && modelId.equals(view.getSecondaryId()))
            {

                ModelEditorView modelEditorView = (ModelEditorView) view.getView(false);
                if (modelEditorView != null)
                {
                    modelEditorView.setPartName(newName + " - " + getSuffix(modelId));
                }
            }
        }
    }

    /**
     * Description.
     */
    private void closeView(final String modelId)
    {
        for (IViewReference reference : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences())
        {
            if (reference.getId().equals(ModelEditorView.ID) && modelId.equals(reference.getSecondaryId()))
            {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(reference);
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.process.support.HijackModelEditorView#hijack(org.prowim.portal.view.process.support.LockingEditorInfo)
     */
    @Override
    public boolean hijack(final LockingEditorInfo lockingEditorInfo)
    {
        Validate.notNull(lockingEditorInfo);
        Validate.isTrue(lockingEditorInfo.getView() == this);
        Validate.isTrue(lockingEditorInfo.getDisplay() != Display.getCurrent());

        LOG.debug("hijacking editor");

        ((ModelEditorView) lockingEditorInfo.getView()).modelEditor.setSaveOnClose(false);
        ((ModelEditorView) lockingEditorInfo.getView()).saveModel();

        // Show headerTitle on remote display
        lockingEditorInfo.getDisplay().asyncExec(new Runnable()
        {
            @Override
            public void run()
            {
                MessageDialog.openWarning(null, null, Resources.Frames.Process.Texts.PROCESS_HIJACK_MESSAGE.getText());
                closeView(((ModelEditorView) lockingEditorInfo.getView()).modelEditor.getModelId());
            }
        });
        return true;
    }
}
