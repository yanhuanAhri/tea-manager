(function() {
	var app = angular.module('PermissionApp', ['ngAnimate', 'ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.treeView', 'ui.grid.autoResize', 'toggle-switch',['js/permission/main.css']]);

	app.controller('PermissionController', [ '$scope', '$http', 'uiGridConstants', 'uiGridTreeViewConstants', '$uibModal', '$interval', 'PermissionService', function($scope, $http, uiGridConstants, uiGridTreeViewConstants, $modal, $interval, service) {
		var $ctrl = this;
		var filter = null;
		$scope.showOrHide = '隐藏过滤';
		$ctrl.animationsEnabled = true;
		
		var expandedRowIds = [];
		
		var btnCols = '<div class="line-height-30 line-center">'
				+'<a href="#/permission/addChildMenu" ng-click="grid.appScope.openAddChildWin(row.entity.id)" data-toggle="tooltip"  data-trigger="hover" data-placement="left" title="添加子菜单或者按钮" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-plus"></i></a>' 
				+'<a href="#/permission/edit" ng-click="grid.appScope.openEditWin(row.entity)" data-toggle="tooltip"  data-trigger="tooltip" data-placement="left" title="编辑" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-edit"></i></a>'
				+'<a href="#/permission/delete" ng-click="grid.appScope.openConfirmWin(row.entity.id, row.entity.name)" data-toggle="tooltip"  data-trigger="hover" data-placement="left" title="删除" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-remove"></i></a>'
				+'</div>';
		
		$scope.gridOptions = {
			useExternalSorting: true,
			enableGridMenu: true,
			enableSorting: true,
			enableFiltering: true,
			useExternalFiltering: true,
			showTreeExpandNoChildren: true,
			enableRowSelection: true, 
			enableRowHeaderSelection: false,
			multiSelect : false,
			modifierKeysToMultiSelect: false,
			noUnselect: true,
			enableColumnResizing: true,
			columnDefs: [
				{ name: 'id', field:'id', displayName:'ID', enableSorting: false, visible: false, enableFiltering: false, width:'5%' },
				{ name: 'parentId', field:'parentId', displayName:'父ID', enableSorting: false, visible: false, enableFiltering: false, width:'5%' },
				{ name: 'name', field:'name', displayName:'名称', enableFiltering: true, enableSorting: false, width:'15%' },
				{ name: 'type', field:'type', displayName:'类型', cellFilter:'showTypeFilter:this', enableFiltering: true, enableSorting: false, filter: {
			          term: '-1',
			          type: uiGridConstants.filter.SELECT,
			          selectOptions: [ {value:'-1', label:'全部'}, { value: '1', label: '链接' }, { value: '2', label: '按钮' }]}
				},
				{ name: 'url', field:'url', displayName:'URL', enableFiltering: true, enableSorting: false, width:'20%'},
				{ name: 'method', field:'method', displayName:'方法', enableFiltering: true, enableSorting: false,filter: {
			          term: '-1',
			          type: uiGridConstants.filter.SELECT,
			          selectOptions: [ {value:'-1', label:'全部'}, { value: 'GET', label: 'GET' }, { value: 'POST', label: 'POST' }, { value: 'PUT', label: 'PUT' }, { value: 'DELETE', label: 'DELETE' }]}
				},
				{ name: 'code', field:'code', displayName:'编码', enableFiltering: true, enableSorting: false , width:'15%' },
				{ name: 'icon', field:'icon', displayName:'图标', enableFiltering: true, enableSorting: false, width:'20%' },
				{ name: 'sortby', field:'sortby', displayName:'排序', enableFiltering: false, enableSorting: false },
				{ name:'action', displayName: '操作',cellClass:'controller-btn-a', enableSorting: false, alignment:'right', width: 100, cellTemplate: btnCols, enableFiltering: false}
			],
			onRegisterApi: function( gridApi ) {
				$scope.gridApi = gridApi;
				
				$scope.gridApi.treeBase.on.rowExpanded($scope, function(row) {
					service.getPage(row.entity.id, filter, addChildcallbackFn);
				});
				
				$scope.gridApi.core.on.filterChanged( $scope, function() {
					var grid = this.grid;
					var nameFilter = grid.columns[3].filters[0].term;
					var typeFilter = grid.columns[4].filters[0].term;
					var urlFilter = grid.columns[5].filters[0].term;
					var methodFilter = grid.columns[6].filters[0].term;
					var codeFilter = grid.columns[7].filters[0].term;
					var iconFilter = grid.columns[8].filters[0].term;
					filter = getFilter(nameFilter, typeFilter, urlFilter, methodFilter, codeFilter, iconFilter);
					
					service.getPage(0, filter, callbackFn);
				});
				
			    gridApi.treeBase.on.rowCollapsed($scope, function(row) {
			    	removeExpandedRowIds(row.entity.id);
			    });
			      
			    gridApi.treeBase.on.rowExpanded($scope,function(row){
			    	addExpandedRowIds(row.entity.id);
			    });
			    
			   /* $scope.gridApi.grid.registerDataChangeCallback(function() {
			    	//$scope.gridApi.treeBase.expandAllRows();
			    	if ($scope.gridApi.grid.treeBase.tree instanceof Array) {
			    		angular.forEach($scope.gridApi.grid.treeBase.tree, function(node) {
			    			if (hasExpandRowId(node.row.entity.id)) {
			    				$scope.gridApi.treeBase.expandRow(node.row);
			    			}
			    		});
			    	}
				});*/
				
			}
		};
		
		var removeExpandedRowIds = function(id) {
			angular.forEach(expandedRowIds, function(obj, i) {
	    		if(id == obj) {
	    			expandedRowIds.splice(i, 1);
	    		} 
	    	});
		}
		
		var addExpandedRowIds = function(id) {
			var hasId = false;
	    	angular.forEach(expandedRowIds, function(obj, i) {
	    		 if(id == obj) {
	    			 hasId = true;
	    			 return false;
	    		 } 
	    	});
	    	if(!hasId) {
	    		expandedRowIds.push(id);
	    	}
		}
		
		var hasExpandRowId = function(id) {
			var hasId = false;
	    	angular.forEach(expandedRowIds, function(obj, i) {
	    		 if(id == obj) {
	    			 hasId = true;
	    			 return false;
	    		 } 
	    	});
	    	
	    	return hasId;
		}
		
		var expandedRow = function() {
			if ($scope.gridApi.grid.treeBase.tree instanceof Array) {
	    		angular.forEach($scope.gridApi.grid.treeBase.tree, function(node) {
	    			if (hasExpandRowId(node.row.entity.id)) {
	    				$scope.gridApi.treeBase.expandRow(node.row);
	    				expandedRowChild(node.children);
	    			}
	    		});
	    	}
		}
		
		var expandedRowChild = function(childs) {
			if(childs == null || childs.length <=0) {
				return;
			}
			
			angular.forEach(childs, function(node) {
				if (hasExpandRowId(node.row.entity.id)) {
					$scope.gridApi.treeBase.expandRow(node.row);
					expandedRowChild(node.children);
	    		}
			});
		}
		
		var getFilter = function(nameFilter, typeFilter, urlFilter, methodFilter, codeFilter, iconFilter) {
			var filter = null;
			var filterObj = {};
			if(nameFilter != null && nameFilter != '') {
				filterObj.name = nameFilter;
			}
			if(typeFilter != null && typeFilter != '') {
				filterObj.type = typeFilter;
			} 
			if(urlFilter != null && urlFilter != '') {
				filterObj.url = urlFilter;
			}
			if(methodFilter != null && methodFilter != '') {
				filterObj.method = methodFilter;
			}
			if(codeFilter != null && codeFilter != '') {
				filterObj.code = codeFilter;
			}
			
			if(iconFilter != null && iconFilter != '') {
				filterObj.icon = iconFilter;
			}
			
			if(typeof(filterObj['name']) == 'undefined' && typeof(filterObj['type']) == 'undefined' && 
					typeof(filterObj['url']) == 'undefined' && typeof(filterObj['method']) == 'undefined' &&
					typeof(filterObj['code']) == 'undefined' && typeof(filterObj['icon']) == 'undefined'){
				filter = null;
			} else {
				filter = filterObj;
			}
			
			return filter;
		}
		
		$scope.openAddFirstMenuWin = function() {
			$ctrl.items = [{'parentId': 0,'title':'添加一级菜单', 'msg':'成功添加一级菜单'}, null, service.getPage, callbackFn];
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'permission/add.html',
				controller: 'AddPermissionController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.openAddChildWin = function(id) {
            //删除黑色泡泡提示
            utils.deletTip();

			$ctrl.items = [{'parentId': id,'title':'添加子菜单或者按钮', 'msg':'添加成功'}, null, service.getPage, callbackFn];
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'permission/add.html',
				controller: 'AddPermissionController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.openEditWin = function(row) {
            //删除黑色泡泡提示
            utils.deletTip();

			$ctrl.items = [row, null, service.getPage, callbackFn];
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'permission/add.html',
				controller: 'EditPermissionController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		$scope.openConfirmWin = function(id, name) {
            //删除黑色泡泡提示
            utils.deletTip();

			$scope.buttonName = '删除';
			$ctrl.items = [{'id':id, 'name': name}, null, service.getPage, callbackFn];
			
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'permission/confirm.html',
				controller: 'DeleteController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};
		
		var callbackFn = function(data) {
			setTreeLevel(data);
			$scope.gridOptions.data = data;
			
			$interval(function() {
				expandedRow();
			}, 300, 1);
		};
		
		var addChildcallbackFn = function(data) {
			if(data == null || data.length <=0) {
				return;
			};
			
			removeSelf(data);
			setTreeLevel(data);
			var index = findIndx(data);
			
			for(var i =0; i < data.length; i ++) {
				$scope.gridOptions.data.splice(index + i + 1, 0, data[i]);
			}
			
			$interval(function() {
				expandedRow();
			}, 300, 1);
			
			//$scope.nodeLoaded = true;
		};
		
		var setTreeLevel = function(data) {
			var len = data.length;
			for(var i = 0; i < len ; i ++) {
				data[i].$$treeLevel = data[i].level;
			}
		};
		
		var findIndx = function(data) {
			var srcData = $scope.gridOptions.data;
			var parentId = data[0].parentId;
			for(var i = 0; i < srcData.length; i ++) {
				if(srcData[i].id == parentId) {
					return i;
				}
			}
			
			return null;
		};
		
		var removeSelf = function(data) {
			
			$.each(data, function(i, dobj) {
				$.each($scope.gridOptions.data, function(index, obj) {
					if(obj && obj.id == dobj.id) {
						$scope.gridOptions.data.splice(index, 1);
					}
				});
			});
		}
		
		service.getPage(0, null, callbackFn);
		
		$scope.toggleFiltering = function(){
			$scope.gridOptions.enableFiltering = !$scope.gridOptions.enableFiltering;
			$scope.gridApi.core.notifyDataChange( uiGridConstants.dataChange.COLUMN );
			
			$scope.showOrHide = $scope.gridOptions.enableFiltering ? '隐藏过滤' : '显示过滤';
		};
        $scope.gridOptions.enableFiltering = false;
	}]);
	
	app.filter('showTypeFilter', function(){
		return function(value, scope) {
			if(value == 1) {
				return '链接';
			}
			
			return '按钮';
		}
	});
	
	app.controller('AddPermissionController', [ '$scope', '$http', 'uiGridConstants', '$uibModalInstance', 'PermissionService', 'items', function($scope, $http, uiGridConstants, $uibModalInstance, service, items) {
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.showCode = false;
		$scope.title=items[0].title;
		$scope.data = {
			model: '1',
			availableOptions: [ {value: 1, name: '链接'}, {value: 2, name: '按钮'}	]
		};
		$scope.method = {
			model: '',
			availableOptions: [ {value: '', name: '请选择'}, {value: 'GET', name: 'GET'}, {value: 'POST', name: 'POST'}, {value: 'PUT', name: 'PUT'}, {value: 'DELETE', name: 'DELETE'}	]
		}
		
		$scope.changeType = function(){
			var type = $scope.data.model;
			$scope.showCode = (type == 2 ? true : false);
		}
		
		$scope.add = function() {
			var type = $scope.data.model;
			var code = $scope.code?$scope.code:'';
			if(type == 1) {
				code = '';
			}
			var data = {'parentId':items[0].parentId, 'name':$scope.name, 'type':type, 'url':$scope.url? $scope.url : '', 'method':$scope.method.model, 'code':code, 'icon': $scope.icon, 'sortby':$scope.sortby};
			service.addPermission(data, items, function() {$uibModalInstance.close();});
		}
	}]);
	
	app.controller('EditPermissionController', [ '$scope', '$http', 'uiGridConstants', '$uibModalInstance', 'PermissionService', 'items', function($scope, $http, uiGridConstants, $uibModalInstance, service, items) {
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.showCode = false;
		$scope.title='编辑';
		
		
		var entity = items[0];
		$scope.name = entity.name;
		$scope.url = entity.url;
		$scope.code = entity.code;
		$scope.icon = entity.icon;
		$scope.sortby = entity.sortby;
		
		$scope.data = {
			model: entity.type.toString(),
			availableOptions: [ {'value': 1, 'name': '链接'}, {'value': 2, 'name': '按钮'}	]
		};
		$scope.method = {
				model: entity.method == null ? '':entity.method,
				availableOptions: [ {value: '', name: '请选择'}, {value: 'GET', name: 'GET'}, {value: 'POST', name: 'POST'}, {value: 'PUT', name: 'PUT'}, {value: 'DELETE', name: 'DELETE'}	]
		}
		
		if(entity.type == 2) {
			$scope.showCode = true;
		}
		
		$scope.changeType = function(){
			var type = $scope.data.model;
			$scope.showCode = (type == 2 ? true : false);
		}
		
		$scope.add = function() {
			var type = $scope.data.model;
			var code = $scope.code?$scope.code:'';
			var data = {'id':items[0].id,'parentId':items[0].parentId, 'name':$scope.name, 'type':type, 'url':$scope.url? $scope.url : '', 'method':$scope.method.model, 'code':code, 'icon':$scope.icon, 'sortby':$scope.sortby};
			service.updatePermission(data, items, function() {$uibModalInstance.close();});
		}
	}]);
	
	app.controller('DeleteController', ['$scope', '$uibModalInstance', 'PermissionService', 'items', function ($scope, $uibModalInstance, service, items) {
		var $ctrl = this;
		
		$scope.name = items[0].name;
		$scope.title = '确定删除此权限吗?';
		$scope.buttonName = '删除';
		  
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.deletePermission = function() {
			var data = {'id':items[0].id, 'name' : $scope.name, 'description' : $scope.description};
			service.deletePermission(items[0], items, function(){$uibModalInstance.close(); $(".tooltip").remove();});
		}
	}]);
	
	app.factory('PermissionService', ['$q', '$filter', '$timeout', '$http', function ($q, $filter, $timeout, $http) {
		
		var getPage = function(parentId, filter, callbackFn) {
			var url = '/permission/pages?parentId='+parentId;
			url = url + '&filter=' + (filter ? angular.toJson(filter) : '');
				 
			$http.get(url)
				.success(function (response) {
					if(!callbackFn) {
						return;
					}
					callbackFn(response.data);
				});
		};
		
		var addPermission = function(data, items, closeFun) {
			$http.post('/permission', angular.toJson(data))
    		.then(
    			function successCallback(response) {
					toastr["success"](items[0].msg);
					items[2](0, null, items[3]);
					closeFun();
				}
    		);
		}
		
		var getPermission = function(id, callbakcFun) {
	    	$http.get('/permission/' + id, null)
	    	.then(
	    			function successCallback(response) {
	    				callbakcFun(response.data);
					}
	    		);
	    }
		
		var updatePermission = function(data, items, closeFun) {
		    	$http.put('/permission/' + data.id, angular.toJson(data))
		    		.then(
		    			function successCallback(response) {
							toastr["success"]("权限更新成功");
							items[2](0, null, items[3]);
							closeFun();
						}
		    		);
		 };
		    
		 var deletePermission = function(data, items, closeFun) {
			 $http.delete('/permission/' + data.id, null)
	    		.then(
	    			function successCallback(response) {
						toastr["success"]("权限"+data.name+"删除成功");
						items[2](0, null, items[3]);
						closeFun();
					}
	    		);
		 };
		
		return {
			getPage : getPage,
			getPermission : getPermission,
			addPermission : addPermission,
			updatePermission : updatePermission,
			deletePermission : deletePermission
		}
	}]);
	
})();
