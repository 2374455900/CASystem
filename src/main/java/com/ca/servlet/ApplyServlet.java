package com.ca.servlet;

import com.ca.dao.CertificateDao;
import com.ca.util.Ende;
import com.ca.util.KeyUtil;
import com.ca.util.RSASignature;
import com.ca.util.SHADigest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.DigestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "ApplyServlet", value = "/ApplyServlet")
public class ApplyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String upload_path = this.getServletContext().getRealPath("/upload");
        File file = new File(upload_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Map<String, String> parameters = new HashMap<>();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> fileItemList = servletFileUpload.parseRequest(request);
                for (FileItem fileItem : fileItemList) {
                    if (!fileItem.isFormField()) {
                        String file_name = fileItem.getName();
                        if (file_name.isEmpty()) {
                            return;
                        }
                        InputStream inputStream = fileItem.getInputStream();
                        String line = "";
                        String content = "";
                        BufferedReader bufferedReader =
                                new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        while ((line = bufferedReader.readLine()) != null) {
                            content += line;
                        }
                        bufferedReader.close();
                        upload_path +=
                                "\\" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                                        + ".key";
                        BufferedWriter bufferedWriter =
                                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(upload_path))));
                        bufferedWriter.write(content);
                        parameters.put("public_key", content);
                        bufferedWriter.flush();
                        bufferedWriter.close();
                        fileItem.delete();
                    } else {
                        parameters.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
        try {
            PrivateKey privateKey =
                    Ende.loadPrivateKeyByStr("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIsXFT8gGPB/RSMaxfL93HpCJEfqm+9mga+1S0OEZPVarRWBraHsJQrA1rM3z+XxiSwqDPTUmogSHw1QAY4Vje1UyT+w2UyBioJbJHDpC8y2yksN7d3Mmf/zeoCwyrfCvc6jGCTXjcBdXpiVeEO+KlirBqw/uW7BGtuB1MA0i2XfAgMBAAECgYBbfR94mkBNUSnE4YN7RgiUUpVSyLsxSZfcX7/V9WwDB1X6Y4Y2kAH9hMK0t+2ELtAvwKktEftjrafHNe0P7JWhomzE37/qoiJ2bpw9+P5Obnkq9nYhZKRR7mEz5Sr8IH3lnRqXehtiyxyvT4edcZkWpIMRTvWsGAgoPe28rNt5YQJBAMCCdFIOvldIfxlCvW4dWzxqMdk5lbRcq5OVcS0UIcRSV8yLokQFXpySgmiLyfvXaGe0+almQMgZnSvgF5MnI10CQQC49nAUVzC6LbkjOQS2aRcQh1T/ZKJkXKVulH8M8sucQUmv5kAVebhYr2yv9XLvNF2F8JRJ15d0krymETInAjZrAkBoNTXqRXjbnq7OacZJGTMOHR4mzHkxTQjDtx2wnTk6IKjOXLfVwmJYtyZImYMZBJ3LpbeP734Z02O1IHUifwkxAkBU8FboAGJQHU837adMXVZKMNvHrN8mV6Vg8rClsZnvV8wPCx3CvvL5RxYSeBUf5FxOdfyjLG5RClG3sY3mfA2hAkEAmByH9dFOd4o6WfyTH8kGK5Kaagy55n3YsjcnEvib6rsciFlAaPRAqs+IK1IqQET+JfDM5edUolrG2PRaKPchbg==");
            parameters.put("organization", new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(parameters.get("organization"))), "utf-8"));
            parameters.put("juridical_person", new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(parameters.get("juridical_person"))), "utf-8"));
            parameters.put("charge_person", new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(parameters.get("charge_person"))), "utf-8"));
            parameters.put("charge_mail", new String(Ende.decrypt((RSAPrivateKey) privateKey, Base64.getDecoder().decode(parameters.get("charge_mail"))), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!SHADigest.getDigest(parameters.get("organization")).equalsIgnoreCase(parameters.get("sign_organization"))
                || !SHADigest.getDigest(parameters.get("juridical_person")).equalsIgnoreCase(parameters.get("sign_juridical_person"))
                || !SHADigest.getDigest(parameters.get("charge_person")).equalsIgnoreCase(parameters.get("sign_charge_person"))
                || !SHADigest.getDigest(parameters.get("charge_mail")).equalsIgnoreCase(parameters.get("sign_charge_mail"))) {
            request.setAttribute("isApplied", "报文可能被损毁！请报警！");
            request.getRequestDispatcher("apply.jsp").forward(request, response);
            return;
        }
        String serial_number =
                getSerialNumber(new String[]{new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), parameters.get("organization")});
        List<String> cerLines = new ArrayList<>();
        cerLines.add("Serial Number: " + serial_number);
        cerLines.add("Sign Algorithm: sha1RSA");
        cerLines.add("Encrypt Algorithm: sha256");
        cerLines.add(
                "GothamCityTrust RSA CA 2019, " + "www.tofushen.com, " + "Gotham City Trust, " + "CN");
        String[] valid_time = getTTL(Integer.parseInt(parameters.get("valid_time")));
        cerLines.add("Valid Time From: " + valid_time[0]);
        cerLines.add("Valid Time To: " + valid_time[1]);
        String organization = parameters.get("organization");
        cerLines.add("User: " + organization);
        cerLines.add("Public Key: " + parameters.get("public_key"));
        //        cerLines.add("Sign: " + getSign(SHADigest.getDigest(parameters.get("public_key"))));
        cerLines.add("Sign: "
                + RSASignature.sign(parameters.get("public_key"), KeyUtil.loadPrivateKeyByFile(this.getServletContext().getRealPath("/download/sk.key")), "utf-8"));
        String upload_cer_path = makeCertificate(cerLines);
        String registration_number = parameters.get("registration_number");
        String juridical_person = parameters.get("juridical_person");
        String charge_person = parameters.get("charge_person");
        String charge_phone = parameters.get("charge_phone");
        CertificateDao certificateDao = new CertificateDao();
        boolean isApplied = certificateDao.isApplied(organization, registration_number);
        if (isApplied) {
            request.setAttribute("isApplied", "该组织结构已持有有效证书！");
            request.getRequestDispatcher("apply.jsp").forward(request, response);
        } else {
            certificateDao.register(serial_number, organization, registration_number, upload_cer_path, valid_time, juridical_person, charge_person, charge_phone, request.getSession().getAttribute("username").toString());
        }
        request.setAttribute("serial_number", cerLines.get(0).substring(
                cerLines.get(0).indexOf(":") + 2));
        request.setAttribute("organization", organization);
        request.setAttribute("charge_person", parameters.get("charge_person"));
        request.getRequestDispatcher("apply_success.jsp").forward(request, response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
    private String makeCertificate(List<String> cerLines) {
        String cer_path = this.getServletContext().getRealPath("/download/");
        String cer_name =
                cerLines.get(0).substring(cerLines.get(0).indexOf(":") + 2);
        try {
            BufferedWriter bufferedWriter =
                    new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(
                            cer_path + cer_name))));
            for (int i = 0; i < cerLines.size(); i++) {
                System.out.println(cerLines.get(i));
                bufferedWriter.write(cerLines.get(i));
                bufferedWriter.write("\n");
                bufferedWriter.flush();
            }
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cer_path + cer_name;
    }
    private String getSerialNumber(String[] message) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < message.length; i++) {
            stringBuilder.append(message[i]);
        }
        String serialNumber = DigestUtils.md5DigestAsHex(stringBuilder.toString().getBytes());
        return serialNumber;
    }
    private String[] getTTL(int ttl) {
        String present_time =
                new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Calendar.getInstance().getTime());
        Calendar temp = Calendar.getInstance();
        temp.add(Calendar.YEAR, ttl);
        String future_time = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(temp.getTime());
        return new String[]{present_time, future_time};
    }
    private String getSign(String message) {
        byte[] plaintext = new byte[0];
        plaintext = message.getBytes(StandardCharsets.UTF_8);
        String file_path = this.getServletContext().getRealPath("/download/sk.key");
        File file = new File(file_path);
        String line = "";
        String key_file = "";
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            while ((line = bufferedReader.readLine()) != null) {
                key_file += line;
            }
            bufferedReader.close();
            inputStreamReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            byte[] buffer = Base64.getDecoder().decode(key_file);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] ciphertext = cipher.doFinal(plaintext);
            //            return HexStringUtil.getInstance().bytesToHexString(ciphertext);
            return Base64.getEncoder().encodeToString(ciphertext);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
