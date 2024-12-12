document.addEventListener("DOMContentLoaded", () => {
  const complaintMod = document.getElementById("complaintModal");
  const complaintForm = document.getElementById("complaintForm");
  const saveComplaintBtn = document.getElementById("saveComplaint");
  const editModeFields = document.getElementById("editModeFields");
  let isEditMode = false;

  document.getElementById("addButton").addEventListener("click", () => {
    isEditMode = false;
    resetForm();
    complaintMod.classList.add("show");
    document.querySelector(".closefrom").focus();
  });

  document.querySelector(".closeform").addEventListener("click", () => {
    complaintMod.classList.remove("show");
    document.getElementById("addButton").focus();
  });

  window.onclick = function (event) {
    if (event.target == complaintMod) {
      complaintMod.classList.remove("show");
    }
  };

  const table = $("#complaintsTable").DataTable({
    language: {
      url: "https://cdn.datatables.net/plug-ins/1.13.4/i18n/zh-HANT.json",
    },
    processing: true,
    dom: '<"dataTables-controls d-flex justify-content-between"<"left-search"f><"right-length"l>>rt<"bottom"<"right-pagination"ip>>',

    lengthMenu: [10, 15, 20],
    columnDefs: [
      { className: "text-center", targets: "_all" },
      { orderable: false, targets: 9 },
      { width: "150px", targets: 0 },
      { width: "150px", targets: 1 },
      { width: "150px", targets: 2 },
      { width: "120px", targets: 3 },
      { width: "140px", targets: 4 },
      { width: "120px", targets: 7 },
      { width: "120px", targets: 8 },
      { width: "200px", targets: 9 },
      {
        targets: 5,
        render: function (data, type, row) {
          if (type === "display" && data.length > 20) {
            return data.substr(0, 20) + "...";
          }
          return data;
        },
      },
    ],
    scrollY: "500px",
    scrollCollapse: true,
  });

  function loadData() {
    fetch("/rent_web/Complaints")
      .then((response) => response.json())
      .then((data) => {
        table.clear().draw();
        data.forEach((item) => {
          //console.log("check loadData", item.complation_id);
          table.row
            .add([
              item.complaints_id,
              item.user_id,
              item.username,
              item.category,
              item.subject,
              item.content,
              item.note || "無",
              item.status || "待處理",
              new Date(item.submission_date).toLocaleDateString("zh-Hant", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
                hour: "2-digit",
                minute: "2-digit",
                second: "2-digit",
                hour12: false, // 確保是 24 小時制
              }),
              `<td class="action-buttons">
			  <button class="editbtn edit-button" data-id="${item.complaints_id}">編輯</button>
			  <button class="complete complete-button" data-id="${item.complaints_id}">完成</button>
            </td>`,
			// <button class="btn btn-primary btn-sm edit-button" data-id="${item.complaints_id}">編輯</button>
              //<button class="btn btn-danger btn-sm delete-button" data-id="${item.complaints_id}">刪除</button>
            ])
            .draw(false);
        });
      });
  }

  function resetForm() {
    complaintForm.reset();
    document.getElementById("complaintId").value = "";
    document.getElementById("contenttent").value = "";
    console.log(
      "resetForm test",
      document.getElementById("contenttent").value,
      "the end"
    );
  }

  function insertEditModeFields() {
    if (isEditMode) {
      editModeFields.innerHTML = `
    <div class="mb-3">
      <label for="note" class="form-label">客服人員註記當前狀況：</label>
      <textarea id="note" name="note" rows="5" class="form-control" required></textarea>
    </div>
    <div class="mb-3">
      <label><input type="radio" name="status" value="待處理" /> 待處理</label>
      <label><input type="radio" name="status" value="處理中" /> 處理中</label>
      <label><input type="radio" name="status" value="已完成" /> 已完成</label>
    </div>
            `;
    } else {
      editModeFields.innerHTML = "";
    }
  }

  //trigger function
  document
    .querySelector("#complaintsTable")
    .addEventListener("click", function (e) {
      const itemId = e.target.dataset.id;

      if (e.target.classList.contains("edit-button")) {
        console.log("trigger edit-btn");
        console.log("complaintsTable");
        console.log("Fetching complaint with ID:", itemId);

        isEditMode = true;
        resetForm();
        insertEditModeFields();

        fetch(`/rent_web/Complaints/${itemId}`)
          .then((response) => response.json())
          .then((item) => {
            console.log("try object check item", Object.keys(item));
            console.log("check item.content", item.content);
            console.log("edit-button item", item);
            console.log("edit-button itemId", itemId);
            console.log("check form 1", item.content, "check the end 1");

            document.getElementById("complaintId").value = item.complaints_id;
            document.getElementById("user_id").value = item.user_id;
            document.getElementById("username").value = item.username;
            document.getElementById("subject").value = item.subject;
            document.getElementsByName("statFus").forEach((radio) => {
              radio.checked = radio.value === item.status;
            });
            document.getElementById("note").value = item.note || "無";
            document.getElementsByName("category").forEach((radio) => {
              radio.checked = radio.value === item.category;
            });
            console.log("check form 2", item.content, "check the end 2");
            document.getElementById("contenttent").value = item.content || "無";

            complaintMod.classList.add("show");
          });
      }

      //delete
      if (e.target.classList.contains("delete-button")) {
        if (confirm("確定要刪除此項目嗎？")) {
          fetch(`/rent_web/Complaints/${itemId}`, {
            method: "DELETE",
          })
            .then((response) => {
              if (response.ok) {
                loadData();
              } else {
                alert("刪除失敗");
              }
            })
            .catch((error) => console.error("Error:", error));
        }
      }

      //status
      if (e.target.classList.contains("complete-button")) {
        if (confirm("確定將此項標記為已完成嗎？")) {
          fetch(`/rent_web/Complaints/${itemId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ complaint_id: itemId , status: "已完成" }),//complaint_id: itemId,
          })
            .then((response) => {
              if (response.ok) {
                loadData();
              } else {
                alert("標記為已完成失敗");
              }
            })
            .catch((error) => console.error("Error:", error));
        }
      }
    });

  //save
  saveComplaintBtn.addEventListener("click", () => {
    const formData = new FormData(complaintForm);
    const formObject = Object.fromEntries(formData.entries());

    console.log("check formdata", formObject.content);

    let method = isEditMode ? "PUT" : "POST";
    let url = isEditMode
      ? `/rent_web/Complaints/${formObject.complaint_id}`
      : "/rent_web/Complaints/";

    fetch(url, {
      method: method,
      body: JSON.stringify(formObject),
      headers: { "Content-Type": "application/json" },
    })
      .then((response) => response.json())
      .then((data) => {
        console.log("Request URL:", url);
        console.log("Request Method:", method);
        console.log("Request Body:", JSON.stringify(formObject));

        complaintMod.classList.remove("show");
        loadData();
      })
      .catch((error) => console.error("Error:", error));
  });

  loadData();
});
