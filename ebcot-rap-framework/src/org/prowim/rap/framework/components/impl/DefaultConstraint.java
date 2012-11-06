/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 17:14:33 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/components/impl/DefaultConstraint.java $
 * $LastChangedRevision: 4972 $
 *------------------------------------------------------------------------------
 * (c) 26.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 */
package org.prowim.rap.framework.components.impl;

import org.apache.commons.lang.Validate;
import org.prowim.rap.framework.components.Constraint;



/**
 * This class set the properties of a field or component. This are minimum, maximum and requirements of field.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4972 $
 * 
 * @see Constraint
 */
public class DefaultConstraint implements Constraint
{
    /** minimal Value in this component */
    private long    min      = 0L;

    /** maximal Value in this component */
    private long    max      = 0L;

    /** Value requirement in this component */
    private boolean required = false;

    /**
     * Constructor. This set values for minimum, maximum and requirements of a field or component
     * 
     * @param minimal Minimal value for a field or component, e.g. string length or int size
     * @param maximal Maximal value for a field or component, e.g. string length or int size
     * @param required if set to true, the field is mandatory
     */
    public DefaultConstraint(long minimal, long maximal, boolean required)
    {
        this.min = minimal;
        this.max = maximal;
        this.required = required;

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.Constraint#getMax()
     */
    @Override
    public Long getMax()
    {
        return max;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.Constraint#getMin()
     */
    @Override
    public Long getMin()
    {
        return min;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.Constraint#isRequired()
     */
    @Override
    public boolean isRequired()
    {
        return required;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.Constraint#setMax(Long)
     */
    @Override
    public void setMax(Long max)
    {
        Validate.isTrue(max >= min);
        this.max = max;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.Constraint#setMin(Long)
     */
    @Override
    public void setMin(Long min)
    {
        Validate.isTrue(max >= min);
        this.min = min;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.Constraint#setRequired(boolean)
     */
    @Override
    public void setRequired(boolean value)
    {
        this.required = value;
    }

}
