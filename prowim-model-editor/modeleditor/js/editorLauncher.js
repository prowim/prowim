// Handles all errors occurring in JS environment
window.onerror = function(message, fileURI, lineOfCode){
	
	try {
		if (fileURI.indexOf("mxGraph")== -1)
		{
			BFShowErrorDialog(message,fileURI,lineOfCode);
		}
		
	} catch (e) {
		
		alert("An error occured in "+fileURI+"(:"+lineOfCode+") \n"+message);
	}
}

function startupEditor() {
	loadMainClass(1, 'config/WorkflowEditor.xml', false);
}

function startupViewer() {
	loadMainClass(1, 'config/WorkflowViewer.xml', true);
}

// wait for all required BrowserFunction to be loaded, then start the model
// editor
function loadMainClass(iteration, config, viewer) {
	// Don't wait longer than 30 seconds (1 iteration lasts 100ms)
	if (iteration > 300) {
		// get the missing browser functions to put them out in the alert box
		var missingFunctions = new Array();
		if (neededBrowserFunctions != undefined) {
			for ( var i = 0; i < neededBrowserFunctions.length; i++) {
				if (window[neededBrowserFunctions[i]] == undefined) {
					missingFunctions.push(neededBrowserFunctions[i]);
				}
			}
		}
		alert("Cannot load modelEditor. Not all needed BrowserFunction found: "
				+ missingFunctions.join(",\n"));

		// remove to blocking overlay
		getQXModelEditor().hideOverlay();

		return;
	}
	var allFunctionsfound = true;

	if (typeof neededBrowserFunctions !== 'undefined') {
		for ( var i = 0; i < neededBrowserFunctions.length; i++) {
			if (typeof window[neededBrowserFunctions[i]] === 'undefined') {
				allFunctionsfound = false;
				break;
			}
		}
	} else {
		allFunctionsfound = false;
	}

	if (allFunctionsfound) {
		try {
			mxConstants.DEFAULT_HOTSPOT = 1;

			var mainEditor = new editorMain(config);
			ApplicationEventsHandler.setEditor(mainEditor);
		} catch (e) {
			throw e;
		} finally {
			// remove to blocking overlay
			getQXModelEditor().hideOverlay();
		}
	} else {
		// try again in 100ms
		window.setTimeout("loadMainClass(" + (iteration + 1) + ", '" + config
				+ "', " + (viewer ? 'true' : 'false') + ")", 100);
	}
}