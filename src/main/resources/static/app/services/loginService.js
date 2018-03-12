myApp.service('loginService', ['$http', function($http) {
		this.getLoginDetails = function (user) {
			 return $http.post('/login/userDetails/',user).then(function(response){
	              //console.log("userDetails...."+JSON.stringify(response));
	              return response.data;
	          });
		};
}]);

