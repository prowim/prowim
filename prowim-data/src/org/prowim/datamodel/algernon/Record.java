/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This is a data-object-class represents Record.
 * 
 * @author Saad Wardi
 * @version $Revision: 2084 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "record", propOrder = { "fields", "numberOfFields" })
public class Record
{
    /** nil able. */
    @XmlElement(nillable = true)
    private List<String> fields;
    private int          numberOfFields;

    /**
     * Gets the fields attribute
     * 
     * @return the fields
     */
    public List<String> getFields()
    {
        return fields;
    }

    /**
     * Sets the fields attribute.
     * 
     * @param fields the fields to set
     */
    public void setFields(List<String> fields)
    {
        this.fields = fields;
    }

    /**
     * Gets the numberOfFields attribute.
     * 
     * @return the numberOfFields
     */
    public int getNumberOfFields()
    {
        return numberOfFields;
    }

    /**
     * Sets the numberOfFields attribute.
     * 
     * @param numberOfFields the numberOfFields to set
     */
    public void setNumberOfFields(int numberOfFields)
    {
        this.numberOfFields = numberOfFields;
    }

}
