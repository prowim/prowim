/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-08-31 11:06:44 +0200 (Mo, 31 Aug 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/dms/DocumentIdentification.java $
 * $LastChangedRevision: 2300 $
 *------------------------------------------------------------------------------
 * (c) 14.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.dms;

import org.apache.commons.lang.Validate;


/**
 * Describes a {@link Document} in DMS. Stores the uuid and the path to a document.
 * 
 * @author Saad Wardi
 * @version $Revision: 2300 $
 */
public class DocumentIdentification
{
    private String uuid;
    private String path;

    /**
     * Creates a new {@link DocumentIdentification} object.
     * 
     * @param uuid {@link DocumentIdentification#setUuid(String)}, may not be null
     * @param path {@link DocumentIdentification#setPath(String)}, can be null
     */
    public DocumentIdentification(String uuid, String path)
    {
        setUuid(uuid);
        setPath(path);
    }

    /**
     * Sets the unified unique id of a {@link Document}
     * 
     * @return the uuid, can be null
     */
    public String getUuid()
    {
        return uuid;
    }

    /**
     * {@link DocumentIdentification#setUuid(String)}
     * 
     * @param uuid the uuid to set, may not be null
     */
    private void setUuid(String uuid)
    {
        Validate.notNull(uuid);
        this.uuid = uuid;
    }

    /**
     * {@link DocumentIdentification#setPath(String)}
     * 
     * @return the path, can be null
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Sets the document path in DMS.
     * 
     * @param path the path to set, can be null
     */
    private void setPath(String path)
    {
        this.path = path;
    }

}
