/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-05-07 14:37:57 +0200 (Fr, 07 Mai 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/OntologyVersion.java $
 * $LastChangedRevision: 3801 $
 *------------------------------------------------------------------------------
 * (c) 22.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents OntologyVersion.
 * 
 * @author Saad Wardi
 * @version $Revision: 3801 $
 * @since 2.0
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "OntologyVersion")
@XmlType(namespace = "org.prowim.datamodel.prowim", name = "OntologyVersion", propOrder = { "id", "label", "createTime", "creator" })
public class OntologyVersion
{
    private String id;
    private String label;
    private String createTime;
    private String creator;

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected OntologyVersion()
    {
    }

    /**
     * Creates this.
     * 
     * @param id {@link OntologyVersion#setID(String)}.
     * @param label {@link OntologyVersion#setLabel(String)}.
     * @param createTime {@link OntologyVersion#setCreateTime(String)}.
     * @param creator {@link OntologyVersion#setCreator(String)}.
     */
    protected OntologyVersion(String id, String label, String createTime, String creator)
    {
        setID(id);
        setLabel(label);
        setCreateTime(createTime);
        setCreator(creator);
    }

    /**
     * Gets the version ID.
     * 
     * @return the id
     */
    public String getID()
    {
        return id;
    }

    /**
     * Sets the version ID.
     * 
     * @param id not null ID.
     */
    public void setID(String id)
    {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * Gets the version Label.
     * 
     * @return the label
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * Sets the version label.
     * 
     * @param label the label to set, not null.
     */
    public void setLabel(String label)
    {
        Validate.notNull(label);
        this.label = label;
    }

    /**
     * Gets the version createTime.
     * 
     * @return the createTime
     */
    public String getCreateTime()
    {
        return createTime;
    }

    /**
     * Sets the version createTime.
     * 
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    /**
     * Gets the version creator.
     * 
     * @return the creator
     */
    public String getCreator()
    {
        return creator;
    }

    /**
     * Sets the version creator.
     * 
     * @param creator the creator to set
     */
    public void setCreator(String creator)
    {
        this.creator = creator;
    }

}
