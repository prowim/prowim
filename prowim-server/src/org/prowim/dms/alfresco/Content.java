/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-13 11:47:38 +0200 (Fr, 13 Aug 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/Content.java $
 * $LastChangedRevision: 4605 $
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.dms.alfresco;

import java.rmi.RemoteException;

import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.authoring.AuthoringServiceSoapBindingStub;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.types.Version;
import org.prowim.datamodel.dms.DMSException;
import org.prowim.jca.connector.algernon.OntologyErrorException;



/**
 * Creates "ProWim" folder, creates a session to alfresco DMS.
 * 
 * @author Saad Wardi
 * @version $Revision: 4605 $
 */
public interface Content
{

    /**
     * Creates a session to the alfresco DMS and returns the {@link ContentServiceSoapBindingStub}<br>
     * The {@link ContentServiceSoapBindingStub} is used to write and read contents to and from the DMS.
     * 
     * @return {@link ContentServiceSoapBindingStub} or {@link AuthenticationFault}.
     */
    ContentServiceSoapBindingStub getContentService();

    /**
     * Creates and returns a reference to the {@link RepositoryServiceSoapBindingStub}.
     * 
     * @return {@link RepositoryServiceSoapBindingStub}.
     */
    RepositoryServiceSoapBindingStub getRepositoryService();

    /**
     * Creates and returns a reference to the {@link AuthoringServiceSoapBindingStub}.
     * 
     * @return {@link AuthoringServiceSoapBindingStub}
     */
    AuthoringServiceSoapBindingStub getAuthoringService();

    /**
     * creates new reference object in Alfresco.
     * 
     * @param name the content name
     * @param userID the ID of the user logged in.
     * @return {@link Reference}
     * @throws RemoteException if an error occurs.
     */
    Reference createReference(String name, String userID) throws RemoteException;

    /**
     * Gets the {@link Reference} related to the given path, uuid and storeRef.
     * 
     * @param path nullable path.
     * @param uuid not null UUID
     * @param storeRef not null {@link Store}
     * @return {@link Reference}
     */
    Reference getReference(String path, String uuid, Store storeRef);

    /**
     * Creates the folder to store the contents.
     * 
     * @throws Exception can occurs
     */
    void createProWimFolder() throws Exception;

    /**
     * Parse a file type name and returns the mime type used in alfresco.
     * 
     * @param fileExtension this is the extension of the file to be uploaded.
     * @return the mime type of the content.
     */
    String getMimeType(String fileExtension);

    /**
     * Gets the version of a document stored in the alfresco DMS.
     * 
     * @param frameID the ID of a the frame that stores a document in alfresco DMS.
     * @return {@link Version}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @throws DMSException if an error occurs in the DMS backend
     */
    org.prowim.datamodel.dms.VersionHistory getVersionHistory(String frameID) throws OntologyErrorException, DMSException;

}
