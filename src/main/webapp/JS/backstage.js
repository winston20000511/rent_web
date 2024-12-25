// 倒計時 10分鐘無反應登出
let logoutTimer;
resetTimer();
document.onmousemove = resetTimer;
document.onkeydown = resetTimer;

$(function() {
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
});

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
	outPanel();
}

function outPanel() {
	window.location.href = "LogoutServlet";
}



