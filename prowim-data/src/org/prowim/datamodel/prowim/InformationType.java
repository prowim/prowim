/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-02-03 09:27:41 +0100 (Mi, 03 Feb 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/InformationType.java $
 * $LastChangedRevision: 3182 $
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
package org.prowim.datamodel.prowim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.factory.ProwimDataObjectFactory;



/**
 * * This is a data-object-class represents InformationType.
 * 
 * @author Saad Wardi
 * @version $Revision: 3182 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "informationType", propOrder = { "id", "denotation", "minValue", "maxValue", "contentString" })
public class InformationType
{

    private String id;
    private String denotation;
    private Long   minValue;
    private Long   maxValue;
    private String contentString;

    /**
     * Description.
     */
    protected InformationType()
    {

    }

    /**
     * This the only way to create an instance of this class without using the {@link ProwimDataObjectFactory}.
     * 
     * @param id {@link InformationType#setID(String)}
     * @param denotation {@link InformationType#setDenotation(String)}
     * @param minValue {@link InformationType#setMinValue(Long)}
     * @param maxValue {@link InformationType#setMaxValue(Long)}
     * @param contentString {@link InformationType#setContentString(String)}
     */
    protected InformationType(String id, String denotation, Long minValue, Long maxValue, String contentString)
    {
        setID(id);
        setDenotation(denotation);
        setMinValue(minValue);
        setMaxValue(maxValue);
        setContentString(contentString);
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getID()
    {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the id to set
     */
    public void setID(String id)
    {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * Gets the denotation.
     * 
     * @return the denotation
     */
    public String getDenotation()
    {
        return denotation;
    }

    /**
     * Sets the denotation.
     * 
     * @param denotation the denotation to set
     */
    public void setDenotation(String denotation)
    {
        Validate.notNull(denotation);
        this.denotation = denotation;
    }

    /**
     * Gets the minValue.
     * 
     * @return the minValue
     */
    public Long getMinValue()
    {
        return minValue;
    }

    /**
     * Sets the minValue
     * 
     * @param minValue the minValue to set
     */
    public void setMinValue(Long minValue)
    {
        Validate.notNull(minValue);
        this.minValue = minValue;
    }

    /**
     * Gets the maxValue.
     * 
     * @return the maxValue
     */
    public Long getMaxValue()
    {
        return maxValue;
    }

    /**
     * Sets the maxValue.
     * 
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(Long maxValue)
    {
        Validate.notNull(maxValue);
        this.maxValue = maxValue;
    }

    /**
     * Gets the contentString.
     * 
     * @return the contentString
     */
    public String getContentString()
    {
        return contentString;
    }

    /**
     * Sets the contentString.
     * 
     * @param contentString the contentString to set
     */
    public void setContentString(String contentString)
    {
        Validate.notNull(contentString);
        this.contentString = contentString;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getDenotation();
    }

}
