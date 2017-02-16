angular.module('newApp')
.controller('crmCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','apiserviceCrm','$q','$rootScope','$compile','$timeout','$route','uiGridConstants', function ($scope,$http,$location,$filter,$routeParams,$upload,apiserviceCrm,$q,$rootScope,$compile,$timeout,$route,uiGridConstants) {
	if(!$scope.$$phase) {
		//$scope.$apply();
	}
	
	

	 $scope.gridOptionsNew = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		 paginationPageSize: 150,
	 		 enableFiltering: true,
	 		 useExternalFiltering: true,
 		     rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
    		 };
    		 $scope.gridOptionsNew.enableHorizontalScrollbar = 2;
    		 $scope.gridOptionsNew.enableVerticalScrollbar = 2;
    		 
	    
	   		$scope.gridOptionsNew.onRegisterApi = function(gridApi){
	   		 $scope.gridApi = gridApi;
			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
			 $scope.rowData = rowEntity;
			 $scope.$apply();
				
				 console.log($scope.rowData);
									 
			 });
		   		
	   		};
	
	$scope.allLoc = true;
	$scope.gridOptions = {
   		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
   		    paginationPageSize: 150,
   		    enableFiltering: true,
   		   // useExternalFiltering: true,
   		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
   		 };
   		 $scope.gridOptions.enableHorizontalScrollbar = 2;
   		 $scope.gridOptions.enableVerticalScrollbar = 2;
   		
   		 $scope.gridOptions.columnDefs = [
										{ name: 'isSelect', displayName: 'Select', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
											headerCellTemplate:	'<label style="margin-top: 5px; margin-left: 8px;">Select</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input style="margin-top: 5px; margin-left: 8px;" type=\"checkbox\"  ng-model=\"checker.checked\"  ng-change="grid.appScope.selectAllCheck(checker.checked)" autocomplete="off">',
											cellTemplate:'<input type=\"checkbox\" ng-model=\"row.entity.checkBoxSelect\"  ng-click="grid.appScope.doAction(row,row.entity.checkBoxSelect)" autocomplete="off">',
										}, 
   		                                 { name: 'contactId', displayName: '#', width:'4%',cellEditableCondition: false,enableColumnMenu: false,
   		                                 },
   		                                 { name: 'type', displayName: 'Type',cellEditableCondition: false, enableSorting: false, enableColumnMenu: false, width:'10%',
   		                                	filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal2></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByType(text)" ng-model="text"></div>'
   		                                 },
   		                                 { name: 'firstName', displayName: 'Name', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
   		                                 },
   		                                /* { name: 'lastName', displayName: 'Last Name', width:'11%',cellEditableCondition: false,
   		                                 },*/
   		                                 /*{ name: 'companyName', displayName: 'Company Name', width:'12%',cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
   		                                	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilter(text)" ng-model="text"></div>'
   		                                 },*/
   		                                 { name: 'email', displayName: 'Email', width:'10%',cellEditableCondition: false, enableColumnMenu: false,
   		                                 },
   		                                 { name: 'phone', displayName: 'Phone',cellEditableCondition: false, width:'10%', enableColumnMenu: false,
   		                                 },
   		                                 { name: 'assignedToName', displayName: 'Assigned To',enableSorting: false, width:'12%',cellEditableCondition: false, enableColumnMenu: false,
   		                                	filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal1></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByAssigned(text)" ng-model="text"></div>'
   		                                 },
   		                                 { name: 'salutation', displayName: 'Salutation', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'suffix', displayName: 'Suffix', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'city', displayName: 'City', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal3 type="city"></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByCity(text)" ng-model="text"></div>',
		                                 },
		                                 { name: 'state', displayName: 'State', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal3 type="state"></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByState(text)" ng-model="text"></div>',
		                                 },
		                                 { name: 'zip', displayName: 'ZipCode', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'allPhone', displayName: 'All Phone', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'allEmail', displayName: 'All Email', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'title', displayName: 'Title', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'birthday', displayName: 'Birthday', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'backgroundInfo', displayName: 'BackgrountInfo', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'industry', displayName: 'Industry', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                	 filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal3 type="industry"></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByIndustry(text)" ng-model="text"></div>',
		                                 },
		                                 { name: 'creationDate', displayName: 'CreationDate', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'lastEditedDate', displayName: 'LastEditedDate', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 /*{ name: 'priority', displayName: 'Priority', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },*/
		                                 { name: 'relationships', displayName: 'Relationships', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
		                                 { name: 'notes', displayName: 'Notes', width:'11%',cellEditableCondition: false, enableColumnMenu: false,
		                                 },
       		                                 ];  
    
   		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		/*$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          console.log(grid.columns[0].filters[0].term,"grid.columns[0].filters[0].term")
		          console.log(grid.columns[1].filters[0].term,"grid.columns[1].filters[0].term")
		          console.log(grid.columns[2].filters[0].term,"grid.columns[2].filters[0].term")
		          console.log(grid.columns[3].filters[0].term,"grid.columns[3].filters[0].term")
		          console.log(grid.columns[4].filters[0].term,"grid.columns[4].filters[0].term")
		          console.log(grid.columns[5].filters[0].term,"grid.columns[5].filters[0].term")
		          console.log(grid.columns[6].filters[0].term,"grid.columns[6].filters[0].term")
		          console.log(grid.columns[7].filters[0].term,"grid.columns[7].filters[0].term")
		          console.log($scope.contactsList);
		        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'contactId':grid.columns[1].filters[0].term,'type':grid.columns[2].filters[0].term,'firstName':grid.columns[3].filters[0].term,'companyName':grid.columns[4].filters[0].term,'email':grid.columns[5].filters[0].term,'phone':grid.columns[6].filters[0].term,'assignedToName':grid.columns[7].filters[0].term},undefined);
		        });*/
	   		
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
   		 console.log("--------------------");
   		    console.log($scope.contactsList);
   		 //$scope.gridOptions.data = $scope.contactsList;
   		$scope.contactsList.forEach( function ( row ) {
   			if(row.companyName != null){
   				if(row.companyName.trim() == "null" || row.companyName.trim() == ""){
   	   				row.companyName = null;
   	   			}
   			}
   			
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
   		    var html = '<div class="modal fade" id="modelClose" tabindex="-1" role="dialog" aria-hidden="true"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter Company Name</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="close()">Filter</button> <button id="buttonClose" class="btn btnRed" ng-click="removeCompanyFulter()">Remove Filters</button></div></div></div></div></div></div>';
   		    $elm = angular.element(html);
   		    angular.element(document.body).prepend($elm);
   		    $compile($elm)($scope);
   		    $("#modelClose").modal('show');
   		  }
   		
		$scope.selectedCrm = [];
 		$scope.selectedCrmObj = "";
 		$scope.selectAllCheckOldNew = function(checked){
 			$scope.selectedCrm = [];
 			if(checked){
 				for(var i=0;i<$scope.gridOptionsNew.data.length;i++){
 					$scope.gridOptionsNew.data[i].checkBoxSelect = true;
 				}//checked = checked
 				angular.forEach($scope.gridOptionsNew.data, function(obj, index){
 					$scope.selectedCrm.push(obj.email);
       	  			$scope.selectedCrmObj = obj;
 	   			 });
 			}else{
 				for(var i=0;i<$scope.gridOptionsNew.data.length;i++){
 					$scope.gridOptionsNew.data[i].checkBoxSelect = false;
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
   		
 		$scope.companyNameData = {};
   		$scope.close = function() {
   		   // var ages = $scope.gridApi.selection.getSelectedRows();
   			$scope.companyNameData = $scope.gridApi.selection.getSelectedRows();
   			$scope.colFilter.listTerm = [];
   		    
   			$scope.companyNameData.forEach( function( companyName ) {
   				
   		      $scope.colFilter.listTerm.push( companyName.companyName );
   		    });
   		    
   		    $scope.colFilter.term = $scope.colFilter.listTerm.join(', ');
   		    console.log("$scope.colFilter.term----",$scope.colFilter.term);
   		    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
   		    
   		    if ($elm) {
   		      $elm.remove();
   		    }
   		  };
   		  
   		$scope.closepopUp = function(){
   			if ($elm) {
     		      $elm.remove();
     		    }
   		}  
   		  
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
						$scope.gridOptions.columnDefs.push({ name: name, displayName: value.label, width:'10%',cellEditableCondition: false,enableColumnMenu: false,
							filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal3 type="'+name+'"></div><input type="text" style="width:100px;margin-top: 7px;" ng-change="grid.appScope.searchFilterByType(text)" ng-model="text"></div>',
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
			  $scope.gridOptionsNew.data = data.newContact;
			  console.log($scope.gridOptionsNew.data);
			  $('#NewOldContact').modal('show');
			  $scope.functionForGrid($scope.gridOptionsNew.data);
			   // $scope.getContactsData();
		   });
	   }
	   
	   $scope.connectMShow = "0";
	   
	   $scope.functionForGrid = function(data){
		   $scope.gridOptionsNew.columnDefs = [
												 { name: 'isSelect', displayName: '#', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
													headerCellTemplate:	'<label style="margin-top: 5px; margin-left: 8px;">#</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input style="margin-top: 5px; margin-left: 8px;" type=\"checkbox\"  ng-model=\"checked\"  ng-change="grid.appScope.selectAllCheckOldNew(checked)" autocomplete="off">',
													cellTemplate:'<input type=\"checkbox\" ng-model=\"row.entity.checkBoxSelect\"  ng-click="grid.appScope.doActionOldNew(row,row.entity.checkBoxSelect)" autocomplete="off">',
												 }, 
  	   		                                 { name: 'type', displayName: 'Type',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
  	   		                                 },
  	   		                                 { name: 'salutation', displayName: 'Salutation',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
	   		                                 },
  	   		                                 { name: 'firstName', displayName: 'Name',headerCellClass: 'greenColor', width:'14%',enableFiltering: false,
  	   		                                 },
  	   		                                 { name: 'suffix', displayName: 'Suffix',headerCellClass: 'greenColor', width:'14%',enableFiltering: false,
	   		                                 },
  	   		                                 { name: 'companyName', displayName: 'Company Name',headerCellClass: 'greenColor', width:'14%',enableFiltering: false,
  	   		                                 },
  	   		                                 { name: 'email', displayName: 'Email',headerCellClass: 'greenColor', width:'15%',enableFiltering: false,
  	   		                                 },
  	   		                                 { name: 'phone', displayName: 'Phone',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
  	   		                                 },
	  	   		                             { name: 'city', displayName: 'City',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
		   		                             },
		   		                             { name: 'state', displayName: 'State',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
		   		                             },
		   		                             { name: 'zip', displayName: 'ZipCode',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
		   		                             },
		   		                             { name: 'allEmail', displayName: 'All Email',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
		   		                             },
		   		                             { name: 'allPhone', displayName: 'All Phone',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
		   		                             },
		   		                             { name: 'website', displayName: 'WebSite',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
		   		                             },
			   		                         { name: 'allAddresses', displayName: 'Address',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'title', displayName: 'Title',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'brithday', displayName: 'Brithday',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'backgroundInfo', displayName: 'BackGound Info',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'industry', displayName: 'Industry',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         }, 
			   		                         { name: 'creationDate', displayName: 'CreateDate',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'lastEditedDate', displayName: 'Last Edited Date',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'assignedTo', displayName: 'Assigned To',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         }, 
			   		                         { name: 'campaignSource', displayName: 'Campaign Source',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'priority', displayName: 'Priority',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },
			   		                         { name: 'groupsName', displayName: 'Groups',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },      
			   		                         { name: 'relationships', displayName: 'Relationships',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                         },        
			   		                         { name: 'notes', displayName: 'Notes',headerCellClass: 'greenColor',enableFiltering: false, width:'14%',
			   		                          },   
			   		     
  	   		                           
  	   		                               ];   
		    var flag = 0;$scope.connectMShow = "0";
		    console.log(data);
		   angular.forEach(data,function(obj,key){
			   if(flag == 0){
				   angular.forEach(obj.customData,function(obj1,key1){
					       $scope.connectMShow = "1";
						   var name = obj1.key;
						   $scope.flagValFlag = 1;
						   name = name.replace(" ","");
						   $scope.gridOptionsNew.columnDefs.push({ name: name, displayName: obj1.key,headerCellClass: 'redColor', width:'10%',enableFiltering: false,

						});
					});   
			   }
			   flag = 1;
			   
		   });
		   
		  
		   /*angular.forEach(data, function(value, key) {
			   
		   });*/
		   /*$scope.gridOptions.columnDefs.push({ name: 'phone', displayName: 'Phone',enableFiltering: false, width:'14%',
                });*/
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
	    			$scope.contactsDetails.birthday = $("#birthdayDate").val()
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
	    			$scope.contactsDetails.birthday = $("#birthday").val();
	    			console.log($("#birthday").val());
    				console.log($scope.contactsDetails);
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
				$scope.assignedFlag = 0;
	   		    $scope.listOfAges = [];
	   		 $scope.contactsList.forEach( function ( row ) {
	   		    	if($scope.listOfAges.indexOf( row.assignedToName ) === -1 ) {
	   		        	$scope.listOfAges.push( row.assignedToName );
	   		        	console.log(row.assignedToName);
	   		      	}
	   		    });
	   		    $scope.listOfAges.sort();
	   		    $scope.gridOptions = { 
	   		    	data: [],
	   		    	enableColumnMenus: false,
	   		    	onRegisterApi: function( gridApi) {
	   		        $scope.gridApi = gridApi;
	   		        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
	   		        	var msg = 'row selected ' + row.isSelected;
	   		        	if($scope.gridApi.selection.getSelectedRows().length >= 1){
	   		        		$scope.assignedFlag = 1;
	   		        	}else{
	   		        		$scope.assignedFlag = 0;
	   		        	}
	   		        });
	   		        
	   		        if($scope.colFilter && $scope.colFilter.listTerm ){
	   		        	$timeout(function() {
	   		        		$scope.colFilter.listTerm.forEach( function( assignedToName ) {
	   		        			var entities = $scope.gridOptions.data.filter( function( row ) {
	   		        				return row.assignedToName === assignedToName;
	   		        			}); 
	   		              
	   		        			if(entities.length > 0 ) {
	   		        				$scope.gridApi.selection.selectRow(entities[0]);
	   		        				console.log(entities[0]);
	   		        			}
	   		        		});
	   		        	});
	   		        }
	   		      } 
	   		    };
	   		    $scope.listOfAges.forEach(function( assignedToName ) {
	   		    	$scope.gridOptions.data.push({assignedToName: assignedToName});
	   		    });
	   		    console.log( $scope.gridOptions.data);
	   		    var html = '<div class="modal fade" id="modelClose" tabindex="-1" role="dialog" aria-hidden="true"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter Assigned To</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="checkAssigned()">Filter</button><button ng-show="assignedFlag == 1" id="buttonClose" class="btn btnRed" ng-click="removeFilters()">Remove Filters</button></div></div></div></div></div></div>';
	   		    $elm = angular.element(html);
	   		    angular.element(document.body).prepend($elm);
	   		    $compile($elm)($scope);
	   		    $("#modelClose").modal('show');
			}
			
			$scope.assignedTo = {};
			$scope.checkAssigned = function() {
				//var ages = $scope.gridApi1.selection.getSelectedRows();
				$scope.assignedTo = $scope.gridApi.selection.getSelectedRows();
				console.log($scope.assignedTo);
	   		    $scope.colFilter.listTerm = [];
	   		    $scope.assignedTo.forEach( function( assignedToName ) {
	   		    	$scope.colFilter.listTerm.push( assignedToName.assignedToName );
	   		    });
	   		    $scope.colFilter.term = $scope.colFilter.listTerm.join(', ');
	   		    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
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
	 	  
	 	  	$scope.doActionOldNew = function(row,checkBoxSelect){
	 	  		if(checkBoxSelect == undefined || checkBoxSelect == false){
	 	  			$scope.selectedCrm.push(row.entity.email);
	 	  			$scope.selectedCrmObj = row.entity;
	 	  		}else{
	 	  			$scope.deleteActionSelectedLeadOldNew(row.entity);
	 	  		}
	 	  		console.log($scope.selectedCrm);
	 	  	}
	 	  	
	 	  	$scope.deleteActionSelectedLeadOldNew = function(row){
	 	  		angular.forEach($scope.selectedCrm, function(obj, index){
	 	  			if ((row.email == obj)) {
	 	  				$scope.selectedCrm.splice(index, 1);
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
	 	  	$scope.changeds = 0;
	 	  	$scope.chnagesNameFunction = function(value){
	 	  		console.log(value);
	 	  		$scope.changedUser = value;
	 	  	}
	 		 $scope.changeAssignedUser = function() {
	 			 console.log($scope.changedUser);
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
	   			for(var i=0;i<$scope.gridOptionsNew.data.length;i++){
 					$scope.gridOptionsNew.data[i].checkBoxSelect = false;
 				}
	   			if(type == "new"){
	   				$scope.gridOptionsNew.data = $scope.oldNewContact.newContact;
	   			}else if(type == "old"){
	   				$scope.gridOptionsNew.data = $scope.oldNewContact.oldContact;
	   			}
	   		}
	   		
	 		$scope.ImportSelectedValue = function(){
                console.log($scope.oldNewContact);
                $scope.otherObj = {};
                $scope.otherObj.oldContact = [];
                $scope.otherObj.newContact = [];
                angular.forEach($scope.oldNewContact.oldContact, function(value, key) {
                        angular.forEach($scope.selectedCrm, function(value1, key1) {
                                   if(value.email == value1){
                                       $scope.otherObj.oldContact.push(value);
                                       }
                        });
               });
                angular.forEach($scope.oldNewContact.newContact, function(value, key) {
                        angular.forEach($scope.selectedCrm, function(value1, key1) {
                                   if(value.email == value1){
                                       $scope.otherObj.newContact.push(value);
                                       }
                        });
               });
                console.log($scope.otherObj);
                       console.log($scope.selectedCrm);
                apiserviceCrm.importContacts($scope.otherObj).then(function(data){
                         $('#NewOldContact').modal('hide');
                         $scope.getContactsData();
                  });

        }
	 		$scope.columnList = [];
 			$scope.obj = {};
	 		$scope.connecmanually = function(){
	 			console.log($scope.oldNewContact);
	 			$scope.columnList = [];
	 			  angular.forEach($scope.oldNewContact.otherColumnName, function(value, key) {
	 				$scope.columnList.push({"name":value,"match":""});
	 			  });
	 			 $("#showotherColumn").modal('show');
	 		}
	 		
	 		$scope.checkCondition= function(obj,obj1,obj2){
	 			 if(obj2.name == obj1.key){
	 					 if(obj2.match == "Last Name"){
	 						 obj.lastName = obj1.value;
	 					 }else if(obj2.match == "Middle Name"){
	 						 obj.middleName = obj1.value;
	 					 }else if(obj2.match == "First Name"){
	 						 obj.firstName = obj1.value;
	 					 }else if(obj2.match == "Salutation"){
	 						 obj.salutation = obj1.value;
	 					 }else if(obj2.match == "Type"){
	 						 obj.type = obj1.value;
	 					 }else if(obj2.match == "Suffix"){
	 						 obj.suffix = obj1.value;
	 					 }else if(obj2.match == "Company"){
	 						 obj.companyName = obj1.value;
	 					 }else if(obj2.match == "Primary Email"){
	 						 obj.email = obj1.value;
	 					 }else if(obj2.match == "Primary Phone"){
	 						 obj.phone = obj1.value;
	 					 }else if(obj2.match == "Primary Street"){
	 						 obj.street = obj1.value;
	 					 }else if(obj2.match == "Primary City"){
	 						 obj.city = obj1.value;
	 					 }else if(obj2.match == "Primary State"){
	 						 obj.state = obj1.value;
	 					 }else if(obj2.match == "Primary Zip"){
	 						 obj.zip = obj1.value;
	 					 }else if(obj2.match == "Primary Country"){
	 						 obj.country = obj1.value;
	 					 }else if(obj2.match == "All Email"){
	 						 obj.allEmail = obj1.value;
	 					 }else if(obj2.match == "All Phone"){
	 						 obj.allPhone = obj1.value;
	 					 }else if(obj2.match == "Website"){
	 						 obj.website = obj1.value;
	 					 }else if(obj2.match == "All Addresses"){
	 						 obj.allAddresses = obj1.value;
	 					 }else if(obj2.match == "Title"){
	 						 obj.title = obj1.value;
	 					 }else if(obj2.match == "Birthday"){
	 						 obj.birthday = obj1.value;
	 					 }else if(obj2.match == "Background Info"){
	 						 obj.backgroundInfo = obj1.value;
	 					 }else if(obj2.match == "Industry"){
	 						 obj.industry = obj1.value;
	 					 }else if(obj2.match == "# of Employees"){
	 						 obj.numberOfEmployees = obj1.value;
	 					 }else if(obj2.match == "Creation Date"){
	 						 obj.creationDate = obj1.value;
	 					 }else if(obj2.match == "Last Edited Date"){
	 						 obj.lastEditedDate = obj1.value;
	 					 }else if(obj2.match == "Assigned To"){
	 						 obj.assignedTo = obj1.value;
	 					 }else if(obj2.match == "Campaign Source"){
	 						 obj.campaignSource = obj1.value;
	 					 }else if(obj2.match == "Priority"){
	 						 obj.priority = obj1.value;
	 					 }else if(obj2.match == "Groups"){
	 						 obj.groupsName = obj1.value;
	 					 }else if(obj2.match == "Relationships"){
	 						 obj.relationships = obj1.value;
	 					 }else if(obj2.match == "Notes"){
	 						 obj.notes = obj1.value;
	 					 }
	 					 
	 					 
	 				 }
	 		}

	 		$scope.connecmanuallyMatch = function(){
	 			console.log($scope.oldNewContact);
	 			console.log($scope.obj);
	 			console.log($scope.columnList);
	 			console.log($scope.gridOptionsNew.columnDefs);
	 			var lessFlag = $scope.gridOptionsNew.columnDefs.length - 1;
	 			
	 			angular.forEach($scope.columnList, function(obj2, key2) {
	 				console.log(lessFlag);
	 				$scope.gridOptionsNew.columnDefs.splice(lessFlag, 1);
	 				lessFlag--;
	 			});
	 			console.log($scope.gridOptionsNew.columnDefs);
	 				 angular.forEach($scope.oldNewContact.newContact, function(obj, key) {
	 					angular.forEach(obj.customData, function(obj1, key1) {
	 						 angular.forEach($scope.columnList, function(obj2, key2) {
	 							  $scope.checkCondition(obj,obj1,obj2);
	 							
	 						 });
	 					});
	 					
	 				 });
	 				 angular.forEach($scope.oldNewContact.oldContact, function(obj, key) {
		 					angular.forEach(obj.customData, function(obj1, key1) {
		 						 angular.forEach($scope.columnList, function(obj2, key2) {
		 							  $scope.checkCondition(obj,obj1,obj2);
		 							
		 						 });
		 					});
		 					
		 				 });
	 				 console.log($scope.oldNewContact.newContact);
	 				$scope.gridOptionsNew.data = $scope.oldNewContact.newContact;
	 				 $("#showotherColumn").modal('hide')
	 		}
	 		
	 $scope.ImportAllValue = function(){
		 console.log($scope.oldNewContact);
		 apiserviceCrm.importContacts($scope.oldNewContact).then(function(data){
			  $('#NewOldContact').modal('hide');
			  $scope.getContactsData();
		   });
	 }
	 $scope.removeFilters = function(){
		 $scope.assignedTo = $scope.gridApi.selection.getSelectedRows();
		    $scope.colFilter.listTerm = [];
		    console.log($scope.assignedTo);
		    $scope.assignedTo.forEach( function( assignedToName ) {
		    	$scope.colFilter.listTerm.splice( assignedToName.assignedToName );
		    });
		    $scope.colFilter.term = undefined;
		    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
		    if ($elm) {
		    	$elm.remove();
		    }
		 
		 /*if($elm){
  		      $elm.remove();
  		 }
		 $route.reload();
		 //$scope.getContactsData();
*/	 };
	 
	 $scope.removeCompanyFulter = function(){
		 $scope.companyNameData = $scope.gridApi.selection.getSelectedRows();
			$scope.colFilter.listTerm = [];
		    
			$scope.companyNameData.forEach( function( companyName ) {
		      $scope.colFilter.listTerm.splice( companyName.companyName );
		    });
		    
		    $scope.colFilter.term = undefined;
		    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
		 if($elm){
  		      $elm.remove();
  		 }
		// $route.reload();
		// $scope.getContactsData();
	 }
	 
	 
	 $scope.showTypeModal = function() {
		    $scope.listOfAges = [];
		    $scope.typeFlag = 0;
		    $scope.contactsList.forEach( function ( row ) {
		      if ( $scope.listOfAges.indexOf( row.type ) === -1 ) {
		        $scope.listOfAges.push( row.type );
		      }
		    });
		    $scope.listOfAges.sort();
		    
		    $scope.gridOptions = { 
		      data: [],
		      enableColumnMenus: false,
		      onRegisterApi: function( gridApi) {
		        $scope.gridApi = gridApi;
		        
		        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
   		        	var msg = 'row selected ' + row.isSelected;
   		        	if($scope.gridApi.selection.getSelectedRows().length >= 1){
   		        		$scope.typeFlag = 1;
   		        	}else{
   		        		$scope.typeFlag = 0;
   		        	}
   		        });
		        
		        if ( $scope.colFilter && $scope.colFilter.listTerm ){
		          $timeout(function() {
		            $scope.colFilter.listTerm.forEach( function( type ) {
		              var entities = $scope.gridOptions.data.filter( function( row ) {
		                return row.type === type;
		              });
		              
		              if( entities.length > 0 ) {
		                $scope.gridApi.selection.selectRow(entities[0]);
		              }
		            });
		          });
		        }
		      } 
		    };
		    
		    $scope.listOfAges.forEach(function( type ) {
		      $scope.gridOptions.data.push({type: type});
		    });
		    
		    var html = '<div class="modal fade" id="modelClose" tabindex="-1" role="dialog" aria-hidden="true"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter Of Type</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="closeType()">Filter</button> <button ng-show="typeFlag == 1" id="buttonClose" class="btn btnRed" ng-click="removeTypeFilter()">Remove Filters</button></div></div></div></div></div></div>';
		    $elm = angular.element(html);
		    angular.element(document.body).prepend($elm);
		 
		    $compile($elm)($scope);
		    $("#modelClose").modal('show');
		  }
	 
	 	$scope.typeToData = {};
	 	$scope.closeType = function() {
	 			$scope.typeToData = $scope.gridApi.selection.getSelectedRows();
			    $scope.colFilter.listTerm = [];
			    
			    $scope.typeToData.forEach( function( type ) {
			      $scope.colFilter.listTerm.push( type.type );
			    });
			    
			    $scope.colFilter.term = $scope.colFilter.listTerm.join(', ');
			    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
			    if ($elm) {
			      $elm.remove();
			    }
		  };
		  
		  $scope.removeTypeFilter = function(){
			  $scope.typeToData = $scope.gridApi.selection.getSelectedRows();
			    $scope.colFilter.listTerm = [];
			    
			    $scope.typeToData.forEach( function( type ) {
			      $scope.colFilter.listTerm.splice( type.type );
			    });
			    
			    $scope.colFilter.term = undefined;
			    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
			    if ($elm) {
			      $elm.remove();
			    }
		  }
		  
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
		  
	   		$scope.searchFilterByIndustry = function(text){
	   			console.log(text);
	   			if(text == ''){
	   				$scope.gridOptions.data = $scope.contactsList;
	   			}else{
	   			compData = [];
	   			$scope.contactsList.forEach( function addDates( row, index ){
	   				if(row.industry){
	   					if(row.industry.includes(text)){
	   						compData.push(row);
	   					}
	   				}
	   			});
	   			$scope.gridOptions.data = compData;
	   			}
	   		}
	   		
		  $scope.searchFilterByCity = function(text){
	   			console.log(text);
	   			if(text == ''){
	   				$scope.gridOptions.data = $scope.contactsList;
	   			}else{
	   			compData = [];
	   			$scope.contactsList.forEach( function addDates( row, index ){
	   				if(row.city){
	   					if(row.city.includes(text)){
	   						compData.push(row);
	   					}
	   				}
	   			});
	   			$scope.gridOptions.data = compData;
	   			}
	   		}
		  
		  $scope.searchFilterByState = function(text){
	   			console.log(text);
	   			if(text == ''){
	   				$scope.gridOptions.data = $scope.contactsList;
	   			}else{
	   			compData = [];
	   			$scope.contactsList.forEach( function addDates( row, index ){
	   				if(row.state){
	   					if(row.state.includes(text)){
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
		  
		  
		  $scope.showDyanemicModal = function(colType) {
			  console.log(colType);
			    $scope.listOfAges = [];
			    $scope.dynamicFlag = 0;
			    $scope.col.grid.appScope.gridOptions.data.forEach( function ( row ) {
			      if ( $scope.listOfAges.indexOf( row[colType] ) === -1 ) {
			        $scope.listOfAges.push( row[colType] );
			      }
			    });
			    $scope.listOfAges.sort();
			    
			    $scope.gridOptions = { 
			      data: [],
			      enableColumnMenus: false,
			      onRegisterApi: function( gridApi) {
			        $scope.gridApi = gridApi;
			        
			        gridApi.selection.on.rowSelectionChanged($scope, function (row) {
	   		        	var msg = 'row selected ' + row.isSelected;
	   		        	if($scope.gridApi.selection.getSelectedRows().length >= 1){
	   		        		$scope.dynamicFlag = 1;
	   		        	}else{
	   		        		$scope.dynamicFlag = 0;
	   		        	}
	   		        });
			        
			        if ( $scope.colFilter && $scope.colFilter.listTerm ){
			          $timeout(function() {
			            $scope.colFilter.listTerm.forEach( function( type ) {
			              var entities = $scope.gridOptions.data.filter( function( row ) {
			                return row.type === type;
			              });
			              
			              if( entities.length > 0 ) {
			                $scope.gridApi.selection.selectRow(entities[0]);
			              }
			            });
			          });
			        }
			      } 
			    };
			    
			    $scope.listOfAges.forEach(function( type ) {
			      $scope.gridOptions.data.push({type: type});
			    });
			    
			    var html = '<div class="modal fade" id="modelClose" tabindex="-1" role="dialog" aria-hidden="true"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions" ui-grid-selection class="modalGrid"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="selectDyanemicType()">Filter</button> <button ng-show="dynamicFlag == 1" id="buttonClose" class="btn btnRed" ng-click="removeDyanemicFilter()">Remove Filters</button></div></div></div></div></div></div>';
			    $elm = angular.element(html);
			    angular.element(document.body).prepend($elm);
			 
			    $compile($elm)($scope);
			    $("#modelClose").modal('show');
			    
			  }
		  
		  $scope.removeDyanemicFilter = function(){
			  $scope.typeToData = $scope.gridApi.selection.getSelectedRows();
			    $scope.colFilter.listTerm = [];
			    
			    /*$scope.typeToData.forEach( function( type ) {
			      $scope.colFilter.listTerm.splice( type.type );
			    });*/
			    
			    $scope.colFilter.term = undefined;
			    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
			    if ($elm) {
			      $elm.remove();
			    }
		  }
		  $scope.selectDyanemicType = function() {
	 			$scope.typeToData = $scope.gridApi.selection.getSelectedRows();
			    $scope.colFilter.listTerm = [];
			    
			    $scope.typeToData.forEach( function( type ) {
			      $scope.colFilter.listTerm.push( type.type );
			    });
			    
			    $scope.colFilter.term = $scope.colFilter.listTerm.join(', ');
			    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
			    if ($elm) {
			      $elm.remove();
			    }
		  };
		  
}]);