/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 03.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.process;

import java.util.EnumSet;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.model.ProcessLeaf;
import org.prowim.portal.tables.DefaultTable;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.view.DefaultView;



/**
 * Shows by simple click of a process name. It shows actual activities of this process templates and description of process templates
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ProcessInfoView extends DefaultView
{
    /** ID of view */
    public static final String ID = ProcessInfoView.class.getName();
    private String             processName;
    private String             processID;
    private String             processDesc;
    private DefaultTableModel  activeProcessModel;

    private DefaultTable       activeProcTable;

    /**
     * Constructor became a object of processmodel. This will set process name, process ID and process description
     * 
     * @param processModel ProcessModel
     * @param activeProcessModel Model to create table
     * 
     */
    public ProcessInfoView(ProcessLeaf processModel, DefaultTableModel activeProcessModel)
    {
        setProcessName(processModel.getName());
        setProcessID(processModel.getID());
        setProcessDesc(processModel.getProcess().getDescription());
        setActiveProcessModel(activeProcessModel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        Group rightGroup = new Group(parent, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        rightGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        rightGroup.setLayout(new GridLayout(1, false));
        rightGroup.setText(Resources.Frames.Process.Texts.SELECTIVE_PROCESS.getText());

        // Create a group for the frame
        final Group activeProcGroup = new Group(rightGroup, SWT.SHADOW_OUT | SWT.TOP | SWT.V_SCROLL | SWT.H_SCROLL);
        activeProcGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        activeProcGroup.setLayout(new GridLayout(1, false));
        activeProcGroup.setText(Resources.Frames.Process.Actions.PROCESS_ACTIVE_NAV.getText() + " " + Resources.Frames.Global.Texts.OF.getText()
                + " " + processName);

        ScrolledComposite scrollComp = new ScrolledComposite(activeProcGroup, SWT.NONE);
        scrollComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        scrollComp.setExpandHorizontal(true);

        // Create table
        activeProcTable = new DefaultTable(scrollComp, getActiveProcessModel(), SWT.SINGLE);
        activeProcTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        activeProcTable.sortAtColumn(0);
        activeProcTable.setLinesVisible(true);
        activeProcTable.setSize(400, 300);
        activeProcTable.redraw();

        scrollComp.setContent(activeProcTable);
        scrollComp.pack();

        // Create a group for the frame
        final Group infoGroup = new Group(rightGroup, SWT.SHADOW_OUT | SWT.TOP);
        infoGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        infoGroup.setLayout(new GridLayout(1, false));
        infoGroup.setText(Resources.Frames.Process.Texts.DESCRIPTION_OF_PROCESS.getText());

        Text infoText = new Text(infoGroup, SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.WRAP);
        infoText.setText(this.processDesc);
        infoText.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label infoLabel = new Label(infoGroup, SWT.NONE);
        infoLabel.setText(Resources.Frames.Process.Texts.DESCRIPTION_FOR_PROCESS_START.getText());

    }

    /**
     * getProcessName
     * 
     * @return the processName
     */
    public String getProcessName()
    {
        return processName;
    }

    /**
     * 
     * Description.
     * 
     * @param name name of process
     */
    public void setProcessName(String name)
    {
        Validate.notNull(name);
        this.processName = name;
    }

    /**
     * getProcessID
     * 
     * @return the processID
     */
    public String getProcessID()
    {
        return processID;
    }

    /**
     * 
     * Description.
     * 
     * @param id ID of process
     */
    public void setProcessID(String id)
    {
        Validate.notNull(id);
        this.processID = id;
    }

    /**
     * getProcessDesc
     * 
     * @return the processDesc
     */
    public String getProcessDesc()
    {
        return processDesc;
    }

    /**
     * 
     * Description.
     * 
     * @param desc description of process
     */
    public void setProcessDesc(String desc)
    {
        Validate.notNull(desc);
        this.processDesc = desc;
    }

    /**
     * setActiveProcessModel
     * 
     * @param activeProcessModel the activeProcessModel to set
     */
    public void setActiveProcessModel(DefaultTableModel activeProcessModel)
    {
        this.activeProcessModel = activeProcessModel;
    }

    /**
     * getActiveProcessModel
     * 
     * @return the activeProcessModel
     */
    public DefaultTableModel getActiveProcessModel()
    {
        return activeProcessModel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.PROCESS);
    }

}
