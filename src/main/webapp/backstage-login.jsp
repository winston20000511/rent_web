<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Rent189租屋 登入</title>
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
</style>
</head>

<body>
	<!-- Login Container -->
	<div class="login-container">
		<div class="login-box">
			<h2>管理者登入</h2>
			<!-- 表單 -->
			<form action="BackstageLoginServlet" method="POST">
				<div class="form-group">
					<label for="userEmail">Email</label> <input type="text"
						class="form-control" id="userEmail" name="userEmail"
						placeholder="請輸入Email" autocomplete="off" required />
				</div>
				<div class="form-group">
					<label for="password">密碼</label> <input type="password"
						class="form-control" id="password" name="password"
						autocomplete=off placeholder="請輸入密碼" required />
				</div>
				<!-- 顯示錯誤訊息 -->
				<p id="error-message"
					class="<%= request.getAttribute("loginError") != null ? "visible" : "" %>">
					<%= request.getAttribute("loginError") != null ? request.getAttribute("loginError") : "" %>
				</p>
				<button type="submit" class="btn btn-login">登入</button>
			</form>
			<div class="login-footer">
				<div>租屋網站後台管理 &copy; 2024</div>
			</div>
		</div>
	</div>

</body>
</html>