<?php
include_once("includes/db_connect.php");

$response = array();

//js_name(firstname, lastname), phone, address, email, likes, username, password, skill_set
      $username = $_GET['username'];
      $password  = md5($_GET['password']);
      $cat_likes = $_GET['cat_likes'];
      $firstname = $_GET['firstname'];
      $lastname = $_GET['lastname'];
      $email = $_GET['email'];
      $phone = $_GET['phone'];
      $location = $_GET['location'];
      $keyskill = $_GET['keyskills'];

$js_name = $firstname . " " . $lastname;

$insert_id = "";


$q = "insert into jobseeker(js_name, phone, address, email, likes, username, password, skill_set)
      values('$js_name', '$phone', '$location', '$email', '$cat_likes', '$username', '$password', '$keyskill')";

$result = mysql_query($q) or die( 'error' . mysql_error() );

$insert_id = mysql_insert_id();

if(mysql_affected_rows() == 1){
      $response["success"] = 1;
      $response['js_id'] = $insert_id;
      $response['message'] = "Registration successful.";
      
      echo json_encode($response, JSON_PRETTY_PRINT);

}else{
      $response["success"] = 0;
      $response['message'] = "Could not Register user at the moment. Try again later.";

      echo json_encode($response, JSON_PRETTY_PRINT);
}
      
?>