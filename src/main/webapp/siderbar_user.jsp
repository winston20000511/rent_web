<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">

<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>訂單管理系統</title>
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
<link rel="stylesheet" type="text/css" href="CSS/orderManage.css">

</head>
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
	


	<%-- Content --%>
	<div class="content">
		<div class="content">
			<div>
				<!-- <form action="SearchUserServlet.do" method="POST"
					onsubmit="searchUser(event)">
					<div class="search-box">
						<label class="select-box-label"> <select
							name="searchCondition" id="search-condition">
								<option value="userId">會員ID</option>
								<option value="userName">會員姓名</option>
								<option value="userEmail">會員Email</option>
						</select>
						</label> <label class="select-box-label"> <input type="text"
							name="searchInput" id="search-input-box" placeholder="請輸入搜尋內容" />
						</label> <label class="select-box-label"> <select
							name="userCondition" id="user-condition">
								<option value="" disabled selected>會員狀態</option>
								<option value="all">顯示全部</option>
								<option value="true">正常帳戶</option>
								<option value="false">遭檢舉帳戶</option>
						</select>
						</label>

						<button id="search-btn" type="submit">搜尋</button>
					</div>
				</form> -->
				<div class="Content">

					<h1>會員管理</h1>
					<table id="usertable" class="display">
						<thead>
							<tr>
								<th>會員ID</th>
								<th>會員姓名</th>
								<th>會員Email</th>
								<th>會員密碼</th>
								<th>會員手機</th>
								<th>會員註冊日期</th>
								<th>會員性別</th>
								<th>會員狀態</th>
								<th>操作功能</th>
							</tr>
						</thead>
						<tbody>
							<!-- 搜尋結果會動態填入這裡 -->
						</tbody>
					</table>
					<div class="ad-detail-box" style="display: none;">
						<!-- 初始隱藏 -->
						<div class="title-box">會員詳細資料</div>
						<button type="button" class="leave-btn" onclick="hideDetails()">X（離開）</button>
						<table id="user-details-table">
							<tbody>
								<tr>
									<th>會員姓名</th>
									<td class="user-name"><input type="text"
										class="user-name-input" disabled /></td>
								</tr>
								<tr>
									<th>電子郵件</th>
									<td class="user-email"><input type="email"
										class="user-email-input" disabled /></td>
								</tr>
								<tr>
									<th>會員密碼</th>
									<td class="user-password"><input type="password"
										class="user-password-input" disabled /></td>
								</tr>
								<tr>
									<th>手機號碼</th>
									<td class="user-phone"><input type="text"
										class="user-phone-input" disabled /></td>
								</tr>
								<tr>
									<th>註冊日期</th>
									<td class="registration-date"></td>
								</tr>
								<tr>
									<th>會員性別</th>
									<td class="user-gender"></td>
								</tr>
								<tr>
									<th>會員狀態</th>
									<td class="user-status"></td>
								</tr>
							</tbody>
						</table>
						<button type="button" class="modify-btn" onclick="enableEdit()">修改資料</button>
						<button type="button" class="cancel-btn" onclick="cancelEdit()"
							style="display: none;">取消修改</button>
						<button type="button" class="submit-btn" onclick="submitChanges()"
							style="display: none;">確認送出</button>
					</div>
				</div>
			</div>
		</div>

		<%-- 管理者編輯<彈窗> --%>
		<div id="settingsModal" class="modal">
			<div class="modal-content">
				<span class="close" onclick="closeSettings()">&times;</span>
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
<script src="JS/backstage.js"></script>
<script src="JS/sidebar_user.js"></script>
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