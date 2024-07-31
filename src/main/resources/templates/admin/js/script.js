var app = angular.module("myApp", []);
var API_PREFIX = "http://localhost:8080/api/v1/";
var LIMIT_PAGE = 15;

app.controller("MainController", function ($scope, $http) {
  $scope.products = [];
  $scope.currentPage = 0;
  $scope.totalProducts = 0;
  $scope.totalPages = 0;

  $scope.categories = [];

  $scope.newProduct = {};

  $scope.files = {};

  $scope.handleFiles = function (files) {
    $scope.files = files; // Lưu trữ các tệp đã chọn
    console.log("Selected files:", $scope.files);
  };

  $scope.createProduct = function () {
    console.log($scope.newProduct);
    // Gửi yêu cầu POST với dữ liệu sản phẩm dưới dạng JSON
    console.log($scope.files);
    $http
      .post(API_PREFIX + "products", $scope.newProduct, {
        headers: {
          "Content-Type": "application/json", // Đảm bảo rằng dữ liệu được gửi dưới dạng JSON
        },
      })
      .then(function (response) {
        var productId = response.data.id;

        var formData = new FormData();

        if ($scope.files && $scope.files.length > 0) {
          angular.forEach($scope.files, function (file) {
            formData.append("files", file);
          });
        } else {
          console.log("No files selected");
        }

        $http.post(API_PREFIX + "products/uploads/" + productId, formData, {
          headers: {
            "Content-Type": undefined,
          }
        });

        // Hiển thị thông điệp thành công
        alert("Product added successfully");
        // Đóng modal
        $("#addProductModal").modal("hide");
        // Cập nhật danh sách sản phẩm
        $scope.getProducts($scope.currentPage);
        // Đặt lại đối tượng sản phẩm mới để làm sạch form
        $scope.newProduct = {};
        $scope.files = {};
      })
      .catch(function (error) {
        console.error("Error adding product:", error);
        alert(
          "Error adding product: " + (error.data.message || "Please try again.")
        );
      });
  };

  $scope.getProducts = function (page) {
    $scope.limit = LIMIT_PAGE;
    $http
      .get(API_PREFIX + "products?page=" + page + "&limit=" + $scope.limit)
      .then(function (response) {
        $scope.products = response.data.products;
        $scope.totalProducts = response.data.totalItems;
        $scope.totalPages = response.data.totalPages;
        $scope.currentPage = page;
      });
  };
  $scope.getProducts(0);

  $scope.getCategories = function () {
    $http
      .get(API_PREFIX + "categories")
      .then(function (response) {
        $scope.categories = response.data;

        // Tim category theo id -> gan vao selectedProduct.category

        if ($scope.selectedProduct && $scope.selectedProduct.category) {
          const selectedCategory = $scope.categories.find(
            (category) => category.id === $scope.selectedProduct.category.id
          );

          if (selectedCategory) {
            $scope.selectedProduct.category = selectedCategory;
          }
        }
      })
      .catch(function (error) {
        console.error("Error fetching categories:", error);
      });
  };
  // Khởi tạo bằng cách lấy danh mục
  $scope.getCategories();

  $scope.openEditProductModal = function (productId) {
    $scope.selectedProduct = $scope.products.find((p) => p.id === productId);
    $("#editProductModal").modal("show");
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
          alert("Product updated successfully");
          $scope.getProducts(0); // Refresh the product list
          $("#editProductModal").modal("hide");
        })
        .catch(function (error) {
          console.error("Error updating product:", error);
        });
    }
  };

  $scope.openDeleteModal = function (productId) {
    $scope.productId = productId;
    console.log($scope.productId);
    $("#deleteProductModal").modal("show");
  };

  $scope.deleteProduct = function () {
    if ($scope.productId) {
      $http
        .delete(API_PREFIX + "products/" + $scope.productId)
        .then(function (response) {
          alert("Product deleted succesfully");
          // Đóng modal
          $("#deleteProductModal").modal("hide");
          // Cập nhật danh sách sản phẩm (nếu cần)
          $scope.getProducts($scope.currentPage);
        })
        .catch(function (error) {
          console.error("Error deleting product:", error);
        });
    }
  };
});
