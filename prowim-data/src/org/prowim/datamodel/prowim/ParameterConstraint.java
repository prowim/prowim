/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-07-29 10:12:46 +0200 (Mi, 29 Jul 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/ParameterConstraint.java $
 * $LastChangedRevision: 2088 $
 *------------------------------------------------------------------------------
 * (c) 22.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 * This is a data-object-class represents ParameterConstraint.
 * 
 * @author Saad Wardi
 * @version $Revision: 2088 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parameterConstraint", propOrder = { "min", "max", "required" })
public class ParameterConstraint
{
    private Long    min;
    private Long    max;
    private boolean required;

    /**
     * Creates an object of this class withe all its defined attribut.
     * 
     * @param min {@link ParameterConstraint#setMin(Long)}
     * @param max {@link ParameterConstraint#setMax(Long)}
     * @param required {@link ParameterConstraint#setRequired(boolean)}
     */
    protected ParameterConstraint(Long min, Long max, boolean required)
    {
        setMin(min);
        setMax(max);
        setRequired(required);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    public ParameterConstraint()
    {
        // TODO $Author: wardi $ Auto-generated constructor stub
    }

    /**
     * Gets the min attribut. {@link ParameterConstraint#setMin(Long)}
     * 
     * @return not null long object that represents the min value that a {@link Parameter} can get.
     */
    public Long getMin()
    {
        return min;
    }

    /**
     * Sets the min attribut.
     * 
     * @param min the min to set. not null long value that a {@link Parameter} can get minimal.
     */
    public void setMin(Long min)
    {
        Validate.notNull(min);
        this.min = min;
    }

    /**
     * Gets the max attribut. {@link ParameterConstraint#setMax(Long)}
     * 
     * @return not null long object that represents the max value that a {@link Parameter} can get.
     */
    public Long getMax()
    {
        return max;
    }

    /**
     * Sets the max attribut.
     * 
     * @param max the max to set. not null long value that a {@link Parameter} can get maximal.
     */
    public void setMax(Long max)
    {
        Validate.notNull(max);
        this.max = max;
    }

    /**
     * Gets required attribut. This indicates that a {@link Parameter} value is mandatory. <br/>
     * {@link ParameterConstraint#setRequired(boolean)}
     * 
     * @return the required default is false.
     */
    public boolean isRequired()
    {
        return required;
    }

    /**
     * Sets the required attribut.
     * 
     * @param required the required to set
     */
    public void setRequired(boolean required)
    {
        Validate.notNull(required);
        this.required = required;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.min + " , " + this.max + " , " + this.required;
    }

}
