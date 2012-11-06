// Add all BFs, which are required for starting the modelEditor to the neededBFs-array
// When trying to start the modelEditor, it first waits for all the functions listed in this array to be injected into the iFrame. Then the actual mxGrapg start is performed
/**This file is part of ProWim.

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


var neededBrowserFunctions = 
	[
	 	"getQXModelEditor",
	 	"BFCreateObject", 
	 	"BFDeleteObject", 
	 	"BFGetName",
	 	"BFRename",
	 	"BFGetDescription",
	 	"BFApproveProcess", 
	 	"BFDisapproveProcess", 
	 	"BFGetModels",
	 	"BFSaveProcessModelAsXML", 
	 	"BFGetPossibleRelations",
	 	"BFSetRelationValue",
	 	"BFRemoveRelationValue",
	 	"BFConnectActivityRole",
	 	"BFSetProduct",
	 	"BFGetPossibleCombinationRules",
	 	"BFConnectActivityMean",
	 	"BFIsGlobal",
	 	"BFGetCombinationRule",
	 	"BFBendControlFlow",
	 	"BFCallPickElementDialog",
	 	"BFGetProcessInfoOfObj",
	 	"BFGetAllExistSubProcesses",
	 	"BFShowProcess",
	 	"BFEditProcess",
	 	"BFSetSubProcessFlagForProcess",
	 	"BFDeleteElementFromModel",
	 	"BFGetElementsOfProcess",
	 	"BFLog",
	 	"BFSaveProcessAsNewVersion",
	 	"BFGetKnowledgeObjects",
	 	"BFGetActivtiyRoles",
	 	"BFGetActivityMeans",
	 	"BFAddProcessElementToActivity",
	 	"BFShowActivityRelationsDialog",
	 	"BFShowModelURL",
	 	"BFSetProcessLandscapeFlag",
	 	"BFIsProcessLandscape",
	 	"BFIsProcessApproved",
	 	"BFAddKnowledge",
	 	"BFShowErrorDialog"
	 ];