/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-13 10:35:58 +0200 (Mo, 13 Sep 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/view/DefaultView.java $
 * $LastChangedRevision: 4787 $
 *------------------------------------------------------------------------------
 * (c) 08.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view;

import java.util.Collection;
import java.util.EnumSet;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.swt.widgets.UpdateDialogSynchronizer;
import org.prowim.portal.update.UpdateEvent;
import org.prowim.portal.update.UpdateNotificationCollection;
import org.prowim.portal.update.UpdateRegistry;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.rap.framework.toolbar.ToolBarProvider;

import de.ebcot.tools.logging.Logger;


/**
 * This default view should help us to create standards windows for this application. You can extend this view from your view and re-implement the necessary algorithms. By default create this class a toolbar, a container for one inner view and action for Help.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4787 $
 */
public class DefaultView extends ViewPart
{

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DefaultView.class);

    /**
     * Toolbar of each views
     */
    protected ToolBarProvider   toolbar;

    /**
     * Help action, necessary for each view
     */
    protected Action            helpToolBar;

    /**
     * Sachform, which parting or divide the view
     */
    protected SashForm          sashForm;

    // private Composite leftContainer;
    /** Two composite of the default view. This part the view in left and right site */
    protected Composite         leftContainer, rightContainer;

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(final Composite parent)
    {
        // Set layout of view
        Composite top = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.marginLeft = 0;
        top.setLayout(layout);

        // Create all actions for this view
        createActions();

        // Create toolbar for executable process view
        createToolbar(top);

        // Create tree for executable processes
        createContent(top);

        // Create contextMenu for this view
        createContextMenu();

        registerUpdateListener(parent);
    }

    /**
     * 
     * Register the update listener.
     * 
     * @param parent parent {@link Composite}.
     */
    protected void registerUpdateListener(final Composite parent)
    {
        for (EntityType entityType : getUpdateListenerType())
        {
            UpdateRegistry.getInstance().addListener(entityType, this, parent.getDisplay());
        }

    }

    /**
     * This algorithms give you a start point of the main view. The window is divided in two parts, left and right.
     * 
     * @param parent parent of inner composite. can not be null
     */
    protected void createContent(Composite parent)
    {
        Validate.notNull(parent);
        sashForm = new SashForm(parent, SWT.HORIZONTAL);
        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Create left container
        leftContainer = new Composite(sashForm, SWT.NONE | SWT.H_SCROLL | SWT.V_SCROLL);
        leftContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        leftContainer.setLayout(new GridLayout(1, false));

        // Create left container
        rightContainer = new Composite(sashForm, SWT.NONE);
        rightContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        rightContainer.setLayout(new GridLayout(1, false));

        sashForm.setMaximizedControl(null);
    }

    /**
     * Toolbar for this view. Image of background and Help-Item are setting by default
     * 
     * @param parent parent of inner composite. can not be null
     */
    protected void createToolbar(Composite parent)
    {
        Validate.notNull(parent);
        toolbar = new ToolBarProvider(parent);
        toolbar.setToolBarBackgroundImage(Resources.Frames.Toolbar.Images.IMAGE_TOLLBAR_BACKGROUND.getImage());
        // TODO : activate this if the help is enabled
        // this.helpToolBar.setEnabled(false);
        // toolbar.addToolBarItem(this.helpToolBar);

    }

    /**
     * Create actions for this view. Action of help is created by default
     */
    protected void createActions()
    {
        // Help toolbar
        this.helpToolBar = Resources.Frames.Toolbar.Actions.HELP_TOOL_BAR.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                System.out.println("Hilfe");
            }
        });

    }

    /**
     * 
     * Create context menu. You have to overwrite this method for you application. No Context menu will created default.
     */
    protected void createContextMenu()
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose()
    {
        UpdateRegistry.getInstance().removeListener(this);
        super.dispose();
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

    /**
     * 
     * Handle a {@link Collection} of {@link UpdateEvent} of one of the registered {@link EntityType}s.<br/>
     * 
     * Make sure this method is only called within the UI-thread an when {@link Display#getCurrent()} is the one the instance of this {@link DefaultView} was initialized with. Else, updates would be sent to any client.<br/>
     * In fact, better never call this method. It is purely intended to be called by {@link UpdateRegistry#beforePhase(org.eclipse.rwt.lifecycle.PhaseEvent)}
     * 
     * @param updates containing all updated ids for one {@link EntityType}
     */
    public void handleUpdateEvents(final UpdateNotificationCollection updates)
    {

        final UpdateDialogSynchronizer synchronizer = UpdateDialogSynchronizer.getInstance();

        if (updates.isContainingForeignEvents() && synchronizer.getShowUpdateDialogRights(this))
        {

            try
            {
                Display.getCurrent().asyncExec(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // try
                        // {
                        // TODO Die Meldung wurde auf Wünsch erstmal rasugenommen. Wenn man aktiv in Portal arbeitet kommt die Meldung jede Sekunde.
                        // MessageDialog.openInformation(null, Resources.Frames.Global.Texts.UPDATE_DIALOG_TITLE.getText(),
                        // Resources.Frames.Global.Texts.UPDATE_DIALOG_MESSAGE.getText());
                        // }
                        // finally
                        // {
                        synchronizer.returnShowUpdateDialogRights(DefaultView.this);
                        // }
                    }
                });
            }
            catch (RuntimeException e)
            {
                // Make sure resource is free, if asyncExec fails
                synchronizer.returnShowUpdateDialogRights(DefaultView.this);
                throw e;
            }
        }
    }

    /**
     * 
     * getUpdateListenerType
     * 
     * @return List of {@link EntityType}.
     */
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.noneOf(EntityType.class);
    }
}
