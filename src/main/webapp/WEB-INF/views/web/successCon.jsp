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
					<h1 class="h3 mb-2 text-gray-800">WordList - Frequency</h1>
					<p class="mb-4">
						Thống kê tất cả các từ phân biệt có trong đoạn text, số lần xuất
						hiện, phần trăm số lần xuất hiện </a>.
					</p>


					<!-- DataTales Example -->
					<div class="card shadow mb-4">
						<div class="card-header py-3"></div>
						<div class="card-body">
							<div class="table-responsive">
								<table class="table table-bordered" id="dataTable"
									style="width: 100%; cellspacing: 0;">
									<thead>
										<tr>
											<th>STT</th>
											<th>Word</th>
											<th>Pos</th>
											<th>Frequency</th>
											<th>Percent</th>
											<th>File</th>
										</tr>
									</thead>
									<tfoot>
										<tr>
											<th>STT</th>
											<th>Word</th>
											<th>Pos</th>
											<th>Frequency</th>
											<th>Percent</th>
											<th>File</th>
										</tr>
									</tfoot>
									<tbody>
									
									</tbody>
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
      var wordList = ${word_list};
      var content = wordList.map(function(word){
      	return '<tr><td>' + word.file + '</td><td>' + word.pos + '</td><td>' + word.percent_file + '%</td><td>' +word.word + '</td><td>' + word.percent+'%</td><td>' +word.frequency+'</td><tr>';
      });
      var thead = '<thead><tr><th>File</th><th>Pos</th><th>Percent_file</th><th>Word</th><th>Percent</th><th>Frequency</th></tr></thead><tfoot><tr><th>File</th><th>Pos</th><th>Percent_file</th><th>Word</th><th>Percent</th><th>Frequency</th></tr></tfoot><tbody>';
      showTable.innerHTML = thead+content.join('')+'</tbody>';
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