/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 28.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
package org.prowim.rap.framework.components;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


/**
 * A factory to create buttons which are initialized by an {@link Action}.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0.a8
 */
public final class ButtonFactory
{
    /**
     * Static utility class.
     */
    private ButtonFactory()
    {
    }

    /**
     * Creates a new button and sets the attributes defined by the parameter action:
     * 
     * <li>{@link Button#setText} is set by {@link Action#getText()}</li>
     * 
     * <li>{@link Button#setImage} is set by {@link Action#getImageDescriptor()}</li>
     * 
     * <li>{@link Button#setToolTipText} is set by {@link Action#getToolTipText()}</li>
     * 
     * If the action is enabled, the button will be enabled, too. Also the necessary listener is registered and the action is stored in the button by calling {@link Button#setData(Object)}.
     * 
     * @param parent {@link Composite}, may not be null
     * @param style {@link SWT}
     * @param action This {@link Action} defines the appearance of the {@link Button}, may not be null
     * @return {@link Button}, never null
     */
    public static Button create(Composite parent, int style, final Action action)
    {
        Validate.notNull(action);
        Validate.notNull(parent);

        Button button = new Button(parent, style);
        button.setText(action.getText());
        button.setImage(action.getImageDescriptor().createImage());
        button.setToolTipText(action.getToolTipText());
        button.setData(action);

        button.addListener(SWT.Selection, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                action.runWithEvent(event);
            }
        });
        button.setEnabled(action.isEnabled());

        return button;
    }

    /**
     * Creates a new default button with style {@link SWT#PUSH} and sets the attributes defined by the parameter action:
     * 
     * <li>{@link Button#setText} is set by {@link Action#getText()}</li>
     * 
     * <li>{@link Button#setImage} is set by {@link Action#getImageDescriptor()}</li>
     * 
     * <li>{@link Button#setToolTipText} is set by {@link Action#getToolTipText()}</li>
     * 
     * If the action is enabled, the button will be enabled, too. Also the necessary listener is registered and the action is stored in the button by calling {@link Button#setData(Object)}.
     * 
     * @param parent {@link Composite}, may not be null
     * @param action This {@link Action} defines the appearance of the {@link Button}, may not be null
     * @return Button, never null
     */
    public static Button create(Composite parent, final Action action)
    {
        return create(parent, SWT.PUSH, action);
    }
}
