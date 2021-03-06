angular.module('newApp')
.controller('premiumCtrl', ['$scope','$http','$location','$filter','$interval', function ($scope,$http,$location,$filter,$interval) {
	$scope.leadList = [];
	apiserviceMoreInfo.getSelectedLeadType().then(function(response){
	
		angular.forEach(response, function(value, key) {
			if(value.id > 3){
				$scope.leadList.push(value); 
			}
		});
	
	});
	
	
  $scope.gridOptions = {
 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
 		    paginationPageSize: 150,
 		    enableFiltering: true,
 		    useExternalFiltering: true,
 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
 		 };
 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
 		 $scope.gridOptions.enableVerticalScrollbar = 2;
 		 $scope.gridOptions.columnDefs = [
 		                                 { name: 'title', displayName: 'Title', width:'12%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
 		                                       if (row.entity.isRead === false) {
 		                                         return 'red';
 		                                       }
 		                                	} ,
 		                                 },
 		                                 { name: 'designer', displayName: 'Designer', width:'8%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
  		                                       if (row.entity.isRead === false) {
  		                                         return 'red';
  		                                     }
 		                                	} ,
 		                                 },
 		                                 { name: 'year', displayName: 'Year', width:'7%',cellEditableCondition: false,
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
 		                                 { name: 'email', displayName: 'Email',enableFiltering: false, width:'9%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                                 { name: 'requestDate', displayName: 'Request Date',enableFiltering: false, width:'8%',cellEditableCondition: false,
 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
   		                                       if (row.entity.isRead === false) {
   		                                         return 'red';
   		                                     }
  		                                	} ,
 		                                 },
 		                               
		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'6%',cellEditableCondition: false,
	   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    		                                       if (row.entity.isRead === false) {
	    		                                         return 'red';
	    		                                     }
	   		                                	} ,
	   		                                 },
	   		                               { name: 'leadType', displayName: 'Type of Request',enableFiltering: false, width:'9%',cellEditableCondition: false,
	   		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
	    		                                       if (row.entity.isRead === false) {
	    		                                         return 'red';
	    		                                     }
	   		                                	} ,
	   		                                 },
	   		                              { name: 'claim', displayName: 'Claim',enableFiltering: false, width:'3%',cellEditableCondition: false,
	   		                                	cellTemplate:'<div class="icheck-list"><input type="checkbox" ng-model="row.entity.isRead" ng-change="grid.appScope.setAsRead(row.entity.isRead,row.entity)" data-checkbox="icheckbox_flat-blue" title="Claim this lead" style="margin-left:18%;"></div>',
	   		                                	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		    		                                       if (row.entity.isRead === false) {
		    		                                         return 'red';
		    		                                     }
		   		                                	} ,
		   		                                 },
		                                 { name: 'btn', displayName: 'Assign',enableFiltering: false, width:'17%', cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
 		                                	 cellTemplate:'<button type="button" ng-click="grid.appScope.assignCanceledLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:3%;">ASSIGN</button><button type="button" ng-click="grid.appScope.releaseLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:3%;">RELEASE</button><button type="button" ng-click="grid.appScope.deleteLead(row.entity)" class="btn btn-sm btn-primary" style="margin-top:2%;margin-left:3%;">REMOVE</button>', 
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
		          $scope.gridOptions.data = $filter('filter')($scope.tradeInList,{'vin':grid.columns[0].filters[0].term,'model':grid.columns[1].filters[0].term,'make':grid.columns[2].filters[0].term,'stock':grid.columns[3].filters[0].term,'name':grid.columns[4].filters[0].term},undefined);
		        });
	   		
		};
 		 
//		setting claim
		$scope.setAsRead = function(flag,row) {  
			if(flag){
				apiserviceMoreInfo.changeAssignedUser(row.id, userKey, row.leadType).then(function(data){
		        		$scope.getAllPremiumData();
					});
			}
			
		  }
		apiserviceMoreInfo.getSalesUserValue().then(function(data){
		
			$scope.salesPersonPerf = data;
			
		});
		
		
		 $scope.changeAssignedUser = function() {
			 apiserviceMoreInfo.changeAssignedUser($scope.leadlId, $scope.changedUser, $scope.leadType).then(function(data){
					$('#assignUserModal').modal('hide');
					 $scope.getAllPremiumData();
				});
	        	
	        }
		 $scope.leadId = null;
		 $scope.leadType = null;
		 $scope.deleteLead = function(entity){
			 $scope.leadId = entity.id;
			 $scope.leadType = entity.leadType;
			 $('#deleteBtn').click();
		 };
		 
		 $scope.deletePremiumLead = function(){
			 apiserviceMoreInfo.deletePremiumLead($scope.leadId, $scope.leadType).then(function(data){
				 apiserviceMoreInfo.getAllPremiumIn().then(function(data){
					$scope.gridOptions.data = data;
					$scope.tradeInList = data;
					if(data.length > 0){
						$scope.userRole = data[0].userRole;
					}
				});
				});
		 };
		 
		 $scope.releaseLead = function(entity){
			 apiserviceMoreInfo.releaseLeads(entity.id, entity.leadType).then(function(data){
			 
				 $scope.getAllPremiumData();
				});
		 }
		
 		    	
		 $scope.userRole = null;
		 apiserviceMoreInfo.getAllPremiumIn().then(function(data){
	  
			$scope.gridOptions.data = data;
			$scope.tradeInList = data;
			if(data.length > 0){
				$scope.userRole = data[0].userRole;
			}
		});
  
	  $scope.getAllPremiumData = function() {
		  apiserviceMoreInfo.getAllPremiumIn().then(function(data){
		  
			$scope.gridOptions.data = data;
			//$scope.tradeInList = data;
		});
	  }
	  
	  var promo =  $interval(function(){
		  apiserviceMoreInfo.getAllPremiumIn().then(function(data){
			  
				$scope.gridOptions.data = data;
				$scope.tradeInList = data;
			});
		},60000);
	  
	 
	  
	  $scope.assignCanceledLead = function(entity) {
      	$scope.leadlId = entity.id;
      	$scope.leadType = entity.leadType;
      	$scope.changedUser = "";
      	$('#btnAssignUser').click();
      }
      
     
	  
		 
	  $scope.testDrive = function() {
			$location.path('/scheduleTest');
		} 
	  $scope.requestMore = function() {
			$location.path('/requestMoreInfo');
		}  
	  $scope.tradeIn = function() {
			$location.path('/tradeIn');
		}
	  $scope.goContactUs = function() {
			$location.path('/contactUsInfo');
		}
	  $scope.otherLeads = function(leads) {
			$location.path('/otherLeads/'+leads.id);
		}
	  
}]);