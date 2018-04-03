(function() {
	
	var app = angular.module('ProfileApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.pagination','ui.grid.treeView','toggle-switch','ui.grid.selection', ['js/profile/main.css']]);

	app.controller('ProfileController', [ '$scope', '$http', 'uiGridConstants', 'uiGridTreeViewConstants', '$uibModal', 'ProfileService', function($scope, $http, uiGridConstants, uiGridTreeViewConstants, $modal, service) {

		var $ctrl = this;
		
		
	}]);
	
	
	app.factory('ProfileService', ['$q', '$filter', '$timeout', '$http', function ($q, $filter, $timeout, $http) {
		
		var get = function(callbackFun) {
			var url = '/profile';
			$http.get(url)
				.success(function (response) {
					callbackFun(response);
				});
		};
		
		var update = function(data, closeFun) {
			$http.put('/profile/' + data.id, angular.toJson(data))
    		.then(
    			function successCallback(response) {
					toastr["success"]("更新成功");
					items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
					closeFun();
				}
    		);
		};
		
		return {
			get : get,
			update : update
		}
	}]);
	
})();
