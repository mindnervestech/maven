angular.module('app.apiserviceAddCropInventory', [])
.service('apiserviceAddCropInventory', function($http,$q){

	var accessUrl = '';

	this.findLocation=function(){
		var defer = $q.defer();
		
		$http.get('/findLocation').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getInventoryImageById=function(id){
		var defer = $q.defer();
		
		$http.get('http://www.glider-autos.com:9889/getInventoryImageById/'+id).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getInventoryImageById=function(id){
		var defer = $q.defer();
		
		$http.get('/getInventoryImageById/'+id).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.editInventoryImage=function(coords){
		var defer = $q.defer();
		$http.post('http://www.glider-autos.com:9889/editInventoryImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.editInventoryImage=function(coords){
		var defer = $q.defer();
		$http.post('/editInventoryImage',coords).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
})