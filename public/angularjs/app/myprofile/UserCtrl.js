angular.module('newApp')
.controller('createUserCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceUser', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceUser) {
	console.log($routeParams.inPage);
	$scope.user = {};

	$scope.img = "/assets/images/profile-pic.jpg";
	$scope.user = {};
	$scope.permission = [];
	$scope.permissionList =[
	{name:'Dashboard',isSelected:false},
	{name:'Website Editing',isSelected:false},
	{name:'Add Vehicle',isSelected:false},
	{name:'Inventory',isSelected:false},
	{name:'Customer Requests',isSelected:false},
	{name:'Blogs',isSelected:false},
	{name:'My Profile',isSelected:false},
	{name:'Website Analytics',isSelected:false},
	{name:'CRM',isSelected:false},
	{name:'Financial Statistics',isSelected:false},
	{name:'Account Settings',isSelected:false}];
	
	
	apiserviceUser.getUserRole().then(function(data){
		$scope.userRole = data;
		console.log("ddddddddddddddddd",data);
	});
	/*$http.get('/getAllPermission')
	.success(function(data) {
		$scope.permissionList =[];
		angular.forEach(data, function(obj, index){
			var jsonObj = {name:obj.name,isSelected:false};
			$scope.permissionList.push(jsonObj);
		});
		console.log($scope.permissionList);
		console.log("????????????????");
	});*/
	
	$http.get('/getAllPermissionById')
	.success(function(data) {
		console.log(data);
		$scope.permissionList =[];
		/*angular.forEach(data, function(obj, index){
			var jsonObj = {name:obj.name,isSelected:false,id:obj.id};
			$scope.permissionList.push(jsonObj);
		});*/
		angular.forEach(data, function(obj, index){
			obj.isSelected = false;
			if(obj.name != "Inventory"){
				angular.forEach(obj.childData, function(obj1, index1){
					obj1.isSelected = false;
				});
			}
			if(obj.name == "Customer Requests"){
				angular.forEach(obj.childData, function(obj2, index1){
					if(obj2.name != "Able to assign online request to other sales people" || obj2.name != "Abel to add online request to CRM database" || obj2.name != "Delete online request from the database"){
						obj2.isSelected = "";
					}
				});
			}
			if(obj.name == "CRM"){
				angular.forEach(obj.childData, function(obj2, index1){
					if(obj2.name != "Manage Groups" || obj2.name != "Export contacts" || obj2.name != "Import Contacts"){
						obj2.isSelected = "";
					}
				});
			}
		});
		$scope.permissionList = data;
		console.log($scope.permissionList);
	});
	
	$scope.userData = {};
	$scope.trial;
	$scope.num;
	$scope.duration="month";
	$scope.contactVal;
	$scope.imagesList =[
	                    	{name:'img1',srcName:'/assets/images/p1.png'},
	                    	{name:'img2',srcName:'/assets/images/p2.png'},
	                    	{name:'img3',srcName:'/assets/images/p3.png'},
	                    	{name:'img4',srcName:'/assets/images/p4.png'},
	                    	{name:'img5',srcName:'/assets/images/p5.png'},
	                    	{name:'img6',srcName:'/assets/images/p6.png'},
	                    	{name:'img7',srcName:'/assets/images/p7.png'}
	                       ];
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
	 		                                 { name: 'fullName', displayName: 'Name', width:'25%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'email', displayName: 'Email', width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'phone', displayName: 'Phone', width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'userType', displayName: 'Role', width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'edit', displayName: '', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editUser(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteUser(row)"></i>', 
    		                                 
    		                                 },
	     		                                 ];
	
	 		$scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.activeUserList,{'fullName':grid.columns[0].filters[0].term,'email':grid.columns[1].filters[0].term,'phone':grid.columns[2].filters[0].term,'userType':grid.columns[3].filters[0].term,},undefined);
			        });
		   		
	   		};
	 		 
	 		 
	 		$scope.gridOptions1 = {
	 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		 		    paginationPageSize: 150,
	 		 		    enableFiltering: false,
	 		 		    useExternalFiltering: false,
	 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 		 };
	 		 		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
	 		 		 $scope.gridOptions1.enableVerticalScrollbar = 2;
	 		 		 $scope.gridOptions1.columnDefs = [
														{ name: 'edit', displayName: '', width:'5%',cellEditableCondition: false,
														 	cellTemplate:'<input type="checkbox" ng-change="grid.appScope.selectLead(row)" ng-model="row.entity.check"> ',
															 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
																 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
														               return 'red';
														           }
														     	} ,
														  },
														{ name: 'name', displayName: 'Name', width:'10%',cellEditableCondition: false,
													     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.name}}</a> ',
													    	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													    		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                   return 'red';
													               }
													         	} ,
													      },
													      { name: 'phone', displayName: 'Phone', width:'10%',cellEditableCondition: false,
													     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.phone}}</a> ',
													     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                return 'red';
													            }
													      	} ,
													      },
													      { name: 'email', displayName: 'Email', width:'15%',cellEditableCondition: false,
													     	cellTemplate:'<a  href="mailto:{{row.entity.email}}">{{row.entity.email}}</a> ',
													     	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                return 'red';
													            }
													      	} ,
													      },
													   { name: 'requestDate', displayName: 'Date Added', width:'10%',cellEditableCondition: false,
													     	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.requestDate}}</a> ',
													     	 cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
													     		 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
													                 return 'red';
													             }
													       	} ,
													       },
		 		 		                                 { name: 'vin', displayName: 'Vin', width:'15%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.vin}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                                 { name: 'model', displayName: 'Model', width:'10%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.model}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                                 { name: 'make', displayName: 'Make', width:'10%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.make}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                            { name: 'leadType', displayName: 'type', width:'15%',cellEditableCondition: false,
		 		 		                                	cellTemplate:'<a ng-click="grid.appScope.editVinData(row.entity)" style="color: #5b5b5b;">{{row.entity.leadType}}</a> ',
		 		 		                                	cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
		 		 		                                	 if (row.entity.confirmDate === null && row.entity.noteFlag != 1) {
		 		   		                                         return 'red';
		 		   		                                     }
		 		  		                                	} ,
		 		 		                                 },
		 		 		                                 
		 		     		                          ];	 		 
	 		 
	 		 		$scope.gridOptions2 = {
	 		 		 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		 		 		    paginationPageSize: 150,
	 		 		 		    enableFiltering: true,
	 		 		 		    useExternalFiltering: true,
	 		 		 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 		 		 };
	 		 		 		 $scope.gridOptions2.enableHorizontalScrollbar = 0;
	 		 		 		 $scope.gridOptions2.enableVerticalScrollbar = 2;
	 		 		 		 $scope.gridOptions2.columnDefs = [
	 		 		 		                                 { name: 'fullName', displayName: 'Name', width:'25%',cellEditableCondition: false,
	 		 		 		                                	
	 		 		 		                                 },
	 		 		 		                                 { name: 'email', displayName: 'Email', width:'20%',cellEditableCondition: false,
	 		 		 		                                	
	 		 		 		                                 },
	 		 		 		                                 { name: 'phone', displayName: 'Phone', width:'20%',cellEditableCondition: false,
	 		 		 		                                	
	 		 		 		                                 },
	 		 		 		                                 { name: 'userType', displayName: 'Role', width:'20%',cellEditableCondition: false,
	 		 		 		                                	
	 		 		 		                                 },
	 		 		 		                                 { name: 'edit', displayName: '', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	 		 	        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editPhotographer(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i> &nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deletePhotographer(row)"></i>', 
	 		 	    		                                 
	 		 	    		                                 },
	 		 		     		                                 ];
	 		 		
	 		 		 		$scope.gridOptions2.onRegisterApi = function(gridApi){
	 		 					 $scope.gridApi = gridApi;
	 		 					 
	 		 			   		$scope.gridApi.core.on.filterChanged( $scope, function() {
	 		 				          var grid = this.grid;
	 		 				        	  $scope.gridOptions2.data = $filter('filter')($scope.activePhotographerList,{'fullName':grid.columns[0].filters[0].term,'email':grid.columns[1].filters[0].term,'phone':grid.columns[2].filters[0].term,'userType':grid.columns[3].filters[0].term,},undefined);
	 		 				        });
	 		 			   		
	 		 		   		};
	 		 		 
	$scope.gotoProfile = function() {
		$location.path('/myprofile');
	};
	$scope.goToUsers = function() {
			$location.path('/createUser/normal');
		};
	$scope.goToContractors = function() {
			$location.path('/contractors');
		};
	$scope.goToDeactive = function() {
		$location.path('/deactiveUsers');
	};
	
	$scope.goToDeactivePhotographer = function() {
		$location.path('/deactivePhotographer');
	};
	$scope.gotoHoursOfOperation = function() {
			$location.path('/hoursOfOperations');
		};
	
	$scope.userId = null;
	$scope.checked = [];
	$scope.usersList = null;
	$scope.assignUser = null;
	$scope.userLeads = {};
	
	apiserviceUser.getAllUsersToAssign().then(function(data){
		$scope.usersList = data;
		console.log(data);
		angular.forEach($scope.usersList, function(obj, index){
			 if ((obj.userType == "Manager")) {
				 $scope.assignUser = obj.id;
				 
		    };
		  });	
	});
	/*$http.get('/getAllUsersToAssign')
	.success(function(data) {
		$scope.usersList = data;
		angular.forEach($scope.usersList, function(obj, index){
			 if ((obj.userType == "Manager")) {
				 $scope.assignUser = obj.id;
		    };
		  });
	});*/
	
	apiserviceUser.getInternalPdfData().then(function(data){
		$scope.internalPdfList=data;
		$scope.internalPdfListSize = $scope.internalPdfList.length;
		console.log($scope.internalPdfListSize);
	});
	
	/*$http.get('/getInternalPdfData')
		.success(function(data) {
			
			$scope.internalPdfList=data;
		});	*/	
	
	$scope.selectLead = function(row){
		if(row.entity.check == true){
			$scope.checked.push(row.entity);
		}else{
			$scope.deleteSelectedLead(row.entity);
		}
	};
	
	$scope.deleteSelectedLead = function(item){
		angular.forEach($scope.checked, function(obj, index){
			 if ((item.id == obj.id) && (item.leadType == obj.leadType)) {
				 $scope.checked.splice(index, 1);
		       	return;
		    };
		  });
	}
	
	$scope.selectAll = function(){
		$scope.checked = [];
		angular.forEach($scope.gridOptions1.data, function(obj, index){
			 obj.check = true;
			 $scope.checked.push(obj);
		  });
		console.log($scope.checked);
	};
	$scope.assignTo = function(){
		//console.log($scope.assignUser);
		//console.log($scope.checked);
		$scope.userLeads.id = $scope.assignUser;
		$scope.userLeads.leads = $scope.checked;
		//console.log($scope.userLeads);
		if($scope.userLeads.leads.length > 0){
			
			apiserviceUser.assignToUser($scope.userLeads).then(function(data){
				apiserviceUser.getAllLeadsByUser($scope.userId).then(function(data){
					$scope.gridOptions1.data = data;
				});
		            $scope.clouseUserLead();
			});
			
		}else{
			$.pnotify({
			    title: "Error",
			    type:'success',
			    text: "Please select leads",
			});
		}
	};
	$scope.deactivateAccount = function(){
		console.log($scope.gridOptions1.data.length);
		if($scope.gridOptions1.data.length <= 0){
			apiserviceUser.deactivateAccount($scope.userId).then(function(data){
				$scope.init();
    			$('#clsUserLead').click();
			});
		}else{
			$.pnotify({
			    title: "Error",
			    type:'success',
			    text: "Please assign all leads",
			});
		}
	};
	
	
	$scope.clouseUserLead = function(){
		$scope.checked = [];
		$scope.usersList = null;
		$scope.assignUser = null;
		$scope.userLeads = {};
		
		apiserviceUser.getAllUsersToAssign().then(function(data){
			$scope.usersList = data;
			angular.forEach($scope.usersList, function(obj, index){
				 if ((obj.userType == "Manager")) {
					 $scope.assignUser = obj.id;
			    };
			  });
		});
	};
	$scope.showOtherFild = 0;
	
	$scope.selectOptionEdit = function(){
		var type = $scope.userData.userType;
		console.log(type);
		if(type == "Front Desk"){
			$scope.showOtherFild = 1;
		}
		if(type == "Sales Person"){
			$scope.showOtherFild = 1;
		}
		
		if(type == "Photographer"){
		
			$scope.showOtherFild = 2;
		}
	}
	
	$scope.selectOption = function(){
		var type = $('#userType').val();
		console.log(type);
		console.log($scope.userData);
		
		if(type == "Front Desk"){
			$scope.showOtherFild = 1;
		}
		if(type == "Sales Person"){
			$scope.showOtherFild = 1;
		}
		
		if(type == "Photographer"){
		
			$scope.showOtherFild = 2;
		}
		
		if(type == "Sales Person"){
			angular.forEach($scope.permissionList, function(obj, index){
				 if ((obj.name == "My Profile") || (obj.name == "Inventory") || (obj.name == "Dashboard")) {
					 $scope.permission.push(obj.name);
					 obj.isSelected = true;
					 angular.forEach(obj.childData, function(obj1, index1){
						 if(obj1.name != "Edit Business Information"){
							 //if(obj1.name != "Inventory"){
								 if(obj1.isSelected == false){
									 obj1.isSelected = true;
									 $scope.permission.push(obj1.name);
								 }
							// }
						 } 
					 });
			    };
			  });
			console.log($scope.permission);
		}else if(type == "Front Desk"){
			angular.forEach($scope.permissionList, function(obj, index){
				 if ((obj.name == "My Profile") || (obj.name == "Dashboard")) {
					 $scope.permission.push(obj.name);
					 obj.isSelected = true;
					 angular.forEach(obj.childData, function(obj1, index1){
						 if(obj1.name != "Edit Business Information"){
							 if(obj1.isSelected == false){
								 obj1.isSelected = true;
								 $scope.permission.push(obj1.name);
							 }
						 } 
					 });
			    };
			  });
		}
		else{
			$scope.permission = [];
			angular.forEach($scope.permissionList, function(obj, index){
				
				if ((obj.name == "My Profile") || (obj.name == "Inventory") || (obj.name == "Dashboard")) {
					$scope.permission.splice(index, 1);
					obj.isSelected = false;
				}
			  });
		}
	}
	$scope.activeUserList ={};
	$scope.init = function() {
		apiserviceUser.getAllUsers().then(function(data){
			$scope.gridOptions.data = data;
			$scope.activeUserList = data
			console.log($scope.activeUserList);
		});
	}
	
	$scope.initlize = function() {
		$location.path('/contractors');
		apiserviceUser.findLocation().then(function(data){
			console.log(data);
			$scope.loca = data;
			$scope.getAllPhotographerData();
		});
	}
	
	$scope.activePhotographerList ={};
	$scope.getAllPhotographerData = function() {
		
		console.log($scope.loca);
		$scope.name = "MavenFurniture";
		console.log($scope.name);
		apiserviceUser.getAllPhotographer($scope.name,$scope.loca).then(function(data){
			$scope.gridOptions2.data = data;
			$scope.activePhotographerList = data
			console.log($scope.activePhotographerList);
		});
	}
	
	$scope.contract = function(type){
		$scope.contactVal= type;
		if(type=="Employee" || type=="One Time Order"){
			$("#number").attr("disabled", true);
			$("#duration").attr("disabled", true);
			$("#number1").attr("disabled", true);
			$("#duration1").attr("disabled", true);
		}else{
			$("#number").attr("disabled", false);
			$("#duration").attr("disabled", false);
			$("#number1").attr("disabled", false);
			$("#duration1").attr("disabled", false);
		}
	}
	
	$scope.createNewUser=function(){
		$scope.user = {};
		$scope.showOtherFild = 0;
		$scope.contactVal = "";
		$("#cnfstartDateValue").val("");
		$scope.permission = [];
		console.log($scope.permissionList);
		angular.forEach($scope.permissionList, function(obj, index){
			 obj.isSelected = false;
			 obj.childData.isSelected = false;
			 
		});
		$scope.img="/assets/images/profile-pic.jpg ";
		
	}
	
	
	if($routeParams.inPage == "external" || $routeParams.inPage == "invite" || $routeParams.inPage == "backToDashBoard"){
		//$scope.user.userType = "Sales Person";
		
		$http.get('/getAllPermissionById')
		.success(function(data) {
			console.log(data);
			$scope.permissionList =[];
			/*angular.forEach(data, function(obj, index){
				var jsonObj = {name:obj.name,isSelected:false,id:obj.id};
				$scope.permissionList.push(jsonObj);
			});*/
			angular.forEach(data, function(obj, index){
				obj.isSelected = false;
				angular.forEach(obj.childData, function(obj1, index1){
					obj1.isSelected = false;
				});
				
			});
			$scope.permissionList = data;
			$('#userType').val("Sales Person");
			$scope.selectOption();
			$('#modal-basic').modal('show');
			$scope.createNewUser();
			console.log($scope.permissionList);
		});
		
	}
	
	$scope.imageBorder = function(flag) {
		$scope.userData.imageName = "null";
		angular.forEach($scope.imagesList, function(obj, index){
			if(obj.name==flag){
				$("#"+flag).removeClass('noClass').addClass('ImageBorder');
				$("#u"+flag).removeClass('noClass').addClass('ImageBorder');
				$scope.img=obj.srcName;
				logofile = undefined;
			}else{
				$("#"+obj.name).removeClass('ImageBorder').addClass('noClass');
				$("#u"+obj.name).removeClass('ImageBorder').addClass('noClass');
			}
		});
		
		
	}	   
	
	$scope.showText = 0;
	$scope.getProvided = function(validText){
		//console.log(validText);
		if(validText == "Yes"){
			$scope.showText = 1;
		}else{
			$scope.showText = 0;
		}
		
	}
	$scope.getTrial = function(validText){
		//console.log(validText);
		if(validText == "Yes"){
			$scope.showText = 1;
		}else{
			$scope.showText = 0;
		}
		
	}
	$scope.inventoryData = [
	{name:'Compelte access to Inventory : Add, Edit, Remove, Statistics',isSelected:false},
	{name:'Update existing Inventorys Information : No Statistics is displayed',isSelected:false},
	{name:'Only View Inventory',isSelected:false}];
	
	$scope.customerReqData = [
	      {name:'User see only automatically/manually assigned online Request'},
	      {name:'User see all submitted online Request'}
	      ];
	
	$scope.crmData = [
	       {name:'Manage Own Contacts data base'},
	       {name:'Access to the Whole Contacts data base'}
	       ];
	
	//console.log($scope.permissionList);
	$scope.permission =[];
	
	$scope.rolesRedioClicked = function(e, rolePer,value,type){
		console.log(rolePer);
		console.log(value);
		console.log(e);
		console.log(type)
		angular.forEach($scope.permission, function(obj, index){
			if(type == "Inventory"){
				angular.forEach($scope.inventoryData, function(obj1, index){
					if(obj == obj1.name){
							$scope.deleteItem(obj1);
					}
				});
			}else if(type == "Customer Requests"){
				angular.forEach($scope.customerReqData, function(obj1, index){
					if(obj == obj1.name){
							$scope.deleteItem(obj1);
					}
				});
			}else if(type == "CRM"){
				angular.forEach($scope.crmData, function(obj1, index){
					if(obj == obj1.name){
							$scope.deleteItem(obj1);
					}
				});
			}
			
		 });
			$scope.permission.push(rolePer.name);
		
		
		angular.forEach(rolePer.childData, function(obj, index){
				
				if(value == false){
					obj.isSelected = true;
				}
				else if(value == true){
					obj.isSelected = false;
				}
				if(obj.isSelected == true){
					$scope.permission.push(obj.name);
				}else{
					$scope.deleteItem(obj);
				}
		});
	
		console.log($scope.permission);
	}
	
	
	$scope.rolesClicked = function(e, rolePer,value){
		console.log(rolePer);
		console.log(value);
		console.log(e);
		angular.forEach(rolePer.childData, function(obj, index){
				
				if(value == false){
					obj.isSelected = true;
				}
				else if(value == true){
					obj.isSelected = false;
				}
				if(obj.isSelected == true){
					if(obj.name != "Manage Own Contacts data base" && obj.name != "Access to the Whole Contacts data base" && obj.name != "User see only automatically/manually assigned online Request" && obj.name != "User see all submitted online Request" && obj.name != "Compelte access to Inventory : Add, Edit, Remove, Statistics" && obj.name !="Update existing Inventorys Information : No Statistics is displayed" && obj.name != "Only View Inventory"){
						$scope.permission.push(obj.name);
					}
					
				}else{
					$scope.deleteItem(obj);
				}
		});
		if(value == false){
			
				$scope.permission.push(rolePer.name);
			
		}else{
			if(rolePer.name == "Customer Requests"){
				if($scope.userData.applyOnlineReqUser == 1){
					$('#modal-online-requ').modal('show');
				}else{
					$scope.deleteItem(rolePer);
				}
				
			}else{
				$scope.deleteItem(rolePer);
			}
			
		}
		console.log($scope.permission);
	}
	$scope.deleteItem = function(rolePer){
		angular.forEach($scope.permission, function(obj, index){
			 if ((rolePer.name == obj)) {
				 $scope.permission.splice(index, 1);
				 /*if ((rolePer.name == obj)) {
					 $scope.permission.splice(index, 1);
			       	return;
			    };*/
		       	return;
		    };
		  });
	}
	
	$scope.continueOnReq = function(){
		
		apiserviceUser.getOnlineReqUserChange().then(function(data){
			console.log("caaaaallllllllllll");
			$('#modal-online-requ').modal('hide');
		});
	}
	
	$scope.pdfDoc = [];
	$scope.selectPdf = function(e,item,value){
		console.log(item);
		console.log(value);
		if(value == false || value == undefined){
			$scope.pdfDoc.push(item.internalPdfId);
		}else{
			$scope.deletepdfItem(item);
		}
		console.log($scope.pdfDoc);
	}
	$scope.deletepdfItem = function(item){
		angular.forEach($scope.pdfDoc, function(obj, index){
			 if ((item.internalPdfId == obj)) {
				 $scope.pdfDoc.splice(index, 1);
		       	return;
		    };
		  });
	}
	
	$scope.closeEditPop = function(){
		$scope.userData = angular.copy($scope.userTemp);
		console.log($scope.userData);
		if($scope.userData.imageName == null || $scope.userData.imageName == "null"){
			$scope.img = $scope.userData.imageUrl;
		}else{
			$scope.img = "http://glider-autos.com/MavenImg/images"+$scope.userData.imageUrl;
		}
		console.log($scope.img);
	}
	
	$scope.editUser = function(row) {
		//$scope.permission = [];
	
		console.log($scope.permission);
		console.log(row.entity);
		$scope.userTemp = angular.copy(row.entity);
		console.log($scope.permissionList);
		$('#editUserModal').click();
		if(row.entity.contractDur=="Employee"){
			$scope.conUser = "Employee";
			$scope.contactVal= row.entity.contractDur;
				$("#number").attr("disabled", true);
				$("#duration").attr("disabled", true);
				$('#employee1').click();
		}else{
			$scope.conUser = "Contractor";
			var durations = row.entity.contractDur;
			var val = durations.split(' ');
			$scope.num1 = parseInt(val[0]);
			$scope.duration1=val[1];
			$('#txt1').click();
		}
		//console.log($scope.num1);
		//console.log($scope.duration1);
		$scope.userData = row.entity;
		if($scope.userData.userType == "Front Desk"){
			$scope.showOtherFild = 1;
		}
		
		if($scope.userData.userType == "Sales Person"){
			$scope.showOtherFild = 1;
		}
		
		if($scope.userData.userType == "Photographer"){
		
			$scope.showOtherFild = 2;
		}
		
		
		$scope.userData.trialPeriod = parseInt($scope.userData.trialPeriod);
		//console.log($scope.userData);
		$scope.permission = [];
		
		angular.forEach($scope.permissionList, function(obj, index){
			angular.forEach($scope.userData.permissions, function(obj1, index1){
				if(obj.name == obj1){
					 obj.isSelected = true;
					 obj.childData.isSelected = true;
					 $scope.permission.push(obj.name);
				 }
			});
		});
		
		angular.forEach($scope.permissionList, function(obj, index){
			angular.forEach(obj.childData, function(obj1, index1){
				angular.forEach(row.entity.permissions, function(obj2, index2){
					if(obj1.name == obj2){
						//obj.isSelected = false;
						obj1.isSelected = true;
						$scope.permission.push(obj1.name);
					}
				});
			});
			
		});
		
		if($scope.userData.imageName == null || $scope.userData.imageName == "null"){
			$scope.img = $scope.userData.imageUrl;
		}else{
			$scope.img = "http://glider-autos.com/MavenImg/images"+$scope.userData.imageUrl;
		}
	}
	
	$scope.editPhotographer = function(row) {
		angular.forEach($scope.permissionList, function(obj, index){
			 obj.isSelected = false;
		});
		console.log(row.entity);
		$scope.userTemp = angular.copy(row.entity);
		console.log($scope.permissionList);
		$('#editUserModal').click();
		if(row.entity.contractDur=="Employee"){
			$scope.conUser = "Employee";
			$scope.contactVal= row.entity.contractDur;
				$("#number").attr("disabled", true);
				$("#duration").attr("disabled", true);
				$('#employee1').click();
		}else{
			$scope.conUser = "Contractor";
			var durations = row.entity.contractDur;
			var val = durations.split(' ');
			$scope.num1 = parseInt(val[0]);
			$scope.duration1=val[1];
			$('#txt1').click();
		}
		//console.log($scope.num1);
		//console.log($scope.duration1);
		$scope.userData = row.entity;
		if($scope.userData.userType == "Front Desk"){
			$scope.showOtherFild = 1;
		}
		
		if($scope.userData.userType == "Sales Person"){
			$scope.showOtherFild = 1;
		}
		
		if($scope.userData.userType == "Photographer"){
		
			$scope.showOtherFild = 2;
		}
		
		
		$scope.userData.trialPeriod = parseInt($scope.userData.trialPeriod);
		//console.log($scope.userData);
		$scope.permission = [];
		
		angular.forEach($scope.permissionList, function(obj, index){
			angular.forEach($scope.userData.permissions, function(obj1, index1){
				if(obj.name == obj1){
					 obj.isSelected = true;
					 $scope.permission.push(obj.name);
				 }
			});
		});
		
		if($scope.userData.imageName == null || $scope.userData.imageName == "null"){
			$scope.img = $scope.userData.imageUrl;
		}else{
			$scope.img = "http://glider-autos.com/glivrTestImg/images"+$scope.userData.imageUrl;
		}
	}
	
	$scope.deleteUser = function(row) {
		//console.log(row.entity);
		//console.log(row.entity.id);
		$scope.userId = row.entity.id;
		apiserviceUser.getAllLeadsByUser($scope.userId).then(function(data){
			$scope.gridOptions1.data = data;
			console.log(data);
			$scope.allLeadUserList = data;
			$scope.allLeadLength = $scope.allLeadUserList.length
			
		});
		if($scope.allLeadLength > 0){
			$('#deleteModal').click();
		}
		else{
			$scope.deactivateAccount();
		}
		   $scope.rowDataVal = row;
	};
	
	$scope.deletePhotographer = function(row) {
		console.log(row.entity.id);
		apiserviceUser.deactivatePhotographerAccount(row.entity.id).then(function(data){
			$scope.initlize();
			
		});
	};
	/*$scope.deleteUserById = function() {
		$http.get('/deleteUserById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			$('#deleteClose').click();
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "User deleted succesfully!",
			});
			$scope.init();
		});
	};*/
	
	 var logofile;
		$scope.onLogoFileSelect = function ($files) {
			logofile = $files;
			for (var i = 0; i < $files.length; i++) {
				var $file = $files[i];
				if (window.FileReader && $file.type.indexOf('image') > -1) {
					var fileReader = new FileReader();
					fileReader.readAsDataURL($files[i]);
					var loadFile = function (fileReader, index) {
						fileReader.onload = function (e) {
							$timeout(function () {
								$scope.img = e.target.result;
								//console.log(e.target.result);
							});
							
						}
					}(fileReader, i);
				}
			}
		}	
	   
		$scope.hOperation = {};
	$scope.saveImage = function() {
		apiserviceUser.findLocation().then(function(data){
			console.log(data);
			$scope.locationId = data;
			$scope.user.locationId = data;
		console.log($scope.permissionList);
		
		if($routeParams.inPage == "external" || $routeParams.inPage == "invite" || $routeParams.inPage == "backToDashBoard"){
			$scope.user.userType = "Sales Person";
			
			angular.forEach($scope.permissionList, function(obj, index){
				 if ((obj.name == "My Profile") || (obj.name == "Inventory") || (obj.name == "Dashboard")) {
					 $scope.permission.push(obj.name);
					 obj.isSelected = true;
					 angular.forEach(obj.childData, function(obj1, index1){
						 if(obj1.name != "Edit Business Information"){
							 if(obj1.isSelected == true){
								 $scope.permission.push(obj1.name);
							 }
						 } 
					 });
			    };
			  });
			
		}
		$scope.user.permissions = $scope.permission;
		
		$scope.user.pdfIds = $scope.pdfDoc;
		
		if($scope.user.premiumFlag == undefined){
			$scope.user.premiumFlag = false;
		}
		if($("#cnfstartDateValue").val() != undefined)
			$scope.user.contractDurStartDate = $("#cnfstartDateValue").val();
		if($("#cnfendDateValue").val() != undefined)
			$scope.user.contractDurEndDate = $("#cnfendDateValue").val();
		
		if($scope.user.userType == "Photographer"){
			$scope.user.hOperation.sunOpenTime = $('#sunOpen').val();
			$scope.user.hOperation.monOpenTime = $('#monOpen').val();
			$scope.user.hOperation.tueOpenTime = $('#tueOpen').val();
			$scope.user.hOperation.wedOpenTime = $('#wedOpen').val();
			$scope.user.hOperation.thuOpenTime = $('#thuOpen').val();
			$scope.user.hOperation.friOpenTime = $('#friOpen').val();
			$scope.user.hOperation.satOpenTime = $('#satOpen').val();
			
			$scope.user.hOperation.sunCloseTime = $('#sunClose').val();
			$scope.user.hOperation.monCloseTime = $('#monClose').val();
			$scope.user.hOperation.tueCloseTime = $('#tueClose').val();
			$scope.user.hOperation.wedCloseTime = $('#wedClose').val();
			$scope.user.hOperation.thuCloseTime = $('#thuClose').val();
			$scope.user.hOperation.friCloseTime = $('#friClose').val();
			$scope.user.hOperation.satCloseTime = $('#satClose').val();


			if($scope.user.hOperation.sunOpen == undefined){
				$scope.user.hOperation.sunOpen = false;
			}
			if($scope.user.hOperation.monOpen == undefined){
				$scope.user.hOperation.monOpen = false;
			}
			if($scope.user.hOperation.tueOpen == undefined){
				$scope.user.hOperation.tueOpen = false;
			}
			if($scope.user.hOperation.wedOpen == undefined){
				$scope.user.hOperation.wedOpen = false;
			}
			if($scope.user.hOperation.thuOpen == undefined){
				$scope.user.hOperation.thuOpen = false;
			}
			if($scope.user.hOperation.friOpen == undefined){
				$scope.user.hOperation.friOpen = false;
			}
			if($scope.user.hOperation.satOpen == undefined){
				$scope.user.hOperation.satOpen = false;
			}
			
				$scope.user.portalName = "MavenFurniture";
			
			if($scope.user.hOperation.sunClose == undefined){
				$scope.user.hOperation.sunClose = false;
			}
			if($scope.user.hOperation.monClose == undefined){
				$scope.user.hOperation.monClose = false;
			}
			if($scope.user.hOperation.tueClose == undefined){
				$scope.user.hOperation.tueClose = false;
			}
			if($scope.user.hOperation.wedClose == undefined){
				$scope.user.hOperation.wedClose = false;
			}
			if($scope.user.hOperation.thuClose == undefined){
				$scope.user.hOperation.thuClose = false;
			}
			if($scope.user.hOperation.friClose == undefined){
				$scope.user.hOperation.friClose = false;
			}
			if($scope.user.hOperation.satClose == undefined){
				$scope.user.hOperation.satClose = false;
			}
		}
		
		if($scope.contactVal=="Employee"){
			$scope.user.contractDur = $scope.contactVal;
		}else if($scope.contactVal=="One Time Order"){
			$scope.user.contractDur = $scope.contactVal;
		}else{
			if($scope.num==null){
				$scope.num=0;
			}
			$scope.user.contractDur = $scope.num+" "+$scope.duration;
		}
		$scope.user.imageUrl = $scope.img;
		if($scope.user.userType == "Photographer"){
			$scope.user.imageUrl = "path";
			if(angular.isUndefined(logofile)) {
				console.log($scope.user);
				
					apiserviceUser.uploadImageFile($scope.user, $scope.user.userType).then(function(data){
						if(data == null){
							$('#btnClose').click();
						}else{
							$scope.user.firstName=" ";
				            $scope.user.lastName=" ";
				            $scope.user.email=" ";
				            $scope.user.phone=" ";
				            $scope.user.userType=" ";
				            $scope.user.img=" ";
				            $scope.user = {};
				            $('#btnClose').click();
						}
			            
					});
					
			} else {
				/*$('#btnClose').click();
				console.log($scope.user);
				$scope.user.imageUrl = "path";
				apiserviceUser.uploadImageFileLoad($scope.user, $scope.user.userType, logofile).then(function(data){
					 console.log('success');
			            $scope.user.firstName=" ";
			            $scope.user.lastName=" ";
			            $scope.user.email=" ";
			            $scope.user.phone=" ";
			            $scope.user.userType=" ";
			            $scope.user.img=" ";
			            $("#file").val('');
			            $('#btnClose').click();
			            $scope.init();
				});*/
				apiserviceUser.uploadImageFile($scope.user, $scope.user.userType).then(function(data){
					console.log("changesssss");
					console.log(data);
					if(data == null){
						$('#btnClose').click();
						if($routeParams.inPage == "external"){
							$location.path('/otherLeads/35');
						}else if($routeParams.inPage == "invite"){
							$location.path('/crm');
						}else if($routeParams.inPage == "backToDashBoard"){
							$location.path('/');
						}
						
					}else{
						apiserviceUser.updateImageFileLoadPhoto(data,$scope.user.userType, logofile).then(function(data){
							console.log("succeee!!!!!!!!");
							if(data ==  null){
								$('#btnClose').click();
							}else{
								 $scope.user.firstName=" ";
						            $scope.user.lastName=" ";
						            $scope.user.email=" ";
						            $scope.user.phone=" ";
						            $scope.user.userType=" ";
						            $scope.user.img=" ";
						            $scope.user = {};
						            $("#file").val('');
						            $('#btnClose').click();
						            $scope.init();
							}
							if($routeParams.inPage == "external"){
								$location.path('/otherLeads/35');
							}else if($routeParams.inPage == "invite"){
								$location.path('/crm');
							}else if($routeParams.inPage == "backToDashBoard"){
								$location.path('/');
							}
						});
					}
		            
				});					
			}
		}else{
			console.log($scope.user.userType);
			if(angular.isUndefined(logofile)) {
					apiserviceUser.uploadImageFile($scope.user, $scope.user.userType).then(function(data){
						$scope.user.firstName=" ";
			            $scope.user.lastName=" ";
			            $scope.user.email=" ";
			            $scope.user.phone=" ";
			            $scope.user.userType=" ";
			            $scope.user.img=" ";
			            $scope.user = {};
			            $('#btnClose').click();
			            if($routeParams.inPage == "external"){
							$location.path('/otherLeads/35');
						}else if($routeParams.inPage == "invite"){
							$location.path('/crm');
						}else if($routeParams.inPage == "backToDashBoard"){
							$location.path('/');
						}
			            $scope.init();
					});
			} else {
				if($scope.user.pdfIds.length <= 0){
					delete $scope.user.pdfIds;
			}
					apiserviceUser.uploadImageFileLoad($scope.user, $scope.user.userType, logofile).then(function(data){
						 console.log('success');
				            $scope.user.firstName=" ";
				            $scope.user.lastName=" ";
				            $scope.user.email=" ";
				            $scope.user.phone=" ";
				            $scope.user.userType=" ";
				            $scope.user.img=" ";
				            $("#file").val('');
				            $('#btnClose').click();
				            if($routeParams.inPage == "external"){
								$location.path('/otherLeads/35');
							}else if($routeParams.inPage == "invite"){
								$location.path('/crm');
							}else if($routeParams.inPage == "backToDashBoard"){
								$location.path('/');
							}
				            $scope.init();
					});
				  
			}
		}
		
		});
		
	   }
	
	$scope.openClick = function(){
		$('#sunOpen').timepicker();
		$('#sunClose').timepicker();
		$('#monOpen').timepicker();
		$('#monClose').timepicker();
		$('#tueOpen').timepicker();
		$('#tueClose').timepicker();
		$('#wedOpen').timepicker();
		$('#wedClose').timepicker();
		$('#thuOpen').timepicker();
		$('#thuClose').timepicker();
		$('#friOpen').timepicker();
		$('#friClose').timepicker();
		$('#satOpen').timepicker();
		$('#satClose').timepicker();
		
	}
	$scope.updateImageOfPhoto = function() {
		console.log($scope.loca);
		$scope.userData.permissions = $scope.permission;
		$scope.userData.pdfIds = $scope.pdfDoc;
		console.log($scope.userData.pdfIds);
		delete $scope.userData.successRate;
		
		if($("#cnfstartDateValue").val() != undefined)
			$scope.userData.contractDurStartDate = $("#cnfstartDateValue").val();
		if($("#cnfendDateValue").val() != undefined)
			$scope.userData.contractDurEndDate = $("#cnfendDateValue").val();
		
		console.log($scope.userData);
		
		if($scope.userData.userType == "Photographer"){
			$scope.userData.hOperation.sunOpenTime = $('#sunOpen').val();
			$scope.userData.hOperation.monOpenTime = $('#monOpen').val();
			$scope.userData.hOperation.tueOpenTime = $('#tueOpen').val();
			$scope.userData.hOperation.wedOpenTime = $('#wedOpen').val();
			$scope.userData.hOperation.thuOpenTime = $('#thuOpen').val();
			$scope.userData.hOperation.friOpenTime = $('#friOpen').val();
			$scope.userData.hOperation.satOpenTime = $('#satOpen').val();
			
			$scope.userData.hOperation.sunCloseTime = $('#sunClose').val();
			$scope.userData.hOperation.monCloseTime = $('#monClose').val();
			$scope.userData.hOperation.tueCloseTime = $('#tueClose').val();
			$scope.userData.hOperation.wedCloseTime = $('#wedClose').val();
			$scope.userData.hOperation.thuCloseTime = $('#thuClose').val();
			$scope.userData.hOperation.friCloseTime = $('#friClose').val();
			$scope.userData.hOperation.satCloseTime = $('#satClose').val();
			
			if($scope.userData.hOperation.sunOpen == undefined){
				$scope.userData.hOperation.sunOpen = false;
			}
			if($scope.userData.hOperation.monOpen == undefined){
				$scope.userData.hOperation.monOpen = false;
			}
			if($scope.userData.hOperation.tueOpen == undefined){
				$scope.userData.hOperation.tueOpen = false;
			}
			if($scope.userData.hOperation.wedOpen == undefined){
				$scope.userData.hOperation.wedOpen = false;
			}
			if($scope.userData.hOperation.thuOpen == undefined){
				$scope.userData.hOperation.thuOpen = false;
			}
			if($scope.userData.hOperation.friOpen == undefined){
				$scope.userData.hOperation.friOpen = false;
			}
			if($scope.userData.hOperation.satOpen == undefined){
				$scope.userData.hOperation.satOpen = false;
			}
			
			
			if($scope.userData.hOperation.sunClose == undefined){
				$scope.userData.hOperation.sunClose = false;
			}
			if($scope.userData.hOperation.monClose == undefined){
				$scope.userData.hOperation.monClose = false;
			}
			if($scope.userData.hOperation.tueClose == undefined){
				$scope.userData.hOperation.tueClose = false;
			}
			if($scope.userData.hOperation.wedClose == undefined){
				$scope.userData.hOperation.wedClose = false;
			}
			if($scope.userData.hOperation.thuClose == undefined){
				$scope.userData.hOperation.thuClose = false;
			}
			if($scope.userData.hOperation.friClose == undefined){
				$scope.userData.hOperation.friClose = false;
			}
			if($scope.userData.hOperation.satClose == undefined){
				$scope.userData.hOperation.satClose = false;
			}
		}
		console.log($scope.userData);
		if($scope.contactVal=="Employee"){
			$scope.userData.contractDur = $scope.contactVal;
		}else if($scope.contactVal=="One Time Order"){
			$scope.userData.contractDur = $scope.contactVal;
		}else{
			if($scope.num1==null){
				$scope.num1=0;
			}
			$scope.userData.contractDur = $scope.num1+" "+$scope.duration1;
		}
		
		//console.log($scope.userData);
		$scope.userData.locationId = 0;
		$scope.userData.loca = $scope.loca;
		$scope.userData.name = "MavenFurniture";
		if(angular.isUndefined(logofile)) {
			$scope.userData.imageUrl = $scope.img;
			//console.log("no logofile");
			if($scope.emailMsg == "") {
				apiserviceUser.updateImagePhotographer($scope.userData).then(function(data){
					$('#btnEditClose').click();
		            $scope.init();
				});
					
				}
			} else {
				if($scope.emailMsg == "") {
					$scope.userData1 = {};
					$scope.userData1.id = $scope.userData.id;
					apiserviceUser.updateImagePhotographer($scope.userData).then(function(data){
						if(data == null){
							$('#btnClose').click();
						}else{
							apiserviceUser.updateImageFileLoadPhotoPhotographer(data, $scope.userData.userType, logofile).then(function(data){
								$('#btnEditClose').click();
					            $scope.initlize();
							});
						}
					});
					
				}
			}
	   }
	
	
	$scope.updateImage = function() {
		$scope.userData.permissions = $scope.permission;
		$scope.userData.pdfIds = $scope.pdfDoc;
		delete $scope.userData.successRate;
		
		if($("#cnfstartDateValue").val() != undefined)
			$scope.userData.contractDurStartDate = $("#cnfstartDateValue").val();
		if($("#cnfendDateValue").val() != undefined)
			$scope.userData.contractDurEndDate = $("#cnfendDateValue").val();
		
		console.log($scope.userData);
		
		if($scope.userData.userType == "Photographer"){
			$scope.userData.hOperation.sunOpenTime = $('#sunOpen').val();
			$scope.userData.hOperation.monOpenTime = $('#monOpen').val();
			$scope.userData.hOperation.tueOpenTime = $('#tueOpen').val();
			$scope.userData.hOperation.wedOpenTime = $('#wedOpen').val();
			$scope.userData.hOperation.thuOpenTime = $('#thuOpen').val();
			$scope.userData.hOperation.friOpenTime = $('#friOpen').val();
			$scope.userData.hOperation.satOpenTime = $('#satOpen').val();
			
			if($scope.userData.hOperation.sunOpen == undefined){
				$scope.userData.hOperation.sunOpen = false;
			}
			if($scope.userData.hOperation.monOpen == undefined){
				$scope.userData.hOperation.monOpen = false;
			}
			if($scope.userData.hOperation.tueOpen == undefined){
				$scope.userData.hOperation.tueOpen = false;
			}
			if($scope.userData.hOperation.wedOpen == undefined){
				$scope.userData.hOperation.wedOpen = false;
			}
			if($scope.userData.hOperation.thuOpen == undefined){
				$scope.userData.hOperation.thuOpen = false;
			}
			if($scope.userData.hOperation.friOpen == undefined){
				$scope.userData.hOperation.friOpen = false;
			}
			if($scope.userData.hOperation.satOpen == undefined){
				$scope.userData.hOperation.satOpen = false;
			}
		}
		
		if($scope.contactVal=="Employee"){
			$scope.userData.contractDur = $scope.contactVal;
		}else if($scope.contactVal=="One Time Order"){
			$scope.userData.contractDur = $scope.contactVal;
		}else{
			if($scope.num1==null){
				$scope.num1=0;
			}
			$scope.userData.contractDur = $scope.num1+" "+$scope.duration1;
		}
		
		//console.log($scope.userData);
		$scope.userData.locationId = 0;
		if(angular.isUndefined(logofile)) {
			$scope.userData.imageUrl = $scope.img;
			//console.log("no logofile");
			if($scope.emailMsg == "") {
				apiserviceUser.updateImageFile($scope.userData).then(function(data){
					$('#btnEditClose').click();
		            $scope.init();
				});
					/*$http.post('/updateImageFile',$scope.userData)
					.success(function(data) {
						
					});*/
				}
			} else {
				if($scope.emailMsg == "") {
					$scope.userData1 = {};
					$scope.userData1.id = $scope.userData.id;
					apiserviceUser.updateImageFile($scope.userData).then(function(data){
						apiserviceUser.updateImageFileLoad($scope.userData1, logofile,$scope.userData1.userType).then(function(data){
							$('#btnEditClose').click();
				            $scope.init();
						});
					});
					
					/*$upload.upload({
			            url : '/updateImageFile',
			            method: 'post',
			            file:logofile,
			            data:$scope.userData
			        }).success(function(data, status, headers, config) {
			            //console.log('success');
			            $('#btnEditClose').click();
			           
			            $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "User saved successfully",
						});
			            $scope.init();
			        });*/
				}
			}
	   }
	$scope.emailMsg = "";
	$scope.checkEmail = function(type) {
		console.log($('#userEmail').val());
		if(type == 'create') {
			$scope.email = $('#userEmail').val()
		}
		if(type == 'edit') {
			$scope.email = $('#userEditEmail').val()
		}
		
		apiserviceUser.checkEmailOfUser($scope.email).then(function(data){
			$scope.emailMsg = data;
		});
		/*$http.get('/checkEmailOfUser/'+$scope.email)
		.success(function(data) {
			$scope.emailMsg = data;
		});*/
	}
	
}]);	

angular.module('newApp')
.controller('DeactivateUserCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiserviceUser', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiserviceUser) {
	//console.log("in DeactivateUserCtrl");
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
	 		                                 { name: 'fullName', displayName: 'Name', width:'25%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'email', displayName: 'Email', width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'phone', displayName: 'Phone', width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'userType', displayName: 'Role', width:'20%',cellEditableCondition: false,
	 		                                	
	 		                                 },
	 		                                 { name: 'edit', displayName: '', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
       		                                 cellTemplate:'<button type="button" ng-click="grid.appScope.activateAccount(row.entity)" style="margin-left:5px;" class="btn btn-sm btn-primary">Active</button>', 
   		                                 
   		                                 },
	     		                                 ];
	 		 
	 		$scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.deactiveUserList,{'fullName':grid.columns[0].filters[0].term,'email':grid.columns[1].filters[0].term,'phone':grid.columns[2].filters[0].term,'userType':grid.columns[3].filters[0].term,},undefined);
			        });
		   		
	   		};

	   		$scope.gridOptions1 = {
	   	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	   	 		    paginationPageSize: 150,
	   	 		    enableFiltering: true,
	   	 		    useExternalFiltering: true,
	   	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	   	 		 };
	   	 		 $scope.gridOptions1.enableHorizontalScrollbar = 0;
	   	 		 $scope.gridOptions1.enableVerticalScrollbar = 2;
	   	 		 $scope.gridOptions1.columnDefs = [
	   	 		                                 { name: 'fullName', displayName: 'Name', width:'25%',cellEditableCondition: false,
	   	 		                                	
	   	 		                                 },
	   	 		                                 { name: 'email', displayName: 'Email', width:'20%',cellEditableCondition: false,
	   	 		                                	
	   	 		                                 },
	   	 		                                 { name: 'phone', displayName: 'Phone', width:'20%',cellEditableCondition: false,
	   	 		                                	
	   	 		                                 },
	   	 		                                 { name: 'userType', displayName: 'Role', width:'20%',cellEditableCondition: false,
	   	 		                                	
	   	 		                                 },
	   	 		                                 { name: 'edit', displayName: '', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	          		                                 cellTemplate:'<button type="button" ng-click="grid.appScope.activatePhotographerAccount(row.entity)" style="margin-left:5px;" class="btn btn-sm btn-primary">Active</button>', 
	      		                                 
	      		                                 },
	   	     		                                 ];
	   	 		 
	   	 		$scope.gridOptions1.onRegisterApi = function(gridApi){
	   				 $scope.gridApi = gridApi;
	   				 
	   		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
	   			          var grid = this.grid;
	   			        	  $scope.gridOptions1.data = $filter('filter')($scope.deactivePhotographerList,{'fullName':grid.columns[0].filters[0].term,'email':grid.columns[1].filters[0].term,'phone':grid.columns[2].filters[0].term,'userType':grid.columns[3].filters[0].term,},undefined);
	   			        });
	   		   		
	   	   		};

	   		
	 		$scope.init = function() {
	 			apiserviceUser.getAllDeactivateUsers().then(function(data){
	 				$scope.gridOptions.data = data;	
	 				$scope.deactiveUserList = data;
	 			});
	 		};
	 		
	 		$scope.initlize = function() {
	 			$location.path('/deactivePhotographer');
	 			apiserviceUser.findLocation().then(function(data){
	 				console.log(data);
	 				$scope.loca = data;
	 				$scope.getAllDeactivatePhotographerData();
	 			});
	 		}
	 		
	 		$scope.getAllDeactivatePhotographerData = function() {
	 			console.log($scope.loca);
	 			$scope.name = "MavenFurniture";
	 			console.log($scope.name);
	 			apiserviceUser.getAllDeactivatePhotographer($scope.name,$scope.loca).then(function(data){
	 				$scope.gridOptions1.data = data;	
	 				console.log(data);
	 				$scope.deactivePhotographerList = data;
	 			});
	 		};
	 		
	 		
	 		$scope.activateAccount = function(item){
	 			$scope.userId = item.id;
	 			$('#activateModal').click();
	 		};
	 		$scope.activatePhotographerAccount = function(item){
	 			$scope.userId = item.id;
	 			$('#activateModal').click();
	 		};
	 		
	 		$scope.activePhotoAccount = function(){
	 			apiserviceUser.activePhotographerAccount($scope.userId).then(function(data){
	 				$scope.initlize();
	 			});
	 		};
	 		$scope.activeUserAccount = function(){
	 			apiserviceUser.activeAccount($scope.userId).then(function(data){
	 				$scope.init();
	 			});
	 		};
	 		
	 		
	 		$scope.gotoProfile = function() {
	 			$location.path('/myprofile');
	 		};
	 		$scope.goToLoaction = function() {
	 			$location.path('/createLocation');
	 		};
	 		
	 		$scope.goToUsers = function() {
	 			$location.path('/createUser/normal');
	 		};
	 		
	 		$scope.goToContractors = function() {
	 			$location.path('/contractors');
	 		};
	 		
	 		$scope.goToDeactive = function() {
	 			$location.path('/deactiveUsers');
	 		};
	 		$scope.goToDeactivePhotographer = function() {
	 			$location.path('/deactivePhotographer');
	 		};
}]);