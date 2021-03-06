angular.module('newApp')
	.controller('ProductImgCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
		
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
			   Dropzone.autoDiscover = false;
			   myDropzone.processQueue();
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
						$http.get('/removeDefaultImageProd/'+image.id)
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
				$http.post('/saveImageTitle',imageObj)
				.success(function(data) {
					$scope.manufactureImage = {};
				});
			}
			
			$scope.deleteImage = function(img) {
				$http.get('/deleteProductById/'+img.id)
				.success(function(data) {
					$scope.imageList.splice($scope.imageList.indexOf(img),1);
				});
				
			}
			
			$scope.showFullImage = function(image) {
				$scope.imageId = image.id;
				$scope.imageName = image.imgName;
			}
			
			$scope.editImage = function(image) {
				$location.path('/cropImage/'+image.id+'/'+$routeParams.id);
			}
			
		   
		   $scope.showDefaultMsg = 0;
		   $scope.valuepul = 0;
		   $scope.init = function() {
			   $scope.showDefaultMsg = 0;
				$http.get('/getImagesByProduct/'+$routeParams.id)
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
					if($scope.showDefaultMsg == 0 ){
						 $('#msgModal').modal();
					}
					if(data.length < 2 ){
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