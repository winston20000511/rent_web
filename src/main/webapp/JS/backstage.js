window.onload = function() {
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



}

$(document).ready(function() {
	// 顯示管理員名稱
	var adminName = "${sessionScope.admin != null ? sessionScope.admin.adminName : '管理員'}";
	document.getElementById("adminName").innerText = adminName;
});

// 倒計時 10分鐘無反應登出
var logoutTimer;
resetTimer();
document.onmousemove = resetTimer;
document.onkeydown = resetTimer;


// sidebar(側邊欄位)監聽器
const links = document.querySelectorAll('.sidebar a');

links.forEach(link => {
	link.addEventListener('click', function(event) {

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