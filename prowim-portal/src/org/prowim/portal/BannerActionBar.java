/*==============================================================================
 * File $Id: BannerActionBar.java 5031 2011-02-02 15:44:35Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:35 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/BannerActionBar.java $
 * $LastChangedRevision: 5031 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.action.LogoutAction;
import org.prowim.portal.dialogs.AboutDialog;
import org.prowim.portal.dialogs.PersonDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.model.PersonLeaf;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.knowledge.KnowledgeSearchView;
import org.prowim.rap.framework.resource.FontManager;



/**
 * 
 * This class create the logout, configuration and search functions in header.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5031 $
 */
public class BannerActionBar extends Composite
{
    private final static Logger LOG = Logger.getLogger(BannerActionBar.class);

    private Text                searchTxt;
    private Action              aboutAction;

    private Action              changePSWAction;

    /**
     * 
     * Create and configure the action in header. These are logout, configuration and search functions
     * 
     * @param parent Composite
     */
    public BannerActionBar(final Composite parent)
    {
        super(parent, SWT.NONE);

        createAction();
        // Logout button
        final Action logoutAction = Resources.Frames.Header.Actions.LOGOUT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new LogoutAction(window).run();
            }
        });

        Button logoutBtn = new Button(parent, SWT.NO_TRIM | SWT.TRANSPARENCY_MASK);
        logoutBtn.setImage(logoutAction.getImageDescriptor().createImage());
        logoutBtn.setBackground(Graphics.getColor(238, 238, 238));
        logoutBtn.setToolTipText(logoutAction.getToolTipText());
        logoutBtn.setForeground(parent.getForeground());
        logoutBtn.pack();
        FormData fdLogoutBtn = new FormData();
        logoutBtn.setLayoutData(fdLogoutBtn);
        fdLogoutBtn.top = new FormAttachment(0, 6);
        fdLogoutBtn.right = new FormAttachment(100, 0);
        fdLogoutBtn.height = 24;
        fdLogoutBtn.width = 24;
        logoutBtn.addListener(SWT.Selection, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                logoutAction.runWithEvent(event);
            }
        });

        // Configuration button

        Button changePSWBtn = new Button(parent, SWT.NONE);
        changePSWBtn.setImage(changePSWAction.getImageDescriptor().createImage());
        changePSWBtn.setBackground(Graphics.getColor(238, 238, 238));
        changePSWBtn.setToolTipText(changePSWAction.getToolTipText());
        changePSWBtn.setForeground(parent.getForeground());
        changePSWBtn.pack();
        FormData fdChangeBtn = new FormData();
        changePSWBtn.setLayoutData(fdChangeBtn);
        fdChangeBtn.top = new FormAttachment(0, 6);
        fdChangeBtn.right = new FormAttachment(logoutBtn, -5);
        fdChangeBtn.height = 24;
        fdChangeBtn.width = 24;
        changePSWBtn.addListener(SWT.Selection, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                changePSWAction.runWithEvent(event);
            }
        });

        Button aboutBtn = new Button(parent, SWT.NO_TRIM | SWT.TRANSPARENCY_MASK);
        aboutBtn.setImage(aboutAction.getImageDescriptor().createImage());
        aboutBtn.setBackground(Graphics.getColor(238, 238, 238));
        aboutBtn.setToolTipText(aboutAction.getToolTipText());
        aboutBtn.setForeground(parent.getForeground());
        aboutBtn.pack();
        FormData fdaboutBtn = new FormData();
        aboutBtn.setLayoutData(fdaboutBtn);
        fdaboutBtn.top = new FormAttachment(0, 6);
        fdaboutBtn.right = new FormAttachment(changePSWBtn, -1);
        fdaboutBtn.height = 24;
        fdaboutBtn.width = 24;
        aboutBtn.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                aboutAction.runWithEvent(event);
            }
        });

        // Create Search fields
        createSearchField(parent, aboutBtn);

    }

    /**
     * 
     * Create the search field and belonging actions
     * 
     * @param parent Composite to insert elements
     * @param neighbor the neighbor button of this. This is necessary to position this elements
     */
    private void createSearchField(Composite parent, Button neighbor)
    {
        Label lebel = new Label(parent, SWT.NONE);
        lebel.setText(Resources.Frames.Global.Texts.SEARCH_KEY_WORD.getText() + GlobalConstants.DOUBLE_POINT);
        FormData fdsearchLabel = new FormData();
        lebel.setLayoutData(fdsearchLabel);
        fdsearchLabel.top = new FormAttachment(0, 13);
        fdsearchLabel.right = new FormAttachment(neighbor, -210);
        fdsearchLabel.height = 15;

        // Create Search-Composite
        Composite searchComp = new Composite(parent, SWT.NO_TRIM | SWT.CENTER);
        FormData fdSearch = new FormData();
        searchComp.setLayoutData(fdSearch);
        searchComp.setBackground(Graphics.getColor(250, 250, 250));
        fdSearch.top = new FormAttachment(0, 7);
        fdSearch.height = 24;
        fdSearch.width = 178;
        fdSearch.right = new FormAttachment(neighbor, -20);
        searchComp.setLayout(new FormLayout());
        searchComp.setBackgroundMode(SWT.INHERIT_FORCE);

        // Search Text
        searchTxt = new Text(searchComp, SWT.LEFT | SWT.BORDER);
        searchTxt.setFont(FontManager.FONT_VERDANA_12_NORMAL);
        searchTxt.pack();
        FormData fdsearchTxt = new FormData();
        searchTxt.setLayoutData(fdsearchTxt);
        fdsearchTxt.top = new FormAttachment(0, 2);
        fdsearchTxt.height = 24;
        fdsearchTxt.width = 150;

        searchTxt.addKeyListener(new KeyListener()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.keyCode == 13)
                    startSearch();
            }
        });

        // Create Search-Button
        final Action searchAction = Resources.Frames.Header.Actions.SEARCH.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                startSearch();
            }
        });

        Button searchBtn = new Button(searchComp, SWT.NONE);
        searchBtn.setImage(searchAction.getImageDescriptor().createImage());
        searchBtn.setBackground(Graphics.getColor(250, 250, 250));
        searchBtn.setToolTipText(searchAction.getToolTipText());
        searchBtn.setForeground(parent.getForeground());
        searchBtn.pack();
        FormData fdSearchBtn = new FormData();
        searchBtn.setLayoutData(fdSearchBtn);
        fdSearchBtn.top = new FormAttachment(0, 0);
        fdSearchBtn.left = new FormAttachment(searchTxt, 0);
        fdSearchBtn.height = 24;
        fdSearchBtn.width = 24;
        searchBtn.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                searchAction.runWithEvent(event);
            }
        });
    }

    /**
     * Start search view
     */
    protected void startSearch()
    {
        try
        {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IViewPart viewPart = window.getActivePage().showView(KnowledgeSearchView.ID, Integer.toString(0), IWorkbenchPage.VIEW_ACTIVATE);
            KnowledgeSearchView knowSearchView = (KnowledgeSearchView) viewPart;
            knowSearchView.goSearch(searchTxt.getText());

        }
        catch (PartInitException e)
        {
            LOG.error("Error by open view: ", e);
        }
    }

    // Create actions for view
    private void createAction()
    {
        changePSWAction = Resources.Frames.Header.Actions.CHANGE_PASSWORD.getAction(new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                Person actuallPerson = LoggedInUserInfo.getInstance().getPerson();
                if (actuallPerson != null)
                {
                    PersonDialog personDilaog = new PersonDialog(null, Resources.Frames.Dialog.Actions.EDIT_USER.getAction(),
                                                                 Resources.Frames.Dialog.Texts.DESC_USER_CREATE_EDIT.getText(),
                                                                 new PersonLeaf(actuallPerson), true);

                    if (personDilaog.open() == IDialogConstants.OK_ID)
                    {
                        Person person = personDilaog.getPerson();
                        MainController.getInstance().updateUserInfo(person);
                    }

                }

            }
        });

        // About button
        aboutAction = Resources.Frames.Header.Actions.ABAOUT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                AboutDialog aboutDialog = new AboutDialog(null, aboutAction, "");
                aboutDialog.open();
            }
        });

    }
}
