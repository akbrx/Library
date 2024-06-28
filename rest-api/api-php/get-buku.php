<?php

include '../koneksi.php';

/**
 * @var $conn PDO
 */

// Method selain GET tidak boleh
if ($_SERVER['REQUEST_METHOD'] != 'GET') {
    header('Content-Type: application/json');
    http_response_code(405); // Method Not Allowed
    $response = [
        'status' => 'error',
        'message' => 'GET method required',
        'data' => null
    ];
    echo json_encode($response);
    exit();
}

try {
    // Query untuk mengambil semua buku, diurutkan berdasarkan id secara descending
    $bukuQuery = "SELECT kode_buku as id, nama_buku, deskripsi, gambar as imageUrl FROM buku ORDER BY kode_buku DESC";
    $statement = $conn->prepare($bukuQuery);
    $statement->execute();

    // Ambil hasil query sebagai array asosiatif
    $buku = $statement->fetchAll(PDO::FETCH_ASSOC);

    // Set respons
    if ($buku) {
        $response = [
            'status' => 'success',
            'message' => 'Books retrieved successfully',
            'data' => $buku
        ];
        $code = 200;
    } else {
        $response = [
            'status' => 'success',
            'message' => 'No books found',
            'data' => []
        ];
        $code = 200; // Masih 200 karena ini bukan error, hanya data kosong
    }

} catch (PDOException $exception) {
    // Tangkap eksepsi PDOException
    $response = [
        'status' => 'error',
        'message' => 'Database error: ' . $exception->getMessage(),
        'data' => null
    ];
    $code = 500; // Internal Server Error
}

// Header respons JSON
header('Content-Type: application/json');
http_response_code($code);
echo json_encode($response);