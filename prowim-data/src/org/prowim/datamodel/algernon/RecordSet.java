/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-28 07:48:49 +0200 (Mo, 28 Jun 2010) $
 * $LastChangedBy: wolff $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/algernon/RecordSet.java $
 * $LastChangedRevision: 4094 $
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */
package org.prowim.datamodel.algernon;

import java.io.Serializable;
import java.util.Hashtable;

import org.apache.commons.lang.Validate;


/**
 * RecordSet
 * 
 * @author Saad Wardi
 */
public class RecordSet implements Serializable
{
    /** SUCCEDDED or FAILED. */
    private String                      result;
    /** Path Succeedded or Path Failed. */
    private String                      resultString;
    private Hashtable<String, String>[] records;
    private String[]                    fieldNames;
    private int                         noOfRecords;
    private int                         noOfColumns;

    /**
     * Gets the result attribute. SUCCEDDED or FAILED.
     * 
     * @return the result
     */
    public String getResult()
    {
        return result;
    }

    /**
     * Sets the result attribute.
     * 
     * @param result the result to set
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * Gets the resultString attribute: Path Succeeded or Path Failed.
     * 
     * @return the resultString
     */
    public String getResultString()
    {
        return resultString;
    }

    /**
     * Sets the resultString attribute.
     * 
     * @param resultString the resultString to set
     */
    public void setResultString(String resultString)
    {
        this.resultString = resultString;
    }

    /**
     * Gets the fieldNames attribute. FieldNames are the variable names used in the executed rule.
     * 
     * @return the fieldNames
     */
    public String[] getFieldNames()
    {
        return fieldNames;
    }

    /**
     * Sets the fieldNames attributes.
     * 
     * @param fieldNames the fieldNames to set
     */
    public void setFieldNames(String[] fieldNames)
    {
        this.fieldNames = fieldNames;
    }

    /**
     * Sets the records attribute.
     * 
     * @param records the records to set
     */
    public void setRecords(Hashtable<String, String>[] records)
    {
        this.records = records;
    }

    /**
     * Gets the number of records.
     * 
     * @return the noOfRecords
     */
    public int getNoOfRecords()
    {
        return noOfRecords;
    }

    /**
     * Sets the number of records attribute.
     * 
     * @param noOfRecords the noOfRecords to set
     */
    public void setNoOfRecords(int noOfRecords)
    {
        this.noOfRecords = noOfRecords;
    }

    /**
     * Gets the number of columns attribute.
     * 
     * @return the noOfColumns
     */
    public int getNoOfColumns()
    {
        return noOfColumns;
    }

    /**
     * Sets the noOfComulmns attribute.
     * 
     * @param noOfColumns the number of columns to set
     */
    public void setNoOfColumns(int noOfColumns)
    {
        this.noOfColumns = noOfColumns;
    }

    /**
     * Gets the records attribute.
     * 
     * @return the records use type safe methode
     */

    public Hashtable<String, String>[] getRecords()
    {
        return records;
    }

    /**
     * This can be used, if only one dataset is waiting for. If an error in ontology occurs, an exception will be thrown.
     * 
     * @param returnKey the key for which the result should be accessed.
     * @return a String if found, otherwise an exception occurs, cannot be null
     */
    public String getSingleResult(String returnKey)
    {
        Validate.isTrue(getRecords() != null, "RecordSet::getSingleResult(): getRecords not initialized -> null");
        if (getResult().equals(AlgernonConstants.OK))
        {
            return getRecords()[0].get(returnKey);
        }
        else
            throw new IllegalStateException("Algernon could not be executed successfully: " + getResult());
    }
}
