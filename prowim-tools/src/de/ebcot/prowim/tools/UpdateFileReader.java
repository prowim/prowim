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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.ebcot.tools.logging.Logger;


/**
 * Reads the updatescript from a file.
 * 
 * @author Saad Wardi
 * @version 2.0
 */
public final class UpdateFileReader
{
    private static final Logger LOG = Logger.getLogger(UpdateFileReader.class);

    private UpdateFileReader()
    {

    }

    /**
     * Gets the content of the file.
     * 
     * @param filename a file
     * @return the file content.
     */
    public static String importScriptFromFile(String filename)
    {
        return getContents(filename);
    }

    /**
     * Fetch the entire contents of a text file, and return it in a String. This style of implementation does not throw Exceptions to the caller.
     * 
     * @param aFile is a file which already exists and can be read.
     */
    private static String getContents(String filename)
    {

        File aFile = new File(filename);
        LOG.debug("Found file: " + aFile.getAbsolutePath());
        StringBuilder contents = new StringBuilder();

        try
        {
            // use buffering, reading one line at a time
            // FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try
            {
                String line = null;
                while ((line = input.readLine()) != null)
                {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            }
            finally
            {
                input.close();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return contents.toString();
    }

}
