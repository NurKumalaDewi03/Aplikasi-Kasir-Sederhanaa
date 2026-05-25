package UTS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Proyek_UTS extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Membuka layar Menu Utama saat aplikasi pertama kali di-Run
        Parent root = FXMLLoader.load(getClass().getResource("/UTS/Menu_Utama.fxml"));
        
        primaryStage.setTitle("Aplikasi Kasir TS MINIMARKET");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // Perintah wajib untuk menjalankan JavaFX
    }
}