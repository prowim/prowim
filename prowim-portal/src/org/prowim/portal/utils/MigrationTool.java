/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 01.02.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.utils;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.portal.MainController;
import org.prowim.utils.DMSUtility;



/**
 * To migrate the files in DMS. Method gets the documents and set the content type of files in correct format.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.3
 */
public final class MigrationTool
{

    /**
     * Description.
     */
    private MigrationTool()
    {
    }

    /**
     * 
     * To migration of Alfresco
     */
    public static void migration()
    {
        List<DocumentContentProperties> allDocuments = MainController.getInstance().getAllDocuments();
        for (DocumentContentProperties documentContentProperties : allDocuments)
        {

            if (documentContentProperties.getID() != null && !documentContentProperties.getID().equals("")
                    && documentContentProperties.getVersion() != null)
            {
                Document downloadDocumentFromID = MainController.getInstance().downloadDocumentFromID(documentContentProperties.getID(),
                                                                                                      documentContentProperties.getVersion());

                String name = downloadDocumentFromID.getName();

                if (name.indexOf("__ts__") > 0)
                    name = name.substring(0, name.indexOf("__ts__"));

                String extention = DMSUtility.getExtension(name);
                String contentType = getContentType(extention);
                if ( !contentType.equals(""))
                {
                    downloadDocumentFromID.setContentType(contentType);
                    MainController.getInstance().updateDMSDocument(downloadDocumentFromID, documentContentProperties.getID());
                }
            }
        }

        MessageDialog.openInformation(null, "DMS Migration Tool", "Die Migration ist beendet.");
    }

    /**
     * Description.
     * 
     * @param extention
     */
    private static String getContentType(String extention)
    {
        if (extention.equalsIgnoreCase("pdf"))
        {
            return "application/pdf";
        }
        else if (extention.equalsIgnoreCase("doc"))
        {
            return "application/msword";
        }
        else if (extention.equalsIgnoreCase("xls"))
        {
            return "application/vnd.ms-excel";
        }
        else if (extention.equalsIgnoreCase("xlsx"))
        {
            return "application/vnd.excel";
        }
        else if (extention.equalsIgnoreCase("ppt"))
        {
            return "application/vnd.ms-powerpoint";
        }
        else if (extention.equalsIgnoreCase("txt"))
        {
            return "text/plain";
        }
        else if (extention.equalsIgnoreCase("xml"))
        {
            return "text/xml";
        }
        else if (extention.equalsIgnoreCase("jpg"))
        {
            return "image/jpeg";
        }
        else if (extention.equalsIgnoreCase("png"))
        {
            return "image/png";
        }
        else if (extention.equalsIgnoreCase("docx"))
        {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }

        else
            return "";
    }

}
