/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-12-02 17:14:58 +0100 (Mi, 02 Dez 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/InstanceProperty.java $
 * $LastChangedRevision: 2883 $
 *------------------------------------------------------------------------------
 * (c) 02.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.prowim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents InstanceProperty.
 * 
 * @author Saad Wardi
 * @version $Revision: 2883 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceProperty", propOrder = { "name", "value" })
public class InstanceProperty
{
    private String name;
    private String value;

    /**
     * Creates a new instance.
     * 
     * @param name {@link InstanceProperty#setName(String)}
     * @param value {@link InstanceProperty#setName(String)}
     */
    protected InstanceProperty(String name, String value)
    {
        setName(name);
        setValue(value);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected InstanceProperty()
    {

    }

    /**
     * Gets the property name.
     * 
     * @return the not null name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the instance property name.
     * 
     * @param name not null name.
     */
    public void setName(String name)
    {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * Gets the instance property value.
     * 
     * @return the not null value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the instance property vaöue.
     * 
     * @param value not null property value.
     */
    public void setValue(String value)
    {
        Validate.notNull(value);
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
        return this.name;
    }

}
