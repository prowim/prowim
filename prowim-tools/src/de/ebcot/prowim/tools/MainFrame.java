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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.util.UIFactory;


/**
 * 
 * Frame to choose the tools.
 * 
 * @author Oliver Wolff
 * @version $Revision$
 * @since 1.0
 */
public class MainFrame extends JFrame
{

    // actions
    private Action migrationAction;
    private Action cleaningAction;
    private Action closeAction;

    /**
     * Default Constructor.
     */
    public MainFrame()
    {
        ApplicationStarter.setWaitCursor(false, this);
        createActions();
        build();
        init();
        ApplicationStarter.setWaitCursor(false, this);
    }

    /** Initializes the frame with values */
    private void init()
    {
        // TODO $Author$ Auto-generated method stub
    }

    /** Builds the GUI */
    private void build()
    {
        setSize(new Dimension(320, 240));
        setContentPane(buildContentPane());
        setTitle("ProWim Tools " + getVersionText());
    }

    /** Builds the content pane of the frame */
    private JPanel buildContentPane()
    {
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(ApplicationStarter.BORDER_EMPTY);
        mainPanel.add(UIFactory.createStrippedScrollPane(buildContent()));

        return mainPanel;
    }

    /** builds the content panel */
    private JPanel buildContent()
    {
        // create and set the panel
        FormLayout layout = new FormLayout("right:p, 4dlu, fill:p:grow", "p");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);

        builder.setDefaultDialogBorder();
        builder.appendSeparator("Hauptmenü");
        builder.nextLine();
        builder.append(new JLabel("Ontologieupgrade:"));
        builder.append(new JButton(migrationAction));
        builder.nextLine();
        builder.append(new JLabel("Aufräumen:"));
        builder.append(new JButton(cleaningAction));
        builder.nextLine();
        builder.append(new JLabel("Anwendung:"));
        builder.append(new JButton(closeAction));
        builder.nextLine();

        return builder.getPanel();
    }

    /**
     * Version string of this software.
     * 
     * @return non null version string like 1.0
     */
    private String getVersionText()
    {
        return "1.0";
    }

    /** Creates the needed actions for buttons etc. */
    private void createActions()
    {
        migrationAction = ApplicationStarter.getAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doMigration();
            }

            private void doMigration()
            {
                new MigrationFrame().setVisible(true);
            }
        }, "STARTEN", null, null, null, "Upgrade von diversen Ontologieversionen.");

        cleaningAction = ApplicationStarter.getAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doCleaning();
            }

            private void doCleaning()
            {
                new CleaningFrame().setVisible(true);
            }
        }, "STARTEN", null, null, null, "R�umt die Ontologie auf bzgl. fehlerhafter Datens�tze.");

        closeAction = ApplicationStarter.getAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        }, "BEENDEN", null, null, null, "Beendet die gesamte Anwendung.");
    }
}
