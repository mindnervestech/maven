angular.module('app.apiserviceViewInventory', [])
.service('apiserviceViewInventory', function($http,$q){

	var accessUrl = '';

	this.updateProduct=function(rowData){
		var defer = $q.defer();
		
		$http.post('/updateProductInfo',rowData).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllInventoryData=function(){
		var defer = $q.defer();
		$http.get('/getAllInventoryData').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getVehicleHistory=function(vin){
		var defer = $q.defer();
		
		$http.get('/getVehicleHistory/'+vin).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getHideProduct=function(id){
		var defer = $q.defer();
		
		$http.get('/getHideProduct/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	this.getGoTodraft=function(id){
		var defer = $q.defer();
		
		$http.get('/getGoTodraft/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllVehiclesByType=function(sts){
		var defer = $q.defer();
		
		$http.get('/getAllVehiclesByType/'+sts).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSoldVehiclesByType=function(sts){
		var defer = $q.defer();
		
		$http.get('/getAllSoldVehiclesByType/'+sts).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.findLocation=function(){
		var defer = $q.defer();
		
		$http.get('/findLocation').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllVehiclesImage=function(userLocationId){
		var defer = $q.defer();
		
		$http.get('http://www.glider-autos.com/getAllVehicles/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllProduct=function(userLocationId,status){
		var defer = $q.defer();
		var date = new Date().getTime();
		console.log(date);
		$http.get('/getAllProduct/'+status+'/'+date).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSoldVehicles=function(){
		var defer = $q.defer();
		
		$http.get('/getAllSoldVehicles').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllDraftVehiclesdata=function(userLocationId){
		var defer = $q.defer();
		
		$http.get('http://www.glider-autos.com/getAllDraftVehicles/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllDraftVehicles=function(userLocationId){
		var defer = $q.defer();
		
		$http.get('/getAllDraftVehicles/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteVehicleById=function(id){
		var defer = $q.defer();
		
		$http.get('/deleteVehicleById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteVehicleByIdPer=function(id){
		var defer = $q.defer();
		
		$http.get('/deleteVehicleByIdPer/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	
	this.addPublicCar=function(id){
		var defer = $q.defer();
		
		$http.get('/addPublicCar/'+id).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Vehicle published",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.addSameNewCar=function(id){
		var defer = $q.defer();
		
		$http.get('/addSameNewCar/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.setVehicleStatus=function(soldContact){
		var defer = $q.defer();
		
		$http.post('/setVehicleStatus/'+soldContact).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.exportDataAsCSV=function(){
		var defer = $q.defer();
		
		$http.get('/exportDataAsCSV').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.exportCarfaxCSV=function(){
		var defer = $q.defer();
		
		$http.get('/exportCarfaxCSV').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.exportCarGurusCSV=function(){
		var defer = $q.defer();
		
		$http.get('/exportCarGurusCSV').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllCollectionData=function(){
		var defer = $q.defer();
		
		$http.get('/getAllCollectionData').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.addNewCollection=function(collections){
		var defer = $q.defer();
		
		$http.post('/addNewCollection',collections).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "saved successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteCollectionData=function(collections){
		var defer = $q.defer();
		
		$http.get('/deleteCollectionData/'+collections).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Remove User",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllInventoryData=function(){
		var defer = $q.defer();
		$http.get('/getAllInventoryData').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
})

/*angular.module('app.apiserviceViewInventory', [])
.service('apiserviceViewInventory', function($http,$q){

	var accessUrl = '';

	this.findLocation=function(){
		var defer = $q.defer();
		
		$http.get('/findLocation').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getAllInventory=function(userLocationId){
		var defer = $q.defer();
		
		$http.get('/getAllInventory/'+userLocationId).success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
})*/