<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ include file="/common/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Word List</title>

<!-- Custom fonts for this template-->
<link
	href="<c:url value='/template/web/vendor/fontawesome-free/css/all.min.css'/>"
	rel="stylesheet" type="text/css">

<!-- Page level plugin CSS-->
<link
	href="<c:url value='https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i'/>"
	rel="stylesheet" />

<!-- Custom styles for this template-->
<link href="<c:url value='/template/web/css/sb-admin-2.min.css'/>"
	rel="stylesheet" type="text/css">
<link
	href="<c:url value='/template/web/vendor/datatables/dataTables.bootstrap4.min.css'/>"
	rel="stylesheet" type="text/css">
</head>
<body id="page-top">
	<!-- Navigation -->
	<div id="wrapper">

		<!-- Sidebar -->
		<%@ include file="/common/web/header.jsp"%>
		<!-- End of Sidebar -->

		<!-- Content Wrapper -->



		<div id="content-wrapper" class="d-flex flex-column">

			<!-- Main Content -->
			<div id="content">

				<!-- Topbar -->
				<nav
					class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

					<!-- Sidebar Toggle (Topbar) -->
					<button id="sidebarToggleTop"
						class="btn btn-link d-md-none rounded-circle mr-3">
						<i class="fa fa-bars"></i>
					</button>

					<!-- Topbar Search -->
					<form
						class="d-none d-sm-inline-block form-inline mr-auto ml-md-3 my-2 my-md-0 mw-100 navbar-search">
						<div class="input-group">
							<input type="text" class="form-control bg-light border-0 small"
								placeholder="Search for..." aria-label="Search"
								aria-describedby="basic-addon2">
							<div class="input-group-append">
								<button class="btn btn-primary" type="button">
									<i class="fas fa-search fa-sm"></i>
								</button>
							</div>
						</div>
					</form>

					<!-- Topbar Navbar -->

				</nav>
				<div class="container-fluid">

					<!-- Page Heading -->
					

					<h3 class="h4 mb-2 text-gray-800"><b>Tùy chọn</b></h3>
					<p class="mb-4" id="options">
					
					</p>
					<!-- DataTales Example -->
					<div class="card shadow mb-4">
						<div class="card-header py-3">
						<button id="myBtn1" onClick="">Tần suất</button>
						<button id="myBtn" onClick="">Thống kê</button>
						</div>
						<div class="card-body" id="frequency">
							<div class="table-responsive">
								<table class="table table-bordered" id="dataTable"
									style="width: 100%; cellspacing: 0;">
									
								</table>
								
								
							</div>
						</div>
						
						<div class="card-body" id="statistics">
							<div class="table-responsive">
								<table class="table table-bordered" id="dataTable1"
									style="width: 100%; cellspacing: 0;">
									
								</table>
								
								
							</div>
						</div>
						
					</div>
					
					

				</div>

			</div>
			<!-- End of Main Content -->

			<!-- Footer -->
			<footer class="sticky-footer bg-white">
				<div class="container my-auto">
					<div class="copyright text-center my-auto">
						<span>Copyright &copy; Your Website 2020</span>
					</div>
				</div>
			</footer>
			<!-- End of Footer -->

		</div>
		<!-- End of Content Wrapper -->

	</div>
	<!-- End of Page Wrapper -->
	<script>
      var showTable = document.getElementById("dataTable");
      var option = ${option};
      var contentOption = 'Có phân biệt chữ hoa, thường: '+option[0]+' <br/>Có tính dấu câu: '+option[1]+' <br/>Có tính số: '+option[2]+'<br/>Có tính từ loại: '+option[3];
      document.getElementById("options").innerHTML = contentOption;
      var wordList = ${word_list};
      var statistic = ${statistics};
      var count=0;
      var content = wordList.map(function(word){
    	 count++;
      	return '<tr><td>' + count +'</td><td>'+ word.word + '</td><td>' + word.pos + '</td><td>' + word.frequency + '</td><td>' +word.percent + '%</td><td>' + word.file+'</td><td>' +word.percent_file+'%</td></tr>';
      	
      });
      var thead = '<thead><tr><th>STT</th><th>Từ</th><th>Từ loại</th><th>Tần suất</th><th>Phần trăm tần suất</th><th>File</th><th>Phần trăm file</th></tr></thead><tbody>';
      showTable.innerHTML = thead+content.join('')+'</tbody>';
      document.getElementById("myBtn1").addEventListener("click",function(){
    	  document.getElementById("statistics").hidden=true;
    	  document.getElementById("frequency").hidden=false;
    	  showTable.innerHTML = thead+content.join('')+'</tbody>';
      });
      var file_index = ${fileIndex};
      var stt=-1;
      var contentStatistics = statistic.map(function(word){
    	  stt++;
    	 return '<tr><td>' + stt + '</td><td>' + file_index[stt] + '</td><td>' + word.file_size + '</td><td>'+ word.tokens_in_text + '</td><td>' + word.words_in_text + '</td><td>' + word.words_used_for_word_list + '</td><td>' + word.types + '</td><td>' + word.TWR + '%</td><td>' + word.sentences +'</td><td>' + word.mean_token_length + '</td><td>' + word.std_token_length + '</td><td>' + word.mean_word_length + '</td><td>' + word.std_word_length + '</td><td>'+ word.mean_sentence_length + '</td><td>'+ word.std_sentence_length + '</td><td>' + word.numbers_removed + '</td><td>' + word.punctuations_removed + '</td><td>' + word._1gram_word + '</td><td>'+ word._2gram_word + '</td><td>' + word._3gram_word + '</td><td>' + word._4gram_word + '</td><td>'+ word._5gram_word + '</td><td>'+ word.ngram_word + '</td></tr>'; 
      });
      
      var tHead = '<thead><tr><th>STT</th><th>File</th><th>Kích cỡ file (ký tự)</th><th>Tổng số chữ</th><th>Tổng số từ</th><th>Số từ được dùng</th><th>Số từ phân biệt</th><th>Số từ phân biệt/Số từ được dùng </th><th>Số câu</th><th>Độ dài trung bình của chữ</th><th>Độ lệch chuẩn</th><th>Độ dài trung bình của từ</th><th>Độ lệch chuẩn</th><th>Độ dài trung bình của câu</th><th>Độ lệch chuẩn</th><th>Số số bị xóa</th><th>Số dấu câu bị xóa</th><th>Từ có 1 chữ</th><th>Từ có 2 chữ</th><th>Từ có 3 chữ</th><th>Từ có 4 chữ</th><th>Từ có 5 chữ</th><th>Từ có nhiều hơn 5 chữ</th></tr></thead><tbody>';
      //showTable.innerHTML = tHead+contentStatistics.join('')+'</tbody>';
      document.getElementById("myBtn").addEventListener("click", function(){
    	  document.getElementById("frequency").hidden=true;
    	  document.getElementById("dataTable1").innerHTML = tHead+contentStatistics.join('')+'</tbody>';
      });   
    </script>

	<!-- Bootstrap core JavaScript -->
	<script
		src="<c:url value='/template/web/vendor/jquery/jquery.min.js'/>"></script>
	<script
		src="<c:url value='/template/web/vendor/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
	<script
		src="<c:url value='/template/web/vendor/jquery-easing/jquery.easing.min.js'/>"></script>
	<script src="<c:url value='/template/web/js/sb-admin-2.min.js'/>"></script>
	<script
		src="<c:url value='/template/web/vendor/datatables/jquery.dataTables.min.js'/>"></script>
	<script
		src="<c:url value='/template/web/vendor/datatables/dataTables.bootstrap4.min.js'/>"></script>
	<script src="<c:url value='/template/web/js/demo/datatables-demo.js'/>"></script>


</body>
</html>