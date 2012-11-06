/*==============================================================================
 * File $Id: MenuController.java 1581 2009-05-26 07:56:54Z khodaei $
 * Project: ebcot.rap.framework
 *
 * $LastChangedDate: 2009-05-26 09:56:54 +0200 (Di, 26 Mai 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/menu/MenuController.java $
 * $LastChangedRevision: 1581 $
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
package org.prowim.rap.framework.menu;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * MenuController is one interface for MenuProvider. For use the MenuBar, the developer has to create one instances of this class. You get than one MenuItem. You can add the MenuItems more subMenuItems and so on.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 1581 $
 */
public class MenuController
{
    private MenuProvider              menuProvider = null;
    private Menu                      menu         = null;
    private MenuItemSelectionListener listener     = null;

    /**
     * 
     * Constructor to create a Menu or Navigation Bar
     * 
     * @param parent container, where the MenuBar will be insert
     */
    public MenuController(Composite parent)
    {
        this.menuProvider = new MenuProvider(parent);
        this.menu = new Menu();
        this.menuProvider.setMenu(menu);
    }

    /**
     * 
     * Interface to create one MenuItem.
     * 
     * @param action Action of current Menu
     * @param style SWT.PUSH | SWT.FLAT
     * @return MenuItem
     */
    public MenuItem addMenuItem(final Action action, final int style)
    {
        MenuItem menuItem = new MenuItem(this.menuProvider.getMenuComposite(), action.getText(), action.getToolTipText(), action.getImageDescriptor()
                .createImage(), style);
        this.listener = new MenuItemSelectionListener(menuItem, this.menuProvider);
        menuItem.addSelectionListener(listener);
        menuItem.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                action.runWithEvent(event);
            }
        });

        this.menu.add(menuItem);
        return menuItem;
    }

    /**
     * 
     * Description.
     * 
     * @return MenuItemSelectionListener
     */
    public MenuItemSelectionListener getMenuItemListener()
    {
        return this.listener;
    }
}
