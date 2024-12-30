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