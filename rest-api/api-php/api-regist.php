<?php

require_once("../koneksi.php");

// Periksa apakah data POST ada dan lengkap
if (
    !isset($_POST["username"]) ||
    !isset($_POST["email"]) ||
    !isset($_POST["password"])
) {
    echo json_encode([
        'response' => false,
        'message' => 'Data tidak lengkap'
    ]);
    exit;
}

$username = $_POST["username"];
$email = $_POST["email"];
$password = $_POST["password"];

try {
    // Periksa apakah email sudah terdaftar
    $check_query = "SELECT * FROM member WHERE email = :email";
    $check_stmt = $conn->prepare($check_query);
    $check_stmt->bindParam(':email', $email);
    $check_stmt->execute();
    $check_result = $check_stmt->fetch(PDO::FETCH_ASSOC);

    if ($check_result) {
        echo json_encode([
            'response' => false,
            'message' => 'Email sudah terdaftar'
        ]);
        exit;
    }

    // Gunakan prepared statement untuk memasukkan data baru
    $insert_query = "INSERT INTO member (username, email, password) VALUES (:username, :email, :password)";
    $insert_stmt = $conn->prepare($insert_query);
    $insert_stmt->bindParam(':username', $username);
    $insert_stmt->bindParam(':email', $email);
    $insert_stmt->bindParam(':password', $password);

    if ($insert_stmt->execute()) {
        echo json_encode([
            'response' => true,
            'message' => 'Registrasi berhasil',
            'payload' => [
                "fullname" => $username,
                "email" => $email
            ]
        ]);
    } else {
        echo json_encode([
            'response' => false,
            'message' => 'Registrasi gagal'
        ]);
    }

} catch (PDOException $e) {
    echo json_encode([
        'response' => false,
        'message' => 'Registrasi gagal: ' . $e->getMessage()
    ]);
}

// Tutup koneksi database

header('Content-Type: application/json');
?>
