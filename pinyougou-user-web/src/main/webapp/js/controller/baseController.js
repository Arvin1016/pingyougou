app.controller("baseController", function ($scope) {
    // 分页控件配置
    $scope.paginationConf = {
        // 当前页
        currentPage: 1,
        // 总记录数
        totalItems: 10,
        // 每页记录数
        itemsPerPage: 10,
        // 分页选项
        perPageOptions:[10, 20, 30, 40, 50],
        // 当页码变化时执行的方法
        onChange: function () {
            $scope.reloadList();
        }
    };

    // 刷新列表
    $scope.reloadList = function(){
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
        $scope.selectIds = []; // 页面刷新，应清空
    };

    // 获取选中复选框的id
    $scope.selectIds = [];
    $scope.updateSelection = function ($event, id) { // $event获取源，$event.target获取元素对象
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            var index = $scope.selectIds.indexOf(id); // 获得id元素的索引
            $scope.selectIds.splice(index, 1); // 从index位置移除m个元素
        }
    };

    // 提取json字符串中的某个属性，返回自定义拼接字符串
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += "，";
            }
            value += json[i][key];
        }
        return value;
    };

    // 在list集合中，根据key值查询对象，查到直接返回该对象
    $scope.searchObjectByKey = function (list, key, value) {

        // 遍历集合
        if (list != null) {
            for (var i = 0; i < list.length; i++) {
                if (list[i][key] === value) {
                    return list[i];
                }
            }
        }
        return null;
    };


});