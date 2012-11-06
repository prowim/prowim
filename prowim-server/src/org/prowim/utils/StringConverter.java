/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-22 20:42:21 +0200 (Do, 22 Jul 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/utils/StringConverter.java $
 * $LastChangedRevision: 4415 $
 *------------------------------------------------------------------------------
 * (c) 23.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.ProcessEngineConstants;

import de.ebcot.tools.logging.Logger;


/**
 * Converts Strings to other java Basetypes.
 * 
 * @author Saad Wardi
 * @version $Revision: 4415 $
 */
public final class StringConverter
{
    private static final Logger   LOG                     = Logger.getLogger(StringConverter.class);
    private final static String   DATE_FORMAT             = "dd.MM.yyyy";
    private final static String   TIMESTAMP_FORMAT        = "dd.MM.yyyy HH:MM";
    private final static String   DATE_REGULAR_EXPRESSION = "\\.";
    private final static DateTime DEFAULT_DATE            = new DateTime(1970, 1, 1, 0, 0, 0, 0);

    private StringConverter()
    {

    }

    /**
     * Converts a String to a {@link Long} object.
     * 
     * @param longAsString the long as String
     * @return a <code>Long</code> object holding the value represented by the string argument in the specified value
     */
    public static Long strToLong(String longAsString)
    {
        Long result = null;
        try
        {
            Long longValue = new Long(longAsString);
            result = longValue;
        }
        catch (NumberFormatException e)
        {
            LOG.error("Error by converting string to Long ", e);
        }
        return result;
    }

    /**
     * Converts algernon boolean as String to a java boolean.
     * 
     * @param boolAsString :TRUE or :FALSE
     * @return true if boolAsString==":TRUE" otherwise false.
     */
    public static boolean strToBool(String boolAsString)
    {
        boolean result = false;

        if (boolAsString.equals(ProcessEngineConstants.Variables.Common.ALGERNON_TRUE))
        {
            result = true;
        }
        return result;
    }

    /**
     * Converts algernon boolean as String to a java boolean.
     * 
     * @param boolAsString SUCCESSFULL or FAILED
     * @return true if boolAsString==SUCCESSFULL otherwise false.
     */
    public static boolean algStatusStrToBool(String boolAsString)
    {
        boolean result = false;

        if (boolAsString.equals(AlgernonConstants.OK))
        {
            result = true;
        }
        return result;
    }

    /**
     * Convert a String to an Integer.
     * 
     * @param value the Integer as String
     * @return an <code>Integer</code> object holding the value represented by the string argument in the specified value.
     */
    public static Integer strToInteger(String value)
    {
        Integer result = null;
        try
        {
            result = new Integer(value);
        }
        catch (NumberFormatException e)
        {
            result = null;
        }
        return result;

    }

    /**
     * Converts Date as String to long (milliseconds).
     * 
     * @param datestr the minimum or maximum of a date.
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the datestr.
     * 
     */
    public static Long datestrToLong(String datestr)
    {
        Validate.notNull(datestr);
        Long result = null;
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.GERMAN);
        try
        {
            Date date = formatter.parse(datestr);
            result = new Long(date.getTime());

        }
        catch (ParseException e)
        {
            LOG.error("Error by converting date to Long ", e);
        }

        return result;
    }

    /**
     * Converts Date as String to long (milliseconds).
     * 
     * @param longstr the minimum or maximum of a date.
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the datestr.
     * 
     */
    public static String longStringToDateString(String longstr)
    {
        Validate.notNull(longstr);
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.GERMAN);

        Long dateAsLong = new Long(longstr);
        Date date = new Date(dateAsLong);
        String result = formatter.format(date);

        return result;
    }

    /**
     * Converts a long to Timestamp as String.
     * 
     * @param longValueAsString the timestamp as long.
     * @return not null formatted Timestamp as String.
     * 
     */
    public static String longStringToTimestampString(String longValueAsString)
    {
        Validate.notNull(longValueAsString);
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.GERMAN);

        Long dateAsLong = new Long(longValueAsString);
        Date date = new Date(dateAsLong);
        String result = formatter.format(date);

        return result;
    }

    /**
     * Converts Date as String to long (milliseconds) using {@link org.joda.time.DateTime}.
     * 
     * @param datestr the minimum or maximum of a date.
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by the datestr.
     * 
     */
    public static Long timestampstr2Long(String datestr)
    {
        Long result = null;
        String date = datestr;
        String[] dateFields = date.split(DATE_REGULAR_EXPRESSION);
        if (dateFields.length != 3)
        {
            throw new IllegalArgumentException("DateFormat not supported " + datestr);
        }
        else
        {
            String day = dateFields[0];
            String mounth = dateFields[1];
            String yearAndTime = dateFields[2];
            String[] yearAndTimeSplit = yearAndTime.split(" ");
            String year = null;

            if (yearAndTimeSplit.length != 2)
            {
                throw new IllegalArgumentException("DateFormat not supported " + datestr);
            }
            else
            {
                year = yearAndTimeSplit[0];
                String time = yearAndTimeSplit[1];
                String[] hhmm = time.split(":");
                if (hhmm.length != 2)
                {
                    throw new IllegalArgumentException("DateFormat not supported " + datestr);
                }
                else
                {
                    String hh = hhmm[0];
                    String mm = hhmm[1];

                    DateTime dateTime = new DateTime(strToInteger(year).intValue(), strToInteger(mounth).intValue(), strToInteger(day).intValue(),
                                                     strToInteger(hh).intValue(), strToInteger(mm).intValue(), 0, 0);
                    if (dateTime != null)
                    {
                        result = dateTime.getMillis();
                    }
                }
            }

        }

        return result;
    }

    /**
     * Converts Timestamp as String to long (milliseconds).
     * 
     * @param timestampstr the minimum or maximum of a date.
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this date.
     * 
     */
    public static Long datestr2Long(String timestampstr)
    {
        Long result = null;
        String date = timestampstr;
        String[] dateFields = date.split(DATE_REGULAR_EXPRESSION);
        if (dateFields.length != 3)
        {
            throw new IllegalArgumentException("DateFormat not supported " + timestampstr);
        }
        else
        {
            String day = dateFields[0];
            String mounth = dateFields[1];
            String year = dateFields[2];
            DateTime dateTime = null;
            if (day != null && mounth != null && year != null)
            {
                dateTime = new DateTime(strToInteger(year).intValue(), strToInteger(mounth).intValue(), strToInteger(day).intValue(), 0, 0, 0, 0);
            }
            else
            {
                dateTime = DEFAULT_DATE;
            }
            if (dateTime != null)
            {
                result = dateTime.getMillis();
            }
            else
            {
                result = DEFAULT_DATE.getMillis();
            }
        }

        return result;
    }

    /**
     * Description.
     * 
     * @param timestampstr the timestamp as string.
     * @return the timestamp as long.
     */
    public static Long datestr2Long2(String timestampstr)
    {
        Long result = new Long(timestampstr);

        return result;
    }

    /**
     * Converts Timestamp as String to long (milliseconds).
     * 
     * @param timestampstr the minimum or maximum of a date.
     * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this date.
     * 
     */
    public static Long timestampstrToLong(String timestampstr)
    {
        Validate.notNull(timestampstr);
        Long result = null;
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.GERMAN);
        try
        {
            Date date = formatter.parse(timestampstr);
            result = new Long(date.getTime());

        }
        catch (ParseException e)
        {
            LOG.error("Error by converting date to Long ", e);
        }

        return result;
    }

}
