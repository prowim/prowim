/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-23 17:30:30 +0200 (Fr, 23 Jul 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/filesystem/open/OpenFileApp.java $
 * $LastChangedRevision: 4435 $
 *------------------------------------------------------------------------------
 * (c) 18.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
 */
package de.ebcot.prowim.filesystem.open;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import javax.swing.JOptionPane;


/**
 * This application is started from jnlp to open a file in the local filesystem.
 * 
 * @author Saad Wardi
 * @version $Revision: 4435 $
 * @since 2.0.0
 */
public final class OpenFileApp
{

    /**
     * Empty constructor. class only uses static methods.
     */
    private OpenFileApp()
    {
    }

    /**
     * The main method.
     * 
     * @param args arguments.
     */
    public static void main(String[] args)
    {
        if (args != null && args.length > 0)
        {
            try
            {
                openSelectedDocumentPath(URLDecoder.decode(args[0], "UTF-8"));
            }
            catch (Exception e)
            {
                showError(e.getLocalizedMessage());
                return;
            }
        }
        else
        {
            showError("Es ist kein Dokument verfügbar.");
        }
    }

    /**
     * Opens a document from the local filesystem.
     * 
     * @param path the path.
     */
    private static void openSelectedDocumentPath(String path)
    {

        File file = new File(path);
        if (file.exists() && file.canRead())
        {
            try
            {

                if (java.awt.Desktop.isDesktopSupported())
                {
                    java.awt.Desktop.getDesktop().open(file);
                }
                else
                {
                    throw new IllegalArgumentException("Desktop is not supported! ");

                }

            }
            catch (NullPointerException npe)
            {
                showError("Ein unerwarteter Fehler ist aufgetreten: " + npe.getLocalizedMessage());
            }

            catch (IOException e)
            {
                showError("Ein unerwarteter Fehler ist aufgetreten: " + e.getLocalizedMessage());
            }

        }
        else
        {
            showError("Die Datei " + path + " konnte nicht gefunden werden.");
        }
    }

    /**
     * Show error
     * 
     * @param message
     */
    private static void showError(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}
