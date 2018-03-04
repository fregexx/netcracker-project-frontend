<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
<head>
    <title>Objects information</title>
</head>
    <body>
        <form:form method="post" commandName="obj" action="http://localhost:8080/backend/objects">
            <table border="1" bgcolor="#d3d3d3">
                <th>Object Information</th>

                <tr><td>Id</td><td><form:input path="id" readonly="true" /></td></tr>
                <tr><td>Name</td><td><form:input path="name" /></td></tr>
                <tr><td>TypeId</td><td><form:input path="typeId" readonly="true" /></td></tr>
                <tr><td>Description</td><td><form:input path="description" /></td></tr>

                <c:if test="${obj.values.size() != 0}">
                    <tr><td>Values</td></tr>
                <c:forEach items="${obj.values}" var="mapElement" varStatus="pStatus">
                    <tr>
                        <td>${mapElement.key}</td>
                        <td><form:input path="values['${mapElement.key}']" /></td>
                    </tr>
                </c:forEach>
                </c:if>

                <c:if test="${obj.dateValues.size() != 0}">
                     <tr><td>Date Values</td></tr>
                <c:forEach items="${obj.dateValues}" var="mapElement" varStatus="pStatus">
                    <tr>
                        <td>${mapElement.key}</td>
                        <td><form:input path="dateValues['${mapElement.key}']"/></td>
                    </tr>
                </c:forEach>
                </c:if>

                <c:if test="${obj.listValues.size() != 0}">
                    <tr><td>List Values</td></tr>
                <c:forEach items="${obj.listValues}" var="mapElement" varStatus="pStatus">
                    <tr>
                        <td>${mapElement.key}</td>
                        <td><form:input path="listValues['${mapElement.key}']"/></td>
                    </tr>
                </c:forEach>
                </c:if>

                <c:if test="${obj.references.size() != 0}">
                    <tr><td>References</td></tr>
                <c:forEach items="${obj.references}" var="mapElement" varStatus="pStatus">
                    <tr>
                        <td>${mapElement.key}</td>
                        <td><form:input path="references['${mapElement.key}']"/></td>
                    </tr>
                </c:forEach>
                </c:if>

                <tr>
                    <td colspan="2" height="40" align="center" bgcolor="#d3d3d3">
                        <input class="button" type="submit" name="button" value="Save"/>
                        <input class="button" type="submit" name="button" value="Delete"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </body>
</html>
