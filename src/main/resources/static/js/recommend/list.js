(function() {
    /*--------------------------------------------------------------
     | 全局变量
     |--------------------------------------------------------------
    */
    var rowSelectionIdArr  = [];
    var gridApis = null;
    var $ctrl = this;

    var app = angular.module('RecommendApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination','toggle-switch', 'ui.grid.selection', 'ui.grid.autoResize', ['js/common/main.css', 'js/bd/main.css']]);


    //专门用于初始化全局数据
    app.run(function($rootScope) {
        $rootScope.serviceTimeError = true;
    });

    //列表控制器
    app.controller('RecommendController', ['$scope', 'i18nService', '$uibModal', 'RecommendService', 'uiGridConstants', '$rootScope', function($scope, i18nService, $modal, service, uiGridConstants, $rootScope) {
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

        var imgTemplate = "<div class=\"fleft\"><img ng-src='{{row.entity.path}}'  height='150'></div>";
        
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
            rowHeight: 150,
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
                    field: 'path',
                    name: '图片',
                    width: 150,
                    enableSorting: false,
                    enableFiltering: false,
                    cellTemplate: imgTemplate
                },
                {
                    field: 'name',
                    name: '商品',
                    minWidth: 180,
                    enableSorting: false,
                    enableFiltering: false,
                },{
                    field: 'action',
                    name: '操作',
                    cellClass: 'controller-btn-a',
                    enableSorting: false,
                    enableFiltering: false,
                    width:100,
                    cellTemplate: function () {
                    	 return '<div class="line-height-30 line-center">' +
                         '<a href="#/recommend/del"     ng-click="grid.appScope.del(row.entity.id,row.entity.name)" data-toggle="tooltip" data-placement="left" title="删除" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-remove"></i></a>' +
                         '</div>';
                    }
                }
            ],
            onRegisterApi: function(gridApi) {
                 $scope.gridApi = gridApi;

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

        $scope.add = function(){

        	 pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
             pageParams.pageSize = $scope.gridOptions.paginationPageSize;

             $ctrl.items = [
                 {
                 },
                 pageParams,
                 service.getPage,
                 getPageCallbackFun,
                 $scope.gridOptions.totalItems
             ];
        	
        	 $modal.open({
                 animation: true,
                 templateUrl: 'recommend/commodity.html',
                 controller: 'RecommendAddController',
                 controllerAs: '$ctrl',
                 resolve: {
                     items: function () {
                         return $ctrl.items;
                     }
                 }
             });
        	
        }
        
        $scope.del = function(id,name){
        	
        	 pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
             pageParams.pageSize = $scope.gridOptions.paginationPageSize;

             $ctrl.items = [
                 {
                 },
                 pageParams,
                 service.getPage,
                 getPageCallbackFun
             ];

             Confirm({
 				  title:'删除推荐商品:'+name+'',
 		          msg: '是否要继续？',
 		          onOk: function(){
 		        	  service.del(id,$ctrl.items);
 		          },
 		          onCancel: function(){
 		        	  $.Deferred().reject();
 		          }
 		      })
        	
        	
        	
        	
        }
        

    }]);

    
    app.controller('RecommendAddController', ['$scope', '$uibModalInstance', 'items', 'RecommendService','uiGridConstants', function ($scope, $uibModalInstance, items, service,uiGridConstants) {
    	
    	
    	var $ctrl = this;
      	 
     	  $scope.cancel = function() {
               $uibModalInstance.dismiss();
           };
           
           var pageParams = {};
           
           var pageOptions = {
                   page: 1,
                   sort: ''
               }
           
           
           $scope.gridOptions = {
        		paginationPageSizes: [5, 10, 25, 50, 75, 100],
   				enableColumnResizing: true,
   				paginationPageSize: 25,
   				useExternalPagination: true,
   				useExternalSorting: true,
   				enableGridMenu: true,
   				enableFiltering: true,
   				useExternalFiltering: true,
   				enableRowSelection: true, 
   				enableRowHeaderSelection:true,
   				enableSelectAll: true,
   				selectionRowHeaderWidth: 35,
   				multiSelect : true,
   				modifierKeysToMultiSelect: false,
   				noUnselect : false,
                   columnDefs: [
                       {
                           field: 'id',
                           name: 'id',
                           minWidth: 10,
                           enableSorting: false,
                           enableFiltering: false,
                           visible: false
                           
                       },
                       {
                           field: 'commodityNum',
                           name: '商品编号',
                           width: 170,
                           enableSorting: false,
                           enableFiltering: true
                       },
                       {
                           field: 'tradeName',
                           name: '商品名称',
                           width: 300,
                           enableSorting: false,
                           enableFiltering: false,
                       },
                       {
                           field: 'marketPrice',
                           name: '市场价',
                           minWidth: 100,
                           enableSorting: false,
                           enableFiltering: false,
                           visible: false
                       },
                       {
                       	field: 'productType',
                       	name: '产品类别',
                       	minWidth: 100,
                       	enableSorting: false,
                       	enableFiltering: false
                       },
                       {
                       	field: 'goodsGrade',
                       	name: '商品等级',
                       	minWidth: 100,
                       	enableSorting: false,
                       	enableFiltering: false,
                        visible: false
                       },
                       {
                       	field: 'productNum',
                       	name: '库存',
                       	width:100,
                       	enableSorting: false,
                       	enableFiltering: false
                       },
                       {
                       	field: 'soldOutNum',
                       	name: '已售数量',
                       	minWidth: 100,
                       	enableSorting: false,
                       	enableFiltering: false,
                        visible: false
                       },
                       {
                       	field: 'repertoryStatus',
                       	name: '库存状态',
                       	minWidth: 100,
                       	enableSorting: false,
                       	enableFiltering: false,
                      	 filter: {
                               term: '',
                               type: uiGridConstants.filter.SELECT,
                               selectOptions: [
                                   {
                                       value: '',
                                       label: '全部'
                                   },
                                   {
                                       value: '1',
                                       label: '有货'
                                   },
                                   {
                                       value: '2',
                                       label: '缺货'
                                   }
                               ]
                           },cellTemplate: function() {
                               return '<div class="line-height-30 line-center">&nbsp;' +
                               '<span ng-if="row.entity.repertoryStatus == 1">有货</span>' +
                               '<span ng-if="row.entity.repertoryStatus ==2">缺货</span>' +
                           '</div>';
                           }
                       }
                   ],
                   onRegisterApi: function(gridApi) {
                       $scope.gridApi = gridApi;

                       //排序事件
                       $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
                           if (typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
                               pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
                               pageParams.pageSize = grid.options.paginationPageSize;
                               pageParams.sort = sortColumns[0].name + ' ' + sortColumns[0].sort.direction;

                               //获取过滤筛选参数
                               getFilter(grid);

                               //请求更新表格数据
                               service.getCommodityPage(pageParams, getPageCallbackFun);
                           }
                       });

                       //过滤搜索事件
                       $scope.gridApi.core.on.filterChanged($scope, function() {
                           pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
                           pageParams.pageSize = this.grid.options.paginationPageSize;

                           //获取过滤筛选参数
                           getFilter(this.grid);

                           //请求更新表格数据
                           service.getCommodityPage(pageParams, getPageCallbackFun);
                       });

                       //分页
                       $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
                           pageParams.pageNumber = newPage;
                           pageParams.pageSize = pageSize;

                           //获取过滤筛选参数
                           getFilter(this.grid);

                           //请求更新表格数据
                           service.getCommodityPage(pageParams, getPageCallbackFun);
                       });
                   }
               }
           
           
           var getPageCallbackFun = function(response) {
               $scope.gridOptions.totalItems = response.total;
               $scope.gridOptions.data = response.data;
           };
           
           //初始化获取数据
           pageParams.pageNumber = pageOptions.page;
           pageParams.pageSize = $scope.gridOptions.paginationPageSize;
           pageParams.sort = pageOptions.sort ? pageOptions.sort : '';

           var getFilter = function(grid) {
               var filter = {};

               // 消息类型
               var commodityNum = grid.columns[2].filters[0].term;
               if(commodityNum != '' && commodityNum != 'null' && commodityNum != undefined)
               		filter.commodityNum = commodityNum != '' ? commodityNum : '';

               	pageParams.filter = filter;
           }
           
           service.getCommodityPage(pageParams, getPageCallbackFun);
           
           
           $scope.add = function(){
        	   var currentSelection = $scope.gridApi.selection.getSelectedRows();
   				if(currentSelection==null||currentSelection==''||currentSelection==undefined){
   					toastr["error"]('请选择商品');
   					return;
   				}
   				if(currentSelection.length +items[4] > 4){
   					toastr["error"]('最多只能添加4个');
   					return;
   				}
   				
   				var param = {};
   				var data = [];
   				$.each(currentSelection,function(index,val){
   					 var obj = {};
   					 obj.id = val.id;
   					 obj.commodityNum = val.commodityNum;
   					 data.push(obj);
   				});
   				
   				param.param = data;
   				
   				
   				service.save(param,items,function(){
   				 $uibModalInstance.dismiss();
   				});
           }
    	
    	
    }]);


    /*--------------------------------------------------------------
     | 自定义服务
     |--------------------------------------------------------------
    */
    app.factory('RecommendService', ['$http', function($http) {
        return {
        	//查询列表
            getPage: function(pageParams, callbackFun) {
                $http.get('/recommend/list').success(function(response) {
                	if (callbackFun) {
                        callbackFun(response);
                    } else{
                        throw new Error('未填写参数');
                    }
                });
            },
            getCommodityPage: function(pageParams, callbackFun) {
                pageParams.pageNumber = (pageParams.pageNumber - 1);
                pageParams.status = 1;
                $http.get('/commodity/list', {
                    params: pageParams
                }).success(function(response, status, headers, congfig) {
                    if (callbackFun) {
                        callbackFun(response);
                    } else{
                        throw new Error('未填写参数');
                    }
                });
            },
            del:function(id,items){
            	 $http.delete("/recommend/"+id).success(function(response) {
            		 toastr["success"]('删除成功');
            		 items[2](items[1], items[3]);
                });
            },
            save:function(param,items,closeFun){
            	$http.post("/recommend",angular.toJson(param)).success(function(response) {
           		 toastr["success"]('保存成功');
        		 items[2](items[1], items[3]);
        		 closeFun();
            });
            	
            	
            }
           
        }
    }]);

})();