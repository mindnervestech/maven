angular.module('formlyIonic', ['formly'], ["formlyConfigProvider", function configFormlyIonic(formlyConfigProvider) {
    'use strict';


    angular.forEach(['checkbox', 'input', 'radio', 'range', 'textarea', 'toggle', 'select', 'floating-input', 'stacked-input', 'inline-input','signature','ui-grid','image'], function (fieldName) {
        if(fieldName === 'signature'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName),
                controller:'SignatureCTRl'
            });
        }else if(fieldName === 'ui-grid'){
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName)
                //template: '<div  style ="height: 1px !important;" ui-grid="{ data: model[options.key], columnDefs: to.columnDefs, onRegisterApi: to.onRegisterApi}"></div>'
                //wrapper: ['bootstrapLabel', 'bootstrapHasError']
            });
        }else{
            formlyConfigProvider.setType({
                name: fieldName,
                templateUrl: getFieldTemplateUrl(fieldName)
            });
        }
    });

    formlyConfigProvider.templateManipulators.preWrapper.push(function ariaDescribedBy(template, options, scope) {
        if (options.templateOptions && angular.isDefined(options.templateOptions.description) &&
            options.type !== 'radio' && options.type !== 'checkbox') {
            var el = angular.element('<a></a>');
            el.append(template);
            var modelEls = angular.element(el[0].querySelectorAll('[ng-model]'));
            if (modelEls) {
                el.append(
                    '<p id="' + scope.id + '_description"' +
                    'class="help-block"' +
                    'ng-if="options.templateOptions.description">' +
                    '{{options.templateOptions.description}}' +
                    '</p>'
                );
                modelEls.attr('aria-describedby', scope.id + '_description');
                return el.html();
            } else {
                return template;
            }
        } else {
            return template;
        }
    });

    function getFieldTemplateUrl(name) {
        return 'fields/ion-' + name + '.html';
    }


}]);

angular.module("formlyIonic")
.controller('SignatureCTRl',function($scope){
    console.log('SignatureCTRl');
    var app = this;
    app.signature = {};
    app.close = function(){
      if (app.signature.isEmpty()) {
          app.showRequiredMsg = true;
      } else {
          app.hasSignature = true;
      }
    }
})

.run(["$templateCache", function($templateCache) {
//$templateCache.put("fields/ion-checkbox.html","<label style=\"margin-left: 18px;font-size:17px;\">{{options.name}}</label><div class=\"list\"><ion-checkbox ng-repeat=\"item in options.templateOptions.label\" ng-model=\"model[item.id]\">{{item.value}}</ion-checkbox></div>");
//$templateCache.put("fields/ion-checkbox.html","<label style=\"margin-left: 18px;font-size:17px;\">{{options.name}}</label><div class=\"list\"><ion-checkbox ng-repeat=\"item in options.templateOptions.label\" ng-model=\"model[item.id]\" formly-dynamic-name=\"id\" formly-custom-validation=\"options.validators\" ng-required=\"options.templateOptions.required\" ng-disabled=\"options.templateOptions.disabled\">{{item.value}}</ion-checkbox></div>");
$templateCache.put("fields/ion-checkbox.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.name}}</span></label><div class=\"list\"><ion-checkbox ng-repeat=\"item in options.templateOptions.label\" ng-model=\"model[item.id]\">{{item.value}}</ion-checkbox></div>");

$templateCache.put("fields/ion-floating-input.html","<label class=\"item item-input item-floating-label form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\">{{options.templateOptions.label}}</span> <div class=\"col-sm-4 form-group\"><input class=\"col-sm-10\" ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></div></label>");
$templateCache.put("fields/ion-inline-input.html","<label class=\"item item-input form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\">{{options.templateOptions.label}}</span> <div class=\"col-sm-4 form-group\"><input class=\"col-sm-10\" ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></div></label>");
$templateCache.put("fields/ion-input.html","<label class=\"item item-input form-group col-sm-12\" style=\"padding-left: 0px;\"><i class=\"icon {{options.templateOptions.icon}}\" ng-if=\"options.templateOptions.icon\" ng-class=\"{\'placeholder-icon\': options.templateOptions.iconPlaceholder}\"></i><div class=\"col-sm-4 form-group\"><input class=\"col-sm-10\" ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></div></label>");

//$templateCache.put("fields/ion-radio.html","<label style=\"margin-left: 18px;font-size:17px;\">{{options.name}}</label><ion-radio ng-repeat=\"item in options.templateOptions.options\" icon=\"{{item.icon? item.icon: \'ion-checkmark\'}}\" ng-value=\"item.value\" ng-model=\"model[options.key]\">{{ item.text }}</ion-radio>");
$templateCache.put("fields/ion-radio.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.name}}</span></label><div class=\"list\"><ion-radio ng-repeat=\"item in options.templateOptions.options\" icon=\"{{item.icon? item.icon: \'ion-checkmark\'}}\" ng-value=\"item.value\" ng-model=\"model[options.key]\">{{ item.text }}</ion-radio></div>");

$templateCache.put("fields/ion-range.html","<div class=\"item range\" ng-class=\"\'range-\' + options.templateOptions.rangeClass\"><span class=\"col-sm-3\">{{options.templateOptions.label}}</span> <i class=\"icon\" ng-if=\"options.templateOptions.minIcon\" ng-class=\"options.templateOptions.minIcon\"></i> <input type=\"range\" min=\"{{options.templateOptions.min}}\" max=\"{{options.templateOptions.max}}\" step=\"{{options.templateOptions.step}}\" value=\"{{options.templateOptions.value}}\" ng-model=\"model[options.key]\"> <i class=\"icon\" ng-if=\"options.templateOptions.maxIcon\" ng-class=\"options.templateOptions.maxIcon\"></i></div>");
$templateCache.put("fields/ion-select.html","<label class=\"item item-input item-select form-group col-sm-12\" style=\"padding-left: 0px;\"><div class=\"input-label form-group col-sm-2\">{{to.label}}</div><div class=\"col-sm-4 form-group\"><select class=\"col-sm-10\" ng-model=\"model[options.key]\" ng-options=\"option[to.valueProp || \'value\'] as option[to.labelProp || \'name\'] group by 			  option[to.groupProp || \'group\'] for option in to.options\"></select></div></label>");

//$templateCache.put("fields/ion-stacked-input.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}</span> <input ng-model=\"model[options.key]\" placeholder=\"{{options.templateOptions.placeholder}}\" type=\"{{options.templateOptions.type}}\"></label>");
$templateCache.put("fields/ion-stacked-input.html","<label class=\"item item-input item-stacked-label form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\">{{options.templateOptions.label}}</span><div class=\"col-sm-4 form-group\"><input class=\"col-sm-10\" ng-model=\"model[options.key]\" formly-dynamic-name=\"id\" formly-custom-validation=\"options.validators\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-required=\"options.templateOptions.required\" ng-disabled=\"options.templateOptions.disabled\"></div></label>");

$templateCache.put("fields/ion-image.html","<label class=\"item item-input item-stacked-label\"><img  ng-src=\"{{options.templateOptions.imageUrl}}\" id=\"{{options.templateOptions.label}}\" ng-model=\"model[options.key]\" style=\"cursor:pointer;\" ng-click=\"takePic($event)\"></label>");

// $templateCache.put("fields/ion-textarea.html","<span ng-click = \"addTextArea()\" ng-show = \"options.templateOptions.isRepeatable || iFrameModel.textObj.isRepeat\" style = \"cursor : pointer; float: right;\" class=\"ion-android-add\"></span><div  ng-repeat = \"text in iFrameModel.textObj\" > <span ng-show = \"iFrameModel.textObj[$index].isChild\" ng-click = \"remove(iFrameModel)\" class=\"ion-minus\" style = \"cursor : pointer; float:right;\"></span> <label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}</span><textarea rows=\"{{options.templateOptions.rows? options.templateOptions.rows: \'5\'}}\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-model = \"iFrameModel.textObj[$index].textarea\"></textarea></label><div>" );
// $templateCache.put("fields/ion-textarea.html","<span ng-click = \"addTextArea()\" ng-show = \"options.templateOptions.isRepeatable || iFrameModel.textObj.isRepeat\" style = \"cursor : pointer; float: right;\" class=\"ion-android-add\"></span><div  ng-repeat = \"text in iFrameModel.textObj\" > <span ng-show = \"iFrameModel.textObj[$index].isChild\" ng-click = \"remove(iFrameModel.textObj[$index])\" class=\"ion-minus\" style = \"cursor : pointer; float:right;\"></span> <label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}</span><textarea rows=\"{{options.templateOptions.rows? options.templateOptions.rows: \'5\'}}\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-model = \"model[options.key]\"></textarea></label><div>" );
$templateCache.put("fields/ion-textarea.html","<label class=\"item item-input item-stacked-label form-group col-sm-12\" style=\"padding-left: 0px;\"><span class=\"input-label form-group col-sm-2\">{{options.templateOptions.label}}</span><div class=\"col-sm-4 form-group\"><textarea class=\"col-sm-10\" rows=\"{{options.templateOptions.rows? options.templateOptions.rows: \'5\'}}\" placeholder=\"{{options.templateOptions.placeholder}}\" ng-model=\"model[options.key]\"></textarea></div></label>");

// $templateCache.put("fields/ion-textarea.html","<span style = \"cursor : pointer;\" class=\"ion-android-add\"></span>" );

// $templateCache.put("fields/ion-textarea.html","src/template/textareaTemplate.html");

//$templateCache.put("fields/ion-signature.html","<div style=\"height: 250px;\"><label style=\"margin-left: 18px;font-size:17px;\">{{options.templateOptions.label}}</label><div signature-pad signature=\"app.signature\" close=\"app.close()\"></div><div></div></div><div><div></div></div>");
$templateCache.put("fields/ion-signature.html","<div style=\"height: 250px;\"><label class=\"item item-input item-stacked-label\" style=\"border-bottom: none;\"><span class=\"input-label\">{{options.templateOptions.label}}</span></label><div signature-pad signature=\"app.signature\" close=\"app.close()\"></div></div>");

$templateCache.put("fields/ion-ui-grid.html","<div class=\"item item-input item-stacked-label\" >{{options.templateOptions.label}}  <a style=\"cursor:pointer;\" ng-click=\"addRow()\">add</a> </div><div style =\"height: 100% !important;\" ui-grid=\"{ data: model[options.key], columnDefs: to.columnDefs, onRegisterApi: to.onRegisterApi,enableColumnMenus: to.enableColumnMenus,enableSorting: to.enableSorting}\" ui-grid-edit ui-grid-row-edit ui-grid-cellNav class=\"grid\" ng-dblclick=\"getFocus()\"></div>");

//$templateCache.put("fields/ion-ui-grid.html","<label class=\"item item-input item-stacked-label\"><span class=\"input-label\">{{options.templateOptions.label}}  <a style=\"cursor:pointer;\" ng-click=\"addRow()\">add</a> </span> </label><div style =\"height: 100% !important;\" ui-grid=\"{ data: model[options.key], columnDefs: to.columnDefs, onRegisterApi: to.onRegisterApi,enableColumnMenus: to.enableColumnMenus,enableSorting: to.enableSorting}\" ui-grid-edit ui-grid-row-edit ui-grid-cellNav class=\"grid\" ng-dblclick=\"getFocus()\"></div>");

$templateCache.put("fields/ion-toggle.html","<ion-toggle ng-model=\"model[options.key]\" toggle-class=\"toggle-{{options.templateOptions.toggleClass}}\">{{options.templateOptions.label}}</ion-toggle>");}]);