(function () {
    /*--------------------------------------------------------------
     | 全局变量
     |--------------------------------------------------------------
    */
    var $ctrl = this;

    var app = angular.module('DashboardApp', ['ngTouch', 'ui.grid', 'ui.grid.autoResize', 'ui.grid.moveColumns', 'ui.grid.resizeColumns', 'ui.grid.pagination', 'toggle-switch', 'ui.grid.selection', ['js/common/main.css', 'js/dashboard/main.css']]);

    /*--------------------------------------------------------------
     | 控制器
     |--------------------------------------------------------------
    */
    app.controller('DashboardController', ['$scope', 'i18nService', 'DashboardService', '$uibModal', 'uiGridConstants', function ($scope, i18nService, service, $modal, uiGridConstants) {
        var followChart = null;
        var useChart = null;
        var rankingChart = null;
        var takeoffChart = null;
        var deviceDeliveryChart = null;
        var deliveryStatusChart = null;

        //表格切换
        $scope.tabIsActive = 'publicNumber';

        $scope.canonTabData = [
            {
                type: 'publicNumber',
                name: '公众号加粉情况'
            },
            {
                type: 'device',
                name: '设备加粉情况'
            }
        ];

        //日期按钮搜索
        $scope.isActive = 'yestoday';

        $scope.timeBtnData = [
            {
                type: 'yestoday',
                name: '昨日'
            },
            {
                type: 'frontday',
                name: '前日'
            },
            {
                type: 'nearlyWeek',
                name: '一周'
            }
        ];

        $scope.showOrHide = '隐藏过滤';
        $scope.serviceStartTime = '';
        $scope.serviceEndTime = '';

        //分页初始化设置
        var pageOptions = {
            page: 1,
            sort: ''
        }

        //分页参数
        var pageParams = {};

        //表格语言中文
        i18nService.setCurrentLang("zh-cn");

        //公众号加粉表格
        $scope.publicNumberGridOptions = {
            paginationPageSizes: [5, 10, 25, 50, 75, 100, 500],
            paginationPageSize: 25,
            useExternalPagination: true,
            useExternalSorting: true,
            enableGridMenu: true,
            enableFiltering: true,
            useExternalFiltering: true,
            multiSelect: false,
            allowCopy: false,
            modifierKeysToMultiSelect: false,
            noUnselect: false,
            enableRowSelection: false,
            enableRowHeaderSelection: false,
            enableColumnResizing: true,
            columnDefs: [
                {
                    field: 'appName',
                    name: '公众号名称',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'putScaleNum',
                    name: '已投放设备',
                    enableSorting: false,
                    enableFiltering: false,
                },
                {
                    field: 'avgScaleFans',
                    name: '平均平台加粉',
                    enableSorting: false,
                    enableFiltering: false,
                },
                {
                    field: 'subscribeNum',
                    name: '新增关注数',
                    enableSorting: false,
                    enableFiltering: false,
                },
                {
                    field: 'noSubscribeNum',
                    name: '取消关注数',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'newFans',
                    name: '新增粉丝',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'scanNum',
                    name: '二维码生成数',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'phoneScanNum',
                    name: '二维码扫码数',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'longPressNum',
                    name: '长按二维码关注数',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'subscribedScanNum',
                    name: '已关注扫码数',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'scanDistanctNum',
                    name: '重复扫码数',
                    enableSorting: false,
                    enableFiltering: false
                }
            ],
            onRegisterApi: function (gridApi) {
                $scope.gridApi = gridApi;

                //排序事件
                $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                    if (typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
                        pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
                        pageParams.pageSize = grid.options.paginationPageSize;
                        pageParams.sort = sortColumns[0].name + ' ' + sortColumns[0].sort.direction;

                        //获取过滤筛选参数
                        //getFilter(grid);

                        //请求更新表格数据
                        service.getPage(pageParams, getPageCallbackFun);
                    }
                });

                //过滤搜索事件
                $scope.gridApi.core.on.filterChanged($scope, function () {
                    pageParams.pageNumber = $scope.gridOptions.paginationCurrentPage;
                    pageParams.pageSize = this.grid.options.paginationPageSize;

                    //获取过滤筛选参数
                    //getFilter(this.grid);

                    //请求更新表格数据
                    service.getPage(pageParams, getPageCallbackFun);
                });

                //分页
                $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
                    pageParams.pageNumber = newPage;
                    pageParams.pageSize = pageSize;

                    //获取过滤筛选参数
                    //getFilter(this.grid);

                    //请求更新表格数据
                    service.getPage(pageParams, getPageCallbackFun);
                });
            }
        };

        /**
         * 请求成功回调函数获取表格数据
         * @author Sea
         * @param {object} response 表格数据
         * @date 2017-10-11
         * @return viod
         */
        var getPageCallbackFun = function (response) {
            if (response.official == null) {
                response.official = {
                    subSum: 0,
                    noSum: 0,
                    newsum: 0,
                    page: {
                        total: 0,
                        data: []
                    }
                };
            }

            $scope.publicNumberGridOptions.totalItems = response.total;
            $scope.publicNumberGridOptions.data = response.data;

            //设备统计
            /*
            $scope.placeTotal = response.monitor.placeTotal; //已投放
            $scope.normalTotal = response.monitor.normalTotal; //在线
            $scope.shutdownTotal = response.monitor.shutdownTotal; //关机
            $scope.alertTotal = response.monitor.alertTotal; //告警
            $scope.breakdownTotal = response.monitor.breakdownTotal; //故障
            $scope.timeoutTotal = response.monitor.timeoutTotal;//超时检查


            var newNumberOfConcerns = 0; //新增关注数
            var cancelTheNumberOfConcerns = 0; //取消关注数
            var newNumberOfNewFans = 0; //新增粉丝数
            */


            //底部统计
            /*
            var tableFooterHtml = '<div ng-repeat="(rowRenderIndex, row) in rowContainer.renderedRows track by $index" class="ui-grid-row ui-grid-row-footer ng-scope" ng-style="Viewport.rowStyle(rowRenderIndex)" ng-class="{\'ui-grid-row-selected\': row.isSelected,\'ui-grid-tree-header-row\': row.treeLevel > -1}" style="background: #cccccc; height: 90px;">\n' +
                '    <div role="row" ui-grid-row="row" row-render-index="rowRenderIndex" class="ng-isolate-scope" style="float: left; height: 90px;">\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000D"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514289558187-0-uiGrid-000D-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">' +
                                '<span>本页合计</span><br/>' + $scope.publicNumberGridOptions.data.length +
                            '</div>\n' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000E"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514289558187-0-uiGrid-000E-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">' +
                                '<span>总记录合计</span><br/>' + response.totalData.subSum +
                            '</div>' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000F"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514289558187-0-uiGrid-000F-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">' +
                                '<span>总记录合计</span><br/>' + response.totalData.subSum +
                            '</div>' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000G"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514289558187-0-uiGrid-000G-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">' +
                                '<span>总记录合计</span><br/>' + response.totalData.subSum +
                            '</div>' +
                '        </div>\n' +
                '    </div>\n' +
                '</div>';

            setTimeout(function () {
                $('.grid-publicNumber-dashboard .ui-grid-canvas').append(tableFooterHtml);
            }, 50);

            */

        };

        //初始化获取数据
        pageParams.pageNumber = pageOptions.page;
        pageParams.pageSize = $scope.publicNumberGridOptions.paginationPageSize;
        pageParams.sort = pageOptions.sort ? pageOptions.sort : '';

        //获取昨天日期
        var timeObj = utils.dateRange('yestoday');
        pageParams.staTime = timeObj.startTime;
        pageParams.endTime = timeObj.endTime;
        //pageParams.staTime = '2018-01-15';
        //pageParams.endTime = '2018-01-15';

        service.getPage(pageParams, getPageCallbackFun);


        /**
         * 设备加粉情况
         */
        //分页初始化设置
        var devicePageOptions = {
            page: 1,
            sort: ''
        }

        //分页参数
        var devicePageParams = {};

        $scope.deviceGridOptions = {
            paginationPageSizes: [5, 10, 25, 50, 75, 100, 500],
            paginationPageSize: 25,
            useExternalPagination: true,
            useExternalSorting: true,
            enableGridMenu: true,
            enableFiltering: true,
            useExternalFiltering: true,
            multiSelect: false,
            allowCopy: false,
            modifierKeysToMultiSelect: false,
            noUnselect: false,
            enableRowSelection: false,
            enableRowHeaderSelection: false,
            enableColumnResizing: true,
            columnDefs: [
                {
                    field: 'scaleId',
                    name: '设备号',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'scaleSerialNumber',
                    name: '设备编码',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'scaleName',
                    name: '设备名称',
                    enableSorting: false,
                    enableFiltering: false,
                },
                {
                    field: 'addFansCount',
                    name: '新增关注',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'delFansCount',
                    name: '取消关注',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'newFans',
                    name: '新增粉丝数',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'sendCodeCount',
                    name: '随机码生成次数',
                    enableSorting: false,
                    enableFiltering: false
                },
                {
                    field: 'useCodeCount',
                    name: '随机码使用次数',
                    enableSorting: false,
                    enableFiltering: false
                }
            ],
            onRegisterApi: function (gridApi) {
                $scope.deviceGridApi = gridApi;

                //排序事件
                $scope.deviceGridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                    if (typeof(sortColumns[0]) != 'undefined' && typeof(sortColumns[0].sort) != 'undefined') {
                        devicePageParams.pageNumber = $scope.deviceGridOptions.paginationCurrentPage;
                        devicePageParams.pageSize = grid.options.paginationPageSize;
                        devicePageParams.sort = sortColumns[0].name + ' ' + sortColumns[0].sort.direction;

                        //获取过滤筛选参数
                        // getFilter(grid);

                        //请求更新表格数据
                        service.deviceGetPage(devicePageParams, deviceGetPageCallbackFun);
                    }
                });

                //过滤搜索事件
                $scope.deviceGridApi.core.on.filterChanged($scope, function () {
                    devicePageParams.pageNumber = $scope.deviceGridOptions.paginationCurrentPage;
                    devicePageParams.pageSize = this.grid.options.paginationPageSize;

                    //获取过滤筛选参数
                    //getFilter(this.grid);

                    //请求更新表格数据
                    service.deviceGetPage(pageParams, deviceGetPageCallbackFun);
                });

                //分页
                $scope.deviceGridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize, sortColumns) {
                    devicePageParams.pageNumber = newPage;
                    devicePageParams.pageSize = pageSize;

                    //获取过滤筛选参数
                    //getFilter(this.grid);

                    //请求更新表格数据
                    service.deviceGetPage(devicePageParams, deviceGetPageCallbackFun);
                });
            }
        };

        /**
         * 请求成功回调函数获取表格数据
         * @author Sea
         * @param {object} response 表格数据
         * @date 2017-10-11
         * @return viod
         */
        var deviceGetPageCallbackFun = function (response) {
            if (response.scale == null) {
                response.scale = {
                    subSum: 0,
                    noSum: 0,
                    newSum: 0,
                    randomSend: 0,
                    randomUse: 0,
                    page: {
                        total: 0,
                        data: []
                    }
                };
            }

            $scope.deviceGridOptions.totalItems = response.total;
            $scope.deviceGridOptions.data = response.data;

            /*
            //底部统计
            var tableFooterHtml = '<div ng-repeat="(rowRenderIndex, row) in rowContainer.renderedRows track by $index" class="ui-grid-row ui-grid-row-footer ng-scope" ng-style="Viewport.rowStyle(rowRenderIndex)" ng-class="{\'ui-grid-row-selected\': row.isSelected,\'ui-grid-tree-header-row\': row.treeLevel > -1}" style="background: #cccccc; height: 90px;">\n' +
                '    <div role="row" ui-grid-row="row" row-render-index="rowRenderIndex" class="ng-isolate-scope" style="float: left; height: 90px;">\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000H"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514290443629-0-uiGrid-000H-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">\n' +
                                '<span>本页合计</span><br/>' + $scope.deviceGridOptions.data.length +
                            '</div>\n' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000I"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514290443629-0-uiGrid-000I-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">\n' +
                                '<span>本页合计</span><br/>' + $scope.deviceGridOptions.data.length +
                            '</div>\n' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000J"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514290443629-0-uiGrid-000J-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">\n' +
                                '<span>本页合计</span><br/>' + newNumberOfConcerns + '<br/><span>总记录合计</span><br/>' + response.scale.subSum +
                            '</div>\n' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000K"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514290443629-0-uiGrid-000K-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">\n' +
                                '<span>本页合计</span><br/>' + cancelTheNumberOfConcerns + '<br/><span>总记录合计</span><br/>' + response.scale.noSum +
                            '</div>\n' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000L"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514290443629-0-uiGrid-000L-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">\n' +
                                '<span>本页合计</span><br/>' + newNumberOfNewFans + '<br/><span>总记录合计</span><br/>' + response.scale.newSum +
                            '</div>\n' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000M"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514290443629-0-uiGrid-000M-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">\n' +
                                '<span>本页合计</span><br/>' + generate + '<br/><span>总记录合计</span><br/>' + response.scale.randomSend +
                            '</div>\n' +
                '        </div>\n' +
                '        <div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid"\n' +
                '             ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'"\n' +
                '             class="ui-grid-cell ng-scope ui-grid-disable-selection ui-grid-coluiGrid-000N"\n' +
                '             ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="gridcell" ui-grid-cell=""\n' +
                '             id="1514290443629-0-uiGrid-000N-cell" style="height: 90px;">\n' +
                '            <div class="ui-grid-cell-contents ng-binding ng-scope">\n' +
                                '<span>本页合计</span><br/>' + use + '<br/><span>总记录合计</span><br/>' + response.scale.randomUse +
                            '</div>\n' +
                '        </div>\n' +
                '    </div>\n' +
                '</div>\n';

            setTimeout(function () {
                $('.grid-device-dashboard .ui-grid-canvas').append(tableFooterHtml);
            }, 50);
            */
        };

        //公众号加粉情况、设备加粉情况切换
        $scope.canonTab = function (type) {
            $scope.isActive = 'yestoday';
            $scope.serviceStartTime = '';
            $scope.serviceEndTime = '';
            $scope.tabIsActive = type;
            $scope.getDeviceStatus = false; //缓存状态，true为缓存


            if (type == 'publicNumber') {
                //初始化获取数据
                pageParams.pageNumber = 1;
                pageParams.pageSize = $scope.publicNumberGridOptions.paginationPageSize;
                pageParams.sort = pageOptions.sort ? pageOptions.sort : '';

                //获取昨天日期
                var timeObj = utils.dateRange('yestoday');
                pageParams.staTime = timeObj.startTime;
                pageParams.endTime = timeObj.endTime;
                //pageParams.staTime = '2017-12-01';
                //pageParams.endTime = '2017-12-20';

                service.getPage(pageParams, getPageCallbackFun);
            } else if (type == 'device') {
                //获取设备图表数据，并缓存数据
                if (!$scope.getDeviceStatus) {
                    service.getDevice(function(response) {
                        $scope.getDeviceStatus = false;
                        $scope.delivery(response);
                        $scope.deliveryStatus(response);
                    });
                }

                //初始化获取数据
                devicePageParams.pageNumber = 1;
                devicePageParams.pageSize = $scope.deviceGridOptions.paginationPageSize;
                devicePageParams.sort = devicePageOptions.sort ? devicePageOptions.sort : '';

                //获取昨天日期
                var timeObj = utils.dateRange('yestoday');
                devicePageParams.staTime = timeObj.startTime;
                devicePageParams.endTime = timeObj.endTime;
                //devicePageParams.staTime = '2018-01-15';
                //devicePageParams.endTime = '2018-01-15';

                service.deviceGetPage(devicePageParams, deviceGetPageCallbackFun);
            }
        }

        //日期按钮搜索切换
        $scope.tabTimeBtn = function(type) {
            $scope.isActive = type;
            $scope.serviceStartTime = '';
            $scope.serviceEndTime = '';
            var timeObj = null;

            if (type == 'yestoday') {
                timeObj = utils.dateRange('yestoday');
            } else if (type == 'frontday') {
                timeObj = utils.dateRange('frontday');
            } else if (type == 'nearlyWeek') {
                timeObj = utils.dateRange('nearlyWeek');
            }

            //判断是否是公众号加粉搜索还是设备号加粉搜索
            if ($scope.tabIsActive == 'publicNumber') {
                pageParams.pageNumber = pageOptions.page;
                pageParams.pageSize = 25;
                pageParams.staTime = timeObj.startTime;
                pageParams.endTime = timeObj.endTime;
                service.getPage(pageParams, getPageCallbackFun);
            } else if ($scope.tabIsActive == 'device') {
                //初始化获取数据
                devicePageParams.pageNumber = devicePageOptions.page;
                devicePageParams.pageSize = 25;
                devicePageParams.sort = devicePageOptions.sort ? devicePageOptions.sort : '';
                devicePageParams.staTime = timeObj.startTime;
                devicePageParams.endTime = timeObj.endTime;
                service.deviceGetPage(devicePageParams, deviceGetPageCallbackFun);
            } else {
                throw new Error('参数不正确');
            }
        }

        //日期范围搜索
        $scope.timeSearch = function() {
            //验证搜索日期范围不能为空
            if ($scope.serviceStartTime == '') {
                toastr["warning"]("开始时间未填写");
            } else if ($scope.serviceEndTime == '') {
                toastr["warning"]("结束时间未填写");
            } else {
                //验证搜索日期是否超出了31天
                var startTime = $scope.serviceStartTime.replace(/[^1234567890.]+/g, '');
                var endTime = $scope.serviceEndTime.replace(/[^1234567890.]+/g, '');

                //验证搜索日期是否超出了31天
                var startTime = $scope.serviceStartTime.replace(/[^1234567890.]+/g, '');
                var endTime = $scope.serviceEndTime.replace(/[^1234567890.]+/g, '');

                if (startTime > endTime) {
                    toastr["warning"]("开始时间不能大于结束时间");
                } else if (utils.dateDifference($scope.serviceStartTime, $scope.serviceEndTime) > 31) {
                    toastr["warning"]("时间范围不能大于31天");
                } else {
                    $scope.isActive = '';

                    //判断是否是公众号加粉搜索还是设备号加粉搜索
                    if ($scope.tabIsActive == 'publicNumber') {
                        pageParams.pageNumber = pageOptions.page;
                        pageParams.pageSize = 25;
                        pageParams.staTime = startTime;
                        pageParams.endTime = endTime;
                        service.getPage(pageParams, getPageCallbackFun);
                    } else if ($scope.tabIsActive == 'device') {
                        //初始化获取数据
                        devicePageParams.pageNumber = devicePageOptions.page;
                        devicePageParams.pageSize = 25;
                        devicePageParams.sort = devicePageOptions.sort ? devicePageOptions.sort : '';
                        devicePageParams.staTime = startTime;
                        devicePageParams.endTime = endTime;
                        service.deviceGetPage(devicePageParams, deviceGetPageCallbackFun);
                    }
                }
            }
        }


        /*-------------------------------------------------------------------------------
         | 公众号加粉情况
         |-------------------------------------------------------------------------------
         */

        /**
         * 新增关注数量图表数据
         */
        $scope.follow = function(response) {
            //新增关注数量
            var addNameArr = [];
            var addValueArr = [];

            if (response.officialCount.subscribe) {
                var subscribeArr = utils.objectToArray(response.officialCount.subscribe).reverse();

                for (var i in subscribeArr) {
                    addNameArr.push(subscribeArr[i].time);
                    addValueArr.push(subscribeArr[i].value);
                }
            }

            //取消关注数量
            var cancelNameArr = [];
            var cancelValueArr = [];

            if (response.officialCount.noSubscribe) {
                var noSubscribeArr = utils.objectToArray(response.officialCount.noSubscribe).reverse();

                for (var i in noSubscribeArr) {
                    cancelNameArr.push(noSubscribeArr[i].time);
                    cancelValueArr.push(noSubscribeArr[i].value);
                }
            }

            var option = {
                title: {
                    text: '关注数量',
                    left: '15',
                    top: '15'
                },
                backgroundColor: '#F2F2F2',
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['新增关注数量','取消关注数量'],
                    top: '20'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '12%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: addNameArr, //['周一','周二','周三','周四','周五','周六','周日']
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name: '新增关注数量',
                        type: 'line',
                        stack: '总量',
                        data: addValueArr //[120, 132, 101, 134, 90, 230, 210]
                    },
                    {
                        name: '取消关注数量',
                        type: 'line',
                        stack: '总量',
                        data: cancelValueArr //[220, 182, 191, 234, 290, 330, 310]
                    }
                ],
                dataZoom: [{
                    type: 'slider',
                    disabled: true,
                    start: 0,
                    end: 12
                },
                {
                    handleSize: 0,
                    handleStyle: {
                        color: '#00A0E8',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    },
                    textStyle: false
                }]
            };

            followChart = echarts.init(getWidthToHeight('J-follow'));

            setTimeout(function() {
                followChart = echarts.init(getWidthToHeight('J-follow'));
                followChart.setOption(option, true);
            }, 5);
        }

        /**
         * 使用数量图表数据
         */
        $scope.use = function(response) {
            //总称重数量
            var publicNameArr = [];
            var scanValueArr = [];

            if (response.officialCount.scanNum) {
                var scanNumArr = utils.objectToArray(response.officialCount.scanNum).reverse();

                for (var i in scanNumArr) {
                    publicNameArr.push(scanNumArr[i].time);
                    scanValueArr.push(scanNumArr[i].value);
                }
            }

            //总使用数量
            var useValueArr = [];

            if (response.officialCount.phoneScanNum) {
                var phoneScanNumArr = utils.objectToArray(response.officialCount.phoneScanNum).reverse();

                for (var i in phoneScanNumArr) {
                    useValueArr.push(phoneScanNumArr[i].value);
                }
            }

            //总长按数量
            var pressValueArr = [];

            if (response.officialCount.longPressNum) {
                var longPressNumArr = utils.objectToArray(response.officialCount.longPressNum).reverse();

                for (var i in longPressNumArr) {
                    pressValueArr.push(longPressNumArr[i].value);
                }
            }

            var option = {
                title: {
                    text: '使用数量',
                    left: '15',
                    top: '15'
                },
                backgroundColor: '#F2F2F2',
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['总称重数量','总使用数量', '总长按数量'],
                    top: '20'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '12%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: publicNameArr, //['周一','周二','周三','周四','周五','周六','周日']
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name: '总称重数量',
                        type: 'line',
                        stack: '总量',
                        data: scanValueArr
                    },
                    {
                        name: '总使用数量',
                        type: 'line',
                        stack: '总量',
                        data: useValueArr
                    },
                    {
                        name: '总长按数量',
                        type: 'line',
                        stack: '总量',
                        data: pressValueArr //[220, 182, 191, 234, 290, 330, 310]
                    }
                ],
                dataZoom: [{
                    type: 'slider',
                    disabled: true,
                    start: 0,
                    end: 12
                },
                {
                    handleSize: 0,
                    handleStyle: {
                        color: '#00A0E8',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    },
                    textStyle: false
                }]
            };

            setTimeout(function() {
                useChart = echarts.init(getWidthToHeight('J-use'));
                useChart.setOption(option, true);
            }, 5);
        }

        /**
         * 订阅号加粉排行图表数据
         */
        $scope.ranking = function(response) {
            var nameArr = [];
            var valueArr = [];

            if (response.officialAddRankCount) {
                for (var i in response.officialAddRankCount) {
                    nameArr.push(i);
                    valueArr.push(response.officialAddRankCount[i]);
                }
            }

            var option = {
                title: {
                    text: '订阅号加粉排行',
                    left: '15',
                    top: '15'
                },
                backgroundColor: '#F2F2F2',
                color: ['#00A0E8'],
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {
                        type : 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '12%',
                    containLabel: true
                },
                xAxis : [
                    {
                        type : 'category',
                        data : nameArr, //['老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐', '老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐', '老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐', '老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐'],
                        axisTick: {
                            alignWithLabel: true
                        }
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'加粉数',
                        type:'bar',
                        barWidth: '30%',
                        label: {
                            normal: {
                                show: true,
                                position: 'top'
                            }
                        },
                        data: valueArr //[10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220]
                    }
                ],
                dataZoom: [{
                    type: 'slider',
                    disabled: true,
                    start: 0,
                    end: 12
                },
                {
                    handleSize: 0,
                    handleStyle: {
                        color: '#00A0E8',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    },
                    textStyle: false
                }]
            };

            setTimeout(function() {
                rankingChart = echarts.init(getWidthToHeight('J-ranking'));
                rankingChart.setOption(option, true);
            }, 5);
        }

        /**
         * 订阅号加粉取关率
         */
        $scope.takeoff = function(response) {
            var nameArr = [];
            var valueArr = [];

            if (response.officialAddRaTeCount) {
                for (var i in response.officialAddRaTeCount) {
                    nameArr.push(i);
                    valueArr.push(response.officialAddRaTeCount[i]);
                }
            }

            var option = {
                title: {
                    text: '订阅号取关排行',
                    left: '15',
                    top: '15'
                },
                backgroundColor: '#F2F2F2',
                color: ['#00A0E8'],
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {
                        type : 'shadow'
                    }
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '12%',
                    containLabel: true
                },
                xAxis : [
                    {
                        type : 'category',
                        data : nameArr, //['老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐', '老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐', '老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐', '老北京吃喝玩乐', '广州吃喝玩乐', '桂林吃喝玩乐', '上海吃喝玩乐', '章港吃喝玩乐', '香格里拉吃喝玩乐', '丽江吃喝玩乐'],
                        axisTick: {
                            alignWithLabel: true
                        }
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : [
                    {
                        name:'加粉数',
                        type:'bar',
                        barWidth: '30%',
                        label: {
                            normal: {
                                show: true,
                                position: 'top'
                            }
                        },
                        data: valueArr //[10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220]
                    }
                ],
                dataZoom: [{
                    type: 'slider',
                    disabled: true,
                    start: 0,
                    end: 12
                },
                {
                    handleSize: 0,
                    handleStyle: {
                        color: '#00A0E8',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0, 0, 0, 0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    },
                    textStyle: false
                }]
            };

            setTimeout(function() {
                takeoffChart = echarts.init(getWidthToHeight('J-takeoff'));
                takeoffChart.setOption(option, true);
            }, 5);
        }

        /*-------------------------------------------------------------------------------
         | 设备加粉情况
         |-------------------------------------------------------------------------------
         */
        /**
         * 已投放设备
         */
        $scope.delivery = function(response) {
            var deviceData = [];
            var deviceNameData = [];
            var deviceCount = 0;

            for (var i in response.scaleMonitorCount) {
                if (i == '投放') {

                } else if (i == '已投放') {

                } else {
                    deviceData.push({
                        name: i,
                        value: response.scaleMonitorCount[i]
                    });

                    deviceNameData.push({
                        name: i,
                        value: response.scaleMonitorCount[i]
                    });
                }
            }

            //已投放设备
            var option = {
                backgroundColor: '#F2F2F2',
                color: ['#EFA949', '#E96929', '#CDD42F', '#FCCC00', '#CADBE3', '#00A0E8', '#56C2F1', '#77CCF3', '#01D1C6', '#6BB25A'],
                title : {
                    text: '已投放设备（'+ response.scaleMonitorCount['已投放'] +'/'+ response.scaleMonitorCount['投放'] +'）台',
                    left: '20',
                    top: '20',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{b} : {c}" //{a} <br/>{b} : {c} ({d}%)
                },
                legend: {
                    orient: 'vertical',
                    left: '20',
                    top: '60',
                    data: deviceNameData,
                    formatter: function(name) {
                        return name + '：' + response.scaleMonitorCount[name] + '台';
                    }
                },
                series : [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius : '70%',
                        center: ['60%', '53%', '30%', '40%'],
                        data: deviceData,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 100,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        },
                        label: {
                            show: false
                        }
                    }
                ]
            };

            deviceDeliveryChart = echarts.init(getWidthToHeight('J-deviceDelivery'));
            deviceDeliveryChart.setOption(option, true);
        }

        /**
         * 已投放设备状态
         */
        $scope.deliveryStatus = function(response) {
            var deviceData = [];
            var deviceNameData = [];

            for (var i in response.scaleStatisticsCount) {
                if (i != '确认发货') {
                    deviceData.push({
                        name: i,
                        value: response.scaleStatisticsCount[i]
                    });

                    deviceNameData.push({
                        name: i,
                        value: response.scaleStatisticsCount[i]
                    });
                }
            }

            //已投放设备状态
            var option = {
                backgroundColor: '#F2F2F2',
                color: ['#EFA949', '#E96929', '#CDD42F', '#FCCC00', '#CADBE3', '#00A0E8', '#56C2F1', '#77CCF3'],
                title : {
                    text: '设备投放状态',
                    left: '20',
                    top: '20',
                    //subtext: '纯属虚构',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{b} : {c}" //{a} <br/>{b} : {c} ({d}%)
                },
                legend: {
                    orient: 'vertical',
                    left: '20',
                    top: '60',
                    data: deviceNameData,
                    formatter: function(name) {
                        return name + '：' + response.scaleStatisticsCount[name] + '台';
                    }
                },
                series : [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius : '70%',
                        center: ['60%', '53%', '30%', '40%'],
                        data: deviceData,
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 100,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        },
                        label: {
                            show: false
                        }
                    }
                ]
            };

            deliveryStatusChart = echarts.init(getWidthToHeight('J-deliveryStatus'));
            deliveryStatusChart.setOption(option, true);
        }

        /**
         * 图表响应式
         */
        var getWidthToHeight = function(domId) {
            var dom = document.getElementById(domId);
            var innerWidthToHeight = Number($('.content').width()) - 30;

            if (window.innerWidth < 750) {
                dom.style.width = (innerWidthToHeight + 30) + 'px';
            } else {
                dom.style.width = (innerWidthToHeight / 2) + 'px';
            }

            return dom;
        }

        /**
         * 监听浏览器变化调整图表响应式
         */
        var getOnresize = function() {
            getWidthToHeight('J-follow');
            followChart.resize();

            getWidthToHeight('J-use');
            useChart.resize();

            getWidthToHeight('J-ranking');
            rankingChart.resize();

            getWidthToHeight('J-takeoff');
            takeoffChart.resize();

            if (deviceDeliveryChart != null) {
                getWidthToHeight('J-deviceDelivery');
                deviceDeliveryChart.resize();
            }

            if (deliveryStatusChart) {
                getWidthToHeight('J-deliveryStatus');
                deliveryStatusChart.resize();
            }
        }

        //获取订阅号图表数据
        service.getPublicNumber(function(response) {
            //初始化加载图表数据
            $scope.ranking(response);
            $scope.takeoff(response);
            $scope.follow(response);
            $scope.use(response);

            //执行浏览器变化调整图表响应式
            window.onresize = function() {
                getOnresize();
            };

            //如果切换菜单显示方式
            $('.sidebar-toggle').click(function() {
                setTimeout(function() {
                    getOnresize();
                }, 500);
            });
        });
    }]);


    /*--------------------------------------------------------------
     | 自定义服务
     |--------------------------------------------------------------
    */
    app.factory('DashboardService', ['$http', function ($http) {
        return {
            /**
             * 获取表格分页数据
             * @author Sea
             * @param {object} pageParams 页码、条数、过滤、排序
             * @param {function} callbackFun 分页回调
             * @date 2017-10-11
             * @return void
             */
            getPage: function (pageParams, callbackFun) {
                pageParams.pageNumber = (pageParams.pageNumber - 1);

                $http.get('/home/homePage/appFans', {
                    params: pageParams
                }).success(function (response, status, headers, congfig) {
                    if (callbackFun) {
                        callbackFun(response);
                    } else {
                        throw new Error('未填写参数');
                    }
                });
            },
            /**
             * 获取表格分页数据
             * @author Sea
             * @param {object} pageParams 页码、条数、过滤、排序
             * @param {function} callbackFun 分页回调
             * @date 2017-10-11
             * @return void
             */
            deviceGetPage: function (pageParams, callbackFun) {
                pageParams.pageNumber = (pageParams.pageNumber - 1);

                $http.get('/home/homePage/scaleFans', {
                    params: pageParams
                }).success(function (response, status, headers, congfig) {
                    if (callbackFun) {
                        callbackFun(response);
                    } else {
                        throw new Error('未填写参数');
                    }
                });
            },
            /**
             * 订阅号加粉排行柱状图图表
             * @author Sea
             * @param {function} callbackFun 回调
             * @date 2018-02-09
             * @return void
             */
            getPublicNumber: function (callbackFun) {
                $http.get('/home/homePage/officialAddSituation')
                .success(function (response, status, headers, congfig) {
                    if (callbackFun) {
                        callbackFun(response);
                    } else {
                        throw new Error('未填写参数');
                    }
                });
            },
            /**
             * 设备加粉排行柱状图图表
             * @author Sea
             * @param {function} callbackFun 回调
             * @date 2018-02-09
             * @return void
             */
            getDevice: function (callbackFun) {
                $http.get('/home/homePage/scaleAddSituation')
                .success(function (response, status, headers, congfig) {
                    if (callbackFun) {
                        callbackFun(response);
                    } else {
                        throw new Error('未填写参数');
                    }
                });
            }
        }
    }]);

    //日期
    app.directive('dasDatePicker', function () {
        return {
            require: 'ngModel',
            link: function ($scope, element, attr, ngModel) {
                $(element).datetimepicker({
                    format: "yyyy-mm-dd hh:ii",
                    autoclose: true,
                    todayBtn: true,
                    clearBtn: true,
                    startDate: new Date(),
                    minuteStep: 10
                }).on('changeDate', function (ev) {

                }).on('changeDate', function (ev) {
                    $scope.$apply(function () {
                        $scope.startTime = $scope.publish ? $scope.publish.replace(/[^1234567890.]+/g, '') : '';
                        $scope.endTime = $scope.expire ? $scope.expire.replace(/[^1234567890.]+/g, '') : '';

                        if (Number($scope.startTime) > Number($scope.endTime)) {
                            $scope.timeVerificationStatus = true;

                        } else {
                            $scope.timeVerificationStatus = false;

                        }
                    });
                });
            }
        }
    });
})();
