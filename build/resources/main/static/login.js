function login(evt) {
  evt.preventDefault();
  var form = evt.target.form;
  $.post("/api/login",
         { name: form["email"].value,
           pwd: form["pwd"].value })

}

function logout(evt) {
  evt.preventDefault();
  $.post("/app/logout")

}