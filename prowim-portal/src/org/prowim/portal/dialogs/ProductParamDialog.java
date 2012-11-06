/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 27.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.Product;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.ProductParamTableViewer;
import org.prowim.rap.framework.components.table.DefaultTableViewer;



/**
 * Show a list of {@link ProcessInformation}s which are in given {@link Product}. User can change the name and the type of {@link ProcessInformation}.
 * 
 * {@link ProcessInformation} can be from type
 * 
 * 
 * <li>Document</li> <li>link</li> <li>short text</li> <li>long text</li>
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class ProductParamDialog extends DefaultDialog
{

    private DefaultTableViewer       mainTable;
    private Button                   addBtn;
    private Button                   deleteBtn;
    private final String             productID;
    private ScrolledComposite        scrollComp;
    private List<ProcessInformation> processInformations = new ArrayList<ProcessInformation>();

    /**
     * Constructor.
     * 
     * @see ProductParamDialog
     * 
     * @param parentShell the parent shell
     * @param action the action. Not null.
     * @param description the description. not null.
     * @param productID not null productID. Not null
     */
    public ProductParamDialog(Shell parentShell, Action action, String description, final String productID)
    {
        super(parentShell, action, description);
        Validate.notNull(productID);
        this.productID = productID;
    }

    /**
     * 
     * create and fill result table with data
     * 
     * @param searchResult the not null search result composite,
     */
    private DefaultTableViewer createSouthTable(final ScrolledComposite searchResult)
    {
        return new ProductParamTableViewer(searchResult, SWT.SINGLE);
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
        // the main composite.
        final Control control = super.createCustomArea(parent);
        scrollComp = new ScrolledComposite((Composite) control, SWT.NONE);
        scrollComp.setLayout(new GridLayout());
        scrollComp.setExpandHorizontal(true);
        scrollComp.setExpandVertical(true);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gridData.heightHint = 120;
        scrollComp.setLayoutData(gridData);

        mainTable = createSouthTable(scrollComp);
        mainTable.setInput(convertToProcessInformationList(MainController.getInstance().getProductsOfActivity(productID)));
        mainTable.getTable().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                deleteBtn.setEnabled(true);
            }
        });

        scrollComp.setContent(mainTable.getTable());
        return (Composite) control;
    }

    /**
     * Converts an {@link ObjectArray } that contains {@link ProcessInformation } objects into a {@link List <ProcessInformation> }.
     * 
     * @param processInformations the not null {@link ProcessInformation } {@link ObjectArray }.
     */
    private List<ProcessInformation> convertToProcessInformationList(final ObjectArray processInformations)
    {
        Validate.notNull(processInformations);
        final List<ProcessInformation> result = new ArrayList<ProcessInformation>();
        Iterator<Object> it = processInformations.iterator();
        while (it.hasNext())
        {
            result.add((ProcessInformation) it.next());
        }
        return result;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(final Composite parent)
    {
        addBtn = createButton(parent, 123, Resources.Frames.Global.Texts.ADD.getText(), true);
        deleteBtn = createButton(parent, 123, Resources.Frames.Global.Texts.DELETE.getText(), true);

        Button dummy = createButton(parent, 123, Resources.Frames.Global.Texts.ADD.getText(), true);
        dummy.setEnabled(false);
        dummy.setVisible(false);

        Button dummy2 = createButton(parent, 123, Resources.Frames.Global.Texts.ADD.getText(), true);
        dummy2.setEnabled(false);
        dummy2.setVisible(false);

        deleteBtn.setEnabled(false);
        createButton(parent, IDialogConstants.OK_ID, Resources.Frames.Global.Texts.OK.getText(), true);

        if (deleteBtn != null)
        {
            deleteBtn.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    if (mainTable.getTable().getSelectionCount() > 0)
                    {
                        TableItem selectedItem = mainTable.getTable().getItem(mainTable.getTable().getSelectionIndex());

                        if (selectedItem != null)
                        {
                            ProcessInformation pi = (ProcessInformation) selectedItem.getData();
                            MainController.getInstance().deleteObject(pi.getID());
                            mainTable.setInput(convertToProcessInformationList(MainController.getInstance().getProductsOfActivity(productID)));
                        }
                    }
                }
            });

        }
        if (addBtn != null)
            addBtn.addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    MainController.getInstance().addProcessInformationToProduct(productID);
                    mainTable.setInput(convertToProcessInformationList(MainController.getInstance().getProductsOfActivity(productID)));
                    // set the table editable
                    mainTable.getTable().redraw();
                    mainTable.refresh();
                    mainTable.getTable().select(mainTable.getTable().getItemCount() - 1);
                    setEnableDeleteButton();
                }
            });
    }

    /**
     * Sets the delete button enabled.
     */
    private void setEnableDeleteButton()
    {
        this.deleteBtn.setEnabled((mainTable.getTable().getSelectionCount() > 0));
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
        mainTable.getTable().forceFocus();
        processInformations = (List<ProcessInformation>) mainTable.getInput();
        setReturnCode(OK);
        close();
    }

    /**
     * Returns a list of {@link ProcessInformation}s, which are created or edited in this dialog.
     * 
     * @return Non null {@link List} of {@link ProcessInformation}s. If no item exists, list is empty.
     */
    public List<ProcessInformation> getProcessInformations()
    {
        return this.processInformations;
    }
}
