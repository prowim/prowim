/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.02.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.document;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.DocumentTableViewer;
import org.prowim.portal.tables.DocumentVersionHistoryTableViewer;
import org.prowim.portal.tables.filter.DocumentFilter;
import org.prowim.portal.utils.GlobalFunctions;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.knowledge.open.DownloadFile;



/**
 * Shows all documents in Data Management System. User can filter the list at name of document. By selecting a document the different versions of document <br>
 * will show in the bottom table.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.1.0
 */
public class DocumentView extends DefaultView
{
    /** ID of view */
    public static final String                ID            = DocumentView.class.getName();

    /** table component to show the search results */
    private DocumentTableViewer               documentTable = null;
    private DocumentVersionHistoryTableViewer versionTable  = null;
    private Text                              filterTxt;
    private DocumentFilter                    filter;
    private Action                            actionShowDocument, actionReloadList;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);

        // Set title of View
        this.setPartName(Resources.Frames.Navigation.Actions.EXISTING_DOCUMENTS_NAV.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Navigation.Actions.EXISTING_DOCUMENTS_NAV.getImage());
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createToolbar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createToolbar(Composite parent)
    {
        super.createToolbar(parent);
        toolbar.addToolBarItem(this.actionReloadList);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite parent)
    {
        sashForm = new SashForm(parent, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite searchComp = new Composite(sashForm, SWT.NONE);
        searchComp.setLayout(new GridLayout(1, false));
        searchComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite versionComp = new Composite(sashForm, SWT.NONE);
        versionComp.setLayout(new GridLayout(1, false));
        versionComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        createViewArea(searchComp);
        createVersionArea(versionComp);
        sashForm.setWeights(new int[] { 80, 20 });
    }

    /**
     * This is the search part and the result table.
     * 
     * @param container Group, which elements included.
     */
    private void createViewArea(Composite container)
    {
        createFilterArea(container);
        createDocumentViewArea(container);
    }

    /**
     * Shows different versions of selected {@link Document}.
     * 
     * @param versionComp
     */
    private void createVersionArea(Composite versionComp)
    {
        Group resultGroup = new Group(versionComp, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        resultGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        resultGroup.setLayout(new GridLayout());
        resultGroup.setText(Resources.Frames.Global.Texts.VERSION_HISTORY.getText());

        // Set scroll composite to set view scrollable
        ScrolledComposite versionComposite = new ScrolledComposite(resultGroup, SWT.NONE);
        versionComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        versionComposite.setLayout(new GridLayout());
        versionComposite.setExpandHorizontal(true);
        versionComposite.setExpandVertical(true);
        versionTable = createVersionTable(versionComposite);
        versionComposite.setContent(versionTable.getControl());
    }

    /**
     * Create table to show the different versions of a document.
     * 
     * @param versionComposite
     * @return
     */
    private DocumentVersionHistoryTableViewer createVersionTable(ScrolledComposite versionComposite)
    {
        DocumentVersionHistoryTableViewer versionTable = new DocumentVersionHistoryTableViewer(versionComposite, SWT.SINGLE);
        versionTable.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        versionTable.addDoubleClickListener(new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent event)
            {
                Version documentVersion = getDocumentVersion();
                getDocument(documentVersion.getInstanceID(), documentVersion.getLabel());
            }
        });
        return versionTable;
    }

    /**
     * Initialize result area. This included a table, where the results are show.
     * 
     * @param searchComp result table
     */
    private void createDocumentViewArea(Composite searchComp)
    {
        Group resultGroup = new Group(searchComp, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        resultGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        resultGroup.setLayout(new GridLayout());
        resultGroup.setText(Resources.Frames.Dialog.Texts.EXISTING_DOCUMENT.getText());

        // Set scroll composite to set view scrollable
        ScrolledComposite searchResult = new ScrolledComposite(resultGroup, SWT.NONE);
        searchResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        searchResult.setLayout(new GridLayout());
        searchResult.setExpandHorizontal(true);
        searchResult.setExpandVertical(true);
        documentTable = createDocumentTable(searchResult);
        initDocumentTable();
        searchResult.setContent(documentTable.getControl());
    }

    /**
     * 
     * create and fill result table with data
     * 
     * @param searchResult
     */
    private DocumentTableViewer createDocumentTable(final ScrolledComposite searchResult)
    {
        final DocumentTableViewer resultTable = new DocumentTableViewer(searchResult, SWT.SINGLE);
        resultTable.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        filter = new DocumentFilter();
        resultTable.addFilter(filter);

        resultTable.getTable().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                DocumentContentProperties documentConent = getSelectedDocument();
                if (documentConent != null)
                {
                    initVersionTable(documentConent);
                    actionShowDocument.setEnabled(true);
                }
                else
                    actionShowDocument.setEnabled(false);
            }
        });

        resultTable.getTable().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                DocumentContentProperties knowledgeSearchResult = getSelectedDocument();
                if (knowledgeSearchResult.getVersion() != null)
                    getDocument(knowledgeSearchResult.getID(), knowledgeSearchResult.getVersion());
            }
        });

        return resultTable;
    }

    /**
     * Returns the current selected result object from the table.
     * 
     * @return the result object, can not be <code>NULL</code>
     */
    private DocumentContentProperties getSelectedDocument()
    {
        TableItem selectedItem = documentTable.getTable().getItem(documentTable.getTable().getSelectionIndex());
        return (DocumentContentProperties) selectedItem.getData();
    }

    /**
     * Returns the {@link Version} currently selected.
     * 
     * @return the {@link Version} or null, if nothing was selected
     */
    public Version getDocumentVersion()
    {
        if (versionTable.getTable().getSelectionIndex() != -1)
        {
            TableItem tableItem = versionTable.getTable().getItem(versionTable.getTable().getSelectionIndex());
            return (Version) tableItem.getData();
        }
        return null;
    }

    /**
     * Initialize the filter text.
     * 
     * @param searchComp
     */
    private void createFilterArea(Composite searchComp)
    {
        Group searchAreaGroup = new Group(searchComp, SWT.SHADOW_OUT | SWT.TOP | SWT.V_SCROLL);
        searchAreaGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        searchAreaGroup.setLayout(new GridLayout(2, false));
        searchAreaGroup.setText(Resources.Frames.Global.Texts.FILTER_THE_LIST.getText());

        filterTxt = new Text(searchAreaGroup, SWT.BORDER);
        filterTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        filterTxt.addVerifyListener(new VerifyListener()
        {
            @Override
            public void verifyText(VerifyEvent event)
            {
                String currentStr = event.text;
                filter.setSearchText(currentStr);
                documentTable.refresh();
            }
        });
    }

    /** Sets the results into the table */
    private void initDocumentTable()
    {
        documentTable.setInput(MainController.getInstance().getAllDocuments());
        documentTable.getTable().redraw();
        documentTable.refresh();
    }

    /** Sets the results into the table */
    private void initVersionTable(DocumentContentProperties documentConent)
    {
        VersionHistory history = MainController.getInstance().getVersionHistoryFromNodeID(documentConent.getID());
        versionTable.setInput(history.getVersions());
        versionTable.refresh();
    }

    /**
     * This action open the file included in actual selected knowledge link.
     */
    private Action createActionShowDocument()
    {
        return Resources.Frames.Knowledge.Actions.SHOW_KNOW_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                DocumentContentProperties knowledgeSearchResult = getSelectedDocument();
                Version documentVersion = getDocumentVersion();
                if (documentVersion != null)
                    getDocument(documentVersion.getInstanceID(), documentVersion.getLabel());
                else if (knowledgeSearchResult.getVersion() != null)
                    getDocument(knowledgeSearchResult.getID(), knowledgeSearchResult.getVersion());

            }
        });
    }

    /**
     * This action open the file included in actual selected knowledge link.
     */
    private Action createActionReloadList()
    {
        return Resources.Frames.Table.Actions.RELOAD_TABLE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                initDocumentTable();
            }
        });
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

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContextMenu()
     */
    @Override
    protected void createContextMenu()
    {
        // Create menu manager.
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener()
        {
            public void menuAboutToShow(IMenuManager mgr)
            {
                fillContextMenu(mgr);
            }
        });

        // Create menu.
        Menu menu = menuMgr.createContextMenu(this.documentTable.getControl());
        this.documentTable.getTable().setMenu(menu);

        menu = menuMgr.createContextMenu(this.versionTable.getControl());
        this.versionTable.getTable().setMenu(menu);

    }

    /**
     * 
     * Fill context menu with actions. This actions are the same action as in toolbar.
     * 
     * @param MenuManager Included all menu items in this view
     */
    private void fillContextMenu(IMenuManager mgr)
    {
        mgr.add(this.actionShowDocument);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createActions()
     */
    @Override
    protected void createActions()
    {
        super.createActions();
        this.actionShowDocument = createActionShowDocument();
        this.actionReloadList = createActionReloadList();
    }

}
