<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>房屋管理</title>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.4/css/jquery.dataTables.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.4/js/jquery.dataTables.min.js"></script>

</head>
<body>
    <h1>房屋管理</h1>

    <div class="refresh-container">
        <button class="refresh-btn" onclick="refreshTable()">重新刷新表格</button>
    </div>

    <table id="houseTable" class="display">
        <thead>
            <tr>
                <th>房屋編號</th>
                <th>房屋照片</th>
                <th>標題</th>
                <th>價格</th>
                <th>地址</th>
                <th>擁有者名稱</th>
                <th>電子郵件</th>
                <th>狀態</th>
            </tr>
        </thead>
    </table>

    <script>
        $(document).ready(function () {
            const table = $('#houseTable').DataTable({
                "processing": true,
                "serverSide": true,
                "ajax": {
                    "url": "/rent_web/HouseListServlet",
                    "type": "GET",
                    "data": function (d) {
                        d.action = "list"; // 傳遞動作到伺服器
                    }
                },
                "columns": [
                    { "data": "houseId", "orderable": false },
                    {
                        "data": "houseId",
                        "orderable": false,
                        "render": function (data, type, row) {
                            return '<img src="/rent_web/HouseImageServlet?houseId=' + data + '" alt="房屋圖片" width="100" height="100">';
                        }
                    },
                    { "data": "title", "orderable": false },
                    { "data": "price", "orderable": false },
                    { "data": "address", "orderable": false },
                    { "data": "userName", "orderable": false },
                    { "data": "userEmail", "orderable": false },
                    { 
                        "data": "status", "orderable": false,
                        "render": function (data, type, row) {
                            const options = [
                                { value: 0, text: "待審查" },
                                { value: 1, text: "上架中" },
                                { value: 2, text: "下架" }
                            ];

                            let select = '<select id="statusSelect_' + row.houseId + '" style="font-size: 18px;">';
                            options.forEach(option => {
                                select += '<option value="' + option.value + '"' + (data === option.value ? ' selected' : '') + '>' + option.text + '</option>';
                            });
                            select += '</select> ';
                            select += '<button style="font-size: 18px;" onclick="confirmStatusChange(' + row.houseId + ')">確認</button>';

                            return select;
                        }
                    },
                ]
            });

            window.confirmStatusChange = function (houseId) {
                const selectElement = document.getElementById('statusSelect_' + houseId);
                const newStatus = selectElement.value;

                if (confirm("確定要更改狀態嗎？")) {
                    $.post("/rent_web/UpdateHouseStatusServlet", { houseId: houseId, status: newStatus }, function (response) {
                        if (response.success) {
                            alert(response.message);
                            const row = table.row(function (idx, data, node) {
                                return data.houseId === houseId;
                            });

                            if (row.node()) {
                                $(row.node()).addClass('highlight');
                                setTimeout(() => $(row.node()).removeClass('highlight'), 2000);
                            }
                        } else {
                            alert(response.message);
                        }
                    }, "json");
                }
            };

            window.refreshTable = function () {
                table.ajax.reload(null, false);
            };
        });
    </script>
</body>
</html>
