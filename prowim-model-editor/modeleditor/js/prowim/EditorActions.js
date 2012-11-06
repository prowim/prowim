/**
 * Register actions used in Viewer and Editor
 * 
 * @param editor
 *            the editor to register the actions on
 *            
 *            This file is part of ProWim.

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
function RegisterViewerActions(editor) {
	// Action to set the product parameter
	editor.addAction('addKnowledgeObject', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();

			if (cell != null) {
				EditorProxy.addKnowledge(cell.getAttribute('prowimid'));
			}
		}
	});

	// Action to set the sub process parameter
	editor.addAction('showSubProcess', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
				EditorProxy.showProcess(cell.getAttribute('subprocessid'));
			}
		}
	});

	
	editor.addAction('showProcess', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if ((cell != null) && (cell.getAttribute('prowimtype') == 'PROCESS_LANDSPACE'))
			{
				EditorProxy.showProcess(cell.getAttribute('elementid'));
			}
		}
	});

}

/**
 * Register actions used in Editor
 * 
 * @param editor
 */
function RegisterEditorActions(editor) {

	editor.addAction('editName', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();

			if (cell != null)
			{
				if (cell.getAttribute('prowimtype') == "GROUP")
				{
					// Do Nothing;
				}
				else if (cell.getAttribute('prowimtype') == "SWIMLANE")
				{
					EditorProxy.callPickElementDialog(false, "SWIMLANE");
				}
				else if(cell.getAttribute('prowimtype')  == "PROCESSTYPE_LANDSPACE")
				{
					EditorProxy.callPickElementDialog(false, "PROCESSTYPE_LANDSPACE");
				}
				else if(cell.getAttribute('prowimtype')  == "PROCESS_LANDSPACE")
				{
					EditorProxy.callPickElementDialog(false, "PROCESS_LANDSPACE");
				}					
				else
				{
					EditorProxy.editNameAndDescription(cell.getAttribute('prowimid'));	
				}
			}
		}
	});

	editor.addAction('procEnabling', function(editor) {
		var cell = editor.graph.getSelectionCell();

		if (cell != null) {
			var prowimType = cell.getAttribute('prowimtype');
			if (prowimType == "PROCESS") {
				var prowimID = cell.getAttribute('prowimid');
				EditorProxy.openModel(prowimID);
				editor.graph.model.beginUpdate();
				try {
					if( EditorProxy.isProcessLandscape(prowimID))
					{
						editor.graph.setCellStyles("fillColor", '#99FF66');
						editor.graph.setCellStyles("strokeColor", '#006600');
						editor.graph.setCellStyles("strokeWidth", '5');
					}
					else
					{
						editor.graph.setCellStyles("strokeColor", 'green');
						editor.graph.setCellStyles("fillColor", 'green');
					}
				} finally {
					editor.graph.model.endUpdate();
				}

				editor.graph.refresh(cell);
				alert(mxResources.get('processenabled'));
			}
		}
	});

	editor.addAction('procDisabling', function(editor) {
		var cell = editor.graph.getSelectionCell();

		if (cell != null) {
			var prowimType = cell.getAttribute('prowimtype');
			if (prowimType == "PROCESS") {
				var prowimID = cell.getAttribute('prowimid');
				EditorProxy.closeModel(prowimID);
				editor.graph.model.beginUpdate();
				try {
					if( EditorProxy.isProcessLandscape(prowimID))
					{
						editor.graph.setCellStyles("fillColor", '#99FF66');
						editor.graph.setCellStyles("strokeWidth", '0');
					}
					else
					{
						editor.graph.setCellStyles("strokeColor", '#C3D9FF');
						editor.graph.setCellStyles("fillColor", '#C3D9FF');
						
					}
				} finally {
					editor.graph.model.endUpdate();
				}

				editor.graph.refresh(cell);
				alert(mxResources.get('processdisabled'));
			}
		}
	});
	
	editor.addAction('setProcessLandscapeFlag', function(editor) {
		var cell = editor.graph.getSelectionCell();

		if (cell != null) {
			var prowimType = cell.getAttribute('prowimtype');
			if (prowimType == "PROCESS") {
				var prowimID = cell.getAttribute('prowimid');
				var style = editor.graph.getCellStyle(cell);
				
				var flag = EditorProxy.isProcessLandscape(prowimID);

				if (EditorProxy.setProcessLandscapeFlag(prowimID, !flag))
				{
					editor.graph.model.beginUpdate();
					try {
						var isProcessApproved = EditorProxy.isProcessApproved(prowimID);
						if (!flag)
						{
							editor.graph.setCellStyles("fillColor", '#99FF66');
							if(isProcessApproved)
							{
								editor.graph.setCellStyles("strokeColor", '#006600');
								editor.graph.setCellStyles("strokeWidth", '5');
							}
						}
						else
						{
							editor.graph.setCellStyles("strokeWidth", '0');
							if(isProcessApproved)
							{
								editor.graph.setCellStyles("strokeColor", 'green');
								editor.graph.setCellStyles("fillColor", 'green');
							}
							else
							{
								editor.graph.setCellStyles("strokeColor", '#C3D9FF');
								editor.graph.setCellStyles("fillColor", '#C3D9FF');
							}
						}
					} finally {
						editor.graph.model.endUpdate();
					}
					editor.graph.refresh(cell);
				}					
				else
					alert('Fehler beim Setzen von Prozesslandkarte!');
				
			}
		}
	});


	editor.addAction('saveGraph', function(editor) {
		EditorProxy.saveModelAsXML(false);
	});

	editor.addAction('saveGraphVersion', function(editor) {
		EditorProxy.saveProcessAsNewVersion();
	});

	editor.addAction('newDiagram', function(editor) {
		editor.execute('saveGraph');
		window.open('./prowim-model-editor.html', '_self');
	});

	editor.addAction('openGraph', function(editor) {
		editor.execute('saveGraph');
		window.open('./index.html', '_self');
	});

	// Deletes a selected cell  TODO: Die Methode sollte reviewt werden !!!!!
	editor.addAction('deleteCell', function(editor) {
			if (editor.graph.isEnabled()) {
				var cell = editor.graph.getSelectionCell();
				if (cell.getAttribute('prowimtype') != 'PROCESS') {
					if (cell.getAttribute('prowimtype') != 'RELATION') {
						if (cell.getAttribute('prowimtype') == 'SWIMLANE' || cell.getAttribute('prowimtype') == 'GROUP') {
							var returnDlg = mxUtils.confirm(mxResources.get('confirmDeleteSwimlane'));
							if (returnDlg == true) {
								editor.graph.removeCells();
								var cells = cell.children;
								if (cells != null) {
									for ( var i = 0; i < cells.length; i++) {
										if (cells[i].getAttribute('prowimtype') != 'SWIMLANE' && cells[i].getAttribute('prowimtype') != 'GROUP') {
											if (cells[i].getAttribute('prowimtype') != 'RELATION') {
												if (EditorProxy.isGlobal(cells[i].getAttribute('prowimid')) == false) {
													EditorProxy.deleteObject(cells[i].getAttribute('prowimid'));
												} else {
													modelID = EditorFunctions.getIdFromURL('prowimid');
													EditorProxy.deleteElementFromModel(modelID, cells[i].getAttribute('prowimid'));
												}
											} else {
												EditorProxy.removeRelationValue(cells[i].source.getAttribute('prowimid'),cells[i].getAttribute('label'));
											}
										}
									}
								}
							}
						} else {
							// see if there is just one copy
							// then remove it from the model,
							// else just remove it from the view
							if (EditorFunctions.copyCount(editor, cell) == 1) {
								if (cell.getAttribute('prowimid') != undefined) {
									var edgeCount = cell.getEdgeCount();
									for ( var i = 0; i < edgeCount; i++) {
										if (cell.getEdgeAt(i).getAttribute('prowimtype') != undefined) {
											if (cell.getEdgeAt(i).getAttribute('prowimtype') != "RELATION") {
												if (EditorFunctions.copyCount(editor,cell.getEdgeAt(i)) == 1) {
													if (cell.getEdgeAt(i).getAttribute('prowimid') != undefined) {
														if (EditorProxy.isGlobal(cell.getEdgeAt(i).getAttribute('prowimid')) == false) {
															EditorProxy.deleteObject(cell.getEdgeAt(i).getAttribute('prowimid'));
														} else {
															modelID = EditorFunctions.getIdFromURL('prowimid');
															EditorProxy.deleteElementFromModel(modelID,cells.getAttribute('prowimid'));
														}
													}
												}
											} else {
												EditorProxy.removeRelationValue(cell.getEdgeAt(i).source.getAttribute('prowimid'),cell.getEdgeAt(i).getAttribute('label'));
											}
										}
									}
								}
								// This try catch the error,
								// when the shape exist in model
								// but not in ontology. In this
								// case user should can deletes
								// the shape from model.
								try {
									if (cell.getAttribute('prowimid') != undefined) {
										if (EditorProxy.isGlobal(cell.getAttribute('prowimid')) == false) {
											EditorProxy.deleteObject(cell.getAttribute('prowimid'));
										} else {
											modelID = EditorFunctions.getIdFromURL('prowimid');
											EditorProxy.deleteElementFromModel(modelID,cell.getAttribute('prowimid'));
										}
									}
								} catch (exception) {
									EditorFunctions.removeCells(editor.graph,[ cell ]);
								}
								EditorFunctions.removeCells(editor.graph, [ cell ]);
							} else if (EditorFunctions.copyCount(editor, cell) > 1) {
								if (cell.getAttribute('prowimid') != undefined) {
									var edgeCount = cell.getEdgeCount();
									for ( var i = 0; i < edgeCount; i++) {
										if (cell.getEdgeAt(i).getAttribute('prowimtype') != undefined) {
											if (cell.getEdgeAt(i).getAttribute('prowimtype') != "RELATION") {
												if (EditorFunctions.copyCount(editor,cell.getEdgeAt(i)) == 1) {
													if (EditorProxy.isGlobal(cell.getEdgeAt(i).getAttribute('prowimid')) == false) {
														EditorProxy.deleteObject(cell.getEdgeAt(i).getAttribute('prowimid'));
													} else {
														modelID = EditorFunctions.getIdFromURL('prowimid');
														EditorProxy.deleteElementFromModel(modelID,cell.getAttribute('prowimid'));
													}
												}
											} else {
												EditorProxy.removeRelationValue(cell.getEdgeAt(i).source.getAttribute('prowimid'),cell.getEdgeAt(i).getAttribute('label'));
											}
										}
									}
								}
								EditorFunctions.removeCells(editor.graph, [ cell ]);
							}
						}
					} else {
							EditorProxy.removeRelationValue(cell.source.getAttribute('prowimid'),cell.getAttribute('label'));
							EditorFunctions.removeCells(editor.graph, [ cell ]);
					}
				}
				// This is only for the case, if a shape is
				// created with an error and has no prowimid. In
				// this case user can deletes the shape.
				if (cell.getAttribute('prowimid') == undefined && cell.getAttribute('prowimtype') != 'SWIMLANE' && cell.getAttribute('prowimtype') != 'GROUP') {
					EditorFunctions.removeCells(editor.graph,[ cell ]);
				}
			}
	});

	editor.addAction('copyCell', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
				if ((cell.getAttribute('prowimtype') == 'DEPOT')
						|| (cell.getAttribute('prowimtype') == 'MEAN')
						|| (cell.getAttribute('prowimtype') == 'ROLE')) {
					mxClipboard.copy(editor.graph);
				} else {
					alert(mxResources.get('copynotallowd'));
				}
			}
		}
	});

	editor.addAction('pasteCell', function(editor) {
		if (editor.graph.isEnabled())
			mxClipboard.paste(editor.graph);
	});

	editor.addAction('copy', function() {
	});
	editor.addAction('paste', function() {
	});
	editor.addAction('delete', function() {
	});
	editor.addAction('selectNext', function() {
	});
	editor.addAction('selectPrevious', function() {
	});
	editor.addAction('selectParent', function() {
	});
	editor.addAction('selectChild', function() {
	});
	editor.addAction('undo', function() {
	});
	editor.addAction('click', function() {
	});
	editor.graph.addListener(mxEvent.CELL_MOVED, function() {
	});
	
	mxGraph.prototype.isCellDisconnectable = function(cell, terminal, source) {
		if (cell.isEdge()) {
			return false;
		} else {
			return this.isCellsDisconnectable() && !this.isCellLocked(cell);
		}
	};

	// Set Action for loop function windows
	editor.addAction('undoBend', function(editor) {
		var selectedCell = editor.graph.getSelectionCell();
		var selectionModel = editor.graph.getSelectionModel();
		selectionModel.removeCell(selectedCell);
		EditorFunctions.removeCells(editor.graph, [ selectedCell ]);

	});

	// Set Action for loop function windows
	editor
			.addAction(
					'toggleOutline',
					function(editor) {
						editor.showOutline();
						editor.outline
								.setLocation(
										1,
										(document.body.clientHeight || document.documentElement.clientHeight) - 200);
					});


	// Action to set a contrloflow disable or enable
	editor.addAction('disableEnableEdge', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
				if (cell.getAttribute('prowimtype') == 'CONTROLFLOW')

				var style = editor.graph.getCellStyle(cell);
				if (style.textOpacity == '0') {
					editor.graph.setCellStyles("textOpacity", '100');
				} else {
					editor.graph.setCellStyles("textOpacity", '0');
				}
			}
		}
	});


	
	
	// Action to set the description of a decision
	editor.addAction('setDecisionDesc', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
				if (cell.getAttribute('prowimtype') == 'DECISION') {
					var processInfoID = EditorProxy.getProcessInfoOfObj(cell
							.getAttribute('prowimid'));

					var label = mxUtils.prompt(mxResources.get('changeText'),
							EditorProxy.getName(processInfoID) || '');
					if (label != null && processInfoID != '') {
						EditorProxy.rename(processInfoID, label);
					}
				}
			}
		}
	});

	// Action to set the product parameter
	editor.addAction('setProductParam', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
					var act = cell.source.getAttribute('prowimid');
					BFOpenProductParameters(cell.getAttribute('prowimid'), act);
			}
		}
	});

	// Action to set the product parameter
	editor.addAction('getModelURL', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			EditorProxy.showModelURL(cell.getAttribute('prowimid'));
		}
	});

	// Action to set the product parameter
	editor.addAction('setProcessAsSubprocess', function(editor) {
		var cell = editor.graph.getSelectionCell();

		if (cell != null) {
			var prowimType = cell.getAttribute('prowimtype');
			if (prowimType == "PROCESS") {
				var prowimID = cell.getAttribute('prowimid');
				EditorProxy.setSubProcessFlagForProcess(prowimID);
				editor.graph.model.beginUpdate();
				try {
					editor.graph.setCellStyles("rounded", '1');
					editor.graph.setCellStyles("shape", 'subProcessShape');
				} finally {
					editor.graph.model.endUpdate();
				}

				editor.graph.refresh(cell);
				alert(mxResources.get('processIsSub'));
			}
		}
	});

	
	// Action to set the sub process parameter
	editor.addAction('editSubProcess', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
				EditorProxy.editProcess(cell.getAttribute('subprocessid'));
			}
		}
	});
	
	// Action to edit a process
	editor.addAction('editProcess', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if ((cell != null) && (cell.getAttribute('prowimtype') == 'PROCESS_LANDSPACE'))
			{
				EditorProxy.editProcess(cell.getAttribute('elementid'));
			}
		}
	});



	// Action to set the sub process parameter
	editor.addAction('showSubProcessViewer', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
				EditorProxy.showProcess(cell.getAttribute('subprocessid'));
			}
		}
	});

	// Action to set the sub process parameter
//	editor.addAction('setActivityAsAuto', function(editor) {
//		if (editor.graph.isEnabled()) {
//			var cell = editor.graph.getSelectionCell();
//			if (cell != null) {
//				EditorProxy.setActivityAsAuto(cell.getAttribute('prowimid'));
//				editor.graph.setCellStyles("strokeColor", '#006600');
//				editor.graph.setCellStyles("strokeWidth", '3');
//			}
//		}
//	});

	// Action to set the sub process parameter
//	editor.addAction('setActivityAsManual', function(editor) {
//		if (editor.graph.isEnabled()) {
//			var cell = editor.graph.getSelectionCell();
//			if (cell != null) {
//				EditorProxy.setActivityAsManual(cell.getAttribute('prowimid'));
//				editor.graph.setCellStyles("strokeColor", '#444444');
//				editor.graph.setCellStyles("strokeWidth", '0');
//			}
//		}
//	});

	// Action to set the product parameter
	editor.addAction('addRole', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();

			if (cell != null) {
					var activityID = cell.getAttribute('prowimid');
					var model = editor.graph.getModel();
					var proccessID = model.getCell("2")
							.getAttribute('prowimid');
					EditorProxy.addProcessElementToActivity(proccessID, activityID, 'Role');
			}
		}
	});

	// Action to set the product parameter
	editor.addAction('addMean', function(editor) {
		if (editor.graph.isEnabled()) {
			var cell = editor.graph.getSelectionCell();

			if (cell != null) {
					var activityID = cell.getAttribute('prowimid');
					var model = editor.graph.getModel();
					var proccessID = model.getCell("2")
							.getAttribute('prowimid');
					EditorProxy.addProcessElementToActivity(proccessID, activityID, 'Mean');
			}
		}
	});

	function showColorPicker(editor, callback, curColor) {
		var input = document.createElement("span");
		
		var iFrameWidth = $(window.document).width();
		var iFrameHeight = $(window.document).height();

		$(input).css( {
		'position' : 'absolute',
		'left' : (iFrameWidth / 2) - (356 / 2) + 'px',
		'top' : (iFrameHeight / 2) - 100 + 'px'

	});

		document.body.appendChild(input);
		
		$(input).jPicker(
		{
			'window' : {title : 'Farbe wählen', position : {x : 'screenCenter', y : 'center' },   expandable: false,  effects:
		    {
			      type: 'fast', // effect used to show/hide an expandable picker. Acceptable values "slide", "show", "fade"
			      speed:
			      {
			        show: 'slow', // duration of "show" effect. Acceptable values are "fast", "slow", or time in ms
			        hide: 'fast' // duration of "hide" effect. Acceptable value are "fast", "slow", or time in ms
			      }
			    }},
			'images' : { 'clientPath' : 'js/jpicker/images/' }
		},
        function(color, context)
        {
          var all = color.val('all');
          if (all == null)
        	  callback('#000000');
          else
        	  callback('#'+all.hex);	
          
          this.hide();
          color.destroy();
        },
        function(color, context)
        {
        },
        function(color, context)
        {
        	this.hide();
        	color.destroy();
        }
        );      
	}

	editor.addAction('fillColor', function(editor) {

		showColorPicker(editor, function(hexEncodedColor) {
			editor.graph.model.beginUpdate();
			try {
				editor.graph.setCellStyles("fillColor", hexEncodedColor);
			} finally {
				editor.graph.model.endUpdate();
			}
		}, editor.graph.getCellStyle(editor.graph.getSelectionCell()).fillColor);
	});
	
	editor.addAction('strokeColor', function(editor) {

		showColorPicker(editor, function(hexEncodedColor) {
			editor.graph.model.beginUpdate();
			try {
				editor.graph.setCellStyles("strokeColor", hexEncodedColor);
			} finally {
				editor.graph.model.endUpdate();
			}
		}, editor.graph.getCellStyle(editor.graph.getSelectionCell()).strokeColor);
	});
	
	editor.addAction('gradientColor', function(editor) {

		showColorPicker(editor, function(hexEncodedColor) {
			editor.graph.model.beginUpdate();
			try {
				editor.graph.setCellStyles("gradientColor", hexEncodedColor);
			} finally {
				editor.graph.model.endUpdate();
			}
		}, editor.graph.getCellStyle(editor.graph.getSelectionCell()).gradientColor);
	});
}
