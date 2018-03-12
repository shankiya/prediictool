myApp.service('emailService', ['$http', function($http) {
	
	console.log("emailService");
	
	this.sendEmailService = function (email) {
		  return $http.post('/email/emailSend',email).then(function(response){
              //console.log("email...."+JSON.stringify(response));
              return response.data;
          });
	};
	
}]);

