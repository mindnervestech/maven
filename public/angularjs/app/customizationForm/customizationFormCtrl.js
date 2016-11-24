'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */

angular.module('newApp')
  .controller('customizationFormCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http','$compile','$interval','$filter','$location','$timeout','$route','$q','$upload','$builder','$routeParams','apiserviceCustomizationForm',function ($scope, dashboardService, pluginsService,$http,$compile,$interval,$filter,$location,$timeout,$route,$q,$upload,$builder,$routeParams,apiserviceCustomizationForm) {

	  	$scope.showSaveButton = $routeParams.pageType;
	  	$scope.formType = $routeParams.formType;
	  	console.log($routeParams.formType);
	  	
	  if($routeParams.formType == "Request Appointment"){
		  $scope.appointFlag=1;
		  
	  }
	  
		   $scope.initFunction = function(){
			   apiserviceCustomizationForm.getCustomizationform($routeParams.formType).then(function(response){
				   $scope.outcome = [];
					console.log(response);
					/*var arr = [];
		  			   arr = response.outcome.split('-');
		  			   for(var i=0;i<arr.length;i++){
		  				 $scope.outcome.push(arr[i]);
		  			   }*/
					//$scope.outcome = "["+response.outcome+"]";
					//$scope.outcome = angular.fromJson("["+response.outcome+"]");
				//	console.log($scope.outcome);
					if(response == 0){
						$scope.setjson = null;
						$scope.setjson.jsonData = null;
						$scope.initComponent = null;
					}else{
						$scope.setjson = response;
						$scope.setjson.jsonData = angular.fromJson(response.jsonData);
						$scope.initComponent = angular.fromJson(response.jsonData);
					}
					
			});
			   apiserviceCustomizationForm.getAllSites().then(function(data){
				   $scope.siteList = data;
		 		});
			   
			   apiserviceCustomizationForm.getAlloutcome().then(function(data){
				   $scope.outcomemenu = data;
				   console.log($scope.outcomemenu);
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
		    count++; 
		    };
		    

		    $scope.form = $builder.forms['default'];
		       $scope.$watch('form', function() {
		         console.log('$scope.form',$scope.form);
		         console.log('$builder.forms',$builder.forms);
		       }, true);
		       
		       $scope.editform = {};
		      // $scope.showOutcomeMsg = 0;
		  $scope.saveCreateLeadForm = function(){
			//  if($scope.outcome != undefined){
				  $scope.showOutcomeMsg = 0;
				  var obj = localStorage.getItem('popupType');
				  $scope.leadId = localStorage.getItem('leadId');
				angular.forEach($builder.forms['default'], function(value, key) {
					 var key;
	               		key = value.label;
	               		key = key.replace("  ","_");
	               		key = key.replace(" ","_");
	               		key = key.toLowerCase();
	               		value.key = key;
	               		console.log(key);
					 console.log(value.key);
				 });
				 console.log($builder.forms['default']);
				 $scope.editform.formType = $routeParams.formType;
				 $scope.editform.jsonform = $builder.forms['default'];
				 $scope.editform.outcome = $scope.outcome;
				 console.log($scope.editform);
				 apiserviceCustomizationForm.getLeadCrateForm($scope.editform).then(function(data){
				  
					 apiserviceCustomizationForm.getCustomizationform($routeParams.formType).then(function(response){
						 $scope.josnData1 = angular.fromJson(response.jsonData);
						 console.log($scope.josnData1);
						 $scope.formListData=[];
						 		angular.forEach($scope.josnData1, function(value1, key) {
						 			$scope.formListData.push({
			    		   	  			value:$routeParams.formType,
			    		   	  			key:value1.key,
			    		   	  			savecrm:value1.savecrm,
			    		   	  			displayGrid:value1.displayGrid,
			    		   	  		    displayWebsite:value1.displayWebsite,
			    		   	  			
			    					});
						 			console.log($scope.formListData);
						 		});
						 		$scope.objBind = {};
						 		$scope.objBind.customDataAll = $scope.formListData; 
						 		apiserviceCustomizationForm.getFormBuilderData($scope.objBind).then(function(data){
									 console.log(data);
								 });
					 });
					 
				});
				 
				 
				 if(obj == "Lead"){
					 $scope.getLeadTypeDataById($scope.leadId);
				 }
			 // }else{
				//  $scope.showOutcomeMsg = 1;
		//	  }
			 
		  }  
		  
		  $scope.saveCreateLeadFormOnly = function(){
			  $scope.showOutcomeMsg = 0;
			  var obj = localStorage.getItem('popupType');
			  $scope.leadId = localStorage.getItem('leadId');
			angular.forEach($builder.forms['default'], function(value, key) {
				 var key;
               		key = value.label;
               		key = key.replace("  ","_");
               		key = key.replace(" ","_");
               		key = key.toLowerCase();
               		value.key = key;
               		console.log(key);
				 console.log(value.key);
			 });
			 console.log($builder.forms['default']);
			 $scope.editform.formType = $routeParams.formType;
			 $scope.editform.jsonform = $builder.forms['default'];
			 $scope.editform.outcome = $scope.outcome;
			 console.log($scope.editform);
			 apiserviceCustomizationForm.getLeadCrateForm($scope.editform).then(function(data){
			  
					});
		  }
		  
		  $scope.saveCreateLeadFormAddOuctcome = function(){
			  var obj = localStorage.getItem('popupType');
			  $scope.leadId = localStorage.getItem('leadId');
			  if(obj == "Lead"){
					 $scope.getLeadTypeDataById($scope.leadId);
				 }
		  }
		  
		  $scope.getLeadTypeDataById = function(leadId){
			  console.log(leadId);
			  apiserviceCustomizationForm.getLeadTypeDataById(leadId).then(function(data){
				  console.log(data);
				  $scope.callAction = data;
				  console.log($scope.callAction);
				  console.log(data.callToAction);
				  if(data.callToAction == true){
					  $scope.callToActonP = "1";
				  }else{
					  $scope.callToActonP = "0";
				  }
					  $('#completedPopup').modal('show');
				});
		  }
		  apiserviceCustomizationForm.getInternalPdfData().then(function(data){
				$scope.internalPdfList=data;
				console.log($scope.internalPdfList);
			});
		  var logofile;
		  $scope.onFileSelect = function($file){
			  console.log($file[0]);
			  logofile = $file;
			  $upload.upload({
		 	         url : '/saveInternalPdf',
		 	         method: 'POST',
		 	         file:logofile,
		 	      }).success(function(data) {
		 	  			$.pnotify({
		 	  			    title: "Success",
		 	  			    type:'success',
		 	  			    text: "pdf saved successfully",
		 	  			});
		 	      }); 
			  apiserviceCustomizationForm.getInternalPdfData().then(function(data){
	  				$scope.internalPdfList=data;
	  				console.log($scope.internalPdfList);
	  			});
		  }
		  
		  $scope.deleteInternalPdf = function(id) {
			  apiserviceCustomizationForm.getInternalPdfDataById(id).then(function(data){
					$scope.internalPdfName=data;
	  			});
				$('#btndeleteInternalPdf').click();
				$scope.internalPdfId = id;
			}
		  
		  $scope.deletePdfInternal = function() {
			  apiserviceCustomizationForm.deleteInternalPdf($scope.internalPdfId).then(function(data){
        	  console.log("$scope.internalPdfId"+$scope.internalPdfId);
        	  $scope.internalPdfList ={};
			});
		}
		  
		  
		  apiserviceCustomizationForm.getCustomerPdfData().then(function(data){
				
				$scope.customerPdfList=data;
				
			});
		  
		  var logofile1;
			$scope.onCustomerFileSelect = function ($files) {
				logofile1 = $files;
				
			}
			
			$scope.saveDoc = false;
			$scope.checkSaveDoc = function(savedoc){
					console.log(savedoc);
					if(savedoc == false){
						$upload.upload({
				 	         url : '/saveCustomerPdf',
				 	         method: 'POST',
				 	         file:logofile1,
				 	      }).success(function(data) {
				 	  			$.pnotify({
				 	  			    title: "Success",
				 	  			    type:'success',
				 	  			    text: "pdf saved successfully",
				 	  			});
				 	  			apiserviceCustomizationForm.getCustomerPdfData().then(function(data){
				 	  				$scope.customerPdfList=data;
				 	  			});		
				 	  			
					
				 	      });
					}
			}
		  
		  $scope.callActionId ={};
		 $scope.callToAction = function(){
			 
			 console.log($scope.leadId);
			 $scope.callAction.id = $scope.leadId;
			 console.log($scope.callAction);
			 $scope.callAction.actionOutcomes = $scope.callactions.toString(); 
			 $scope.callAction.outcome = $scope.actions.toString(); 
			 if(logofile == undefined){
				 apiserviceCustomizationForm.saveLeadFormPopup($scope.callAction).then(function(data){
				 
				 });
			 }else if(logofile != undefined){
				 $upload.upload({
		            url : '/saveLeadFormPopup',
		            method: 'POST',
		            file:logofile,
		            data:$scope.callAction
		         }).success(function(data) {
		   			console.log(data);
		   			$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Your Progress has been Saved",
					});		   			
		   		 });
				}
			 $("#completedPopup").modal('hide');
			 $location.path('/configuration');
		 }
		  
		  $scope.editLeadInfo = function(title){
			   $scope.setjson.showFild = title;
			  $('#edititle').modal('show');

		  }
		  
		  $scope.actions = [];
		  $scope.actionClicked = function(e, rolePer,value){
				console.log(rolePer);
				console.log(value);
				if(value == false || value == undefined){
					$scope.actions.push(rolePer.id);
				}else{
					$scope.deleteItem(rolePer);
				}
				console.log($scope.actions);
			}
			$scope.deleteItem = function(rolePer){
				angular.forEach($scope.actions, function(obj, index){
					 if ((rolePer.id == obj)) {
						 $scope.actions.splice(index, 1);
				       	return;
				    };
				  });
			}
			
			$scope.callActionsList =[
			                    	{name:'New Customer Request',isSelected:false},
			                    	{name:'Generate PDF from the form',isSelected:false},
			                    	{name:'Client downloads PDF file',isSelected:false},
			                    	{name:'Automatically add to CRM',isSelected:false}];
			                    	
			
			 $scope.callactions = [];
			  $scope.checkClicked = function(e, actionPer,value){
					console.log(actionPer);
					console.log(value);
					if(value == false || value == undefined){
						console.log(actionPer.name);
						$scope.callactions.push(actionPer.name);
					}else{
						$scope.deleteItemCall(actionPer);
					}
					console.log($scope.callactions);
				}
			  $scope.deleteItemCall = function(actionPer){
					angular.forEach($scope.callactions, function(obj, index){
						 if ((actionPer.name == obj)) {
							 $scope.callactions.splice(index, 1);
					       	return;
					    };
					  });
				}	
			
			
		  
		  $scope.editAllTitle = function(){
			 
			  delete $scope.setjson.locations;
			  delete $scope.setjson.showFild;
			  delete $scope.setjson.jsonData;
			  $scope.setjson.formType = $routeParams.formType;
			  apiserviceCustomizationForm.getLeadCrateFormTitle($scope.setjson).then(function(data){

				  		$scope.initFunction();
						$('#edititle').modal('hide');
					});
			  
		  }
		  
		
		 
		   
  }]);
