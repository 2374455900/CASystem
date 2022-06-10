package com.ca.dao;

import com.ca.model.CertItem;
import com.ca.model.Mycrl;
import com.ca.model.Page;
import com.ca.util.JDBCUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CertificateDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());

    /**
     * 向数据库中申请证书
     *
     * @param serial_number       证书序列号
     * @param organization        申请组织机构
     * @param registration_number 工商注册号
     * @param file_path           服务器下证书路径
     * @param valid_time          证书的有效期，valid_time[0]为有效期起，valid_time[1]为有效期止
     * @param juridical_person    法人姓名
     * @param charge_person       经办人
     * @param charge_phone        经办人电话
     * @param username            登录者用户名
     */
    public void register(String serial_number, String organization, String registration_number, String file_path, String[] valid_time, String juridical_person, String charge_person, String charge_phone, String username) {
        Object[] insertArgs =
                {serial_number, organization, registration_number, file_path, valid_time[0], valid_time[1], juridical_person, charge_person, charge_phone, username};
        Object[] deleteArgs = {organization, registration_number};
        String sql1 = "INSERT INTO certificate(serial_number, organization, "
                + "registration_number, file_path, " + "start_time, end_time, juridical_person, "
                + "charge_person, charge_phone, username) " + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        String sql2 = "DELETE FROM crl WHERE organization = ? or registration_number = ?";
        template.update(sql1, insertArgs);
        template.update(sql2, deleteArgs);
    }

    /**
     * 获取证书文件的服务器下路径
     *
     * @param serial_number 证书序列号
     * @return 返回数据库中存储的路径信息
     */
    public String getFilePath(String serial_number) {
        String sql = "SELECT FILE_PATH FROM CERTIFICATE WHERE SERIAL_NUMBER = ?";
        Object[] a = {serial_number};
        String file_path = null;
        try {
            file_path = template.queryForObject(sql, a, String.class);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
        return file_path;
    }

    /**
     * 撤销证书
     *
     * @param serial_number 撤销证书的序列号
     * @return 返回撤销证书的信息，数组内容依次为序列号、组织机构、工商注册号、有效期起、有效期止
     */
    public String[] revoke(String serial_number) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        //        String sql1 = "SELECT COUNT(*) FROM CERTIFICATE WHERE serial_number = ?";
        String sql2 = "SELECT COUNT(*) FROM crl WHERE serial_number = ?";
        Object[] a = {serial_number};
        //        int u1 = template.queryForObject(sql1, a, Integer.class);
        //        if (u1 < 1) {
        //            return null;
        //        }
        int u2 = template.queryForObject(sql2, a, Integer.class);
        if (u2 >= 1) {
            return null;
        }
        String sql3 =
                "SELECT SERIAL_NUMBER, ORGANIZATION, certificate.registration_number, START_TIME, END_TIME FROM CERTIFICATE WHERE "
                        + "SERIAL_NUMBER = ?";
        Mycrl crl = null;
        try {
            crl =
                    template.queryForObject(sql3, new BeanPropertyRowMapper<>(Mycrl.class), serial_number);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
        try {
            if (simpleDateFormat.parse(crl.getEnd_time()).before(new Date())
                    || simpleDateFormat.parse(crl.getStart_time()).after(new Date())) {
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sql4 =
                "INSERT INTO crl(serial_number, organization, registration_number, start_time, "
                        + "end_time) VALUES (?,?,?,?,?)";
        Object[] insertArgs =
                {crl.getSerial_number(), crl.getOrganization(), crl.getRegistration_number(), crl.getStart_time(), crl.getEnd_time()};
        template.update(sql4, insertArgs);
        return crl.show();
    }

    /**
     * 获取CRL数据库中的内容
     *
     * @return CRL数据库列表，每一个元素都是一个Mycrl对象
     */
    public List<Mycrl> getCrl() {
        String sql = "SELECT * FROM CRL";
        List<Mycrl> mycrlList = new ArrayList<>();
        List<Map<String, Object>> rows = template.queryForList(sql);
        for (Map row : rows) {
            Mycrl mycrl = new Mycrl();
            mycrl.setSerial_number((String) row.get("serial_number"));
            mycrl.setOrganization((String) row.get("organization"));
            mycrl.setStart_time((String) row.get("start_time"));
            mycrl.setEnd_time((String) row.get("end_time"));
            mycrlList.add(mycrl);
        }
        return mycrlList;
    }

    /**
     * 判断证书是否已被申请有效，一个组织机构（工商注册号）只能拥有一个有效证书
     *
     * @param organization        组织结构
     * @param registration_number 工商注册号
     * @return
     */
    public boolean isApplied(String organization, String registration_number) {
        String sql1 = "SELECT COUNT(*) FROM certificate WHERE organization = ?";
        String sql2 = "SELECT COUNT(*) FROM certificate WHERE registration_number = ?";
        String sql3 = "SELECT COUNT(*) FROM crl WHERE organization = ?";
        String sql4 = "SELECT COUNT(*) FROM crl WHERE registration_number = ?";
        Object[] arg1 = {organization};
        Object[] arg2 = {registration_number};
        int u1 = template.queryForObject(sql1, arg1, Integer.class);
        int v1 = template.queryForObject(sql3, arg1, Integer.class);
        if (u1 == v1 + 1) {
            return true;
        }
        int u2 = template.queryForObject(sql2, arg2, Integer.class);
        int v2 = template.queryForObject(sql4, arg2, Integer.class);
        if (u2 == v2 + 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取证书库中的所有证书
     *
     * @return 证书列表，每一个元素都是一个CertItem对象
     */
    public List<CertItem> getAllCer() {
        return template.query("SELECT * FROM certificate", new BeanPropertyRowMapper<CertItem>(CertItem.class));
    }

    /**
     * 按页获取证书数据库信息
     *
     * @param page 页状态类
     * @return 证书列表，每一个元素都是一个CertItem对象
     */
    public List<CertItem> getCerByPage(Page page) {
        return template.query(
                "SELECT * FROM certificate LIMIT " + page.getBeginIndex() + ","
                        + page.getEveryPage(), new BeanPropertyRowMapper<CertItem>(CertItem.class));
    }

    /**
     * 获取证书库中的条目数量
     *
     * @return 证书库中的条目数量
     */
    public int getCount() {
        return template.queryForObject("SELECT COUNT(*) FROM certificate", Integer.class);
    }
}
