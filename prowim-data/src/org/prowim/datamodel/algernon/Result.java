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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This is a data-object-class represents algernonResult complex type.
 * 
 * @author Saad Wardi
 * @version $Revision: 4106 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "result")
@XmlType(namespace = "org.prowim.datamodel.algernon", name = "result", propOrder = { "fieldNames", "noOfColumns", "noOfRecords", "records",
        "result", "resultString" })
public class Result
{

    /** number of columns. */
    protected int        noOfColumns;
    /** number of records. */
    protected int        noOfRecords;
    /** result. */
    protected String     result;
    /** the result string. */
    protected String     resultString;
    /** fields name. */
    @XmlElement(nillable = true)
    private List<String> fieldNames;
    /** records. */
    @XmlElement(nillable = true)
    private List<Record> records;

    /**
     * Gets the value of the numberOfColumns property.
     * 
     * @return the number of columns
     */
    public int getNumberOfColumns()
    {
        return noOfColumns;
    }

    /**
     * Sets the value of the numberOfColumns property.
     * 
     * @param value the value.
     */
    public void setNumberOfColumns(int value)
    {
        this.noOfColumns = value;
    }

    /**
     * Gets the value of the numberOfRecords property.
     * 
     * @return the number of records.
     */
    public int getNumberOfRecords()
    {
        return noOfRecords;
    }

    /**
     * Sets the value of the numberOfRecords property.
     * 
     * @param value the value.
     */
    public void setNumberOfRecords(int value)
    {
        this.noOfRecords = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getResult()
    {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setResult(String value)
    {
        this.result = value;
    }

    /**
     * Gets the value of the resultString property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getResultString()
    {
        return resultString;
    }

    /**
     * Sets the value of the resultString property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setResultString(String value)
    {
        this.resultString = value;
    }

    /**
     * Sets the value of the fieldNames property.
     * 
     * @param fieldNames allowed object is {@link List}
     */
    public void setFieldNames(List<String> fieldNames)
    {
        this.fieldNames = fieldNames;
    }

    /**
     * Gets the value of the fieldNames property.
     * 
     * @return possible object is {@link List }
     */
    public List<String> getFieldNames()
    {
        return fieldNames;
    }

    /**
     * Sets the value of the records property.
     * 
     * @param records allowed object is {@link String }
     */
    public void setRecords(List<Record> records)
    {
        this.records = records;
    }

    /**
     * Gets the value of the records property.
     * 
     * @return allowed object is {@link List}
     */
    public List<Record> getRecords()
    {
        return records;
    }

}
