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

<link rel="stylesheet" type="text/css" href="CSS/backstage.css">
<style>
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
				<li><a class="bi bi-house-door-fill" href="#"
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
				<li><a class="bi bi-receipt" href="#"
					data-content="sidebar_adOrder">廣告訂單管理<span
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
					<div class="container mt-4">
						<h1>房屋管理</h1>
						<table class="table table-bordered" id="myTable">
							<thead>
								<tr>
									<th>房屋ID</th>
									<th>房屋標題</th>
									<th>屋主ID</th>
									<th>屋主姓名</th>
									<th>地址</th>
									<th>價格</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody id="tableBody"></tbody>
						</table>

						<div id="message" class="alert alert-info" style="display: none"></div>

						<!-- Optional No Match Message -->
						<div id="noMatch" class="alert alert-warning"
							style="display: none;">未找到匹配的結果。</div>
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
	<script src="https://cdn.datatables.net/2.1.8/js/dataTables.min.js"></script>

	<script>
		window.onload = function() {	
			loadTable();
			// 無法獲取session時登出
        	var adminOnOff = "${sessionScope.admin != null ? sessionScope.admin : ''}";
        	if (adminOnOff == "" || adminOnOff == null) {
            	outPanel();
        	}
        	
        
        	// 資料更新確認
            var updateConfirm = "${requestScope.updateConfirm}";
			console.log("結果: "+updateConfirm);//判斷用可移除
            if (updateConfirm !== "" && updateConfirm !== null) {
            	if (updateConfirm === true || updateConfirm === "true") {
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
	    document.addEventListener("DOMContentLoaded", function() {
	        loadTable();
	    });
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
	    function loadTable() {
	        $.ajax({
	            url: "http://localhost:8080/rent_web/houses.123", // API 端點
	            type: "GET", // 使用 GET 方法
	            dataType: "json", // 資料類型為 JSON
	            success: function(data) {
	                console.log("Data received:", data); // 確認數據是否正確
	                
	                // 清空表格內容
	                $("#tableBody").empty();

	                // 檢查是否有匹配的結果
	                if (data.length === 0) {
	                    $("#noMatch").show(); // 顯示未找到匹配結果的提示
	                } else {
	                    $("#noMatch").hide(); // 隱藏提示
	                    
	                    // 遍歷每筆房屋資料並添加到表格
	                    $.each(data, function(index, house) {
	                        $("#tableBody").append(
	                        		'<tr>' +
	                                '<td>' + house.houseId + '</td>' +
	                                '<td>' + house.title + '</td>' +
	                                '<td>' + house.userId + '</td>' +
	                                '<td>' + house.userName + '</td>' +
	                                '<td>' + house.address + '</td>' +
	                                '<td>' + house.price + '</td>' +
	                                '<td>' +
	                                '<button class="btn btn-success" onclick="window.location.href=\'housePage.html?houseId=' + house.houseId + '\'">查看</button>' +
	                                '<button class="btn btn-danger" onclick="confirmDelete(' + house.houseId + ')">刪除</button>' +
	                                '</td>' +
	                                '</tr>'
	                        );
	                    });
	                }

	                // 銷毀舊的 DataTable 實例（如果存在）
	                if ($.fn.DataTable.isDataTable('#myTable')) {
	                    $('#myTable').DataTable().destroy();
	                }

	                // 初始化 DataTable
	                $("#myTable").DataTable({
	                    language: {
	                        url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json" // 使用繁體中文
	                    },
	                    autoWidth: true,
	                    info:true,
	                    processing: true, // 顯示資料加載中提示
	                    stateSave: true, // 保存使用者狀態
	                    lengthMenu: [10], // 每頁顯示 10 條數據
	                    dom: '<f<t><ip>>', // 自訂 DOM 結構
	                });
	            },
	            error: function(xhr, status, error) {
	                console.error("AJAX Error: ", status, error); // 處理錯誤
	            }
	        });
	    }


	    function confirmDelete(houseId) {
            console.log("Attempting to delete house with ID:", houseId); // Debugging log
            if (typeof houseId === 'undefined' || houseId <= 0) {
                console.error("Invalid house ID:", houseId);
                alert("Invalid house ID.");
                return;
            }

            if (confirm('確定要刪除這個房屋嗎？')) {
                const xhr = new XMLHttpRequest();
                xhr.open("POST", "/rent_web/housesDelete.123", true);
                xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4) {
                        const messageDiv = document.getElementById("message");
                        if (xhr.status === 200) {
                            messageDiv.innerHTML = '房屋已成功刪除。';
                            messageDiv.style.display = "block";
                            loadTable(); // Refresh the table after deletion
                        } else {
                            console.error('Error:', xhr.status, xhr.responseText);
                            messageDiv.innerHTML = '刪除失敗，請稍後再試。錯誤代碼：' + xhr.status + '，訊息：' + xhr.responseText;
                            messageDiv.style.display = "block";
                        }
                    }
                };

                xhr.send(JSON.stringify({ id: houseId }));
            }
            
        }
</script>
</body>
</html>
