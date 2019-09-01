//购物车服务层
app.service('cartService', function ($http) {
    //购物车列表
    this.findCartList = function () {
        return $http.get('cart/findCartList.do')
    };

    this.addGoodsToCartList = function (itemId, num) {
        return $http.get('cart/addGoodsToCartList.do?itemId=' + itemId + "&num=" + num);
    };

    this.sum = function (cartList) {
        var totalValue = {totalNuM: 0, totalMoney: 0.00};
        for (var i = 0; i < cartList.length; i++) {
            var cart = cartList[i];
            for (var j = 0; j < cart.orderItemList.length; j++) {
                var orderItem = cart.orderItemList[j];
                totalValue.totalNuM += orderItem.num;
                totalValue.totalMoney += orderItem.totalFee;
            }
        }
        return totalValue;
    };

    //获取地址列表
    this.findAddressList=function(){
        return $http.get('cart/findListByLoginUser.do');
    };

    //保存订单
    this.submitOrder = function (order) {
        return $http.post('order/add.do',order);
    }

});