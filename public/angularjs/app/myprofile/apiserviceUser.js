angular.module('app.apiserviceUser', [])
.service('apiserviceUser', function($http,$q,$upload){

	var autodealerTestUrl = 'http://www.glider-autos.com:7071/';
	var autodealerUrlTest = 'http://www.glider-autos.com:7071';
	this.getAllUsersToAssign=function(){
		var defer = $q.defer();
		$http.get('/getAllUsersToAssign').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getInternalPdfData = function(data){
		var defer = $q.defer();
		$http.get('/getInternalPdfData').success(function(data){
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
	this.assignToUser = function(data){
		var defer = $q.defer();
		$http.post("/assignToUser",data).success(function(data){
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Leads assign successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllLeadsByUser = function(userId){
		var defer = $q.defer();
		$http.get('/getAllLeadsByUser/'+userId).success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
	this.deactivateAccount = function(userId){
		var defer = $q.defer();
		$http.get('/deactivateAccount/'+userId).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Account deactive successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.deactivatePhotographerAccount = function(userId){
		var defer = $q.defer();
		$http.get(autodealerUrlTest+'/deactivatePhotographerAccount/'+userId).success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Account deactive successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	
	this.getOnlineReqUserChange = function(){
		var defer = $q.defer();
		$http.get('/getOnlineReqUserChange').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllUsers = function(){
		var defer = $q.defer();
		$http.get('/getAllUsers').success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	this.getAllPhotographer = function(name,locationId){
		var defer = $q.defer();
		$http.get(autodealerTestUrl+'getAllPhotographer/'+name+"/"+locationId).success(function(data) {
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.getAllDeactivatePhotographer=function(name,locationId){
		var defer = $q.defer();
		$http.get(autodealerUrlTest+'/getAllDeactivatePhotographer/'+name+"/"+locationId).success(function(data) {
				defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.findLocation = function(){
		var defer = $q.defer();
		$http.get('/findLocation').success(function(response) {
			 	defer.resolve(response);
    	});
		
		return defer.promise;
	};
	
	this.uploadImageFile = function(data, userRole){
		var defer = $q.defer();
		if(userRole == 'Photographer'){
			$http.post(autodealerTestUrl+"uploadImageFile",data).success(function(data){
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
				 	defer.resolve(data);
	    	}).error(function(data, status, headers, config){
	        	
	        	defer.resolve(data);
	        });
		}else{
			$http.post("/uploadImageFile",data).success(function(data){
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
				 	defer.resolve(data);
	    	}).error(function(data, status, headers, config){
	    		defer.resolve(data);
	        });
		}
		
		
		return defer.promise;
	};
	
	this.uploadImageFileLoad = function(userdata, userRole, logofile){
		var defer = $q.defer();
		if(userRole == 'Photographer'){
			$upload.upload({
	            url : autodealerTestUrl+'uploadImageFile',
	            method: 'post',
	            file:logofile,
	            data:userdata
	        }).success(function(data, status, headers, config) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
				 	defer.resolve(data);
	    	}).error(function(data, status, headers, config){
	    		defer.resolve(data);
	        });
		}else{
			$upload.upload({
				url : '/uploadImageFile',
	            method: 'post',
	            file:logofile,
	            data:userdata
	        }).success(function(data, status, headers, config) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
				 	defer.resolve(data);
	    	}).error(function(data, status, headers, config){
	    		defer.resolve(data);
	        });
		}
		
		
		return defer.promise;
	};
	this.updateImageFile = function(data){
		var defer = $q.defer();
		$http.post("/updateImageFile",data).success(function(data){
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "User saved successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.updateImagePhotographer = function(data){
		var defer = $q.defer();
		$http.post(autodealerTestUrl+"updateImagePhotographer",data).success(function(data){
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "User saved successfully",
			});
			 	defer.resolve(data);
    	});
		
		return defer.promise;
	};
	
	this.updateImageFileLoadPhotoPhotographer = function(userdata,userRole,logofile){
		var defer = $q.defer();
		if(userRole == 'Photographer'){
			$upload.upload({
	            url : autodealerTestUrl+'updateUserMaven',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}else{
			$upload.upload({
	            url : '/updateUserMaven',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}
	
		
		return defer.promise;
	};
	
	this.updateImageFileLoadPhoto = function(userdata,userRole,logofile){
		var defer = $q.defer();
		if(userRole == 'Photographer'){
			$upload.upload({
	            url : autodealerTestUrl+'updateUserMaven',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}else{
			$upload.upload({
	            url : '/updateUserMaven',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}
	
		
		return defer.promise;
	};
	
	this.updateImageFileLoad = function(userdata,logofile,userRole){
		var defer = $q.defer();
		if(userRole == 'Photographer'){
			$upload.upload({
	            url : autodealerTestUrl+'updateImageFile',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}else{
			$upload.upload({
	            url : '/updateImageFile',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}
	
		
		return defer.promise;
	};
	
	
	this.updateImageFilePhotographer = function(userdata,logofile,userRole){
		var defer = $q.defer();
		if(userRole == 'Photographer'){
			$upload.upload({
	            url : autodealerTestUrl+'updateImagePhotographer',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}else{
			$upload.upload({
	            url : '/updateImagePhotographer',
	            method: 'post',
	            data:userdata,
	            file:logofile
	        }).success(function(data, status, headers, config) {
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "User saved successfully",
				});
	            defer.resolve(data);
	        }).error(function(data, status, headers, config){
	        	$.pnotify({
				    title: "Error",
				    type:'Success',
				    text: "Error",
				});
	        	defer.resolve(data);
	        });
		}
	
		
		return defer.promise;
	};
	
	this.checkEmailOfUser = function(email){
		var defer = $q.defer();
		$http.get('/checkEmailOfUser/'+email).success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.getAllDeactivateUsers=function(){
		var defer = $q.defer();
		$http.get('/getAllDeactivateUsers').success(function(data) {
				defer.resolve(data);
		});
		return defer.promise;
	};
	
	this.activeAccount=function(userId){
		var defer = $q.defer();
		$http.get('/activeAccount/'+userId).success(function(data) {
			defer.resolve(data);
			});
		return defer.promise;
	};
	this.activePhotographerAccount=function(userId){
		var defer = $q.defer();
		console.log(userId);
		$http.get(autodealerUrlTest+'/activePhotographerAccount/'+userId).success(function(data) {
			defer.resolve(data);
			});
		return defer.promise;
	};
	
	this.activeLocationById=function(id){
		var defer = $q.defer();
		$http.get('/activeLocationById/'+id).success(function(data) {
			defer.resolve(data);
			});
		return defer.promise;
	};
	this.getUserRole=function(){
		var defer = $q.defer();
		$http.get('/getUserRole').success(function(data) {
			defer.resolve(data);
		});
		return defer.promise;
	};

	
	
})
