/*==============================================================================
 * File:        MailMessenger.java
 * Project:     BSC-Tool LISA
 *
 * Last change: date:       $Date: 2008-11-18 16:52:41 +0100 (Di, 18 Nov 2008) $
 *              by:         $Author: wiesner $
 *              revision:   $Revision: 13336 $
 *------------------------------------------------------------------------------
 * Copyright:   Copyright 2004 Ebcot GmbH  All rights reserved.
 *              Use is subject to license terms.
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
package org.prowim.tools.mail;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import de.ebcot.tools.logging.Logger;


/**
 * The bean implementation of the {@link MailMessengerService mail service}
 * 
 * @author Thomas Wiesner, Sven Kiesow
 * @version $Revision: 13336 $
 */
@Stateless
public class MailMessengerBean implements MailMessengerService
{

    // ~ Static fields/initializers -------------------------------------------------------------------------------------------------------------------

    /**
     * The constant for the property key of mail session configuartion in deployed mail-service.xml.
     */
    private static final String MAIL_FROM_KEY = "mail.from";

    /**
     * The instance of the logger
     */
    private static final Logger LOG           = Logger.getLogger(MailMessengerBean.class);

    /**
     * The format of the mail content
     */
    private static final String MAIL_FORMAT   = "text/plain";

    /**
     * Address of the sender
     */
    private InternetAddress     fromAddress   = null;

    /**
     * Subject of the Message
     */
    private String              subject       = "";

    /**
     * Footer for the Message
     */
    private String              footer        = "";

    // ~ Instance fields ------------------------------------------------------------------------------------------------------------------------------

    /**
     * Bulk of messages, where the key is the receiver
     */
    private final Hashtable     messageToSend = new Hashtable();

    /**
     * the instance of the mail session
     */
    @Resource(mappedName = "java:/Mail")
    private Session             mailSession;

    // ~ Constructors ---------------------------------------------------------------------------------------------------------------------------------

    /**
     * Constructs this service
     */
    public MailMessengerBean()
    {
    }

    // ~ Methods --------------------------------------------------------------------------------------------------------------------------------------

    // Set the Subject used by the MailMessenger
    private void setSubject(String subject)
    {
        this.subject = subject;
    }

    // Set the Footer used by the MailMessenger
    private void setFooter(String footer)
    {
        this.footer = footer;
    }

    /**
     * 
     * Initializes the mail address of the sender from the configured mail session.
     */
    @PostConstruct
    private void initializeFromMailAdress()
    {
        String fromAddress = mailSession.getProperty(MAIL_FROM_KEY);
        try
        {
            this.fromAddress = new InternetAddress(fromAddress);
        }
        catch (AddressException e)
        {
            LOG.error("The adress of the sender " + fromAddress + " are wrong", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.tools.mail.MailMessengerService#addMessages(java.lang.String, java.lang.String[], String, String)
     */
    @Override
    public void addMessages(String content, String[] receiver, String subject, String footer)
    {
        this.setSubject(subject);
        this.setFooter(footer);

        LOG.debug("addMessages with content: " + content);
        for (int i = 0; i < receiver.length; i++)
        {
            if (receiver[i] != null)
            {
                LOG.debug("processing message for receiver: " + receiver[i]);
                if (messageToSend.get(receiver[i]) == null)
                {
                    messageToSend.put(receiver[i], createMessage(content));
                }
                else
                {
                    MimeMessage tempMsg = (MimeMessage) messageToSend.get(receiver[i]);

                    try
                    {
                        String mailContent = (String) tempMsg.getContent() + content;
                        tempMsg.setContent(mailContent, MAIL_FORMAT);
                        messageToSend.put(receiver[i], tempMsg);
                    }
                    catch (IOException e)
                    {
                        LOG.error(e, e);
                    }
                    catch (MessagingException e)
                    {
                        LOG.error(e, e);
                    }
                }
            }
        }
    }

    /**
     * Creates a single message for an configured messenger
     * 
     * @param content content of the message
     * @return see main description
     */
    private MimeMessage createMessage(String content)
    {
        LOG.debug("createMessage(): \n" + content);

        MimeMessage msg = new MimeMessage(mailSession);

        try
        {
            msg.setFrom(fromAddress);
            msg.setSubject(subject);
            msg.setContent(content, MAIL_FORMAT);
        }
        catch (AddressException e)
        {
            LOG.error("Wrong address: ", e);
        }
        catch (MessagingException e)
        {
            LOG.error("Wrong message: ", e);
        }

        return msg;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.tools.mail.MailMessengerService#sendAllMessages()
     */
    @Override
    public void sendAllMessages()
    {
        LOG.debug("sendAllMessages()");

        java.util.Date senddate = new java.util.Date();

        try
        {
            Set keys = messageToSend.keySet();
            Iterator messages = keys.iterator();
            while (messages.hasNext())
            {
                String item = (String) messages.next();
                MimeMessage sendmsg = (MimeMessage) messageToSend.get(item);
                Address[] address = { new InternetAddress(item) };
                sendmsg.setRecipients(javax.mail.Message.RecipientType.TO, address);
                sendmsg.setSentDate(senddate);
                sendmsg.setContent(sendmsg.getContent() + "\n\n" + footer, MAIL_FORMAT);
                Transport.send(sendmsg, address);
            }
        }
        catch (AddressException e)
        {
            LOG.error(e, e);
        }
        catch (NoSuchProviderException e)
        {
            LOG.error(e, e);
        }
        catch (MessagingException e)
        {
            LOG.error(e, e);
        }
        catch (IOException e)
        {
            LOG.error(e, e);
        }
        messageToSend.clear();
    }
}