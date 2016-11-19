angular.module('app.apiserviceCustomizationForm', [])
.service('apiserviceCustomizationForm', function($http,$q){

	var accessUrl = '';

	this.getCustomizationform=function(formType){
		var defer = $q.defer();
		
		$http.get('/getCustomizationform/'+formType).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllSites=function(){
		var defer = $q.defer();
		
		$http.get('/getAllSites').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAlloutcome=function(){
		var defer = $q.defer();
		
		$http.get('/getAlloutcome').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	
	this.getLeadCrateForm=function(editform){
		var defer = $q.defer();
		
		$http.post('/getLeadCrateForm',editform).success(function(data) {
			 $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Form Created successfully",
				});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getLeadCrateFormTitle=function(setjson){
		var defer = $q.defer();
		
		$http.post('/getLeadCrateFormTitle',setjson).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Title Edit successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getLeadTypeDataById=function(leadId){
		var defer = $q.defer();
		
		$http.get('/getLeadTypeDataById/'+leadId).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getInternalPdfData=function(){
		var defer = $q.defer();
		
		$http.get('/getInternalPdfData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.saveLeadFormPopup=function(setjson){
		var defer = $q.defer();
		console.log(setjson);
		$http.post('/saveLeadFormPopup',setjson).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Save successfully",
				text: "Save successfully",
			});
			defer.resolve(data);
		});
		return defer.promise;
	};
	this.getInternalPdfData=function(){
		var defer = $q.defer();
		
		$http.get('/getInternalPdfData').success(function(data) {
			defer.resolve(data);
		});

		return defer.promise;
	};
	
	this.getInternalPdfDataById=function(id){
		var defer = $q.defer();
		$http.get('/getInternalPdfDataById/'+id).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.deleteInternalPdf=function(internalPdfId){
		var defer = $q.defer();
		$http.get('/deleteInternalPdf/'+internalPdfId).success(function(data) {
			$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "PDF deleted successfully",
				});
			defer.resolve(data);
		});
		return defer.promise;
	};
})