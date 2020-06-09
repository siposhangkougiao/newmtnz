<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018\3\30 0030
  Time: 9:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>Title</title>
    <base href="<%=basePath%>">
    <!-- jsp文件头和头部 -->
    <%@ include file="../../system/admin/top.jsp"%>
</head>
<body>
    <div class="container-fluid" id="main-container">
        <div id="page-content" class="clearfix">
            <div class="row-fluid">
                <div class="row-fluid">
                    <form action="banner/list.do" method="post" name="Form" id="Form">
                        <table>
                            <tr>
                                <td>
                                    <span class="input-icon">
                                        <input autocomplete="off" id="nav-search-input" type="text"
                                                name="KEYWORD" value="${pd.KEYWORD}" placeholder="这里输入关键词" />
										<i class="icon-search"></i>
                                    </span>
                                </td>
                                <td>
                                    <button class="btn btn-mini btn-light" onclick="search();" title="检索">
                                        <i class="icon-search"></i>
                                        <i class="icon-search"></i>
                                    </button>
                                </td>
                            </tr>
                        </table>
                        <table id="table_report" class="table table-striped table-bordered table-hover">
                            <thead>
                                <tr>
                                    <th class="center">
                                        <label><input type="checkbox" id="zcheckbox" /><span class="lbl"></span></label>
                                    </th>
                                    <th class="center">序号</th>
                                    <th class="center">图片</th>
                                    <th class="center">操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- 开始循环 -->
                                <c:choose>
                                    <c:when test="${not empty varList}">
                                        <c:forEach items="${varList}" var="var" varStatus="vs">
                                            <tr>
                                                <td class='center' style="width: 30px;">
                                                    <label><input type='checkbox' name='ids' value="${var.OPENID}" />
                                                        <span class="lbl"></span>
                                                    </label>
                                                </td>
                                                <td class='center' style="width: 30px;">${vs.index+1}</td>
                                                <td class='center'>
                                                    <a href="${var.IMG}" title="" target="_blank"><img src="${var.IMG}" alt="" width="80"></a>
                                                </td>
                                                <td style="width: 30px;" class="center">
                                                    <div class='hidden-phone visible-desktop btn-group'>
                                                        <div class="inline position-relative">
                                                            <button class="btn btn-mini btn-info" data-toggle="dropdown">
                                                                <i class="icon-cog icon-only"></i>
                                                            </button>
                                                            <ul class="dropdown-menu dropdown-icon-only dropdown-light pull-right dropdown-caret dropdown-close">
                                                                <li><a style="cursor:pointer;" title="编辑" onclick="edit('${var.USER_ID}');" class="tooltip-success" data-rel="tooltip" title="" data-placement="left"><span class="green"><i class="icon-edit"></i></span></a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr class="main_info">
                                            <td colspan="100" class="center">没有相关数据</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                        <div class="page-header position-relative">
                            <table style="width: 100%;">
                                <tr>
                                    <td style="vertical-align: top;">
                                        <div class="pagination" style="float: right; padding-top: 0px; margin-top: 0px;">
                                            ${page.pageStr}
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- 返回顶部  -->
    <a href="#" id="btn-scroll-up" class="btn btn-small btn-inverse"> <i
            class="icon-double-angle-up icon-only"></i>
    </a>

    <!-- 引入 -->
    <script type="text/javascript">window.jQuery || document.write("<script src='static/js/jquery-1.9.1.min.js'>\x3C/script>");</script>
    <script src="static/js/bootstrap.min.js"></script>
    <script src="static/js/ace-elements.min.js"></script>
    <script src="static/js/ace.min.js"></script>

    <script type="text/javascript" src="static/js/chosen.jquery.min.js"></script>
    <!-- 下拉框 -->
    <script type="text/javascript"
            src="static/js/bootstrap-datepicker.min.js"></script>
    <!-- 日期框 -->
    <script type="text/javascript" src="static/js/bootbox.min.js"></script>
    <!-- 确认窗口 -->
    <!-- 引入 -->
    <script type="text/javascript" src="static/js/jquery.tips.js"></script>
    <!--提示框-->
    <script type="text/javascript">
        $(top.hangge());
    </script>
</body>
</html>
