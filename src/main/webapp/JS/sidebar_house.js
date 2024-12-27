$(document).ready(function () {
    let page = 1;
    const pageSize = 10;

    function loadTableData() {
        $.ajax({
            url: "HouseListInPage",
            type: "GET",
            data: {
                page: page,
                pageSize: pageSize,
                keyword: ""
            },
            success: function (response) {
                const tableBody = $("#tableBody");
                tableBody.empty();
                response.forEach(house => {
                    const row = `
                        <tr>
                            <td>${house.houseId}</td>
                            <td><img src="HouseImage?houseId=${house.houseId}" alt="House Photo" width="50"></td>
                            <td>${house.title}</td>
                            <td>${house.userId}</td>
                            <td>${house.userEmail}</td>
                            <td>${house.userName}</td>
                            <td>${house.address}</td>
                            <td>${house.price}</td>
                            <td>${house.status === 1 ? "可出租" : "不可出租"}</td>
                            <td>
                                <button class="btn btn-primary view-btn" data-id="${house.houseId}">查看</button>
                            </td>
                        </tr>`;
                    tableBody.append(row);
                });
                bindViewButtons();
            },
            error: function (xhr) {
                alert("無法加載數據：" + xhr.responseText);
            }
        });
    }

    function bindViewButtons() {
        $(".view-btn").on("click", function () {
            const houseId = $(this).data("id");
            $.ajax({
                url: "HouseDetails",
                type: "GET",
                data: { houseId },
                success: function (data) {
                    $("#houseDetails").html(JSON.stringify(data, null, 2)); // 根據需求設置詳細信息
                    $("#viewModal").modal("show");
                },
                error: function () {
                    alert("無法加載詳細信息");
                }
            });
        });
    }

    // 分頁邏輯
    $("#prevPage").on("click", function () {
        if (page > 1) {
            page--;
            loadTableData();
        }
    });

    $("#nextPage").on("click", function () {
        page++;
        loadTableData();
    });

    // 初始化
    loadTableData();
});