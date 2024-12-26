$(document).ready(function () {
    // 加载表格数据
    function loadTableData() {
        $.ajax({
            url: "GetPaginatedHouseList",
            type: "GET",
            data: {
                page: 1,
                pageSize: 10,
                keyword: "" // 可以添加搜索关键字
            },
            success: function (response) {
                let tableBody = $("#tableBody");
                tableBody.empty();
                response.forEach(house => {
                    let row = `
                        <tr>
                            <td>${house.houseId}</td>
                            <td><img src="GetSmallestImageByHouseId?houseId=${house.houseId}" alt="House Photo" width="50"></td>
                            <td>${house.title}</td>
                            <td>${house.userId}</td>
                            <td>${house.userEmail}</td>
                            <td>${house.userName}</td>
                            <td>${house.address}</td>
                            <td>${house.price}</td>
                            <td>${house.status === 1 ? "Available" : "Unavailable"}</td>
                            <td>
                                <button class="btn btn-primary view-btn" data-id="${house.houseId}">查看</button>
                            </td>
                        </tr>`;
                    tableBody.append(row);
                });

                // 绑定查看按钮事件
                bindViewButtons();
            },
            error: function () {
                alert("无法加载数据");
            }
        });
    }

    // 查看按钮逻辑
    function bindViewButtons() {
        $(".view-btn").on("click", function () {
            const houseId = $(this).data("id");
            $.ajax({
                url: "GetHouseDetailsById",
                type: "GET",
                data: { houseId },
                success: function (data) {
                    // 填充布尔字段图标
                    updateBooleanIcons(data);

                    // 显示查看模态框
                    $("#viewModal").modal("show");

                    // 绑定编辑按钮
                    $("#editButton").off("click").on("click", function () {
                        openEditModal(data);
                    });
                },
                error: function () {
                    alert("无法加载详细信息");
                }
            });
        });
    }

    // 更新布尔字段图标
    function updateBooleanIcons(data) {
        const booleanFields = [
            "pet", "parkingSpace", "elevator", "balcony", "shortTerm",
            "cooking", "waterDispenser", "managementFee", "washingMachine", "airConditioner"
        ];

        booleanFields.forEach(field => {
            const iconElement = $(`#${field}-icon`);
            if (data[field]) {
                iconElement.css("background-image", `url('/webapp/imags/house-on/${field}.ico')`);
                iconElement.removeClass("boolean-false");
            } else {
                iconElement.css("background-image", "none");
                iconElement.addClass("boolean-false");
            }
        });
    }

    // 打开编辑模态框
    function openEditModal(data) {
        $("#viewModal").modal("hide");
        Object.keys(data).forEach(key => {
            const field = $(`#editForm [name=${key}]`);
            if (field.is(":checkbox")) {
                field.prop("checked", data[key]);
            } else if (field.is(":input")) {
                field.val(data[key]);
            }
        });
        $("#editModal").modal("show");
    }

    // 提交编辑表单
    $("#editForm").on("submit", function (e) {
        e.preventDefault();
        const formData = $(this).serialize();
        $.ajax({
            url: "UpdateHouseDetails",
            type: "POST",
            data: formData,
            success: function () {
                alert("更新成功");
                $("#editModal").modal("hide");
                loadTableData();
            },
            error: function () {
                alert("更新失败");
            }
        });
    });

    // 初始化加载数据
    loadTableData();
});
