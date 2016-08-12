angular.module('newApp')
.controller('PhotoUploadCtrl', ['$scope','$routeParams','$location', function ($scope,$routeParams,$location) {
   var myDropzone = new Dropzone("#dropzoneFrm",{
	   parallelUploads: 30,
	   headers: { "vinNum": $routeParams.num },
	   acceptedFiles:"image/*",
	   addRemoveLinks:true,
	   autoProcessQueue:false,
	   init:function () {
		   this.on("queuecomplete", function (file) {
		          
		          $location.path('/managePhotos/'+$routeParams.num);
		          $scope.$apply();
		      });
		   this.on("complete", function() {
			   this.removeAllFiles();
		   });
	   }
   });
   $scope.uploadFiles = function() {
	   Dropzone.autoDiscover = false;
	   myDropzone.processQueue();
	   
   }
   
}]);


angular.module('newApp')
.controller('ManagePhotoCtrl', ['$scope','$routeParams','$location','$http','$timeout', function ($scope,$routeParams,$location,$http,$timeout) {
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
				       start: function(event, $element, widget) {}, // optional callback fired when drag is started,
				       drag: function(event, $element, widget) {}, // optional callback fired when item is moved,
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
	$scope.init = function() {
		 $timeout(function(){
			$http.get('/getImagesByVin/'+$routeParams.num)
			.success(function(data) {
				$scope.imageList = data;
			});
		 }, 3000);
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
	
	$scope.editImage = function(image) {
		$location.path('/cropImage/'+image.id);
	}
	
}]);


angular.module('newApp')
.controller('SliderCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = "/getSliderImage/"+$routeParams.id+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getSliderImageDataById/'+$routeParams.id)
			.success(function(data) {
				imageW = data.width;
				imageH = data.height;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				$('#target').css({
					height: Math.round((727)/(data.col/data.row)) + 'px'
				});
				
				$scope.image = data;
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
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
		 
		   
		    
		$scope.saveImage = function(image) {
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editSliderImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "All changed has been saved",
				});
				$location.path('/homePage');
				$scope.$apply();
			});
		}    
		 
		
}]);



angular.module('newApp')
.controller('CoverCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/aboutUsCoverImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		
		
		 $http.get('/getCoverDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
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
			 
			    console.log("??????????");
			    console.log(c);
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
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editCovrImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/siteAboutUs');
				//$scope.$apply();
			});
		}    
		 
		
}]);


angular.module('newApp')
.controller('VehicleCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/vehicleProfileImageByIdForCrop/"+$routeParams.id+"/"+$routeParams.makeValue+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		$scope.makeValue=$routeParams.makeValue;	
		
		 $http.get('/getVehicleProfileDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
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
			 
			    console.log("??????????");
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
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
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editVehicleProfileImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				console.log("/vehicleProfile/"+$scope.makeValue);
				//$location.path('/vehicleProfile/'+$scope.makeValue);
				//$scope.$apply();
			});
		}    
		 
		
}]);



angular.module('newApp')
.controller('WarrantyCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	console.log("jjjjhhh");
	console.log($routeParams.findById);
	$scope.imgId = "/warrantyImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		
		
		 $http.get('/getWarDataById/'+$routeParams.id)
			.success(function(data) {
				
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
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
			 
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
			    
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
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editWarImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/warranty');
				//$scope.$apply();
			});
		}    
		 
		
}]);


angular.module('newApp')
.controller('BlogCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/blogImageById/"+$routeParams.findById+"/full?d="+ Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.showErrorMsg = 0;
	$scope.init = function() {
		
		
		 $http.get('/getBlogDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
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
			 
			    console.log("??????????");
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
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
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editBlogImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/blog');
				//$scope.$apply();
			});
		}    
		 
		
}]);




angular.module('newApp')
.controller('CompareCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/compareImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		
		
		 $http.get('/getCompareDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: data.width/data.height
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
			 
			    console.log("??????????");
			    console.log(c);
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
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editCompareImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/comparision');
				//$scope.$apply();
			});
		}    
		 
		
}]);



angular.module('newApp')
.controller('ContactCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {
	$scope.coords = {};
	$scope.imgId = "/contactImageById/"+$routeParams.findById+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		
		
		 $http.get('/getContactDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
			//	$scope.thumbPath=data.thumbPath;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				 $('#target').css({
					 width: Math.round(727) + 'px',
					 height: Math.round(727*(imageH/imageW)) + 'px'
					 });
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect:   [ 0, 0, data.width, data.height ],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
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
			 
			    console.log("??????????");
			    console.log(c);
			    if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
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
		    
		$scope.saveImage = function(image) {
			console.log(image);
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editContactImage',$scope.coords)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Croped Image has been saved",
				});
				
				$location.path('/contactUs');
				//$scope.$apply();
			});
		}    
		 
		
}]);


angular.module('newApp')
.controller('FeaturedCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = "/getFeaturedImage/"+$routeParams.id+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		 $http.get('/getFeaturedImageDataById/'+$routeParams.id)
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
				        aspectRatio: data.width/data.height
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
		 
		$scope.saveImage = function(image) {
			$scope.coords.imageId = $routeParams.id;
			$scope.coords.imgName = image.imgName;
			$scope.coords.description = image.description;
			$scope.coords.link = image.link;
			
			$http.post('/editFeaturedImage',$scope.coords)
			.success(function(data) {
				$location.path('/homePage');
				$scope.$apply();
			});
		}    
		 
		
}]);


angular.module('newApp')
.controller('InventoryCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {

	$scope.coords = {};
	$scope.imgId = "/getInventoryImage/"+$routeParams.findById+"/"+$routeParams.vType+"/full?d=" + Math.random();
	var imageW, imageH, boundx, boundy;
	$scope.minImgheight;
	$scope.minImgwidth;
	$scope.init = function() {
		 $http.get('/getInventoryImageDataById/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				imageW = data.col;
				imageH = data.row;
				
				$scope.minImgheight = data.height;
				$scope.minImgwidth = data.width;
				$('#set-height').val(data.height);
				$('#set-width').val(data.width);
				
				$scope.image = data;
				
				$('#target').css({
					width: Math.round(727) + 'px',
					height: Math.round(727*(imageH/imageW)) + 'px'
				});
				
				    $('#target').Jcrop({
				        onSelect: showCoords,
				        onChange: showCoords,
				        setSelect: [ 0, 0, data.width, data.height],
				        minSize:[data.width,data.height],
				        allowSelect: false,
				        trueSize: [data.col,data.row],
				        aspectRatio: 4/1
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
			 if(c.x < 0 || c.y < 0){
			    	$scope.showErrorMsg = 1;
			    }
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
			console.log($scope.coords);
			$scope.coords.imageId = $routeParams.id;
			//$scope.coords.imgName = image.imgName;
			//$scope.coords.description = image.description;
			//$scope.coords.link = image.link;
			
			$http.post('/editInventoryImage',$scope.coords)
			.success(function(data) {
				$location.path('/goToInventoryNew/New');
				$scope.$apply();
			});
		}    
		 
		
}]);