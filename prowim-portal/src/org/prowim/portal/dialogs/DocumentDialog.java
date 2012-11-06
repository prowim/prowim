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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.knowledge.KnowledgeLinkTemp;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.DocumentTableViewer;
import org.prowim.portal.tables.DocumentVersionHistoryTableViewer;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.utils.GlobalFunctions;
import org.prowim.portal.view.knowledge.open.DownloadFile;
import org.prowim.rap.framework.components.table.DefaultTableViewer;
import org.prowim.rap.framework.resource.FontManager;



/**
 * A dialog shows existing documents in DMS and bind a selected document in a version to a selected link.
 * 
 * @author Saad Wardi
 * @since 2.0
 */
public class DocumentDialog extends DefaultDialog
{
    private DefaultTableViewer          existingDocumentsTable = null;
    private DefaultTableViewer          versionHistoryTable    = null;
    private ScrolledComposite           existingDocsScrollComp;
    private ScrolledComposite           versionHistoryScrollComp;
    /** The selected link. */
    private final KnowledgeLinkTemp     knowledgeLinkTemp;
    private TableItem                   selectedDocumentItem;
    private TableItem                   selectedDocumentVersion;
    /** Binds a document in version to a selected link. */
    private Button                      buttonBindDocument;
    /** Shows all documents stored in DMS. */
    private Button                      buttonOpenDocument;
    private final KnowledgeObjectDialog callingDialog;

    /**
     * Constructs this.
     * 
     * @param parentShell the parent shell.
     * @param action the action to use for texts, may not be null
     * @param description see {@link DefaultDialog}
     * @param knowledgeLinkTemp the knowledge link, may not be null
     * @param callingDialog TODO
     */
    public DocumentDialog(Shell parentShell, Action action, String description, KnowledgeLinkTemp knowledgeLinkTemp,
            KnowledgeObjectDialog callingDialog)
    {
        super(parentShell, action, description);

        Validate.notNull(knowledgeLinkTemp, "The knowledgeLink given is null!");
        Validate.notNull(callingDialog, "the calling dialog must not be null!");
        this.knowledgeLinkTemp = knowledgeLinkTemp;
        this.callingDialog = callingDialog;
    }

    /**
     * <p>
     * Returns the {@link DocumentContentProperties} of the selected Document.
     * </p>
     * 
     * <p>
     * Returns null, if no document was selected.
     * </p>
     * 
     * @return {@link DocumentContentProperties} or null, if nothing was selected
     */
    public DocumentContentProperties getDocumentContentProperties()
    {
        if (selectedDocumentItem != null)
        {
            return (DocumentContentProperties) selectedDocumentItem.getData();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        buttonOpenDocument = createButton(parent, 12, Resources.Frames.Dialog.Texts.OPEN.getText(), true);
        buttonOpenDocument.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                selectedDocumentItem = existingDocumentsTable.getTable().getItem(existingDocumentsTable.getTable().getSelectionIndex());
                if (selectedDocumentItem != null)
                {
                    DocumentContentProperties props = (DocumentContentProperties) selectedDocumentItem.getData();
                    Version selectedVersion = getDocumentVersion();
                    if (selectedVersion != null)
                    {
                        getDocument(props.getID(), selectedVersion.getLabel());
                    }
                }
            }
        });
        buttonOpenDocument.setEnabled(true);
        buttonBindDocument = createButton(parent, 13, Resources.Frames.Dialog.Texts.ASSIGN.getText(), true);
        buttonBindDocument.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (getDocumentContentProperties() != null)
                {
                    if (getDocumentVersion() != null)
                    {
                        callingDialog.setDocumentContentProperties(getDocumentContentProperties());
                        callingDialog.setDocumentVersion(getDocumentVersion());
                        okPressed();
                    }
                }
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
                setReturnCode(13);
                close();
            }
        });
        setEnableButtons(false);
        createButton(parent, IDialogConstants.CANCEL_ID, Resources.Frames.Global.Texts.CANCEL.getText(), true);
    }

    /**
     * Returns the {@link Version} currently selected.
     * 
     * @return the {@link Version} or null, if nothing was selected
     */
    public Version getDocumentVersion()
    {
        if (versionHistoryTable.getTable().getSelectionIndex() != -1)
        {
            TableItem tableItem = versionHistoryTable.getTable().getItem(versionHistoryTable.getTable().getSelectionIndex());
            return (Version) tableItem.getData();
        }

        return null;
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
        composite3.setText(Resources.Frames.Dialog.Texts.EXISTING_DOCUMENT.getText());
        composite3.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite3.setLayout(new GridLayout(1, false));

        existingDocsScrollComp = new ScrolledComposite(composite3, SWT.H_SCROLL | SWT.V_SCROLL);
        existingDocsScrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        existingDocsScrollComp.setLayout(new GridLayout());
        existingDocsScrollComp.setExpandHorizontal(true);
        existingDocsScrollComp.setExpandVertical(true);

        createNorthTable();

        // the version history table
        final Group composite4 = new Group(sashForm, SWT.SHADOW_OUT | SWT.BEGINNING | SWT.H_SCROLL | SWT.V_SCROLL);
        composite4.setFont(FontManager.DIALOG_HEADER);
        composite4.setText(Resources.Frames.Global.Texts.VERSION_HISTORY.getText());
        composite4.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite4.setLayout(new GridLayout(1, false));
        versionHistoryScrollComp = new ScrolledComposite(composite4, SWT.H_SCROLL | SWT.V_SCROLL);
        versionHistoryScrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        versionHistoryScrollComp.setLayout(new GridLayout());
        versionHistoryScrollComp.setExpandHorizontal(true);
        versionHistoryScrollComp.setExpandVertical(true);

        createSouthTable();

        sashForm.setWeights(new int[] { 7, 3 });
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
     * Creates and initialize the north (all documents) table.
     */
    private void createNorthTable()
    {
        existingDocumentsTable = createResultTable(existingDocsScrollComp);
        existingDocumentsTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        existingDocumentsTable.getTable().setLinesVisible(true);
        List<DocumentContentProperties> documentProperties = MainController.getInstance().getAllDocuments();
        List<DocumentContentProperties> filteredProperties = new ArrayList<DocumentContentProperties>();
        for (DocumentContentProperties documentContentProperties : documentProperties)
        {
            if ( !(documentContentProperties.getContentName().startsWith("prowimcore_Instance_") && documentContentProperties.getContentName()
                    .endsWith(".xml")))
            {
                filteredProperties.add(documentContentProperties);
            }
        }

        existingDocumentsTable.setInput(filteredProperties);
        existingDocumentsTable.getTable().addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
                if (existingDocumentsTable.getTable().getItems() != null && existingDocumentsTable.getTable().getItems().length > 0)
                {
                    existingDocumentsTable.getTable().select(0);
                    selectedDocumentItem = existingDocumentsTable.getTable().getItem(0);
                }
                else
                {
                    setEnableButtons(false);
                }
            }

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                selectedDocumentItem = existingDocumentsTable.getTable().getItem(existingDocumentsTable.getTable().getSelectionIndex());
                if (selectedDocumentItem != null)
                {
                    createSouthTable();
                    selectTheLastVersion();
                    setEnableButtons(true);
                }
                else
                {
                    setEnableButtons(false);
                }
            }
        });
        existingDocumentsTable.getTable().addMouseListener(new MouseListener()
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
                selectedDocumentItem = existingDocumentsTable.getTable().getItem(existingDocumentsTable.getTable().getSelectionIndex());
                if (selectedDocumentItem != null)
                {
                    DocumentContentProperties props = (DocumentContentProperties) selectedDocumentItem.getData();
                    getDocument(props.getID(), DMSConstants.INITIAL_VERSION_LABEL);
                    createSouthTable();
                }
            }
        });

        if (checkLink())
        {
            String assignedID = MainController.getInstance().getLinkDocumentID(knowledgeLinkTemp.getID());
            if (assignedID != null)
            {
                TableItem[] items = existingDocumentsTable.getTable().getItems();
                for (int i = 0; i < items.length; i++)
                {
                    DocumentContentProperties props = (DocumentContentProperties) items[i].getData();
                    if (props.getID().equals(assignedID))
                    {
                        existingDocumentsTable.getTable().select(i);
                        selectedDocumentItem = existingDocumentsTable.getTable().getItem(i);

                        existingDocumentsTable.scrollDown(selectedDocumentItem.getBounds().x, selectedDocumentItem.getBounds().y);
                        break;
                    }
                }
            }
        }
        existingDocsScrollComp.setContent(existingDocumentsTable.getControl());
    }

    /**
     * Enables the button.
     * 
     * @param flag true or false.@see Button#setEnabled(boolean).
     */
    private void setEnableButtons(boolean flag)
    {
        this.buttonBindDocument.setEnabled(flag);
        this.buttonOpenDocument.setEnabled(flag);
    }

    /**
     * Creates and initializes the south (version history) table.
     */
    private void createSouthTable()
    {
        versionHistoryTable = createSouthTable(versionHistoryScrollComp);
        versionHistoryTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
        versionHistoryTable.getTable().setLinesVisible(true);
        if (selectedDocumentItem != null)
        {
            DocumentContentProperties props = (DocumentContentProperties) selectedDocumentItem.getData();
            VersionHistory history = MainController.getInstance().getVersionHistoryFromNodeID(props.getID());
            Version currentVersion = new Version("", "", AlgernonConstants.EDITABLE_USER_VERSION_LABEL);
            history.add(currentVersion);
            versionHistoryTable.setInput(history.getVersions());
            versionHistoryTable.refresh();
        }
        else
        {
            versionHistoryTable.setInput(initList());
        }

        versionHistoryTable.getTable().addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
                setEnableButtons(true);
            }

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setEnableButtons(true);
            }
        });

        versionHistoryTable.getTable().addMouseListener(new org.eclipse.swt.events.MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                if (selectedDocumentItem != null)
                {
                    DocumentContentProperties props = (DocumentContentProperties) selectedDocumentItem.getData();
                    selectedDocumentVersion = versionHistoryTable.getTable().getItem(versionHistoryTable.getTable().getSelectionIndex());
                    Version currentversion = (Version) selectedDocumentVersion.getData();
                    getDocument(props.getID(), currentversion.getLabel());
                }
            }
        });

        if (checkLink())
        {
            setVersionSelection();
        }

        versionHistoryScrollComp.setContent(versionHistoryTable.getControl());
    }

    /**
     * Checks if the knowledge link is already created and saved in the ontology and the attributes are set correctly.
     * 
     * @return true if the link is created and saved, false otherwise (newly created links)
     */
    private boolean checkLink()
    {
        boolean ret1 = knowledgeLinkTemp != null && knowledgeLinkTemp.getID() != null && !knowledgeLinkTemp.getID().equals("");

        return (ret1 && knowledgeLinkTemp.getRepositoryName().equals(GlobalConstants.PROWIM_DMS) && getDocumentContentProperties() != null);
    }

    /**
     * Selects to the link assigned document version if exists.
     * 
     * @return true if the link has a document version assigned.
     */
    private boolean setVersionSelection()
    {
        boolean result = false;

        if (knowledgeLinkTemp != null && knowledgeLinkTemp.getID() != null && !knowledgeLinkTemp.getID().equals(""))
        {
            DocumentContentProperties props = (DocumentContentProperties) selectedDocumentItem.getData();
            VersionHistory history = MainController.getInstance().getVersionHistoryFromNodeID(props.getID());
            versionHistoryTable.setInput(history.getVersions());
            String versionLabel = MainController.getInstance().getDocumentVersionLabel(knowledgeLinkTemp.getID());
            if (versionLabel != null)
            {
                TableItem[] versions = versionHistoryTable.getTable().getItems();
                if (versions != null)
                    for (int i = 0; i < versions.length; i++)
                    {
                        Version currentversion = (Version) versions[i].getData();
                        if (props.getID().equals(MainController.getInstance().getLinkDocumentID(knowledgeLinkTemp.getID()))
                                && currentversion.getLabel().equals(versionLabel))
                        {
                            versionHistoryTable.getTable().select(i);
                            result = true;
                            break;
                        }
                    }
            }
        }
        return result;
    }

    /**
     * Selects the last version for a document.
     */
    private void selectTheLastVersion()
    {
        if ( !setVersionSelection())
        {
            TableItem[] versions = versionHistoryTable.getTable().getItems();
            if (versions != null && versions.length > 0)
                versionHistoryTable.getTable().select(0);
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
        return new DocumentTableViewer(searchResult, SWT.H_SCROLL | SWT.V_SCROLL);
    }

    /**
     * 
     * create and fill result table with data
     * 
     * @param searchResult the not null searchresult composite,
     */
    private DefaultTableViewer createSouthTable(final ScrolledComposite searchResult)
    {
        return new DocumentVersionHistoryTableViewer(searchResult, SWT.SINGLE);
    }

    /**
     * Inits a list with some dummy elements to get the View better.
     * 
     * @return not null {@link List}. If no item exists, an empty list is returned.
     */
    private List<Version> initList()
    {
        List<Version> defaultList = new ArrayList<Version>();
        defaultList.add(new Version(" ", " ", " "));
        defaultList.add(new Version(" ", " ", " "));
        defaultList.add(new Version(" ", " ", " "));
        defaultList.add(new Version(" ", " ", " "));
        return defaultList;
    }

    /**
     * Downloads a document in a version from the dms.
     * 
     * @param documentID not null document ID.
     * @param versionLabel not null version label.
     */
    private void getDocument(String documentID, String versionLabel)
    {
        Validate.notNull(documentID);
        Validate.notNull(versionLabel);
        // Get Document from Data base
        Document document = MainController.getInstance().downloadDocumentFromID(documentID, versionLabel);
        // This part is deal with the suffixes in older file, which are stored in DMS. Check if a file has this suffix and cut it to show this.
        Document doc = GlobalFunctions.renameDocument(document);
        DownloadFile.download(doc.getContent(), doc.getName());
    }
}
