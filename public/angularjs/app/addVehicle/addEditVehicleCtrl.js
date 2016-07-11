
angular.module('newApp')
.controller('addVehicleCtrl', ['$scope','$http','$location','$upload','$rootScope', function ($scope,$http,$location,$upload,$rootScope) {
  
	  
	$http.get('/getCustomizationform/'+'Inventory').success(function(response) {
		console.log(response);
		 $scope.editInput = response;
		 //$scope.josnData = angular.fromJson(response.jsonData);
		 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
		 console.log($scope.userFields);
		 $scope.user = {};
		});
	
	
	$http.get('/getColl').success(function(response) {
		 $scope.collectionList = response[0];
		 console.log($scope.collectionList);
	});
	
	$scope.vinErr = false;
   $scope.vehicleInit = function() {
 	  $http.get('/getAllSites')
 		.success(function(data) {
 			$scope.siteList = data;
 		});
 	 /*$http.get('/getDealerProfile').success(function(data) {
 		
 		$scope.vinData.specification.location = data.dealer.address;
 	});*/
   }
   $scope.makeList = [];
   $scope.modelList = [];
   $scope.trimList = [];
   $scope.labelList = [];
   $scope.madeInList = [];
   $scope.stereoList = [];
   $scope.driveTypeList = [];
   $scope.fuelTypeList = [];
   $scope.exteriorColorList = [];
   
   $http.get('/getMakeList').success(function(data) {
		$scope.labelList = data.label;
		$scope.makeList = data.make;
		$scope.madeInList = data.madeIn;
		$scope.stereoList = data.stereo;
		$scope.driveTypeList = data.driveType;
		$scope.fuelTypeList = data.fuelType;
		$scope.exteriorColorList = data.exteriorColor;
	});
   $scope.selectedMake = function (selectObj) {
		if(selectObj != undefined){
			$scope.vinData.specification.make = selectObj.title;
			$http.get('/getModelList/'+selectObj.title)
			.success(function(data) {
				$scope.modelList = data;
			});
		}
	};
	$scope.selectedModel = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.model = selectObj.title;
				$http.get('/getTrimList/'+selectObj.title)
				.success(function(data) {
					$scope.trimList = data;
				});
			}
	};
	$scope.selectedTrim = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.trim_level = selectObj.title;
			}
	};  
	$scope.selectedLabel = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.label = selectObj.title;
			}
	};	
   $scope.siteIds = [];
   $scope.setSiteId = function(id,flag) {
 	  if(flag == true) {
 		  $scope.siteIds.push(id);
 	  } 
 	  if(flag == false) {
 		  $scope.siteIds.splice($scope.siteIds.indexOf(id),1);
 	  }
 	  
   };
   $scope.selectedFuelType = function (selectObj) {
		if(selectObj != undefined){
			$scope.vinData.specification.fuelType = selectObj.title;
		}
   };
   $scope.fuelFocusOut = function(){
	   $scope.vinData.specification.fuelType = $('#fuelTypeSearch_value').val();
   }
   $scope.fuelTypeChange = function(){
	   $('#fuelTypeSearch_value').val($scope.vinData.specification.fuelType);
   }
   var pdffile;
		$scope.onLogoFileSelect = function($files) {
			pdffile = $files;
		}
   
   $scope.checkStock = function(stock){
	   if(stock == undefined){
		   $.pnotify({
			    title: "Error",
			    type:'success',
			    text: "Please Enter Unique Stock Number",
			});
	   }else{
		   $http.get('/checkStockNumber/'+stock)
			.success(function(data) {
				if(data != 0){
					$.pnotify({
					    title: "Error",
					    type:'success',
					    text: "Please Enter Unique Stock Number",
					});
					$scope.vinData.specification.stock = null;
				}
			});
	   }
   }
		
   $scope.vinData = {};
   $scope.vinData.specification = {};
   $scope.vinData.specification.typeofVehicle = "New";
   
   $scope.getVinData = function() {
	   if(!angular.isUndefined($scope.vinNumber)) {
	 	  $http.get('/getVehicleInfo/'+$scope.vinNumber)
			.success(function(data) {
				if(data.success == true) {
					$scope.vinData = data;
					if($scope.vinData.specification.siteIds != null) {
						for(var i=0;i<$scope.vinData.specification.siteIds.length;i++) {
							for(var j=0;j<$scope.siteList.length;j++) {
								if($scope.vinData.specification.siteIds[i] == $scope.siteList[j].id) {
									$scope.siteList[j].flag = true;
								}
							}
						}
					}
				}
				
				if(data.success == false) {
					$scope.vinErr = true;
				} else {
					$scope.vinErr = false;
				}
				
			});
	   }
   }
   $scope.dataShow1 = function(check){
		$scope.vinData.specification.price = 0;
	}
   
   
   $scope.saveVehicle = function() {
	   console.log("kkkkkkkkkkkkk");
	  
	   /*$scope.vinData.specification.model = $('#modelSearch_value').val();
	   $scope.vinData.specification.make = $('#makeSearch_value').val();
	   $scope.vinData.specification.trim_level = $('#trimSearch_value').val();
	   $scope.vinData.specification.label = $('#labelSearch_value').val();
	   
	   $scope.vinData.specification.made_in = $('#madeInSearch_value').val();
	   $scope.vinData.specification.extColor = $('#extColorSearch_value').val();
	   $scope.vinData.specification.stereo = $('#stereoSearch_value').val();
	   $scope.vinData.specification.drivetrain = $('#driveTypeSearch_value').val();
		   
		   $scope.vinData.specification.cost = $scope.vinData.specification.cost.split(',').join('');
		   var comingSoonDate = {};
	  if($scope.vinData.specification.commingSoonVehicle == true){
		  $scope.vinData.specification.comingSoonDate = $('#comingsoonDate').val();
		  
	  }*/
 	  //$scope.vinData.specification.siteIds = $scope.siteIds;
 	  
 	  
 	//  var saveFlag = 0;
 	  
 	/* if($scope.vinData.specification.commingSoonVehicle == true){
 		  if($scope.vinData.specification.price != null && $scope.vinData.specification.price != ''){
 			  $scope.vinData.specification.price = $scope.vinData.specification.price.split(',').join('');
 		  }else{
 			 $scope.vinData.specification.price = 0;
 		  }
 		 saveFlag = 1;
 	  }else{
 		  if($scope.vinData.specification.price == null || $scope.vinData.specification.price == '' || $scope.vinData.specification.price == undefined){
 			 $.pnotify({
 			    title: "Error",
 			    type:'success',
 			    text: "Price fields required",
 			});
 			saveFlag = 0;
 		  }else{
 			 $scope.vinData.specification.price = $scope.vinData.specification.price.split(',').join('');
 		     saveFlag = 1;
 		  }
 		 
 	  }*/
 	  
 	// if(saveFlag == 1){
 		//if(($scope.vinData.specification.model != null && $scope.vinData.specification.model != "") && ($scope.vinData.specification.make != null && $scope.vinData.specification.make != " ")){
 	 		 var ele = document.getElementById('loadingmanual');	
 	     	$(ele).show();
 	     	 	     	
 	     	if($rootScope.fileCustom != undefined){
 	     		
 	     		pdffile = $rootScope.fileCustom;
 	     		
 	 	 		$http.post('/saveVehicle',$scope.vinData.specification)
 	 			.success(function(data) {
 	 				$.pnotify({
 	 				    title: "Success",
 	 				    type:'success',
 	 				    text: "Vehicle saved successfully",
 	 				});
 	 				
 	 				$scope.dataBeforePdf=data;
 	 				$upload.upload({
 	 		 	         url : '/saveVehiclePdf/'+data,
 	 		 	         method: 'POST',
 	 		 	         file:pdffile,
 	 		 	      }).success(function(data) {
 	 		 	  			$.pnotify({
 	 		 	  			    title: "Success",
 	 		 	  			    type:'success',
 	 		 	  			    text: "Vehicle saved successfully",
 	 		 	  			});
 	 		 	  		$location.path('/editVehicle/'+$scope.dataBeforePdf+"/"+true);
 	 		 	      });
 	 			});
 	 	 	 }else{
 	 	 		$http.post('/saveVehicle',$scope.vinData.specification)
 	 			.success(function(data) {
 	 				$.pnotify({
 	 				    title: "Success",
 	 				    type:'success',
 	 				    text: "Vehicle saved successfully",
 	 				});
 	 				$location.path('/editVehicle/'+data+"/"+true);
 	 			});
 	 	 	 }
 	 	  /*}else{
 	 		 $.pnotify({
 				    title: "Error",
 				    type:'success',
 				    text: "Please select all fields",
 				});
 	 	  }*/
 	 //}
 	  
 	 /* */
 	  
   }
   
   $scope.setFlagVal = function() {
	   $scope.flagVal = false;
   }
   
   $scope.addPhoto = function() {
	    $scope.customList =[];
		//$scope.customData.setTime = $("#bestTimes").val();
		
		console.log($("#autocomplete").val());
		   console.log($scope.vinData.specification);
		   $scope.customData.custName = $('#exCustoms_value').val();
			if($scope.customData.custName == undefined){
				delete $scope.customData.custName;
			}
			$scope.customData.address_bar = $("#autocomplete").val();
			if($scope.customData.address_bar == undefined){
				delete $scope.customData.address_bar;
			}
			$scope.customData.time_range = $("#bestTimes").val();
			if($scope.customData.time_range == undefined){
				delete $scope.customData.time_range;
			}
			
			console.log($scope.customData);
			console.log($scope.userFields);
		
		$.each($scope.customData, function(attr, value) {
			angular.forEach($scope.userFields, function(value1, key) {
				if(value1.key == attr){
					$scope.customList.push({
		   	  			key:attr,
		   	  			value:value,
		   	  			savecrm:value1.savecrm,
		   	  			displayGrid:value1.displayGrid,
		   	  			
					});
				} 
			});
		   });
		
		 
		
		console.log($scope.customList);
		$scope.vinData.specification.customData = $scope.customList;
		$scope.flagVal = true;
   }
   
}]);

angular.module('newApp')
.controller('EditVehicleCtrl', ['$filter','$scope','$http','$location','$routeParams','$upload','$route', function ($filter,$scope,$http,$location,$routeParams,$upload,$route) {
      
	$scope.userFields = [];
	$scope.customData = {};
      var ele = document.getElementById('loadingmanual');	
  	$(ele).hide();
	$scope.publishVehicle = function(id){
		   $http.get('/addPublicCar/'+id).success(function(data){
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Vehicle published SuccessFully",
					});
				   $location.path('/viewVehicles');
		   });
	   }
	
	
	$http.get('/getColl').success(function(response) {
		 $scope.collectionList = response[0];
		 console.log($scope.collectionList);
	});
	
	$http.get('/getCustomizationform/'+'Inventory').success(function(response) {
		 $scope.editInput = response;
		 //$scope.josnData = angular.fromJson(response.jsonData);
		 $scope.userFields = $scope.addFormField(angular.fromJson(response.jsonData));
		 console.log($scope.userFields);
		 $scope.user = {};
		});
	
	$scope.gridsterOpts = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
		    margins: [10, 10], // the pixel distance between each widget
		    outerMargin: true, // whether margins apply to outer edges of the grid
		    isMobile: false, // stacks the grid items if true
		    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
		    mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
		    minColumns: 6, // the minimum columns the grid must have
		    minRows: 1, // the minimum height of the grid, in rows
		    maxRows: 100,
		    defaultSizeX: 1, // the default width of a gridster item, if not specifed
		    defaultSizeY: 1, // the default height of a gridster item, if not specified
		    resizable: {
			       enabled: false,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, widget) {} // optional callback fired when item is finished resizing
			    },
		    /* minSizeX: 1, // minimum column width of an item
		    maxSizeX: null, // maximum column width of an item
		    minSizeY: 1, // minumum row height of an item
		    maxSizeY: null, // maximum row height of an item
		   */
			    draggable: {
				       enabled: true, // whether dragging items is supported
				       handle: '.my-class', // optional selector for resize handle
				       start: function(event, $element, widget) { }, // optional callback fired when drag is started,
				       drag: function(event, $element, widget) { }, // optional callback fired when item is moved,
				       stop: function(event, $element, widget) {
				    	   
				    	   if($(event.target).html() == 'Set Default' || $(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   for(var i=0;i<$scope.imageList.length;i++) {
				    		   delete $scope.imageList[i].description;
				    		   delete $scope.imageList[i].width;
				    		   delete $scope.imageList[i].height;
				    		   delete $scope.imageList[i].link;
				    	   }
				    	   $http.post('/savePosition',$scope.imageList)
					   		.success(function(data) {
					   			$.pnotify({
								    title: "Success",
								    type:'success',
								    text: "Position saved successfully",
								});
					   		});
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	$scope.imageList = [];
	$scope.isUpdated = false;
	$scope.vinData;
	$scope.makeList = [];
	$scope.modelList = [];
	$scope.trimList = [];
	$scope.labelList = [];
	$scope.madeInList = [];
	$scope.stereoList = [];
	$scope.driveTypeList = [];
	$scope.fuelTypeList = [];
	$scope.exteriorColorList = [];
	
	$scope.init = function() {
		$scope.isUpdated = false;
		$http.get('/getAllSites')
 		.success(function(data) {
 			$scope.siteList = data;
 		});
		
		$http.get('/getVehicleById/'+$routeParams.id)
		.success(function(data) {
			console.log(data);
			
			 $scope.vinData = data;
			 $scope.customData = $scope.vinData.specification.customMapData;
			 console.log($scope.customData);
			 if($scope.customData.time_range != undefined){
				 $("#bestTimes").val($scope.customData.time_range);
			 }
			 
			 if($scope.customData.address_bar != undefined){
				 $("#autocomplete").val($scope.customData.address_bar);
			 }
			 
			 
			 
			 $('#comingsoonDateEdit').val(data.specification.comingSoonDate);
			 if(data.specification.comingSoonFlag ==1){
				 $scope.master=true;
				 $scope.vinData.specification.commingSoonVehicle = true;
			 }
			//for publish button flag
				$http.get('/isCarPublic/'+$scope.vinData.specification.id).success(function(data){
					if(data == 'true'){
						$scope.isPublished = true;						
					}else{
						$scope.isPublished = false;
					}
				});
			   $('#modelSearch_value').val($scope.vinData.specification.model);
			   $('#makeSearch_value').val($scope.vinData.specification.make);
			   $('#trimSearch_value').val($scope.vinData.specification.trim_level);
			   $('#labelSearch_value').val($scope.vinData.specification.label);
			   
			   $('#madeInSearch_value').val($scope.vinData.specification.made_in);
			   $('#extColorSearch_value').val($scope.vinData.specification.extColor);
			   $('#stereoSearch_value').val($scope.vinData.specification.stereo);
			   $('#driveTypeSearch_value').val($scope.vinData.specification.drivetrain);
			 
			   $http.get('/getModelList/'+$scope.vinData.specification.make)
				.success(function(data) {
					$scope.modelList = data;
				});
			   
			   $http.get('/getTrimList/'+$scope.vinData.specification.model)
				.success(function(data) {
					$scope.trimList = data;
				});
			   
			 $http.get('/getPriceHistory/'+data.vin)
				.success(function(data) {
					$scope.priceHistory = data;
					angular.forEach($scope.priceHistory, function(value, key) {
						value.dateTime = $filter('date')(value.dateTime,"dd/MM/yyyy HH:mm:ss")
					});
					
				});
			 
				if($scope.vinData.specification.siteIds != null) {
					for(var i=0;i<$scope.vinData.specification.siteIds.length;i++) {
						for(var j=0;j<$scope.siteList.length;j++) {
							if($scope.vinData.specification.siteIds[i] == $scope.siteList[j].id) {
								$scope.siteList[j].flag = true;
							}
						}
					}
				}
				$scope.setDropZone();
				
				if($routeParams.temp == 'true'){
					$('#vImg').click();
					$scope.getImages();
				}
		});
		
		
	}
	
	   
	   $http.get('/getMakeList').success(function(data) {
			$scope.labelList = data.label;
			$scope.makeList = data.make;
			$scope.madeInList = data.madeIn;
			$scope.stereoList = data.stereo;
			$scope.driveTypeList = data.driveType;
			$scope.fuelTypeList = data.fuelType;
			$scope.exteriorColorList = data.exteriorColor;
		});
	   $scope.selectedMake = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.make = selectObj.title;
				$http.get('/getModelList/'+selectObj.title)
				.success(function(data) {
					$scope.modelList = data;
				});
			}
		};
		$scope.selectedModel = function (selectObj) {
				if(selectObj != undefined){
					$scope.vinData.specification.model = selectObj.title;
					$http.get('/getTrimList/'+selectObj.title)
					.success(function(data) {
						$scope.trimList = data;
					});
				}
		};
		$scope.selectedTrim = function (selectObj) {
				if(selectObj != undefined){
					$scope.vinData.specification.trim_level = selectObj.title;
				}
		};  
		$scope.selectedLabel = function (selectObj) {
				if(selectObj != undefined){
					$scope.vinData.specification.label = selectObj.title;
				}
		};
		$scope.selectedFuelType = function (selectObj) {
			if(selectObj != undefined){
				$scope.vinData.specification.fuelType = selectObj.title;
			}
	   };
	   $scope.fuelFocusOut = function(){
		   $scope.vinData.specification.fuelType = $('#fuelTypeSearch_value').val();
	   }
	   $scope.fuelTypeChange = function(){
		   $('#fuelTypeSearch_value').val($scope.vinData.specification.fuelType);
	   }
	
	var myDropzone;
	$scope.setDropZone = function() {
		myDropzone = new Dropzone("#dropzoneFrm",{
			   parallelUploads: 30,
			   headers: { "vinNum": $scope.vinData.specification.vin },
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   init:function () {
				   this.on("queuecomplete", function (file) {
				          
					   $scope.getImages();
				          $scope.$apply();
				      });
				   
				   this.on("complete", function() {
					   this.removeAllFiles();
				   });
			   }
		   });
	}
	
	
	   $scope.uploadFiles = function() {
		   Dropzone.autoDiscover = false;
		   myDropzone.processQueue();
		   
	   }
	
	$scope.getImages = function() {
		$scope.isUpdated = false;
		$http.get('/getImagesByVin/'+$scope.vinData.specification.vin)
		.success(function(data) {
			$scope.imageList = data;
		});
	}
	
	   $scope.setSiteId = function(id,flag) {
	 	  if(flag == true) {
	 		 $scope.vinData.specification.siteIds.push(id);
	 	  } 
	 	  if(flag == false) {
	 		 $scope.vinData.specification.siteIds.splice($scope.vinData.specification.siteIds.indexOf(id),1);
	 	  }
	 	  
	   };
	
	   var pdfFile;
		/*$scope.onPdfFileSelect = function($files) {
			pdfFile = $files;
		}*/
	   
		$scope.removePdf = function(){
			$http.get('/removeVehiclePdf/'+$routeParams.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Pdf remove successfuly",
				});
				$route.reload();
			});
		};
		
		
		$scope.vinData = {};
		
		$scope.dataShow = function(check){
			//vinData.specification.commingSoonVehicle
			//$scope.vinData.specification.price = 0;
			if(check == undefined){
				$('#comingsoonDateEdit').val('');
			}
			
			if(check == true){
				
				$('#comingsoonDateEdit').val('');
			}
			//$('#comingsoonDateEdit').val();
		}	
	   
	$scope.updateVehicle = function() {
		$scope.customList = [];
		console.log($("#autocomplete").val());
		   console.log($scope.vinData.specification);
		   $scope.customData.custName = $('#exCustoms_value').val();
			if($scope.customData.custName == undefined){
				delete $scope.customData.custName;
			}
			$scope.customData.address_bar = $("#autocomplete").val();
			if($scope.customData.address_bar == undefined){
				delete $scope.customData.address_bar;
			}
			$scope.customData.time_range = $("#bestTimes").val();
			if($scope.customData.time_range == undefined){
				delete $scope.customData.time_range;
			}
			
			console.log($scope.customData);
			console.log($scope.userFields);
		
		$.each($scope.customData, function(attr, value) {
			angular.forEach($scope.userFields, function(value1, key) {
				if(value1.key == attr){
					$scope.customList.push({
		   	  			key:attr,
		   	  			value:value,
		   	  			savecrm:value1.savecrm,
		   	  			displayGrid:value1.displayGrid,
		   	  			
					});
				} 
			});
		   });
		
		 
		
		console.log($scope.customList);
		$scope.vinData.specification.customData = $scope.customList;
		
		
		/*$scope.vinData.specification.model = $('#modelSearch_value').val();
		   $scope.vinData.specification.make = $('#makeSearch_value').val();
		   $scope.vinData.specification.trim_level = $('#trimSearch_value').val();
		   $scope.vinData.specification.label = $('#labelSearch_value').val();
		   
		   $scope.vinData.specification.made_in = $('#madeInSearch_value').val();
		   $scope.vinData.specification.extColor = $('#extColorSearch_value').val();
		   $scope.vinData.specification.stereo = $('#stereoSearch_value').val();
		   $scope.vinData.specification.drivetrain = $('#driveTypeSearch_value').val();*/
		   
		   if($scope.vinData.specification.price == null || $scope.vinData.specification.price == ""){
			   $scope.vinData.specification.price = 0;
		   }
		   
		   //var saveFlag = 0;
		   
		   /*if($scope.vinData.specification.commingSoonVehicle == true){
			   	$scope.vinData.specification.comingSoonFlag = 1;
				  $scope.vinData.specification.comingSoonDate = $('#comingsoonDateEdit').val();
				  saveFlag = 1;
			  }else{
				  $scope.vinData.specification.comingSoonFlag = 0;
				  if($scope.vinData.specification.price == 0){
					  $.pnotify({
			 			    title: "Error",
			 			    type:'success',
			 			    text: "Price fields required",
			 			});
			 			saveFlag = 0;
				  }else{
					  	saveFlag = 1;
				  }
			  }*/
		   
		   console.log($scope.vinData.specification);
		// if(saveFlag == 1){
			   
		  // if(($scope.vinData.specification.model != null && $scope.vinData.specification.model != "") && ($scope.vinData.specification.make != null && $scope.vinData.specification.make != " ")){
				if(pdfFile != undefined){
					$http.post('/updateVehicleById',$scope.vinData.specification)
					.success(function(data) {
						$scope.isUpdated = true;
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Vehicle updated successfuly",
						});
						
						$scope.vinData.specification.siteIds = null;
						$upload.upload({
				 	         url : '/updateVehicleByIdPdf/'+$scope.vinData.specification.id,
				 	         method: 'POST',
				 	         file:pdfFile,
				 	      }).success(function(data) {
				 	  			$http.get('/getPriceHistory/'+data.vin)
								.success(function(data) {
									$scope.priceHistory = data;
									angular.forEach($scope.priceHistory, function(value, key) {
										value.dateTime = $filter('date')(value.dateTime,"dd/MM/yyyy HH:mm:ss")
									});
									$route.reload();
								});
				 	      });
					});
			 	 }else{
			 		$http.post('/updateVehicleById',$scope.vinData.specification)
					.success(function(data) {
						$scope.isUpdated = true;
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Vehicle updated successfuly",
						});
						$http.get('/getPriceHistory/'+data.vin)
						.success(function(data) {
							$scope.priceHistory = data;
							angular.forEach($scope.priceHistory, function(value, key) {
								value.dateTime = $filter('date')(value.dateTime,"dd/MM/yyyy HH:mm:ss")
							});
							
						});
					});
			 	 }
		   /*}else{
			   $.pnotify({
				    title: "Success",
				    type:'Error',
				    text: "Please select all fields",
				});
		   }*/
	//}  
	}
	
	$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				$('#imgId'+i).css("border","3px solid");
				$('#imgId'+i).css("color","red");
			}
		}
	});
	
$scope.setAsDefault = function(image,index) {
		
		for(var i=0;i<$scope.imageList.length;i++) {
			if($scope.imageList[i].defaultImage == true) {
				$http.get('/removeDefault/'+$scope.imageList[i].id+'/'+image.id)
				.success(function(data) {
				});
				$('#imgId'+i).removeAttr("style","");
				$scope.imageList[i].defaultImage = false;
				image.defaultImage = true;
				$('#imgId'+index).css("border","3px solid");
				$('#imgId'+index).css("color","red");
				break;
			}
		}
		
		if(i == $scope.imageList.length) {
			$http.get('/setDefaultImage/'+image.id)
			.success(function(data) {
			});
			
			image.defaultImage = true;
			$('#imgId'+index).css("border","3px solid");
			$('#imgId'+index).css("color","red");
		}
		
		
	}
	
	$scope.deleteImage = function(img) {
		$http.get('/deleteImage/'+img.id)
		.success(function(data) {
			$scope.imageList.splice($scope.imageList.indexOf(img),1);
		});
		
	}
	
	$scope.showFullImage = function(image) {
		$scope.imageId = image.id;
		$scope.imageName = image.imgName;
	}
	
	$scope.updateVehicleStatus = function(){
		   $http.get('/updateVehicleStatus/'+$routeParams.id+'/'+"Sold")
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Status saved successfully",
				});
			});
	   }
	
	$scope.deleteVehicle = function(){
		 $('#deleteModal').click();
		   
	   }
	
	$scope.deleteVehicleRow = function() {
		$http.get('/deleteVehicleById/'+$routeParams.id)
		.success(function(data) {
			$location.path('/addVehicle');
		});
	}
	
	var file;
	$scope.onFileSelect = function($files) {
		file = $files;
	}
	
	$scope.uploadAudio = function() {
		$upload.upload({
            url : '/uploadSoundFile',
            method: 'post',
            file:file,
            data:{"vinNum":$scope.vinData.specification.vin}
        }).success(function(data, status, headers, config) {
            $scope.getAllAudio();
            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
        });
	}
	
	$scope.confirmFileDelete = function(id) {
		$scope.audioFileId = id;
		$('#deleteModal2').click();
	}
	
	$scope.deleteAudioFile = function() {
		$http.get('/deleteAudioFile/'+$scope.audioFileId)
		.success(function(data) {
			$scope.getAllAudio();
		});
	}
	
	$scope.getAllAudio = function() {
		$http.get('/getAllAudio/'+$scope.vinData.specification.vin)
		.success(function(data) {
			$scope.audioList = data;
		});
	}
	$scope.vData = {};
	$scope.videoData={};
	$scope.getVirtualTourData = function() {
		$http.get('/getVirtualTour/'+$scope.vinData.specification.id)
		.success(function(data) {
			
			$scope.vData = data.virtualTour;
			$scope.videoData = data.video;
		});
	}
	
	$scope.saveVData = function() {
		
		$scope.vData.vin = $scope.vinData.specification.vin;
		$scope.vData.vehicleId = $scope.vinData.specification.id;
		$http.post('/saveVData',$scope.vData)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
		});
	}
	
	
	$scope.saveVideoData = function() {
		
		$scope.videoData.vin = $scope.vinData.specification.vin;
		$scope.videoData.vehicleId = $scope.vinData.specification.id;
		$http.post('/saveVideoData',$scope.videoData)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
		});
	}
	
	
	$scope.editImage = function(image) {
		$location.path('/cropImage/'+image.id+'/'+$routeParams.id);
	}
	
}]);	
	