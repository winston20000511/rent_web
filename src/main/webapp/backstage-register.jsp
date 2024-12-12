<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Rent189租屋 註冊</title>
<link rel="Rent icon" href="./imags/lpoqq-yg0x4-001.ico">
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
body {
	background-color: #f1f1f1;
	font-family: "Arial", sans-serif;
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

.visible {
	visibility: visible;
}
.loginatag{
  text-decoration: none;
  cursor: pointer;
  display: inline-block;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
}
</style>
</head>

<body>
	<!-- Register Container -->
	<div class="login-container">
		<div class="login-box">
			<h2>管理者註冊</h2>
			<!-- 表單 -->
			<form action="rent_web/RegisterServlet" id="registerform" method="POST">
				<div class="form-group">
					<label for="adminName">姓名</label>
					<input type="text" class="form-control" id="name" name="name"
						placeholder="請輸入姓名" autocomplete="off" required />
				</div>
				<div class="form-group">
					<label for="userEmail">Email</label>
					<input type="email" class="form-control" id="email" name="email"
						placeholder="請輸入Email" autocomplete="off" required />
				</div>
				<div class="form-group">
					<label for="password">密碼</label>
					<input type="password" class="form-control" id="password" name="password"
						autocomplete=off placeholder="請輸入密碼" required />
				</div>
				<div class="form-group">
					<label for="confirmPassword">確認密碼</label>
					<input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
						autocomplete=off placeholder="請再次輸入密碼" required />
				</div>
				<div class="form-group">
                    <label for="adminPhone">輸入電話</label>
                    <input type="text" class="form-control" id="phone" name="phone">
                </div>
                <div class="form-group">
                    <label>權限：</label>
                    <select id="role" class="role" default="Admin" ><!--  class="form-control" set input? -->
                      <option value="SAdmin" >SAdmin</option>
                      <option value="Admin" selected>Admin</option>
                      <option value="RAdmin-ad" >RAdmin-ad</option>
                      <option value="RAdmin-book" >RAdmin-book</option>
                      <option value="RAdmin-complaint" >RAdmin-complaint</option>
                      <option value="RAdmin-house" >RAdmin-house</option>
                      <option value="RAdmin-order" >RAdmin-order</option>
                      <option value="RAdmin-user" >RAdmin-user</option>
                    </select>
                  </div>

				<!-- 顯示錯誤訊息 -->
				<p id="error-message"
					class="<%= request.getAttribute("registerError") != null ? "visible" : "" %>">
					<%= request.getAttribute("registerError") != null ? request.getAttribute("registerError") : "" %>
				</p>
				<button type="submit" class="btn btn-login">註冊</button>
			<a class="loginatag" href="http://localhost:8888/rent_web/backstage-login.jsp">登入</a>

			</form>
			<div class="login-footer">
				<div>租屋網站後台管理 &copy; 2024</div>
			</div>
		</div>
	</div>

	<script>
        const registerform = document
        .getElementById("registerform")
        .addEventListener("submit",(e) =>{
            e.preventDefault();
            
        const name =document.getElementById("name").value;
        const email =document.getElementById("email").value;
        const password =document.getElementById("password").value;
        const confirmPassword =document.getElementById("confirmPassword").value;
        const adminPhone =document.getElementById("phone").value;
        const role =document.getElementById("role").value;
        
	console.log("name",name);
	console.log("email",email);
	console.log("PWD",password);
	console.log("adminPhone",adminPhone);
	console.log("role",role);
	
    if (password !== confirmPassword) {
        alert("密碼和確認密碼不一致！");
        return;
    }
    const regform = { name, email, password, adminPhone, adminPhone, role };

    fetch("/rent_web/RegisterServlet",{
        method: "post",
        headers:{
            "Content-Type" : "application/JSON"
        },
        body:JSON.stringify(regform),
    })    
    .then((response) =>response.json())
    .then((data) =>{
        console.log("response JSON" ,data);
        if (data.success) {
                alert("註冊成功！");
                window.location.href = "/rent_web/backstage-login.jsp"; 
            } else {
                alert("註冊失敗：" + data.message);
            }
        })
    .catch((e)=>{
        console.e("error",e)
        alert("提交失敗，請稍後重試！");
    })

    alert("註冊表單已完成，準備提交數據！");
    
        })

	</script>
</body>
</html>
