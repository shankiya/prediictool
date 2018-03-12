myApp.controller('loginController', ['$scope','loginService','commonService','$location','$loading','$cookies', function($scope,loginService,commonService,$location,$loading,$cookies) {
	
	  console.log("login Controller");
	  $scope.user = {};
	  $scope.login= function(){
		  loginService.getLoginDetails($scope.user).then(function (response) {
			  $loading.start("nissan-spinner");
			    $scope.loginUser = response;
			    //console.log("loginDetails"+JSON.stringify($scope.loginUser));
			    if($scope.loginUser !=null && $scope.loginUser !=undefined && $scope.loginUser !=""){
			    	//commonService.setData('userDetails',$scope.loginUser);
			    	commonService.setData('userName',$scope.loginUser.userName);
			    	$cookies.put('userName',$scope.loginUser.userName);
			    	 $location.path('/home');
			    }else{
			    	$scope.loginUser = {};
			    	$scope.errorMsg = 'Invaid UserName and Password';
			    }
			    $loading.finish("nissan-spinner");
			});
	  }
	  
	  $scope.action = function(key){

		  if(key == 13){
			  if($scope.user == undefined || $scope.user ==null || $scope.user.userName == undefined 
					  || $scope.user.userName ==null  || $scope.user.userName =="" || 
					  $scope.user.password == undefined 
					  || $scope.user.password == null || $scope.user.password =="" ){
				  
				  $scope.errorMsg = 'Please enter valid UserName and Password';
			  }else{
				  loginService.getLoginDetails($scope.user).then(function (response) {
					    $scope.loginUser = response;
					    //console.log("loginDetails"+JSON.stringify($scope.loginUser));
					    if($scope.loginUser !=null && $scope.loginUser !=undefined && $scope.loginUser !=""){
					    	//commonService.setData('userDetails',$scope.loginUser);
					    	commonService.setData('userName',$scope.loginUser.userName);
					    	$cookies.put('userName',$scope.loginUser.userName);
					    	 $location.path('/home');
					    }else{
					    	$scope.loginUser = {};
					    	$scope.errorMsg = 'Invaid UserName and Password';
					    }
					});
			  }
		  }
		  
	  }
	  
	  $scope.myInterval = 3000;
	  $scope.slides = [
	    {
	      image: 'http://lorempixel.com/400/200/'
	    },
	    {
	      image: 'http://lorempixel.com/400/200/food'
	    },
	    {
	      image: 'http://lorempixel.com/400/200/sports'
	    },
	    {
	      image: 'http://lorempixel.com/400/200/people'
	    }
	  ];
}]);