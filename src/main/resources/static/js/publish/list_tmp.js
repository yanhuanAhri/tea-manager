(function() {
	
	var app = angular.module('publishApp', ['ngTouch', 'ui.grid.treeView','toggle-switch',[
		'js/publish/main.css',
		'dist/css/fileinput.min.css',
        'plugins/bootstrap-treeview/1.2.0/bootstrap-treeview.min.css',
        'plugins/bootstrap-treeview/1.2.0/bootstrap-treeview.min.js',
        'plugins/bootstrap-paginator/bootstrap-paginator.js',
        'plugins/file/fileinput.js',
		]]);

	app.controller('PublishController', [ '$scope', '$http', '$uibModal', '$interval', 'PublishService','$sce', function($scope, $http, $modal, $interval, service,$sce) {

		var $ctrl = this;
		
		
		$scope.purposeData = [{'id':1,'purpose':'自饮'},{'id':2,'purpose':'(简装礼盒)+送礼(精装礼盒)'}];
		
		$scope.purpose = $scope.purposeData[0];
		
		
		$scope.repertoryData = [{'id':1,'repertory':'有货'},{'id':2,'repertory':'缺货'}];
		
		$scope.repertory = $scope.repertoryData[0];
		
		//图片集合
		$scope.coverlist = [];
		$scope.detaillist = [];
		$scope.particularlist = [];
		
		
		var getPageCallbackFun = function(type,obj){
		}
		
		
		$scope.select = function() {
			$ctrl.items = [getPageCallbackFun,$scope.coverlist,$scope.detaillist,$scope.particularlist];
			
			$modal.open({
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'publish/imgs.html',
				controller: 'ImgslistController',
				controllerAs: '$ctrl',
				size:'1200',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
			}});
		}
		
		var saveCallbackFun = function(){
			
			return;
			//清空数据
			$scope.coverlist = [];
			$scope.detaillist = [];
			$scope.particularlist = [];
			
			$scope.tradeName = '';
			$scope.teaName = '';
			$scope.productType = '';
			$scope.pickYear = '';
			$scope.pickSeason = '';
			$scope.goodsGrade = '';
			$scope.netContent = '';
			$scope.originPlace = '';
			$scope.foodProductionLicence = '';
			
			$scope.purpose = $scope.purposeData[0];
			$scope.repertory = $scope.repertoryData[0];
			
			$scope.specification = '';
			$scope.storeMethod = '';
			$scope.expirationData = '';
			$scope.craft = '';
			$scope.packingSpecification = '';
			$scope.productNum = '';
			$scope.marketPrice = '';
			$scope.promotionPrice = '';
			
			
			
		}
	
		
		$scope.release = function(type){
			
			var productObj = new Object();
			productObj['type'] = type;
			
			if($scope.coverlist.length == 0 && $scope.detaillist.length == 0 && $scope.particularlist.length == 0){
				toastr["warning"]("至少上传一张图片");
				return;
				
			}
			
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
				toastr["warning"]("动价格不能小于0");
				return;
			}
			
			productObj['promotionPrice'] = $scope.promotionPrice;
			
			service.saveProduct(productObj,saveCallbackFun);
				
		}
	}]);
	
	
	app.controller('ImgslistController', [ '$scope', '$http', '$uibModal', '$interval', 'PublishService','$sce','items' ,'$uibModalInstance',function($scope, $http, $modal, $interval, service,$sce,items,$uibModalInstance) {
		
		var $ctrl = this;
		
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.typeData = [{'id':'cover','type':'封面图'},{'id':'detail','type':'详情图'},{'id':'particular','type':'细节图'}]
		$scope.type = $scope.typeData[0];
		$scope.active = $scope.type.id;
		$scope.cateChange = function(type){
			$scope.active = type.id;
		}
		
		
		$scope.coverlist = items[1];
		$scope.detaillist = items[2];
		$scope.particularlist = items[3];
		
		
		var imgPathCallbackFun = function(type,path){
			
			var obj = {};
			obj.path = path;
			console.log(path);
			if(type == 'cover'){
				$scope.coverlist.push(obj);
			}
			
			if(type == 'detail'){
				$scope.detaillist.push(obj);
			}
			
			if(type == 'particular'){
				$scope.particularlist.push(obj);
			}
			
			items[0](type,obj);
		}
		
		
		
		$scope.okselect = function(){
			
			$ctrl.items = [$scope.type.id,imgPathCallbackFun];
			
			$modal.open({
				size:'1200',
				animation: $ctrl.animationsEnabled,  
				templateUrl: 'publish/upload.html',
				controller: 'PublishImgUploadController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function () {
			    		return $ctrl.items;
			    	}
				}
			});
		}
		
	
		
	}]);
	
	app.controller('PublishImgUploadController', [ '$scope', '$http', 'uiGridConstants', '$uibModalInstance', 'PublishService', 'items', function($scope, $http, uiGridConstants, $uibModalInstance, service, items) {
		
		$scope.cancel = function() {
			 $uibModalInstance.dismiss();
		};
		
		$scope.types = items[0];
		$scope.upload = function(outData) {
			items[1](items[0],outData.response.path)
			toastr["success"]("添加图片成功");
			$scope.cancel();
		}
	}]);
	
	
	app.factory('PublishService', ['$q', '$filter', '$timeout', '$http', function ($q, $filter, $timeout, $http) {
		
		var saveProduct = function(params, callbackFn) {
			$http.post('/publish',angular.toJson(params))
			.success(function (response) {
				callbackFn();
				toastr["success"]("保存成功");
			});
		};
		


		
		return {
			saveProduct:saveProduct
		}
	}]);
	
})();
