private void decFile(String fileLocation) throws IOException {

        //File to String
        
        File encFile=new File(fileLocation);
        InputStream inputStream=null;
        try {
            inputStream = new FileInputStream(encFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;

        while (!done) {
            String line=null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream.close();

        String encString=stringBuilder.toString();
        
        
        //Start Decryption

        byte[] data64= android.util.Base64.decode(encString, android.util.Base64.DEFAULT);
        if(data64.length < 17) {
            Toast.makeText(this,"Something went Wrong",Toast.LENGTH_SHORT).show();
        }
        
        byte[] ivBytes = Arrays.copyOfRange(data64, 0, 16);
        byte[] contentBytes = Arrays.copyOfRange(data64, 16, data64.length);
        
        try {
            Cipher ciper = Cipher.getInstance("AES/CBC/PKCS5Padding");
            String temp=getDecKey();

            SecretKeySpec keySpec = new SecretKeySpec(temp.getBytes("UTF-8"),"AES");
            IvParameterSpec iv = new IvParameterSpec(ivBytes,0, ciper.getBlockSize());

            ciper.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] decString=ciper.doFinal(contentBytes);

            long millis = System.currentTimeMillis();

            long seconds = millis / 1000;
            String path=this.getFilesDir().getAbsolutePath()+"/decFile_"+seconds+".pdf";
            
            File decFile = new File(path);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(decFile));
            bos.write(decString);
            bos.flush();
            bos.close();
            
            //send to the pdfviewer with the file location

            Intent intent = new Intent(this,PDFWebViewActivity.class);
            intent.putExtra("fromOffline",1);
            intent.putExtra("filelocation",decFile.getAbsoluteFile().toString());
            startActivity(intent);
        } catch (
                NoSuchAlgorithmException |
                        NoSuchPaddingException |
                        UnsupportedEncodingException |
                        InvalidAlgorithmParameterException |
                        InvalidKeyException |
                        IllegalBlockSizeException |
                        BadPaddingException ignored
        ) {
         
            ignored.printStackTrace();
        }
    }
