angular.module('newApp')
.controller('InventoryImageCropCtrl', ['$scope','$http','$location','$filter','$routeParams','apiserviceAddCropInventory', function ($scope,$http,$location,$filter,$routeParams,apiserviceAddCropInventory) {


	
	$scope.coords = {};
	if(userRole == "Photographer"){
		$scope.imgId = "http://www.glider-autos.com:9889/getImageInv/"+$routeParams.id+"/full?d=" + Math.random();
		}
		else{
			$scope.imgId = "/getImageInv/"+$routeParams.id+"/full?d=" + Math.random();
		}
	var imageW, imageH, boundx, boundy;
	$scope.init = function() {
		if(userRole == "Photographer"){
			apiserviceAddCropInventory.findLocation().then(function(data){
			 
					$scope.userLocationId = data;
					apiserviceAddCropInventory.getInventoryImageById($routeParams.id).then(function(data){
			
				imageW = data.col;
				imageH = data.row;
				
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
				});
		}else{
			apiserviceAddCropInventory.findLocation().then(function(data){
				
				$scope.userLocationId = data;
				apiserviceAddCropInventory.getInventoryImageById($routeParams.id).then(function(data){
			
				imageW = data.col;
				imageH = data.row;
				
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
			});
		}
		 
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
			
	if(userRole == "Photographer"){
		apiserviceAddCropInventory.editInventoryImage($scope.coords).then(function(data){
				$location.path('/editInventory/'+$routeParams.vid+'/'+true+"/"+$routeParams.productId);
				});
				
			}else{
				apiserviceAddCropInventory.editInventoryImage($scope.coords).then(function(data){
					$location.path('/editInventory/'+$routeParams.vid+'/'+true+"/"+$routeParams.productId);
				});
			}
		}        
}]);