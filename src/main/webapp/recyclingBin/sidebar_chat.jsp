<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">
<head>
<meta charset="UTF-8" />
<link rel="stylesheet"
	href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.css" />
<title>聊天管理系統</title>
<style></style>
</head>
<body>
	<h1>聊天管理</h1>
	<table id="myTable" class="display">
		<thead>
			<tr>
				<th>房屋 ID</th>
				<th>用戶 ID</th>
				<th>預約日期</th>
				<th>開始時間</th>
				<th>預約 ID</th>
				<th>結束時間</th>
				<th>狀態</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>
					<button>編輯</button>
					<button>刪除</button>
				</td>
			</tr>
			<tr>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>
					<button>編輯</button>
					<button>刪除</button>
				</td>
			</tr>
			<tr>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>123</td>
				<td>
					<button>編輯</button>
					<button>刪除</button>
				</td>
			</tr>
		</tbody>
	</table>

	<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
	<script src="https://cdn.datatables.net/2.1.8/js/dataTables.js"></script>
	<script>
		$("#myTable").DataTable();
	</script>
</body>
</html>