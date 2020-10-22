<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.*"%>

<%-- 导入文件 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<%-- script方法 --%>
<script>
    <%-- 提交判空 --%>
    $(function() {
        $("#addForm").submit(function() {
            if (checkEmpty("name", "属性名称"))
                return true;
            return false;
        });
    });
</script>

<%-- title --%>
<title>属性管理</title>

<%-- body --%>
<div class="workingArea">

    <%-- 菜单 --%>
    <ol class="breadcrumb">
        <li><a href="admin_category_list">所有分类</a></li>
        <li><a href="admin_property_list?cid=${c.id}">${c.name}</a></li>
        <li class="active">属性管理</li>
    </ol>

    <%-- 属性表 --%>
    <div class="listDataTableDiv">
        <table class="table table-striped table-bordered table-hover  table-condensed">
            <thead>
                <tr class="success">
                    <th>ID</th>
                    <th>属性名称</th>
                    <th>编辑</th>
                    <th>删除</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${ps}" var="p">
                <tr>
                    <%-- ID --%>
                    <td>${p.id}</td>
                    <%-- 属性名称 --%>
                    <td>${p.name}</td>
                    <%-- 编辑 --%>
                    <td><a href="admin_property_edit?id=${p.id}"><span class="glyphicon glyphicon-edit"></span></a></td>
                    <%-- 删除 --%>
                    <td><a deleteLink="true" href="admin_property_delete?id=${p.id}"><span class="glyphicon glyphicon-trash"></span></a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <%-- 分页 --%>
    <div class="pageDiv">
        <%@include file="../include/admin/adminPage.jsp"%>
    </div>

    <%-- 新增属性 --%>
    <div class="panel panel-warning addDiv">
        <div class="panel-heading">新增属性</div>
        <div class="panel-body">
            <form method="post" id="addForm" action="admin_property_add">
                <table class="addTable">
                    <tr>
                        <td>属性名称</td>
                        <td><input id="name" name="name" type="text" class="form-control"></td>
                    </tr>
                    <tr class="submitTR">
                        <td colspan="2" align="center">
                            <input type="hidden" name="cid" value="${c.id}">
                            <button type="submit" class="btn btn-success">提 交</button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>

</div>

<%@include file="../include/admin/adminFooter.jsp"%>