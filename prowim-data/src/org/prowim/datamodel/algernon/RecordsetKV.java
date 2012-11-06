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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This is a data-object-class represents AlgernonRecordsetKV complex type.
 * 
 * @author Saad Wardi
 * @version $Revision: 2672 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recordsetKV", propOrder = { "fieldNames", "noOfColumns", "noOfRecords", "records", "result", "resultString" })
public class RecordsetKV
{
    /** result. */
    protected String       result;
    /** result string. */
    protected String       resultString;
    /** number of columns. */
    protected int          noOfColumns;
    /** number of records. */
    protected int          noOfRecords;
    /** fields name. */
    @XmlElement(nillable = true)
    private List<String>   fieldNames;
    /** records. */
    @XmlElement(nillable = true)
    private List<RecordKV> records;

    /**
     * Gets the fields names.
     * 
     * @return the fieldNames
     */
    public List<String> getFieldNames()
    {
        return fieldNames;
    }

    /**
     * Sets the fieldNames attribute.
     * 
     * @param fieldNames the fieldNames to set
     */
    public void setFieldNames(List<String> fieldNames)
    {
        this.fieldNames = fieldNames;
    }

    /**
     * Gets the number of columns.
     * 
     * @return the numberOfColumns
     */
    public int getNumberOfColumns()
    {
        return noOfColumns;
    }

    /**
     * Sets the number of columns
     * 
     * @param noOfColumns the numberOfColumns to set
     */
    public void setNumberOfColumns(int noOfColumns)
    {
        this.noOfColumns = noOfColumns;
    }

    /**
     * Gets the nu,ber of records.
     * 
     * @return the numberOfRecords
     */
    public int getNumberOfRecords()
    {
        return this.noOfRecords;
    }

    /**
     * Sets the number of records.
     * 
     * @param noOfRecords the numberOfRecords to set
     */
    public void setNumberOfRecords(int noOfRecords)
    {
        this.noOfRecords = noOfRecords;
    }

    /**
     * Gets the records.
     * 
     * @return the records
     */
    public List<RecordKV> getRecords()
    {
        return records;
    }

    /**
     * Sets the Records.
     * 
     * @param records the records to set
     */
    public void setRecords(List<RecordKV> records)
    {
        this.records = records;
    }

    /**
     * Gets the result.
     * 
     * @return the result
     */
    public String getResult()
    {
        return result;
    }

    /**
     * Sets the result.
     * 
     * @param result the result to set
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * Gets the resultString attribute
     * 
     * @return the resultString
     */
    public String getResultString()
    {
        return resultString;
    }

    /**
     * Set the resultString sttribute
     * 
     * @param resultString the resultString to set
     */
    public void setResultString(String resultString)
    {
        this.resultString = resultString;
    }

}
