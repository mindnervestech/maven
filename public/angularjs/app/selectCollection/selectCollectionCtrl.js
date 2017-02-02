angular.module('newApp')
.controller('selectCollectionCtrl', ['$scope','$http','$location','$filter','$upload','apiserviceSelectCollection','$routeParams','$q','$window', function ($scope,$http,$location,$filter,$upload,apiserviceSelectCollection,$routeParams,$q,$window) {
	
	console.log("Hello this is ***** this is selectCollectionCtrl  ");
	console.log($routeParams.leadId);
	console.log($routeParams.type);
	//show
	//save
	//edit
	//editValue
	if($routeParams.type == "editValue"){
		$scope.editleadtype = {};
		$scope.selectmanu = [];
		apiserviceSelectCollection.getLeadTypeDataById($routeParams.leadId).then(function(data){
			console.log("lead typeeeeeeeeeeee");
			console.log(data);
			$scope.editleadtype.profile = data.profile;
			if(data.profile == "Select Collections Manually" || data.profile == "Select products Manually"){
				var arr = [];
				arr = data.maunfacturersIds.split(',');
				for(var i=0;i<arr.length;i++){
					$scope.selectmanu.push(arr[i]);
				}
				  if(data.profile == "Select Collections Manually"){
					  $scope.newlyArrivedTab();
				  }else if(data.profile == "Select products Manually"){
					  $scope.productTab();
				  }
			}
			
		});
	}
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
			var flag =0;
			angular.forEach($scope.allCollectionDate, function(obj, index){
				if(flag == 0){
					if(obj.hideWebsite == false){
						flag = 1;
						$scope.getCollectionData(obj);
					}
				}
			});
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
	 /*$scope.onLogoFileSelect = function($files) {
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
	}*/
     $scope.gridOptions = {
    		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
    		 expandableRowTemplate: '/dealer/viewInventory/expandableRowTemplate.html',
    		 expandableRowScope: {
	  				  
					 manuClicked:function(item,value){
						 console.log("xxxcccc");
						 console.log(value);
						 $scope.manuClicked(item,value);
					 }
    			 
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
         		                                 cellTemplate:'<input type="checkbox" ng-checked="row.entity.hideWebsite" name="vehicle" ng-model="selected" ng-change="grid.options.expandableRowScope.manuClicked(row,selected)" autocomplete="off">', 
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
															cellTemplate:'<input type="checkbox" ng-checked="row.entity.hideWebsite" name="vehicle" ng-model="selected" ng-change="grid.appScope.manuClicked(row,selected)" autocomplete="off">', 
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
     		    			 			 console.log($scope.selectmanu);
         		    					angular.forEach($scope.selectmanu, function(obj, index){
         		    						angular.forEach($scope.gridOptions.data, function(obj1, index1){
         		    							if(obj == obj1.id){
         		    								obj1.hideWebsite = true;
         		    							}
         		    							 angular.forEach(obj1.subCollection, function(obj2, index2){
         		    								 if(obj == obj2.id){
         		    									 obj2.hideWebsite = true;
         		    								 }
         		    							 });
         		    						});
         		    					});
     		    			 			console.log($scope.gridOptions.data);
     		    			 		});
    		    					 
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
    		    			 			row.hideWebsite = false;
	    			 				});
    		    			 		
    		    			 		angular.forEach($scope.selectmanu, function(obj, index){
     		    						angular.forEach($scope.gridOptions4.data, function(obj1, index1){
     		    							if(obj == obj1.id){
     		    								obj1.hideWebsite = true;
     		    							}
     		    						});
     		    					});
    		    			 	});
    		    			 }
    		    			 
    		    			
    		    			
    		    			 $scope.desGrid = function(){
    		    				 $scope.array = [
					    		    				                 { name: 'select', displayName: 'Select', width:'5%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
					    		    				                	 cellTemplate:'<input type="checkbox" ng-checked="row.entity.hideWebsite" name="vehicle" ng-model="selected" ng-change="grid.appScope.manuClicked(row,selected)" autocomplete="off">', 
					         		                                 }, 
    		    		    		                                 { name: 'title', displayName: 'Title', width:'25%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <a style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'description', displayName: 'Description',enableColumnMenu: false, width:'25%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <label  style="line-height: 200%;" title="{{row.entity.description}}" data-content="{{row.entity.description}}" >{{row.entity.description}}</label> </div>',
    		    		    		                                 },
    		    		    		                                 { name: 'fileName', displayName: 'Logo', width:'25%',cellEditableCondition: false,
    		    		    		                                	 cellTemplate: '<div> <a style="line-height: 200%;white-space: nowrap;" title="{{row.entity.fileName}}" data-content="{{row.entity.fileName}}" >{{row.entity.fileName}}</a></div>',
    		    		    		                                 },
    		    		    		                                 { name: 'addedDate', displayName: 'Date Added',enableColumnMenu: false, width:'15%',
    		    		    		                                 },
    		    		    		                                    		    		    		                                 
    		    		        		                                
    		    		        		                                 ];
    		    				 
    		    				 	return $scope.array;
    		    			 }
    		    			 
    		    		
    		    	
    		    			 
    	
    	
   $scope.vehiClesList = [];
  
   $scope.addMainCollFields = [];
   $scope.viewVehiclesInit = function() {
	   if($routeParams.type != "editValue"){
		   $scope.newlyArrivedTab();
		   setTimeout(function(){ $scope.newlyArrivedTab(); }, 1000);
		   
		   apiserviceSelectCollection.getAllInventoryData().then(function(data){
				console.log(data);
				$scope.addMainCollFields = data;
				console.log($scope.addMainCollFields);
		   });
	   }
			
   }
   
   $scope.soldContact = {};
   
   $scope.updateVehicleStatusPublic = function(row){
	   apiserviceSelectCollection.addPublicCar(row.entity.id).then(function(data){
	   
		   	$scope.hideText = "Vehicle has been published";
		   	$scope.hideTitle = "Vehicle has been published";
			$('#hideVehicle').click();
			   
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
	
	$scope.leadcreateData = {};
	$scope.addCollectionAndProduct = function(){
		
		if($routeParams.type == "save"){
				localStorage.setItem('popupType','Lead');
				$scope.leadcreateData.leadName = $routeParams.leadId;
				$scope.leadcreateData.callToAction = true;
				$scope.leadcreateData.hideLead = false;
				localStorage.setItem('callToAction',true);
				localStorage.setItem('hideLead',true);
				console.log($scope.leadcreateData);
				apiserviceSelectCollection.addnewrUser($scope.leadcreateData).then(function(data){
					localStorage.setItem('popupType','Lead');
					localStorage.setItem('leadId', data.id);
					$scope.saveCollectionAndProd(data.id).then(function(respones){
						$window.location.reload();
						localStorage.setItem('typeOfForm', $routeParams.leadId);
						$location.path('/otherForm/'+"save"+"/"+$routeParams.leadId);
					});
				});
		}else if($routeParams.type == "show" || $routeParams.type == "editValue" ){
			$scope.saveCollectionAndProd($routeParams.leadId).then(function(respones){
				$location.path('/leadtype');
			});
			 
		}else if($routeParams.type == "edit"){
			$scope.saveCollectionAndProd($routeParams.leadId).then(function(respones){
				$location.path('/leadtype');
			});
		}
		
	}
		$scope.saveCollectionAndProd = function(id){
			var defer = $q.defer();
			$scope.editleadtype.id = id;
			$scope.editleadtype.maunfacturersIds = $scope.selectmanu.toString();
			 console.log("out of funtion");
			 console.log($scope.editleadtype);
			 if($scope.editleadtype.profile == "All Collections" || $scope.editleadtype.profile == "All Products"){
				 $scope.editleadtype.maunfacturersIds = null;
			 }
			 apiserviceSelectCollection.Updatecheckbox($scope.editleadtype).then(function(data){
				 console.log(data);
				 console.log("in of funtion");
	    	});
			defer.resolve("ok");
    		return defer.promise;
		}
	
	
	$scope.editCollection = function(row){
		console.log(row.entity);
		$scope.collections = row.entity;
		$('#editcollectionpopup').modal('show');
	}
	
	$scope.selectmanu = [];
	  $scope.manuClicked = function(rolePer,value){
			console.log(rolePer.entity);
			console.log(value);
			console.log($scope.editleadtype.profile);
			console.log($scope.gridOptions.data);
			if(value == true){//ng-checked="row.entity.hideWebsite"
				$scope.selectmanu.push(rolePer.entity.id);
					if($scope.editleadtype.profile == "Select products Manually"){
						angular.forEach($scope.gridOptions4.data, function(obj, index){
							if(rolePer.entity.id == obj.id){
								obj.hideWebsite = true;
							}
						});
					}else if($scope.editleadtype.profile == "Select Collections Manually"){
						angular.forEach($scope.gridOptions.data, function(obj, index){
							if(rolePer.entity.id == obj.id){
								obj.hideWebsite = true;
								 angular.forEach(obj.subCollection, function(obj1, index1){
									 obj1.hideWebsite = true;
									 $scope.selectmanu.push(obj1.id);
								 });
							}
						});
					}
				
			}else{
				console.log("sssss");
				$scope.deleteItems(rolePer.entity);
					if($scope.editleadtype.profile == "Select products Manually"){
						angular.forEach($scope.gridOptions4.data, function(obj, index){
							if(rolePer.entity.id == obj.id){
								obj.hideWebsite = false;
							}
							 
						});
					}else if($scope.editleadtype.profile == "Select Collections Manually"){
						angular.forEach($scope.gridOptions.data, function(obj, index){
							if(rolePer.entity.id == obj.id){
								obj.hideWebsite = false;
								 angular.forEach(obj.subCollection, function(obj1, index1){
									 obj1.hideWebsite = false;
									 $scope.deleteItems(obj1);
								 });
							}
							 
						});
					}
				
			}
			console.log($scope.selectmanu);
		}
		$scope.deleteItems = function(rolePer){
			angular.forEach($scope.selectmanu, function(obj, index){
				 if ((rolePer.id == obj)) {
					 $scope.selectmanu.splice(index, 1);
			       	return;
			    };
			  });
		}
		
		
		$scope.selectProfile = function(selectP){
			$scope.selectmanu = [];
			  if(selectP == "All Collections"){
				  angular.forEach($scope.manufacturerslist, function(obj, index){
					  $scope.selectmanu.push(obj.id);
				 });
			  }else if(selectP == "All Products"){
				  
			  }else if(selectP == "Select Collections Manually"){
				  $scope.newlyArrivedTab();
			  }else if(selectP == "Select products Manually"){
				  $scope.productTab();
			  }
			  console.log($scope.selectmanu);
			  
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


