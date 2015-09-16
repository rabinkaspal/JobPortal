<?php
ob_start();
session_start();
include_once('includes/function.php');

header('Location:dashboard.php');
exit(0);


ob_flush();


?>