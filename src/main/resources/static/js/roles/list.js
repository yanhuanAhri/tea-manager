(function() {
	
	var app = angular.module('rolesApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.pagination','ui.grid.treeView','toggle-switch','ui.grid.selection', 'ui.grid.autoResize', ['js/roles/main.css']]);

	app.controller('RolesController', [ '$scope', '$http', 'uiGridConstants', 'uiGridTreeViewConstants', '$uibModal', 'RolesService', function($scope, $http, uiGridConstants, uiGridTreeViewConstants, $modal, service) {

		var $ctrl = this;
		var pageSize = 25;
		var sort = null;
		var filter = null;
		
		$ctrl.animationsEnabled = true;
		
		$scope.tableHeight = 'height: 800px';
		$scope.showOrHide = '隐藏过滤';
		
		var btnCols = '<div class="line-height-30 line-center"><a href="#/role/edit" ng-click="grid.appScope.openEditWin(row.entity.id)" data-toggle="tooltip" data-placement="left" title="编辑" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-edit"></i></a>' +
	    '<a href="#/roles/permission" ng-click="grid.appScope.openPermissionWin(row.entity.id)" data-toggle="tooltip" data-placement="left" title="授权" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-sitemap"></i></a>' +
		'<a href="#/roles/delete" ng-click="grid.appScope.openConfirmWin(row.entity.id, row.entity.name)" data-toggle="tooltip" data-placement="left" title="删除" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-remove"></i></a>' +
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
			noUnselect: true,
			columnDefs: [
				{ name:'id', field:'id', displayName:'ID', enableSorting: false, visible: false, enableFiltering: false },
				{ name:'name', field:'name', displayName:'名称', enableSorting: true },
				{ name:'code', field:'code', displayName:'编码', enableSorting: true },
				{ name:'description', field:'description', displayName:'描述', enableSorting: true },
				{ name:'action', displayName: '操作',cellClass:'controller-btn-a', enableSorting: false, alignment:'right', maxWidth: 150, minWidth: 50, cellTemplate: btnCols, enableFiltering: false}
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
//				$scope.gridApi.core.on.sortChanged( $scope, $scope.sortChanged );
//			    $scope.sortChanged($scope.gridApi.grid, [ $scope.gridOptions.columnDefs[1] ] );
				$scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
					if(typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
						sort = sortColumns[0].field + ' ' + sortColumns[0].sort.direction;
						
						var page = $scope.gridOptions.paginationCurrentPage;
						var pageSize = grid.options.paginationPageSize;
						
						var nameFilter = grid.columns[1].filters[0].term;
						var desFilter = grid.columns[2].filters[0].term;
						filter = getFilter(nameFilter, desFilter);
						
						service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
					}
				});
			    
				gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
					var grid = this.grid;
			    	paginationOptions.pageNumber = newPage;
			        paginationOptions.pageSize = pageSize;
			        
			        var nameFilter = grid.columns[1].filters[0].term;
					var desFilter = grid.columns[2].filters[0].term;
					filter = getFilter(nameFilter, desFilter);
			        
			        service.getPage(newPage, pageSize, sort, filter, getPageCallbackFun);
				});
				
				$scope.gridApi.core.on.filterChanged( $scope, function() {
					var grid = this.grid;
					var nameFilter = grid.columns[1].filters[0].term;
					var desFilter = grid.columns[2].filters[0].term;
					
					filter = getFilter(nameFilter, desFilter);
					
					var page = $scope.gridOptions.paginationCurrentPage;
					var pageSize = grid.options.paginationPageSize;
					service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
				});
			}
		};
		
		var getFilter = function(nameFilter, desFilter) {
			var filter = null;
			var filterObj = {};
			if(nameFilter != null && nameFilter != '') {
				filterObj.name = nameFilter;
			} 
			if(desFilter != null && desFilter != '') {
				filterObj.description = desFilter;
			}
			
			if(typeof(filterObj['name']) == 'undefined' && typeof(filterObj['description']) == 'undefined'){
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
		
		$scope.openPermissionWin = function(id){
            //删除黑色泡泡提示
            utils.deletTip();

			$ctrl.items = [{'id':id}];
			$modal.open({
				animation: $ctrl.animationsEnabled,
				templateUrl: 'roles/permission.html',
				controller: 'RolesPermissionController',
				controllerAs: '$ctrl',
				size:'1000',
				resolve: {
					items: function() {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.openAddWin = function() {
            //删除黑色泡泡提示
            utils.deletTip();

			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'roles/add.html',
				controller: 'AddRoleController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function() {
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
				templateUrl: 'roles/edit.html',
				controller: 'EditRoleController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function() {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.openConfirmWin = function(id, name) {
            //删除黑色泡泡提示
            utils.deletTip();

			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{'id':id, 'name': name}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];
			
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'roles/confirm.html',
				controller: 'DeleteRoleController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function() {
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
	
	app.filter('showTypeFilter', function(){
		return function(value, scope) {
			if(value == 1) {
				return '链接';
			}
			
			return '按钮';
		}
	});

	app.controller('RolesPermissionController', ['$scope', '$http', '$interval', 'uiGridConstants','uiGridTreeViewConstants', '$uibModalInstance','RolesService', 'items', function($scope, $http, $interval, uiGridConstants,uiGridTreeViewConstants,$uibModalInstance, service, items) {
		var $ctrl = this;
		var filter = null;
		var selParentAndChild = false;
		var rowsId = [];
        $scope.loading = true;

		$scope.gridOptions = {
				enableRowSelection: true,
			    enableSelectAll: true,
			    selectionRowHeaderWidth: 35,
			    showGridFooter:true,
			    isRowSelectable: function(row){
	                   if(row.entity.check){
	                       row.grid.api.selection.selectRow(row.entity); // 选中行
	             }
			    },
			    rowHeight: 35,
				useExternalSorting: true,
				enableGridMenu: true,
				enableSorting: true,
				enableFiltering: true,
				showTreeExpandNoChildren: true,
				columnDefs: [
								{ name: 'id', field:'id', displayName:'ID', enableSorting: false, visible: false, enableFiltering: false },
								{ name: 'parent_id', field:'parentId', displayName:'父ID', enableSorting: false, visible: false, enableFiltering: false },
								{ name: 'name', field:'name', displayName:'名称', enableFiltering: false, enableSorting: false },
								{ name: 'type', field:'type', displayName:'类型', cellFilter:'showTypeFilter:this', enableFiltering: false, enableSorting: false },
								{ name: 'url', field:'url', displayName:'URL', enableFiltering: false, enableSorting: false, },
							],
				onRegisterApi: function( gridApi ) {
					$scope.gridApi = gridApi;
				}
				}
		$scope.gridOptions.multiSelect = true;

		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};

		$scope.gridOptions.onRegisterApi = function(gridApi){
			$scope.gridApi = gridApi;

		    //全选
			$scope.gridApi.selection.on.rowSelectionChangedBatch($scope, function(row, event) {
				angular.forEach($scope.gridApi.grid.rows, function(row, index) {
					if (row.isSelected) {
						rowsId.push(row.entity.id);
					} else {
						rowsId = [];
					}
				});
			});

		      //单选
		      gridApi.selection.on.rowSelectionChanged($scope,function(row){

		    	var date = $scope.gridOptions.data;
		    	if(row.treeLevel !=0){
		    		if(row.isSelected && selParentAndChild){
			    		var entity = row.treeNode.parentRow.entity;
			    		row.grid.api.selection.selectRow(entity);

			    		var childs = row.treeNode.children;
			    		angular.forEach(childs, function(obj, i) {
			    			row.grid.api.selection.selectRow(obj.row.entity);
			    		});
		    		}
		    	}
		    	if(row.isSelected) {
		    		rowsId.push(row.entity.id);
		    	} else {
		    		var index = null;
		    		angular.forEach(rowsId, function(obj, i) {
		    			if(obj == row.entity.id) {
		    				rowsId.splice(i, 1);
		    				return false;
		    			}
		    		});

		    		/*var childs = row.treeNode.children;
		    		angular.forEach(childs, function(obj, i) {
		    			row.grid.api.selection.unSelectRow(obj.row.entity);
		    		});*/
		    	}
		      });
		    };

		var callbackFn = function(data) {
			setTreeLevel(data);
			$scope.gridOptions.data = data;
			$interval(function() {
				$scope.gridApi.treeBase.expandAllRows();

				$interval(function() {
					selParentAndChild = true;
					$scope.gridOptions.showTreeExpandNoChildren = !$scope.gridOptions.showTreeExpandNoChildren;
				    $scope.gridApi.grid.refresh();
				}, 500, 1);
			}, 1000, 1);
		};

		var setTreeLevel = function(data) {
			var len = data.length;
			for(var i = 0; i < len ; i ++) {
				data[i].$$treeLevel = data[i].level;
			}
		};

		$scope.addRolesPermission = function() {
			if ($scope.loading) {
                if (rowsId.length == 0) {
                    toastr["warning"]('权限全部去掉会导致系统无法访问，不能操作');
                } else {
                    //禁止按钮
                    $scope.myForm.$invalid = true;
                    $scope.loading = false;

                    service.addPermission(items[0].id, rowsId, $scope.cancel, function(invalidStatus, loadingStatus) {
                        $scope.myForm.$invalid = invalidStatus;
                        $scope.loading = loadingStatus;
                    });
                }
			}
		}


		service.getPermission(items[0].id,null, callbackFn);
	}]);

	app.controller('AddRoleController', [ '$scope', '$http', 'uiGridConstants', '$uibModalInstance', 'RolesService', 'items', function($scope, $http, uiGridConstants, $uibModalInstance, service, items) {
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.addRole = function() {
			var data = {'name' : $scope.name, 'code':$scope.code, 'description' : $scope.description};
			service.addRole(data, items, function() {$uibModalInstance.close();});
		}
	}]);
	
	app.controller('EditRoleController', ['$scope', '$uibModalInstance', 'RolesService', 'items', function ($scope, $uibModalInstance, service, items) {
		var $ctrl = this;
		service.getRole(items[0].id, function(data) {
			$scope.name = data.name;
			$scope.code = data.code;
			$scope.description = data.description;
		});
		  
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.updateRole = function() {
			var data = {'id':items[0].id, 'name' : $scope.name, 'code':$scope.code, 'description' : $scope.description};
			service.updateRole(data, items, function(){$uibModalInstance.close();});
		}
	}]);
	
	app.controller('DeleteRoleController', ['$scope', '$uibModalInstance', 'RolesService', 'items', function ($scope, $uibModalInstance, service, items) {
		var $ctrl = this;
		
		$scope.name = items[0].name;
		$scope.title = '确定删除角色吗?';
		$scope.buttonName = '删除';
		  
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.deleteRole = function() {
			var data = {'id':items[0].id, 'name' : $scope.name, 'description' : $scope.description};
			service.deleteRole(items[0], items, function(){$uibModalInstance.close();});
		}
	}]);
	
		
	app.factory('RolesService', ['$q', '$filter', '$timeout', '$http', function ($q, $filter, $timeout, $http) {
		
		var getPage = function(page, pageSize, sort, filter, callbackFun) {
			var url = '/roles/pages/' + (page -1) + '?pageSize=' + pageSize;
			url = url + '&filter=' + (filter ? angular.toJson(filter) : '');
			url = url + '&sort=' +  (sort ? sort : '');
				 
			$http.get(url)
				.success(function (response) {
					callbackFun(response);
				});
		};
		
		var getRole = function(id, callbackFun) {
			$http.get('/roles/' + id, null)
			.then(
	    		function successCallback(response) {
	    			callbackFun(response.data);
				}
	    	);
		};
		
		var addRole = function(data, items, closeFun) {
			$http.post('/roles', angular.toJson(data))
    		.then(
    			function successCallback(response) {
					toastr["success"]("创建角色成功");
					items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
					closeFun();
				}
    		);
		}
		
		var updateRole = function(data, items, closeFun) {
			$http.put('/roles/' + data.id, angular.toJson(data))
    		.then(
    			function successCallback(response) {
					toastr["success"]("更新角色成功");
					items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
					closeFun();
				}
    		);
		};
		
		var deleteRole = function(data, items, closeFun) {
			$http.delete('/roles/' + data.id, angular.toJson(data))
    		.then(
    			function successCallback(response) {
					toastr["success"]("角色"+items[0].name+"删除成功");
					items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
					closeFun();
				}
    		);
		};
		
		
		var getPermission = function(roleId,filter, callbackFn) {
			var url = '/permission/roles/'+roleId+'?filter=' + (filter ? angular.toJson(filter) : '');
				 
			$http.get(url)
				.success(function (response) {
					if(!callbackFn) {
						return;
					}
					callbackFn(response.data);
				});
		};
		
		var addPermission = function(roleId, data, cancel, statusCallFuns){
			$http.post('/permission/roles/'+ roleId, data)
			.success(function(response) {
				toastr["success"]("保存成功");

                //禁止按钮状态回调
                statusCallFuns(false, true);
			})
			.error(function(response, status, headers, congfig) {
                //禁止按钮状态回调
                statusCallFuns(false, true);
            });
		};
		
		return {
			getPage : getPage,
			getRole : getRole,
			addRole : addRole,
			updateRole : updateRole,
			deleteRole : deleteRole,
			getPermission : getPermission,
			addPermission : addPermission
		}
	}]);
	
})();
