/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-02-03 12:33:07 +0100 (Mi, 03 Feb 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/ProcessInformation.java $
 * $LastChangedRevision: 3187 $
 *------------------------------------------------------------------------------
 * (c) 14.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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


/**
 * This is a data-object-class represents processinformation.
 * 
 * @author Saad Wardi
 * @version $Revision: 3187 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ProcessInformation")
@XmlType(namespace = "org.prowim.datamodel.prowim", name = "ProcessInformation", propOrder = { "editable", "infoType" })
public class ProcessInformation extends ProcessElement
{
    private boolean         editable = false;
    private InformationType infoType;

    /**
     * {@link ProcessElement}.
     * 
     * @param id {@link ProcessElement#getID()}.
     * @param name {@link ProcessElement#setName(String)}.
     * @param createTime {@link ProcessElement#getCreateTime()}.
     */
    protected ProcessInformation(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected ProcessInformation()
    {

    }

    /**
     * Gets the status editable.
     * 
     * @return the editable
     */
    public boolean isEditable()
    {
        return editable;
    }

    /**
     * Sets the status editable.
     * 
     * @param editable the editable to set
     */
    public void setEditable(final boolean editable)
    {
        this.editable = editable;
    }

    /**
     * {@link ProcessInformation#setInfoType(InformationType)}.
     * 
     * @return nullable infoType.
     */
    public InformationType getInfoType()
    {
        return infoType;
    }

    /**
     * Sets the {@link InformationType}.
     * 
     * @param infoType infoType to set. Null is possible.
     */
    public void setInfoType(InformationType infoType)
    {
        this.infoType = infoType;
    }

}
