const baseUrl = "http://localhost:8080/rent_web";

// 初始化篩選條件
let filteredParams = {
	searchCondition: "all",
	paidCondition: "all",
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
					{ data: "adId" },
					{ data: "userId" },
					{ data: "userName" },
					{ data: "houseId" },
					{ data: "adtypeName" },
					{ data: "isPaid" },
					{ data: "actions" }
				],
				columnDefs: [
					{ adable: false, targets: [4, 6] }
				]
			});

			// 更新表格數據
			table.clear();
			$.each(data, function(index, ad) {
				table.row.add({
					adId: ad.adId || "未提供",
					userId: ad.userId || "未提供",
					userName: ad.userName || "未提供",
					houseId: ad.houseId || "未提供",
					adtypeName: ad.adtypeName.replace("?", "天") || "未提供",
					isPaid: ad.isPaid || "未提供",
					actions: `<button class="details-btn btn btn-warning btn-sm" data-id="${ad.adId}">查看詳細</button>
                              <button class="delete-btn btn btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModelBox" data-id="${ad.adId}">刪除廣告</button>`
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
function disableCancelButton(adId) {
	$(".delete-btn[data-id='" + adId + "']").prop("disabled", true)
		.css({
			"background-color": "#ccc",
			"cursor": "not-allowed"
		});
}

// 禁用刪除按鈕
function disableCancelButtonsOnDraw() {
	$("#adtable .delete-btn").each(function() {
		const adId = $(this).data("id");
		const isPaid = $(this).closest("tr").find("td:eq(5)").text();

		if (isPaid === "已付款") {
			disableCancelButton(adId);
		}
	});
}

// 初始化動態按鈕
function initDynamicButtonEvents() {
	$("#adtable").on("click", ".details-btn", function() {
		const adId = $(this).data("id");
		viewAdDetails(adId);
	});

	$("#adtable").on("click", ".delete-btn", function() {
		const adId = $(this).data("id");
		const isPaid = $(this).closest("tr").find("td:eq(5)").text();

		if (isPaid === "已付款") {
			disableButton('.modify');
			return;
		}

		cancelAd(adId);
	});
}


const modal = document.getElementById("ad-details-modal");
const closeButton = document.querySelector(".leave");

function openModal() {
	const modal = document.getElementById("ad-details-modal");
	modal.style.display = "flex";
}

function closeModal() {
	const modal = document.getElementById("ad-details-modal");
	modal.style.display = "none";
}

// 點擊結束按鈕關閉彈窗
closeButton.addEventListener("click", closeModal);


function setupModalButtons(adId) {
	const cancelAdButton = document.querySelector('.modify');
	cancelAdButton.addEventListener('click', function() {
		modifyAd(adId);
	});

	// 為結束按鈕添加事件監聽
	const leaveButton = document.querySelector('.leave');
	leaveButton.addEventListener('click', function() {
		closeModal();
	});
}

// 查看廣告詳細
// 查看廣告詳細
function viewAdDetails(adId) {
	const param = {
		"adId": adId,
	};

	console.log("查看廣告詳細 ID: ", adId);

	$.ajax({
		url: `${baseUrl}/AdCheckDetailsServlet.do`,
		type: "post",
		dataType: "json",
		contentType: "application/json",
		data: JSON.stringify(param),
		success: function(adDetails) {
			console.log("廣告詳細資料: ", adDetails);

			// 填充廣告資料
			setupAdDetailsModal(adDetails);

			// 設置編輯按鈕
			setupModalButtons(adId);
			if (adDetails.isPaid === "已付款") {
				disableButton(".modify[data-id='" + adId + "']");
			} else {
				enableButton(".modify[data-id='" + adId + "']");
			}

			// 打開彈窗
			openModal();
		},
	});
}

// 設置廣告詳情彈窗
function setupAdDetailsModal(adDetails) {
	document.querySelector(".ad-id").textContent = adDetails.adId;
	document.querySelector(".user-id").textContent = adDetails.userId;
	document.querySelector(".user-name").textContent = adDetails.userName;
	document.querySelector(".house-id").textContent = adDetails.houseId;
	document.querySelector(".ad-type").textContent = adDetails.adtypeName.replace("?", "天");
	document.querySelector(".is-paid").textContent = adDetails.isPaid;
	document.querySelector(".order-id").textContent = adDetails.orderId || "無";
	document.querySelector(".paid-date").textContent = adDetails.paidDate ? adDetails.paidDate.substring(0, 10) : "無";
	document.querySelector(".expires-at").textContent = adDetails.expiryDate ? adDetails.expiryDate.substring(0, 10) : "無";

	// 設置 modify 按鈕的 data-id
	document.querySelector(".modify").setAttribute("data-id", adDetails.adId);
}

function setupModalButtons(adId) {
	const modifyButton = document.querySelector('.modify');
	modifyButton.addEventListener('click', function() {
		modifyAd(adId);
	});

	const leaveButton = document.querySelector('.leave');
	leaveButton.addEventListener('click', function() {
		closeModal();
	});
}

function modifyAd(adId) {
	// 假設我們會在這裡顯示一個表單，讓用戶修改廣告的資料
	console.log("開始修改廣告 ID: ", adId);
	// 例如，可以開啟一個表單或彈窗，允許用戶輸入新的廣告資訊
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

let currentAdTypeId;  
// 編輯廣告




// 刪除廣告
function cancelAd(adId) {
	const param = {
		"adId": adId,
	};

	console.log("刪除廣告 ID: ", adId);

	const userConfirm = confirm("確定要刪除廣告嗎？")
	if (userConfirm) {
		$.ajax({
			url: `${baseUrl}/CancelAdServlet.do`,
			type: "post",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify(param),
			success: function(result) {
				console.log("刪除廣告結果: ", result);

				if (result === "已刪除") {
					alert("廣告已成功刪除");
					closeModal();
					filterAds();
				} else {
					alert("刪除廣告失敗: " + result);
				}
			},
			error: function(error) {
				console.log("刪除廣告請求失敗: ", error);
				alert("刪除廣告請求失敗，請稍後再試");
			}
		});
	} else {
		alert("廣告尚未刪除");
	}
}

// 設置 ad detail modal
function setupAdDetailsModal(adDetails) {
	const adIdElement = document.querySelector(".ad-id");
	adIdElement.textContent = adDetails.adId;

	const userIdElement = document.querySelector(".user-id");
	userIdElement.textContent = adDetails.userId;

	const userNameElement = document.querySelector(".user-name");
	userNameElement.textContent = adDetails.userName;

	const houseIdElement = document.querySelector(".house-id");
	houseIdElement.textContent = adDetails.houseId;

	const adtypeElement = document.querySelector(".ad-type");
	adtypeElement.textContent = adDetails.adtypeName.replace("?", "天");

	const isPaidElement = document.querySelector(".is-paid");
	isPaidElement.textContent = adDetails.isPaid;

	const isOrderIdElement = document.querySelector(".order-id");
	isOrderIdElement.textContent = adDetails.orderId || "無";

	const PaidDateElement = document.querySelector(".paid-date");
	PaidDateElement.textContent =
		adDetails.paidDate ? adDetails.paidDate.substring(0, 10) : "無";

	const expiresAtElement = document.querySelector(".expires-at");
	expiresAtElement.textContent =
		adDetails.expiryDate ? adDetails.expiryDate.substring(0, 10) : "無";

	document.querySelector(".modify").setAttribute("data-id", adDetails.adId);
}

document.getElementById("search").addEventListener("click", function() {
	// 更新篩選條件
	updateFilterParams();
	console.log("更新後的篩選條件: ", filteredParams);
	filterAds();
});

document.addEventListener('DOMContentLoaded', function() {
	// 監聽選擇條件的變化
	const searchConditionSelect = document.getElementById('search-condition');
	const searchInputBox = document.getElementById('search-input-box');

	// 初始狀態
	toggleSearchInput(searchConditionSelect.value);

	// 當選擇條件變化時，觸發顯示或隱藏搜尋框
	searchConditionSelect.addEventListener('change', function() {
		toggleSearchInput(this.value);
	});

	// 根據選擇的條件顯示或隱藏搜尋框
	function toggleSearchInput(selectedValue) {
		if (selectedValue === "all") {
			// 若選擇的是 "所有訂單"，隱藏搜尋框
			searchInputBox.style.display = "none";
		} else {
			// 其他情況顯示搜尋框
			searchInputBox.style.display = "inline-block";

			if (selectedValue === "adId") {
				searchInputBox.placeholder = "請輸入廣告編號";
			}

			if (selectedValue === "userId") {
				searchInputBox.placeholder = "請輸入屋主編號";
			}

			if (selectedValue === "houseId") {
				searchInputBox.placeholder = "請輸入房屋編號";
			}

		}
	}
});


const adStatusSelection = document.getElementById("paid-condition");
adStatusSelection.addEventListener("change", function() {
	updateFilterParams();
});

function updateFilterParams() {
	filteredParams.searchCondition = document.getElementById("search-condition").value;
	filteredParams.paidCondition = document.getElementById("paid-condition").value;
	filteredParams.input = document.getElementById("search-input-box").value.trim();
};


async function filterAds() {
	console.log("執行篩選參數: ", filteredParams);

	$.ajax({
		url: `${baseUrl}/AdFilterServlet.do`,
		type: "post",
		dataType: "json",
		contentType: "application/json",
		xhrFields: { withCredentials: true },
		data: JSON.stringify(filteredParams),
		success: function(data) {

			console.log("篩選出來的廣告: ", data);

			// 更新表格
			const table = $("#adtable").DataTable();
			table.clear();

			if (!data || data.length === 0) {
				console.warn("沒有符合篩選條件的廣告資料");
				table.draw();
				return;
			}

			table.clear();
			$.each(data, function(index, ad) {
				table.row.add({
					adId: ad.adId || "未提供",
					userId: ad.userId || "未提供",
					userName: ad.userName || "未提供",
					houseId: ad.houseId || "未提供",
					adtypeName: ad.adtypeName.replace("?", "天") || "未提供",
					isPaid: ad.isPaid || "未提供",
					actions: `<button class="details-btn btn btn-warning btn-sm" data-id="${ad.adId}">查看詳細</button>
			                              <button class="delete-btn btn btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModelBox" data-id="${ad.adId}">刪除廣告</button>`
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


