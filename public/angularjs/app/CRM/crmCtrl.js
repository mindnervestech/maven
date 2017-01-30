angular.module('newApp')
.controller('crmCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','apiserviceCrm','$q','$rootScope','$compile','$timeout', function ($scope,$http,$location,$filter,$routeParams,$upload,apiserviceCrm,$q,$rootScope,$compile,$timeout) {
	if(!$scope.$$phase) {
		//$scope.$apply();
	}
	
	

	 $scope.gridOptions1 = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		 paginationPageSize: 150,
	 		 enableFiltering: true,
	 		 useExternalFiltering: true,
 		    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
    		 };
    		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
    		 $scope.gridOptions1.enableVerticalScrollbar = 2;
    		 $scope.gridOptions1.columnDefs = [
												 { name: 'isSelect', displayName: '#', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
													headerCellTemplate:	'<label style="margin-top: 5px; margin-left: 8px;">#</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input style="margin-top: 5px; margin-left: 8px;" type=\"checkbox\"  ng-model=\"checked\"  ng-change="grid.appScope.selectAllCheckOldNew(checked)" autocomplete="off">',
													cellTemplate:'<input type=\"checkbox\" ng-model=\"row.entity.checkBoxSelect\"  ng-click="grid.appScope.doActionOldNew(row,row.entity.checkBoxSelect)" autocomplete="off">',
												 }, 
    	   		                                 { name: 'type', displayName: 'Type',enableFiltering: false, width:'14%',
    	   		                                 },
    	   		                                 { name: 'firstName', displayName: 'Name', width:'14%',enableFiltering: false,
    	   		                                 },
    	   		                                 { name: 'companyName', displayName: 'Company Name', width:'14%',enableFiltering: false,
    	   		                                 },
    	   		                                 { name: 'email', displayName: 'Email', width:'15%',enableFiltering: false,
    	   		                                 },
    	   		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'14%',
    	   		                                 },
    	   		                                /* { name: 'edit', displayName: 'Edit', width:'8%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    	   		                                	 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Remove Contact" ng-click="grid.appScope.deleteContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>', 
    	   		                                 },*/
        		                                 ];   
	    
	   		$scope.gridOptions1.onRegisterApi = function(gridApi){
	   		 $scope.gridApi = gridApi;
			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
			 $scope.rowData = rowEntity;
			 $scope.$apply();
				
				 console.log($scope.rowData);
					/* apiserviceConfigPage.updateProductName($scope.rowData).then(function(data){
						 $.pnotify({
							    title: "Success",
							    type:'success',
							    text: "Update successfully",
							});
					 });*/
				 
			 });
		   		
	   		};
	
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
										{ name: 'isSelect', displayName: 'Select', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
											headerCellTemplate:	'<label style="margin-top: 5px; margin-left: 8px;">Select</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input style="margin-top: 5px; margin-left: 8px;" type=\"checkbox\"  ng-model=\"checker.checked\"  ng-change="grid.appScope.selectAllCheck(checker.checked)" autocomplete="off">',
											cellTemplate:'<input type=\"checkbox\" ng-model=\"row.entity.checkBoxSelect\"  ng-click="grid.appScope.doAction(row,row.entity.checkBoxSelect)" autocomplete="off">',
										}, 
   		                                 { name: 'contactId', displayName: '#', width:'4%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'type', displayName: 'Type',cellEditableCondition: false, enableSorting: false, enableColumnMenu: false, width:'10%',
   		                                	filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter2 in col.filters"><div my-custom-modal2></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByType(text)" ng-model="text"></div>'
   		                                 },
   		                                 { name: 'firstName', displayName: 'Name', width:'11%',cellEditableCondition: false,
   		                                 },
   		                                /* { name: 'lastName', displayName: 'Last Name', width:'11%',cellEditableCondition: false,
   		                                 },*/
   		                                 { name: 'companyName', displayName: 'Company Name', width:'12%',cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
   		                                	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilter(text)" ng-model="text"></div>'
   		                                 },
   		                                 { name: 'email', displayName: 'Email', width:'10%',cellEditableCondition: false,
   		                                 },
   		                                 { name: 'phone', displayName: 'Phone',cellEditableCondition: false, width:'10%',
   		                                 },
   		                                 { name: 'assignedToName', displayName: 'Assigned To',enableSorting: false, width:'12%',cellEditableCondition: false, enableColumnMenu: false,
   		                                	filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter1 in col.filters"><div my-custom-modal1></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByAssigned(text)" ng-model="text"></div>'
   		                                 },
       		                                
       		                                 ];  
    
   		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'contactId':grid.columns[1].filters[0].term,'type':grid.columns[2].filters[0].term,'firstName':grid.columns[3].filters[0].term,'companyName':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'phone':grid.columns[6].filters[0].term,'assignedToName':grid.columns[7].filters[0].term},undefined);
		        });
	   		
   		};
   		 
   		$scope.searchFilter = function(text){
   			console.log(text);
   			if(text == ''){
   				$scope.gridOptions.data = $scope.contactsList;
   			}else{
   			compData = [];
   			$scope.contactsList.forEach( function addDates( row, index ){
   				//console.log(row.companyName.match(text));
   				if(row.companyName){
   					if(row.companyName.includes(text)){
   						compData.push(row);
   					}
   				}
   			});
   			$scope.gridOptions.data = compData;
   			}
   		}
   		
   		$scope.searchFilterByAssigned = function(text){
   			console.log(text);
   			if(text == ''){
   				$scope.gridOptions.data = $scope.contactsList;
   			}else{
   			compData = [];
   			$scope.contactsList.forEach( function addDates( row, index ){
   				if(row.assignedToName){
   					if(row.assignedToName.includes(text)){
   						compData.push(row);
   					}
   				}
   			});
   			$scope.gridOptions.data = compData;
   			}
   		}
   		
   		$scope.showAgeModal = function() {
   		    $scope.listOfAges = [];
   		    
   		    $scope.col.grid.appScope.gridOptions.data.forEach( function ( row ) {
   		      if ( $scope.listOfAges.indexOf( row.companyName ) === -1 ) {
   		        $scope.listOfAges.push( row.companyName );
   		      }
   		    });
   		    $scope.listOfAges.sort();
   		    
   		    $scope.gridOptions = { 
   		      data: [],
   		      enableColumnMenus: false,
   		      onRegisterApi: function( gridApi) {
   		        $scope.gridApi = gridApi;
   		        
   		        if ( $scope.colFilter && $scope.colFilter.listTerm ){
   		          $timeout(function() {
   		            $scope.colFilter.listTerm.forEach( function( companyName ) {
   		              var entities = $scope.gridOptions.data.filter( function( row ) {
   		                return row.companyName === companyName;
   		              });
   		              
   		              if( entities.length > 0 ) {
   		                $scope.gridApi.selection.selectRow(entities[0]);
   		              }
   		            });
   		          });
   		        }
   		      } 
   		    };
   		    
   		    $scope.listOfAges.forEach(function( companyName ) {
   		      $scope.gridOptions.data.push({companyName: companyName});
   		    });
   		    
   		    var html = '<div class="modal" ng-style="{display: \'block\'}"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter Company Name</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="close()">Filter</button></div></div></div></div>';
   		    $elm = angular.element(html);
   		    angular.element(document.body).prepend($elm);
   		 
   		    $compile($elm)($scope);
   		    
   		  }
   		
		$scope.selectedCrm = [];
 		$scope.selectedCrmObj = "";
 		$scope.selectAllCheckOldNew = function(checked){
 			if(checked){
 				for(var i=0;i<$scope.gridOptions1.data.length;i++){
 					$scope.gridOptions1.data[i].checkBoxSelect = true;
 				}//checked = checked
 				angular.forEach($scope.gridOptions1.data, function(obj, index){
 					$scope.selectedCrm.push(obj.email);
       	  			$scope.selectedCrmObj = obj;
 	   			 });
 			}else{
 				for(var i=0;i<$scope.gridOptions1.data.length;i++){
 					$scope.gridOptions1.data[i].checkBoxSelect = false;
 				}
   	  			$scope.deleteCrmSelected($scope.selectedCrm);
 				
 			}
 			console.log($scope.selectedCrm);
 		}
 		
 		$scope.deleteCrmSelected = function(objList){
   				 if ((objList == $scope.selectedCrm)) {
   					 $scope.selectedCrm = [];
   			       	return;
   			    };
   	  	}	
   		
   		$scope.close = function() {
   		    var ages = $scope.gridApi.selection.getSelectedRows();
   		    $scope.colFilter.listTerm = [];
   		    
   		    ages.forEach( function( companyName ) {
   		      $scope.colFilter.listTerm.push( companyName.companyName );
   		    });
   		    
   		    $scope.colFilter.term = $scope.colFilter.listTerm.join(', ');
   		    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
   		    
   		    if ($elm) {
   		      $elm.remove();
   		    }
   		  };
   		  
   		$scope.deleteAllContactPop = function(){
   			$('#removeModal').click();
   		};
   		$scope.removeAllContactsData = function(){
   			apiserviceCrm.removeAllContactsData().then(function(data){
   				$scope.getContactsData();
   			});
   		};
   		$scope.exportCsvPop = function(){
   			$('#exportModal').click();
   		};
   		
   		
   		apiserviceCrm.getAllInventoryData().then(function(data){
   				$scope.getAllCollection = data;
   		});
   		
   		
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
								});
   			});
   		}
   		
   		apiserviceCrm.getAllContactsData().then(function(data){
   			$scope.editgirdData(data);
				$scope.contactsList = data;
				
   		});
   		
   		//group start
   		$scope.getAllGroupList = function(){
   			apiserviceCrm.getAllGroupList().then(function(data){
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
										if(value2.component == "daterange"){
   											$scope.gridMapObect.push({values: value1.value , key: value1.key, label: "Start Date", component: value2.component});
   											angular.forEach(value.customData,function(value3,key3){
	   											if(value3.key == value1.key+"_endDate"){
	   												$scope.gridMapObect.push({values: value3.value , key: value3.key, label: "End Date", component: value2.component});
		   										}
	   										});
   										}else if(value2.component == "productType"){
   											angular.forEach($scope.getAllCollection,function(obj,val){
   												 if(obj.id.toString() == value1.value){
   													value1.value = obj.collection;
   													//$scope.gridMapObect.push({values:  "abcd" , key: value1.key+"_subCollection",label:"SubCollection"});
   													$scope.gridMapObect.push({values: value1.value , key: value1.key,label:value2.label});
   												 }
   											});
   											
   										}else{
   											$scope.gridMapObect.push({values: value1.value , key: value1.key,label:value2.label});
   										}
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
					
					/*$scope.gridOptions.columnDefs.push({ name: 'edit', displayName: 'Edit', width:'6%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                               cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Remove Contact" ng-click="grid.appScope.deleteContactsDetail(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i>', 
                        });*/
					/*$scope.gridOptions.columnDefs.push({ name: 'newsletter', displayName: 'Newsletter',enableFiltering: false, width:'8%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                        cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.newsletter" ng-checked:newsletter ng-change="grid.appScope.setAsRead(row.entity.newsletter,row.entity.contactId)" data-checkbox="icheckbox_flat-blue" style="margin-left:42%;"></div>', 
                        });*/
			   });
		   });
		
			
		
	  }
	  $scope.showQuickList = "0";
  	$scope.addGroupPopUp = function(){
  		apiserviceCrm.getAllMailchimpEnable().then(function(data){
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
			$scope.locationId = data.location.id;
			if($scope.userRole != "General Manager"){
			}
		});
  	 
   		$scope.getLocationData = function(locatnId){
   			$scope.allLoc = false;
   			$scope.locId = locatnId;
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
   				$scope.gridOptions.data = data;
   				/*$scope.contactsList = data;
   				
   				$scope.customData = data.customMapData;
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
   						 $scope.customData[attr] = JSON.parse(value);
   					 }
   				 });
   			 });*/
   		  });
   		}
   		 
   		var logofile;
		$scope.onCsvFileSelect = function($files) {
			logofile = $files;
			$scope.saveContactsFile();
		}
	   $scope.progress;
	   $scope.showProgress = false;
	   $scope.oldNewContact = {};
	   $scope.saveContactsFile = function() {
		   apiserviceCrm.uploadContactsFile(logofile).then(function(data){
			  console.log(data);
			  $scope.oldNewContact = data;
			  $scope.gridOptions1.data = data.newContact;
			  console.log($scope.gridOptions1.data);
			  $('#NewOldContact').modal('show');
			   // $scope.getContactsData();
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
			 $scope.contactsList.collection=row.entity.collection;
			 if($scope.customData.time_range != undefined){
				 $("#bestTimes").val($scope.customData.time_range);
			 }
			 
			 if($scope.customData.address_bar != undefined){
				 $("#autocomplete").val($scope.customData.address_bar);
			 }
			 
			
			 
			 $.each($scope.customData, function(attr, value) {
				 value = JSON.stringify(value);
				 var res = value.split("[");
					 if(res[1] != undefined){
						 $scope.customData[attr] = JSON.parse(value);
					 }
				 });
			 
		   $('#contactsModal').modal();
		   
		   $scope.customData = row.entity.customMapData;
			 $scope.contactsList.collection=row.entity.collection;
			 if($scope.customData.time_range != undefined){
				 $("#bestTimes").val($scope.customData.time_range);
			 }
			 
			 if($scope.customData.address_bar != undefined){
				 $("#autocomplete").val($scope.customData.address_bar);
			 }
			 
			
			 
			 $.each($scope.customData, function(attr, value) {
				 value = JSON.stringify(value);
				 var res = value.split("[");
					 if(res[1] != undefined){
						 $scope.customData[attr] = JSON.parse(value);
					 }
				 });
			 
		   $('#contactsModal').modal();
	   }
   		 
	   $scope.updateContacts = function() {
		   $scope.customList =[];
			
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
   					
	    			$scope.contactsDetails.customData = $scope.customList;
   					
	    			$scope.contactsDetails.customData = $scope.customList;
	    			 apiserviceCrm.updateContactsData($scope.contactsDetails).then(function(data){
	    				 $('#contactsModal').modal('hide');
	    				 $scope.getContactsData();
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
				 $scope.editInput = response;
				 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
				 $scope.josnData = angular.fromJson(response.jsonData);
				 $scope.user = {};
				});*/
		   $('#createcontactsModal').modal();
	   }
	   $scope.customData = {};
	   $scope.saveContact = function() {
		   $scope.customList =[];
			
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
    					
	    			$scope.contactsDetails.customData = $scope.customList;
    					
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
		   apiserviceCrm.deleteGroup(groupId).then(function(data){
			   apiserviceCrm.getgroupInfo().then(function(data){
					$scope.allGroup = data;
				 });
			});
	   }
	   
	   $scope.deleteContactsDetail = function(row){
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	    }
		   
		 $scope.deleteContactRow = function() {
			 console.log( $scope.rowDataVal);
			apiserviceCrm.deleteContactsById($scope.rowDataVal).then(function(data){
			 if(data=='success'){
				 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				 });
	
				 $scope.getContactsData();
				 $scope.actionSelectedLead = [];
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
			
		/*--------------------------------Custome Search Filter --------------------------------*/	
			
			$scope.showAssignedModal = function() {
	   		    $scope.listOfAges1 = [];
	   		    $scope.col.grid.appScope.gridOptions.data.forEach( function ( row ) {
	   		    	if($scope.listOfAges1.indexOf( row.assignedToName ) === -1 ) {
	   		        	$scope.listOfAges1.push( row.assignedToName );
	   		        	console.log(row.assignedToName);
	   		      	}
	   		    });
	   		    $scope.listOfAges1.sort();
	   		    $scope.gridOptions1 = { 
	   		    	data: [],
	   		    	enableColumnMenus: false,
	   		    	onRegisterApi: function( gridApi) {
	   		        $scope.gridApi1 = gridApi;
	   		        if($scope.colFilter1 && $scope.colFilter1.listTerm ){
	   		        	$timeout(function() {
	   		        		$scope.colFilter1.listTerm.forEach( function( assignedToName ) {
	   		        			var entities = $scope.gridOptions1.data.filter( function( row ) {
	   		        				return row.assignedToName === assignedToName;
	   		        			}); 
	   		              
	   		        			if(entities.length > 0 ) {
	   		        				$scope.gridApi1.selection.selectRow(entities[0]);
	   		        				console.log(entities[0]);
	   		        			}
	   		        		});
	   		        	});
	   		        }
	   		      } 
	   		    };
	   		    $scope.listOfAges1.forEach(function( assignedToName ) {
	   		    	$scope.gridOptions1.data.push({assignedToName: assignedToName});
	   		    });
	   		    console.log( $scope.gridOptions1.data);
	   		    var html = '<div class="modal" ng-style="{display: \'block\'}"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter Assigned To</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions1" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="checkAssigned()">Filter</button></div></div></div></div>';
	   		    $elm = angular.element(html);
	   		    angular.element(document.body).prepend($elm);
	   		    $compile($elm)($scope);
			}
			
			$scope.checkAssigned = function() {
				var ages = $scope.gridApi1.selection.getSelectedRows();
	   		    $scope.colFilter1.listTerm = [];
	   		    ages.forEach( function( assignedToName ) {
	   		    	$scope.colFilter1.listTerm.push( assignedToName.assignedToName );
	   		    });
	   		    $scope.colFilter1.term = $scope.colFilter1.listTerm.join(', ');
	   		    $scope.colFilter1.condition = new RegExp($scope.colFilter1.listTerm.join('|'));
	   		    if ($elm) {
	   		    	$elm.remove();
	   		    }
	   		};
	   		
	   		$scope.actionSelectedLead = [];
	 		$scope.actionSelectedLeadObj = "";
	 		$scope.selectAllCheck = function(checked){
	 			if(checked){
	 				for(var i=0;i<$scope.gridOptions.data.length;i++){
	 					$scope.gridOptions.data[i].checkBoxSelect = true;
	 				}//checked = checked
	 				angular.forEach($scope.gridOptions.data, function(obj, index){
	 					$scope.actionSelectedLead.push(obj.contactId);
	       	  			$scope.actionSelectedLeadObj = obj;
	 	   			 });
	 			}else{
	 				for(var i=0;i<$scope.gridOptions.data.length;i++){
	 					$scope.gridOptions.data[i].checkBoxSelect = false;
	 				}
	   	  			$scope.deleteActionSelected($scope.actionSelectedLead);
	 			}
	 			console.log($scope.actionSelectedLead);
	 		}
	 		
	 		$scope.deleteActionSelected = function(objList){
	 			if ((objList == $scope.actionSelectedLead)) {
	 				$scope.actionSelectedLead = [];
  			       	return;
  			    };
	 		}	
	 		
	 		$scope.actionSelectedLead = [];
	 	  	$scope.actionSelectedLeadObj = "";
	 	  	$scope.doAction = function(row,checkBoxSelect){
	 	  		console.log(row.entity);
	 	  		$scope.contactById = row;
	 	  		if(checkBoxSelect == undefined || checkBoxSelect == false){
	 	  			$scope.actionSelectedLead.push(row.entity.contactId);
	 	  			$scope.actionSelectedLeadObj = row.entity;
	 	  		}else{
	 	  			$scope.deleteActionSelectedLead(row.entity);
	 	  		}
	 	  		console.log($scope.actionSelectedLead);
	 	  	}
	 	  	
	 	  	$scope.deleteActionSelectedLead = function(row){
	 	  		angular.forEach($scope.actionSelectedLead, function(obj, index){
	 	  			if ((row.contactId == obj)) {
	 	  				$scope.actionSelectedLead.splice(index, 1);
	 			       	return;
	 			    };
	 			 });
	 	  	}
	 	  	
	 	  	$scope.assignCanceledLead = function() {
	        	$scope.changedUser = "";
	        	$scope.getSalesDataValue($scope.locationId);
	 			$("#assignUserModal").modal("show");
	        }
	 	  	
	 	  	$scope.getSalesDataValue = function(locationValue){
	 	  		apiserviceCrm.getSalesUserOnly(locationValue).then(function(data){
	    			$scope.salesPersonPerf = data;
	    			console.log(data);
	    			angular.forEach($scope.salesPersonPerf, function(value, key) {
	    				value.isSelected = false;
	    			});
	    		});
	 		}
	 		
	 		 $scope.changeAssignedUser = function() {
	 			apiserviceCrm.changeContactAssignedUser($scope.actionSelectedLead.toString(), $scope.changedUser).then(function(data){
	         		$("#assignUserModal").modal("hide");
	         		$scope.getContactsData();
	         		$scope.actionSelectedLead = [];
	 			});
			}
	 		 
	 		
	 		$scope.addToGroupPopUP = function() {
	        	$scope.changedUser = "";
	        	$scope.getGroupOfData();
	 			$("#addGroupUserModal").modal("show");
	        }
	 		
	 		$scope.getGroupOfData = function(){
	 	  		apiserviceCrm.getGroupOfData().then(function(data){
	    			$scope.groupData = data;
	    			console.log(data);
	    			angular.forEach($scope.groupData, function(value, key) {
	    				value.isSelected = false;
	    			});
	    		});
	 		}
	 		
	 		$scope.changeGroupData = function() {
	 			console.log($scope.changegroup);
	 			apiserviceCrm.saveChangeGroupData($scope.actionSelectedLead.toString(), $scope.changegroup).then(function(data){
	         		$("#addGroupUserModal").modal("hide");
	         		$scope.getContactsData();
	         		$scope.actionSelectedLead = [];
	 			});
			}
	 		
	 		$scope.showContactType = function(type){
	   			console.log("ssssss");
	   			$scope.checked = false;
	   			$scope.selectedCrm = [];
	   			for(var i=0;i<$scope.gridOptions1.data.length;i++){
 					$scope.gridOptions1.data[i].checkBoxSelect = false;
 				}
	   			if(type == "new"){
	   				$scope.gridOptions1.data = $scope.oldNewContact.newContact;
	   			}else if(type == "old"){
	   				$scope.gridOptions1.data = $scope.oldNewContact.oldContact;
	   			}
	   		}
	   		
	 $scope.ImportAllValue = function(){
		 console.log($scope.oldNewContact);
		 apiserviceCrm.importContacts($scope.oldNewContact).then(function(data){
			  $('#NewOldContact').modal('hide');
			  $scope.getContactsData();
		   });
	 }
	 
	 $scope.showTypeModal = function() {
		    $scope.listOfAges1 = [];
		    
		    $scope.col.grid.appScope.gridOptions.data.forEach( function ( row ) {
		      if ( $scope.listOfAges1.indexOf( row.type ) === -1 ) {
		        $scope.listOfAges1.push( row.type );
		      }
		    });
		    $scope.listOfAges1.sort();
		    
		    $scope.gridOptions2 = { 
		      data: [],
		      enableColumnMenus: false,
		      onRegisterApi: function( gridApi) {
		        $scope.gridApi2 = gridApi;
		        
		        if ( $scope.colFilter2 && $scope.colFilter2.listTerm ){
		          $timeout(function() {
		            $scope.colFilter2.listTerm.forEach( function( type ) {
		              var entities = $scope.gridOptions2.data.filter( function( row ) {
		                return row.type === type;
		              });
		              
		              if( entities.length > 0 ) {
		                $scope.gridApi2.selection.selectRow(entities[0]);
		              }
		            });
		          });
		        }
		      } 
		    };
		    
		    $scope.listOfAges1.forEach(function( type ) {
		      $scope.gridOptions2.data.push({type: type});
		    });
		    
		    var html = '<div class="modal" ng-style="{display: \'block\'}"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter Of Type</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions2" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="closeType()">Filter</button><button id="button" class="btn btn-primary" ng-click="removeCompanyFulter()">Remove Filters</button></div></div></div></div>';
		    $elm = angular.element(html);
		    angular.element(document.body).prepend($elm);
		 
		    $compile($elm)($scope);
		    
		  }
	 
	 	$scope.closeType = function() {
			    var ages = $scope.gridApi2.selection.getSelectedRows();
			    $scope.colFilter2.listTerm = [];
			    
			    ages.forEach( function( type ) {
			      $scope.colFilter2.listTerm.push( type.type );
			    });
			    
			    $scope.colFilter2.term = $scope.colFilter2.listTerm.join(', ');
			    $scope.colFilter2.condition = new RegExp($scope.colFilter2.listTerm.join('|'));
			    if ($elm) {
			      $elm.remove();
			    }
		  };
		  
		  
		  $scope.searchFilterByType = function(text){
	   			console.log(text);
	   			if(text == ''){
	   				$scope.gridOptions.data = $scope.contactsList;
	   			}else{
	   			compData = [];
	   			$scope.contactsList.forEach( function addDates( row, index ){
	   				if(row.type){
	   					if(row.type.includes(text)){
	   						compData.push(row);
	   					}
	   				}
	   			});
	   			$scope.gridOptions.data = compData;
	   			}
	   		}
		  
		  $scope.inviteNewUser = function(){
	    		console.log("edit");
	    		$location.path('/createUser/invite');
	    	}
}]);