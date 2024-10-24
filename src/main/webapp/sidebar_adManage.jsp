<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">

<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>廣告管理系統</title>
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
<link rel="stylesheet" type="text/css" href="CSS/adManage.css">

</head>

<body>
	<nav class="nav navbar navbar-expand-lg navbar-dark">
		<div class="container-fluid">
			<h1 class="navbar-brand">
				<span>租屋網站後台管理</span>
			</h1>
			<div class="d-flex justify-content-end align-items-center">
				<span id="timer" class="text-white me-3"></span> <span
					class="navbar-text text-white me-3"> 歡迎, <span
					id="adminName" class="admin-name" onclick="showSettings()">
				</span>
				</span> <a href="#" onclick="confirmLogout()" class="btn btn-outline-light">登出</a>
			</div>
		</div>
	</nav>
	<%-- Containers --%>
	<div class="containers">
		<%-- Sidebar --%>
		<div class="sidebar">

			<ul>
				<li><a class="bi bi-person-fill" href="sidebar_user.html"
					data-content="sidebar_member">會員管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-house-door-fill" href="sidebar_house.jsp"
					data-content="sidebar_house">房源管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-calendar-check-fill"
					href="sidebar_booking.jsp" data-content="sidebar_booking">預約管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-headset" href="#"
					data-content="sidebar_cusService">客服管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-chat-dots" href="#"
					data-content="sidebar_chat">聊天管理系統<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-receipt" href="sidebar_adMagae"
					data-content="sidebar_adManage">廣告訂單管理<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-card-checklist" href="#"
					data-content="sidebar_backendData">後台數據<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
				<li><a class="bi bi-gear" href="#"
					data-content="sidebar_settings">設定<span
						class="bi bi-caret-right-fill caret-icon"></span></a></li>
			</ul>
		</div>

		<%-- Content --%>
		<div class="content">
			<div class="content">
				<div class="Content">
					<div class="search-box">
						<label class="select-box-label"> <select
							name="searchCondition" id="search-condition">
								<option value="adId">廣告編號</option>
								<option value="userId">屋主編號</option>
								<option value="houseId">房屋編號</option>
						</select>
						</label> <label class="select-box-label"> <input type="text"
							name="searchInput" id="search-inupt-box" placeholder="請輸入編號" />
						</label> <label class="select-box-label"> <select
							name="paidCondition" id="paid-condition">
								<option value="" disabled selected>付款狀況</option>
								<option value="all">顯示全部</option>
								<option value="true">已付款</option>
								<option value="false">未付款</option>
						</select>
						</label>
						<button id="search" class="btn" type="button">搜尋</button>
					</div>

					<div>
						<div>
							<div class="title-box">搜尋結果</div>

							<table id="adtable" class="display">
								<thead>
									<tr>
										<th>廣告編號</th>
										<th>用戶編號</th>
										<th>用戶姓名</th>
										<th>房屋編號</th>
										<th>廣告類別</th>
										<th>廣告數量</th>
										<th>付款狀態</th>
										<th>操作功能</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="ad-id"></td>
										<td class="user-id"></td>
										<td class="user-name"></td>
										<td class="house-id"></td>
										<td class="ad-type"></td>
										<td class="ad-quantity"></td>
										<td class="is-paid"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>


					<div class="ad-details-box">
						<div>廣告詳細資料</div>
						<table id="ad-details-table">
							<tbody>
								<tr>
									<th>廣告編號</th>
									<td class="ad-id">1</td>
								</tr>
								<tr>
									<th>用戶編號</th>
									<td class="user-id">1</td>
								</tr>
								<tr>
									<th>用戶姓名</th>
									<td class="user-name">USER 1</td>
								</tr>
								<tr>
									<th>房屋編號</th>
									<td class="house-id">HOUSE 1</td>
								</tr>
								<tr>
									<th>廣告類別</th>
									<td class="ad-type">AD TYPE</td>
								</tr>
								<tr>
									<th>廣告天期</th>
									<td class="ad-duration">30天</td>
								</tr>
								<tr>
									<th>廣告單價</th>
									<td class="ad-price">10000</td>
								</tr>
								<tr>
									<th>數量</th>
									<td class="ad-quantity">QTY</td>
								</tr>
								<tr>
									<th>小計</th>
									<td class="ad-price-subtotal">小計</td>
								</tr>
								<tr>
									<th>新增／修改日期</th>
									<td class="created-date">日期</td>
								</tr>
								<tr>
									<th>付款狀態</th>
									<td class="is-paid">未付款</td>
								</tr>
								<tr>
									<th>訂單號碼</th>
									<td class="order-id">N/A</td>
								</tr>
								<tr>
									<th>付款日期</th>
									<td class="paid-date">N/A</td>
								</tr>
								<tr>
									<th>到期日期</th>
									<td class="expires-at">N/A</td>
								</tr>
							</tbody>
						</table>

						<div class="button-box">
							<button type="button" class="modify btn">修改資料</button>
							<button type="button" class="cancel btn">取消修改</button>
							<button type="button" class="submit btn">確認送出</button>
							<button type="button" class="leave btn">結束</button>
						</div>

					</div>

				</div>
			</div>
		</div>
	</div>
	<%-- 管理者編輯<彈窗> --%>
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
	<script src="JS/sidebar_booking.js"></script>
	<script src="JS/sidebar_adManage.js"></script>
	<script>
		window.onload = function() {		
			// 無法獲取session時登出
        	var adminOnOff = "${sessionScope.admin}";
        	if (adminOnOff == "" || adminOnOff == null) {
            	outPanel();
        	}
        	
        
        	// 資料更新確認
            var updateConfirm = "${requestScope.updateConfirm}";
			console.log("結果: "+updateConfirm);//判斷用可移除
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
        	
		}	
        // 倒計時 10分鐘無反應登出
        var logoutTimer;
        resetTimer();
	    document.onmousemove = resetTimer;
		document.onkeydown = resetTimer;
			
			
		// sidebar(側邊欄位)監聽器
    	const links = document.querySelectorAll('.sidebar a');
	
	    links.forEach(link => {
	        link.addEventListener('click', function (event) {
	          
	            links.forEach(l => {
	                const icon = l.querySelector('.caret-icon');
	                icon.classList.remove('active'); //顏色復原
	            });
	            const currentIcon = link.querySelector('.caret-icon');
	            currentIcon.classList.add('active'); //更換顏色
	            
	        });
	    });

		// 讀取管理者(admin)資料 (****需改成SERVLET請求)
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
				<label for="adminPasswordInput" class="form-label">密碼</label>
				<input type="text" id="adminPasswordInput" name="adminPassword" class="form-control" value="${sessionScope.admin.adminPassword}" required/>
			</div>
			<div class="mb-3">
				<label for="adminPhoneInput" class="form-label">電話</label>
				<input type="text" id="adminPhoneInput" name="adminPhone" class="form-control" value="${sessionScope.admin.adminPhone}" required/>
			</div>
		`;

		// 顯示管理員名稱
		var adminName = "${sessionScope.admin != null ? sessionScope.admin.adminName : '管理員'}";
		document.getElementById("adminName").innerText = adminName;
		
		
/* 下面放 function */
		
		// 管理員編輯<顯示彈窗>  
			function showSettings() {
	        	document.getElementById("settingsModal").classList.add('show');	//編輯的視窗
	        	document.getElementById("settingsModalBackdrop").classList.add('show');	//遮罩
	    	}
		
		// 管理員編輯<關閉彈窗> 
			function closeSettings() {
			document.getElementById("settingsModal").classList.remove('show');	//編輯的視窗
			document.getElementById("settingsModalBackdrop").classList.remove('show');	//遮罩
			}
		
		// 資料更新<關閉彈窗>
		function closeTable() {
	        document.getElementById("updateTable").style.display = "none";	// 更新的視窗
	        document.getElementById("settingsModalBackdrop").classList.remove('show');	//遮罩
	    }
		
		// 登出按鈕
	    function confirmLogout() {
	        // 彈出確認框，返回布林值
	        const confirmation = confirm("你確定要登出嗎？");
	        if (confirmation) {
	            outPanel(); // 如果點擊「確定」，則執行登出操作
	        }
	    }
		
		// 倒計時<登出>
	    function resetTimer() {
			clearTimeout(logoutTimer);
			logoutTimer = setTimeout(logout, 600000); // 毫秒(10分鐘)
		}
	    
	    
	    function logout() {
			alert("您已經超過 10 分鐘未操作，將自動登出");
			window.location.href = "LogoutServlet"; 
		}
	    
	    function outPanel() {
	    	window.location.href = "LogoutServlet"; 
	    }
	 	
</script>
</body>
</html>
