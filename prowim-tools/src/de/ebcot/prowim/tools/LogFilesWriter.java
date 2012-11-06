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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.Validate;


/**
 * Writes log files.
 * 
 * @author Saad Wardi.
 * @version $Revision: 3677 $
 */
public final class LogFilesWriter
{
    private final static String SEPARATOR = "/";
    private final static String LOGS_DIR  = "logs";

    private LogFilesWriter()
    {

    }

    /**
     * Writes the contents to a text file. If the file not exists, a new file will be created.
     * 
     * @param filename the filename.
     * @param aContents the file content.
     * @param foldername the foldername.
     * @param version the version.
     * @throws IOException if problem encountered during write.
     * */
    static public void setContents(String filename, String aContents, String foldername, String version) throws IOException
    {

        File dirVersion = new File(LOGS_DIR + SEPARATOR + foldername + SEPARATOR + version);

        if ( !dirVersion.exists())
        {
            dirVersion.mkdirs();
        }

        File aFile = new File(LOGS_DIR + SEPARATOR + foldername + SEPARATOR + version + SEPARATOR + filename);
        if ( !aFile.exists())
        {
            aFile.createNewFile();
        }

        // throw new FileNotFoundException("File does not exist: " + aFile);

        if ( !aFile.isFile())
        {
            throw new IllegalArgumentException("Should not be a directory: " + aFile);
        }
        if ( !aFile.canWrite())
        {
            throw new IllegalArgumentException("File cannot be written: " + aFile);
        }

        // use buffering
        Writer output = new BufferedWriter(new FileWriter(aFile.getPath()));
        try
        {
            // FileWriter always assumes default encoding is OK!
            output.write(aContents);
        }
        finally
        {
            output.close();
        }
    }

    /**
     * Writes a file.
     * 
     * @param filename not null filename.
     * @param content not null content.
     * @param foldername not null foldername.
     * @param version not null ontology version.
     */
    public static void writeFile(String filename, String content, String foldername, String version)
    {
        Validate.notNull(filename);
        Validate.notNull(content);
        Validate.notNull(foldername);
        Validate.notNull(version);

        try
        {
            setContents(filename, content, foldername, version);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
