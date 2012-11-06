/**
 * 
 * 
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


/**
 * 
 * @param theEditor
 * @returns
 */
function EditorFunctions(theEditor) {
	EditorFunctions.editor = theEditor;
}
/** the editor object. */
EditorFunctions.editor;
/** Sets the editor attribute. */
EditorFunctions.setEditor = function(theEditor){
	EditorFunctions.editor = theEditor;
};

/** Gets the editor attribute. */
EditorFunctions.getEditor = function(theEditor){
	return EditorFunctions.editor;
};

EditorFunctions.cache = new Array();
EditorFunctions.cache.overlays = new Array();

    EditorFunctions.rename = function (editor, cell, newName) {
    	  if (cell != null && cell.getAttribute('prowimtype') != "GROUP")
    	  {
			  var prowimID = cell.getAttribute('prowimid');
			  var cells = editor.graph.getModel().getDescendants();
			  for (var j = 0; j < cells.length; j++)
			  {
				  if (cells[j].getAttribute('prowimid') != undefined)
				  {
					  if (cells[j].getAttribute('prowimid') == prowimID)
					  {
						  if (cells[j].getAttribute('prowimtype') != 'PROCESSPOINT'){
						      cells[j].setAttribute('label', newName);
						  }
						  editor.graph.refresh(cells[j]);
					  }
				  }
			   }
    	  }
    	  else if (cell.getAttribute('prowimtype') == "GROUP")
    	  {
			  cell.setAttribute('label', newName);
			  editor.graph.refresh(cell);
    	  }
    };
    
    
    EditorFunctions.copyCount = function(editor, cell){
    	var prowimID = cell.getAttribute('prowimid');
    	var cells = editor.graph.getModel().getDescendants();
    	var count = 0;
    	for (var j = 0; j < cells.length; j++){
			  if (cells[j].getAttribute('prowimid') != undefined){
				  if (cells[j].getAttribute('prowimid') == prowimID){
					 count++;
				  }
			  }
		}
    	return count;
    };
    	
	EditorFunctions.createInstance = 	function (prowimType, modelID) 
	{
		if (prowimType == "PROCESS")
		{
			 var createdId = EditorProxy.createObject("model", "Prozess");
		}
		else if(prowimType == "ACTIVITY" || prowimType == "SUBPROCESS")
		{
			var createdId = EditorProxy.createObject(modelID, "Aktivität");
		}
		else if(prowimType == "ROLE")
		{
			var createdId = EditorProxy.createObject(modelID, "Rolle");
		}
		else if(prowimType == "AGITATION")
		{
			var createdId = EditorProxy.createObject(modelID, "Tätigkeit");
		}
		else if(prowimType == "MEAN")
		{
			var createdId = EditorProxy.createObject(modelID, "Mittel");
		}
		else if(prowimType == "DEPOT")
		{
			var createdId = EditorProxy.createObject(modelID, "Ablage");
		}
//		else if(prowimType == "DECISION")
//		{
//			var createdId = EditorProxy.createObject(modelID, "Entscheidung");
//		}
//		else if(prowimType == "CONTROLFLOW")
//		{
//			var createdId = EditorProxy.createObject(modelID, "Kontrollfluss");
//		}
		else if(prowimType == "JOB")
		{
			var createdId = EditorProxy.createObject(modelID, "Tätigkeit");
		}
		else if(prowimType == "PRODUCT")
		{
			var createdId = EditorProxy.createObject(modelID, "Produkt");
		}
		else if(prowimType == "FUNCTION")
		{
			var createdId = EditorProxy.createObject(modelID, "Funktion");
		}
//		else if(prowimType == "AND" || prowimType == "OR" || prowimType == "COMBINATIONRULES")
//		{
//			var createdId = EditorProxy.createObject(modelID, "Verknüpfung");
//		}
		else if(prowimType == "PROCESSINFORMATION")
		{
			var createdId = EditorProxy.createObject(modelID, "Prozessinformation");
		}
		else
		{
			// Do nothing
		}
		
		return createdId;
	};

	// Function to create or set a role
	EditorFunctions.getExistRoles = function(modelID)
    {
		EditorProxy.selectProcessElement(modelID, 'Role');
    };

	// Function to create or set a depot
	EditorFunctions.getExistDepots = function(modelID)
    {
		EditorProxy.selectProcessElement(modelID, 'Depot');
    };

	// Function to create or set a mean
	EditorFunctions.getExistMeans = function(modelID)
    {
		EditorProxy.selectProcessElement(modelID, 'Mean');
    };

	EditorFunctions.validation = 	function (graph)
	{
		// Validations rules for Process *************************************************************************************

		// Process nodes needs 1 connected Targets
		// As source
		graph.multiplicities.push(new mxMultiplicity(
		   true, 'Process', null, null, 0, 1, ['Activity'],
		   mxResources.get('oneOutputConnectionAllowed'),
		   mxResources.get('connectionNotAllowed'),
		   null));

		// As target
		graph.multiplicities.push(new mxMultiplicity(
		   false, 'Process', null, null, 0, 1, ['Activity'],
		   mxResources.get('oneInputConnectionAllowed'),
		   mxResources.get('connectionNotAllowed'),
		   null));

		// 	AND *********************************************************************************************************
		// As source
		graph.multiplicities.push(new mxMultiplicity(
		   true, 'And', null, null, 0, 1, ['Activity', 'And', 'Or', 'CombinationRules', 'Decision', 'Subprocess'],
		   mxResources.get('oneOutputConnectionAllowed'),
		   mxResources.get('connectionNotAllowed'),
		   null));

		// As target
		graph.multiplicities.push(new mxMultiplicity(
		   false, 'And', null, null, 0, null, ['Activity', 'And', 'Or', 'CombinationRules', 'Decision', 'Subprocess'],
		   '',
		   mxResources.get('connectionNotAllowed'),
		   null));

		// 	OR *********************************************************************************************************
		// As source
		graph.multiplicities.push(new mxMultiplicity(
		   true, 'Or', null, null, 0, 1, ['Activity', 'And', 'Or', 'CombinationRules', 'Decision', 'Subprocess'],
		   mxResources.get('oneOutputConnectionAllowed'),
		   mxResources.get('connectionNotAllowed'),
		   null));

		// As target
		graph.multiplicities.push(new mxMultiplicity(
		   false, 'Or', null, null, 0, null, ['Activity', 'And', 'Or', 'CombinationRules', 'Decision', 'Subprocess'],
		   '',
		   mxResources.get('connectionNotAllowed'),
		   null));

		// 	CombinationRules *********************************************************************************************************
		// As source
		graph.multiplicities.push(new mxMultiplicity(
		   true, 'CombinationRules', null, null, 0, 1, ['Activity', 'And', 'Or', 'CombinationRules', 'Decision', 'Subprocess'],
		   mxResources.get('oneOutputConnectionAllowed'),
		   mxResources.get('connectionNotAllowed'),
		   null));

		// As target
		graph.multiplicities.push(new mxMultiplicity(
		   false, 'CombinationRules', null, null, 0, null, ['Activity', 'And', 'Or', 'CombinationRules', 'Decision', 'Subprocess'],
		   '',
		   mxResources.get('connectionNotAllowed'),
		   null));

		// 	Decision *********************************************************************************************************
		// As source
		graph.multiplicities.push(new mxMultiplicity(
		   true, 'Decision', null, null, 0, null, ['Activity', 'And', 'Or', 'CombinationRules', 'Subprocess'],
		   '',
		   mxResources.get('connectionNotAllowed')
		   ));

		// As target
		graph.multiplicities.push(new mxMultiplicity(
		   false, 'Decision', null, null, 0, 1, ['Activity'],
		   mxResources.get('oneInputConnectionAllowed'),
		   mxResources.get('connectionNotAllowed'),
		   null));


		// Validations rules for Role ************************************************************************************* 
		/// As target
		graph.multiplicities.push(new mxMultiplicity(
		   false, 'Role', null, null, 0, null, ['Process'],
		   mxResources.get('connectDirectionNotAllowed') ,
		   null)); 

		// As source
		graph.multiplicities.push(new mxMultiplicity(
		   true, 'Role', null, null, 1, null, ['Process', 'Activity', 'Subprocess'],
		   mxResources.get('roleSourceFirstdMsg'),
		   mxResources.get('roleSourceSecondMsg')
		   ));
		//*********************************************************************************************************
		

		// Validations rules for Mean ************************************************************************************* 
		// As target
		graph.multiplicities.push(new mxMultiplicity(
		   false, 'Mean', null, null, 0, 0, null,
		   mxResources.get('connectDirectionNotAllowed') ,
		   null)); 

		// As source
		graph.multiplicities.push(new mxMultiplicity(
		   true, 'Mean', null, null, 1, null, ['Activity'],
		   mxResources.get('toolSourceFirstdMsg'),
		   mxResources.get('toolSourceSecondMsg')
		   ));
		//*********************************************************************************************************
	};

	EditorFunctions.getIdFromURL = function( name )
	{
	  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
	  var regexS = "[\\?&]"+name+"=([^&#]*)";
	  var regex = new RegExp( regexS );
	  var results = regex.exec( window.location.href );
	  if( results == null )
	    return "";
	  else
	    return decodeURI(results[1]);
	};

	// function to remove given cells
	EditorFunctions.removeCells = function(graph,cells)
	{
		graph.removeCells(cells);
	};

	// Function to set overlay for a cell 
	EditorFunctions.updateOverlays = function(editor,cell)
	{
		if (cell != null)
		{
			var overlays = editor.graph.getCellOverlays(cell);
			if (overlays == null)
			{
				// Creates a new overlay with an image and a tooltip
				var overlay = new mxCellOverlay(
						new mxImage('images/overlays/information.png', 16, 16),
						'Diese Aktivität steht in Relation zu andaren Elementen.');
				
				// Sets the overlay for the cell in the graph
				editor.graph.addCellOverlay(cell, overlay);
			}
		}
	};


	// Initializes the model for the given model id
	// It updates the loaded model with additional attributes from ontology dynamicly
	EditorFunctions.initModel = function(editor, codec, modelID)
	{
		// Get Process elements
		var processElements = EditorProxy.getElementsOfProcess(modelID);
		if (processElements != null)
		{
			for(var i=0;i<processElements.length;i++)
			{
				var value =  processElements[i];
				var cellValue = value.id;

				// Get the object of given id
				var actDoc = codec.getElementById(value.id, "prowimid");

				// Get class of element
				var className = value.className;
				if (actDoc != null)
				{
					if ((className != "Verknüpfung") &&  (className != "Tätigkeit") &&  (className !="Funktion") &&  (className != "Entscheidung"))
					{
						// Filter to get shapes, which are more than one at the model 
						var cells = editor.graph.getModel().filterDescendants(
								function(cell) {
									return cellValue == cell.getAttribute("prowimid");
								}
						)

						var count = cells.length;
						for(var j=0;j<count;j++)
						{
							var actElement = cells[j];
							mxCodec.prototype.setAttribute(actElement, "label", value.name);
							mxCodec.prototype.setAttribute(actElement, "description", value.description);

							// Sets the overlay for the cell in the graph
							if (className == "Aktivität" && value.hasRelations == "true")
							{
								EditorFunctions.initOverlay(editor,actElement,'');
								EditorFunctions.initOverlayCache(value.id);
								EditorFunctions.updateOverlayCacheForIsRequired(value.id, true);
							}
							else if (className == "Aktivität" && value.hasRelations == "false")
							{
								EditorFunctions.initOverlayCache(value.id);	
							}

							var id = mxCodec.prototype.getId(actElement);
							codec.putObject(id, actElement);
						}
						cells.clear;
					}
				}
				// return a error if values are not exist in model-xml, extend of Rolle, Mittel, Ablage and Prozessinformation
				else if ((className != "Rolle") &&  (className != "Mittel") && (className !="Ablage") && (className != "Prozessinformation") )
					EditorProxy.log(value.name, "error", mxResources.get('elementDoesNotExist'));
			}
		}
		else
			EditorProxy.log(processElements, "error", mxResources.get('noAnswerFromServer'));
	};


	// Adds the items of the given list to the tooltip 
	EditorFunctions.addTooltipItems = function(arrayList, tooltip)
	{
		for(var k=0;k<arrayList.length;k++)
		{
			tooltip = tooltip + ' - '+ arrayList[k] + '\n';
		}

		return tooltip;
	}
	
	// Updates the overlay of the given cell with values from possibly updated overlay cache
	EditorFunctions.updateOverlay = function(editor, cell)
	{
		var cellID = cell.getAttribute("prowimid");
		
		if (cell.getAttribute("prowimtype") == "ACTIVITY" && EditorFunctions.cache.overlays[cellID].isOverlayRequiredList)
		{
			var knowledgeList = EditorFunctions.cache.overlays[cellID].knowledgeList;
			var roleList = EditorFunctions.cache.overlays[cellID].roleList;
			var meanList = EditorFunctions.cache.overlays[cellID].meanList;
			tooltip = '';
			
			if (knowledgeList != null && knowledgeList.length > 0) {
				tooltip = '<b>' + mxResources.get('knowledgeObjects')
						+ ':</b>\n';
				tooltip = EditorFunctions.addTooltipItems(knowledgeList, tooltip);
			}
			if (roleList != null && roleList.length > 0) {
				tooltip = tooltip + '<b>' + mxResources.get('roles')
						+ ':</b>\n';
				tooltip = EditorFunctions.addTooltipItems(roleList, tooltip);
			}
			if (meanList != null && meanList.length > 0) {
				tooltip = tooltip + '<b>' + mxResources.get('means')
						+ ':</b>\n';
				tooltip = EditorFunctions.addTooltipItems(meanList, tooltip);
			}
			
			var currentOverlay = editor.graph.getCellOverlays(cell);
			
			if(currentOverlay != null){
				currentOverlay[0].tooltip = tooltip;
			} else {
				EditorFunctions.initOverlay(editor,cell,tooltip);
			}
		}
	}	

	// Initialiazes the overlay for the given cell element like activity
	// Adds an info icon to a shape which holds the given tooltip
	// Adds a listener for loading the overlay informations content on deman by clicking the overlay
	EditorFunctions.initOverlay = function(editor,cell,tooltip)
	{
		var cellID = cell.getAttribute("prowimid");
		var overlay;
		
		if (EditorFunctions.cache.overlays[cellID] == undefined) {
			
			overlay = new mxCellOverlay(
					new mxImage('images/overlays/information_grey.png', 16, 16),tooltip);
			
		} else {
			
			if (EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded) {
				
				overlay = new mxCellOverlay(
						new mxImage('images/overlays/information.png', 16, 16),tooltip);
			} else {
				
				overlay = new mxCellOverlay(
						new mxImage('images/overlays/information_grey.png', 16, 16),tooltip);

			}
		}
		
		// Set the click listener
		overlay.addListener(mxEvent.CLICK, 	function(sender,evt)
		{
			var currentOverlay = sender;
			if (currentOverlay != null)
			{
				var cellID = cell.getAttribute("prowimid");
				currentOverlay.image = new mxImage('images/overlays/information-loading.png', 16, 16);
				editor.graph.refresh(cell);

				EditorFunctions.loadInfos(cellID);
				EditorFunctions.updateOverlay(editor, cell);
				EditorProxy.showInfo(currentOverlay.tooltip);

				currentOverlay.image = new mxImage('images/overlays/information.png', 16, 16);
				editor.graph.refresh(cell);
			}
		});

				
		var mxCellRendererInstallCellOverlayListeners = mxCellRenderer.prototype.installCellOverlayListeners;
		mxCellRenderer.prototype.installCellOverlayListeners = function(state, overlay, shape)
		{
		   mxCellRendererInstallCellOverlayListeners.apply(this, arguments);

		   mxEvent.addListener(shape.node, 'mouseover', function (evt)
		   {
		      // Fires an event via the overlay instance
		      overlay.fireEvent(new mxEventObject('mouseover',
		            'event', evt, 'cell', state.cell));
		   });
		};


		overlay.addListener(mxEvent.mouseover, function(sender,evt)
		{
			var currentOverlay = sender;
			if (currentOverlay != null)
			{
				var tooltip = currentOverlay.tooltip;
				if (tooltip  == "")
				{
					var cellID = cell.getAttribute("prowimid");
					currentOverlay.image = new mxImage('images/overlays/information-loading.png', 16, 16);
					editor.graph.refresh(cell);

					EditorFunctions.loadInfos(cellID);
					EditorFunctions.updateOverlay(editor, cell);

					currentOverlay.image = new mxImage('images/overlays/information.png', 16, 16);
					editor.graph.refresh(cell);
				}
			}
		});

		editor.graph.addCellOverlay(cell, overlay);
	}

	// Loads the infos for overlay tooltips in cache
	EditorFunctions.loadInfos = function(cellID){
		
		if (EditorFunctions.isOverlayCacheEmpty(cellID) && !EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded) {
			
			EditorFunctions.cache.overlays[cellID].knowledgeList = EditorProxy.getKnowledgeObjects(cellID);
			EditorFunctions.cache.overlays[cellID].meanList = EditorProxy.getActivityMeans(cellID);
			EditorFunctions.cache.overlays[cellID].roleList = EditorProxy.getActivtiyRoles(cellID);
			EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded = true;
		}
	}
	
	// Initializes the cache for the overlay tooltips and for isOverlayRequired-Flag and isOverlayInfoLoaded-Flag
	EditorFunctions.initOverlayCache = function(id)
	{
		EditorFunctions.cache.overlays[id] = new Array();
		EditorFunctions.cache.overlays[id].knowledgeList = new Array();
		EditorFunctions.cache.overlays[id].roleList = new Array();
		EditorFunctions.cache.overlays[id].meanList = new Array();
		EditorFunctions.cache.overlays[id].isOverlayRequiredList = false;
		EditorFunctions.cache.overlays[id].isOverlayInfoLoaded = false;
	}

	// Updates the whole cache for overlay informations for the given cell
	EditorFunctions.updateOverlayCache = function(cellID)
	{
		EditorFunctions.updateOverlayCacheForKnowledge(cellID);
		EditorFunctions.updateOverlayCacheForRole(cellID);
		EditorFunctions.updateOverlayCacheForMean(cellID);
	}

	// Updates the cache for overlay information regarding the knowledge objects
	EditorFunctions.updateOverlayCacheForKnowledge = function(cellID)
	{
		// Was Cache initialized with overlay infos
		if (!EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded) {
			
			EditorFunctions.loadInfos(cellID);
			EditorFunctions.cache.overlays[cellID].isOverlayRequiredList = true;
			
		// Update or empty cache
		} else {
			
			EditorFunctions.cache.overlays[cellID].knowledgeList = EditorProxy.getKnowledgeObjects(cellID);
			EditorFunctions.checkUpdateOverlayCacheIsRequired(cellID);
		}
		
	}
	
	// Updates the cache for overlay information regarding the roles
	EditorFunctions.updateOverlayCacheForRole = function(cellID)
	{
		// Was Cache initialized with overlay infos
		if (EditorFunctions.cache.overlays[cellID] != undefined && !EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded) {
			
			EditorFunctions.loadInfos(cellID);
			EditorFunctions.cache.overlays[cellID].isOverlayRequiredList = true;
			
		// Update or empty cache
		} else {
			
			EditorFunctions.cache.overlays[cellID].roleList = EditorProxy.getActivtiyRoles(cellID);
			EditorFunctions.checkUpdateOverlayCacheIsRequired(cellID);
		}
	}

	// Updates the cache for overlay information regarding the means
	EditorFunctions.updateOverlayCacheForMean = function(cellID)
	{
		// Was Cache initialized with overlay infos
		if (!EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded ) {
			
			EditorFunctions.loadInfos(cellID);
			EditorFunctions.cache.overlays[cellID].isOverlayRequiredList = true;
			
			// Update or empty cache
		} else {
			
			EditorFunctions.cache.overlays[cellID].meanList = EditorProxy.getActivityMeans(cellID);
			EditorFunctions.checkUpdateOverlayCacheIsRequired(cellID);
		}
		
	}
	
	// Checks if the cache for the flag 'overlayIsRequired' must be changed
	// Changes to false if the last relation was removed, changes to true if the first relation are added
	EditorFunctions.checkUpdateOverlayCacheIsRequired = function(cellID){
		
		if (EditorFunctions.isOverlayCacheEmpty(cellID) && EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded) {
			
			EditorFunctions.updateOverlayCacheForIsRequired(cellID, false);
			EditorFunctions.cache.overlays[cellID].isOverlayInfoLoaded = false;
		} 
	}
	
	//Updates the cache for the flag if an overlay is required for the given cell
	EditorFunctions.updateOverlayCacheForIsRequired = function(cellID,isRequired)
	{
		EditorFunctions.cache.overlays[cellID].isOverlayRequiredList = isRequired;
	}
	
	// Checks if the overlay cache for the given cell is empty
	EditorFunctions.isOverlayCacheEmpty = function(cellID)
	{
		var knowledgeList = EditorFunctions.cache.overlays[cellID].knowledgeList;
		var roleList = EditorFunctions.cache.overlays[cellID].roleList;
		var meanList = EditorFunctions.cache.overlays[cellID].meanList;
		
		if ((knowledgeList != null && knowledgeList.length > 0) || (roleList != null && roleList.length > 0) || (meanList != null && meanList.length > 0))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	