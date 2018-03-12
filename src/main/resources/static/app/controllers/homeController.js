myApp.controller('homeController', ['$scope','loginService','fileUploadService','analyticsService','commonService','$mdPanel','$http','$mdSidenav','uiGridConstants','uiGridExporterService', 'uiGridExporterConstants','$loading','$location','$cookies', function($scope,loginService,fileUploadService,analyticsService,commonService,$mdPanel,$http,$mdSidenav,uiGridConstants,uiGridExporterService,uiGridExporterConstants,$loading,$location,$cookies) {
	  console.log("home Controller");
	  //$scope.currentNavItem = 'page1';
	  $scope.register = {};
	  $scope.lockLeft = true;
	  $scope.isSidenavOpen = false;
	  $scope.selectedIndex = 0;
	  $scope.isUploadTabDisabled = false;
	  $scope.isanalyticsTabDisabled = true;
  	  $scope.isdashboardTabDisabled = true;
  	  $scope.analytics = {};
  	  $scope.data=[];
  	$scope.analyticsdata=[];
  	  $scope.gridHeader=[];
  	$scope.chartData=[];
  	  
  	$scope.gridHeaderData=[];
  	  
      $scope.uploadFile= function(){
    	  $loading.start("nissan-spinner");
    	  var file = $scope.myFile;
    	  var uploadUrl = "/fileUpload";
    	  
    	  fileUploadService.uploadFileToUrl(file, uploadUrl).then(function (response) {
    		  $loading.finish("nissan-spinner");
    		  //alert("response"+JSON.stringify(response))
    		  $scope.gridOptions.data=[];
    		  $scope.gridHeaderData=[];
    		  $scope.gridOptions.data = response;
    		  //alert(JSON.stringify($scope.gridOptions.data[0])+"....."+$scope.gridOptions.data[5]);
    		  $scope.data.push($scope.gridOptions.data[0]);
    		  
    		  //alert("length"+$scope.gridOptions.data.length);
    		  $scope.data.push($scope.gridOptions.data[$scope.gridOptions.data.length-1]);
    		  
    		  //alert("data"+JSON.stringify($scope.data))
    		  //alert(Object.keys($scope.gridOptions.data[0]).length);
    		  
    		  for(var i=0; i<Object.keys($scope.gridOptions.data[0]).length; i++){
    			  $scope.gridHeader.push({ field: Object.keys($scope.gridOptions.data[0])[i],enableFiltering: false});
    			  $scope.gridHeaderData.push(Object.keys($scope.gridOptions.data[0])[i]);
    		  }
    		  
    		  
    		  
    		  for(var i=0; i<$scope.gridHeaderData.length; i++){
    			  for(var j=0; j<=0; j++){
    				  var dataitem = {};
    				  dataitem['header'] = $scope.gridHeaderData[i];
    				  dataitem['startdata'] =  $scope.data[0][$scope.gridHeaderData[i]];
    				  dataitem['enddata'] =  $scope.data[1][$scope.gridHeaderData[i]];
    				  dataitem['disp'] = '';
    				  
    				  $scope.analyticsdata.push(dataitem);
    				  
    			  }
    		  }
    		  
    		  //alert("data"+JSON.stringify($scope.analyticsdata))
    		  //alert(JSON.stringify($scope.gridHeader));
    		  
    		  
			});
	  }
  	  
  	  
  	var userName = commonService.getData('userName')
  	if(userName == undefined || userName == null || userName == '' || userName == 'null' ){
  		userName = $cookies.get('userName');
  		if(userName == undefined || userName == null || userName == '' || userName == 'null'){
  			 $location.path('/login');
  		}
  	}
  	$scope.userName = userName;
  	
  	 $scope.logout = function(){
		  commonService.setData('userName',null);
		  $cookies.put('userName',null);
		  $location.path('/login');
	  }
  	 
  	 
	 $scope.analyticsTabNext = function(){
		 
		 $loading.start("nissan-spinner");
		 
		  $scope.analytics['datalist']=$scope.gridOptions.data;
		  $scope.analytics['analyticsdata']=$scope.analyticsdata;
		  
		  analyticsService.analyticsTabService($scope.analytics).then(function (response) {
			  
			 
			  
			  $loading.finish("nissan-spinner");
			  //alert("response"+JSON.stringify(response))
			  $scope.isUploadTabDisabled = true;
		    	$scope.isanalyticsTabDisabled = true;
		    	$scope.isdashboardTabDisabled = false;
		    	
		    	$scope.chartData=response.data;
		    	$scope.period=response.period
		  	  
		  	  $scope.basicAreaChart = {
		  	            chart: {
		  	                type: 'line',
		  	                animation: Highcharts.svg, // don't animate in old IE
		  	                marginRight: 10,
		  	            },
		  	            title: {
		  	                text: 'Live random data'
		  	            },
		  	            xAxis: {
		  	    	        categories: $scope.period
		  	    	    },
		  	    	    yAxis: {
		  	    	        title: {
		  	    	            text: 'Temperature (°C)'
		  	    	        }
		  	    	    },
		  	    	    plotOptions: {
		  	    	        line: {
		  	    	            dataLabels: {
		  	    	                enabled: true
		  	    	            },
		  	    	            enableMouseTracking: false
		  	    	        }
		  	    	    },
		  	            series:$scope.chartData 
		  	        }

		    	
			  
		  });
		  
	  }
  	
  	 this.startDate = new Date();
  	 this.endDate = new Date();
  	  
  	 /* $scope.analytics.history = {
  	      group1 : 'Monthly',
  	      group2 : 'Sales'
  	  };*/
  	  
  	$scope.analytics= {
    	      predictcolumn : 'Units',
    	      frequency : 'Monthly'
    	  };
	  
	  $scope.toggleLeft = buildToggler('left');

	    function buildToggler(componentId) {
	        return function() {
	          $mdSidenav(componentId).toggle();
	        };
	      }
	    
	 
	    
	  $scope.$watch('isSidenavOpen', function(isSidenavOpen) {
	     // alert('sidenav is ' + (isSidenavOpen ? 'open' : 'closed'));
	  });
	  
	  $scope.showMoreItems= function($event){
		  $scope.menu = {
			      name: 'settings',
			      items: [
			        'Settings',
			        'About',
			        'Logout'
			      ]
			    };
		  
		  this.menuTemplate = '' +
	        '<div class="menu-panel" md-whiteframe="4">' +
	        '  <div class="menu-content">' +
	        '    <div class="menu-item" ng-repeat="item in ctrl.items">' +
	        '      <button class="md-button">' +
	        '        <span>{{item}}</span>' +
	        '      </button>' +
	        '    </div>' +
	        '  </div>' +
	        '</div>';
		  
		  var template = this.menuTemplate;
		  
		  var position = $mdPanel.newPanelPosition()
          .relativeTo($event.srcElement)
          .addPanelPosition(
            $mdPanel.xPosition.ALIGN_START,
            $mdPanel.yPosition.BELOW
          );
		  
		  var config = {
			        id: 'toolbar_' + $scope.menu.name,
			        attachTo: angular.element(document.body),
			        controller: 'homeController',
			        controllerAs: 'ctrl',
			        template: template,
			        position: position,
			        clickOutsideToClose: true,
			        panelClass: 'menu-panel-container',
			        locals: {
			          items: $scope.menu.items
			        },
			        openFrom: $event,
			        focusOnOpen: false,
			        zIndex: 100,
			        propagateContainerEvents: true,
			        groupName: ['toolbar', 'menus']
			      };

	      $mdPanel.open(config);
	  }
	  
	  
	  $scope.changeTab = function(index){
		    $scope.selectedIndex = index;
		    $scope.isSidenavOpen = false;		    
		    if(index == 0){
		    	$scope.isUploadTabDisabled = false;
		    	$scope.isanalyticsTabDisabled = true;
		    	$scope.isdashboardTabDisabled = true;
		    }else if(index == 1){
		    	$scope.isUploadTabDisabled = true;
		    	$scope.isanalyticsTabDisabled = false;
		    	$scope.isdashboardTabDisabled = true;
		    }else if(index == 2){
		    	$scope.analyticsTabNext();
		    	/*$scope.isUploadTabDisabled = true;
		    	$scope.isanalyticsTabDisabled = true;
		    	$scope.isdashboardTabDisabled = false;*/
		    }
		    
		}
	  
	  $scope.gridOptions = {
			    enableFiltering: true,
			    showGridFooter:true,
			    showColumnFooter:false,
			    enableGridMenu: true,
			    enableSorting: false,
			    exporterCsvFilename: 'myFile.csv',
			    exporterPdfDefaultStyle: {fontSize: 9},
			    exporterPdfTableStyle: {margin: [30, 30, 30, 30]},
			    exporterPdfTableHeaderStyle: {fontSize: 10, bold: true, italics: true, color: 'red'},
			    exporterPdfHeader: { text: "My Header", style: 'headerStyle' },
			    exporterPdfFooter: function ( currentPage, pageCount ) {
			      return { text: currentPage.toString() + ' of ' + pageCount.toString(), style: 'footerStyle' };
			    },
			    exporterPdfCustomFormatter: function ( docDefinition ) {
			      docDefinition.styles.headerStyle = { fontSize: 22, bold: true };
			      docDefinition.styles.footerStyle = { fontSize: 10, bold: true };
			      return docDefinition;
			    },
			    exporterPdfOrientation: 'portrait',
			    exporterPdfPageSize: 'LETTER',
			    exporterPdfMaxGridWidth: 500,
			    exporterCsvLinkElement: angular.element(document.querySelectorAll(".custom-csv-link-location")),
			    onRegisterApi: function(gridApi){
			      $scope.gridApi = gridApi;
			    },    
			    columnDefs: $scope.gridHeader
			  };  
	  
	  	  
		  
	  $scope.states = ('Products Region Units Period Amounts').split(' ').map(function(state) {
			        return {abbrev: state};
			      });
	  
	  
}]).directive('fileModel', ['$parse', function ($parse) {
	
	//alert();
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
           var model = $parse(attrs.fileModel);
           var modelSetter = model.assign;
           
           element.bind('change', function(){
              scope.$apply(function(){
                 modelSetter(scope, element[0].files[0]);
              });
           });
        }
     };
  }]);