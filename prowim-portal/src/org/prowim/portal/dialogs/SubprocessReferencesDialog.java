/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 09.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.prowim.Process;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.ProcessTableViewer;
import org.prowim.portal.view.process.ModelEditorView;
import org.prowim.rap.framework.components.table.DefaultTableViewer;
import org.prowim.rap.framework.resource.FontManager;



/**
 * A dialog shows existing documents in DMS and bind a selected document in a version to a selected link.
 * 
 * @author Saad Wardi
 * @since 2.0
 */
public class SubprocessReferencesDialog extends DefaultDialog
{
    private DefaultTableViewer existingProcessesTable = null;

    private ScrolledComposite  existingDocsScrollComp;
    private ScrolledComposite  versionHistoryScrollComp;
    /** The selected link. */
    private final String       subProcess;
    private Action             showModel;

    /**
     * Constructs this.
     * 
     * @param parentShell the parent shell.
     * @param action the action.
     * @param description see {@link DefaultDialog}
     * @param subprocess the knowledge link.
     */
    public SubprocessReferencesDialog(Shell parentShell, Action action, String description, String subprocess)
    {
        super(parentShell, action, description);
        this.subProcess = subprocess;
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
        Composite mainGroup = new Composite(control, SWT.NONE);
        mainGroup.setLayout(new GridLayout(1, true));
        mainGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        // header **********************************************************************************
        SashForm sashForm = new SashForm(mainGroup, SWT.VERTICAL | SWT.H_SCROLL | SWT.V_SCROLL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        final Group composite3 = new Group(sashForm, SWT.SHADOW_OUT | SWT.BEGINNING | SWT.H_SCROLL | SWT.V_SCROLL);
        composite3.setFont(FontManager.DIALOG_HEADER);
        composite3.setText(Resources.Frames.Dialog.Texts.EXISTING_PROCESSES.getText());
        composite3.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite3.setLayout(new GridLayout(1, false));

        existingDocsScrollComp = new ScrolledComposite(composite3, SWT.H_SCROLL | SWT.V_SCROLL);
        existingDocsScrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        existingDocsScrollComp.setLayout(new GridLayout());
        existingDocsScrollComp.setExpandHorizontal(true);
        existingDocsScrollComp.setExpandVertical(true);

        createProcessesTable();

        // the versionhistory table
        final Composite composite4 = new Composite(sashForm, SWT.SHADOW_OUT | SWT.BEGINNING | SWT.H_SCROLL | SWT.V_SCROLL);
        composite4.setFont(FontManager.DIALOG_HEADER);
        // composite4.setText("");
        composite4.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite4.setLayout(new GridLayout(1, false));
        versionHistoryScrollComp = new ScrolledComposite(composite4, SWT.H_SCROLL | SWT.V_SCROLL);
        versionHistoryScrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        versionHistoryScrollComp.setLayout(new GridLayout());
        versionHistoryScrollComp.setExpandHorizontal(true);
        versionHistoryScrollComp.setExpandVertical(true);

        sashForm.setWeights(new int[] { 9, 1 });
        createActionShowModel();
        return control;
    }

    /**
     * 
     * This method call the constructor of this dialog and get the results and gives these back to the main frame. It is better to call this
     * 
     * method for creating this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param subprocess the current selected knowledge link
     * @return A object, that can indeed more values. If no return value exist, than return null.
     */
    public static boolean openDialog(final Shell parentShell, final Action action, String subprocess)
    {
        Validate.notNull(action);
        int status = new SubprocessReferencesDialog(parentShell, action, "", subprocess).open();

        return (status == KnowledgeObjectDialog.OK || status == 13);
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
     * Creates and initialize the processes table.
     */
    private void createProcessesTable()
    {
        existingProcessesTable = createResultTable(existingDocsScrollComp);
        existingProcessesTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        existingProcessesTable.getTable().setLinesVisible(true);

        List<Process> processList = MainController.getInstance().getSubprocessReferences(subProcess);
        existingProcessesTable.setInput(processList);
        existingProcessesTable.getTable().addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {

            }

            @Override
            public void widgetSelected(SelectionEvent e)
            {

            }
        });
        existingProcessesTable.getTable().addMouseListener(new MouseListener()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
            }

            @Override
            public void mouseDown(MouseEvent e)
            {
            }

            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                showModel.runWithEvent(new Event());
            }
        });

        existingDocsScrollComp.setContent(existingProcessesTable.getControl());
    }

    /**
     * Shows the selected process model.
     */
    private void createActionShowModel()
    {
        this.showModel = Resources.Frames.Toolbar.Actions.SHOW_PROCESS_MODEL.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String processID = getSelectedProcessID();
                if (processID != null)
                {
                    Process newestVersionProcess = MainController.getInstance().getNewestProcessModelVersionID(processID);
                    showModelEditor(newestVersionProcess.getTemplateID());
                }
            }
        });
    }

    /**
     * Gets the ID of the selected process.
     * 
     * @return the ID of the selected process.
     */
    private String getSelectedProcessID()
    {
        TableItem selectedItem = existingProcessesTable.getTable().getItem(existingProcessesTable.getTable().getSelectionIndex());
        if (selectedItem != null)
        {
            Process process = (Process) selectedItem.getData();
            if (process != null && process.getTemplateID() != null)
            {
                return process.getTemplateID();
            }
        }
        return null;
    }

    /**
     * Shows the model with the given id in the model editor. uses the viewer-mode if the current user has insufficient rights.
     * 
     * @param modelId
     */
    private void showModelEditor(String modelId)
    {
        ModelEditorView modelEditorView = null;
        try
        {
            modelEditorView = (ModelEditorView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(ModelEditorView.ID, modelId, IWorkbenchPage.VIEW_ACTIVATE);
            modelEditorView.setProcessId(modelId);
        }
        catch (PartInitException e)
        {
            throw new RuntimeException("Cannot open model editor view", e);

        }
    }

    /**
     * 
     * create and fill result table with data
     * 
     * @param searchResult the not null searchresult composite,
     */
    private DefaultTableViewer createResultTable(final ScrolledComposite searchResult)
    {
        return new ProcessTableViewer(searchResult, SWT.H_SCROLL | SWT.V_SCROLL);
    }

}
