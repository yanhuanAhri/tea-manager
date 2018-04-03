(function() {
    /*--------------------------------------------------------------
     | 全局变量
     |--------------------------------------------------------------
    */
    var rowSelectionIdArr  = [];
    var gridApis = null;
    var $ctrl = this;

    var app = angular.module('UserInfoApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination','toggle-switch', 'ui.grid.selection', 'ui.grid.autoResize', ['js/common/main.css', 'js/bd/main.css']]);


    //专门用于初始化全局数据
    app.run(function($rootScope) {
        $rootScope.serviceTimeError = true;
    });

    //列表控制器
    app.controller('UserInfoController', ['$scope', 'i18nService', '$uibModal', 'UserInfoService', 'uiGridConstants', '$rootScope', function($scope, i18nService, $modal, service, uiGridConstants, $rootScope) {
        rowSelectionIdArr  = [];
        gridApis = null;

        /*--------------------------------------------------------------
         | 初始化model变量
         |--------------------------------------------------------------
        */
        //显示、隐藏按钮
        $scope.showOrHide = '隐藏过滤';
        var messageTypeData = []; //表格消息类型下拉搜索数据

        /*--------------------------------------------------------------
         | 表格
         |--------------------------------------------------------------
        */
        //分页初始化设置
        var pageOptions = {
            page: 1,
            sort: ''
        }

        //分页参数
        var pageParams = {};

        //表格语言中文
        i18nService.setCurrentLang("zh-cn");

        $scope.gridOptions = {
            paginationPageSizes: [5, 10, 25, 50, 75, 100,500],
            paginationPageSize: 25,
            useExternalPagination: true,
            useExternalSorting: true,
            enableGridMenu: true,
            enableFiltering: true,
            useExternalFiltering: true,
            multiSelect: true,
            allowCopy: true,
            modifierKeysToMultiSelect: false,
            noUnselect: false,
            enableRowSelection: true,
            enableRowHeaderSelection : true,
            enableColumnResizing: true,
            columnDefs: [
                {
                    field: 'id',
                    name: 'id',
                    minWidth: 150,
                    enableSorting: false,
                    enableFiltering: false,
                    visible: false
                    
                },
                {
                    field: 'userName',
                    name: '用户名',
                    enableSorting: false,
                    enableFiltering: true
                },
                {
                    field: 'nickName',
                    name: '昵称',
                    minWidth: 180,
                    enableSorting: false,
                    enableFiltering: false,
                },
                {
                    field: 'email',
                    name: '邮箱',
                    minWidth: 180,
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                	field: 'phone',
                	name: '手机号',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: true
                },
                {
                	field: 'sex',
                	name: '性别',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: true,
                	filter: {
                        term: '',
                        type: uiGridConstants.filter.SELECT,
                        selectOptions: [
                            {
                                value: '',
                                label: '全部'
                            },
                            {
                                value: '0',
                                label: '女'
                            },
                            {
                                value: '1',
                                label: '男'
                            }
                        ]
                    },cellTemplate: function () {
                        return '<div class="line-height-30 line-center">' +
                        '<span ng-if="row.entity.sex == 0">女</span>' +
                        '<span ng-if="row.entity.sex ==1">男</span>' +
                        +'</div>';
        }
                },
                {
                	field: 'birthday',
                	name: '生日',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: false
                },
                {
                	field: 'integral',
                	name: '积分',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: false
                },
                {
                	field: 'createTime',
                	name: '注册时间',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: false
                }
            ],
            onRegisterApi: function(gridApi) {
                gridApis = $scope.gridApi = gridApi;

                //排序事件
                $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                    if (typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
                        pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
                        pageParams.pageSize = grid.options.paginationPageSize;
                        pageParams.sort = sortColumns[0].name + ' ' + sortColumns[0].sort.direction;

                        //获取过滤筛选参数
                        getFilter(grid);

                        //请求更新表格数据
                        service.getPage(pageParams, getPageCallbackFun);
                    }
                });

                //过滤搜索事件
                $scope.gridApi.core.on.filterChanged($scope, function() {
                    pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
                    pageParams.pageSize = this.grid.options.paginationPageSize;

                    //获取过滤筛选参数
                    getFilter(this.grid);

                    //请求更新表格数据
                    service.getPage(pageParams, getPageCallbackFun);
                });

                //分页
                $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
                    pageParams.pageNumber = newPage;
                    pageParams.pageSize = pageSize;

                    //获取过滤筛选参数
                    getFilter(this.grid);

                    //请求更新表格数据
                    service.getPage(pageParams, getPageCallbackFun);
                });
            }
        }

        /**
         * 请求成功回调函数获取表格数据
         * @param {object} response 表格数据
         * @return viod
         */
        var getPageCallbackFun = function(response) {
            $scope.gridOptions.totalItems = response.total;
            $scope.gridOptions.data = response.data;
        };

        /**
         * 表格过滤搜索处理函数
         * @param {object} grid 表格对象
         * @return viod
         */
        var getFilter = function(grid) {

        	var filter ={};
            // 消息类型
            var userName = grid.columns[1].filters[0].term;
            
            if(userName != '' && userName != 'null' && userName != undefined)
            	 filter.userName = userName != '' ? userName : '';
           
            
            
            var phone = grid.columns[4].filters[0].term;
            
            if(phone != '' && phone != 'null' && phone != undefined)
            	filter.phone = phone != '' ? phone : '';
            
            
            var sex = grid.columns[5].filters[0].term;
            if(sex != '' && sex != 'null' && sex != undefined)
            	filter.sex = sex != '' ? sex : '';
            
            
            pageParams.filter = filter;
        }

        //初始化获取数据
        pageParams.pageNumber = pageOptions.page;
        pageParams.pageSize = $scope.gridOptions.paginationPageSize;
        pageParams.sort = pageOptions.sort ? pageOptions.sort : '';


        
        service.getPage(pageParams, getPageCallbackFun);

        /*--------------------------------------------------------------
         | 事件
         |--------------------------------------------------------------
        */
        //显示、隐藏过滤事件
        $scope.toggleFiltering = function() {
            $scope.gridOptions.enableFiltering = !$scope.gridOptions.enableFiltering;
            $scope.gridApi.core.notifyDataChange(uiGridConstants.dataChange.COLUMN);
            $scope.showOrHide = $scope.gridOptions.enableFiltering ? '隐藏过滤' : '显示过滤';
        }


        



    }]);

    




    /*--------------------------------------------------------------
     | 自定义服务
     |--------------------------------------------------------------
    */
    app.factory('UserInfoService', ['$http', function($http) {
        return {
        	//查询列表
            getPage: function(pageParams, callbackFun) {
                pageParams.pageNumber = (pageParams.pageNumber - 1);

                $http.get('/userInfo/list',{ params: pageParams}).success(function(response, status, headers, congfig) {
                	if (callbackFun) {
                        callbackFun(response);
                    } else{
                        throw new Error('未填写参数');
                    }
                });
            }
          
           
        }
    }]);

})();