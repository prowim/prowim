/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 30.11.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.knowledge;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.search.KnowledgeSearchLinkResult;
import org.prowim.datamodel.search.KnowledgeSearchResult;
import org.prowim.datamodel.search.KnowledgeSearchResult.KnowledgeSource;
import org.prowim.datamodel.search.SearchProcessResult;
import org.prowim.portal.controller.knowledge.SearchResultController;
import org.prowim.portal.dialogs.KnowledgeObjectDialog;
import org.prowim.portal.dialogs.feedback.WarningDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.KnowledgeSearchTableViewer;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.knowledge.open.OpenKnowledgeLink;
import org.prowim.portal.view.process.ModelEditorView;



/**
 * View to search knowledge. User can gives a keyword and search in DB. At this time it will only search in knowledge objects and knowledge link.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class KnowledgeSearchView extends DefaultView
{
    /** ID of view */
    public static final String         ID                = KnowledgeSearchView.class.getName();

    private static final int           SEARCH_ATTR_COUNT = 6;

    private Text                       searchTxt;

    /** table component to show the search results */
    private KnowledgeSearchTableViewer searchTable       = null;

    private Action                     showKnowLink, actionShowKnowledgeObject;

    private final boolean[]            searchSelections  = new boolean[SEARCH_ATTR_COUNT];

    /**
     * 
     * This method let you start a search from outer of the view. It will open the view and start the search.
     * 
     * @param keyWord word to search, not null
     */
    public void goSearch(final String keyWord)
    {
        initSelections();
        Validate.notNull(keyWord);
        searchTxt.setText(keyWord);
        setResults();
    }

    /**
     * Initializes all selections with <code>TRUE</code>.
     */
    private void initSelections()
    {
        for (int i = 0; i < searchSelections.length; i++)
        {
            searchSelections[i] = true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);

        // Set title of View
        this.setPartName(Resources.Frames.Navigation.Actions.KNOWLEDGE_SEARCH_NAV.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Navigation.Actions.KNOWLEDGE_SEARCH_NAV.getImage());

        // Remove toolbar from view
        super.toolbar.dispose();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite container)
    {
        sashForm = new SashForm(container, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite selectionComp = new Composite(sashForm, SWT.NONE);
        selectionComp.setLayout(new GridLayout(1, false));
        selectionComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite searchComp = new Composite(sashForm, SWT.NONE);
        searchComp.setLayout(new GridLayout(1, false));
        searchComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        createSelectionArea(selectionComp);
        createCompleteSearchArea(searchComp);
        sashForm.setWeights(new int[] { 15, 85 });
    }

    /**
     * Creates a area for selecting the search attributes e.g. DMS, Wiki.
     * 
     * @param selectionComp the component which holds this area
     */
    private void createSelectionArea(Composite selectionComp)
    {
        initSelections();

        Group selectionGroup = new Group(selectionComp, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        selectionGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        selectionGroup.setLayout(new GridLayout(6, false));

        selectionGroup.setText(Resources.Frames.Knowledge.Texts.KNOWLEDGE_SEARCH_SELECTION.getText());

        createCheckBoxWithLabel(selectionGroup, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_KNOWLEDGE_LINK.getText(),
                                SearchResultController.SEARCH_ATTRI_KNOWLEDGE_LINK);
        createCheckBoxWithLabel(selectionGroup, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_KNOWLEDGE_OBJECT.getText(),
                                SearchResultController.SEARCH_ATTRI_KNOWLEDGE_OBJECT);
        createCheckBoxWithLabel(selectionGroup, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_DMS.getText(),
                                SearchResultController.SEARCH_ATTRI_DMS);
        createCheckBoxWithLabel(selectionGroup, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_WIKI.getText(),
                                SearchResultController.SEARCH_ATTRI_WIKI);
        createCheckBoxWithLabel(selectionGroup, Resources.Frames.Process.Texts.PROCESSES.getText(), SearchResultController.SEARCH_ATTRI_PROCESS);
        createCheckBoxWithLabel(selectionGroup, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SOURCE_KNOWLEDGE_DOMAIN.getText(),
                                SearchResultController.SEARCH_ATTRI_KNOWLEDGE_DOMAIN);

    }

    /**
     * Creates a checkbox and a label with given text.
     * 
     * @param selectionGroup the component to create the widgets in
     * @param labelText the text for the label
     * @param index the index of the result array for the checkbox selection result
     */
    private void createCheckBoxWithLabel(Group selectionGroup, String labelText, final int index)
    {
        final Button selectionButton = new Button(selectionGroup, SWT.CHECK);
        selectionButton.setSelection(searchSelections[index]);

        selectionButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                searchSelections[index] = selectionButton.getSelection();
            }
        });

        Label selectionLabel = new Label(selectionGroup, SWT.NONE);
        selectionLabel.setText(labelText);
    }

    /**
     * This is the search part and the result table.
     * 
     * @param container Group, which elements included.
     */
    private void createCompleteSearchArea(Composite container)
    {
        Composite searchComp = new Composite(container, SWT.NONE);
        searchComp.setLayout(new GridLayout(1, false));
        searchComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        createSearchInputArea(searchComp);
        createSearchResultArea(searchComp);
    }

    /**
     * Initialize result area. This included a table, where the results are show.
     * 
     * @param searchComp result table
     */
    private void createSearchResultArea(Composite searchComp)
    {
        Group resultGroup = new Group(searchComp, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        resultGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        resultGroup.setLayout(new GridLayout());
        resultGroup.setText(Resources.Frames.Global.Texts.SEARCH_RESULT.getText());

        // Set scroll composite to set view scrollable
        ScrolledComposite searchResult = new ScrolledComposite(resultGroup, SWT.NONE);
        searchResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        searchResult.setLayout(new GridLayout());
        searchResult.setExpandHorizontal(true);
        searchResult.setExpandVertical(true);
        searchTable = createResultTable(searchResult);
        searchResult.setContent(searchTable.getControl());

        showKnowLink();
        actionShowKnowledgeObject();
        showKnowLink.setEnabled(false);
        actionShowKnowledgeObject.setEnabled(false);
    }

    /**
     * Initialize the search text and search button.
     * 
     * @param searchComp
     */
    private void createSearchInputArea(Composite searchComp)
    {
        Group searchAreaGroup = new Group(searchComp, SWT.SHADOW_OUT | SWT.TOP | SWT.V_SCROLL);
        searchAreaGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        searchAreaGroup.setLayout(new GridLayout(2, false));
        searchAreaGroup.setText(Resources.Frames.Global.Texts.SEARCH_KEY_WORD.getText());

        searchTxt = new Text(searchAreaGroup, SWT.BORDER);
        searchTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

//                                 new KeyAdapter()
//        {
//            /**
//             * 
//             */
//            private static final long serialVersionUID = 1L;
//
//            @Override
//            public void keyPressed(KeyEvent e)
//            {
//                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
//                {
//                    setResults();
//                }
//                super.keyPressed(e);
//            }
//            
//            @Override
//            public void keyReleased(KeyEvent e) {
//              System.out.println("Key released");
//              
//            }
//            
//        });

        Button searchBtn = new Button(searchAreaGroup, SWT.PUSH);
        searchBtn.setImage(Resources.Frames.Header.Actions.SEARCH.getImage());
        searchBtn.setToolTipText(Resources.Frames.Header.Actions.SEARCH.getTooltip());
        searchBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setResults();
            }
        });
    }

    /** Gets for the current search key the results */
    private List<KnowledgeSearchResult> getResults()
    {
        SearchResultController controller = new SearchResultController();
        controller.setSelections(searchSelections);
        String textToSearch = searchTxt.getText();

        if (searchSelections[SearchResultController.SEARCH_ATTRI_WIKI] && textToSearch.length() < 4 && !StringUtils.isEmpty(textToSearch))
        {
            WarningDialog.openWarning(null, Resources.Frames.Knowledge.Texts.KNOWLEDGE_WIKI_TOO_FEW_CHAR.getText());
        }
        else if ( !ArrayUtils.contains(searchSelections, true))
        {
            if (StringUtils.isEmpty(textToSearch))
            {
                WarningDialog.openWarning(null, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SEARCH_AREA_AND_TEXT_EMPTY.getText());
                return controller.search(textToSearch);
            }
            WarningDialog.openWarning(null, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SEARCH_AREA_EMPTY.getText());
        }
        else if (StringUtils.isEmpty(textToSearch))
        {
            if ( !ArrayUtils.contains(searchSelections, true))
            {
                WarningDialog.openWarning(null, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SEARCH_AREA_AND_TEXT_EMPTY.getText());
                return controller.search(textToSearch);
            }
            WarningDialog.openWarning(null, Resources.Frames.Knowledge.Texts.KNOWLEDGE_SEARCH_TEXT_EMPTY.getText());
        }
        return controller.search(textToSearch);
    }

    /** Sets the results into the table */
    private void setResults()
    {
        searchTable.setInput(getResults());
        searchTable.getTable().redraw();
        searchTable.refresh();
    }

    /**
     * 
     * create and fill result table with data
     * 
     * @param searchResult
     */
    private KnowledgeSearchTableViewer createResultTable(final ScrolledComposite searchResult)
    {
        final KnowledgeSearchTableViewer resultTable = new KnowledgeSearchTableViewer(searchResult, SWT.SINGLE);
        resultTable.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        resultTable.getTable().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                showKnowLink.setEnabled(false);
                actionShowKnowledgeObject.setEnabled(false);
                KnowledgeSearchResult selectedResult = getSelectedResult();
                KnowledgeSource resultSource = selectedResult.getSource();

                switch (resultSource)
                {
                    case KNOWLEDGE_DOAMAIN:
                        actionShowKnowledgeObject.setEnabled(false);
                        break;
                    case KNOWLEDGE_OBJECT:
                        actionShowKnowledgeObject.setEnabled(true);
                        break;
                    case KNOWLEDGE_LINK:
                        showKnowLink.setEnabled(true);
                        actionShowKnowledgeObject.setEnabled(true);
                        break;
                    case DMS:
                    case WIKI:
                        showKnowLink.setEnabled(true);
                        break;
                    case PROCESS:
                        showKnowLink.setEnabled(true);
                        break;
                    default:
                        showKnowLink.setEnabled(false);
                        actionShowKnowledgeObject.setEnabled(false);
                }
            }
        });

        resultTable.getTable().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                showKnowLink.runWithEvent(new Event());
            }
        });

        return resultTable;

    }

    /**
     * This action open the file included in actual selected knowledge link.
     */
    private void showKnowLink()
    {
        this.showKnowLink = Resources.Frames.Knowledge.Actions.SHOW_KNOW_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeSearchResult knowledgeSearchResult = getSelectedResult();
                if (knowledgeSearchResult.getSource() == KnowledgeSource.KNOWLEDGE_OBJECT)
                    actionShowKnowledgeObject.runWithEvent(new Event());
                else if (knowledgeSearchResult.getSource() == KnowledgeSource.PROCESS)
                {
                    ModelEditorView modelEditorView;
                    Process process = ((SearchProcessResult) knowledgeSearchResult).getProcess();
                    try
                    {
                        modelEditorView = (ModelEditorView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                                .showView(ModelEditorView.ID, process.getTemplateID(), IWorkbenchPage.VIEW_ACTIVATE);
                        modelEditorView.setProcessId(process.getTemplateID());
                    }
                    catch (PartInitException e)
                    {
                        throw new RuntimeException("Cannot open model editor view", e);
                    }
                }
                else if (knowledgeSearchResult.getSource() != KnowledgeSource.KNOWLEDGE_DOAMAIN)
                {
                    OpenKnowledgeLink.open(knowledgeSearchResult);
                }
            }
        });
    }

    /**
     * Action to show a {@link KnowledgeObject}.
     */
    private void actionShowKnowledgeObject()
    {
        actionShowKnowledgeObject = Resources.Frames.Knowledge.Actions.EDIT_WOB.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeSearchResult knowledgeSearchResult = getSelectedResult();
                String knowledgeObjectID = "";
                if (knowledgeSearchResult.getSource() == KnowledgeSource.KNOWLEDGE_OBJECT)
                    knowledgeObjectID = knowledgeSearchResult.getResultID();
                else if (knowledgeSearchResult.getSource() == KnowledgeSource.KNOWLEDGE_LINK)
                {
                    knowledgeObjectID = ((KnowledgeSearchLinkResult) knowledgeSearchResult).getKnowledgeObject();
                }
                new KnowledgeObjectDialog(null, actionShowKnowledgeObject, "").openDialog(knowledgeObjectID);
            }
        });
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
        Menu menu = menuMgr.createContextMenu(this.searchTable.getControl());
        this.searchTable.getTable().setMenu(menu);
    }

    /**
     * 
     * Fill context menu with actions. This actions are the same action as in toolbar.
     * 
     * @param MenuManager Included all menu items in this view
     */
    private void fillContextMenu(IMenuManager mgr)
    {
        mgr.add(showKnowLink);
        mgr.add(actionShowKnowledgeObject);
        mgr.add(new Separator());
        mgr.add(helpToolBar);
    }

    /**
     * Returns the current selected result object from the table.
     * 
     * @return the result object, can not be <code>NULL</code>
     */
    private KnowledgeSearchResult getSelectedResult()
    {
        TableItem selectedItem = searchTable.getTable().getItem(searchTable.getTable().getSelectionIndex());
        Object obj = selectedItem.getData();

        KnowledgeSearchResult knowledgeSearchResult = (KnowledgeSearchResult) obj;
        return knowledgeSearchResult;
    }
}
