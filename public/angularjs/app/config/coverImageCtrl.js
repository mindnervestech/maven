angular.module('newApp')
	.controller('CoverImageCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$route', function ($scope,$http,$location,$filter,$routeParams,$upload,$route) {
		
		$scope.tempDate = new Date().getTime();
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
					       enabled: false, // whether dragging items is supported
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
					    	 /*  $http.post('/savePosition',$scope.imageList)
						   		.success(function(data) {
						   			$.pnotify({
									    title: "Success",
									    type:'success',
									    text: "Position saved successfully",
									});
						   		});*/
					    	   
					       } // optional callback fired when item is finished dragging
					    }
			};
		
		var myDropzone = new Dropzone("#dropzoneFrm",{
			   parallelUploads: 1,
			   headers: { "id": $routeParams.collId },
			   acceptedFiles:"image/*",
			   addRemoveLinks:true,
			   autoProcessQueue:false,
			   maxFiles:1,
			   accept: function(file, done) {
				   file.rejectDimensions = function() { done("Invalid dimension."); };
				   file.acceptDimensions = done;
				  
				  },
			   init:function () {
				   this.on("queuecomplete", function (file) {
					     
					   $scope.init();
				          $scope.$apply();
				      
				      });
				   this.on("thumbnail", function(file) {
					   		console.log(file);
					   		if (file.width < 1440 || file.height < 920) {
					   			
					   			$('#sliderMsg').modal('show');
					   			file.rejectDimensions();
						     }
					   		
						     else {
						        file.acceptDimensions();
						      }
					    });
				   this.on("complete", function() {
					   this.removeAllFiles();
					   //$scope.init();
					   //$route.reload();
				   });
			   }
		   });
		
		   $scope.pathId = {};
		   $scope.uploadFiles = function() {
			   
			   if($routeParams.collId != 0){
				   console.log($scope.imageList);
				   angular.forEach($scope.imageList, function(value, key) {
					   $scope.pathId = value.path;
				   });
				   
				   if($scope.pathId == null){
					   Dropzone.autoDiscover = false;
					   myDropzone.processQueue();
					   $scope.init();
					   if($routeParams.entry == "menu"){
						   $location.path('/viewInventory');
					   }else if($routeParams.entry == "setting"){
						   $location.path('/inventoryManagement');
					   }
					   //$location.path('/inventoryManagement')
				   }else{
					   console.log("cover image already exit");
					   $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Cover image already exit. If you want to add cover image before remove existing image",
						});
				   }
			   }else{
				   $.pnotify({
					    title: "Success",
					    type:'success',
					    text: "First Save Main Collection Title",
					});
			   }
			  
			   //$location.path('/viewInventory');
		   }
		   
		   $scope.uploadFilesDreft = function(){
			   $scope.object = {};
			   $scope.object.id = $routeParams.id;
			   $scope.object.publicStatus = "draft";
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
						console.log($scope.imageList[i]);
					}
				}
				
				/*if(i == $scope.imageList.length) {
					$http.get('/setDefaultImage/'+image.id)
					.success(function(data) {
						console.log('success');
					});
					
				}*/
			}
			
			$scope.addImageTitle = function(image,index){
				$scope.manufactureImage = image;
				$('#addTitle').modal('show');
			}
			
			$scope.saveImageTitle = function(imageObj){
				console.log(imageObj);
				$http.post('/saveImageTitle',imageObj)
				.success(function(data) {
					console.log('success');
					$scope.manufactureImage = {};
				});
			}
			
			$scope.deleteImage = function(img) {
				angular.forEach(img, function(value, key) {
					$scope.imageId = value.id;
				});
				$http.get('/deleteCollectionImageById/'+$scope.imageId)
				.success(function(data) {
					console.log('success');
					$scope.imageList.splice($scope.imageList.indexOf(img),1);
					$scope.imageList = [];
					 $scope.init();
				});
				
			}
			
			$scope.showFullImage = function(image) {
				$scope.imageId = image.id;
				$scope.imageName = image.imgName;
			}
			
			$scope.editImage = function(image) {
				console.log($routeParams.collId);
				angular.forEach(image, function(value, key) {
						$scope.imageId = value.id;
					});
				console.log($scope.imageId);
				$location.path('/collectCropImage/'+$scope.imageId+'/'+$routeParams.collId);
				
			}
			
			console.log($routeParams.collFlag);
			$scope.colllectionFlag = $routeParams.collFlag;
		   $scope.showDefaultMsg = 0;
		   $scope.valuepul = 0;
		   $scope.init = function() {
			   $scope.showDefaultMsg = 0;
				$http.get('/getImagesByCollections/'+$routeParams.collId)
				.success(function(data) {
					console.log(data);
					angular.forEach(data, function(value, key) {
						$scope.valuepul = $scope.valuepul + 1;
						if(value.defaultImage == true){
							$scope.showDefaultMsg =1;
						}
					});
					if($scope.valuepul == 0){
						$scope.showDefaultMsg = 1;
					}
					if($scope.showDefaultMsg == 0 ){
						 $('#msgModal').modal();
					}
					if(data.length < 2 ){
						 $('#msgModal').modal();
					}
					$scope.imageList = data;
				});
				$scope.getCollectionById();
		   };
		   
			$scope.$on('ngRepeatFinished', function(ngRepeatFinishedEvent) {
				console.log($scope.imageList);
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
			
			
			$scope.collData = {};
			$scope.editMainCollect = function(collection){
				$scope.collData.collection = collection;
				$scope.collData.id = $routeParams.collId;
				console.log($scope.collData);
				 $http.post('/updateCollById',$scope.collData)
			   		.success(function(data) {
			   			console.log(data);
			   			$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Collection updated successfully",
						});
			   			if($routeParams.collId == 0){
			   				$location.path('/coverImage/'+data.id+'/'+0+"/"+$routeParams.entry);
			   				
			   			}
			   			
			   		})
			}
			
			$scope.getCollectionById = function(){
				$http.get('/getCollectionById/'+$routeParams.collId)
				.success(function(data) {
					console.log(data);
					$scope.collection = data.collection;
				});
			}
			
			$scope.collec = {};
			$scope.saveMainCollect = function(coll){
				console.log(coll);
				$scope.collec.enableInven = true;
				$scope.collec.hideWebsite = false;
				$scope.collec.collection = coll;
				$http.post('/saveCollections',$scope.collec)
			   		.success(function(data) {
			   			$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Collection saved successfully",
						});
			   		})
			}
	}]);