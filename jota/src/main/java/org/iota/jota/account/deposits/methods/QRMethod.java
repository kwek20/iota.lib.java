package org.iota.jota.account.deposits.methods;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.bouncycastle.util.encoders.Base64;
import org.iota.jota.account.AccountState;
import org.iota.jota.account.deposits.DepositConditions;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import net.glxn.qrgen.javase.QRCode;

public class QRMethod implements DepositMethod<QRCode>{

    private ObjectMapper objectMapper;

    public QRMethod() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @Override
    public DepositConditions parse(QRCode method) {
        ByteArrayOutputStream baos = method.stream();
        
        String conditions;
        try {
            conditions = readQRCode(new ByteArrayInputStream( baos.toByteArray()));
        } catch (NotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        DepositConditions depositConditions = loadFromInputStream((Base64.decode(conditions)));
        return depositConditions;
    }
    
    public static String readQRCode(InputStream stream) throws IOException, NotFoundException {
          BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
              new BufferedImageLuminanceSource(
                  ImageIO.read(stream))));
          Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
          return qrCodeResult.getText();
        }

    @Override
    public QRCode build(DepositConditions conditions) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            writeToOutputStream(bo, conditions);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        return QRCode.from(new String(Base64.encode(bo.toByteArray())));
    }
    
    protected DepositConditions loadFromInputStream(byte[] stream){
        DepositConditions conditions;
        try {
            conditions = objectMapper.readValue(stream, new TypeReference<DepositConditions>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return conditions;
    }
    
    protected void writeToOutputStream(OutputStream stream, DepositConditions store) throws IOException {
        objectMapper.writeValue(stream, store);
    }

}