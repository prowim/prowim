/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 22.02.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.rap.rwt.supplemental.fileupload.DiskFileUploadReceiver;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadEvent;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadHandler;
import org.eclipse.rap.rwt.supplemental.fileupload.FileUploadListener;
import org.eclipse.rwt.widgets.FileUpload;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.prowim.datamodel.dms.Document;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;



/**
 * To upload files from local repository to DMS. It shows a simple view with a file chooser and upload button. <br>
 * If file exists it will updated and the version will increased.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.1.0
 */
public class UploadView extends DefaultView
{
    /** ID of view */
    public static final String     ID  = UploadView.class.getName();
    private final Logger           log = Logger.getLogger(UploadView.class);

    private FileUpload             fileUpload;
    private Document               document;
    private Label                  resultLable;
    private Label                  fileNameLabel;
    private DiskFileUploadReceiver receiver;
    private Group                  uploadGroup;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);

        // Set title of View
        this.setPartName(Resources.Frames.Dialog.Actions.ADD_FILE.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Dialog.Actions.ADD_FILE.getImage());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite parent)
    {
        final Composite uploadComp = new Composite(parent, SWT.NONE);
        uploadComp.setLayout(new GridLayout(1, false));
        uploadComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        uploadGroup = new Group(uploadComp, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        uploadGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        uploadGroup.setLayout(new GridLayout(4, false));
        uploadGroup.setText(Resources.Frames.Dialog.Actions.ADD_FILE.getTooltip());

        Label file = new Label(uploadGroup, SWT.LEFT);
        file.setText(Resources.Frames.Global.Texts.FILE.getText() + GlobalConstants.DOUBLE_POINT);

        fileNameLabel = new Label(uploadGroup, SWT.BORDER | SWT.LEFT);
        fileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        fileUpload = new FileUpload(uploadGroup, SWT.NONE);
        fileUpload.setText(Resources.Frames.Dialog.Texts.BROWSE.getText());
        fileUpload.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        fileUpload.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String fileName = fileUpload.getFileName();
                fileNameLabel.setText(fileName == null ? "" : fileName);
            }
        });

        Button uploadButton = new Button(uploadGroup, SWT.PUSH | SWT.RIGHT);
        uploadButton.setText(Resources.Frames.Dialog.Actions.ADD_FILE.getText());
        uploadButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseUp(MouseEvent e)
            {
                uploadFile(uploadComp);
            }
        });

        resultLable = new Label(uploadGroup, SWT.SHADOW_OUT);
        resultLable.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
    }

    private String startUploadReceiver(final Composite uploadComp)
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
                }
                catch (FileNotFoundException e)
                {
                    log.error("Your log-message: ", e);
                }
                catch (IOException e)
                {
                    log.error("Your log-message: ", e);
                }

                document = new Document(event.getFileName(), event.getContentType(), content);

                if ( !MainController.getInstance().uploadDocument(document))
                {
                    MainController.getInstance().updateDocument(document);
                }
            }
        });
        return uploadHandler.getUploadUrl();
    }

    /**
     * Upload the selected file, when OK is pressed.
     * 
     * @throws InterruptedException
     */
    private void uploadFile(Composite uploadComp)
    {
        // Set cursor of wait while upload
        uploadComp.setCursor(org.eclipse.rwt.graphics.Graphics.getCursor(SWT.CURSOR_WAIT));
        resultLable.setText(Resources.Frames.Document.Texts.FILE_UPLOAD_WAIT.getText());
        final String url = startUploadReceiver(uploadComp);
        fileUpload.submit(url);

        resultLable.setText(Resources.Frames.Document.Texts.UPLOAD_FINISHED.getText());

        // Set cursor back of arrow
        uploadComp.setCursor(org.eclipse.rwt.graphics.Graphics.getCursor(SWT.CURSOR_ARROW));
    }

}
