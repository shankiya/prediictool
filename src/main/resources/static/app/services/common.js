myApp.service('commonService', [ function() {
		
	var input = {};
	
		this.setData = function (key,vaue) {
			input[key]= vaue; 
		};
		
		this.getData = function (key) {
			return input[key]; 
		};
}]);

