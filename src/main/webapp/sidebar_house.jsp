<%@ page contentType="text/html;charset=UTF-8" language="java"
    pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>房屋管理</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.datatables.net/2.1.8/css/dataTables.dataTables.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet" />
    <link rel="stylesheet" href="./CSS/House.css">
</head>

<body>

    <nav class="nav navbar navbar-expand-lg navbar-dark">
        <div class="container-fluid">
            <h1 class="navbar-brand">
                <a class="navbar-brand" href="backstage-panel.jsp"><span>租屋網站後台管理</span></a>
            </h1>
            <div class="d-flex justify-content-end align-items-center">
                <div class="d-flex justify-content-end align-items-center navbar-text text-white me-3">
                    <span>歡迎,</span>
                    <span class="admin-name" onclick="showSettings()">
                        <span id="adminName"></span> <span class="bi bi-pin-angle-fill"></span>
                    </span>
                </div>
                <a href="#" onclick="confirmLogout()" class="btn btn-outline-light">登出</a>
            </div>
        </div>
    </nav>

    <div class="sidebar">
        <ul>
            <li><a class="bi bi-card-checklist" href="backstage-panel.jsp">首 頁</a></li>
            <li><a class="bi bi-person-fill" href="siderbar_user.jsp">會員管理系統</a></li>
            <li><a class="bi bi-houses-fill" href="sidebar_house.jsp">房源管理系統</a></li>
            <li><a class="bi bi-calendar-check-fill" href="sidebar_booking.jsp">預約管理系統</a></li>
            <li><a class="bi bi-badge-ad" href="sidebar_adManage.jsp">廣告管理系統</a></li>
            <li><a class="bi bi-wallet" href="sidebar_orderManage.jsp">訂單管理系統</a></li>
            <li><a class="bi bi-wallet" href="sidebar_complaints.jsp">客訴管理系統</a></li>
        </ul>
    </div>


    <div class="content">
        <h1>房屋管理</h1>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>房屋ID</th>
                    <th>照片</th>
                    <th>房屋標題</th>
                    <th>屋主ID</th>
                    <th>屋主Email</th>
                    <th>屋主姓名</th>
                    <th>地址</th>
                    <th>價格</th>
                    <th>狀態</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody id="tableBody">

            </tbody>
        </table>
    </div>

 
    <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="viewModalLabel">查看房屋詳細信息</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div id="houseDetails">
                        <%-- 動態加載的詳細信息 --%>
                        <div class="boolean-fields">
                            <div class="mb-2">
                                <span class="boolean-icon" id="pet-icon"></span>
                                允許寵物
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="parkingSpace-icon"></span>
                                停車位
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="elevator-icon"></span>
                                有電梯
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="balcony-icon"></span>
                                有陽台
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="shortTerm-icon"></span>
                                短期出租
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="cooking-icon"></span>
                                允許煮食
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="waterDispenser-icon"></span>
                                提供飲水機
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="managementFee-icon"></span>
                                含管理費
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="washingMachine-icon"></span>
                                有洗衣機
                            </div>
                            <div class="mb-2">
                                <span class="boolean-icon" id="airConditioner-icon"></span>
                                有空調
                            </div>
                        </div>
                    </div>
                    <button id="editButton" class="btn btn-warning mt-3" style="display: none;">編輯</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editModalLabel">編輯房屋資料</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editForm">
                        <%-- Boolean Fields as Checkboxes --%>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="pet" name="pet" />
                            <label class="form-check-label" for="pet">允許寵物</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="parkingSpace" name="parkingSpace" />
                            <label class="form-check-label" for="parkingSpace">有停車位</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="elevator" name="elevator" />
                            <label class="form-check-label" for="elevator">有電梯</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="balcony" name="balcony" />
                            <label class="form-check-label" for="balcony">有陽台</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="shortTerm" name="shortTerm" />
                            <label class="form-check-label" for="shortTerm">短期出租</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="cooking" name="cooking" />
                            <label class="form-check-label" for="cooking">允許煮食</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="waterDispenser" name="waterDispenser" />
                            <label class="form-check-label" for="waterDispenser">提供飲水機</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="managementFee" name="managementFee" />
                            <label class="form-check-label" for="managementFee">含管理費</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="washingMachine" name="washingMachine" />
                            <label class="form-check-label" for="washingMachine">有洗衣機</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="airConditioner" name="airConditioner" />
                            <label class="form-check-label" for="airConditioner">有空調</label>
                        </div>

                        <button type="submit" class="btn btn-primary mt-3">保存更改</button>
                    </form>
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
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="JS/backstage.js"></script>
	<script src="/JS/sidebar_house.js"></script>
</body>

</html>
