angular.module('newApp')
.controller('viewInventoryCtrl', ['$scope','$http','$location','$filter','apiserviceViewInventory', function ($scope,$http,$location,$filter,apiserviceViewInventory) {
	$scope.tempDate = new Date().getTime();
	$scope.type = "All";
	$scope.vType;
	$scope.doPublic = 0;
     $scope.gridOptions = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    paginationPageSize: 150,
    		    enableFiltering: true,
    		    enableCellEditOnFocus: true,
    		    useExternalFiltering: true,
    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		 };
    		 $scope.gridOptions.enableHorizontalScrollbar = 0;
    		 $scope.gridOptions.enableVerticalScrollbar = 2;
    		 $scope.gridOptions.columnDefs = [
    		                                 { name: 'title', displayName: 'Title',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
    		                                	 cellTemplate: '<div> <a style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		                                 },
    		                                 { name: 'description', displayName: 'Description',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
    		                                 },
    		                                 { name: 'fileName', displayName: 'Logo',enableColumnMenu: false, width:'15%',
    		                                 },
    		                                 { name: 'countImages', displayName: 'Images',enableColumnMenu: false,enableFiltering: false, width:'15%',cellEditableCondition: false,
    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.gotoImgTag(row)" style="line-height: 200%;" title="" data-content="{{row.entity.countImages}}">{{row.entity.countImages}}</a></div>',
    		                                 },
    		                                 { name: 'pageViewCount', displayName: 'Views',enableColumnMenu: false,enableFiltering: false, width:'10%',cellEditableCondition: true,
    		                                 },
    		                                 /*{ name: 'city_mileage', displayName: 'City MPG',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: true,
    		                                 },
    		                                 { name: 'highway_mileage', displayName: 'HWY MPG',enableFiltering: false, enableColumnMenu: false,width:'8%',cellEditableCondition: true,
    		                                 },
    		                                 { name: 'price', displayName: 'Price',enableFiltering: false,enableColumnMenu: false, width:'8%',
    		                                 },
    		                                 { name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false,type:'number',enableColumnMenu: false, width:'4%',cellEditableCondition: false,
    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.getImages(row)" style="line-height: 200%;" title="" data-content="{{row.entity.vehicleCnt}}">{{row.entity.vehicleCnt}}</a></div>',
    		                                 },
    		                                 { name: 'testDrive', displayName:'Next Test Drive' ,enableFiltering: false,enableColumnMenu: false, width:'10%',cellEditableCondition: false,
    		                                 },
    		                                 { name: 'pageViewCount', displayName: 'Views',enableFiltering: false,type:'number',enableColumnMenu: false, width:'7%',cellEditableCondition: false,
    		                                	 cellTemplate:'<span style="margin-left:10px;">{{row.entity.pageViewCount}}</span><i ng-if="row.entity.sold" title="Vehicle History" style="margin-left:10px;"class="glyphicon glyphicon-eye-open" ng-click="grid.appScope.historyVehicle(row)"></i>',
    		                                 },*/
    		                                 /*{ name: 'Hide', displayName: 'Hide', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
        		                                 cellTemplate:'<input type="checkbox" name="vehicle" ng-click="grid.appScope.hideVehicle(row)" autocomplete="off">', 
    		                                 
    		                                 },*/
    		                                 { name: 'edit', displayName: 'Action', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		                                	 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" ng-if="row.entity.userRole == \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp; <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" ng-if="row.entity.userRole != \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"></i>&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-stats" ng-if="row.entity.userRole != \'Photographer\'" ng-click="grid.appScope.showSessionData(row)" title="sessions"></i>&nbsp;', 
    		                                 
    		                                 },
        		                                
        		                                 ];  
     
    		 
    		 
    		 $scope.editPhoto = function(row){
    			 console.log(row);
    			 $location.path('/editVehicle/'+row.entity.id+"/"+true+"/"+row.entity.vin);
    			 
    		 }
    		 
    		 $scope.gotoImgTag = function(row){
    			 $location.path('/manufacturersImages/'+row.entity.id);
    			 console.log("ssssssssssss");
    		 }
    		 
    		 $scope.editProduct = function(row) {
    				$scope.flag = "product";
    				$location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
    			}
    		 
    		 $scope.gridOptions.onRegisterApi = function(gridApi){
    			 $scope.rowData = {};
    			 $scope.gridApi = gridApi;
    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    				 $scope.rowData.id = rowEntity.id;
    			 $scope.rowData.description = rowEntity.description;
    			 $scope.rowData.title = rowEntity.title;
    			 $scope.rowData.publicStatus = rowEntity.publicStatus;
    			 $scope.$apply();
    			 console.log("hhhhhhhhhhhhhhhhhhhhh");
    				console.log($scope.rowData);
    				 apiserviceViewInventory.updateProduct($scope.rowData).then(function(data){
    			 
    				 	
    				});
    			 });
    			 
    			 
    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		          var grid = this.grid;
    		          $scope.gridOptions.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		        });
    			 
    			 };
    			 
    			 
    			 $scope.gridOptions1 = {
    		    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    		    paginationPageSize: 150,
    		    		    enableFiltering: true,
    		    		    enableCellEditOnFocus: true,
    		    		    useExternalFiltering: true,
    		    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		    		 };
    		    		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
    		    		 $scope.gridOptions1.enableVerticalScrollbar = 2;
    		    		 $scope.gridOptions1.columnDefs = [
																{ name: 'title', displayName: 'Title',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
																	 cellTemplate: '<div> <a ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
																},
																{ name: 'description', displayName: 'Description',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
																	 cellTemplate: '<div> <label  style="line-height: 200%;" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
																},
																{ name: 'fileName', displayName: 'Logo',enableColumnMenu: false, width:'15%',
																},
																{ name: 'countImages', displayName: 'Images',enableColumnMenu: false,enableFiltering: false, width:'15%',cellEditableCondition: false,
																	cellTemplate: '<div> <a ng-click="grid.appScope.gotoImgTag(row)" style="line-height: 200%;" title="" data-content="{{row.entity.countImages}}">{{row.entity.countImages}}</a></div>',
																},
																{ name: 'pageViewCount', displayName: 'Views',enableColumnMenu: false,enableFiltering: false, width:'10%',cellEditableCondition: true,
																},
																
																/*{ name: 'city_mileage', displayName: 'City MPG',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: true,
																},
																{ name: 'highway_mileage', displayName: 'HWY MPG',enableFiltering: false, enableColumnMenu: false,width:'8%',cellEditableCondition: true,
																},
																{ name: 'price', displayName: 'Price',enableFiltering: false,enableColumnMenu: false, width:'8%',
																},
																{ name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false,type:'number',enableColumnMenu: false, width:'4%',cellEditableCondition: false,
																	 cellTemplate: '<div> <a ng-click="grid.appScope.getImages(row)" style="line-height: 200%;" title="" data-content="{{row.entity.vehicleCnt}}">{{row.entity.vehicleCnt}}</a></div>',
																},
																{ name: 'testDrive', displayName:'Next Test Drive' ,enableFiltering: false,enableColumnMenu: false, width:'10%',cellEditableCondition: false,
																},
																{ name: 'pageViewCount', displayName: 'Views',enableFiltering: false,type:'number',enableColumnMenu: false, width:'7%',cellEditableCondition: false,
																	 cellTemplate:'<span style="margin-left:10px;">{{row.entity.pageViewCount}}</span><i ng-if="row.entity.sold" title="Vehicle History" style="margin-left:10px;"class="glyphicon glyphicon-eye-open" ng-click="grid.appScope.historyVehicle(row)"></i>',
																},*/
																/*{ name: 'Hide', displayName: 'Hide', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																    cellTemplate:'<input type="checkbox" name="vehicle" ng-click="grid.appScope.hideVehicle(row)" autocomplete="off">', 
																
																},*/
																{ name: 'edit', displayName: 'Action', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																	 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" ng-if="row.entity.userRole == \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp; <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" ng-if="row.entity.userRole != \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.hideVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"  title="Add to Current Inventory"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"></i>&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-stats" ng-if="row.entity.userRole != \'Photographer\'" ng-click="grid.appScope.showSessionData(row)" title="sessions"></i>&nbsp;', 
																
																},
    		        		                                
    		        		                                 ];  
    		     
    		    		 $scope.gridOptions1.onRegisterApi = function(gridApi){
    		    			 $scope.gridApi = gridApi;
    		    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    		    			 $scope.rowData = rowEntity;
    		    			 $scope.$apply();
    		    				 var str = $scope.rowData.price.split(" ");
    		    				 $scope.rowData.price = str[1];
    		    				 apiserviceViewInventory.updateVehicle($scope.rowData).then(function(data){
    		    			 
    		    				 	$scope.rowData.price = "$ "+$scope.rowData.price;
    		    				
    		    				});
    		    			 });
    		    			 
    		    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		    		          var grid = this.grid;
    		    		          $scope.gridOptions1.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		    		        });
    		    			 
    		    			 };	 
    			 
    		    			 $scope.gridOptions2 = {
    		    		    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    		    		    paginationPageSize: 150,
    		    		    		    enableFiltering: true,
    		    		    		    enableCellEditOnFocus: true,
    		    		    		    useExternalFiltering: true,
    		    		    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		    		    		 };
    		    		    		 $scope.gridOptions2.enableHorizontalScrollbar = 0;
    		    		    		 $scope.gridOptions2.enableVerticalScrollbar = 2;
    		    		    		 $scope.gridOptions2.columnDefs = [
																		{ name: 'title', displayName: 'Title',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
																			 cellTemplate: '<div> <a ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
																		},
																		{ name: 'description', displayName: 'Description',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
																			 cellTemplate: '<div> <label  style="line-height: 200%;" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
																		},
																		{ name: 'fileName', displayName: 'Logo',enableColumnMenu: false, width:'15%',
																		},
																		{ name: 'countImages', displayName: 'Images',enableColumnMenu: false,enableFiltering: false, width:'15%',cellEditableCondition: false,
																			cellTemplate: '<div> <a ng-click="grid.appScope.gotoImgTag(row)" style="line-height: 200%;" title="" data-content="{{row.entity.countImages}}">{{row.entity.countImages}}</a></div>',
																		},
																		{ name: 'pageViewCount', displayName: 'Views',enableColumnMenu: false,enableFiltering: false, width:'10%',cellEditableCondition: true,
																		},
																		/*{ name: 'city_mileage', displayName: 'City MPG',enableFiltering: false, width:'8%',enableColumnMenu: false,cellEditableCondition: true,
																		},
																		{ name: 'highway_mileage', displayName: 'HWY MPG',enableFiltering: false, enableColumnMenu: false,width:'8%',cellEditableCondition: true,
																		},
																		{ name: 'price', displayName: 'Price',enableFiltering: false,enableColumnMenu: false, width:'8%',
																		},
																		{ name: 'vehicleCnt', displayName: 'Photos',enableFiltering: false,type:'number',enableColumnMenu: false, width:'4%',cellEditableCondition: false,
																			 cellTemplate: '<div> <a ng-click="grid.appScope.getImages(row)" style="line-height: 200%;" title="" data-content="{{row.entity.vehicleCnt}}">{{row.entity.vehicleCnt}}</a></div>',
																		},
																		{ name: 'testDrive', displayName:'Next Test Drive' ,enableFiltering: false,enableColumnMenu: false, width:'10%',cellEditableCondition: false,
																		},
																		{ name: 'pageViewCount', displayName: 'Views',enableFiltering: false,type:'number',enableColumnMenu: false, width:'7%',cellEditableCondition: false,
																			 cellTemplate:'<span style="margin-left:10px;">{{row.entity.pageViewCount}}</span><i ng-if="row.entity.sold" title="Vehicle History" style="margin-left:10px;"class="glyphicon glyphicon-eye-open" ng-click="grid.appScope.historyVehicle(row)"></i>',
																		},*/
																		/*{ name: 'Hide', displayName: 'Hide', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																		    cellTemplate:'<input type="checkbox" name="vehicle" ng-click="grid.appScope.hideVehicle(row)" autocomplete="off">', 
																		
																		},*/
																		{ name: 'edit', displayName: 'Action', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																			 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" ng-if="row.entity.userRole == \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp; <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" ng-if="row.entity.userRole != \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.hideVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"  title="Add to Current Inventory"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"></i>&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-stats" ng-if="row.entity.userRole != \'Photographer\'" ng-click="grid.appScope.showSessionData(row)" title="sessions"></i>&nbsp;', 
																		
																		},
    		    		        		                                
    		    		        		                                 ];  
    		    		     
    		    		    		 $scope.gridOptions2.onRegisterApi = function(gridApi){
    		    		    			 $scope.gridApi = gridApi;
    		    		    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    		    		    			 $scope.rowData = rowEntity;
    		    		    			 $scope.$apply();
    		    		    				 var str = $scope.rowData.price.split(" ");
    		    		    				 $scope.rowData.price = str[1];
    		    		    				 apiserviceViewInventory.updateVehicle($scope.rowData).then(function(data){
    		    		    			 
    		    		    				 	$scope.rowData.price = "$ "+$scope.rowData.price;
    		    		    				});
    		    		    			 });
    		    		    			 
    		    		    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		    		    		          var grid = this.grid;
    		    		    		          $scope.gridOptions2.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		    		    		        });
    		    		    			 
    		    		    			 };

    			 
    			 $scope.updateVehicleBody = function(row){
    				 $scope.rowData = row.entity;
    				 if($scope.rowData.price !=null && $scope.rowData.price != undefined){
    					 var str = $scope.rowData.price.split(" ");
        				 $scope.rowData.price = str[1];
    				 }
    				 apiserviceViewInventory.updateVehicle($scope.rowData).then(function(data){
    				 
        					$scope.rowData.price = "$ "+$scope.rowData.price;
        				});
    			 };
    			 
    			 
    			 $scope.historyVehicle = function(row){
    				 apiserviceViewInventory.getVehicleHistory(row.entity.vin).then(function(data){
    				 
    						$scope.vehicleHistory = data;
    						$('#vehicleHistory').click();
    					});
    			 };
    			 
    			 $scope.hideVehicle = function(row){
    				 console.log(row);
    				 if(row.entity.publicStatus == "publish"){
    						$scope.hideText = "Product has been hidden from the website and moved to Drafts list";
 							$scope.hideTitle = "Product moved to drafts"; 
    				 }else{
    						$scope.hideText = "Product has been moved to Current Manufacturers list";
 							$scope.hideTitle = "Product moved to Current Manufacturers";
    				 }
    				 apiserviceViewInventory.getGoTodraft(row.entity.id).then(function(data){
    				 
 						
 							//$scope.newlyArrivedTab();
 							$('#hideVehicle').click();
 							
 						if(row.entity.publicStatus == "publish"){
 							$.pnotify({
 							    title: "Success",
 							    type:'success',
 							    text: "Product Added In Draft",
 							}); 
 							$scope.newlyArrivedTab();
 						   
 	    				 }else{
 	    					$.pnotify({
 							    title: "Success",
 							    type:'success',
 							    text: "Product Added In Current Manufacturers",
 							});
 	    					$scope.draftTab();
 	    				 }
 							
 						
 					});
    				 
    			 }
    			 
    			 $scope.mouse = function(row) {
    					$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
    					$('#thumb_image').show();
    					$('#imagePopup').modal();
    				};
    				$scope.mouseout = function(row) {
    					console.log("Hiiiiiiiiiiii");
    					//$('#imgClose').click();
    				};
    				
    				$scope.vehicleData = function(sts){
    					if($scope.vType == 'new'){
    						 $scope.doPublic = 0;
    						 apiserviceViewInventory.getAllVehiclesByType(sts).then(function(data){
    						 
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "new";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
    					}
    					if($scope.vType == 'sold'){
    						 $scope.doPublic = 2;
    						 apiserviceViewInventory.getAllSoldVehiclesByType(sts).then(function(data){
   						 
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "sold";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions2.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
   					}
    				};
    				$scope.getImages = function(row) {
    					$location.path('/editVehicle/'+row.entity.id+"/"+true+"/"+row.entity.vin);
    				};
    				
    				
    		    			 $scope.newlyArrivedTab = function() {
    		    				 $scope.flagForUser = true;
    		    				 $scope.gridOptions.data = [];
    		    				 $scope.doPublic = 0;
    		    				 console.log($scope.userType);
    		    				 apiserviceViewInventory.findLocation().then(function(data){
    		    				 
		    							console.log(data);
		    							$scope.userLocationId = data;
    		    				 if($scope.userType == "Photographer"){
    		    					 apiserviceViewInventory.getAllVehiclesImage($scope.userLocationId).then(function(data){
    		    				
    		    			 			console.log(data);
    		    			 			/*for(var i=0;i<data.length;i++) {
    		    			 				data[i].price = "$ "+data[i].price;
    		    			 			}*/
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				
    		    			 				data[i].userRole=$scope.userRole;
    		    			 				
    		    							}
    		    			 			
    		    			 			$scope.vType = "new";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions.data = data;
    		    			 			console.log($scope.gridOptions.data);
    		    			 			//$scope.gridOptions.columnDefs[10].displayName='Views';
    		    			 		});
    		    				 }
    		    				 else{
    		    					 apiserviceViewInventory.getAllProduct($scope.userLocationId,"publish").then(function(data){
    		    					
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].price = "$ "+data[i].price;
     		    			 			}
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].userRole=$scope.userRole;
     		    			 			}
     		    			 			$scope.vType = "new";
     		    			 			$scope.vehiClesList = data;
     		    			 			$scope.gridOptions.data = data;
     		    			 			console.log($scope.gridOptions.data);
     		    			 			//$scope.gridOptions.columnDefs[10].displayName='Views';
     		    			 		});
    		    					 
    		    				 }
    		    				 });
    		    			 }	    			 
    		    			 
    		    			 $scope.soldTab = function() {
    		    				 $scope.ch = false;
    		    				 $scope.doPublic = 2;
    		    				 $scope.flagForUser = true;
    		    				 apiserviceViewInventory.getAllProduct(16,"deleted").then(function(data){
    		    				 
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				data[i].price = "$ "+data[i].price;
    		    			 			}
		    			 				for(var i=0;i<data.length;i++) {
		    			 					data[i].userRole=$scope.userRole;
		    			 				}
		    			 				$scope.vType = "sold";
    		    			 			$scope.type = "All";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions2.data = data;
    		    			 			//$scope.gridOptions.columnDefs[9].displayName='History';
    		    			 		});
    		    			 }
    		    			 $scope.draftTab = function() {
    		    				 $scope.flagForUser = true;
    		    				 $scope.doPublic = 1;
    		    				 apiserviceViewInventory.findLocation().then(function(data){
    		    					 
		    							console.log(data);
		    							$scope.userLocationId = data;
    		    				 if($scope.userType == "Photographer"){
    		    					 console.log($scope.userLocationId);
    		    					 apiserviceViewInventory.getAllProduct($scope.userLocationId,"draft").then(function(data){
    		    					 
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				data[i].price = "$ "+data[i].price;
    		    			 			}
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				
    		    			 				data[i].userRole=$scope.userRole;
    		    			 				
    		    							}
    		    			 			$scope.doPublic = 1;
    		    			 			$scope.vType = "sold";
    		    			 			$scope.type = "All";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions1.data = data;
    		    			 			//$scope.gridOptions.columnDefs[9].displayName='Views';
    		    			 			
    		    			 			
    		    			 		});
    		    				 }
    		    				 else{
    		    					 apiserviceViewInventory.getAllProduct($scope.userLocationId,"draft").then(function(data){
    		    					 
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].price = "$ "+data[i].price;
     		    			 			}
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].userRole=$scope.userRole;
     		    			 			}
     		    			 			$scope.doPublic = 1;
     		    			 			$scope.vType = "sold";
     		    			 			$scope.type = "All";
     		    			 			$scope.vehiClesList = data;
     		    			 			$scope.gridOptions1.data = data;
     		    			 		//	$scope.gridOptions.columnDefs[9].displayName='Views';
     		    			 		});
    		    					 
    		    				 }
    		    			 }); 
    		    			 }
    		    			 
    	$scope.editVehicle = function(row) {
    		$location.path('/editVehicleNew/'+row.entity.id+"/"+false+"/"+row.entity.vin);
    	}	 
    	
   $scope.vehiClesList = [];
  
   $scope.viewVehiclesInit = function() {
	   $scope.newlyArrivedTab();
   }
   
   $scope.deleteVehicle = function(row){
	   $('#deleteModal').click();
	   $scope.rowDataVal = row;
   }
   
   $scope.showSessionData = function(row){
	   $location.path('/sessionsAnalytics/'+row.entity.id+"/"+row.entity.vin+"/"+row.entity.status);
   }
   
   $scope.deleteVehicleRow = function() {
	   
	   apiserviceViewInventory.deleteVehicleById($scope.rowDataVal.entity.id).then(function(data){
	   
		   $scope.newlyArrivedTab();
		   $scope.draftTab();
		});
   }
   
   $scope.soldContact = {};
   
   $scope.updateVehicleStatusPublic = function(row){
	   apiserviceViewInventory.addPublicCar(row.entity.id).then(function(data){
	   
		   	$scope.hideText = "Vehicle has been published";
		   	$scope.hideTitle = "Vehicle has been published";
			$('#hideVehicle').click();
			   
			   $scope.draftTab();
	   });
   }
   $scope.updateVehicleStatus = function(row){
	   $scope.statusVal = "";
	   console.log("llllllllllllll");
	   if(row.entity.status == 'Newly Arrived') {
		   $('#btnStatusSchedule').click();
		   $scope.soldContact.statusVal = "Sold";
	   }
	   if(row.entity.status == 'Sold') {
		   
		    $('#AddbtnInventory').modal();
		  
		  }
	   /*$scope.addtoinventory = function() {
		   
		   apiserviceViewInventory.addSameNewCar(row.entity.id).then(function(data){
		   
			   if(data=='success'){
				   $scope.soldContact.statusVal = "Newly Arrived";
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle has been added to Inventory",
					});
			   }else{
				   $.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Vehicle already in Inventory",
					});
			   }
		   });
		   
		   
	   }*/
	   
	   $scope.soldContact.make = row.entity.make;
	   $scope.soldContact.mileage = row.entity.mileage;
	   $scope.soldContact.model = row.entity.model;
	   $scope.soldContact.year = row.entity.year;
	   $scope.soldContact.vin = row.entity.vin;
	   $scope.soldContact.id = row.entity.id;
	   var str = row.entity.price.split(" ");
	   $scope.soldContact.price = str[1];
   }
   
	$scope.saveVehicalStatus = function() {
		apiserviceViewInventory.setVehicleStatus($scope.soldContact).then(function(data){
		
			$('#vehicalStatusModal').modal('hide');
			if($scope.soldContact.statusVal == 'Newly Arrived') {
				 $scope.viewVehiclesInit();
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle status marked Newly Arrived",
					});
				 $scope.soldTab();
			} 
			if($scope.soldContact.statusVal == 'Sold') {
				$scope.soldTab();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle status marked sold",
				});
				
				$scope.newlyArrivedTab();
			}
			
	});
}
   
   $scope.editingData = [];
   
   for (var i = 0; i <  $scope.vehiClesList.length; i++) {
     $scope.editingData[$scope.vehiClesList[i].id] = false;
     $scope.viewField = false;
   }
   

   $scope.modify = function(tableData){
	   $scope.viewField = true;
       $scope.editingData[tableData.id] = true;
   };

   $scope.update = function(tableData){
       $scope.editingData[tableData.id] = false;
       $scope.viewField = false;
       apiserviceViewInventory.updateVehicle(tableData).then(function(data){
      
		});
   };
   
   $scope.cancle = function(tableData){
	   $scope.editingData[tableData.id] = false;
	   $scope.viewField = false;
   }
   
   $scope.exportDataAsCSV = function() {
	   apiserviceViewInventory.exportDataAsCSV().then(function(data){
	   
		});
   }
   
   $scope.exportCarfaxCSV = function() {
	   apiserviceViewInventory.exportCarfaxCSV().then(function(data){
	   
		});
   }
   
   $scope.exportCarGurusCSV = function() {
	   apiserviceViewInventory.exportCarGurusCSV().then(function(data){
	   
		});
   }
   
  
   
   
   
   /*------------------------------------Add coll------------------------*/
   
   
   $scope.gridOptions3 = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 $scope.gridOptions3.enableHorizontalScrollbar = 0;
	 $scope.gridOptions3.enableVerticalScrollbar = 2;
	 
	
	$scope.flagForChart1 = true;
	$scope.flagForUser = true;
	$scope.allCollectionData = function(){
		$scope.flagForUser = false;
		 $scope.doPublic = 3;
		 apiserviceViewInventory.getAllCollectionData().then(function(data){
		
			console.log(data);
			$scope.leadtypeObjList = data;
			$scope.gridOptions3.data = data;
			
			console.log("gghgfhghghg");
			console.log($scope.gridOptions3.data);
		});
		
		
		
		$scope.gridOptions3.columnDefs = [
		                                 { name: 'id', displayName: 'Id', width:'15%',cellEditableCondition: false
		                                 },
		                                 { name: 'title', displayName: 'Title', width:'60%',cellEditableCondition: false
		                                 },
		                                 
		                                 { name: 'edit', displayName: ' ', width:'25%',
  		                                 cellTemplate:'<i class="fa fa-trash" ng-if="row.entity.title != \'New\' && row.entity.title != \'Used\' && row.entity.title != \'Coming Soon\'" ng-click="grid.appScope.removeCollection(row)"  title="Delete"></i> &nbsp;&nbsp;&nbsp&nbsp;<i class="glyphicon glyphicon-pencil" ng-if="row.entity.title != \'New\' && row.entity.title != \'Used\' && row.entity.title != \'Coming Soon\'" ng-click="grid.appScope.editCollection(row)"  title="Edit"></i> ', 
  		                                 
		                                 },
		                                    ];
	}
	
	$scope.addManufacturers = function(){
		$location.path('/addManufacturers');
		
	}
	
	
	$scope.collections = {};
	$scope.saveCollectionData = function(){
		console.log("in collection");
		$scope.collections.title = $scope.collections.title;
		if($scope.collections.id == undefined){
			$scope.collections.id = 0;
		}
		console.log($scope.collections);
		apiserviceViewInventory.addNewCollection($scope.collections).then(function(data){
		
				$scope.form = data;
			 console.log("::::::success")
				
			 $("#collectionpopups").modal('hide');
			 $('#editcollectionpopup').modal('hide');
			 $scope.allCollectionData();
			});
	}
	
	
	$scope.editCollection = function(row){
		console.log(row.entity);
		$scope.collections = row.entity;
		$('#editcollectionpopup').modal('show');
	}
	
	
	$scope.removeCollection = function(row){
		console.log(row.entity);
		$scope.collections = row.entity.id;
		console.log($scope.collections);
		$('#deletepopup').modal('show');
	}
	
	
	$scope.deleteCollection = function(){
		 console.log("in deletend functio");
		 console.log($scope.collections);
		 apiserviceViewInventory.deleteCollectionData($scope.collections).then(function(data){
		
				 console.log("out deletend functio");
				 $scope.allCollectionData();
				 
				 

			});
	 }
	
	
}]);



/*angular.module('newApp')
.controller('viewInventoryCtrl', ['$scope','$http','$location','$filter','apiserviceViewInventory', function ($scope,$http,$location,$filter,apiserviceViewInventory) {
	$scope.tempDate = new Date().getTime();
	$scope.type = "All";
	$scope.vType;
	$scope.doPublic = 0;
     $scope.gridOptions = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    paginationPageSize: 150,
    		    enableFiltering: true,
    		    enableCellEditOnFocus: true,
    		    useExternalFiltering: true,
    		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
    		 };
    		 $scope.gridOptions.enableHorizontalScrollbar = 0;
    		 $scope.gridOptions.enableVerticalScrollbar = 2;
    		 
     
    			
    		 
    		 $scope.editPhoto = function(row){
    			 console.log(row);
    			 console.log(row.entity.portalName);
    			 if(row.entity.portalName == "MavenFurniture"){
    				 $location.path('/editInventory/'+row.entity.id+"/"+true+"/"+row.entity.productId);
    			 }else  if(row.entity.portalName == "Autodealer"){
    				 $location.path('/editVehicle/'+row.entity.id+"/"+true+"/"+row.entity.vin);
    				 
    			 }
    			 
    			 
    		 }
    		 
    		 $scope.gridOptions.onRegisterApi = function(gridApi){
    			 $scope.gridApi = gridApi;
    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    			 $scope.rowData = rowEntity;
    			 $scope.$apply();
    				// var str = $scope.rowData.price.split(" ");
    				// $scope.rowData.price = str[1];
    			 $http.post('/updateVehicle',$scope.rowData)
    			 .success(function(data) {
    				 	$scope.rowData.price = "$ "+$scope.rowData.price;
    				});
    			 });
    			 
    			 
    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		          var grid = this.grid;
    		          $scope.gridOptions.data = $filter('filter')($scope.vehiClesList,{'make':grid.columns[0].filters[0].term,'last4Vin':grid.columns[1].filters[0].term,'stock':grid.columns[2].filters[0].term},undefined);
    		        });
    			 
    			 };
    		    		     
    		    		    		
    			 
    			 $scope.updateVehicleBody = function(row){
    				 $scope.rowData = row.entity;
    				 if($scope.rowData.price !=null && $scope.rowData.price != undefined){
    					 var str = $scope.rowData.price.split(" ");
        				 $scope.rowData.price = str[1];
    				 }
    				 $http.post('/updateVehicle',$scope.rowData)
        			 .success(function(data) {
        					$scope.rowData.price = "$ "+$scope.rowData.price;
        				});
    			 };
    			 
    			 
    			 $scope.historyVehicle = function(row){
    				 $http.get('/getVehicleHistory/'+row.entity.vin)
    					.success(function(data) {
    						$scope.vehicleHistory = data;
    						$('#vehicleHistory').click();
    					});
    			 };
    			 
    			 $scope.hideVehicle = function(row){
    				 $http.get('/getGoTodraft/'+row.entity.id)
 						.success(function(data) {
 							$scope.hideText = "Vehicle has been hidden from the website and moved to Drafts list";
 							$scope.hideTitle = "Vehicle moved to drafts";
 							$scope.newlyArrivedTab();
 							$('#hideVehicle').click();
 							$.pnotify({
 							    title: "Success",
 							    type:'success',
 							    text: "Vehicle Added In Draft",
 							});
 						
 					});
    				 
    			 }
    			 
    			 $scope.mouse = function(row) {
    					$('#thumb_image').attr("src", "/getImageInv/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
    					$('#thumb_image').show();
    					$('#imagePopup').modal();
    				};
    				$scope.mouseout = function(row) {
    					$('#imgClose').click();
    				};
    				
    				$scope.vehicleData = function(sts){
    					if($scope.vType == 'new'){
    						 $scope.doPublic = 0;
    						 $http.get('/getAllVehiclesByType/'+sts)
		    			 		.success(function(data) {
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "new";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
    					}
    					if($scope.vType == 'sold'){
    						 $scope.doPublic = 2;
   						 $http.get('/getAllSoldVehiclesByType/'+sts)
		    			 		.success(function(data) {
		    			 			for(var i=0;i<data.length;i++) {
		    			 				data[i].price = "$ "+data[i].price;
		    			 			}
		    			 			$scope.vType = "sold";
		    			 			$scope.vehiClesList = data;
		    			 			$scope.gridOptions2.data = data;
		    			 			$scope.gridOptions.columnDefs[9].displayName='Next Test Drive';
		    			 			$scope.gridOptions.columnDefs[10].displayName='Views';
		    			 		});
   					}
    				};
    				$scope.getImages = function(row) {
    					$location.path('/editVehicle/'+row.entity.id+"/"+true);
    				};
    				
    				
    		    			 $scope.newlyArrivedTab = function() {
    		    				 $scope.gridOptions.data = [];
    		    				 apiserviceViewInventory.findLocation().then(function(data){		    				
    		    				
		    							console.log(data);
		    							$scope.userLocationId = data;
		    							apiserviceViewInventory.getAllInventory($scope.userLocationId).then(function(data){
		    	    		    				 
		    	    		    			 			console.log(data);
		    	    		    			 			for(var i=0;i<data.length;i++) {
		    	    		    			 				data[i].userRole = $scope.userRole;
		    	    		    			 				data[i].portalName = "MavenFurniture";
		    	    		    						}	    	    	
		    	    		    			 			
		    	    		    			 			$scope.vehiClesList = data;
		    	    		    			 			$scope.gridOptions.data = data;
		    	    		    			 			console.log($scope.gridOptions.data);
		    	    		    			 		});
		    	    		    				 
		    								 
		    								 $scope.gridOptions.columnDefs = [
		    						    		                                 { name: 'title', displayName: 'Title',enableColumnMenu: false, width:'20%',cellEditableCondition: true,
		    						    		                                	 cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
		    						    		                                 },
		    						    		                                 { name: 'description', displayName: 'Description',enableFiltering: false,type:'number',enableColumnMenu: false, width:'15%',cellEditableCondition: false,
		    						    		                                 }, 
		    						    		                                 { name: 'id', displayName: 'Logo',enableFiltering: false,type:'number',enableColumnMenu: false, width:'20%',cellEditableCondition: false,
		    						    		                                 },
		    						    		                                 { name: 'edit', displayName: '', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
		    						    		                                	 cellTemplate:'<i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editVehicle(row)" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;', 
		    						    		                                 
		    						    		                                 },
		    						        		                                
		    						        		                                 ];  
		    								 
    		    				 });
    		    			 }	    			 
    		    			 
    		    			
    		    			 
    	$scope.editVehicle = function(row) {
    		$location.path('/editInventory/'+row.entity.id+"/"+false+"/"+row.entity.productId);
    	}	 
    	
   $scope.vehiClesList = [];
  
   $scope.viewInit = function() {
	   console.log("**********************88888888********************8");
				   $scope.newlyArrivedTab();
	   
   }
   
   $scope.deleteVehicle = function(row){
	   $('#deleteModal').click();
	   $scope.rowDataVal = row;
   }
   
   $scope.showSessionData = function(row){
	   $location.path('/sessionsAnalytics/'+row.entity.id+"/"+row.entity.vin+"/"+row.entity.status);
   }
   
   $scope.deleteVehicleRow = function() {
	   $http.get('/deleteVehicleById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			if($scope.rowDataVal.entity.status == 'Newly Arrived') {
				 $scope.viewVehiclesInit();
			} 
			if($scope.rowDataVal.entity.status == 'Sold') {
				$scope.soldTab();
			}
		});
   }
   
   //$scope.soldContact = {};
   
   $scope.updateVehicleStatusPublic = function(row){
	   $http.get('/addPublicCar/'+row.entity.id).success(function(data){
		   	$scope.hideText = "Vehicle has been published";
		   	$scope.hideTitle = "Vehicle has been published";
			$('#hideVehicle').click();
			   $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle published",
				});
			   $scope.draftTab();
	   });
   }
   $scope.updateVehicleStatus = function(row){
	   $scope.statusVal = "";
	   if(row.entity.status == 'Newly Arrived') {
		   $('#btnStatusSchedule').click();
		   $scope.soldContact.statusVal = "Sold";
	   }
	   if(row.entity.status == 'Sold') {
		   
		    $('#AddbtnInventory').modal();
		  
		  }
	   $scope.addtoinventory = function() {
		   $http.get('/addSameNewCar/'+row.entity.id).success(function(data){
			   if(data=='success'){
				   $scope.soldContact.statusVal = "Newly Arrived";
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle has been added to Inventory",
					});
			   }else{
				   $.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Vehicle already in Inventory",
					});
			   }
		   });
		   
		   
	   }
	   
	   $scope.soldContact.make = row.entity.make;
	   $scope.soldContact.mileage = row.entity.mileage;
	   $scope.soldContact.model = row.entity.model;
	   $scope.soldContact.year = row.entity.year;
	   $scope.soldContact.vin = row.entity.vin;
	   $scope.soldContact.id = row.entity.id;
	   var str = row.entity.price.split(" ");
	   $scope.soldContact.price = str[1];
   }
   
	$scope.saveVehicalStatus = function() {
		$http.post('/setVehicleStatus',$scope.soldContact)
		.success(function(data) {
			$('#vehicalStatusModal').modal('hide');
			if($scope.soldContact.statusVal == 'Newly Arrived') {
				 $scope.viewVehiclesInit();
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle status marked Newly Arrived",
					});
				 $scope.soldTab();
			} 
			if($scope.soldContact.statusVal == 'Sold') {
				$scope.soldTab();
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Vehicle status marked sold",
				});
				
				$scope.newlyArrivedTab();
			}
			
	});
}
   
  // $scope.editingData = [];
   
   for (var i = 0; i <  $scope.vehiClesList.length; i++) {
     $scope.editingData[$scope.vehiClesList[i].id] = false;
     $scope.viewField = false;
   }
   

   $scope.modify = function(tableData){
	   $scope.viewField = true;
       $scope.editingData[tableData.id] = true;
   };

   $scope.update = function(tableData){
       $scope.editingData[tableData.id] = false;
       $scope.viewField = false;
       $http.post('/updateVehicle',tableData)
		.success(function(data) {
		});
   };
   
   $scope.cancle = function(tableData){
	   $scope.editingData[tableData.id] = false;
	   $scope.viewField = false;
   }
   
   $scope.exportDataAsCSV = function() {
	   $http.get('/exportDataAsCSV')
		.success(function(data) {
		});
   }
   
   $scope.exportCarfaxCSV = function() {
	   $http.get('/exportCarfaxCSV')
		.success(function(data) {
		});
   }
   
   $scope.exportCarGurusCSV = function() {
	   $http.get('/exportCarGurusCSV')
		.success(function(data) {
		});
   }
      
}]);
*/