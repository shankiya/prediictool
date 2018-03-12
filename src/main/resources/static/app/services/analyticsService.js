myApp.service('analyticsService', [
		'$http',
		function($http) {
			console.log("analyticsService");
			
			this.analyticsTabService = function(finalRecord) {
				return $http.post('/analytics/prediction/', finalRecord).then(
						function(response) {
							return response.data;
						});
			};
			
		} ]);