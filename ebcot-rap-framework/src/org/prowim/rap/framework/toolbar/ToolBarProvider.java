/*==============================================================================
 * File $Id: ToolBarProvider.java 5072 2011-04-14 11:28:41Z khodaei $
 * Project: ebcot.rap.framework
 *
 * $LastChangedDate: 2011-04-14 13:28:41 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/toolbar/ToolBarProvider.java $
 * $LastChangedRevision: 5072 $
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
package org.prowim.rap.framework.toolbar;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.prowim.rap.framework.components.ButtonFactory;



/**
 * Toolbar component with only buttons.
 * 
 * For creating a toolbar is necessary to create first one instance if this class. Than you can create and insert elements in toolbar. Elements are {@link Button}s. The height of toolbar compute from the high of the highest Button plus a border set by {@link #BORDER_SIZE}.
 * 
 * @author Maziar Khodaei, Oliver Wolff
 * @version $Revision: 5072 $
 */
public class ToolBarProvider extends Composite
{
    /* vertical borderSize for the coolItem */
    private static final int BORDER_SIZE = 4;
    /* storing the coolItem and container */
    private CoolBar          coolBar     = null;
    /* storing the buttons */
    private Composite        container   = null;
    /* storing the container */
    private CoolItem         coolBarItem = null;

    /**
     * This is the constructor of Toolbar.
     * 
     * @param parent parent
     */
    public ToolBarProvider(final Composite parent)
    {
        super(parent, SWT.NONE);
        this.setLayout(new GridLayout());
        this.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Create a coolbar
        this.coolBar = new CoolBar(this, SWT.CENTER);
        this.coolBar.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.coolBar.setLocked(true);
        this.coolBar.setBackgroundMode(SWT.INHERIT_DEFAULT);

        // tool item with a button.
        this.coolBarItem = new CoolItem(coolBar, SWT.CENTER);
        this.container = new Composite(coolBar, SWT.NONE);

        // Set the properties for the layout of container
        RowLayout rowLayout = new RowLayout();
        rowLayout.justify = false;
        rowLayout.type = SWT.HORIZONTAL;
        rowLayout.marginLeft = 0;
        rowLayout.marginTop = 2;
        rowLayout.marginRight = 2;
        rowLayout.marginBottom = 2;
        rowLayout.spacing = 2;

        this.container.setLayout(rowLayout);
        container.setBackgroundMode(SWT.INHERIT_DEFAULT);

        // Add the container to toolbarItem
        this.coolBarItem.setControl(this.container);
    }

    /**
     * Set background image.
     * 
     * @param image Background image
     */
    public final void setToolBarBackgroundImage(final Image image)
    {
        this.coolBar.setBackgroundImage(image);
    }

    /**
     * Get the ToolBar BackgroundImage.
     * 
     * @return Image null is possible
     */
    public final Image getToolBarBackgroundImage()
    {
        return this.coolBar.getBackgroundImage();
    }

    /**
     * Adds one toolbarItem to this provider. Default style is {@link SWT#PUSH}.
     * 
     * @param action {@link Action} of toolbarItem to add a new button, cannot be null
     */
    public final void addToolBarItem(final Action action)
    {
        Validate.notNull(action);
        addToolBarItem(action, SWT.PUSH);
    }

    /**
     * 
     * Adds one toolbarItem to this provider.
     * 
     * @param action {@link Action} of toolbarItem to add a new button, cannot be null
     * @param style only the {@link SWT} styles are allowed for a {@link Button}.
     */
    public final void addToolBarItem(final Action action, int style)
    {
        Validate.notNull(action);

        // set appearance
        Button newItem = ButtonFactory.create(this.container, style, action);
        newItem.setForeground(null);
        newItem.setBackgroundImage(coolBar.getBackgroundImage());
        newItem.setAlignment(SWT.CENTER | SWT.TRANSPARENCY_MASK);
        newItem.pack();

        // set now size and listener
        coolBarItem.setSize(this.coolBarItem.computeSize(this.container.getSize().x, newItem.getSize().y + BORDER_SIZE));
    }

    /**
     * 
     * Get the item of the given label.
     * 
     * @param label name of item to get.
     * @return A button, which present the item. Else not found then runtime exception.
     * @deprecated the label is not unique - use the Action itself
     */
    @Deprecated
    public Button getItem(String label)
    {
        Control[] control = this.container.getChildren();

        for (int i = 0; i < control.length; i++)
        {
            Button btn = (Button) control[i];
            if (btn.getText().equals(label))
                return btn;
        }

        throw new IllegalArgumentException("Label not found: " + label);
    }

    /**
     * 
     * Get the item of the given action.
     * 
     * @param action to get the button for, cannot be null and the action MUST exist!
     * @return A button, which present the item. If not found an IllegalArgumentExcpetion is thrown, so cannot be null.
     */
    public Button getItem(Action action)
    {
        Validate.notNull(action);
        Button result = findItem(action);
        if (result == null)
            throw new IllegalArgumentException("Button not found for action: " + action);
        else
            return result;
    }

    /**
     * 
     * Searchs for the item of the given action.
     * 
     * @param action to get the button for, cannot be null
     * @return A button, which present the item. NULL if not found.
     */
    private Button findItem(Action action)
    {
        Control[] controls = this.container.getChildren();
        for (Control button : controls)
        {
            Button currentButton = (Button) button;
            if (currentButton.getData().equals(action))
                return currentButton;
        }
        return null;
    }

    /**
     * Checks if the given action is added in the toolBar.
     * 
     * @param action to check
     * @return true, if item exist, else false
     */
    public boolean hasItem(Action action)
    {
        return findItem(action) != null;
    }

    /**
     * 
     * Check, or the item exists in toolbar
     * 
     * @param label name of item to get.
     * @return true, if item exist, else false
     * @deprecated use hasItem(Action) instead, because this is not unique
     */
    @Deprecated
    public boolean itemExist(String label)
    {
        Control[] control = this.container.getChildren();

        for (int i = 0; i < control.length; i++)
        {
            Button btn = (Button) control[i];
            if (btn.getText().equals(label))
                return true;
        }

        return false;
    }
}
