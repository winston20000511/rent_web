<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="rent189.model.AdminBean"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">

<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>租屋網站後台管理</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	data-integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	data-crossorigin="anonymous" />
<!-- Bootstrap Icons -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" />
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
	font-family: Arial, sans-serif;
}

body {
	display: flex;
	flex-direction: column;
	height: 100vh;
}

nav {
	
	display: flex;
	justify-content: space-between;
	background-color: #343a40;
	padding: 20px;
	height: 60px
}

.containers {
	display: flex;
	justify-content:space-between;
	flex-direction: row
}

.sidebar {
	background-color: #343a49;
	padding: 10px;
	
}

.content {
	flex: 1;
	background-color: #fff;
	padding: 10px;
	box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
	overflow-y: auto; /* 內容超過能滾動 */
}

footer {
	background-color: #343a40;
	text-align: center;
	padding: 20px;
	color: white;
	bottom: 0;
	margin-top: auto;
}

.login-container {
	display: flex;
	justify-content: center;
	align-items: center;
	min-height: 100vh;
}

.login-box {
	background-color: #ffffff;
	padding: 40px;
	border-radius: 10px;
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
	width: 100%;
	max-width: 400px;
}

.login-box h2 {
	margin-bottom: 20px;
	font-size: 24px;
	text-align: center;
	color: #343a40;
}

.form-control {
	border-radius: 5px;
	margin-bottom: 20px;
}

.btn-login {
	width: 100%;
	background-color: #343a40;
	color: white;
	padding: 10px;
	border-radius: 5px;
	font-size: 16px;
}

.btn-login:hover {
	background-color: #495057;
}

.login-footer {
	text-align: center;
	margin-top: 20px;
	color: #343a40;
}

#error-message {
	color: red;
	font-weight: bold;
	margin-top: 15px;
	text-align: center;
	visibility: hidden;
}

.navbar-brand {
	color: #fff;
	font-weight: bold;
}

.navbar-text {
	color: #fff;
	font-size: 16px;
}

.btn-outline-light {
	border-color: #fff;
	color: #fff;
}

.btn-outline-light:hover {
	background-color: #fff;
	color: #343a40;
}

.sidebar a {
	color: #fff;
	text-decoration: none;
	padding: 10px 20px;
	display: block;
}

.sidebar a:hover {
	background-color: #495057;
	border-radius: 5px;
}

.sidebar ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

table {
	width: 100%;
	margin-bottom: 20px;
	border-collapse: collapse;
}

th, td {
	padding: 12px;
	text-align: left;
}

th {
	background-color: #6c757d;
	color: white;
}

td {
	background-color: white;
}

.admin-name {
	cursor: pointer;
	text-decoration: underline;
	color: #ffd700;
}

/* Modal styles */
.modal {
	display: none;
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 600px;
	height: 550px;
	background-color: white;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
	border-radius: 8px;
	z-index: 1000;
	animation: fadeIn 0.3s ease-in-out;
}

.modal-content {
	display: flex;
	flex-direction: column;
	height: 100%;
}

.modal-header {
	background-color: #007bff;
	color: white;
	padding: 20px;
	font-size: 24px;
	text-align: center;
	border-top-left-radius: 8px;
	border-top-right-radius: 8px;
}

.modal-body {
	flex-grow: 1;
	padding: 20px;
	overflow: hidden;
}

.modal-body .mb-3 {
	margin-bottom: 20px;
	padding-bottom: 20px;
	border-bottom: 1px solid #ccc;
}

.modal-body input {
	width: 100%;
	padding: 12px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.modal-footer {
	background-color: #f8f9fa;
	text-align: right;
	padding: 10px;
	border-bottom-left-radius: 8px;
	border-bottom-right-radius: 8px;
}

.modal-footer button {
	padding: 10px 20px;
	background-color: #007bff;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.modal-footer button:hover {
	background-color: #0056b3;
}

.modal-backdrop {
	display: none;
	position: fixed;
	top: 0;
	left: 0;
	width: 100vw;
	height: 100vh;
	background: rgba(0, 0, 0, 0.6);
	z-index: 999;
}

@
keyframes fadeIn {from { opacity:0;
	transform: translate(-50%, -60%);
}

to {
	opacity: 1;
	transform: translate(-50%, -50%);
}

}
.modal.show, .modal-backdrop.show {
	display: block;
	transition: opacity 0.3s ease-in-out;
}

/* 確保表單樣式一致 */
.form-label {
	font-weight: bold;
}

.form-control {
	border-radius: 5px;
	padding: 10px;
	border: 1px solid #ccc;
	box-sizing: border-box;
	width: 100%;
}

.mb-3 {
	margin-bottom: 20px;
}

.btn-primary {
	background-color: #007bff;
	color: white;
	border-radius: 5px;
	padding: 10px;
	width: 100%;
}

.btn-primary:hover {
	background-color: #0056b3;
}

.modal-content {
	padding: 20px;
}

.modal-body {
	padding: 20px;
}

.modal-footer {
	padding: 10px;
	text-align: right;
}

.error-message {
	color: red;
	font-weight: bold;
	margin-top: 10px;
}

.close {
	position: absolute;
	top: 20px;
	right: 20px;
	width: 40px;
	height: 40px;
	background-color: #007bff;
	color: white;
	border-radius: 50%;
	display: flex;
	justify-content: center;
	align-items: center;
	font-size: 24px;
	cursor: pointer;
	box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
	transition: background-color 0.3s ease;
}

.close:hover {
	background-color: #0056b3;
}

.close:focus {
	outline: none;
}

.update-alert {
	padding: 15px;
	margin-top: 20px;
	border-radius: 5px;
	text-align: center;
	font-size: 16px;
}

.alert-success {
	background-color: #d4edda;
	color: #155724;
	border: 1px solid #c3e6cb;
}

.alert-danger {
	background-color: #f8d7da;
	color: #721c24;
	border: 1px solid #f5c6cb;
}

/* 基本彈出框樣式 */
.popup {
	position: fixed;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 300px;
	padding: 20px;
	background-color: #fff;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	border-radius: 10px;
	z-index: 1000;
	text-align: center;
}

/* 成功樣式 */
.popup-success {
	border-left: 5px solid #4CAF50;
}

/* 失敗樣式 */
.popup-error {
	border-left: 5px solid #f44336;
}

/* 關閉按鈕樣式 */
.popup-content .close {
	position: absolute;
	top: 10px;
	right: 10px;
	font-size: 20px;
	cursor: pointer;
}

/* 彈出框文字 */
.popup-content p {
	font-size: 16px;
	margin: 20px 0;
}
</style>

</head>

<body>
	<nav class="navbar navbar-expand-lg navbar-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="#">租屋網站後台管理</a>
			<div class="d-flex justify-content-end align-items-center">
				<span class="navbar-text text-white me-3"> 歡迎, <span
					id="adminName" class="admin-name" onclick="showSettings()">
				</span>
				</span> <a href="#" onclick="confirmLogout()" class="btn btn-outline-light">登出</a>
			</div>
		</div>
	</nav>
	<!-- Containers -->
	<div class="Containers">
		<!-- Sidebar -->
		<div class="sidebar">

			<ul>
				<li><a class="bi bi-person-fill" href="#"
					data-content="member_management">&nbsp;會員管理系統</a></li>
				<li><a class="bi bi-house-door-fill" href="#"
					data-content="house_management">&nbsp;房源管理系統</a></li>
				<li><a class="bi bi-calendar-check-fill" href="#"
					data-content="booking_management">&nbsp;預約管理系統</a></li>
				<li><a class="bi bi-headset" href="#"
					data-content="customer_service">&nbsp;客服管理系統</a></li>
				<li><a class="bi bi-chat-dots" href="#"
					data-content="chat_management">&nbsp;聊天管理系統</a></li>
				<li><a class="bi bi-receipt" href="#"
					data-content="ad_order_management">&nbsp;廣告訂單管理</a></li>
				<li><a class="bi bi-card-checklist" href="#"
					data-content="backend_data">&nbsp;後台數據</a></li>
				<li><a class="bi bi-gear" href="#" data-content="settings">&nbsp;設定</a></li>
			</ul>
		</div>

		<!-- Content -->
		<div class="content">
			
		</div>
	</div>

	<!-- 設定彈出視窗 -->

	<div id="settingsModal" class="modal">
		<div class="modal-content">
			<span class="close" onclick="closeSettings()">&times;</span>
			<h4>編輯管理者資料</h4>
			<form id="settingsForm" method="post" action="updateAdminDataServlet">
				<div id="adminData">
					<!-- 這些欄位會在 JavaScript 中動態插入 -->
				</div>
				<button type="submit" class="btn btn-primary">保存更改</button>
			</form>
		</div>
	</div>

	<!-- 背景遮罩 -->
	<div id="settingsModalBackdrop" class="modal-backdrop"></div>



	<footer>
		<p>租屋網站後台管理 &copy; 2024</p>
	</footer>

	<script>
		window.onload = function() {
        	var adminOnOff = "${sessionScope.admin}";
        	
        	if (adminOnOff === "" || adminOnOff === null) {
            outPanel();
        	}
    	}
	
		// 設定點擊事件監聽器
    	const links = document.querySelectorAll('.sidebar a');
	
	    links.forEach(link => {
	        link.addEventListener('click', function (event) {
	            event.preventDefault(); // 阻止預設的鏈接行為
	            const contentDiv = document.querySelector('.content'); // 找到 content div
	            const page = link.getAttribute('data-content'); // 讀取 data-content 的值
	            loadContent(page); // 載入對應的 JSP 文件
	        });
	    });

	    // 定義載入 JSP 文件的函數
	    function loadContent(page) {
	        const xhr = new XMLHttpRequest();
	        xhr.open('post', page + '.jsp', true); // 根據點擊的內容載入對應的 JSP 文件
	        xhr.onreadystatechange = function () {
	            if (xhr.readyState === 4 && xhr.status === 200) {
	                document.querySelector('.content').innerHTML = xhr.responseText; // 將 JSP 文件的內容載入到 content 區域
	            }
	        };
	        xhr.send();
    	}
	
 		// 顯示管理員名稱
		var adminName = "${sessionScope.admin != null ? sessionScope.admin.adminName : '管理員'}";
		document.getElementById("adminName").innerText = adminName;

		    
		function showSettings() {
        	document.getElementById("settingsModal").classList.add('show');
        	document.getElementById("settingsModalBackdrop").classList.add('show');
    	}
		    
		function closeSettings() {
		document.getElementById("settingsModal").classList.remove('show');
		document.getElementById("settingsModalBackdrop").classList.remove('show');
		}
		
		function closeModal() {
            var modal = document.getElementById("statusModal");
            modal.classList.remove("show");
        }
		
		// 編輯管理者資料
		document.getElementById("adminData").innerHTML = `
			<div class="mb-3">
				<label for="adminNameInput" class="form-label">姓名</label>
				<input type="text" id="adminNameInput" name="adminName" class="form-control" value="${sessionScope.admin.adminName}" required/>
			</div>
			<div class="mb-3">
				<label for="adminEmailInput" class="form-label">電子郵件</label>
				<input type="email" id="adminEmailInput" name="adminEmail" class="form-control" value="${sessionScope.admin.adminEmail}" required/>
			</div>
			<div class="mb-3">
				<label for="adminPInput" class="form-label">密碼</label>
				<input type="text" id="adminPasswordInput" name="adminPassword" class="form-control" value="${sessionScope.admin.adminPassword}" required/>
			</div>
			<div class="mb-3">
				<label for="adminPhoneInput" class="form-label">電話</label>
				<input type="text" id="adminPhoneInput" name="adminPhone" class="form-control" value="${sessionScope.admin.adminPhone}" required/>
			</div>
		`;
		            
		// 設置登出計時器
		var logoutTimer;
		function resetTimer() {
			clearTimeout(logoutTimer);
			logoutTimer = setTimeout(logout, 10 * 60 * 1000); // 10 分鐘後登出
		}

		function logout() {
			alert("您已經超過 10 分鐘未操作，將自動登出");
			window.location.href = "backstage-login.jsp"; // 登出後跳轉至登入頁面
		}
		
		// 更新確認-顯示彈出框
		var updateConfirm = "${sessionScope.updateConfirm}";
		
		if(updateConfirm!=""){
       		if (updateConfirm) {
        		showUpdateConfirm("更新成功", "success");
        	}else{
        	showUpdateConfirm("更新失敗", "error");
        	}
		}    
		
		/* 以下function */
	    function showUpdateConfirm(message, type) {
	        var popup = document.createElement("div");
	        popup.classList.add("popup");

	        // 根據 type 給予不同的樣式
	        if (type == "success") {
	            popup.classList.add("popup-success");
	        } else if (type == "error") {
	            popup.classList.add("popup-error");
	        }

	        popup.innerHTML = `
	            <div class="popup-content">
	                <span class="close" onclick="closeUpdateConfirm()">&times;</span>
	                <div style="font-weight: bold">更新成功</div>
	            </div>
	        `;
	        
	        document.body.appendChild(popup);
	     	// 清除 session 中的 updateConfirm
	        <%session.removeAttribute("updateConfirm");%>
	    }
		
	 	
	    function closeUpdateConfirm() {
	        var popup = document.querySelector(".popup");
	        if (popup) {
	            popup.remove();
	        }
	    }
		
	 	
	    function confirmLogout() {
	        // 彈出確認框，返回布林值
	        const confirmation = confirm("你確定要登出嗎？");
	        if (confirmation) {
	            outPanel(); // 如果點擊「確定」，則執行登出操作
	        }
	    }

	    function outPanel() {
	        window.location.href = "backstage-login.jsp";
	    }
	 	
	    
	    
		window.onload = resetTimer;
		document.onmousemove = resetTimer;
		document.onkeydown = resetTimer;
</script>
</body>
</html>
