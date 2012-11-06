/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:35 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/FileDialog.java $
 * $LastChangedRevision: 5031 $
 *------------------------------------------------------------------------------
 * (c) 17.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.dialogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.rap.rwt.supplemental.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadEvent;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadHandler;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadListener;
import org.eclipse.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.prowim.datamodel.dms.Document;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;



/**
 * To create this dialog call openDialog method. Please attention, that you need the package <br/>
 * org.eclipse.rwt.widgets.upload to use this dialog.<br/>
 * The method upload.getPath() returns only the file name for Firefox 3.<br/>
 * This class is not finished. You have to implement it with DMS and test it again.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5031 $
 */
public final class FileDialogProWim extends DefaultDialog
{
    private final Logger           log = Logger.getLogger(FileDialogProWim.class);
    private Document               document;
    private FileUpload             fileUpload;
    private Composite              actParent;
    private Label                  fileNameLabel;
    private DiskFileUploadReceiver receiver;

    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     */
    public FileDialogProWim(final Shell parentShell, final Action action, String description)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage());
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Composite createCustomArea(Composite parent)
    {
        actParent = parent;
        Composite control = super.createCustomArea(parent);
        (control).setLayout(new GridLayout(3, false));
        Label file = new Label(control, SWT.RIGHT);
        file.setText(Resources.Frames.Global.Texts.FILE.getText() + GlobalConstants.DOUBLE_POINT);

        fileNameLabel = new Label(control, SWT.BORDER | SWT.LEFT);
        fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        fileUpload = new FileUpload(control, SWT.NONE);
        fileUpload.setText(Resources.Frames.Dialog.Texts.BROWSE.getText());
        fileUpload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        fileUpload.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String fileName = fileUpload.getFileName();
                fileNameLabel.setText(fileName == null ? "" : fileName);
                uploadFile();
            }
        });

        return control;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        uploadFile();
        setReturnCode(OK);
        close();
    }

    /**
     * Upload the selected file, when OK is pressed.
     * 
     * @throws InterruptedException
     */
    private void uploadFile()
    {
        // Set cursor of wait while upload
        this.actParent.setCursor(org.eclipse.rwt.graphics.Graphics.getCursor(SWT.CURSOR_WAIT));

        final String url = startUploadReceiver();
        fileUpload.submit(url);

        // Set cursor back of arrow
        this.actParent.setCursor(org.eclipse.rwt.graphics.Graphics.getCursor(SWT.CURSOR_ARROW));
    }

    /**
     * 
     * Returns the name of uploaded file.
     * 
     * @return Downloaded file. Not null.
     */
    public Document getUploadedDocumnet()
    {
        return this.document;
    }

    private String startUploadReceiver()
    {
        receiver = new DiskFileUploadReceiver();
        FileUploadHandler uploadHandler = new FileUploadHandler(receiver);
        uploadHandler.addUploadListener(new FileUploadListener()
        {
            public void uploadProgress(FileUploadEvent event)
            {
            }

            public void uploadFailed(FileUploadEvent event)
            {
            }

            public void uploadFinished(FileUploadEvent event)
            {
                byte[] content = null;
                InputStream fis;
                try
                {
                    File targetFile = receiver.getTargetFile();
                    String absolutePath = targetFile.getAbsolutePath();
                    fis = new FileInputStream(absolutePath);
                    content = new byte[(int) event.getBytesRead()];
                    fis.read(content);
                    fis.close();

                    document = new Document(event.getFileName(), event.getContentType(), content);
                }
                catch (FileNotFoundException e)
                {
                    log.error("Your log-message: ", e);
                }
                catch (IOException e)
                {
                    log.error("Your log-message: ", e);
                }

            }
        });
        return uploadHandler.getUploadUrl();
    }

}
