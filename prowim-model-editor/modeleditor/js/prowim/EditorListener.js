/**
 * This file is part of ProWim.

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
function EditorListener() {}


	/*
	 * Add Listener on the model to be notified of changes made to the model. 
	 * After each change, the current XML-model will be synchronized to the server
	 */
	EditorListener.addUpdateListener = function(editor)
	{
		var notifierFunction = function(sender, evt) {
			var enc = new mxCodec(mxUtils.createXmlDocument());
			var node = enc.encode(editor.graph.getModel());
			var header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			var xml = header + mxUtils.getXml(node);
			getQXModelEditor().setModelXML(xml);
			getQXModelEditor().transferModelXML();
		};
		
		editor.graph.model.addListener(mxEvent.CHANGE, notifierFunction);
	};

	EditorListener.addCells = function(editor, modelID)
	{
		// Add listener to create objects in ontology
		editor.graph.addListener(mxEvent.ADD_CELLS, function(sender, evt)
		{
			var cell = evt.getProperties().cells;

			var prowimType;
			var isDeleted = false;
			if (editor.graph.model.isEdge(cell[0]))
			{
				var sourceProwimType = cell[0].source.getAttribute('prowimtype');
				var targetProwimType = cell[0].target.getAttribute('prowimtype');

				if ((cell[0].getStyle() == null) || (cell[0].getStyle() == '')) 
				{
					if (sourceProwimType == 'PROCESS' || sourceProwimType == 'PROCESSPOINT')
					{
						cell[0].setStyle( "relationEdge");
					}
					else if (sourceProwimType == 'ACTIVITY' || sourceProwimType == 'SUBPROCESS')
					{
						if (targetProwimType == 'PROCESS' || targetProwimType == 'PROCESSPOINT')
							cell[0].setStyle( "relationEdge");
						else if (targetProwimType == 'PROCESSTYPE_LANDSPACE' || targetProwimType == 'PROCESS_LANDSPACE')
						{
							cell[0].setStyle( "relationEdge");
							isDeleted = true;
						}
						else
							cell[0].setStyle( "controlFlowEdge");
					}
					else if (sourceProwimType == 'ROLE')
					{
						if (targetProwimType == 'ACTIVITY' || targetProwimType == 'SUBPROCESS')
							cell[0].setStyle( "jobEdge");
						else if (targetProwimType == 'PROCESS' || targetProwimType == 'PROCESSPOINT')
							cell[0].setStyle( "relationEdge");
					}
					else if (sourceProwimType == 'DEPOT')
					{
						cell[0].setStyle( "productEdge");
					}
					else if (sourceProwimType == 'MEAN')
					{
						cell[0].setStyle("functionEdge");
					}
					else if (sourceProwimType == 'AND' || sourceProwimType == 'OR' || sourceProwimType == 'DECISION' || sourceProwimType == 'COMBINATIONRULES' )
					{
						cell[0].setStyle("controlFlowEdge");
					}
					else if (sourceProwimType == 'PROCESSTYPE_LANDSPACE' || sourceProwimType == 'PROCESS_LANDSPACE' )
					{
						cell[0].setStyle("controlFlowEdge");
						isDeleted = true;
					}

				}	
				
				if (editor.graph.model.getStyle(cell[0]) == "relationEdge")
				{
					prowimType = "RELATION";
					var count = null;
					
					if (sourceProwimType == 'ACTIVITY')
						count = cell[0].target.getEdgeCount();
					else if (targetProwimType == 'ACTIVITY')
						count = cell[0].source.getEdgeCount();
					
					if (count != null && count < 2)
						{
							cell[0].setAttribute("prowimtype", "RELATION");
							prowimType = "RELATION";
							EditorProxy.getPossibleRelations(cell[0].source.getAttribute('prowimid'), cell[0].target.getAttribute('prowimid'));
						}
					else
						{
							EditorFunctions.removeCells(editor.graph, [cell[0]]);
							isDeleted = true;
							alert(mxResources.get('oneInOutConnectionAllowed'));
						}
				}
				else if (editor.graph.model.getStyle(cell[0]) == "controlFlowEdge")
				{
					if ((sourceProwimType == 'ACTIVITY' &&  targetProwimType != 'ACTIVITY' && targetProwimType != 'AND' && targetProwimType != 'OR'
							&& targetProwimType != 'DECISION' && targetProwimType != 'COMBINATIONRULES' && targetProwimType != 'SUBPROCESS')
							
					 || (targetProwimType == 'ACTIVITY' &&  sourceProwimType != 'ACTIVITY' && sourceProwimType != 'AND' && sourceProwimType != 'OR'
						&& sourceProwimType != 'DECISION' && sourceProwimType != 'COMBINATIONRULES' && sourceProwimType != 'SUBPROCESS' && sourceProwimType != 'PROCESSTYPE_LANDSPACE' &&  sourceProwimType != 'PROCESS_LANDSPACE' )
					)
					{
						EditorFunctions.removeCells(editor.graph, [cell[0]]);
						isDeleted = true;
						alert(mxResources.get('connectionNotAllowed'));
					}
					else
					{
						if ( targetProwimType != 'ACTIVITY' && targetProwimType != 'SUBPROCESS')
						{
							cell[0].setAttribute("prowimtype", "CONTROLFLOW");
							prowimType = "CONTROLFLOW";
						}
						else
						{
							cell[0].setAttribute("prowimtype", "CONTROLFLOW");
							prowimType = "CONTROLFLOW";
							if (sourceProwimType == 'DECISION')
							{
								mxUtils.setCellStyles(editor.graph.model, cell, "textOpacity", "100");
							}
						}
					}
				} 
				else if (editor.graph.model.getStyle(cell[0]) == "jobEdge")
				{
						cell[0].setAttribute("prowimtype", "JOB");
						prowimType = "JOB";
				}
				else if (editor.graph.model.getStyle(cell[0]) == "productEdge")
				{
					if ((sourceProwimType == 'ACTIVITY' && targetProwimType == 'ACTIVITY') ||
						(sourceProwimType == 'DEPOT' && targetProwimType == 'ACTIVITY') ||
						(sourceProwimType == 'ACTIVITY' && targetProwimType == 'DEPOT'))
					{
						cell[0].setAttribute("prowimtype", "PRODUCT");
						prowimType = "PRODUCT";
					}
					else
					{
						EditorFunctions.removeCells(editor.graph, [cell[0]]);
						isDeleted = true;
						alert(mxResources.get('connecNotAllowedSelectCon'));
					}
				}
				else if (editor.graph.model.getStyle(cell[0]) == "functionEdge")
				{
					if ((sourceProwimType == 'MEAN' && targetProwimType == 'ACTIVITY'))
					{
						cell[0].setAttribute("prowimtype", "FUNCTION");
						prowimType = "FUNCTION";
					}
					else
					{
						EditorFunctions.removeCells(editor.graph, [cell[0]]);
						isDeleted = true;
						alert(mxResources.get('connectionNotAllowed'));
					}
				}
			}
			else
			{
				prowimType = cell[0].getAttribute("prowimtype");
			}
		
			// Create vertex or edges in database
			if (!isDeleted && (cell != null) && (prowimType != 'OR') && (prowimType != 'AND') && (prowimType != 'DECISION') && (prowimType != 'CONTROLFLOW') && (prowimType != 'SWIMLANE') && (prowimType != 'GROUP') && (prowimType != 'ROLE') && (prowimType != 'MEAN') && (prowimType != 'DEPOT') && (prowimType != 'PROCESSPOINT')  && (prowimType != 'PROCESSTYPE_LANDSPACE') && (prowimType != 'PROCESS_LANDSPACE'))
			{	
				var instanceID = EditorFunctions.createInstance(prowimType, modelID);
				if (prowimType == "PROCESS")
					modelID = instanceID; 

				if (prowimType == "ACTIVITY")
					EditorFunctions.initOverlayCache(instanceID);
			
//				if (prowimType == "CONTROLFLOW")
//				{
//					EditorProxy.connectActivityControlFlow(cell[0].source.getAttribute('prowimid'), cell[0].target.getAttribute('prowimid'), instanceID);
//				}
				else if (prowimType == "JOB")
				{
					EditorProxy.connectActivityRole(cell[0].target.getAttribute('prowimid'), cell[0].source.getAttribute('prowimid'), instanceID);
					cell[0].setAttribute('label', '');

					// Actualize the overlay array
					var targetProwimID = cell[0].target.getAttribute('prowimid');
					EditorFunctions.updateOverlayCacheForRole(targetProwimID);
				}
				else if (prowimType == "PRODUCT")
				{
					EditorProxy.setProduct(cell[0].source.getAttribute('prowimid'), cell[0].target.getAttribute('prowimid'), instanceID);
					var procInfoID = EditorFunctions.createInstance('PROCESSINFORMATION', modelID);
					EditorProxy.setRelationValue(instanceID, 'hat_Prozessinformation', procInfoID);
					EditorProxy.setRelationValue(procInfoID, 'ist_vom_Informationstyp', 'ShortText');
					EditorProxy.rename(procInfoID, 'Neuer Produktparameter');
					cell[0].setAttribute('label', '');

				}
				else if (prowimType == "FUNCTION")
				{
					EditorProxy.connectActivityMittel(cell[0].target.getAttribute('prowimid'), cell[0].source.getAttribute('prowimid'), instanceID);
					cell[0].setAttribute('label', '');
					
					// Actualize the overlay array
					var targetProwimID = cell[0].target.getAttribute('prowimid');
					EditorFunctions.updateOverlayCacheForMean(targetProwimID,true);
				}
//				else if(prowimType == "AND")
//				{
//					EditorProxy.setCombinationRules(instanceID, "AND_Conjunction");
//					cell[0].setAttribute('label', mxResources.get('and'));
//				}
//				else if(prowimType == "OR")
//				{
//					EditorProxy.setCombinationRules(instanceID, "OR_Conjunction");
//					cell[0].setAttribute('label', mxResources.get('or'));
//				}
				else if(prowimType == "COMBINATIONRULES")
				{
					EditorProxy.getCombinationRules(instanceID);
				}
//				else if(prowimType == "DECISION")
//				{
//					EditorProxy.setActivationRules(instanceID, 'Entscheidung_Standard_Auswahl');
//					var procInfoID = EditorFunctions.createInstance('PROCESSINFORMATION', modelID);
//					EditorProxy.setRelationValue(instanceID, 'hat_Prozessinformation', procInfoID);
//					EditorProxy.setRelationValue(procInfoID, 'ist_vom_Informationstyp', 'ComboBoxControlFlow');
//					cell[0].setAttribute('label', '');
//				}
				else if(prowimType == "SUBPROCESS")
				{
					EditorProxy.getAllExistSubProcesses(instanceID);
				}
				
				
				if (prowimType != "RELATION")
				{
					cell[0].setAttribute("prowimid", instanceID);
					if(prowimType != "AND" && prowimType != "OR" && prowimType != "DECISION" && prowimType != "DECISION" && prowimType != "JOB" && prowimType != "FUNCTION")
					{
						var instanceName = EditorProxy.getName(instanceID);
						cell[0].setAttribute('label', instanceName);
					}
				}
			} else if ((prowimType == 'SWIMLANE'))
			{
				EditorProxy.callPickElementDialog(true, "SWIMLANE");
			}
			else if(prowimType == "ROLE")
			{
				EditorFunctions.getExistRoles(modelID);
			}
			else if(prowimType == "MEAN")
			{
				EditorFunctions.getExistMeans(modelID);
			}
			else if(prowimType == "DEPOT")
			{
				EditorFunctions.getExistDepots(modelID);
			}
			else if(prowimType == "PROCESSPOINT")
			{
				cell[0].setAttribute("prowimid", modelID);
			}
			else if(prowimType == "PROCESSTYPE_LANDSPACE")
			{
				EditorProxy.callPickElementDialog(true, "PROCESSTYPE_LANDSPACE");
			}
			else if(prowimType == "PROCESS_LANDSPACE")
			{
				EditorProxy.callPickElementDialog(true, "PROCESS_LANDSPACE");
			}
			else if(prowimType == "DECISION")
			{
				cell[0].setAttribute('label', '');
			}
			else if(prowimType == "AND")
			{
				cell[0].setAttribute('label', mxResources.get('and'));
			}
			else if(prowimType == "OR")
			{
				cell[0].setAttribute('label', mxResources.get('or'));
			}

		});
	};
	
	
	// action to rename a cell
	EditorListener.renameCell = function(editor)
	{
		editor.graph.addListener(mxEvent.LABEL_CHANGED, function(sender, evt)
		{
			var cell = editor.graph.getSelectionCell();
			var newName ;
			if (cell != null)
			{
			  var cellID = cell.getAttribute("prowimid");
			  newName = cell.getAttribute('label');
			  EditorFunctions.rename(editor, cell, newName);
			  if (cell.getAttribute('prowimtype') != "GROUP")
				  EditorProxy.rename(cellID, newName);
			} 
		});
	};
	
	
	EditorListener.dblClick  = function(editor)
	{
		editor.graph.addListener(mxEvent.DOUBLE_CLICK, function(sender, evt)
		{
			var cell = editor.graph.getSelectionCell();
			if (cell != null) {
				if (cell.getAttribute('prowimtype') != undefined) {
					if ((cell.getAttribute('prowimtype') == "RELATION" ) || (cell.getAttribute('prowimtype') == "AND")
				    		  || (cell.getAttribute('prowimtype') == "OR") || (cell.getAttribute('prowimtype') == "PROCESSPOINT"))
					{
						editor.graph.stopEditing(false);
					}
					else if (cell.getAttribute('prowimtype') == "SWIMLANE")
					{
						editor.graph.stopEditing(false);
						EditorProxy.callPickElementDialog(false, "SWIMLANE");
					}
					else if (cell.getAttribute('prowimtype') == "COMBINATIONRULES")
					{
						editor.graph.stopEditing(false);
						EditorProxy.getCombinationRules(cell.getAttribute('prowimid'));
					}
					else if (cell.getAttribute('prowimtype') == "GROUP")
					{
							//Do nothing. Allow editing
					}
					else if (cell.getAttribute('prowimtype') == "PROCESSTYPE_LANDSPACE")
					{
						editor.graph.stopEditing(false);
						EditorProxy.callPickElementDialog(false, "PROCESSTYPE_LANDSPACE");
					}
					else if (cell.getAttribute('prowimtype') == "PROCESS_LANDSPACE")
					{
						editor.graph.stopEditing(false);
						EditorProxy.callPickElementDialog(false, "PROCESS_LANDSPACE");
					}
					else
					{
						var elementID = cell.getAttribute('prowimid');
						EditorProxy.editNameAndDescription(elementID);
						
					}
				}
			}
		});
	};
	
	
	EditorListener.click  = function(editor)
	{
		editor.graph.addListener(mxEvent.CLICK, function(sender, evt)
		{
			//Forward click to parent document, which connects to the Lifecycle
			getQXModelEditor().handleEditorClick();
			
			var cell = editor.graph.getSelectionCell();
			
			if (cell != null && (cell.getAttribute('prowimtype') != undefined)) 
			{
				var elementID = cell.getAttribute('prowimid');
				if (elementID != undefined)
				{
				    	BFShowAttributes(elementID);
				}
			}
		});
	};
	
	
	// Check by changes for overlays and set this if exists
	checkForOverlays = function (editor, model, cell)
	{
	    var childCount = cell.getChildCount();

	    for (var i = 0; i < childCount; i++) {
		      var child = cell.getChildAt(i);

		      if (child.getChildCount() > 0)
		      {
		    	  checkForOverlays(editor, model, child);
		      }
		      else 
		      {
		    	  if (model.isEdge(child)) continue;

		    	  var value =  child.getAttribute('prowimid');
		    	  if (value != undefined)
		    		  EditorFunctions.updateOverlay(editor, child);

		      }
		    }
	}
	
	
	EditorListener.change  = function(editor)
	{
		editor.graph.getModel().addListener(mxEvent.CHANGE, function(sender, evt)
		{
		    var model = editor.graph.getModel();
		    var parent = editor.graph.getDefaultParent();
		    checkForOverlays(editor, model, parent);
		});
	};
	
	
	EditorListener.cellsRemoved  = function(editor)
	{
		editor.graph.addListener(mxEvent.CELLS_REMOVED, function(sender, evt)
		{
			var cells = evt.getProperty('cells');
			
			for (var i=0; i< cells.length;i++)
			{
				var cell = cells[i];
				if (cell.isEdge())
				{
					var type = cell.getAttribute('prowimtype');
					var targetCellID = cell.target.getAttribute('prowimid');
					if (type == 'JOB')
					{
						EditorFunctions.updateOverlayCacheForRole(targetCellID);
						EditorFunctions.updateOverlay(editor, cell.target);
					}
					else if (type == 'FUNCTION')
					{
						EditorFunctions.updateOverlayCacheForMean(targetCellID);
						EditorFunctions.updateOverlay(editor, cell.target);
					}
				}
			}
		});
	};
