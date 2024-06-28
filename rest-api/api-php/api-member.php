<?php

include '../koneksi.php';

// Mengambil data dari tabel user
$userQuery = $conn->query("SELECT * FROM member");

// Inisialisasi array untuk menyimpan data user
$member = [];

// Mengambil setiap baris data dan menambahkannya ke array member
while ($row = $userQuery->fetch(PDO::FETCH_ASSOC)) {
    $member[] = $row;
}

// Menyiapkan respons JSON
$response = [
    'status' => true,
    'message' => 'Data pengguna berhasil diambil',
    'data' => $member
];

// Mengonversi respons menjadi format JSON
$json = json_encode($response, JSON_PRETTY_PRINT);

// Menampilkan JSON
echo $json;
?>