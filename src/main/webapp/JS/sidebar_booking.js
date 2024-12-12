$(document).ready(function() {
	loadBookings(); // 頁面加載時自動加載預約數據
});

function loadBookings() {
	// 檢查是否已經初始化過 DataTable
	if ($.fn.DataTable.isDataTable('#myTable')) {
		// 銷毀現有的 DataTable 實例
		$('#myTable').DataTable().clear().destroy();
	}

	$.ajax({
		url: 'BookingServlet?action=list', // 請求的 URL
		type: 'post',
		dataType: 'json',
		success: function(data) {
			// 清空表格
			console.log(data);
			const table = $("#myTable").DataTable({
				language: {
					url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json" // 使用繁體中文
				},
				processing: true,
				dom: '<f><t><i><p>',	//l:length不使用;控制一次展示幾筆

				columnDefs: [
					{ orderable: false, targets: [4, 6] } // 取消排列,索引由0開始
				]
			});
			table.clear(); // 清空現有數據

			// 將新數據添加到表格中
			$.each(data, function(index, booking) {
				table.row.add([
					`<td data-original-value="${booking.bookingId}">${booking.bookingId}</td>`,
					`<td data-original-value="${booking.houseTitle}">${booking.houseTitle}</td>`,
					`<td data-original-value="${booking.userName}">${booking.userName}</td>`,
					`<td data-original-value="${booking.bookingDate}">${booking.bookingDate}</td>`,
					`<td data-original-value="${booking.startTime}">${booking.startTime}</td>`,
					`<td data-original-value="${booking.status}">${booking.status}</td>`,
					`<td>
	                       <button class="editBtn btn btn-warning">編輯功能</button>
	                       <button class="updateBtn btn btn-outline-success" style="display:none;">更新</button>
	                       <button class="cancelBtn btn btn-outline-secondary" style="display:none;">取消</button>
					</td>`
				]);

			});
		},
		error: function(xhr, status, error) {
			console.error("AJAX Error: ", status, error);
			console.error("Response Text: ", xhr.responseText);
		}
	});
}

$(document).on('click', '.editBtn', function() {
	const row = $(this).closest('tr');	//取得同一行

	$('#myTable tbody tr').not(row).find('.editBtn').prop('disabled', true); //停用 編輯
	$('#settingsModalBackdrop').addClass('show');
	row.css({
		'position': 'relative',
		'z-index': 1500,
		'background-color': 'white'
	});

	row.find('td').each(function(index) {
		const cell = $(this);
		const currentValue = cell.text();
		cell.data('original-value', currentValue); // 儲存原始值

		if (index == 5) {
			cell.html(`		
				<select class="form-select" aria-label="Default select example">
				  <option value="open" ${currentValue === 'open' ? 'selected' : ''}>open</option>
				  <option value="close" ${currentValue === 'close' ? 'selected' : ''}>close</option>
				</select>`);
		}

	});
	row.find('.editBtn').hide();
	row.find('.updateBtn, .cancelBtn').show();
});


$(document).on('click', '.updateBtn', function() {
	const row = $(this).closest('tr');
	$('#settingsModalBackdrop').removeClass('show');

	const Id = row.find('td').eq(0).text();
	const newStatus = row.find('select').val();

	if (Id != null) {
		// 發送 AJAX 請求更新資料庫
		$.ajax({
			url: 'BookingServlet?action=update', // 更新資料的 Servlet
			type: 'POST',
			data: {
				bookingId: Id,
				status: newStatus
			},
			success: function(response) {
				alert("更新成功");
				loadBookings(); // 重新載入
			},
			error: function() {
				alert("更新失敗");
			}
		});
	} else {
		alert("更新無效");
	}
});


$(document).on('click', '.cancelBtn', function() {
	const row = $(this).closest('tr');

	$('#myTable tbody tr').find('.editBtn').prop('disabled', false); //啟用 編輯/刪除
	$('#settingsModalBackdrop').removeClass('show');
	row.css({
		'position': '',
		'z-index': '',
		'background-color': ''
	});
	row.find('td').each(function(index) {
		if (index < 6) {	// 恢復存儲的原始值
			const cell = $(this);
			const originalValue = cell.data('original-value');
			cell.text(originalValue);
		}
	});
	row.find('.editBtn').show();
	row.find('.updateBtn, .cancelBtn').hide();
});


$(document).on('click', '.deleteBtn', function() {
	const row = $(this).closest('tr');
	const bookingId = row.find('td').eq(0).text();	//
	if (confirm("確定要刪除這筆資料嗎？")) {
		$.ajax({
			url: 'BookingServlet?action=delete', // 刪除資料的 Servlet
			type: 'POST',
			data: { id: bookingId },
			success: function(response) {
				alert("刪除成功");
				loadBookings(); // 重新載入
			},
			error: function() {
				alert("刪除失敗");
			}
		});
	}
});