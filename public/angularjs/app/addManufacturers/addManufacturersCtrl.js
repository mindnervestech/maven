angular.module('newApp')
.controller('addManufacturersCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$scope.newProd = false;
	var files =[];
	$scope.addProduct = {};
	$scope.sectColl = "null";
	$scope.sect = {};
	$scope.status= function(sts){
		$scope.collection = sts;
		console.log($scope.collection);
	}
	
	$scope.chengeSection = function(){
		$scope.sectColl= "tr";
		console.log($scope.sect.id);
		$http.get('/getSectionCollection/'+$scope.sect.id)
		.success(function(data) {
			$scope.sectionCollection = data;
			console.log($scope.sectionCollection);
		});
	}
	
	
	$scope.saveManufacturers = function(){
		//$("#submit").attr("disabled", true);
		console.log(logofile);
		 $scope.addProduct.id = $('#sectionId').val();
		 console.log($('#sectionId').val());
		 if($scope.addProduct.newFlag != true)
			 $scope.addProduct.newFlag=false;

		 
		 console.log("check.."+$scope.addProduct.newFlag);
		 if(logofile == undefined){
			console.log($scope.addProduct);
			$http.post('/saveProduct',$scope.addProduct)
	   		.success(function(data) {
	   			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Update successfully",
				});
	   			$("#submit").attr("disabled", false);
	   			$location.path('/addProductImages/'+data.id);
	   		});
		}else if(logofile != undefined){
		console.log("logofile");
		 $upload.upload({
            url : '/saveProduct',
            method: 'POST',
            file:logofile,
            data:$scope.addProduct
         }).success(function(data) {
   			console.log(data);
   			console.log('success');
   			console.log($location);
   			$("#submit").attr("disabled", false);
   			$location.path('/addProductImages/'+data.id);
   			
   		 });
		}else if(logofile == undefined && cadfile != undefined){
			console.log("cadfile");
			 $upload.upload({
	            url : '/saveProduct',
	            method: 'POST',
	            file:cadfile,
	            data:$scope.addProduct
	         }).success(function(data) {
	   			console.log(data);
	   			console.log('success');
	   			console.log($location);
	   			$("#submit").attr("disabled", false);
	   			$location.path('/addProductImages/'+data.id);
	   			
	   		 });
			}else if(logofile != undefined && cadfile != undefined){
				
				
				console.log(names);
				console.log(files.length);
				console.log(files);
				console.log("bothfile");
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
		   			console.log(data);
		   			console.log('success');
		   			console.log($location);
		   			$("#submit").attr("disabled", false);
		   			$location.path('/addProductImages/'+data.id);
		   			
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
		   
	   }
	   

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
		
		$scope.deleteImage = function(img) {
			$http.get('/deleteImage/'+img.id)
			.success(function(data) {
				console.log('success');
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
			$http.get('/getImagesByProduct/'+$routeParams.id)
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
}]);


angular.module('newApp')
.controller('ImageCropCtrl', ['$scope','$http','$location','$filter','$routeParams', function ($scope,$http,$location,$filter,$routeParams) {


	
	console.log($routeParams.pId);
	
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
		 
		$scope.saveImage = function() {
			console.log($routeParams.id);
			$scope.coords.imageId = $routeParams.id;
			console.log($scope.coords);
			
			$http.post('/editImage',$scope.coords)
			.success(function(data) {
				console.log('success');
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Saved successfully",
				});
				console.log($routeParams.pId);
				console.log($routeParams.id);
				$location.path('/addProductImages/'+$routeParams.pId);
			});
		}        
}]);

