app.controller('itemController', function ($scope, $http) {

    $scope.addNum = function (x) {
        $scope.num = $scope.num + x;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
    };

    $scope.specificationItems = {};//��¼�û�ѡ��Ĺ��
    //�û�ѡ����
    $scope.selectSpecification = function (key, value) {
        $scope.specificationItems[key] = value;
        searchSku();//��ȡ sku
    };
    //�ж�ĳ���ѡ���Ƿ��û�ѡ��
    $scope.isSelected = function (key, value) {
        if ($scope.specificationItems[key] == value) {
            return true;
        } else {
            return false;
        }
    };
    //����Ĭ�� SKU
    $scope.loadSku = function () {
        $scope.sku = skuList[0];
        $scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
    };

    //ƥ����������
    matchObject = function (map1, map2) {
        for (var k in map1) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }
        for (var k in map2) {
            if (map2[k] != map1[k]) {
                return false;
            }
        }
        return true;
    };

    //��ѯ SKU
    searchSku = function () {
        for (var i = 0; i < skuList.length; i++) {
            if (matchObject(skuList[i].spec, $scope.specificationItems)) {
                $scope.sku = skuList[i];
                return;
            }
        }
        $scope.sku = {id: 0, title: '--------', price: 0};//���û��ƥ���
    };

    //�����Ʒ�����ﳵ
    //{'withCredentials':true} �����Ƿ���Բ���cookie
    $scope.addToCart = function () {
        $http.get('http://localhost:9107/cart/addGoodsToCartList.do?itemId='+$scope.sku.id+"&num="+$scope.num,{'withCredentials':true}).success(
            function (response) {
                if (response.success){
                    location.href = "http://localhost:9107/cart.html";
                }else {
                    alert(response.message);
                }
            }
        )
    }
});


	