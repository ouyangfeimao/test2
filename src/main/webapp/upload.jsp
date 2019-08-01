<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>文件上传</title>
    <style>
        div {
            margin-left: 200px;
            margin-top: 100px;
        }

        form input {
            padding: 4px;
        }
    </style>
</head>

<body>
<div>
    <c:if test="${msg != null}">
        <p style="color: red">${msg}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/fileupload?action=upload" enctype="multipart/form-data"
          method="post">
        上传文件：<input type="file" name="file"><br/>
        <hr/>

        <input type="submit" value="提交">
    </form>
</div>

</body>
</html>