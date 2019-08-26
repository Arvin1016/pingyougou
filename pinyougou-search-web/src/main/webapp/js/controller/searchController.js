app.controller("searchController", function ($scope, $location, searchService) {

    // 初始化查询条件集合searchMap
    $scope.searchMap = {
        "keywords": "",
        "category": "",
        "brand": "",
        "spec": {},
        "price": "",
        "pageNo": 1,
        "pageSize": 40,
        "sort": "",
        "sortField": ""
    };

    // 结果集
    $scope.resultMap = {rows: '', categoryList: '', brandList: '', specList: {}, totalPages: '', total: ''};

    // 搜索
    var search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo); // 字符串转为Integer
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                buildPageLabel(); // 构建分页栏
            }
        );
    };

    // 构建分页栏
    var buildPageLabel = function () {
        var firstPage = 1; // 首页
        var lastPage = $scope.resultMap.totalPages; // 尾页
        $scope.firstDot = false; // 左边省略号是否显示
        $scope.lastDot = false; // 右边省略号是否显示
        if (lastPage > 5) {
            if ($scope.searchMap.pageNo <= 3) {
                lastPage = 5;
                $scope.lastDot = true;
            } else if ($scope.searchMap.pageNo >= lastPage - 2) {
                firstPage = lastPage - 4;
                $scope.firstDot = true;
            } else {
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
                $scope.firstDot = true;
                $scope.lastDot = true;
            }
        }
        // 构建页码
        $scope.pageLabel = []; // 初始化页码集合
        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLabel.push(i);
        }
    };

    // 分页查询
    $scope.queryByPage = function (pageNo) {
        /*if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }*/
        $scope.searchMap.pageNo = pageNo;
        search();
    };

    // 搜索按钮查询，需初始化过滤条件
    $scope.searchButton = function () {
        if ($scope.searchMap.keywords === '') {
            return;
        }
        $scope.searchMap.category = "";
        $scope.searchMap.brand = "";
        $scope.searchMap.spec = {};
        $scope.searchMap.price = "";
        $scope.searchMap.pageNo = 1;
        search();
    };

    // 动态添加筛选条件
    $scope.addSearchItem = function (key, value) {
        if (key === "category" || key === "brand" || key === "price") {
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        $scope.searchMap.pageNo = 1; // 重置当前页
        search();
    };

    // 动态移除筛选条件
    $scope.removeSearchItem = function (key) {
        if (key === "category" || key === "brand" || key === "price") {
            $scope.searchMap[key] = "";
        } else {
            delete $scope.searchMap.spec[key];
        }
        $scope.searchMap.pageNo = 1; // 重置当前页
        search();
    };

    // 判断当前页是否是第一页
    $scope.isTopPage = function () {
        return $scope.searchMap.pageNo === 1;
    };

    // 判断当前页是否是最后一页
    $scope.isEndPage = function () {
        return $scope.searchMap.pageNo === $scope.resultMap.totalPages;
    };

    // 排序
    $scope.sortSearch = function (sort, sortField) {
        $scope.searchMap.sort = sort;
        $scope.searchMap.sortField = sortField;
        search();
    };

    //判断关键字是不是品牌
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {
                return true;
            }
        }
        return false;
    };

    //加载查询字符串
    $scope.loadKeywords = function () {
        $scope.searchMap.keywords = $location.search()['keywords'];
        search();
    }


});