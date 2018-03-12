//var chartDirective = function () {
//    return {
//        restrict: 'E',
//        replace: true,
//        template: '<div></div>',
//        scope: {
//            config: '='
//        },
//        link: function (scope, element, attrs) {
//            var chart;
//            var process = function () {
//                var defaultOptions = {
//                    chart: { renderTo: element[0] },
//                };
//                var config = angular.extend(defaultOptions, scope.config);
//                chart = new Highcharts.Chart(config);
//            };
//            process();
//            scope.$watch("config.series", function (loading) {
//                process();
//            });
//            scope.$watch("config.loading", function (loading) {
//                if (!chart) {
//                    return;
//                }
//                if (loading) {
//                    chart.showLoading();
//                } else {
//                    chart.hideLoading();
//                }
//            });
//        }
//    };
//};

angular.module('chartDirective',[])

.directive('chart', function () {
  return {
    restrict: 'E',
    template: '<div></div>',
    scope: {
        chartData: "=value"
    },
    transclude:true,
    replace: true,

    link: function (scope, element, attrs) {
      var chartsDefaults = {
        chart: {
          renderTo: element[0],
          type: attrs.type || null,
          height: attrs.height || null,
          width: attrs.width || null
        }
      };
      
        //Update when charts data changes
        scope.$watch(function() { return scope.chartData; }, function(value) {
          if(!value) return;
            // We need deep copy in order to NOT override original chart object.
            // This allows us to override chart data member and still the keep
            // our original renderTo will be the same
            var deepCopy = true;
            var newSettings = {};
            $.extend(deepCopy, newSettings, chartsDefaults, scope.chartData);
            var chart = new Highcharts.Chart(newSettings);
        });
      }
    };

});