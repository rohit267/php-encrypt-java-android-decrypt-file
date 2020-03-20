<?php

$data=file_get_contents($file_address);  // converts the file to string
$encData=encFile($data);                // encrypts the string
$fullFileName="temp.pdf";
file_put_contents($fullFileName, $encData);   // makes a file with the encrypted string.



function encFile($data){
  $iv = openssl_random_pseudo_bytes(16,$secure);
  if (false === $iv) {
    throw new \RuntimeException('iv generation failed');
  }

  $data = $iv . openssl_encrypt($data, 'AES-128-CBC', $your_key, OPENSSL_RAW_DATA, $iv);
  $data=base64_encode($data);
  return $data;
}

?>
