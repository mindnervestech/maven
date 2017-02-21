angular.module('newApp')
.controller('addManufacturersCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$scope.newProd = false;
	var files =[];
	$scope.addProduct = {};
	$scope.sectColl = "null";
	$scope.sect = {};
	$scope.status= function(sts){
		$scope.collection = sts;
	}
	
	$scope.chengeSection = function(){
		$scope.sectColl= "tr";
		$http.get('/getAllProduct/'+$scope.sect.id)
		.success(function(data) {
			$scope.sectionCollection = data;
		});
	}
	
	$scope.setStatusvalue = function(value){
		$scope.statusValue = value;
	}
	var date = new Date();
	$http.get('/getAllProduct/'+'publish'+'/'+date)
	.success(function(data) {
		$scope.childManu = data;
	});
	
	$http.get('/getAllProductData/'+'publish')
	.success(function(data) {
		$scope.childManuData = [];
		var mainColl = localStorage.getItem('mainCollection');
		angular.forEach(data, function(value, key) {
			if(value.mainCollection != null){
				if(value.mainCollection.id == JSON.parse($scope.mainCollection).id){
					$scope.childManuData.push(value);
				}
			}
		});
	});
	
		
	$http.get('/getAllInventoryData').success(function(data) {
		$scope.mainCollections = data;
		var mainColl = localStorage.getItem('mainCollection');
		if(mainColl != undefined){
			$scope.mainCollection = mainColl;
		}
		$scope.mainCollectionData = JSON.parse($scope.mainCollection).collection
		//setTimeout(function(){$scope.$apply();},100);
	});
	
	$scope.setOpenPopup = function(){
		$('#cancelModal').modal('show');
	}
	$scope.cancelYes = function(){
		$location.path('/viewInventory');
	}
	
	$scope.saveManufacturers = function(){
		if($scope.mainCollection != null || $scope.mainCollection != undefined){
			$scope.addProduct.mainCollection = JSON.parse($scope.mainCollection);
			delete $scope.addProduct.mainCollection.locations.createdDate;
		}
		//$("#submit").attr("disabled", true);
		$scope.addProduct.publicStatus = $scope.statusValue;
		 $scope.addProduct.id = $('#sectionId').val();
		 if($scope.addProduct.newFlag != true)
			 $scope.addProduct.newFlag=false;

		 if($scope.addProduct.hideWebsite == true){
			 $scope.addProduct.hideWebsite = 1;
		 }else{
			 $scope.addProduct.hideWebsite = 0;
		 }
		 
		 if($scope.addProduct.subhideWebsite == true){
			 $scope.addProduct.subhideWebsite = 1;
		 }else{
			 $scope.addProduct.subhideWebsite = 0;
		 }
		 
		 if(logofile == undefined){
			$http.post('/saveProduct',$scope.addProduct)
	   		.success(function(data) {
	   			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Update successfully",
				});
	   			$("#submit").attr("disabled", false);
	   			if($scope.statusValue != "draft"){
	   				$location.path('/manufacturersImages/'+data.id);
	   			}else{
	   				$location.path('/viewInventory');
	   				
	   			}
	   			
	   		});
		}else if(logofile != undefined){
			if($scope.addProduct.userData == null)
				delete $scope.addProduct.userData;
			$scope.addProduct.id = 0;
		 $upload.upload({
            url : '/saveProduct',
            method: 'POST',
            file:logofile,
            data:$scope.addProduct
         }).success(function(data) {
   			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Your Progress has been Saved",
			});
   			$("#submit").attr("disabled", false);
   			
   			if($scope.statusValue != "draft"){
   				$location.path('/manufacturersImages/'+data.id);
   			}else{
   				$location.path('/addManufacturers');
   				$scope.addProduct= [];
   			}
   			
   		 });
		}else if(logofile == undefined){
			if($scope.addProduct.userData == null)
				delete $scope.addProduct.userData;
			 $upload.upload({
	            url : '/saveProduct',
	            method: 'POST',
	            file:cadfile,
	            data:$scope.addProduct
	         }).success(function(data) {
	        	 $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
	   			$("#submit").attr("disabled", false);
	   			if($scope.statusValue != "draft"){
	   				$location.path('/manufacturersImages/'+data.id);
	   			}else{
	   				$location.path('/viewInventory');
	   			}
	   				
	   				
	   			
	   		 });
			}else if(logofile != undefined){
				
				
				 $upload.upload({
		            url : '/saveProduct',
		            method: 'POST',
		            fileFormDataName: names,
		            file:files,
		            //file:files,
		            
		            //file:files,
		            //file: files
		            //file:$scope.files,
		            //file:logofile,cadfile,
		            data:$scope.addProduct
		         }).success(function(data) {
		        	 $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Update successfully",
						});
		   			$("#submit").attr("disabled", false);
		   			if($scope.statusValue != "draft"){
		   				$location.path('/manufacturersImages/'+data.id);
		   			}else{
		   				$location.path('/viewInventory');
		   			}
		   			
		   		 });
				}
		 
	}
	 var logofile;
	 var names=[];
		
		$scope.onLogoFileSelect = function($files) {
			logofile = $files;
			files[0] = $files[0];
			names[0]= "logoFile";
		}
	var cadfile;	
		$scope.onCADFileSelect = function($files) {
			cadfile = $files;
			files[1] = $files[0];
			names[1] = "cadFile";
		}
	
}]);	

angular.module('newApp')
.controller('ProductImagesCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$scope.tempDate = new Date().getTime();
	$scope.gridsterOpts = {
		    columns: 3, // the width of the grid, in columns
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
		    minColumns: 3, // the minimum columns the grid must have
		    minRows: 1, // the minimum height of the grid, in rows
		    maxRows: 1,
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
				    		   delete $scope.imageList[i].findNewId;
				    		   delete $scope.imageList[i].findNewId;
				    		   delete $scope.imageList[i].findNewId;
				    		   
				    	   }
				    	  
				    	   $scope.newObjArray = [];
				    	   angular.forEach($scope.imageList, function(value, key) {
				    		   $scope.newObj = {};
				    		   $scope.newObj.id = value.id;
				    		   $scope.newObj.srNumber = value.col;
				    		   $scope.newObjArray.push($scope.newObj);
				    	   });
				    	   $http.post('/savePosition',$scope.newObjArray)
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
	
	var myDropzone = new Dropzone("#dropzoneFrm",{
		   parallelUploads: 30,
		   headers: { "id": $routeParams.id },
		   acceptedFiles:"image/*",
		   addRemoveLinks:true,
		   autoProcessQueue:false,
		   init:function () {
			   this.on("queuecomplete", function (file) {
			         
			      });
			   this.on("complete", function() {
				   this.removeAllFiles();
				   $scope.init();
			   });
		   }
	   });
	   $scope.uploadFiles = function() {
		   if($scope.imageList.length < 3){
			   Dropzone.autoDiscover = false;
			   myDropzone.processQueue();
		   }else{
			   $('#msgModal').modal('show');
		   }
		
	   }
	   $scope.uploadFilesDreft = function(){
		   $scope.object = {};
		   $scope.object.id = $routeParams.id;
		   $scope.object.publicStatus = "noStatus";
		   $http.post('/updateProductInfo',$scope.object)
	   		.success(function(data) {
	   			
	   		});
		   $scope.uploadFiles();
	   }
	   
	   $scope.uploadImagesFiles = function(){
		   Dropzone.autoDiscover = false;
		   myDropzone.processQueue();
		   $location.path('/viewInventory');
	   };
	   

		$scope.setAsDefault = function(image,index) {
			
			for(var i=0;i<$scope.imageList.length;i++) {
				if($scope.imageList[i].id == image.id) {
					$http.get('/removeDefaultProduct/'+image.id)
					.success(function(data) {
					});
					//$('#'+image.id).removeAttr("style","");
					//$scope.imageList[i].defaultImage = false;
					image.defaultImage = true;
					$('#'+image.id).css("border","3px solid");
					$('#'+image.id).css("color","red");
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Default image set successfully",
					});
					//break;
				}else{
					$('#'+$scope.imageList[i].id).css("color","white");
					$('#'+$scope.imageList[i].id).css("border","0px solid");
					$scope.imageList[i].defaultImage = false;
					image.defaultImage = false;
				}
			}
			
			/*if(i == $scope.imageList.length) {
				$http.get('/setDefaultImage/'+image.id)
				.success(function(data) {
				});
				
			}*/
		}
		
		$scope.addImageTitle = function(image,index){
			$scope.manufactureImage = image;
			$('#addTitle').modal('show');
		}
		
		$scope.saveImageTitle = function(imageObj){
			$http.post('/saveImageCollectionTitle',imageObj)
			.success(function(data) {
				$scope.manufactureImage = {};
			});
		}
		
		$scope.deleteImage = function(img) {
			$http.get('/deleteCollectionById/'+img.id)
			.success(function(data) {
				$scope.imageList.splice($scope.imageList.indexOf(img),1);
			});
			
		}
		
		$scope.showFullImage = function(image) {
			$scope.imageId = image.id;
			$scope.imageName = image.imgName;
		}
		
		$scope.editImage = function(image) {
			//$location.path('/cropImage/'+image.id);
			$location.path('/cropImage/'+image.id+'/'+$routeParams.id);
			
		}
		
	   
	   $scope.showDefaultMsg = 0;
	   $scope.valuepul = 0;
	   $scope.init = function() {
		   $scope.showDefaultMsg = 0;
			$http.get('/getImagesByCollection/'+$routeParams.id)
			.success(function(data) {
				angular.forEach(data, function(value, key) {
					$scope.valuepul = $scope.valuepul + 1;
					if(value.defaultImage == true){
						$scope.showDefaultMsg =1;
					}
				});
				if($scope.valuepul == 0){
					$scope.showDefaultMsg = 1;
				}
				/*if($scope.showDefaultMsg == 0 ){
					 $('#msgModal').modal();
				}*/
				if(data.length != 3 ){
					 $('#msgModal').modal();
				}
				$scope.imageList = data;
			});
	   };
	   
		$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
			for(var i=0;i<$scope.imageList.length;i++) {
				if($scope.imageList[i].defaultImage == true) {
					$('#'+$scope.imageList[i].id).css("border","3px solid");
					$('#'+$scope.imageList[i].id).css("color","red");
				}else{
					$('#'+$scope.imageList[i].id).css("border","0px solid");
					$('#'+$scope.imageList[i].id).css("color","white");
				}
			}
		});
}]);


angular.module('newApp')
.controller('ImageCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {


	
	$scope.coords = {};
	$scope.imgId = "/getImage/"+$routeParams.id+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getImageById/'+$routeParams.id)
			.success(function(data) {
				imageW = data.width;
				imageH = data.height;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				$scope.image = data;
				$('#target').css({
					height: Math.round((727)/(data.col/data.row)) + 'px'
				});
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio:   data.width/data.height
				        
				      
				    },function(){
				    	var bounds = this.getBounds();
				        boundx = bounds[0];
				        boundy = bounds[1];
				        //$('#preview')
				    });
			});
		 
	}
		 function showCoords(c)
		    {
			    var rx = 200 / c.w;
				var ry = 200*(imageH/imageW) / c.h;
				
				$('#preview-container').css({
					width: Math.round(200) + 'px',
					height: Math.round(200*(imageH/imageW)) + 'px'
				});
				
				$('#preview').css({
					width: Math.round(rx * boundx) + 'px',
					height: Math.round(ry * boundy) + 'px',
					marginLeft: '-' + Math.round(rx * c.x) + 'px',
					marginTop: '-' + Math.round(ry * c.y) + 'px'
				});
			 
				 $scope.coords.x = c.x;
				 $scope.coords.y = c.y;
				 $scope.coords.x2 = c.x2;
				 $scope.coords.y2 = c.y2;
				 $scope.coords.w = c.w;
				 $scope.coords.h = c.h;
		    };
		 
		$scope.saveImage = function() {
			$scope.coords.imageId = $routeParams.id;
			
			$http.post('/editImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Saved successfully",
				});
				$location.path('/addProductImages/'+$routeParams.pId);
			});
		}        
}]);

angular.module('newApp')
.controller('editProductsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$scope.collection="null";
	$scope.cId=null;
	$scope.newProd = false;
	var date = new Date();
	$http.get('/getAllProduct/'+'publish'+'/'+date)
	.success(function(data) {
		$scope.childManu = data;
	});
	$http.get('/getAllProductData/'+'publish')
	.success(function(data) {
		//$scope.childManuData = data;
		$scope.childManuData = [];
		var mainColl = localStorage.getItem('mainCollection');
		angular.forEach(data, function(value, key) {
			if(value.mainCollection != null){
				if(value.mainCollection.id == JSON.parse(localStorage.getItem('mainCollection')).id){
					$scope.childManuData.push(value);
				}
			}
		});
	});
			
	$http.get('/getAllInventoryData').success(function(data) {
		$scope.mainCollections = data;
	});
	
	$http.get('/getProductData/'+$routeParams.id)
	.success(function(data) {
			
			if(data.subhideWebsite == 1){
				data.subhideWebsite = true;
			}else{
				data.subhideWebsite = false;
			}
			
			if(data.hideWebsite == 1){
				data.hideWebsite = true;
			}else{
				data.hideWebsite = false;
			}
			
			$scope.productData = data;
			setTimeout(function(){
				if(data.mainCollection != null){
					$scope.mainCollection = JSON.stringify(data.mainCollection);
				}
				if(data.parentId != null)
					$scope.productData.parentId = data.parentId.toString();
				$scope.$apply();
			}, 10);
	});
	
	$scope.status= function(sts){
		$scope.collection = sts;
	}

	$http.get('/getList').success(function(data) {
		$scope.CollectionList = data;
	});
	
	$scope.collectionList = function(){
		$http.get('/getColl')
		.success(function(data) {
			$scope.readyCollection = data[0];
				/*angular.forEach($scope.readyCollection, function(value, index){
				
					if(value.collectionId == $scope.cId){
						$scope.collection = 'readyMade';
					}
						
				});
*/			
			$scope.proCollection = data[1];
				/*angular.forEach($scope.proCollection, function(value, index){
					
					if(value.collectionId == $scope.cId){
						$scope.collection = "product";
					}
						
				});*/
			$scope.SectionCollection = data[2];
				
		});
		
	}

	$scope.setStatusvalue = function(value){
		$scope.statusValue = value;
	}
	
	var logofile = null;
	var cadfile = null;
	var names = [];
	var files =[];
	$scope.updateProduct = function(){
		if($scope.mainCollection != null || $scope.mainCollection != undefined){
			$scope.productData.mainCollection = JSON.parse($scope.mainCollection);
			delete $scope.productData.mainCollection.locations.createdDate;
		}
		$scope.productData.id = $routeParams.id;
		$scope.productData.publicStatus = $scope.statusValue;
		$scope.productData.collectionId = 0;
		delete $scope.productData.pageViewCount;
		if($scope.productData.parentId == null)
			delete $scope.productData.parentId;
		if($scope.productData.userData == null)
			delete $scope.productData.userData;
		
		
		if($scope.productData.hideWebsite == true){
			 $scope.productData.hideWebsite = 1;
		 }else{
			 $scope.productData.hideWebsite = 0;
		 }
		 
		 if($scope.productData.subhideWebsite == true){
			 $scope.productData.subhideWebsite = 1;
		 }else{
			 $scope.productData.subhideWebsite = 0;
		 }
		
		if(logofile != null){
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
		        	if($scope.productData.publicStatus != "noStatus"){
		        		$location.path('/manufacturersImages/'+$routeParams.id);
		   			}else{
		   				$location.path('/viewInventory');
		   			}
		        	//$location.path('/manufacturersImages/'+$routeParams.id);
		   		});	
		
		}else if(logofile != undefined){
		 $upload.upload({
			 url : '/updateProduct',
	         method: 'POST',
            fileFormDataName: names,
            file:files,
            data:$scope.productData
         }).success(function(data) {
	        	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Product Update successfully",
				});
	        	if($scope.productData.publicStatus != "noStatus"){
	        		$location.path('/manufacturersImages/'+$routeParams.id);
	   			}else{
	   				$location.path('/viewInventory');
	   			}
	        	//$location.path('/manufacturersImages/'+$routeParams.id);
	   		});
		}
		else{
		$scope.productData.id = $routeParams.id;
		$http.post('/updateProductInfo',$scope.productData)
   		.success(function(data) {
   			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Product Update successfully",
			});
   			if($scope.productData.publicStatus != "noStatus"){
   				$location.path('/manufacturersImages/'+$routeParams.id);
   			}else{
   				$location.path('/viewInventory');
   			}
   			//$location.path('/manufacturersImages/'+$routeParams.id);
   		});
		}	
	}
	
	$scope.download = function(){
		$scope.productData.id = $routeParams.id;
		$.fileDownload('/downloadFile',
				{	  
				  httpMethod : "POST",
				  data : {
				  path : $scope.productData.filePath
				  }
				}).done(function(e, response)
				{
				}).fail(function(e, response)
				{
				});
	}
	$scope.downloadCad = function(){
		$scope.productData.id = $routeParams.id;
		$.fileDownload('/cadFileDownload',
				{	  
				  httpMethod : "POST",
				  data : {
				  path : $scope.productData.cadfilePath
				  }
				}).done(function(e, response)
				{
				}).fail(function(e, response)
				{
				});
	}
	

		$scope.goToImages = function(){
			$location.path('/manufacturersImages/'+$routeParams.id);
		}
		
		$scope.onLogoFileSelect = function($files) {
			logofile = $files;
			$scope.productData.fileName = logofile[0].name;
			files[0] = $files[0];
			names[0]= "logoFile";
		}
		
		$scope.deleteVehicle = function(row){
			$('#modal-basic').modal('show');
			   $scope.rowDataVal = row;
		   }
		   
		   $scope.deleteVehiclePer = function(row){
			   $('#modal-basic1').modal('show');
			   $scope.rowDataVal = row;
		   }
		   
		    
		   
		   $scope.deleteVehicleRow = function() {
			   
			   $http.get('/deleteVehicleById/'+$scope.rowDataVal.id).success(function(data) {
			   
				   $location.path('/viewInventory');
				});
		   }
		   		   
		$scope.deleteVehicleRowPer = function() {
			   
			 $http.get('/deleteVehicleByIdPer/'+$scope.rowDataVal.id).success(function(data) {
				   if(data == "Error"){
					   $.pnotify({
						    title: "Error",
						    type:'success',
						    text: "Can't Delete parent Manufacturer",
						});
				   }else{
					   $location.path('/viewInventory');
				   }
				});
		   }
		
		$scope.onCadFileSelect = function($files) {
			cadfile = $files;
			$scope.productData.cadfileName = cadfile[0].name;
			files[1] = $files[0];
			names[1] = "cadFile";
		}


}]);
