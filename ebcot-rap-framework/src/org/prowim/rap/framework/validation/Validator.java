/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 22.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.validation;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.prowim.rap.framework.components.Constraint;
import org.prowim.rap.framework.utils.FrameWorkConsts;

import de.ebcot.tools.logging.Logger;


/**
 * Validator checks input values, if these are valid to given constraint
 * 
 * @see org.prowim.rap.framework.components.Constraint
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class Validator
{
    private final static Logger LOG = Logger.getLogger(Validator.class.toString());

    private final Constraint    constraint;

    /**
     * Constructor of validation.
     * 
     * @param constraint Set min, max and required
     */
    public Validator(Constraint constraint)
    {
        this.constraint = constraint;
    }

    /**
     * 
     * Check validation for a string field.
     * 
     * @param text Given text to validate
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    public byte checkString(String text)
    {
        // text could not be negative length
        if (constraint.getMin() < 0)
        {
            return ErrorState.ILLEGAL_STATE;
        }
        else
        {
            // check min and max
            if (text.length() < constraint.getMin())
                return ErrorState.TOO_SMALL;
            else if (text.length() > constraint.getMax())
                return ErrorState.TOO_HIGH;
            else
            {
                // check mandatory
                if (constraint.isRequired())
                {
                    if (text != null && !text.trim().equals(""))
                        return ErrorState.NO_ERROR;
                    else
                        return ErrorState.REQUIRED;
                }
                else
                    return ErrorState.NO_ERROR;
            }
        }

    }

    /**
     * 
     * Check a integer field for validation.
     * 
     * @param text integer
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    @SuppressWarnings("finally")
    public byte checkInt(String text)
    {
        // check mandatory
        if ((constraint.isRequired() && (text == null || text.equals(""))))
        {
            return ErrorState.REQUIRED;
        }
        else if (text != null && !text.trim().equals(""))
        {
            try
            {
                if (Integer.valueOf(text) < constraint.getMin())
                    return ErrorState.TOO_SMALL;
                else if (Integer.valueOf(text) > constraint.getMax())
                    return ErrorState.TOO_HIGH;
                else
                    return ErrorState.NO_ERROR;

            }
            catch (NumberFormatException e)
            {
                LOG.error("Error by checkInt in Validator: ", e);
            }
            finally
            {
                return ErrorState.NO_ERROR;
            }

        }
        else
        {
            return ErrorState.NO_ERROR;
        }
    }

    /**
     * 
     * check a float filed for validation
     * 
     * @param text as float
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    @SuppressWarnings("finally")
    public byte checkFloat(String text)
    {
        // check mandatory
        if ((constraint.isRequired() && (text == null || text.trim().equals(""))))
        {
            return ErrorState.REQUIRED;
        }
        else if (text != null && !text.trim().equals(""))
        {
            try
            {
                if (Float.valueOf(text) < constraint.getMin())
                    return ErrorState.TOO_SMALL;
                else if (Float.valueOf(text) > constraint.getMax())
                    return ErrorState.TOO_HIGH;
                else
                    return ErrorState.NO_ERROR;

            }
            catch (NumberFormatException e)
            {
                LOG.error("Error by checkFloat in Validator: ", e);
            }
            finally
            {
                return ErrorState.NO_ERROR;
            }
        }
        else
        {
            return ErrorState.NO_ERROR;
        }
    }

    /**
     * 
     * Check a list of validation. This valid check uses for singleList, multiList, persons and organizations
     * 
     * @param selectionCount count of selected items
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    public byte checkList(int selectionCount)
    {
        if (constraint.isRequired() && selectionCount < 0)
            return ErrorState.REQUIRED;
        if (selectionCount < constraint.getMin())
            return ErrorState.TOO_SMALL;
        if (selectionCount > constraint.getMax())
            return ErrorState.TOO_HIGH;
        else
            return ErrorState.NO_ERROR;

    }

    /**
     * 
     * Check a document of validation. Its checks if a document is given and if this is necessary
     * 
     * @param ducumentCount count of given document
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    public byte checkDocument(int ducumentCount)
    {
        if (constraint.isRequired() && ducumentCount < 1)
            return ErrorState.REQUIRED;
        if (ducumentCount < constraint.getMin())
            return ErrorState.TOO_SMALL;
        if (ducumentCount > constraint.getMax())
            return ErrorState.TOO_HIGH;
        else
            return ErrorState.NO_ERROR;

    }

    /**
     * 
     * Check a link filed for validation.
     * 
     * @param text included a link
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    public byte checkLink(String text)
    {
        // text could not be negative length
        if (constraint.getMin() < 0)
        {
            return ErrorState.ILLEGAL_STATE;
        }
        else
        {
            if ( !text.matches(FrameWorkConsts.URL_PATTERN))
                return ErrorState.ILLEGAL_STATE;
            // check min and max
            else if (text.length() < constraint.getMin())
                return ErrorState.TOO_SMALL;
            else if (text.length() > constraint.getMax())
                return ErrorState.TOO_HIGH;

            else
            {
                // check mandatory
                if (constraint.isRequired())
                {
                    if (text != null && !text.trim().equals(""))
                        return ErrorState.NO_ERROR;
                    else
                        return ErrorState.REQUIRED;
                }
                else
                    return ErrorState.NO_ERROR;
            }
        }
    }

    /**
     * 
     * Check a mail address filed for validation.
     * 
     * @param text included a mail adddess
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    public byte checkMail(String text)
    {
        // text could not be negative length
        if (constraint.getMin() < 0)
        {
            return ErrorState.ILLEGAL_STATE;
        }
        else
        {
            if ( !text.trim().equals("") && !text.matches(FrameWorkConsts.MAIL_PATTERN))
                return ErrorState.ILLEGAL_STATE;
            // check min and max
            else if (text.length() < constraint.getMin())
                return ErrorState.TOO_SMALL;
            else if (text.length() > constraint.getMax())
                return ErrorState.TOO_HIGH;

            else
            {
                // check mandatory
                if (constraint.isRequired())
                {
                    if (text != null && !text.trim().equals(""))
                        return ErrorState.NO_ERROR;
                    else
                        return ErrorState.REQUIRED;
                }
                else
                    return ErrorState.NO_ERROR;
            }
        }
    }

    /**
     * 
     * Check date and time stamp for validation
     * 
     * @param time selected date. Not null.
     * @return byte @see org.prowim.rap.framework.validation#ErrorState
     */
    public byte checkDate(final String time)
    {
        Validate.notNull(time);
        try
        {
            DateTime dateTime = new DateTime(new Long(time).longValue());
            if (constraint.isRequired() && time.equals(""))
                return ErrorState.REQUIRED;
            else if ((dateTime.isBefore(constraint.getMin())))
                return ErrorState.TOO_SMALL;
            else if (dateTime.isAfter(constraint.getMax()))
                return ErrorState.TOO_HIGH;
            else
                return ErrorState.NO_ERROR;
        }
        catch (NumberFormatException e)
        {
            LOG.error("Error by checkFloat in checkDate: ", e);
            return ErrorState.NO_ERROR;
        }
    }
}
