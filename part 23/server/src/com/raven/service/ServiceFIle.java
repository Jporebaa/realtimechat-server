package com.raven.service;

import com.raven.app.MessageType;
import com.raven.connection.DatabaseConnection;
import com.raven.model.Model_File;
import com.raven.model.Model_File_Receiver;
import com.raven.model.Model_Package_Sender;
import com.raven.model.Model_Receive_Image;
import com.raven.model.Model_Send_Message;
import com.raven.swing.blurHash.BlurHash;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ServiceFIle {

    public ServiceFIle() {
        this.con = DatabaseConnection.getInstance().getConnection();
        this.fileReceivers = new HashMap<>();
    }

    public Model_File addFileReceiver(String fileExtension) throws SQLException {
        Model_File data;
        PreparedStatement p = con.prepareStatement(INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
        p.setString(1, fileExtension);
        p.execute();
        ResultSet r = p.getGeneratedKeys();
        r.first();
        int fileID = r.getInt(1);
        data = new Model_File(fileID, fileExtension);
        r.close();
        p.close();
        return data;
    }

    public void updateBlurHashDone(int fileID, String blurhash) throws SQLException {
        PreparedStatement p = con.prepareStatement(UPDATE_BLUR_HASH_DONE);
        p.setString(1, blurhash);
        p.setInt(2, fileID);
        p.execute();
        p.close();
    }

    public void updateDone(int fileID) throws SQLException {
        PreparedStatement p = con.prepareStatement(UPDATE_DONE);
        p.setInt(1, fileID);
        p.execute();
        p.close();
    }

    public void initFile(Model_File file, Model_Send_Message message) throws IOException {
        fileReceivers.put(file.getFileID(), new Model_File_Receiver(message, toFileObject(file)));
    }

    public void receiveFile(Model_Package_Sender dataPackage) throws IOException {
        if (!dataPackage.isFinish()) {
            fileReceivers.get(dataPackage.getFileID()).writeFile(dataPackage.getData());
        } else {
            fileReceivers.get(dataPackage.getFileID()).close();
        }
    }

    public Model_Send_Message closeFile(Model_Receive_Image dataImage) throws IOException, SQLException {
        Model_File_Receiver file = fileReceivers.get(dataImage.getFileID());
        if (file.getMessage().getMessageType() == MessageType.IMAGE.getValue()) {
            //  Image file
            //  So create blurhash image string
            file.getMessage().setText("");
            String blurhash = convertFileToBlurHash(file.getFile(), dataImage);
            updateBlurHashDone(dataImage.getFileID(), blurhash);
        } else {
            updateDone(dataImage.getFileID());
        }
        fileReceivers.remove(dataImage.getFileID());
        //  Get message to send to target client when file receive finish
        return file.getMessage();
    }

    private String convertFileToBlurHash(File file, Model_Receive_Image dataImage) throws IOException {
        BufferedImage img = ImageIO.read(file);
        Dimension size = getAutoSize(new Dimension(img.getWidth(), img.getHeight()), new Dimension(200, 200));
        //  Convert image to small size
        BufferedImage newImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(img, 0, 0, size.width, size.height, null);
        String blurhash = BlurHash.encode(newImage);
        dataImage.setWidth(size.width);
        dataImage.setHeight(size.height);
        dataImage.setImage(blurhash);
        return blurhash;
    }

    private Dimension getAutoSize(Dimension fromSize, Dimension toSize) {
        int w = toSize.width;
        int h = toSize.height;
        int iw = fromSize.width;
        int ih = fromSize.height;
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.min(xScale, yScale);
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        return new Dimension(width, height);
    }

    private File toFileObject(Model_File file) {
        return new File(PATH_FILE + file.getFileID() + file.getFileExtension());
    }

    //  SQL
    private final String PATH_FILE = "C:/Users/Michal_Mordarski/IdeaProjects/realtimechat-server/part 23/server/server_data/";
    private final String INSERT = "insert into files (FileExtension) values (?)";
    private final String UPDATE_BLUR_HASH_DONE = "update files set BlurHash=?, `Status`='1' where FileID=? limit 1";
    private final String UPDATE_DONE = "update files set `Status`='1' where FileID=? limit 1";
    //  Instance
    private final Connection con;
    private final Map<Integer, Model_File_Receiver> fileReceivers;
}