package com.opensource.msgui.manager.pay.wechat.sdk;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WXPayUtil {
    private static final Random RANDOM = new SecureRandom();

    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilder documentBuilder = WXPayXmlUtil.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); idx++) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == 1) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception exception) {
            }
            return data;
        } catch (Exception ex) {
            getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }
    }

    public static String mapToXml(Map<String, String> data) throws Exception {
        Document document = WXPayXmlUtil.newDocument();
        Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty("encoding", "UTF-8");
        transformer.setOutputProperty("indent", "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception exception) {
        }
        return output;
    }

    public static String generateSignedXml(Map<String, String> data, String key) throws Exception {
        return generateSignedXml(data, key, WXPayConstants.SignType.MD5);
    }

    public static String generateSignedXml(Map<String, String> data, String key, WXPayConstants.SignType signType) throws Exception {
        String sign = generateSignature(data, key, signType);
        data.put("sign", sign);
        return mapToXml(data);
    }

    public static boolean isSignatureValid(String xmlStr, String key) throws Exception {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey("sign")) {
            return false;
        }
        String sign = data.get("sign");
        return generateSignature(data, key).equals(sign);
    }

    public static boolean isSignatureValid(Map<String, String> data, String key) throws Exception {
        return isSignatureValid(data, key, WXPayConstants.SignType.MD5);
    }

    public static boolean isSignatureValid(Map<String, String> data, String key, WXPayConstants.SignType signType) throws Exception {
        if (!data.containsKey("sign")) {
            return false;
        }
        String sign = data.get("sign");
        return generateSignature(data, key, signType).equals(sign);
    }

    public static String generateSignature(Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, WXPayConstants.SignType.MD5);
    }

    public static String generateSignature(Map<String, String> data, String key, WXPayConstants.SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.<String>toArray(new String[keySet.size()]);
        Arrays.sort((Object[]) keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (!k.equals("sign")) {
                if (((String) data.get(k)).trim().length() > 0) {
                    sb.append(k).append("=").append(((String) data.get(k)).trim()).append("&");
                }
            }
        }
        sb.append("key=").append(key);
        if (WXPayConstants.SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        if (WXPayConstants.SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        throw new Exception(String.format("Invalid sign_type: %s", new Object[]{signType}));
    }

    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; index++) {
            nonceChars[index] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(RANDOM.nextInt("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".length()));
        }
        return new String(nonceChars);
    }

    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString(item & 0xFF | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString(item & 0xFF | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    public static Logger getLogger() {
        Logger logger = LoggerFactory.getLogger("wxpay java sdk");
        return logger;
    }

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }
}
