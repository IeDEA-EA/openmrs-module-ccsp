<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
  <li class="first"><a
                        href="${pageContext.request.contextPath}/admin"><spring:message
                        code="admin.title.short" /></a></li>

  <li
      <c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
      <a
          href="${pageContext.request.contextPath}/module/ccsp/manage.form"><spring:message
          code="ccsp.manage" /></a>
    </li>
    
    <!-- Add further links here -->
  </ul>
  <h2>
    <spring:message code="ccsp.title" />
  </h2>
