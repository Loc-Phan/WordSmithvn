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
					<h1 class="h3 mb-2 text-gray-800">Concord</h1>
					<p class="mb-4">
						Thống kê từ đồng hiện
					</p>


					<!-- DataTales Example -->
					<div class="card shadow mb-4">
						<div class="card-header py-3">
						<button id="myBtn" onClick="">Tần suất</button>
						<button id="myBtn1" onClick="">Từ đồng hiện</button>
						<button id="myBtn2" onClick="">Thống kê</button></div>
						
						<div class="card-body" id="concordance">
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
						<div class="card-body" id="collocates">
							<div class="table-responsive">
								<table class="table table-bordered" id="dataTable2"
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
      var concordance = ${concordance};
      var statistics = ${statistics};
      var collocates = ${collocates};
      var tu = "${tu}";
      var stt=0;
      var files = ${files};
     
      var content = concordance.map(function(word){
    	  stt++;
      	return '<tr><td>'+ stt + '</td><td>' + word.context[0] + '<b style="color:blue;"> ' + word.context[1] +' </b>' + word.context[2] + '</td><td>' +word.pos + '</td><td>' + word.word_index+'</td><td>' +word.percent_word_index+'%</td><td>'+word.sentence_index+'</td><td>'+word.percent_sentence_index+'%</td><td>'+files[stt-1]+'</td></tr>';
      });
      var thead = '<thead><tr><th>STT</th><th>Ngữ cảnh</th><th>Từ loại</th><th>Vị trí từ</th><th>Phần trăm vị trí từ</th><th>Vị trí câu</th><th>Phần trăm vị trí câu</th><th>File</th></tr></thead>';
      showTable.innerHTML = thead+content.join('')+'</tbody>';
      document.getElementById("myBtn").addEventListener("click",function(){
    	  document.getElementById("concordance").hidden=false;
    	  document.getElementById("collocates").hidden=true;
    	  document.getElementById("statistics").hidden=true;
    	  showTable.innerHTML = thead+content.join('')+'</tbody>';
      }); 
      
      var stt1=0;
      var contentStatistics = statistics.map(function(word){
    	  stt1++;
      	return '<tr><td>'+ stt1 + '</td><td>' + files[stt1-1] + '</td><td>' +word.words + '</td><td>' + word.hits+'</td><td>' +word.dispersion+'</td></tr>';
      });
      var tHead = '<thead><tr><th>STT</th><th>File</th><th>Tổng số từ</th><th>Tần suất</th><th>Độ phân tán</th></tr></thead>';
      document.getElementById("myBtn2").addEventListener("click",function(){
    	  document.getElementById("concordance").hidden=true;
    	  document.getElementById("collocates").hidden=true;
    	  document.getElementById("statistics").hidden=false;
      	document.getElementById("dataTable1").innerHTML = tHead+contentStatistics.join('')+'</tbody>';
      });
      
      
 		     var stt2=0;
      var contentCollocates = collocates.map(function(word){
    	  stt2++;
    	  var key = [];
    	  for(k in word) {
    		  key.push(k);
    	  }
    	  var temp = key[0];
    	  var arr = word[temp];
    	  
    	  
    	  var totalL = 0;
    	  var totalR = 0;
    	  for(var i=0;i<5;i++) {
    		  totalL+=arr[i];
    	  }
    	  
    
    	  for(var i=5;i<10;i++) {
    		  totalR+=arr[i];
    	  }
    	  var total = totalL + totalR;
    	  for(var i=0;i<10;i++) {
    		  if(arr[i]==0) {
    			  arr[i]="";
    		  }
    	  }
      	var res =  '<tr><td>'+ stt2 + '</td><td>' + key[0] + '</td><td>' +total + '</td><td>' + totalL+'</td><td>' +totalR+'</td><td>' +arr[0]+'</td><td>' +arr[1]+'</td><td>' +arr[2]+'</td><td>' +arr[3]+'</td><td>' +arr[4]+ '</td><td>'+""+'</td><td>' +arr[5]+'</td><td>' +arr[6]+'</td><td>' +arr[7]+'</td><td>' +arr[8]+'</td><td>' +arr[9]+'</td></tr>';
      	//console.log(res);
      	return res;
      });
      
      var tHead_ = '<thead><tr><th>STT</th><th>Từ</th><th>Tổng cộng</th><th>Tổng trái</th><th>Tổng phải</th><th>L5</th><th>L4</th><th>L3</th><th>L2</th><th>L1</th><th>Centre</th><th>R1</th><th>R2</th><th>R3</th><th>R4</th><th>R5</th></tr></thead>';
      var centre = '<tr><td>'+ 0 + '</td><td>' + tu.toUpperCase() + '</td><td>' + concordance.length + '</td><td>' +0 +'</td><td>' +0+'</td><td>' +""+'</td><td>' +""+'</td><td>' +""+'</td><td>' +""+'</td><td>' +""+'</td><td style="color:red">'+concordance.length+'</td><td>' + ""+'</td><td>' +""+'</td><td>' +""+'</td><td>' +""+'</td><td>' +""+'</td></tr>';
      //console.log(centre);
      
      document.getElementById("myBtn1").addEventListener("click",function(){
    	  document.getElementById("concordance").hidden=true;
    	  document.getElementById("statistics").hidden=true;
    	  document.getElementById("collocates").hidden=false;
    	  
      	
    	  document.getElementById("dataTable2").innerHTML = tHead_+centre+('')+contentCollocates.join('')+'</tbody>';
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