// Ready Made Collection
angular.module('newApp').controller('ReadyMadeCollectionCtrl',['$scope','$http','$location','$filter','$routeParams','$interval', function ($scope,$http,$location,$filter,$routeParams,$interval) {

	$scope.tempDate = new Date().getTime();
	$scope.txt='Collections';
	$http.get('/getReadyMadeCollection')
		.success(function(data) {
			$scope.gridOptions.data = data;
			$scope.contactsList = data;
			console.log(data);
		});
	
	  $scope.gridOptions = {
			  	paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
			  	//rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>",
			    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
			    columnDefs: [
								{ name: 'count', displayName: 'Index No', width:'10%',cellEditableCondition: false,enableFiltering: false,
									  cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row)}}</div>',
								},
								{ name: 'title', displayName: 'Collection Title', width:'25%',cellEditableCondition: false,enableFiltering: true,
	                                	
                                 },
                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'25%',cellEditableCondition: false,
                                	
                                 },
                                { name: 'image_name', displayName: 'Image Name', width:'25%',cellEditableCondition: false,enableFiltering: false,
									 	
								 },
                                 { name: 'edit', displayName: 'Edit', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
									 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editcollection(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteCollection(row)"></i>', 
                                
                                },
                            ]
			  };
	  
	  $http.get('/getAllSection')
		.success(function(data) {
			console.log(data);
			$scope.secList = data;
		});
	  
	  $scope.gridOptions.onRegisterApi = function(gridApi){
			 $scope.gridApi = gridApi;
			 
	   		$scope.gridApi.core.on.filterChanged( $scope, function() {
		          var grid = this.grid;
		        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[1].filters[0].term,'description':grid.columns[2].filters[0].term},undefined);
		        });
		};
$scope.goToaccessories = function() {
	$location.path('/accessoriesInventory');
}
$scope.goToSale = function() {
	$location.path('/sale');
}
$scope.goToSection = function(id) {
	console.log(id);
	$location.path('/sectionInventory/'+id);
}
$scope.goToCollection = function() {
	$location.path('/collection');
}
$scope.goToReadyMade = function() {
	$location.path('/inventory');
}
$scope.goToReadyMadeOnly = function() {
	$location.path('/readyMadeOnly');
}
$scope.goToproducts = function() {
	$location.path('/productsInventory');	
}
$scope.goToAllProducts = function() {
	$location.path('/allproductsInventory');	
}
$scope.editcollection = function(row) {
	$scope.flag = "readymade";
	$location.path('/editcollection/'+row.entity.id+'/'+$scope.flag);
}
$scope.deleteCollection = function(row){
	   $('#deleteModalc').click();
	   $scope.rowDataVal = row;
}

$scope.deleteCollectionRow = function() {
	console.log("RM Collection Delete");
	$http.get('/deleteCollectionById/'+$scope.rowDataVal.entity.id)
	.success(function(data) {
		$.pnotify({
		    title: "Success",
		    type:'success',
		    text: "Delete successfully",
		});
		 $http.get('/getReadyMadeCollection')
			.success(function(data) {
			$scope.gridOptions.data = data;
		});
	});
}	  
	  
			  
			  
}]).filter('myProgressType1', function() {
			  return function (input) {
			    return +input > 65 ? 'danger' : (+input > 35 ? 'warning' : 'success');
			  };;
			});

			(function() {
			    'use strict';

			    angular.module('ui.grid.draggable-rows', ['ui.grid'])

			    .constant('uiGridDraggableRowsConstants', {
			        featureName: 'draggableRows',
			        ROW_OVER_CLASS: 'ui-grid-draggable-row-over',
			        ROW_OVER_ABOVE_CLASS: 'ui-grid-draggable-row-over--above',
			        ROW_OVER_BELOW_CLASS: 'ui-grid-draggable-row-over--below',
			        POSITION_ABOVE: 'above',
			        POSITION_BELOW: 'below',
			        publicEvents: {
			            draggableRows: {
			                rowDragged: function(scope, info, rowElement) {},
			                rowDropped: function(scope, info, targetElement) {},
			                rowOverRow: function(scope, info, rowElement) {},
			                rowEnterRow: function(scope, info, rowElement) {},
			                rowLeavesRow: function(scope, info, rowElement) {},
			                rowFinishDrag: function(scope) {}
			            }
			        }
			    })

			    .factory('uiGridDraggableRowsCommon', [function() {
			        return {
			            draggedRow: null,
			            draggedRowEntity: null,
			            position: null,
			            fromIndex: null,
			            toIndex: null
			        };
			    }])

			    .service('uiGridDraggableRowsService', ['uiGridDraggableRowsConstants', function(uiGridDraggableRowsConstants) {
			        this.initializeGrid = function(grid, $scope, $element) {
			            grid.api.registerEventsFromObject(uiGridDraggableRowsConstants.publicEvents);

			            grid.api.draggableRows.on.rowFinishDrag($scope, function() {
			                angular.forEach($element[0].querySelectorAll('.' + uiGridDraggableRowsConstants.ROW_OVER_CLASS), function(row) {
			                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
			                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
			                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
			                });
			            });
			        };
			    }])

			    .service('uiGridDraggableRowService', ['uiGridDraggableRowsConstants', 'uiGridDraggableRowsCommon', function(uiGridDraggableRowsConstants, uiGridDraggableRowsCommon) {
			        var move = function(from, to) {
			            /*jshint validthis: true */
			            this.splice(to, 0, this.splice(from, 1)[0]);
			        };

			        this.prepareDraggableRow = function($scope, $element) {
			            var grid = $scope.grid;
			            var data = grid.options.data;
			            var row = $element[0];
			            var listeners = {
			                onDragOverEventListener: function(e) {
			                    if (e.preventDefault) {
			                        e.preventDefault();
			                    }

			                    var dataTransfer = e.dataTransfer || e.originalEvent.dataTransfer;
			                    dataTransfer.effectAllowed = 'copyMove';
			                    dataTransfer.dropEffect = 'move';

			                    var offset = e.offsetY || e.layerY || (e.originalEvent ? e.originalEvent.offsetY : 0);

			                    $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_CLASS);

			                    if (offset < this.offsetHeight / 2) {
			                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_ABOVE;

			                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
			                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);

			                    } else {
			                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_BELOW;

			                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
			                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
			                    }

			                    //grid.api.draggableRows.raise.rowOverRow(uiGridDraggableRowsCommon, this);
			                },

			                onDragStartEventListener: function(e) {
			                    this.style.opacity = '0.5';

			                    uiGridDraggableRowsCommon.draggedRow = this;
			                    uiGridDraggableRowsCommon.draggedRowEntity = $scope.$parent.$parent.row.entity;

			                    uiGridDraggableRowsCommon.position = null;

			                    uiGridDraggableRowsCommon.fromIndex = data.indexOf(uiGridDraggableRowsCommon.draggedRowEntity);
			                    uiGridDraggableRowsCommon.toIndex = null;

			                   // grid.api.draggableRows.raise.rowDragged(uiGridDraggableRowsCommon, this);
			                },

			                onDragLeaveEventListener: function() {
			                    this.style.opacity = '1';

			                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
			                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
			                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);

			                   // grid.api.draggableRows.raise.rowLeavesRow(uiGridDraggableRowsCommon, this);
			                    
			                },

			                onDragEnterEventListener: function() {
			                   // grid.api.draggableRows.raise.rowEnterRow(uiGridDraggableRowsCommon, this);
			                },

			                onDragEndEventListener: function() {
			                   // grid.api.draggableRows.raise.rowFinishDrag();
			                },

			                onDropEventListener: function(e) {
			                    var draggedRow = uiGridDraggableRowsCommon.draggedRow;

			                    if (e.stopPropagation) {
			                        e.stopPropagation();
			                    }

			                    if (e.preventDefault) {
			                        e.preventDefault();
			                    }

			                    if (draggedRow === this) {
			                        return false;
			                    }

			                    uiGridDraggableRowsCommon.toIndex = data.indexOf($scope.$parent.$parent.row.entity);

			                    if (uiGridDraggableRowsCommon.position === uiGridDraggableRowsConstants.POSITION_ABOVE) {
			                        if (uiGridDraggableRowsCommon.fromIndex < uiGridDraggableRowsCommon.toIndex) {
			                            uiGridDraggableRowsCommon.toIndex -= 1;
			                        }

			                    } else if (uiGridDraggableRowsCommon.fromIndex >= uiGridDraggableRowsCommon.toIndex) {
			                        uiGridDraggableRowsCommon.toIndex += 1;
			                    }

			                    $scope.$apply(function() {
			                        move.apply(data, [uiGridDraggableRowsCommon.fromIndex, uiGridDraggableRowsCommon.toIndex]);
			                        //console.log(data);
			                    });
			                    console.log(data);	
			                    	console.log("............................");
			                    	$.ajax({
				                    		type: "POST",
				                            url: '/saveCollectionOrder',
				                            data: JSON.stringify(data),
				                            contentType: "application/json; charset=utf-8",
				                            dataType: "json",
			                    		});
			                    
			                   // grid.api.draggableRows.raise.rowDropped(uiGridDraggableRowsCommon, this);

			                    e.preventDefault();
			                }
			            };

			            row.addEventListener('dragover', listeners.onDragOverEventListener, false);
			            row.addEventListener('dragstart', listeners.onDragStartEventListener, false);
			            row.addEventListener('dragleave', listeners.onDragLeaveEventListener, false);
			            row.addEventListener('dragenter', listeners.onDragEnterEventListener, false);
			            row.addEventListener('dragend', listeners.onDragEndEventListener, false);
			            row.addEventListener('drop', listeners.onDropEventListener);
			        };
			    }])

			    .directive('uiGridDraggableRow', ['uiGridDraggableRowService', function(uiGridDraggableRowService) {
			        return {
			            restrict: 'ACE',
			            scope: {
			                grid: '='
			            },
			            compile: function() {
			                return {
			                    pre: function($scope, $element) {
			                        uiGridDraggableRowService.prepareDraggableRow($scope, $element);
			                    }
			                };
			            }
			        };
			    }])

			    .directive('uiGridDraggableRows', ['uiGridDraggableRowsService', function(uiGridDraggableRowsService) {
			        return {
			            restrict: 'A',
			            replace: true,
			            priority: 0,
			            require: 'uiGrid',
			            scope: false,
			            compile: function() {
			                return {
			                    pre: function($scope, $element, $attrs, uiGridCtrl) {
			                        uiGridDraggableRowsService.initializeGrid(uiGridCtrl.grid, $scope, $element);
			                    }
			                };
			            }
			        };
			    }]);
			}());


/*  Sale product  */
  
 angular.module('newApp')
.controller('SaleInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.tempDate = new Date().getTime();
	$scope.txt='Sold';
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
												{ name: 'title', displayName: 'Product Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
													cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'20%',cellEditableCondition: false,
		 		                                	
		 		                                 },
		 		                                { name: 'collectionTitle', displayName: 'In Collection', width:'15%',cellEditableCondition: false,enableFiltering: true,
													 	
												 },
		 		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'cost', displayName: 'Cost',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'countImages', displayName: 'No of Images',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                 { name: 'edit', displayName: 'Edit', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
		 		                                	cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProduct(row)"></i>&nbsp;&nbsp;&nbsp;<a href="" ng-click="grid.appScope.moveProduct(row)"><u>Move To Ready Made</u></a>', 
	    		                                 
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getAllSale')
				.success(function(data) {
				$scope.gridOptions.data = data;
				console.log(data);
				$scope.contactsList = data;
			});
		  
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.mouse = function(row) {
				console.log(row.entity.imgId);
				$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
				$('#thumb_image').show();
				$('#imagePopup').modal();
			};
			$scope.mouseout = function(row) {
				$('#imgClose').click();
			};
		  
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'collectionTitle':grid.columns[2].filters[0].term,'description':grid.columns[1].filters[0].term},undefined);
			        });
			};
		  
	$scope.goToaccessories = function() {
		$location.path('/accessoriesInventory');
	}
	$scope.goToSale = function() {
		$location.path('/sale');
	}
	$scope.goToSection = function(id) {
		  console.log(id);
		  $location.path('/sectionInventory/'+id);
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToreadyMadeCollection = function() {
		$location.path('/allReadyMadeCollection');
	}
	$scope.goToReadyMadeOnly = function() {
		$location.path('/readyMadeOnly');
	}
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.goToAllProducts = function() {
		$location.path('/allproductsInventory');	
	}
	$scope.editProduct = function(row) {
		$scope.flag = "sale";
		$location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
	}	
	$scope.moveProduct = function(row) {
		console.log(row.entity.id+"  "+row.entity.title);
		console.log("move product function");
		$http.get('/moveProductById/'+row.entity.id)
		.success(function(data) {
			console.log("success");
			$http.get('/getAllSale')
			.success(function(data) {
			$scope.gridOptions.data = data;
			console.log(data);
		});
		});
	}
	$scope.saleProduct = function(row) {
		console.log(row.entity.id+"  "+row.entity.title);
		console.log("sale product function");
		$http.get('/saleProductById/'+row.entity.id)
		.success(function(data) {
			console.log("success");
		});
	}
	$scope.updateProduct = function(row) {
		$scope.productData.id = $routeParams.id
		$http.post('/updateProduct',$scope.productData)
			.success(function(data) {
				console.log('success');
				
				
			});
	}	 

	

	$scope.deleteProduct = function(row){
		console.log('avfvvv');
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteProductRow = function() {
		   $http.get('/deleteProductById/'+$scope.rowDataVal.entity.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				});
				 $http.get('/getAllSale')
					.success(function(data) {
					$scope.gridOptions.data = data;
				});
			});
	}


}]);
 
  
 /* Ready Made product Only  */
 
 angular.module('newApp')
.controller('OnlyReadyMadeInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.tempDate = new Date().getTime();
	$scope.txt='Products';
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
		 		                                 { name: 'title', displayName: 'Product Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
		 		                                	cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'20%',cellEditableCondition: false,
		 		                                	
		 		                                 },
												{ name: 'collectionTitle', displayName: 'In Collection', width:'15%',cellEditableCondition: false,enableFiltering: true,
													 	
												 },
		 		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'cost', displayName: 'Cost',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'countImages', displayName: 'No of Images',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                 { name: 'edit', displayName: 'Edit', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProduct(row)"></i>&nbsp;&nbsp;&nbsp;<a href="" ng-click="grid.appScope.saleProduct(row)"><u>Mark as Sold</u></a>', 
	    		                                 
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getOnlyReadyMade')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
				console.log(data);
			});
		  
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.mouse = function(row) {
				console.log(row.entity.imgId);
				$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
				$('#thumb_image').show();
				$('#imagePopup').modal();
			};
			$scope.mouseout = function(row) {
				$('#imgClose').click();
			};
		  
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'description':grid.columns[1].filters[0].term,'collectionTitle':grid.columns[2].filters[0].term},undefined);
			        });
			};
			
	$scope.goToaccessories = function() {
		$location.path('/accessoriesInventory');
	}
	$scope.goToreadyMadeCollection = function() {
		$location.path('/allReadyMadeCollection');
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToSale = function() {
		$location.path('/sale');
	}
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.goToSection = function(id) {
		console.log(id);
		$location.path('/sectionInventory/'+id);
	}
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToReadyMadeOnly = function() {
		$location.path('/readyMadeOnly');
	}
	$scope.goToAllProducts = function() {
		$location.path('/allproductsInventory');	
	}
	$scope.editProduct = function(row) {
		$scope.flag = "readymade";
		$location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
	}	
	$scope.saleProduct = function(row) {
		console.log(row.entity.id+"  "+row.entity.title);
		console.log("sale product function");
		$http.get('/saleProductById/'+row.entity.id)
		.success(function(data) {
			console.log("success");
			$http.get('/getOnlyReadyMade')
			.success(function(data) {
			$scope.gridOptions.data = data;
			console.log(data);
		});
		});
	}
	$scope.updateProduct = function(row) {
		$scope.productData.id = $routeParams.id
		$http.post('/updateProduct',$scope.productData)
			.success(function(data) {
				console.log('success');
				
				
			});
	}	 

	

	$scope.deleteProduct = function(row){
		console.log('avfvvv');
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteProductRow = function() {
		   $http.get('/deleteProductById/'+$scope.rowDataVal.entity.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				});
				 $http.get('/getOnlyReadyMade')
					.success(function(data) {
					$scope.gridOptions.data = data;
				});
			});
	}


}]);



angular.module('newApp')
.controller('ReadyMadeInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload','$cookieStore', function ($scope,$http,$location,$filter,$routeParams,$upload,$cookieStore) {


	$scope.tempDate = new Date().getTime();
	$scope.txt='Products';
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
		 		                                 { name: 'title', displayName: 'Product Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
		 		                                	cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'20%',cellEditableCondition: false,
		 		                                	
		 		                                 },
												{ name: 'collectionTitle', displayName: 'In Collection', width:'15%',cellEditableCondition: false,enableFiltering: true,
													 	
												 },
		 		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'cost', displayName: 'Cost',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'countImages', displayName: 'No of Images',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                 { name: 'edit', displayName: 'Edit', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProduct(row)"></i>&nbsp;&nbsp;&nbsp;<a ng-click="grid.appScope.saleProduct(row)"><u>Mark as Sold</u></a>', 
	    		                                 
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getAllReadyMade')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
			});
		  
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.mouse = function(row) {
				console.log(row.entity.imgId);
				$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
				$('#thumb_image').show();
				$('#imagePopup').modal();
			};
			$scope.mouseout = function(row) {
				//$('#imagePopup').modal('hide');
				$('#imgClose').click();
			};
			
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'description':grid.columns[1].filters[0].term,'collectionTitle':grid.columns[2].filters[0].term},undefined);
			        });
			};
		  
	$scope.goToaccessories = function() {
		$location.path('/accessoriesInventory');
	}
	$scope.goToreadyMadeCollection = function() {
		$location.path('/allReadyMadeCollection');
	}
	$scope.goToSale = function() {
		$location.path('/sale');
	}
	$scope.goToReadyMadeOnly = function() {
		$location.path('/readyMadeOnly');
	}
	$scope.goToSection = function(id) {
		console.log(id);
		$location.path('/sectionInventory/'+id);
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToAllProducts = function() {
		$location.path('/allproductsInventory');	
	}
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.editProduct = function(row) {
		$scope.flag = "readymade";
		$location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
	}	
	$scope.saleProduct = function(row) {
		console.log(row.entity.id+"  "+row.entity.title);
		console.log("sale product function");
		$http.get('/saleProductById/'+row.entity.id)
		.success(function(data) {
			console.log("success");
			$http.get('/getOnlyReadyMade')
			.success(function(data) {
			$scope.gridOptions.data = data;
			console.log(data);
		});
		});
		
	}
	$scope.updateProduct = function(row) {
		$scope.productData.id = $routeParams.id
		$http.post('/updateProduct',$scope.productData)
			.success(function(data) {
				console.log('success');
				
				
			});
	}	 

	

	$scope.deleteProduct = function(row){
		console.log('avfvvv');
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteProductRow = function() {
		   $http.get('/deleteProductById/'+$scope.rowDataVal.entity.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				});
				 $http.get('/getAllReadyMade')
					.success(function(data) {
					$scope.gridOptions.data = data;
				});
			});
	}


}]);

angular.module('newApp')
.controller('AccessoriesInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
												{ name: 'title', displayName: 'Collection Title', width:'25%',cellEditableCondition: false,enableFiltering: true,
												 	
												 },
		 		                                 { name: 'website', displayName: 'Collection Link', width:'25%',cellEditableCondition: false,enableFiltering: true,
		 		                                	
		 		                                 },
		 		                                 { name: 'image_name', displayName: 'Image Name',enableFiltering: false, width:'25%',cellEditableCondition: false,
		 		                                	
		 		                                 },
		 		                                 { name: 'edit', displayName: 'Edit', width:'25%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
		 		                                	 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editcollection(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteCollection(row)"></i>',
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getAllAccessories')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
			});
		  
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'website':grid.columns[1].filters[0].term},undefined);
			        });
			};
		  
		  
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.editcollection = function(row) {
		$scope.flag = "accessories";
		$location.path('/editcollection/'+row.entity.id+'/'+$scope.flag);
		//$location.path('/editcollection/'+row.entity.id);
	}
	$scope.goToSection = function(id) {
		$location.path('/sectionInventory/'+id);
	}
	$scope.deleteCollection = function(row){
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteCollectionRow = function() {
		$http.get('/deleteCollectionById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Delete successfully",
			});
			 $http.get('/getAllAccessories')
				.success(function(data) {
				$scope.gridOptions.data = data;
			});
		});
	}
	
}]);

angular.module('newApp')
.controller('ProductsInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.tempDate = new Date().getTime();
	$scope.txt='Products';
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
		 		                                 { name: 'title', displayName: 'Product Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
		 		                                	cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'20%',cellEditableCondition: false,
		 		                                	
		 		                                 },
												{ name: 'collectionTitle', displayName: 'Collection Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
													 	
												 },
		 		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'cost', displayName: 'Cost',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'countImages', displayName: 'No of Images',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'edit', displayName: 'Edit', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProduct(row)"></i>', 
	    		                                 
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getAllProductTypes')
				.success(function(data) {
				console.log(data);
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
				
			});
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $('#cls').hide();
		  $scope.mouse = function(row) {
				console.log(row.entity.imgId);
				$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
				$('#thumb_image').show();
				$('#imagePopup').modal();
			};
			$scope.mouseout = function(row) {
				$('#imgClose').click();
			};
			
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'description':grid.columns[1].filters[0].term,'collectionTitle':grid.columns[2].filters[0].term},undefined);
			        });
			};
	
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.goToProductCollection = function() {
		console.log("Pro...");
		$location.path('/allProductCollection');
	}
	$scope.goToreadyMadeCollection = function() {
		$location.path('/allReadyMadeCollection');
	}
	$scope.goToSection = function(id) {
		$location.path('/sectionInventory/'+id);
	}
	$scope.goToAllProducts = function() {
		$location.path('/allproductsInventory');	
	}
	$scope.goToaccessories = function() {
		$location.path('/accessoriesInventory');
	}
	$scope.editProduct = function(row) {
		$scope.flag = "product";
		$location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
	}	 
	$scope.updateProduct = function(row) {
		$scope.productData.id = $routeParams.id
		$http.post('/updateProduct',$scope.productData)
			.success(function(data) {
				console.log('success');
				
				
			});
	}	 



	$scope.deleteProduct = function(row){
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteProductRow = function() {
		   $http.get('/deleteProductById/'+$scope.rowDataVal.entity.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				});
				 $http.get('/getAllProductTypes')
					.success(function(data) {
					$scope.gridOptions.data = data;
				});
				/*if($scope.rowDataVal.entity.status == 'Newly Arrived') {
					 $scope.viewVehiclesInit();
				} 
				if($scope.rowDataVal.entity.status == 'Sold') {
					$scope.soldTab();
				}*/
			});
	}

	
}]);

// Product Collection

angular.module('newApp')
.controller('ProductCollectionCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.txt='Collections';
/*	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
												{ name: 'title', displayName: 'Collection Title', width:'25%',cellEditableCondition: false,enableFiltering: true,
		 		                                	
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'25%',cellEditableCondition: false,
		 		                                	
		 		                                 },
		 		                                { name: 'image_name', displayName: 'Image Name', width:'25%',cellEditableCondition: false,enableFiltering: false,
													 	
												 },
		 		                                 { name: 'edit', displayName: 'Edit', width:'25%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
													 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editcollection(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteCollection(row)"></i>', 
	    		                                 
	    		                                 },
	     		                                 ];*/
	  
		  $http.get('/getProductCollection')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
				console.log(data);
			});
		  
		  $scope.gridOptions = {
				  	paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
		 		    paginationPageSize: 150,
		 		    enableFiltering: true,
		 		    useExternalFiltering: true,
				  	//rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>",
				    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
				    columnDefs: [
									{ name: 'count', displayName: 'Index No', width:'10%',cellEditableCondition: false,enableFiltering: false,
										  cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row)}}</div>',
									},
									{ name: 'title', displayName: 'Collection Title', width:'25%',cellEditableCondition: false,enableFiltering: true,
		                                	
	                                 },
	                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'25%',cellEditableCondition: false,
	                                	
	                                 },
	                                { name: 'image_name', displayName: 'Image Name', width:'25%',cellEditableCondition: false,enableFiltering: false,
										 	
									 },
	                                 { name: 'edit', displayName: 'Edit', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
										 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editcollection(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteCollection(row)"></i>', 
	                                 
	                                 },
	                            ]
				  };		  
		  
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[1].filters[0].term,'description':grid.columns[2].filters[0].term},undefined);
			        });
			};
		  
	$scope.goToaccessories = function() {
		console.log("Acc Call..");
		$location.path('/accessoriesInventory');
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToproducts = function() {
		console.log("Product..");
		$location.path('/productsInventory');	
	}
	$scope.goToAllProducts = function() {
		console.log("Call..");
		$location.path('/allproductsInventory');	
	}
	$scope.editcollection = function(row) {
		$scope.flag = "product";
		$location.path('/editcollection/'+row.entity.id+'/'+$scope.flag);
	}
	$scope.deleteCollection = function(row){
		   $('#deleteModalc').click();
		   $scope.rowDataVal = row;
	}
	$scope.goToSection = function(id) {
		$location.path('/sectionInventory/'+id);
	}

	$scope.deleteCollectionRow = function() {
		console.log("Pro Collection Delete");
		$http.get('/deleteCollectionById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Delete successfully",
			});
			 $http.get('/getProductCollection')
				.success(function(data) {
				$scope.gridOptions.data = data;
			});
		});
	}

}]).filter('myProgressType', function() {
	  return function (input) {
	    return +input > 65 ? 'danger' : (+input > 35 ? 'warning' : 'success');
	  };;
	});

	(function() {
	    'use strict';

	    angular.module('ui.grid.draggable-rows', ['ui.grid'])

	    .constant('uiGridDraggableRowsConstants', {
	        featureName: 'draggableRows',
	        ROW_OVER_CLASS: 'ui-grid-draggable-row-over',
	        ROW_OVER_ABOVE_CLASS: 'ui-grid-draggable-row-over--above',
	        ROW_OVER_BELOW_CLASS: 'ui-grid-draggable-row-over--below',
	        POSITION_ABOVE: 'above',
	        POSITION_BELOW: 'below',
	        publicEvents: {
	            draggableRows: {
	                rowDragged: function(scope, info, rowElement) {},
	                rowDropped: function(scope, info, targetElement) {},
	                rowOverRow: function(scope, info, rowElement) {},
	                rowEnterRow: function(scope, info, rowElement) {},
	                rowLeavesRow: function(scope, info, rowElement) {},
	                rowFinishDrag: function(scope) {}
	            }
	        }
	    })

	    .factory('uiGridDraggableRowsCommon', [function() {
	        return {
	            draggedRow: null,
	            draggedRowEntity: null,
	            position: null,
	            fromIndex: null,
	            toIndex: null
	        };
	    }])

	    .service('uiGridDraggableRowsService', ['uiGridDraggableRowsConstants', function(uiGridDraggableRowsConstants) {
	        this.initializeGrid = function(grid, $scope, $element) {
	            grid.api.registerEventsFromObject(uiGridDraggableRowsConstants.publicEvents);

	            grid.api.draggableRows.on.rowFinishDrag($scope, function() {
	                angular.forEach($element[0].querySelectorAll('.' + uiGridDraggableRowsConstants.ROW_OVER_CLASS), function(row) {
	                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
	                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
	                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
	                });
	            });
	        };
	    }])

	    .service('uiGridDraggableRowService', ['uiGridDraggableRowsConstants', 'uiGridDraggableRowsCommon', function(uiGridDraggableRowsConstants, uiGridDraggableRowsCommon) {
	        var move = function(from, to) {
	            /*jshint validthis: true */
	            this.splice(to, 0, this.splice(from, 1)[0]);
	        };

	        this.prepareDraggableRow = function($scope, $element) {
	            var grid = $scope.grid;
	            var data = grid.options.data;
	            var row = $element[0];
	            var listeners = {
	                onDragOverEventListener: function(e) {
	                    if (e.preventDefault) {
	                        e.preventDefault();
	                    }

	                    var dataTransfer = e.dataTransfer || e.originalEvent.dataTransfer;
	                    dataTransfer.effectAllowed = 'copyMove';
	                    dataTransfer.dropEffect = 'move';

	                    var offset = e.offsetY || e.layerY || (e.originalEvent ? e.originalEvent.offsetY : 0);

	                    $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_CLASS);

	                    if (offset < this.offsetHeight / 2) {
	                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_ABOVE;

	                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
	                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);

	                    } else {
	                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_BELOW;

	                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
	                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
	                    }

	                    //grid.api.draggableRows.raise.rowOverRow(uiGridDraggableRowsCommon, this);
	                },

	                onDragStartEventListener: function(e) {
	                    this.style.opacity = '0.5';

	                    uiGridDraggableRowsCommon.draggedRow = this;
	                    uiGridDraggableRowsCommon.draggedRowEntity = $scope.$parent.$parent.row.entity;

	                    uiGridDraggableRowsCommon.position = null;

	                    uiGridDraggableRowsCommon.fromIndex = data.indexOf(uiGridDraggableRowsCommon.draggedRowEntity);
	                    uiGridDraggableRowsCommon.toIndex = null;

	                   // grid.api.draggableRows.raise.rowDragged(uiGridDraggableRowsCommon, this);
	                },

	                onDragLeaveEventListener: function() {
	                    this.style.opacity = '1';

	                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
	                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
	                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);

	                   // grid.api.draggableRows.raise.rowLeavesRow(uiGridDraggableRowsCommon, this);
	                    
	                },

	                onDragEnterEventListener: function() {
	                   // grid.api.draggableRows.raise.rowEnterRow(uiGridDraggableRowsCommon, this);
	                },

	                onDragEndEventListener: function() {
	                   // grid.api.draggableRows.raise.rowFinishDrag();
	                },

	                onDropEventListener: function(e) {
	                    var draggedRow = uiGridDraggableRowsCommon.draggedRow;

	                    if (e.stopPropagation) {
	                        e.stopPropagation();
	                    }

	                    if (e.preventDefault) {
	                        e.preventDefault();
	                    }

	                    if (draggedRow === this) {
	                        return false;
	                    }

	                    uiGridDraggableRowsCommon.toIndex = data.indexOf($scope.$parent.$parent.row.entity);

	                    if (uiGridDraggableRowsCommon.position === uiGridDraggableRowsConstants.POSITION_ABOVE) {
	                        if (uiGridDraggableRowsCommon.fromIndex < uiGridDraggableRowsCommon.toIndex) {
	                            uiGridDraggableRowsCommon.toIndex -= 1;
	                        }

	                    } else if (uiGridDraggableRowsCommon.fromIndex >= uiGridDraggableRowsCommon.toIndex) {
	                        uiGridDraggableRowsCommon.toIndex += 1;
	                    }

	                    $scope.$apply(function() {
	                        move.apply(data, [uiGridDraggableRowsCommon.fromIndex, uiGridDraggableRowsCommon.toIndex]);
	                        //console.log(data);
	                    });
	                    console.log(data);	
	                    	console.log("............................");
	                    	$.ajax({
		                    		type: "POST",
		                            url: '/saveCollectionOrder',
		                            data: JSON.stringify(data),
		                            contentType: "application/json; charset=utf-8",
		                            dataType: "json",
	                    		});
	                    
	                   // grid.api.draggableRows.raise.rowDropped(uiGridDraggableRowsCommon, this);

	                    e.preventDefault();
	                }
	            };

	            row.addEventListener('dragover', listeners.onDragOverEventListener, false);
	            row.addEventListener('dragstart', listeners.onDragStartEventListener, false);
	            row.addEventListener('dragleave', listeners.onDragLeaveEventListener, false);
	            row.addEventListener('dragenter', listeners.onDragEnterEventListener, false);
	            row.addEventListener('dragend', listeners.onDragEndEventListener, false);
	            row.addEventListener('drop', listeners.onDropEventListener);
	        };
	    }])

	    .directive('uiGridDraggableRow', ['uiGridDraggableRowService', function(uiGridDraggableRowService) {
	        return {
	            restrict: 'ACE',
	            scope: {
	                grid: '='
	            },
	            compile: function() {
	                return {
	                    pre: function($scope, $element) {
	                        uiGridDraggableRowService.prepareDraggableRow($scope, $element);
	                    }
	                };
	            }
	        };
	    }])

	    .directive('uiGridDraggableRows', ['uiGridDraggableRowsService', function(uiGridDraggableRowsService) {
	        return {
	            restrict: 'A',
	            replace: true,
	            priority: 0,
	            require: 'uiGrid',
	            scope: false,
	            compile: function() {
	                return {
	                    pre: function($scope, $element, $attrs, uiGridCtrl) {
	                        uiGridDraggableRowsService.initializeGrid(uiGridCtrl.grid, $scope, $element);
	                    }
	                };
	            }
	        };
	    }]);
	}());

angular.module('newApp')
.controller('AllProductsInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.tempDate = new Date().getTime();
	$scope.txt='Products';
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
		 		                                 { name: 'title', displayName: 'Product Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
		 		                                	
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'20%',cellEditableCondition: false,
		 		                                	
		 		                                 },
												{ name: 'collectionTitle', displayName: 'Collection Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
													 	
												 },
		 		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'cost', displayName: 'Cost',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'countImages', displayName: 'No of Images',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'edit', displayName: 'Edit', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProduct(row)"></i>', 
	    		                                 
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getAllProductTypes')
				.success(function(data) {
				console.log(data);
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
			});
	
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'collectionTitle':grid.columns[2].filters[0].term,'description':grid.columns[1].filters[0].term},undefined);
			        });
			};
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.goToProductCollection = function() {
		$location.path('/allProductCollection');
	}
	$scope.goToreadyMadeCollection = function() {
		$location.path('/allReadyMadeCollection');
	}
	$scope.goToAllProducts = function() {
		$location.path('/allproductsInventory');	
	}
	$scope.goToSection = function(id) {
		$location.path('/sectionInventory/'+id);
	}
	$scope.goToaccessories = function() {
		$location.path('/accessoriesInventory');
	}
	$scope.editProduct = function(row) {
		$scope.flag = "product";
		$location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
	}	 
	$scope.updateProduct = function(row) {
		$scope.productData.id = $routeParams.id
		$http.post('/updateProduct',$scope.productData)
			.success(function(data) {
				console.log('success');
				
				
			});
	}	 



	$scope.deleteProduct = function(row){
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteProductRow = function() {
		   $http.get('/deleteProductById/'+$scope.rowDataVal.entity.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				});
				 $http.get('/getAllProductTypes')
					.success(function(data) {
					$scope.gridOptions.data = data;
				});
				/*if($scope.rowDataVal.entity.status == 'Newly Arrived') {
					 $scope.viewVehiclesInit();
				} 
				if($scope.rowDataVal.entity.status == 'Sold') {
					$scope.soldTab();
				}*/
			});
	}

	
}]);
angular.module('newApp')
.controller('EditProductsCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$scope.collection="null";
	$scope.cId=null;
	$scope.newProd = false;
	$http.get('/getProductData/'+$routeParams.id)
	.success(function(data) {
			console.log("Update data = ");	
			console.log(data);	
			$scope.productData = data;
			$scope.cId = data.collectionId;
			$scope.collection = data.collectionTitle;
			
			if($scope.collection=="section"){
				console.log("section");
				$('#section').click();
			}if($scope.collection=="readymade"){
				console.log("readymade");
				$('#readymade').click();
			}if($scope.collection=="product"){
				console.log("product");
				$('#product').click();
			}
	});
	
	$scope.status= function(sts){
		$scope.collection = sts;
	}
	
	console.log($routeParams.id);

	$http.get('/getList').success(function(data) {
		console.log(data);
		$scope.CollectionList = data;
	
	});
	
console.log($scope.cId);
	$scope.collectionList = function(){
		$http.get('/getColl')
		.success(function(data) {
			$scope.readyCollection = data[0];
				/*angular.forEach($scope.readyCollection, function(value, index){
				
					if(value.collectionId == $scope.cId){
						console.log('aaaaaa');
						$scope.collection = 'readyMade';
					}
						
				});
*/			
			$scope.proCollection = data[1];
				/*angular.forEach($scope.proCollection, function(value, index){
					
					if(value.collectionId == $scope.cId){
						console.log('bbbbbbbbb');
						$scope.collection = "product";
					}
						
				});*/
			$scope.SectionCollection = data[2];
				
		});
		
	}
	/*
	$http.get('/getProductCollectionId/'+$routeParams.id)
	.success(function(data) {
		console.log('{{{{{{{{{}}}}}}}}}');
		   console.log(data);
		   $scope.collection = data;
		   console.log($scope.collection);
	});*/
	
	var logofile = null;
	var cadfile = null;
	var names = [];
	var files =[];
	$scope.updateProduct = function(){
		console.log("In update Product function");
		$scope.productData.id = $routeParams.id;
		console.log($scope.productData);
		console.log(logofile);
		console.log(cadfile);
		
		if(logofile != null && cadfile==null){
			console.log("logofile");
				$upload.upload({
		            url : '/updateProduct',
		            method: 'POST',
		            file:logofile,
		            data:$scope.productData
		        }).success(function(data) {
		        	$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Product Update successfully",
					});
		   			if($routeParams.flag=="sale")
		            	$location.path('/sale');
		            if($routeParams.flag=="product")
		            	$location.path('/productsInventory');
		            if($routeParams.flag=="readymade")
		            	$location.path('/readyMadeOnly');
		   			
		   		});	
		
		}else if(cadfile != null && logofile==null){
			console.log("cadfile");
			$upload.upload({
	            url : '/updateProduct',
	            method: 'POST',
	            file:cadfile,
	            data:$scope.productData
	        }).success(function(data) {
	        	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Product Update successfully",
				});
	   			if($routeParams.flag=="sale")
	            	$location.path('/sale');
	            if($routeParams.flag=="product")
	            	$location.path('/productsInventory');
	            if($routeParams.flag=="readymade")
	            	$location.path('/readyMadeOnly');
	   			
	   		});	
	
	}else if(logofile != undefined && cadfile != undefined){
		console.log("both");
		console.log(names);
		console.log(files.length);
		console.log(files);
		console.log("bothfile");
		 $upload.upload({
			 url : '/updateProduct',
	         method: 'POST',
            fileFormDataName: names,
            file:files,
            data:$scope.productData
         }).success(function(data) {
	        	$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Product Update successfully",
				});
	   			if($routeParams.flag=="sale")
	            	$location.path('/sale');
	            if($routeParams.flag=="product")
	            	$location.path('/productsInventory');
	            if($routeParams.flag=="readymade")
	            	$location.path('/readyMadeOnly');
	   			
	   		});
		}
		else{
		$scope.productData.id = $routeParams.id;
		console.log($scope.productData);
		$http.post('/updateProductInfo',$scope.productData)
   		.success(function(data) {
   			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Product Update successfully",
			});
   			if($routeParams.flag=="sale")
            	$location.path('/sale');
            if($routeParams.flag=="product")
            	$location.path('/productsInventory');
            if($routeParams.flag=="readymade")
            	$location.path('/readyMadeOnly');
   			
   		});
		}	
	}
	
	$scope.download = function(){
		$scope.productData.id = $routeParams.id;
		console.log("File path = "+$scope.productData.filePath);
		$.fileDownload('/downloadFile',
				{	  
				  httpMethod : "POST",
				  data : {
				  path : $scope.productData.filePath
				  }
				}).done(function(e, response)
				{
					console.log("Success");
				}).fail(function(e, response)
				{
					console.log("Fail");
				});
	}
	$scope.downloadCad = function(){
		$scope.productData.id = $routeParams.id;
		console.log("File path = "+$scope.productData.cadfilePath);
		$.fileDownload('/cadFileDownload',
				{	  
				  httpMethod : "POST",
				  data : {
				  path : $scope.productData.cadfilePath
				  }
				}).done(function(e, response)
				{
					console.log("Success");
				}).fail(function(e, response)
				{
					console.log("Fail");
				});
	}
	

		$scope.goToImages = function(){
			$location.path('/addProductImages/'+$routeParams.id);
		}
		
		$scope.onLogoFileSelect = function($files) {
			logofile = $files;
			console.log("File Upload");
			console.log(logofile);
			$scope.productData.fileName = logofile[0].name;
			files[0] = $files[0];
			names[0]= "logoFile";
		}
		
		$scope.onCadFileSelect = function($files) {
			cadfile = $files;
			console.log("File Upload");
			console.log(cadfile);
			$scope.productData.cadfileName = cadfile[0].name;
			files[1] = $files[0];
			names[1] = "cadFile";
		}


}]);
angular.module('newApp')
.controller('collectionInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
												{ name: 'title', displayName: 'Collection Title', width:'25%',cellEditableCondition: false,enableFiltering: true,
												 	
												 },
		 		                                 { name: 'description', displayName: 'Description', width:'25%',cellEditableCondition: false,enableFiltering: true,
		 		                                	
		 		                                 },
		 		                                 { name: 'section', displayName: 'section',enableFiltering: true, width:'30%',cellEditableCondition: false,
		 		                                	
		 		                                 },
		 		                                 { name: 'edit', displayName: 'Edit', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editcollection(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteCollection(row)"></i>', 
	    		                                 
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getAllCollection')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
			});
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'description':grid.columns[1].filters[0].term,'section':grid.columns[2].filters[0].term},undefined);
			        });
		   		
	   		};
		  
	$scope.goToaccessories = function() {
		$location.path('/accessoriesInventory');
	}
	
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.editcollection = function(row) {
		$scope.flag = "collection";
		$location.path('/editcollection/'+row.entity.id+'/'+$scope.flag);
	}	 
	$scope.goToAllProducts = function() {
		$location.path('/allproductsInventory');	
	}
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToSection = function(id) {
		$location.path('/sectionInventory/'+id);
	}
	
	$scope.updateProduct = function(row) {
		$scope.productData.id = $routeParams.id
		$http.post('/updateProduct',$scope.productData)
			.success(function(data) {
				console.log('success');
				
				
			});
	}	 

	

	$scope.deleteCollection = function(row){
		console.log('avfvvv');
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteCollectionRow = function() {
		   $http.get('/deleteCollectionById/'+$scope.rowDataVal.entity.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				});
				 $http.get('/getAllCollection')
					.success(function(data) {
					$scope.gridOptions.data = data;
				});
			});
	}
}]);

angular.module('newApp')
.controller('EditCollectionCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$http.get('/getSections')
	.success(function(data) {
		console.log(data);
		$scope.sections = data;
	});
	console.log("flag.."+$routeParams.flag);
	console.log("id.."+$routeParams.id);
	$scope.temp = $routeParams.flag;
	$scope.colSec = 'disable';
	$http.get('/getCollectionData/'+$routeParams.id)
	.success(function(data) {
		console.log(data);	
		$scope.addCollection = data;
		$scope.colSec = data.section;
	});
	
	$scope.addCollectionNew = {};
	$scope.saveCollection = function(section){
		$scope.addCollectionNew = {};
		if($('#sectionId').val() !='null'){
			$scope.addCollection.section = $('#sectionId').val();
		}else{
			$scope.addCollection.section = $scope.colSec;
		}
		console.log($scope.addCollection.section);
		$scope.addCollection.id = $routeParams.id; 
		console.log($scope.addCollection);
		if($scope.addCollection.section != "accessories"){
			$scope.addCollectionNew.title = $scope.addCollection.title;
			$scope.addCollectionNew.newFlag = $scope.addCollection.newFlag;
			$scope.addCollectionNew.description = $scope.addCollection.description;
			$scope.addCollectionNew.section = $scope.addCollection.section;
			$scope.addCollectionNew.id = $scope.addCollection.id;
		}
		if($scope.addCollection.section == "accessories"){
			$scope.addCollectionNew.title = $scope.addCollection.title;
			$scope.addCollectionNew.website = $scope.addCollection.website;
			$scope.addCollectionNew.section = $scope.addCollection.section;
			$scope.addCollectionNew.newFlag = $scope.addCollection.newFlag;
			$scope.addCollectionNew.id = $scope.addCollection.id;
		}
		
		if(logofile == undefined && thumbfile == undefined){
			
			$scope.temp = $scope.addCollectionNew.section;
			  $http.post('/updateCollection',$scope.addCollectionNew)
		   		.success(function(data) {
		   			$.pnotify({
					    title: "Success",
					    type:'success',
					    text: "Collection Update successfully",
					});
		   			//$location.path('/editCollectionImg/'+$routeParams.id+'/'+$scope.temp);
		   			if($routeParams.flag=="collection")
		            	$location.path('/collection');
		            if($routeParams.flag=="accessories")
		            	$location.path('/accessoriesInventory');
		            if($routeParams.flag=="product")
		            	$location.path('/allProductCollection');
		            if($routeParams.flag=="readymade")
		            	$location.path('/allReadyMadeCollection');
		   		});
		}else if(thumbfile == undefined && logofile != undefined){
					console.log($scope.addCollectionNew);
					$upload.upload({
			            url : '/updateCollection',
			            method: 'POST',
			            file:logofile,
			            data:$scope.addCollectionNew
			        }).success(function(data, status, headers, config) {
			            $.pnotify({
						    title: "Success",
						    type:'success',
						    text: "Collection Update successfully",
						});
            
			            if($routeParams.flag=="collection"){	
			            	if($scope.addCollectionNew.section == "product")
			            		$location.path('/editCollectionImg/'+$routeParams.id+'/'+$scope.temp);
			            	else
			                	$location.path('/collection');
			            }
			            if($routeParams.flag=="accessories"){
			            	if($scope.addCollectionNew.section == "product")
			            		$location.path('/editCollectionImg/'+$routeParams.id+'/'+$scope.temp);
			            	else
			            		$location.path('/accessoriesInventory');
			            }
			            if($routeParams.flag=="product"){
			            	if($scope.addCollectionNew.section == "product")
			            		$location.path('/editCollectionImg/'+$routeParams.id+'/'+$scope.temp);
			            }
		            if($routeParams.flag=="readymade"){
		            	if($scope.addCollectionNew.section == "product")
		            		$location.path('/editCollectionImg/'+$routeParams.id+'/'+$scope.temp);
		            	else
			            	$location.path('/allReadyMadeCollection');
		            }
			        });
		}else{
			$upload.upload({
	            url : '/updateReadyCollection',
	            method: 'POST',
	            data:$scope.addCollectionNew,
	            fileFormDataName: readyMadeFilesName,
	            file:readyMadeFiles
	            
	        }).success(function(data, status, headers, config) {
	            console.log('success');
	            $.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Collection update successfully",
				});	
	            $location.path('/editCollectionImg/'+$routeParams.id+'/'+$scope.temp);
	        });
		}
	}
	var logofile;
	 var readyMadeFiles = [];
	 var readyMadeFilesName = [];
		$scope.onLogoFileSelect = function($files) {
			logofile = $files;
			readyMadeFiles[0] = $files[0];
			readyMadeFilesName[0] = "featured";
			if(thumbfile == undefined || thumbfile == null){
				readyMadeFiles[1] = $files[0];
				readyMadeFilesName[1] = "undefines";
			}
		}
		var thumbfile;
		$scope.onThumbFileSelect = function($files) {
			thumbfile = $files;
			if(logofile == undefined || logofile == null){
				readyMadeFiles[0] = $files[0];
				readyMadeFilesName[0] = "undefines";
			}
			readyMadeFiles[1] = $files[0];
			readyMadeFilesName[1] = "thumbnail";
		}	

		$scope.getCollectionImg = function(temp){
			console.log("IMGtemp.."+temp);
			$scope.temp = temp;
			console.log("IMGtemp.."+$scope.temp);
			$location.path('/editCollectionImg/'+$routeParams.id+'/'+$scope.temp);
		}
		
		$scope.goTocollectionPage = function(temp){
			console.log("temp.."+temp);
			if(temp=="product")
				$location.path('/allProductCollection');
			if(temp=="accessories")
				$location.path('/accessoriesInventory');
			if(temp=="readymade")
				$location.path('/allReadyMadeCollection');
			if(temp=="collection")
				$location.path('/collection');
		}

}]);

angular.module('newApp')
.controller('EditCollectionImgCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	$scope.tempDate = new Date().getTime();
	console.log("Img flag.."+$routeParams.temp);
	console.log("id.."+$routeParams.id);
	$scope.temp = $routeParams.temp;
	$scope.gridsterOpts = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
		    margins: [10, 10], // the pixel distance between each widget
		    outerMargin: true, // whether margins apply to outer edges of the grid
		    isMobile: false, // stacks the grid items if true
		    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
		    mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
		    minColumns: 6, // the minimum columns the grid must have
		    minRows: 1, // the minimum height of the grid, in rows
		    maxRows: 100,
		    defaultSizeX: 1, // the default width of a gridster item, if not specifed
		    defaultSizeY: 1, // the default height of a gridster item, if not specified
		    resizable: {
			       enabled: false,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, widget) {} // optional callback fired when item is finished resizing
			    },
		    /* minSizeX: 1, // minimum column width of an item
		    maxSizeX: null, // maximum column width of an item
		    minSizeY: 1, // minumum row height of an item
		    maxSizeY: null, // maximum row height of an item
		   */
			    draggable: {
				       enabled: false, // whether dragging items is supported
				       handle: '.my-class', // optional selector for resize handle
				       start: function(event, $element, widget) { }, // optional callback fired when drag is started,
				       drag: function(event, $element, widget) { }, // optional callback fired when item is moved,
				       stop: function(event, $element, widget) {
				    	   
				    	   if($(event.target).html() == 'Set Default' || $(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   /*for(var i=0;i<$scope.imageList.length;i++) {
				    		   delete $scope.imageList[i].description;
				    		   delete $scope.imageList[i].width;
				    		   delete $scope.imageList[i].height;
				    		   delete $scope.imageList[i].link;
				    	   }*/
				    	 /*  $http.post('/savePosition',$scope.imageList)
					   		.success(function(data) {
					   			$.pnotify({
								    title: "Success",
								    type:'success',
								    text: "Position saved successfully",
								});
					   		});*/
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
	/*var myDropzone = new Dropzone("#dropzoneFrm",{
		   parallelUploads: 30,
		   headers: { "id": $routeParams.id },
		   acceptedFiles:"image/*",
		   addRemoveLinks:true,
		   autoProcessQueue:false,
		   init:function () {
			   this.on("queuecomplete", function (file) {
			         
			      });
			   this.on("complete", function() {
				   this.removeAllFiles();
				   $scope.init();
			   });
		   }
	   });*/
	   /*$scope.uploadFiles = function() {
		   Dropzone.autoDiscover = false;
		   myDropzone.processQueue();
		   
	   }*/
	   

	/*	$scope.setAsDefault = function(image,index) {
			
			
			for(var i=0;i<$scope.imageList.length;i++) {
				if($scope.imageList[i].defaultImage == true) {
					$http.get('/removeDefault/'+$scope.imageList[i].id+'/'+image.id)
					.success(function(data) {
					});
					$('#imgId'+i).removeAttr("style","");
					$scope.imageList[i].defaultImage = false;
					image.defaultImage = true;
					$('#imgId'+index).css("border","3px solid");
					$('#imgId'+index).css("color","red");
					break;
				}
			}
			
			if(i == $scope.imageList.length) {
				$http.get('/setDefaultImage/'+image.id)
				.success(function(data) {
					console.log('success');
				});
				
				image.defaultImage = true;
				$('#imgId'+index).css("border","3px solid");
				$('#imgId'+index).css("color","red");
			}
		}*/
		
		/*$scope.deleteImage = function(img) {
			$http.get('/deleteImage/'+img.id)
			.success(function(data) {
				console.log('success');
				$scope.imageList.splice($scope.imageList.indexOf(img),1);
			});
			
		}*/
		
		$scope.showFullImage = function(image) {
			$scope.imageId = image.id;
			$scope.imageName = image.imgName;
		}
		
		$scope.editImage = function(image) {
			$location.path('/ImageCollectionCrop/'+image.id+'/'+$routeParams.temp);
		}
		$scope.goTocollectionPage = function(temp){
			console.log("temp.."+temp);
			if(temp=="product")
				$location.path('/allProductCollection');
			if(temp=="accessories")
				$location.path('/accessoriesInventory');
			if(temp=="readymade")
				$location.path('/allReadyMadeCollection');
			if(temp=="collection")
				$location.path('/collection');
		}
	   
	   
	   $scope.init = function() {
			$http.get('/getImagesByCollection/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				$scope.imageList = data;
				
			});
	   };
}]);

angular.module('newApp')
.controller('AddCollectionImgCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {
	
	console.log("Addid.."+$routeParams.id);
	$scope.gridsterOpts = {
		    columns: 6, // the width of the grid, in columns
		    pushing: true, // whether to push other items out of the way on move or resize
		    floating: true, // whether to automatically float items up so they stack (you can temporarily disable if you are adding unsorted items with ng-repeat)
		    swapping: true, // whether or not to have items of the same size switch places instead of pushing down if they are the same size
		     width: 'auto', // can be an integer or 'auto'. 'auto' scales gridster to be the full width of its containing element
		     colWidth: 'auto', // can be an integer or 'auto'.  'auto' uses the pixel width of the element divided by 'columns'
		    rowHeight: 'match', // can be an integer or 'match'.  Match uses the colWidth, giving you square widgets.
		    margins: [10, 10], // the pixel distance between each widget
		    outerMargin: true, // whether margins apply to outer edges of the grid
		    isMobile: false, // stacks the grid items if true
		    mobileBreakPoint: 600, // if the screen is not wider that this, remove the grid layout and stack the items
		    mobileModeEnabled: true, // whether or not to toggle mobile mode when screen width is less than mobileBreakPoint
		    minColumns: 6, // the minimum columns the grid must have
		    minRows: 1, // the minimum height of the grid, in rows
		    maxRows: 100,
		    defaultSizeX: 1, // the default width of a gridster item, if not specifed
		    defaultSizeY: 1, // the default height of a gridster item, if not specified
		    resizable: {
			       enabled: false,
			       handles: ['n', 'e', 's', 'w', 'ne', 'se', 'sw', 'nw'],
			       start: function(event, $element, widget) {}, // optional callback fired when resize is started,
			       resize: function(event, $element, widget) {}, // optional callback fired when item is resized,
			       stop: function(event, $element, widget) {} // optional callback fired when item is finished resizing
			    },
		   
			    draggable: {
				       enabled: false, // whether dragging items is supported
				       handle: '.my-class', // optional selector for resize handle
				       start: function(event, $element, widget) { }, // optional callback fired when drag is started,
				       drag: function(event, $element, widget) { }, // optional callback fired when item is moved,
				       stop: function(event, $element, widget) {
				    	   
				    	   if($(event.target).html() == 'Set Default' || $(event.target).html() == 'Edit' || $(event.target)[0].className == 'glyphicon glyphicon-zoom-in' || $(event.target)[0].className == 'btn fa fa-times') {
				    		   return;
				    	   };
				    	   
				    	   
				       } // optional callback fired when item is finished dragging
				    }
		};
	
		
		$scope.showFullImage = function(image) {
			$scope.imageId = image.id;
			$scope.imageName = image.imgName;
		}
		
		$scope.editImage = function(image) {
			$location.path('/ImageCollectionCrop/'+image.id+'/'+$scope.temp);
		}
		$scope.goTocollectionPage = function(){
			
				$location.path('/collection');
		}
	   
	   
	   $scope.init = function() {
			$http.get('/getImagesByCollection/'+$routeParams.id)
			.success(function(data) {
				console.log(data);
				$scope.imageList = data;
				$scope.temp = data.section;
			});
	   };
}]);
angular.module('newApp')
.controller('SectionInventoryCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.id = $routeParams.id;
	$scope.tempDate = new Date().getTime();
	$scope.txt='Products';
	$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
		 		                                 { name: 'title', displayName: 'Product Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
		 		                                	cellTemplate: '<div> <a ng-mouseenter="grid.appScope.mouse(row)" ng-mouseleave="grid.appScope.mouseout(row)" style="line-height: 200%;" title="" data-content="{{row.entity.title}}">{{row.entity.title}}</a></div>',
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'20%',cellEditableCondition: false,
		 		                                	
		 		                                 },
												{ name: 'collectionTitle', displayName: 'Collection Title', width:'15%',cellEditableCondition: false,enableFiltering: true,
													 	
												 },
		 		                                 { name: 'price', displayName: 'Price',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'cost', displayName: 'Cost',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'countImages', displayName: 'No of Images',enableFiltering: false, width:'10%',cellEditableCondition: false,
			 		                                	
		 		                                 },
		 		                                { name: 'edit', displayName: 'Edit', width:'20%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
	        		                                 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editProduct(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteProduct(row)"></i>', 
	    		                                 
	    		                                 },
	     		                                 ];
	  
		  $http.get('/getAllSectionTypes/'+$routeParams.id)
				.success(function(data) {
				console.log(data);
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
			});
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.mouse = function(row) {
				console.log(row.entity.imgId);
				$('#thumb_image').attr("src", "/getImage/"+row.entity.imgId+"/thumbnail?date="+$scope.tempDate );
				$('#thumb_image').show();
				$('#imagePopup').modal();
			};
			$scope.mouseout = function(row) {
				$('#imgClose').click();
			};
		  
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[0].filters[0].term,'description':grid.columns[1].filters[0].term,'collectionTitle':grid.columns[2].filters[0].term},undefined);
			        });
			};
	
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToproducts = function() {
		$location.path('/productsInventory');	
	}
	$scope.goToSectionCollection = function(id) {
		console.log("Section...");
		$location.path('/allSectionCollection/'+id);
	}
	$scope.goToreadyMadeCollection = function() {
		$location.path('/allReadyMadeCollection');
	}
	$scope.goToAllProducts = function() {
		$location.path('/allproductsInventory');	
	}
	$scope.goToaccessories = function() {
		$location.path('/accessoriesInventory');
	}
	$scope.goToSection = function(id) {
		$location.path('/sectionInventory/'+id);
	}
	
	$scope.editProduct = function(row) {
		$scope.flag = "product";
		$location.path('/editProduct/'+row.entity.id+'/'+$scope.flag);
	}	 
	$scope.updateProduct = function(row) {
		$scope.productData.id = $routeParams.id
		$http.post('/updateProduct',$scope.productData)
			.success(function(data) {
				console.log('success');
				
				
			});
	}	 



	$scope.deleteProduct = function(row){
		   $('#deleteModal').click();
		   $scope.rowDataVal = row;
	}

	$scope.deleteProductRow = function() {
		   $http.get('/deleteProductById/'+$scope.rowDataVal.entity.id)
			.success(function(data) {
				$.pnotify({
				    title: "Success",
				    type:'success',
				    text: "Delete successfully",
				});
				$http.get('/getAllSectionTypes')
				.success(function(data) {
				console.log(data);
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
				
			});
				/*if($scope.rowDataVal.entity.status == 'Newly Arrived') {
					 $scope.viewVehiclesInit();
				} 
				if($scope.rowDataVal.entity.status == 'Sold') {
					$scope.soldTab();
				}*/
			});
	}

	
}]);
angular.module('newApp')
.controller('SectionCollectionCtrl', ['$scope','$http','$location','$filter','$routeParams','$upload', function ($scope,$http,$location,$filter,$routeParams,$upload) {

	$scope.id = $routeParams.id;
	$scope.txt='Collections';
	/*$scope.gridOptions = {
	 		 paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
	 		    paginationPageSize: 150,
	 		    enableFiltering: true,
	 		    useExternalFiltering: true,
	 		    rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
	 		 };
	 		 $scope.gridOptions.enableHorizontalScrollbar = 0;
	 		 $scope.gridOptions.enableVerticalScrollbar = 2;
	 		 $scope.gridOptions.columnDefs = [
												{ name: 'count', displayName: 'Index No', width:'10%',cellEditableCondition: false,enableFiltering: false,
													  cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row)}}</div>',
												},
												{ name: 'title', displayName: 'Collection Title', width:'25%',cellEditableCondition: false,enableFiltering: true,
		 		                                	
		 		                                 },
		 		                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'25%',cellEditableCondition: false,
		 		                                	
		 		                                 },
		 		                                { name: 'image_name', displayName: 'Image Name', width:'25%',cellEditableCondition: false,enableFiltering: false,
													 	
												 },
		 		                                 { name: 'edit', displayName: 'Edit', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
													 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editcollection(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteCollection(row)"></i>', 
	    		                                 
	    		                                 },
	     		                                 ];*/
	  
		  $http.get('/getSectionCollectionData/'+$routeParams.id)
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
				console.log(data);
			});
		  
		  $scope.gridOptions = {
				  	paginationPageSizes: [10, 25, 50, 75,100,125,150,175,200],
		 		    paginationPageSize: 150,
		 		    enableFiltering: true,
		 		    useExternalFiltering: true,
				  	//rowTemplate: "<div style=\"cursor:pointer;\" ng-dblclick=\"grid.appScope.showInfo(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>",
				    rowTemplate: '<div grid="grid" class="ui-grid-draggable-row" draggable="true"><div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader, \'custom\': true }" ui-grid-cell></div></div>',
				    columnDefs: [
									{ name: 'count', displayName: 'Index No', width:'10%',cellEditableCondition: false,enableFiltering: false,
										  cellTemplate: '<div class="ui-grid-cell-contents">{{grid.renderContainers.body.visibleRowCache.indexOf(row)}}</div>',
									},
									{ name: 'title', displayName: 'Collection Title', width:'25%',cellEditableCondition: false,enableFiltering: true,
	                                	
	                                 },
	                                 { name: 'description', displayName: 'Description',enableFiltering: true, width:'25%',cellEditableCondition: false,
	                                	
	                                 },
	                                { name: 'image_name', displayName: 'Image Name', width:'25%',cellEditableCondition: false,enableFiltering: false,
										 	
									 },
	                                 { name: 'edit', displayName: 'Edit', width:'15%',enableFiltering: false, cellEditableCondition: false, enableSorting: false, enableColumnMenu: false,
										 cellTemplate:' <i class="glyphicon glyphicon-edit" ng-click="grid.appScope.editcollection(row)" style="margin-top:7px;margin-left:14px;" title="Edit"></i>&nbsp;&nbsp;&nbsp;<i class="fa fa-trash" title="Delete" ng-click="grid.appScope.deleteCollection(row)"></i>', 
	                                 
	                                 },
	                             ]
				  };
		  $http.get('/getAllSection')
			.success(function(data) {
				console.log(data);
				$scope.secList = data;
			});
		  
		  $scope.gridOptions.onRegisterApi = function(gridApi){
				 $scope.gridApi = gridApi;
				 
		   		$scope.gridApi.core.on.filterChanged( $scope, function() {
			          var grid = this.grid;
			        	  $scope.gridOptions.data = $filter('filter')($scope.contactsList,{'title':grid.columns[1].filters[0].term,'description':grid.columns[2].filters[0].term},undefined);
			        });
			};
		  
	$scope.goToaccessories = function() {
		console.log("Acc Call..");
		$location.path('/accessoriesInventory');
	}
	$scope.goToCollection = function() {
		$location.path('/collection');
	}
	$scope.goToReadyMade = function() {
		$location.path('/inventory');
	}
	$scope.goToproducts = function() {
		console.log("Product..");
		$location.path('/productsInventory');	
	}
	$scope.goToAllProducts = function() {
		console.log("Call..");
		$location.path('/allproductsInventory');	
	}
	$scope.editcollection = function(row) {
		$scope.flag = "product";
		$location.path('/editcollection/'+row.entity.id+'/'+$scope.flag);
	}
	$scope.deleteCollection = function(row){
		   $('#deleteModalc').click();
		   $scope.rowDataVal = row;
	}
	$scope.goToSection = function(id) {
		$location.path('/sectionInventory/'+id);
	}

	$scope.deleteCollectionRow = function() {
		console.log("Pro Collection Delete");
		$http.get('/deleteCollectionById/'+$scope.rowDataVal.entity.id)
		.success(function(data) {
			$.pnotify({
			    title: "Success",
			    type:'success',
			    text: "Delete successfully",
			});
				$http.get('/getSectionCollectionData')
				.success(function(data) {
				$scope.gridOptions.data = data;
				$scope.contactsList = data;
				console.log(data);
			});
		});
	}

}]).filter('myProgressType', function() {
	  return function (input) {
	    return +input > 65 ? 'danger' : (+input > 35 ? 'warning' : 'success');
	  };;
	});

	(function() {
	    'use strict';

	    angular.module('ui.grid.draggable-rows', ['ui.grid'])

	    .constant('uiGridDraggableRowsConstants', {
	        featureName: 'draggableRows',
	        ROW_OVER_CLASS: 'ui-grid-draggable-row-over',
	        ROW_OVER_ABOVE_CLASS: 'ui-grid-draggable-row-over--above',
	        ROW_OVER_BELOW_CLASS: 'ui-grid-draggable-row-over--below',
	        POSITION_ABOVE: 'above',
	        POSITION_BELOW: 'below',
	        publicEvents: {
	            draggableRows: {
	                rowDragged: function(scope, info, rowElement) {},
	                rowDropped: function(scope, info, targetElement) {},
	                rowOverRow: function(scope, info, rowElement) {},
	                rowEnterRow: function(scope, info, rowElement) {},
	                rowLeavesRow: function(scope, info, rowElement) {},
	                rowFinishDrag: function(scope) {}
	            }
	        }
	    })

	    .factory('uiGridDraggableRowsCommon', [function() {
	        return {
	            draggedRow: null,
	            draggedRowEntity: null,
	            position: null,
	            fromIndex: null,
	            toIndex: null
	        };
	    }])

	    .service('uiGridDraggableRowsService', ['uiGridDraggableRowsConstants', function(uiGridDraggableRowsConstants) {
	        this.initializeGrid = function(grid, $scope, $element) {
	            grid.api.registerEventsFromObject(uiGridDraggableRowsConstants.publicEvents);

	            grid.api.draggableRows.on.rowFinishDrag($scope, function() {
	                angular.forEach($element[0].querySelectorAll('.' + uiGridDraggableRowsConstants.ROW_OVER_CLASS), function(row) {
	                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
	                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
	                    row.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
	                });
	            });
	        };
	    }])

	    .service('uiGridDraggableRowService', ['uiGridDraggableRowsConstants', 'uiGridDraggableRowsCommon', function(uiGridDraggableRowsConstants, uiGridDraggableRowsCommon) {
	        var move = function(from, to) {
	            /*jshint validthis: true */
	            this.splice(to, 0, this.splice(from, 1)[0]);
	        };

	        this.prepareDraggableRow = function($scope, $element) {
	            var grid = $scope.grid;
	            var data = grid.options.data;
	            var row = $element[0];
	            var listeners = {
	                onDragOverEventListener: function(e) {
	                    if (e.preventDefault) {
	                        e.preventDefault();
	                    }

	                    var dataTransfer = e.dataTransfer || e.originalEvent.dataTransfer;
	                    dataTransfer.effectAllowed = 'copyMove';
	                    dataTransfer.dropEffect = 'move';

	                    var offset = e.offsetY || e.layerY || (e.originalEvent ? e.originalEvent.offsetY : 0);

	                    $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_CLASS);

	                    if (offset < this.offsetHeight / 2) {
	                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_ABOVE;

	                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
	                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);

	                    } else {
	                        uiGridDraggableRowsCommon.position = uiGridDraggableRowsConstants.POSITION_BELOW;

	                        $element.removeClass(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
	                        $element.addClass(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);
	                    }

	                    //grid.api.draggableRows.raise.rowOverRow(uiGridDraggableRowsCommon, this);
	                },

	                onDragStartEventListener: function(e) {
	                    this.style.opacity = '0.5';

	                    uiGridDraggableRowsCommon.draggedRow = this;
	                    uiGridDraggableRowsCommon.draggedRowEntity = $scope.$parent.$parent.row.entity;

	                    uiGridDraggableRowsCommon.position = null;

	                    uiGridDraggableRowsCommon.fromIndex = data.indexOf(uiGridDraggableRowsCommon.draggedRowEntity);
	                    uiGridDraggableRowsCommon.toIndex = null;

	                   // grid.api.draggableRows.raise.rowDragged(uiGridDraggableRowsCommon, this);
	                },

	                onDragLeaveEventListener: function() {
	                    this.style.opacity = '1';

	                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_CLASS);
	                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_ABOVE_CLASS);
	                    this.classList.remove(uiGridDraggableRowsConstants.ROW_OVER_BELOW_CLASS);

	                   // grid.api.draggableRows.raise.rowLeavesRow(uiGridDraggableRowsCommon, this);
	                    
	                },

	                onDragEnterEventListener: function() {
	                   // grid.api.draggableRows.raise.rowEnterRow(uiGridDraggableRowsCommon, this);
	                },

	                onDragEndEventListener: function() {
	                   // grid.api.draggableRows.raise.rowFinishDrag();
	                },

	                onDropEventListener: function(e) {
	                    var draggedRow = uiGridDraggableRowsCommon.draggedRow;

	                    if (e.stopPropagation) {
	                        e.stopPropagation();
	                    }

	                    if (e.preventDefault) {
	                        e.preventDefault();
	                    }

	                    if (draggedRow === this) {
	                        return false;
	                    }

	                    uiGridDraggableRowsCommon.toIndex = data.indexOf($scope.$parent.$parent.row.entity);

	                    if (uiGridDraggableRowsCommon.position === uiGridDraggableRowsConstants.POSITION_ABOVE) {
	                        if (uiGridDraggableRowsCommon.fromIndex < uiGridDraggableRowsCommon.toIndex) {
	                            uiGridDraggableRowsCommon.toIndex -= 1;
	                        }

	                    } else if (uiGridDraggableRowsCommon.fromIndex >= uiGridDraggableRowsCommon.toIndex) {
	                        uiGridDraggableRowsCommon.toIndex += 1;
	                    }

	                    $scope.$apply(function() {
	                        move.apply(data, [uiGridDraggableRowsCommon.fromIndex, uiGridDraggableRowsCommon.toIndex]);
	                        //console.log(data);
	                    });
	                    console.log(data);	
	                    	console.log("............................");
	                    	$.ajax({
		                    		type: "POST",
		                            url: '/saveCollectionOrder',
		                            data: JSON.stringify(data),
		                            contentType: "application/json; charset=utf-8",
		                            dataType: "json",
	                    		});
	                    
	                   // grid.api.draggableRows.raise.rowDropped(uiGridDraggableRowsCommon, this);

	                    e.preventDefault();
	                }
	            };

	            row.addEventListener('dragover', listeners.onDragOverEventListener, false);
	            row.addEventListener('dragstart', listeners.onDragStartEventListener, false);
	            row.addEventListener('dragleave', listeners.onDragLeaveEventListener, false);
	            row.addEventListener('dragenter', listeners.onDragEnterEventListener, false);
	            row.addEventListener('dragend', listeners.onDragEndEventListener, false);
	            row.addEventListener('drop', listeners.onDropEventListener);
	        };
	    }])

	    .directive('uiGridDraggableRow', ['uiGridDraggableRowService', function(uiGridDraggableRowService) {
	        return {
	            restrict: 'ACE',
	            scope: {
	                grid: '='
	            },
	            compile: function() {
	                return {
	                    pre: function($scope, $element) {
	                        uiGridDraggableRowService.prepareDraggableRow($scope, $element);
	                    }
	                };
	            }
	        };
	    }])

	    .directive('uiGridDraggableRows', ['uiGridDraggableRowsService', function(uiGridDraggableRowsService) {
	        return {
	            restrict: 'A',
	            replace: true,
	            priority: 0,
	            require: 'uiGrid',
	            scope: false,
	            compile: function() {
	                return {
	                    pre: function($scope, $element, $attrs, uiGridCtrl) {
	                        uiGridDraggableRowsService.initializeGrid(uiGridCtrl.grid, $scope, $element);
	                    }
	                };
	            }
	        };
	    }]);
	}());