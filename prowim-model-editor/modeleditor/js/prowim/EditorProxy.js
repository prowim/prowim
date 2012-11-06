/**
 A proxy object that implements all methods of the EditorBean to provide web service calls to the prowim EditorRemote interface.	

 @author Saad Wardi. 
 
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
*/
function EditorProxy() {}

// the createObject web service
EditorProxy.createObject = function(modelID, oid){
	return BFCreateObject(modelID, oid);
};

// the deleteObject web service
EditorProxy.deleteObject = function(id){
	BFDeleteObject(id);
};

//the getName web service
EditorProxy.getName = function(id){
	return BFGetName(id);
};

//the getName web service
EditorProxy.rename = function(id, newName){
	BFRename(id, newName);
	return newName;
};

//the getDescription web service
EditorProxy.getDescription = function(id){
	return BFGetDescription(id);
};

//Set process as approve
EditorProxy.openModel = function(modelID){
	BFApproveProcess(modelID);
};

//Set process as disapprove
EditorProxy.closeModel = function(modelID){
	BFDisapproveProcess(modelID);
};

//the getModels web service
EditorProxy.getModels = function(){
    var result = BFGetModels();
	for(var i = 0; i < result.length; i++)
	{
	   var stringToSplit = result[i];
	   var stringsSplited = stringToSplit.split("/");

	   var p = new Process(stringsSplited[0], stringsSplited[1]);
	   models[i] = p;
	}
	
	return models;
};

// the saveModel web service
EditorProxy.saveModelAsXML = function(createNewVersion){
	BFSaveProcessModelAsXML(createNewVersion);
};


// the getPossibleRelations web service
EditorProxy.getPossibleRelations = function(source, target){
	return BFGetPossibleRelations(source, target);
};

// the setRelationValue web service
EditorProxy.setRelationValue = function(instanceID, slot, relationID){
	BFSetRelationValue(instanceID, slot, relationID);
};


//the removeRelationValue web service
EditorProxy.removeRelationValue = function(source,  value){
	BFRemoveRelationValue(source, value);
};

// the connectActivityRole web service
EditorProxy.connectActivityRole = function(activityID, roleID, taskID){
	BFConnectActivityRole(activityID, roleID, taskID);
};

// the setProduct web service
EditorProxy.setProduct = function(source, target, productID){
	BFSetProduct(source, target, productID);
};

//the getCombinationRules web service
EditorProxy.getCombinationRules = function(elementID){
	return BFGetPossibleCombinationRules(elementID);
};

// the connectActivityMittel web service
EditorProxy.connectActivityMittel = function(activityID, mittelID, functionID){
	BFConnectActivityMean(activityID, mittelID, functionID);
};

//Checks, if the instance is global or local
EditorProxy.isGlobal = function(id){
	return BFIsGlobal(id);
};

// the getCombinationRule web service
EditorProxy.getCombinationRule = function(controlflowID){
	return BFGetCombinationRule(controlflowID);
};

// the .bendControlFlow web service
EditorProxy.bendControlFlow = function(edge,oldSource, oldTarget, newSource, newTarget){
	return BFBendControlFlow(edge, oldSource, oldTarget, newSource, newTarget);
};


/**
 * flag true, if shape should deletes by cancel dialog, else false
 * elementType element, which is select for in dialog
 */
EditorProxy.callPickElementDialog = function(flag, elementType){
	BFCallPickElementDialog(flag, elementType);
};


//the getCombinationRule web service
EditorProxy.getProcessInfoOfObj = function(objectID){
	return BFGetProcessInfoOfObj(objectID);
};

//Returns all exists sub processes
EditorProxy.getAllExistSubProcesses = function(activityID){
	BFGetAllExistSubProcesses(activityID);
};

// shows a subprocess
EditorProxy.showProcess = function(subProcessID){
	BFShowProcess(subProcessID);
	
};

//shows a subprocess
EditorProxy.editProcess = function(subProcessID){
	return BFEditProcess(subProcessID);
	
};

//Set a process as sub process
EditorProxy.setSubProcessFlagForProcess = function(processID){
	BFSetSubProcessFlagForProcess(processID);
};

//Set a process as sub process
EditorProxy.deleteElementFromModel = function(processID, elementID){
	BFDeleteElementFromModel(processID, elementID);
};

//getElemntsOfProcess
EditorProxy.getElementsOfProcess = function(processID){
	return BFGetElementsOfProcess(processID);
};

//getElemntsOfProcess
EditorProxy.log = function(elementID, logFlag, message){
	return BFLog(elementID, logFlag, message);
};

// Save a process model as a new version
EditorProxy.saveProcessAsNewVersion = function(processID){
	BFSaveProcessAsNewVersion(processID);
};

//Returns the knowledge objects name of the given element id.
EditorProxy.getKnowledgeObjects = function(elementID){
	return BFGetKnowledgeObjects(elementID);
};

//Returns the roles name of the given activity id.
EditorProxy.getActivtiyRoles = function(elementID){
	return BFGetActivtiyRoles(elementID);
};

//Returns the means name of the given activity id.
EditorProxy.getActivityMeans = function(elementID){
	return BFGetActivityMeans(elementID);
};

//Calls a dialog, which show exists roles, means or depots. User can select one of this elements or create a new one.
EditorProxy.addProcessElementToActivity = function(proccessID, activityID,  className){
	return BFAddProcessElementToActivity(proccessID, activityID, className);
};

EditorProxy.showInfo = function(infoMessage){
	BFShowActivityRelationsDialog(infoMessage);
}

EditorProxy.showModelURL = function(modelID){
	BFShowModelURL(modelID);
}

EditorProxy.editNameAndDescription = function(elementID){
	BFEditNameAndDescription(elementID);
}

EditorProxy.selectProcessElement = function(modelID, elementType){
	BFSelectProcessElement(modelID, elementType);
}

EditorProxy.setProcessLandscapeFlag = function(modelID, flag){
	return BFSetProcessLandscapeFlag(modelID, flag);
}

EditorProxy.isProcessLandscape = function(modelID){
	return BFIsProcessLandscape(modelID);
}

EditorProxy.isProcessApproved = function(modelID){
	return BFIsProcessApproved(modelID);
}

EditorProxy.addKnowledge = function(modelID){
	BFAddKnowledge(modelID);
}

