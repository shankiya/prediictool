var myApp = angular.module('prediictool', ['ui.router','ngCookies','ngMaterial','md.data.table','ngMessages','ngTouch','ui.grid.pagination',
    'ui.grid.selection',
    'ui.grid.cellNav',
    'ui.grid.expandable',
    'ui.grid.edit',
    'ui.grid.rowEdit',
    'ui.grid.saveState',
    'ui.grid.resizeColumns',
    'ui.grid.pinning',
    'ui.grid.moveColumns',
    'ui.grid.exporter',
    'ui.grid.infiniteScroll',
    'ui.grid.importer',
    'ui.grid.grouping',
    'darthwade.dwLoading',
    'chartDirective']);

myApp.config(function($stateProvider,$urlRouterProvider,$locationProvider) {
	
	$urlRouterProvider.otherwise('/login');
	
	 $stateProvider
     .state('login', {
         url:  '/login',
         controller: 'loginController',
         templateUrl: 'app/views/login.html'
     })
     
     .state('home', {
         url:  '/home',
         controller: 'homeController',
         templateUrl: 'app/views/home.html'
     })
     
     .state('register', {
         url:  '/register',
         controller: 'registerController',
         templateUrl: 'app/views/registerUser.html'
     })
     
     .state('forgetPassword', {
         url:  '/forgetPassword',
         controller: 'passwordManagementController',
         templateUrl: 'app/views/forgetPassword.html'
     })
});
