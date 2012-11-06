/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 28.03.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.knowledge.open;

import org.apache.commons.lang.Validate;
import org.eclipse.rwt.widgets.ExternalBrowser;


/**
 * Call the default mail client of current OS and sets the informations of TO, CC, BCC and subject.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.2.0
 */
public final class CallMailClient
{
    /**
     * 
     * Description.
     */
    private CallMailClient()
    {
    }

    /**
     * 
     * Opens a {@link ExternalBrowser}, which calls the default mail client. You can set several eMail addresses, which should separated with a comma.
     * 
     * @param mailTo mail addresses, which included in TO Field. Can be empty. Not null.
     * @param mailCC mail addresses, which included in CC Field. Can be empty. Not null.
     * @param mailBCC mail addresses, which included in BCC Field. Can be empty. Not null.
     * @param mailSubject the Subject of email. Can be empty. Not null.
     */
    public static void open(String mailTo, String mailCC, String mailBCC, String mailSubject)
    {
        Validate.notNull(mailTo, "mailTo can not be null");
        Validate.notNull(mailCC, "mailCC can not be null");
        Validate.notNull(mailBCC, "mailBCC can not be null");
        Validate.notNull(mailSubject, "mailSubject can not be null");

        String to = "mailto:" + mailTo;
        String cc = "?cc=" + mailCC;
        String bcc = "&bcc=" + mailBCC;
        String subject = "&subject=" + mailSubject;

        if (mailBCC.equals(""))
            bcc = "";

        String param = to + cc + bcc + subject;
        ExternalBrowser.open("FEEDBACK_MAIL", param, 0);
        ExternalBrowser.close("FEEDBACK_MAIL");
    }
}
