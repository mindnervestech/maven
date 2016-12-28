angular.module('newApp')
.controller('crmCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','apiserviceCrm','$q','$rootScope', function ($scope,$http,$location,$filter,$routeParams,$upload,apiserviceCrm,$q,$rootScope) {
	if(!$scope.$$phase) {
		$scope.$apply();
	}
	
	
	$scope.allLoc = true;
	$scope.gridOptions = {
   		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
   		    paginationPageSize: 150,
   		    enableFiltering: true,
   		    useExternalFiltering: true,
   		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
   		 };
   		 $scope.gridOptions.enableHorizontalScrollbar = 2;
   		 $scope.gridOptions.enableVerticalScrollbar = 2;
   		
   		 $scope.gridOptions.columnDefs = [
   		                                 { name: 'contactId', displayName: 'Contact Id', width:'10%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'type', displayName: 'Type',enableFiltering: false, width:'10%',
   		                                 },
   		                                 { name: 'firstName', displayName: 'First Name', width:'11%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'lastName', displayName: 'Last Name', width:'11%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'companyName', displayName: 'Company Name', width:'12%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'email', displayName: 'Email', width:'10%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'10%',
   		                                 },
   		                                 { name: 'assignedToName', displayName: 'Assigned To',enableFiltering: false, width:'12%',cellEditableCondition: false,
   		                                 },
       		                                
       		                                 ];  
    
   		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'contactId':grid.columns[0].filters[0].term,'firstName':grid.columns[2].filters[0].term,'lastName':grid.columns[3].filters[0].term,'companyName':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term},undefined);
		        });
	   		
   		};
   		 
   		$scope.deleteAllContactPop = function(){
   			$('#removeModal').click();
   		};
   		$scope.removeAllContactsData = function(){
   			apiserviceCrm.removeAllContactsData().then(function(data){
   				console.log("success.");
   				$scope.getContactsData();
   			});
   		};
   		$scope.exportCsvPop = function(){
   			$('#exportModal').click();
   		};
   		$scope.exportCsv = function(){
   			
   			apiserviceCrm.exportContactsData().then(function(data){
   				$.fileDownload('/downloadStatusFile',
						{	   	
							   /*httpMethod : "POST",
							   data : {
							   }*/
						}).done(function(e, response)
								{
									$.pnotify({
												title: "Success",
												type:'success',
												text: "File download successfully",
									});
								}).fail(function(e, response)
								{
									// failure
									console.log('fail');
									console.log(response);
									console.log(e);
								});
   			});
   		}
   		
   		apiserviceCrm.getAllContactsData().then(function(data){
   			console.log(data);
				//$scope.gridOptions.data = data;
   			$scope.editgirdData(data);
				$scope.contactsList = data;
				
   		});
   		
   		//group start
   		$scope.getAllGroupList = function(){
   			apiserviceCrm.getAllGroupList().then(function(data){
   	   			console.log(data);
   				$scope.groupList = data;				
   	   		});
   		};
   		
   		$scope.getAllGroupList();
   		$scope.grNameReq = false;
   		$scope.showAddGr = false;
   		$scope.updateGr = false;
   		$scope.saveNewGroup = function(group){
			if(group.name == undefined ||  group.name == null){
				$scope.grNameReq = true;
				$scope.errMsg= "Group name required.";
			}else{
				$scope.grNameReq = false;
				apiserviceCrm.saveNewGroup(group).then(function(data){
					if(data !='error'){
						$scope.group = {};
						$scope.getAllGroupList();
						$scope.showAddGr = !$scope.showAddGr;
					}else{
						$scope.grNameReq = true;
						$scope.errMsg= "Group name already exist.";
					}
					
				});
			}
		}
   		
   		$scope.updateGroup = function(grp){
			if(grp.name == undefined ||  grp.name == null){
				$scope.grNameReq = true;
			}else{
				$scope.grNameReq = false;
				apiserviceCrm.updateGroup(grp).then(function(data){
					if(data !='error'){
						$scope.group = {};
						$scope.getAllGroupList();
						$scope.closeUpdateGr();
					}else{
						$scope.grNameReq = true;
						$scope.errMsg= "Group name already exist.";
					}
				});
			}
		}
   		
   		$scope.addGrClick = function(){
   			$scope.showAddGr = !$scope.showAddGr;
   			$scope.updateGr = false;
   			$scope.grNameReq = false;
   			$scope.group = {};
   		};
   		
   		$scope.deleteGroupBy = function(group){
   			$scope.deleteGr = group;
   		};
   		$scope.deleteGroupList = function(){
   			console.log($scope.deleteGr);
   			apiserviceCrm.deleteGroup($scope.deleteGr.id).then(function(data){
				$scope.group = {};
				$scope.getAllGroupList();
				$scope.closeUpdateGr();
			});
   		};
   		
   		$scope.viewGroup = function(gr){
   			$scope.group = gr.group;
   			$scope.showAddGr = true;
   			$scope.updateGr = true;
   		};
   		
   		$scope.closeUpdateGr = function(){
   			$scope.showAddGr = !$scope.showAddGr;
   			$scope.updateGr = false;
   			$scope.group = {};
   			$scope.grNameReq = false;
   			$scope.getAllGroupList();
   		};
   		
   		//group end
   		
   		
  	  $scope.editgirdData = function(data){
		  $scope.gridOptions.data = data;
		  $scope.formField = [];
		  $scope.getFormDesign('Create New Lead').then(function(success){
			   $scope.formField = $scope.userList;
			   $scope.getFormDesign('New Contact').then(function(success){
				   angular.forEach($scope.userList, function(value, key) {
					   $scope.formField.push(value);
				   });
				   $scope.gridMapObect = [];
					var findFlag = 0;
					angular.forEach($scope.gridOptions.data,function(value,key){
							angular.forEach(value.customData,function(value1,key1){
								angular.forEach($scope.formField,function(value2,key2){
									if(value1.key == value2.key){
										$scope.gridMapObect.push({values: value1.value , key: value1.key,label:value2.label});
								    }
								});
								//$scope.gridMapObect.push({values: value1.value , key: value1.key});
							});
					});
					angular.forEach($scope.gridOptions.data,function(value,key){
						angular.forEach($scope.gridMapObect,function(value1,key1){
							var name = value1.key;
							name = name.replace(" ","");
							value[name] = null;
							angular.forEach(value.customData,function(value2,key2){
								if(value1.key == value2.key){
									value[name] = value2.value;
								}
							});
						});
					});	
					
					console.log($scope.gridMapObect);
					
					$scope.gridMapObect = UniqueArraybyId($scope.gridMapObect ,"key");
					angular.forEach($scope.gridMapObect,function(value,key){
						var name = value.key;
						$scope.flagValFlag = 1;
						name = name.replace(" ","");
						$scope.gridOptions.columnDefs.push({ name: name, displayName: value.label, width:'10%',cellEditableCondition: false,
			              	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
			              		if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
			                        return 'red';
			                    }
			              	} ,
			              });
						
					});
					
					$scope.gridOptions.columnDefs.push({ name: 'edit', displayName: 'Edit', width:'6%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                               cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Remove Contact" ng-click="grid.appScope.deleteContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>', 
                        });
					$scope.gridOptions.columnDefs.push({ name: 'newsletter', displayName: 'Newsletter',enableFiltering: false, width:'8%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                        cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.newsletter" ng-checked:newsletter ng-change="grid.appScope.setAsRead(row.entity.newsletter,row.entity.contactId)" data-checkbox="icheckbox_flat-blue" style="margin-left:42%;"></div>', 
                        });
			   });
		   });
		
			
		
	  }
	  $scope.showQuickList = "0";
  	$scope.addGroupPopUp = function(){
  		apiserviceCrm.getAllMailchimpEnable().then(function(data){
  			console.log(data);
  			if(data.synchronizeContact == true){
  				$scope.showQuickList = "1";
  				apiserviceCrm.getAllMailchimpList().then(function(data){
  					$scope.mailChimpLists = data;
  				});
  			}else{
  				$scope.showQuickList = "0";
  			}
  			
  		});
  		
  		$("#addNewGroup").modal('show');
  	}
  	function UniqueArraybyId(collection, keyname) {
        var output = [], 
            keys = [];

        angular.forEach(collection, function(item) {
            var key = item[keyname];
            if(keys.indexOf(key) === -1) {
                keys.push(key);
                output.push(item);
            }
        });
        return output;
    };
   		
  	    apiserviceCrm.getUsers().then(function(data){
			$scope.allUser = data;
		 });
  	    
  	    apiserviceCrm.getlocations().then(function(data){
			$scope.locationData = data;
		 });
  	    
  	    apiserviceCrm.getUserRole().then(function(data){
			$scope.userRole = data.role;
			if($scope.userRole != "General Manager"){
			}
		});
   		$scope.getLocationData = function(locatnId){
   			$scope.allLoc = false;
   			$scope.locId = locatnId;
   			console.log($scope.locId);
   			if(locatnId !=null){
   				apiserviceCrm.getAllContactsByLocation(locatnId).then(function(data){
   					$scope.gridOptions.data = data;
   	   				$scope.contactsList = data;
   				});
   			}
   		};

   		 apiserviceCrm.getgroupInfo().then(function(data){
			$scope.allGroup = data;
		 });
   		 
   		 $scope.getContactsData = function() {
   			 $scope.allLoc = true;
   			$scope.locId = null;
   		  apiserviceCrm.getAllContactsData().then(function(data){
   			    $scope.editgirdData(data);
				$scope.contactsList = data;
   				/*$scope.gridOptions.data = data;
   				$scope.contactsList = data;
   				
   				$scope.customData = data.customMapData;
   			 console.log($scope.customData);
   			 $scope.contactsList.collection=data.collection;
   			 if($scope.customData.time_range != undefined){
   				 $("#bestTimes").val($scope.customData.time_range);
   			 }
   			 
   			 if($scope.customData.address_bar != undefined){
   				 $("#autocomplete").val($scope.customData.address_bar);
   			 }
   			 
   			
   			 
   			 $.each($scope.customData, function(attr, value) {
   				 var res = value.split("[");
   					 if(res[1] != undefined){
   						 console.log(JSON.parse(value));
   						 $scope.customData[attr] = JSON.parse(value);
   				   	  			
   					 }
   							
   				 });
   			 
   			 console.log($scope.customData);
   				
   			 });*/
   		  });
   		}
   		 
   		var logofile;
		$scope.onCsvFileSelect = function($files) {
			logofile = $files;
		}
	   $scope.progress;
	   $scope.showProgress = false;
	   $scope.saveContactsFile = function() {
		   apiserviceCrm.uploadContactsFile(logofile).then(function(data){
			   $scope.getContactsData();
		   });
	   }
	   
   		$scope.contactsDetails = {};
	   $scope.editContactsDetail = function(row) {
		   $scope.showFormly = '0';
		   $scope.contactsDetails = row.entity;
		   $scope.formField = [];
		   $scope.getFormDesign('Create New Lead').then(function(success){
			   $scope.formField = $scope.userList;
			   $scope.getFormDesign('New Contact').then(function(success){
				   angular.forEach($scope.userList, function(value, key) {
					   $scope.formField.push(value);
				   });
				   $scope.userFields = $scope.addFormField($scope.formField);
					 $scope.userFieldsCopy = angular.copy($scope.userFields);
			   });
			   
				
		   });
		   
		   $scope.customData = row.entity.customMapData;
			 console.log($scope.customData);
			 $scope.contactsList.collection=row.entity.collection;
			 if($scope.customData.time_range != undefined){
				 $("#bestTimes").val($scope.customData.time_range);
			 }
			 
			 if($scope.customData.address_bar != undefined){
				 $("#autocomplete").val($scope.customData.address_bar);
			 }
			 
			
			 
			 $.each($scope.customData, function(attr, value) {
				 value = JSON.stringify(value);
	   				console.log(value);
				 console.log(value);
				 
				 var res = value.split("[");
					 if(res[1] != undefined){
						 console.log(JSON.parse(value));
						 $scope.customData[attr] = JSON.parse(value);
				   	  			
					 }
							
				 });
			 
		   $('#contactsModal').modal();
		   
		   $scope.customData = row.entity.customMapData;
			 console.log($scope.customData);
			 $scope.contactsList.collection=row.entity.collection;
			 if($scope.customData.time_range != undefined){
				 $("#bestTimes").val($scope.customData.time_range);
			 }
			 
			 if($scope.customData.address_bar != undefined){
				 $("#autocomplete").val($scope.customData.address_bar);
			 }
			 
			
			 
			 $.each($scope.customData, function(attr, value) {
				 value = JSON.stringify(value);
	   				console.log(value);
				 console.log(value);
				 
				 var res = value.split("[");
					 if(res[1] != undefined){
						 console.log(JSON.parse(value));
						 $scope.customData[attr] = JSON.parse(value);
				   	  			
					 }
							
				 });
			 
		   $('#contactsModal').modal();
	   }
   		 
	   $scope.updateContacts = function() {
		   $scope.customList =[];
			
			console.log($("#autocomplete").val());
			   console.log($scope.specification);
			   $scope.multiSelectBindWithCustomData();
  			$scope.customList =[];
  			$scope.customData.time_range = $("#bestTimes").val();
  			if($scope.customData.time_range == undefined){
  				delete $scope.customData.time_range;
  			}
  			$scope.customData.custName = $('#exCustoms_value').val();
  			if($scope.customData.custName == undefined){
  				delete $scope.customData.custName;
  			}
  			$scope.customData.autocompleteText = $("#autocomplete").val();
  			if($scope.customData.autocompleteText == undefined){
  				delete $scope.customData.autocompleteText;
  			}
				
				console.log($scope.customData);
				console.log($scope.userFields);
			
				apiserviceCrm.getCustomizationform('Create New Lead').then(function(response){
   				
	    			
   				$scope.josnData = angular.fromJson(response.jsonData);
   				angular.forEach($scope.josnData, function(obj, index){
   					obj.formName = "Create New Lead";
   				});
   				
   				$scope.josnData1 = null;
   				apiserviceCrm.getCustomizationform('New Contact').then(function(response1){
   					$scope.josnData1 = angular.fromJson(response1.jsonData);
   					angular.forEach($scope.josnData1, function(obj, index){
   						obj.formName = 'New Contact';
   						$scope.josnData.push(obj);
   						
  	    				});
   					if(response1.additionalData == true){
   						angular.forEach(angular.fromJson(response1.jsonDataAdd), function(obj, index){
	    						obj.formName = $scope.selectedLead;
	    						$scope.josnData.push(obj);
	    						
	   	    				});
   					}
   					console.log("()()()(0");
   					console.log($scope.josnData);
   					console.log($scope.customData);
   					var oneProduct = 0;
   					
   					
   					$.each($scope.customData, function(attr, value) {
   						angular.forEach($scope.josnData, function(value1, key) {
   							if(value1.key == attr){
   								if(value1.component == "leadTypeSelector"){
   									$scope.contactsDetails.leadType = value;
   								}
   								if(value1.component == "productType"){
   									if(oneProduct == 0){
   										$scope.contactsDetails.manufacturers =  $rootScope.subColl;
   										oneProduct++;
   									}
   								}
   							}
   						});
   					});	
   					
   					$scope.getCreateCustomList($scope.customData,$scope.josnData).then(function(response){
   						$scope.customList = response;
   					});
   					
   					
   				console.log($("#bestTimes").val());
	    			console.log($scope.customData);
	    			console.log($scope.customList);
	    			 
	    			
	    			$scope.contactsDetails.customData = $scope.customList;
   					
	    			console.log($scope.customList);
	    			$scope.contactsDetails.customData = $scope.customList;
	    			 apiserviceCrm.updateContactsData($scope.contactsDetails).then(function(data){
	    				 $('#contactsModal').modal('hide');
	    			 });
     	  		});
			
			});
		   
	   }
	   
	   $scope.setAsRead = function(newsletter,id) {
		   
		   apiserviceCrm.addNewsLetter(newsletter,id).then(function(data){
		   });
		   
	   }
	   
	   $scope.getFormDesign = function(formType){
			var deferred = $q.defer();
			
			apiserviceCrm.getCustomizationform(formType).then(function(response){
   			
				$scope.userList = [];
				angular.forEach(angular.fromJson(response.jsonData), function(obj, index){
					$scope.userList.push(obj);
					if(obj.component == "daterange"){
						obj.label = "Start Date";
						var objlist = angular.copy(obj);
						objlist.key = objlist.key+"_endDate";
						objlist.label = "End Date";
						$scope.userList.push(objlist);
					}
				});
				 $scope.editInput = response;
				 
				 deferred.resolve(response);
		   });
			
			return deferred.promise;
		}
	   $scope.showFormly = '1';
	   $scope.contactMsg = "";
	   $scope.createContact = function() {
		   $scope.showFormly = '1';
		   $scope.contactsDetails = {};
		   $scope.contactsDetails.workEmail = "Work";
		   $scope.contactsDetails.workEmail1 = "Work";
		   $scope.contactsDetails.workPhone = "Work";
		   $scope.contactsDetails.workPhone1 = "Work";
		   $scope.contactMsg = "";
		  
		   $scope.formField = [];
		   $scope.getFormDesign('Create New Lead').then(function(success){
			   $scope.formField = $scope.userList;
			   $scope.getFormDesign('New Contact').then(function(success){
				   angular.forEach($scope.userList, function(value, key) {
					   $scope.formField.push(value);
				   });
				   $scope.userFields = $scope.addFormField($scope.formField);
					 $scope.userFieldsCopy = angular.copy($scope.userFields);
			   });
			   
				
		   });
		  /* apiserviceCrm.getCustomizationform('New Contact').then(function(response){
				console.log(response);
				 $scope.editInput = response;
				 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
				 $scope.josnData = angular.fromJson(response.jsonData);
				 console.log($scope.userFields);
				 $scope.user = {};
				});*/
		   $('#createcontactsModal').modal();
	   }
	   $scope.customData = {};
	   $scope.saveContact = function() {
		   $scope.customList =[];
			
			console.log($("#autocomplete").val());
			   console.log($scope.specification);
			   $scope.multiSelectBindWithCustomData();
   			$scope.customList =[];
   			$scope.customData.time_range = $("#bestTimes").val();
   			if($scope.customData.time_range == undefined){
   				delete $scope.customData.time_range;
   			}
   			$scope.customData.custName = $('#exCustoms_value').val();
   			if($scope.customData.custName == undefined){
   				delete $scope.customData.custName;
   			}
   			$scope.customData.autocompleteText = $("#autocomplete").val();
   			if($scope.customData.autocompleteText == undefined){
   				delete $scope.customData.autocompleteText;
   			}
				
				console.log($scope.customData);
				console.log($scope.userFields);
			
				apiserviceCrm.getCustomizationform('Create New Lead').then(function(response){
    				
	    			
    				$scope.josnData = angular.fromJson(response.jsonData);
    				angular.forEach($scope.josnData, function(obj, index){
    					obj.formName = "Create New Lead";
    				});
    				
    				$scope.josnData1 = null;
    				apiserviceCrm.getCustomizationform('New Contact').then(function(response1){
    					$scope.josnData1 = angular.fromJson(response1.jsonData);
    					angular.forEach($scope.josnData1, function(obj, index){
    						obj.formName = 'New Contact';
    						$scope.josnData.push(obj);
    						
   	    				});
    					if(response1.additionalData == true){
    						angular.forEach(angular.fromJson(response1.jsonDataAdd), function(obj, index){
	    						obj.formName = $scope.selectedLead;
	    						$scope.josnData.push(obj);
	    						
	   	    				});
    					}
    					console.log("()()()(0");
    					console.log($scope.josnData);
    					console.log($scope.customData);
    					var oneProduct = 0;
    					
    					
    					$.each($scope.customData, function(attr, value) {
    						angular.forEach($scope.josnData, function(value1, key) {
    							if(value1.key == attr){
    								if(value1.component == "leadTypeSelector"){
    									$scope.contactsDetails.leadType = value;
    								}
    								if(value1.component == "productType"){
    									if(oneProduct == 0){
    										$scope.contactsDetails.manufacturers =  $rootScope.subColl;
    										oneProduct++;
    									}
    								}
    							}
    						});
    					});	
    					
    					$scope.getCreateCustomList($scope.customData,$scope.josnData).then(function(response){
    						$scope.customList = response;
    					});
    					
    					
    				console.log($("#bestTimes").val());
	    			console.log($scope.customData);
	    			console.log($scope.customList);
	    			 
	    			
	    			$scope.contactsDetails.customData = $scope.customList;
    					
	    			console.log($scope.customList);
	    			apiserviceCrm.saveContactsData($scope.contactsDetails).then(function(data){
	    				 if(data == "" || data != "Email already exists") {
	    					 $scope.contactMsg="";
	    					 $('#createcontactsModal').modal('hide');
	    					 $scope.getContactsData();
	    				 } else {
	    					 $scope.contactMsg = data;
	    				 }
	    				 
	    				});
      	  		});
			
			});
			
			
	   }
	   
	   
	   $scope.multiSelectBindWithCustomData = function(){
			console.log($rootScope.rObj);
			if($rootScope.rObj != undefined){
				$.each($rootScope.rObj, function(attr, value) {
   				$scope.customData[attr] = value;
   			});
			}
			if($rootScope.subColl != undefined){
				$scope.customData["subCollection"] = $rootScope.subColl;
			}
			
		}
	   
	   $scope.saveGroup = function(createGroup){
		   apiserviceCrm.saveGroup(createGroup).then(function(data){
			   apiserviceCrm.getgroupInfo().then(function(data){
					$scope.allGroup = data;
				 });
			});
	   }
		$scope.getCreateCustomList = function(customeDataList , josnData){
			 var deferred = $q.defer();
			 var addrs = 0;
			 var fileFlag = 0;
			 var timeRangs = 0;
			 $scope.dateFlag = 0
			$scope.customList = [];
			$.each(customeDataList, function(attr, value) {
				angular.forEach(josnData, function(value1, key) {
					
					if(addrs == 0){
						if(value1.component == "autocompleteText"){
							addrs = 1;
							$scope.customList.push({
								fieldId:value1.fieldId,
	    		   	  			key:value1.key,
	    		   	  			value:customeDataList.autocompleteText,
	    		   	  			savecrm:value1.savecrm,
	    		   	  			displayGrid:value1.displayGrid,
	    		   	  		    displayWebsite:value1.displayWebsite,
	    		   	  		    component:value1.component,
	    		   	  			formName:value1.formName,
	    					});
						}
					}
					if(timeRangs == 0){
						
						if(value1.component == "timerange"){
							timeRangs = 1;
							$scope.customList.push({
								fieldId:value1.fieldId,
	    		   	  			key:value1.key,
	    		   	  			value:customeDataList.time_range,
	    		   	  			savecrm:value1.savecrm,
	    		   	  			displayGrid:value1.displayGrid,
	    		   	  		    displayWebsite:value1.displayWebsite,
	    		   	  		    component:value1.component,
	    		   	  			formName:value1.formName,
	    					});
						}
					}
					
					if(fileFlag == 0){
						if(value1.component == "fileuploaders"){
							if($rootScope.fileCustom != undefined){
			    				console.log($rootScope.fileCustom[0].name);
			    				 $scope.fileFlag = 1;
								$scope.customList.push({
   								fieldId:value1.fieldId,
		    		   	  			key:value1.key,
		    		   	  			value:$rootScope.fileCustom[0].name,
		    		   	  			savecrm:value1.savecrm,
		    		   	  			displayGrid:value1.displayGrid,
		    		   	  		    displayWebsite:value1.displayWebsite,
		    		   	  		    component:value1.component,
		    		   	  			formName:value1.formName,
		    					});
			    			}
						}
					}
					
					if(value1.key == attr){
						
						if(value1.component == "multipleselect"){
							value = value.toString();
						}
						
						$scope.customList.push({
							fieldId:value1.fieldId,
   		   	  			key:attr,
   		   	  			value:value,
   		   	  			savecrm:value1.savecrm,
   		   	  			displayGrid:value1.displayGrid,
   		   	  		    displayWebsite:value1.displayWebsite,
   		   	  		    component:value1.component,
   		   	  			formName:value1.formName,
   					});
						
						if(value1.component == "productType"){
							$scope.customList.push({
								fieldId:value1.fieldId,
	    		   	  			key:attr+'_subCollection',
	    		   	  			value:$rootScope.subColl,
	    		   	  			savecrm:value1.savecrm,
	    		   	  			displayGrid:value1.displayGrid,
	    		   	  		    displayWebsite:value1.displayWebsite,
	    		   	  		    component:value1.component,
	    		   	  			formName:value1.formName,
	    					});
						}
						
						if(value1.component == "daterange"){
							
							$.each(customeDataList, function(attr1, value3) {
								if(value1.key+"_endDate" == attr1){
								
									console.log(new Date(value));
									console.log(new Date(value3));
									console.log(new Date(value3) >= new Date(value));
									if(new Date(value) >= new Date(value3)){
										$scope.dateFlag = 1;
									}else{
										$scope.dateFlag = 0;
									}
								
									$scope.customList.push({
	    								fieldId:value1.fieldId,
			    		   	  			key:attr1,
			    		   	  			value:value3,
			    		   	  			savecrm:value1.savecrm,
			    		   	  			displayGrid:value1.displayGrid,
			    		   	  		    displayWebsite:value1.displayWebsite,
			    		   	  		    component:value1.component,
			    		   	  			formName:value1.formName,
			    					});
								}
							});
						}

						
						var arr = [];
						var arr = attr.split('_');
						if(value1.component == "emailSelect"){
							
							$scope.customList.push({
								fieldId:value1.fieldId,
	    		   	  			key: arr[0]+"_emailType",
	    		   	  			value:$rootScope.selectEmailType,
	    		   	  			savecrm:value1.savecrm,
	    		   	  			displayGrid:value1.displayGrid,
	    		   	  		    displayWebsite:value1.displayWebsite,
	    		   	  		    component:value1.component,
	    		   	  			formName:value1.formName,
	    					});
						}
						
						if(value1.component == "phoneSelect"){
							$scope.customList.push({
								fieldId:value1.fieldId,
	    		   	  			key:arr[0]+"_phoneType",
	    		   	  			value:$rootScope.selectPhoneType,
	    		   	  			savecrm:value1.savecrm,
	    		   	  			displayGrid:value1.displayGrid,
	    		   	  		    displayWebsite:value1.displayWebsite,
	    		   	  		    component:value1.component,
	    		   	  			formName:value1.formName,
	    					});
						}
					} 
				});
			   });
				    			
			deferred.resolve($scope.customList);
			
			return deferred.promise;
		}
	   
	   $scope.deleteGroup = function(groupId){
		   console.log(groupId);
		   apiserviceCrm.deleteGroup(groupId).then(function(data){
			   apiserviceCrm.getgroupInfo().then(function(data){
					console.log(data);
					$scope.allGroup = data;
				 });
			});
	   }
	   
	   $scope.deleteContactsDetail = function(row){
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	    }
		   
		 $scope.deleteContactRow = function() {
			apiserviceCrm.deleteContactsById($scope.rowDataVal.entity.contactId).then(function(data){
			 if(data=='success'){
				 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				 });
	
				 $scope.getContactsData();
			 }else{
				 $.pnotify({
				    title: "Error",
				    type:'error',
				    text: "Contact can't deleted",
				 });
	
			 }

		 	});
		}
		 $scope.callListMailChim = function(){
			 apiserviceCrm.callList().then(function(data){
							$scope.getContactsData();
			 });
			
		}
		 
		 
		 $scope.saveNewList = function(newList){
				if(newList.nickName == undefined ||  newList.nickName == null || newList.listId == undefined ||  newList.listId == null){
					$scope.newError = true;
				}else{
					$scope.newError = false;
					apiserviceCrm.saveNewList(newList).then(function(data){
						$scope.newList = {};
						$scope.getAllMailchimpList();
					});
				}
			}
		 $scope.getAllMailchimpList = function(){
			 apiserviceCrm.getAllMailchimpList().then(function(data){
					$scope.mailChimpLists = data;
				});
			}
		 $scope.viewUpdate = function(list){
				console.log(list);
				$scope.newList = list;
				$scope.updateList = true;
			};
			$scope.deleteItem = function(list){
				$scope.listObj = list;
			};
			
			$scope.updateListItem = function(newList){
				if(newList.nickName == undefined ||  newList.nickName == null || newList.listId == undefined ||  newList.listId == null){
					$scope.newError = true;
				}else{
					$scope.newError = false;
					apiserviceConfigPage.updateListItem(newList).then(function(data){
						$scope.newList = {};
						$scope.closeUpdate();
						$scope.getAllMailchimpList();
					});
				}
			}
	   
}]);