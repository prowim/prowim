/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-22 20:42:21 +0200 (Do, 22 Jul 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/ejb/process/RepositoryHelper.java $
 * $LastChangedRevision: 4415 $
 *------------------------------------------------------------------------------
 * (c) 27.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.ejb.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.dms.Document;
import org.prowim.resources.ResourcesLocator;

import de.ebcot.tools.logging.Logger;


/**
 * Downloads documents from DMS and unzip them into the prowim repository. NOTE: the prowim repository is a directory under apache Webserver.<br>
 * The used path is $JBOSS_HOME/etc/repository/
 * 
 * Approach:<br>
 * 1. create the dir with same name as the zip file.<br>
 * 2. unzip the zip file there<br>
 * 3. generate the url.
 * 
 * 
 * @author Saad Wardi
 * @version $Revision: 4415 $
 */
public class RepositoryHelper
{
    private static final String ARCHIVE_EXTENSION  = ".zip";
    private static final String PATH_SEPARATOR     = "/";
    private static final String HTM_FILE_EXTENSION = ".htm";
    private final static String PARENT_URL         = "http://" + ResourcesLocator.getServerHost() + "/repository";
    private final static String PROCESSES_CHART    = PARENT_URL + "/" + "processes_charts/";
    private static final Logger LOG                = Logger.getLogger(RepositoryHelper.class);
    private Document            document           = null;
    private String              path               = null;
    private String              htmFileName;

    /**
     * Creates a RepositoryHelper and initialize it with a {@link Document}.
     * 
     * @param document not null {@link Document} that contains the byte to be stored as a file.
     */
    protected RepositoryHelper(Document document)
    {
        Validate.notNull(document);
        this.document = document;
        this.path = createDirs();
        storeFile(document, path);
        LOG.info("PATH  " + path + "/" + document.getName());
        zipExtract(path + PATH_SEPARATOR + document.getName(), path + PATH_SEPARATOR);
    }

    /**
     * Generates the url to the chart represents the process as image.
     * 
     * @return the chart url.
     */
    public String generateChartURL()
    {
        String[] items = document.getName().split(ARCHIVE_EXTENSION);
        String nodeName = null;
        if (items != null && items.length > 0)
        {
            nodeName = items[0];
        }
        else
        {
            throw new IllegalStateException("Bad archive name " + document.getName());
        }
        if (nodeName != null)
        {
            return PROCESSES_CHART + nodeName + PATH_SEPARATOR + htmFileName;
        }
        else
        {
            throw new IllegalStateException("Bad archive name " + nodeName);
        }

    }

    /**
     * Extracts the content of the zip file.
     * 
     * @param zipFile the zipfile path
     * @param targetPath the target path , where the zip file will be extracted.
     */
    private void zipExtract(String zipFile, String targetPath)
    {
        OutputStream out = null;
        try
        {
            ZipInputStream in1 = new ZipInputStream(new FileInputStream(zipFile));
            ZipFile zf1 = new ZipFile(zipFile);
            int a = 0;
            for (Enumeration<? > em2 = zf1.entries(); em2.hasMoreElements();)
            {
                String folderName;
                em2.nextElement().toString();
                ZipEntry ze = in1.getNextEntry();

                String afileName = ze.getName();
                String[] items = afileName.split(PATH_SEPARATOR);
                if (items != null && items.length > 0)
                {
                    folderName = items[0];
                }
                else
                {
                    items = afileName.split("\\");
                    if (items != null && items.length > 0)
                    {
                        folderName = items[0];
                    }
                    else
                    {
                        throw new IllegalStateException("Could not create file  " + afileName);
                    }
                }
                File parentDir = new File(targetPath + folderName);
                if ( !parentDir.exists())
                {
                    boolean success = parentDir.mkdir();
                    if ( !success)
                    {
                        throw new IllegalStateException("Could not create parent directory  " + parentDir.getName());
                    }
                }
                in1.close();
                zf1.close();
                break;
            }

            // now unzip all files
            ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
            ZipFile zf = new ZipFile(zipFile);
            for (Enumeration<? > em = zf.entries(); em.hasMoreElements();)
            {
                String targetfile = em.nextElement().toString();
                in.getNextEntry();
                LOG.info("TARGET FILE  " + targetfile);
                if (targetfile.endsWith(HTM_FILE_EXTENSION))
                {
                    htmFileName = targetfile;
                }
                out = new FileOutputStream(targetPath + targetfile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }
                a = a + 1;
            }
            if (a > 0)
            {
                LOG.debug("Files unzipped.");
            }
            out.close();
            in.close();
            zf.close();
            File toDelete = new File(zipFile);
            boolean status = toDelete.delete();
            LOG.debug("Delete zip file  " + status);
        }
        catch (FileNotFoundException e)
        {
            LOG.error("Could not found file  " + e);
        }
        catch (IOException e)
        {
            LOG.error("Error: Operation failed!");
        }

    }

    /**
     * Stores the downloaded document on the repository.
     * 
     * @param doc not null {@link Document}.
     * @param path not null existing path on the server file system.
     */
    private void storeFile(final Document doc, final String path)
    {
        File f = new File(path + "/" + doc.getName());
        FileOutputStream fout;
        try
        {
            fout = new FileOutputStream(f);
            fout.write(doc.getContent(), 0, new Long(doc.getSize()).intValue());
            fout.close();
        }
        catch (FileNotFoundException e)
        {
            LOG.error("FileNotFoundException: ", e);
        }
        catch (IOException e)
        {
            LOG.error("IOException: ", e);
        }
    }

    /**
     * Creates the directory where the content of the zip will be reposed.
     * 
     * @return the path.
     */
    private String createDirs()
    {

        String path = ResourcesLocator.getRepositoryProcessChartDir();
        String fileName = document.getName();
        String[] items = fileName.split(ARCHIVE_EXTENSION);
        String dirName = fileName;
        if (items != null && items.length > 0)
        {
            dirName = items[0];
        }
        path += PATH_SEPARATOR + dirName;
        File file = new File(path);
        boolean exists = file.exists();
        if (exists)
        {
            LOG.info("Directory exists  : " + path);
        }
        else
        {
            LOG.info("Directory does not exists creating  " + path);
            File newFile = new File(path);
            boolean success = newFile.mkdir();
            if (success)
            {
                LOG.debug("Directory created  ");

            }
            else
            {
                throw new IllegalStateException("Could not create directory  " + path);
            }
        }
        return path;
    }

    /**
     * Deletes a directory.
     * 
     * @param dir the directory
     * @return true if success, otherwise throws an IllegalStateException.
     */
    @SuppressWarnings("unused")
    private boolean deleteDir(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if ( !success)
                {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

}
