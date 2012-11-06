/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
 *
 */

package de.ebcot.prowim.tools;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.prowim.utils.DateUtility;



/**
 * 
 * Class to start the ProWim-Tools.
 * 
 * @author Oliver Wolff
 * @version $Revision$
 * @since 1.0
 */
public final class ApplicationStarter
{
    /** EMPTY_BORDER_ */
    public static final int     FRAME_EMPTY_BORDER = 10;
    /** empty default border */
    public static final Border  BORDER_EMPTY       = new EmptyBorder(FRAME_EMPTY_BORDER, FRAME_EMPTY_BORDER, FRAME_EMPTY_BORDER, FRAME_EMPTY_BORDER);
    private static final Logger LOG                = Logger.getLogger(ApplicationStarter.class);                                                     // Create a Logger for Debugging

    private ApplicationStarter()
    {

    }

    // empty border in inner frames

    /**
     * Starting class for the ProWim-Tools.
     * 
     * @param args if needed, just now unsupported - no function
     */
    public static void main(String[] args)
    {

        PatternLayout layout = new PatternLayout("%d{ISO8601} %-5p [%t] %c: %m%n");
        DailyRollingFileAppender fileAppender = null;
        try
        {
            String now = new DateUtility().getActDate().replace('.', '_').replace(':', '_');
            fileAppender = new DailyRollingFileAppender(layout, "logs/errors" + now + ".log", "'.'yyyy-MM-dd_HH-mm");
            LOG.addAppender(fileAppender);
            LOG.setLevel(Level.ALL);

        }
        catch (IOException e)
        {
            LOG.error("IOException: ", e);
        }
        LOG.debug("Starting MainFrame...");
        new MainFrame().setVisible(true);
    }

    /**
     * Action with given listener registered.
     * 
     * @param actionListener Action listener.
     * @param label to show non null
     * @param mnemonic to show, null possible
     * @param accelerator to show, null possible
     * @param icon to show, null possible
     * @param tooltip to show, null possible
     * @return Action non null
     */
    public static Action getAction(final ActionListener actionListener, String label, Integer mnemonic, KeyStroke accelerator, Icon icon,
                                   String tooltip)
    {
        if (actionListener == null)
        {
            throw new IllegalArgumentException("actionListener is null" /* NOI18N */);
        }

        // create action for listener
        javax.swing.Action action = new AbstractAction()
        {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e)
            {
                actionListener.actionPerformed(e);
            }
        };

        // set properties for action
        action.putValue(javax.swing.Action.NAME, label);

        if (mnemonic != null)
        {
            action.putValue(javax.swing.Action.MNEMONIC_KEY, mnemonic);
        }

        if (accelerator != null)
        {
            action.putValue(javax.swing.Action.ACCELERATOR_KEY, accelerator);
        }

        if (icon != null)
        {
            action.putValue(javax.swing.Action.SMALL_ICON, icon);
        }

        if (tooltip != null)
        {
            action.putValue(javax.swing.Action.SHORT_DESCRIPTION, tooltip);
        }

        return action;
    }

    /**
     * Sets the wait cursor
     * 
     * @param showWaitCursor boolean
     * @param frame the frame which calls the method
     */
    public static void setWaitCursor(boolean showWaitCursor, JFrame frame)
    {
        if (showWaitCursor)
        {
            frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        }
        else
        {
            frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
