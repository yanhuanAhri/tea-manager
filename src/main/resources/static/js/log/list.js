(function() {
	var app = angular.module('SysLogApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination','toggle-switch', 'ui.grid.autoResize', ['js/log/main.css']]);
	
	app.directive('datePicker', function () {
		 return {
		        require: 'ngModel',
		        link: function (scope, element, attr, ngModel) {
		            $(element).datetimepicker({
		            	 format: "yyyy-mm-dd hh:ii:ss",
		                 autoclose: true,
		                 todayBtn: true,
		                 clearBtn:true,
		                 startDate: "1899-12-31 00:00:00",
		                 minuteStep: 10
		            });
		        }
		 }
	});
	
	app.controller('SysLogController', [ '$scope', '$http', 'uiGridConstants', '$uibModal', 'SysLogService', function($scope, $http, uiGridConstants, $modal, service) {

		var $ctrl = this;
		var pageSize = 25;
		var sort = null;
		var filter = null;
		
		$ctrl.animationsEnabled = true;
		
		$scope.tableHeight = 'height: 800px';
		$scope.showOrHide = '隐藏过滤';
		
		$scope.switchStatus = true;
		
		var btnCols = '<div><a href="#/log/delete" ng-click="grid.appScope.openConfirmWin(row.entity.id)" data-toggle="tooltip" data-placement="left" title="删除" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-remove"></i></a></div>';
		
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
				{ name:'user_id', field:'userId', displayName:'用户ID', enableSorting: false, visible: false, enableFiltering: false },
				{ name:'user_name', field:'userName', displayName:'用户', enableSorting: true },
				{ name:'cr_time', field:'crTime', displayName:'创建时间', cellFilter: 'date', enableSorting: true,maxWidth: 300,enableFiltering: true,enableCellEdit: false,
					filterHeaderTemplate:'<div class="ui-grid-filter-container">FR : <input style="display:inline; width:60%; margin:0 0 5px 0;"  class="ui-grid-filter-input date-filter"  date-picker type="text" ng-model="col.filters[0].term"/><br />TO : <input style="display:inline; width:60%" class="ui-grid-filter-input" date-picker type="text" ng-model="col.filters[1].term"/></div>',
					filters:[
						 { condition: function(term, value, row, column){
						        return true;
							 
						 }}, {condition:function(term, value, row, column){
						        return true;
						 }}]
				},
				{ name:'module', field:'module', displayName:'模块', enableSorting: true },
				{ name:'operate', field:'operate', displayName:'动作', enableSorting: true },
				{ name:'content', field:'content', displayName:'请求路径', enableSorting: true }
				/*{ name:'action', displayName: '操作', cellClass:'controller-btn-a',enableSorting: false, alignment:'right', maxWidth: 100, minWidth: 50, cellTemplate: btnCols, enableFiltering: false}*/
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
				$scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
					if(typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
						sort = sortColumns[0].name + ' ' + sortColumns[0].sort.direction;
						
						var page = $scope.gridOptions.paginationCurrentPage;
						var pageSize = grid.options.paginationPageSize;
						
						var nameFilter = grid.columns[2].filters[0].term;
						var startCrTimeFilter = grid.columns[3].filters[0].term;
						var endCrTimeFilter = grid.columns[3].filters[1].term;
						var moduleFilter = grid.columns[4].filters[0].term;
						var operateFilter = grid.columns[5].filters[0].term;
						var contentFilter = grid.columns[6].filters[0].term;
						filter = getFilter(nameFilter, startCrTimeFilter, endCrTimeFilter, moduleFilter, operateFilter, contentFilter);
						
						service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
						grid.refresh(true);
					}
				});
			    
				gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
					var grid = this.grid;
			    	paginationOptions.pageNumber = newPage;
			        paginationOptions.pageSize = pageSize;
			        
			        var nameFilter = grid.columns[2].filters[0].term;
					var startCrTimeFilter = grid.columns[3].filters[0].term;
					var endCrTimeFilter = grid.columns[3].filters[1].term;
					var moduleFilter = grid.columns[4].filters[0].term;
					var operateFilter = grid.columns[5].filters[0].term;
					var contentFilter = grid.columns[6].filters[0].term;
					filter = getFilter(nameFilter, startCrTimeFilter, endCrTimeFilter, moduleFilter, operateFilter, contentFilter);
			        
			        service.getPage(newPage, pageSize, sort, filter, getPageCallbackFun);
				});
				
				$scope.gridApi.core.on.filterChanged( $scope, function() {
					var grid = this.grid;
					var page = $scope.gridOptions.paginationCurrentPage;
					var pageSize = grid.options.paginationPageSize;

					var nameFilter = grid.columns[2].filters[0].term;
					var startCrTimeFilter = grid.columns[3].filters[0].term;
					var endCrTimeFilter = grid.columns[3].filters[1].term;
					var moduleFilter = grid.columns[4].filters[0].term;
					var operateFilter = grid.columns[5].filters[0].term;
					var contentFilter = grid.columns[6].filters[0].term;
					filter = getFilter(nameFilter, startCrTimeFilter, endCrTimeFilter, moduleFilter, operateFilter, contentFilter);

					service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
				});
			}
		};
		
		var getFilter = function(nameFilter, startCrTimeFilter, endCrTimeFilter, moduleFilter, operateFilter, contentFilter) {
			var filter = null;
			var filterObj = {};
			if(nameFilter != null && nameFilter != '') {
				filterObj.userName = nameFilter;
			}
			if(startCrTimeFilter != null && startCrTimeFilter != '') {
				filterObj.startCrTime = startCrTimeFilter;
			} 
			if(endCrTimeFilter != null && endCrTimeFilter != '') {
				filterObj.endCrTime = endCrTimeFilter;
			}
			if(moduleFilter != null && moduleFilter != '') {
				filterObj.module = moduleFilter;
			}
			if(operateFilter != null && operateFilter != '') {
				filterObj.operate = operateFilter;
			}
			if(contentFilter != null && contentFilter != '') {
				filterObj.content = contentFilter;
			}
			
			if(typeof(filterObj['userName']) == 'undefined' && typeof(filterObj['startCrTime']) == 'undefined' && 
					typeof(filterObj['endCrTime']) == 'undefined' && typeof(filterObj['module']) == 'undefined' &&
					typeof(filterObj['operate']) == 'undefined' && typeof(filterObj['content']) == 'undefined'){
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
		
		$scope.openConfirmWin = function(id) {
			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{'id':id}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];
			
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'log/confirm.html',
				controller: 'DeleteSysLogController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.toggleFiltering = function(){
			$scope.gridOptions.enableFiltering = !$scope.gridOptions.enableFiltering;
			$scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
			
			$scope.showOrHide = $scope.gridOptions.enableFiltering ? '隐藏过滤' : '显示过滤';
		};
	}]);


	
	app.controller('DeleteSysLogController', ['$scope', '$uibModalInstance', 'SysLogService', 'items', function ($scope, $uibModalInstance, service, items) {
		var $ctrl = this;

		$scope.title = '确定删除此日志吗?';
		$scope.buttonName = '删除';
		  
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.deleteSysLog = function() {
			service.deleteLog(items[0].id, items, function(){$uibModalInstance.close();});
		}
	}]);
	
	app.factory('SysLogService', ['$q', '$filter', '$timeout', '$http', function ($q, $filter, $timeout, $http) {
		
		var getPage = function(page, pageSize, sort, filter, callbackFun) {
			var url = '/log/pages/' + (page -1) + '?pageSize=' + pageSize;
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
		    
		 var deleteLog = function(id, items, closeFun) {
			 $http.delete('/log/' + id, null)
	    		.then(
	    			function successCallback(response) {
						toastr["success"]("删除成功");
						items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
						closeFun();
					},function errorCallback(response) {
						var msg = response.data.msg;
				    	toastr["error"](msg);
					}
	    		);
		 };
		
		return {
			getPage : getPage,
			deleteLog : deleteLog
		}
	}]);
	
})();
