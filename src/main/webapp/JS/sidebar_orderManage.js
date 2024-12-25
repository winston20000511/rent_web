"use strict";

// 取出固定不變的元素
let searchBtn;
let tableInitial;

$(document).ready(function() {

	// 回傳的ads資料
	let orders = [];

	// 選取所有的 buttons
	const btns = document.querySelectorAll(".btn");
	btns.forEach((btn) => {
		btn.addEventListener("click", {
			once: true,
		});
	});

	searchBtn = document.getElementById("search");

	loadTable();

	// async: 頁面載入時，確保在獲取所有資料之前不會繼續執行其他程式碼
	async function loadTable() {

		// 銷毀舊的 DataTable 實例（如果存在）
		if ($.fn.DataTable.isDataTable('#orderTable')) {
			$('#ordertable').DataTable().destroy();
		}

		// 初始化 DataTable，將所有設置合併
		tableInitial = $("#ordertable").DataTable({
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
			data: orders, // 設置數據
			columns: [
				// 設置欄位名稱及對應的數據
				{ title: "訂單編號", data: "merchantTradNo" },
				{ title: "用戶編號", data: "userId" },
				{ title: "訂單狀態", data: "orderStatus" },
				{ title: "下單時間", data: "merchantTradDate" },
				{
					title: "操作功能",
					data: null,
					render: function(data, type, row) {
						return `<button type="button" class="btn btn-warning btn-sm details-btn">詳細資料</button>`;
					},
				},
			],
		});

		// 0. 顯示所有訂單
		orders = await displayData({ orders, tableInitial, searchParams: {} });
		// debugging
		console.log(orders);

		// 1. 搜尋：傳送選取條件
		searchBtn.addEventListener("click", function() {
			searchData(orders, tableInitial);
		});

		// 2. 確認訂單詳細資料
		// 3. 確認訂單詳細 > 修改 + 送出
		// 4. 確認訂單詳細 > 結束
		checkDetails(orders, tableInitial);

	} // loadTable

});

// functions

// 0. 顯示所有訂單
async function displayData({ orders, tableInitial, searchParams }) {
	if (!searchParams.input) searchParams.input = "";
	let dataToSend = [
		"search",
		searchParams.condition || "all",
		searchParams.orderStatus || "all",
		searchParams.input.trim() || "",
	];

	// debugging
	console.log("display all data to send: " + dataToSend);

	try {
		let fetchedOrders = await fetchData(
			"http://localhost:8080/rent_web/OrderDataOperationServlet.do",
			dataToSend
		);

		orders = Array.isArray(fetchedOrders) ? fetchedOrders : [fetchedOrders];

		tableInitial.clear().rows.add(orders).draw();

		return orders;
	} catch (error) {
		console.log("fetch error:", error);
		return [];
	}
}

// 1. 搜尋資料
async function searchData(orders, tableInitial) {
	// 取得搜尋條件及值
	let searchParams = {
		condition: document.getElementById("search-condition").value,
		orderStatus: document.getElementById("order-status").value,
		input: document.getElementById("search-inupt-box").value,
	};

	// 呼叫 displayData() 以顯示結果
	const resultAds = await displayData({ orders, tableInitial, searchParams });

	// debugging
	console.log("search data: " + resultAds);
}

// 2.
// 查看詳細：顯示訂單詳細表
async function checkDetails(orders, tableInitial) {
	$("#ordertable tbody").on("click", ".details-btn", async function() {
		try {
			// 顯示
			let orderDetailsBox = document.querySelector(".order-details-box");
			orderDetailsBox.style.display = "block";

			// 取得該 row 的 order id
			let rowMerchantTradNo = tableInitial.row($(this).closest("tr")).data().merchantTradNo;
			// 取得該 row 的 order status
			let rowOrderStatus = tableInitial.row($(this).closest("tr")).data().orderStatus;

			// 顯示訂單詳細表內容
			displayOrderDetails(orders, tableInitial, rowMerchantTradNo);
			console.log(rowOrderStatus)
			
			// 取消訂單
			// 已取消的不能取消
			if (rowOrderStatus === "已取消") {
				console.log("inner if: " + rowOrderStatus);
				document.querySelector(".cancel-order.btn").innerHTML = "訂單已取消";
				document.querySelector(".cancel-order.btn").disabled = true;
			} else if(rowOrderStatus === "一般訂單") {
				console.log("inner if: " + rowOrderStatus);
				document.querySelector(".cancel-order.btn").innerHTML = "取消訂單";
				document.querySelector(".cancel-order.btn").disabled = false;
				$(".button-box").off("click", ".cancel-order.btn").on("click", ".cancel-order.btn", async function() {
					if (confirm("是否確定要取消訂單？")) {
						let canceledOrder = await cancelOrder(rowMerchantTradNo);
						alert("訂單已取消");
						// 同步更新 data table
						tableInitial.clear().rows.add(canceledOrder).draw();
						// 印出新 order details
						displayOrderDetails(canceledOrder, tableInitial, rowMerchantTradNo);
						// 鎖定取消按鈕
						document.querySelector(".cancel-order.btn").innerHTML = "訂單已取消";
						document.querySelector(".cancel-order.btn").disabled = true;
					} else {
						alert("訂單未取消");
					}
				});
			}

			// 點擊結束編輯 = 結束查看訂單詳細資料
			$(".button-box").on("click", ".leave.btn", async function() {
				endupChecking();
			});

			// 按下搜尋後，關閉訂單明細
			searchBtn.addEventListener("click", function() {
				endupChecking();
			});
		} catch (error) {
			console.log("fetch error:", error);
		}
	});
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

// 顯示訂單詳細內容
async function displayOrderDetails(orders, tableInitial, merchantTradNo) {
	// 取得訂單詳細資料表的所有 td，放進 map
	let classNames = [
		"merchantTradNo",
		"merchantTradDate",
		"user-id",
		"user-name",
		"choose-payment",
		"order-status",
	];

	console.log("class names: " + classNames);

	let tds = classNames.map((className) => {
		return document.querySelectorAll(`#order-details .${className}`);
	});

	// debugging
	let dataToSend = ["orderDetails", merchantTradNo];

	let fetchedData = await fetchData(
		"http://localhost:8080/rent_web/OrderDataOperationServlet.do",
		dataToSend
	);

	// 解析json物件
	let { userId, merchantTradDate, choosePayment, orderStatus, totalAmount } = fetchedData;
	let { ads } = fetchedData;

	// 確保 data 是陣列 + 取得訂單表格
	orders = { userId, merchantTradNo, merchantTradDate, choosePayment, orderStatus, totalAmount };

	// debugging	
	console.log("fetch order details: " + JSON.stringify(orders));
	console.log("fetch ads details: " + JSON.stringify(ads));

	// 更新 data table
	tableInitial.clear().rows.add([orders]).draw();

	if (Object.keys(orders).length > 0) {

		// 將資料更新到對應的 td
		let orderDetails = [
			orders.merchantTradNo,
			orders.merchantTradDate,
			userId,
			ads[0].userName,
			orders.choosePayment,
			orders.orderStatus
		];

		console.log("order details: " + orderDetails);

		tds.forEach((tdList, index) => {
			tdList.forEach((td) => (td.textContent = orderDetails[index]));
		});

		// 動態新增廣告詳細資料
		addAdDetails(ads); // 整合到這裡，將取得的廣告資料加入
	}

	return orders;
}

// 動態加上廣告詳細資料
function addAdDetails(adDetails) {
	console.log("add ad details: ", adDetails);

	const adDetailsTbody = document.querySelector(
		"#ad-details .ad-details-tbody"
	);

	adDetailsTbody.innerHTML = "";

	// 將每個廣告資料動態加入到表格
	adDetails.forEach((ad) => {
		const tr = document.createElement("tr");

		tr.innerHTML = `
      <td>${ad.houseId}</td>
      <td>${ad.adId}</td>
      <td>${ad.adType}</td>
      <td>${ad.adDuration}天</td>
      <td>${ad.adPrice}</td>
      <td>${ad.quantity}</td>
      <td>${ad.adPrice * ad.quantity}</td>
    `;

		adDetailsTbody.appendChild(tr);

		// 將 tr 加入到 tbody 中
		adDetailsTbody.appendChild(tr);
	});

	// 動態插入總計欄位
	const totalRow = document.createElement("tr");
	totalRow.classList.add("total-amount");
	totalRow.innerHTML = `
      <th colspan="5">總計</th>
      <td>${adDetails.reduce(
		(sum, ad) => sum + ad.adPrice * ad.quantity,
		0
	)}</td>
    `;

	adDetailsTbody.appendChild(totalRow);
}

function endupChecking() {
	let OrderDetailsBox = document.querySelector(".order-details-box");
	OrderDetailsBox.style.display = "none";
}

// 取消訂單
async function cancelOrder(rowMerchantTradNo) {
	let dataToSend = ["cancelOrder", rowMerchantTradNo];
	console.log("cancel order data to send: ", dataToSend);

	let fetchedData = await fetchData(
		"http://localhost:8080/rent_web/OrderDataOperationServlet.do",
		dataToSend
	);

	console.log("fetch data canceled order: ", JSON.stringify(fetchedData));

	// 確保取得並回傳陣列
	let canceledOrderData = Array.isArray(fetchedData)
		? fetchedData
		: [fetchedData];

	return canceledOrderData;
}
