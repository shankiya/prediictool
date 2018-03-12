myApp.controller('registerController', [
		'$scope',
		'$location',
		'userRegisterService',
		function($scope, $location, userRegisterService) {
			
			console.log("register Controller");
			$scope.register = {};
			
			/*$scope.registerFunction = function() {
				
				alert(JSON.stringify($scope.register));
				userRegisterService.registerUser($scope.register).then(
						function(response) {
							alert("response" + response);
							var status = response;
							alert("status"+JSON.stringify(status))

							if (status != undefined && status != null
									&& status != "") {
								$location.path('/login');
							} else {
								
								$scope.register.userName="";
								$scope.register.emailId="";
								$scope.errormsg = "User already exist";
							}
						});
			}
*/
			
			
			$scope.registerFunction = function() {
				$scope.register.errorMsg = "";
				userRegisterService.registerUser($scope.register).then(
						function(response) {
							var status = response;
							if (status != undefined && status != null
									&& status != "" && status.userName != undefined && status.userName != null
									&& status.userName != "" && status.emailId != undefined && status.emailId != null
									&& status.emailId != "") {
								$location.path('/login');
							} else {
								$scope.register = status;
							}
						});
			}
			
			
		} ]);
		
		/*.directive('isUser', function(){
		    return {
		        require: 'ngModel',
		        link: function(scope, element, attr, mCtrl){
		            mCtrl.$parsers.push(function(value){
		            	
		            	alert("value"+value);
		            });
		        }
		    };
		})*/