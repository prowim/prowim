/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:35 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/view/admin/AdminView.java $
 * $LastChangedRevision: 5031 $
 *------------------------------------------------------------------------------
 * (c) 24.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.security.RightsRole;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.dialog.RightsRoleController;
import org.prowim.portal.controller.knowledge.SearchPersonsController;
import org.prowim.portal.dialogs.PersonDialog;
import org.prowim.portal.dialogs.feedback.ConfirmDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.i18n.Resources.Frames.Header;
import org.prowim.portal.models.tree.model.PersonLeaf;
import org.prowim.portal.tables.DefaultTable;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.update.UpdateEvent;
import org.prowim.portal.update.UpdateNotificationCollection;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;



/**
 * Provides a View for administrate user.
 * 
 * @author Saad Wardi, Maziar Khodaei
 * @version $Revision: 5031 $
 * @since 2.0
 */
public class AdminView extends DefaultView
{
    /** ID of view */
    public static final String      ID                  = AdminView.class.getName();

    private static List<RightsRole> assignedRightsRoles = new ArrayList<RightsRole>();

    private Action                  addNewUserAction, editUserAction, allUserAction, deletePersonAction;
    private DefaultTable            searchTable         = null;
    private DefaultTable            assignedRolesTable  = null;
    private DefaultTable            existingRolesTable  = null;
    private Text                    personSearchTxt     = null;
    private Group                   allUserGroup;
    private Group                   topLeftGroup;
    private Button                  removeSelectedRoleButton;
    private Button                  assigneSelectedRoleButton;
    private Button                  removeAllAssignedRolesButton;
    private Button                  assignAllRolesButton;

    private ScrolledComposite       scrollComp;

    private ScrolledComposite       scrollAddComp;

    private List<RightsRole>        existingRightsRoles;

    /**
     * Description.
     */
    public AdminView()
    {
        assignedRightsRoles = new ArrayList<RightsRole>();
        existingRightsRoles = new ArrayList<RightsRole>();
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
        this.setPartName(Resources.Frames.Global.Texts.USER_MANAGEMENT.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Navigation.Actions.ADMIN_NAV.getImage());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite container)
    {
        Composite mainGroup = new Composite(container, SWT.NONE);
        mainGroup.setLayout(new GridLayout(1, true));
        mainGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        SashForm sashForm = new SashForm(mainGroup, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        initTopControl(sashForm);
        initButtomControl(sashForm);
        sashForm.setWeights(new int[] { 5, 5 });
        createActions();
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
        this.addNewUserAction.setEnabled(true);
        toolbar.addToolBarItem(this.allUserAction);
        toolbar.addToolBarItem(this.addNewUserAction);
        this.editUserAction.setEnabled(false);
        toolbar.addToolBarItem(this.editUserAction);

        this.deletePersonAction.setEnabled(false);
        toolbar.addToolBarItem(this.deletePersonAction);
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
        Menu menu = menuMgr.createContextMenu(this.searchTable);
        this.searchTable.setMenu(menu);
    }

    /**
     * 
     * Fill context menu with actions. This actions are the same action as in toolbar.
     * 
     * @param MenuManager Included all menu items in this view
     */
    private void fillContextMenu(IMenuManager mgr)
    {
        mgr.add(addNewUserAction);
        mgr.add(editUserAction);
        mgr.add(deletePersonAction);
        mgr.add(new Separator());
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createActions()
     */
    @Override
    protected void createActions()
    {
        super.createActions();
        this.addNewUserAction = Resources.Frames.Dialog.Actions.ADD_NEW_USER.getAction(new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                openNewPersonDialog();
            }
        });

        this.editUserAction = Resources.Frames.Dialog.Actions.EDIT_USER.getAction(new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                editPersonDialog();
            }
        });

        this.allUserAction = Resources.Frames.Dialog.Actions.GET_ALL_USER.getAction(new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                setResultsAll();
                disableRoleAssignmentGroup();

            }
        });

        this.deletePersonAction = Resources.Frames.Dialog.Actions.DELETE_USER.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (ConfirmDialog.openConfirm(null, deletePersonAction.getToolTipText()))
                {
                    TableItem selectedItem = searchTable.getItem(searchTable.getSelectionIndex());

                    Object obj = selectedItem.getData(Integer.toString(0));

                    if (obj instanceof Person)
                    {
                        Person person = (Person) obj;
                        MainController.getInstance().deleteInstance(person.getID());

                        // Remove deleted person from table
                        searchTable.remove(searchTable.getSelectionIndex());
                        searchTable.redraw();
                        searchTable.pack();
                    }
                }

                if (searchTable.getSelectionIndex() < 0)
                {
                    deletePersonAction.setEnabled(false);
                    toolbar.getItem(Resources.Frames.Dialog.Actions.DELETE_USER.getText()).setEnabled(false);
                    disableRoleAssignmentGroup();
                }

            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus()
    {
    }

    private void initTopControl(Composite group)
    {
        Composite composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(1, true));

        // header top **********************************************************************************

        Composite topGroup = new Composite(composite, SWT.NONE);
        topGroup.setLayout(new GridLayout(1, false));
        topGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        final Group topLeftGroup = new Group(topGroup, SWT.SHADOW_IN | SWT.RIGHT);
        topLeftGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        topLeftGroup.setLayout(new GridLayout(3, false));
        topLeftGroup.setText(Resources.Frames.Dialog.Texts.USER_SELECTION.getText());

        Label personSearchLabel = new Label(topLeftGroup, SWT.TRAIL);
        personSearchLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        personSearchLabel.setText(Resources.Frames.Dialog.Texts.USER_SEARCH.getText() + GlobalConstants.DOUBLE_POINT);

        personSearchTxt = new Text(topLeftGroup, SWT.BORDER);
        personSearchTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        personSearchTxt.setEditable(true);
        personSearchTxt.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.keyCode == 13)
                {
                    setResults();
                }
            }
        });

        Button personSearchBtn = new Button(topLeftGroup, SWT.PUSH);
        personSearchBtn.setImage(Header.Actions.SEARCH.getImage());
        personSearchBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                setResults();
                disableRoleAssignmentGroup();
            }
        });

        Label personsFoundLabel = new Label(topLeftGroup, SWT.TRAIL);
        personsFoundLabel.setLayoutData(new GridData(GridData.END, SWT.TOP, false, false));
        personsFoundLabel.setText(Resources.Frames.Dialog.Texts.USER_LABEL.getText() + GlobalConstants.DOUBLE_POINT);

        Group resultGroup = new Group(topLeftGroup, SWT.SHADOW_OUT | SWT.BEGINNING | SWT.H_SCROLL | SWT.V_SCROLL);
        resultGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        resultGroup.setLayout(new GridLayout(1, false));
        resultGroup.setText(Resources.Frames.Global.Texts.SEARCH_RESULT.getText());

        ScrolledComposite searchResult = new ScrolledComposite(resultGroup, SWT.H_SCROLL | SWT.V_SCROLL);
        searchResult.setLayoutData(new GridData(GridData.FILL_BOTH));
        searchResult.setLayout(new GridLayout());
        searchResult.setExpandHorizontal(true);
        searchResult.setExpandVertical(true);
        searchTable = createResultTable(searchResult);
        searchTable.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                TableItem item = searchTable.getItem(searchTable.getSelectionIndex());
                Object obj = item.getData(Integer.toString(0));
                if (obj instanceof Person)
                {
                    getRolesResults();
                    initExistingRoles();
                }
            }
        });

        searchTable.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent event)
            {
                if (searchTable.getSelectionCount() > 0)
                {
                    enableRoleAssignmentGroup();
                }
            }

            @Override
            public void focusLost(FocusEvent event)
            {
                if (searchTable.getSelectionCount() < 0)
                {
                    disableRoleAssignmentGroup();
                    resetMenus();
                }
            }
        });
        searchTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                editPersonDialog();
            }
        });
        searchResult.setContent(searchTable);
    }

    private void initExistingRoles()
    {
        existingRightsRoles = MainController.getInstance().getExistingRightsRoles();
        Iterator<RightsRole> assignedIt = assignedRightsRoles.iterator();
        Iterator<RightsRole> existIt;
        while (assignedIt.hasNext())
        {
            RightsRole assignedRole = assignedIt.next();

            existIt = existingRightsRoles.iterator();
            while (existIt.hasNext())
            {
                RightsRole existRole = existIt.next();
                if (assignedRole.getID().equals(existRole.getID()))
                {
                    existIt.remove();
                }
            }
        }
        createRightTable();
        enableRoleAssignmentGroup();
        assignedRolesTable.redraw();
    }

    private void disableRoleAssignmentGroup()
    {
        topLeftGroup.setEnabled(false);
        allUserGroup.setEnabled(false);
        clearTables();
        setDisableButtons();
    }

    private void clearTables()
    {
        assignedRolesTable.clearAll();
        existingRolesTable.clearAll();
    }

    private void enableRoleAssignmentGroup()
    {
        topLeftGroup.setEnabled(true);
        allUserGroup.setEnabled(true);
        setEnableButtons();
    }

    private void initButtomControl(Composite group)
    {
        Composite composite = new Composite(group, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(1, true));

        Composite topGroup = new Composite(composite, SWT.NONE);
        topGroup.setLayout(new GridLayout(1, false));
        topGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

        topLeftGroup = new Group(topGroup, SWT.SHADOW_IN | SWT.RIGHT);
        topLeftGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        topLeftGroup.setLayout(new GridLayout(1, false));
        topLeftGroup.setText(Resources.Frames.Global.Texts.ROLES_ASSIGNEMENT.getText());

        Composite resultGroup = new Composite(topLeftGroup, SWT.SHADOW_OUT | SWT.BEGINNING | SWT.H_SCROLL | SWT.V_SCROLL);
        resultGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        resultGroup.setLayout(new GridLayout(3, false));
        createRolesArea(resultGroup);
    }

    /**
     * Creates the buttom area to manage RightsRoles.
     * 
     * @param parent the parent composite.
     */
    private void createRolesArea(Composite parent)
    {
        // the main composite.
        Control control = (parent);
        ((Composite) control).setLayout(new GridLayout(3, false));

        // Group for left table
        allUserGroup = new Group((Composite) control, SWT.SHADOW_OUT | SWT.BEGINNING | SWT.H_SCROLL | SWT.V_SCROLL);
        allUserGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        allUserGroup.setLayout(new GridLayout(1, false));
        allUserGroup.setText(Resources.Frames.Global.Texts.ASSIGNED_ROLES.getText());

        scrollComp = new ScrolledComposite(allUserGroup, SWT.H_SCROLL | SWT.V_SCROLL);
        scrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        scrollComp.setLayout(new GridLayout());
        scrollComp.setExpandHorizontal(true);
        scrollComp.setExpandVertical(true);

        createLeftTable();
        /** Create buttons. */
        Composite middleContainer = new Composite((Composite) control, SWT.NONE);
        middleContainer.setLayout(new GridLayout(1, false));

        removeSelectedRoleButton = new Button(middleContainer, SWT.PUSH);
        removeSelectedRoleButton.setEnabled(false);
        removeSelectedRoleButton.setImage(Resources.Frames.Dialog.Actions.FORWARD_REMOVE_SELECTED_ROLE.getImage());
        removeSelectedRoleButton.setToolTipText(Resources.Frames.Dialog.Actions.FORWARD_REMOVE_SELECTED_ROLE.getTooltip());

        removeSelectedRoleButton.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                unassigneSelectedRightRole();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

        assigneSelectedRoleButton = new Button(middleContainer, SWT.PUSH);
        assigneSelectedRoleButton.setEnabled(false);
        assigneSelectedRoleButton.setImage(Resources.Frames.Dialog.Actions.FORWARD_ADD_SELECTED_ROLE.getImage());
        assigneSelectedRoleButton.setToolTipText(Resources.Frames.Dialog.Actions.FORWARD_ADD_SELECTED_ROLE.getTooltip());
        assigneSelectedRoleButton.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                assigneSelectedRightRole();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

        removeAllAssignedRolesButton = new Button(middleContainer, SWT.PUSH);
        removeAllAssignedRolesButton.setImage(Resources.Frames.Dialog.Actions.FORWARD_DOUBLE_REMOVE_ALL_ROLES.getImage());
        removeAllAssignedRolesButton.setToolTipText(Resources.Frames.Dialog.Actions.FORWARD_DOUBLE_REMOVE_ALL_ROLES.getTooltip());
        removeAllAssignedRolesButton.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                removeAllAssignedRightsRoles();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

        assignAllRolesButton = new Button(middleContainer, SWT.PUSH);
        assignAllRolesButton.setImage(Resources.Frames.Dialog.Actions.FORWARD_DOUBLE_ADD_ALL_ROLES.getImage());
        assignAllRolesButton.setToolTipText(Resources.Frames.Dialog.Actions.FORWARD_DOUBLE_ADD_ALL_ROLES.getTooltip());
        assignAllRolesButton.addSelectionListener(new SelectionListener()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                assigneAllRightsRoles();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

        // Create right container

        // Create group for right table
        Group selectUser = new Group((Composite) control, SWT.H_SCROLL | SWT.V_SCROLL);
        selectUser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        selectUser.setLayout(new GridLayout(1, false));
        selectUser.setText(Resources.Frames.Global.Texts.EXISTING_ROLES.getText());

        scrollAddComp = new ScrolledComposite(selectUser, SWT.H_SCROLL | SWT.V_SCROLL);
        scrollAddComp.setLayoutData(new GridData(GridData.FILL_BOTH));
        scrollAddComp.setLayout(new GridLayout());
        scrollAddComp.setExpandHorizontal(true);
        scrollAddComp.setExpandVertical(true);

        // Create right table
        createRightTable();

        this.disableRoleAssignmentGroup();
    }

    /** send the data to the server. */
    private void assigneRolesToSelectedUser(boolean modified)
    {
        String selectedUserID = getSelectedUserID();
        if (selectedUserID != null && modified)
        {
            commitRightsRoles(selectedUserID);
            createLeftTable();
        }
    }

    /**
     * Assigne all existing RightsRoles to the selected user.
     */
    private void assigneAllRightsRoles()
    {
        Iterator<RightsRole> it = existingRightsRoles.iterator();
        boolean modified = false;
        while (it.hasNext())
        {
            RightsRole role = it.next();
            if ( !exists(assignedRightsRoles, role))
            {
                assignedRightsRoles.add(role);
                modified = true;
            }
        }
        assigneRolesToSelectedUser(modified);
        initExistingRoles();

    }

    /**
     * Removes all RightsRoles assigned to the selected user.
     */
    private void removeAllAssignedRightsRoles()
    {
        assignedRightsRoles.clear();
        assigneRolesToSelectedUser(true);
        initExistingRoles();
    }

    // Delete a item from selected list in selectable list
    private void unassigneSelectedRightRole()
    {
        if (assignedRolesTable.getSelectionIndex() > -1)
        {
            TableItem item = assignedRolesTable.getItem(assignedRolesTable.getSelectionIndex());
            RightsRole role = (RightsRole) item.getData("0");

            assignedRightsRoles.remove(role);
            assignedRolesTable.remove(assignedRolesTable.getSelectionIndex());
            assigneRolesToSelectedUser(true);
            initExistingRoles();
        }
    }

    /** Assigne the selected RightsRoles to the selected user. */
    private void assigneSelectedRightRole()
    {
        if (existingRolesTable.getSelectionIndex() > -1)
        {
            TableItem item = existingRolesTable.getItem(existingRolesTable.getSelectionIndex());
            RightsRole role = (RightsRole) item.getData("0");

            boolean modified = false;
            if ( !exists(assignedRightsRoles, role))
            {
                assignedRightsRoles.add(role);
                modified = true;
            }

            createLeftTable();
            assigneRolesToSelectedUser(modified);
            initExistingRoles();
        }
    }

    /**
     * checks if a role already exists in the list.
     * 
     * @param assignedRightsRoles to be checked list
     * @param role role to search.
     * @return true if role already exists. False otherwise.
     */
    private boolean exists(List<RightsRole> assignedRightsRoles, RightsRole role)
    {
        boolean result = false;
        Iterator<RightsRole> it = assignedRightsRoles.iterator();
        while (it.hasNext())
        {
            RightsRole aRole = it.next();
            if (aRole.getID().equals(role.getID()))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    private void commitRightsRoles(String userID)
    {
        MainController.getInstance().assigneRightsRolesToUser(userID, assignedRightsRoles);
    }

    /**
     * 
     * convertHorizontalDLUsToPixels.
     * 
     * @param dlus integer
     * @return integer
     */
    protected int convertHorizontalDLUsToPixels(int dlus)
    {
        // test for failure to initialize for backward compatibility
        Font dialogFont = JFaceResources.getDialogFont();
        return (int) ((Graphics.getAvgCharWidth(dialogFont) * dlus + 4 / 2) / 4);
    }

    private void createLeftTable()
    {

        assignedRolesTable = createResultTable(scrollComp);
        assignedRolesTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        initLeftTable();

        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, true)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 3), SWT.DEFAULT).applyTo(assignedRolesTable);

        scrollComp.setContent(assignedRolesTable);
    }

    /**
     * 
     * Initialize the assignedRolesTable
     */
    private void initLeftTable()
    {
        RightsRoleController controller = new RightsRoleController();
        DefaultTableModel personModel = new DefaultTableModel(controller.getTableModel(assignedRightsRoles), controller.getColumns());
        assignedRolesTable.setTableModel(personModel);
        assignedRolesTable.redraw();
    }

    /**
     * Eneables the buttons.
     */
    private void setEnableButtons()
    {
        this.assignAllRolesButton.setEnabled(true);
        this.assigneSelectedRoleButton.setEnabled(true);
        this.removeAllAssignedRolesButton.setEnabled(true);
        this.removeSelectedRoleButton.setEnabled(true);
    }

    /**
     * Eneables the buttons.
     */
    private void setDisableButtons()
    {
        this.assignAllRolesButton.setEnabled(false);
        this.assigneSelectedRoleButton.setEnabled(false);
        this.removeAllAssignedRolesButton.setEnabled(false);
        this.removeSelectedRoleButton.setEnabled(false);
    }

    private void createRightTable()
    {
        existingRolesTable = createResultTable(scrollAddComp);
        initRightTable();
        existingRolesTable.sortAtColumn(0);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 3), SWT.DEFAULT).applyTo(existingRolesTable);

        scrollAddComp.setContent(existingRolesTable);
    }

    private void initRightTable()
    {
        RightsRoleController controller = new RightsRoleController();
        DefaultTableModel availableRightsRolesModel = new DefaultTableModel(controller.getTableModel(existingRightsRoles), controller.getColumns());
        existingRolesTable.setTableModel(availableRightsRolesModel);

    }

    /**
     * 
     * create and fill result table with data
     * 
     * @param searchResult
     */
    private DefaultTable createResultTable(final ScrolledComposite searchResult)
    {

        DefaultTableModel resultMode = new DefaultTableModel();
        final DefaultTable resultTable = new DefaultTable(searchResult, resultMode, SWT.SINGLE);
        resultTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        resultTable.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                toolbar.getItem(Resources.Frames.Dialog.Actions.DELETE_USER.getText()).setEnabled(true);
                deletePersonAction.setEnabled(true);
                toolbar.getItem(Resources.Frames.Dialog.Actions.EDIT_USER.getText()).setEnabled(true);
                editUserAction.setEnabled(true);

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

        return resultTable;

    }

    private void openNewPersonDialog()
    {
        PersonDialog personDilaog = new PersonDialog(null, Resources.Frames.Dialog.Actions.ADD_NEW_USER.getAction(),
                                                     Resources.Frames.Dialog.Texts.DESC_USER_CREATE_EDIT.getText(), null, true);

        if (personDilaog.open() == IDialogConstants.OK_ID)
        {
            MainController.getInstance().createUser(personDilaog.getPerson());

            setResults();
        }
    }

    private void editPersonDialog()
    {
        if (searchTable.getSelectionIndex() >= 0)
        {
            TableItem selectedItem = searchTable.getItem(searchTable.getSelectionIndex());
            Object obj = selectedItem.getData(Integer.toString(0));

            if (obj instanceof Person)
            {
                Person p = (Person) obj;

                PersonLeaf personModel = new PersonLeaf(p);

                PersonDialog personDilaog = new PersonDialog(null, Resources.Frames.Dialog.Actions.EDIT_USER.getAction(),
                                                             Resources.Frames.Dialog.Texts.DESC_USER_CREATE_EDIT.getText(), personModel, true);

                if (personDilaog.open() == IDialogConstants.OK_ID)
                {
                    Person person = personDilaog.getPerson();
                    MainController.getInstance().updateUserInfo(person);
                }

            }
            setResults();
        }
    }

    private String getSelectedUserID()
    {
        if (searchTable.getSelectionIndex() >= 0)
        {
            TableItem selectedItem = searchTable.getItem(searchTable.getSelectionIndex());
            Object obj = selectedItem.getData(Integer.toString(0));

            if (obj instanceof Person)
            {
                Person person = (Person) obj;
                return person.getID();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /** Sets the results into the table */
    private void setResults()
    {
        searchTable.setTableModel(getResults());
        searchTable.sortAtColumn(0);
        resetMenus();

    }

    private void resetMenus()
    {
        this.deletePersonAction.setEnabled(false);
        toolbar.getItem(Resources.Frames.Dialog.Actions.DELETE_USER.getText()).setEnabled(false);
        this.editUserAction.setEnabled(false);
        toolbar.getItem(Resources.Frames.Dialog.Actions.EDIT_USER.getText()).setEnabled(false);
    }

    /** Sets the results into the table */
    private void setResultsAll()
    {
        personSearchTxt.setText("");
        searchTable.setTableModel(getResults());
        searchTable.sortAtColumn(0);
    }

    /** Gets for the current search key the results */
    private DefaultTableModel getResults()
    {
        SearchPersonsController controller = new SearchPersonsController(personSearchTxt.getText());
        return new DefaultTableModel(controller.getTableModel(), controller.getColumns());
    }

    /** Gets for the current search key the results */
    private void getRolesResults()
    {
        if (searchTable.getSelectionIndex() >= 0)
        {
            TableItem selectedItem = searchTable.getItem(searchTable.getSelectionIndex());
            Object obj = selectedItem.getData(Integer.toString(0));

            if (obj instanceof Person)
            {
                Person person = (Person) obj;
                RightsRoleController controller = new RightsRoleController();
                DefaultTableModel model = new DefaultTableModel(controller.getTableModel(person.getID()), controller.getColumns());
                assignedRolesTable.setTableModel(model);
                assignedRightsRoles.clear();
                copyObjectsFromModel(model);
                createLeftTable();
            }
        }

    }

    private void copyObjectsFromModel(DefaultTableModel model)
    {
        for (int i = 0; i < model.getRowCount(); i++)
        {
            RightsRole role = (RightsRole) model.getValueAt(i, 0);
            assignedRightsRoles.add(role);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.PERSON);
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

        for (UpdateEvent updateEvent : updates)
        {
            Collection<String> entitiyIds = updateEvent.getEntityIds();
            for (String id : entitiyIds)
            {
                if (searchTable.getSelectionIndex() >= 0)
                {
                    TableItem selectedItem = searchTable.getItem(searchTable.getSelectionIndex());

                    if (id.equals(((Person) selectedItem.getData(Integer.toString(0))).getID()))
                    {
                        initLeftTable();
                        initExistingRoles();
                    }
                }
            }
        }
    }
}
