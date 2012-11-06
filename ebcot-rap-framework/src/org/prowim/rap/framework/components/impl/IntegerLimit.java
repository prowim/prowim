/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 29.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
package org.prowim.rap.framework.components.impl;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.prowim.rap.framework.components.Constraint;
import org.prowim.rap.framework.resource.ColorManager;
import org.prowim.rap.framework.validation.ErrorState;
import org.prowim.rap.framework.validation.Validator;



/**
 * You can only right here integer values. All other keys will not accepted. <br/>
 * You can set the minimal, maximal and re1uired values about constraint class.<br/>
 * Min and max values are the mathematical comparing between two numbers.
 * 
 * Required means, that this field must be set (non null).
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class IntegerLimit extends Text
{
    private final ErrorState verified = new ErrorState();

    /**
     * 
     * This constructor create a text filed, with properties, which are saved in constraint.
     * 
     * @param parent Composite, where this component shows
     * @param constraint A controller, which set the values min, max and required
     */
    public IntegerLimit(Composite parent, final Constraint constraint)
    {
        super(parent, SWT.SINGLE | SWT.BORDER);

        // Check for validation
        Validate.notNull(constraint);

        final Validator validator = new Validator(constraint);

        this.addVerifyListener(new VerifyListener()
        {

            @Override
            public void verifyText(VerifyEvent event)
            {

                // Get the including text of text field
                String string = event.text;
                char[] chars = new char[string.length()];
                string.getChars(0, chars.length, chars, 0);

                for (int i = 0; i < chars.length; i++)
                {
                    if ( !('0' <= chars[i] && chars[i] <= '9'))
                    {
                        event.doit = false;
                    }
                }

                verified.setErrorState(validator.checkInt(event.text));

                if (verified.hasError())
                    IntegerLimit.this.setBackground(ColorManager.REQUIRED_FIELD);
                else
                    IntegerLimit.this.setBackground(ColorManager.BACKGROUND_COLOR);

            }
        });

        this.addKeyListener(new KeyListener()
        {

            @Override
            public void keyReleased(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
                if ( !('0' <= e.character && e.character <= '9' || e.keyCode == 8))
                {
                    e.doit = false;
                }
                verified.setErrorState(validator.checkInt(getText()));

                if (verified.hasError())
                    IntegerLimit.this.setBackground(ColorManager.REQUIRED_FIELD);
                else
                    IntegerLimit.this.setBackground(ColorManager.BACKGROUND_COLOR);

            }
        });

    }

    /**
     * 
     * return the value of verifying. This will be set by verifying the min, max and requirement given by constraint.
     * 
     * @return true, if verifying is OK, else false.
     */
    public boolean isVerified()
    {
        return !verified.hasError();
    }

}
