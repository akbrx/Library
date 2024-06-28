<?php

// Menampilkan seluruh produk

include '../koneksi.php';

/**
 * @var $conn PDO
 */

// Method selain GET tidak boleh
if ($_SERVER['REQUEST_METHOD'] != 'GET') {
    header('Content-Type: application/json');
    http_response_code(405); // Method Not Allowed
    $reply['meta'] = [
        'message' => 'GET method required',
        'status' => false
    ];
    echo json_encode($reply);
    exit();
}

// Inisialisasi variabel respons
$status = false;
$message = "";
$code = 200;

try {
    // Query untuk mengambil semua produk, diurutkan berdasarkan id secara descending
    $userQuerry = "SELECT * FROM member ORDER BY id DESC";
    $statement = $conn->prepare($userQuerry);
    $statement->execute();

    // Ambil hasil query sebagai array asosiatif
    $member = $statement->fetchAll(PDO::FETCH_ASSOC);

    // Set respons jika produk ditemukan
    if ($member) {
        $reply['data_user'] = $member;
        $message = "data member ditemukan";
        $status = true;
        $code = 200;
    } else {
        // Set respons jika tidak ada produk yang ditemukan
        $reply['data_user'] = [];
        $message = "data member tidak ditemukan";
        $status = false;
        $code = 404; // Not Found
    }

} catch (PDOException $exception) {
    // Tangkap eksepsi PDOException
    $reply['data_user'] = null;
    $message = $exception->getMessage();
    $status = false;
    $code = 500; // Internal Server Error
}

// Header respons JSON
header('Content-Type: application/json');
$reply['meta'] = [
    'message' => $message,
    'status' => $status
];
http_response_code($code);
echo json_encode($reply);