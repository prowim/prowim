/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-09-18 13:55:40 +0200 (Fr, 18 Sep 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/utils/MailMessenger.java $
 * $LastChangedRevision: 2414 $
 *------------------------------------------------------------------------------
 * (c) 08.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.utils;

import java.util.Date;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.ebcot.tools.logging.Logger;


/**
 * Sends mails using the jboss-mail-service.
 * 
 * @author Saad Wardi
 * @version $Revision: 2414 $
 */
public class MailMessenger
{
    private static final Logger LOG                          = Logger.getLogger(MailMessenger.class);
    private final static String JMS_MAIL_SESSION             = "ebcot.mail";
    private final static String MAIL_FROM_PROP               = "mail.from";
    private final static String MAIL_TRANSPORT_PROTOCOL_PROP = "mail.transport.protocol";
    private Session             mailSession                  = null;

    /**
     * Creates a javax.mail.Session.
     */
    protected MailMessenger()
    {
        createSession();
    }

    /**
     * Sends a mail with a given subject, from , to and message text.
     * 
     * @param subject the email subject.
     * @param from the email from address.
     * @param to the destination address
     * @param msgtext the message text.
     * @return true if the mail was sent successfully , false otherwise.
     */
    protected boolean sendMail(String subject, String from, String to, String msgtext)
    {
        try
        {
            Address[] recipient = InternetAddress.parse(to, false);
            String mailFrom = (String) mailSession.getProperties().get(MAIL_FROM_PROP);
            String addressFromNew = "\"" + from;
            String addressNew = "\" <" + mailFrom + ">";
            String fromAddress = addressFromNew + addressNew;

            // create message
            javax.mail.Message message = new MimeMessage(mailSession);

            message.setSubject(subject);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(javax.mail.Message.RecipientType.TO, recipient);
            message.setSentDate(new Date());
            message.setText(msgtext);

            // Send message
            Transport transport = mailSession.getTransport((String) mailSession.getProperties().get(MAIL_TRANSPORT_PROTOCOL_PROP));
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            Transport.send(message);
        }
        catch (Exception e)
        {
            LOG.error("Could not send mail  ", e);
            return false;
        }
        return true;
    }

    /**
     * Creates a javax.mail.Session.
     */
    private void createSession()
    {
        InitialContext initialContext = null;
        try
        {
            initialContext = new InitialContext();
            mailSession = (Session) initialContext.lookup(JMS_MAIL_SESSION);
        }
        catch (NamingException e)
        {
            LOG.error("Name not found : " + JMS_MAIL_SESSION);
        }
    }

}
