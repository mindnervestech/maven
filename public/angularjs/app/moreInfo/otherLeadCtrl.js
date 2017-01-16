angular.module('newApp')
.controller('otherLeadCtrl', ['$scope','$http','$location','$filter','$interval','$routeParams','apiserviceMoreInfo', function ($scope,$http,$location,$filter,$interval,$routeParams,apiserviceMoreInfo) {
	
	console.log("&&");
	console.log($routeParams.leadId);
	$scope.leadId = $routeParams.leadId;
	
	$scope.leadList = [];
	apiserviceMoreInfo.getSelectedLeadType().then(function(response){
	
		console.log(response);
		angular.forEach(response, function(value, key) {
			if(value.callToAction){
				$scope.leadList.push(value); 
			}
			if(value.id == $scope.leadId){
				$scope.leadName = value.leadName;
				$scope.leadId = value.id;
			}
		});
		console.log($scope.leadList);
		console.log($scope.leadId);
	});
	
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
										{ name: 'title', displayName: 'Title', width:'12%',cellEditableCondition: false,
										 	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
										        if (row.entity.isRead === false) {
										          return 'red';
										        }
										 	} ,
										  },
										
 		                                 { name: 'name', displayName: 'Name', width:'9%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'phone', displayName: 'Phone',enableFiltering: false, width:'8%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'10%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'requestDate', displayName: 'Request Date',enableFiltering: false, width:'10%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                { name: 'price', displayName: 'Price',enableFiltering: false, width:'5%',cellEditableCondition: false,
 		                                	cellTemplate:'<div>{{row.entity.price | number:0}}</div>',
	   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    		                                       if (row.entity.isRead === false) {
	    		                                         return 'red';
	    		                                     }
	   		                                	} ,
	   		                             },
 		                                { name: 'salesRep', displayName: 'Sales Rep',enableFiltering: false, width:'10%',cellEditableCondition: false,
  		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
  		                                 },
  		                               { name: 'status', displayName: 'Status',enableFiltering: false, width:'8%',cellEditableCondition: false,
  		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
  		                                 },
  		                               { name: 'message', displayName: 'Message',enableFiltering: false, width:'8%',cellEditableCondition: false,
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
		          $scope.gridOptions.data = $filter('filter')($scope.requsetMoreList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'stock':grid.columns[3].filters[0].term,'name':grid.columns[4].filters[0].term},undefined);
		        });
	   		
  		};
  		apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data){
	  
				console.log(data);
				
				
			//$scope.gridOptions.data = data;
				$scope.editgirdData(data);
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
					$scope.gridOptions.columnDefs.push({ name: 'viewPdf', displayName: 'View',enableFiltering: false, width:'5%',cellEditableCondition: false,
	                  	cellTemplate:'<a href="/showPdf/{{row.entity.id}}" target="_blank">View</a>',
	                        	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	                                if (row.entity.isRead === false) {
	                                  return 'red';
	                              }
	                        	} ,
	                     });
				}
				
				console.log( $scope.gridMapObectView);
				console.log($scope.gridMapObect);
				console.log("------------------------------------------------");
				angular.forEach($scope.gridMapObectView,function(value,key){
					var name = value.key;
					name = name.replace(" ","");
					console.log(name);
					$scope.gridOptions.columnDefs.push({ name: name, displayName: value.label, width:'10%',cellEditableCondition: false,
		              	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		              		if (row.entity.isRead === false) {
	                            return 'red';
	                        }
		              	} ,
		               });
				});
				
				$scope.gridOptions.columnDefs.push({ name: 'isRead', displayName: 'Claim',enableFiltering: false, width:'7%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
                	 cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity.id)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="margin-left:18%;"></div>', 
                  	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                          if (row.entity.isRead === false) {
                            return 'red';
                        }
                  	} ,
                   });
				
				console.log($scope.gridOptions.data);
		  }
	  
	  $scope.getAllRequestInfo = function() {
		  apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data){
		  console.log(data);
			//$scope.gridOptions.data = data;
				$scope.editgirdData(data);
			$scope.requsetMoreList = data;
		});
	  
	  }
	  
	  var promo =  $interval(function(){
		  apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data){
			  console.log(data);
				//$scope.gridOptions.data = data;
					$scope.editgirdData(data);
				$scope.requsetMoreList = data;
			});
	  },60000);
	  
	  $scope.premiumFlagForSale = 0;
  $scope.setAsRead = function(flag,id) {
	  apiserviceMoreInfo.requestInfoMarkRead(flag, id).then(function(data){
	  
			//$scope.gridOptions.data = data;
			apiserviceMoreInfo.getAllOtherLeadInfo($scope.leadId).then(function(data1){
			
				console.log(data1);
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
		console.log(leads.id);
		$location.path('/otherLeads/'+leads.id);
	}
	$scope.requestMore = function() {
		$location.path('/requestMoreInfo');
	} 
  
	$scope.exportCsvPop = function(){
		console.log("check");
		$('#exportModal').modal('show');
	};
	
	$scope.exportCsvOtherLead = function(){
		console.log($scope.leadId);
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
								console.log('fail');
								console.log(response);
								console.log(e);
							});
			});
		}
	
}]);