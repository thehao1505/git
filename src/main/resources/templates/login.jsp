<!DOCTYPE html>
<html>
<head>
    <title>Trang đăng nhập</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <h1>Đăng nhập</h1>
    <form method="post" action="loginProcess.jsp">
        <div class="form-group">
            <label>Tên đăng nhập:</label>
            <input type="text" name="username" required>
        </div>
        <div class="form-group">
            <label>Mật khẩu:</label>
            <input type="password" name="password" required>
        </div>
        <div class="form-group">
            <input type="submit" value="Đăng nhập">
        </div>
    </form>
</div>
</body>
</html>