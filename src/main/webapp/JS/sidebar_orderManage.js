const baseUrl = "http://localhost:8080/rent_web";

// 初始化篩選條件
let filteredParams = {
  searchCondition: "all",
  orderStatus: "all",
  input: "",
};

initOrderTable();

function initOrderTable() {
  $.ajax({
    url: `${baseUrl}/OrderFilterServlet.do`,
    type: "post",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(filteredParams),
    success: function (data) {
      console.log("篩選出來的訂單: ", data);
      if (!data || data.length === 0) {
        console.warn("沒有符合篩選條件的訂單資料");
        return;
      }

      const table = $("#ordertable").DataTable({
        language: {
          url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json",
        },
        processing: true,
        dom: "<t><i><p>",
        searching: false,
        columns: [
          { data: "orderId" },
          { data: "userId" },
          { data: "userName" },
          { data: "orderStatus" },
          { data: "paidDate" },
          { data: "actions" },
        ],
      });

      // 更新表格數據
      table.clear();
      $.each(data, function (index, order) {
        table.row.add({
          orderId: order.orderId || "未提供",
          userId: order.userId || "未提供",
          userName: order.userName || "未提供",
          orderStatus: order.orderStatus || "未提供",
          paidDate: order.paidDate || "未提供",
          actions: `<button class="details-btn btn btn-warning btn-sm" data-id="${order.orderId}">查看詳細</button>
            <button class="delete-btn btn btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModelBox" data-id="${order.orderId}">取消訂單</button>`,
        });
      });

      table.draw();

      initDynamicButtonEvents();
    },
    error: function (xhr, status, error) {
      console.error("AJAX Error: ", status, error);
      console.error("Response Text: ", xhr.responseText);
    },
  });
}

// 初始化動態按鈕
function initDynamicButtonEvents(){
  // 查看詳細
  $("#ordertable").on("click", ".details-btn", function(){
    const orderId = $(this).data("id");
    viewOrderDetails(orderId);
  });

  // 刪除訂單
  $("#ordertable").on("click", ".delete-btn", function(){
    const orderId = $(this).data("id");
    console.log("取消訂單，訂單ID: ", orderId);
    cancelOrder(orderId);
  });
};

// 查看訂單詳細
const orderDetailsBox = document.getElementById("order-details-box");
function viewOrderDetails(orderId){

  const param = {
    "orderId" : orderId,
  };

  console.log("查看訂單詳細 ID: ", orderId);

  $.ajax({
    url: `${baseUrl}/OrderCheckDetailsServlet.do`,
    type: "post",
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(param),
    success: function (orderDetails) {
      console.log("訂單詳細資料: ", orderDetails);
      setupOrderDetailsModal(orderDetails);
    },
  });
};

function cancelOrder(orderId){
  console.log("取消訂單 ID: ", orderId);
}

// 設置 order detail modal
function setupOrderDetailsModal(orderDetails){
  orderDetails.adIds.forEach((adId, index) =>{
    const tr = document.createElement("tr");

    const houseIdTd = document.createElement("td");
    houseIdTd.textContent = orderDetails[index] || "";
    tr.appendChild(houseIdTd);

    const adIdTd = document.createElement("td");
    adIdTd.textContent = adId || "";
    tr.appendChild(adIdTd);

    const adtypeTd = document.createElement("td");
    adtypeTd.textContent = orderDetails.adtypes[index] || "";
    tr.appendChild(adtypeTd);

  })
}



// 開關篩選欄位
const searchInput = document.getElementById("search-input-box");
function toggleSearchInput(){
  if(searchInput.style.display === "none" || searchInput.style.display === ""){
    searchInput.style.display = "block";
  }else{
    searchInput.style.display = "none";
  }
}

document.getElementById("search").addEventListener("click", function () {
  // 更新篩選條件
  updateFilterParams();
  console.log("更新後的篩選條件: ", filteredParams);
  filterOrders();
});

// 監聽使用者選擇的條件
const searchConditionSelection = document.getElementById("search-condition");
searchConditionSelection.addEventListener("change", function(){
  console.log("searchConditionSelection.value: ", searchConditionSelection.value);
  
  if(searchConditionSelection.value === "merchantTradNo"){
    searchInput.style.display = "block";
    searchInput.placeholder = "請輸入訂單編號";
  }
  
  if(searchConditionSelection.value === "userId"){
    searchInput.style.display = "block";
    searchInput.placeholder = "請輸入屋主編號";
  }

  if(searchConditionSelection.value === "all"){
    searchInput.placeholder = "";
    searchInput.style.display = "none";
  }

	updateFilterParams();
});

const orderStatusSelection = document.getElementById("order-status");
orderStatusSelection.addEventListener("change", function(){
	updateFilterParams();
});

function updateFilterParams(){
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
    success: function (data) {
      
      console.log("篩選出來的訂單: ", data);

      // 更新表格
      const table = $("#ordertable").DataTable();
      table.clear();

      if (!data || data.length === 0) {
        console.warn("沒有符合篩選條件的訂單資料");
        table.draw();
        return;
      }

      $.each(data, function (index, order) {
        table.row.add({
          orderId: order.orderId || "未提供",
          userId: order.userId || "未提供",
          userName: order.userName || "未提供",
          orderStatus: order.orderStatus || "未提供",
          paidDate: order.paidDate || "未提供",
          actions: `<button class="details-btn btn btn-warning btn-sm" data-id="${order.orderId}">查看詳細</button>
            <button class="delete-btn btn btn-sm" data-bs-toggle="modal" data-bs-target="#deleteModelBox" data-id="${order.orderId}">取消訂單</button>`,
        });
      });
      table.draw();
    },
    error: function (xhr, status, error) {
      console.error("AJAX Error: ", status, error);
      console.error("Response Text: ", xhr.responseText);
    },
  });
};



