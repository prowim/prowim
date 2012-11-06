/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-27 18:12:35 +0200 (Fr, 27 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/Search.java $
 * $LastChangedRevision: 4713 $
 *------------------------------------------------------------------------------
 * (c) 11.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.dms.alfresco;

import java.rmi.RemoteException;

import org.prowim.datamodel.ContentProperties;
import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.dms.DocumentContentProperties;



/**
 * Methods for browse, search into the alfresco DMS.
 * 
 * @author Saad Wardi
 * @version $Revision: 4713 $
 */
public interface Search
{

    /**
     * Retrieves the properties of a content.
     * 
     * @param uuid the uuid of the content.
     * @return not null {@link DocumentContentProperties}
     */
    ContentProperties retrieveMetaData(String uuid);

    /**
     * Gets propeties to all documents stored in the space.
     * 
     * @param space the name of the folder its items will be returned.
     * @return not null {@link DocumentContentPropertiesArray}. If no items exist an empty list is returned.
     * @throws RemoteException if the given space does not exist.
     */
    DocumentContentPropertiesArray getAllDocuments(String space) throws RemoteException;

    /**
     * 
     * Searches the given key word (expression) in the given space.
     * 
     * @param keyWord the key word to search in all items of DMS
     * @param customerFolder the name of the folder its items will be returned.
     * @return not null {@link DocumentContentPropertiesArray}. If no items exist an empty list is returned.
     * @throws RemoteException if the given space does not exist.
     */
    DocumentContentPropertiesArray searchFullText(String keyWord, String customerFolder) throws RemoteException;

}
