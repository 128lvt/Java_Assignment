var app = angular.module("myApp", []);
var API_PREFIX = "http://localhost:8080/api/v1/";
var LIMIT_PAGE = 15;

app.controller("MainController", function ($scope, $http) {
  $scope.products = [];
  $scope.currentPage = 0;
  $scope.totalEntries = 0;
  $scope.totalPages = 0;

  $scope.getItems = function (page) {
    $scope.limit = LIMIT_PAGE;
    $http
      .get(API_PREFIX + "products?page=" + page + "&limit=" + $scope.limit)
      .then(function (response) {
        $scope.products = response.data.products;
        $scope.totalEntries = response.data.totalItems;
        $scope.totalPages = response.data.totalPages;
        $scope.currentPage = page;
      });
  };

  $scope.editProduct = function (productId) {
    $scope.selectedProduct = $scope.products.find((p) => p.id === productId);
    $("#editProductModal").modal("show");
  };

  $scope.updateProduct = function () {
    if ($scope.selectedProduct) {
      $http
        .put(
          API_PREFIX + "products/" + $scope.selectedProduct.id,
          $scope.selectedProduct
        )
        .then(function (response) {
          $scope.getItems($scope.currentPage);
          $("#editProductModal").modal("hide");
        })
        .catch(function (error) {
          console.error("Error updating product:", error);
        });
    }
  };

  $scope.updateProduct = function () {
    if ($scope.selectedProduct) {
      var productData = {
        name: $scope.selectedProduct.name,
        price: $scope.selectedProduct.price,
        description: $scope.selectedProduct.description,
        category_id: $scope.selectedProduct.category.categoryId,
      };

      $http({
        method: "PUT",
        url: API_PREFIX + "products/" + $scope.selectedProduct.id,
        data: productData,
        headers: { "Content-Type": "application/json" },
      })
        .then(function (response) {
          $scope.getItems(0); // Refresh the product list
          $("#editProductModal").modal("hide");
        })
        .catch(function (error) {
          console.error("Error updating product:", error);
        });
    }
  };

  $scope.getItems(0);
});
