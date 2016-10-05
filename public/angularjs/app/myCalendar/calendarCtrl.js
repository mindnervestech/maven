angular.module('newApp')
.controller('calendarCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$timeout','apiservice', function ($scope,$http,$location,$filter,$routeParams,$upload,$timeout,apiservice) {
	//$.fn.Data.checkbox();
	$scope.eventList = [];
	$scope.eventdataValue = [];
	$scope.flag = 0;
	
	
	$scope.init = function(){
		
		apiservice.getTimeTableOf().then(
			function(success){
				console.log(success);
				//$scope.postalNameList = success;
				$scope.calenderTimeTable = success;
				angular.forEach($scope.calenderTimeTable, function(obj, index){
					$scope.eventList.push({
						id:obj.id,
						title:obj.email,
						name:obj.name,
						start: obj.startTime,//contractDurStartDate+"T"+obj.openTime,
						end: obj.endTime,//contractDurStartDate+"T"+obj.closeTime,
						backgroundColor:"red",
						locationId:"16",
						editable:true,
						allDay: false,
							
					});
				});
				console.log($scope.eventList);
				/*angular.forEach($scope.postalNameList, function(obj, index){
				$('#external-events').append("<div class='external-event col-md-12' style='color: white;background:"+obj.color+";'>"+obj.title+"</div>");
				});*/
				$('.external-event').addClass('fc-event-draggable');
				$scope.initFirst($scope.eventList);
				$scope.selectSalesPerson();
			});
	}
	
	$scope.dataOfCalendar = function(calendraData){
		apiservice.getTimeOfUser(calendraData).then(
			function(success){
				$scope.eventList = [];
				console.log(success);
				//$scope.postalNameList = success;
				$scope.calenderTime = success;
				console.log($scope.calenderTime);
				angular.forEach($scope.calenderTime, function(obj, index){
					$scope.eventList.push({
						id:obj.id,
						title:obj.email,
						name:obj.name,
						start: obj.startTime,//contractDurStartDate+"T"+obj.openTime,
						end: obj.endTime,//contractDurStartDate+"T"+obj.closeTime,
						backgroundColor:"red",
						locationId:"16",
						editable:true,
						allDay: false,
							
					});
				});
				console.log($scope.eventList);
				/*angular.forEach($scope.postalNameList, function(obj, index){
					$('#external-events').append("<div class='external-event col-md-12' style='color: white;background:"+obj.color+";'>"+obj.title+"</div>");
				});*/
				$('.external-event').addClass('fc-event-draggable');
				$scope.initFirst($scope.eventList);
				$scope.selectSalesPerson();
			});
	}
	
	$scope.selectSalesPerson = function(){
		apiservice.getSalesPerson().then(function(data){
			$scope.calendra = data;
			console.log($scope.calendra);
		});
	}
	
	$scope.initFirst = function(eventList){
		
		$scope.flag = 0;
		console.log(eventList);
		console.log(eventList.length);
		var eventDrag = function(el){
			console.log(el);
	        var eventObject = {
	            title: $.trim(el.text()) // use the element's text as the event title
	        };
	        el.data('eventObject', eventObject);
	        el.draggable({
	            zIndex: 999,
	            revert: true,      // will cause the event to go back to its
	            revertDuration: 0,  //  original position after the drag
	        });
	    };
	    $('#external-events div.external-event').each(function() {
	        eventDrag($(this));
	    });
				$('#calendar').fullCalendar({
				defaultView: 'agendaWeek',
			    editable: true,
			    droppable: true,
			    events: eventList,
			    
		        eventDrop: function (event, delta, revertFunc, jsEvent, ui, view) {
		        	 $scope.flagSave = 0;
		        	 $scope.saveObj = {};
		        	 var flagDataAb = 0;
		        	 $scope.date = 0;
		        	 angular.forEach($scope.eventList, function(obj, index){
		        		 console.log(obj);
		        		 console.log(event);
		        		 if(obj.title == event.title){
		        			
		        			var arr = [];
		        			arr = obj.start.split('T');
		        			console.log(arr[0]);
		        			console.log($filter('date')(event._start._d, 'yyyy-MM-dd'));
		        			if(arr[0] == $filter('date')(event._start._d, 'yyyy-MM-dd')){
		        				
		        				if(obj.id != event.id){
		        					flagDataAb = 1;
		        				}
		        			}
		        		}
		        	 });
		        	 if(flagDataAb == 0){
		            	
		           
		        		 if(event._start._i instanceof Object == true){
		        			 $scope.saveObj.originStartDate = "0";
		        		 }else{
		        			 $scope.saveObj.originStartDate = event._start._i;
		        		 }
		        		 	$scope.saveObj.portalName = event.title;
			            	$scope.saveObj.contractDurStartDate = event._start._d;
			            	if(event._end == null){
			            		$scope.saveObj.contractDurEndDate = "0";
			            		$scope.saveObj.originEndDate = "0";
			            	}else{
			            		$scope.saveObj.contractDurEndDate = event._end._d;
			            		$scope.saveObj.originEndDate = event._end._i;
			            	}
			            	$scope.saveObj.openTime = delta._milliseconds;
			            	
			            	apiservice.saveNewEvent($scope.saveObj).then(function(success){
		            			console.log("succ");
			            	});			
			            	
		        	 }else{
		            	$('#calendar').fullCalendar('removeEvents',event.id);
		            	$('#eventExist').modal('show');
		        	 }
		        },
		        
		        
		        eventResize: function (event, delta, revertFunc, jsEvent, ui, view) {
		        	
		        	$scope.saveObj = {};
		        
		            $scope.saveObj.originStartDate = "0";
		            	$scope.saveObj.portalName = event.title;
		            	$scope.saveObj.contractDurStartDate = event._start._d;
		            	if(event._end == null){
		            		$scope.saveObj.contractDurEndDate = "0";
		            		$scope.saveObj.originEndDate = "0";
		            	}else{
		            		$scope.saveObj.contractDurEndDate = event._end._d;
		            		$scope.saveObj.originEndDate = event._end._i;
		            	}
		            	$scope.saveObj.openTime = delta._milliseconds;
		            	
		            	
		            	apiservice.saveNewEvent($scope.saveObj).then(function(success){
	            			console.log("succ");
		            	});	
		            
		        },
		        eventRender: function(event, element) {
		        	$scope.eventdataValue.push(event);
		        	//element.append( "<span ng-click='deleteActivityModal()'>X</span>" );
		        	element.bind('mousedown', function (e) {
		        		console.log(e);
		        		console.log(event);
		                if (e.which == 3) {
		                	$scope.eventdata = event;
		                	$('#deleteForeverModal').modal('show');
		                }
		            });
                
		        },
		        disableResizing: true,
		        // this allows things to be dropped onto the calendar !!!
		        drop:function(date, allDay) { // this function is called when something is dropped
		        	console.log(date);
		        	console.log(allDay);
		        	$scope.saveObj = {};
		            // retrieve the dropped element's stored Event Object
		            var originalEventObject = $(this).data('eventObject');
		            // we need to copy it, so that multiple events don't have a reference to the same object
		            var copiedEventObject = $.extend({}, originalEventObject);
		            console.log(copiedEventObject);
		            console.log($filter('date')(date._d, 'yyyy-MM-dd'));
		            var flagDataAb = 0;
		            angular.forEach($scope.eventdataValue, function(obj, index){
		            	console.log(obj.backgroundColor);
		        		if(obj.title == copiedEventObject.title){
		        			//var arr = [];
		        			//arr = obj._start._d.split('T');
		        			if($filter('date')(obj._start._d, 'yyyy-MM-dd') == $filter('date')(date._d, 'yyyy-MM-dd')){
		        				flagDataAb = 1;
		        			}
		        			copiedEventObject.backgroundColor = obj.backgroundColor;
		        		}
					});
		            if(flagDataAb == 0){
		            	// assign it the date that was reported
			            copiedEventObject.start = date;
			            copiedEventObject.allDay = false;

			            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
			            	$scope.saveObj.portalName = copiedEventObject.title;
			            	$scope.saveObj.contractDurStartDate = date._d;
			            	$scope.saveObj.originStartDate = "0";
			            	$scope.saveObj.contractDurEndDate = "0";
			            	apiservice.saveNewEvent($scope.saveObj).then(function(success){
		            			console.log("succ");
			            	});	
			        }else{
		            	$('#eventExist').modal('show');
		            }
		            if ($('#drop-remove').is(':checked')) {
		                // if so, remove the element from the "Draggable Events" list
		                $(this).remove();
		            }
		        }
		        
			});
				$('#calendar').fullCalendar('removeEvents');
				$("#someID").change(function () {
					$('#yourCalendar').fullCalendar('refetchEvents');
				});
				$('#calendar').fullCalendar( 'removeEventSource',eventList)
				$('#calendar').fullCalendar( 'addEventSource', eventList)
				
	}
	
}]);	