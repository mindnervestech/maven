angular.module('newApp').controller('mainCtrl',
    ['$scope', 'applicationService', 'quickViewService', 'builderService', 'pluginsService', '$location','$http','$interval',
        function ($scope, applicationService, quickViewService, builderService, pluginsService, $location,$http,$interval) {
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
           
            
            $http.get('/getUserInfo').success(function(data,status, headers, config){
            	
            	$scope.name = data.firstName + " " + data.lastName;
            	if(data.imageName == null){
            		$scope.userimage = data.imageUrl;
            	}else{
            		$scope.userimage = "http://glider-autos.com/glivrImg/images"+data.imageUrl;
            	}

        		console.log($scope.userimage);
            	
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
            				 console.log($scope.locationName);
            				 
            			});	
                    
                     }
                }else{
                	$scope.setFalg = 1;
                	$scope.locationName=data.locationName;
                	console.log("::locationname");
                	console.log($scope.locationName);
                }
               
            })
            
            var promo =  $interval(function(){
				
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
                        console.log("getJsonBForTextInput====", jsonObject);
                        return isHideComponent(jsonObject); 
                      }
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
                        console.log("-?>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", jsonObject);
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
