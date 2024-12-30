const baseUrl = "http://localhost:8080/rent_web";

// 初始化篩選條件
let filteredParams = {
	searchCondition: "all",
	orderStatus: "all",
	input: "",
};

$(document).ready(function() {
	initAdTable();
});

function initAdTable() {
    if ($.fn.DataTable.isDataTable('#adtable')) {
        $('#adtable').DataTable().clear().destroy();
    }
    $.ajax({
        url: `${baseUrl}/AdFilterServlet.do`,
        type: "post",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(filteredParams),
        success: function(data) {
            console.log("篩選出來的廣告': ", data);
            if (!data || data.length === 0) {
                console.warn("沒有符合篩選條件的廣告資料");
                return;
            }

            const table = $("#adtable").DataTable({
                language: {
                    url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json",
					info: "", 
                },
                processing: true,
                searching: false,
                pageLength: 20,
                dom: '<f><t><i><p>', //l:length不使用;控制一次展示幾筆
                columns: [
                    { data: "orderId" },
                    { data: "userId" },
                    { data: "userName" },
                    { data: "orderStatus" },
                    { data: "paidDate" },
                    { data: "actions" }  // 確保 actions 列存在
                ],
                columnDefs: [
                    { orderable: false, targets: [4, 5] } // 修正為 targets: [4, 5]
                ]
            });

            // 更新表格數據
            table.clear();
            $.each(data, function(index, ad) {
                table.row.add({
                    adId: ad.adId || "未提供",
                    userId: order.userId || "未提供",
                    userName: order.userName || "未提供",
                    orderStatus: order.orderStatus || "未提供",
                    paidDate: order.paidDate || "未提供",
                    actions: `<button class="details-btn btn btn-warning btn-sm" data-id="${order.orderId}">查看詳細</button>
                              <button class="delete-btn btn btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModelBox" data-id="${order.orderId}">取消訂單</button>`
                });
            });

            table.draw();
            disableCancelButtonsOnDraw();
            initDynamicButtonEvents();
        },
        error: function(xhr, status, error) {
            console.error("AJAX Error: ", status, error);
            console.error("Response Text: ", xhr.responseText);
        }
    });
}


// 禁用按鈕樣式
function disableCancelButton(orderId) {
	$(".delete-btn[data-id='" + orderId + "']").prop("disabled", true)
		.css({
			"background-color": "#ccc",
			"cursor": "not-allowed"
		});
}

// 禁用取消按鈕
function disableCancelButtonsOnDraw() {
	$("#ordertable .delete-btn").each(function() {
		const orderId = $(this).data("id");
		const orderStatus = $(this).closest("tr").find("td:eq(3)").text();

		if (orderStatus === "已取消") {
			disableCancelButton(orderId);
		}
	});
}

// 初始化動態按鈕
function initDynamicButtonEvents() {
	$("#ordertable").on("click", ".details-btn", function() {
		const orderId = $(this).data("id");
		viewOrderDetails(orderId);
	});

	$("#ordertable").on("click", ".delete-btn", function() {
		const orderId = $(this).data("id");
		const orderStatus = $(this).closest("tr").find(".order-status").text();

		if (orderStatus === "已取消") {
			disableButton('.cancel-order');
			return;
		}

		cancelOrder(orderId);
	});
}

const modal = document.getElementById("order-details-modal");
const closeButton = document.querySelector(".leave");

function openModal() {
	const modal = document.getElementById("order-details-modal");
	modal.style.display = "flex";
}

function closeModal() {
	const modal = document.getElementById("order-details-modal");
	modal.style.display = "none";
}

// 點擊結束按鈕關閉彈窗
closeButton.addEventListener("click", closeModal);


function setupModalButtons(orderId) {
	const cancelOrderButton = document.querySelector('.cancel-order');
	cancelOrderButton.addEventListener('click', function() {
		cancelOrder(orderId);
	});

	// 為結束按鈕添加事件監聽
	const leaveButton = document.querySelector('.leave');
	leaveButton.addEventListener('click', function() {
		closeModal();
	});
}

// 查看訂單詳細
const orderDetailsBox = document.getElementById("order-details-box");
function viewOrderDetails(orderId) {
	const param = {
		"orderId": orderId,
	};

	console.log("查看訂單詳細 ID: ", orderId);

	$.ajax({
		url: `${baseUrl}/OrderCheckDetailsServlet.do`,
		type: "post",
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify(param),
		success: function(orderDetails) {
			console.log("訂單詳細資料: ", orderDetails);

			// 填充訂單資料
			setupOrderDetailsModal(orderDetails);
			setupAdInfoForOrderDetailsModal(orderDetails);

			// 設置取消按鈕
			setupModalButtons(orderId);
			if (orderDetails.orderStatus === "已取消") {
				disableButton(".cancel-order[data-id='" + orderId + "']");
			} else {
				// 若訂單狀態不是已取消，啟用取消按鈕
				enableButton(".cancel-order[data-id='" + orderId + "']");
			}

			// 打開彈窗
			openModal();
		},
	});
}

// 禁用按鈕
function disableButton(buttonSelector) {
	const button = document.querySelector(buttonSelector);

	button.disabled = true;

	button.style.backgroundColor = "#ccc";
	button.style.cursor = "not-allowed";
}

// 啟用按鈕
function enableButton(buttonSelector) {
	$(buttonSelector).prop("disabled", false).css({
		"background-color": "#343a40",
		"cursor": ""
	});
}

// 取消訂單
function cancelOrder(orderId) {
	const param = {
		"orderId": orderId,
	};

	console.log("取消訂單 ID: ", orderId);

	const userConfirm = confirm("確定要取消訂單嗎？")
	if (userConfirm) {
		$.ajax({
			url: `${baseUrl}/CancelOrderServlet.do`,
			type: "post",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify(param),
			success: function(response) {
				console.log("取消訂單結果: ", response);

				if (response.orderStatus === "已取消") {
					alert("訂單已成功取消");
					disableButton(".delete-btn[data-id='" + orderId + "']");
					disableButton(".cancel-order[data-id='" + orderId + "']");
					setupOrderDetailsModal(response);
					setupAdInfoForOrderDetailsModal(response);
				} else {
					alert("取消訂單失敗: " + response.message);
				}
			},
			error: function(error) {
				console.log("取消訂單請求失敗: ", error);
				alert("取消訂單請求失敗，請稍後再試");
			}
		});
	} else {
		alert("訂單尚未取消");
	}
}

// 設置 order detail modal
function setupOrderDetailsModal(orderDetails) {
	const orderIdElement = document.querySelector(".merchantTradNo");
	orderIdElement.textContent = orderDetails.orderId;

	const userIdElement = document.querySelector(".user-id");
	userIdElement.textContent = orderDetails.userId;

	const userNameElement = document.querySelector(".user-name");
	userNameElement.textContent = orderDetails.userName;

	const paymentMethodElement = document.querySelector(".payment-method");
	paymentMethodElement.textContent = orderDetails.paymentMethod;

	const orderStatusElement = document.querySelector(".order-status");
	orderStatusElement.textContent = orderDetails.orderStatus;

	const paidDateElement = document.querySelector(".paid-date");
	paidDateElement.textContent = orderDetails.paidDate.substring(0, 10);

	document.querySelector(".cancel-order").setAttribute("data-id", orderDetails.orderId);
}

function setupAdInfoForOrderDetailsModal(orderDetails) {
	const tbody = document.getElementById("ad-details-tbody");
	const totalAmountTr = tbody.querySelector(".total-amount");

	if (!tbody || !totalAmountTr) {
		console.error("未找到 tbody 或 .total-amount 的 <tr>");
		return;
	}

	// 清除舊的詳細資料行
	const rowsToRemove = Array.from(tbody.querySelectorAll("tr:not(.total-amount)"));
	rowsToRemove.forEach(row => row.remove());

	// 確保所有屬性數組長度一致
	const rowCount = orderDetails.adIds.length;

	for (let i = 0; i < rowCount; i++) {
		const tr = document.createElement("tr");

		// 插入指定屬性
		const houseIdTd = document.createElement("td");
		houseIdTd.textContent = orderDetails.houseIds[i] || "";
		tr.appendChild(houseIdTd);

		const houseTitleTd = document.createElement("td");
		houseTitleTd.textContent = orderDetails.houseTitles[i] || "";
		tr.appendChild(houseTitleTd);

		const adIdTd = document.createElement("td");
		adIdTd.textContent = orderDetails.adIds[i] || "";
		tr.appendChild(adIdTd);

		const adNameTd = document.createElement("td");
		adNameTd.textContent = orderDetails.adNames[i].replace("?", "天") || "";
		tr.appendChild(adNameTd);

		const adOriginalPriceTd = document.createElement("td");
		adOriginalPriceTd.textContent = orderDetails.adOriginalPrices[i] || "";
		tr.appendChild(adOriginalPriceTd);

		const adDiscountedTd = document.createElement("td");
		const discount = (orderDetails.adOriginalPrices[i] - orderDetails.adDisCountedPrices[i]);
		console.log("折扣優惠: " + discount);
		adDiscountedTd.textContent = discount || "";
		tr.appendChild(adDiscountedTd);

		const adPeriodTd = document.createElement("td");
		adPeriodTd.textContent = orderDetails.adPeriods[i] || "";
		tr.appendChild(adPeriodTd);
		
		const adDiscountedPriceTd = document.createElement("td");
		adDiscountedPriceTd.textContent = orderDetails.adDisCountedPrices[i] || "";
		tr.appendChild(adDiscountedPriceTd);
		
		const totalAmountThElement = document.querySelector(".total");
		totalAmountThElement.textContent = orderDetails.totalAmount;

		tbody.insertBefore(tr, totalAmountTr);
	}
}



// 開關篩選欄位
const searchInput = document.getElementById("search-input-box");
function toggleSearchInput() {
	if (searchInput.style.display === "none" || searchInput.style.display === "") {
		searchInput.style.display = "block";
	} else {
		searchInput.style.display = "none";
	}
}

document.getElementById("search").addEventListener("click", function() {
	// 更新篩選條件
	updateFilterParams();
	console.log("更新後的篩選條件: ", filteredParams);
	filterOrders();
});

// 監聽使用者選擇的條件
const searchConditionSelection = document.getElementById("search-condition");
searchConditionSelection.addEventListener("change", function() {
	console.log("searchConditionSelection.value: ", searchConditionSelection.value);

	if (searchConditionSelection.value === "merchantTradNo") {
		searchInput.style.display = "block";
		searchInput.placeholder = "請輸入訂單編號";
	}

	if (searchConditionSelection.value === "userId") {
		searchInput.style.display = "block";
		searchInput.placeholder = "請輸入屋主編號";
	}

	if (searchConditionSelection.value === "all") {
		searchInput.placeholder = "";
		searchInput.style.display = "none";
	}

	updateFilterParams();
});

const orderStatusSelection = document.getElementById("order-status");
orderStatusSelection.addEventListener("change", function() {
	updateFilterParams();
});

function updateFilterParams() {
	filteredParams.searchCondition = document.getElementById("search-condition").value;
	filteredParams.orderStatus = document.getElementById("order-status").value;
	filteredParams.input = document.getElementById("search-input-box").value.trim();
};


async function filterOrders() {
	console.log("執行篩選參數: ", filteredParams);

	$.ajax({
		url: `${baseUrl}/OrderFilterServlet.do`,
		type: "post",
		dataType: "json",
		contentType: "application/json",
		xhrFields: { withCredentials: true },
		data: JSON.stringify(filteredParams),
		success: function(data) {

			console.log("篩選出來的訂單: ", data);

			// 更新表格
			const table = $("#ordertable").DataTable();
			table.clear();

			if (!data || data.length === 0) {
				console.warn("沒有符合篩選條件的訂單資料");
				table.draw();
				return;
			}

			$.each(data, function(index, order) {
				table.row.add({
					orderId: order.orderId || "未提供",
					userId: order.userId || "未提供",
					userName: order.userName || "未提供",
					orderStatus: order.orderStatus || "未提供",
					paidDate: order.paidDate || "未提供",
					actions: `<button class="details-btn btn btn-warning btn-sm" data-id="${order.orderId}">查看詳細</button>
						<button class="delete-btn btn btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModelBox" data-id="${order.orderId}">取消訂單</button>`
				});

			});

			table.draw();
			disableCancelButtonsOnDraw();
			initDynamicButtonEvents();
		},
		error: function(xhr, status, error) {
			console.error("AJAX Error: ", status, error);
			console.error("Response Text: ", xhr.responseText);
		},
	});
};


