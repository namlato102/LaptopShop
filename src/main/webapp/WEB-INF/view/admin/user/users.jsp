<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Users</title>
    <%--Bootstrap CSS--%>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

    <%--jquery--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  </head>

  <body>
    <div class="container mt-5">
      <div class="row">
        <div class="col-12 mx-auto">
          <div class="d-flex justify-content-between">
            <h3>Users</h3>
            <a href="<c:url value="/admin/user/create"/>" class="btn btn-primary">Create new user</a>
          </div>

          <hr />
          <table class="table table-bordered table-hover">
            <thead>
            <tr>
              <th>ID</th>
              <th>Email</th>
              <th>Full Name</th>
              <th>Action</th>
            </tr>
            </thead>

            <tbody>
              <c:forEach var="user" items="${users}">
                <tr>
                  <th>${user.id}</th>
                  <td>${user.email}</td>
                  <td>${user.fullName}</td>
                  <td>
                    <a href="/admin/user/${user.id}" class="btn btn-success">View</a>
                    <a href="/admin/user/update/${user.id}" class="btn btn-warning  mx-2">Update</a>
                    <a href="/admin/user/delete/${user.id}" class="btn btn-danger">Delete</a>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </body>
</html>