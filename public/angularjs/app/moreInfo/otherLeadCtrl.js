angular.module('newApp')
.controller('otherLeadCtrl', ['$scope','$http','$location','$filter','$interval','$routeParams','apiserviceMoreInfo','$q','$rootScope','$compile','$timeout', function ($scope,$http,$location,$filter,$interval,$routeParams,apiserviceMoreInfo,$q,$rootScope,$compile,$timeout) {
	
	$scope.leadId = $routeParams.leadId;
	$scope.userList = [];
	$scope.leadList = [];
	$scope.cancelFlag = 0;
	$scope.actionSelectedLead = [];
	$scope.scheduLeadId = [];
	$scope.customData = {};
	$scope.userFields = [];
	$scope.tabInfo = $routeParams.tabInfo;
	console.log(userKey);
	apiserviceMoreInfo.getSelectedLeadType().then(function(response){
	
		angular.forEach(response, function(value, key) {
			if(value.callToAction){
				$scope.leadList.push(value); 
			}else{
				if(value.hideTab != "0"){
					$scope.leadList.push(value); 
				}
			}
			if(value.id == $scope.leadId){
				$scope.leadName = value.leadName;
				$scope.leadId = value.id;
			}
		});
	});
	$scope.changeTabInfo = function(tabInfo){
			console.log(tabInfo);
			$scope.tabInfo = tabInfo;
			$scope.getAllRequestInfo();
	}
	
  $scope.gridOptions = {
 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
 		    paginationPageSize: 150,
 		    enableFiltering: true,
 		    useExternalFiltering: true,
 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
 		 };
  //cellTemplate:'<input type="checkbox" ng-change="grid.appScope.selectUser(row)" ng-model="row.entity.isSelect" ng-show="row.entity.disabled" > <input type="checkbox" ng-change="grid.appScope.selectUserPop(row)" ng-model="row.entity.isSelect" ng-if="row.entity.disabled == false">',
 		 $scope.gridOptions.enableHorizontalScrollbar = 2;
 		 $scope.gridOptions.enableVerticalScrollbar = 2;
 		 $scope.gridOptions.columnDefs = [
										{ name: 'isSelect', displayName: 'Select', width:'5%',cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
											/*headerCellTemplate:	'<label style="margin-top: 5px; margin-left: 8px;">Select</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input style="margin-top: 5px; margin-left: 8px;" type=\"checkbox\"  ng-model=\"checker.checked\"  ng-change="grid.appScope.selectAllCheck(checker.checked)" autocomplete="off">',*/
											filterHeaderTemplate: '<div class="ui-grid-filter-container" ng-repeat="colFilter in col.filters"><div my-custom-modal-req></div></div>',
											cellTemplate:'<input type=\"checkbox\" ng-model=\"row.entity.checkBoxSelect\" ng-click="grid.appScope.doAction(row,row.entity.checkBoxSelect)" autocomplete="off">',
										},  
										{ name: 'title', displayName: 'Title', width:'12%',enableColumnMenu: false,cellEditableCondition: false,
										 	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
										        if (row.entity.isRead === false) {
										          return 'red';
										        }
										 	} ,
										  },
										
 		                                 { name: 'name', displayName: 'Name', width:'9%',enableColumnMenu: false,cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'10%',enableColumnMenu: false,cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'requestDate', displayName: 'Request Date',enableFiltering: false, width:'10%',enableColumnMenu: false,cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                { name: 'price', displayName: 'Price',enableFiltering: false, width:'5%',enableColumnMenu: false,cellEditableCondition: false,
 		                                	cellTemplate:'<div ng-if="row.entity.price != 0">{{row.entity.price}}</div><div ng-if="row.entity.price == 0">N/A</div>',
	   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    		                                       if (row.entity.isRead === false) {
	    		                                         return 'red';
	    		                                     }
	   		                                	} ,
	   		                             },
 		                                { name: 'salesRep', displayName: 'Sales Rep',enableFiltering: false, width:'10%',enableColumnMenu: false,cellEditableCondition: false,
  		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
  		                                 },
  		                               { name: 'status', displayName: 'Status',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: false,
  		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
  		                                 },
  		                               { name: 'message', displayName: 'Message',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: false,
   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
    		                                       if (row.entity.isRead === false) {
    		                                         return 'red';
    		                                     }
   		                                	} ,
   		                                 },
   		                              { name: 'confirmTime', displayName: 'Time',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: false,
    		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
     		                                       if (row.entity.isRead === false) {
     		                                         return 'red';
     		                                     }
    		                                	} ,
    		                                 },
     		                                 ];
  
 		$scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		          $scope.actionSelectedLead = [];
		   		 	$scope.actionSelectedLeadObj = "";
			   			 if(grid.columns[0].filters[0].term == "Select All"){
			   				 $scope.selectAllCheck(true);
			   			 }else if(grid.columns[0].filters[0].term == "Select All, Select unclaimed"){
			   				$scope.selectAllCheck(true);
			   			 }else if(grid.columns[0].filters[0].term == "Select All, Select unclaimed"){
			   				$scope.selectAllCheck(true);
			   			 }else if(grid.columns[0].filters[0].term == "Select unclaimed"){
			   				$scope.selectUnclaimed();
			   			 }else{
			   				$scope.gridOptions.data = $filter('filter')($scope.requsetMoreList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'stock':grid.columns[3].filters[0].term,'name':grid.columns[4].filters[0].term},undefined);
			   			 }
		          
		          
		        });
	   		
  		};
  		apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data){
  			var allData = [];
  		  if($scope.tabInfo == "All Requests"){
		  		allData = data;
		  	}else {
		  		 angular.forEach(data, function(obj, index){
					  	if($scope.tabInfo == "Only unclaimed"){
					  		if(obj.isRead == false){
					  			allData.push(obj);
					  		}
					  	}else if($scope.tabInfo == "Only claimed"){
					  		if(obj.isRead == true){
					  			allData.push(obj);
					  		}
					  	}
				  });
		  	}
  			
			$scope.editgirdData(allData);
			$scope.gridOptions.data = $filter('orderBy')($scope.gridOptions.data,'status');
			$scope.gridOptions.data = $scope.gridOptions.data.reverse();
			
			$scope.requsetMoreList = data;
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
				if($scope.userRole == "Sales Person"){
					$scope.premiumFlagForSale = data[0].premiumFlagForSale
				}
			}
		});
	  
	  $scope.editgirdData = function(data){
			  $scope.gridOptions.data = data;
			  $scope.gridMapObect = [];
				var findFlag = 0;
				
				$scope.josnData1 = [];
				if($scope.gridOptions.data.length > 0){
					angular.forEach(angular.fromJson($scope.gridOptions.data[0].customizDataValue.jsonData),function(value,key){
						$scope.josnData1.push(value);
					});
				}
			
				angular.forEach($scope.gridOptions.data,function(value,key){
						angular.forEach(value.customData,function(value1,key1){
							angular.forEach($scope.josnData1,function(value2,key2){
								if(value1.key == value2.key){
									for(var i=0;i<$scope.gridMapObect.length;i++){
										if($scope.gridMapObect[i].key != value2.key){
											$scope.gridMapObect.push({values: value1.value , key: value1.key, label: value2.label});
										}
									}
   									if($scope.gridMapObect.length == 0){
   										$scope.gridMapObect.push({values: value1.value , key: value1.key, label: value2.label});
   									}
   								}
							});
						});
				});
				$scope.viewPdfFlag = 0;
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
					if(value.viewPdfId == 0){
						$scope.viewPdfFlag = 1;
					}
				});	
				
				$scope.gridMapObectView = [];
				angular.forEach($scope.gridMapObect,function(value,key){
					var flag1 = 0;
					 for(var i=0;i<$scope.gridMapObectView.length;i++){
						 if($scope.gridMapObectView[i].key == value.key){
							 flag1 = 1;
						 }
					 }
					 
					 if(key == 0){
						 $scope.gridMapObectView.push(value);
					 }else{
						 if(flag1 == 0){
							 $scope.gridMapObectView.push(value);
						 }
					 }
					 
				});
				
				if($scope.viewPdfFlag == 0){
					$scope.gridOptions.columnDefs.push({ name: 'viewPdf', displayName: 'View',enableColumnMenu: false,enableFiltering: false, width:'5%',cellEditableCondition: false,
	                  	cellTemplate:'<a href="/showPdf/{{row.entity.id}}" target="_blank">View</a>',
	                        	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	                                if (row.entity.isRead === false) {
	                                  return 'red';
	                              }
	                        	} ,
	                     });
				}
				
				angular.forEach($scope.gridMapObectView,function(value,key){
					var name = value.key;
					name = name.replace(" ","");
					$scope.gridOptions.columnDefs.push({ name: name, displayName: value.label, width:'10%',enableColumnMenu: false,cellEditableCondition: false,
		              	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		              		if (row.entity.isRead === false) {
	                            return 'red';
	                        }
		              	} ,
		               });
				});
				
				/*$scope.gridOptions.columnDefs.push({ name: 'isRead', displayName: 'Claim',enableFiltering: false, width:'7%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity.id)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="margin-left:18%;"></div>', 
                  	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                          if (row.entity.isRead === false) {
                            return 'red';
                        }
                  	} ,
                   });*/
		  }
	  
	  $scope.getAllRequestInfo = function() {
		  apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data){
			  var allData = [];
			  if($scope.tabInfo == "All Requests"){
			  		allData = data;
			  	}else {
			  		 angular.forEach(data, function(obj, index){
						  	if($scope.tabInfo == "Only unclaimed"){
						  		if(obj.isRead == false){
						  			allData.push(obj);
						  		}
						  	}else if($scope.tabInfo == "Only claimed"){
						  		if(obj.isRead == true){
						  			allData.push(obj);
						  		}
						  	}
					  });
			  	}
			 
				$scope.editgirdData(allData);
			$scope.requsetMoreList = allData;
		});
	  
	  }
	  
	  /*var promo =  $interval(function(){
		  apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data){
					$scope.editgirdData(data);
				$scope.requsetMoreList = data;
			});
	  },60000);*/
	  
	  $scope.premiumFlagForSale = 0;
  /*$scope.setAsRead = function(flag,id) {
	  apiserviceMoreInfo.requestInfoMarkRead(flag, id).then(function(data){
	  
			apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data1){
			
			$scope.gridOptions.data = data1;
			$scope.requsetMoreList = data1;
			if(data1.length > 0){
				$scope.userRole = data1[0].userRole;
				if($scope.userRole == "Sales Person"){
					$scope.premiumFlagForSale = data1[0].premiumFlagForSale
				}
			}
		});
			$scope.$emit('getCountEvent', '123');
	});
	  $scope.getAllRequestInfo();
  }*/
  
  $scope.setAsRead = function() {
	 	  console.log($scope.leadType);
	  apiserviceMoreInfo.changeAssignedUser($scope.actionSelectedLead.toString(), userKey, $scope.leadType).then(function(data){
   		$("#assignUserModal").modal("hide");
   		$scope.actionSelectedLead = [];
   		$scope.getAllRequestInfo();
	});
  }
  $scope.showButton = "0";
  $scope.actionSelectedLead = [];
	  	$scope.actionSelectedLeadObj = "";
	  	$scope.doAction = function(row,checkBoxSelect){
	  		if(checkBoxSelect == undefined || checkBoxSelect == false){
	  			$scope.actionSelectedLead.push(row.entity.id);
	  		$scope.actionSelectedLeadObj = row.entity;
	  		}else{
	  			$scope.deleteActionSelectedLead(row.entity);
	  		}
	  		var isReadValue = 0;
	  		var notReadValue = 0;
	  		angular.forEach($scope.gridOptions.data, function(obj, index){
	  			angular.forEach($scope.actionSelectedLead, function(obj1, index1){
	  					if(obj1 == obj.id){
	  						if(obj.isRead === false){
	  							isReadValue = 1;
	  						}else{
	  							notReadValue = 1;
	  						}
	  					}
	  			});
	   		});
	  		if(isReadValue == 1 && notReadValue == 1){
	  			$scope.showButton = "1";
	  		}else if(notReadValue == 1 && isReadValue == 0){
	  			$scope.showButton = "1";
	  		}else{
	  			$scope.showButton = "0";
	  		}
	  		
	  		
	  		console.log($scope.actionSelectedLead);
	  	}
	  	$scope.deleteActionSelectedLead = function(row){
			angular.forEach($scope.actionSelectedLead, function(obj, index){
				 if ((row.id == obj)) {
					 $scope.actionSelectedLead.splice(index, 1);
			       	return;
			    };
			  });
	  	}
	
 		$scope.selectAllCheck = function(checked){
 			console.log(checked);
 			
 			if(checked){
 				for(var i=0;i<$scope.gridOptions.data.length;i++){
 					$scope.gridOptions.data[i].checkBoxSelect = true;
 				}//checked = checked
 				angular.forEach($scope.gridOptions.data, function(obj, index){
 					$scope.actionSelectedLead.push(obj.id);
       	  			$scope.actionSelectedLeadObj = obj;
 	   			 });
 			}else{
 				for(var i=0;i<$scope.gridOptions.data.length;i++){
 					$scope.gridOptions.data[i].checkBoxSelect = false;
 				}
   	  			$scope.deleteActionSelected($scope.actionSelectedLead);
 				
 			}
 			
 			var isReadValue = 0;
	  		var notReadValue = 0;
	  		angular.forEach($scope.gridOptions.data, function(obj, index){
	  			angular.forEach($scope.actionSelectedLead, function(obj1, index1){
	  					if(obj1 == obj.id){
	  						if(obj.isRead === false){
	  							isReadValue = 1;
	  						}else{
	  							notReadValue = 1;
	  						}
	  					}
	  			});
	   		});
	  		
	  		if(isReadValue == 1 && notReadValue == 1){
	  			$scope.showButton = "1";
	  		}else{
	  			$scope.showButton = "0";
	  		}
 			console.log($scope.actionSelectedLead);
 			console.log($scope.gridOptions.data);
 		}
 		
 		$scope.selectUnclaimed = function(){
 			$scope.showButton = "0";
 				for(var i=0;i<$scope.gridOptions.data.length;i++){
 					if($scope.gridOptions.data[i].isRead === false){
 						$scope.gridOptions.data[i].checkBoxSelect = true;
 					}else{
 						$scope.gridOptions.data[i].checkBoxSelect = false;
 					}
 					
 				}
 				angular.forEach($scope.gridOptions.data, function(obj, index){
 					if(obj.isRead === false){
 						$scope.actionSelectedLead.push(obj.id);
 	       	  			$scope.actionSelectedLeadObj = obj;
 					}else{
 						//$scope.deleteActionSelected($scope.actionSelectedLead);
 					}
 	   			 });
 			console.log($scope.actionSelectedLead);
 			console.log($scope.gridOptions.data);
 		}
 		
 		
 		$scope.deleteActionSelected = function(objList){
   				 if ((objList == $scope.actionSelectedLead)) {
   					 $scope.actionSelectedLead = [];
   			       	return;
   			    };
   	  	}	
 		
 		
 		$scope.assignCanceledLead = function() {
        	$scope.changedUser = "";
        	$scope.getSalesDataValue(16);
 			$("#assignUserModal").modal("show");
        }	
	  	
 		
 		$scope.getSalesDataValue = function(locationValue){
 			apiserviceMoreInfo.getSalesUserOnly(locationValue).then(function(data){
    			$scope.salesPersonPerf = data;
    			console.log(data);
    			angular.forEach($scope.salesPersonPerf, function(value, key) {
    				value.isSelected = false;
    			});
    		});
 		}
 		
 		 $scope.changeAssignedUser = function() {
 			apiserviceMoreInfo.changeAssignedUser($scope.actionSelectedLead.toString(), $scope.changedUser, $scope.leadType).then(function(data){
         		$("#assignUserModal").modal("hide");
         		$scope.getAllRequestInfo();
 			});
         	
         }
 		 
 		$scope.showAgeModalReq = function() {
   		console.log("99999999999999999");
   		    
   		    $scope.gridOptions1 = { 
   		      data: [],
   		      enableColumnMenus: false,
   		      onRegisterApi: function( gridApi) {
   		        $scope.gridApi = gridApi;
   		        
   		        if ( $scope.colFilter && $scope.colFilter.listTerm ){
   		          $timeout(function() {
   		            $scope.colFilter.listTerm.forEach( function( companyName ) {
   		              var entities = $scope.gridOptions1.data.filter( function( row ) {
   		                return "SelectAll" === "SelectAll";
   		              });
   		              
   		              if( entities.length > 0 ) {
   		                $scope.gridApi.selection.selectRow(entities[0]);
   		              }
   		            });
   		          });
   		        }
   		      } 
   		    };
   		      $scope.gridOptions1.data.push({selectValue: "Select All"});
   		   $scope.gridOptions1.data.push({selectValue: "Select unclaimed"});
   		    
   		     var html = '<div class="modal" ng-style="{display: \'block\'}"><div class="modal-dialog"><div class="modal-content"><div class="modal-header">Filter</div><div class="modal-body"><div id="grid1" ui-grid="gridOptions1" ui-grid-selection class="modalGrid" style="height: 180px;"></div></div><div class="modal-footer"><button id="buttonClose" class="btn btn-primary" ng-click="close()">Filter</button></div></div></div></div>';
   		    $elm = angular.element(html);
   		    angular.element(document.body).prepend($elm);
   		 
   		    $compile($elm)($scope);
   		    
   		  }
 		
 		$scope.close = function() {
   		    var ages = $scope.gridApi.selection.getSelectedRows();
   		    console.log(ages);
   		 $scope.colFilter.listTerm = [];
   		    
	   		
	   		ages.forEach( function( selectValue ) {
	   			console.log(selectValue);
  		      $scope.colFilter.listTerm.push( selectValue.selectValue );
  		    });
  		    
  		    $scope.colFilter.term = $scope.colFilter.listTerm.join(', ');
  		    $scope.colFilter.condition = new RegExp($scope.colFilter.listTerm.join('|'));
  		   if ($elm) {
   		      $elm.remove();
   		    }
   		 };
 		 
 		$scope.cancelScheduleStatus = function() {
    		var flag = 0;
    	
    		
    		$("#scheduleCancelModal").modal("show");
    	}
 		
 		  $scope.showFomeD = function(type){
   	  		  if(type == "createLead"){
   	  			 $scope.addAddress = true;
    			 $scope.editAddress = false;
    			 $scope.historyAddressBar = false;
    			 $scope.cancellingAddress = false;
    			 $scope.scheduleAddress = false;
    			 $scope.appointmentFlag = false;
    			 $scope.createContactFlag = false;
   	  		  }else if(type == "Canceling lead"){
   	  		     $scope.addAddress = false;
				 $scope.editAddress = false;
				 $scope.historyAddressBar = false;
				 $scope.cancellingAddress = true;
				 $scope.scheduleAddress = false;
				 $scope.appointmentFlag = false;
				 $scope.createContactFlag = false;
   	  		  }else if(type == "Create Contact"){
   	  			 $scope.addAddress = false;
   				 $scope.editAddress = false;
   				 $scope.historyAddressBar = false;
   				 $scope.cancellingAddress = false;
   				 $scope.scheduleAddress = false;
   				 $scope.appointmentFlag = false;
   				 $scope.createContactFlag = true;
      	  		  }
   	  		
   	  	  }  
 		
 		$scope.getFormDesign = function(formType){
			var deferred = $q.defer();
			
			apiserviceMoreInfo.getCustomizationform(formType).then(function(response){
    			console.log(angular.fromJson(response.jsonData));
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
				 console.log($scope.userList);
				 deferred.resolve(response);
		   });
			
			return deferred.promise;
		}
 		 
 		$scope.allCanFlag = 0;
    	$scope.cancelAllLead = function(value){
    		if(value == undefined || value == false){
    			$scope.allCanFlag = 1;
    			$scope.leadDetId = $scope.actionSelectedLead;
    		}else{
    			$scope.allCanFlag = 0;
    			$scope.leadDetId = [];
    		}
    	}
    	$scope.cancelAllLeadSchedule = function(value){
    		if(value == undefined || value == false){
    			$scope.allCanFlag = 1;
    			$scope.leadDetId = $scope.actionSelectedLead;
    		}else{
    			$scope.allCanFlag = 0;
    			$scope.leadDetId = [];
    		}
    	}
    	
    	$scope.cancelSure = function(reasonToCancel){
    		$("#scheduleCancelModal").modal("hide");
    			$scope.saveScheduleClose(reasonToCancel);
    		
    	}
 		 
    	$scope.saveScheduleClose = function(reasonToCancel) {
    		$scope.closeleadObj = {};
    		$scope.closeFlg++;
    					var oneProduct = 0;
    					
        			
        			$scope.closeleadObj.actionSelectedLead = $scope.actionSelectedLead;
    	    		$scope.closeleadObj.reasonToCancel = reasonToCancel;
    	    		console.log($scope.closeleadObj);
    	    		$scope.reasonFlag = 0;
    					if($scope.closeleadObj.reasonToCancel != ""){
    						$scope.reasonFlag = 0;
    						apiserviceMoreInfo.setScheduleStatusClose($scope.closeleadObj).then(function(data){
    							
    							if($scope.closeleadObj.actionSelectedLead.length == 1){
    								$.pnotify({
    		    					    title: "Success",
    		    					    type:'success',
    		    					    text: $scope.closeleadObj.actionSelectedLead.length+ " lead has been deleted",
    		    					});
    							}else{
    								$.pnotify({
    		    					    title: "Success",
    		    					    type:'success',
    		    					    text: $scope.closeleadObj.actionSelectedLead.length+ " leads have been deleted",
    		    					});
    							}
    		    	    		 
    							$scope.getAllRequestInfo();
    		    	    		
    		    	    		  $scope.reasonToCancel = "";
    		    				});
    						
    		    	    	  	if($scope.actionSelectedLead.length == $scope.closeFlg){
    		    	    	  		$scope.actionSelectedLead = [];
    		    	    	  		$("#scheduleCancelModal").modal("hide");
    		    	    	  }
    					}else{
    						$scope.reasonFlag = 1;
    					}
    	    	
    			
    				
    		
    	}
    	
    	
    	$scope.getCreateCustomList = function(customeDataList , josnData){
			 var deferred = $q.defer();
			 var addrs = 0;
			 var fileFlag = 0;
			 var timeRangs = 0;
			 $scope.dateFlag = 0
			$scope.customList = [];
			 console.log(customeDataList);
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
    	
    	$scope.multiSelectBindWithCustomData = function(){
			if($rootScope.rObj != undefined){
				$.each($rootScope.rObj, function(attr, value) {
    				$scope.customData[attr] = value;
    			});
			}
			if($rootScope.productArrayList != undefined){
				$scope.customData["productList"] = $rootScope.productArrayList.toString();
			}
			
			if($rootScope.subColl != undefined){
    				$scope.customData["subCollection"] = $rootScope.subColl;
			}
		}
    	
    	apiserviceMoreInfo.getUsers().then(function(data){
			  $scope.allUser = data;
			 });
    	
    	$scope.createContact = function(entity) {
			  $scope.contactsDetails = {};
			  $scope.contactsDetails.workEmail = "Work";
			   $scope.contactsDetails.workEmail1 = "Work";
			   $scope.contactsDetails.workPhone = "Work";
			   $scope.contactsDetails.workPhone1 = "Work";
			  $scope.contactMsg = "";
			  $scope.contactsDetails.firstName = entity.name;
			  $scope.contactsDetails.email = entity.email;
			  $scope.contactsDetails.phone = entity.phone;
			  
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
			   $scope.showFomeD("Create Contact");
			  $('#createcontactsModal').modal();
		  }
    	
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
	   		apiserviceMoreInfo.getCustomizationform('Create New Lead').then(function(response){
	   			   $scope.josnData = angular.fromJson(response.jsonData);
	   			   angular.forEach($scope.josnData, function(obj, index){
	   			   obj.formName = "Create New Lead";
	    		});
	    				
	    		$scope.josnData1 = null;
	    		apiserviceMoreInfo.getCustomizationform('New Contact').then(function(response1){
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
	    								$scope.contactsDetails.manufacturers = value;
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
	    			apiserviceMoreInfo.saveContactsData($scope.contactsDetails).then(function(data){
	    					$scope.actionSelectedLead = [];
	    						if(data == "") {
	    							
	    							$('#createcontactsModal').modal('hide');
	    						}else {
	    							$scope.contactMsg = data;
	    						}
	    				});
	    			});
				});
		   }
    	
    	$scope.inviteNewUser = function(){
    		console.log("edit");
    		$location.path('/createUser/external');
    	}
    	
	$scope.testDrive = function() {
		$location.path('/scheduleTest');
	}  
  
	$scope.tradeIn = function() {
		$location.path('/tradeIn');
	}
	
	$scope.goPremium = function() {
		$location.path('/premiumpage');
	}
	
	$scope.goContactUs = function() {
		$location.path('/contactUsInfo');
	}
	$scope.otherLeads = function(leads) {
		$location.path('/otherLeads/'+leads.id+"/"+$scope.tabInfo);
	}
	$scope.requestMore = function() {
		$location.path('/requestMoreInfo');
	} 
  
	$scope.exportCsvPop = function(){
		$('#exportModal').modal('show');
	};
	
	$scope.exportCsvOtherLead = function(){
		apiserviceMoreInfo.exportOtherLeadsData($scope.leadId).then(function(data){
				$.fileDownload('/downloadRequestMoreFile',
					{	   	
					}).done(function(e, response)
							{
								$.pnotify({
											title: "Success",
											type:'success',
											text: "File download successfully",
								});
							}).fail(function(e, response)
							{
							});
			});
		}
	
}]);