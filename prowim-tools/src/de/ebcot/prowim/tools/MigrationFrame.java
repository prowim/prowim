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
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.prowim.datamodel.collections.UpdateFrameArray;
import org.prowim.datamodel.prowim.OntologyVersion;
import org.prowim.datamodel.prowim.UpdateFrame;
import org.prowim.datamodel.prowim.UpdateWork;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.utils.DateUtility;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.swing.util.UIFactory;



/**
 * 
 * Frame to administer the migration of ontology.
 * 
 * @author Oliver Wolff
 * @version $Revision$
 * @since 1.0
 */
public class MigrationFrame extends JFrame
{
    private static final Logger LOG              = Logger.getLogger(MigrationFrame.class);
    private static final long   serialVersionUID = 1L;
    private static final String NO_CONNECTION    = "Nicht verbunden";

    private final Semaphore     requestSemaphore = new Semaphore(1, true);

    // actions
    private Action              connectAction;
    private Action              startAction, importAction;

    // gui components
    private final JTextField    sourceServer     = new JTextField();
    private final JTextField    targetServer     = new JTextField();
    private final JTextField    sourceVersion    = new JTextField();
    private final JTextField    targetVersion    = new JTextField();
    private final JTextField    serviceName      = new JTextField();
    private JTable              versionTable;
    private JTextArea           migrationScript  = new JTextArea();

    // controller
    private MigrationController sourceControler;
    private MigrationController targetControler;
    private MigrationController updateControler;

    /**
     * Default Constructor.
     */
    public MigrationFrame()
    {
        setLogger();
        ApplicationStarter.setWaitCursor(false, this);
        createActions();
        build();
        init();
        ApplicationStarter.setWaitCursor(false, this);
    }

    /** Initializes the frame with values */
    private void init()
    {
        startAction.setEnabled(true);
        importAction.setEnabled(true);

        sourceServer.setText("localhost:9180");
        targetServer.setText("localhost:9280");
        serviceName.setText("ProWimServices/AdminBean?wsdl");
        sourceVersion.setText(NO_CONNECTION);
        targetVersion.setText(NO_CONNECTION);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn(NO_CONNECTION);
        versionTable.setModel(model);
        versionTable.setEnabled(false);
        migrationScript.setText(NO_CONNECTION);
        migrationScript.setAutoscrolls(true);

    }

    /** Builds the GUI */
    private void build()
    {
        setSize(new Dimension(1024, 768));
        setContentPane(buildContentPane());
        setTitle("Ontologiemigration");
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
        // Create a table
        versionTable = new JTable();
        versionTable.setPreferredScrollableViewportSize(new Dimension(320, 200));
        final JScrollPane scrollPaneVersions = new JScrollPane(versionTable);

        // add listener
        SelectionListener listener = new SelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent arg0)
            {
                if ( !arg0.getValueIsAdjusting())
                {
                    try
                    {
                        setUpdateFrames();
                    }
                    catch (OntologyErrorException e)
                    {
                        JOptionPane.showMessageDialog(scrollPaneVersions, e.getMessage());
                    }
                }
            }
        };

        versionTable.getSelectionModel().addListSelectionListener(listener);
        // versionTable.getColumnModel().getSelectionModel().addListSelectionListener(listener);

        migrationScript = new JTextArea();
        migrationScript.setBorder(LineBorder.createGrayLineBorder());
        migrationScript.setAutoscrolls(true);

        // create and set the panel
        FormLayout layout = new FormLayout("right:p, 4dlu, fill:p:grow, 4dlu, fill:p, 4dlu, fill:p:grow", "p");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);

        builder.setDefaultDialogBorder();
        builder.appendSeparator("Einstellungen zum Upgrade");
        builder.nextLine();
        builder.append(new JLabel("Quellserver:"));
        builder.append(sourceServer);
        builder.append(new JLabel("Quellversion:"));
        builder.append(sourceVersion);
        builder.nextLine();
        builder.append(new JLabel("Zielserver:"));
        builder.append(targetServer);
        builder.append(new JLabel("Zielversion:"));
        builder.append(targetVersion);
        builder.nextLine();
        builder.append(new JLabel("Servicename:"));
        builder.append(serviceName, 5);
        builder.nextLine();
        builder.append(new JLabel("Quell- und Zielserver:"));
        builder.append(new JButton(connectAction), 5);
        builder.nextLine();
        builder.appendSeparator("Neue Versionen des Quellservers");
        builder.nextLine();
        builder.append(scrollPaneVersions, 7);
        builder.nextLine();
        builder.appendSeparator("Ausführung des Upgrades");
        builder.nextLine();
        builder.append(new JLabel("Änderungen:"));
        builder.append(migrationScript, 5);
        builder.nextLine();
        builder.append(new JLabel("Migration:"));
        builder.append(new JButton(startAction), 5);
        builder.nextLine();
        builder.append(new JLabel("Ersetze Wiki URLs:"));
        builder.append(new JButton(importAction), 5);
        builder.nextLine();

        return builder.getPanel();
    }

    /** Creates the needed actions for buttons etc. */
    private void createActions()
    {
        connectAction = ApplicationStarter.getAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doConnect();
                doConnectForImport();
            }
        }, "VERBINDEN", null, null, null, null);

        startAction = ApplicationStarter.getAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doMigration();

            }
        }, "STARTEN", null, null, null, null);

        importAction = ApplicationStarter.getAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doImport();

            }
        }, "Import", null, null, null, null);
    }

    /** starts the complete batch process for upgrade */
    private void doMigration()
    {
        try
        {
            doUpdate();
            JOptionPane.showMessageDialog(this, "Migration ist erfolgreich durchlaufen.\nAchten Sie bitte auf die erzeugten Logdateien!");
        }
        catch (OntologyErrorException e)
        {
            LOG.error(e);
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /** starts the complete batch process for upgrade */
    private void doImport()
    {
        boolean status = executeScript();
        if (status)
            JOptionPane.showMessageDialog(this, "Import ist erfolgreich durchlaufen.\nAchten Sie bitte auf die erzeugten Logdateien!");
        else
            JOptionPane.showMessageDialog(this, "Fehler beim Import!");
    }

    /** Connects to the source- and target-server to read version history ProWimServices/AdminBean?wsdl */
    private void doConnect()
    {
        String sourceServerName = this.sourceServer.getText();
        String targetServerName = this.targetServer.getText();
        String wsdlSuffix = this.serviceName.getText();
        String sourceWsdl = "http://" + sourceServerName + "/" + wsdlSuffix;
        String targetWsdl = "http://" + targetServerName + "/" + wsdlSuffix;

        sourceControler = new MigrationController();
        targetControler = new MigrationController();
        try
        {
            sourceControler.connect(sourceWsdl);
            String sourceVersionLabel = sourceControler.getLastVersion();
            sourceVersion.setText(sourceVersionLabel);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Could not connect to " + sourceWsdl + "\n\nException message: " + e.getMessage());
            System.out.println(e);
        }

        try
        {
            targetControler.connect(targetWsdl);
            String targetVersionLabel = targetControler.getLastVersion();
            targetVersion.setText(targetVersionLabel);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Could not connect to " + targetWsdl + "\n\nException message: " + e.getMessage());
            System.out.println(e);
        }
        try
        {
            checkInvalidConnect();
        }
        catch (OntologyErrorException e)
        {
            JOptionPane.showMessageDialog(this, "Could not connect to ontology\n\nException message: " + e.getMessage());
            System.out.println(e);
        }
    }

    /** Connects to the source- and target-server to read version history */
    private void doConnectForImport()
    {
        String wsdlSUFFIX = "ProWimServices/ProWimOntologyBean?wsdl";
        String sourceServerName = this.sourceServer.getText();
        String wsdlSuffix = wsdlSUFFIX;
        String sourceWsdl = "http://" + sourceServerName + "/" + wsdlSuffix;

        updateControler = new MigrationController();
        try
        {

            System.out.println();
            updateControler.connecAccess(sourceWsdl);

            //

        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // sets the values in the source table
    private void setSourceVersions() throws OntologyErrorException
    {
        DefaultTableModel model = new DefaultTableModel();

        // Create a couple of columns
        model.addColumn("Version");
        model.addColumn("Datum");
        model.addColumn("Author");

        List<OntologyVersion> versions = sourceControler.getAllVersions();

        // iterate the complete results from service
        Iterator<OntologyVersion> iterator = versions.iterator();
        while (iterator.hasNext())
        {
            OntologyVersion version = iterator.next();
            String versionLabel = version.getLabel();
            String versionCreateTime = version.getCreateTime();
            String versionCreator = version.getCreator();
            // set date
            // set date as DateTime object
            DateTime dt = new DateTime();
            DateTimeFormatter fmt = DateTimeFormat.fullDate().withLocale(Locale.GERMAN);
            String dateStamp = fmt.print(dt);

            // set author
            String author = versionCreator;
            dateStamp = versionCreateTime;

            // check if version is equals target version
            // if (version.getLabel().equals(targetVersion.getText()))
            // break;

            // Append a row
            VersionLabelID labelID = new VersionLabelID(versionLabel, version.getID());
            String targetActualVersion = this.targetVersion.getText();

            if (isVersionHigher(versionLabel, targetActualVersion))
                model.addRow(new Object[] { labelID, dateStamp, author });
        }
        versionTable.setModel(model);
        versionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        versionTable.setRowSelectionInterval(0, 0);
    }

    /**
     * Needded to get the label and the id of a version from the tablemodel.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision$
     */
    private class VersionLabelID
    {
        private final String label;
        private final String id;

        VersionLabelID(String label, String id)
        {
            this.id = id;
            this.label = label;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return label;
        }
    }

    /**
     * checks if all connection and entries are valid of source and target
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */

    private void checkInvalidConnect() throws OntologyErrorException
    {
        if (sourceVersion.getText().equals(NO_CONNECTION) || targetVersion.getText().equals(NO_CONNECTION))
            JOptionPane.showMessageDialog(this, "Die Quell- oder Zielversion konnte nicht ausgelesen werden.", "Fehler", JOptionPane.ERROR_MESSAGE);
        else
        {
            // if valid check upgrade possibility
            if ( !isVersionHigher(sourceVersion.getText(), targetVersion.getText()))
            {
                JOptionPane.showMessageDialog(this, "Die Quellversion ist gleich oder niedriger als die Zielversion.", "Fehler",
                                              JOptionPane.ERROR_MESSAGE);
                startAction.setEnabled(false);
            }
            else
            {
                startAction.setEnabled(true);
                setSourceVersions();
            }
        }
    }

    /** Checks if first parameter is numerical higher than second parameter */
    private boolean isVersionHigher(String higherText, String lowerText)
    {
        String[] versionHigh = StringUtils.split(higherText, ".");
        String[] versionLow = StringUtils.split(lowerText, ".");

        // can only check same lenght of version strings
        if (versionHigh.length != versionLow.length)
            return false;

        // check all dots
        for (int i = 0; i < versionLow.length; i++)
        {
            try
            {
                Integer valueLow = Integer.parseInt(versionLow[i]);
                Integer valueHigh = Integer.parseInt(versionHigh[i]);
                if (valueLow > valueHigh)
                    return false;
                else if (valueHigh > valueLow)
                    return true;
                // if else iterate next step (check next dot)
            }
            catch (NumberFormatException nfe)
            {
                JOptionPane.showMessageDialog(this, "Eine der Quell- oder Zielversion ist nicht numerisch.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // both versions must be equal, so return false
        return false;
    }

    /** listener for changes of table */
    private class SelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            // Row selection changed
            int selection = e.getFirstIndex();
            if (selection == 0)
                migrationScript.setText("Algernon1\n\nAlgernon2\n\nAlgernon3\n\netc. - " + selection);
            else
                migrationScript.setText("Algernon1\n\netc. - " + selection);
        }

    }

    /**
     * Sets the update frames.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void setUpdateFrames() throws OntologyErrorException
    {
        final int selectedRow = versionTable.getSelectedRow();
        if (selectedRow >= 0)
        {
            final VersionLabelID labelID = (VersionLabelID) versionTable.getModel().getValueAt(selectedRow, 0);
            final UpdateFrameArray updateFrames = sourceControler.getUpdateFrames(labelID.id);
            final Iterator<UpdateFrame> iterator = updateFrames.iterator();
            this.migrationScript.setText("");
            while (iterator.hasNext())
            {
                UpdateFrame frame = iterator.next();
                this.migrationScript.append(frame.getName() + "\n");
            }
        }
    }

    /**
     * do update.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    private void doUpdate() throws OntologyErrorException
    {
        // setInvalid
        waitForTicket();
        // targetControler.setVersionInvalid();
        boolean status = true;
        int selectedRow = versionTable.getSelectedRow();
        int rowCount = versionTable.getRowCount();
        if (rowCount - 1 >= selectedRow)
        {

            for (int i = rowCount - 1; i >= selectedRow; i--)
            {
                final VersionLabelID labelID = (VersionLabelID) versionTable.getModel().getValueAt(i, 0);
                final UpdateWork updateWork = sourceControler.getUpdateWork(labelID.id);
                status = targetControler.executeUpdate(updateWork, this.targetServer.getText());
                if ( !status)
                {
                    LOG.error(updateWork.getUpdateScript());
                    JOptionPane.showMessageDialog(this,
                                                  "Ein Fehler ist aufgetreten bei der Aktualisierung des item : " + updateWork.getUpdateScript(),
                                                  "Fehler", JOptionPane.ERROR_MESSAGE);

                    break;
                }
            }
        }

        // setValid if the status ok
        // if (status)
        // targetControler.setVersionValid();
        removeTicket();

    }

    /** execute the import script. */
    private boolean executeScript()
    {
        LOG.debug("Start execute WIKI update");
        // this.updateControler.updateWIKI();
        return true;
    }

    /**
     * Acquires the {@link #requestSemaphore}. returns as soon as the resource is free.
     */
    private void waitForTicket()
    {
        try
        {
            this.requestSemaphore.acquire();
        }
        catch (InterruptedException e)
        {
            LOG.info("Interrupted while waiting for semaphore", e);
        }
    }

    /**
     * releases the acquired semaphore
     */
    private void removeTicket()
    {
        this.requestSemaphore.release();
    }

    /**
     * Sets the logger file appender.
     */
    private void setLogger()
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
    }

}
