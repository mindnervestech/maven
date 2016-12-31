angular.module('newApp')
.controller('productCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$route', function ($scope,$http,$location,$filter,$routeParams,$upload,$route) {
	console.log("productCtrl ....");
	
	
	$http.get('/getAllInventoryData').success(function(data) {
		console.log(data);
		$scope.mainCollections = data;
	});
	
	$scope.getSubCollection = function(obj){
		console.log(obj);
		$http.get('/getAllCollection/'+obj.id).success(function(data) {
			console.log(data);
			$scope.subCollections = data;
		});
	};
	 var mainCollObj = localStorage.getItem('mainCollection');
	 $scope.mainCollection = JSON.parse(mainCollObj);
	 $scope.getSubCollection($scope.mainCollection);
	 if(mainCollObj != undefined){
		 //$scope.mainCollection = mainCollObj;
		 //console.log($scope.mainCollection);
	 }
	 $scope.product = {};
	 $scope.product.newFlag = false;
	
	 var cadfile;
	 var logofile;
	 var names=[];
	 var files =[];
	 
	$scope.onLogoFileSelect = function($files) {
		logofile = $files;
		if(files[0] == undefined || files[0] == null){
			files[0] = $files[0];
			names[0]= "logoFile";
		}else{
			files[1] = $files[0];
			names[1]= "logoFile";
		}		
	}
		
	$scope.onCADFileSelect = function($files) {
		cadfile = $files;
		console.log()
		if(files[0] == undefined || files[0] == null){
			files[0] = $files[0];
			names[0]= "cadFile";
		}else{
			files[1] = $files[0];
			names[1]= "cadFile";
		}
	}
	
	$scope.saveProduct = function(){
		if($scope.mainCollection != null && $scope.mainCollection != undefined)
			$scope.product.mainCollection = $scope.mainCollection.id;
		if($scope.subCollection != null && $scope.subCollection != undefined)
			$scope.product.collection = JSON.parse($scope.subCollection).id;

		console.log("save Product..",$scope.product);
		
		if(logofile == undefined && cadfile == undefined){
			console.log($scope.product);
			$http.post('/saveNewProduct',$scope.product).success(function(data) {
				if(data != 'error'){
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
					if($scope.product.publicStatus != "draft"){
						$location.path('/productImages/'+data);
					}
					$route.reload();
				}	   			
	   		});
		}else{				
			console.log(names);
			console.log(files);
			console.log("bothfile");
			$upload.upload({
				url : '/saveNewProduct',
		        method: 'POST',
		        fileFormDataName: names,
		        file:files,
		        data:$scope.product
		   }).success(function(data) {
			   if(data != 'error'){
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
						if($scope.product.publicStatus != "draft"){
						$location.path('/productImages/'+data);
					}
					$route.reload();
				}		   			
		   });
		}
	};
	
	
}]);

angular.module('newApp')
.controller('updateProductCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$route', function ($scope,$http,$location,$filter,$routeParams,$upload,$route) {
	console.log("updateProductCtrl ....",$routeParams.id);
	
	$http.get('/getProductDataById/'+$routeParams.id).success(function(data) {
		console.log(data);
		$scope.product = data;
		if($scope.product.year != null){
			$scope.product.year = parseInt($scope.product.year);
		}
		if($scope.product.mainCollection != null){
			$scope.mainCollection = JSON.stringify($scope.product.mainCollection);
			console.log($scope.mainCollection);
			$scope.getSubCollection($scope.mainCollection);
		}
		if($scope.product.collection != null){
			$scope.subCollection = JSON.stringify($scope.product.collection);
			console.log($scope.subCollection);
		}
	});
	
	$http.get('/getAllInventoryData').success(function(data) {
		console.log(data);
		$scope.mainCollections = data;
	});
	
	$scope.getSubCollection = function(obj){
		console.log(obj);
		$http.get('/getAllCollection/'+JSON.parse(obj).id).success(function(data) {
			console.log(data);
			$scope.subCollections = data;
		});
	};
		
	 var cadfile;
	 var logofile;
	 var names=[];
	 var files =[];
	 
	$scope.onLogoFileSelect = function($files) {
		logofile = $files;
		if(files[0] == undefined || files[0] == null){
			files[0] = $files[0];
			names[0]= "logoFile";
		}else{
			files[1] = $files[0];
			names[1]= "logoFile";
		}		
	}
		
	$scope.onCADFileSelect = function($files) {
		cadfile = $files;
		console.log()
		if(files[0] == undefined || files[0] == null){
			files[0] = $files[0];
			names[0]= "cadFile";
		}else{
			files[1] = $files[0];
			names[1]= "cadFile";
		}
	}
	
	$scope.cancelUpdate = function(){
		$location.path('/viewInventory');
	};
	
	$scope.updateProduct = function(){
		if($scope.mainCollection != null && $scope.mainCollection != undefined){
			$scope.product.mainCollection = JSON.parse($scope.mainCollection).id;
		}else{
			delete $scope.product.mainCollection;
		}			
		if($scope.subCollection != null && $scope.subCollection != undefined){
			$scope.product.collection = JSON.parse($scope.subCollection).id;
		}else{
			delete $scope.product.collection;
		}
			

		delete $scope.product.locations;
		delete $scope.product.user;
		console.log("update Product..",$scope.product);
		
		if(logofile == undefined && cadfile == undefined){
			console.log($scope.product);
			$http.post('/updateNewProduct',$scope.product).success(function(data) {
				if(data != 'error'){
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
					if($scope.product.publicStatus != "draft"){
						$location.path('/productImages/'+$scope.product.id);
					}
					//$location.path('/viewInventory');
				}else{
					$.pnotify({
					    title: "Error",
					    type:'success',
					    text: "can't update product",
					});
				}	   			
	   		});
		}else{
			$upload.upload({
				url : '/updateNewProduct',
		        method: 'POST',
		        fileFormDataName: names,
		        file:files,
		        data:$scope.product
		   }).success(function(data) {
			   if(data != 'error'){
					$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Update successfully",
					});
					if($scope.product.publicStatus != "draft"){
						$location.path('/productImages/'+$scope.product.id);
					}
					//$location.path('/viewInventory');
				}else{
					$.pnotify({
					    title: "Error",
					    type:'success',
					    text: "can't update product",
					});
				}		   			
		   });
		}
	};
	
	
}]);