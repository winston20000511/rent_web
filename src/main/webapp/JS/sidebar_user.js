$(document).ready(function() {
   loadAllUsers();
});

function loadAllUsers() {
    console.log("Loading all users...");
    fetch('/rent_web/LoadAllUsersServlet.do')
    .then(response => response.json())
    .then(data => {
        console.log("Fetched user data:", data);
        const userTableBody = document.querySelector("#usertable tbody");
        userTableBody.innerHTML = ""; // Clear table
        data.sort((a, b) => new Date(b.createtime) - new Date(a.createtime));
        
        // Populate table
        data.forEach(user => {
            userTableBody.innerHTML += `
                <tr>
                    <td>${user.userId}</td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.password}</td>
                    <td>${user.phone}</td>
                    <td>${user.createtime}</td>
                    <td>${user.gender == 0 ? '男' : '女'}</td>
                    <td>${user.status == 0 ? '停權會員' : '一般會員'}</td>
                    <td class="action"><button type="button" class="action-btn" onclick="fillDetails('${user.userId}', '${user.name}', '${user.email}', '${user.password}', '${user.phone}', '${user.createtime}', '${user.gender}', '${user.status}')">執行操作</button></td>
                </tr>
            `;
        });

        // Initialize DataTable
        if ($.fn.DataTable.isDataTable('#usertable')) {
            $('#usertable').DataTable().destroy();
        }
        
        $("#usertable").DataTable({
            language: {
                url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json"
            },
            processing: true,
            stateSave: true,
            pageLength: 10,
            lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
            dom: '<f<t><ip>>',
        });
    })
    .catch(error => console.error('發生錯誤:', error));
}

function searchUser(event) {
    event.preventDefault();
    const searchCondition = document.getElementById('search-condition').value;
    const searchInput = document.getElementById('search-input-box').value;
    let body;

    if (searchCondition === 'userId') {
        body = new URLSearchParams({ searchInput: searchInput, searchType: 'userId' });
    } else if (searchCondition === 'userName') {
        body = new URLSearchParams({ searchInput: searchInput, searchType: 'userName' });
    } else if (searchCondition === 'userEmail') {
        body = new URLSearchParams({ searchInput: searchInput, searchType: 'userEmail' });
    }

    fetch('/rent_web/SearchUserServlet.do', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: body
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('網頁請求失敗，狀態碼：' + response.status);
        }
        return response.json();
    })
    .then(data => {
        const userTableBody = document.querySelector("#usertable tbody");
        userTableBody.innerHTML = ""; // 清空表格
        if (Object.keys(data).length > 0) {
            // 更新表格中的內容
            data.forEach(user => {
                userTableBody.innerHTML += `
                    <tr>
                        <td>${user.userId}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${user.password}</td>
                        <td>${user.phone}</td>
                        <td>${user.createtime}</td>
                        <td>${user.gender == 0 ? '男' : '女'}</td>
                        <td>${user.status}</td>
                        <td class="action"><button type="button" class="action-btn" onclick="fillDetails('${user.userId}', '${user.name}', '${user.email}', '${user.password}', '${user.phone}', '${user.picture}', '${user.createtime}', '${user.gender}','${user.status}')">執行操作</button></td>
                    </tr>
                `;
            });
        } else {
            userTableBody.innerHTML = `<tr><td colspan="9">查無資料</td></tr>`;
        }
    })
    .catch(error => console.error('發生錯誤:', error));
}

function fillDetails(userId, name, email, password, phone, createtime, gender, status) {
    currentUserId = userId; // 記錄當前用戶ID
    document.querySelector(".user-name input").value = name;
    document.querySelector(".user-email input").value = email;
    document.querySelector(".user-password input").value = password;
    document.querySelector(".user-phone input").value = phone;
    document.querySelector(".registration-date").textContent = createtime;
    document.querySelector(".user-gender").textContent = (gender == 0 ? '男' : '女');
    document.querySelector(".user-status").textContent = status;

    document.querySelector('.ad-detail-box').style.display = 'block'; // 顯示詳細資料框
}

function hideDetails() {
    document.querySelector('.ad-detail-box').style.display = 'none';
}

function enableEdit() {
    document.querySelectorAll('.ad-detail-box input').forEach(input => input.disabled = false); // 使所有輸入框可編輯
    document.querySelector('.modify-btn').style.display = 'none'; // 隱藏修改按鈕
    document.querySelector('.cancel-btn').style.display = 'inline'; // 顯示取消按鈕
    document.querySelector('.submit-btn').style.display = 'inline'; // 顯示確認按鈕
}

function cancelEdit() {
    document.querySelectorAll('.ad-detail-box input').forEach(input => input.disabled = true); // 使所有輸入框不可編輯
    document.querySelector('.modify-btn').style.display = 'inline'; // 顯示修改按鈕
    document.querySelector('.cancel-btn').style.display = 'none'; // 隱藏取消按鈕
    document.querySelector('.submit-btn').style.display = 'none'; // 隱藏確認按鈕
}

function submitChanges() {
    const updatedUser = {
        userId: currentUserId,
        name: document.querySelector(".user-name input").value,
        email: document.querySelector(".user-email input").value,
        password: document.querySelector(".user-password input").value,
        phone: document.querySelector(".user-phone input").value,
        gender: document.querySelector(".user-gender").textContent === '男' ? 0 : 1,
		status: document.querySelector(".user-status").textContent,
    };

	console.log("update user: " + updatedUser.userId);
	
    fetch('/rent_web/UpdateUserServlet.do', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedUser)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('更新失敗，狀態碼：' + response.status);
        }
        return response.json();
    })
    .then(data => {
        alert('更新成功');
        // Reload user data after successful update
        location.reload(); 
    })
    .catch(error => {
        console.error('Error:', error);
        alert('更新失敗，請重試。');
    });
}