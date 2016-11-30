﻿angular.module('newApp').controller('mainCtrl',
    ['$scope', 'applicationService', 'quickViewService', 'builderService', 'pluginsService', '$location','$http','$interval','$filter','$rootScope',
        function ($scope, applicationService, quickViewService, builderService, pluginsService, $location,$http,$interval,$filter,$rootScope) {
    	var ele = document.getElementById('loadingmanual');	
    	$(ele).hide();
            $(document).ready(function () {
                applicationService.init();
                quickViewService.init();
                builderService.init();
                pluginsService.init();
                Dropzone.autoDiscover = false;
            });
            
            var array = [];
           
            $scope.userRole=userRole;
            $http.get('/getUserInfo').success(function(data,status, headers, config){
            	
            	$scope.name = data.firstName + " " + data.lastName;
            	if(data.imageName == null){
            		$scope.userimage = data.imageUrl;
            	}else{
            		$scope.userimage = "http://glider-autos.com/MavenImg/images"+data.imageUrl;
            	}

            	
            }).error(function(data,status){
            	if(status == 401) {
            		window.location.href = "/login";
            	}
            });

            $http.get('/getInfoCount').success(function(data,status, headers, config){
            	$scope.requestMoreLength = data.req;
                $scope.scheduleTestLength = data.schedule;
                $scope.tradeInLength = data.trade;
                $scope.premiumlength = data.premium;
                $scope.userType = data.userType;
                
                $scope.setFalg = 0;
                
                if($scope.userType == "General Manager"){
                	 if(locationId != 0){
                     	$scope.setFalg = 1;
                     	$http.get('/getLocationName/'+locationId)
            			.success(function(data1) {
            				
            				 $scope.locationName=data1;
            				 
            			});	
                    
                     }
                }else{
                	$scope.setFalg = 1;
                	$scope.locationName=data.locationName;
                	console.log("::locationname");
                	console.log($scope.locationName);
                }
               
            })
            
            $http.get('/getAllPermissionData')
            .success(function(data){
			console.log(data);
			angular.forEach(data, function(value, key) {
				 if(value.name == "Leads"){
        			$scope.perName = "/configuration";
        		}
				else if(value.name == "Emails"){
        			$scope.perName = "/newsLetter";
        		}
        		else if(value.name == "Documentation"){
        			$scope.perName = "/documentation";
        		}
        		else if(value.name == "Marketing"){
        			$scope.perName = "/socialMedia";
        		}
        		else if(value.name == "Domain"){
        			$scope.perName = "/domainDetails";
        		}
        		else if(value.name == "Forms"){
        			$scope.perName = "/form";
        		}
        		else if(value.name == "Website Analytic"){
        			$scope.perName = "/webAnalytics";
        		}
        	});
			console.log($scope.perName);
			$scope.permissionValue = data;
			
		    });
            
            
            $http.get('/getSalesUserValue')
            .success(function(data){
			console.log(data);
			$scope.salesPersonPerf = data;
			
		    });
            
            $scope.assignCanceledLead = function(item) {
        		console.log(item.typeOfLead);
              	$scope.leadlId = item.id;
              	$scope.leadType = item.typeOfLead;
              	$scope.changedUser = "";
              	$('#btnAssig').click();
              }
            
            $scope.changeUser = function() {
       	        	$http.get('/changeAssignedUser/'+$scope.leadlId+'/'+$scope.changedUser+'/'+$scope.leadType)
       				.success(function(data) {
       					$('#assignUserModal').modal('hide');
       					$.pnotify({
       					    title: "Success",
       					    type:'success',
       					    text: "User assigned successfully",
       					});
       					$('#btnAssig').click();
       					$scope.indexInitFunction();
       				});
       	        	
       	        }
            
            $scope.releaseLeads = function(entity){
            	console.log(entity.leadType);
   			$http.get('/releaseLeads/'+entity.id+'/'+entity.leadType)
   				.success(function(data) {
   					$.pnotify({
   					    title: "Success",
   					    type:'success',
   					    text: "This lead has been released for everyone to claim ",
   					});
   					$scope.indexInitFunction();
   				});
   			
   		 }
            
            $scope.setNewFlag = function() {
            	console.log("inside.....in")
                $scope.flagForPop=0;
              }
            
            $scope.flagForPop=0;
            $scope.setFlagForPop = function() {
            	
              $scope.flagForPop=1;
            }
            
            $scope.setNewFlagNew = function() {
            	console.log("inside.....in")
                $scope.flagForPopNew=0;
              }
            
            $scope.flagForPopNew=0;
            $scope.setFlagForPopNew = function() {
            	
              $scope.flagForPopNew=1;
            }
            
            $scope.showRichNotification = function(obj){
	          	var opt={
	          			icon: obj.imageUrl,
	          		    body: "New "+obj.leadType+" has been submitted!"
	          	};
	          	if (!("Notification" in window)) {
	          	    alert("This browser does not support desktop notification");
	          	}else if (Notification.permission === "granted") {
	          		var notification = new Notification("Request More Info!",opt);
	          	}else if (Notification.permission !== 'denied') {
	          	    Notification.requestPermission(function (permission) {
		          	    if (permission === "granted") {
		          	        var notification = new Notification("Request More Info!",opt);
		          	    }
	          	    });
	          	}
	          	notification.onclick = function(event) {
	            	console.log("on Click");
	            	window.location.href = "#otherLeads/"+obj.isContactusType;
	            };
            };            
            
            $scope.leadCount = function(){
            	$scope.leadData={};
                $http.get('/getLeadInfo').success(function(data,status, headers, config){
                	$scope.leadData=data;
                	$scope.notifLength=0;
                	angular.forEach(data, function(value, key) {
                		if(value.richNotification == 0){
                			$scope.showRichNotification(value);
                			$http.get('/requestInfoRichNotification/'+value.id).success(function(data) {});
                		}                		
                		if(value.notifFlag == 0){
                			$scope.notifLength++;
                			
                		}
                		
                	});
                	
                	$scope.leadData = $filter('orderBy')($scope.leadData,'timeDiff');
                	$scope.dataLength=$scope.leadData.length;
                   
                })
            }
            
            $scope.releaseFromNotif = function(entity){
            	console.log(entity);
   			$http.get('/releaseFromNotif/'+entity.id+'/'+entity.leadType)
   				.success(function(data) {
   					$.pnotify({
   					    title: "Success",
   					    type:'success',
   					    text: "This lead has been released from notification list ",
   					});
   					$scope.leadCount();
   				});
   			
   		 }
            
            $scope.setAsRead = function (item) {
                console.log("IIIIDDDD");
                
                console.log(item);
                var flag=true;
                if(item.typeOfLead =="Request More"){
                	 $http.get('/requestInfoMarkRead/'+flag+'/'+item.id)
             		.success(function(data) {
             			$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Claimed Successfully",
						});
             			 $scope.indexInitFunction(); 
             		console.log(data);
             	});
                	
                }
                
                else if(item.typeOfLead =="Schedule Test"){
                	var flag=true;
                	$http.get('/scheduleTestMarkRead/'+flag+'/'+item.id)
            		.success(function(data) {
            			$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Claimed Successfully",
						});
            			 $scope.indexInitFunction(); 
            			console.log(data);
            		});
                }
                
               else if(item.typeOfLead =="Trade In"){
            	   
            	   $http.get('/tradeInMarkRead/'+flag+'/'+item.id)
       			.success(function(data) {
       				$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Claimed Successfully",
					});
       			 $scope.indexInitFunction(); 
       				console.log(data);
       	       	});
                }
               else if(item.typeOfLead =="Premium"){
            	   $http.get('/changeAssignedUser/'+item.id+'/'+userKey+'/'+item.type)
					.success(function(data) {
						$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Claimed Successfully",
						});
						 $scope.indexInitFunction(); 
					});
            	   
               	
               }
               else if(item.typeOfLead == null){
            	   console.log("item.typeOfLead");
               	 $http.get('/requestInfoMarkRead/'+flag+'/'+item.id)
            		.success(function(data) {
            			$.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Claimed Successfully",
						});
            			 $scope.indexInitFunction(); 
            		console.log(data);
            	});
               	
               }
               
                
            };
            
            
            $scope.notificationArray = [];
            $scope.notifictionCount = 0;
            $scope.indexInitFunction = function(){
            	console.log("testing");
            	 $scope.notificationArray = [];
            	$http.get('/getNotificationData').success(function(data,status, headers, config){
            		console.log("In the function");
            		$scope.notifictionCount = 0;
                	angular.forEach(data.commentLike, function(value, key) {
                		$scope.notificationArray.push({
							title: value.firstName+"  "+ value.lastName+" just Liked your work!"+ value.userComment,
							iconClass: "glyphicon glyphicon-heart editClassColorRed",
							findBy:"comment like",
							value:value,
							timeDiff:"4 minutes ago",
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.planScheduleMonthly, function(value, key) {
                		var month=value.month;
                		console.log(value.diffDays);
    					if(month !=null){
    						month = month.toLowerCase();
    						month=month.substring(0,1).toUpperCase()+month.substring(1);
    					}
                		
                		$scope.notificationArray.push({
							title: month+"'s plan has been assigned",
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"month plan",
							value:value,
							timeDiff:value.diffDays,
							id:value.id,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.invitationData, function(value, key) {
                		$scope.notificationArray.push({
							title: "New meeting invitation received",
							iconClass: "fa fa-car editClassColorBlack",
							findBy:"invitation received",
							value:value,
							timeDiff:value.diffDays,
							id:value.id,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.declineMeeting, function(value, key) {
                		$scope.notificationArray.push({
							title: "Your invitation to "+value.firstName+" "+value.lastName+" has been declined",
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"declined meeting",
							value:value,
							timeDiff:value.diffDays,
							id:value.id,
						});
                		$scope.notifictionCount++;
                	});
                	
                	
                	angular.forEach(data.comingSoonData, function(value, key) {
                		$scope.notificationArray.push({
							title: value.make+" "+value.model+" coming Soon Vehicle",
							iconClass: "fa fa-car editClassColorBlack",
							findBy:"coming soon",
							value:value,
							timeDiff:value.diffDays,
							id:value.id,
						});
                		$scope.notifictionCount++;
                		
                	});
                	
                	angular.forEach(data.acceptedMeeting, function(value, key) {
                		$scope.notificationArray.push({
							title: value.firstName+"  "+value.lastName+" accepted your invitation to "+value.name,
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"accept meeting",
							value:value,
							timeDiff:value.diffDays,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.deleteMeeting, function(value, key) {
                		if(value.declineUser == 'Host'){
                			$scope.notificationArray.push({
    							title: value.name+" has been cancelled",
    							iconClass: "glyphicon glyphicon-star editClassColor",
    							findBy:"delete meeting",
    							value:value,
    							timeDiff:"4 minutes ago",
    						});
                		
                		}else if(value.declineUser == 'this person'){
                			$scope.notificationArray.push({
    							title: value.firstName+" "+value.lastName+" can't go to the "+value.name,
    							iconClass: "glyphicon glyphicon-star editClassColor",
    							findBy:"delete meeting",
    							value:value,
    							timeDiff:"4 minutes ago",
    						});
                		}
                		
                		$scope.notifictionCount++;
                	});
                	
                	
                	angular.forEach(data.updateMeeting, function(value, key) {
                		$scope.notificationArray.push({
							title: value.name+" information has been changed",
							iconClass: "glyphicon glyphicon-star editClassColor",
							findBy:"update meeting",
							value:value,
							timeDiff:value.diffDays,
						});
                		$scope.notifictionCount++;
                	});
                	
                	angular.forEach(data.reminderPopup, function(value, key) {
                		$scope.notificationArray.push({
							title: value.notes,
							iconClass: "fa fa-car editClassColorBlack",
							findBy:"reminder popup",
							value:value,
							timeDiff:value.diffDays,
						});
                		$scope.notifictionCount++;
                	});
                	
                	$scope.decline(data.declineMeeting);
                	$scope.invitationMsg(data.invitationData);
                	$scope.likeMsg(data.commentLike);
                	console.log(data.planScheduleMonthly);
                	$scope.planMsg(data.planScheduleMonthly);
                	$scope.acceptMsg(data.acceptedMeeting);
                	$scope.deleteMeeting(data.deleteMeeting);
                	$scope.updateMeeting(data.updateMeeting);
                	
            	});
            	
            }
            
            $scope.planForsalePersonForMonth = function(month){
     		   $('#salepersonPlanModelForMonth').modal();
     		   $http.get('/getPlanByMonthAndUser/'+userKey+'/'+month)
     			.success(function(data) {
     				if(data != null){
     					$scope.monthFlagForSale=1;
     				}
     				$scope.saleMonthTotalPerForMonth=data;
     			});
     	   }
            
            $scope.dismissFunction = function(infoNotifiction){
            	angular.forEach($scope.notificationArray, function(value, key) {
            		if(infoNotifiction.findBy == value.findBy){
            			console.log(value);
            			$http.get('/changeNotifFlag/'+value.id+"/"+value.findBy)
    		    		.success(function(data){
    		    			
    		    			$scope.indexInitFunction();
    		    			
    		    		});
            			
            			
            			$scope.notificationArray.splice(key, 1);
            		}
            	});
      }
            
            $scope.invitationFlag=0;
            $scope.acceptInvitationFlag=0;
            $scope.declineInvitationFlag=0;
            $rootScope.comingSoonFlag=0;
            $scope.showDetailsFunction = function(infoNotifiction){
            	$scope.arrayAdd = [];
            	console.log(infoNotifiction);
            	if(infoNotifiction.findBy == "month plan"){
            		$scope.planForsalePersonForMonth(infoNotifiction.value.month);
            	}
            	if(infoNotifiction.findBy == "declined meeting"){
            		$scope.declineInvitationFlag=1;
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.decline($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "comment like"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.likeMsg($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "invitation received"){
            		$scope.invitationFlag=1;
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.invitationMsg($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "coming soon"){
            		$rootScope.comingSoonFlag=1;
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$rootScope.$emit("CallComingSoonMethod", {});
            	}
            	if(infoNotifiction.findBy == "accept meeting"){
            		$scope.acceptInvitationFlag=1;
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.acceptMsg($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "delete meeting"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.deleteMeeting($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "update meeting"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$scope.updateMeeting($scope.arrayAdd);
            	}
            	if(infoNotifiction.findBy == "reminder popup"){
            		$scope.arrayAdd.push(infoNotifiction.value);
            		$rootScope.$emit("CallReminderMethod", {});
            		
            	}
            	
            
            }
            
            $scope.decline = function(data){
        		
        		
				var notifContent;
				angular.forEach(data, function(value, key) {
					if(value.declineMeeting == 2 || $scope.declineInvitationFlag==1){
				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> Your invitation to "+value.firstName+"&nbsp;&nbsp;"+value.lastName+" has been declined</span><br><span> Reason: "+value.declineReason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
				
				var position = 'topRight';
    	        if ($('body').hasClass('rtl')) position = 'topLeft';
    	        var n = noty({
    	            text: notifContent,
    	            type: 'success',
    	            layout: position,
    	            theme: 'made',
    	            animation: {
    	                open: 'animated bounceIn',
    	                close: 'animated bounceOut'
    	            },
    	            
    	            callback: {
    	                onShow: function () {
    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
    	                },
    	                onCloseClick: function () {
    	                	$('html, body').animate({scrollTop:480}, 'slow');
    	                }
    	            }
    	        });
    	        
    	        var element = $('#cnt');
				//$compile(element)($scope);
				}
				});
		
	}
            
        	$scope.updateMeeting = function(data){

				var notifContent;
				angular.forEach(data, function(value, key) {
				//var t = $filter('date')(value.confirmTime,"hh:mm a");
				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.name+" information has been changed</span><br><span>"+value.confirmDate+"   "+value.confirmTime+" - "+value.confirmEndTime+"</span><br><span>"+value.reason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
				
				var position = 'topRight';
    	        if ($('body').hasClass('rtl')) position = 'topLeft';
    	        var n = noty({
    	            text: notifContent,
    	            type: 'success',
    	            layout: position,
    	            theme: 'made',
    	            animation: {
    	                open: 'animated bounceIn',
    	                close: 'animated bounceOut'
    	            },
    	            
    	            callback: {
    	                onShow: function () {
    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('margin-left', -30).css('bottom', 10);
    	                },
    	                onCloseClick: function () {
    	                	$('html, body').animate({scrollTop:480}, 'slow');
    	                }
    	            }
    	        });
    	        
    	        var element = $('#cnt');
				//$compile(element)($scope);
				});
	}
            
        	$scope.deleteMeeting = function(data){
        		

				var notifContent;
				angular.forEach(data, function(value, key) {
					if(value.declineUser == 'Host'){
						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.name+" has been cancelled</span><br></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
		    	        var n = noty({
		    	            text: notifContent,
		    	            type: 'success',
		    	            layout: position,
		    	            theme: 'made',
		    	            animation: {
		    	                open: 'animated bounceIn',
		    	                close: 'animated bounceOut'
		    	            },
		    	            
		    	            callback: {
		    	                onShow: function () {
		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
		    	                },
		    	                onCloseClick: function () {
		    	                	$('html, body').animate({scrollTop:480}, 'slow');
		    	                }
		    	            }
		    	        });
		    	        
		    	        var element = $('#cnt');
						//$compile(element)($scope);
					}else if(value.declineUser == 'this person'){
						notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> "+value.firstName+"&nbsp;&nbsp;"+value.lastName+" can't go to the "+value.name+"</span><br><span>"+value.confirmDate+"&nbsp;&nbsp"+value.confirmTime+"</span><br><span>"+value.reason+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
	    				
	    				var position = 'topRight';
		    	        if ($('body').hasClass('rtl')) position = 'topLeft';
		    	        var n = noty({
		    	            text: notifContent,
		    	            type: 'success',
		    	            layout: position,
		    	            theme: 'made',
		    	            animation: {
		    	                open: 'animated bounceIn',
		    	                close: 'animated bounceOut'
		    	            },
		    	            
		    	            callback: {
		    	                onShow: function () {
		    	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('bottom', 10);
		    	                },
		    	                onCloseClick: function () {
		    	                	$('html, body').animate({scrollTop:480}, 'slow');
		    	                }
		    	            }
		    	        });
		    	        
		    	        var element = $('#cnt');
						//$compile(element)($scope);
					}
				
				});
		
		
	}
            
        	$scope.acceptMsg = function(data){

                console.log(data);		
   				var notifContent;
   				angular.forEach(data, function(value, key) {
   					if(value.acceptMeeting == 2 || $scope.acceptInvitationFlag==1){
   				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span>"+value.firstName+"&nbsp;&nbsp;"+value.lastName+" accepted your invitation to "+value.name+"</span></p><p class='row' style='margin-left:0;'></p><p class='pull-left' style='margin-left:65%;'><a class='f-12'>Close&nbsp;<i></i></a></p></div></div>";
   				
   				var position = 'topRight';
       	        if ($('body').hasClass('rtl')) position = 'topLeft';
       	        var n = noty({
       	            text: notifContent,
       	            type: 'success',
       	            layout: position,
       	            theme: 'made',
       	            animation: {
       	                open: 'animated bounceIn',
       	                close: 'animated bounceOut'
       	            },
       	            
       	            callback: {
       	                onShow: function () {
       	                    $('#noty_topRight_layout_container, .noty_container_type_success').css('width', 350).css('margin-left', -30).css('bottom', 10);
       	                },
       	                onCloseClick: function () {
       	                	$('html, body').animate({scrollTop:480}, 'slow');
       	                }
       	            }
       	        });
       	        
       	        var element = $('#cnt');
   				//$compile(element)($scope);
   				}
   				});
   	}
            
        	$scope.planMsg = function(data){
				
				var notifContent;
				angular.forEach(data, function(value, key) {
					if(value.flagMsg != 0){
					var month=value.month;
					if(month !=null){
						month = month.toLowerCase();
						month=month.substring(0,1).toUpperCase()+month.substring(1);
					}
					
				notifContent = "<div class='alert alert-dark media fade in bd-0' id='message-alert'><div class='media-left'></div><div class='media-body width-100p'><p class='row' style='margin-left:0;'><span> "+month+"'s plan has been assigned</span></p></div></div>";
				
				var position = 'topRight';
    	        if ($('body').hasClass('rtl'))
    	        	position = 'topLeft';
    	        var n = noty({
					text : notifContent,
					type : 'success',
					layout : position,
					theme : 'made',
					buttons: [
					          {
					              addClass: 'general-button customLeft', text: 'See', onClick: function($noty)
					              {
					            	  
					            	  $scope.planForsalePersonForMonth(value.month);
					                 $noty.close();
					              }
					          }
							 ],
					animation : {
						open : 'animated bounceIn',
						close : 'animated bounceOut'
					},

					callback : {
						onShow : function() {
							$(
									'#noty_topRight_layout_container, .noty_container_type_success')
									.css(
											
											'width',
											477)
									.css('margin-left', -135)
									.css(
											
											'bottom',
											10);
						},
						onCloseClick : function() {
							$('html, body')
									.animate(
											{
												scrollTop : 480
											},
											'slow');
						}
					}
				});
    	        
    	        var element = $('#cnt');
				//$compile(element)($scope);
    	        
				}
				});
	}
            
        	$scope.likeMsg = function(data) {

				
				angular.forEach(data, function(value, key) {
					var notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div>'
						+ '<div class="media-body width-100p col-md-12" style="padding: 0px;"><div class="col-md-3" style="padding: 0px;"><img style="width: 120px;" src="'+value.imageUrl+'"></div><div class="col-md-9"><div class="col-md-12" style="text-align: center;"><h2 style="color: goldenrod;margin-top: 0px;">Congratulations!</h2></div><span class="col-md-12" style="margin-left: 22px;text-align: center;"><h3 style="margin-top: 0px;"><span>'+value.firstName+'  '+ value.lastName+'</span><br><span> just Liked your work!</span><br><span>'+value.userComment+'</span></h3></span><p class="pull-left" style="margin-left:85%;"><a class="f-12">Close&nbsp;</a></p></div></div>'
						+ '</div>';
				var position = 'topRight';
				if ($('body').hasClass(
						'rtl'))
					position = 'topLeft';
				var n = noty({
					text : notifContent,
					type : 'success',
					layout : position,
					theme : 'made',
					animation : {
						open : 'animated bounceIn',
						close : 'animated bounceOut'
					},

					callback : {
						onShow : function() {
							$(
									'#noty_topRight_layout_container, .noty_container_type_success')
									.css(
											
											'width',
											410)
									.css('margin-left', -82)
									.css(
											
											'bottom',
											10);
						},
						onCloseClick : function() {
							$('html, body')
									.animate(
											{
												scrollTop : 480
											},
											'slow');
						}
					}
				});
				});


}
            
            $scope.declineDate = function(value){
        		$('#decline-model').modal();
        		$scope.valueId = value;
        	}
            
        	$scope.invitationMsg = function(data) {

				angular.forEach(data, function(value, key) {
					if(value.sendInvitation != 0 || $scope.invitationFlag==1 ){
					var notifContent = '<div class="alert alert-dark media fade in bd-0" id="message-alert"><div class="media-left"></div>'
						+ '<div class="media-body width-100p col-md-12" style="padding: 0px;"><div class="col-md-3" style="padding: 0px;"><img style="width: 120px;" src="'+value.imageUrl+'"></div><div class="col-md-9"><div class="col-md-12" style="text-align: center;"><h3 style="margin-top: 0px;">New meeting invitation received</h3></div><span class="col-md-12" style="margin-left: 22px;text-align: center;border-bottom: solid;"><h3><span>'+value.name+'</span><br><span style="color: cornflowerblue;"><b>'+value.confirmDate+'&nbsp;&nbsp;&nbsp;&nbsp;'+value.confirmTime+' </b></span></h3></span><hr><p class="pull-left" style="margin-left:85%;"></p></div></div>'
						+ '</div>';
				var position = 'topRight';
				if ($('body').hasClass(
						'rtl'))
					position = 'topLeft';
				var n = noty({
					text : notifContent,
					type : 'success',
					layout : position,
					theme : 'made',
					buttons: [
					          {
							        addClass: 'general-button btnText', text: 'Decline', onClick: function($noty)
							              {
							            	  $scope.declineDate(value);
							                 $noty.close();
							              }
							          },
					          {
					              addClass: 'general-button btnText', text: 'Accept', onClick: function($noty)
					              {
					            	  $scope.acceptDate(value);
					                 $noty.close();
					              }
					          }
							 ],
					animation : {
						open : 'animated bounceIn',
						close : 'animated bounceOut'
					},

					callback : {
						onShow : function() {
							$(
									'#noty_topRight_layout_container, .noty_container_type_success')
									.css(
											
											'width',
											477)
									.css('margin-left', -135)
									.css(
											
											'bottom',
											10);
						},
						onCloseClick : function() {
							$('html, body')
									.animate(
											{
												scrollTop : 480
											},
											'slow');
						}
					}
				});
				
				
				}
				});


}
            
            var promo =  $interval(function(){
            	$scope.leadCount();
				$http.get('/getInfoCount').success(function(data,status, headers, config){
	            	$scope.requestMoreLength = data.req;
	                $scope.scheduleTestLength = data.schedule;
	                $scope.tradeInLength = data.trade;
	                $scope.premiumlength = data.premium;
	                $scope.userType = data.userType;
	            })
				},15000);
            
            $scope.$on('getCountEvent', function (event, args) {
            	$http.get('/getInfoCount').success(function(data,status, headers, config){
                	$scope.requestMoreLength = data.req;
                    $scope.scheduleTestLength = data.schedule;
                    $scope.tradeInLength = data.trade;
                    $scope.premiumlength = data.premium;
                    $scope.userType = data.userType;
                })
            });
            
            
            $scope.$on('$viewContentLoaded', function () {
                pluginsService.init();
                applicationService.customScroll();
                applicationService.handlePanelAction();
                $('.nav.nav-sidebar .nav-active').removeClass('nav-active active');
                $('.nav.nav-sidebar .active:not(.nav-parent)').closest('.nav-parent').addClass('nav-active active');

                if($location.$$path == '/' || $location.$$path == '/layout-api'){
                    $('.nav.nav-sidebar .nav-parent').removeClass('nav-active active');
                    $('.nav.nav-sidebar .nav-parent .children').removeClass('nav-active active');
                    if ($('body').hasClass('sidebar-collapsed') && !$('body').hasClass('sidebar-hover')) return;
                    if ($('body').hasClass('submenu-hover')) return;
                    $('.nav.nav-sidebar .nav-parent .children').slideUp(200);
                    $('.nav-sidebar .arrow').removeClass('active');
                }

            });

            $scope.isActive = function (viewLocation) {
                return viewLocation === $location.path();
            };

            
            $scope.addFormField = function(editInput){
            	console.log("");
            		
            		console.log(editInput);
                    $scope.fields = [];
                   // var subSectons = Object.keys($scope.form);  
                   // for(var i = 0; i < subSectons.length; i++) {
                      var formFields = [];
                      var form_details = {};  
                      angular.forEach(editInput,function(form){
                        if(form.component === "textInput"){
                          formFields.push(getJsonBForTextInput(form));
                        }
                        if(form.component === "sampleInput"){
                          formFields.push(getJsonBForSampleInput(form));
                        }
                        if(form.component === "checkbox"){
                          formFields.push(getJsonBForCheckBox(form));
                        }
                        if (form.component === "textArea") {
                          formFields.push(getJsonBForTextArea(form));
                        }
                        if(form.component === "radio"){
                          formFields.push(getJsonBForRadio(form));
                        }
                        if(form.component === "select"){
                          formFields.push(getJsonBForSelect(form));
                        }
                        if(form.component === "date"){
                          formFields.push(getJsonBForDate(form));
                        }
                        if(form.component === 'signature'){
                          formFields.push(getJsonBForSignature(form));
                        }
                        if(form.component === 'ui-grid'){
                          formFields.push(getJsonBForUIGrid(form));
                        }
                        if(form.component === 'image'){
                          formFields.push(getJsonBForImage(form));
                        }
                        if(form.component === 'financialcalculator'){
                            formFields.push(getJsonBForFinancialcalculator(form));
                        }
                        if(form.component === "daterange"){
                            formFields.push(getJsonBForDateRange(form));
                          }
                        if(form.component === "autocompleteText"){
                            formFields.push(getJsonBForAutocomplete(form));
                          }
                        if(form.component === "contactssearch"){
                        	 formFields.push(getJsonBForContactssearch(form));
                        }
                        if(form.component === "inventorysearch"){
                       	 formFields.push(getJsonBForInventorysearch(form));
                       }
                        if(form.component === "fileuploaders"){
                          	 formFields.push(getJsonBForFileuploader(form));
                          }
                        if(form.component === "timerange"){
                         	 formFields.push(getJsonBForTimeRange(form));
                         }
                        if(form.component === "headerlabel"){
                        	 formFields.push(getJsonBForheaderlabel(form));
                        }
                        
                        if(form.component === "numberInput"){
                       	 formFields.push(getJsonBForNumberInput(form));
                       }
                        if(form.component === "leadTypeSelector"){
                          	 formFields.push(getJsonBForleadTypeSelector(form));
                          }
                        if(form.component === "emailSelect"){
                         	 formFields.push(getJsonBForEmailSelect(form));
                         }
                        if(form.component === "phoneSelect"){
                        	 formFields.push(getJsonBForPhoneSelect(form));
                        }
                        if(form.component === "productType"){
                         	 formFields.push(getJsonBForproductType(form));
                         }
                        if(form.component === "selectGroup"){
                        	 formFields.push(getJsonBForselectGroup(form));
                        }
                        
                        if(form.component === "multipleselect"){
                          	 formFields.push(getJsonBForMultipleselect(form));
                          }
                          if(form.component === "singleSelect"){
                        	console.log("insisdeee");
                          	 formFields.push(getJsonBForSingleSelect(form));
                          }
                      });
                      /* form_details.name = subSectons[i];
                       form_details.isRepeatable  = true;
                       form_details.isChild  = false;
                       form_details.fields = formFields;
                       if( formFields.length != 0) {
                       $scope.fields.push(form_details);

                       }*/
                    // }
                       
                       console.log("&**^^^^^^^^^^^^^^^^^^^^6");
                       console.log(formFields);
                       $scope.userFields = formFields;
                       console.log($scope.userFields);
                       return formFields;
                    //fields = angular.copy($scope.fields);
                    //model = $scope.model;
                    //options = $scope.options;
                  }; 	
            	
              	function getPropertiesForEditable(editable){
        	        var prop = {};
        	        if(editable){
        	         	return prop;
        	        }else{
        	        	prop = {
        	            	'templateOptions.disabled': '!model.text'
        	          	};
        	         	return prop;
        	        }
        	   	}

              	
              	function getJsonBForheaderlabel(jsonObject){
              		var key;
                	if(jsonObject.key === ""){
                  		key = jsonObject.label;
                  		key = key.replace(" ","_");
        		        key = key.toLowerCase();
        		   	}else{
        		    	key = jsonObject.key;
        		  	}

                	var properties = getPropertiesForEditable(jsonObject.editable);
                	var convertedObject = {
                  		"key": key,
                    	"type": 'headerlabel',
                    	"templateOptions": {
                      		"label": jsonObject.label,
                    	},	
                	};
                	return convertedObject;
                }
              	
              	function getJsonBForNumberInput(jsonObject){
              		var key;
                	if(jsonObject.key === ""){
                  		key = jsonObject.label;
                  		key = key.replace(" ","_");
        		        key = key.toLowerCase();
        		   	}else{
        		    	key = jsonObject.key;
        		  	}

                	var properties = getPropertiesForEditable(jsonObject.editable);
                	var convertedObject = {
                      		"key": key,
                        	"type": 'stacked-input',
                        	"templateOptions": {
                          		"type": "number",
                          		"label": jsonObject.label,
                          		"placeholder": jsonObject.placeholder,
                          		"required": jsonObject.required
                        	},	
                          
                    	};
                	return convertedObject;
                }
              	function getJsonBForTextInput(jsonObject){
                	var key;
                	if(jsonObject.key === ""){
                  		key = jsonObject.label;
                  		key = key.replace(" ","_");
        		        key = key.toLowerCase();
        		   	}else{
        		    	key = jsonObject.key;
        		  	}

                	var properties = getPropertiesForEditable(jsonObject.editable);
                	var convertedObject = {
                  		"key": key,
                    	"type": 'stacked-input',
                    	"templateOptions": {
                      		"type": "text",
                      		"label": jsonObject.label,
                      		"placeholder": jsonObject.placeholder,
                      		"required": jsonObject.required
                    	},	
                       // controller: 'formState.textCtrl',
                    	"expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                	};
                	return convertedObject;
              	}
              	
              	function getJsonBForAutocomplete(jsonObject){
              		var key;
                	if(jsonObject.key === ""){
                  		key = jsonObject.label;
                  		key = key.replace(" ","_");
        		        key = key.toLowerCase();
        		   	}else{
        		    	key = jsonObject.key;
        		  	}

                	var properties = getPropertiesForEditable(jsonObject.editable);
                	var convertedObject = {
                  		"key": key,
                    	"type": 'autocompleteText',
                    	"templateOptions": {
                      		"type": "text",
                      		"label": jsonObject.label,
                      		"placeholder": jsonObject.placeholder,
                      		"required": jsonObject.required
                    	},	
                       // controller: 'formState.textCtrl',
                    	"expressionProperties": properties,
                      
                	};
                	return convertedObject;
              	}

              	function getJsonBForSampleInput(jsonObject){
                	var key;
                	if(jsonObject.key === ""){
                  		key = jsonObject.label;
                  		key = key.replace(" ","_");
                  		key = key.toLowerCase();
                	}else{
                  		key = jsonObject.key;
                	}
                	var properties = getPropertiesForEditable(jsonObject.editable);
                	var convertedObject = {
                  		"key": key,
                    	"type": 'stacked-input',
                    	"templateOptions": {
                      		"type": "text",
                      		"label": jsonObject.label,
        	              	"placeholder": jsonObject.placeholder,
        	              	"required": jsonObject.required
        	            	},
        	            	"expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject, scope); 
                        }
                	};
                	return convertedObject;
              	}

              	$scope.getCheckboxOptions = function(options){
                	$scope.options=[];
                	angular.forEach(options,function(opt){
                  		//key = opt;
                  		//key = key.replace(" ","_");
                  		var data = {
                        "checked":false,
                    		"id":opt,
                    		"value":opt
                  		}
                  		$scope.options.push(data)
                	})
                	return $scope.options;
              	}

              function getJsonBForCheckBox(jsonObject){

                var key;
                if(jsonObject.key === ""){
                  key = jsonObject.label;
                  //key = key.replace(" ","_");
                  //key = key.toLowerCase();
                }else{
                  key = jsonObject.key;
                }
                var opt = getCheckboxOptions(jsonObject.options);
                var convertedObject = {
                  "type": jsonObject.component,
                  "key": key,
                  "name":jsonObject.label,
                  "templateOptions": {
                    "label": opt,
                    "placeholder": jsonObject.placeholder,
                    "required": jsonObject.required
                  }, 
                   "hideExpression" : function($viewValue, $modelValue, scope) {
                    return isHideComponent(jsonObject); 
                  }
                }
                return convertedObject;
              }

              function getJsonBForTextArea(jsonObject){
                var key;
                if(jsonObject.key === ""){
                  key = jsonObject.label;
                  key = key.replace(" ","_");
                  key = key.toLowerCase();
                }else{
                  key = jsonObject.key;
                }
                console.log(jsonObject, "logic");
                var properties = getPropertiesForEditable(jsonObject.editable);
                var convertedObject = {
                  "type": "textarea",
                  "key": key,
                  "templateOptions": {
                    "label": jsonObject.label,
                    "placeholder": jsonObject.placeholder,
                    "required": jsonObject.required
                    // "isRepeatable": jsonObject.isRepeatable
                  },
                 // controller: 'formState.textCtrl',
                  "expressionProperties" : properties,
                  "hideExpression" : function($viewValue, $modelValue, scope) {
                     console.log("-?>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", jsonObject);
                       
                     return isHideComponent(jsonObject);
                  }
                }
                return convertedObject;
              }
              
              function isHideComponent(jsonObject) {
                  if(jsonObject.logic.component == null) {
                      return false;
                  }      
                  var logic_component = JSON.parse(jsonObject.logic.component);
                  console.log(logic_component,"==");
                  switch (logic_component.component) {
                      case 'textInput':
                        return isHide(jsonObject,  logic_component);
                      case 'checkbox':
                          return isHide(jsonObject, logic_component);
                      case 'textArea':
                          return isHide(jsonObject, logic_component);
                      case 'radio' :
                          return isHide(jsonObject, logic_component);
                      case 'date':
                        return isHide(jsonObject, logic_component);
                      case 'select':
                        return isHide(jsonObject, logic_component);
                      case 'signature':
                        return false;
                      case 'ui-grid':
                        return false;
                    }
                }

                function isHide(jsonObject, logic_component) {
                  var flag = jsonObject.logic.comparator === undefined ? true : (jsonObject.logic.comparator == 'Equal to') ? true : false;
                  var modelValue = "";
                  var subSectons = Object.keys($scope.form);  
                  for(var i = 0; i < subSectons.length; i++) {
                      var formFields = [];  
                      angular.forEach($scope.form[subSectons[i]],function(form){
                      if (form.component ===  logic_component.component && form.id === logic_component.id ) {
                        modelValue  = form.key;
                       
                      }
                      console.log(subSectons[i], "<>", form.id, "<>", logic_component.id,"<>", form.component, "<>", logic_component.component);
                      });
                  }
                  console.log(modelValue,"modelValue", model);
                  angular.forEach($scope.form, function(form){
                    
                  });
                  var isShow = jsonObject.logic.action === "Show";
                  if ((model[modelValue] === undefined )&& (isShow || !flag)) {
                    return isShow ? true : false;
                  }
                  if((model[modelValue] == jsonObject.logic.value)) {
                      return isShow ? !flag : flag;
                  }else{
                    return isShow ? flag : !flag;
                  }
                }

                function getPropertiesForRadioOptions(options){
                  var optionsArray = [];
                  for(var i=0;i<options.length;i++){
                    var data = {
                      "value":options[i],
                      "text":options[i]
                    }
                    optionsArray.push(data);
                  }
                  return optionsArray;
                }

                function getJsonBForRadio(jsonObject){
                  var key;
                  if(jsonObject.key === ""){
                    key = jsonObject.label;
                    key = key.replace(" ","_");
                    key = key.toLowerCase();
                  }else{
                    key = jsonObject.key;
                  }
                  var properties = getPropertiesForEditable(jsonObject.editable);
                  var options = [];
                  options = getPropertiesForRadioOptions(jsonObject.options);
                  var convertedObject = {
                    "key": key,
                    "type": jsonObject.component,
                    "name":jsonObject.label,
                    "templateOptions": {
                      "label": jsonObject.label,
                       "options": options,
                       "required": jsonObject.required
                      },
                      // controller: 'formState.textCtrl',
                      "expressionProperties": properties,
                      "hideExpression" : function($viewValue, $modelValue, scope) {
                        return isHideComponent(jsonObject); 
                      }
                  }
                  return convertedObject;
                }

                function getPropertiesForSelectOptions(options){
                  var optionsArray = [];
                  for(var i=0;i<options.length;i++){
                    var data = {
                      "label":options[i],
                      "id":options[i]
                    }
                    optionsArray.push(data);
                  }
                  return optionsArray;
                }
                
                function getJsonBForselectGroup(jsonObject){
                    var key;
                    if(jsonObject.key === ""){
                      key = jsonObject.label;
                      key = key.replace(" ","_");
                      key = key.toLowerCase();
                    }else{
                      key = jsonObject.key;
                    }
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    var options = [];
                    options = getPropertiesForSelectOptions(jsonObject.options);
                    var convertedObject = {
                      "key": key,
                      "type": jsonObject.component,
                      "templateOptions": {
                        "label": jsonObject.label,
                        "options": options,
                        "valueProp": "id",
                        "labelProp": "label",
                        "required": jsonObject.required
                        },
                        "expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                    }
                    return convertedObject;

                }
                
                function getJsonBForproductType(jsonObject){
                    var key;
                    if(jsonObject.key === ""){
                      key = jsonObject.label;
                      key = key.replace(" ","_");
                      key = key.toLowerCase();
                    }else{
                      key = jsonObject.key;
                    }
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    var options = [];
                    options = getPropertiesForSelectOptions(jsonObject.options);
                    var convertedObject = {
                      "key": key,
                      "type": jsonObject.component,
                      "templateOptions": {
                        "label": jsonObject.label,
                        "options": options,
                        "valueProp": "id",
                        "labelProp": "label",
                        "required": jsonObject.required
                        },
                        "expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                    }
                    return convertedObject;
                  }

                function getJsonBForSelect(jsonObject){
                  var key;
                  if(jsonObject.key === ""){
                    key = jsonObject.label;
                    key = key.replace(" ","_");
                    key = key.toLowerCase();
                  }else{
                    key = jsonObject.key;
                  }
                  var properties = getPropertiesForEditable(jsonObject.editable);
                  var options = [];
                  options = getPropertiesForSelectOptions(jsonObject.options);
                  var convertedObject = {
                    "key": key,
                    "type": jsonObject.component,
                    "templateOptions": {
                      "label": jsonObject.label,
                      "options": options,
                      "valueProp": "id",
                      "labelProp": "label",
                      "required": jsonObject.required
                      },
                      "expressionProperties": properties,
                      "hideExpression" : function($viewValue, $modelValue, scope) {
                        return isHideComponent(jsonObject);
                      }
                  }
                  return convertedObject;
                }
                
                function getJsonBForPhoneSelect(jsonObject){
                	console.log("------+++++++-------------");
                	console.log(jsonObject);
                    var key;
                    if(jsonObject.key === ""){
                      key = jsonObject.label;
                      key = key.replace(" ","_");
                      key = key.toLowerCase();
                    }else{
                      key = jsonObject.key;
                    }
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    var options = [];
                    options = getPropertiesForSelectOptions(jsonObject.options);
                    var convertedObject = {
                      "key": key,
                      "type": jsonObject.component,
                      "templateOptions": {
                        "label": jsonObject.label,
                        "options": options,
                        "valueProp": "id",
                        "labelProp": "label",
                        "required": jsonObject.required
                        },
                        "expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                    }
                    return convertedObject;
                  }

                
                
                function getJsonBForEmailSelect(jsonObject){
                	console.log("------+++++++-------------");
                	console.log(jsonObject);
                    var key;
                    if(jsonObject.key === ""){
                      key = jsonObject.label;
                      key = key.replace(" ","_");
                      key = key.toLowerCase();
                    }else{
                      key = jsonObject.key;
                    }
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    var options = [];
                    options = getPropertiesForSelectOptions(jsonObject.options);
                    var convertedObject = {
                      "key": key,
                      "type": jsonObject.component,
                      "templateOptions": {
                        "label": jsonObject.label,
                        "options": options,
                        "valueProp": "id",
                        "labelProp": "label",
                        "required": jsonObject.required
                        },
                        "expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                    }
                    return convertedObject;
                  }

                
                
                function getJsonBForleadTypeSelector(jsonObject){
                    var key;
                    if(jsonObject.key === ""){
                      key = jsonObject.label;
                      key = key.replace(" ","_");
                      key = key.toLowerCase();
                    }else{
                      key = jsonObject.key;
                    }
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    var options = [];
                    options = getPropertiesForSelectOptions(jsonObject.options);
                    var convertedObject = {
                      "key": key,
                      "type": jsonObject.component,
                      "templateOptions": {
                        "label": jsonObject.label,
                        "options": options,
                        "valueProp": "id",
                        "labelProp": "label",
                        "required": jsonObject.required
                        },
                        "expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                    }
                    return convertedObject;
                  }

                function getJsonBForSelect(jsonObject){
                  var key;
                  if(jsonObject.key === ""){
                    key = jsonObject.label;
                    key = key.replace(" ","_");
                    key = key.toLowerCase();
                  }else{
                    key = jsonObject.key;
                  }
                  var properties = getPropertiesForEditable(jsonObject.editable);
                  var options = [];
                  options = getPropertiesForSelectOptions(jsonObject.options);
                  var convertedObject = {
                    "key": key,
                    "type": jsonObject.component,
                    "templateOptions": {
                      "label": jsonObject.label,
                      "options": options,
                      "valueProp": "id",
                      "labelProp": "label",
                      "required": jsonObject.required
                      },
                      "expressionProperties": properties,
                      "hideExpression" : function($viewValue, $modelValue, scope) {
                        return isHideComponent(jsonObject);
                      }
                  }
                  return convertedObject;
                }
                
                function getJsonBForMultipleselect(jsonObject){
                    var key;
                    if(jsonObject.key === ""){
                      key = jsonObject.label;
                      key = key.replace(" ","_");
                      key = key.toLowerCase();
                    }else{
                      key = jsonObject.key;
                    }
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    var options = [];
                    options = getPropertiesForSelectOptions(jsonObject.options);
                    var convertedObject = {
                      "key": key,
                      "type": jsonObject.component,
                      "templateOptions": {
                        "label": jsonObject.label,
                        "options": options,
                        "valueProp": "id",
                        "labelProp": "label",
                        "required": jsonObject.required
                        },
                        "expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                    }
                    return convertedObject;
                  }
                
                function  getJsonBForSingleSelect(jsonObject){
                    var key;
                    if(jsonObject.key === ""){
                      key = jsonObject.label;
                      key = key.replace(" ","_");
                      key = key.toLowerCase();
                    }else{
                      key = jsonObject.key;
                    }
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    var options = [];
                    options = getPropertiesForSelectOptions(jsonObject.options);
                    var convertedObject = {
                      "key": key,
                      "type": jsonObject.component,
                      "templateOptions": {
                        "label": jsonObject.label,
                        "options": options,
                        "valueProp": "id",
                        "labelProp": "label",
                        "required": jsonObject.required
                        },
                        "expressionProperties": properties,
                        "hideExpression" : function($viewValue, $modelValue, scope) {
                          return isHideComponent(jsonObject);
                        }
                    }
                    return convertedObject;
                  }
                
                

                function getJsonBForDate(jsonObject){
                  var key;
                  if(jsonObject.key === ""){
                    key = jsonObject.label;
                    key = key.replace(" ","_");
                    key = key.toLowerCase();
                  }else{
                    key = jsonObject.key;
                  }
                  var properties = getPropertiesForEditable(jsonObject.editable);
                  var convertedObject = {
                    "key": key,
                      "type": 'stacked-input',
                      "templateOptions": {
                        "type": "date",
                        "label": jsonObject.label,
                        "placeholder": jsonObject.placeholder,
                        "required": jsonObject.required
                      },
                      "expressionProperties": properties,
                      "hideExpression" : function($viewValue, $modelValue, scope) {
                        return isHideComponent(jsonObject);
                      }
                  };
                  return convertedObject;
                }
                function getJsonBForTimeRange(jsonObject){
                	 var key;
                     if(jsonObject.key === ""){
                       key = jsonObject.label;
                       key = key.replace(" ","_");
                       key = key.toLowerCase();
                     }else{
                       key = jsonObject.key;
                     }
                     var properties = getPropertiesForEditable(jsonObject.editable);
                     var convertedObject = {
                       "key": key,
                         "type": 'timerange',
                         "templateOptions": {
                           "label": jsonObject.label,
                           "placeholder": jsonObject.placeholder,
                         },
                         "expressionProperties": properties,
                         
                     };
                     return convertedObject;
                }
                
                function getJsonBForDateRange(jsonObject){
                	 var key;
                     if(jsonObject.key === ""){
                       key = jsonObject.label;
                       key = key.replace(" ","_");
                       key = key.toLowerCase();
                     }else{
                       key = jsonObject.key;
                     }
                     var properties = getPropertiesForEditable(jsonObject.editable);
                     var convertedObject = {
                       "key": key,
                         "type": 'daterange',
                         "templateOptions": {
                           "type": "date",
                           "label": jsonObject.label,
                           "placeholder": jsonObject.placeholder,
                           "required": jsonObject.required
                         },
                         "expressionProperties": properties,
                         
                     };
                     return convertedObject;
                }

                function getJsonBForSignature(jsonObject){
                  var key;
                  if(jsonObject.key === ""){
                    key = jsonObject.label;
                    key = key.replace(" ","_");
                    key = key.toLowerCase();
                  }else{
                    key = jsonObject.key;
                  }
                  var properties = getPropertiesForEditable(jsonObject.editable);
                  var convertedObject = {
                    "key": key,
                      "type": "signature",
                      "templateOptions": {
                          "label": jsonObject.label
                      },
                      "hideExpression" : function($viewValue, $modelValue, scope) {
                        return isHideComponent(jsonObject); 
                      }
                  };
                  return convertedObject;
                }

                function getPropertiesForColumnDefs(options){
                  var colArray = [];
                  angular.forEach(options,function(colName){
                    col = colName.split(":");
                    var columnOptions;
                    if(col[0] === ''){
                      columnOptions = {
                        name:col[0],
                        field:col[1],
                        //cellTemplate:'<div class="custom-button"><button type="submit" class="button button-small button-assertive" ng-click="delete()">delete</button></div>',
                        cellTemplate:'<div class="custom-button"><i class="icon ion-close-circled" style="cursor: pointer;"></i></div>',
                        enableCellEdit:false,
                        width: 30
                      }
                      colArray.push(columnOptions);
                    }else{
                      columnOptions = {
                        name:col[0],
                        field:col[1]
                      }
                      colArray.push(columnOptions);
                    }
                    
                  });
                  columnOptions = {
                    name:'',
                    field:'action',
                    //cellTemplate:'<div class="custom-button"><button type="submit" class="button button-small button-assertive" ng-click="delete()">delete</button></div>',
                    cellTemplate:'<div class="custom-button"><i class="icon ion-close-circled" style="cursor: pointer;"></i></div>',
                    enableCellEdit:false,
                    width: 30
                  }
                  colArray.push(columnOptions);
                  return colArray;
                }

                  function getPropertiesForColumnDefs1(options){
                  var colArray = [];
                  angular.forEach(options,function(colName){

                    var columnOptions;
                    if(colName.column_name === ''){
                      columnOptions = {
                        name : colName.column_name,
                        field : colName.field,
                        isDerivedCol : colName.grid_derived_col,
                        col_id : colName.col_id,
                        //cellTemplate:'<div class="custom-button"><button type="submit" class="button button-small button-assertive" ng-click="delete()">delete</button></div>',
                        cellTemplate : '<div class="custom-button"><i class="icon ion-close-circled" style="cursor: pointer;"></i></div>',
                        enableCellEdit : false,
                        width : 30
                      }
                      colArray.push(columnOptions);
                    }else{
                      columnOptions = {
                        name : colName.column_name,
                        field : colName.field,
                        isDerivedCol : colName.grid_derived_col,
                        col_id : colName.col_id,
                      }
                      colArray.push(columnOptions);
                    }
                    
                  });
                  columnOptions = {
                    name:'',
                    field:'action',
                    //cellTemplate:'<div class="custom-button"><button type="submit" class="button button-small button-assertive" ng-click="delete()">delete</button></div>',
                    cellTemplate:'<div class="custom-button"><i class="icon ion-close-circled" style="cursor: pointer;"></i></div>',
                    enableCellEdit:false,
                    width: 30
                  }
                  colArray.push(columnOptions);
                  return colArray;
                }

                function getJsonBForUIGrid(jsonObject){
                  var columnDefs = [];
                  var columnDefs1 = [];
                  var properties = getPropertiesForEditable(jsonObject.editable);
                  columnDefs = getPropertiesForColumnDefs1(jsonObject.columnOptions);
                  // columnDefs1 = getPropertiesForColumnDefs1(jsonObject.columnOptions);
                  columnDef = columnDefs;
                  equation = jsonObject.equation;
                  
                  var convertedObject = {
                    key: 'list',
                    type: 'ui-grid',
                    templateOptions: {
                      label: jsonObject.label,
                      columnDefs: columnDefs,
                      enableColumnMenus:false,
                      enableSorting: false,
                      onRegisterApi: ''
                    },
                    controller: 'formState.uiGridCtrl',
                    "hideExpression" : function($viewValue, $modelValue, scope) {
                      return isHideComponent(jsonObject);
                    }
                  };
                  
                  return convertedObject;
                }

                function getJsonBForImage(jsonObject){
                  var properties = getPropertiesForEditable(jsonObject.editable);
                  var convertedObject = {
                    "key": jsonObject.key,
                      "type": 'image',
                      "templateOptions": {
                        "type": "text",
                        "label": jsonObject.label,
                        "src":jsonObject.imageUrl,
                        "placeholder": jsonObject.placeholder,
                        "required": jsonObject.required
                      },
                      "expressionProperties": properties,
                      "controller": 'formState.imageCtrl',
                      "hideExpression" : function($viewValue, $modelValue, scope) {
                        return isHideComponent(jsonObject, scope); 
                      }

                  };
                  return convertedObject;
                };
                function getJsonBForFileuploader(jsonObject){
                    
                    var convertedObject = {
                      key: 'fileuploaders',
                      type: 'fileuploaders',
                      templateOptions: {
                    	  "label": jsonObject.label,
                      }
                     
                    };
                    
                    return convertedObject;
                	
              
                }
                function getJsonBForInventorysearch(jsonObject){
                	 var columnDefs = [];
                     var columnDefs1 = [];
                     var properties = getPropertiesForEditable(jsonObject.editable);
                     columnDefs = getPropertiesForColumnDefs1(jsonObject.columnOptions);
                     // columnDefs1 = getPropertiesForColumnDefs1(jsonObject.columnOptions);
                     columnDef = columnDefs;
                     equation = jsonObject.equation;
                     
                     var convertedObject = {
                       key: 'inventorysearch',
                       type: 'inventorysearch',
                       templateOptions: {
                         label: 'Inventory'
                       },
                      
                     };
                     
                     return convertedObject;
                }
                
                function getJsonBForContactssearch(jsonObject){
               	 var columnDefs = [];
                    var columnDefs1 = [];
                    var properties = getPropertiesForEditable(jsonObject.editable);
                    columnDefs = getPropertiesForColumnDefs1(jsonObject.columnOptions);
                    // columnDefs1 = getPropertiesForColumnDefs1(jsonObject.columnOptions);
                    columnDef = columnDefs;
                    equation = jsonObject.equation;
                    
                    var convertedObject = {
                      key: 'contactssearch',
                      type: 'contactssearch',
                      templateOptions: {
                        label: 'Contacts'
                      },
                     
                    };
                    
                    return convertedObject;
               }
                
                
                function getJsonBForFinancialcalculator(jsonObject){
                	 var columnDefs = [];
                     var columnDefs1 = [];
                     var properties = getPropertiesForEditable(jsonObject.editable);
                     columnDefs = getPropertiesForColumnDefs1(jsonObject.columnOptions);
                     columnDef = columnDefs;
                     equation = jsonObject.equation;
                     
                     var convertedObject = {
                       key: 'financialcalculator',
                       type: 'financialcalculator',
                       templateOptions: {
                         label: 'fina'
                       },
                      
                     };
                     
                     return convertedObject;
                }
                
                $scope.saveJsonObject = function(object){
                  if($stateParams.templateId === ''){
                    service.saveJsonObject(object).then(
                      function(response){
                        console.log('response',response);
                      },
                      function(error){
                        console.log('error',error);
                      }
                    );
                  }else{
                    service.updateJsonObject(object,$stateParams.templateId).then(
                      function(response){
                        console.log('response',response);
                      },
                      function(error){
                        console.log('error',error);
                      }
                    );
                  }
                  
                }

                $scope.saveDraft = function(formName){
                  var object = {
                    'formname': formName,
                    'status':'Draft',
                    'component' : $scope.fields
                  }
                  $scope.saveJsonObject(object);
                };

                $scope.savePublish = function(formName){
                  var object = {
                    'formname': formName,
                    'status':'Publish',
                    'component' : $scope.fields
                  }
                  $scope.saveJsonObject(object);
                } 
            
        }]);
angular.module('newApp').controller('customizationCtrl',
	    ['$scope', 'applicationService', 'quickViewService', 'builderService', 'pluginsService', '$location','$http','$interval','$rootScope',
	        function ($scope, applicationService, quickViewService, builderService, pluginsService, $location,$http,$interval,$rootScope) {
	    
	    	$rootScope.selectEmailType = "";
	    	
	    	$http.get('/getDataFromCrm').success(function(data){
	    		$scope.searchList = data;
	    	 });
	    	
	    	$http.get('/getSelectedLeadType').success(function(data) {
				$scope.leadList = data; 
				
			});
	    	
	    	$scope.selectEmailType = function(emailType){
	    		console.log(emailType);
	    		$rootScope.selectEmailType = emailType;
	    	}
	    	
	    	$scope.selectPhoneType = function(phoneType){
	    		console.log(phoneType);
	    		$rootScope.selectPhoneType = phoneType;
	    	}
	    	
	    	
	    	
	    	
			var date = new Date().getTime();
	    	$http.get('/getAllProduct/'+"publish"+'/'+date).success(function(data) {
				$scope.manufacturerslist = data; 
			});
	    	
	    	$http.get('/getAllGroupList').success(function(data) {
	    		$scope.grouplist = data;
			});
	    	
	    	
	    	$scope.selectLead = function(data){
	    		/*$http.get('/getAllProductWise/'+"publish"+'/'+date+'/'+data).success(function(data) {
					
				});*/
	    		$scope.$emit("selectLeadDashbord", data);
    		}
	    	
	    	$scope.selecProductType = function(productType){
	    		console.log(productType);
	    		$http.get('/getSelectedLeadTypeWise/'+productType).success(function(data) {
	    			console.log(data);
	    			console.log($scope.leadList);
	    			$scope.leadList = data; 
			
	    		});
	    	}
	    	
	    	
	    	$scope.financeData = {};
	    	 $scope.financeData.downPayment=1000;
  	  		  $scope.financeData.annualInterestRate=7;
  	  		  $scope.financeData.numberOfYears=5;
  	  		//  $scope.financeData.price=entity.price;
  	  		  $scope.financeData.frequencyOfPayments=26;
	    	
	    	
	    	 $scope.calculateFinancialData = function(financeData){
	    	   	  	var cost         =financeData.price;
	    			var down_payment =financeData.downPayment;
	    			var interest     =financeData.annualInterestRate;
	    			var loan_years   =financeData.numberOfYears;
	    			var frequency_rate    =financeData.frequencyOfPayments;
	    			
	    			var interest_rate = (interest) / 100;
	    			 var rate          = interest_rate / frequency_rate;
	    			 $scope.payments      = loan_years * frequency_rate;
	    			var difference    = cost - down_payment;
	    			$scope.payment = Math.floor((difference*rate)/(1-Math.pow((1+rate),(-1* $scope.payments)))*100)/100;
	          }
	            
	    	 
	    	  $scope.test = function(){
	            	$scope.initAutocomplete();
	            };
	            
	           var placeSearch, autocomplete;
	            
	            $scope.initAutocomplete = function() {
	            	 autocomplete = new google.maps.places.Autocomplete((document.getElementById('autocomplete')),
	            	     {types: ['geocode']});
	            	 autocomplete.addListener('place_changed', fillInAddress);
	            	}

	            function fillInAddress(){
	            	var place = autocomplete.getplace();
	            }
	            	
	            	/*$http.get('/getAllVehicles')
	        		.success(function(data) {
	        			$scope.prodSearchList = data;
	        		});*/
	            	
	            	$scope.focusIn11 = function(index, stockRp){
		    			stockRp.stockNumber = $scope.prodSearchList[index].title;
		    			$scope.getStockDetails(stockRp)
		    		}
	            	
	            	 $scope.stockWiseData = [];
	     	  		$scope.stockWiseData.push({});  	
	     	  		$scope.getStockDetails = function(stockRp) {
		    			$scope.isStockError = false;
		    			$http.get('/getStockDetails/'+stockRp.stockNumber).success(function(response) {
		    				if(response.isData) {
		    					$scope.isStockError = false;
		    					stockRp.designer = response.designer;
		    					stockRp.price = response.price;
		    					stockRp.vehicleImage = response.vehicleImage;
		    					stockRp.imgId = response.imgId;
		    					stockRp.year = response.year;
		    					stockRp.primaryTitle = response.primaryTitle;
		    					stockRp.title = response.title; 
		    					stockRp.id = response.id;
		    					stockRp.productId = response.productId;
		    				} else {
		    					$scope.isStockError = true;
		    				}
		    			});
		    		};
		    		
		    		$scope.pushRecord = function(){
		    			$scope.stockWiseData.push({});
		    		}
		    		
		    		$scope.onLogoFileSelect = function($files){
		    			console.log($files);
		    			$rootScope.fileCustom = $files; 
		    			
		    		}		
		    		$scope.showtimepick = function(){
		    			$('#bestTimes').timepicker();
		    		}
		    		/*$scope.selectedObjs = function(select){
		    			console.log(select);
		    		}*/
}]);
