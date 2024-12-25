<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">

<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>房屋管理</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<link
	href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.min.css"
	rel="stylesheet" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
	rel="stylesheet" />


<link rel="stylesheet" type="text/css" href="CSS/comp.css">
<link rel="stylesheet" type="text/css" href="CSS/backstage.css">
<style>
.btn-warning {
	background-color: #ff9800; /* Custom orange color */
	border-color: #ff9800; /* Match border color */
}

.btn-warning:hover {
	background-color: #e68a00; /* Darker shade on hover */
	border-color: #e68a00; /* Match border color */
}

body {
	background-color: #f8f9fa; /* Light gray background */
}

h1 {
	color: #343a40; /* Dark gray for the heading */
}

#filterInput, #addressFilterInput {
	margin-bottom: 10px;
	padding: 8px;
	width: 20%; /* Adjust width as necessary */
	box-sizing: border-box;
}

table {
	width: 100%;
	margin-top: 20px;
	background-color: #ffffff; /* White background for the table */
}

th {
	background-color: #6c757d; /* Gray header background */
	color: white; /* White text for headers */
}

td {
	background-color: #e9ecef; /* Light gray for table cells */
	color: #212529; /* Dark text for readability */
}

tr:hover td {
	background-color: #ced4da; /* Darker gray on hover */
}

.image-gallery img {
	width: 50px;
	height: 50px;
	object-fit: cover;
	margin-right: 5px;
}
</style>
</head>

<body>
	<%-- header --%>
	<nav class="nav navbar navbar-expand-lg navbar-dark">
		<div class="container-fluid">
			<h1 class="navbar-brand">
				<a class="navbar-brand" href="backstage-panel.jsp"><span>租屋網站後台管理</span></a>
			</h1>
			<div class="d-flex justify-content-end align-items-center">
				<div
					class="d-flex justify-content-end align-items-center  navbar-text text-white me-3 ">
					<span>歡迎,</span> <span class="admin-name" onclick="showSettings()">
						<span id="adminName"></span> <span class="bi bi-pin-angle-fill"></span>
					</span>
				</div>
				<a href="#" onclick="confirmLogout()" class="btn btn-outline-light">登出</a>
			</div>
		</div>
	</nav>
	<%-- Containers --%>
	<div class="containers">
		<%-- Sidebar --%>
		<div class="sidebar">
			<ul>
				<li><a class="bi bi-card-checklist" href="backstage-panel.jsp"
					data-content="sidebar_backendData">首 頁<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-person-fill" href="siderbar_user.jsp"
					data-content="sidebar_member">會員管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-houses-fill" href="sidebar_house.jsp"
					data-content="sidebar_house">房源管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-calendar-check-fill"
					href="sidebar_booking.jsp" data-content="sidebar_booking">預約管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-badge-ad" href="sidebar_adManage.jsp"
					data-content="sidebar_adOrder">廣告管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-wallet" href="sidebar_orderManage.jsp"
					data-content="sidebar_settings">訂單管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-wallet" href="ComplationFrom.jsp"
					data-content="sidebar_settings">客訴單管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>

			</ul>
		</div>
		

		<%-- Content --%>
		<div class="contentFrom">
			<div class="contentFrom">
				<div class="contentFrom" id="content">
					<h2>客服管理</h2>
					<div class="dataTables-controls">
						<div class="d-flex addBtn mb-3">
							<button type="button" class="btn btn-success" id="addButton">新增</button>
						</div>
					</div>

					<table id="complaintsTable" class="table table-bordered">
						<thead>
							<tr>
								<th>投訴單ID</th>
								<th>使用者 ID</th>
								<th>使用者名字</th>
								<th>類別</th>
								<th>主題</th>
								<th>內容</th>
								<th>客服人員註記</th>
								<th>狀態</th>
								<th>提交日期</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<!-- Data will be inserted here -->
						</tbody>
					</table>
				</div>
				<!-- Test form 1 -->
 
				<div id="complaintModal" class="popupp">
					<div>
						<h5>客服資料</h5>
						<button type="button" class="closeform">×</button>

					</div>

					<form id="complaintForm">
						<input type="hidden" id="complaintId" name="complaint_id" />
						<div class="mb-3">
							<label for="username" class="form-label">會員名稱：</label> <input
								type="text" id="username" name="username" class="form-control"
								required />
						</div>
						<div class="mb-3">
							<label for="user_id" class="form-label">會員ID：</label> <input
								type="text" id="user_id" name="user_id" class="form-control"
								required />
						</div>
						<div class="mb-3">
							<label>類別：</label>
							<div>
								<label><input type="radio" name="category" value="刊登問題" />
									刊登問題</label> <label><input type="radio" name="category"
									value="儲值問題" /> 儲值問題</label> <label><input type="radio"
									name="category" value="會員問題" /> 會員問題</label> <label><input
									type="radio" name="category" value="檢舉不法" /> 檢舉不法</label> <label><input
									type="radio" name="category" value="其它" /> 其它</label>
							</div>
						</div>
						<div class="mb-3">
							<label for="subject" class="form-label">主題：</label> <input
								type="text" id="subject" name="subject" class="form-control"
								required />
						</div>
						<div class="mb-3">
							<label for="contenttest" class="form-label">內容：</label>
							<!-- <input type="text" id="content" name="content" class="form-control" required> -->
							<textarea id="contenttent" name="content" rows="5"
								class="form-control" required></textarea>
						</div>
						<div id="editModeFields"></div>

						<div class="modal-footer">
							<button type="button" class="btn btn-primary" id="saveComplaint">確認</button>
						</div>
					</form>
				</div>
			</div>
		</div>
</div>
		<%-- 管理者編輯<彈窗> --%>
		<div id="settingsModal" class="modal">
			<div class="modal-content">
				<span class="closes" onclick="closeSettings()">&times;</span>
				<h4>編輯管理者資料</h4>
				<form id="settingsForm" method="post"
					action="UpdateAdminDataServlet">
					<div id="adminData">
						<!-- 這些欄位會在 JavaScript 中動態插入 -->
					</div>
					<button type="submit" class="btn btn-primary">保存更改</button>
				</form>
			</div>
		</div>

		<%-- 背景遮罩 --%>
		<div id="settingsModalBackdrop" class="modal-backdrop"></div>

		<%-- 資料更新 --%>
		<div id="updateTable" class="popup" style="display: none;">
			<table class="table">
				<thead>
					<tr>
						<th>狀態</th>
						<th>訊息</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td id="statusCell"></td>
						<td id="messageCell"></td>
					</tr>
				</tbody>
			</table>
			<button onclick="closeTable()" class="btn btn-primary">關閉</button>
		</div>

		<footer>
			<p>租屋網站後台管理 &copy; 2024</p>
		</footer>


		<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
		<script src="https://cdn.datatables.net/2.1.8/js/dataTables.min.js"></script>
		  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
		<script src="JS/complation.js"></script>
		<script src="JS/backstage.js"></script>
		<script>
$(function() {	
    	
    	var adminName = "${sessionScope.admin != null ? sessionScope.admin.adminName : '管理員'}";
		document.getElementById("adminName").innerText = adminName;
    	
        // 無法獲取session時登出
        var adminOnOff = "${sessionScope.admin}";
        if (adminOnOff == "" || adminOnOff == null) {
            outPanel();
        }
        
        // 資料更新確認
        var updateConfirm = "${requestScope.updateConfirm}";
        if (updateConfirm !== "" && updateConfirm !== null) {
            if (updateConfirm === "true") {
                document.getElementById("statusCell").innerText = "成功";
                document.getElementById("messageCell").innerText = "更新成功";
                document.getElementById("updateTable").classList.add('popup-success');	
                document.getElementById("settingsModalBackdrop").classList.add('show');	//添加遮罩
                
            } else {
                document.getElementById("statusCell").innerText = "失敗";
                document.getElementById("messageCell").innerText = "更新失敗";
                document.getElementById("updateTable").classList.add('popup-error');	
                document.getElementById("settingsModal").classList.add('show');
                document.getElementById("settingsModalBackdrop").classList.add('show');	//添加遮罩
            }
            document.getElementById("updateTable").style.display = "block"; // 顯示表格
        }
        
     
        
     // 讀取管理者(admin)資料 (****需改成SERVLET請求)
        document.getElementById("adminData").innerHTML = `
            <div class="mb-3">
                <label for="adminNameInput" class="form-label">姓名</label>
                <input type="text" id="adminNameInput" name="adminName" class="form-control" value="${sessionScope.admin.adminName}" autocomplete="off" required/>
            </div>						
            <div class="mb-3">
                <label for="adminEmailInput" class="form-label">電子郵件</label>
                <input type="email" id="adminEmailInput" name="adminEmail" class="form-control" value="${sessionScope.admin.adminEmail}" autocomplete="off" required/>
            </div>
            <div class="mb-3">
                <label for="adminPasswordInput" class="form-label">密碼</label>
                <input type="text" id="adminPasswordInput" name="adminPassword" class="form-control" value="${sessionScope.admin.adminPassword}" autocomplete="off" required/>
            </div>
            <div class="mb-3">
                <label for="adminPhoneInput" class="form-label">電話</label>
                <input type="text" id="adminPhoneInput" name="adminPhone" class="form-control" value="${sessionScope.admin.adminPhone}" autocomplete="off" required/>
            </div>
        `;
    });	

</script>
</body>
</html>
