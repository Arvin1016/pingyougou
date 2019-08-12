//品牌控制层
app.controller('baseController', function ($scope) {

    $scope.paginationConf = {
        currentPage: 1, //当前页
        totalItems: 10, //总记录数
        itemsPerPage: 10, //每页显示条数
        perPageOptions: [10, 20, 30, 40, 50], //分页选项
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    //刷新
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage)
    };


    //更新复选框
    $scope.selectIds = []; //复选框的集合
    $scope.updateSelection = function ($event, id) {
        //如果选中,就把该品牌的id假如到集合中
        if ($event.target.checked) {
            $scope.selectIds.push(id);
            //取消选中,则从集合中删除
        } else {
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);
        }
    };

    //json格式转字符串
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);
        var value = "";

        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ",";
            }
            value += json[i][key];
        }
        return value;
    }
});