<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-hant-TW">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>租屋網站後台管理</title>
    <!-- Bootstrap CSS -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
      crossorigin="anonymous"
    />
    <!-- Bootstrap Icons -->
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css"
    />
    <style>
      body {
        background-color: #f7f7f7;
        font-family: "Arial", sans-serif;
      }
      /* Navbar styles */
      .navbar {
        background-color: #343a40;
      }
      .navbar-brand {
        color: #fff;
        font-weight: bold;
      }
      .navbar-nav .nav-link {
        color: #fff;
      }
      .sidebar {
        background-color: #343a40;
        height: 100vh;
        position: fixed;
        width: 250px;
        padding-top: 20px;
      }
      .sidebar a {
        color: #fff;
        text-decoration: none;
        padding: 10px 20px;
        display: block;
      }
      .sidebar a:hover {
        background-color: #495057;
        border-radius: 5px;
      }
      .content {
        margin-left: 260px;
        padding: 20px;
      }
      /* Table styles */
      table {
        width: 100%;
        margin-bottom: 20px;
        border-collapse: collapse;
      }
      th,
      td {
        padding: 12px;
        text-align: left;
      }
      th {
        background-color: #6c757d;
        color: white;
      }
      td {
        background-color: white;
      }
      /* Footer */
      .footer {
        text-align: center;
        padding: 20px;
        background-color: #343a40;
        color: white;
        position: fixed;
        bottom: 0;
        width: 100%;
      }
      @media (max-width: 768px) {
        .sidebar {
          width: 100%;
          height: auto;
          position: relative;
        }
        .content {
          margin-left: 0;
        }
      }
    </style>
  </head>
  <body>
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">租屋網站後台管理</a>
      </div>
    </nav>

    <!-- Sidebar -->
    <div class="sidebar">
      <a href="#"><i class="bi bi-house-door-fill"></i> 房屋管理</a>
      <a href="#"><i class="bi bi-person-fill"></i> 用戶管理</a>
      <a href="#"><i class="bi bi-calendar-check-fill"></i> 預約管理</a>
      <a href="#"><i class="bi bi-card-checklist"></i> 廣告訂單</a>
      <a href="#"><i class="bi bi-headset"></i> 客服管理</a>
      <a href="#"><i class="bi bi-bar-chart-fill"></i> 報表分析</a>
      <a href="#"><i class="bi bi-gear-fill"></i> 設定</a>
      <a href="#"><i class="bi bi-box-arrow-right"></i> 登出</a>
    </div>

    <!-- Content -->
    <div class="content">
      <h2>房屋管理</h2>
      <p>這裡可以管理網站上的所有房屋資料。</p>

      <table class="table table-bordered">
        <thead>
          <tr>
            <th>房屋 ID</th>
            <th>地址</th>
            <th>房東</th>
            <th>租金</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>台北市信義區忠孝東路</td>
            <td>陳先生</td>
            <td>$30,000</td>
            <td>
              <button class="btn btn-primary">
                <i class="bi bi-pencil-fill"></i> 編輯
              </button>
              <button class="btn btn-danger">
                <i class="bi bi-trash-fill"></i> 刪除
              </button>
            </td>
          </tr>
          <tr>
            <td>2</td>
            <td>台中市西屯區文心路</td>
            <td>林小姐</td>
            <td>$20,000</td>
            <td>
              <button class="btn btn-primary">
                <i class="bi bi-pencil-fill"></i> 編輯
              </button>
              <button class="btn btn-danger">
                <i class="bi bi-trash-fill"></i> 刪除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Footer -->
    <div class="footer">
      <p>租屋網站後台管理 &copy; 2024</p>
    </div>

    <!-- Bootstrap JS (optional) -->
    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-QHTmIOcmyMHb9Elcdo6nqK7LFRdJ5GfUlE6NrbdUvtiBhPfs7RZRlCpzNYf1NwOw"
      crossorigin="anonymous"
    ></script>
    <script>
      document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll(".sidebar ul li a").forEach(function (link) {
          link.addEventListener("click", function (event) {
            event.preventDefault();
            const selectedContent = link.getAttribute("data-content");

            // 使用 AJAX 請求加載 JSP 文件
            const xhr = new XMLHttpRequest();
            xhr.open("GET", selectedContent + ".jsp", true);
            xhr.onload = function () {
              if (xhr.status === 200) {
                document.getElementById("content").innerHTML = xhr.responseText;
              } else {
                document.getElementById("content").innerHTML =
                  "<h2>錯誤</h2><p>無法加載內容。</p>";
              }
            };
            xhr.send();
          });
        });
      });
    </script>
  </body>
</html>
