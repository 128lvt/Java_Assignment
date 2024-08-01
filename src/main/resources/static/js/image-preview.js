$(document).ready(function() {
    $('#imageInput').on('change', function() {
      const $input = $(this);
      if ($input[0].files && $input[0].files[0]) { // Kiểm tra tệp có tồn tại
        const fileReader = new FileReader();
        fileReader.onload = function (data) {
          $('.image-preview').attr('src', data.target.result);
        };
        fileReader.readAsDataURL($input[0].files[0]); // Sử dụng [0] để lấy tệp đầu tiên
        $('.image-button').hide(); // Ẩn nút chọn hình ảnh
        $('.image-preview').show(); // Hiển thị hình ảnh
        $('.change-image').show(); // Hiển thị nút thay đổi hình ảnh
      }
    });
  
    $('.change-image').on('click', function() {
      const $control = $(this);			
      $('#imageInput').val(''); // Đặt lại giá trị của input file
      const $preview = $('.image-preview');
      $preview.attr('src', ''); // Đặt lại thuộc tính src
      $preview.hide(); // Ẩn hình ảnh
      $control.hide(); // Ẩn nút thay đổi hình ảnh
      $('.image-button').show(); // Hiển thị lại nút chọn hình ảnh
    });
  });
  