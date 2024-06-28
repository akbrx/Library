<?php

include '../koneksi.php';
/**
 *  @var $connection PDO
 */

if ($_SERVER['REQUEST_METHOD'] != 'POST') {
    header('Content-Type: application/json');
    http_response_code(400);
    $reply['meta'] = [
        'message' => 'POST method required',
        'status' => false
    ];
    echo json_encode($reply);
    exit();
}

/**
 *  Input data
 */

$nama_buku = $_POST['nama_buku'] ?? '';
$deskripsi = $_POST['deskripsi'] ?? '';
$gambar = $_FILES['gambar'] ?? null;

/**
 * Validation
 */
$status = false;
$message = "";
$code = 200;
$isValidate = true;

if (empty($nama_buku)) {
    $message = "nama_buku buku harus diisi";
    $isValidate = false;
}
if (empty($deskripsi)) {
    $message = "Deskripsi harus diisi";
    $isValidate = false;
}
if (empty($gambar) || $gambar['error'] != UPLOAD_ERR_OK) {
    $message = "Gambar harus diunggah";
    $isValidate = false;
}

if (!$isValidate) {
    http_response_code(400);
    $reply['meta'] = [
        'message' => $message,
        'status' => $isValidate
    ];
    echo json_encode($reply);
    exit();
}

try {
    // Ensure the uploads directory exists and is writable
    $uploadDir = '../daftarbuku/';
    if (!is_dir($uploadDir)) {
        mkdir($uploadDir, 0755, true);
    }

    // Generate a unique file name to prevent overwriting
    $uniqueName = uniqid() . '-' . basename($gambar['name']);
    $uploadFile = $uploadDir . $uniqueName;

    if (!move_uploaded_file($gambar['tmp_name'], $uploadFile)) {
        throw new Exception("Gagal mengunggah gambar.");
    }

    // Insert into database
    $insertboking = "INSERT INTO buku (nama_buku, deskripsi, gambar) VALUES (:nama_buku, :deskripsi, :gambar)";
    $statement = $conn->prepare($insertboking);

    // Bind values
    $statement->bindValue(":nama_buku", $nama_buku);
    $statement->bindValue(":deskripsi", $deskripsi);
    $statement->bindValue(":gambar", $uniqueName);

    $execute = $statement->execute();
    if ($execute) {
        $message = "buku berhasil ditambahkan";
        $status = true;
    } else {
        $message = $statement->errorInfo();
        $code = 400;
    }

} catch (Exception $exception) {
    $message = $exception->getMessage();
    $status = false;
    $code = 400;
}

// HEADER API
header('Content-Type: application/json');
$reply['meta'] = [
    'message' => $message,
    'status' => $status
];
http_response_code($code);
echo json_encode($reply);

?>