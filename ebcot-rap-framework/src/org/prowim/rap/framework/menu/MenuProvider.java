/*==============================================================================
 * File $Id: MenuProvider.java 2647 2009-11-05 15:43:12Z wardi $
 * Project: ebcot.rap.framework
 *
 * $LastChangedDate: 2009-11-05 16:43:12 +0100 (Do, 05 Nov 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/menu/MenuProvider.java $
 * $LastChangedRevision: 2647 $
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
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.prowim.rap.framework.resource.ColorManager;
import org.prowim.rap.framework.resource.FontManager;



/**
 * MenuProvider provides a standard way of creating menu viewers that can be shared across different clients that need to create a menu (ProWimMenuView for instance).
 * 
 * @author wardi, Maziar Khodaei
 * @version $Revision: 2647 $
 */
public class MenuProvider // extends AbstractTreeViewer
{

    /** The label at the top of the menu. */
    private Label     lbMenu           = null;

    /** the menu object. */
    private Menu      menu             = null;

    /** the menu bottom composite. */
    private Composite menuComposite    = null;

    /** the menu bottom composite. */
    private Composite subMenuComposite = null;

    /** the SashForm to provide split behavior. */
    private SashForm  sashForm;

    private Composite subMenuContainer = null;

    /**
     * Create an MenuProvider.
     * 
     * @param parent the parent Composite object where the Menu object reside.
     */
    public MenuProvider(Composite parent)
    {
        // super(parent,SWT.NONE);
        // this.setLayout(new GridLayout());
        // this.setLayoutData(new GridData(GridData.FILL_BOTH));

        initGui(parent);
    }

    /**
     * initialize.
     * 
     * @param parent the parent Composite.
     */
    private void initGui(Composite parent)
    {
        /** the Sash. */
        sashForm = new SashForm(parent, SWT.VERTICAL);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(sashForm);

        /** create the intern Composite. */
        subMenuComposite = new Composite(sashForm, SWT.CENTER | SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
        menuComposite = new Composite(sashForm, SWT.BOTTOM | SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);

        sashForm.SASH_WIDTH = 0;
        sashForm.setWeights(new int[] { 8, 2 });
        sashForm.pack();

        sashForm.addListener(SWT.Resize, new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                if (sashForm.getSize().y < 630 && sashForm.getSize().y > 456)
                {
                    sashForm.setWeights(new int[] { 7, 3 });

                }
                else if (sashForm.getSize().y < 456)
                {
                    sashForm.setWeights(new int[] { 6, 4 });

                }
                else
                {
                    sashForm.setWeights(new int[] { 8, 2 });
                }
            }
        });

        /** set the GridLayout. */
        GridLayout gridLayout = new GridLayout(1, false);

        subMenuComposite.setLayout(gridLayout);
        menuComposite.setLayout(gridLayout);

        /** create the menu label. */
        lbMenu = new Label(subMenuComposite, SWT.LEFT | SWT.BOLD | SWT.BORDER);
        // lbMenu.setText("Home");
        lbMenu.setFont(FontManager.FONT_VERDANA_16_BOLD);
        lbMenu.setBackground(ColorManager.COLOR_ORANGE);
        lbMenu.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        subMenuContainer = new Composite(subMenuComposite, SWT.NONE);
        subMenuContainer.setLayout(new GridLayout(1, false));
        subMenuContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * The menu label background color.
     * 
     * @param color a color
     */
    public void setMenuTitleBackgroundColor(Color color)
    {
        lbMenu.setBackground(color);
    }

    /**
     * get the menu label background.
     * 
     * @return the background color.
     */
    public Color getMenuTitleBackgroundColor()
    {
        return lbMenu.getBackground();
    }

    /**
     * The menu background color.
     * 
     * @param color a color
     */
    public void setMenuBackgroundColor(Color color)
    {
        menuComposite.setBackground(color);
    }

    /**
     * get the menu background.
     * 
     * @return the background color.
     */
    public Color getMenuBackgroundColor()
    {
        return menuComposite.getBackground();
    }

    /**
     * The sub menu background color.
     * 
     * @param color a color
     */
    public void setSubMenuBackgroundColor(Color color)
    {
        this.subMenuComposite.setBackground(color);
    }

    /**
     * get the sub menu background.
     * 
     * @return the background color.
     */
    public Color getSubMenuBackgroundColor()
    {
        return this.subMenuComposite.getBackground();
    }

    /**
     * set the menu object.
     * 
     * @param menu the menu
     */
    public void setMenu(Menu menu)
    {
        this.menu = menu;
    }

    /**
     * reset the sub menu.
     */
    public void resetSubMenu()
    {
        Control[] controls = this.subMenuContainer.getChildren();
        for (int i = 0; i < controls.length; i++)
            if ( !controls[i].equals(lbMenu))
                controls[i].dispose();

        /** reset the selection. */
        LinkedList<MenuItem> l = menu.getElements();
        Iterator<MenuItem> it = l.iterator();
        while (it.hasNext())
        {
            MenuItem mi = it.next();
            mi.setSelected(null, false);
        }
    }

    /**
     * set the sub menu items.
     * 
     * @param currentMenu the sub menu items.
     * @param menuItemArray Array of subMenuItem to a Menu
     */
    public void setSubMenuArray(MenuItem currentMenu, ArrayList<Action> menuItemArray)
    {
        if (menuItemArray != null)
        {
            for (int i = 0; i < menuItemArray.size(); i++)
            {
                Action action = menuItemArray.get(i);
                currentMenu.setSubMenuItems(subMenuContainer, action);
            }
        }
        sashForm.redraw();
    }

    /**
     * set the menu label at the top of the view.
     * 
     * @param text the selected menu item views his text on this label.
     */
    public void setMenuLabel(String text)
    {
        lbMenu.setText(text);
    }

    /**
     * 
     * Description.
     * 
     * @param image Image
     */
    public void setMenuLabelBackgroundImage(Image image)
    {
        lbMenu.setBackgroundImage(image);
    }

    /**
     * return the Composite object that contains the menu.
     * 
     * @return Composite
     */
    public Composite getMenuComposite()
    {
        return this.menuComposite;
    }

}
