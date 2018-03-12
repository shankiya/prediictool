myApp.service('userRegisterService', ['$http', function($http) {
	console.log("userRegisterService");
	this.registerUser = function (user) {
		  return $http.post('/login/registerUser/',user).then(function(response){
              //console.log("userRegisterService...."+JSON.stringify(response));
              return response.data;
          });
	};
	
}]);

