angular.module('app', [])
.controller('CartController', ['$scope', '$http', function($scope, $http) {
    var vm = this;
    
    vm.product = {}; // Biến lưu trữ thông tin sản phẩm
    vm.sizes = []; // Mảng các kích thước
    vm.quantity = 1; // Số lượng mặc định
    vm.selectedSize = null; // Kích thước được chọn

    vm.loadProductDetails = function() {
        // Giả sử API lấy thông tin sản phẩm và kích thước
        $http.get('/api/product/details').then(function(response) {
            vm.product = response.data.product;
            vm.sizes = response.data.sizes;
        });
    };

    vm.addToCart = function() {
        if (!vm.selectedSize) {
            alert("Vui lòng chọn kích thước");
            return;
        }
        var data = {
            productId: vm.product.id,
            sizeId: vm.selectedSize,
            quantity: vm.quantity
        };
        $http.post('/api/cart/add', data).then(function(response) {
            alert("Thêm vào giỏ hàng thành công!");
        }).catch(function(error) {
            alert("Có lỗi xảy ra!");
        });
    };

    vm.selectSize = function(sizeId) {
        vm.selectedSize = sizeId;
    };
}]);
