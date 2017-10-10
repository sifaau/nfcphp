package nfread;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory; 
import org.datafx.provider.ObjectDataProvider;
import org.datafx.provider.ObjectDataProviderBuilder;
import org.datafx.reader.RestSource;
import org.datafx.reader.RestSourceBuilder;
/*
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL; */



public class Nfread extends Application {

    private final StringProperty latestId = new SimpleStringProperty(" --- ");

    @Override
    public void start(Stage primaryStage) throws UnsupportedEncodingException {
        doCardReaderCommunication();
        HBox hbox = new HBox(3);
        Label info = new Label("UID nya: ");
        Label latestCheckinLabel = new Label();
        latestCheckinLabel.textProperty().bind(latestId);
        hbox.getChildren().addAll(info, latestCheckinLabel);
        Scene scene = new Scene(hbox, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void doCardReaderCommunication() {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                TerminalFactory terminalFactory = TerminalFactory.getDefault();
                try {
                    List<CardTerminal> cardTerminalList = terminalFactory.terminals().list();
                    //deteksi reader kartu
                    if (cardTerminalList.size() > 0) {
                        System.out.println("1 reader terdeteksi");
                        CardTerminal cardTerminal = cardTerminalList.get(0);
                        //deteksi kartu
                        while(true){
                            if (cardTerminal.waitForCardPresent(0)) {
                                System.out.println("kartu terdeteksi");
                                Card card;
                                //coba baca kartu
                                try {
                                    card = cardTerminal.connect("*");
                                    System.out.println("Card: " + card);
                                    ATR atr = card.getATR();
                                    //deteksi kartu suppot atau tidak
                                    if (card != null) {
                                        System.out.println("kartu support");
                                        //coba baca UID kartu
                                        try {
                                            CardChannel channel = card.getBasicChannel();
                                            CommandAPDU command = new CommandAPDU(getAddress);
                                            ResponseAPDU response = channel.transmit(command);
                                            byte[] uidBytes = response.getData();
                                            String uid = readable(uidBytes);
                                            Platform.runLater(new Runnable() {
                                                public void run() {
                                                    latestId.setValue(uid);
                                                    sendToBackend(uid);
                                                }
                                            });
                                        } catch (CardException e) {
                                            System.out.println("Error saaat baca UID kartu");
                                            e.printStackTrace();
                                        }
                                    } else {
                                        System.out.println("kartu tidak support!");
                                    }
                                } catch (CardException e) {
                                    System.out.println("tidak bisa membaca kartu, coba lagi");
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }else{
                                System.out.println("tidak ada kartu terdeteksi");
                            }
                        }
                        
                        
                        
                        /*while (true) {
                            cardTerminal.waitForCardPresent(0);
                            System.out.println("Inserted card");
                            handleCard(cardTerminal);
                            cardTerminal.waitForCardAbsent(0);
                            System.out.println("Removed card");
                        }*/
                    } else {
                        System.out.println("Ouch, setup tidak bekerja. tidak ada reader terdeteksi");
                    }
                } catch (Exception e) {
                    System.out.println("An exception occured while doing card reader communication.");
                    e.printStackTrace();
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    
    private static String readable(byte[] src) {
        String answer = "";
        for (byte b : src) {
            answer = answer + String.format("%02X", b);
        }
        return answer;
    }

    private void sendToBackend(String uid) {

        RestSource restSource = RestSourceBuilder.create()
                .host("http://127.0.0.1")
                .path("server/inputData.php?uid="+uid)
                .build();
   
        ObjectDataProvider odp = ObjectDataProviderBuilder.create()
                .dataReader(restSource).build();
        odp.retrieve();;
    } 
    static byte[] desfire = new byte[]{0x3b, (byte) 0x81, (byte) 0x80,
        0x01, (byte) 0x80, (byte) 0x80};
    static byte[] getAddress = new byte[] { (byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
	
	//membaca data dari database
	
	//read status
	
	//logikakan status
	//if status mati
	//eksekusi command mati | pemanggilan file mati.py
	//if status hidup
	//eksekusi command hidup | pemanggilan file hidup.py
		
	
}