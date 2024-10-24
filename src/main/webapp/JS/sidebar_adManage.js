$(document).ready(function() {

	// 取出固定不變的元素
	let searchBtn;
	let tableInitial;

	// 回傳的ads資料
	let ads = [];

	// 選取所有的 buttons
	const btns = document.querySelectorAll(".btn");
	btns.forEach((btn) => {
	  btn.addEventListener("click", {
	    once: true,
	  });
	});

	searchBtn = document.getElementById("search");
	
	loadTable();

	async function loadTable() {
		
		// 選取所有的 buttons
		const btns = document.querySelectorAll(".btn");
		btns.forEach((btn) => {
		  btn.addEventListener("click", {
		    once: true,
		  });
		});

		searchBtn = document.getElementById("search");

		// 銷毀舊的 DataTable 實例（如果存在）
		if ($.fn.DataTable.isDataTable('#adTable')) {
			$('#adtable').DataTable().destroy();
		}

		// 初始化 DataTable
		tableInitial = $("#adtable").DataTable({
			language: {
				url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json" // 使用繁體中文
			},
			autoWidth: true,
			info: true,
			processing: true, // 顯示資料加載中提示
			stateSave: true, // 保存使用者狀態
			lengthMenu: [10], // 每頁顯示 10 條數據
			dom: '<f<t><ip>>', // 自訂 DOM 結構
			searching: false,
			lengthChange: false,
			data: ads, // 設置數據
			columns: [
				// 設置欄位名稱及對應的數據
				{ title: "廣告編號", data: "adId" },
				{ title: "用戶編號", data: "userId" },
				{ title: "用戶姓名", data: "userName" },
				{ title: "房屋編號", data: "houseId" },
				{ title: "廣告類別", data: "adType" },
				{ title: "廣告數量", data: "quantity" },
				{ title: "付款狀態", data: "isPaid" },
				{
					title: "操作功能",
					data: null,
					render: function(data, type, row) {
						return `<button type="button" class="btn btn-warning btn-sm details-btn">詳細資料</button>`;
					},
				},
			],
		});


		// 0. 顯示所有廣告
		ads = await displayData({ ads, tableInitial, searchParams: {} });
		// debugging
		console.log(ads);

		// 1. 搜尋：傳送選取條件
		searchBtn.addEventListener("click", function() {
			searchData(ads, tableInitial);
		});

		// 當按下非修改、提交、取消按鈕時重置修改按鈕
		$(document).on('click', 'button', function(event) {
			let clickedButton = $(event.target);

			// 判斷點擊的是否為非修改按鈕
			if (!clickedButton.hasClass('modify') && !clickedButton.hasClass('submit') && !clickedButton.hasClass('cancel')) {
				resetModifyButtons();
			}
		});

		// 2. 確認廣告詳細資料
		// 3. 確認廣告詳細 > 修改 + 送出
		// 4. 確認廣告詳細 > 結束
		checkDetails(ads, tableInitial);

	} // loadTable

});

// functions

// 0. 顯示所有廣告
async function displayData({ ads, tableInitial, searchParams }) {
	if (!searchParams.input) searchParams.input = "";
	let dataToSend = [
		searchParams.condition || "adId",
		searchParams.input.trim() || "",
		searchParams.paid || "all",
	];

	// debugging
	console.log(dataToSend);

	try {
		let fetchedAds = await fetchData(
			"http://localhost:8080/rent_web/GetAdDataServlet.do",
			dataToSend
		);

		// 確保 data 是陣列
		ads = Array.isArray(fetchedAds) ? fetchedAds : [fetchedAds];

		// 更新 datatable
		tableInitial.clear().rows.add(ads).draw();

		return ads; // 返回更新後的 ads
	} catch (error) {
		console.log("fetch error:", error);
		return []; // 在錯誤情況下也要返回空陣列
	}
}

// 1. 搜尋資料
async function searchData(ads, tableInitial) {
	// 取得搜尋條件及值
	let searchParams = {
		condition: document.getElementById("search-condition").value,
		input: document.getElementById("search-inupt-box").value,
		paid: document.getElementById("paid-condition").value,
	};

	// 呼叫 displayData() 以顯示結果
	const resultAds = await displayData({ ads, tableInitial, searchParams });

	// debugging
	console.log(resultAds);
}

// 2.
// 查看詳細：顯示廣告詳細表
async function checkDetails(ads, tableInitial) {
	$("#adtable tbody").on("click", ".details-btn", async function() {
		try {
			// 顯示
			let AdDetailsBox = document.querySelector(".ad-details-box");
			AdDetailsBox.style.display = "block";

			// 取得該 row 的 ad id
			let rowAdId = tableInitial.row($(this).closest("tr")).data().adId;

			console.log("rowData:" + rowAdId);

			// 顯示廣告詳細表內容
			let retrievedAds = displayAdDetails(ads, tableInitial, rowAdId);

			// 點擊修改廣告內容 + 取消修改
			$(".button-box").off("click", ".modify.btn").on("click", ".modify.btn", async function() {
				let modifyBtn = this;
				modifyDetails(retrievedAds, tableInitial, modifyBtn);
			});

			// 點擊結束編輯 = 結束查看廣告詳細資料
			$(".button-box").on("click", ".leave.btn", async function() {
				endupChecking();
			});

			// 按下搜尋後，關閉廣告明細
			searchBtn.addEventListener("click", function() {
				AdDetailsBox.style.display = "none";
			});


		} catch (error) {
			console.log("fetch error:", error);
		}
	});
}

// 3.
// 修改資料：內容廣告修改
async function modifyDetails(ads, tableInitial, modifyBtn) {

	// 直接在函式內部獲取當前的廣告信息，確保是最新的值
	let adId = document.querySelector('.ad-id').textContent;
	let adTypeElement = document.querySelector(".ad-type");
	let quantityElement = document.querySelector(".ad-quantity");
	let subtotalElement = document.querySelector("#ad-details-table .ad-price-subtotal");
	let adDurationElement = document.querySelector("#ad-details-table .ad-duration");

	// 儲存原始值
	let originalParameters = {
		adType: adTypeElement.textContent,
		adDuration: adDurationElement.textContent,
		quantity: quantityElement.textContent,
	};

	// debugging
	console.log("ad id + originalParameters: " + adId + originalParameters.adType);

	// 修改操作邏輯
	adTypeElement.innerHTML =
		`<select name="ad-type-select" class="ad-type-selected">
        <option value="a" ${originalParameters.adType === "A廣告" ? "selected" : ""}>A廣告</option>
        <option value="b" ${originalParameters.adType === "B廣告" ? "selected" : ""}>B廣告</option>
    </select>`;
	quantityElement.innerHTML =
		`<input type="text" class="ad-quantity-input" 
    value="${originalParameters.quantity}" placeholder="請輸入數量"/>`;

	// 選取輸入欄位
	let adTypeSelected = document.querySelector(".ad-type-selected");
	let quantityInput = document.querySelector(".ad-quantity-input");

	// 更新廣告天數 & 小計
	let updateAdDurationAndSubtotal = function() {
		let duration = adTypeSelected.value === "a" ? "30天" : "60天";
		let unitPrice = adTypeSelected.value === "a" ? 10000 : 20000;

		adDurationElement.textContent = duration;
		subtotalElement.textContent = quantityInput.value * unitPrice;
	};

	adTypeSelected.addEventListener("change", function() {
		updateAdDurationAndSubtotal();
	});

	quantityInput.addEventListener("blur", function() {
		updateAdDurationAndSubtotal();
	});

	// 隱藏修改按鈕，顯示取消及提交按鈕
	modifyBtn.style.display = "none";

	let cancelBtn = document.querySelector(".cancel.btn");
	let submitBtn = document.querySelector(".submit.btn");
	cancelBtn.style.display = "inline";
	submitBtn.style.display = "inline";

	let buttons = { modifyBtn, cancelBtn, submitBtn };

	// 按下取消修改
	$(".button-box").off("click", ".cancel.btn").on("click", ".cancel.btn", async function() {
		cancelModification(buttons, originalParameters);
	});

	let modifiedParameters = {
		adId, // 帶入 ad id value
		adTypeSelected,
		adDurationElement,
		quantityInput,
	};

	// 按下確認送出
	$(".button-box").off("click", ".submit.btn").on("click", ".submit.btn", async function(event) {
		event.stopPropagation(); // 停止事件冒泡
		let button = $(this); // 取得被點擊的按鈕
		button.prop('disabled', true); // 禁用按鈕以防止重複點擊

		try {
			await submitModification(ads, tableInitial, buttons, modifiedParameters);
		} catch (error) {
			console.error("修改提交失敗:", error);
		} finally {
			button.prop('disabled', false); // 重新啟用按鈕
		}
	});
}

// 4.
// 取消修改
async function cancelModification(buttons, originalParameters) {
	document.querySelector("#ad-details-table .ad-type").textContent =
		originalParameters.adType;
	document.querySelector("#ad-details-table .ad-duration").textContent =
		originalParameters.adDuration;
	document.querySelector("#ad-details-table .ad-quantity").textContent =
		originalParameters.quantity;

	// 隱藏修改按鈕及送出按鈕
	buttons.cancelBtn.style.display = "none";
	buttons.submitBtn.style.display = "none";

	// 顯示編輯按鈕
	buttons.modifyBtn.style.display = "inline";
}

// 5.
// 確認修改資料：更新database及網頁呈現
async function submitModification(ads, tableInitial, buttons, modifiedParameters) {
	// debugging
	console.log(modifiedParameters);

	// 取得使用者輸入資料 -> 更新表單內容
	let dataToSend = [
		modifiedParameters.adId,
		modifiedParameters.adTypeSelected.value,
		"", // duration
		"", // unit price
		modifiedParameters.quantityInput.value,
		"", // created time
		"", // is paid
		"", // order id
		"", // paid date
		"" // expires at
	];

	// debugging
	console.log("data to send: " + dataToSend);

	// 傳更改資料給servlet，並獲取更新資料
	let fetchedDetails = await fetchData(
		"http://localhost:8080/rent_web/UpdateAdDataServelt.do",
		dataToSend
	);

	// debugging
	console.log(fetchedDetails);

	ads = Array.isArray(fetchedDetails) ? fetchedDetails : [fetchedDetails];

	// 更新 data table
	tableInitial.clear().rows.add(ads).draw();

	// 展示新資料到廣告詳細表上
	displayAdDetails(ads, tableInitial, modifiedParameters.adId);

	// 修改按鈕及送出按鈕消失
	buttons.cancelBtn.style.display = "none";
	buttons.submitBtn.style.display = "none";

	// 編輯按鈕出現
	buttons.modifyBtn.style.display = "inline";
}

// 6.
// 結束查看訂單：隱藏廣告詳細表
function endupChecking() {
	let AdDetailsBox = document.querySelector(".ad-details-box");
	AdDetailsBox.style.display = "none";
}

// 取得資料庫 data
async function fetchData(url, dataToSend) {
	try {
		let response = await fetch(url, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"Cache-Control": "no-cache", // 防止瀏覽器快取
				Pragma: "no-cache", // 適用於舊版 HTTP
			},
			body: JSON.stringify(dataToSend),
		});

		if (!response.ok) throw new Error("server response error");

		const data = await response.json();
		return data || []; // 返回空陣列以防止 undefined
	} catch (error) {
		console.log("fetch error:", error);
		return []; // 在錯誤情況下返回空陣列
	}
}

// 顯示廣告詳細表內容
async function displayAdDetails(ads, tableInitial, adId) {
	// 取得廣告詳細資料表的所有 td，放進 map
	let classNames = [
		"ad-id",
		"user-id",
		"user-name",
		"house-id",
		"ad-type",
		"ad-duration",
		"ad-price",
		"ad-quantity",
		"ad-price-subtotal",
		"created-date",
		"is-paid",
		"order-id",
		"paid-date",
		"expires-at",
	];

	let tds = classNames.map((className) => {
		return document.querySelectorAll(`.${className}`);
	});

	// debugging
	console.log(adId);
	let dataToSend = ["adDetails", adId];

	let fetchedDetails = await fetchData(
		"http://localhost:8080/rent_web/GetAdDataServlet.do",
		dataToSend
	);

	ads = Array.isArray(fetchedDetails) ? fetchedDetails : [fetchedDetails];

	// 更新 data table
	tableInitial.clear().rows.add(ads).draw();

	// debugging
	console.log(ads);

	if (fetchedDetails.length > 0) {
		// debugging
		console.log(fetchedDetails);

		// 更新廣告詳細表格
		let {
			adId,
			userId,
			userName,
			houseId,
			adType,
			adDuration,
			adPrice,
			quantity,
			createdAt,
			isPaid,
			orderId,
			paidDate,
			expiresAt,
		} = fetchedDetails[0];

		// 將資料更新到對應的 td
		const adDetails = [
			adId,
			userId,
			userName,
			houseId,
			adType,
			adDuration,
			adPrice,
			quantity,
			parseInt(adPrice) * parseInt(quantity),
			// 應該用db select再傳到前端，確保資料正確，以及用戶擅自修改
			createdAt,
			isPaid,
			orderId,
			paidDate,
			expiresAt,
		];

		tds.forEach((tdList, index) => {
			tdList.forEach((td) => (td.textContent = adDetails[index]));
		});

		return ads;
	}
}

// 重置修改按鈕狀態
function resetModifyButtons() {
	// 顯示 "修改" 按鈕，隱藏 "取消" 和 "提交" 按鈕
	$('.modify.btn').show();
	$('.cancel.btn').hide();
	$('.submit.btn').hide();
}
