'use strict';
var app = angular.module('angularApp');
app.factory("sampleService", function(){
	// Service keeps data search results and current sample in memory to pass between controllers //
	return {
		sampleData: {data: null },
		sampleId: {data: null },
		type: {data: null },
		charId: {data: null },
		charClassName: {data: null },
		isEdit: {data: null },
		scratchPad: {data: null },
		pocData: {data: null},
		simpleDialog: function() {}
	}

});