/*==============================================================================
 * File $Id: MenuItem.java 3933 2010-06-17 07:35:49Z khodaei $
 * Project: ebcot.rap.framework
 *
 * $LastChangedDate: 2010-06-17 09:35:49 +0200 (Do, 17 Jun 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/menu/MenuItem.java $
 * $LastChangedRevision: 3933 $
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

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.prowim.rap.framework.resource.FontManager;



/**
 * A implementation of an item in a menu. A menu item is essentially a button sitting in a composite. When the user selects the "button", the action associated with the menu item is performed.
 * 
 * @author wardi, Maziar Khodaei
 * @version $Revision: 3933 $
 */
public class MenuItem extends Button
{
    private final LinkedList<MenuItem> subMenuItems = new LinkedList<MenuItem>();
    private final ArrayList<Action>    menuItems    = new ArrayList<Action>();

    private Image                      selectionBackgroundImage;
    private Image                      defaultBackgroundImage;
    private String                     id           = "";
    private String                     defaultSubMenu;

    /**
     * Constructor to create a menuItem with set text.
     * 
     * @param parent the parent composite
     * @param text the name of button
     * @param toolTipText ToolTipText for this button
     * @param image Image for this button
     * @param style SWT.PUSH | SWT.FLAT
     */
    public MenuItem(final Composite parent, String text, String toolTipText, Image image, final int style)
    {
        super(parent, SWT.LEFT | style);
        parent.getParent().layout();
        setText(text);
        setFont(FontManager.FONT_VERDANA_12_BOLD);
        setImage(image);
        setToolTipText(toolTipText);
        setLayoutData(new GridData(GridData.FILL_BOTH));
        pack();
    }

    /**
     * 
     * Add subMenus to one Menu.
     * 
     * @param parent Co0mposite, where subMenu must added
     * @param action Action of current subMenu
     */
    public void setSubMenuItems(final Composite parent, final Action action)
    {
        final MenuItem subMenuItem = new MenuItem(parent, action.getText(), action.getToolTipText(), action.getImageDescriptor().createImage(),
                                                  SWT.PUSH);

        subMenuItem.setItemID(action.getId());
        subMenuItem.setText(action.getText());
        subMenuItem.setFont(FontManager.FONT_VERDANA_12_NORMAL);
        subMenuItem.setImage(action.getImageDescriptor().createImage());
        subMenuItem.setToolTipText(action.getToolTipText());
        GridData grid = new GridData(GridData.FILL_BOTH);
        grid.verticalSpan = 6;

        subMenuItem.setLayoutData(grid);

        subMenuItem.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                action.runWithEvent(event);
            }
        });

        // if this menuItem is the default, than execute the action of this
        if (subMenuItem.getItemID().equals(this.defaultSubMenu))
            action.runWithEvent(new Event());

        setSubMenuItems(subMenuItem);

    }

    /**
     * 
     * This function saves the subMenues Temporary until one menuButton is selected
     * 
     * @param action Action of Menu
     */
    public void addSubMenuItem(Action action)
    {
        this.menuItems.add(action);
    }

    /**
     * 
     * Set the ID of one item.
     * 
     * @param id String
     */
    public void setItemID(String id)
    {
        this.id = id;
    }

    /**
     * 
     * Get the ID of one item.
     * 
     * @return String
     */
    public String getItemID()
    {
        return this.id;
    }

    /**
     * Sets the sub menu items to this menu item.
     * 
     * @param subMenuItems the sub menu items
     */
    private void setSubMenuItems(MenuItem subMenuItems)
    {
        /** TODO: This is a work around. Sub menu buttons are not shown correctly. */
        String lastID = "";
        if (this.subMenuItems.size() > 0)
        {
            lastID = this.subMenuItems.getLast().id;
        }
        if (lastID.equals(subMenuItems.id))
        {
            subMenuItems.setVisible(false);
        }
        this.subMenuItems.add(subMenuItems);
    }

    /**
     * return the sub menu items.
     * 
     * @return the sub menu items
     */
    public LinkedList<MenuItem> getSubMenuItems()
    {
        return this.subMenuItems;
    }

    /**
     * return the sub menu items.
     * 
     * @return the sub menu items
     */
    public ArrayList<Action> getSubMenuArray()
    {
        return this.menuItems;
    }

    /**
     * 
     * Set button selected or not.
     * 
     * @param controller Constructor of Menu, can be null, if it is not necessary to reload the Benchwork
     * @param value true, if button should be selected, else false
     */
    public void setSelected(MenuController controller, boolean value)
    {
        if (value)
        {
            this.setBackgroundImage(this.selectionBackgroundImage);
            if (controller != null)
                controller.getMenuItemListener().widgetSelected(null);
        }
        else
        {
            this.setBackgroundImage(defaultBackgroundImage);
        }
    }

    /**
     * 
     * Set the background color, when it is selected.
     * 
     * @param image Image
     */
    public void setSelectedBackgroundImage(Image image)
    {
        this.selectionBackgroundImage = image;
    }

    /**
     * 
     * Set the default background image.
     * 
     * @param image Image
     */
    public void setDefaultBackgroundImage(Image image)
    {
        this.defaultBackgroundImage = image;
    }

    /**
     * Set the subMenu, which should be selected, when the menu is selected as default
     * 
     * @param defaultSubMenu Name of subMenu
     */
    public void setDefaultSubMenu(String defaultSubMenu)
    {
        this.defaultSubMenu = defaultSubMenu;
    }

    /**
     * get the subMenu, which is selected, when the Menu goes select
     * 
     * @return String
     */
    public String getDefaultSubMenu()
    {
        return this.defaultSubMenu;
    }
}
