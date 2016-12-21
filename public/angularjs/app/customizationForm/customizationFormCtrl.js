'use strict';

/**
 * @ngdoc function
 * @name newappApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the newappApp
 */

angular.module('newApp')
  .controller('customizationFormCtrl', ['$scope', 'dashboardService', 'pluginsService', '$http','$compile','$interval','$filter','$location','$timeout','$route','$q','$upload','$builder','$routeParams','apiserviceCustomizationForm','$window',function ($scope, dashboardService, pluginsService,$http,$compile,$interval,$filter,$location,$timeout,$route,$q,$upload,$builder,$routeParams,apiserviceCustomizationForm,$window) {

	  	$scope.showSaveButton = $routeParams.pageType;
	  	$scope.formType = $routeParams.formType;
	  	localStorage.setItem('formType',$routeParams.formType);
	  	console.log($routeParams.formType);
	  	$scope.loactionVal = localStorage.getItem('popupType');
	  	
	  	$scope.showDefault = localStorage.getItem('callToAction');
	  if($routeParams.formType == "Request Appointment"){
		  $scope.appointFlag=1;
		  
	  }
	  
		   $scope.initFunction = function(){
			   apiserviceCustomizationForm.getCustomizationform($routeParams.formType).then(function(response){
				   $scope.outcome = [];
					console.log(response);
				//	$builder.forms['default'] = [];
					//console.log($builder.forms['default']);
					
					if(response == 0){
						
						$scope.setjson = null;
						$scope.setjson.jsonData = null;
						$scope.initComponent = null;
						$scope.additionalComponent = null;
					}else{
						$scope.setjson = response;
						$scope.setjson.jsonData = angular.fromJson(response.jsonData);
						$scope.initComponent = angular.fromJson(response.jsonData);
						if(response.additionalData == true){
							$scope.additionalData = true;
							$scope.additionalComponent = angular.fromJson(response.jsonDataAdd);
						}else{
							$scope.additionalData == false;
						}
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
		   $scope.additionalComponent = [];
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
		   	$scope.additionalComponent = [];
		  $scope.initComponent = [];
		  $scope.FB = [];
		  $scope.FBAdd = [];
		  var FBuilder = {
		    'initComponent': $scope.initComponent,
		    'name':'default'
		  };
		  
		  var FBuilderAdd = {
				    'additionalComponent': $scope.additionalComponent,
				    'name':'defaultAdd'
				  };
		    var count = 0;  
		    $scope.FB.push(FBuilder);
		    $scope.FBAdd.push(FBuilderAdd);
		 
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
		       
		       
		       $scope.additionalDataFunction = function(additionalData){
		    	   $scope.additionalData = additionalData;
		    	   if(additionalData == undefined){
		    		   $scope.additionalData = true;
		    	   }else if(additionalData == false){
		    		   $scope.additionalData = true;
		    	   }else{
		    		   $scope.additionalData = false;
		    	   }
		    	   console.log("lllll");
		    	   console.log($scope.additionalData);
		       }
		       
		       $scope.editform = {};
		       
		      $scope.setKeyValues = function(){
		    	  
		    	 var fc = $routeParams.formType.charAt(0);
		    	 var lc = $routeParams.formType.charAt($routeParams.formType.length-1);
		    	  var keystart = fc+""+lc.toLowerCase();
		    	  angular.forEach($builder.forms['default'], function(value, key) {
						 var key;
		               		key = value.label;
		               		key = key.replace("  ","_");
		               		key = key.replace(" ","_");
		               		key = key.toLowerCase();
		               		value.key = keystart+"_"+key;
		               		console.log(key);
						 console.log(value.key);
					 });
		    	  
		    	  
		    	  angular.forEach($builder.forms['defaultAdd'], function(value, key) {
						 var key;
		               		key = value.label;
		               		key = key.replace("  ","_");
		               		key = key.replace(" ","_");
		               		key = key.toLowerCase();
		               		value.key = keystart+"1"+"a"+"_"+key;
		               		console.log(key);
						 console.log(value.key);
					 });
		      } 
		       
		  $scope.saveCreateLeadForm = function(){
				  $scope.showOutcomeMsg = 0;
				  var obj = localStorage.getItem('popupType');
				  $scope.leadId = localStorage.getItem('leadId');
				  $scope.setKeyValues();
				  $scope.oneField = 0;
				  if($builder.forms['default'].length <= 0){
					  $scope.oneField = 1;
					  $window.scrollTo(0, 0);
				  }else{
					  $scope.oneField = 0;
				  }
				  
				  if($scope.oneField == 0){
					  console.log($builder.forms['default']);
						 $scope.editform.formType = $routeParams.formType;
						 $scope.editform.jsonform = $builder.forms['default'];
						 $scope.editform.jsonformAdd = $builder.forms['defaultAdd'];
					
						 if($scope.additionalData == undefined){
							 $scope.additionalData = false;
						 }
						 $scope.editform.additionalData = $scope.additionalData;
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
						 
						 
						 //if(obj == "Lead"){
						 $scope.getLeadTypeDataById($scope.leadId);
					// }
				 // }else{
					//  $scope.showOutcomeMsg = 1;
			//	  }
				  }
		  }  
		  
		  $scope.saveCreateLeadFormOnly = function(){
			  $scope.showOutcomeMsg = 0;
			  var obj = localStorage.getItem('popupType');
			  $scope.leadId = localStorage.getItem('leadId');
			  $scope.setKeyValues();
			  $scope.flagAppointment = 0;
			  $scope.oneField = 0;
			  console.log($builder.forms['default']);
			  if($builder.forms['default'].length <= 0){
				  $scope.oneField = 1;
				  $window.scrollTo(0, 0);
			  }else{
				  $scope.oneField = 0;
			  }
			
			  if($scope.formType == "My Leads - Schedule an appointment"){
				  angular.forEach($builder.forms['default'], function(value, key) {
						 if(value.key == "Mt_appointment_type"){
							 if(value.options.length <= 0){
								 $scope.flagAppointment = 1;
								 $window.scrollTo(0, 0);
							 }else{
								 $scope.flagAppointment = 0;
							 }
						 }
					 });
			  }
			if($scope.flagAppointment == 0 && $scope.oneField == 0){
				 console.log($builder.forms['default']);
				 console.log($builder.forms['defaultAdd']);
				 $scope.editform.formType = $routeParams.formType;
				 $scope.editform.jsonform = $builder.forms['default'];
				 $scope.editform.jsonformAdd = $builder.forms['defaultAdd'];
				
				 $scope.editform.outcome = $scope.outcome;
				 console.log($scope.editform);
				 if($scope.additionalData == undefined){
					 $scope.additionalData = false;
				 }
				 $scope.editform.additionalData = $scope.additionalData;
				 apiserviceCustomizationForm.getLeadCrateForm($scope.editform).then(function(data){
					  console.log($scope.formType);
					 apiserviceCustomizationForm.getCustomizationform($scope.formType).then(function(response){
						 $scope.josnData1 = angular.fromJson(response.jsonData);
						 if(response.jsonDataAdd != null){
							 angular.forEach(angular.fromJson(response.jsonDataAdd), function(value2, key2) {
								 $scope.josnData1.push(value2);
							 });
						 }
						 
						 console.log($scope.josnData1);
						 $scope.formListData=[];
						 		angular.forEach($scope.josnData1, function(value1, key) {
						 			$scope.formListData.push({
						 				fieldId:value1.fieldId,
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
									// $location.path('/configuration');
								 });
					 });
					 
				});
			}
			  
			
		  }
		  
		  $scope.saveCreateLeadFormAddOuctcome = function(){
			 
			  var obj = localStorage.getItem('popupType');
			  $scope.leadId = localStorage.getItem('leadId');
			 // if(obj == "Lead"){
					 $scope.getLeadTypeDataById($scope.leadId);
				// }
		  }
		  
		  $scope.getLeadTypeDataById = function(leadId){
			  console.log(leadId);
			  apiserviceCustomizationForm.getLeadTypeDataById(leadId).then(function(data){
				  console.log(data);
				  $scope.sendpdfIds = data.sendpdfIds;
				  if(data.confirmationMsg == null){
					  data.confirmationMsg = "Thank you for your interest!";
				  }
				 
				  var arr = [];
				  var arr1 = [];
				  if(data.actionOutcomes != null){
					  arr = data.actionOutcomes.split(',');
					  for(var i=0;i<arr.length;i++){
						  angular.forEach($scope.callActionsList, function(obj, index){
							  if(obj.name == arr[i]){
								  obj.isSelected = true;
							  }
						  });
					  }
				  }
				  console.log($scope.outcomemenu);
				  
				  if(data.systemOutcome != null){
					  $scope.actions = [];
					  arr1 = data.systemOutcome.split(',');
					  for(var i=0;i<arr1.length;i++){
						  angular.forEach($scope.outcomemenu, function(obj, index){
							  if(obj.id == arr1[i]){
								  obj.isSelected = true;
								  $scope.actions.push(obj.id);
							  }
						  });
					  }
				  }
				  
				  
				  $scope.callAction = data;
				  $scope.callAction.actionTitle = data.leadName;
				  console.log($scope.callAction);
				  console.log(data.callToAction);
				  console.log($scope.callAction.actionOutcomes);
				  if($scope.callAction.actionOutcomes != null){
					  var arr = [];
					  arr = $scope.callAction.actionOutcomes.split(',');
					  for(var i=0;i<arr.length;i++){
						  $scope.callactions.push(arr[i]);
					  }	  
					  console.log($scope.callactions);
				  }
				 
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
			 /* $upload.upload({
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
	  			});*/
		  }
		  
		  $scope.checkSaveDocCall = function(savedoc){
				console.log(savedoc);
				if(savedoc == false){
					$upload.upload({
			 	         url : '/saveCustomerPdf',
			 	         method: 'POST',
			 	         file:logofile,
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
			$scope.checkSaveDocCallTo = function(savedoc){
				
			}
			$scope.sendpdfIds = 0;
		  $scope.sendpdfId = function(item){
			  console.log(item);
			  $scope.sendpdfIds = item.customerPdfId;
		  }
		  $scope.callActionId ={};
		 $scope.callToAction = function(){
			 
			 console.log($scope.leadId);
			 $scope.callAction.id = $scope.leadId;
			 console.log($scope.callAction);
			 $scope.callAction.sendpdfIds = $scope.sendpdfIds;
			 
			 $scope.callAction.actionOutcomes = $scope.callactions.toString(); 
			 $scope.callAction.outcome = $scope.actions.toString(); 
			 if(logofile == undefined){
				 apiserviceCustomizationForm.saveLeadFormPopup($scope.callAction).then(function(data){
					 $scope.saveCreateLeadFormOnly();
					 $location.path('/configuration');
				 });
			 }else if(logofile != undefined){
				 $upload.upload({
		            url : '/saveLeadFormPopup',
		            method: 'POST',
		            file:logofile,
		            data:$scope.callAction
		         }).success(function(data) {
		        	 $scope.saveCreateLeadFormOnly();
		   			console.log(data);
		   			$location.path('/configuration');
		   			$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Your Progress has been Saved",
					});		   			
		   		 });
				 
				}
			 
			 $("#completedPopup").modal('hide');
			
			 
		 }
		  
		  $scope.editLeadInfo = function(title){
			   $scope.setjson.showFild = title;
			  $('#edititle').modal('show');

		  }
		  
		  $scope.actions = [];
		  $scope.checkBocId = 0;
		  $scope.actionClicked = function(e, rolePer,value){
			  
			  console.log($builder.forms['default']);
				 console.log($builder.forms['defaultAdd']);
				 $scope.autoCrmFlag = 0;
				 $scope.autoDateFlag = 0;
				 $scope.autoTimeFlag = 0;
				 angular.forEach($builder.forms['default'], function(obj, index){
					  if(obj.component == "selectGroup"){
						  $scope.autoCrmFlag = 1;
					  }
					  if(obj.component == "timerange"){
						  $scope.autoTimeFlag = 1;
					  }
					  if(obj.component == "date"){
						  $scope.autoDateFlag = 1;
					  }
				 });
				 angular.forEach($builder.forms['defaultAdd'], function(obj, index){
					 if(obj.component == "selectGroup"){
						 $scope.autoCrmFlag = 1;
					  }
					 if(obj.component == "timerange"){
						  $scope.autoTimeFlag = 1;
					  }
					  if(obj.component == "date"){
						  $scope.autoDateFlag = 1;
					  }
				 });
				
				 if(rolePer.id == 4){
					 $scope.checkBocId = 4;
					 if($scope.autoTimeFlag == 0 || $scope.autoDateFlag == 0){
						 $('#dateTimePopup').modal('show');
						 rolePer.isSelected = false;
					 }
				 }else if(rolePer.id == 3){
					 $scope.checkBocId = 3;
					 if($scope.autoCrmFlag == 0){
						 $('#crmPopup').modal('show');
						 rolePer.isSelected = false;
						 console.log(rolePer);
					 }
				 }
				 
				 console.log(rolePer.isSelected);
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
			
			$scope.uncheckCrm = function(){
				console.log($scope.checkBocId);
				
				angular.forEach($scope.outcomemenu, function(obj, index){
					if(obj.id == $scope.checkBocId){
						obj.isSelected = false;
						$scope.deleteItem(obj);
					}
				});
				//$('#myCheckbox3').attr('checked', false);
				console.log($scope.actions);
				console.log($scope.outcomemenu);
				
			}
			$scope.uncheckDate = function(){
				angular.forEach($scope.outcomemenu, function(obj, index){
					if(obj.id == $scope.checkBocId){
						obj.isSelected = false;
						$scope.deleteItem(obj);
					}
				});
				//$('#myCheckbox4').attr('checked', false);
				console.log($scope.actions);
				console.log($scope.outcomemenu);
			}
			$scope.closrPopUp = function(){
				console.log($scope.checkBocId);
			
			angular.forEach($scope.outcomemenu, function(obj, index){
				//$('#myCheckbox'+obj.id).attr('checked', false);
				if(obj.id == $scope.checkBocId){
					obj.isSelected = false;
					$scope.deleteItem(obj);
				}
			});
			 $('#completedPopup').modal('hide');
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
