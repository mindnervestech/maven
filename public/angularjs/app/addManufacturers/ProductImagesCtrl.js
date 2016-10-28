angular.module('newApp')
.controller('ProductImagesCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$scope.addProductImages = {};
	$http.get('/addProductImages')
	.success(function(data) {
		$scope.addProductImages = data;
	});
	
	
		$upload.upload({
            url : '/uploadProductImage',
            method: 'POST',
            file:logofile,
            data:$scope.addProduct
        }).success(function(data, status, headers, config) {
            console.log('success');
            $.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Logo image saved successfully",
			});
        });
	}

	 var logofile;
		$scope.onLogoFileSelect = function($files) {
			logofile = $files;
		}
			
			/*----------------------------*/
			var myDropzone2;
			
				myDropzone2 = new Dropzone("#dropzoneFrm2",{
					   parallelUploads: 1,
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
							      // Do the dimension checks you want to do
							      if (file.width < $scope.configList[0].width || file.height < $scope.configList[0].height) {
							    	  $('#sliderBtnMsg').click();
							    	  file.rejectDimensions()
							      }
							      else {
							        file.acceptDimensions();
							      }
							    });
						   this.on("complete", function() {
							   
							   this.removeAllFiles();
						   });
					   }
						  
				   });
			
}]);	
