$(document).ready(function() {
	loadBookings(); // 頁面加載時自動加載預約數據

	function loadBookings() {
		$.ajax({
			url: "BookingServlet?action=list", // 請求的 URL
			type: "post",
			dataType: "json",
			success: function(data) {

				$.each(data, function(index, booking) {
					$("#tableBody").append(
						`<tr>		
							<td data-original-value="${booking.booking_id}">${booking.booking_id}</td>
                            <td data-original-value="${booking.house_id}">${booking.house_id}</td>
                            <td data-original-value="${booking.user_id}">${booking.user_id}</td>
                            <td data-original-value="${booking.booking_date}">${booking.booking_date}</td>
                            <td data-original-value="${booking.start_time}">${booking.start_time}</td>
                            <td data-original-value="${booking.status}">${booking.status}</td>
                            <td>
								<button class="editBtn">編輯功能</button>
				                <button class="updateBtn" style="display:none;">更新</button>
				                <button class="cancelBtn" style="display:none;">取消</button>
				                <button class="deleteBtn">刪除</button>
                            </td>
                        </tr>`
					);
				});
				$("#myTable").DataTable({
					language: {
						url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json" // 使用繁體中文
					},
					processing: true,
					stateSave: true,
					lengthMenu: [10],
					dom: '<f<t><ip>>',

					columnDefs: [
						{ orderable: false, targets: 6 } // 將第6列設置為沒有排列
					]
				}); // 初始化 DataTable
			},
			error: function(xhr, status, error) {
				console.error("AJAX Error: ", status, error);
			}
		});
	}
});

$(document).on('click', '.editBtn', function() {
	const row = $(this).closest('tr');
	$('#myTable tbody tr').not(row).find('.editBtn, .deleteBtn').prop('disabled', true);
	row.find('td').each(function(index) {
		if (index == 2 || index == 3 || index == 4 || index == 5) { // 不編輯操作列
			const cell = $(this);
			const currentValue = cell.text();
			cell.html(`<input type="text" value="${currentValue}" />`);
		}
	});
	row.find('.editBtn, .deleteBtn').hide();
	row.find('.updateBtn, .cancelBtn').show();
});

$(document).on('click', '.updateBtn', function() {
	const row = $(this).closest('tr');
	const updatedData = {};
	let valid = true;

	row.find('input').each(function(index) {
		const inputValue = $(this).val();
		if (inputValue.trim() === "") {
			valid = false; // 確保不為空值
		}
		updatedData[`field${index}`] = inputValue; // 根據需要設定字段名
	});

	if (valid) {
		// 發送 AJAX 請求更新資料庫
		$.ajax({
			url: 'UpdateBookingServlet', // 更新資料的 Servlet
			type: 'POST',
			data: updatedData,
			success: function(response) {
				// 更新成功後，重新加載數據或更新顯示
				loadBookings(); // 重新加載預約數據
			}
		});
	} else {
		alert("所有字段都必須填寫！");
	}
});

$(document).on('click', '.cancelBtn', function() {
	const row = $(this).closest('tr');
	$('#myTable tbody tr').find('.editBtn, .deleteBtn').prop('disabled', false);
	row.find('td').each(function(index) {
		if (index !== 1 && index !== 6) { // 不編輯操作列
			const cell = $(this);
			const originalValue = cell.data('original-value'); // 假設之前存儲了原始值
			cell.text(originalValue);
		}
	});
	row.find('.editBtn, .deleteBtn').show();
	row.find('.updateBtn, .cancelBtn').hide();
});

$(document).on('click', '.deleteBtn', function() {
	const row = $(this).closest('tr');
	const bookingId = row.data('booking_id');
	
	console.log($(this).closest('tr'));
	console.log($(this));
	console.log("row: "+row);
	console.log("ID: "+bookingId);
	if (confirm("確定要刪除這筆資料嗎？")) {
		$.ajax({
			url: 'BookingServlet?action=delete', // 刪除資料的 Servlet
			type: 'POST',
			data: { id: bookingId },
			success: function(response) {
				loadBookings(); // 重新加載預約數據
			}
		});
	}
});
