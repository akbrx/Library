<?php

require_once("../koneksi.php");

// Periksa apakah data POST ada dan lengkap
if (
    !isset($_POST["post_nama"]) ||
    !isset($_POST["post_email"]) ||
    !isset($_POST["post_password"])
) {
    echo json_encode([
        'response' => false,
        'message' => 'Data tidak lengkap'
    ]);
    exit;
}

$username = $_POST["post_nama"];
$email = $_POST["post_email"];
$password = $_POST["post_password"];

// Periksa apakah email sudah terdaftar
$check_query = "SELECT * FROM member WHERE email = ?";
$check_stmt = mysqli_prepare($conn, $check_query);
mysqli_stmt_bind_param($check_stmt, "s", $email);
mysqli_stmt_execute($check_stmt);
$check_result = mysqli_stmt_get_result($check_stmt);

if (mysqli_num_rows($check_result) > 0) {
    echo json_encode([
        'response' => false,
        'message' => 'Email sudah terdaftar'
    ]);
    exit;
}

// Gunakan prepared statement untuk memasukkan data baru
$insert_query = "INSERT INTO member (username, email, password) VALUES (?, ?, ?)";
$insert_stmt = mysqli_prepare($conn, $insert_query);
mysqli_stmt_bind_param($insert_stmt, "sss", $username, $email, $password);

if (mysqli_stmt_execute($insert_stmt)) {
    echo json_encode([
        'response' => true,
        'message' => 'Registrasi berhasil',
        'payload' => [
            "fullname" => $username,
            "email" => $email,
            "password" => $password
        ]
    ]);
} else {
    echo json_encode([
        'response' => false,
        'message' => 'Registrasi gagal: ' . mysqli_error($conn)
    ]);
}

// Tutup prepared statement
mysqli_stmt_close($insert_stmt);

// Tutup koneksi database
mysqli_close($conn);

header('Content-Type: application/json');
?>
