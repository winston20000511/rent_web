<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">

<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>房屋管理系統</title>
<%-- log --%>
<link rel="Rent icon" href="./imags/lpoqq-yg0x4-001.ico">
<%-- datatables --%>
<link rel="stylesheet"
	href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css" />
<%-- Bootstrap CSS --%>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	data-integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	data-crossorigin="anonymous" />
<%-- Bootstrap Icons --%>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" />
<%-- CSS --%>
<link rel="stylesheet" type="text/css" href="CSS/backstage.css">
<link rel="stylesheet" type="text/css" href="CSS/house.css">
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
				<li><a class="bi bi-wallet" href="sidebar_complaints.jsp"
					data-content="sidebar_settings">客訴管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>

			</ul>
		</div>

		<div class="content">
			<div class="content">
				<div class="Content">
					<div class="Content">
				<h1>房屋管理</h1>
				<div class="refresh-container">
					<button class="refresh-btn" onclick="refreshTable()">重新刷新表格</button>
				</div>

				<table id="houseTable" class="display">
					<thead>
						<tr>
							<th>房屋編號</th>
							<th>房屋照片</th>
							<th>標題</th>
							<th>價格</th>
							<th>地址</th>
							<th>擁有者名稱</th>
							<th>電子郵件</th>
							<th>狀態</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		</div>


	</div>
	</div>

	<div id="settingsModal" class="modal">
		<div class="modal-content">
			<span class="close" onclick="closeSettings()">&times;</span>
			<h4>編輯管理者資料</h4>
			<form id="settingsForm" method="post" action="UpdateAdminDataServlet">
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
	<script src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
	<script src="./JS/sidebar_house.js"></script>
	<script type="text/javascript">
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
