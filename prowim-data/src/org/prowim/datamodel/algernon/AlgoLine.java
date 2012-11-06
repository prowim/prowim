/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-30 13:50:09 +0200 (Mi, 30 Jun 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/algernon/AlgernonConstants.java $
 * $LastChangedRevision: 4173 $
 *------------------------------------------------------------------------------
 * (c) 20.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.algernon;

/**
 * AlgoLine a class provides a formatted algernon command line that can be executed from protege.
 * 
 * @author Saad Wardi
 */
public class AlgoLine
{

    private Integer      number;
    private StringBuffer columnName;
    private StringBuffer value;

    /** Creates a new id of AlgoLine */
    public AlgoLine()
    {
        number = null;
        columnName = null;
        value = null;
    }

    /**
     * Getter for property number.
     * 
     * @return Value of property number.
     */
    public java.lang.Integer getNumber()
    {
        return number;
    }

    /**
     * Setter for property number.
     * 
     * @param number New value of property number.
     */
    public void setNumber(java.lang.Integer number)
    {
        this.number = number;
    }

    /**
     * Getter for property columnName.
     * 
     * @return Value of property columnName.
     */
    public java.lang.StringBuffer getColumnName()
    {
        return columnName;
    }

    /**
     * Setter for property columnName.
     * 
     * @param columnName New value of property columnName.
     */
    public void setColumnName(java.lang.StringBuffer columnName)
    {
        this.columnName = columnName;
    }

    /**
     * Getter for property value.
     * 
     * @return Value of property value.
     */
    public java.lang.StringBuffer getValue()
    {
        return value;
    }

    /**
     * Setter for property value.
     * 
     * @param value New value of property value.
     */
    public void setValue(java.lang.StringBuffer value)
    {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Number :" + this.number + " ColumnName :" + this.columnName + " Value :" + this.value;
    }

}
