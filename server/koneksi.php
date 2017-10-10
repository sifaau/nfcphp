<?php
$host		= "localhost";
$username	= "root";
$passwd		= "";
$db_name	= "nfc";

$koneksi = mysqli_connect($host,$username,$passwd,$db_name) or die("Error " . mysqli_error($koneksi));
?>