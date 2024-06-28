<?php

// Pastikan untuk melakukan koneksi ke database atau menginclude file koneksi
include '../koneksi.php';

// Periksa metode request, hanya menerima GET
if ($_SERVER['REQUEST_METHOD'] != 'GET') {
    header('Content-Type: application/json');
    http_response_code(400);
    $reply['meta'] = [
        'message' => 'GET method required',
        'status' => false
    ];
    echo json_encode($reply);
    exit();
}

try {
    // Query untuk mengambil data dari tabel loans
    $query = "SELECT * FROM loans";
    $statement = $conn->prepare($query);
    $statement->execute();

    // Ambil semua data sebagai array asosiatif
    $loans = $statement->fetchAll(PDO::FETCH_ASSOC);

    if ($loans) {
        $reply['meta'] = [
            'message' => 'Data retrieved successfully',
            'status' => true
        ];
        $reply['data'] = $loans;
        http_response_code(200);
    } else {
        $reply['meta'] = [
            'message' => 'No data found',
            'status' => false
        ];
        http_response_code(404);
    }
} catch (Exception $exception) {
    $reply['meta'] = [
        'message' => $exception->getMessage(),
        'status' => false
    ];
    http_response_code(500);
}

// HEADER API
header('Content-Type: application/json');
echo json_encode($reply);

?>
