angular.module('newApp')
.controller('selectCollectionCtrl', ['$scope','$http','$location','$filter','$upload','apiserviceSelectCollection','$routeParams', function ($scope,$http,$location,$filter,$upload,apiserviceSelectCollection,$routeParams) {
	
	console.log("Hello this is ***** this is selectCollectionCtrl  ");
	$scope.addObj = "collection";
	$scope.getAllInventory = function(){
		apiserviceSelectCollection.getAllInventoryData().then(function(data){
			console.log(data);
			$scope.allCollectionDate = data;
			if(data.length >0){
				localStorage.setItem('mainCollection', JSON.stringify(data[0]));
				$scope.mainCollId = data[0].id;
				$scope.hideWebsite = data[0].hideWebsite;
			}
		});
	}
	
	
	
	$scope.getAllInventory();
	
	$scope.getCollectionData = function(obj){
		$scope.mainCollId = obj.id;
		delete obj.$$hashKey;
		localStorage.setItem('mainCollection', JSON.stringify(obj));
		console.log($scope.doPublic,obj);
		$scope.hideWebsite = obj.hideWebsite;
		if($scope.doPublic == 0){
			$scope.newlyArrivedTab();
		}if($scope.doPublic == 1){
		}if($scope.doPublic == 2){
		}if($scope.doPublic == 4){
			$scope.productTab("publish");
		}			
	};
	
	
	$scope.hideWebsite = false;
	$scope.updateHideData = function(hideWebsite){
		$scope.rowData = {};
		$scope.rowData.hideWebsite = hideWebsite;
		$scope.rowData.id = $scope.mainCollId;
		console.log($scope.rowData);
		console.log($scope.mainCollId);
		$http.post('/updateHideData',$scope.rowData).success(function(data) {
			 console.log(data);
		});
	}
	
	$scope.tempDate = new Date().getTime();
	$scope.type = "All";
	$scope.vType;
	$scope.doPublic = 0;
	$scope.myClickFun = function(obj){
		$scope.productData = obj;
		console.log("???????????????????");
		console.log(obj);
		document.getElementById("uploadId").click();
		$scope.productData.publicStatus = "publish";
		$scope.productData.collectionId = 0;
		console.log($scope.productData);
	 };
	 var logofile = null;
	 var names = [];
	 var files =[];
	 $scope.onLogoFileSelect = function($files) {
		logofile = $files;
		console.log("File Upload");
		console.log(logofile);
		$scope.productData.fileName = logofile[0].name;
		files[0] = $files[0];
		names[0]= "logoFile";
		delete $scope.productData.$$hashKey;
		delete $scope.productData.price;
		delete $scope.productData.parentId;
		
		if(logofile != null){
			console.log("logofile");
			console.log($scope.productData);
			console.log(logofile);
				$upload.upload({
		            url : '/updateProduct',
		            method: 'POST',
		            file:logofile,
		            data:$scope.productData
		        }).success(function(data) {
		        	$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Product Update successfully",
					});
		   		}).error(function(data) {
		   			$.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Internal Server Error.",
					});
		   		});
		
		}
	}
     $scope.gridOptions = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		 expandableRowTemplate: '/dealer/viewInventory/expandableRowTemplate.html',
    		 expandableRowScope: {
	    			 editProduct: function(item){ 
	    				 $scope.editProduct(item);
	    			 },
				     deleteVehicle:function(item,type){
						 console.log(item);
						 $scope.deleteVehicle(item,type);
						 console.log("kkkkkkkkkkkkk");
					 },
					 hideProduct:function(item){
						 $scope.hideProduct(item);
					 },
    			 
    		    },
     		 paginationPageSize: 150,
	 		 enableFiltering: true,
	 		 useExternalFiltering: true,
 		    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
    		 };
    		 $scope.gridOptions.enableHorizontalScrollbar = 0;
    		 $scope.gridOptions.enableVerticalScrollbar = 2;
    		 $scope.gridOptions.columnDefs = [
    		                                 { name: 'select', displayName: 'Select', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
         		                                 cellTemplate:'<input type="checkbox" ng-checked="row.entity.hideWebsite" name="vehicle" ng-click="grid.options.expandableRowScope.hideProduct(row)" autocomplete="off">', 
     		                                 }, 
    		                                 { name: 'title', displayName: 'Title', width:'20%',cellEditableCondition: false,
    		                                	 cellTemplate: '<div> <a style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		                                 },
    		                                 { name: 'description', displayName: 'Description',enableColumnMenu: false, width:'25%',cellEditableCondition: false,
    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" title="{{row.entity.description}}" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
    		                                 },
    		                                 { name: 'fileName', displayName: 'Logo', width:'25%',cellEditableCondition: false,
    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.myClickFun(row.entity)" style="line-height: 200%;white-space: nowrap;" title="{{row.entity.fileName}}" data-content="{{row.entity.fileName}}" >{{row.entity.fileName}}</a></div>',
    		                                 },
    		                                 { name: 'addedDate', displayName: 'Date Added',enableColumnMenu: false, width:'25%',
    		                                 }
        		                                
        		                                 ];  
     
    		 
    		 
    		
    		 
    		 $scope.gotoImgTag = function(row){
    			 $location.path('/manufacturersImages/'+row.entity.id);
    			 console.log("ssssssssssss");
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
	    			 
    				 console.log($scope.rowData);
    				 	apiserviceSelectCollection.updateProduct($scope.rowData).then(function(data){
    				 });
    			 });
    			 gridApi.expandable.on.rowExpandedStateChanged($scope, function (row) {
                     console.log("entity   :::: ",row);
                     console.log(row.entity);
                     if (row.isExpanded) {
                         row.entity.subGridOptions = {
                         enableSorting: false,
                         enableFiltering: false,
                         columnDefs: $scope.desGrid()
                     };
                       
                       row.entity.subGridOptions.data = row.entity.subCollection;   
                     }
                 });
    			 
    			 $scope.gridApi.core.on.filterChanged( $scope, function() {
    		          var grid = this.grid;
    		          $scope.gridOptions.data = $filter('filter')($scope.vehiClesList,{'title':grid.columns[0].filters[0].term,'description':grid.columns[1].filters[0].term,'fileName':grid.columns[2].filters[0].term,'addedDate':grid.columns[3].filters[0].term,'countImages':grid.columns[4].filters[0].term,'pageViewCount':grid.columns[5].filters[0].term,},undefined);
    		        });
    			 
    			 };
    			 console.log($scope.vehiClesList);
    			 $scope.gridOptions1 = {
    		    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		    		 paginationPageSize: 150,
    			 		    enableFiltering: true,
    			 		    useExternalFiltering: true,
    		    		    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>'
    		    		 };
    		    		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
    		    		 $scope.gridOptions1.enableVerticalScrollbar = 2;
    		    		 $scope.gridOptions1.columnDefs = [
																{ name: 'title', displayName: 'Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
																	 cellTemplate: '<div> <a ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
																},
																{ name: 'description', displayName: 'Description',enableColumnMenu: false, width:'15%',cellEditableCondition: true,
																	 cellTemplate: '<div> <label  style="line-height: 200%;" title="{{row.entity.description}}" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
																},
																{ name: 'fileName', displayName: 'Logo', width:'15%',cellEditableCondition: false,enableFiltering: false,
					    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.myClickFun(row.entity)" style="line-height: 200%;white-space: nowrap;" title="{{row.entity.fileName}}" data-content="{{row.entity.fileName}}" >{{row.entity.fileName}}</a></div>',
					    		                                 },
																{ name: 'addedDate', displayName: 'Date Added',enableColumnMenu: false, width:'12%',
					    		                                 },
					    		                                { name: 'type', displayName: 'Type',enableColumnMenu: false, width:'12%',
					    		                                },
																{ name: 'countImages', displayName: 'Images',enableColumnMenu: false,enableFiltering: false, width:'15%',cellEditableCondition: false,
																	cellTemplate: '<div> <a ng-click="grid.appScope.gotoImgTag(row)" style="line-height: 200%;" title="" data-content="{{row.entity.countImages}}">{{row.entity.countImages}}</a></div>',
																},
															
																{ name: 'edit', displayName: 'Action', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																	 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" ng-if="row.entity.userRole == \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp; <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProductOrCollection(row)" ng-if="row.entity.userRole != \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.hideVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"  title="Add to Current Inventory"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProductAndCollection(row,\'draft\')" ng-if="row.entity.userRole != \'Photographer\'"></i>&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-stats" ng-if="row.entity.userRole != \'Photographer\'" ng-click="grid.appScope.showSessionData(row)" title="sessions"></i>&nbsp;', 
																
																},
    		        		                                
    		        		                                 ];  
    		     
    		    		 $scope.gridOptions1.onRegisterApi = function(gridApi){
    		    			 $scope.gridApi = gridApi;
    		    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    		    			 $scope.rowData = rowEntity;
    		    			 $scope.$apply();
    		    				 var str = $scope.rowData.price.split(" ");
    		    				 $scope.rowData.price = str[1];
    		    				 console.log("hhhhhhhhhhhhhhhhhhhhh");
    		    				 console.log($scope.rowData);
		    				 	 apiserviceSelectCollection.updateProduct($scope.rowData).then(function(data){
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
    		    			 		    useExternalFiltering: true,
    		    		    		    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
    		    		    		 };
    		    		    		 $scope.gridOptions2.enableHorizontalScrollbar = 0;
    		    		    		 $scope.gridOptions2.enableVerticalScrollbar = 2;
    		    		    		 $scope.gridOptions2.columnDefs = [
																		{ name: 'title', displayName: 'Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
																			 cellTemplate: '<div> <a ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
																		},
																		{ name: 'description', displayName: 'Description',enableColumnMenu: false, width:'18%',cellEditableCondition: true,
																			 cellTemplate: '<div> <label  style="line-height: 200%;" title="{{row.entity.description}}" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
																		},
																		{ name: 'fileName', displayName: 'Logo', width:'17%',cellEditableCondition: false,enableFiltering: false,
							    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.myClickFun(row.entity)" style="line-height: 200%;white-space: nowrap;" title="{{row.entity.fileName}}" data-content="{{row.entity.fileName}}" >{{row.entity.fileName}}</a></div>',
							    		                                 },
																		{ name: 'type', displayName: 'Type',enableColumnMenu: false, width:'15%',
							    		                                 },
																		{ name: 'countImages', displayName: 'Images',enableColumnMenu: false,enableFiltering: false, width:'15%',cellEditableCondition: false,
																			cellTemplate: '<div> <a ng-click="grid.appScope.gotoImgTag(row)" style="line-height: 200%;" title="" data-content="{{row.entity.countImages}}">{{row.entity.countImages}}</a></div>',
																		},
																		{ name: 'pageViewCount', displayName: 'Views',enableColumnMenu: false,enableFiltering: false, width:'10%',cellEditableCondition: true,
																		},
																	
																		
																		{ name: 'edit', displayName: 'Action', width:'12%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
																			 cellTemplate:'<i class="glyphicon glyphicon-picture" ng-click="grid.appScope.editPhoto(row)" ng-if="row.entity.userRole == \'Photographer\'" style="margin-top:7px;margin-left:8px;" title="Edit"></i>  &nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-ok-circle" ng-click="grid.appScope.hideVehicle(row)" ng-if="row.entity.userRole != \'Photographer\'"  title="Add to Current Inventory"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProductAndCollectionPerMa(row)" ng-if="row.entity.userRole != \'Photographer\'"></i>&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-stats" ng-if="row.entity.userRole != \'Photographer\'" ng-click="grid.appScope.showSessionData(row)" title="sessions"></i>&nbsp;', 
																		
																		},
    		    		        		                                
    		    		        		                                 ];  
    		    		     
    		    		    		 $scope.gridOptions2.onRegisterApi = function(gridApi){
    		    		    			 $scope.gridApi = gridApi;
    		    		    			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
    		    		    			 $scope.rowData = rowEntity;
    		    		    			 $scope.$apply();
    		    		    				 var str = $scope.rowData.price.split(" ");
    		    		    				 $scope.rowData.price = str[1];
    		    		    				 apiserviceSelectCollection.updateProduct($scope.rowData).then(function(data){
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
    				 apiserviceSelectCollection.updateVehicle($scope.rowData).then(function(data){
    				 
        					$scope.rowData.price = "$ "+$scope.rowData.price;
        				});
    			 };
    			 
    			 
    			 $scope.gridOptions4 = {
    				paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
		    	 	paginationPageSize: 150,
			 	    enableFiltering: true,
			 	    useExternalFiltering: true,
		    	    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
    			 };
    			 
    			 $scope.gridOptions4.enableHorizontalScrollbar = 0;
    			 $scope.gridOptions4.enableVerticalScrollbar = 2;
    			 $scope.gridOptions4.columnDefs = [
														{ name: 'select', displayName: 'Select', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
														     cellTemplate:'<input type="checkbox" ng-checked="row.entity.hideWebsite" name="vehicle" ng-click="grid.options.expandableRowScope.hideProduct(row)" autocomplete="off">', 
														 }, 
														{ name: 'primaryTitle', displayName: 'Primary Title', width:'30%',cellEditableCondition: false,enableFiltering: true,
															 cellTemplate: '<div> <a ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.primaryTitle}}">{{row.entity.primaryTitle}}</a></div>',
														},
														{ name: 'collectionTitle', displayName: 'Collection', width:'30%',cellEditableCondition: false,
   		    		                                	 cellTemplate: '<div> <a style="line-height: 200%;" title="" data-content="{{row.entity.collectionTitle}}">{{row.entity.collectionTitle}}</a></div>',
   		    		                                    },
														{ name: 'description', displayName: 'Description',enableColumnMenu: false, width:'30%',cellEditableCondition: true,
															 cellTemplate: '<div> <label  style="line-height: 200%;" title="{{row.entity.description}}" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
														},
														
														
														
    			                                   ];
    			 
    			 
    			 $scope.historyVehicle = function(row){
    				 apiserviceSelectCollection.getVehicleHistory(row.entity.vin).then(function(data){
    				 
    						$scope.vehicleHistory = data;
    						$('#vehicleHistory').click();
    					});
    			 };
    			 
    			 $scope.updateProduct = function(row){
    				 console.log(row);
    				 $location.path('/update-product/'+row.id);    				 
    			 };
    			 
    			 $scope.deleteProductAndCollection = function(row){
    				 console.log(row);
    				 if(row.entity.type == "Product"){
    					 $scope.deleteProduct(row.entity);
    				 }else if(row.entity.type == "Collection"){
    					 $scope.deleteVehicle(row,'current');
    				 }
    			 }
    			
    			 $scope.deleteError = false;
    			 $scope.deleteThisProduct = function(){
    				 console.log($scope.deleteObj);
    				 apiserviceSelectCollection.deleteThisProduct($scope.deleteObj.id).then(function(data){    					 
    					 if(data == 'error'){
    						 $.pnotify({
  							    title: "Error",
  							    type:'error',
  							    text: "Can't delete this product",
  							});
    						 $scope.deleteError = true;
    					 }else{
        					 $('#productDeletePop').modal('hide');
    						 $.pnotify({
  							    title: "Success",
  							    type:'success',
  							    text: "Product delete successfully",
  							});
    						 $scope.deleteError = false;
    					 }    					 
 					});    				 
    			 };
    			 
    			 $scope.hideProduct = function(row){
    				 console.log(row);
    				 apiserviceSelectCollection.getHideProduct(row.entity.id).then(function(data){
						
					});
    			 } 
    			 
    			
    			 
    			 $scope.hoverOut = function(mainColl){
    				 $scope.mainCollectionId ="";
    			 }
    			 
    			 $scope.editProductOrCollection = function(row){
    				 console.log(row);
    				 if(row.entity.type == "Product"){
    	    				$location.path('/update-product/'+row.entity.id);
    				 }else  if(row.entity.type == "Collection"){
    					 $scope.flag = "product";
    					 $location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
    				 }
    				 console.log(row.entity.type);
    			 }
    			 
    			
    			 
    			 $scope.hideVehicle = function(row){
    				 console.log(row);
    				 if(row.entity.type == "Product"){
    					 if(row.entity.publicStatus == "publish"){
	     						$scope.hideText = "Product has been hidden from the website and moved to Drafts list";
	  							$scope.hideTitle = "Product moved to drafts"; 
	     				 }else{
	     						$scope.hideText = "Product has been moved to Current Manufacturers list";
	  							$scope.hideTitle = "Product moved to Current Manufacturers";
	     				 }
	     				 apiserviceSelectCollection.getGoTodraftProduct(row.entity.id).then(function(data){
	  						
	  							$('#hideVehicle').click();
	  						if(row.entity.publicStatus == "deleted"){
	  							$.pnotify({
	  							    title: "Success",
	  							    type:'success',
	  							    text: "Product Added In Draft",
	  							}); 
	  							//$scope.newlyArrivedTab();
	  						   
	  	    				 }else{
	  	    					$.pnotify({
	  							    title: "Success",
	  							    type:'success',
	  							    text: "Product Added In Current Manufacturers",
	  							});
	  	    				 }
	  					});
 				 }else if(row.entity.type == "Collection"){
	    					 if(row.entity.publicStatus == "publish"){
	     						$scope.hideText = "Collection has been hidden from the website and moved to Drafts list";
	  							$scope.hideTitle = "Collection moved to drafts"; 
	     				 }else{
	     						$scope.hideText = "Collection has been moved to Current Manufacturers list";
	  							$scope.hideTitle = "Collection moved to Current Manufacturers";
	     				 }
	     				 apiserviceSelectCollection.getGoTodraft(row.entity.id).then(function(data){
	  						
	  							$('#hideVehicle').click();
	  						if(row.entity.publicStatus == "publish"){
	  							$.pnotify({
	  							    title: "Success",
	  							    type:'success',
	  							    text: "Collection Added In Draft",
	  							}); 
	  							//$scope.newlyArrivedTab();
	  						   
	  	    				 }else{
	  	    					$.pnotify({
	  							    title: "Success",
	  							    type:'success',
	  							    text: "Collection Added In Current Manufacturers",
	  							});
	  	    				 }
	  					});
    				 }
    				
    				 
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
    						 apiserviceSelectCollection.getAllVehiclesByType(sts).then(function(data){
    						 
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
    						 apiserviceSelectCollection.getAllSoldVehiclesByType(sts).then(function(data){
   						 
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
    		    				 $scope.addObj = "collection";
    		    				 $scope.flagForUser = true;
    		    				 $scope.gridOptions.data = [];
    		    				 $scope.doPublic = 0;
    		    				 console.log($scope.userType);
    		    				 apiserviceSelectCollection.findLocation().then(function(data){
    		    				 
		    							console.log(data);
		    							$scope.userLocationId = data;
    		    				 if($scope.userType == "Photographer"){
    		    					 apiserviceSelectCollection.getAllVehiclesImage($scope.userLocationId).then(function(data){
    		    				
    		    			 			console.log(data);
    		    			 			
    		    			 			for(var i=0;i<data.length;i++) {
    		    			 				
    		    			 				data[i].userRole=$scope.userRole;
    		    			 				
    		    							}
    		    			 			
    		    			 			$scope.vType = "new";
    		    			 			$scope.vehiClesList = data;
    		    			 			$scope.gridOptions.data = data;
    		    			 			console.log($scope.gridOptions.data);
    		    			 		});
    		    				 }
    		    				 else{
    		    					 apiserviceSelectCollection.getAllCollectionList($scope.userLocationId,"publish",$scope.mainCollId).then(function(data){
    		    					
    		    						 console.log(data);
    		    						
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].price = "$ "+data[i].price;
     		    			 				
     		    			 			}
     		    			 			
     		    			 			
     		    			 			for(var i=0;i<data.length;i++) {
     		    			 				data[i].userRole=$scope.userRole;
     		    			 				if(data[i].hideWebsite == 1){
     		    			 					data[i].hideWebsite = true;
     		    			 				}
     		    			 			}
     		    			 			$scope.vType = "new";
     		    			 			$scope.vehiClesList = data;
     		    			 			$scope.gridOptions.data = data;
     		    			 			console.log($scope.gridOptions.data);
     		    			 		});
    		    					 
    		    				 }
    		    				 });
    		    			 }
    		    			 
    		    			 $scope.productTab = function(status) {
    		    				 $scope.addObj = "product";
    		    				 $scope.ch = false;
    		    				 $scope.doPublic = 4;
    		    				 $scope.flagForUser = true;
    		    				 apiserviceSelectCollection.getAllProductByCollection($scope.mainCollId,status).then(function(data){
    		    					 console.log(data);
    		    			 		 $scope.gridOptions4.data = data;
    		    			 		angular.forEach($scope.gridOptions4.data, function(row,key) {
    		    			 			row.collectionTitle = row.collection.title;
	    			 				});
    		    			 	});
    		    			 }
    		    			 
    		    			
    		    			
    		    			 $scope.desGrid = function(){
    		    				 $scope.array = [
    		    		    		                                 { name: 'title', displayName: 'Title', width:'15%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <a style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'description', displayName: 'Description',enableColumnMenu: false, width:'15%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" title="{{row.entity.description}}" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
    		    		    		                                 },
    		    		    		                                 { name: 'fileName', displayName: 'Logo', width:'15%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <a ng-click="grid.api.expandable.myClickFun(row.entity)" style="line-height: 200%;white-space: nowrap;" title="{{row.entity.fileName}}" data-content="{{row.entity.fileName}}" >{{row.entity.fileName}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'addedDate', displayName: 'Date Added',enableColumnMenu: false, width:'15%',
    		    		    		                                 },
    		    		    		                                 { name: 'countImages', displayName: 'Images',enableColumnMenu: false,enableFiltering: false, width:'9%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <a ng-click="grid.appScope.gotoImgTag(row)" style="line-height: 200%;" title="" data-content="{{row.entity.countImages}}">{{row.entity.countImages}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'pageViewCount', displayName: 'Views',enableColumnMenu: false,enableFiltering: false, width:'8%',cellEditableCondition: false,
    		    		    		                                 },
    		    		    		                                 { name: 'countProduct', displayName: 'Product Count',enableColumnMenu: false,enableFiltering: false, width:'8%',cellEditableCondition: false,
    		    		    		                                 },
    		    		    		                                 
    		    		    		                                 { name: 'Hide', displayName: 'Hide', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
    		    		        		                                 cellTemplate:'<input type="checkbox" ng-checked="row.entity.hideWebsite" name="vehicle" ng-click="grid.appScope.hideProduct(row)" autocomplete="off">', 
    		    		    		                                 }
    		    		    		                                 
    		    		        		                                
    		    		        		                                 ];
    		    				 
    		    				 	return $scope.array;
    		    			 }
    		    			 
    		    		
    		    	
    		    			 
    	$scope.editVehicle = function(row) {
    		$location.path('/editVehicleNew/'+row.entity.id+"/"+false+"/"+row.entity.vin);
    	}	 
    	
   $scope.vehiClesList = [];
  
   $scope.addMainCollFields = [];
   $scope.viewVehiclesInit = function() {
	   console.log("?>?>?>?>?");
	   $scope.newlyArrivedTab();
	   setTimeout(function(){ $scope.newlyArrivedTab(); }, 1000);
	   
	   apiserviceSelectCollection.getAllInventoryData().then(function(data){
			console.log(data);
			$scope.addMainCollFields = data;
			console.log($scope.addMainCollFields);
	   });
			
   }
   
  
   $scope.deleteProductAndCollectionPerMa  = function(row){
	   console.log("kk");
	   $('#modal-basic1').modal('show');
	   $scope.rowDataVal = row;  
	 
		   
	   
   }
  
   $scope.deleteVehicleRow = function() {
	   apiserviceSelectCollection.deleteVehicleById($scope.rowDataVal.entity.id).then(function(data){
		   $scope.newlyArrivedTab();
			   
		});
   }
   
$scope.deleteVehicleRowPer = function() {
	   apiserviceSelectCollection.deleteVehicleByIdPer($scope.rowDataVal.entity.id).then(function(data){
		   if(data == "Error"){
			   $.pnotify({
				    title: "Error",
				    type:'success',
				    text: "Can't Delete parent collection",
				});
		   }
		   $scope.soldTab();
		});
   }

$scope.deleteProductRowPer = function() {
	   apiserviceSelectCollection.deleteProductRowPer($scope.rowDataVal.entity.id).then(function(data){
		   if(data == "Error"){
			   $.pnotify({
				    title: "Error",
				    type:'success',
				    text: "Can't Delete parent collection",
				});
		   }
		   $scope.soldTab();
		});
}


   
   $scope.soldContact = {};
   
   $scope.updateVehicleStatusPublic = function(row){
	   apiserviceSelectCollection.addPublicCar(row.entity.id).then(function(data){
	   
		   	$scope.hideText = "Vehicle has been published";
		   	$scope.hideTitle = "Vehicle has been published";
			$('#hideVehicle').click();
			   
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
		apiserviceSelectCollection.setVehicleStatus($scope.soldContact).then(function(data){
		
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
       apiserviceSelectCollection.updateVehicle(tableData).then(function(data){
      
		});
   };
   
   $scope.cancle = function(tableData){
	   $scope.editingData[tableData.id] = false;
	   $scope.viewField = false;
   }
   
   $scope.exportDataAsCSV = function() {
	   apiserviceSelectCollection.exportDataAsCSV().then(function(data){
	   
		});
   }
   
   $scope.exportCarfaxCSV = function() {
	   apiserviceSelectCollection.exportCarfaxCSV().then(function(data){
	   
		});
   }
   
   $scope.exportCarGurusCSV = function() {
	   apiserviceSelectCollection.exportCarGurusCSV().then(function(data){
	   
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
		 apiserviceSelectCollection.getAllCollectionList().then(function(data){
		
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
		console.log(" In addManufacutures function ");
		
	}
	
	
	$scope.collections = {};
	$scope.saveCollectionData = function(){
		console.log("in collection");
		$scope.collections.title = $scope.collections.title;
		if($scope.collections.id == undefined){
			$scope.collections.id = 0;
		}
		console.log($scope.collections);
		apiserviceSelectCollection.addNewCollection($scope.collections).then(function(data){
		
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
		 apiserviceSelectCollection.deleteCollectionData($scope.collections).then(function(data){
		
				 console.log("out deletend functio");
				 $scope.allCollectionData();
				 
				 

			});
	 }
	
	
}]).filter('myProgressType', function() {
	  return function (input) {
		    return +input > 65 ? 'danger' : (+input > 35 ? 'warning' : 'success');
		  };;
		});

		(function() {
		    'use strict';

		    angular.module('ui.grid.draggable-rows', ['ui.grid'])

		    .constant('uiGridDraggableRowsConstants', {
		        featureName: 'draggableRows',
		        ROW_OVER_CLASS: 'ui-grid-draggable-row-over',
		        ROW_OVER_ABOVE_CLASS: 'ui-grid-draggable-row-over--above',
		        ROW_OVER_BELOW_CLASS: 'ui-grid-draggable-row-over--below',
		        POSITION_ABOVE: 'above',
		        POSITION_BELOW: 'below',
		        publicEvents: {
		            draggableRows: {
		                rowDragged: function(scope, info, rowElement) {},
		                rowDropped: function(scope, info, targetElement) {},
		                rowOverRow: function(scope, info, rowElement) {},
		                rowEnterRow: function(scope, info, rowElement) {},
		                rowLeavesRow: function(scope, info, rowElement) {},
		                rowFinishDrag: function(scope) {}
		            }
		        }
		    })

		    .factory('uiGridDraggableRowsCommon', [function() {
		        return {
		            draggedRow: null,
		            draggedRowEntity: null,
		            position: null,
		            fromIndex: null,
		            toIndex: null
		        };
		    }])

		    .service('uiGridDraggableRowsService', ['uiGridDraggableRowsConstants', function(uiGridDraggableRowsConstants) {
		        this.initializeGrid = function(grid, $scope, $element) {
		            grid.api.registerEventsFromObject(uiGridDraggableRowsConstants.publicEvents);

		            grid.api.draggableRows.on.rowFinishDrag($scope, function() {
		                angular.forEach($element[0].querySelectorAll('.' + uiGridDraggableRowsConstants.ROW_OVER_CLASS), function(row) {
		                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
		                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
		                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
		                });
		            });
		        };
		    }])

		    .service('uiGridDraggableRowService', ['uiGridDraggableRowsConstants', 'uiGridDraggableRowsCommon', function(uiGridDraggableRowsConstants, uiGridDraggableRowsCommon) {
		        var move = function(from, to) {
		            this.splice(to, 0, this.splice(from, 1)[0]);
		        };

		        this.prepareDraggableRow = function($scope, $element) {
		            var grid = $scope.grid;
		            var data = grid.options.data;
		            var row = $element[0];
		            var listeners = {
		                onDragOverEventListener: function(e) {
		                    if (e.preventDefault) {
		                        e.preventDefault();
		                    }

		                    var dataTransfer = e.dataTransfer || e.originalEvent.dataTransfer;
		                    dataTransfer.effectAllowed = 'copyMove';
		                    dataTransfer.dropEffect = 'move';

		                    var offset = e.offsetY || e.layerY || (e.originalEvent ? e.originalEvent.offsetY : 0);

		                    $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_CLASS);

		                    if (offset < this.offsetHeight / 2) {
		                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_ABOVE;

		                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
		                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);

		                    } else {
		                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_BELOW;

		                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
		                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
		                    }

		                },

		                onDragStartEventListener: function(e) {
		                    this.style.opacity = '0.5';

		                    uiGridDraggableRowsCommon.draggedRow = this;
		                    uiGridDraggableRowsCommon.draggedRowEntity = $scope.$parent.$parent.row.entity;

		                    uiGridDraggableRowsCommon.position = null;

		                    uiGridDraggableRowsCommon.fromIndex = data.indexOf(uiGridDraggableRowsCommon.draggedRowEntity);
		                    uiGridDraggableRowsCommon.toIndex = null;

		                },

		                onDragLeaveEventListener: function() {
		                    this.style.opacity = '1';

		                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
		                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
		                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);

		                    
		                },

		                onDragEnterEventListener: function() {
		                },

		                onDragEndEventListener: function() {
		                },

		                onDropEventListener: function(e) {
		                    var draggedRow = uiGridDraggableRowsCommon.draggedRow;

		                    if (e.stopPropagation) {
		                        e.stopPropagation();
		                    }

		                    if (e.preventDefault) {
		                        e.preventDefault();
		                    }

		                    if (draggedRow === this) {
		                        return false;
		                    }

		                    uiGridDraggableRowsCommon.toIndex = data.indexOf($scope.$parent.$parent.row.entity);

		                    if (uiGridDraggableRowsCommon.position === uiGridDraggableRowsConstants.POSITION_ABOVE) {
		                        if (uiGridDraggableRowsCommon.fromIndex < uiGridDraggableRowsCommon.toIndex) {
		                            uiGridDraggableRowsCommon.toIndex -= 1;
		                        }

		                    } else if (uiGridDraggableRowsCommon.fromIndex >= uiGridDraggableRowsCommon.toIndex) {
		                        uiGridDraggableRowsCommon.toIndex += 1;
		                    }

		                    $scope.$apply(function() {
		                        move.apply(data, [uiGridDraggableRowsCommon.fromIndex, uiGridDraggableRowsCommon.toIndex]);
		                    });
		                    console.log(data);	
		                    	console.log("............................");
		                    	$.ajax({
			                    		type: "POST",
			                            url: '/saveCollectionOrder',
			                            data: JSON.stringify(data),
			                            contentType: "application/json; charset=utf-8",
			                            dataType: "json",
		                    		});
		                    

		                    e.preventDefault();
		                }
		            };

		            row.addEventListener('dragover', listeners.onDragOverEventListener, false);
		            row.addEventListener('dragstart', listeners.onDragStartEventListener, false);
		            row.addEventListener('dragleave', listeners.onDragLeaveEventListener, false);
		            row.addEventListener('dragenter', listeners.onDragEnterEventListener, false);
		            row.addEventListener('dragend', listeners.onDragEndEventListener, false);
		            row.addEventListener('drop', listeners.onDropEventListener);
		        };
		    }])

		    .directive('uiGridDraggableRow', ['uiGridDraggableRowService', function(uiGridDraggableRowService) {
		        return {
		            restrict: 'ACE',
		            scope: {
		                grid: '='
		            },
		            compile: function() {
		                return {
		                    pre: function($scope, $element) {
		                        uiGridDraggableRowService.prepareDraggableRow($scope, $element);
		                    }
		                };
		            }
		        };
		    }])

		    .directive('uiGridDraggableRows', ['uiGridDraggableRowsService', function(uiGridDraggableRowsService) {
		        return {
		            restrict: 'A',
		            replace: true,
		            priority: 0,
		            require: 'uiGrid',
		            scope: false,
		            compile: function() {
		                return {
		                    pre: function($scope, $element, $attrs, uiGridCtrl) {
		                        uiGridDraggableRowsService.initializeGrid(uiGridCtrl.grid, $scope, $element);
		                    }
		                };
		            }
		        };
		    }]);
		}());


