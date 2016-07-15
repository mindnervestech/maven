angular.module('newApp')
.controller('ConfigPageCtrl', ['$scope','$http','$location','$filter','$upload','$routeParams', function ($scope,$http,$location,$filter,$upload,$routeParams) {
	$scope.premium = {};
	
	$scope.form = function(){
		$location.path('/form/');
	}
	

	
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 $scope.gridOptions.enableVerticalScrollbar = 2;
	 
	
	
	$scope.init = function() {
		$http.get('/getImageConfig')
		.success(function(data) {
			$scope.cover=data.coverData;
			$scope.vehSize=data.vehicleImageConfig;
			$scope.slider = data.slider;
			$scope.featured = data.featured;
			$scope.newsletterDay = data.NewsletterDate;
			$scope.newsletterId = data.NewsletterId;
			$scope.newsletterTime = data.newsletterTime;
			$scope.newsletterTimeZone = data.NewsletterTimeZone;
			$scope.domain = data.domain;
			$scope.showLoginPasswordText($scope.domain.hostingProvider);
		
			$scope.premium.priceVehical = parseInt(data.premiumLeads.premium_amount);
			
			if(data.premiumLeads.premium_flag == 1){
				$scope.premium.premiumFlag = true;
			}else{
				$scope.premium.premiumFlag = false;
			}
		});
		
		
		$http.get('/getAllSalesUsers').success(function(data){
			$scope.salesPersonList =data;
		
			
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
	
		
		
			$http.get('/getSocialMediadetail')
			.success(function(data) {
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.media=data;
				//$scope.customerPdfList=data;
				
			});	
		
		$http.get('/getCustomerPdfData')
			.success(function(data) {
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.customerPdfList=data;
				
			});
			
		
		$http.get('/getEmailDetails')
			.success(function(data) {
				
				$scope.email=data;
				
			});	
		
			$http.get('/getInternalPdfData')
			.success(function(data) {
				/*$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Slider config saved successfully",
				});*/
				$scope.internalPdfList=data;
			});	
			
			$http.get('/getAllSites')
	 		.success(function(data) {
	 			$scope.siteList = data;
	 			
	 		});
		
	}
	
	$scope.flagForChart1 = true;
	$scope.systemInfo = function(){
		
		console.log("sdfghjkp0000");
		$http.get('/getsystemInfo').success(function(data){
			console.log("systemInfo");
			console.log(data);
			$scope.leadtypeObjList = data;
 			$scope.gridOptions.data = data;
		});
		
		
		//ng-click="grid.appScope.editName(row)"  use for edit name
		$scope.gridOptions.columnDefs = [
		                                 { name: 'name', displayName: 'Name', width:'70%',
		                                	 cellTemplate:'<div ><label  style="color:#319DB5;cursor:pointer;"  ng-click="grid.appScope.ShowCreateNewForm1(row)">{{row.entity.name}}</label></div>',
		                                 },
		                                 
		                                 { name: 'edit', displayName: ' ', width:'30%',
    		                                 cellTemplate:'<a ng-click="grid.appScope.ShowCreateNewForm(row)">&nbsp;&nbsp;&nbsp;&nbsp;<i class="glyphicon glyphicon-pencil"  title="Edit"></i></a> ', 
    		                                 /*ng-if="(row.entity.leadName != "Request More Info" || row.entity.leadName != "Schedule Test" || row.entity.leadName != "Trade In")"*/
		                                 },
		                                    ];
	}
	$scope.flagForChart1 = true;
$scope.leadTypeAll = function(){
		
		console.log("sdfghjkp0000");
		$http.get('/getLeadTypeData').success(function(data){
			console.log("lead type data");
			console.log(data);
			$scope.leadtypeObjList = data;
 			$scope.gridOptions.data = data;
 			angular.forEach($scope.gridOptions.data, function(obj, index){
				if(obj.checkValue == 1){
					obj.checkValue = true;
				}else if(obj.checkValue == 0){
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
		                                	 cellTemplate:'<div class="link-domain" ><input type="checkbox" ng-model="checkValue" ng-checked="row.entity.checkValue" ng-click="grid.appScope.selectCheck(row,checkValue)">  </div>',
		                                 },
		                                 { name: 'edit', displayName: ' ', width:'20%',
    		                                 cellTemplate:'<i class="fa fa-trash" ng-if="row.entity.leadName != \'Request More Info\' && row.entity.leadName != \'Schedule Test\' && row.entity.leadName != \'Trade In\'" ng-click="grid.appScope.removeUser(row)"  title="Delete"></i> &nbsp;&nbsp;&nbsp<i class="glyphicon glyphicon-pencil" ng-if="row.entity.leadName != \'Request More Info\' && row.entity.leadName != \'Schedule Test\' && row.entity.leadName != \'Trade In\'" ng-click="grid.appScope.EditUser(row)"  title="Edit"></i> ', 
    		                                 /*ng-if="(row.entity.leadName != "Request More Info" || row.entity.leadName != "Schedule Test" || row.entity.leadName != "Trade In")"*/
		                                 },
		                                    ];
	}
	
	$scope.allFormName = function(){
		$http.get('/allFormName')
		.success(function(data){
			$scope.gridOptions.data=data;
			console.log(data);
		})
	}	
	
	$scope.selectCheck = function(row,checkValue){
		$scope.entityId=row.entity.id;
		console.log(row);
		console.log(checkValue);
		var intValue = 0;
		if(checkValue == undefined){
			intValue = 1;
		}
		if(checkValue == true){
			intValue = 0;
		}else if(checkValue == false){
			intValue = 1;
		}
		
		$http.get('/getCheckButton/'+$scope.entityId+"/"+intValue)
		.success(function(data){
			//$scope.gridOptions.data=data;
			//console.log(data);
		})
	}
	
	$scope.ShowCreateNewForm = function(row){
		console.log(row);
		if(row.entity.name == "Add Product"){

			$location.path('/'+'InventoryForm/'+"Edit"+"/"+'Inventory');
			
		}
		else if(row.entity.name == "Add to CRM"){

			$location.path('/'+'CRMForm/'+"Edit"+"/"+'CRM');
			
		}else if(row.entity.name == "Request More Info"){
			$location.path('/RequestMoreInfoForm/'+"Edit"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Contact Us"){
			$location.path('/ContactUsForm/'+"Edit"+"/"+row.entity.name);
		}
		else if(row.entity.name == "Request Appointment"){
			$location.path('/RequestAppointmentForm/'+"Edit"+"/"+row.entity.name);
		}
			
	}

	$scope.ShowCreateNewForm1 = function(row){
		var str = row.entity.name.replace(" ","");
			$location.path('/'+str+'Form/'+"Preview"+"/"+row.entity.name);
	}
	
	
	$scope.allLeaddata = function(){
		$http.get('/getAllLeadData')
		.success(function(data){
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
		
		$scope.saveCompleted = function(){
		console.log("::::::insideRegester");
		console.log($scope.leadcreate);
		$http.post("/addnewrUser",$scope.leadcreate).success(function(data){
				$scope.form = data;
			 console.log("::::::success")
				
			 $("#completedPopup").modal('hide');
			 $scope.allLeaddata();
			});
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
			 $http.get('/getdeletelead/'+$scope.entityId)
				.success(function(data) {
					 console.log("out deletend functio");
					 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Remove User",
						});
					 
					 $scope.allLeaddata();
					 
					 

				});
		 }
		 
		 $scope.editName = function(row){
				
			 $('#editPopup').click();
			 console.log(row.entity)
			 $scope.name = row.entity.name;
			 $scope.editleadtype.id = row.entity.id;
			 
		 }
			
		 $scope.editleadtype={};
		 $scope.Updatename = function(name){
			 console.log($scope.editleadtype);
			 console.log("out of funtion");
			 $scope.editleadtype.name = name;
			 $http.post("/UpdateName",$scope.editleadtype)
			 .success(function(data){
				 console.log("in of funtion");
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
         		$("#editPopups").modal('hide');
         		$scope.allFormName();
    		});
		 }
		 
		 $scope.addNewForm = function(){
				$scope.addform={"name":""};
				//$('#createLeadPopup').click();
				$('#completedPopup').modal('show');
			}
			
			$scope.savedNewForm = function(){
			console.log("::::::insideRegester");
			console.log($scope.leadcreate);
			$http.post("/addnewForm",$scope.addform).success(function(data){
					$scope.form = data;
				 console.log("::::::success")
					
				 $("#completedPopup").modal('hide');
				 $scope.allFormName();
				});
			}
		 
		
		 $scope.EditUser = function(row){
			
			 $('#editPopup').click();
			 console.log(row.entity)
			 $scope.leadName = row.entity.leadName;
			 $scope.editleadtype.id = row.entity.id;
			 
		 }
			
		 $scope.editleadtype={};
		 $scope.UpdateLeadType = function(leadName){
			 console.log($scope.editleadtype);
			 console.log("out of funtion");
			 $scope.editleadtype.leadName = leadName;
			 $http.post("/UpdateLeadType",$scope.editleadtype)
			 .success(function(data){
				 console.log("in of funtion");
				 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
         		$("#editPopups").modal('hide');
         		$scope.allLeaddata();
    		});
		 }
		 
		 
	$scope.documentationSetting = function() {
		$location.path('/documentation');
		
	}
	
	
	$scope.leadTypeInfo = function() {
		//console.log("ddd22");
		$location.path('/leadtype');
		
	}
	/*$scope.systemInfo = function() {
		//console.log("ddd22");
		$location.path('/form');
		
	}*/
	
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
	
	$scope.saveEmailDetails= function(auto) {
		$http.post('/saveEmailDetails',auto)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Email Info saved successfully",
			});
            
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
				
		$http.post('/saveDomain',sitenameData)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Domain saved successfully",
			});
            
		});
		
		
		
	}
	
	
	
	
	
	
	
	
	$scope.autoPort={};
	$scope.saveAutoPortal= function(auto,siteName) {
		auto.sitename=siteName;
		$scope.autoPort=auto;
		//$scope.autoPort.siteName=siteName;
		$http.post('/saveAutoPortal',$scope.autoPort)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Autoportal details saved successfully",
			});
            
		});
		
	}
	
	$scope.saveEmailLinks= function(email) {
		console.log(email);
		//$scope.autoPort.siteName=siteName;
		$http.post('/saveEmailLinks',email)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Social Links saved successfully",
			});
            
		});
	}
	
	
	
	
	$scope.autoPort1={};
	$scope.acountDetails= function(auto,siteName) {
		auto.sitename=siteName;
		$scope.autoPort1=auto;
		//$scope.autoPort.siteName=siteName;
		$http.post('/acountDetails',$scope.autoPort1)
		.success(function(data) {

            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Username and Passward saved successfully",
			});
            
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
			
		 	  			$http.get('/getInternalPdfData')
		 	  			.success(function(data) {
		 	  				
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
			
		 	  			$http.get('/getCustomerPdfData')
		 	  			.success(function(data) {
		 	  				/*$.pnotify({
		 	  				    title: "Success",
		 	  				    type:'success',
		 	  				    text: "Slider config saved successfully",
		 	  				});*/
		 	  				
		 	  			});		
		 	  			
			
		});	
		}
		
		$scope.deletePdf = function(id) {
			
			$http.get('/getCustomerPdfDataById/'+id)
  			.success(function(data) {
		        $scope.customerPdfName=data;
  			});
			
			$('#btndeleteCustomerPdf').click();
			$scope.customerPdfId = id;
			
		}
		
		
		$scope.deletePdfCustomer = function() {
			console.log("$scope.customerPdfId"+$scope.customerPdfId);
			
			$http.get('/deletePdfById/'+$scope.customerPdfId)
	  			.success(function(data) {
	  				$.pnotify({
	  				    title: "Success",
	  				    type:'success',
	  				    text: "PDF deleted successfully",
	  				});
			
	  			});
		}
		
		

		$scope.deleteInternalPdf = function(id) {
			
			$http.get('/getInternalPdfDataById/'+id)
  			.success(function(data) {
		        $scope.internalPdfName=data;
  			});
			
			$('#btndeleteInternalPdf').click();
			$scope.internalPdfId = id;
		}
		
		
		
          $scope.deletePdfInternal = function() {
			
        	  console.log("$scope.internalPdfId"+$scope.internalPdfId);
			$http.get('/deleteInternalPdf/'+$scope.internalPdfId)
	  			.success(function(data) {
	  				$.pnotify({
	  				    title: "Success",
	  				    type:'success',
	  				    text: "PDF deleted successfully",
	  				});
			
	  			});
		}
		
		$scope.permiumAss = function(saleP){
		$http.get('/setPermiumFlag/'+saleP.id).success(function(data){
			console.log("Yesssss");
		});
	}
	
	$scope.saveSlider = function() {
		$http.get('/saveSliderConfig/'+$scope.slider.width+'/'+$scope.slider.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Slider config saved successfully",
			});
		});
	}
	
	$scope.saveFeatured = function() {
		$http.get('/saveFeaturedConfig/'+$scope.featured.width+'/'+$scope.featured.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Featured config saved successfully",
			});
		});
	}
	
	$scope.saveVehicleSize = function() {
		$http.get('/saveVehicleConfig/'+$scope.vehSize.width+'/'+$scope.vehSize.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle config saved successfully",
			});
		});
	}
	
	
	
	
	$scope.saveCoverImageSize = function() {
		$http.get('/setCoverImage/'+$scope.cover.width+'/'+$scope.cover.height)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle config saved successfully",
			});
		});
	}
	
	$scope.savePremium = function() {
		if($scope.premium.premiumFlag == undefined || $scope.premium.premiumFlag == null){
			$scope.premium.premiumFlag = "0";
		}
		$http.get('/savePremiumConfig/'+$scope.premium.priceVehical+'/'+$scope.premium.premiumFlag)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Premium saved successfully",
			});
		});
	}
	
	$scope.saveDayOfMonth = function() {
		$scope.newsletterTime = $('#newsTime').val();
		$http.get('/saveNewsletterDate/'+$scope.newsletterDay+'/'+$scope.newsletterTime+'/'+$scope.newsletterId+'/'+$scope.newsletterTimeZone)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Newsletter date saved successfully",
			});
		});
	}
	
}]);	
