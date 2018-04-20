(function() {
    /*--------------------------------------------------------------
     | 全局变量
     |--------------------------------------------------------------
    */
    var rowSelectionIdArr  = [];
    var gridApis = null;
    var $ctrl = this;

    var app = angular.module('OrderApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination','toggle-switch', 'ui.grid.selection', 'ui.grid.autoResize', ['js/common/main.css', 'js/bd/main.css']]);


    //专门用于初始化全局数据
    app.run(function($rootScope) {
        $rootScope.serviceTimeError = true;
    });

    //列表控制器
    app.controller('OrderController', ['$scope', 'i18nService', '$uibModal', 'OrderService', 'uiGridConstants', '$rootScope', function($scope, i18nService, $modal, service, uiGridConstants, $rootScope) {
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
                    field: 'orderNum',
                    name: '订单编号',
                    width:270,
                    enableSorting: false,
                    enableFiltering: true
                },
                {
                    field: 'tradeName',
                    name: '商品名',
                    minWidth: 180,
                    enableSorting: false,
                    enableFiltering: true,
                },
                {
                    field: 'paymentAmount',
                    name: '支付金额',
                    minWidth: 90,
                    enableSorting: false,
                    enableFiltering: false,
                },
                {
                    field: 'buyNum',
                    name: '商品数量',
                    minWidth: 90,
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'totalAmount',
                    name: '商品总额',
                    minWidth: 90,
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                	field: 'consignee',
                	name: '收货人',
                    minWidth: 100,
                	enableSorting: false,
                	enableFiltering: true
                },
                {
                	field: 'phone',
                	name: '联系方式',
                    minWidth: 120,
                	enableSorting: false,
                	enableFiltering: true
                },
                {
                	field: 'address',
                	name: '收货方式',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: false
                },
                {
                	field: 'logisticsMode',
                	name: '物流',
                    minWidth: 100,
                	enableSorting: false,
                	enableFiltering: false
                },
                {
                	field: 'paymentMode',
                	name: '支付方式',
                    minWidth: 100,
                	enableSorting: false,
                	enableFiltering: false
                },
                {
                	field: 'remark',
                	name: '留言',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: false
                },
                {
                	field: 'status',
                	name: '订单状态',
                    minWidth: 100,
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
                                 label: '未付款'
                             },
                             {
                                 value: '1',
                                 label: '完成'
                             },
                             {
                                 value: '2',
                                 label: '待发货'
                             },
                             {
                                 value: '3',
                                 label: '待收货'
                             },
                             {
                                 value: '4',
                                 label: '待评价'
                             },
                             {
                                 value: '5',
                                 label: '退款售后'
                             }, {
                                 value: '10',
                                 label: '交易关闭'
                             }
                         ]
                     },
                     
                     cellTemplate: function() {
                         return '<div class="line-height-30 line-center">&nbsp;' +
                                     '<span ng-if="row.entity.status == 0">未付款</span>' +
                                     '<span ng-if="row.entity.status == 1">完成</span>' +
                                     '<span ng-if="row.entity.status ==2">待发货</span>' +
                                     '<span ng-if="row.entity.status == 3">待收货</span>' +
                                     '<span ng-if="row.entity.status == 4">待评价</span>' +
                                     '<span ng-if="row.entity.status == 5">退款售后</span>' +
                                     '<span ng-if="row.entity.status == 10">交易关闭</span>' +
                                 '</div>';
                     }
                },
                {
                	field: 'creatTime',
                	name: '创建时间',
                    minWidth: 180,
                	enableSorting: false,
                	enableFiltering: false,
                	cellTemplate: function() {
                        return '<div class="line-height-30 line-center">&nbsp;' +
                        '<span >{{row.entity.creatTime | date:\'yyyy-MM-dd hh:mm:ss\'}}</span>' +
                    '</div>';
                    }
                },
                {
                    field: 'action',
                    name: '操作',
                    cellClass: 'controller-btn-a',
                    enableSorting: false,
                    enableFiltering: false,
                    width:100,
                    cellTemplate: function () {
                    	 return '<div class="line-height-30 line-center">' +
                         '<a href="#/order/deliveryn" has-perm="ORDER_DELIVERYN"   ng-if="row.entity.status == 2" ng-click="grid.appScope.openDeliveryn(row.entity.id)" data-toggle="tooltip" data-placement="left" title="发货" class="btn btn-social-icon btn-xs btn-bitbucker"><i class="fa fa-fw fa-truck"></i></a>' +
                         '<a href="#/order/refund"  has-perm="ORDER_REFUND" ng-if="row.entity.status == 5" ng-click="grid.appScope.openRefund(row.entity.id)" data-toggle="tooltip" data-placement="left" title="退款" class="btn btn-social-icon btn-xs btn-dange"><i class="fa fa-fw fa-edit"></i></a>' +
                         '</div>';
                    }
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
            var filterObj = {};

            var orderNum = grid.columns[1].filters[0].term;
            if(orderNum != '' && orderNum != 'null' && orderNum != undefined)
            	filterObj.orderNum =orderNum;
            
            var tradeName = grid.columns[2].filters[0].term;
            if(tradeName != '' && tradeName != 'null' && tradeName != undefined)
            	filterObj.tradeName =tradeName;
            
            
            var consignee = grid.columns[6].filters[0].term;
            if(consignee != '' && consignee != 'null' && consignee != undefined)
            	filterObj.consignee =consignee;
            
            var phone = grid.columns[7].filters[0].term;
            if(phone != '' && phone != 'null' && phone != undefined)
            	filterObj.phone =phone;
            
            var status = grid.columns[12].filters[0].term;
            if(status != '' && status != 'null' && status != undefined)
            	filterObj.status =status;
            

            pageParams.filter = filterObj;
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

        //发货
        $scope.openDeliveryn = function(id){
        	
        	 utils.deletTip();

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
				  title:'确定发货?',
		          msg: '是否要继续？',
		          onOk: function(){
		        	  service.deliveryn(id,$ctrl.items);
		          },
		          onCancel: function(){
		        	  $.Deferred().reject();
		          }
		      })
        	
        }
        
        //退款
        $scope.openRefund = function(id){
        	
        	
        	 utils.deletTip();

             pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
             pageParams.pageSize = $scope.gridOptions.paginationPageSize;
        	
             $ctrl.items = [
                            {
                            'id':id
                            },
                            pageParams,
                            service.getPage,
                            getPageCallbackFun
             ];
        	
             

 			$modal.open({
 				animation: $ctrl.animationsEnabled,  
 				templateUrl: 'order/audit.html',
 				controller: 'OrderAuditController',
 				controllerAs: '$ctrl',
 				resolve: {
 			    	items: function () {
 			    		return $ctrl.items;
 			    	}
 			}});
        }
    }]);

    
    app.controller('OrderAuditController', [ '$scope', '$http', '$uibModal', '$interval', 'OrderService','$sce','items' ,'$uibModalInstance',function($scope, $http, $modal, $interval, service,$sce,items,$uibModalInstance) {
    	
    	var $ctrl = this;
		
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
    	
		$scope.audit = function(type){
			
			if(type == 0 && ($scope.reason == '' || $scope.reason == undefined)){
				toastr["warning"]("请输入不通过理由");
				return;
			}
			
			var param = {};
			param.remark = $scope.reason;
			param.type = type;
			
			service.openRefund(param,items,function(){
				$uibModalInstance.dismiss();
			});
			
		} 	
    }]);




    /*--------------------------------------------------------------
     | 自定义服务
     |--------------------------------------------------------------
    */
    app.factory('OrderService', ['$http', function($http) {
        return {
        	//查询列表
            getPage: function(pageParams, callbackFun) {
                pageParams.pageNumber = (pageParams.pageNumber - 1);

                $http.get('/order/list', {
                    params: pageParams
                }).success(function(response, status, headers, congfig) {
                    if (callbackFun) {
                        callbackFun(response);
                    } else{
                        throw new Error('未填写参数');
                    }
                });
            },
            //发货
            deliveryn:function(id,items){
                $http.put('/order/deliveryn/'+id)
                .success(function(response) {
                	 toastr["success"]('发货成功');
                	items[2](items[1], items[3]);
                });
            },
            //退款
            openRefund:function(param,items,closeFan){
                $http.post('/order/refund/'+items[0].id,angular.toJson(param))
                .success(function(response) {
                	 toastr["success"]('提交成功');
                	items[2](items[1], items[3]);
                	closeFan();
                });
            }
          
           
        }
    }]);

})();