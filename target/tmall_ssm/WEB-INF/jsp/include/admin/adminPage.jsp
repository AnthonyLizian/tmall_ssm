<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%-- 点击页面方法 --%>
<script>
$(function(){
	$("ul.pagination li.disabled a").click(function(){
		return false;
	});
});
</script>

<nav>
  <ul class="pagination">
    <%-- 首页 --%>
    <%-- 如果当前页没有上一页，"首页"不可点击 --%>
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a href="?start=0${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&laquo;</span>
      </a>
    </li>

    <%-- 上一页 --%>
    <%-- 如果当前页没有上一页，"上一页"不可点击 --%>
    <li <c:if test="${!page.hasPreviouse}">class="disabled"</c:if>>
      <a href="?start=${page.start-page.count}${page.param}" aria-label="Previous" >
        <span aria-hidden="true">&lsaquo;</span>
      </a>
    </li>

    <%-- 遍历页面 --%>
    <c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">
        <%-- 如果一共只有一页，该页不可点击 --%>
        <li <c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
            <a href="?start=${status.index*page.count}${page.param}"
            <%-- 判断是否为当前页 --%>
            <c:if test="${status.index*page.count==page.start}">class="current"</c:if>
            >${status.count}</a>
        </li>
    </c:forEach>

    <%-- 下一页 --%>
    <%-- 如果当前页没有下一页，"下一页"不可点击 --%>
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.start+page.count}${page.param}" aria-label="Next">
        <span aria-hidden="true">&rsaquo;</span>
      </a>
    </li>

    <%-- 末页 --%>
    <%-- 如果当前页没有下一页，"末页"不可点击 --%>
    <li <c:if test="${!page.hasNext}">class="disabled"</c:if>>
      <a href="?start=${page.last}${page.param}" aria-label="Next">
        <span aria-hidden="true">&raquo;</span>
      </a>
    </li>

  </ul>
</nav>
