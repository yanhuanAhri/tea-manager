(function() {
    /*--------------------------------------------------------------
	 | 全局变量
	 |--------------------------------------------------------------
	*/
    var rowSelectionIdArr  = [];
    var departmentGridApi = null;
    var $ctrl = this;

	var app = angular.module('UsersApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination','toggle-switch', 'ui.grid.autoResize', ['js/users/main.css']]);

	app.controller('UsersController', [ '$scope', 'i18nService', '$http', 'uiGridConstants', '$uibModal', 'UsersService', function($scope, i18nService, $http, uiGridConstants, $modal, service) {
        rowSelectionIdArr  = [];
        departmentGridApi = null;
        var mechanismData = []; //表格所属机构下拉搜索数据
        var roleNamesData = []; //表格角色下拉搜索数据
		var pageSize = 25;
		var sort = null;
		var filter = null;


        //表格语言中文
        i18nService.setCurrentLang("zh-cn");

		$ctrl.animationsEnabled = true;

		$scope.tableHeight = 'height: 800px';
		$scope.showOrHide = '隐藏过滤';

		$scope.switchStatus = true;

		var btnCols = '<div class="line-height-30 line-center"><a href="#/users/resetPassword" ng-click="grid.appScope.openResetPwdWin(row.entity.id)" data-toggle="tooltip" data-placement="left" title="重置密码" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-key"></i></a>' +
			'<a href="#/users/edit" ng-click="grid.appScope.openEditWin(row.entity.id)" data-toggle="tooltip" data-placement="left" title="编辑" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-edit"></i></a>' +
            '<a href="#/users/department" ng-click="grid.appScope.openDepartmentWin(row.entity.id)" data-toggle="tooltip" data-placement="left" title="分配数据权限" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-group"></i></a>' +
            '<a href="#/users/delete" ng-click="grid.appScope.openConfirmWin(row.entity.id, row.entity.name)" data-toggle="tooltip" data-placement="left" title="删除" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-remove"></i></a>' +
			'</div>';

		var statusCols = '<div class="line-height-30 line-center">'
			+ '<toggle-switch x-ng-init="switchStatus=(row.entity.status == 1)"  x-ng-value="switchStatus=(row.entity.status == 1)"  x-ng-model="switchStatus" class="switch-mini switch-success" on-label="激活" off-label="禁用"  x-ng-change="grid.appScope.changeStatus(row.entity.id, switchStatus)"></toggle-switch>'
			+ '</div>';

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
                {
                    name: 'id',
                    field: 'id',
                    displayName: 'ID',
                    enableSorting: false,
                    visible: false,
                    enableFiltering: false
                },
                {name: 'login_name', field: 'loginName', displayName: '登录名', enableSorting: true},
                {name: 'name', field: 'name', displayName: '姓名', enableSorting: true},
                {
                	name: 'roleNames',
					field: 'roleNames',
					displayName: '角色',
					enableSorting: false,
                    enableFiltering: true,
                    filter: {
                        term: '',
                        type: uiGridConstants.filter.SELECT,
                        selectOptions: roleNamesData
                    },
                    cellTemplate: function() {
                		return '<div class="line-height-30 line-center">{{row.entity.roleNames | roleNamesFormat}}</div>'
					}

				},
                {name: 'email', field: 'email', displayName: 'E-mail', enableSorting: true},
                {name: 'mobile', field: 'mobile', displayName: '手机号码', enableSorting: true},
                {
                    name: 'create_time',
                    field: 'createTime',
                    displayName: '创建时间',
                    enableSorting: true,
                    enableFiltering: false,
                    cellTemplate: function() {
                        return '<div class="line-height-30 line-center">{{row.entity.createTime | date:\'yyyy-MM-dd HH:mm:ss\'}}</div>'
                    }
                },
                {
                    name: 'deptName',
					field: 'deptName',
					displayName: '所属分公司',
					enableSorting: false,
					enableFiltering: true,
                    filter: {
                        term: '',
                        type: uiGridConstants.filter.SELECT,
                        selectOptions: mechanismData
                    }
                },
                {
                    name: 'status',
					displayName: '状态',
					filter: {
                    term: '',
                    type: uiGridConstants.filter.SELECT,
                    selectOptions: [{value: '', label: '全部'}, {value: '1', label: '激活'}, {value: '0', label: '禁用'}]
                },
                    cellTemplate: statusCols,
                    alignment: 'center', maxWidth: 100, minWidth: 50, enableSorting: true
                },
                {
                    name: 'action',
                    displayName: '操作',
                    cellClass: 'controller-btn-a',
                    enableSorting: false,
                    alignment: 'right',
                    maxWidth: 120,
                    minWidth: 50,
                    cellTemplate: btnCols,
                    enableFiltering: false
                }
			],
			onRegisterApi: function(gridApi) {
				$scope.gridApi = gridApi;
//				$scope.gridApi.core.on.sortChanged( $scope, $scope.sortChanged );
//			    $scope.sortChanged($scope.gridApi.grid, [ $scope.gridOptions.columnDefs[1] ] );
				$scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
					if(typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
						sort = sortColumns[0].name + ' ' + sortColumns[0].sort.direction;

                        var grid = this.grid;
                        var page = $scope.gridOptions.paginationCurrentPage;
                        var pageSize = grid.options.paginationPageSize;

                        var lnameFilter = grid.columns[1].filters[0].term;
                        var nameFilter = grid.columns[2].filters[0].term;
                        var roleNamesFilter = grid.columns[3].filters[0].term;
                        var emailFilter = grid.columns[4].filters[0].term;
                        var mobileFilter = grid.columns[5].filters[0].term;
                        var deptFilter = grid.columns[7].filters[0].term;
                        var statusFilter = grid.columns[8].filters[0].term;

                        filter = getFilter(lnameFilter, nameFilter, emailFilter, mobileFilter, statusFilter, deptFilter, roleNamesFilter);
                        service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
						grid.refresh(true);
					}
				});

				gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
                    var grid = this.grid;
                    var page = $scope.gridOptions.paginationCurrentPage;
                    var pageSize = grid.options.paginationPageSize;

                    var lnameFilter = grid.columns[1].filters[0].term;
                    var nameFilter = grid.columns[2].filters[0].term;
                    var roleNamesFilter = grid.columns[3].filters[0].term;
                    var emailFilter = grid.columns[4].filters[0].term;
                    var mobileFilter = grid.columns[5].filters[0].term;
                    var deptFilter = grid.columns[7].filters[0].term;
                    var statusFilter = grid.columns[8].filters[0].term;

                    filter = getFilter(lnameFilter, nameFilter, emailFilter, mobileFilter, statusFilter, deptFilter, roleNamesFilter);
                    service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
				});

				$scope.gridApi.core.on.filterChanged( $scope, function() {
					var grid = this.grid;
                    var page = $scope.gridOptions.paginationCurrentPage;
                    var pageSize = grid.options.paginationPageSize;

                    var lnameFilter = grid.columns[1].filters[0].term;
                    var nameFilter = grid.columns[2].filters[0].term;
                    var roleNamesFilter = grid.columns[3].filters[0].term;
                    var emailFilter = grid.columns[4].filters[0].term;
                    var mobileFilter = grid.columns[5].filters[0].term;
                    var deptFilter = grid.columns[7].filters[0].term;
                    var statusFilter = grid.columns[8].filters[0].term;

					filter = getFilter(lnameFilter, nameFilter, emailFilter, mobileFilter, statusFilter, deptFilter, roleNamesFilter);
					service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
				});
			}
		};

		var getFilter = function(lnameFilter, nameFilter, emailFilter, mobileFilter, statusFilter, deptFilter, roleNamesFilter) {
			var filter = null;
			var filterObj = {};

			if(lnameFilter != null && lnameFilter != '') {
				filterObj.loginName = lnameFilter;
			}
			if(nameFilter != null && nameFilter != '') {
				filterObj.name = nameFilter;
			}
			if(emailFilter != null && emailFilter != '') {
				filterObj.email = emailFilter;
			}
			if(mobileFilter != null && mobileFilter != '') {
				filterObj.mobile = mobileFilter;
			}
			if(statusFilter != null && statusFilter != '') {
				filterObj.status = statusFilter;
			}

			if(deptFilter != null && deptFilter != ''){
                filterObj.deptId = deptFilter;
			}

			if (roleNamesFilter != null && roleNamesFilter != '') {
                filterObj.roleId = roleNamesFilter;
			}

			if(typeof(filterObj['loginName']) == 'undefined' && typeof(filterObj['name']) == 'undefined' &&
					typeof(filterObj['email']) == 'undefined' && typeof(filterObj['mobile']) == 'undefined' &&
					typeof(filterObj['status']) == 'undefined' && typeof(filterObj['deptId']) == 'undefined' && typeof(filterObj['roleId']) == 'undefined'){
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

        //获取表格所属机构下拉数据
		service.getOrg(function(response) {
            mechanismData.push({
                value: '',
                label: '全部'
            });

            angular.forEach(response, function(obj, index) {
                var data = {
                    value: obj.id,
                    label: obj.name
                }

                mechanismData.push(data);
            });
		});


        //获取表格角色下拉数据
		service.getAllRoles(function(response) {
            roleNamesData.push({
                value: '',
                label: '全部'
            });

            angular.forEach(response, function(obj, index) {
                var data = {
                    value: obj.id,
                    label: obj.name
                }

                roleNamesData.push(data);
            });
		});

        service.getPage(1, pageSize, null, null, getPageCallbackFun);

		$scope.openAddWin = function() {
			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];
			$modal.open({
				animation: $ctrl.animationsEnabled,
				templateUrl: 'users/add.html',
				controller: 'AddUserController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};

		$scope.changeStatus = function(id, status) {
			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;

			var callbackFn = function() {
				service.getPage(page, pageSize, sort, filter, getPageCallbackFun);
			}

			$scope.switchStatus = status;
			service.changeStatus(id, status, callbackFn);
		};

		$scope.openResetPwdWin = function(id) {
            //删除黑色泡泡提示
            utils.deletTip();

			$ctrl.items = [{'userId':id}];
			$modal.open({
				animation: $ctrl.animationsEnabled,
				templateUrl: 'users/resetPassword.html',
				controller: 'resetPasswordController',
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
				templateUrl: 'users/edit.html',
				controller: 'EditUsersController',
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

			var page = $scope.gridOptions.paginationCurrentPage;
			var pageSize = $scope.gridOptions.paginationPageSize;
			$ctrl.items = [{'id':id, 'name': name}, {'page': page, 'pageSize':pageSize, 'sort':sort, 'filter':filter}, service.getPage, getPageCallbackFun];

			$modal.open({
				animation: $ctrl.animationsEnabled,
				templateUrl: 'users/confirm.html',
				controller: 'DeleteUsersController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		};

        $scope.openDepartmentWin = function(id){
            //删除黑色泡泡提示
            utils.deletTip();

            $ctrl.items = [{'id':id}];
            $modal.open({
                animation: $ctrl.animationsEnabled,
                templateUrl: 'users/department.html',
                controller: 'UsersDepartmentController',
                controllerAs: '$ctrl',
                size:'1000',
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

	app.controller('AddUserController', [ '$scope', '$http', 'uiGridConstants', '$uibModalInstance', 'UsersService', '$interval', 'items', function($scope, $http, uiGridConstants, $uibModalInstance, service, $interval, items) {
        $scope.orgData = [];
        $scope.orgId = '';

		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};

		service.getAllRoles(function(data) {
			$scope.roles = data;
			$interval(function(){
				$('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
					checkboxClass: 'icheckbox_minimal-blue',
					radioClass: 'iradio_minimal-blue'
				});

			}, 200, 1);
		});

		//获取所属机构
        service.getOrg(function(response) {
            var data = {
                id: '',
                name: '请选择机构'
            }

            $scope.orgData.push(data);

            angular.forEach(response, function(obj) {
                $scope.orgData.push(obj);
            });
        });

		$scope.addUser = function() {
			var roles = [];
			$.each($(':input[name="brand-suppliers-list1"]:checked'), function(index, obj) {
				roles[index] = obj.value;
			});

			if(roles.length <=0) {
				toastr["warning"]("请选择角色");
				return;
			}

            var data = {
                'loginName': $scope.loginName,
                'name': $scope.name,
                'email': $scope.email,
                'mobile': $scope.mobile,
                'password': $scope.password,
                'confirmPassword': $scope.confirmPwd,
                'status': $scope.switchUserStatus == true ? 1 : 0,
				'deptId': $scope.orgId,
                'roles': roles
            };

			service.addUser(data, items, function() {$uibModalInstance.close();});
		}
	}]);

	app.controller('resetPasswordController', ['$scope','UsersService', '$uibModalInstance', 'items', function ($scope, service, $uibModalInstance, items) {
		var $ctrl = this;
		$ctrl.items = items;
		$ctrl.selected = {
			item: $ctrl.items[0]
		};

		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};

		$scope.resetPassword = function() {
			service.resetPassword($ctrl.selected.item.userId, $scope.newPwd, $scope.confirmPwd, function(){$uibModalInstance.close();});
		}
	}]);

	app.controller('EditUsersController', ['$scope', '$uibModalInstance', 'UsersService', '$interval', 'items', function ($scope, $uibModalInstance, service, $interval, items) {
		var $ctrl = this;
        $scope.orgData = [];

		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};

        //获取所属机构
        service.getOrg(function(response) {
        	var data = {
        		id: '',
				name: '请选择机构'
			}

            $scope.orgData.push(data);

            angular.forEach(response, function(obj) {
                $scope.orgData.push(obj);
			});
        });

        service.getUser(items[0].id, function(obj) {
            $scope.loginName = obj.loginName;
            $scope.name = obj.name;
            $scope.email = obj.email;
            $scope.mobile = obj.mobile;
            $scope.orgId = obj.deptId;
            $scope.switchUserStatus = obj.status;
        });

		service.getAllRoles(function(data) {
			$scope.roles = data;
			$interval(function(){
				$('input[type="checkbox"].minimal, input[type="radio"].minimal').iCheck({
					checkboxClass: 'icheckbox_minimal-blue',
					radioClass: 'iradio_minimal-blue'
				});

				service.getUserRole(items[0].id, function(data) {
					$.each($(':input[name="brand-suppliers-list1"]'), function(index, obj) {
						$.each(data, function(i, role) {
							if(obj.value == role.id) {
								$(':input[name="brand-suppliers-list1"][value="'+role.id+'"]').iCheck('check');

							}
						})
					});
				});

			}, 200, 1);
		});

		$scope.updateUser = function() {
			var roles = [];
			$.each($(':input[name="brand-suppliers-list1"]:checked'), function(index, obj) {
				roles[index] = obj.value;
			});

			if(roles.length <=0) {
				toastr["warning"]("请选择角色");
				return;
			}

			var data = {'id': items[0].id, 'loginName':$scope.loginName, 'name':$scope.name, 'email':$scope.email, 'mobile':$scope.mobile, 'deptId': $scope.orgId, 'status':$scope.switchUserStatus == true? 1 : 0,'roles':roles};
			service.updateUser(data, items, function(){$uibModalInstance.close();});
		}
	}]);

	app.controller('DeleteUsersController', ['$scope', '$uibModalInstance', 'UsersService', 'items', function ($scope, $uibModalInstance, service, items) {
		var $ctrl = this;

		$scope.name = items[0].name;
		$scope.title = '确定删除角色吗?';
		$scope.buttonName = '删除';

		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};

		$scope.deleteUser = function() {
			var data = {'id':items[0].id, 'name' : $scope.name, 'description' : $scope.description};
			service.deleteUser(items[0], items, function(){$uibModalInstance.close();});
		}
	}]);

    app.controller('UsersDepartmentController', ['$scope', '$http', '$interval', 'uiGridConstants','uiGridTreeViewConstants', '$uibModalInstance','UsersService', 'items', function($scope, $http, $interval, uiGridConstants,uiGridTreeViewConstants, $uibModalInstance, service, items) {
        var $ctrl = this;
        var filter = null;
        var selParentAndChild = false;
        var rowsId = [];
        var rowSelectionStatus = false;
        rowSelectionIdArr = [];

        $scope.gridOptions = {
            enableRowSelection: true,
            enableSelectAll: true,
            selectionRowHeaderWidth: 35,
            showGridFooter:true,
            isRowSelectable: function(row){
                if (row.entity.check) {
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
                {
                    name: 'id',
                    field: 'id',
                    displayName: 'ID',
                    enableSorting: false,
                    visible: false,
                    enableFiltering: false
                },
                {
                    name: 'parent_id',
                    field: 'parentId',
                    displayName: '父ID',
                    enableSorting: false,
                    visible: false,
                    enableFiltering: false
                },
                {
                    name: 'name',
                    field: 'name',
                    displayName: '名称',
					enableFiltering: false,
					enableSorting: false
                },
                {
                    name: 'description',
                    field: 'description',
                    displayName: '描述',
                    enableFiltering: false,
                    enableSorting: false
                }
            ]
        }
        $scope.gridOptions.multiSelect = true;

        $scope.cancel = function() {
            $uibModalInstance.dismiss();
        };

        $scope.gridOptions.onRegisterApi = function(gridApi){
            departmentGridApi = $scope.gridApi = gridApi;

            //全选
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope, function(row, event) {
            	if (rowSelectionStatus) {
                    angular.forEach($scope.gridApi.grid.rows, function(row, index) {
                        if (row.isSelected) {
                            rowSelectionIdArr.push(row.entity.id);
                        } else {
                            rowSelectionIdArr = [];
                        }
                    });
				}
            });

            //单选
            $scope.gridApi.selection.on.rowSelectionChanged($scope, function(row, index) {
            	if (rowSelectionStatus) {
                    if (row.isSelected && rowSelectionIdArr.indexOf(row.entity.id) == -1) {
                        allQuerySubclass(row.entity.id);
                    } else {
                        allDelSubclass(row.entity.id);
                    }
				}
            });
        };

        //递归查找是否有子类，有就勾选上
		var allQuerySubclass = function(id) {
			if (rowSelectionIdArr.indexOf(id) == -1) {
                rowSelectionIdArr.push(id);
			}

            angular.forEach(departmentGridApi.grid.rows, function(row, index) {
                if (row.entity.parentId == id && rowSelectionIdArr.indexOf(row.entity.id) == -1) {
                    rowSelectionIdArr.push(row.entity.id);
                    departmentGridApi.grid.api.selection.selectRow(row.entity);

                    //递归
                    allQuerySubclass(row.entity.id);
                }
            });
		}

        //递归删除所有有子类，去掉勾选
        var allDelSubclass = function(id) {
            if (rowSelectionIdArr.indexOf(id) >= 0 ) {
                rowSelectionIdArr.splice(rowSelectionIdArr.indexOf(id), 1);
            }

            angular.forEach(departmentGridApi.grid.rows, function(row, index) {
                if (row.entity.parentId == id) {
                    departmentGridApi.grid.api.selection.unSelectRow(row.entity);

                    if (rowSelectionIdArr.indexOf(row.entity.id) >= 0) {
                        rowSelectionIdArr.splice(rowSelectionIdArr.indexOf(row.entity.id), 1);
					}

                    allDelSubclass(row.entity.id);
                }
            });

        }


        var callbackFn = function(data) {
            setTreeLevel(data);
            $scope.gridOptions.data = data;
            $interval(function() {
                $scope.gridApi.treeBase.expandAllRows();

                $interval(function() {
                    selParentAndChild = true;
                    $scope.gridOptions.showTreeExpandNoChildren = !$scope.gridOptions.showTreeExpandNoChildren;
                    $scope.gridApi.grid.refresh();

                    //把默认的放入数组
                    angular.forEach(departmentGridApi.grid.rows, function(row, index) {
                        if (row.isSelected) {
                            rowSelectionIdArr.push(row.entity.id);
                        }
                    });


                    //加载完后才能执行点击行操作状态
                    rowSelectionStatus = true;
                }, 500, 1);
            }, 1000, 1);
        };

        var setTreeLevel = function(data) {
            var len = data.length;
            for(var i = 0; i < len ; i ++) {
                data[i].$$treeLevel = data[i].level;
            }
        };

        //取消弹窗
        $scope.cancel = function() {
            $uibModalInstance.dismiss();
        };

        $scope.addUsersDepartment = function(){
            service.addDepartment(items[0].id, rowSelectionIdArr,$scope.cancel);
        };

        service.getDepartment(items[0].id,null, callbackFn);
    }]);


    app.factory('UsersService', ['$q', '$filter', '$timeout', '$http', function ($q, $filter, $timeout, $http) {

		var getPage = function(page, pageSize, sort, filter, callbackFun) {
			var url = '/users/pages/' + (page -1) + '?pageSize=' + pageSize;
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

		var getUserRole = function(id, callbackFun) {
			var url = 'users/'+id+'/roles';

			$http.get(url)
				.success(function (response) {
					if(!callbackFun) {
						return;
					}
					callbackFun(response);
				});
		};

		var getAllRoles = function(callbackFun) {
			var url = '/roles';

			$http.get(url)
				.success(function (response) {
					if(!callbackFun) {
						return;
					}
					callbackFun(response);
				});
		};

		var addUser = function(data, items, closeFun) {
			$http.post('/users', angular.toJson(data))
    		.then(
    			function successCallback(response) {
					toastr["success"]("创建用户成功");
					items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
					closeFun();
				}
    		);
		}

		var getUser = function(id, callbakcFun) {
	    	$http.get('/users/' + id, null)
	    	.then(
	    			function successCallback(response) {
	    				callbakcFun(response.data);
					}
	    		);
	    }

		var changeStatus = function(id, status, callbackFn) {
			$http.post('/users', {id: id, status : status == true ? 1 : 0})
				.then(function successCallback(response) {
					if(!status) {
						toastr["warning"]("此用户已经禁用，将不能登录系统");
					} else {
						toastr["success"]("此用户已经成功激活");

					}

					if(callbackFn) {
						callbackFn();
					}

		    	});
		}

		var resetPassword = function(id, newPwd, confirmPwd, callbackFun) {
			$http.put('/users/' + id + '/passwords', {'id':id, 'newPassword': newPwd, 'confirmPassword': confirmPwd})
				.then(
					function successCallback(response) {
						toastr["success"]("密码更新成功");
						callbackFun();
                        //删除提示标签
                        if(document.querySelector('.tooltip')){
                            document.body.removeChild(document.querySelector('.tooltip'));
                        }
			    	}
				);
		};

		var updateUser = function(data, items, closeFun) {
		    	$http.put('/users/' + data.id, angular.toJson(data))
		    		.then(
		    			function successCallback(response) {
							toastr["success"]("更新用户成功");
							items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
							closeFun();
                            //删除提示标签
                            if(document.querySelector('.tooltip')){
                                document.body.removeChild(document.querySelector('.tooltip'));
                            }
						}
		    		);
		 };

		 var deleteUser = function(data, items, closeFun) {
			 $http.delete('/users/' + data.id, null)
	    		.then(
	    			function successCallback(response) {
						toastr["success"]("用户"+data.name+"删除成功");
						items[2](items[1].page, items[1].pageSize, items[1].sort, items[1].filter, items[3]);
						closeFun();
					},function errorCallback(response) {
						var msg = response.data.msg;
				    	toastr["error"](msg);
					}
	    		);
		 };

        var getDepartment = function(userId,filter, callbackFn) {
            var url = '/users/' + userId + '/department';

            $http.get(url)
                .success(function (response) {
                    if(!callbackFn) {
                        return;
                    }
                    callbackFn(response.data);
                });
        };

        var addDepartment = function(userId,data,cancel){
            var url = '/users/' + userId + '/department';
            $http.post(url, data)
                .then(
                    function successCallback(response) {
                        toastr["success"]("保存成功");
                        cancel();
                        //删除提示标签
                        if(document.querySelector('.tooltip')){
                            document.body.removeChild(document.querySelector('.tooltip'));
                        }
                    }

                );
        };

        //获取所属机构
        var getOrg = function(callbackFun) {
            $http.get('/users/department')
			.success(function(response, status, headers, congfig) {
				if (callbackFun) {
					callbackFun(response);
				} else{
					throw new Error('未填写参数');
				}
			})
        }

		return {
			getPage : getPage,
			getUserRole : getUserRole,
			getAllRoles : getAllRoles,
			addUser : addUser,
			changeStatus : changeStatus,
			resetPassword : resetPassword,
			getUser : getUser,
			updateUser : updateUser,
			deleteUser : deleteUser,
            getDepartment : getDepartment,
            addDepartment : addDepartment,
            getOrg: getOrg
		}
	}]);

    //标签格式化处理
    app.filter('roleNamesFormat',function() {
        return function(data) {
            if (data.length > 0) {
                var tabHtml = '';
                angular.forEach(data, function(obj, index) {
                    if (data.length - 1 == index) {
                        tabHtml += obj;
                    } else {
                        tabHtml += obj + '，';
                    }
                });

                return tabHtml;
            }
        }
    });
})();
