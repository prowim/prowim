/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-20 11:46:06 +0200 (Tue, 20 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/Parameter.java $
 * $LastChangedRevision: 4338 $
 *------------------------------------------------------------------------------
 * (c) 26.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.algernon.ProcessEngineConstants;
import org.prowim.datamodel.collections.ObjectArray;



/**
 * This is a data-object-class represents parameter.
 * 
 * @author Saad Wardi
 * @version $Revision: 4338 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parameter", propOrder = { "id", "name", "templateID", "referenceID", "infoTypeID", "contentType", "possibleSelection",
        "selectedValues", "constraint", "order", "editable" })
public class Parameter
{

    private static final Integer DEFAULT_ORDER = 0;
    private String               id;
    private String               name;
    private String               templateID;
    private String               referenceID;
    private String               infoTypeID;
    private String               contentType;
    private ObjectArray          possibleSelection;            // this list are the elements, which can be chosen
    @XmlElement(nillable = false)
    private ObjectArray          selectedValues;               // this list are the elements, which are chosen
    private ParameterConstraint  constraint;
    private Integer              order         = DEFAULT_ORDER;
    private boolean              editable;

    /**
     * Description.
     * 
     * @param id {@link Parameter#setID(String)}.
     * @param name {@link Parameter#setName(String)}.
     * @param referenceID {@link Parameter#setReferenceID(String)}.
     * @param infoTypeID {@link Parameter#setInfoTypeID(String)}.
     * @param values {@link Parameter#setSelectedValues(ObjectArray)}
     */
    protected Parameter(String id, String name, String referenceID, String infoTypeID, ObjectArray values)
    {
        setID(id);
        setName(name);
        setReferenceID(referenceID);
        setInfoTypeID(infoTypeID);
        setSelectedValues(values);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Parameter()
    {
    }

    /**
     * See {@link Parameter#setID(String)} .
     * 
     * @return not null {@link String}.
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
     * See {@link Parameter#setName(String)} .
     * 
     * @return not null String selectedValues.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the selectedValues of the name property. The name is the name of the parameter defined in the ontology.
     * 
     * @param name the name to set.
     */
    public void setName(String name)
    {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * See {@link Parameter#setReferenceID(String)}
     * 
     * @return the instanceID
     */
    public String getReferenceID()
    {
        return referenceID;
    }

    /**
     * Sets the selectedValues of the id property. The id is the id of the parameter instance.
     * 
     * @param referenceID the process or activity id to set
     */
    public void setReferenceID(String referenceID)
    {
        Validate.notNull(referenceID);
        this.referenceID = referenceID;
    }

    /**
     * See {@link Parameter#setTemplateID(String)}
     * 
     * @return a not null templateID
     */
    public String getTemplateID()
    {
        return templateID;
    }

    /**
     * Sets the selectedValues of the templateID property. The templateID is the id of the parameter template.
     * 
     * @param templateID the templateID to set
     */
    public void setTemplateID(String templateID)
    {
        this.templateID = templateID;
    }

    /**
     * See {@link Parameter#setInfoTypeID(String)}.
     * 
     * @return the infoTypeID
     */
    public String getInfoTypeID()
    {
        return infoTypeID;
    }

    /**
     * Sets the infoTypeID is the id of the informationType.<br/>
     * Example : ShortText is the id of the informationtype KurzText
     * 
     * @param infoTypeID the infoTypeID to set
     */
    public void setInfoTypeID(String infoTypeID)
    {
        Validate.notNull(infoTypeID);
        this.infoTypeID = infoTypeID;
    }

    /**
     * {@link Parameter#setContentType(String)}
     * 
     * @return the contentType
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Sets the contentType
     * 
     * @param contentType the contentType to set is the selectedValues of the attribut Werteablageattribut<br/>
     *        defined in the InformationType class.
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * {@link Parameter#setPossibleSelection(ObjectArray)}
     * 
     * @return the possibleSelection {@link Parameter#setPossibleSelection(ObjectArray)}
     */
    public ObjectArray getPossibleSelection()
    {
        if (possibleSelection == null)
        {
            return new ObjectArray();
        }
        return possibleSelection;
    }

    /**
     * Sets the possibleSelection.
     * 
     * @param possibleSelection the possibleSelection to set are the items in the Auswahl-Liste<br/>
     *        attribut defined in the ProcessInformation
     */
    public void setPossibleSelection(ObjectArray possibleSelection)
    {
        Validate.notNull(possibleSelection);
        this.possibleSelection = possibleSelection;
    }

    /**
     * Gets the selectedValues.
     * 
     * @return the selectedValues {@link Parameter#setSelectedValues(ObjectArray)}.
     */
    public ObjectArray getSelectedValues()
    {
        if (selectedValues == null)
        {
            selectedValues = new ObjectArray();
        }
        return selectedValues;
    }

    /**
     * Sets the selectedValues.
     * 
     * @param values the selectedValues to set is assigned in the field contentType
     */
    public void setSelectedValues(ObjectArray values)
    {
        Validate.notNull(values);
        this.selectedValues = values;

    }

    /**
     * Gets the minimum value that this Parameter can get.
     * 
     * @return {@link ParameterConstraint#getMin()}
     */
    public Long getMinValue()
    {
        return constraint.getMin();
    }

    /**
     * Gets the maximum value that this Parameter can get.
     * 
     * @return {@link ParameterConstraint#getMax()}
     */
    public Long getMaxValue()
    {
        return constraint.getMax();
    }

    /**
     * Gets the required value. True if the value of the Parameter is mandatory.
     * 
     * @return {@link ParameterConstraint#isRequired()}
     */
    public boolean isRequired()
    {
        return constraint.isRequired();
    }

    /**
     * Sets the constraint attribute.
     * 
     * @param constraint the {@link ParameterConstraint} to set.
     */
    public void setConstraint(ParameterConstraint constraint)
    {
        this.constraint = constraint;
    }

    /**
     * Gets the value of the order defined for the Parameter
     * 
     * @return the order, not null <code>Integer</code> object that represents the order value.
     */
    public Integer getOrder()
    {
        return order;
    }

    /**
     * Sets the positive order value to this parameter.
     * 
     * @param order the order to set. If order is Null order will be set to the DEFAULT_ORDER.
     */
    public void setOrder(Integer order)
    {
        Validate.isTrue(order.intValue() >= 0, "Order can not be negative", order);
        this.order = order;
    }

    /**
     * {@link Parameter#setEditable(boolean)}
     * 
     * @return the editable
     */
    public boolean isEditable()
    {
        return editable;
    }

    /**
     * Sets the property editable.
     * 
     * @param editable the editable to set
     */
    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (selectedValues != null && !selectedValues.isEmpty())
        {
            if (selectedValues.getItem().get(0) != null)
            {
                return selectedValues.getItem().get(0).toString();
            }
            else
            {
                return ProcessEngineConstants.Variables.Common.EMPTY;
            }
        }
        else
        {
            return ProcessEngineConstants.Variables.Common.EMPTY;
        }
    }

    // /**
    // * {@inheritDoc}
    // *
    // * @see java.lang.Comparable#compareTo(java.lang.Object)
    // */
    // @Override
    // public int compareTo(Parameter o)
    // {
    // return name.compareTo(o.name);
    // }
}
