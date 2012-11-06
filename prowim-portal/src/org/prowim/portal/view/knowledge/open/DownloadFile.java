/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.knowledge.open;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;
import org.eclipse.ui.PlatformUI;


/**
 * Calls a save as dialog to open or download a file.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a10
 */
public final class DownloadFile
{

    /**
     * Description.
     */
    private DownloadFile()
    {
    }

    /**
     * 
     * Registers the {@link DownloadServiceHandler} to make a file ready for download
     * 
     * @param content {@link Byte} list of downloded document. Not null.
     * @param name Name of document. This should include the ending of document. Not null.
     * 
     * @deprecated see bug http://bugs.ebcot.info/view.php?id=4648
     */
    @Deprecated
    public static void download(byte[] content, String name)
    {
        // Register service handler to create download
        RWT.getServiceManager().registerServiceHandler("downloadServiceHandler", new DownloadServiceHandler(content));

        // Create a hidden dialog to open created URL
        new DownloadDialog(PlatformUI.createDisplay().getActiveShell(), createDownloadUrl(name));
    }

    /**
     * 
     * create a URL to given filename. This method call the downloadServiceHandler and set a link to content of file <br>
     * given in request.
     * 
     * @param filename Name of given file.
     * @return URL to given file.
     */
    private static String createDownloadUrl(String filename)
    {
        StringBuilder url = new StringBuilder();
        url.append(RWT.getRequest().getContextPath());
        url.append(RWT.getRequest().getServletPath());
        url.append("?");
        url.append(IServiceHandler.REQUEST_PARAM);
        url.append("=downloadServiceHandler");
        url.append("&filename=");
        url.append(filename);
        String encodedURL = RWT.getResponse().encodeURL(url.toString());
        return encodedURL;
    }

}
