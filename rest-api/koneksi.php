<?php
$host = 'localhost'; // Sesuaikan dengan host database Anda
$username = 'root'; // Sesuaikan dengan username database Anda
$password = ''; // Sesuaikan dengan password database Anda
$database = 'library'; // Sesuaikan dengan nama database Anda
$dsn = "mysql:host={$host};dbname={$database}";

$conn = null;

try {
    $conn = new PDO ($dsn, $username, $password);

    $conn -> setAttribute (PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (Exception $e) {
    echo "Connection failed: " . $e->getMessage();
}
?>