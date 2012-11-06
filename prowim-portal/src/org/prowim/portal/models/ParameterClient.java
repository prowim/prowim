/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 15.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.portal.MainController;
import org.prowim.portal.utils.GlobalConstants;



/**
 * Model for Parameter at client site. This get all methods from the main parameter at server site and override the ToString()-Method of this.
 * 
 * @see org.prowim.datamodel.prowim.Parameter
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ParameterClient extends Parameter
{
    /** minimal Value in this component */
    private Long    min      = new Long(0);

    /** maximal Value in this component */
    private Long    max      = new Long(0);

    /** Value requirement in this component */
    private boolean required = false;

    /**
     * 
     * Constructor.
     * 
     * @param parameterValues Parameter @see de.ebcot.prowim.datamodel.Parameter
     */
    public ParameterClient(Parameter parameterValues)
    {
        Validate.notNull(parameterValues);
        if (parameterValues.getSelectedValues() != null)
            setSelectedValues(parameterValues.getSelectedValues());

        setID(parameterValues.getID());
        setPossibleSelection(parameterValues.getPossibleSelection());
        setContentType(parameterValues.getContentType());
        setInfoTypeID(parameterValues.getInfoTypeID());
        setName(parameterValues.getName());
        setReferenceID(parameterValues.getReferenceID());
        setTemplateID(parameterValues.getTemplateID());
        setMinValue(parameterValues.getMinValue());
        setMaxValue(parameterValues.getMaxValue());
        setRequired(parameterValues.isRequired());
        setOrder(parameterValues.getOrder());
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        ObjectArray objectArray = new ObjectArray();

        // return empty string, if object is null
        if (getSelectedValues() == null)
            return "";
        else if (getInfoTypeID().equals(GlobalConstants.SHORT_TEXT))
            return getStrValue();
        else if (getInfoTypeID().equals(GlobalConstants.LONG_TEXT))
            return getStrValue();
        else if (getInfoTypeID().equals(GlobalConstants.INTEGER))
            return getStrValue();
        else if (getInfoTypeID().equals(GlobalConstants.FLOAT))
            return getStrValue();
        else if (getInfoTypeID().equals(GlobalConstants.SINGLE_LIST))
            return getStrValue();
        else if (getInfoTypeID().equals(GlobalConstants.MULTI_LIST) || getInfoTypeID().equals(GlobalConstants.MULTI_LIST_CONTROL_FLOW))
        {
            objectArray = getSelectedValues();

            if ( !objectArray.isEmpty() && objectArray.getItem().get(0) != null)
            {
                if (objectArray.getItem().size() > 1)
                    return objectArray.getItem().get(0).toString() + ",...";
                else
                    return objectArray.getItem().get(0).toString();
            }
            else
                return "";
        }
        else if (getInfoTypeID().equals(GlobalConstants.COMBO_BOX) || getInfoTypeID().equals(GlobalConstants.COMBO_BOX_CONTROL_FLOW))
            return getStrValue();

        else if (getInfoTypeID().equals(GlobalConstants.DOCUMENT))
        {
            objectArray = getSelectedValues();
            Object object = objectArray.getItem().get(0);
            if ( !objectArray.isEmpty() && object != null && !object.toString().equals(""))
            {
                return MainController.getInstance().getDocumentName(getID());
            }
            else
                return "";

        }
        else if (getInfoTypeID().equals(GlobalConstants.PERSON) || getInfoTypeID().equals(GlobalConstants.ORGANIZATION_UNIT))
        {
            objectArray = getSelectedValues();

            if ( !objectArray.isEmpty() && objectArray.getItem().get(0) != null)
            {
                if (objectArray.getItem().size() > 1)
                    return objectArray.getItem().get(0).toString() + ",...";
                else
                    return objectArray.getItem().get(0).toString();
            }
            else
                return "";
        }
        else if (getInfoTypeID().equals(GlobalConstants.DATE) || getInfoTypeID().equals(GlobalConstants.TIME_STAMP))
        {
            DateTimeFormatter pattern;
            if (getInfoTypeID().equals(GlobalConstants.DATE))
                pattern = DateTimeFormat.forPattern(GlobalConstants.DATE_GR_PATTERN);

            else
                pattern = DateTimeFormat.forPattern(GlobalConstants.DATE_TIME_GR_PATTERN);

            objectArray = getSelectedValues();
            if ( !objectArray.isEmpty() && objectArray.getItem().get(0) != null)
            {
                DateTime dateTime = new DateTime(new Long((String) objectArray.getItem().get(0)).longValue());
                return dateTime.toString(pattern);
            }
            else
                return "";
        }
        else if (getInfoTypeID().equals(GlobalConstants.LINK))
            return getStrValue();
        else
            throw new IllegalArgumentException(getInfoTypeID());

    }

    // Get string value of object is exist, else empty string
    private String getStrValue()
    {
        ObjectArray objectArray = new ObjectArray();

        objectArray = getSelectedValues();
        if ( !objectArray.isEmpty() && objectArray.getItem().get(0) != null)
            return objectArray.getItem().get(0).toString();
        else
            return "";
    }

    /**
     * 
     * Convert a date value to dateMidnight
     * 
     * @param dateValue instance of DateMidnight in string format
     * @return DateMidnight
     */
    public DateTime objectToDateTime(String dateValue)
    {
        return new DateTime(new Long(dateValue).longValue());
    }

    /**
     * Set min
     * 
     * @param min the min to set
     */
    private void setMinValue(Long min)
    {
        this.min = min;
    }

    /**
     * get min
     * 
     * @return the min
     */
    @Override
    public Long getMinValue()
    {
        return min;
    }

    /**
     * set max
     * 
     * @param max the max to set
     */
    private void setMaxValue(Long max)
    {
        this.max = max;
    }

    /**
     * get max
     * 
     * @return the max
     */
    @Override
    public Long getMaxValue()
    {
        return max;
    }

    /**
     * 
     * Description.
     * 
     * @param value
     */
    private void setRequired(boolean value)
    {
        this.required = value;
    }

    /**
     * 
     * Description.
     * 
     * @return boolean
     */
    @Override
    public boolean isRequired()
    {
        return this.required;
    }

}
