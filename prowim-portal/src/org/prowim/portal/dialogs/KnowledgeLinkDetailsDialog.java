/*==============================================================================
 * File $Id: KnowledgeLinkDetailsDialog.java 5083 2011-05-10 12:14:31Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2011-05-10 14:14:31 +0200 (Di, 10 Mai 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/KnowledgeLinkDetailsDialog.java $
 * $LastChangedRevision: 5083 $
 *------------------------------------------------------------------------------
 * (c) 01.04.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.dms.VersionHistory;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.dialog.DocumentVersionController;
import org.prowim.portal.dialogs.knowledge.KnowledgeLinkTemp;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.DefaultTable;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.resource.FontManager;



/**
 * A dialog shows details and version history to a knowledgelink.
 * 
 * @author Saad Wardi
 * @version $Revision: 5083 $
 * @since 2.0
 */
public class KnowledgeLinkDetailsDialog extends DefaultDialog
{
    private KnowledgeLinkTemp currentKnowledgeLink = null;
    private final String      knowledgeObjectOwner;

    private DefaultTable      assignedRolesTable   = null;
    private List<Version>     versionsList         = new ArrayList<Version>();
    private ScrolledComposite scrollComp;

    /**
     * Constructs this.
     * 
     * @param parentShell the parent shell.
     * @param action the action.
     * @param description see {@link DefaultDialog}
     * @param link the current knowledgelink
     * @param knowledgeobjectOwner the knowledgeobject owner is the owner of the knowledgelink, may not be null
     */
    public KnowledgeLinkDetailsDialog(Shell parentShell, Action action, String description, final KnowledgeLinkTemp link,
            final String knowledgeobjectOwner)
    {
        super(parentShell, action, description);
        currentKnowledgeLink = link;
        knowledgeObjectOwner = knowledgeobjectOwner;

        init();
    }

    /**
     * Initialize data.
     */
    private void init()
    {
        if (currentKnowledgeLink.getRepositoryName().contains(GlobalConstants.PROWIM_DMS))
        {
            if (currentKnowledgeLink.getHyperlink() != null && !currentKnowledgeLink.getHyperlink().equals(""))
            {
                VersionHistory versions = MainController.getInstance().getKnowledgeLinkVersionHistory(currentKnowledgeLink.getID());
                if (versions != null)
                {
                    versionsList = versions.getVersions();
                }
            }
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
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Composite createCustomArea(Composite parent)
    {
        Composite control = super.createCustomArea(parent);

        // header **********************************************************************************

        final Group innerGroup = new Group(control, SWT.SHADOW_IN | SWT.RIGHT);
        innerGroup.setText(Resources.Frames.Global.Texts.GENERAL.getText());
        innerGroup.setFont(FontManager.DIALOG_HEADER);
        innerGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        innerGroup.setLayout(new GridLayout(2, false));

        Label labelKnowledgeLinkNameLabel = new Label(innerGroup, SWT.LEFT);
        Label labelKnowledgeLinkName = new Label(innerGroup, SWT.LEFT);
        labelKnowledgeLinkNameLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        labelKnowledgeLinkNameLabel.setText(Resources.Frames.Global.Texts.NAME.getText() + GlobalConstants.DOUBLE_POINT);
        labelKnowledgeLinkName.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        labelKnowledgeLinkName.setText(currentKnowledgeLink.getName());

        String username = "";
        if ((knowledgeObjectOwner != null) && !knowledgeObjectOwner.equals(""))
        {
            Person user = MainController.getInstance().getUserWithID(knowledgeObjectOwner);
            if (user != null)
            {
                username = user.toString();
            }
        }
        Label labelCreatedByLabel = new Label(innerGroup, SWT.LEFT);
        Label labelCreatedBy = new Label(innerGroup, SWT.LEFT);
        labelCreatedByLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        labelCreatedBy.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        labelCreatedByLabel.setText(Resources.Frames.Global.Texts.CREATED_FROM.getText() + GlobalConstants.DOUBLE_POINT);
        labelCreatedBy.setText(username);

        Label labelCreatedAtLabel = new Label(innerGroup, SWT.LEFT);
        Label labelCreatedAt = new Label(innerGroup, SWT.LEFT);
        labelCreatedAtLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        labelCreatedAt.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        labelCreatedAtLabel.setText(Resources.Frames.Global.Texts.CREATED_AT.getText() + GlobalConstants.DOUBLE_POINT);
        labelCreatedAt.setText(currentKnowledgeLink.getCreateTime());

        Label labelDocumentNameLabel = new Label(innerGroup, SWT.LEFT);
        Label labelDocumentName = new Label(innerGroup, SWT.LEFT);
        labelDocumentNameLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        labelDocumentName.setLayoutData(new GridData(GridData.BEGINNING, SWT.CENTER, false, false));
        labelDocumentNameLabel.setText(Resources.Frames.Global.Texts.DOCUMENT.getText() + GlobalConstants.DOUBLE_POINT);
        labelDocumentName.setText(currentKnowledgeLink.getHyperlink());

        if (currentKnowledgeLink.getRepositoryName().contains(GlobalConstants.PROWIM_DMS))
        {
            final Group composite3 = new Group(control, SWT.SHADOW_IN | SWT.RIGHT);
            composite3.setFont(FontManager.DIALOG_HEADER);
            composite3.setText(Resources.Frames.Global.Texts.VERSION_HISTORY.getText());
            composite3.setLayoutData(new GridData(GridData.FILL_BOTH));
            composite3.setLayout(new GridLayout(1, false));
            scrollComp = new ScrolledComposite(composite3, SWT.H_SCROLL | SWT.V_SCROLL);
            scrollComp.setLayoutData(new GridData(GridData.FILL_BOTH));
            scrollComp.setLayout(new GridLayout());
            scrollComp.setExpandHorizontal(true);
            scrollComp.setExpandVertical(true);
            if (versionsList == null || versionsList.size() == 0)
            {
                composite3.setText(Resources.Frames.Global.Texts.NODOCUMENT.getText());
            }
            else
            {
                createLeftTable();
            }
        }
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

    private void createLeftTable()
    {
        DocumentVersionController controller = new DocumentVersionController();
        DefaultTableModel versionModel = new DefaultTableModel(controller.getTableModel(versionsList), controller.getColumns());

        assignedRolesTable = createResultTable(scrollComp);
        assignedRolesTable.setTableModel(versionModel);
        assignedRolesTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        assignedRolesTable.redraw();
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, true)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 3), SWT.DEFAULT).applyTo(assignedRolesTable);
        scrollComp.setContent(assignedRolesTable);
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

        return resultTable;
    }
}
