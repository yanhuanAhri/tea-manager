/**
 * 
 */
(function() {
	console.log("--------user route-------1-----")
	angular.module('menuApp', ['ngMessages','ui.router','oc.lazyLoad'])
	.config(['$logProvider', '$stateProvider', '$urlRouterProvider', function($logProvider, $stateProvider, $urlRouterProvider) {
		 $logProvider.debugEnabled(true);
		 console.log("--------user route------------")
		 $stateProvider.state('/users',{
	            url: '/users',
	            views : {
					'USERS':{
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
	        });
		 
	}]);
})();
	
	

