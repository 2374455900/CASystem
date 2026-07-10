package com.ca.servlet;

import com.ca.dao.CertificateDao;
import com.ca.dao.LoginDao;
import com.ca.model.Mycrl;
import com.ca.model.User;
import com.ca.util.DbUtil;
import com.ca.util.Ende;
import com.ca.util.SHADigest;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "revokeCerServlet", value = "/revokeCerServlet")
public class revokeCerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        CertificateDao certificateDao = new CertificateDao();
        String serial_number = request.getParameter("serial_number");
        String revoke_pwd = request.getParameter("revoke_pwd");
        String sign_serial_number = request.getParameter("sign_serial_number");
        String sign_revoke_pwd = request.getParameter("sign_revoke_pwd");
        PrivateKey privateKey;
        try {
            privateKey =
                    Ende.loadPrivateKeyByStr("MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEA" +
                            "AoGBAKIRy1Ayqp5COdLW9ToFc5pVOGFqdRw5PruPOLDbk25VQoQeNmCzX3g47PkhnTD1" +
                            "Qa3Ofn2ikHDLfb3lQpv2vZAsxdMVaHzzN5nsoXgss16ydiHDLQwheSdvqXGcnsN+fPk" +
                            "r6+AYl3Q7QuZNQkND7GeWCmSHvHaNWsMmCVxTkzRpAgMBAAECgYEAimdFyEwsdpA5z" +
                            "zsxGoaTTaYfStnd/udIEmZh1G7/fYakEi225GfqTMHYZXz2P1wC5cnlLadJUHoG/M" +
                            "cvVf+lq3+6ph2pA6wHI4bvMSPS9SoLkcLNiggcqAKnySu2X9ZNCEza+NGMj726Ixs" +
                            "yxB7+3PCzSfiiwzv3JvxkyA614AECQQDen/yaEpO0F+mh4aQe6e9pj2xjJuLb7so" +
                            "oDDFOJaotxnY/1aaNdongbqA2j7HeBf6isv19F+w+nWKWXLVoOTQBAkEAul3Hsm" +
                            "FH5z8fwiv+B9AGrK6vfbadBqlTeK1gMxqDPDUuDMNbW4rPRlCXEBaoUMSFRDFNM" +
                            "rfFPejnn5tu1pzgaQJBANvj8kDMcI/FvsJieRT/w7XkMA6PbiwF5C9CO8EQetLT" +
                            "4CCVCvlXSEAhhKXfsLO4ABb77F0OsA34rlQOJjBXsAECQQCS+cS07D2NpN3B/3n" +
                            "O5YNuCjICfdMm3sEiqfD1PJKFGBeiHytcbYN8G7CXEpdZYzMKjaspNX8LjTOmTy" +
                            "nBfWUJAkEAtWOybjlu0zLSiC3gTTyK1Q+aS1eLji/a9l/XdG7L9dQ0fEpfB8XTt" +
                            "UQdmmt3vDAe3qwyV5t/vJkfZOrHY+yIYg==");
            serial_number =
                    new String(Ende.decrypt((RSAPrivateKey) privateKey
                            , Base64.getDecoder().decode(serial_number)), "utf-8");
            revoke_pwd =
                    new String(Ende.decrypt((RSAPrivateKey) privateKey
                            , Base64.getDecoder().decode(revoke_pwd)), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!SHADigest.getDigest(serial_number).equalsIgnoreCase(sign_serial_number)
                || !SHADigest.getDigest(revoke_pwd).equalsIgnoreCase(sign_revoke_pwd)) {
            request.setAttribute("msg", "消息已损坏！请报警！");
            request.getRequestDispatcher("revoke_cer.jsp").forward(request, response);
            return;
        }
        String username = request.getSession().getAttribute("username").toString();
        LoginDao userDao = new LoginDao();
        User presentUser = new User();
        DbUtil dbUtil = new DbUtil();
        Connection connection = null;
        try {
            connection = dbUtil.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        presentUser.setUsername(username);
        presentUser.setPassword(revoke_pwd);
        try {
            if (userDao.login(connection, presentUser) != null) {
                request.setAttribute("msg", "撤销身份验证密码错误！请您确认登录信息后输入正确的验证密码！");
                request.getRequestDispatcher("cancel_the_certificate.jsp").forward(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] revoke_msg = certificateDao.revoke(serial_number);
        if (revoke_msg != null) {
            request.setAttribute("msg", "此证书已被成功撤销！");
            List<Mycrl> crlList = certificateDao.getCrl();
            ServletContext servletContext = this.getServletContext();
            String file_path = servletContext.getRealPath("/download");
            File file = new File(file_path);
            if (!file.exists()) {
                file.mkdir();
            }
            String file_name = file_path + "/" + "crl.xml";
            file = new File(file_name);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("<?xml version='1.0' encoding='gbk'?>");
            bufferedWriter.write("<crls>\n");
            for (Mycrl mycrl : crlList) {
                bufferedWriter.write("<crl>\n");
                bufferedWriter.write(
                        "<serial_number>" + mycrl.getSerial_number() + "</serial_number>\n");
                bufferedWriter.write(
                        "<organization>" + mycrl.getOrganization() + "</organization" + ">\n");
                bufferedWriter.write("<start_time>" + mycrl.getStart_time() + "</start_time>\n");
                bufferedWriter.write("<end_time>" + mycrl.getEnd_time() + "</end_time>\n");
                bufferedWriter.write("</crl>\n");
                bufferedWriter.flush();
            }
            bufferedWriter.write("</crls>\n");
            bufferedWriter.close();
            fileWriter.close();
            request.setAttribute("serial_number", serial_number);
            request.setAttribute("organization", revoke_msg[1]);
            request.getRequestDispatcher("revoke_result.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "此证书不存在或已失效！");
            request.getRequestDispatcher("revoke_cer.jsp").forward(request, response);
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
