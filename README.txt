read me
	1. add /modify file
	2. add /modify function
	3. introduce user role   
	4. How to update  table   password
	5. what   should   I   do   when   I  encounter   a   problem
	6. **port = = = 8888
	

1. 添加 / 修改檔案 (Add / Modify File)
檔案部分 (File Part)
Bean:
修改 AdminBean
Dao:
修改 AdminDao
新增 registerAccount 和 checkAccount 方法
Impl:
修改 AdminDaoImpl
新增 registerAccount 和 checkAccount 方法的實作
Servlet:
修改 BackstageLoginServlet
新增 setAttribute(role) 方法到 BackstageRegisterServlet 和 SessionFilter
新增 UpdateAdminDataServlet 和 BackstageRegisterServlet
Util:
新增 PasswordEncryptor.java 和 PasswordEncryptor_user.java 類別 (用途不明確，請提供更多資訊)
Html:
新增 backstage-register.jsp 和 404.jsp
修改 backstage-login.jsp
hibernate.cfg.xml:
修改 <mapping class="Bean.fromComplationBean" /> 為 <mapping class="Bean.formComplationBean" /> (改正拼寫錯誤)
pom.xml:
新增依賴項目:
org.projectlombok:lombok:1.18.28 (provided scope)
org.mindrot:jbcrypt:0.4 (密碼加密庫)
2. 功能部分 (Function Part)
新增註冊功能 (register function)
登入時使用 BCrypt 雜湊加密密碼 (login PWD has BCrypt)
如何更新 SQL 密碼 (How to update SQL PWD) (見 PasswordEncryptor.java)
修改過濾器邏輯 (Modify filter logic) (需要查看 SeesionFilter.java)
3. 引入使用者角色 (Introduce User Role)
使用者權限等級 (User Permissions Level)
SAdmin (超級管理員，可訪問所有路徑)

Admin (管理員，可訪問所有路徑)

RAdmin-ad (廣告管理員，可訪問 /AdAnalysisServlet.do 和 /AdDataOperationServelt.do)

RAdmin-book (訂房管理員，可訪問 /BookingServlet)

RAdmin-complaint (申訴管理員，可訪問 /Complaints 和 /Complaints/*)

RAdmin-house (房源管理員，可訪問 /housesDelete.123、/houses.123 和 /houseUpdateServlet.do)

RAdmin-order (訂單管理員，可訪問 /OrderDataOperationServlet.do)

RAdmin-user (使用者管理員，可訪問 /UpdateUserServlet.do、/LoadAllUsersServlet.do 和 /SearchUserServlet.do)

詳細資訊請參閱 SessionFilter.java (servlet)

4. 如何更新資料表密碼 (How to update table password)
您可以使用 util.PasswordEncryptor 類別更新密碼，該類別可能需要以 Java 應用程式的方式運行。

運行 PasswordEncryptor.java 作為 Java 應用程式 (start PasswordEncryptor.java use run as (java application))
或者註冊新帳戶並選擇您想要的權限 (register new account choice you want permissions)
檢查您的權限管理範圍是否運作 (check you manage scope is work ?)

如果無法運作，請檢查控制台訊息 (check console block)，權限錯誤可能是原因之一 (probably role error)。

5. 遇見問題怎麼辦 (What should I do when I encounter a problem)
5-1. 使用最新的 SQL 語法 (use top new sql)
5-2. 如果無法自行解決，請註解 filter.java (Annotation filter.java)。過濾器可能存在問題 (Mabay problmen is filter)。





read me
	1. add /modify file
	2. add /modify function
	3. introduce user role   
	4. How to update  table   password
	5. what   should   I   do   when   I  encounter   a   problem
	6. **port = = = 8888



1. add /modify file
file part :

Bean :
AdminBean --modify

dao :
AdminDao --modify
//add 
	registerAccount , checkAccount

Impl :
AdminDaoImpl --modify
//add 
	registerAccount , checkAccount

servlet :
//modify
BackstageLoginServlet
	
	//add 
	setAttribute(role)
SessionFilter
UpdateAdminDataServlet
BackstageRegisterServlet

util :
//add
PasswordEncryptor.java
PasswordEncryptor_user.java

html : 
//add
backstage-register.jsp
404.jsp
	//modify
backstage-login

hibernate.cfg.xml 
<mapping class="Bean.fromComplationBean" />
		    change to 
           |
           |
          \ /
           V	
<mapping class="Bean.formComplationBean" />


pom.xml
//add dependency*2
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.28</version> 
			<scope>provided</scope>
		</dependency>

		<dependency>
			<groupId>org.mindrot</groupId>
			<artifactId>jbcrypt</artifactId>
			<version>0.4</version>
		</dependency>

---------------------------------------------------------------

2. Function part:

	1. Add register function
	2. Add   login    PWD   has   BYC
	3. How   to update   SQL PWD
	4. Modify   filter   logic
	5. 

---------------------------------------------------------------

3.introduce user role    
user permissions level 
SAdmin (can arrival all path)(super admin)
Admin (can arrival all path)
RAdmin-ad 
(can   arrival /AdAnalysisServlet.do , /AdDataOperationServelt.do")
RAdmin-book
(can   arrival /BookingServlet)
RAdmin-complaint
(can   arrival /Complaints , /Complaints/*)
RAdmin-house
(can   arrival  /housesDelete.123 , /houses.123 , /houseUpdateServlet.do)
RAdmin-order
(can   arrival /OrderDataOperationServlet.do)
RAdmin-user
(can   arrival /UpdateUserServlet.do , /LoadAllUsersServlet.do , /SearchUserServlet.do)
*detile see SeesionFilter.java(servlet)

---------------------------------------------------------------

4.How to update  table   password
update MSSQL PWD (at util.PasswordEncryptor)
*start PasswordEncryptor.java use run as (java application)
OR register new account choice you want permissions

check you manage scope is work ?
if not work check console block 
probably role error

---------------------------------------------------------------

5. what should I do when I encounter a problem
5-1.use top new sql
5-2.
if you can't do anything
Annotation filter.java
The problem is probably caused by the filter.


















