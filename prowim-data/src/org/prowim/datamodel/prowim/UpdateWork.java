/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-05-07 14:37:57 +0200 (Fr, 07 Mai 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/UpdateWork.java $
 * $LastChangedRevision: 3801 $
 *------------------------------------------------------------------------------
 * (c) 27.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.datamodel.collections.ObjectArray;



/**
 * This is a data-object-class represents UpdateWork.
 * 
 * @author Saad Wardi
 * @version $Revision: 3801 $
 * @since 2.0
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UpdateWork")
@XmlType(namespace = "org.prowim.datamodel.prowim", name = "UpdateWork", propOrder = { "updateScript", "updateItems", "versionLabel",
        "deleteItems" })
public class UpdateWork
{
    private String      updateScript;
    private ObjectArray updateItems;
    private String      versionLabel;
    private ObjectArray deleteItems;

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected UpdateWork()
    {
    }

    /**
     * Constructs this.
     * 
     * @param updateScript {@link UpdateWork#setUpdateScript(String)}
     * @param updateItems {@link UpdateWork#setUpdateItems(ObjectArray)}
     * @param versionLabel {@link UpdateWork#setVersionLabel(String)}
     */
    protected UpdateWork(String updateScript, ObjectArray updateItems, String versionLabel)
    {
        setUpdateScript(updateScript);
        setUpdateItems(updateItems);
        setVersionLabel(versionLabel);
    }

    /**
     * {@link UpdateWork#setUpdateScript(String)}
     * 
     * @return the updateScript is the content of all rules that habe to be executed to make the migration.
     */
    public String getUpdateScript()
    {
        return updateScript;
    }

    /**
     * Sets the updatescript used.
     * 
     * @param updateScript the updateScript to set
     */
    public void setUpdateScript(String updateScript)
    {
        Validate.notNull(updateScript);
        this.updateScript = updateScript;
    }

    /**
     * {@link UpdateWork#setUpdateItems(ObjectArray)}
     * 
     * @return the updateItems are the rules that have to be executed to make the migration.
     */
    public ObjectArray getUpdateItems()
    {
        return updateItems;
    }

    /**
     * Sets the updates rule list used.
     * 
     * @param updateItems not null updateItems to set.
     */
    public void setUpdateItems(ObjectArray updateItems)
    {
        Validate.notNull(updateItems);
        this.updateItems = updateItems;
    }

    /**
     * {@link UpdateWork#setVersionLabel(String)}.
     * 
     * @return the versionLabel
     */
    public String getVersionLabel()
    {
        return versionLabel;
    }

    /**
     * Sets the version label.
     * 
     * @param versionLabel not null versionLabel to set.
     */
    public void setVersionLabel(String versionLabel)
    {
        Validate.notNull(versionLabel);
        this.versionLabel = versionLabel;
    }

    /**
     * {@link UpdateWork#setDeleteItems(ObjectArray)}.
     * 
     * @return the deleteItems
     */
    public ObjectArray getDeleteItems()
    {
        return deleteItems;
    }

    /**
     * Sets the delete items.
     * 
     * @param deleteItems the deleteItems to set
     */
    public void setDeleteItems(ObjectArray deleteItems)
    {
        this.deleteItems = deleteItems;
    }

}
