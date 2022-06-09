<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal.id == 0 ? "Add" : "Edit"} Meal</h2>
<form method="POST" action='meals'>
    <input type="hidden" name="id" value="${meal.id}"> <br/>
    DateTime : <input type="datetime-local" value="${meal.dateTime}" name="dateTime" required> <br/>
    <br/>
    Description: <input type="text" value="${meal.description}" size=50 name="description" required> <br/>
    <br/>
    Calories:<input type="number" value="${meal.calories}" name="calories" required> <br/>
    <br/>
    <button type="submit">Save</button>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>