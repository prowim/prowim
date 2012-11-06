/*==============================================================================
 * File $Id: MenuItemSelectionListener.java 1392 2009-05-13 16:07:37Z khodaei $
 * Project: ebcot.rap.framework
 *
 * $LastChangedDate: 2009-05-13 18:07:37 +0200 (Mi, 13 Mai 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/menu/MenuItemSelectionListener.java $
 * $LastChangedRevision: 1392 $
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

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


/**
 * An implementation of a selection listener. The method widgetSelected(SelectionEvent e) is invoked when the menu item is selected.
 * 
 * @author wardi, Maziar Khodaei
 * @version $Revision: 1392 $
 */
public class MenuItemSelectionListener extends SelectionAdapter
{
    private MenuItem     theMenuItem     = null;
    private MenuProvider theMenuProvider = null;

    /**
     * Constructor with a MenuItem object and an MenuProvider object that provides the menu.
     * 
     * @param menuItem the menu item.
     * @param menuProvider the menu provider.
     */
    public MenuItemSelectionListener(MenuItem menuItem, MenuProvider menuProvider)
    {
        this.theMenuItem = menuItem;
        this.theMenuProvider = menuProvider;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent e)
    {
        theMenuProvider.resetSubMenu();

        theMenuProvider.setSubMenuArray(theMenuItem, theMenuItem.getSubMenuArray());

        theMenuProvider.setMenuLabel(theMenuItem.getText());

        theMenuItem.setSelected(null, true);
    }
}
