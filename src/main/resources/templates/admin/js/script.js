var app = angular.module("myApp", []);
var API_PREFIX = "http://localhost:8080/api/v1/";
var LIMIT_PAGE = 15;

app.controller("MainController", function ($scope, $http) {
  $scope.products = [];

  $scope.getItems = function () {
    $scope.limit = LIMIT_PAGE;
    $http
      .get(API_PREFIX + "products?page=0" + "&limit=" + $scope.limit)
      .then(function (response) {
        $scope.products = response.data.products;
        $scope.totalEntries = response.data.totalItems;
        $scope.totalPages = response.data.totalPages;
        console.log($scope.products);
      });
  };

  $scope.getItems();
});
