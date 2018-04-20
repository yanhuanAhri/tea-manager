(function() {
    /*--------------------------------------------------------------
     | 全局变量
     |--------------------------------------------------------------
    */
    var rowSelectionIdArr  = [];
    var gridApis = null;
    var $ctrl = this;

    var app = angular.module('CommodityApp', ['ngTouch', 'ui.grid', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination','toggle-switch', 'ui.grid.selection', 'ui.grid.autoResize', ['js/common/main.css', 'js/bd/main.css']]);


    //专门用于初始化全局数据
    app.run(function($rootScope) {
        $rootScope.serviceTimeError = true;
    });

    //列表控制器
    app.controller('CommodityController', ['$scope', 'i18nService', '$uibModal', 'CommodityService', 'uiGridConstants', '$rootScope', function($scope, i18nService, $modal, service, uiGridConstants, $rootScope) {
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
                    enableFiltering: false
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
                	enableFiltering: false
                },
                {
                	field: 'purpose',
                	name: '用途',
                	minWidth: 90,
                	enableSorting: false,
                	enableFiltering: false
                	,cellTemplate: function() {
                        return '<div class="line-height-30 line-center">&nbsp;' +
                        '<span ng-if="row.entity.purpose == 1">自饮</span>' +
                        '<span ng-if="row.entity.purpose ==2">送礼</span>' +
                    '</div>';
                    }
                },
               /* {
                	field: 'specification',
                	name: '规格',
                	minWidth: 100,
                	enableSorting: false,
                	enableFiltering: false
                },*/
                {
                	field: 'originPlace',
                	name: '产地',
                	minWidth: 100,
                	enableSorting: false,
                	enableFiltering: false
                },
                {
                	field: 'expirationData',
                	name: '保质期',
                	minWidth: 80,
                	enableSorting: false,
                	enableFiltering: false
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
                	enableFiltering: false
                },
                {
                	field: 'updateTime',
                	name: '更新时间',
                	width: 170,
                	enableSorting: false,
                	enableFiltering: false
                	,cellTemplate: function() {
                        return '<div class="line-height-30 line-center">&nbsp;' +
                        '<span >{{row.entity.updateTime | date:\'yyyy-MM-dd hh:mm:ss\'}}</span>' +
                    '</div>';
                    }
                },
                {
                	field: 'repertoryStatus',
                	name: '库存状态',
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
                                 value: '1',
                                 label: '上架'
                             },
                             {
                                 value: '2',
                                 label: '下架'
                             }, {
                                 value: '0',
                                 label: '未上架'
                             }
                         ]
                     },
                     cellTemplate: function() {
                         return '<div class="line-height-30 line-center">&nbsp;' +
                                     '<span ng-if="row.entity.status == 1">上架</span>' +
                                     '<span ng-if="row.entity.status ==2">下架</span>' +
                                     '<span ng-if="row.entity.status == 0">未上架</span>' +
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
                         '<a href="#/commodity/down" has-perm="COMMODITY_OPERATION" ng-if="row.entity.status == 1" ng-click="grid.appScope.openOperConfirmWin(row.entity.id,\'down\')" data-toggle="tooltip" data-placement="left" title="下架" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-arrow-down"></i></a>' +
                         '<a href="#/commodity/edit" has-perm="COMMODITY_EDIT" ng-if="row.entity.status != 1" ng-click="grid.appScope.openEditConfirmWin(row.entity.id)" data-toggle="tooltip" data-placement="left" title="编辑" class="btn btn-social-icon btn-xs btn-bitbucket"><i class="fa fa-fw fa-edit"></i></a>' +
                         '<a href="#/commodity/up" has-perm="COMMODITY_OPERATION" ng-if="row.entity.status == 2 || row.entity.status == 3" ng-click="grid.appScope.openOperConfirmWin(row.entity.id,\'up\')" data-toggle="tooltip" data-placement="left" title="上架" class="btn btn-social-icon btn-xs btn-success"><i class="fa fa-fw  fa-arrow-up"></i></a>' +
                         '<a href="#/commodity/del" has-perm="COMMODITY_DEL" ng-if="row.entity.status != 1" ng-click="grid.appScope.openDelConfirmWin(row.entity.id,row.entity.commodityNum)" data-toggle="tooltip" data-placement="left" title="删除" class="btn btn-social-icon btn-xs btn-danger"><i class="fa fa-fw fa-remove"></i></a>' +
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
            var filter = {};

            // 商品编号
            var commodityNum = grid.columns[1].filters[0].term;
            if(commodityNum != '' && commodityNum != 'null' && commodityNum != undefined)
            	filter.commodityNum = commodityNum != '' ? commodityNum : '';

            // 库存状态
            var repertoryStatus = grid.columns[12].filters[0].term;
            if(repertoryStatus != '' && repertoryStatus != 'null' && repertoryStatus != undefined)
            	filter.repertoryStatus = repertoryStatus != '' ? repertoryStatus : '';
            
            // 商品状态
            var status = grid.columns[13].filters[0].term;
            if(status != '' && status != 'null' && status != undefined)
            	filter.status = status != '' ? status : '';
            
            
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

        
        //上下架操作
        $scope.openOperConfirmWin = function(id,type){
        	
        	utils.deletTip();
        	//页数,
        	pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
            pageParams.pageSize = $scope.gridOptions.paginationPageSize;
            //数据
        	 $ctrl.items = [
                 {
                 },
                 pageParams,
                 service.getPage,
                 getPageCallbackFun
             ];
        	 
        	 var params = {};
        	 params.type = type;
        	
        	service.operation(id,params,$ctrl.items);
        }
        

        //详情编辑
        $scope.openEditConfirmWin = function(id){
        	utils.deletTip();
       	 pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
            pageParams.pageSize = $scope.gridOptions.paginationPageSize;

            $ctrl.items = [
                {
                    id: id,
                },
                pageParams,
                service.getPage,
                getPageCallbackFun
            ];
       	
            $modal.open({
                animation: true,
                templateUrl: 'commodity/edit.html',
                controller: 'CommodityEditController',
                size:'1200',
                controllerAs: '$ctrl',
                resolve: {
                    items: function () {
                        return $ctrl.items;
                    }
                }
            });
       	
       }

        // 删除产品
        $scope.openDelConfirmWin = function(id, name) {
            //删除黑色泡泡提示
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
				  title:'删除商品:'+name+'',
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

    app.controller('CommodityEditController', ['$scope', '$uibModalInstance', 'items', 'CommodityService', function ($scope, $uibModalInstance, items, service) {
    	//商品编辑
    	
    	
   	 var $ctrl = this;
   	 
   	  $scope.cancel = function() {
             $uibModalInstance.dismiss();
         };
         
         $scope.purposeData = [{'id':1,'purpose':'自饮'},{'id':2,'purpose':'(简装礼盒)+送礼(精装礼盒)'}];
         
         $scope.repertoryData = [{'id':1,'repertory':'有货'},{'id':2,'repertory':'缺货'}];
         
         //商品数据回调
         var callbackFun = function(res){
       	  	 var obj = res.data;
        	 $scope.tradeName = obj.tradeName;
        	 $scope.teaName = obj.teaName;
        	 $scope.productType = obj.productType;
        	 $scope.pickYear = obj.pickYear;
        	 $scope.pickSeason = obj.pickSeason;
        	 $scope.goodsGrade = obj.goodsGrade;
        	 $scope.netContent = obj.netContent;
        	 $scope.originPlace =  obj.originPlace;
        	 $scope.foodProductionLicence = obj.foodProductionLicence;
        	 $scope.purpose = $scope.purposeData[obj.purpose-1];
       	  	 $scope.repertory = $scope.repertoryData[obj.repertoryStatus-1];
       	  	 $scope.specification = obj.specification;
       	  	 $scope.storeMethod = obj.storeMethod;
       	  	 $scope.expirationData = obj.expirationData;
       	  	 $scope.craft = obj.craft;
       	  	 $scope.packingSpecification = obj.packingSpecification;
       	  	 $scope.productNum = obj.productNum;
       	  	 $scope.marketPrice = obj.marketPrice;
       	  	 $scope.promotionPrice = obj.promotionPrice;
       	  	 
       	  	 //图片集合
       	  	var coverList = res.cover;
       	  	var detailList = res.detail;
       	    var particularList = res.particular;
       	    var homeList = res.home;
       	  	 
       	    //图片回显
       	  var img = new Image(); 
       	  	var path = coverList[0].path; 
       	  	img.src=path;
			img.onload = function (){ 
				var start = path.lastIndexOf("/");
				var imgName = path.substring(start+1, path.length);
				
				
				$("#input-dim-1").fileinput({'showUpload':false, 'previewFileType':'any',
					allowedFileExtensions: ["jpg", "png", "gif"],
					initialPreview: ["<img id='brand-product-reshow-logo' class='file-preview-image' alt='"+imgName+"' title='"+imgName+"' style='width:360px;height:160px; margin-top: 0px;' src='"+path+"'/>"],
					initialPreviewConfig: [
					{
				        caption: imgName, 
				        width: '256',
				        url: "$urlD",
				        key: 100, 
				        extra: {id: 100}
				    }],
				    showRemove: true,
				    maxFileCount : 1,
	                initialPreviewShowDelete: false,
	                msgFilesTooMany : "只能上传一张",
	                initialPreviewThumbTags: [{
	                    '{CUSTOM_TAG_NEW}': ' ',    
	                    '{CUSTOM_TAG_INIT}': 'lt;spanclass=\'custom-css\'>CUSTOM MARKUP;/span>'
	                }],
	                removeFromPreviewOnError: true});
			}
			 //图片回显
			 var img1 = new Image(); 
			 img1.src=detailList[0].path;
			 img1.onload = function (){ 
			 
				 var configName = [];
				 var imgHtml = [];
				 angular.forEach(detailList, function(node,ind) {
					 var p =node.path;
					 var imgName = p.split("/")[2];
					 
					 var obj = {};
					 obj.width = '256';
					 obj.url = '$urlD';
					 obj.key=100;
					 obj.extra = '{id；100}';
					 obj.caption = imgName;
					 configName.push(obj);
					 
					 
					 var imgH = "<img id='brand-product-reshow-logo' class='file-preview-image' alt='"+imgName+"' title='"+imgName+"' style='width:360px;height:160px; margin-top: 0px;' src='"+p+"'/>";
					 imgHtml.push(imgH);
					 
				 });
				 
				 	
					
					
					$("#input-dim-1-detail").fileinput({'showUpload':false, 'previewFileType':'any',
						allowedFileExtensions: ["jpg", "png", "gif"],
						initialPreview: imgHtml,
						initialPreviewConfig: configName,
					    showRemove: true,
					    maxFileCount : 7,
		                initialPreviewShowDelete: false,
		                msgFilesTooMany : "只能上传7张",
		                initialPreviewThumbTags: [{
		                    '{CUSTOM_TAG_NEW}': ' ',    
		                    '{CUSTOM_TAG_INIT}': 'lt;spanclass=\'custom-css\'>CUSTOM MARKUP;/span>'
		                }],
		                removeFromPreviewOnError: true});
			
			 }
			 
			 //图片回显
			 var homeImg = new Image(); 
			 homeImg.src=homeList[0].path;
			 homeImg.onload = function (){ 
			 
				 var configName = [];
				 var imgHtml = [];
				 angular.forEach(homeList, function(node,ind) {
					 var p =node.path;
					 var imgName = p.split("/")[2];
					 
					 var obj = {};
					 obj.width = '256';
					 obj.url = '$urlD';
					 obj.key=100;
					 obj.extra = '{id；100}';
					 obj.caption = imgName;
					 configName.push(obj);
					 
					 
					 var imgH = "<img id='brand-product-reshow-logo' class='file-preview-image' alt='"+imgName+"' title='"+imgName+"' style='width:360px;height:160px; margin-top: 0px;' src='"+p+"'/>";
					 imgHtml.push(imgH);
					 
				 });
				 
				 	
					
				
					$("#input-dim-1-home").fileinput({'showUpload':false, 'previewFileType':'any',
						allowedFileExtensions: ["jpg", "png", "gif"],
						initialPreview: imgHtml,
						initialPreviewConfig: configName,
					    showRemove: true,
					    maxFileCount : 1,
		                initialPreviewShowDelete: false,
		                msgFilesTooMany : "只能上传1张",
		                initialPreviewThumbTags: [{
		                    '{CUSTOM_TAG_NEW}': ' ',    
		                    '{CUSTOM_TAG_INIT}': 'lt;spanclass=\'custom-css\'>CUSTOM MARKUP;/span>'
		                }],
		                removeFromPreviewOnError: true});
			
			 }
			 //图片回显
			 var img2 = new Image(); 
			 img2.src=particularList[0].path;
			 img2.onload = function (){ 
			 
				 var configName = [];
				 var imgHtml = [];
				 angular.forEach(particularList, function(node,ind) {
					 var p =node.path;
					 var imgName = p.split("/")[2];
					 
					 var obj = {};
					 obj.width = '256';
					 obj.url = '$urlD';
					 obj.key=100;
					 obj.extra = '{id；100}';
					 obj.caption = imgName;
					 configName.push(obj);
					 
					 
					 var imgH = "<img id='brand-product-reshow-logo' class='file-preview-image' alt='"+imgName+"' title='"+imgName+"' style='width:360px;height:160px; margin-top: 0px;' src='"+p+"'/>";
					 imgHtml.push(imgH);
					 
				 });
				 
				 	
					
					
					$("#input-dim-1-particularl").fileinput({'showUpload':false, 'previewFileType':'any',
						allowedFileExtensions: ["jpg", "png", "gif"],
						initialPreview: imgHtml,
						initialPreviewConfig: configName,
					    showRemove: true,
					    maxFileCount : 7,
		                initialPreviewShowDelete: false,
		                msgFilesTooMany : "只能上传7张",
		                initialPreviewThumbTags: [{
		                    '{CUSTOM_TAG_NEW}': ' ',    
		                    '{CUSTOM_TAG_INIT}': 'lt;spanclass=\'custom-css\'>CUSTOM MARKUP;/span>'
		                }],
		                removeFromPreviewOnError: true});
			
			 }
       	  	 
         }
         
         
         $scope.coverlist = [];
         $scope.detaillist =[];
         $scope.particularlist =[];
         $scope.homelist=[];
         //保存商品
        $scope.release = function(type){
			
			var productObj = new Object();
			productObj['type'] = type;
			
			//获取图片
			var coverImg = $("#input-dim-1").parents(".file-input").find("img")[0];
			var detailImg = $('#input-dim-1-detail').parents(".file-input").find("img");
			var particularImg = $('#input-dim-1-particularl').parents(".file-input").find("img");
			var homeImg = $("#input-dim-1-home").parents(".file-input").find("img")[0];
			
			if(homeImg == null){
				toastr["warning"]("请上传首页");
				return;
			}
			
			if(coverImg == null){
				toastr["warning"]("请上传封面图");
				return;
			}
			
			if(detailImg.length == 0){
				toastr["warning"]("请上传细节图");
				return;
			}
			
			
			if(particularImg.length == 0){
				toastr["warning"]("请上传详情图");
				return;
			}
			
			var cover = {};
			cover.path = coverImg.src;
			cover.name = coverImg.title;
			$scope.coverlist.push(cover);
			
			var home = {};
			home.path = homeImg.src;
			home.name = homeImg.title;
			$scope.homelist.push(home);
			
			angular.forEach(detailImg, function(node,ind) {
    			var detail = {};
    			if(ind%2 == 0){
	    			detail.path = node.src;
	    			detail.name = node.title;
	    			$scope.detaillist.push(detail);
    			}
    		});
			
			angular.forEach(particularImg, function(node,ind) {
    			var particular = {};
    			if(ind%2 == 0){
	    			particular.path = node.src;
	    			particular.name = node.title;
	    			$scope.particularlist.push(particular);
    			}
    		});
			
			productObj['home'] = $scope.homelist;
			productObj['cover'] = $scope.coverlist;
			productObj['detail'] = $scope.detaillist;
			productObj['particular'] = $scope.particularlist;
			
			productObj['tradeName'] = $scope.tradeName;
			productObj['teaName'] = $scope.teaName;
			productObj['productType'] = $scope.productType;
			productObj['pickYear'] = $scope.pickYear;
			productObj['pickSeason'] = $scope.pickSeason;
			productObj['goodsGrade'] = $scope.goodsGrade;
			productObj['netContent'] = $scope.netContent;
			productObj['originPlace'] = $scope.originPlace;
			productObj['foodProductionLicence'] = $scope.foodProductionLicence;
			productObj['purpose'] = $scope.purpose.id;
			productObj['repertory'] = $scope.repertory.id;
			productObj['specification'] = $scope.specification;
			productObj['storeMethod'] = $scope.storeMethod;
			productObj['expirationData'] = $scope.expirationData;
			productObj['craft'] = $scope.craft;
			
			
			if($scope.packingSpecification == '' || $scope.packingSpecification == undefined){
				toastr["warning"]("请输入包装规格");
				return;
			}
			productObj['packingSpecification'] = $scope.packingSpecification;
			
			if($scope.productNum == '' || $scope.productNum == undefined){
				toastr["warning"]("请输入库存");
				return;
			}
			
			if($scope.productNum < 0){
				toastr["warning"]("库存不能小于0");
				return;
			}

			
			productObj['productNum'] = $scope.productNum;
			
			
			
			if($scope.marketPrice == '' || $scope.marketPrice == undefined){
				toastr["warning"]("请输入市场价格");
				return;
			}
			
			if($scope.marketPrice < 0){
				toastr["warning"]("市场价格不能小于0");
				return;
			}
			
			productObj['marketPrice'] = $scope.marketPrice;
			
			
			if($scope.promotionPrice == '' || $scope.promotionPrice == undefined){
				toastr["warning"]("请输入活动价格");
				return;
			}
			
			if($scope.promotionPrice < 0){
				toastr["warning"]("活动价格不能小于0");
				return;
			}
			
			productObj['promotionPrice'] = $scope.promotionPrice;
			
			service.edit(items,productObj,function() {
	             $uibModalInstance.dismiss();
	         });
				
		}
   	 
         service.detail(items[0].id,callbackFun);
   	
   }]);





    /*--------------------------------------------------------------
     | 自定义服务
     |--------------------------------------------------------------
    */
    app.factory('CommodityService', ['$http', function($http) {
        return {
            getPage: function(pageParams, callbackFun) {
                pageParams.pageNumber = (pageParams.pageNumber - 1);

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
            // 删除
            del: function(id,items) {
                $http.delete('/commodity/' + id)
                .success(function(response) {
                    toastr["success"]('删除成功');
                    items[2](items[1], items[3]);
                }).error(function(response, status, headers, congfig) {
                    //禁止按钮状态回调
                    statusCallFuns(false, true);
                });
            }, detail: function(id,callbackFun) {
                $http.get('/commodity/' + id)
                .success(function(response) {
                	callbackFun(response);
             })
            },
             //上下架
             operation: function(id,params,items) {
                 $http.post('/commodity/'+id+'/operation',angular.toJson(params))
                 .success(function(response) {
                	 items[2](items[1], items[3]);
              })
             },
             //编辑
              edit: function(items,params,close) {
                  $http.post('/commodity/'+items[0].id+'',angular.toJson(params))
                  .success(function(response) {
                 	 items[2](items[1], items[3]);
                 	close();
               })
          
              }
        }
    }]);

})();