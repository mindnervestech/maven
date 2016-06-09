'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */

angular.module('newApp')
  .controller('customizationFormCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http','$compile','$interval','$filter','$location','$timeout','$route','$q','$upload','$builder', '$validator',function ($scope, dashboardService, pluginsService,$http,$compile,$interval,$filter,$location,$timeout,$route,$q,$upload,$builder, $validator) {

		   
		/*------------------Form Builder ---------------------*/
		   
		   	$scope.initComponent = [];
			var vm = this;
			$scope.fields = [];
			$scope.component = [];
		  $scope.formName;
		  $scope.subSectonsName = 'default';
		 // console.log('$stateParams.myParam',$stateParams.myParam);

		   	$scope.options = {
				formState: {
			    	awesomeIsForced: false,
			  	}
			};
		  $scope.initComponent = [];
		  $scope.FB = [];
		  var FBuilder = {
		    'initComponent': $scope.initComponent,
		    'name':'default'
		  };
		    var count = 0;  
		    $scope.FB.push(FBuilder);
		 
		 console.log($builder, "$builder");
		    $scope.addSubsection = function() {
		    var formName = []
		    $builder.forms['default'+count]= formName;
		    var FBuilder = {
		        'initComponent': $scope.initComponent,
		        'name' : 'default'+count
		    };
		    $scope.subSectonsName = FBuilder.name;
		    $scope.FB.push(FBuilder);
		    // console.log($scope.FB, "FBuilder", $builder.forms); 
		    count++; 
		    };
		    

		    $scope.form = $builder.forms['default'];
		       $scope.$watch('form', function() {
		         console.log('$scope.form',$scope.form);
		         console.log('$builder.forms',$builder.forms);
		       }, true);
		       
		  $scope.saveCreateLeadForm = function(){
			  $http.post('/getLeadCrateForm',$scope.form)
				 .success(function(data) {
 						console.log("jhjjh");
					});
		  }     
		 
		   
  }]);
