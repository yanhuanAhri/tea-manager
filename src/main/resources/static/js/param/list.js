(function() {
	var app = angular.module('paramApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination','toggle-switch', 'ui.grid.autoResize', ['js/param/main.css']]);
	
	app.controller('ParamController', [ '$scope', '$http', 'uiGridConstants', '$uibModal', 'ParamService', function($scope, $http, uiGridConstants, $modal, service) {

		var $ctrl = this;
		var pageSize = 25;
		var sort = null;
		var filter = null;
		
		$ctrl.animationsEnabled = true;
		
		$scope.tableHeight = 'height: 800px';
		$scope.showOrHide = '隐藏过滤';
		
		$scope.switchStatus = true;
		
		var btnCols = '<div class="line-height-30 line-center"><a href="#/param/edit" ng-click="grid.appScope.openEditWin(row.entity.id)" data-toggle="tooltip" data-trigger="hover" data-placement="left" title="编辑" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-edit"></i></a>' +
		    '<a href="#/param/delete" ng-click="grid.appScope.openConfirmWin(row.entity.id, row.entity.paramName)" data-toggle="tooltip" data-trigger="hover" data-placement="left" title="删除" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-remove"></i></a>' +
			'</div>';
		
		var paginationOptions = {
			useExternalSorting: true,
			pageNumber: 1,
		    pageSize: 25,
		    sort: null
		};
		
		$scope.gridOptions = {
		    paginationPageSizes: [5, 10, 25, 50, 75, 100,500],
			paginationPageSize: 25,
			useExternalPagination: true,
			useExternalSorting: true,
			enableGridMenu: true,
			enableFiltering: true,
			useExternalFiltering: true,
			enableRowSelection: true, 
			enableRowHeaderSelection: false,
			multiSelect : false,
			modifierKeysToMultiSelect: false,
			noUnselect : true,
			enableColumnResizing: true,
			columnDefs: [
				{ name:'id', field:'id', displayName:'ID', enableSorting: false, visible: false, enableFiltering: false },
				{ name:'param_name', field:'paramName', displayName:'参数名', enableSorting: true },
				{ name:'param_value', field:'paramValue', displayName:'参数值', enableSorting: true },
				{ name:'description', field:'description', displayName:'描述', enableSorting: true },
				{ name:'action', displayName: '操作',cellClass:'controller-btn-a', enableSorting: false, width: 100, cellTemplate: btnCols, enableFiltering: false, cellClass:"controller-btn-a"}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				$scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
					if(typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
						sort = sortColumns[0].name + ' ' + sortColumns[0].sort.direction;
						
						var page = $scope.gridOptions.paginationCurrentPage;
						var pageSize = grid.options.paginationPageSize;
						
						var nameFilter = grid.columns[1].filters[0].term;
						var valueFilter = grid.columns[2].filters[0].term;
						var descFilter = grid.columns[3].filters[0].term;
						filter = getFilter(nameFilter, valueFilter, descFilter);
						
						service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
						grid.refresh(true);
					}
				});
			    
				gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
					var grid = this.grid;
			    	paginationOptions.pageNumber = newPage;
			        paginationOptions.pageSize = pageSize;
			        
			        var nameFilter = grid.columns[1].filters[0].term;
					var valueFilter = grid.columns[2].filters[0].term;
					var descFilter = grid.columns[3].filters[0].term;
					filter = getFilter(nameFilter, valueFilter, descFilter);
			        
			        service.getPage(newPage, pageSize, sort, filter, getPageCallbackFun);
				});
				
				$scope.gridApi.core.on.filterChanged( $scope, function() {
					var grid = this.grid;
					
					var nameFilter = grid.columns[1].filters[0].term;
					var valueFilter = grid.columns[2].filters[0].term;
					var descFilter = grid.columns[3].filters[0].term;
					filter = getFilter(nameFilter, valueFilter, descFilter);
					
					var page = $scope.gridOptions.paginationCurrentPage;
					var pageSize = grid.options.paginationPageSize;
					service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
				});
			}
		};
		
		var getFilter = function(nameFilter, valueFilter, descFilter) {
			var filter = null;
			var filterObj = {};

			if(nameFilter != null && nameFilter != '') {
				filterObj.paramName = nameFilter;
			}
			if(valueFilter != null && valueFilter != '') {
				filterObj.paramValue = valueFilter;
			}
			if(descFilter != null && descFilter != '') {
				filterObj.description = descFilter;
			}

			if(typeof(filterObj['paramName']) == 'undefined' && typeof(filterObj['paramValue']) == 'undefined' &&
					typeof(filterObj['description']) == 'undefined'){
				filter = null;
			} else {
				filter = filterObj;
			}

			return filter;
		}
		
		var getPageCallbackFun = function(response) {
			$scope.gridOptions.totalItems = response.total;
			$scope.gridOptions.data = response.data;
		};
		
		service.getPage(1, pageSize, null, null, getPageCallbackFun);
		
		$scope.openAddWin = function() {
			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'param/edit.html',
				controller: 'AddParamController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.openEditWin = function(id) {
            //删除黑色泡泡提示
            utils.deletTip();

			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{'id':id}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'param/edit.html',
				controller: 'EditParamController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.openConfirmWin = function(id, paramName) {
            //删除黑色泡泡提示
            utils.deletTip();

			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{'id':id, 'paramName': paramName}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];
			
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'param/confirm.html',
				controller: 'DeleteParamController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function() {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.snydata = function() {
			service.snydata();
		};
		
		$scope.toggleFiltering = function(){
			$scope.gridOptions.enableFiltering = !$scope.gridOptions.enableFiltering;
			$scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
			
			$scope.showOrHide = $scope.gridOptions.enableFiltering ? '隐藏过滤' : '显示过滤';
		};
	}]);
	
	app.controller('AddParamController', [ '$scope', '$http', 'uiGridConstants', '$uibModalInstance', 'ParamService', 'items', function($scope, $http, uiGridConstants, $uibModalInstance, service, items) {
		var $ctrl = this;
		$scope.winTitle = "创建参数";
		
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.updateParam = function() {
			
			var data = {'paramName':$scope.paramName, 'paramValue':$scope.paramValue, 'description':$scope.description};
			service.addParam(data, items, function() {$uibModalInstance.close();});
		}
	}]);
	
	app.controller('EditParamController', ['$scope', '$uibModalInstance', 'ParamService', 'items', function ($scope, $uibModalInstance, service, items) {
		var $ctrl = this;
		$scope.winTitle = "编辑参数"
		
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		service.getParam(items[0].id, function(data) {
			$scope.paramName = data.paramName;
			$scope.paramValue = data.paramValue;
			$scope.description = data.description;
		});
		
		$scope.updateParam = function() {
			var data = {'id': items[0].id, 'paramName':$scope.paramName, 'paramValue':$scope.paramValue, 'description':$scope.description};
			service.update(data, items, function(){$uibModalInstance.close();});
		}
	}]);
	
	app.controller('DeleteParamController', ['$scope', '$uibModalInstance', 'ParamService', 'items', function ($scope, $uibModalInstance, service, items) {
		var $ctrl = this;
		
		$scope.paramName = items[0].paramName;
		$scope.title = '确定删除参数吗?';
		$scope.buttonName = '删除';
		  
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.deleteParam = function() {
			service.deleteParam(items[0], items, function(){$uibModalInstance.close();});
		}
	}]);
	
	app.factory('ParamService', ['$q', '$filter', '$timeout', '$http', function ($q, $filter, $timeout, $http) {
		
		var getPage = function(page, pageSize, sort, filter, callbackFun) {
			var url = '/param/pages/' + (page -1) + '?pageSize=' + pageSize;
			url = url + '&filter=' + (filter ? angular.toJson(filter) : '');
			url = url + '&sort=' +  (sort ? sort : '');
				 
			$http.get(url)
				.success(function (response) {
					if(!callbackFun) {
						return;
					}
					callbackFun(response);
				});
		};
		
		var getParam = function(id, callbakcFun) {
	    	$http.get('/param/' + id, null)
	    	.then(
	    			function successCallback(response) {
	    				callbakcFun(response.data);
					}
	    		);
	    }
		
		var addParam = function(data, items, closeFun) {
			$http.post('/param', angular.toJson(data))
    		.then(
    			function successCallback(response) {
					toastr["success"]("创建参数成功");
					items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
					closeFun();
				}
    		);
		}
		
		var update = function(data, items, closeFun) {
		    	$http.put('/param/' + data.id, angular.toJson(data))
		    		.then(
		    			function successCallback(response) {
							toastr["success"]("更新参数成功");
							items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
							closeFun();
						}
		    		);
		 };
		    
		 var deleteParam = function(data, items, closeFun) {
			 $http.delete('/param/' + data.id, null)
	    		.then(
	    			function successCallback(response) {
						toastr["success"]("参数"+data.name+"删除成功");
						items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
						closeFun();
					},function errorCallback(response) {
						var msg = response.data.msg;
				    	toastr["error"](msg);
					}
	    		);
		 };
		 
		 var snydata = function() {
			$http.get('/param/synch')
			.then(
	    			function successCallback(response) {
						toastr["success"]("数据同步成功");
					},function errorCallback(response) {
						var msg = response.data.msg;
				    	toastr["error"](msg);
					}
	    		);
		 };
		
		return {
			getPage : getPage,
			getParam : getParam,
			addParam : addParam,
			update : update,
			deleteParam : deleteParam,
			snydata : snydata
		}
	}]);
	
})();
