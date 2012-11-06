/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:35 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/view/activity/ActivityEditView.java $
 * $LastChangedRevision: 5031 $
 *------------------------------------------------------------------------------
 * (c) 25.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.portal.view.activity;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.dialog.OrgaController;
import org.prowim.portal.controller.dialog.PersonController;
import org.prowim.portal.dialogs.ComboBoxDialog;
import org.prowim.portal.dialogs.DateDialog;
import org.prowim.portal.dialogs.FileDialogProWim;
import org.prowim.portal.dialogs.FloatDialog;
import org.prowim.portal.dialogs.IntegerDialog;
import org.prowim.portal.dialogs.LinkDialog;
import org.prowim.portal.dialogs.LongTextDialog;
import org.prowim.portal.dialogs.MultiListDialog;
import org.prowim.portal.dialogs.SelectOrganizationDialog;
import org.prowim.portal.dialogs.SelectPersonDialog;
import org.prowim.portal.dialogs.ShortTextDialog;
import org.prowim.portal.dialogs.SingleListDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.ParameterClient;
import org.prowim.portal.tables.DefaultTable;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.knowledge.open.OpenKnowledgeLink;
import org.prowim.rap.framework.components.impl.DefaultConstraint;



/**
 * Show the view to setting the start parameter of a activity. This is not finished.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5031 $
 */
public class ActivityEditView extends DefaultView
{
    /** ID of view */
    public static final String                ID                 = ActivityEditView.class.getName();

    private DefaultTable                      outParamTable      = null;
    private DefaultTable                      decisionParamTable = null;
    private DefaultTable                      selectedTable      = null;
    private DefaultTable                      inParamTable       = null;
    private final DefaultTableModel           inParamModel, outParamModel, decisionParamModel;
    private final Hashtable<String, Document> documentHash       = new Hashtable<String, Document>();
    private String                            description        = "";

    private Action                            deleteValue, addText, addLongText, addInteger, addFloat, addSingleList, addMultiList, addComboBox,
            addFile, addPerson, addOrgUnit, addDate, addLink, openFile;

    /**
     * This view shows the view for starting one activity. It shows a window with a field, which shows the name of activity, a table <br/>
     * which shows input parameters and a table, which shows the output parameters parameter of activity.
     * 
     * @param inParamModel input parameter for active activity
     * @param outParamModel output parameter for active activity
     * @param decisionParamModel Shows parameter to select a decision
     * @param description TODO
     */
    public ActivityEditView(final DefaultTableModel inParamModel, final DefaultTableModel outParamModel, DefaultTableModel decisionParamModel,
            String description)
    {
        this.inParamModel = inParamModel;
        this.outParamModel = outParamModel;
        this.decisionParamModel = decisionParamModel;
        this.description = description;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        createActions();

        // input parameter area
        final Group inParamGroup = new Group(parent, SWT.SHADOW_OUT | SWT.TOP);
        inParamGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        inParamGroup.setLayout(new GridLayout(1, false));
        inParamGroup.setText(Resources.Frames.Activity.Texts.INPUT_PARAMETER.getText());

        // table for input parameter
        inParamTable = new DefaultTable(inParamGroup, this.inParamModel, SWT.SINGLE);
        inParamTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        inParamTable.sortAtColumn(0);
        inParamTable.setLinesVisible(true);
        inParamTable.addMouseListener(new MouseListener()
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
                if (inParamTable.getSelectionIndex() > -1)
                    openDialog(((Parameter) inParamTable.getItem(inParamTable.getSelectionIndex()).getData(Integer.toString(1))).getInfoTypeID());
            }
        });

        // output area
        Group outParamGroup = new Group(parent, SWT.SHADOW_OUT | SWT.TOP);
        outParamGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        outParamGroup.setLayout(new GridLayout(1, false));
        outParamGroup.setText(Resources.Frames.Activity.Texts.OUTPUT_PARAMETER.getText());

        // table for output parameter
        this.outParamTable = new DefaultTable(outParamGroup, this.outParamModel, SWT.SINGLE);
        this.outParamTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.outParamTable.sortAtColumn(0);
        this.outParamTable.setLinesVisible(true);

        this.outParamTable.addMouseListener(new MouseListener()
        {

            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                selectedTable = outParamTable;
                if (outParamTable.getSelectionIndex() > -1)
                    selectDialog(((Parameter) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1))).getInfoTypeID());
            }

            @Override
            public void mouseDown(MouseEvent e)
            {
                selectedTable = outParamTable;
            }

            @Override
            public void mouseUp(MouseEvent e)
            {
            }

        });

        if (this.decisionParamModel != null && this.decisionParamModel.getRowCount() > 0)
        {
            // decision area
            Group decisionParamGroup = new Group(parent, SWT.SHADOW_OUT | SWT.TOP);
            decisionParamGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
            decisionParamGroup.setLayout(new GridLayout(1, false));
            decisionParamGroup.setText(Resources.Frames.Activity.Texts.DECISION_PARAMETER.getText());

            // table for decision parameter
            this.decisionParamTable = new DefaultTable(decisionParamGroup, this.decisionParamModel, SWT.SINGLE);
            this.decisionParamTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            this.decisionParamTable.sortAtColumn(0);
            this.decisionParamTable.setLinesVisible(true);

            this.decisionParamTable.addMouseListener(new MouseListener()
            {

                @Override
                public void mouseDoubleClick(MouseEvent e)
                {
                    selectedTable = decisionParamTable;
                    if (decisionParamTable.getSelectionIndex() > -1)
                        selectDialog(((Parameter) decisionParamTable.getItem(decisionParamTable.getSelectionIndex()).getData(Integer.toString(1)))
                                .getInfoTypeID());
                }

                @Override
                public void mouseDown(MouseEvent e)
                {
                    selectedTable = outParamTable;
                }

                @Override
                public void mouseUp(MouseEvent e)
                {
                }

            });

        }
        // Create a group for the frame
        final Group infoGroup = new Group(parent, SWT.SHADOW_OUT | SWT.TOP);
        infoGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        infoGroup.setLayout(new GridLayout(1, false));
        infoGroup.setText(Resources.Frames.Activity.Texts.DESCRIPTION_OF_ACTIVITY.getText());

        Text infoText = new Text(infoGroup, SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.WRAP);
        infoText.setText(this.description);
        infoText.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Create contextMenu for this view
        createContextMenu();

    }

    /**
     * 
     * Get all outgoing parameters.
     * 
     * @return List<ParameterClient> Never null. returns an empty list, if no parameters are found.
     */
    public List<ParameterClient> getOutputParameters()
    {
        List<ParameterClient> paramList = new ArrayList<ParameterClient>();

        for (int idx = 0; idx < this.outParamTable.getItemCount(); idx++)
        {
            TableItem item = this.outParamTable.getItem(idx);
            if (item != null)
            {
                paramList.add((ParameterClient) item.getData(Integer.toString(1)));
            }
        }
        return paramList;
    }

    /**
     * 
     * Return all outgoing parameter.
     * 
     * @return List#ParameterClient
     */
    public Hashtable<String, Document> getDocumentList()
    {
        return this.documentHash;
    }

    /**
     * 
     * Get all decision parameters.
     * 
     * @return List#ParameterClient
     */
    public List<ParameterClient> getDecisionParam()
    {
        List<ParameterClient> paramList = new ArrayList<ParameterClient>();

        if (this.decisionParamTable != null)
        {
            for (int idx = 0; idx < this.decisionParamTable.getItemCount(); idx++)
            {
                TableItem item = this.decisionParamTable.getItem(idx);
                if (item != null)
                {
                    paramList.add((ParameterClient) item.getData(Integer.toString(1)));
                }
            }
        }
        return paramList;
    }

    /**
     * Set value, which are return from dialog
     */
    private void setValues(ParameterClient parameter, Object returnValues)
    {
        ObjectArray objectArray = new ObjectArray();

        if ( !returnValues.toString().equals(""))
        {
            objectArray.add(returnValues);
            parameter.setSelectedValues(objectArray);
            selectedTable.getItem(selectedTable.getSelectedCell()[0]).setText(1, returnValues.toString());
        }
        else
        {
            parameter.setSelectedValues(objectArray);
            selectedTable.getItem(selectedTable.getSelectedCell()[0]).setText(1, "");
        }

        selectedTable.getItem(selectedTable.getSelectedCell()[0]).setData(Integer.toString(1), parameter);

    }

    /**
     * 
     * Create actions for this view
     */
    @Override
    protected void createActions()
    {
        // Delete value from field
        createDeleteValueAction();

        // Action to add or change a short text
        createAddTextAction();

        // Action for adding a long text
        createAddLongTextAction();

        // Action to adding integer value
        createAddIntegerAction();

        // Action to adding float values
        createAddFlotAction();

        // Action to select values from a single list
        createAddStrListAction();

        // Action to select values from a multi list
        createAddMultiListAction();

        // Action to select values from a combo box
        createAddComboBoxAction();

        // Action to open a dialog to upload a file
        createAddFileAction();

        // Action to add or change a user
        createAddPersonAction();

        // Action to add or change organization unit
        createAddOrgUnitAction();

        // Action to add or change date
        createAddDateAction();

        // Action to add or change date
        createAddLinkAction();

        // Action to open a file
        createOpenFileAction();
    }

    /**
     * action to open a dialog to add a link.
     */
    private void createAddLinkAction()
    {
        this.addLink = Resources.Frames.Dialog.Actions.ADD_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                LinkDialog linkDialog = new LinkDialog(null, addLink, "", parameter.getInfoTypeID(), new DefaultConstraint(parameter.getMinValue(),
                                                                                                                           parameter.getMaxValue(),
                                                                                                                           parameter.isRequired()));
                if (linkDialog.open() == IDialogConstants.OK_ID)
                {
                    setValues(parameter, linkDialog.getLinkValue());
                }
            }
        });
    }

    /**
     * Action to add a date
     */
    private void createAddDateAction()
    {
        this.addDate = Resources.Frames.Dialog.Actions.ADD_DATE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                DateDialog dateDialog = new DateDialog(null, addDate, "", parameter.getInfoTypeID(), new DefaultConstraint(parameter.getMinValue(),
                                                                                                                           parameter.getMaxValue(),
                                                                                                                           parameter.isRequired()));
                if (dateDialog.open() == IDialogConstants.OK_ID)
                {
                    setValues(parameter, dateDialog.getTime());
                }
            }
        });
    }

    /**
     * Action to add a organization unit
     */
    private void createAddOrgUnitAction()
    {
        this.addOrgUnit = Resources.Frames.Dialog.Actions.ADD_ORG_UNIT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                ObjectArray selectableObjArray = parameter.getPossibleSelection();

                List<Organization> listOrgs = new ArrayList<Organization>();

                if (selectableObjArray != null)
                {
                    Iterator<Object> itSelectableObj = selectableObjArray.iterator();

                    while (itSelectableObj.hasNext())
                        listOrgs.add((Organization) itSelectableObj.next());
                }
                // Get and set selected objects
                ObjectArray selectedObjArray = parameter.getSelectedValues();
                List<Organization> selectedList = new ArrayList<Organization>();
                if (selectedObjArray != null)
                {
                    Iterator<Object> itSelectedObj = selectedObjArray.iterator();

                    while (itSelectedObj.hasNext())
                        selectedList.add((Organization) itSelectedObj.next());
                }

                DefaultTableModel orgaModel = new DefaultTableModel(new OrgaController().getTableModel(null), new OrgaController().getColumns());

                SelectOrganizationDialog organizationDialog = new SelectOrganizationDialog(null, addOrgUnit, "", orgaModel, selectedList,
                                                                                           new DefaultConstraint(parameter.getMinValue(), parameter
                                                                                                   .getMaxValue(), parameter.isRequired()));

                if (organizationDialog.open() == IDialogConstants.OK_ID)
                {
                    ObjectArray objectArray = new ObjectArray();
                    Iterator<Organization> itReturnValue = organizationDialog.getSelectedOrganizations().iterator();
                    while (itReturnValue.hasNext())
                        objectArray.add(itReturnValue.next());

                    parameter.setSelectedValues(objectArray);
                    outParamTable.getItem(outParamTable.getSelectedCell()[0]).setText(1, parameter.toString());
                    outParamTable.getItem(outParamTable.getSelectedCell()[0]).setData(Integer.toString(1), parameter);
                }
            }
        });

    }

    /**
     * Action to open dialog to add user.
     */
    private void createAddPersonAction()
    {
        this.addPerson = Resources.Frames.Dialog.Actions.ADD_USER.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                ObjectArray selectableObjArray = parameter.getPossibleSelection();

                List<Person> listPers = new ArrayList<Person>();

                if (selectableObjArray != null)
                {
                    Iterator<Object> itSelectableObj = selectableObjArray.iterator();

                    while (itSelectableObj.hasNext())
                        listPers.add((Person) itSelectableObj.next());
                }
                // Get and set selected objects
                ObjectArray selectedObjArray = parameter.getSelectedValues();
                List<Person> selectedList = new ArrayList<Person>();
                if (selectedObjArray != null)
                {
                    Iterator<Object> itSelectedObj = selectedObjArray.iterator();

                    while (itSelectedObj.hasNext())
                        selectedList.add((Person) itSelectedObj.next());
                }

                PersonController controller = new PersonController();
                DefaultTableModel personModel = new DefaultTableModel(controller.getTableModel(listPers), controller.getColumns());

                SelectPersonDialog personDialog = new SelectPersonDialog(null, addPerson, "", personModel, selectedList,
                                                                         new DefaultConstraint(parameter.getMinValue(), parameter.getMaxValue(),
                                                                                               parameter.isRequired()));

                if (personDialog.open() == IDialogConstants.OK_ID)
                {
                    ObjectArray objectArray = new ObjectArray();
                    Iterator<Person> itReturnValue = personDialog.getSelectedPersons().iterator();
                    while (itReturnValue.hasNext())
                        objectArray.add(itReturnValue.next());

                    parameter.setSelectedValues(objectArray);
                    outParamTable.getItem(outParamTable.getSelectedCell()[0]).setText(1, parameter.toString());
                    outParamTable.getItem(outParamTable.getSelectedCell()[0]).setData(Integer.toString(1), parameter);
                }
            }
        });
    }

    /**
     * Dialog to open a file dialog
     */
    private void createAddFileAction()
    {
        this.addFile = Resources.Frames.Dialog.Actions.ADD_FILE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                FileDialogProWim fileDialog = new FileDialogProWim(null, addFile, "");

                if (fileDialog.open() == IDialogConstants.OK_ID)
                {
                    Document document = fileDialog.getUploadedDocumnet();
                    if (MainController.getInstance().findFolderOrContent(document.getName()) == null)
                    {
                        documentHash.put(parameter.getID(), document);
                        setValues(parameter, document.getName());
                    }
                    else
                        MessageDialog.openInformation(null, Resources.Frames.Dialog.Texts.DOCUMENT_EXISTS_DIALOG_TITLE.getText(),
                                                      Resources.Frames.Global.Texts.DOCUMENT.getText() + GlobalConstants.DOUBLE_POINT + " "
                                                              + document.getName() + "\r\n\r\n"
                                                              + Resources.Frames.Dialog.Texts.DOCUMENT_EXISTS_DIALOG_DESCRIPTION.getText());

                }
            }
        });
    }

    /**
     * Dialog to open a combobox.
     */
    private void createAddComboBoxAction()
    {
        this.addComboBox = Resources.Frames.Dialog.Actions.ADD_COMBO_BOX.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) selectedTable.getItem(selectedTable.getSelectionIndex()).getData("1");

                ObjectArray possibleObjArray = parameter.getPossibleSelection();
                Iterator<Object> it = possibleObjArray.iterator();

                List<Object> list = new ArrayList<Object>();
                while (it.hasNext())
                    list.add(it.next());

                DefaultConstraint defaultConstraint = new DefaultConstraint(parameter.getMinValue(), parameter.getMaxValue(), parameter.isRequired());
                ComboBoxDialog dlg = new ComboBoxDialog(null, addComboBox, "", defaultConstraint, list, parameter.getSelectedValues());
                if (dlg.open() == ComboBoxDialog.OK)
                {
                    setValues(parameter, dlg.getSelection());
                }
            }
        });
    }

    /**
     * Action to open dialog to add a multi list
     */
    private void createAddMultiListAction()
    {
        this.addMultiList = Resources.Frames.Dialog.Actions.ADD_MULTI_LIST.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                // Get and set possible selection objects
                ObjectArray possibleObjArray = parameter.getPossibleSelection();
                Iterator<Object> it = possibleObjArray.iterator();

                List<Object> list = new ArrayList<Object>();
                while (it.hasNext())
                    list.add(it.next());

                // Get and set selected objects
                ObjectArray selectedObjArray = parameter.getSelectedValues();
                if (selectedObjArray == null)
                    selectedObjArray = new ObjectArray();
                Iterator<Object> itSelectedObj = selectedObjArray.iterator();
                List<Object> selectedList = new ArrayList<Object>();

                while (itSelectedObj.hasNext())
                    selectedList.add(itSelectedObj.next());

                MultiListDialog multiListDialog = new MultiListDialog(null, addMultiList, "", list, selectedList, new DefaultConstraint(parameter
                        .getMinValue(), parameter.getMaxValue(), parameter.isRequired()));

                if (multiListDialog.open() == IDialogConstants.OK_ID)
                {
                    ObjectArray objectArray = new ObjectArray();
                    Iterator<Object> itReturnValue = multiListDialog.getSelectedValues().iterator();
                    while (itReturnValue.hasNext())
                        objectArray.add(itReturnValue.next());

                    parameter.setSelectedValues(objectArray);
                    outParamTable.getItem(outParamTable.getSelectedCell()[0]).setText(1, parameter.toString());
                    outParamTable.getItem(outParamTable.getSelectedCell()[0]).setData(Integer.toString(1), parameter);
                }
            }
        });
    }

    /**
     * dialog to add String list.
     */
    private void createAddStrListAction()
    {
        this.addSingleList = Resources.Frames.Dialog.Actions.ADD_SINGLE_LIST.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                ObjectArray possibleObjArray = parameter.getPossibleSelection();

                SingleListDialog<Object> singleListDialog = new SingleListDialog<Object>(null, addSingleList, "",
                                                                                         new ArrayList<Object>(possibleObjArray), null,
                                                                                         new DefaultConstraint(parameter.getMinValue(), parameter
                                                                                                 .getMaxValue(), parameter.isRequired()));

                if (singleListDialog.open() == IDialogConstants.OK_ID)
                {
                    setValues(parameter, singleListDialog.getSelectedValue());
                }
            }
        });
    }

    /**
     * Create float dialog.
     */
    private void createAddFlotAction()
    {
        this.addFloat = Resources.Frames.Dialog.Actions.ADD_FLOAT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                FloatDialog floatDialog = new FloatDialog(null, addFloat, "", parameter.toString(), new DefaultConstraint(parameter.getMinValue(),
                                                                                                                          parameter.getMaxValue(),
                                                                                                                          parameter.isRequired()));

                if (floatDialog.open() == IDialogConstants.OK_ID)
                {
                    setValues(parameter, new Float(floatDialog.getFloatValue()));
                }
            }
        });
    }

    /**
     * Action to open a dialog for integer
     */
    private void createAddIntegerAction()
    {
        this.addInteger = Resources.Frames.Dialog.Actions.ADD_INTEGER.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                IntegerDialog integerDialog = new IntegerDialog(null, addInteger, "", parameter.toString(), new DefaultConstraint(parameter
                        .getMinValue(), parameter.getMaxValue(), parameter.isRequired()));

                if (integerDialog.open() == IDialogConstants.OK_ID)
                {
                    setValues(parameter, new Integer(integerDialog.getIntegerValue()));
                }
            }
        });
    }

    /**
     * Dialog for longtext
     */
    private void createAddLongTextAction()
    {
        this.addLongText = Resources.Frames.Dialog.Actions.ADD_LONG_TEXT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                LongTextDialog longTextDialog = new LongTextDialog(null, addLongText, "", parameter.toString(), new DefaultConstraint(parameter
                        .getMinValue(), parameter.getMaxValue(), parameter.isRequired()));

                if (longTextDialog.open() == IDialogConstants.OK_ID)
                {
                    setValues(parameter, longTextDialog.getText());
                }
            }
        });
    }

    /**
     * Action to open a text dialog
     */
    private void createAddTextAction()
    {
        this.addText = Resources.Frames.Dialog.Actions.ADD_TEXT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1));

                ShortTextDialog shortTextDialog = new ShortTextDialog(null, addText, "", parameter.toString(), new DefaultConstraint(parameter
                        .getMinValue(), parameter.getMaxValue(), parameter.isRequired()), true);

                if (shortTextDialog.open() == IDialogConstants.OK_ID)
                {
                    setValues(parameter, shortTextDialog.getText());
                }
            }
        });
    }

    /**
     * Action for delete value.
     */
    private void createDeleteValueAction()
    {
        this.deleteValue = Resources.Frames.Toolbar.Actions.DELETE_VALUE_TOOL_BAR.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (selectedTable.isFocusControl())
                {
                    ParameterClient parameter = (ParameterClient) selectedTable.getItem(selectedTable.getSelectionIndex())
                            .getData(Integer.toString(1));
                    setValues(parameter, "");
                }
            }
        });
    }

    /**
     * Action for delete value.
     */
    private void createOpenFileAction()
    {
        this.openFile = Resources.Frames.Toolbar.Actions.DELETE_VALUE_TOOL_BAR.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                ParameterClient parameter = (ParameterClient) inParamTable.getItem(inParamTable.getSelectionIndex()).getData(Integer.toString(1));
                OpenKnowledgeLink.open(parameter);
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
        Menu menuProp = menuMgr.createContextMenu(outParamTable);
        outParamTable.setMenu(menuProp);

    }

    /**
     * 
     * Fill context menu with actions. This actions are the same action as in toolbar.
     * 
     * @param MenuManager Included all menu items in this view
     */
    private void fillContextMenu(IMenuManager mgr)
    {
        mgr.add(selectAction(((Parameter) outParamTable.getItem(outParamTable.getSelectionIndex()).getData(Integer.toString(1))).getInfoTypeID()));

        mgr.add(deleteValue);
        mgr.add(new Separator());
    }

    /**
     * 
     * Select dialog which goes with item type
     * 
     * @param item TableItem which is selected
     */
    private void selectDialog(String item)
    {
        Validate.notNull(item);
        if (item.equals(GlobalConstants.SHORT_TEXT))
            this.addText.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.LONG_TEXT))
            this.addLongText.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.INTEGER))
            this.addInteger.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.FLOAT))
            this.addFloat.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.SINGLE_LIST))
            this.addSingleList.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.MULTI_LIST) || item.equals(GlobalConstants.MULTI_LIST_CONTROL_FLOW))
            this.addMultiList.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.COMBO_BOX) || item.equals(GlobalConstants.COMBO_BOX_CONTROL_FLOW))
            this.addComboBox.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.DOCUMENT))
            this.addFile.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.PERSON))
            this.addPerson.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.ORGANIZATION_UNIT))
            this.addOrgUnit.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.DATE) || item.equals(GlobalConstants.TIME_STAMP))
            this.addDate.runWithEvent(new Event());
        else if (item.equals(GlobalConstants.LINK))
            this.addLink.runWithEvent(new Event());

    }

    /**
     * 
     * Select dialog which goes with item type
     * 
     * @param item TableItem which is selected
     */
    private Action selectAction(String item)
    {
        Validate.notNull(item);
        if (item.equals(GlobalConstants.SHORT_TEXT))
            return this.addText;
        else if (item.equals(GlobalConstants.LONG_TEXT))
            return this.addLongText;
        else if (item.equals(GlobalConstants.INTEGER))
            return this.addInteger;
        else if (item.equals(GlobalConstants.FLOAT))
            return this.addFloat;
        else if (item.equals(GlobalConstants.SINGLE_LIST))
            return this.addSingleList;
        else if (item.equals(GlobalConstants.MULTI_LIST) || item.equals(GlobalConstants.MULTI_LIST_CONTROL_FLOW))
            return this.addMultiList;
        else if (item.equals(GlobalConstants.COMBO_BOX) || item.equals(GlobalConstants.COMBO_BOX_CONTROL_FLOW))
            return this.addComboBox;
        else if (item.equals(GlobalConstants.DOCUMENT))
            return this.addFile;
        else if (item.equals(GlobalConstants.PERSON))
            return this.addPerson;
        else if (item.equals(GlobalConstants.ORGANIZATION_UNIT))
            return this.addOrgUnit;
        else if (item.equals(GlobalConstants.DATE) || item.equals(GlobalConstants.TIME_STAMP))
            return this.addDate;
        else if (item.equals(GlobalConstants.LINK))
            return this.addLink;
        else
            return null;

    }

    /**
     * 
     * Description.
     * 
     * @param infoTypeID
     */
    private void openDialog(String infoTypeID)
    {
        Validate.notNull(infoTypeID);
        // if (infoTypeID.equals(GlobalConstants.SHORT_TEXT))
        // return this.addText;
        // else if (infoTypeID.equals(GlobalConstants.LONG_TEXT))
        // return this.addLongText;
        // else if (infoTypeID.equals(GlobalConstants.INTEGER))
        // return this.addInteger;
        // else if (infoTypeID.equals(GlobalConstants.FLOAT))
        // return this.addFloat;
        // else if (infoTypeID.equals(GlobalConstants.SINGLE_LIST))
        // return this.addSingleList;
        // else if (infoTypeID.equals(GlobalConstants.MULTI_LIST) || infoTypeID.equals(GlobalConstants.MULTI_LIST_CONTROL_FLOW))
        // return this.addMultiList;
        // else if (infoTypeID.equals(GlobalConstants.COMBO_BOX) || infoTypeID.equals(GlobalConstants.COMBO_BOX_CONTROL_FLOW))
        // return this.addComboBox;
        if (infoTypeID.equals(GlobalConstants.DOCUMENT) || infoTypeID.equals(GlobalConstants.LINK))
            this.openFile.runWithEvent(new Event());
        // else if (infoTypeID.equals(GlobalConstants.PERSON))
        // return this.addPerson;
        // else if (infoTypeID.equals(GlobalConstants.ORGANIZATION_UNIT))
        // return this.addOrgUnit;
        // else if (infoTypeID.equals(GlobalConstants.DATE) || infoTypeID.equals(GlobalConstants.TIME_STAMP))
        // return this.addDate;
        // else if (infoTypeID.equals(GlobalConstants.LINK))
        // return this.addLink;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.ACTIVITY);
    }

}
