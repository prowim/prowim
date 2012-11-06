/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-05-10 14:14:31 +0200 (Di, 10 Mai 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/utils/DateUtility.java $
 * $LastChangedRevision: 5083 $
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import de.ebcot.tools.logging.Logger;


/**
 * Handles date and time.
 * 
 * @author Saad Wardi
 * @version $Revision: 5083 $
 */
public class DateUtility
{
    private static final Logger LOG         = Logger.getLogger(DateUtility.class);

    /** The date format. */
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    /** The logger. */
    protected Logger            log         = Logger.getLogger(this.getClass().getName());
    private String              actualDate  = null;

    /**
     * Constructor that initialize the attribute actualDate.
     */
    public DateUtility()
    {

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        actualDate = formatter.format(now);
    }

    /**
     * Gets the actual date.
     * 
     * @return date as String
     */
    public String getActDate()
    {
        return actualDate;
    }

    /**
     * Sets the actual date.
     * 
     * @param actualDate actual date
     */
    public void setActDate(String actualDate)
    {
        this.actualDate = actualDate;
    }

    /**
     * Gets the date time.
     * 
     * @return date time as String.
     */
    public String getDateTime()
    {
        Date now = new Date();
        Long lo = new Long(now.getTime());
        return lo.toString();
    }

    /**
     * Formats time stamp.
     * 
     * @param timestamp a time stamp as String
     * @return formated {@link SimpleDateFormat} as String
     */
    public String formatDateTime(String timestamp)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.GERMAN);
        Long ts = new Long(timestamp);
        Date da = new Date();
        da.setTime(ts.longValue());
        return formatter.format(da);
    }

    /**
     * Converts a date to Timestamp as String.
     * 
     * @param date a date
     * @return time stamp as String
     */
    public String dateToTimestamp(String date)
    {
        String timeStamp = "";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.GERMAN);
        Date dateS;
        try
        {
            dateS = formatter.parse(date);
        }
        catch (ParseException e1)
        {
            log.error("Could not parse the date " + date + " with formatter " + formatter, e1);
            return timeStamp;
        }
        timeStamp = new Long(dateS.getTime()).toString();
        return timeStamp;
    }

    /**
     * Formats date.
     * 
     * @param timestamp a timestamp
     * @return formatted timestamp
     */
    public String formatStandardDateTime(String timestamp)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.GERMAN);
        Long ts = new Long(timestamp);
        Date da = new Date();
        da.setTime(ts.longValue());
        return formatter.format(da);
    }

    /**
     * Converts a date format to a timestamp as long.
     * 
     * @param date a date
     * @return timestamp
     */
    public String standardDateToTimestamp(String date)
    {
        Validate.notNull(date, "Date can not be null!");
        String timeStamp = "";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.GERMAN);
        Date dateS;
        try
        {
            dateS = formatter.parse(date);
        }
        catch (ParseException e1)
        {
            log.error("Could not parse the date " + date + " with formatter " + formatter, e1);
            return timeStamp;
        }
        timeStamp = new Long(dateS.getTime()).toString();
        return timeStamp;
    }

    /**
     * Converts a timestamp as long to a date format.
     * 
     * @param timestamp a timestamp
     * @return date
     */
    public String standardTimestampToDate(String timestamp)
    {

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.GERMAN);
        Long ts = new Long(timestamp);
        Date da = new Date();
        da.setTime(ts.longValue());
        return formatter.format(da);
    }

    /**
     * Calculates a date.
     * 
     * @param date a date
     * @param diff difference
     * @return calculated date
     */
    public String calculateDate(String date, String diff)
    {
        Long tsDiff = new Long(diff) * 24 * 3600 * 1000; // Ms
        Long tsDate = new Long(this.standardDateToTimestamp(date));
        tsDate = tsDate + tsDiff;
        String newDate = this.formatStandardDateTime(tsDate.toString());
        return newDate;
    }

    /**
     * Description.
     * 
     * @param date a date
     * @param diff a difference
     * @return date
     */
    public String addDateTime(String date, String diff)
    {
        Long tsDiff = new Long(diff) * 60 * 1000; // Min after Ms
        Long tsDate = new Long(this.standardDateToTimestamp(date));
        tsDate = tsDate + tsDiff;
        String newDate = this.formatStandardDateTime(tsDate.toString());
        return newDate;
    }

    /**
     * Gets the datetime formatted as dd.mm.yyyy hh:mm.
     * 
     * @param dateTime not null DateTime.
     * @return formatted String.
     */
    public static String getDateTimeString(DateTime dateTime)
    {
        Validate.notNull(dateTime);
        final int dayOfMount = dateTime.getDayOfMonth();
        final int mounthOfYear = dateTime.getMonthOfYear();
        final int hourOfDay = dateTime.getHourOfDay();
        final int minuteOfHour = dateTime.getMinuteOfHour();
        final int year = dateTime.getYear();

        String dayOfMountString = dayOfMount + "";
        String mounthOfYearString = mounthOfYear + "";
        String hourOfDayString = hourOfDay + "";
        String minuteOfHourString = minuteOfHour + "";

        if (dayOfMount < 10)
        {
            dayOfMountString = "0" + dayOfMountString;
        }
        if (mounthOfYear < 10)
        {
            mounthOfYearString = "0" + mounthOfYearString;
        }
        if (hourOfDay < 10)
        {
            hourOfDayString = "0" + hourOfDayString;
        }
        if (minuteOfHour < 10)
        {
            minuteOfHourString = "0" + minuteOfHourString;
        }
        final String result = dayOfMountString + "." + mounthOfYearString + "." + year + " " + hourOfDayString + ":" + minuteOfHourString;

        return result;
    }

    /**
     * Returns a {@link DateTime} object representing the given date string.
     * 
     * @param dateString the string to convert
     * @return DateTime, can be null if a parse error occurs
     */
    public static DateTime getDateTimeForString(String dateString)
    {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        Date date;
        try
        {
            date = df.parse(dateString);
        }
        catch (ParseException e)
        {
            LOG.error("Could not parse date string: " + dateString, e);
            return null;
        }

        return new DateTime(date.getTime());
    }
}
