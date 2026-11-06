package com.mycompany.loginregistrationunifiedza.provinceDao;

import com.mycompany.loginregistrationunifiedza.DBConnection.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 *
 * @author chuma
 */


public class provinceDao {


    public Map<Integer, String> getAllProvinces() {
        Map<Integer, String> provinces = new LinkedHashMap<>();
        String sql = "SELECT ProvinceID, ProvinceName FROM Province ORDER BY ProvinceName";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                provinces.put(rs.getInt("ProvinceID"), rs.getString("ProvinceName"));
            }

        } catch (SQLException e) {
            System.out.println("Could not load provinces: " + e.getMessage());
        }

        return provinces;
    }
}
