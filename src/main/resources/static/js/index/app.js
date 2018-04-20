/**
 *
 */

(function(angular){
	var activeTab = "tab1"; //当前的tab页。默认为第一个tab页。
    var previousTab;  //上一个打开的tab页。默认为空。

	var BUTTONS_CODES = null;

	var mainApp = angular.module('menuApp', ['ngTouch', 'ngMessages','ui.router','oc.lazyLoad', 'ngImgCrop','ui.bootstrap']);

	mainApp.filter('timeFilter', function() {
	    return function(time) {
	    	if (time&&time.length==14) {
				var y=time.substring(0,4);
				var m=time.substring(4,6);
				var d=time.substring(6,8);
				var h=time.substring(8,10);
				var min=time.substring(10,12);
				var sen=time.substring(12,14);
				return y+"-"+m+"-"+d+" "+h+":"+min+":"+sen;
			}else if(time&&time.length==12){
				var y=time.substring(0,4);
				var m=time.substring(4,6);
				var d=time.substring(6,8);
				var h=time.substring(8,10);
				var min=time.substring(10,12);
				return y+"-"+m+"-"+d+" "+h+":"+min;
			}
	    	else{
				return "";
			}
	    }
	});

	mainApp.filter('toFixedFilter', function() {
	    return function(val) {
	    	if(val){
	    		return parseFloat(val).toFixed(2);
	    	}
	    }
	});


	mainApp.config(function($locationProvider) {
		$locationProvider.html5Mode(true).hashPrefix('!');
	});
	mainApp.controller('menuCtrl', ['$scope', '$http', '$interval', '$state', '$uibModal', 'ProfileService', function($scope, $http, $interval, $state, $modal, service) {
        //如果屏幕小于1030分辨率就隐藏左侧导航
        if ($(window).width() <= 1280) {
            if ($("body").hasClass('sidebar-collapse')) {
                $("body").removeClass('sidebar-collapse').trigger('expanded.pushMenu');
            } else {
                $("body").addClass('sidebar-collapse').trigger('collapsed.pushMenu');
            }
        } else {
            if ($("body").hasClass('sidebar-open')) {
                $("body").removeClass('sidebar-open').removeClass('sidebar-collapse').trigger('collapsed.pushMenu');
            } else {
                $("body").addClass('sidebar-open').trigger('expanded.pushMenu');
            }
        }



		var $ctrl = this;
		$ctrl.animationsEnabled = true;
		$scope.contentMask = '';

		$http.get("/menus").then(function (res) {
	        $scope.mainMenu= res.data;
	    });

		$scope.avatar = 'images/ypadminlogo_whitemin.png';
		service.getAvatar(function(data) {
			if(data.data) {
				$scope.avatar = data.data;
			}
		});

		$scope.openProfile = function() {
			$ctrl.items = [];
			$modal.open({
				animation: $ctrl.animationsEnabled,
				templateUrl: '/profile.html',
				controller: 'ProfileController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function() {
			    		return $ctrl.items;
			    	}
				}
			});
		};

		$scope.openPwd = function() {
			$ctrl.items = [];
			$modal.open({
				animation: $ctrl.animationsEnabled,
				templateUrl: '/profile/pwd.html',
				controller: 'ProfilePwdController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function() {
			    		return $ctrl.items;
			    	}
				}
			});
		};

		$scope.logout = function(){
			window.location.href = '/logout';
		};

		$scope.tabs = [
		    { 'id':'-1', 'name':'主页', 'active': true } //HOME
		   // { 'id':'-1', 'name':'主页','url':'/dashboard', 'code':'HOME', 'active': true } //HOME
		];


		$scope.openTab = function(url, name, id, code) {
            $scope.contentMask = 'content-mask';

            setTimeout(function() {
                $scope.$apply(function() {
                    $scope.contentMask = '';
                });
            }, 300);

			if(isHas(id)) {
				return;
			};


			var obj = {'id':id, 'name':name, 'url':url, 'code':code, 'active': true};

			$.each($scope.tabs, function(indx, obj) {
				obj.active = false;
			});

            $scope.tabs.push(obj);

            $('#scrollbar').animate({scrollLeft: '+=1000px'}, 100);
		};

		$scope.activeTab = function(id) {
            console.log($state);

			$.each($scope.tabs, function(indx, obj) {
				if(id == obj.id) {
                    $scope.contentMask = 'content-mask';
                    obj.active = true;

                    setTimeout(function() {
                        $scope.$apply(function() {
                            $scope.contentMask = '';
						});
					}, 300);
				} else {
					obj.active = false;
				}
			});
		};

		$scope.closeTab = function(id) {
            $scope.contentMask = 'content-mask';
			var indx = null;

			$.each($scope.tabs, function(index, obj) {
                obj.active = false;

				if(id == obj.id) {
					indx = index;
				}
			});

			if(indx != null) {
				$scope.tabs.splice(indx, 1);
				$scope.tabs[$scope.tabs.length -1].active = true;
			}

            setTimeout(function() {
                $scope.$apply(function() {
                    $scope.contentMask = '';
                });
            }, 300);
		};

		$scope.forward = function() {
            $('#scrollbar').animate({scrollLeft: '-=200px'}, 100);
		}

		$scope.backoff = function() {
            $('#scrollbar').animate({scrollLeft: '+=200px'}, 100);
		}

        var isHas = function(id) {
			var has = false;
			$.each($scope.tabs, function(indx, obj) {
				if(id == obj.id) {
					has = true;
					obj.active = true;
					return;
				}
			});

			if(has) {
				$.each($scope.tabs, function(indx, obj) {
					if(id != obj.id) {
						obj.active = false;
						return;
					}
				});
			}

			return has;
		};
	}]);

	mainApp.controller('ProfileController', [ '$scope', '$http', '$uibModal', '$uibModalInstance', 'ProfileService', 'items', function($scope, $http, $modal, $uibModalInstance, service, items) {
		var $ctrl = this;

		$scope.avatar = 'images/ypadminlogo_whitemin.png';

		service.getAvatar(function(data) {
			if(data.data) {
				$scope.avatar = data.data;
			}
		});

		var showImage = function(data) {
			if(data == null) {
				$scope.avatar = 'images/ypadminlogo_whitemin.png';
			} else {
				$scope.avatar = data;
				service.saveAvatar(data);
			}
		};

		$scope.openImage = function() {
			$ctrl.items = [showImage];
			$modal.open({
				animation: true,
				templateUrl: '/profile/avatar.html',
				controller: 'ProfileAvatarController',
				controllerAs: '$ctrl',
				resolve: {
			    	items: function() {
			    		return $ctrl.items;
			    	}
				}
			});
		};

		$scope.cancel = function() {
			$uibModalInstance.dismiss();
		};

		$scope.update = function() {
			var data = {'loginName': $scope.loginName, 'name' : $scope.name, 'email' : $scope.email, 'mobile': $scope.mobile};
			service.update(data, function() {
				toastr["success"]("更新成功");
			});
		};

		service.get(function(data) {
			$scope.loginName = data.loginName;
			$scope.name = data.name;
			$scope.email = data.email;
			$scope.mobile = data.mobile;
		});

	}]);

	mainApp.controller('ProfileAvatarController', [ '$scope', '$http', '$uibModal', '$uibModalInstance', 'ProfileService', 'items', function($scope, $http, $modal, $uibModalInstance, service, items) {
		var $ctrl = this;

		$scope.cancel = function() {
			$uibModalInstance.dismiss();
		};

		$scope.myImage='';
		$scope.myCroppedImage='';

		var handleFileSelect=function(evt) {
			var file=evt.currentTarget.files[0];
			var reader = new FileReader();
			reader.onload = function (evt) {
		    	$scope.$apply(function($scope){
		    		$scope.myImage=evt.target.result;
		    	});
			};
			reader.readAsDataURL(file);
		};

		$scope.myCropperInit = function() {
			angular.element(document.querySelector('#fileInput')).on('change',handleFileSelect);
		};

		$scope.addAvatar = function() {
			if(document.querySelector('#fileInput').value == '') {
				toastr["warning"]("未选择头像");
				items[0](null);
			} else {
				items[0]($scope.myCroppedImage);
			}

			$uibModalInstance.dismiss();
		};
	}]);

	mainApp.controller('ProfilePwdController', [ '$scope', '$http', '$uibModalInstance', 'ProfileService', 'items', function($scope, $http, $uibModalInstance, service, items) {
		$scope.cancel = function() {
			$uibModalInstance.dismiss();
		};

		$scope.update = function() {

			var data = {'password': $scope.password ,'newPassword':$scope.newPassword, 'confirmPassword' : $scope.confirmPwd};
			console.log(data);
			service.updatePassword(data, function(){$uibModalInstance.close();});
		};
	}]);

	mainApp.factory('ProfileService', ['$q', '$http', function ($q, $http) {
		var get = function(callbackFn) {
			$http.get('/profile')
			.success(function (response) {
				callbackFn(response);
			});
		};

		var getAvatar = function(callbackFn) {
			$http.get('/profile/avatar')
			.success(function (response) {
				if(callbackFn) {
					callbackFn(response);
				}
			});
		};

		var saveAvatar = function(data) {
			$http.post('/profile/avatar', {'data': data})
			.success(function (response) {
				toastr["success"]("头像保存成功");
			});
		};

		var update = function(data, callbackFn) {
			$http.put('/profile', angular.toJson(data))
			.then(
    			function successCallback(response) {
    				callbackFn();
    			});
		};

		var updatePassword = function(data, callbackFun) {
			$http.put('/profile/password', angular.toJson(data))
			.then(
                function successCallback(response) {
                    toastr["success"]("密码更新成功");
                    callbackFun();
                    //删除提示标签
                    if(document.querySelector('.tooltip')){
                        document.body.removeChild(document.querySelector('.tooltip'));
                    }
                });
		};

		return {
			get : get,
			getAvatar : getAvatar,
			saveAvatar : saveAvatar,
			update : update,
			updatePassword : updatePassword
		}
	}]);

	mainApp.directive('hasPerm', ['$animate','PermService', function($animate, service){
		return {
			restrict: 'A',
		    multiElement: true,
		    link: function(scope, element, attr) {

				var callbackFn = function(data) {
					if(data == null || '' == data) {
						return;
					}

					BUTTONS_CODES = data;

					hasPermFn();
				}

				var hasPermFn = function() {
					var isContent = false;
					$.each($(BUTTONS_CODES), function(index, obj) {
						if(obj == attr.hasPerm) {
							isContent = true;
						}
					});

					$animate[isContent ? 'removeClass' : 'addClass'](element, 'ng-hide', {
				    	tempClasses: 'ng-hide-animate'
				 	});
				}

				if(BUTTONS_CODES == null) {
					service.getPermission(callbackFn);
				} else {
					hasPermFn();
				}
		    }
		}
	}]);

	mainApp.factory('PermService', ['$q', '$http', function ($q, $http) {
		var getPermission = function(callbackFn) {
			var url = '/permission/buttons/codes';
			$http.get(url)
			.success(function (response) {
				if(!callbackFn) {
					return;
				}
				callbackFn(response);
			});
		}

		return {
			getPermission : getPermission
		}
	}]);

	mainApp.factory('myInterceptor', ['$q', '$injector', '$interval',function($q, $injector, $interval) {
		return {
			'request': function(config) {
				return config;
			},
			'requestError': function(err) {
				return $q.reject(err);
			},
			'response': function(resp) {
				if (typeof resp.data === 'string' && resp.data.indexOf("<!DOCTYPE html>")>-1){
					toastr["warning"]("会话过期，请重新登录");

					$interval(function() {
						window.location.href = '/login';
					}, 1000, 1);

				}

				return resp;
			},
			'responseError': function(rejection) {
				var msg = rejection.data.message;
				if(msg == null || '' == msg) {
					msg='操作异常，请联系管理员';
				}
				var status = rejection.status;
				switch(status) {
				case 400:
					break;
				case 401:
					break;
				case 403:
					break;
				case 404:
					break;
				case 500:
					break;
				}

				toastr["error"](msg);
				return $q.reject(rejection);
			}
		};
	}]);

	mainApp.config(['$httpProvider', function($httpProvider) {
		 $httpProvider.interceptors.push('myInterceptor');
	}]);

	mainApp.config(['$logProvider', '$stateProvider', '$urlRouterProvider', function($logProvider, $stateProvider, $urlRouterProvider) {
		 $logProvider.debugEnabled(true);

		//解决路由异常的办法在这里
	   // $urlRouteProvider.otherwise('/');

		/* $stateProvider.state('manager',{
		     	url: '/:name',
		     	views : {
					'content@':{
				    	templateUrl: '/users.html',
				        controller:"UsersController",
				        controllerAs:"UsersCtrl",
				        onEnter: function($stateParams){

				        },
				        onExit: function(){

				        },
				        resolve:{
				        	deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad){
				        		return $ocLazyLoad.load("js/users/list.js");
				            }]
				        },
				        sidebarMeta: {
				        	order: 800
				        }
					}
		        }
			 });*/

		 //测试通过的静态路由配置
		 $stateProvider.state('manager',{
		     url:'/',
			 views : {
				 'HOME':{
						templateUrl: '/dashboard.html',
					 	controller:"DashboardController",
					 	controllerAs:"DashboardCtrl",
						resolve:{
					     	deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad){
					     		return $ocLazyLoad.load("js/dashboard/list.js");
					     	}]
						},
						sidebarMeta: {
					    	order: 800
						}
				 },
                 /*--------------------------------------------------------------
				  | 系统管理
				  |--------------------------------------------------------------
				 */
                 //权限管理
                 'PERMISSION':{
                     templateUrl: '/permission.html',
                     controller: 'PermissionController',
                     controllerAs: 'PermissionCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/permission/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },
				 //角色管理
                 'ROLES':{
                     templateUrl: '/roles.html',
                     controller: 'RolesController',
                     controllerAs: 'RolesCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/roles/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },
				 //系统用户管理
                 'USERS':{
                     templateUrl: '/users.html',
                     controller: 'UsersController',
                     controllerAs: 'UsersCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/users/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },
				 //部门管理
                 'DEPARTMENT':{
                     templateUrl: '/department.html',
                     controller: 'DepartmentController',
                     controllerAs: 'DepartmentCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/department/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },
               //商品管理
                 'COMMODITY':{
                     templateUrl: '/commodity.html',
                     controller: 'CommodityController',
                     controllerAs: 'CommodityCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/commodity/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },
                 //商品发布
                 'PUBLISH':{
                     templateUrl: '/publish.html',
                     controller: 'PublishController',
                     controllerAs: 'PublishCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/publish/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },
                 
                 //用户管理
                 'USERINFO':{
                     templateUrl: '/userinfo.html',
                     controller: 'UserInfoController',
                     controllerAs: 'UserinfoCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/userinfo/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },
                 'ORDER':{
                     templateUrl: '/order.html',
                     controller: 'OrderController',
                     controllerAs: 'orderCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/order/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 },'RECOMMEND':{
                     templateUrl: '/recommend.html',
                     controller: 'RecommendController',
                     controllerAs: 'recommendCtrl',
                     resolve:{
                         deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad) {
                             return $ocLazyLoad.load('js/recommend/list.js');
                         }]
                     },
                     sidebarMeta: {
                         order: 800
                     }
                 }
		 	
                 
			 }
		 });
	}]);

	toastr.options = {
			"closeButton": true,
			"debug": false,
			"newestOnTop": false,
			"progressBar": false,
			"positionClass": "toast-top-right",
			"preventDuplicates": false,
			"onclick": null,
			"showDuration": "300",
			"hideDuration": "1000",
			"timeOut": "5000",
			"extendedTimeOut": "1000",
			"showEasing": "swing",
			"hideEasing": "linear",
			"showMethod": "fadeIn",
			"hideMethod": "fadeOut"
	};

})(window.angular);

