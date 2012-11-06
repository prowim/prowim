/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 05.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.dms.Version;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.dialog.PersonController;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.dialogs.knowledge.KnowledgeLinkTemp;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.model.KnowledgeLinkLeaf;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.tables.KnowledgeLinkTableViewer;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.utils.GlobalFunctions;
import org.prowim.portal.view.knowledge.open.DownloadFile;
import org.prowim.portal.view.knowledge.open.OpenKnowledgeLink;
import org.prowim.rap.framework.components.ButtonFactory;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidatedTextField;



/**
 * Dialog to add or edit a knowledge object and its related knowledge links.
 * 
 * @author Oliver Specht
 * @version $Revision$
 */
public class KnowledgeObjectDialog extends DefaultDialog
{
    private final static Logger                     LOG                    = Logger.getLogger(KnowledgeObjectDialog.class);

    // text fields
    private Text                                    textAreaKnowledgeObjectDescription;
    private ValidatedTextField                      textFieldDenotation;
    private Text                                    textFieldKnowledgeLinkName;
    private Text                                    textFieldKnowledgeLinkURL;
    private Text                                    textFieldResponsiblePerson;

    private PersonArray                             personArray;
    private StringArray                             keyWords;
    private List<KnowledgeLinkTemp>                 knowledgeTempLinks;

    private KnowledgeObject                         currentKnowledgeObject;

    private String                                  denotationTxtOut       = null;
    private String                                  currentDes             = null;
    private String                                  currentKeyWords        = "";

    // actions
    private Action                                  actionAddKnowledgeLink;
    private Action                                  actionAddPerson;
    private Action                                  actionDeleteKnowledgeLink;
    private Action                                  actionEditKnowledgeLink;
    private Action                                  actionOpenURL;
    private Action                                  actionShowKnowledgeLinkDetails;
    private Action                                  actionUploadDocument;

    private KnowledgeLinkTableViewer                tableViewerKnowledgeLinks;

    private ScrolledComposite                       scrolledCompositeKnowledgeLinkList;

    private Combo                                   comboKnowledgeRepository;

    private Button                                  buttonOpenURL;
    private Button                                  buttonEditKnowledgeLink;
    private Button                                  buttonUploadDocument;
    private Button                                  buttonShowKnowledgeLinkDetails;
    private Button                                  buttonDeleteKnowledgeLink;

    private Group                                   groupKnowledgeLinkDetails;

    private final Map<Integer, KnowledgeRepository> knowledgeRepositoryMap = new HashMap<Integer, KnowledgeRepository>();
    private KnowledgeRepository                     defaultRepository      = null;
    private DocumentContentProperties               documentContentProperties;
    private Version                                 documentVersion;
    private boolean                                 initializing           = false;

    private List<KnowledgeLink>                     knowledgeLinkList      = new ArrayList<KnowledgeLink>();
    private String                                  incomingObjectID       = "";
    private Text                                    textFieldKeyWords;

    /**
     * Constructor.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     */
    public KnowledgeObjectDialog(Shell parentShell, Action action, String description)
    {
        super(parentShell, action, description);
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
        final Composite control = super.createCustomArea(parent);

        createActions();

        createKnowledgeObjectArea(control);

        createKnowledgeLinkArea(control);

        return control;
    }

    /**
     * Creates the area where the knowledge links are shown.
     * 
     * @param control the {@link Control} to create the area on, may not be null
     */
    private void createKnowledgeLinkArea(Control control)
    {
        Validate.notNull(control);

        // group to show all knowledge links including information and buttons for adding, deleting etc.
        Group groupKnowledgeLinks = new Group((Composite) control, SWT.SHADOW_IN | SWT.RIGHT);
        groupKnowledgeLinks.setText(Resources.Frames.Knowledge.Texts.EXIST_LINK_OF_KNOWLEDGE.getText());
        groupKnowledgeLinks.setLayoutData(new GridData(GridData.FILL_BOTH));
        groupKnowledgeLinks.setLayout(new GridLayout(3, false));

        // create button to show knowledge link details
        buttonShowKnowledgeLinkDetails = ButtonFactory.create(groupKnowledgeLinks, actionShowKnowledgeLinkDetails);
        buttonShowKnowledgeLinkDetails.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        buttonShowKnowledgeLinkDetails.setText("");
        buttonShowKnowledgeLinkDetails.setEnabled(false);

        // create button to add knowledge link
        Button buttonAddKnowledgeLink = ButtonFactory.create(groupKnowledgeLinks, actionAddKnowledgeLink);
        buttonAddKnowledgeLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        buttonAddKnowledgeLink.setText("");

        // Create button to delete selected knowledge link
        buttonDeleteKnowledgeLink = ButtonFactory.create(groupKnowledgeLinks, actionDeleteKnowledgeLink);
        buttonDeleteKnowledgeLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        buttonDeleteKnowledgeLink.setText("");
        buttonDeleteKnowledgeLink.setEnabled(false);

        // group to show list of knowledge links
        scrolledCompositeKnowledgeLinkList = new ScrolledComposite(groupKnowledgeLinks, SWT.NONE);
        scrolledCompositeKnowledgeLinkList.setLayout(new GridLayout());
        scrolledCompositeKnowledgeLinkList.setExpandHorizontal(true);
        scrolledCompositeKnowledgeLinkList.setExpandVertical(true);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        gridData.heightHint = 120;
        scrolledCompositeKnowledgeLinkList.setLayoutData(gridData);

        // Group to show knowledge link details (Eigenschaften)
        // ****************************************************
        groupKnowledgeLinkDetails = new Group(groupKnowledgeLinks, SWT.SHADOW_IN | SWT.END);
        groupKnowledgeLinkDetails.setText(Resources.Frames.Global.Texts.PROPERTIES.getText());
        GridData gridDataDetails = new GridData(GridData.FILL, SWT.FILL, true, true, 3, 1);
        groupKnowledgeLinkDetails.setLayoutData(gridDataDetails);
        groupKnowledgeLinkDetails.setLayout(new GridLayout(4, false));

        buttonOpenURL = ButtonFactory.create(groupKnowledgeLinkDetails, actionOpenURL);
        buttonOpenURL.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 2, 1));
        buttonOpenURL.setText("");
        buttonOpenURL.setEnabled(false);

        buttonEditKnowledgeLink = ButtonFactory.create(groupKnowledgeLinkDetails, actionEditKnowledgeLink);
        buttonEditKnowledgeLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        buttonEditKnowledgeLink.setText("");
        buttonEditKnowledgeLink.setEnabled(false);

        buttonUploadDocument = ButtonFactory.create(groupKnowledgeLinkDetails, actionUploadDocument);
        buttonUploadDocument.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        buttonUploadDocument.setText("");
        buttonUploadDocument.setEnabled(false);

        createRowKnowledgeLinkName(groupKnowledgeLinkDetails);

        createRowKnowledgeLinkRepository(groupKnowledgeLinkDetails);

        createRowKnowledgeLinkURL(groupKnowledgeLinkDetails);

        // init table which shows the knowledge links
        initTable();

        scrolledCompositeKnowledgeLinkList.setContent(tableViewerKnowledgeLinks.getControl());
    }

    private KnowledgeRepository getSelectedKnowledgeRepository()
    {
        return knowledgeRepositoryMap.get(comboKnowledgeRepository.getSelectionIndex());
    }

    /**
     * Set label for hyper link.
     * 
     * @param selectedKnowLinkGroup
     */
    private void createRowKnowledgeLinkURL(Group selectedKnowLinkGroup)
    {
        Label hyperlinkLabel = new Label(selectedKnowLinkGroup, SWT.RIGHT);
        hyperlinkLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        hyperlinkLabel.setText(Resources.Frames.Global.Texts.HYPERLINK.getText() + GlobalConstants.DOUBLE_POINT);

        createTextFieldKnowledgeLinkURL(selectedKnowLinkGroup);
    }

    private void createTextFieldKnowledgeLinkURL(Group selectedKnowLinkGroup)
    {
        textFieldKnowledgeLinkURL = new Text(selectedKnowLinkGroup, SWT.BORDER);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);

        // init with empty text
        textFieldKnowledgeLinkURL.setText("");
        textFieldKnowledgeLinkURL.setLayoutData(gridData);
        textFieldKnowledgeLinkURL.setEnabled(false);

        textFieldKnowledgeLinkURL.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent event)
            {
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();

                if (knowledgeLinkTemp != null)
                {
                    KnowledgeRepository knowRep = getSelectedKnowledgeRepository();
                    if (knowRep != null && ( !knowRep.getName().equals(GlobalConstants.PROWIM_WIKI)))
                    {
                        knowledgeLinkTemp.setHyperlink(textFieldKnowledgeLinkURL.getText());
                        updateKnowledgeLink(knowledgeLinkTemp);
                    }
                }
                buttonOpenURL.setEnabled( !"".equals(textFieldKnowledgeLinkURL.getText()) && checkLink(knowledgeLinkTemp));
            }
        });

    }

    /**
     * <p>
     * Returns the selected {@link KnowledgeLinkTemp} from the table viewer.
     * </p>
     * 
     * <p>
     * If nothing is selected, returns null.
     * </p>
     * 
     * @return {@link KnowledgeLinkTemp} or null, if nothing is currently selected
     */
    private KnowledgeLinkTemp getSelectedKnowledgeLinkTemp()
    {
        KnowledgeLinkTemp returnValue = null;
        IStructuredSelection selection = (IStructuredSelection) tableViewerKnowledgeLinks.getSelection();
        if ( !selection.isEmpty())
        {
            returnValue = (KnowledgeLinkTemp) selection.getFirstElement();
        }

        return returnValue;
    }

    /**
     * This action to open the file included in actual selected knowledge link.
     */
    private void showKnowLink()
    {
        KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
        if (knowledgeLinkTemp != null)
        {
            KnowledgeLink knowledgeLink = DefaultDataObjectFactory.createKnowledgeLink(knowledgeLinkTemp.getID(), knowledgeLinkTemp.getName(),
                                                                                       knowledgeLinkTemp.getCreateTime());
            knowledgeLink.setDescription(knowledgeLinkTemp.getDescription());
            knowledgeLink.setHyperlink(knowledgeLinkTemp.getHyperlink());
            knowledgeLink.setRepository(knowledgeLinkTemp.getRepository());

            if (knowledgeLinkTemp.getRepositoryName().equals(GlobalConstants.PROWIM_WIKI))
            {
                knowledgeLink.setHyperlink(getWikiURL());
            }

            if (checkLink(knowledgeLink))
            {
                KnowledgeLinkLeaf knowledgeLinkLeaf = new KnowledgeLinkLeaf(knowledgeLink);
                OpenKnowledgeLink.open(knowledgeLinkLeaf);
            }
            else
            {
                openCreateInfoDialog();
            }
        }
    }

    /**
     * Checks if the link is created and the attributes set.
     * 
     * @return true if the link is created.
     */
    private boolean checkLink(KnowledgeLink link)
    {
        boolean ret1 = link != null && link.getID() != null && !link.getID().equals("") && link.getHyperlink() != null;
        return (ret1 && !link.getHyperlink().equals(""));
    }

    /**
     * Create label and combo box to show the knowledge repositories.
     * 
     * @param group the {@link Group} to add the row to, may not be null
     */
    private void createRowKnowledgeLinkRepository(Group group)
    {
        Validate.notNull(group);

        Label labelKnowledgeRepository = new Label(group, SWT.RIGHT);
        labelKnowledgeRepository.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        labelKnowledgeRepository.setText(Resources.Frames.Global.Texts.KNOW_MEMORY.getText() + GlobalConstants.DOUBLE_POINT);

        comboKnowledgeRepository = new Combo(group, SWT.READ_ONLY);
        comboKnowledgeRepository.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
        comboKnowledgeRepository.setEnabled(false);

        comboKnowledgeRepository.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                KnowledgeRepository knowRep = getSelectedKnowledgeRepository();

                // the init method should only be called when the user selects another knowledge source by his own because the
                // listeners are fired incorrectly then
                if ( !initializing)
                {
                    initHyperLinkLext();
                }

                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                String repositoryName = comboKnowledgeRepository.getText();

                if (knowledgeLinkTemp != null)
                {
                    knowledgeLinkTemp.setRepository(knowRep.getID());
                    knowledgeLinkTemp.setRepositoryName(knowRep.getName());

                    // if is changed to WIKI , set hyper link to the knowledgelink URL
                    if (repositoryName.equals(GlobalConstants.PROWIM_WIKI))
                    {
                        knowledgeLinkTemp.setHyperlink(textFieldKnowledgeLinkURL.getText());
                    }

                    knowledgeTempLinks.set(knowledgeTempLinks.indexOf(knowledgeLinkTemp), knowledgeLinkTemp);
                }

                if (repositoryName.equals(GlobalConstants.PROWIM_DMS) || repositoryName.equals(GlobalConstants.PROWIM_WIKI))
                {
                    textFieldKnowledgeLinkURL.setEditable(false);
                    if (repositoryName.equals(GlobalConstants.PROWIM_DMS))
                    {
                        setDMSButtonsEnabled(true);
                    }
                }
                else
                {
                    textFieldKnowledgeLinkURL.setEditable(true);
                    setDMSButtonsEnabled(false);
                }
            }
        });

        // fill the combo with data
        KnowledgeRepositoryArray knowledgeRepositories = MainController.getInstance().getKnowledgeRepositories();
        Iterator<KnowledgeRepository> knowRepIt = knowledgeRepositories.iterator();

        int idx = 0;
        while (knowRepIt.hasNext())
        {
            KnowledgeRepository knowRep = knowRepIt.next();
            comboKnowledgeRepository.add(knowRep.getName(), idx);
            knowledgeRepositoryMap.put(idx, knowRep);
            if (knowRep.getName().equals(GlobalConstants.PROWIM_DMS))
            {
                defaultRepository = knowRep;
            }
            idx++;
        }
    }

    /**
     * Set the title label.
     * 
     * @param selectedKnowLinkGroup
     */
    private void createRowKnowledgeLinkName(Group selectedKnowLinkGroup)
    {
        Label titleLabel = new Label(selectedKnowLinkGroup, SWT.RIGHT);
        titleLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        titleLabel.setText(Resources.Frames.Global.Texts.NAME.getText() + GlobalConstants.DOUBLE_POINT);

        textFieldKnowledgeLinkName = new Text(selectedKnowLinkGroup, SWT.BORDER);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1);
        textFieldKnowledgeLinkName.setLayoutData(gridData);
        textFieldKnowledgeLinkName.setEnabled(false);

        textFieldKnowledgeLinkName.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                if (knowledgeLinkTemp != null)
                {

                    knowledgeLinkTemp.setName(textFieldKnowledgeLinkName.getText());

                    // if WIKI is selected, build the new URL and set it to the text field
                    if (getSelectedKnowledgeRepository().getName().equals(GlobalConstants.PROWIM_WIKI))
                    {
                        initializing = true;
                        textFieldKnowledgeLinkURL.setText(getWikiURL());
                        knowledgeLinkTemp.setHyperlink(textFieldKnowledgeLinkURL.getText());
                        initializing = false;
                    }
                    updateKnowledgeLink(knowledgeLinkTemp);
                    tableViewerKnowledgeLinks.setInput(knowledgeTempLinks);
                    tableViewerKnowledgeLinks.refresh();
                }
            }
        });
    }

    /**
     * Updates the given {@link KnowledgeLinkTemp} in the list of links.
     * 
     * @param knowledgeLinkTemp the {@link KnowledgeLinkTemp} to update, may not be null
     */
    private void updateKnowledgeLink(KnowledgeLinkTemp knowledgeLinkTemp)
    {
        Validate.notNull(knowledgeLinkTemp);
        knowledgeTempLinks.set(knowledgeTempLinks.indexOf(knowledgeLinkTemp), knowledgeLinkTemp);
    }

    /**
     * Creates the row which shows the responsible person for the knowledge object.
     * 
     * @param group the {@link Group} to add the row to.
     */
    private void createRowResponsiblePerson(Group group)
    {
        Label personLabel = new Label(group, SWT.TRAIL);
        personLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        personLabel.setText(Resources.Frames.Global.Texts.USER.getText() + GlobalConstants.DOUBLE_POINT);

        textFieldResponsiblePerson = new Text(group, SWT.BORDER | SWT.READ_ONLY);
        GridData gridDataPerson = new GridData(GridData.FILL_HORIZONTAL);
        gridDataPerson.grabExcessHorizontalSpace = true;
        textFieldResponsiblePerson.setLayoutData(gridDataPerson);
        textFieldResponsiblePerson.setEditable(false);

        textFieldResponsiblePerson.setText(personArrayToStr(personArray));
        textFieldResponsiblePerson.setData(personArray.getItem());

        Button personBtn = ButtonFactory.create(group, actionAddPerson);
        personBtn.setText("");
    }

    /**
     * Creates the row which shows the key words for the knowledge object.
     * 
     * @param group the {@link Group} to add the row to.
     */
    private void createRowKeyWords(Group group)
    {
        Label personLabel = new Label(group, SWT.TRAIL);
        personLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        personLabel.setText(Resources.Frames.Global.Texts.KEY_WORDS.getText() + GlobalConstants.DOUBLE_POINT);

        textFieldKeyWords = new Text(group, SWT.BORDER);
        GridData gridDataPerson = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
        gridDataPerson.grabExcessHorizontalSpace = true;
        textFieldKeyWords.setLayoutData(gridDataPerson);
        textFieldKeyWords.setText(keyWordsToStr(keyWords));
        textFieldKeyWords.setToolTipText(Resources.Frames.Dialog.Texts.KEY_WORDS_TOOLTIPS_SEPARATE_COMMA.getText());
        textFieldKeyWords.setData(keyWords.getItem());
        textFieldKeyWords.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                currentKeyWords = textFieldKeyWords.getText();
            }
        });
    }

    /**
     * Opens a DMS link. It means opens the document assigned to the link in DMS.
     * 
     * @param knowledgeLinkTemp the {@link KnowledgeLinkTemp} to use, may not be null
     */
    private void openDMSLink(KnowledgeLinkTemp knowledgeLinkTemp)
    {
        Validate.notNull(knowledgeLinkTemp);

        KnowledgeRepository knowRep = getSelectedKnowledgeRepository();

        if (knowRep != null)
        {
            if (knowRep.getName().equals(GlobalConstants.PROWIM_DMS))
            {
                String versionLabel = MainController.getInstance().getDocumentVersionLabel(knowledgeLinkTemp.getID());
                if (versionLabel == null)
                {
                    getDocument(knowledgeLinkTemp.getID());
                }
                else
                {
                    getDocumentInVersionFromLink(knowledgeLinkTemp.getID(), versionLabel);
                }
            }
        }
    }

    /**
     * Create area which shows the properties of the knowledge object
     */
    private void createKnowledgeObjectArea(Control control)
    {
        Group groupKnowledgeObject = new Group((Composite) control, SWT.SHADOW_IN | SWT.RIGHT);
        groupKnowledgeObject.setText(Resources.Frames.Knowledge.Texts.KNOW_OBJECT.getText());
        groupKnowledgeObject.setLayoutData(new GridData(GridData.FILL_BOTH));
        groupKnowledgeObject.setLayout(new GridLayout(3, false));

        // the denotation text field
        Label denotationLabel = new Label(groupKnowledgeObject, SWT.TRAIL);
        denotationLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        denotationLabel.setText(Resources.Frames.Global.Texts.DENOTATION.getText() + GlobalConstants.DOUBLE_POINT);

        textFieldDenotation = new ValidatedTextField(groupKnowledgeObject, new DefaultConstraint(new Long(1), new Long(1000), true));
        GridData gridDataDenotation = new GridData(GridData.FILL, SWT.CENTER, true, false, 2, 0);
        textFieldDenotation.setLayoutData(gridDataDenotation);
        if (currentKnowledgeObject != null)
        {
            textFieldDenotation.setText(currentKnowledgeObject.getName());
            denotationTxtOut = currentKnowledgeObject.getName();
        }

        textFieldDenotation.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                denotationTxtOut = textFieldDenotation.getText();
                if (currentKnowledgeObject != null)
                    currentKnowledgeObject.setName(textFieldDenotation.getText());
            }
        });

        // the responsible person
        createRowResponsiblePerson(groupKnowledgeObject);

        // the description text area
        Label textareaLabel = new Label(groupKnowledgeObject, SWT.TRAIL);
        textareaLabel.setLayoutData(new GridData(GridData.END, SWT.TOP, false, false));
        textareaLabel.setText(Resources.Frames.Global.Texts.DESCRIPTION.getText() + GlobalConstants.DOUBLE_POINT);

        textAreaKnowledgeObjectDescription = new Text(groupKnowledgeObject, SWT.BORDER | SWT.MULTI | SWT.WRAP);
        GridData gridDataDescription = new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1);
        gridDataDescription.minimumHeight = 50;

        textAreaKnowledgeObjectDescription.setLayoutData(gridDataDescription);
        if (currentKnowledgeObject != null)
        {
            String description = currentKnowledgeObject.getDescription();
            if (description != null)
            {
                textAreaKnowledgeObjectDescription.setText(description);
            }
            else
            {
                textAreaKnowledgeObjectDescription.setText("");
            }

        }

        textAreaKnowledgeObjectDescription.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                currentDes = textAreaKnowledgeObjectDescription.getText();
                if (currentKnowledgeObject != null)
                    currentKnowledgeObject.setDescription(textAreaKnowledgeObjectDescription.getText());
            }
        });

        // Create row for show or create key words
        createRowKeyWords(groupKnowledgeObject);
    }

    /**
     * 
     * This method call the constructor of this dialog and get the results and gives these back to the main frame. It is better to call this
     * 
     * method for creating this dialog.
     * 
     * @param objectID the ID of the knowledge object which
     * @return true, if the user has clicked OK and saved something (new or edited), false, if the user clicked on CANCEL
     */
    public boolean openDialog(final String objectID)
    {
        Validate.notNull(objectID);

        incomingObjectID = objectID;

        currentKnowledgeObject = null;
        keyWords = null;

        knowledgeTempLinks = new ArrayList<KnowledgeLinkTemp>();
        if (action.getId().equals(GlobalConstants.EDIT_WOB))
        {
            currentKnowledgeObject = MainController.getInstance().getKnowlegdeObj(objectID);
            personArray = currentKnowledgeObject.getResponsiblePersons();
            keyWords = currentKnowledgeObject.getKeyWords();
            this.currentKeyWords = keyWordsToStr(keyWords);
            knowledgeLinkList = currentKnowledgeObject.getKnowledgeLinks();
            Iterator<KnowledgeLink> knowLinkIt = knowledgeLinkList.iterator();
            while (knowLinkIt.hasNext())
            {
                KnowledgeLinkTemp temp = new KnowledgeLinkTemp(knowLinkIt.next());
                knowledgeTempLinks.add(temp);
            }
        }
        else
        {
            personArray = new PersonArray();
            keyWords = new StringArray();
        }

        return this.open() == KnowledgeObjectDialog.OK;
    }

    /**
     * 
     * Saves and updates data after user selects OK.
     * 
     * @param objectID ID of objects
     * @param knowLinkList list of knowledge links
     * @return <code>false</code> if the document can not upload, else <code>true</code>
     */
    private boolean save(final String objectID, final List<KnowledgeLink> knowLinkList)
    {
        boolean uploadFlag = true;
        String documentName = "";
        List<KnowledgeLink> knowLinkListFinal = new ArrayList<KnowledgeLink>();

        createKnowledgeObject(objectID);

        // Set repository for this knowledge object
        Iterator<KnowledgeLinkTemp> knowIt = knowledgeTempLinks.iterator();
        while (knowIt.hasNext())
        {
            KnowledgeLinkTemp tempKnowLink = knowIt.next();
            if (tempKnowLink.getID().equals(""))
            {
                KnowledgeLink knowLink = MainController.getInstance().createKnowLink(currentKnowledgeObject.getID(), tempKnowLink.getName(),
                                                                                     tempKnowLink.getRepository(), tempKnowLink.getHyperlink());
                if (knowLink != null)
                {
                    // Replace backslashes with double backslashes, because by adding in DB backslashes will be deleted
                    // knowLink.setHyperlink(tempKnowLink.getHyperlink().replace("\\", "\\\\"));
                    knowLink.setHyperlink(tempKnowLink.getHyperlink());
                    knowLinkListFinal.add(knowLink);
                    if (tempKnowLink.getRepositoryName().equals(GlobalConstants.PROWIM_DMS))
                    {
                        if (tempKnowLink.isNewDocumentUploaded())
                        {
                            if (tempKnowLink.hasDocumentInitially())
                            {
                                MainController.getInstance().updateDocument(tempKnowLink.getDocument(), knowLink.getID());
                            }
                            else
                            {
                                if ( !MainController.getInstance().uploadDocument(tempKnowLink.getDocument(), knowLink.getID()))
                                {
                                    uploadFlag = false;
                                    MainController.getInstance().deleteObject(knowLink.getID());
                                    documentName = tempKnowLink.getDocument().getName();
                                }
                            }
                        }

                        if (uploadFlag)
                        {
                            bindDocument(tempKnowLink, knowLink);
                        }
                    }
                }
            }
            else
            {
                Iterator<KnowledgeLink> tmpIt = knowLinkList.iterator();
                while (tmpIt.hasNext())
                {
                    KnowledgeLink knowLink = tmpIt.next();
                    if (knowLink.getID().equals(tempKnowLink.getID()))
                    {
                        knowLink.setRepository(tempKnowLink.getRepository());
                        knowLink.setName(tempKnowLink.getName());
                        knowLink.setRepository(tempKnowLink.getRepository());
                        if (tempKnowLink.getRepositoryName().equals(GlobalConstants.PROWIM_DMS))
                        {
                            if (tempKnowLink.isNewDocumentUploaded())
                            {
                                if (tempKnowLink.hasDocumentInitially())
                                {
                                    MainController.getInstance().updateDocument(tempKnowLink.getDocument(), knowLink.getID());
                                }
                                else
                                {
                                    if ( !MainController.getInstance().uploadDocument(tempKnowLink.getDocument(), knowLink.getID()))
                                    {
                                        uploadFlag = false;
                                        documentName = tempKnowLink.getDocument().getName();
                                    }
                                }
                            }

                            if (uploadFlag)
                            {
                                bindDocument(tempKnowLink, knowLink);
                            }
                        }
                        else if (tempKnowLink.getRepositoryName().equals(GlobalConstants.INTERNET))
                        {
                            if ( !tempKnowLink.getHyperlink().startsWith(GlobalConstants.HTTP)
                                    && !tempKnowLink.getHyperlink().startsWith(GlobalConstants.HTTPS))
                            {
                                tempKnowLink.setHyperlink(GlobalConstants.HTTP + tempKnowLink.getHyperlink());
                            }
                        }

                        // Replace back slasheshes with dobble backslashes, because by adding in DB backslashes will be deleted
                        knowLink.setHyperlink(tempKnowLink.getHyperlink());
                        knowLinkListFinal.add(knowLink);
                    }
                }
            }
        }

        if (uploadFlag)
        {
            currentKnowledgeObject.setKnowledgeLinks(knowLinkListFinal);
            MainController.getInstance().saveKnowledgeObject(currentKnowledgeObject);
        }
        else
            MessageDialog.openInformation(null, Resources.Frames.Dialog.Texts.DOCUMENT_EXISTS_DIALOG_TITLE.getText(),
                                          Resources.Frames.Global.Texts.DOCUMENT.getText() + GlobalConstants.DOUBLE_POINT + " " + documentName
                                                  + "\r\n\r\n" + Resources.Frames.Dialog.Texts.DOCUMENT_EXISTS_DIALOG_DESCRIPTION.getText());
        return uploadFlag;

    }

    /**
     * Create the knowledge object if it is not exists.
     * 
     * @param objectID ID of incoming object
     */
    private void createKnowledgeObject(final String objectID)
    {
        if (currentKnowledgeObject == null)
        {
            if (objectID.equals(""))
            {
                currentKnowledgeObject = MainController.getInstance().createKnowObj(denotationTxtOut);
            }
            else
            {
                currentKnowledgeObject = MainController.getInstance().createKnowObj(objectID, denotationTxtOut);
            }

            currentKnowledgeObject.setDescription(currentDes);
        }

        // Set dominators of this knowledge object
        if ( !personArray.isEmpty())
        {
            currentKnowledgeObject.setResponsiblePersons(personArray);
        }
        else
        {
            currentKnowledgeObject.setResponsiblePersons(new PersonArray());
        }

        currentKnowledgeObject.setKeyWords(getKeyWords());
    }

    /**
     * Binds a document from the given {@link KnowledgeLinkTemp} to the given {@link KnowledgeLink}.
     * 
     * @param knowledgeLinkTemp the {@link KnowledgeLinkTemp} which contains the information about the document to be bound, may not be null
     * @param knowledgeLink the {@link KnowledgeLink} which provides the ID to bind the document to, may not be null
     */
    private void bindDocument(KnowledgeLinkTemp knowledgeLinkTemp, KnowledgeLink knowledgeLink)
    {
        Validate.notNull(knowledgeLinkTemp, "Temporary knowledge link is null");
        Validate.notNull(knowledgeLink, "KnowledgeLink is null");
        Version version = knowledgeLinkTemp.getVersion();
        if (version != null)
        {
            String versionLabel = version.getLabel();
            if (version.getLabel().equals(AlgernonConstants.EDITABLE_USER_VERSION_LABEL))
            {
                versionLabel = null;
            }
            DocumentContentProperties documentContentPropertiesLink = knowledgeLinkTemp.getDocumentContentProperties();
            if (documentContentPropertiesLink != null)
            {
                MainController.getInstance().bindDocument(documentContentPropertiesLink.getID(), documentContentPropertiesLink.getContentName(),
                                                          knowledgeLink.getID(), versionLabel);
            }
        }
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
        setReturnCode(OK);
        if (textFieldDenotation.isVerified())
        {
            if (save(incomingObjectID, knowledgeLinkList))
                close();
        }
        else
        {
            InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.REQUIRED_ERROR_DIALOG.getTooltip());
        }
    }

    /**
     * Creates all actions.
     */
    private void createActions()
    {
        actionAddPerson = Resources.Frames.Dialog.Actions.ADD_USER.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                List<Person> personList = new ArrayList<Person>();

                personList = personArray.getItem();

                PersonController controller = new PersonController();
                DefaultTableModel personModel = new DefaultTableModel(controller.getTableModel(null), controller.getColumns());

                SelectPersonDialog personDialog = new SelectPersonDialog(null, actionAddPerson, "", personModel, personList,
                                                                         new DefaultConstraint(new Long(0), new Long(100), false));

                if (personDialog.open() == IDialogConstants.OK_ID)
                {
                    personArray = new PersonArray();
                    Iterator<Person> iter = personDialog.getSelectedPersons().iterator();
                    while (iter.hasNext())
                        personArray.add(iter.next());

                    textFieldResponsiblePerson.setText(personArrayToStr(personArray));
                    textFieldResponsiblePerson.setData(personArray.getItem());
                }
            }
        });

        actionDeleteKnowledgeLink = Resources.Frames.Dialog.Actions.DELETE_KNOW_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                if (knowledgeLinkTemp != null)
                {
                    knowledgeTempLinks.remove(knowledgeLinkTemp);
                    tableViewerKnowledgeLinks.setInput(knowledgeTempLinks);
                    tableViewerKnowledgeLinks.refresh();
                    selectFirstLink();
                }
            }
        });

        actionShowKnowledgeLinkDetails = Resources.Frames.Dialog.Actions.KNOWLEDGE_LINK_DETAILS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                if (knowledgeLinkTemp != null)
                {
                    String owner = "";
                    if (currentKnowledgeObject != null)
                    {
                        owner = currentKnowledgeObject.getOwner();
                    }

                    KnowledgeLinkDetailsDialog knowledgeLinkDialg = new KnowledgeLinkDetailsDialog(null, actionShowKnowledgeLinkDetails, "",
                                                                                                   knowledgeLinkTemp, owner);
                    knowledgeLinkDialg.open();

                }
            }
        });

        actionAddKnowledgeLink = Resources.Frames.Dialog.Actions.ADD_KNOW_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeLinkTemp newLink = new KnowledgeLinkTemp("", Resources.Frames.Knowledge.Texts.NEW_KNOW_LINK.getText(), "");
                newLink.setRepository(defaultRepository.getID());
                newLink.setRepositoryName(defaultRepository.getName());

                knowledgeTempLinks.add(newLink);
                tableViewerKnowledgeLinks.setInput(knowledgeTempLinks);
                tableViewerKnowledgeLinks.refresh();
                StructuredSelection selection = new StructuredSelection(tableViewerKnowledgeLinks.getElementAt(knowledgeTempLinks.size() - 1));
                tableViewerKnowledgeLinks.setSelection(selection);
            }
        });

        actionOpenURL = Resources.Frames.Dialog.Actions.OPEN_HYPER_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                if (knowledgeLinkTemp != null)
                {
                    String repositoryName = knowledgeLinkTemp.getRepositoryName();
                    if (GlobalConstants.PROWIM_DMS.equals(repositoryName))
                    {
                        openDMSLink(knowledgeLinkTemp);
                    }
                    else
                    {
                        showKnowLink();
                    }
                }
            }
        });

        actionEditKnowledgeLink = getActionEditKnowledgeLink();

        actionUploadDocument = getActionUploaddocument();
    }

    private Action getActionEditKnowledgeLink()
    {
        return Resources.Frames.Dialog.Actions.OPEN_EXISTING_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                if (knowledgeLinkTemp != null)
                {
                    DocumentDialog dialog = new DocumentDialog(null, actionEditKnowledgeLink, "", knowledgeLinkTemp, KnowledgeObjectDialog.this);
                    int returnCode = dialog.open();

                    if (returnCode == KnowledgeObjectDialog.OK)
                    {
                        DocumentContentProperties documentContentProperties = getDocumentContentProperties();
                        if (documentContentProperties != null)
                        {
                            Version version = getDocumentVersion();
                            if (version != null)
                            {
                                knowledgeLinkTemp.setDocumentContentProperties(documentContentProperties);
                                knowledgeLinkTemp.setVersion(version);
                                knowledgeLinkTemp.setHyperlink(documentContentProperties.getContentName());
                                knowledgeLinkTemp.setNewDocumentUploaded(false);
                                updateKnowledgeLink(knowledgeLinkTemp);
                                textFieldKnowledgeLinkURL.setText(documentContentProperties.getContentName());
                            }
                        }
                        setDocumentVersion(null);
                        setDocumentContentProperties(null);
                    }
                }
            }
        });
    }

    private Action getActionUploaddocument()
    {
        return Resources.Frames.Dialog.Actions.UPLOAD_DOCUMENT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeRepository knowRep = getSelectedKnowledgeRepository();

                if (knowRep != null)
                {
                    if (knowRep.getName().equals(GlobalConstants.PROWIM_WIKI) || knowRep.getName().equals(GlobalConstants.INTERNET))
                    {
                        ExternalBrowser.open(KnowledgeObjectDialog.class.getName(), textFieldKnowledgeLinkURL.getText(), ExternalBrowser.LOCATION_BAR
                                | ExternalBrowser.NAVIGATION_BAR | ExternalBrowser.STATUS);
                    }
                    else if (knowRep.getName().equals(GlobalConstants.PROWIM_DMS))
                    {
                        FileDialogProWim fileDialog = new FileDialogProWim(null, Resources.Frames.Dialog.Actions.ADD_FILE.getAction(), "");

                        if (fileDialog.open() == IDialogConstants.OK_ID)
                        {
                            KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                            if (knowledgeLinkTemp != null)
                            {
                                knowledgeLinkTemp.setDocument(fileDialog.getUploadedDocumnet());
                                knowledgeLinkTemp.setNewDocumentUploaded(true);
                                textFieldKnowledgeLinkURL.setText(fileDialog.getUploadedDocumnet().getName());
                                updateKnowledgeLink(knowledgeLinkTemp);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Returns the {@link DocumentContentProperties} set by the {@link DocumentDialog}.
     * 
     * @return {@link DocumentContentProperties} or null, if nothing was set
     */
    protected DocumentContentProperties getDocumentContentProperties()
    {
        return this.documentContentProperties;
    }

    /**
     * Sets the {@link DocumentContentProperties} to use when binding the DMS document to a knowledge link.
     * 
     * @param documentContentProperties can be null if resetting the link
     */
    protected void setDocumentContentProperties(DocumentContentProperties documentContentProperties)
    {
        this.documentContentProperties = documentContentProperties;
    }

    /**
     * Disables or enables the DMS buttons.
     * 
     * @param enabled set to true to enable
     */
    private void setDMSButtonsEnabled(boolean enabled)
    {
        buttonEditKnowledgeLink.setEnabled(enabled);
        buttonUploadDocument.setEnabled(enabled);
    }

    /**
     * 
     * Create table and fill it with data. Data´s are list of persons and list of knowledge links.
     */
    private void initTable()
    {
        tableViewerKnowledgeLinks = new KnowledgeLinkTableViewer(scrolledCompositeKnowledgeLinkList, SWT.SINGLE);
        tableViewerKnowledgeLinks.setInput(knowledgeTempLinks);
        tableViewerKnowledgeLinks.addDoubleClickListener(new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent event)
            {
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                if (knowledgeLinkTemp != null)
                {
                    String owner = "";
                    if (currentKnowledgeObject != null)
                    {
                        owner = currentKnowledgeObject.getOwner();
                    }

                    KnowledgeLinkDetailsDialog knowledgeLinkDialg = new KnowledgeLinkDetailsDialog(null, actionShowKnowledgeLinkDetails, "",
                                                                                                   knowledgeLinkTemp, owner);
                    knowledgeLinkDialg.open();
                }
            }
        });

        tableViewerKnowledgeLinks.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                initializing = true;
                KnowledgeLinkTemp knowledgeLinkTemp = getSelectedKnowledgeLinkTemp();
                if (knowledgeLinkTemp != null)
                {
                    textFieldKnowledgeLinkName.setEnabled(true);
                    comboKnowledgeRepository.setEnabled(true);
                    textFieldKnowledgeLinkURL.setEnabled(true);

                    comboKnowledgeRepository.select(comboKnowledgeRepository.indexOf(knowledgeLinkTemp.getRepositoryName()));
                    textFieldKnowledgeLinkName.setText(knowledgeLinkTemp.getName());
                    if (getSelectedKnowledgeRepository().getName().equals(GlobalConstants.PROWIM_WIKI))
                    {
                        textFieldKnowledgeLinkURL.setText(getWikiURL());
                    }
                    else
                    {
                        textFieldKnowledgeLinkURL.setText(knowledgeLinkTemp.getHyperlink());
                    }

                    if (knowledgeLinkTemp.getHyperlink() != null && !knowledgeLinkTemp.getHyperlink().equals(""))
                    {
                        buttonOpenURL.setEnabled(true);
                    }
                    buttonShowKnowledgeLinkDetails.setEnabled(true);
                    buttonDeleteKnowledgeLink.setEnabled(true);
                }
                else
                {
                    // clear the text fields and combo selection
                    textFieldKnowledgeLinkName.setText("");
                    textFieldKnowledgeLinkURL.setText("");
                    comboKnowledgeRepository.clearSelection();

                    // disable all text fields
                    textFieldKnowledgeLinkName.setEnabled(false);
                    comboKnowledgeRepository.setEnabled(false);
                    textFieldKnowledgeLinkURL.setEnabled(false);
                    buttonShowKnowledgeLinkDetails.setEnabled(false);
                    buttonDeleteKnowledgeLink.setEnabled(false);
                }
                initializing = false;
            }
        });

        initializing = true;
        selectFirstLink();
        initializing = false;
    }

    /**
     * Selects the first element in the table.
     */
    private void selectFirstLink()
    {
        if (tableViewerKnowledgeLinks.getElementAt(0) != null)
        {
            StructuredSelection selection = new StructuredSelection(tableViewerKnowledgeLinks.getElementAt(0));
            tableViewerKnowledgeLinks.setSelection(selection);
        }
    }

    /**
     * 
     * show persons as string to show in view. If more than one person, than add ",...", else only the name of person.
     * 
     * @param personArray List of persons.
     * @return String
     */
    private String personArrayToStr(PersonArray personArray)
    {
        List<Person> list = personArray.getItem();
        if (list.size() == 1)
            return list.get(0).toString();
        else if (list.size() > 1)
            return list.get(0).toString() + ",...";
        else
            return "";
    }

    /**
     * 
     * show key words as string to show in view.
     * 
     * @param personArray List of persons.
     * @return String
     */
    private String keyWordsToStr(StringArray keyWords)
    {
        String list = "";
        for (String keyWord : keyWords)
        {
            list = list + keyWord + GlobalConstants.COMMA;
        }
        return list;
    }

    /**
     * Returns the complete wiki URL to set to the textfield.
     * 
     * @return String the wiki URL, never null
     */
    private String getWikiURL()
    {
        String returnValue = "";
        KnowledgeRepository knowRep = getSelectedKnowledgeRepository();
        String suffix = GlobalConstants.WIKI_SUFFIX;
        String wikiURL = knowRep.getStorage();
        String wikiURLConcat = wikiURL + suffix;
        if (wikiURLConcat.lastIndexOf("?") == wikiURLConcat.length() - 1)
        {
            returnValue = wikiURLConcat.substring(0, wikiURLConcat.lastIndexOf("?")) + "=" + textFieldKnowledgeLinkName.getText();
        }
        else if (wikiURLConcat.lastIndexOf("=") == wikiURLConcat.length() - 1)
        {
            returnValue = wikiURLConcat.substring(0, wikiURLConcat.lastIndexOf("=")) + textFieldKnowledgeLinkName.getText();
        }

        return returnValue;
    }

    /**
     * Sets the textfield text for the hyper link to the correct init value depending on the selected knowledge repository.
     */
    private void initHyperLinkLext()
    {
        KnowledgeRepository knowRep = getSelectedKnowledgeRepository();
        String hyperText = "";
        if (knowRep.getName().equals(GlobalConstants.PROWIM_WIKI))
        {
            hyperText = getWikiURL();
        }
        else if (knowRep.getName().equals(GlobalConstants.PROWIM_DMS) || knowRep.getName().equals(GlobalConstants.INTERNET))
        {
            hyperText = "";
        }
        else
        {
            hyperText = knowRep.getStorage();
        }

        textFieldKnowledgeLinkURL.setText(hyperText);
    }

    /**
     * Gets the document in a version stored in DMS and assigned to the knowledgeLink with id = linkID.
     * 
     * @param linkID the not null linkID.
     * @param versionLabel the version label of the document version.
     */
    private void getDocumentInVersionFromLink(String linkID, String versionLabel)
    {
        Validate.notNull(linkID);
        Validate.notNull(versionLabel);
        try
        {
            // Get Document from Data base
            Document document = MainController.getInstance().downloadDocumentInVersion(linkID, versionLabel);

            // This part is deal with the suffixes in older file, which are stored in DMS. Check if a file has this suffix and cut it to show this.
            Document doc = GlobalFunctions.renameDocument(document);
            DownloadFile.download(doc.getContent(), doc.getName());
        }
        catch (Exception e)
        {
            LOG.error("Error by opening file in OpenKnwoLink: ", e);
        }

    }

    /**
     * Downloads a document from DMS.
     * 
     * @param linkID the not null link ID that stores the document ID.
     */
    private void getDocument(String linkID)
    {
        Validate.notNull(linkID);
        if ( !linkID.equals(""))
        {
            try
            {
                // Get Document from Data base
                Document document = MainController.getInstance().downloadDocument(linkID);

                // This part is deal with the suffixes in older file, which are stored in DMS. Check if a file has this suffix and cut it to show this.
                Document doc = GlobalFunctions.renameDocument(document);
                DownloadFile.download(doc.getContent(), doc.getName());
            }
            catch (Exception e)
            {
                LOG.error("Error by opening file in OpenKnwoLink: ", e);
            }
        }
    }

    /**
     * Tells the user, he have to click on ok to create a link.
     */
    protected static void openCreateInfoDialog()
    {
        MessageDialog.openInformation(null, Resources.Frames.Knowledge.Texts.CREATE_KNOWLEDGE_LINK_INFO_DIALOG_TITLE.getText(),
                                      Resources.Frames.Knowledge.Texts.CREATE_KNOWLEDGE_LINK_INFO.getText());
    }

    /**
     * Sets the document version to use when binding a document to a knowledge link.
     * 
     * @param selectedVersion can be null if resetting the binding
     */
    protected void setDocumentVersion(Version selectedVersion)
    {
        this.documentVersion = selectedVersion;
    }

    /**
     * Returns the {@link Version} of the document to be bound to a knowledge link as returned by the {@link DocumentDialog}.
     * 
     * @return the {@link Version}, can be null
     */
    protected Version getDocumentVersion()
    {
        return documentVersion;
    }

    /**
     * 
     * Add key word to a temporary {@link StringArray} and give it back.
     * 
     * @return Not null {@link StringArray}
     */
    private StringArray getKeyWords()
    {
        StringArray tempArray = new StringArray();
        String[] split = this.currentKeyWords.split(",");

        for (String keyWord : split)
        {
            tempArray.add(keyWord);
        }
        return tempArray;
    }

}
