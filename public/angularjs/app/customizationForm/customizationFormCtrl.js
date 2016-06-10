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

		   $scope.initFunction = function(){
			   $http.get('/getCustomizationform/'+'Create Lead').success(function(response) {
					
					$scope.setjson = response;
					$scope.setjson.jsonData = angular.fromJson(response.jsonData);
					$scope.initComponent = angular.fromJson(response.jsonData);
					//$scope.initComponent = [{"id":0,"component":"textInput","editable":true,"index":0,"label":"Text Input","description":"","key":"","imageUrl":"","placeholder":"placeholder","options":[],"columnOptions":[],"equation":"","required":false,"isRepeatable":false,"validation":"/.*/","logic":{"action":"Hide"},"pointRules":[],"column":[]}];
					console.log($scope.setjson);
					//$builder.forms['default'] = $scope.setjson.jsonData;
					//console.log($builder);
			});
		   }
	  
	  
		/*------------------Form Builder ---------------------*/
		   $scope.setjson = {};
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
		     console.log($scope.FB, "FBuilder", $builder.forms); 
		    count++; 
		    };
		    

		    $scope.form = $builder.forms['default'];
		       $scope.$watch('form', function() {
		         console.log('$scope.form',$scope.form);
		         console.log('$builder.forms',$builder.forms);
		       }, true);
		       
		       
		  $scope.saveCreateLeadForm = function(){
			 
			  $http.post('/getLeadCrateForm', $scope.form)
				 .success(function(data) {
					 $.pnotify({
	    				    title: "Success",
	    				    type:'success',
	    				    text: "Form Created successfully",
	    				});
					});
		  }     
		  $scope.editLeadInfo = function(title){
			   $scope.setjson.showFild = title;
			  $('#edititle').modal('show');

		  }
		  $scope.editAllTitle = function(){
			  console.log($scope.setjson);
			  $http.post('/getLeadCrateFormTitle', $scope.setjson)
				 .success(function(data) {
					 $.pnotify({
	    				    title: "Success",
	    				    type:'success',
	    				    text: "Title Edit successfully",
	    				});
					 $scope.initFunction();
						$('#edititle').modal('hide');
					});
			  
		  }
		 
		   
  }]);
