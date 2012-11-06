/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 28.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.Validate;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.service.IServiceHandler;


/**
 * This handler get the content of a file and send it in the response. When the service handler is <br>
 * registered, return a output stream of the file, when it is called. <br>
 * You can register it so :RWT.getServiceManager() .registerServiceHandler( "downloadServiceHandler", new DownloadServiceHandler() );
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * 
 * @deprecated see bug http://bugs.ebcot.info/view.php?id=4648
 */
@Deprecated
public class DownloadServiceHandler implements IServiceHandler
{

    private final byte[] data;

    /**
     * Description.
     * 
     * @param data The file data to send. not null.
     */
    public DownloadServiceHandler(final byte[] data)
    {
        Validate.notNull(data);
        this.data = data;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.rwt.service.IServiceHandler#service()
     */
    @Override
    public void service() throws IOException, ServletException
    {
        // Which file to download
        String filename = RWT.getRequest().getParameter("filename");
        // Get the file content
        // Send the file in the response.
        HttpServletResponse response = RWT.getResponse();
        response.setContentType("application/octet-stream");
        response.setContentLength(data.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try
        {
            response.getOutputStream().write(data);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

}
