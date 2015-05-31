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

<h2>Matches against <td><c:out value="${matches[0].opponent}"/></td> </h2>

<form action="${pageContext.request.contextPath}/matches/opponent/year" method="post">
  <input type="hidden" name="oppItem" value="${matches[0].opponent}" />
  <select name="seasonItem">
    <option value="2015">2014/15</option>
    <option value="2014">2013/14</option>
    <option value="2013">2012/13</option>
    <option value="2012">2011/12</option>
    <option value="2011">2010/11</option>
    <option value="2010">2009/10</option>
  </select>
  <select name="gamesItem">
    <option value="1">Regular</option>
    <option value="0">Playoff</option>
    <option value="2">All</option>
  </select>
  <input type="submit" value="Select">
</form>
    
<table border="1">
    <thead>
        <tr>
            <th>Date</th>
            <th>S-goa</th>
            <th>O-goa</th>
            <th>Best player</th>
            <th>Home</th>
        </tr>
    </thead>
    <tr>
        <c:forEach items="${matches}" var="match">
            <tr>
                <td><c:out value="${match.date}"/></td> 
                <td><c:out value="${match.spartaGoals}"/></td>
                <td><c:out value="${match.opponentGoals}"/></td>
                <td><c:out value="${match.bestPlayer}"/></td>
                <td><c:out value="${match.home}"/></td>
            </tr>
        </c:forEach>
    </tr>
</table>

</body>
</html>
