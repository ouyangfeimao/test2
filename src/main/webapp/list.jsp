<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet"/>
    <link rel="stylesheet" href="css/Site.css"/>
    <link rel="stylesheet" href="css/zy.all.css"/>
    <link rel="stylesheet" href="css/font-awesome.min.css"/>
    <link rel="stylesheet" href="css/amazeui.min.css"/>
    <link rel="stylesheet" href="css/admin.css"/>
</head>
<body>
<div class="dvcontent">

    <div>
        <!--tab start-->
        <div class="tabs">
            <div class="bd">
                <ul style="display: block;padding: 20px;">
                    <li>
                        <!--分页显示角色信息 start-->
                        <div id="dv1">
                            <table class="table" id="tbRecord">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>文件名</th>
                                    <th>上传时间</th>
                                    <th>下载</th>
                                    <th>删除</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${fileList}" var="file" varStatus="status">
                                    <tr>
                                        <td>${status.index+1}</td>
                                        <td>${file.fileName}</td>
                                        <td>${file.createTime}</td>
                                        <td class="delete">
                                            <button>
                                                <i class="icon-trash bigger-120"></i>
                                                <a href="fileoperation?action=download&fileName=${file.fileName}">下载</a>
                                            </button>
                                        </td>
                                        <td class="delete">
                                            <button onclick="btn_delete(1)"><i class="icon-trash bigger-120"></i>
                                                <a href="fileoperation?action=delete&fileName=${file.fileName}">删除</a>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>

                            </table>
                        </div>
                        <!--分页显示角色信息 end-->
                    </li>
                </ul>
            </div>
        </div>
        <!--tab end-->

    </div>


    <script src="js/jquery-1.7.2.min.js" type="text/javascript"></script>
    <script src="js/plugs/Jqueryplugs.js" type="text/javascript"></script>
    <script src="js/_layout.js"></script>
    <script src="js/plugs/jquery.SuperSlide.source.js"></script>
    <script>
        var num = 1;
        $(function () {

            $(".tabs").slide({trigger: "click"});

        });

    </script>

</div>
</body>

</html>