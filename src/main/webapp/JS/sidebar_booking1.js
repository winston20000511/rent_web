$(document).ready(function() {
	loadTable();
});

function loadTable() {

	// 檢查是否已經初始化過 DataTable
	if ($.fn.DataTable.isDataTable('#myTable')) {
		// 銷毀現有的 DataTable 實例
		$('#myTable').DataTable().clear().destroy();
	}

	$('#myTable').DataTable({
		processing: true, // 顯示處理中的提示
		serverSide: true, // 啟用伺服器端處理
		language: {
			url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json" // 使用繁體中文
		},
		ajax: {
			url: '/rent_web/BookingServlet',
			type: 'POST',
			data: { action: 'list' }, // 傳送參數給後端
		},
		columns: [
			{ data: 'bookingId' }, // 預約編號
			{ data: 'houseTitle', orderable: false }, // 房屋標題
			{ data: 'houseOwnerName', orderable: false }, // 屋主姓名
			{ data: 'userName', orderable: false }, // 預約者姓名
			{ data: 'bookingDate' }, // 預約日期
			{
				data: 'status', render: function(data) { // 狀態轉換
					switch (data) {
						case 0: return '待確認';
						case 1: return '預約中';
						case 2: return '屋主拒絕';
						case 3: return '屋主取消';
						case 4: return '租客取消';
						case 5: return '屋主未回應';
						case 6: return '已完成';
						default: return '未知狀態';
					}
				},
				orderable: false
			},
			{
				data: null,
				orderable: false,
				render: function(data, type, row) {
					return `
					<button class="details-btn" onclick='viewDetails(${JSON.stringify(row)})'>詳情</button>
					<button class="edit-btn" onclick='editBooking(${JSON.stringify(row)})'>編輯</button>`;
				}
			}
		],
		order: [[0, 'desc']] // 依[預約編號],降序排序
	});
}

// 查看預約詳情
function viewDetails(rowData) {
	
    // 狀態處理
    let statusText;
    switch (rowData.status) {
        case 0: statusText = '待確認'; break;
        case 1: statusText = '預約中'; break;
        case 2: statusText = '屋主拒絕'; break;
        case 3: statusText = '屋主取消'; break;
        case 4: statusText = '租客取消'; break;
        case 5: statusText = '屋主未回應'; break;
        case 6: statusText = '已完成'; break;
        default: statusText = '未知狀態'; break;
    }

    // 將格式化後的內容插入到自定義彈窗中
	document.querySelector('#customOverlay .custom-popup-body').innerHTML = `
	    <table class="custom-detail-table">
	        <tr>
	            <th>房屋標題</th>
	            <td>${rowData.houseTitle}</td>
	        </tr>
	        <tr>
	            <th>地址</th>
	            <td>${rowData.houseAddress}</td>
	        </tr>
	        <tr>
	            <th>價格</th>
	            <td>${rowData.housePrice}元</td>
	        </tr>
	        <tr>
	            <th>屋主姓名</th>
	            <td>${rowData.houseOwnerName}</td>
	        </tr>
	        <tr>
	            <th>預約者</th>
	            <td>${rowData.userName}</td>
	        </tr>
	        <tr>
	            <th>預約日期</th>
	            <td>${rowData.bookingDate}</td>
	        </tr>
	        <tr>
	            <th>預約時間</th>
	            <td>${rowData.bookingTime}</td>
	        </tr>
	        <tr>
	            <th>狀態</th>
	            <td>${statusText}</td>
	        </tr>
	    </table>
	`;

    // 顯示彈窗
    document.getElementById('customOverlay').style.display = 'flex';
}

function closePopup() {
    // 關閉彈窗
    document.getElementById('customOverlay').style.display = 'none';
}


// 編輯預約
function editBooking(rowData) {
	const table = $('#myTable').DataTable();
	const row = table.row(function(idx, data, node) {
		return data.bookingId === rowData.bookingId; // 找到對應的行
	});

	if (!row.node()) {
		console.error('Row not found');
		return;
	}

	// 儲存原本的數據
	const originalHtml = $(row.node()).html();

	// 儲存原始資料作為屬性
	const originalData = {
		bookingDate: rowData.bookingDate,
		bookingTime: rowData.bookingTime,
		status: rowData.status,
	};
	$(row.node()).data('originalData', originalData);

	// 將預約日期拆分成日期和時間
	const bookingDate = rowData.bookingDate;
	const bookingTime = rowData.bookingTime;

	// 狀態選項
	const statusOptions = [
		{ value: 0, text: '待確認' },
		{ value: 1, text: '預約中' },
		{ value: 2, text: '屋主拒絕' },
		{ value: 3, text: '屋主取消' },
		{ value: 4, text: '租客取消' },
		{ value: 5, text: '屋主未回應' },
		{ value: 6, text: '已完成' },
	];

	// 建立編輯行的 HTML
	const editHtml = `
	        <td colspan="6">
	            <div class="edit-container">
	                <div class="edit-field">
	                    <label for="editDate">預約日期</label>
	                    <input type="date" id="editDate" value="${bookingDate}">
	                </div>
	                <div class="edit-field">
	                    <label for="editTime">預約時間</label>
	                    <input type="time" id="editTime" value="${bookingTime}" step="1800">
	                </div>
	                <div class="edit-field">
	                    <label for="editStatus">狀態</label>
	                    <select id="editStatus">
	                        ${statusOptions.map(option => `
	                            <option value="${option.value}" ${option.value === rowData.status ? 'selected' : ''}>
	                                ${option.text}
	                            </option>
	                        `).join('1')}
	                    </select>
	                </div>
	            </div>
	        </td>
			<td>
				<button class="update-btn" disabled>更新</button>
				<button class="cancel-btn">取消</button>
			</td>
	    `;

	// 替換行的內容
	$(row.node()).html(editHtml);

	// 禁用其他行的編輯按鈕和詳情
	$('#myTable tbody tr').not(row.node()).each(function() {
		$(this).find('.edit-btn').prop('disabled', true); // 禁用編輯按鈕
		$(this).find('.details-btn').prop('disabled', true); // 禁用查看按鈕
	});

	// 監聽輸入變更事件，啟用或禁用更新按鈕
	$('#editDate, #editTime, #editStatus').on('input change', function() {
		const currentData = {
			bookingDate: $('#editDate').val(),
			bookingTime: $('#editTime').val(),
			status: parseInt($('#editStatus').val(), 10),
		};

		// 比較原始資料和當前資料
		const isChanged = Object.keys(originalData).some(key => originalData[key] !== currentData[key]);

		// 如果有變更，啟用按鈕；否則禁用
		$('.update-btn').prop('disabled', !isChanged);
	});

	// [更新]click event
	$(row.node()).find('.update-btn').on('click', function() {
		updateBooking(rowData.bookingId);
	});

	// [取消]click event
	$(row.node()).find('.cancel-btn').on('click', function() {
		cancelEdit(row, originalHtml);
	});
}

// 更新預約資料
function updateBooking(bookingId) {
	const updatedData = {
		bookingId,
		bookingDate: $('#editDate').val(),
		bookingTime: $('#editTime').val(),
		status: parseInt($('#editStatus').val(), 10),
	};

	// 將更新的資料提交到伺服器
	$.ajax({
		url: '/rent_web/BookingServlet',
		type: 'POST',
		data: {
			action: 'update',
			...updatedData,
		},
		success: function(response) {
			alert('更新成功');
			console.log(response);
			loadTable(); // 重新載入表格
		},
		error: function(error) {
			alert('更新失敗');
		},
	});
}

// 取消編輯
function cancelEdit(row, originalHtml) {
	// 恢復原本的數據
	$(row.node()).html(originalHtml);

	// 解除 "更新" 按鈕的禁用狀態
	$(row.node()).find('.update-btn').prop('disabled', false);

	// 解除其他行編輯按鈕和詳情按鈕的禁用
	$('#myTable tbody tr').each(function() {
		$(this).find('.edit-btn').prop('disabled', false); // 重新啟用編輯按鈕
		$(this).find('.details-btn').prop('disabled', false); // 重新啟用詳情按鈕
	});
}