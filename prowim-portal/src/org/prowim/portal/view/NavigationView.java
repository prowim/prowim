/*==============================================================================
 * Project: ProWim
 *
 * $LastChangedDate: 2011-10-18 18:27:22 +0200 (Di, 18 Okt 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/view/NavigationView.java $
 * $LastChangedRevision: 5101 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view;

import org.eclipse.jface.action.Action;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.prowim.portal.LoggedInUserInfo;
import org.prowim.portal.MainController;
import org.prowim.portal.action.OpenViewAction;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.view.admin.AdminView;
import org.prowim.portal.view.document.DocumentView;
import org.prowim.portal.view.document.UploadView;
import org.prowim.portal.view.knowledge.KnowledgeSearchView;
import org.prowim.portal.view.knowledge.KnowledgeStructureView;
import org.prowim.portal.view.process.ProcessBrowserView;
import org.prowim.portal.view.process.ProcessModelEditorView;
import org.prowim.rap.framework.menu.MenuController;
import org.prowim.rap.framework.menu.MenuItem;
import org.prowim.resources.ResourcesLocator;



/**
 * main windows. Navigation bar is a instance of MenuController.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5101 $
 */
public class NavigationView extends ViewPart
{
    /** the view id. */
    public static final String ID             = NavigationView.class.getName();

    private MenuItem           menuItem       = null;
    private MenuController     menuController = null;

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.FILL);
        container.setLayout(new GridLayout(1, false));

        /** create the MenuProvider and the Menu objects. */

        menuController = new MenuController(container);

        // Create navigation menu process
        if (LoggedInUserInfo.getInstance().isCurrentUserAdmin() || LoggedInUserInfo.getInstance().isCurrentUserModeler())
            createProcessNav();

        // Create navigation menu Document.
        createDocumentNav();

        // Create navigation menu knowledge.
        createKnowledgeNav();

        if (LoggedInUserInfo.getInstance().isCurrentUserAdmin())
            createUserAdminNav();
    }

    /**
     * 
     * Create navigation menu process
     */
    private void createProcessNav()
    {
        // Create navigation menu Process.
        // Action processNav = Resources.Frames.Navigation.Actions.PROCESS_NAV.getAction();

        Action processModellerNav = Resources.Frames.Process.Actions.PROCESS_MODELLER_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new OpenViewAction(window, ProcessModelEditorView.ID).run();
            }
        });

        menuItem = menuController.addMenuItem(processModellerNav, SWT.PUSH);

        menuItem.setSelectedBackgroundImage(Resources.Frames.Navigation.Images.MENU_SELECTED_BGD.getImage());
        menuItem.setDefaultBackgroundImage(Resources.Frames.Navigation.Images.MENU_SHADOW_SUB_BGD.getImage());
        menuItem.setBackgroundImage(Resources.Frames.Navigation.Images.MENU_SHADOW_SUB_BGD.getImage());
    }

    /**
     * This part will be use by implementation of document navigation. Don´t delete this. <br>
     * Create navigation menu document
     */
    private void createDocumentNav()
    {
        Action documentNav = Resources.Frames.Navigation.Actions.DOCUMENT_NAV.getAction();

        menuItem = menuController.addMenuItem(documentNav, SWT.PUSH);

        menuItem.setSelectedBackgroundImage(Resources.Frames.Navigation.Images.MENU_SELECTED_BGD.getImage());
        menuItem.setDefaultBackgroundImage(Resources.Frames.Navigation.Images.MENU_SHADOW_SUB_BGD.getImage());
        menuItem.setBackgroundImage(Resources.Frames.Navigation.Images.MENU_SHADOW_SUB_BGD.getImage());

        // Search for documents subMenu
        Action showExistingDocuments = Resources.Frames.Navigation.Actions.EXISTING_DOCUMENTS_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new OpenViewAction(window, DocumentView.ID).run();
            }
        });

        menuItem.addSubMenuItem(showExistingDocuments);

        // Upload documents subMenu
        Action documentUploadNav = Resources.Frames.Dialog.Actions.ADD_FILE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new OpenViewAction(window, UploadView.ID).run();
            }
        });

        menuItem.addSubMenuItem(documentUploadNav);

        // Upload documents subMenu
        Action documentMultipleUploadNav2 = Resources.Frames.Dialog.Actions.ADD_FILE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                // This menu is only temporary and it would not be shows in portal.
                // This should disable the design problem in RAP, that the last button is alway broken
            }
        });

        menuItem.addSubMenuItem(documentMultipleUploadNav2);
        menuItem.setDefaultSubMenu(showExistingDocuments.getId());
    }

    /**
     * 
     * Create navigation menu knowledge
     */
    private void createKnowledgeNav()
    {
        Action knowledgeNav = Resources.Frames.Navigation.Actions.KNOWLEDGE_NAV.getAction();

        menuItem = menuController.addMenuItem(knowledgeNav, SWT.PUSH);

        menuItem.setSelectedBackgroundImage(Resources.Frames.Navigation.Images.MENU_SELECTED_BGD.getImage());
        menuItem.setDefaultBackgroundImage(Resources.Frames.Navigation.Images.MENU_SHADOW_SUB_BGD.getImage());
        menuItem.setBackgroundImage(Resources.Frames.Navigation.Images.MENU_SHADOW_SUB_BGD.getImage());

        // Create view to show browser
        Action knowledeBrowserNav = Resources.Frames.Navigation.Actions.KNOWLEDGE_BROWSER_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new OpenViewAction(window, ProcessBrowserView.ID).run();
            }
        });

        menuItem.addSubMenuItem(knowledeBrowserNav);

        // Create view to show knowledge search windows
        Action knowledeSearchNav = Resources.Frames.Navigation.Actions.KNOWLEDGE_SEARCH_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new OpenViewAction(window, KnowledgeSearchView.ID).run();
            }
        });

        menuItem.addSubMenuItem(knowledeSearchNav);

        // Create view to show knowledge structure
        Action knowledeStructureNav = Resources.Frames.Navigation.Actions.KNOWLEDGE_STRUCTURE_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new OpenViewAction(window, KnowledgeStructureView.ID).run();
            }
        });

        menuItem.addSubMenuItem(knowledeStructureNav);

        // Process knowledge subMenu
        Action knowledeWikiNav = Resources.Frames.Navigation.Actions.KNOWLEDGE_WIKI_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String wikiurl = MainController.getInstance().getWikiURL();
                String appURL = ResourcesLocator.getServerHost();
                ExternalBrowser.open(appURL.replaceAll("-", ""), wikiurl, ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR
                        | ExternalBrowser.STATUS);
            }
        });

        menuItem.addSubMenuItem(knowledeWikiNav);

        // Process knowledge subMenu
        Action knowledeWikiNav2 = Resources.Frames.Navigation.Actions.KNOWLEDGE_WIKI_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                // This menu is only temporary and it would not be shows in portal.
                // This should disable the design problem in RAP, that the last button is always broken

            }
        });

        menuItem.addSubMenuItem(knowledeWikiNav2);
        menuItem.setDefaultSubMenu(knowledeBrowserNav.getId());
        menuItem.setSelected(menuController, true);
    }

    /**
     * 
     * Create navigation menu knowledge
     */
    private void createUserAdminNav()
    {
        // Create view to show knowledges of process and domains
        Action aboutAction = Resources.Frames.Navigation.Actions.ADMIN_NAV.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                new OpenViewAction(window, AdminView.ID).run();
            }
        });

        menuItem = menuController.addMenuItem(aboutAction, SWT.PUSH);
        menuItem.setBackgroundImage(Resources.Frames.Navigation.Images.MENU_SELECTED_BGD.getImage());
        menuItem.setSelectedBackgroundImage(Resources.Frames.Navigation.Images.MENU_SELECTED_BGD.getImage());
        menuItem.setDefaultBackgroundImage(Resources.Frames.Navigation.Images.MENU_SHADOW_SUB_BGD.getImage());

        menuItem.setSelected(menuController, false);
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
}
