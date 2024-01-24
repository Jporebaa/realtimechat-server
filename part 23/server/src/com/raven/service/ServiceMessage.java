package com.raven.service;

import com.raven.app.MessageType;
import com.raven.connection.DatabaseConnection;
import com.raven.model.*;
import com.raven.swing.blurHash.BlurHash;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.List;

public class ServiceMessage {

    public ServiceMessage() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }



    public List<Model_Receive_Message> getAllMessages(Model_All_Messages users) throws SQLException {
        PreparedStatement p = con.prepareStatement(GET_MESSAGES);
        p.setInt(1, users.getToUserID());
        p.setInt(2, users.getFromUserID());
        p.setInt(3, users.getFromUserID());
        p.setInt(4, users.getToUserID());
        ResultSet r = p.executeQuery();

        List<Model_Receive_Message> data = new ArrayList<>();
        while (r.next()) {
            int messageType = r.getInt(2);

            int from = r.getInt(3);
            String text = r.getString(5);
            Timestamp date = r.getTimestamp(6);
            data.add(new Model_Receive_Message(messageType, from, text, null, date));
        }
        r.close();
        p.close();

        return data;
    }

    //SQL
    private final String GET_MESSAGES = "select * from messages where (from_user_id=? and to_user_id =?) or (from_user_id=? and to_user_id =?)";

    //  Instance
    private final Connection con;
}