$(document).ready(function() {
    loadTable(); // Load table data on document ready
});

// Load table data
function loadTable() {
    $.ajax({
        url: "http://localhost:8080/rent_web/houses.123", // API endpoint
        type: "GET", // Use GET method
        dataType: "json", // Data type is JSON
        success: function(data) {
            console.log("Data received:", data); // Confirm data is correct
            
            // Clear table content
            $("#tableBody").empty();

            // Check if there are matching results
            if (data.length === 0) {
                $("#noMatch").show(); // Show no match found message
            } else {
                $("#noMatch").hide(); // Hide message
                
                // Iterate through each house data and add to the table
                $.each(data, function(index, house) {
                    $("#tableBody").append(
                        '<tr id="row-' + house.houseId + '">' +
                        '<td>' + house.houseId + '</td>' +
                        '<td>' + house.title + '</td>' +
                        '<td>' + house.userId + '</td>' +
                        '<td>' + house.userName + '</td>' +
                        '<td>' + house.address + '</td>' +
                        '<td>' + house.price + '</td>' +
                        '<td class="status-cell">' + house.status + '</td>' +
                        '<td>' +
                        '<button class="btn btn-success" onclick="window.location.href=\'housePage.html?houseId=' + house.houseId + '\'">查看</button>' +
                        '<button class="editBtn btn btn-warning">編輯</button>' +
                        '<button class="updateBtn btn btn-outline-success" style="display:none;">更新</button>' +
                        '<button class="cancelBtn btn btn-outline-secondary" style="display:none;">取消</button>' +
                        '</td>' +
                        '</tr>'
                    );
                });
            }

            // Destroy old DataTable instance (if exists)
            if ($.fn.DataTable.isDataTable('#myTable')) {
                $('#myTable').DataTable().destroy();
            }

            // Initialize DataTable
            $("#myTable").DataTable({
                language: {
                    url: "https://cdn.datatables.net/plug-ins/2.1.8/i18n/zh-HANT.json" // Use Traditional Chinese
                },
                autoWidth: true,
                info: true,
                processing: true, // Show loading indicator
                stateSave: true, // Save user state
                lengthMenu: [10], // Display 10 records per page
                dom: '<f<t><ip>>', // Custom DOM structure
            });
        },
		error: function(jqXHR, textStatus, errorThrown) {
		       console.error('錯誤:', textStatus, errorThrown);
		       console.log('回應:', jqXHR.responseText);
		   }
    });
}

// Edit button click handler
$(document).on('click', '.editBtn', function() {
    const row = $(this).closest('tr'); // Get the same row

    // Check if any row is already being edited
    if ($('.editing').length > 0) {
        alert("請先完成當前編輯。");
        return; // Exit if another row is being edited
    }

    $('.modal-backdrop').remove(); 
    $('body').append('<div class="modal-backdrop fade show"></div>'); 

    $('#settingsModalBackdrop').addClass('show'); 
    row.addClass('editing'); 

    row.css({
        'position': 'relative',
        'z-index': 1500,
        'background-color': 'white'
    });

    row.find('td').each(function(index) {
        const cell = $(this);
        const currentValue = cell.text();
        cell.data('original-value', currentValue); 

        if (index == 6) { 
            cell.html(`
                <select class='form-select'>
                    <option value='0' ${currentValue === '待審核' ? 'selected' : ''}>待審核</option>
                    <option value='1' ${currentValue === '上架' ? 'selected' : ''}>上架</option>
                    <option value='2' ${currentValue === '停權' ? 'selected' : ''}>停權</option>
                    <option value='3' ${currentValue === '下架' ? 'selected' : ''}>下架</option>
                </select>`);
        }
    });

    row.find('.editBtn').hide();
    row.find('.updateBtn, .cancelBtn').show();
});

// Update button click handler
$(document).on('click', '.updateBtn', function() {
    const row = $(this).closest('tr');
    const Id = row.find('td').eq(0).text(); 
    const newStatus = row.find('select').val(); 

    if (Id) {
        $.ajax({
            url: 'http://localhost:8080/rent_web/houseUpdateServlet.do', 
            type: 'POST',
            data: { houseId: Id, status: newStatus },  
            success: function(response) {
                alert("更新成功");
                loadTable(); 
                
                // Create a mapping of status values to their display text
                const statusMapping = {
                    "0": "待審核",   // Pending
                    "1": "上架",     // Listed
                    "2": "停權",     // Suspended
                    "3": "下架"      // Unlisted
                };

                // Update the status cell text based on the newStatus value
                const updatedRow = $('#row-' + Id);
                updatedRow.find('.status-cell').text(statusMapping[newStatus] || "未知狀態"); // Default to '未知狀態' if not found
                
                updatedRow.find('.editBtn').show();
                updatedRow.find('.updateBtn, .cancelBtn').hide();

                $('#settingsModalBackdrop').removeClass('show'); 
                $('.modal-backdrop').remove(); 
                updatedRow.removeClass('editing'); 
            },
            error: function() {
                alert("更新失敗，請稍後再試。");
            }
        });
    } else {
        alert("更新無效");
    }
});

// Cancel button click handler to revert changes and restore functionality to other rows
$(document).on('click', '.cancelBtn', function() {
    const row = $(this).closest('tr');
    
    $('#settingsModalBackdrop').removeClass('show'); 
    $('.modal-backdrop').remove(); 
    row.removeClass('editing'); 
    
    row.css({
        'position': '',
        'z-index': '',
        'background-color': ''
    });
    
    row.find('td').each(function(index) {
        const cell = $(this);
        const originalValue = cell.data('original-value');

        if (index == 6) { 
            cell.text(originalValue); 
        }
    });

    row.find('.editBtn').show();
    row.find('.updateBtn, .cancelBtn').hide();
});