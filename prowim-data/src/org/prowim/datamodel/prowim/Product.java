/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-12-14 15:54:39 +0100 (Mo, 14 Dez 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/Product.java $
 * $LastChangedRevision: 2970 $
 *------------------------------------------------------------------------------
 * (c) 28.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.prowim.datamodel.collections.ParameterArray;



/**
 * This is a data-object-class represents product.
 * 
 * @author Saad Wardi
 * @version $Revision: 2970 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "product", propOrder = { "activityID", "parameters" })
public class Product extends ProcessElement
{

    private String         activityID;
    private ParameterArray parameters;

    /**
     * Creates a {@link Product} with id, name and createTime.
     * 
     * @param id {@link Product#setID(String)}
     * @param name {@link Product#setName(String)}
     * @param createTime {@link Product#setCreateTime(String)}
     * @param activityID {@link Product#setActivityID(String)}
     */
    protected Product(String id, String name, String createTime, String activityID)
    {
        super(id, name, createTime);
    }

    /**
     * Creates a {@link Product} with id, name and createTime.
     * 
     * @param id {@link Product#setID(String)}
     * @param name {@link Product#setName(String)}
     * @param createTime {@link Product#setCreateTime(String)}
     */
    protected Product(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Product()
    {

    }

    /**
     * {@link Product#setParameters(ParameterArray)}
     * 
     * @return not null {@link ParameterArray}.
     */
    public ParameterArray getParameters()
    {
        return parameters;
    }

    /**
     * Sets the list of {@link Parameter} defined for this product
     * 
     * @param parameters the parameters to set, not null.
     */
    public void setParameters(ParameterArray parameters)
    {
        this.parameters = parameters;
    }

    /**
     * {@link Product#setActivityID(String)}
     * 
     * @return the activityID
     */
    public String getActivityID()
    {
        return activityID;
    }

    /**
     * Sets the {@link Activity} ID where the {@link Product} is defined.
     * 
     * @param activityID the activityID to set.
     */
    public void setActivityID(String activityID)
    {
        this.activityID = activityID;
    }

}
