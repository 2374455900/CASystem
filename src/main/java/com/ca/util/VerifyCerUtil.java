package com.ca.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyCerUtil {

    private static final String timePattern = "(\\d*年\\d*月\\d*日 \\d{2}:\\d{2}:\\d{2})";

    /**
     * 验证证书有效性
     * @param cer 证书内容字串
     * @return 如果有效，返回true；否则，返回false
     * @throws ParseException
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static boolean verify(String cer) throws ParseException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String serial_number = parseSerialNumber(cer);
        String validTimeFrom = parseValidTimeFrom(cer);
        String validTimeTo = parseValidTimeTo(cer);
        String sign = parseSign(cer);
        String publicKey = parsePublicKey(cer);
        // 验证证书有效期
        if (!judgeTime(validTimeFrom, validTimeTo)) {
            return false;
        }
        // 验证证书签名
        if (!RSASignature.doCheck(publicKey, sign, KeyUtil.loadPublicKeyByFile("D:\\DriveY"
                + "\\IntelliJ\\cryptotw2\\out\\artifacts\\cryptotw2_Web_exploded\\download\\pk.key"), "utf-8")) {
            return false;
        }
        // 解析CRL，验证证书是否已被撤销
        List<String> mycrlList = getNode("D:\\DriveY\\IntelliJ\\cryptotw2\\out\\artifacts"
                + "\\cryptotw2_Web_exploded\\download\\crl.xml");
        for (String deprecated : mycrlList) {
            if (deprecated.equals(serial_number)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证证书有效性
     * @param certificate 证书文件
     * @return 如果有效，返回true；否则，返回false
     * @throws IOException
     * @throws ParseException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public static boolean verify(File certificate) throws IOException, ParseException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(new FileInputStream(certificate), "GBK"));
        String cerLine = "";
        String cer = "";
        while ((cerLine = bufferedReader.readLine()) != null) {
            cer += cerLine + "\n";
        }
        bufferedReader.close();
        return verify(cer);
    }

    /**
     * 内部工具方法，解析crl.xml文件内容
     * @param url crl.xml文件url路径
     * @return 将xml文件解析为一个String列表，其中包含xml的每一条目信息
     */
    private static List<String> getNode(String url) {
        List<String> mycrlList = new ArrayList<>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(url);
            NodeList nodeList = document.getElementsByTagName("crl");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE
                            && childNodes.item(j).getNodeName().equals("serial_number")) {
                        mycrlList.add(childNodes.item(j).getFirstChild().getNodeValue());
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mycrlList;
    }

    /**
     * 验证证书有效期
     * @param validTimeFrom 证书的有效期起字段
     * @param validTimeTo 证书的有效期至字段
     * @return 如果证书时间有效，返回true；否则，返回false
     * @throws ParseException
     */
    private static boolean judgeTime(String validTimeFrom, String validTimeTo) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date from = simpleDateFormat.parse(validTimeFrom);
        Date to = simpleDateFormat.parse(validTimeTo);
        Date now = new Date();
        if (now.before(from) || now.after(to)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 解析证书的序列号
     * @param cer 证书文件内容字串
     * @return 证书序列号字串
     */
    private static String parseSerialNumber(String cer) {
        String serial_number = null;
        Pattern serialNumberPattern = Pattern.compile("Serial Number: " + "(\\w*)\\n");
        Matcher serialNumberMatch = serialNumberPattern.matcher(cer);
        while (serialNumberMatch.find()) {
            serial_number = serialNumberMatch.group(1);
        }
        return serial_number;
    }

    /**
     * 解析证书有效期起字串
     * @param cer 证书内容字串
     * @return 证书有效期起字串
     */
    private static String parseValidTimeFrom(String cer) {
        String validTimeFrom = null;
        Pattern validTimeFromPattern = Pattern.compile("Valid Time From: " + timePattern);
        Matcher validTimeFromMatch = validTimeFromPattern.matcher(cer);
        while (validTimeFromMatch.find()) {
            validTimeFrom = validTimeFromMatch.group(1);
        }
        return validTimeFrom;
    }

    /**
     * 解析证书有效期至字串
     * @param cer 证书内容字串
     * @return 证书有效期至字串
     */
    private static String parseValidTimeTo(String cer) {
        String validTimeTo = null;
        Pattern validTimeToPattern = Pattern.compile("Valid Time To: " + timePattern);
        Matcher validTimeToMatch = validTimeToPattern.matcher(cer);
        while (validTimeToMatch.find()) {
            validTimeTo = validTimeToMatch.group(1);
        }
        return validTimeTo;
    }

    /**
     * 解析证书公钥字串
     * @param cer 证书内容字串
     * @return 证书公钥字串
     */
    private static String parsePublicKey(String cer) {
        String publicKey = null;
        Pattern publicKeyPattern = Pattern.compile("Public Key: " + "(.*)\\n");
        Matcher publicKeyMatch = publicKeyPattern.matcher(cer);
        while (publicKeyMatch.find()) {
            publicKey = publicKeyMatch.group(1);
        }
        return publicKey;
    }

    /**
     * 解析证书签名字串
     * @param cer 证书内容字串
     * @return 证书签名字串
     */
    private static String parseSign(String cer) {
        String sign = null;
        Pattern signPattern = Pattern.compile("Sign: " + "(.*)\\n");
        Matcher signMatch = signPattern.matcher(cer);
        while (signMatch.find()) {
            sign = signMatch.group(1);
        }
        return sign;
    }
}
