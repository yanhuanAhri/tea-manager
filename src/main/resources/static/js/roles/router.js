/**
 * 
 */

	
	mainApp.config(['$logProvider', '$stateProvider', '$urlRouterProvider', function($logProvider, $stateProvider, $urlRouterProvider) {
		 $logProvider.debugEnabled(true);
		 
		 $stateProvider.state('/roles',{
		        url: '/roles',
			    views : {
					'ROLES':{
			        	templateUrl: '/roles.html',
				        controller:"RolesController",
				        controllerAs:"RolesCtrl",
				        onEnter: function(){
				           
				        },
				        onExit: function(){
				        	
				        },
				        resolve:{
				            deps:['$stateParams', '$ocLazyLoad',function($stateParams, $ocLazyLoad){
				                return $ocLazyLoad.load("js/roles/list.js");
				            }]
				        },
				        sidebarMeta: {
				        	order: 800
				        }
			        }
		        }
		    });
		 
	}]);

