myApp.controller('passwordManagementController', ['$scope','emailService', function($scope,emailService) {
	  console.log("password Controller");
	  $scope.password = {};
	  $scope.passwordFunction= function(){
		  //alert(JSON.stringify($scope.password));
		  //alert(JSON.stringify($scope.password));
		  emailService.sendEmailService($scope.password);
	  }
	  
}]);
