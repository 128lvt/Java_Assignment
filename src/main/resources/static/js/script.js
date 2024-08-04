var app = angular.module("myApp", []);
var API_PREFIX = "http://localhost:8080/api/v1/";
var LIMIT_PAGE = 15;

app.controller("CategoryController", function ($scope, $http) {
  $scope.categories = [];

  $scope.createCategory = function () {
    $http
      .post(API_PREFIX + "categories", $scope.newCategory, {
        headers: {
          "Content-Type": "application/json",
        },
      })
      .then(function (response) {
        $scope.getCategories();
        $("#addProductModal").modal("hide");
      })
      .catch(function (error) {
        console.error("Error creating category:", error);
      });
  };

  $scope.getCategories = function () {
    $http
      .get(API_PREFIX + "categories")
      .then(function (response) {
        $scope.categories = response.data;
      })
      .catch(function (error) {
        console.error("Error fetching categories:", error);
      });
  };
  // Khởi tạo bằng cách lấy danh mục
  $scope.getCategories();

  $scope.openEditModal = function (categoryId) {
    $scope.selectedCategory = $scope.categories.find(
      (c) => c.categoryId === categoryId
    );
    console.log($scope.selectedCategory);
    $("#editProductModal").modal("show");
  };

  $scope.updateCategory = function () {
    if ($scope.selectedCategory) {
      // Tạo đối tượng category với thông tin từ newCategory
      var category = {
        name: $scope.selectedCategory.categoryName, // Sử dụng newCategory để gửi thông tin
      };
      console.log("Updating category with data:", category);

      $http
        .put(
          API_PREFIX + "categories/" + $scope.selectedCategory.categoryId,
          category,
          {
            headers: { "Content-Type": "application/json" },
          }
        )
        .then(function (response) {
          // Tải lại danh sách danh mục
          $scope.getCategories();

          // Đóng modal
          $("#editProductModal").modal("hide");
        })
        .catch(function (error) {
          console.error("Error updating category:", error);
        });
    }
  };

  $scope.openDeleteModal = function (categoryId) {
    $scope.categoryId = categoryId;
    $("#deleteProductModal").modal("show");
    console.log(categoryId);
  };

  $scope.deleteCategory = function () {
    if ($scope.categoryId) {
      $http
        .delete(API_PREFIX + "categories/" + $scope.categoryId)
        .then(function (response) {
          $scope.getCategories();
          $("#deleteProductModal").modal("hide");
        })
        .catch(function (error) {
          console.error("Error deleting category:", error);
        });
    }
  };
});

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
          },
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

      var formData = new FormData();

      $http({
        method: "PUT",
        url: API_PREFIX + "products/" + $scope.selectedProduct.id,
        data: productData,
        headers: { "Content-Type": "application/json" },
      })
        .then(function (response) {
          if ($scope.files && $scope.files.length > 0) {
            angular.forEach($scope.files, function (file) {
              formData.append("files", file);
            });
          } else {
            console.log("No files selected");
          }

          $http.post(
            API_PREFIX + "products/uploads/" + $scope.selectedProduct.id,
            formData,
            {
              headers: {
                "Content-Type": undefined,
              },
            }
          );
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

app.controller("ImageController", [
  "$scope",
  "$location",
  "$http",
  function ($scope, $location, $http) {
    // Hàm để lấy productId từ URL
    $scope.getProductId = function () {
      const params = new URLSearchParams($location.absUrl().split("?")[1]);
      $scope.productId = params.get("productId");
    };

    // Gọi hàm để lấy productId
    $scope.getProductId();

    $scope.file = null;

    $scope.handleFile = function (element) {
      $scope.$apply(() => {
        // Sử dụng $apply để cập nhật ngữ cảnh
        $scope.file = element.files[0]; // Lưu tệp đầu tiên vào biến
        console.log("Selected file:", $scope.file);
      });
    };

    const dropArea = document.querySelector(".drop-area");

    dropArea.addEventListener("drop", (event) => {
      event.preventDefault(); // Prevent default behavior of browser
      const file = event.dataTransfer.files[0]; // Get the file that was dropped
      $scope.file = file;
    });

    $scope.images = [];
    $scope.selectedImage = null;

    $scope.handleFiles = function (files) {
      $scope.files = files; // Lưu trữ các tệp đã chọn
      console.log("Selected files:", $scope.files);
    };

    $scope.uploadImage = function () {
      if ($scope.files) {
        var formData = new FormData();

        angular.forEach($scope.files, function (file) {
          formData.append("files", file);
        });

        $http
          .post(API_PREFIX + "products/uploads/" + $scope.productId, formData, {
            headers: {
              "Content-Type": undefined,
            },
            transformRequest: angular.identity,
          })
          .then(function (response) {
            alert("Images uploaded successfully");
            $scope.loadImages();
          })
          .catch(function (error) {
            console.error("Error uploading images:", error.data.message);
            if (error.data && error.data.message) {
              alert("Error uploading images: " + error.data.message);
            } else {
              alert("Error uploading images: An unknown error occurred.");
            }
          });
      } else {
        alert("Please select images");
      }
    };

    // Hàm tải hình ảnh theo productId
    $scope.loadImages = function () {
      if ($scope.productId) {
        // Kiểm tra xem productId có tồn tại
        $http
          .get(API_PREFIX + "products/images/?productId=" + $scope.productId)
          .then(function (response) {
            $scope.images = response.data;
          })
          .catch(function (error) {
            console.error("Lỗi khi tải hình ảnh:", error); // Xử lý lỗi
          });
      } else {
        console.log("productId không tồn tại.");
      }
    };

    $scope.openEditModal = function (id) {
      $scope.selectedImage = $scope.images.find(
        (image) => image.productImageId === id
      );
      // console.log($scope.images)
      // console.log($scope.selectedImage);
      $("#editModal").modal("show");
    };

    $scope.updateImage = function () {
      if (!$scope.file) {
        alert("Please select an image");
        return;
      }
      var formData = new FormData();
      formData.append("files", $scope.file);
      if ($scope.selectedImage) {
        $http
          .put(
            API_PREFIX +
              "products/images/" +
              $scope.selectedImage.productImageId,
            formData,
            {
              headers: {
                "Content-Type": undefined,
              },
            }
          )
          .then(function (response) {
            alert("Image updated successfully");
            $scope.loadImages();
            $("#editModal").modal("hide");
          })
          .catch(function (error) {
            console.error("Error updating image:", error);
          });
      }
    };

    $scope.openDeleteModal = function (id) {
      console.log(id);
      $scope.imageId = id;
      $("#deleteProductModal").modal("show");
    };

    $scope.deleteImage = function () {
      if ($scope.imageId) {
        $http
          .delete(API_PREFIX + "products/images/" + $scope.imageId)
          .then(function (response) {
            alert("Image deleted successfully");
            $scope.loadImages();
            $("#deleteProductModal").modal("hide");
          })
          .catch(function (error) {
            console.error("Error deleting image:", error.data.message);
          });
      }
    };

    // Gọi hàm tải hình ảnh
    $scope.loadImages();
  },
]);
