/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 02.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.components.impl;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.prowim.rap.framework.components.Constraint;
import org.prowim.rap.framework.resource.ColorManager;
import org.prowim.rap.framework.utils.FrameWorkConsts;
import org.prowim.rap.framework.validation.ErrorState;
import org.prowim.rap.framework.validation.Validator;



/**
 * All string, which not begin with "http://", "https://", "www", "ftp" will not accepted. <br/>
 * min and max by constrains means minimal or maximal length of text field. Required means, that field should be set.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class LinkLimit extends Text
{
    private final String     httpPrefix = FrameWorkConsts.HTTP;

    private final ErrorState verified   = new ErrorState();

    /**
     * 
     * This constructor create a text filed, with properties, which are saved in constraint.
     * 
     * @param parent Composite, where this component shows
     * @param constraint A controller, which set the values min, max and required
     */
    public LinkLimit(Composite parent, final Constraint constraint)
    {
        super(parent, SWT.SINGLE | SWT.BORDER);
        // Check for validation
        Validate.notNull(constraint);

        final Validator validator = new Validator(constraint);

        // Set limitation of text length
        this.setTextLimit(constraint.getMax().intValue());

        // Default value
        this.setText(httpPrefix);

        // Add Verifying listener
        this.addVerifyListener(new VerifyListener()
        {

            @Override
            public void verifyText(VerifyEvent event)
            {
                verified.setErrorState(validator.checkLink(event.text));
                // If a value is required, then set the background color red, else set background text default.
                if (verified.hasError())
                    LinkLimit.this.setBackground(ColorManager.REQUIRED_FIELD);
                else
                    LinkLimit.this.setBackground(ColorManager.BACKGROUND_COLOR);

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
