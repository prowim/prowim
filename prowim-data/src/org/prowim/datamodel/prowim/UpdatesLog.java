/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-05-07 14:37:57 +0200 (Fr, 07 Mai 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/UpdatesLog.java $
 * $LastChangedRevision: 3801 $
 *------------------------------------------------------------------------------
 * (c) 01.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 * This is a data-object-class represents UpdatesLog.
 * 
 * @author Saad Wardi
 * @version $Revision: 3801 $
 * @since 2.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "UpdatesLog")
@XmlType(namespace = "org.prowim.datamodel.prowim", name = "UpdatesLog", propOrder = { "frameName", "updateScript", "status" })
public class UpdatesLog
{
    private String  frameName;
    private String  updateScript;
    private boolean status;

    /**
     * Creates this.
     * 
     * @param frameName {@link UpdatesLog#setFrameName(String)}.
     * @param updateScript {@link UpdatesLog#setUpdateScript(String)}.
     * @param status {@link UpdatesLog#setStatus(boolean)}.
     */
    protected UpdatesLog(String frameName, String updateScript, boolean status)
    {
        setFrameName(frameName);
        setUpdateScript(updateScript);
        setStatus(status);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected UpdatesLog()
    {
    }

    /**
     * Sets the name of updateframe.
     * 
     * @param frameName not null frameName.
     */
    public void setFrameName(String frameName)
    {
        Validate.notNull(frameName);
        this.frameName = frameName;
    }

    /**
     * {@link UpdatesLog#setFrameName(String)}.
     * 
     * @return the frameName
     */
    public String getFrameName()
    {
        return frameName;
    }

    /**
     * Sets the updatescript.
     * 
     * @param updateScript the not null updateScript.
     */
    public void setUpdateScript(String updateScript)
    {
        Validate.notNull(updateScript);
        this.updateScript = updateScript;
    }

    /**
     * {@link UpdatesLog#setUpdateScript(String)}.
     * 
     * @return the updateScript
     */
    public String getUpdateScript()
    {
        return updateScript;
    }

    /**
     * Sets the status of an updateframe.
     * 
     * @param status the status to set
     */
    public void setStatus(boolean status)
    {
        Validate.notNull(status);
        this.status = status;
    }

    /**
     * {@link UpdatesLog#setStatus(boolean)}.
     * 
     * @return the status
     */
    public boolean isStatus()
    {
        return status;
    }

}
