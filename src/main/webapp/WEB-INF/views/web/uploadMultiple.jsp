<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" import="java.io.*, java.net.*"%> <%@ include
file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Insert title here</title>
    <!-- Custom fonts for this template-->
    <link
      href="<c:url value='/template/web/vendor/fontawesome-free/css/all.min.css'/>"
      rel="stylesheet"
      type="text/css"
    />

    <!-- Page level plugin CSS-->
    <link
      href="<c:url value='https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i'/>"
      rel="stylesheet"
    />

    <!-- Custom styles for this template-->
    <link
      href="<c:url value='/template/web/css/sb-admin-2.min.css'/>"
      rel="stylesheet"
      type="text/css"
    />
    <link
      href="<c:url value='/template/web/vendor/datatables/dataTables.bootstrap4.min.css'/>"
      rel="stylesheet"
      type="text/css"
    />
       <link
      href="<c:url value='https://cdn.datatables.net/1.10.21/css/dataTables.bootstrap4.min.css'/>"
      rel="stylesheet"
      type="text/css"
    />
       <link
      href="<c:url value='https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.css'/>"
      rel="stylesheet"
      type="text/css"
    />
  </head>
  <body>
    <h1>Word List:</h1>
    <!-- 
<div class="container-fluid">
	<div class="card shadow mb-4">
		<div class="card-body">
	    	<div class="table-responsive">
				<table class="table table-bordered" id="word_list" style="width:100%;cellspacing:0;">
					
				</table>
			</div>
		</div>
	</div>
</div>
-->
    <div class="card shadow mb-4">
      <div class="card-body">
        <div class="table-responsive">
          <table
            class="table table-bordered"
            id="dataTable"
            style="width: 100%; cellspacing: 0;"
          >
            
          </table>
        </div>
      </div>
    </div>
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
    <script src="<c:url value='/template/web/vendor/jquery/jquery.min.js'/>"></script>
    <script src="<c:url value='/template/web/vendor/bootstrap/js/bootstrap.bundle.min.js'/>"></script>
    <script src="<c:url value='/template/web/vendor/jquery-easing/jquery.easing.min.js'/>"></script>
    <script src="<c:url value='/template/web/js/sb-admin-2.min.js'/>"></script>
    <script src="<c:url value='/template/web/vendor/datatables/jquery.dataTables.min.js'/>"></script>
    <script src="<c:url value='/template/web/vendor/datatables/dataTables.bootstrap4.min.js'/>"></script>
    <script src="<c:url value='/template/web/js/demo/datatables-demo.js'/>"></script>
    <script type="text/javascript">
    $(document).ready(function() {
        $('#dataTable').DataTable();
    } );
    </script>
  </body>
</html>
