package pendu.controller;

import java.io.File;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pendu.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by Ibrahim on 21/04/2017.
 */
public class SessionController implements Initializable {

    private LoginModel loginModel = new LoginModel();

    private Session session = new Session();

    private String username;

    @FXML
    private Label scoreActuel;

    @FXML
    private Label meilleurScore;

    @FXML
    private ImageView imageView;

    @FXML
    private Label numMot;

    @FXML
    private Label nbEssaisRestants;

    @FXML
    private Label typeIndication;

    @FXML
    private Label indication;

    @FXML
    private HBox motHbox;

    @FXML
    private Button win;

    @FXML
    private Button gameover;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initialize() {
        try {
            //meilleurScore.setText(session.getJoueurActuel().getMeilleurScore()+"");
            meilleurScore.setText(loginModel.getMeilleurScore(username)+"");
            AllerAuMot(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setImageView(String path) {
        File file = new File(path);
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void abondonner (ActionEvent event) { //Abandonner la session
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Etes-vous sure que vous voulez abondonner la session?");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")) {
                ((Node) event.getSource()).getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Pane root = loader.load(getClass().getResource("../view/Menu.fxml").openStream());
                MenuController menuController = (MenuController) loader.getController();
                menuController.setUsername(username);
                menuController.initialize();
                //primaryStage.initStyle(StageStyle.UTILITY);
                primaryStage.setTitle("Le pendu");
                primaryStage.setScene(new Scene(root, 500, 400));
                primaryStage.setResizable(false);
                primaryStage.show();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void win (ActionEvent event) { //Fin du jeu avec succès
        try {
            //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Etes-vous sure que vous voulez abondonner la session?");
            //alert.showAndWait();
            //if (alert.getResult().getText().equals("OK")) {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("../view/Win.fxml").openStream());
            WinController winController = (WinController) loader.getController();
            winController.setUsername(username);
            winController.setScore(Integer.parseInt(scoreActuel.getText()));
            winController.initialize();
            //primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle("Le pendu");
            primaryStage.setScene(new Scene(root, 500, 400));
            primaryStage.setResizable(false);
            primaryStage.show();
            //}
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void gameover (ActionEvent event) { //Fin du jeu avec échec
        try {
            //Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Etes-vous sure que vous voulez abondonner la session?");
            //alert.showAndWait();
            //if (alert.getResult().getText().equals("OK")) {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("../view/GameOver.fxml").openStream());
            GameOverController gameOverController = (GameOverController) loader.getController();
            gameOverController.setUsername(username);
            gameOverController.initialize();
            //primaryStage.initStyle(StageStyle.UTILITY);
            primaryStage.setTitle("Le pendu");
            primaryStage.setScene(new Scene(root, 500, 400));
            primaryStage.setResizable(false);
            primaryStage.show();
            //}
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void AllerAuMot (int pos) { //Afficher le mot
        nbEssaisRestants.setText(session.getNbEssaisRestants()+"");
        setImageView("src/pendu/view/img/Pendu"+(6-session.getNbEssaisRestants())+".jpg");
        scoreActuel.setText(session.getScoreActuel()+"");
        Mot m = session.getMots().get(pos);
        numMot.setText(session.getNumMotActuel()+"");
        ArrayList<Case> cases = m.getCases();
        int j=0;
        for (Case c : cases) {
            if (c instanceof Proposition)
            {
                ComboBox<String> comboBox = new ComboBox<>();
                String[] propositions = ((Proposition) c).getPropositions();
                for (int i=0;i<propositions.length;i++)
                {
                    comboBox.getItems().add(propositions[i]);
                }
                comboBox.setStyle("-fx-background-color: #FFD54F;");
                comboBox.setId(j+"");

                // l'evenement du choix d'une lettre dans la case
                //region comboBox.setOnAction
                comboBox.setOnAction((ActionEvent event) -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Confirmez l'insertion");
                    alert.showAndWait();
                    if (alert.getResult().getText().equals("OK"))
                    {
                        //if la réponse est juste :
                        if (session.getMots().get(session.getNumMotActuel()-1).getCases().get(Integer.parseInt(comboBox.getId())).entrerLettre(comboBox.getValue().charAt(0)))
                        {
                            comboBox.setDisable(true); // on ne peut plus le changer
                            //alert = new Alert(Alert.AlertType.INFORMATION,"Votre réponse est juste!");
                            //alert.showAndWait();
                            if (allDisabled()) // tout le mot est trouvé
                            {
                                session.getMots().get(session.getNumMotActuel()-1).calculerScore();
                                session.setScoreActuel(session.getScoreActuel()+session.getMots().get(session.getNumMotActuel()-1).getScore());
                                motHbox.getChildren().remove(0,motHbox.getChildren().size());
                                if (session.getNumMotActuel()<10)// passer au mot suivant
                                {
                                    session.setNumMotActuel(session.getNumMotActuel()+1);
                                    AllerAuMot(session.getNumMotActuel()-1);
                                }
                                else // aller à win
                                {
                                    win.fire();
                                }
                            }
                        }
                        else // la réponse est fausse
                        {
                            session.getMots().get(session.getNumMotActuel()-1).calculerScore();
                            session.setScoreActuel(session.getScoreActuel()+session.getMots().get(session.getNumMotActuel()-1).getScore());
                            motHbox.getChildren().remove(0,motHbox.getChildren().size());
                            session.setNbEssaisRestants(session.getNbEssaisRestants()-1);
                            setImageView("src/pendu/view/img/Pendu"+(6-session.getNbEssaisRestants())+".jpg");
                            alert = new Alert(Alert.AlertType.INFORMATION,"Votre réponse est fausse!");
                            alert.showAndWait();
                            if ((session.getNumMotActuel()<10) && (session.getNbEssaisRestants()>0)) // aller au mot suivant
                            {
                                session.setNumMotActuel(session.getNumMotActuel()+1);
                                AllerAuMot(session.getNumMotActuel()-1);
                            }
                            else // fin session
                            {
                                if (session.getNbEssaisRestants()>0) // aller à win
                                {
                                    win.fire();
                                }
                                else // aller à gameover
                                {
                                    gameover.fire();
                                }
                            }
                        }
                    }
                    else
                    {
                        comboBox.setPromptText("");
                    }
                });
                //endregion
                // fin de l'evenement

                motHbox.getChildren().add(comboBox);
            }
            else
            {
                if (c instanceof ZeroChanceCase)
                {
                    JFXTextField jfxTextField = new JFXTextField();
                    jfxTextField.setStyle("-fx-background-color: #FF8A65;");
                    jfxTextField.setId(j+"");

                    // l'evenement d'insertion d'une lettre dans la case zerochance
                    //region jfxTextField.setOnKeyReleased
                    jfxTextField.setOnKeyReleased((event) -> {
                        if (jfxTextField.getLength()>1) {
                            jfxTextField.setText(jfxTextField.getText().substring(0,1));
                        }
                        if (!isAlpha(jfxTextField.getText().charAt(0)))
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Veuillez entrer une lettre");
                            alert.showAndWait();
                            jfxTextField.setText("");
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Confirmez l'insertion");
                            alert.showAndWait();
                            if (alert.getResult().getText().equals("OK")) {
                                //if la réponse est juste :
                                if (session.getMots().get(session.getNumMotActuel() - 1).getCases().get(Integer.parseInt(jfxTextField.getId())).entrerLettre(jfxTextField.getText().charAt(0))) {
                                    jfxTextField.setDisable(true); // on ne peut plus le changer
                                    //alert = new Alert(Alert.AlertType.INFORMATION,"Votre réponse est juste!");
                                    //alert.showAndWait();
                                    if (allDisabled()) // tout le mot est trouvé
                                    {
                                        session.getMots().get(session.getNumMotActuel() - 1).calculerScore();
                                        session.setScoreActuel(session.getScoreActuel() + session.getMots().get(session.getNumMotActuel() - 1).getScore());
                                        motHbox.getChildren().remove(0, motHbox.getChildren().size());
                                        if (session.getNumMotActuel() < 10) // passer au mot suivant
                                        {
                                            session.setNumMotActuel(session.getNumMotActuel() + 1);
                                            AllerAuMot(session.getNumMotActuel() - 1);
                                        } else // aller à win
                                        {
                                            win.fire();
                                        }
                                    }
                                } else // la réponse est fausse
                                {
                                    jfxTextField.setText("");
                                    session.getMots().get(session.getNumMotActuel() - 1).calculerScore();
                                    session.setScoreActuel(session.getScoreActuel() + session.getMots().get(session.getNumMotActuel() - 1).getScore());
                                    motHbox.getChildren().remove(0, motHbox.getChildren().size());
                                    session.setNbEssaisRestants(session.getNbEssaisRestants() - 1);
                                    setImageView("src/pendu/view/img/Pendu" + (6 - session.getNbEssaisRestants()) + ".jpg");
                                    alert = new Alert(Alert.AlertType.INFORMATION, "Votre réponse est fausse!");
                                    alert.showAndWait();
                                    if ((session.getNumMotActuel() < 10) && (session.getNbEssaisRestants() > 0)) // aller au mot suivant
                                    {
                                        session.setNumMotActuel(session.getNumMotActuel() + 1);
                                        AllerAuMot(session.getNumMotActuel() - 1);
                                    } else // fin session
                                    {
                                        if (session.getNbEssaisRestants() > 0) // aller à win
                                        {
                                            win.fire();
                                        } else // aller à gameover
                                        {
                                            gameover.fire();
                                        }
                                    }
                                }
                            } else {
                                jfxTextField.setText("");
                            }
                        }
                    });
                    //endregion
                    // fin de l'evenement

                    motHbox.getChildren().add(jfxTextField);
                }
                else // multichances
                {
                    JFXTextField jfxTextField = new JFXTextField();
                    jfxTextField.setStyle("-fx-background-color: #AED581;");
                    jfxTextField.setId(j+"");

                    // l'evenement d'insertion d'une lettre dans la case multichance
                    //region jfxTextField.setOnKeyReleased
                    jfxTextField.setOnKeyReleased((event) -> {
                        if (jfxTextField.getLength()>1) {
                            jfxTextField.setText(jfxTextField.getText().substring(0,1));
                        }
                        /*/
                        Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Confirmez l'insertion");
                        alert.showAndWait();
                        if (alert.getResult().getText().equals("OK"))
                        {
                        /*/
                        transparent(Integer.parseInt(jfxTextField.getId()));
                        if (!isAlpha(jfxTextField.getText().charAt(0)))
                        {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Veuillez entrer une lettre");
                            alert.showAndWait();
                            jfxTextField.setText("");
                        }
                        else
                            //if la réponse est juste :
                        if (session.getMots().get(session.getNumMotActuel()-1).getCases().get(Integer.parseInt(jfxTextField.getId())).entrerLettre(jfxTextField.getText().charAt(0)))
                        {
                            jfxTextField.setDisable(true); // on ne peut plus le changer
                            nontransparent();
                            //Alert alert = new Alert(Alert.AlertType.INFORMATION,"Votre réponse est juste!");
                            //alert.showAndWait();
                            if (allDisabled()) // tout le mot est trouvé
                            {
                                session.getMots().get(session.getNumMotActuel()-1).calculerScore();
                                session.setScoreActuel(session.getScoreActuel()+session.getMots().get(session.getNumMotActuel()-1).getScore());
                                motHbox.getChildren().remove(0,motHbox.getChildren().size());
                                if (session.getNumMotActuel()<10) // passer au mot suivant
                                {
                                    session.setNumMotActuel(session.getNumMotActuel()+1);
                                    AllerAuMot(session.getNumMotActuel()-1);
                                }
                                else // aller à win
                                {
                                    win.fire();
                                }
                            }
                        }
                        else // la réponse est fausse
                        {
                            jfxTextField.setText("");
                            //session.getMots().get(session.getNumMotActuel()-1).calculerScore();
                            int nbTentatives = session.getMots().get(session.getNumMotActuel()-1).getCases().get(Integer.parseInt(jfxTextField.getId())).getNbtentatives();
                            if (nbTentatives<3) // il reste d'autres chances
                            {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Votre réponse est fausse! Il vous reste : "+(3-nbTentatives)+" tentative(s)");
                                alert.showAndWait();
                            }
                            else // il ne reste pas de chances
                            {
                                session.getMots().get(session.getNumMotActuel()-1).calculerScore();
                                session.setScoreActuel(session.getScoreActuel()+session.getMots().get(session.getNumMotActuel()-1).getScore());
                                motHbox.getChildren().remove(0,motHbox.getChildren().size());
                                session.setNbEssaisRestants(session.getNbEssaisRestants()-1);
                                setImageView("src/pendu/view/img/Pendu"+(6-session.getNbEssaisRestants())+".jpg");
                                //alert = new Alert(Alert.AlertType.INFORMATION,"Votre réponse est fausse!");
                                //alert.showAndWait();
                                if ((session.getNumMotActuel()<10) && (session.getNbEssaisRestants()>0)) // aller au mot suivant
                                {
                                    session.setNumMotActuel(session.getNumMotActuel()+1);
                                    AllerAuMot(session.getNumMotActuel()-1);
                                }
                                else // fin session
                                {
                                    if (session.getNbEssaisRestants()>0) // aller à win
                                    {
                                        win.fire();
                                    }
                                    else // aller à gameover
                                    {
                                        gameover.fire();
                                    }
                                }
                            }
                        }
                            /*/
                        }
                        else
                        {
                            jfxTextField.setText("");
                        }
                        /*/
                    });
                    //endregion
                    // fin de l'evenement

                    motHbox.getChildren().add(jfxTextField);
                }
            }
            j++;
        }
        typeIndication.setText(m.getTypeIndication());
        indication.setText(m.getIndication());
    }

    private boolean allDisabled(){ // retourne vrai si le mot est completement trouver, faux sinon
        for (int i=0;i<motHbox.getChildren().size();i++)
        {
            if (!motHbox.getChildren().get(i).isDisable())
            {
                return false;
            }
        }
        return true;
    }

    public void transparent (int pos) { // mettre toutes les cases invisibles sauf la case de position pos
        for (int i=0;i<motHbox.getChildren().size();i++)
        {
            if (i!=pos)
            {
                motHbox.getChildren().get(i).setMouseTransparent(true);
            }
        }
    }

    public void nontransparent () { // mettre toutes les cases visibles
        for (int i=0;i<motHbox.getChildren().size();i++)
        {
            motHbox.getChildren().get(i).setMouseTransparent(false);
        }
    }

    //-------------------Procédures de manipulation de caractère (ASCII)------------//
    private static final int[] ctype = new int[]{8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 26624, 10240, 10240, 10240, 10240, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 8192, 18432, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, 4096, '萀', '萁', '萂', '萃', '萄', '萅', '萆', '萇', '萈', '萉', 4096, 4096, 4096, 4096, 4096, 4096, 4096, '脊', '脋', '脌', '脍', '脎', '脏', 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 4096, 4096, 4096, 4096, 69632, 4096, '舊', '舋', '舌', '舍', '舎', '舏', 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 4096, 4096, 4096, 4096, 8192};
    static int getType(int var0) { return (var0 & -128) == 0?ctype[var0]:0; }
    static boolean isType(int var0, int var1) { return (getType(var0) & var1) != 0; }
    static boolean isAlpha(int var0) { return isType(var0, 768); }
    //------------------------------------------------------------------------------//
}