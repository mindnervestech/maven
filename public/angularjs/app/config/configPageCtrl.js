angular.module('newApp')
.controller('ConfigPageCtrl', ['$scope','$http','$location','$filter','$upload','$routeParams','apiserviceConfigPage','$window', function ($scope,$http,$location,$filter,$upload,$routeParams,apiserviceConfigPage,$window) {
	$scope.premium = {};
	
	$scope.realese = {};
	$scope.realese.allSalesPeople = "Released to all of the sales people";
	
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		enableRowSelection: true,
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 $scope.gridOptions.enableVerticalScrollbar = 2;
	 
	$scope.formInit = function(){
		$scope.systemInfo();
	}
	
	$scope.init = function() {
		apiserviceConfigPage.getImageConfig().then(function(data){
		
			$scope.cover=data.coverData;
			$scope.vehSize=data.vehicleImageConfig;
			$scope.slider = data.slider;
			$scope.featured = data.featured;
			$scope.newsletterDay = data.NewsletterDate;
			$scope.newsletterId = data.NewsletterId;
			$scope.newsletterTime = data.newsletterTime;
			$scope.newsletterTimeZone = data.NewsletterTimeZone;
			$scope.domain = data.domain;
			//$scope.showLoginPasswordText($scope.domain.hostingProvider);
		
			$scope.premium.priceVehical = parseInt(data.premiumLeads.premium_amount);
			if(data.premiumLeads.premium_flag == 1){
				$scope.premium.premiumFlag = true;
			}else{
				$scope.premium.premiumFlag = false;
			}
		});
		$scope.showFlag = 0;
		
		apiserviceConfigPage.getAllSalesUsers().then(function(data){
			$scope.salesPersonList =data;
			angular.forEach($scope.salesPersonList, function(obj, index){
				obj.showDataValue = false;
			});
			console.log($scope.salesPersonList);
		});
		
			/*$scope.showToUser = function(){
		$scope.showTable = 1;
		console.log(locationId);
		$http.get('/getAllSalesUsers').success(function(data){
			$scope.salesPersonList =data;
		
			$scope.user=data;
			if($scope.salesPersonList.length > 0){
				$scope.getAllSalesPersonRecord($scope.salesPersonList[0].id);
			}
		});
	}*/
	
		
		apiserviceConfigPage.getSocialMediadetail().then(function(data){
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.media=data;
				//$scope.customerPdfList=data;
				
			});	
		apiserviceConfigPage.getCustomerPdfData().then(function(data){
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.customerPdfList=data;
				
			});
			
		apiserviceConfigPage.getEmailDetails().then(function(data){
				$scope.email=data;
				
			});	
		apiserviceConfigPage.getInternalPdfData().then(function(data){
			
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.internalPdfList=data;
			});	
		apiserviceConfigPage.getAllSites().then(function(data){
				$scope.siteList = data;
	 			
	 		});
		
	}
	
	
	
	$scope.flagForChart1 = true;
	$scope.systemInfo = function(){
		console.log("sdfghjkp0000");
		apiserviceConfigPage.getsystemInfo().then(function(data){
		
			console.log("systemInfo");
			console.log(data);
			$scope.leadtypeObjList = data;
 			$scope.gridOptions.data = data;
		});
		
		 $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
			 $scope.rowData = rowEntity;
			 $scope.$apply();
				
				 console.log($scope.rowData);
					 apiserviceConfigPage.updateProductName($scope.rowData).then(function(data){
						 $.pnotify({
							    title: "Success",
							    type:'success',
							    text: "Update successfully",
							});
					 });
				 
			 });
			 
					 
			 };	
		
		
		$scope.gridOptions.columnDefs = [
		                                 { name: 'name', displayName: 'Name', width:'70%',
		                                	 cellTemplate:'<div><label  style="color:#319DB5;cursor:pointer;" ng-click="grid.appScope.ShowCreateNewForm1(row)">{{row.entity.name}}</label></div>',
		                                 },
		                                
		                                 
		                                 { name: 'edit', displayName: ' ', width:'30%',
    		                                 cellTemplate:'<i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.ShowCreateNewForm(row)" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" ng-if="row.entity.name != \'Create New Lead\'" ng-click="grid.appScope.deleteCreateNewFormpopup(row)" title="Delete"></i> ', 
    		                                 /*ng-if="(row.entity.leadName != "Request More Info" || row.entity.leadName != "Schedule Test" || row.entity.leadName != "Trade In")"*/
		                                 },
		                                    ];
	}
	$scope.flagForChart1 = true;
	$scope.leadTypeAll = function(){
		
		console.log("sdfghjkp0000");
		apiserviceConfigPage.getLeadTypeData().then(function(data){
		
			console.log("lead type data");
			console.log(data);
			$scope.leadtypeObjList = data;
 			$scope.gridOptions.data = data;
 			angular.forEach($scope.gridOptions.data, function(obj, index){
				if(obj.checkValue == true){
					obj.checkValue = true;
				}else if(obj.checkValue == false){
					obj.checkValue = false;
				}
			});
 			console.log("gghgfhghghg");
 			console.log($scope.gridOptions.data);
		});
		
	
		
		
		$scope.gridOptions.columnDefs = [
		                                 { name: 'id', displayName: 'Id', width:'15%',cellEditableCondition: false
		                                 },
		                                 { name: 'leadName', displayName: 'Lead Type', width:'50%',cellEditableCondition: false
		                                 },
		                                 {name:'org', displayName:'Show on Website', width:'15%',
		                                	 cellTemplate:'<div class="link-domain" ><input type="checkbox" ng-model="checkValue" ng-disabled="row.entity.leadName == \'Trade-In Appraisal\' || row.entity.leadName == \'Request More Info\'"  ng-checked="row.entity.checkValue" ng-click="grid.appScope.selectCheck(row)">  </div>',
		                                 },
		                                 { name: 'edit', displayName: ' ', width:'20%',
    		                                 cellTemplate:'<i class="glyphicon glyphicon-pencil" ng-if="row.entity.leadName != \'Request More Info\' && row.entity.leadName != \'Schedule Test Drive\' && row.entity.leadName != \'Trade-In Appraisal\'" ng-click="grid.appScope.EditUser(row)"  title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" ng-if="row.entity.leadName != \'Request More Info\' && row.entity.leadName != \'Schedule Test Drive\' && row.entity.leadName != \'Trade-In Appraisal\'" ng-click="grid.appScope.DeleteUserPopup(row)"  title="Edit"></i>', 
    		                                 /*ng-if="(row.entity.leadName != "Request More Info" || row.entity.leadName != "Schedule Test" || row.entity.leadName != "Trade In")"*/
		                                 },
		                                    ];
	}
/*<i class="fa fa-trash" ng-if="row.entity.leadName != \'Request More Info\' && row.entity.leadName != \'Schedule Test Drive\' && row.entity.leadName != \'Trade-In Appraisal\'" ng-click="grid.appScope.removeUser(row)"  title="Delete"></i>*/
	$scope.allFormName = function(){
		apiserviceConfigPage.allFormName().then(function(data){
		
			$scope.gridOptions.data=data;
			console.log(data);
		})
	}	
	
	$scope.setActionAndShow = function(value,allData){
		console.log(value);
		console.log(allData);
		$scope.entityId=allData.id;
		var intValue = 0;
		if(value == undefined){
			intValue = 1;
		}
		if(value == true){
			intValue = 0;
		}else if(value == false){
			intValue = 1;
		}
		apiserviceConfigPage.getCheckButton($scope.entityId, intValue).then(function(data){
			$scope.leadTypeAll();
		})
	}
	
	
	$scope.selectCheckbox = function(row){
		console.log(row);
		$scope.entityId=row.entity.id;
		console.log($scope.entityId);
		console.log(row);
		console.log(row.entity.checkValue);
		var intValue = 0;
		if(row.entity.checkValue == undefined){
			intValue = 1;
		}
		if(row.entity.checkValue == true){
			intValue = 0;
		}else if(row.entity.checkValue == false){
			intValue = 1;
		}
		apiserviceConfigPage.getCheckButton($scope.entityId, intValue).then(function(data){
			$scope.leadTypeAll();
		})
		
	}
	
	$scope.deleteCreateNewFormpopup = function(row){
		console.log(row.entity.id);
		$scope.deleteCreateNewFormId = row.entity.id;
		$('#deleteFromModal').modal('show');
	}
	$scope.deleteCreateNewForm = function(){
		console.log($scope.deleteCreateNewFormId);
		apiserviceConfigPage.deleteCreateNewForm($scope.deleteCreateNewFormId).then(function(data){
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Delete successfully",
			});
			$scope.allFormName();
		})
	}
	
	$scope.ShowCreateNewForm = function(row){
		$window.location.reload();
		localStorage.setItem('popupType','Form');
		console.log(row);
		if(row.entity.name == "Create Lead"){
			$location.path('/CreateLeadForm/'+"Edit"+"/"+'Create Lead');
		}else
		if(row.entity.name == "Add Product"){

			$location.path('/InventoryForm/'+"Edit"+"/"+'Inventory');
			
		}
		else if(row.entity.name == "Create New Lead"){

			$location.path('/CreateLeadForm/'+"Edit"+"/"+'Create New Lead');
			
		}
		else if(row.entity.name == "Add to CRM"){

			$location.path('/CRMForm/'+"Edit"+"/"+'CRM');
			
		}else if(row.entity.name == "Request More Info"){
			$location.path('/RequestMoreInfoForm/'+"Edit"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Contact Us"){
			$location.path('/ContactUsForm/'+"Edit"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Request Appointment"){
			$location.path('/RequestAppointmentForm/'+"Edit"+"/"+row.entity.name);
		}
		else{
			$location.path('/otherForm/'+"Edit"+"/"+row.entity.name);
		}
		
	}

	$scope.ShowCreateNewForm1 = function(row){
		//$location.path('/leadCreateForm/'+"Preview");
		localStorage.setItem('popupType','Form');
		console.log($scope.entityname);

		if(row.entity.name == "Create Lead"){
			$location.path('/'+'CreateLeadForm/'+"Preview"+"/"+'Create Lead');
			}else if(row.entity.name == "Add Product"){

			$location.path('/'+'InventoryForm/'+"Preview"+"/"+'Inventory');
			
		}
			else if(row.entity.name == "Create New Lead"){
				
				$location.path('/'+'CreateLeadForm/'+"Preview"+"/"+'Create New Lead');
				
			}
		else if(row.entity.name == "Add to CRM"){

			$location.path('/'+'CRMForm/'+"Preview"+"/"+'CRM');
			
		}else if(row.entity.name == "Request More Info"){
			$location.path('/RequestMoreInfoForm/'+"Preview"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Contact Us"){
			$location.path('/ContactUsForm/'+"Preview"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Request Appointment"){
			$location.path('/RequestAppointmentForm/'+"Preview"+"/"+row.entity.name);
		}
		else{
			$location.path('/otherForm/'+"Preview"+"/"+row.entity.name);
		}
	}
	
	
	$scope.allLeaddata = function(){
		apiserviceConfigPage.getAllLeadData().then(function(data){
		
			$scope.gridOptions.data=data;
			console.log(data);
		})
	}

		$scope.openAddNew = function(){
			console.log("Checkkkk");
			$scope.leadcreate={"leadName":""};
			//$('#createLeadPopup').click();
			$('#completedPopup').modal('show');
		}
		$scope.leadcreateData = {};
		$scope.saveCompleted = function(){
			console.log($scope.leadcreate.callToAction);
			if($scope.leadcreate.callToAction == undefined){
				$scope.leadcreate.callToAction = false;
			}
			$scope.leadcreateData.leadName = $scope.leadcreate.leadName;
			$scope.leadcreateData.callToAction = $scope.leadcreate.callToAction;
			localStorage.setItem('callToAction',$scope.leadcreate.callToAction);
		console.log($scope.leadcreate);
		apiserviceConfigPage.addnewrUser($scope.leadcreate).then(function(data){
			
			$scope.form = data;
			console.log(data);
			localStorage.setItem('leadId', data.id);
			$scope.ShowFormBuilderSave($scope.leadcreateData);
			$("#completedPopup").modal('hide');
			 $scope.leadTypeAll();
			 
			});
		}
		$scope.ShowFormBuilderSave = function(row){
			$window.location.reload();
			$location.path('/otherForm/'+"save"+"/"+row.leadName);
		}
		$scope.ShowFormBuilder = function(row){
			localStorage.setItem('popupType','Lead');
			console.log(row);
			console.log(row.leadName);
			$window.location.reload();
			if(row.leadName == "Create Lead"){
				$location.path('/CreateLeadForm/'+"Edit"+"/"+'Create Lead');
			}else
			if(row.leadName == "Add Product"){

				$location.path('/InventoryForm/'+"Edit"+"/"+'Inventory');
				
			}
			else if(row.leadName == "Create New Lead"){

				$location.path('/CreateLeadForm/'+"Edit"+"/"+'Create New Lead');
				
			}
			else if(row.leadName == "Add to CRM"){

				$location.path('/CRMForm/'+"Edit"+"/"+'CRM');
				
			}else if(row.leadName == "Request More Info"){
				$location.path('/RequestMoreInfoForm/'+"Edit"+"/"+row.entity.name);
			}
			else if(row.leadName == "Contact Us"){
				$location.path('/ContactUsForm/'+"Edit"+"/"+row.entity.name);
			}
			else if(row.leadName == "Request Appointment"){
				$location.path('/RequestAppointmentForm/'+"Edit"+"/"+row.entity.name);
			}
			else{
				$location.path('/otherForm/'+"Edit"+"/"+row.leadName);
			}
			
		}
		
		 $scope.removeUser = function(row){
			 console.log("show popu");
			 $scope.entityId=row.entity.id;
			 $('#leadtypebutton').click();
			// $('#leadtypebutton').modal('show');
			// $scope.deletelead = row.entity;
			 
		 }
		
		 	$scope.deletelead = function(){
			 console.log("in deletend functio");
			 apiserviceConfigPage.getdeletelead($scope.entityId).then(function(data){
			 
					 console.log("out deletend functio");
					 $scope.leadTypeAll();
			 });
		 }
		 
		 $scope.selectCheck = function(row){
			 
			 console.log(row.entity.checkValue)
			if(row.entity.checkValue == false){
				$('#editPopupcheck').click();
				 $scope.editleadtype.id = row.entity.id;
				 $scope.editleadtype.profile = row.entity.profile;
				 console.log($scope.editleadtype.profile);
				 $scope.selectCheckbox(row);
			//	 $scope.leadTypeAll();
			}else{
				$scope.editleadtype = {};
				$scope.editleadtype.id = row.entity.id;
				 $scope.editleadtype.profile = "0";
				$scope.UpdatecheckboxDiect();
				$scope.selectCheckbox(row);
				//$scope.leadTypeAll();
			}
			 
		 }
			
		 $scope.editleadtype={};
		 
		 $scope.UpdatecheckboxDiect = function(){
			 console.log($scope.editleadtype);
			 console.log("out of funtion");
			 apiserviceConfigPage.Updatecheckbox($scope.editleadtype).then(function(data){
			 
				 console.log(data);
				
         	});
		}
		 
		 var date = new Date().getTime();
		 		apiserviceConfigPage.getAllManufacturers("publish",date).then(function(data){
					$scope.manufacturerslist = data; 
					console.log($scope.manufacturerslist);
				});
			
     	//});
		 	
		 
		 
		 $scope.Updatecheckbox = function(){
			 console.log($scope.editleadtype);
			 $scope.editleadtype.maunfacturersIds = $scope.selectmanu.toString();
			 console.log("out of funtion");
			 apiserviceConfigPage.Updatecheckbox($scope.editleadtype).then(function(data){
			 
				 console.log(data);
				 console.log("in of funtion");
				 $("#editPopupscheck").modal('hide');
         	});
		}
		 
		 $scope.addNewForm = function(){
				console.log("Checkkkk");
				$scope.addform={"name":""};
				
				$('#completedPopup').modal('show');
			}
		 
		
		 
			$scope.savedNewForm = function(){
				apiserviceConfigPage.addnewForm($scope.addform).then(function(data){
				$scope.form = data;
				$("#completedPopup").modal('hide');
				 $scope.allFormName();
				 
				});
			}
			
			$scope.flagForChart1 = true;
			$scope.website = {};
			$scope.savedNewFormWebsite = function(){
				console.log($scope.website);
				apiserviceConfigPage.addnewWebSiteForm($scope.website).then(function(data){
					$scope.form = data;
					console.log(data);
					$("#completedPopup").modal('hide');
					 $scope.showEditData();
					 $scope.webSiteinfo();
					 
					});
				
				
				}
			
			
			$scope.selectmanu = [];
			  $scope.ManuClicked = function(e, rolePer,value){
					console.log(rolePer);
					console.log(value);
					if(value == false || value == undefined){
						$scope.selectmanu.push(rolePer.id);
					}else{
						console.log("sssss");
						$scope.deleteItems(rolePer);
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
					  if(selectP == "All Manufaturers"){
						  angular.forEach($scope.manufacturerslist, function(obj, index){
							  $scope.selectmanu.push(obj.id);
						 });
					  }
					  console.log($scope.selectmanu);
					  
				}
			
			$scope.updateNewFormWebsite = function(){
				console.log($scope.website);
				console.log($scope.rowDataVal);
				$scope.website.id = $scope.rowDataVal.id;
				apiserviceConfigPage.updatenewWebSiteForm($scope.website).then(function(data){
				
					$scope.form = data;
					console.log(data);
					$('#outcome').click();
					// $("#outcome").modal('hide');
					$scope.webSiteinfo();
					});
				 	
				
				}
			$scope.webSiteinfo = function(){
				apiserviceConfigPage.getFormWebSiteData().then(function(data){
					console.log(data);
					$scope.gridOptions.data = data;
		 			console.log($scope.gridOptions.data);
		 			console.log("grid data")
				});
				$scope.gridOptions.columnDefs = [
				                                 { name: 'id', displayName: 'Id', width:'10%',cellEditableCondition: false
				                                 },
				                                 { name: 'title', displayName: 'Title', width:'20%',cellEditableCondition: false
				                                 },
				                                 {name:'form_type', displayName:'Form Type', width:'15%'},
				                                 { name: 'lead_name', displayName: 'Lead Name ', width:'15%' },
				                                 { name: 'outcome', displayName: 'Outcome ', width:'15%' },
				                                 {name:'or', displayName:'', width:'15%',
				                                	 /*cellTemplate:'<div><div class="link-domain"ng-click="grid.appScope.outcome(row)">Outcome &nbsp;&nbsp;&nbsp </div><i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.updateAllFormWebsite(row)"  title="Edit"></i></div>',*/
				                                	 cellTemplate:'<i class="link-domain" ng-click="grid.appScope.outcome(row)">Outcome</i> &nbsp;&nbsp;&nbsp<i class="glyphicon glyphicon-pencil" ng-if="row.entity.lead_name != \'Request More Info\' && row.entity.lead_name != \'Schedule Test Drive\' && row.entity.lead_name != \'Trade-In Appraisal\'" ng-click="grid.appScope.updateAllFormWebsite(row)"  title="Edit"></i> ',
				                                 }, 
				                                 {name:'10', displayName:'LeadLink', width:'15%',
				                                	 /*cellTemplate:'<div><div class="link-domain"ng-click="grid.appScope.outcome(row)">Outcome &nbsp;&nbsp;&nbsp </div><i class="glyphicon glyphicon-pencil" ng-click="grid.appScope.updateAllFormWebsite(row)"  title="Edit"></i></div>',*/
				                                	 cellTemplate:'<div class="link-domain"><i class="glyphicon glyphicon-edit" ng-click="grid.appScope.allLeadRender(row)"  title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" ng-click="grid.appScope.deleteallLeadRenderpopup(row)"  title="Edit"></i></div>',
				                                 }, 
				                                 
				                                 ];
			}
			
			
			 $scope.addNewFormWebSite = function(){
					console.log("add form website");
					$scope.website={"title":""};
					$('#completedPopup').modal('show');
					$scope.leadTypeAllData();
					
				}
			 $scope.outcome = function(row){
					console.log(row.entity);
					$scope.website.outcome = row.entity.outcome;
					$('#outcome').click();
					 

					$scope.rowDataVal = row.entity;
				};
				
				
				$scope.deleteallLeadRenderpopup = function(row){
					console.log(row.entity);
					$scope.deleteallLeadRenderId = row.entity.id;
					$('#deleteFromSitModal').modal('show');
				}
				$scope.deleteallLeadRender = function(){
					console.log($scope.deleteallLeadRenderId);
					apiserviceConfigPage.deleteFormWebsite($scope.deleteallLeadRenderId).then(function(data){
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Delete successfully",
						});
						$scope.showEditData();
						 $scope.webSiteinfo();
		         		
		    		});
				}
				 $scope.allLeadRender = function(row){
						console.log(row);
						if(row.entity.lead_name != ""){
						 if(row.entity.lead_name == "Request More Info"){
							$location.path('/RequestMoreInfoForm/'+"Edit"+"/"+row.entity.lead_name);
						}
						else if(row.entity.lead_name == "Trade-In Appraisal"){
							$location.path('/TradeIn/'+"Edit"+"/"+row.entity.lead_name);
						}
						else if(row.entity.lead_name == "Schedule Test Drive"){
							$location.path('/RequestAppointmentForm/'+"Edit"+"/"+row.entity.lead_name);
						}
						else{
							$location.path('/otherForm/'+"Edit"+"/"+row.entity.lead_name);
						}
						}
						
					}
				 
			 
				$scope.editData = {};
				 $scope.updateAllFormWebsite = function(row){
					 $('#editPopupwebsite').click();
					 console.log(row.entity)
					 $scope.editData = row.entity;
					 $scope.leadTypeAllData();
					 $scope.showEditData();
				 }
				 
				/* $scope.viewRegiInit = function(){
					 $scope.pendingUser();
				 }*/
				 
				 $scope.editFormWebsite = function(website){
					 console.log($scope.editData);
					 $scope.website.id = $scope.editData.id;
					 apiserviceConfigPage.getEditFormWebsite($scope.website).then(function(data){
		         		$("#editPopupswebsite").modal('hide');
		         		$scope.webSiteinfo();
		    		});
					 
				 }
				
				 $scope.showEditData = function(){
					 $scope.website.id = $scope.editData.id;
					 console.log($scope.website.id);
					 console.log("ddddddddd");
					 apiserviceConfigPage.showEditData($scope.website.id).then(function(data){
						
							console.log(data);
							$scope.website = data;
						})
					}	
				 
			 $scope.leadTypeAllData = function(){
				 apiserviceConfigPage.getLeadTypeData().then(function(data){
					
						console.log("lead type data");
						console.log(data);
						$scope.leadtypeObjList = data;
			 		});
				}
			 
			 $scope.showOtherFild = 0;
			 $scope.selectOption = function(type){
					//var type = $('#form_type').val();
					console.log(type);
					
					if(type == "Contact Form"){
						$scope.showOtherFild = 1;
					}
					if(type == "Call to Action" || type == "Survey"){
						$scope.showOtherFild = 0;
					}
				}
				
			 
				
		 $scope.EditUser = function(row){
			
			 $('#editPopup').click();
			 console.log(row);
			 console.log(row.entity)
			 $scope.leadTypeData = row.entity; 
			 $scope.editleadtype.id = row.entity.id;
			 
		 }
		 
		 $scope.DeleteUserPopup = function(row){
			 console.log(row.entity)
			 $scope.deleteUserId = row.entity.id;
			 $('#deleteModal').modal('show');
			 
		 }
		
		 $scope.DeleteUser = function(){
			 console.log($scope.deleteUserId);
			 apiserviceConfigPage.deleteLeadType($scope.deleteUserId).then(function(data){
				 if(data == "not delete"){
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Lead available for this type. you can't delete this type",
						});
				 }else{
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Delete successfully",
						});
				 }
				 console.log("in of funtion");
         		 $scope.leadTypeAll();
    		});
		 }
		 
		 $scope.editleadtype={};
		 $scope.UpdateLeadType = function(leadTypeData){
			 console.log(leadTypeData);
			 
			 localStorage.setItem('callToAction',leadTypeData.callToAction);
			 localStorage.setItem('leadId',leadTypeData.id);
			 console.log("out of funtion");
			 $scope.editleadtype.leadName = leadTypeData.leadName;
			 $scope.editleadtype.callToAction = leadTypeData.callToAction;
			 console.log($scope.editleadtype.leadName);
			 apiserviceConfigPage.UpdateLeadType($scope.editleadtype).then(function(data){
				 $scope.ShowFormBuildereEdit(leadTypeData.leadName);
				 console.log("in of funtion");
				$("#editPopups").modal('hide');
         		//$scope.allLeaddata();
         		//$scope.selectCheckbox();
				
         		$scope.leadTypeAll();
    		});
		 }
		 $scope.ShowFormBuildereEdit = function(row){
			 localStorage.setItem('popupType','Lead');
				console.log(row);
				console.log(row.leadName);
				if(row == "Create Lead"){
					$location.path('/CreateLeadForm/'+"Edit"+"/"+'Create Lead');
				}else
				if(row == "Add Product"){

					$location.path('/InventoryForm/'+"Edit"+"/"+'Inventory');
					
				}
				else if(row == "Create New Lead"){

					$location.path('/CreateLeadForm/'+"Edit"+"/"+'Create New Lead');
					
				}
				else if(row == "Add to CRM"){

					$location.path('/CRMForm/'+"Edit"+"/"+'CRM');
					
				}else if(row == "Request More Info"){
					$location.path('/RequestMoreInfoForm/'+"Edit"+"/"+row);
				}
				else if(row == "Contact Us"){
					$location.path('/ContactUsForm/'+"Edit"+"/"+row);
				}
				else if(row == "Request Appointment"){
					$location.path('/RequestAppointmentForm/'+"Edit"+"/"+row);
				}
				else{
					$location.path('/otherForm/'+"Edit"+"/"+row);
				}
				
			}
		 
	$scope.documentationSetting = function() {
		$location.path('/documentation');
		
	}
	
	$scope.customerRequest = function() {
		$location.path('/customerRequest');
	}
	$scope.leadTypeInfo = function() {
		//console.log("ddd22");
		$location.path('/leadtype');
		
	}
	$scope.form = function() {
		//console.log("ddd22");
		$location.path('/form');
		
	}
	$scope.webSite = function() {
		console.log("wesite");
		$location.path('/webSite');
		
	}
	
	$scope.autoPortal = function() {
		$location.path('/autoPortal');
		
	}
	
	$scope.domainDetails= function() {
		$location.path('/domainDetails');
		
	}
	
	
	$scope.plansAndBill= function() {
		$location.path('/plansAndBill');
		
	}
	
	$scope.newsLetterSetting = function() {
		$location.path('/newsLetter');
		
	}
	
	$scope.socialMedia = function() {
		$location.path('/socialMedia');
		
	}
	
	$scope.graphicInfo = function() {
		$location.path('/configuration');
		
	}
	$scope.premiumLeadsInfo= function() {
		$location.path('/premiumLeads');
		
	}
	
	$scope.websiteAnalytics= function() {
		$location.path('/webAnalytics');
		
	}
	
	$scope.mailchimp= function() {
		$location.path('/mailchimpPage');
		
	}
	
	$scope.saveEmailDetails= function(auto) {
		apiserviceConfigPage.saveEmailDetails(auto).then(function(data){
		});
		
		
		
	}
	$scope.selectHost = 0;
	$scope.showLoginPasswordText = function(hostname){
		if(hostname != "Other"){
			$scope.selectHost = 1;
		}else{
			$scope.selectHost = 2;
		}
	}
	
	$scope.saveDomain= function(sitenameData) {
		apiserviceConfigPage.saveDomain(sitenameData).then(function(data){	
		});
		
	}
	
	$scope.saveWebsite= function(webanlyatics) {
		console.log(webanlyatics);
		
		console.log("web analytics");
		apiserviceConfigPage.saveWebsite(webanlyatics).then(function(data){
		console.log("In Function web analytics");
        });
		
	}
	
	$scope.websiteAnalyticsData = function(){
		apiserviceConfigPage.getwebsiteAnalyticsData().then(function(data){
		$scope.webanlyatics = data;
		console.log($scope.webanlyatics);
		
	});	
	
	}
	$scope.disabledTxt = false;
	$scope.showPass = false;
	$scope.updateList = false;
	$scope.savetoQuicklists = function(){
		console.log("???????");
		$('#addQuickListPop').click();
	}
	
	$scope.viewUpdate = function(list){
		console.log(list);
		$scope.newList = list;
		$scope.updateList = true;
	};
	$scope.closeUpdate = function(){
		$scope.updateList = false;
		$scope.newError = false;
		$scope.newList = {};
	}
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
	
	$scope.deleteItem = function(list){
		$scope.listObj = list;
	};
	
	$scope.deleteQuickList = function(){
		console.log("deleteQuickList",$scope.listObj);
		apiserviceConfigPage.deleteList($scope.listObj).then(function(data){
			$scope.getAllMailchimpList();
		});
	};
	
	$scope.mailchimpPage= function(schedular) {
		var schedularObj = angular.copy(schedular);
		if(schedularObj.list != null || schedularObj.list != undefined)
			schedularObj.list = JSON.parse(schedularObj.list);
		apiserviceConfigPage.savemailchimpPage(schedularObj).then(function(data){
			
		});
	}
	$scope.getAllMailchimpList = function(){
		$http.get('/getAllMailchimpList').success(function(data){
			console.log(data);
			$scope.mailChimpLists = data;
		});
	}
	$scope.getAllMailchimpList();
	$scope.newList = {};
	$scope.saveNewList = function(newList){
		if(newList.nickName == undefined ||  newList.nickName == null || newList.listId == undefined ||  newList.listId == null){
			$scope.newError = true;
		}else{
			$scope.newError = false;
			apiserviceConfigPage.saveNewList(newList).then(function(data){
				$scope.newList = {};
				$scope.getAllMailchimpList();
			});
		}
	}
	
	
	
	$scope.mailchimpTest= function(schedular) {
		var schedularObj = angular.copy(schedular);
		if(schedularObj.list != null || schedularObj.list != undefined)
			schedularObj.list = JSON.parse(schedularObj.list);
		apiserviceConfigPage.savemailchimpPage(schedularObj).then(function(data){
			$http.get('/checkMailChim').success(function(data){
				console.log("?...?",data);
				if(data == 'success'){
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "MailChimp connection successfully",
					});
				}else{
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "MailChimp connection failed",
					});
				}
			}).error(function(error){
				console.log(error);
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "MailChimp connection failed Server Error",
				});
			});
		});
	}
	
	$scope.mailchimpData = function(){
		apiserviceConfigPage.getmailchimpData().then(function(data){
		$scope.schedular = data;
		if(data.list != null || data.list != undefined)
			$scope.schedular.list = JSON.stringify(data.list);
		console.log($scope.schedular);
	});	
	
	}
	
	$scope.autoPort={};
	$scope.saveAutoPortal= function(auto,siteName) {
		auto.sitename=siteName;
		$scope.autoPort=auto;
		apiserviceConfigPage.saveAutoPortal($scope.autoPort).then(function(data){
		});
		
	}
	
	$scope.saveEmailLinks= function(email) {
		console.log(email);
		apiserviceConfigPage.saveEmailLinks(email).then(function(data){
		});
	}
	
	
	
	
	$scope.autoPort1={};
	$scope.acountDetails= function(auto,siteName) {
		auto.sitename=siteName;
		$scope.autoPort1=auto;
		apiserviceConfigPage.acountDetails($scope.autoPort1).then(function(data){
		});
	}
	
	
	
	
	 var logofile;
		$scope.onFileSelect = function ($files) {
			logofile = $files;
			$upload.upload({
		 	         url : '/saveInternalPdf',
		 	         method: 'POST',
		 	         file:logofile,
		 	      }).success(function(data) {
		 	  			$.pnotify({
		 	  			    title: "Success",
		 	  			    type:'success',
		 	  			    text: "pdf saved successfully",
		 	  			});
		 	  			apiserviceConfigPage.getInternalPdfData().then(function(data){
		 	  				$scope.internalPdfList=data;
		 	  			});		
		 	  			
			
		});	
		}
		
		var logofile1;
		$scope.onCustomerFileSelect = function ($files) {
			logofile1 = $files;
			$upload.upload({
		 	         url : '/saveCustomerPdf',
		 	         method: 'POST',
		 	         file:logofile1,
		 	      }).success(function(data) {
		 	  			$.pnotify({
		 	  			    title: "Success",
		 	  			    type:'success',
		 	  			    text: "pdf saved successfully",
		 	  			});
		 	  			apiserviceConfigPage.getCustomerPdfData().then(function(data){
		 	  			
		 	  				/*$.pnotify({
		 	  				    title: "Success",
		 	  				    type:'success',
		 	  				    text: "Slider config saved successfully",
		 	  				});*/
		 	  				
		 	  			});		
		 	  			
			
		});	
		}
		
		$scope.deletePdf = function(id) {
			apiserviceConfigPage.getCustomerPdfDataById(id).then(function(data){
				$scope.customerPdfName=data;
  			});
			
			$('#btndeleteCustomerPdf').click();
			$scope.customerPdfId = id;
			
		}
		
		
		$scope.deletePdfCustomer = function() {
			console.log("$scope.customerPdfId"+$scope.customerPdfId);
			apiserviceConfigPage.deletePdfById($scope.customerPdfId).then(function(data){
			});
		}
		
		

		$scope.deleteInternalPdf = function(id) {
			apiserviceConfigPage.getInternalPdfDataById(id).then(function(data){
				$scope.internalPdfName=data;
  			});
			$('#btndeleteInternalPdf').click();
			$scope.internalPdfId = id;
		}
		
		
		
          $scope.deletePdfInternal = function() {
        	  apiserviceConfigPage.deleteInternalPdf($scope.internalPdfId).then(function(data){
        	  console.log("$scope.internalPdfId"+$scope.internalPdfId);
			});
		}
		
		$scope.permiumAss = function(saleP){
			apiserviceConfigPage.setPermiumFlag(saleP.id).then(function(data){
				console.log("Yesssss");
		});
	}
	
	$scope.saveSlider = function() {
		apiserviceConfigPage.saveSliderConfig($scope.slider.width, $scope.slider.height).then(function(data){
		});
	}
	
	$scope.saveFeatured = function() {
		apiserviceConfigPage.saveFeaturedConfig($scope.featured.width, $scope.featured.height).then(function(data){
		});
	}
	
	$scope.saveVehicleSize = function() {
		apiserviceConfigPage.saveVehicleConfig($scope.vehSize.width, $scope.vehSize.height).then(function(data){
		});
	}
	
	$scope.saveCoverImageSize = function() {
		apiserviceConfigPage.setCoverImage($scope.cover.width, $scope.cover.height).then(function(data){
		});
	}
	
	$scope.savePremium = function() {
		if($scope.premium.premiumFlag == undefined || $scope.premium.premiumFlag == null){
			$scope.premium.premiumFlag = "0";
		}
		apiserviceConfigPage.savePremiumConfig($scope.premium.priceVehical, $scope.premium.premiumFlag).then(function(data){
		});
	}
	
	$scope.saveDayOfMonth = function() {
		$scope.newsletterTime = $('#newsTime').val();
		apiserviceConfigPage.saveNewsletterDate($scope.newsletterDay, $scope.newsletterTime, $scope.newsletterId, $scope.newsletterTimeZone).then(function(data){
		});
	}
	/*-----------------------------Customer Request----------------------------*/
	
	$scope.redirestRequest = function(type){
		$scope.redirectValue = type;
		console.log($scope.redirectValue);
		if(type == "Redirect all online requests to"){
			$scope.customerReq = [];
			$scope.customerReq.redirectValue = type;
			$scope.typeValue = type;
		}
		else if(type == "Automatically redirect an online customer requests based on"){
			$scope.customerReq = [];
			$scope.customerReq.redirectValue = type;
			$scope.typeValue = type;
		}
		
	}
	
	apiserviceConfigPage.getUserRole().then(function(data){
		$scope.userRoleData = data;
		console.log($scope.userRoleData.role);
	});
	
	apiserviceConfigPage.getAllSalesPersons().then(function(data){
		$scope.salesPersonName = data;
		if($scope.userRoleData.role == "Manager"){
			$scope.salesPersonName.push($scope.userRoleData); 
		}
		console.log($scope.salesPersonName);
	});
	
	$scope.personsWiseData = function(type){
		$scope.personValue = type;
		console.log($scope.personValue);
		if(type == "Sales Person(s)"){
			apiserviceConfigPage.getAllSalesPersons().then(function(data){
					$scope.salesPersonName = data;
					if($scope.userRoleData.role == "Manager"){
						$scope.salesPersonName.push($scope.userRoleData); 
					}
			});
			
		}
		else{
			$scope.salesPersonName = [];
		}
	}
	
	$scope.realeasedTO = function(value){
		$scope.outLeftAll = value;
		if(value == "Sent to one of the sales people"){
			apiserviceConfigPage.getAllFrontAndSalesPer().then(function(data){
					$scope.salesPersonName = data;
			});
			
		}
		else{
			$scope.salesPersonName = [];
		}
	}
	$scope.listAll = {};
	$scope.saveSalesPeople = function(nameList){
		$scope.listAll = nameList;
		$scope.listAll.outLeftAll = $scope.outLeftAll;
	}
	$scope.saveRedirectToAll = function(){
		console.log($scope.listAll);
		console.log($scope.salesPersonList);
		var priceFlag = 0;
		$scope.priceNotAssign = [];
		angular.forEach($scope.salesPersonList, function(obj, index){
			if(obj.priceEnd == null || obj.priceStart == null){
				$scope.priceNotAssign.push(obj);
				priceFlag = 1;
			}
		});
		if(priceFlag == 0){
			var longId = 0;
			if($scope.realese.allSalesPeople == "Sent to one of the sales people"){
				longId = $scope.listAll.id;
			}
			
			apiserviceConfigPage.saveOutListAll($scope.realese.allSalesPeople,longId).then(function(data){
				console.log(data);
			});
		}else{
			//$scope.priceNotAssign
			$("#priceAlrt").modal('show');
		}
	}
	
	$scope.proceedForSave = function(){
		var longId = 0;
		console.log($scope.listAll.id);
		console.log($scope.realese.allSalesPeople);
		if($scope.realese.allSalesPeople == "Sent to one of the sales people"){
			longId = $scope.listAll.id;
		}
		apiserviceConfigPage.saveOutListAll($scope.realese.allSalesPeople,longId).then(function(data){
			console.log(data);
		});
	}
	
	$scope.saveCustomerData = function(type){
		$scope.salespersonName = type;
	}
	
	$scope.dataSalesPer = {};
	$scope.saveSalesPersonsData = function(value){
		console.log($scope.salespersonName);
		
		$scope.dataSalesPer.personValue = $scope.personValue;
		$scope.dataSalesPer.redirectValue = $scope.redirectValue;
		$scope.dataSalesPer.salespersonName =  $scope.salespersonName.firstName;
		$scope.dataSalesPer.id = $scope.salesId;
		console.log($scope.dataSalesPer);
		apiserviceConfigPage.saveSalesPersons($scope.dataSalesPer).then(function(data){
			console.log(data);
		});
	}
	
	apiserviceConfigPage.getAllCustomerReqData().then(function(data){
		console.log(data);
		$scope.customerReq = data;
		$scope.salesId = $scope.customerReq.id;
		console.log($scope.salerPerFirstName);
	});
	
	apiserviceConfigPage.getAllCustomerManufacturer().then(function(data){
		console.log(data);
		$scope.editCustManufData = data;
	});
	
	apiserviceConfigPage.getAllSalesPersonZipCode().then(function(data){
		console.log(data);
		$scope.editSalesZipData = data;
	});
	
	
	apiserviceConfigPage.getAllManufacturer().then(function(data){
		$scope.allManufacturerList =data;
		console.log($scope.allManufacturerList);
		apiserviceConfigPage.getAllFrontAndSalesPer().then(function(data){
			$scope.allFronAndSalesList =data;
			
			angular.forEach($scope.allFronAndSalesList, function(obj, index){
				obj.manuCount = 0;
				obj.premiumFlag = false;
			});
			angular.forEach($scope.allManufacturerList, function(obj, index){
				obj.userData = angular.copy($scope.allFronAndSalesList); 
			 });
			$scope.zipCode ={};
			angular.forEach($scope.allFronAndSalesList, function(obj, index){
				obj.zipCode = [];
			});
			console.log($scope.allFronAndSalesList);
			angular.forEach($scope.allFronAndSalesList, function(obj, index){
				angular.forEach($scope.editSalesZipData, function(obj1, index1){
					if(obj.id == obj1.user.id){
						obj.zipCode.push({zipcode:obj1.zipCode,
							isSelected:true,
							city:obj1.city,
							state:obj1.state});
					}
				});
			});
			angular.forEach($scope.allManufacturerList, function(obj, index){
				angular.forEach($scope.editCustManufData, function(obj1, index1){
					 if(obj.id == obj1.manufacturer.id){
						 angular.forEach(obj.userData, function(obj2, index2){
							 if(obj2.id == obj1.user.id){
								 obj2.premiumFlag = true;
							 }
						 });
					 }
				 });
			 });
			
			$scope.msgShow($scope.allFronAndSalesList);
			
		});
		
	
	});
	
	$scope.msgShow = function(allFronAndSalesList){
		
		$scope.salePersonNotass = [];
		var saleFlag = 0;
		angular.forEach(allFronAndSalesList, function(obj2, index2){
			saleFlag = 0;
			
			angular.forEach($scope.allManufacturerList, function(obj, index){
				 angular.forEach(obj.userData, function(obj1, index1){
					 if(obj2.id == obj1.id){
						 if(obj1.premiumFlag == true){
							 saleFlag = 1;
							 obj2.manuCount++;
						 }
					 }
					 
				 });
			});
			if(saleFlag == 0){
				 $scope.salePersonNotass.push(obj2);
			}
			
		});
		angular.forEach(allFronAndSalesList, function(obj, index){
			angular.forEach($scope.allManufacturerList, function(obj1, index1){
				 angular.forEach(obj1.userData, function(obj2, index2){
					 if(obj.id == obj2.id){
							obj2.manuCount = obj.manuCount;
					}
				 });
				
			});
		});	
		
		console.log("-------------------------");
		console.log($scope.allManufacturerList);
	}
	
	
	
	$scope.listOfSalesAndFront = [];
	$scope.salesPersonListPop = function(salesper){
		console.log(salesper);
		$('#allSalesAndFront').click();
		$scope.salesPerDetail = salesper;
	}
	
	$scope.storeValue = function(saleP){
		console.log(saleP);
		console.log(saleP.premiumFlag);
		console.log($scope.allManufacturerList)
		
	}
	
	$scope.selectedSalesPer = function(){
		//console.log($scope.listOfSalesAndFront);
	}
	
	$scope.saveDetailsOfUser = function(){
		$scope.obj = {allManufacturerList:$scope.allManufacturerList};
		console.log($scope.obj);
		$scope.notSelectedSales = [];
		var flagValue = 0;
		var flagcheck = 0;
		console.log($scope.allFronAndSalesList);
		angular.forEach($scope.allFronAndSalesList,function(obj3,index3){
			var flagUser =0;
			angular.forEach($scope.obj.allManufacturerList, function(obj1, index1){
				var flag = 0;
				angular.forEach(obj1.userData, function(obj2, index2){
						if(obj2.premiumFlag == true){
							flag = 1;
						}
						if(obj3.id == obj2.id){
							if(obj2.premiumFlag == true){
								flagUser =1;
								
							}
						}
				});
				if(flag == 0){
					flagValue = 1;
				}
			});
			if(flagUser == 0){
				flagcheck = 1;
				$scope.notSelectedSales.push(obj3); 
			}
		});
		console.log($scope.notSelectedSales);
		if(flagValue == 1){
			$.pnotify({
			    title: "Error",
			    type:'success',
			    text: "At List One Manfacturer have one Sales Person is Selected",
			});
		}else{  
			if(flagcheck == 1){
				$('#notselectedSelesPer').click();
			}
			else{
				apiserviceConfigPage.saveManfactSales($scope.obj).then(function(data){
					console.log(data);
				});
			}
		}
		$scope.msgShow($scope.allFronAndSalesList);
	}
	$scope.saveManufactListCust = function(){
		console.log($scope.obj);
		apiserviceConfigPage.saveManfactSales($scope.obj).then(function(data){
			console.log(data);
		});
	}
	
	
	$scope.openPopUpForPrice = function(sale){
		console.log(sale);
		console.log($scope.salesPersonList);
		$('#addPremiumPrice').click();
		$scope.saleDeails = sale;
	}
	
	$scope.addPremiumPrice = function(price){
		
		
		$scope.saleDeails.priceStart = price.priceStart;
		$scope.saleDeails.priceEnd = price.priceEnd;
		angular.forEach($scope.salesPersonList, function(obj, index){
			if(obj.showDataValue == true){
				obj.priceStart = price.priceStart;
				obj.priceEnd = price.priceEnd;
				
				$scope.addPremiumPriceByList(obj);
			}
			/*if(obj.id == $scope.saleDeails.id){
				obj.priceStart = price.priceStart;
				obj.priceEnd = price.priceEnd;
			}*/
		});
		
		$.pnotify({
		    title: "Success",
		    type:'success',
		    text: "Update successfully",
		});
		
		console.log($scope.salesPersonList);
		$('#addPremiumPrice').click();
	}
	
	$scope.addPremiumPriceByList = function(obj){
		apiserviceConfigPage.savePriceFromTo(obj).then(function(data){
			console.log(data);
		});
	} 
	
	$scope.showCityAddPop = function(salesDet){
		console.log(salesDet);
		$scope.zopCodeData = [];
		$scope.salesDetail = salesDet;
		$scope.zipCodeDetailData = [];
		$('#editAddressPop').click();
	}
	
	
	$scope.zopCodeData =[];
	$scope.storeZipCode = function(value){
		console.log(value);
		$scope.zopCodeData.push(value);
		$scope.salesDetail.zipcode = value;
		console.log($scope.allFronAndSalesList);
		angular.forEach($scope.allFronAndSalesList, function(obj, index){
			if(obj.id == $scope.salesDetail.id){
				console.log(obj.id);
				obj.zipCode = angular.copy($scope.zopCodeData);
			}
		 });
		console.log($scope.allFronAndSalesList);
	}
	
	
	$scope.getZipCode = function(address){
		console.log(address);
		apiserviceConfigPage.getZipCodeData(address).then(function(data){
			console.log(data);
			if(data.zip_codes.length == 0){
				$.pnotify({
				    title: "Warning",
				    type:'success',
				    text: "Zip Code not Available",
				});
			}else{
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Zip Code Fatched successfully",
				});
			}
			$scope.zipCodeDetailData = [];
			angular.forEach(data.zip_codes, function(obj, index){
				$scope.zipCodeDetailData.push({
					zipcode:obj,
					isSelected:false,
					city:address.city,
					state:address.state
				});
			 });
			/*angular.forEach($scope.allFronAndSalesList, function(obj, index){
				obj.zipCode = angular.copy($scope.zipCodeDetailData); 
			 });*/
			console.log($scope.zipCodeDetailData);
		});
	}
	$scope.SaveZipCodeData = function(){
		console.log($scope.allFronAndSalesList);
		$scope.obj = {allFronAndSalesList:$scope.allFronAndSalesList};
		console.log($scope.obj);
		apiserviceConfigPage.saveZipCodeDetails($scope.obj).then(function(data){
			console.log(data);
		});
	}
	
	apiserviceConfigPage.getAllStateCodes().then(function(data){
		console.log(data);
		$scope.stateCodesList = data;
	});
	
		$scope.clickedUser = function(users, userSelect){
			$scope.showFlag = 1;
		console.log(users);
		console.log(userSelect);
		console.log($scope.salesPersonList);
	}
}]);	