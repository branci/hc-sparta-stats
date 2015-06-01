<%-- 
    Document   : list
    Created on : May 28, 2015, 11:43:32 PM
    Author     : Branislav Smik <xsmik @fi.muni>
--%>

<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>  
<h1>HC Sparta Praha</h1>

<form action="${pageContext.request.contextPath}/players" method="get">
  <input type="submit" value="Players">
</form>
<form action="${pageContext.request.contextPath}/matches" method="get">
  <input type="submit" value="Opponents">
</form>

<h2>Players</h2>

<%--<form action="${pageContext.request.contextPath}/players/player/year" method="post">
    <input type="hidden" name="idItem" value="${players.id}" />
  <select name="seasonItem">
    <option value="2015">2014/15</option>
    <option value="2014">2013/14</option>
    <option value="2013">2012/13</option>
    <option value="2012">2011/12</option>
    <option value="2011">2010/11</option>
    <option value="2010">2009/10</option>
  </select>
  <input type="submit" value="Select year">
</form>--%>
    
<table border="1">
    <thead>
        <tr>
            <th>Name</th>
            <th>Age</th>
            <th>Height</th>
            <th>Weight</th>
            <th>Num</th>
            <th>Pos</th>
            <th>1st 3rd</th>
            <th>2nd 3rd</th>
            <th>3rd 3rd</th>
            <th>Over time</th>
        </tr>
    </thead>
    <tr>
        <td><c:out value="${player.name}"/></td>
        <td><c:out value="${player.age}"/></td>
        <td><c:out value="${player.height}"/></td>
        <td><c:out value="${player.weight}"/></td>
        <td><c:out value="${player.playerNum}"/></td>
        <td>
            <c:choose>
                <c:when test="${player.position=='1'}">Back</c:when>
                <c:otherwise>Front</c:otherwise>
            </c:choose>    
        </td>       
        <td><c:out value="${player.firstThird}"/></td>
        <td><c:out value="${player.secondThird}"/></td>
        <td><c:out value="${player.thirdThird}"/></td>
        <td><c:out value="${player.extraTime}"/></td>
    </tr>
</table>

</body>
</html>
