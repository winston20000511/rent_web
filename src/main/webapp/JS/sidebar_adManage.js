const baseUrl = "http://localhost:8080/rent_web";

// 初始化篩選條件
let filteredParams = {
	searchCondition: "all",
	paidCondition: "all",
	input: "",
};

let lastFilteredParams = { ...filteredParams };

$(document).ready(function() {
    initAdTable();
    document.getElementById("search").addEventListener("click", function() {
        updateFilterParams();
        if (JSON.stringify(filteredParams) !== JSON.stringify(lastFilteredParams)) {
            filterAds();
            lastFilteredParams = { ...filteredParams };
        }
    });
});

function initAdTable() {
    if ($.fn.DataTable.isDataTable('#adtable')) {
        $('#adtable').DataTable().clear().destroy();
    }

    // 初始請求，這裡會獲得篩選條件為初始的所有廣告資料
    $.ajax({
        url: `${baseUrl}/AdFilterServlet.do`,
        type: "post",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(filteredParams),
        success: function(data) {
            console.log("篩選出來的廣告: ", data);

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
	$("#adtable").off("click", ".details-btn").on("click", ".details-btn", function() {
		const adId = $(this).data("id");
		viewAdDetails(adId);
	});

	$("#adtable").off("click", ".delete-btn").on("click", ".delete-btn", function() {
		const adId = $(this).data("id");
		const isPaid = $(this).closest("tr").find(".paid-condition").text();

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

// ad detail modal按鈕
const modifyButton = document.querySelector('.modify');
const cancelButton = document.querySelector('.cancel');
const submitButton = document.querySelector('.submit');

function setupModalButtons() {
	// 編輯
	modifyButton.addEventListener('click', function() {
		openEditAd();
	});

	// 關閉
	closeButton.addEventListener('click', function() {
		closeModal();
		modifyButton.style.display = 'inline-block';
		cancelButton.style.display = 'none';
		submitButton.style.display = 'none';
	});
}

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

// 保留原本的adtype
let originalAdtype = "";
// 開啟編輯模式
function openEditAd() {
    console.log("開啟編輯模式");

    const adtypeCell = document.querySelector(".ad-type");
    const adtype = adtypeCell.textContent.trim();

    originalAdtype = adtype;
    adtypeCell.setAttribute("data-original-value", adtype);

	// 之後從資料庫撈
    const adtypeSelect = document.createElement("select");
    const options = [
        { value: "1", text: "30天", price: "NTD 300" },
        { value: "2", text: "60天", price: "NTD 550" },
    ];

    options.forEach(option => {
        const optionElement = document.createElement("option");
        optionElement.value = option.value;
        optionElement.textContent = option.text;

        if (adtype === option.text) {
            optionElement.selected = true;
        }

        adtypeSelect.appendChild(optionElement);
    });

    const priceDisplay = document.createElement("p");
    priceDisplay.classList.add("price-display");
    priceDisplay.textContent = `價格: ${options.find(option => option.text === adtype).price}`; // 預設顯示當前選擇的價格

    adtypeCell.innerHTML = "";
    adtypeCell.appendChild(adtypeSelect);
    adtypeCell.appendChild(priceDisplay);

    adtypeSelect.addEventListener('change', function () {
        const selectedOption = options.find(option => option.value === adtypeSelect.value);
        priceDisplay.textContent = `價格: ${selectedOption.price}`;
    });

    document.querySelector(".modify").style.display = "none";
    document.querySelector(".submit").style.display = "inline-block";
    document.querySelector(".cancel").style.display = "inline-block";

    document.querySelector(".cancel").addEventListener('click', function() {
        cancelEdit();
    });

    document.querySelector(".submit").addEventListener('click', function() {
        submitEdit(adtypeCell);
		filterAds();
    });
}


// 取消編輯，將 adtype 欄位恢復原來的文字
function cancelEdit() {
    const adtypeCell = document.querySelector(".ad-type");
    const originalValue = adtypeCell.getAttribute("data-original-value");

    // 恢復為原來的文字顯示
    adtypeCell.textContent = originalValue;

    document.querySelector(".modify").style.display = "inline-block";
    document.querySelector(".cancel").style.display = "none";
    document.querySelector(".submit").style.display = "none";
}

// 提交修改
function submitEdit() {
    const adtypeCell = document.querySelector(".ad-type");  // 確保重新獲取 adtypeCell
    if (!adtypeCell) {
        console.error("adtypeCell 未找到");
        return; // 若找不到 adtypeCell，則不執行
    }

    // 確保 select 元素存在
    const selectElement = adtypeCell.querySelector("select");
    if (!selectElement) {
        console.error("找不到 select 元素");
        return; // 若找不到 select，則不執行
    }

    const newAdtypeId = selectElement.value.trim();  // 取得選中的值

    // 更新顯示為對應的廣告類型文字
    adtypeCell.textContent = newAdtypeId === "1" ? "30天" : "60天";

    // 隱藏按鈕
    document.querySelector(".modify").style.display = "inline-block";
    document.querySelector(".cancel").style.display = "none";
    document.querySelector(".submit").style.display = "none";

    // 取得廣告 ID 並準備提交數據
    const adId = document.querySelector(".ad-id").textContent;
    const editData = {
        adId: adId,
        newAdtypeId: newAdtypeId
    };

    console.log("選擇要提交的廣告類型: ", editData);

    // 發送 AJAX 請求提交修改
    $.ajax({
        url: `${baseUrl}/EditAdServlet.do`,
        type: "post",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(editData),
        success: function(editedDetails) {
            if (editedDetails) {
                console.log("廣告更新結果: ", editedDetails);
                alert("廣告資料已成功更新");
                setupAdDetailsModal();
            } else {
                alert("修改失敗，請稍後再試");
            }
        },
        error: function(xhr, status, error) {
            console.error("修改請求失敗: ", status, error);
            alert("修改請求失敗，請稍後再試");
        }
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
			success: function(success) {
				console.log("刪除廣告結果: ", success);

				if (success) {
					alert("廣告已成功刪除");
					closeModal();
					filterAds();
				} else {
					alert("刪除廣告失敗: " + success);
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
	const searchConditionSelect = document.getElementById('search-condition');
	const searchInputBox = document.getElementById('search-input-box');

	toggleSearchInput(searchConditionSelect.value);

	searchConditionSelect.addEventListener('change', function() {
		toggleSearchInput(this.value);
	});

	function toggleSearchInput(selectedValue) {
		if (selectedValue === "all") {
			searchInputBox.style.display = "none";
		} else {
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
}

// 篩選廣告
function filterAds() {
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
        }
    });
}


