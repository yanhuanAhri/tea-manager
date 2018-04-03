(function(window) {
    window.utils = {
        //图片
        imgSize: 2048, //图片大小限制
        deviceImgSize: 500, //体重秤图片大小限制
        videoSize: 10240, //视频大小限制
        apkSize: 51200, //视频大小限制


        //对象转URL参数
        objectToQueryString: function(obj) {
            return '?' +
            Object.keys(obj).map(function(key) {
                return encodeURIComponent(key) + '=' +
                    encodeURIComponent(obj[key] === undefined || obj[key] == null ? '' : obj[key]);
            }).join('&');
        },
        //表格自适应高度
        tableSelfAdaptionHelght: function() {
            var thHeight = $('.ui-grid-header').height();
            var gridHeight = $('.grid').height();

            if (thHeight == null) {
                //$('.grid').css('height', gridHeight + 'px');
            } else {
                //$('.grid').css('height', (thHeight + gridHeight) + 'px');
            }

            //$('.grid').attr('style', 'height: '+ ($(window).height() - 180) +'px;');
        },
        //没有数据提示
        gridNoDataTip: function(data) {
            if (data == null || data == '') {
                $('.ui-grid-viewport').append('<div class="grid-tip">没有数据</div>');
            } else {
                $('.ui-grid-viewport .grid-tip').remove();
            }
        },
        /**
         * 获取图片base64数据
         * @author Sea
         * @param {string} domId
         * @param {function} callFuns 回调
         * @date 2017-11-13
         * @return void
         */
        getImgBase64: function(domId, callFuns) {
            var file = document.querySelector('#' + domId).files[0];

            //验证图片格式
            if (file.name && file.name.indexOf('.jpg') >= 0 || file.name.indexOf('.png') >= 0 || file.name.indexOf('.PNG') >= 0 || file.name.indexOf('.jpeg') >= 0 || file.name.indexOf('.gif') >= 0) {
                var reader = new FileReader();  //本地预览
                reader.onload = function() {
                    callFuns(reader.result);
                }

                reader.readAsDataURL(file); //Base64
            } else {
                callFuns('error');
            }
        },
        /**
         * 获取base64数据
         * @author Sea
         * @param {string} domId
         * @param {function} callFuns 回调
         * @date 2017-11-13
         * @return void
         */
        getVideoBase64: function(domId, callFuns) {
            var file = document.querySelector('#' + domId).files[0];

            //验证图片格式
            if (file.name && file.name.indexOf('.mp4') >= 0) {
                var reader = new FileReader();  //本地预览
                reader.onload = function() {
                    callFuns(reader.result);
                }

                reader.readAsDataURL(file); //Base64
            } else {
                callFuns('error');
            }
        },
        /**
         * 获取APKbase64数据
         * @author Sea
         * @param {string} domId
         * @param {function} callFuns 回调
         * @date 2017-11-15
         * @return void
         */
        getApkBase64: function(domId, callFuns) {
            var file = document.querySelector('#' + domId).files[0];

            //验证APK格式
            if (file.name && file.name.indexOf('.apk') >= 0) {
                var reader = new FileReader();  //本地预览
                reader.onload = function() {
                    callFuns(reader.result);
                }

                reader.readAsDataURL(file); //Base64
            } else {
                callFuns('error');
            }
        },
        /**
         * 获取xls的base64数据
         * @author Sea
         * @param {string} domId
         * @param {function} callFuns 回调
         * @date 2018-02-02
         * @return void
         */
        getxlsFileBase64: function(domId, callFuns) {
            var file = document.querySelector('#' + domId).files[0];

            //验证APK格式
            if (file.name && file.name.indexOf('.xls') >= 0 || file.name && file.name.indexOf('.xlsx') >= 0) {
                var reader = new FileReader();  //本地预览
                reader.onload = function() {
                    callFuns(reader.result);
                }

                reader.readAsDataURL(file); //Base64
            } else {
                callFuns('error');
            }
        },
        /**
         * 获取上传文件大小
         * @author Sea
         * @param {string} base64 图片数据
         * @param {string} type 类型：img-图片，video-视频，app-安卓软件
         * @date 2017-11-14
         * @return void
         */
        getFileSize: function(base64, type) {
            var imgSize = Number((base64.length - (base64.length / 8) * 2) / 1024).toFixed(2);

            if (type == 'img' || type === undefined) {
                return imgSize > this.imgSize;
            } else if(type == 'deviceImg') {
            	return imgSize > this.deviceImgSize;
            } else if (type == 'video') {
                return imgSize > this.videoSize;
            } else if (type == 'app') {
                return imgSize > this.apkSize;
            } else {
                throw new Error('类型参数不正确');
            }
        },
        /**
         * 格式化PHP时间戳
         * @author Sea
         * @param {int} timeStamp 时间戳
         * @param {string} format 格式
         * @date 2017-11-25
         * @return string
         */
        formatDate: function(timeStamp, format) {
            var today = new Date(timeStamp);
            var y = today.getFullYear();
            var m = (today.getMonth() + 1 < 10) ? '0' + (today.getMonth() + 1) : (today.getMonth() + 1);
            var d = (today.getDate() < 10) ? '0' + today.getDate() : today.getDate();
            var h = (today.getHours() < 10) ? '0' + today.getHours() : today.getHours();
            var i = (today.getMinutes() < 10) ? '0' + today.getMinutes() : today.getMinutes();
            var s = (today.getSeconds() < 10) ? '0' + today.getSeconds() : today.getSeconds();

            var time = '';

            if (format === 'y-m-d h:i') {
                time = y + '-' + m + '-' + d + ' ' + h + ':' + i;
            } else if (format === 'y-m-d h:i:s') {
                time = y + '-' + m + '-' + d + ' ' + h + ':' + i + ':' + s;
            } else if (format === 'y-m-d') {
                time = y + '-' + m + '-' + d;
            } else if (format === 'y-m') {
                time = y + '-' + m;
            } else if (format === 'ymd') {
                time = y + '' + m + '' + d;
            } else if (format === 'd') {
                time = d;
            } else if (format === 'h:i') {
                time = h + ':' + i;
            } else if (format === 'h') {
                time = h;
            }

            return time;
        },
        /**
         * 计算日期之间相差多少天
         * @author Sea
         * @param {string} sDate1 日期1格式为2006-12-18
         * @param {string} sDate2 日期2
         * @date 2017-03-28
         */
        dateDifference: function (sDate1, sDate2) {
            var dateSpan;
            var iDays;

            sDate1 = Date.parse(sDate1);
            sDate2 = Date.parse(sDate2);
            dateSpan = sDate2 - sDate1;
            dateSpan = Math.abs(dateSpan);
            iDays = Math.floor(dateSpan / (24 * 3600 * 1000));

            return iDays;
        },
        /**
         * 搜索前后时间，今日、昨日、本周、上周、本月、上月
         * @author Sea
         * @param {string} option 对应今日、昨日、本周、上周、本月、上月
         * @date 2017-03-28
         */
        dateRange: function(option) {
            var from = "";
            var to = "";
            var fromDate = undefined;
            var toDate = undefined;
            var today = new Date();

            if (option == "today") {//今日
                fromDate = today;
                toDate = today;
            } else if (option == "yestoday") {//昨日
                var yestoday = new Date(today);
                yestoday.setDate(today.getDate() - 1);
                fromDate = yestoday;
                toDate = yestoday;
            } else if (option == "frontday") {//前日
                var frontday = new Date(today);
                frontday.setDate(today.getDate() - 2);
                fromDate = frontday;
                toDate = frontday;
            } else if (option == "nearlyWeek") {//近一周
                var day = today.getDay();
                fromDate = new Date(today);
                fromDate.setDate(today.getDate() - 7);

                toDate = new Date(today);
                toDate.setDate(today.getDate() - 1);
            }  else if (option == "thisWeek") {//本周
                var day = today.getDay();
                fromDate = new Date(today);
                fromDate.setDate(today.getDate() - (day - 1));

                toDate = new Date(today);
                toDate.setDate(today.getDate() + (7 - day));
            } else if (option == "prevWeek") {//上周
                var day = today.getDay();
                fromDate = new Date(today);
                fromDate.setDate(today.getDate() - (day - 1 + 7));

                toDate = new Date(today);
                toDate.setDate(today.getDate() - day);
            } else if (option == "thisMonth") {//本月
                fromDate = new Date(today);
                fromDate.setDate(1);

                toDate = new Date(today);
                toDate.setDate(1);
                toDate.setMonth(today.getMonth() + 1);
                toDate.setDate(0);
            } else if (option == "prevMonth") {//上月
                fromDate = new Date(today);
                fromDate.setDate(1);
                fromDate.setMonth(fromDate.getMonth() - 1);

                toDate = new Date(today);
                toDate.setDate(0);

            } else if (option == "thisQuarter") {//本季度
                var q = Math.floor(today.getMonth() / 3) + 1;
                fromDate = new Date(today);
                fromDate.setMonth(q * 3 - 3);
                fromDate.setDate(1);

                toDate = new Date(today);
                toDate.setMonth(q * 3);
                toDate.setDate(0);
            } else if (option == "prevQuarter") {//上季度
                var q = Math.floor(today.getMonth() / 3);
                fromDate = new Date(today);
                fromDate.setMonth(q * 3 - 3);
                fromDate.setDate(1);

                toDate = new Date(today);
                toDate.setMonth(q * 3);
                toDate.setDate(0);
            } else if (option == "thisYear") {//今年
                fromDate = new Date(today);
                fromDate.setMonth(0);
                fromDate.setDate(1);

                toDate = new Date(today);
                toDate.setFullYear(today.getFullYear() + 1);
                toDate.setMonth(0);
                toDate.setDate(0);
            }

            from = this.getDateString(fromDate);
            to = this.getDateString(toDate);

            return {
                startTime: from,
                endTime: to
            }
        },
        //格式化日期
        getDateString: function(date) {
            var y = date.getFullYear();
            var m = (date.getMonth() + 1 < 10) ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1);
            var d = (date.getDate() < 10) ? '0' + date.getDate() : date.getDate();
            var h = (date.getHours() < 10) ? '0' + date.getHours() : date.getHours();

            var str = y.toString() + "-" + m.toString() + "-" + d.toString();

            return str;
        },
        //删除提示tooltip
        deletTip: function() {
            $('[data-toggle="tooltip"]').blur();

            if (document.querySelector('.tooltip')) {
                document.body.removeChild(document.querySelector('.tooltip'));
            }
        },
        /**
         * 为了更好的用户体验，记录表格设置显示列，下次打开浏览器显示设置的列，清除缓存有清除到失效
         * @author Sea
         * @param {array} 记录表格设置显示列数据
         * @param {array} 原始表格显示列数据
         * @date 2017-03-28
         */
        recordTableColumn: function(scaleMachineListData, listData) {
            for (var i in scaleMachineListData) {
                if (scaleMachineListData[i].visible === undefined) {
                    listData[i].visible = true;
                } else {
                    if (scaleMachineListData[i].field == listData[i].field) {
                        listData[i].visible = scaleMachineListData[i].visible;
                    }
                }
            }

            return listData;
        },
        /**
         * 对象转数组，用于首页图表处理
         * @author Sea
         * @param {object} obj 对象
         * @date 2017-03-28
         */
        objectToArray: function(obj) {
            var arr = [];

            for (var i in obj) {
                arr.push({
                    time: i,
                    value: obj[i]
                })
            }

            return arr;
        }
    }
})(window);