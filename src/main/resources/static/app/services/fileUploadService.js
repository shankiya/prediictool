myApp.service('fileUploadService', [ '$http', function($http) {
	console.log("fileUploadService");
	this.uploadFileToUrl = function(file, uploadUrl) {
		var fd = new FormData();
		fd.append('file', file);
		
		return $http.post('/file/upload/', fd, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		}).then(function(response) {
			// console.log("userRegisterService...."+JSON.stringify(response));
			return response.data;
		});
	};
	
} ]);