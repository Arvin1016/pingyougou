app.controller('brandController', function ($scope, $http,$controller, brandService) {

    $controller('baseController', {$scope: $scope});

    $scope.findAll = function () {
        brandService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    };


    //查询实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        )
    };

    //增加和修改
    $scope.save = function () {
        //定义默认变量为添加
        var object = null;
        //如果有id就变更为修改
        if ($scope.entity.id != null) {
            object = brandService.update($scope.entity);
        } else {
            object = brandService.add($scope.entity);
        }
        //调用方法
        object.success(
            function (response) {
                //如果成功刷新页面
                if (response.success) {
                    $scope.reloadList();
                    //失败则给出提示
                } else {
                    alert(response.message);
                }
            }
        )
    };


    //删除选中
    $scope.delete = function () {
        if (confirm('你确定要删除吗?')) {
            //调用删除方法
            brandService.delete($scope.selectIds).success(
                function (response) {
                    //如果成功,刷新列表
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                    } else {
                        $scope.message;
                    }
                }
            )
        }
    };

    //条件查询
    //定义一个搜索对象
    $scope.searchEntity = {};
    //定义一个查询函数
    $scope.search = function (page, size) {
        //调用条件查询方法
        brandService.search(page, size, $scope.searchEntity).success(
            function (response) {
                $scope.paginationConf.totalItems = response.total;
                $scope.list = response.rows;
            }
        );
    }


});