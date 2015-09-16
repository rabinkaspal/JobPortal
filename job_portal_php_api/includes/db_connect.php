<?php

include_once('config.php');


$con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die('unable to connect');



$db = mysql_select_db(DB_DATABASE, $con) or die('cannot select database');



?>